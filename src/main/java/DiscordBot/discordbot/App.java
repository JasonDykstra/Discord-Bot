package DiscordBot.discordbot;

import java.awt.Color;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.security.auth.login.LoginException;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.requests.GatewayIntent;

/*
 * main class for the bot, music stuff has to go here due to lavaplayer libraries.
 * lots of complicated things fun :D
 */

public class App extends ListenerAdapter {
	public static void main( String[] args ) throws LoginException, IllegalArgumentException, InterruptedException{

		//initialize all the event listeners aka all the other classes
		//jdaBot is the key to my bot that is provided by discord, if someone else uses it they can control my bot,
		//i removed the key so you dont give it away or accidentally run the program and break the bot
		//its not that i dont trust u but its important to me sorry :(

		// Have to enable all intents as this wonky enum set so that the bot can have permission to do things
		// such as getting members, listing message history, etc.
		JDABuilder.createDefault("MzUyNTk5OTk2NjM1NTQ1NjEy.WadOVg.eS1l46uCdU20r1BHHMYtierdRLc", EnumSet.allOf(GatewayIntent.class))
		.addEventListeners(new App())
		.addEventListeners(new Commands())
		.addEventListeners(new HelpCommand())
		.addEventListeners(new TextPhrases())
		.addEventListeners(new UserInfo())
		.addEventListeners(new TicTacToe())
		.addEventListeners(new PatchNotes())
		.addEventListeners(new Connect4())
		.build();
	}

	public static App app = new App();
	private final AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
	private final Map<Long, GuildMusicManager> musicManagers = new HashMap<Long, GuildMusicManager>();
	public MessageChannel lastMessageChannel;

	public App() {
		AudioSourceManagers.registerRemoteSources(playerManager);
		AudioSourceManagers.registerLocalSource(playerManager);
	}

	private synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
		long guildId = Long.parseLong(guild.getId());
		GuildMusicManager musicManager = musicManagers.get(guildId);

		if (musicManager == null) {
			musicManager = new GuildMusicManager(playerManager);
			musicManagers.put(guildId, musicManager);
		}
		//guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

		return musicManager;
	}

	//Pause music and leave the voice channel if nobody else is in it
	@Override
	public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
		if(event.getChannelLeft().equals(event.getGuild().getAudioManager().getConnectedChannel())) {
			if(event.getChannelLeft().getMembers().size() == 1) {
				pause(event.getGuild(), lastMessageChannel);
				event.getGuild().getAudioManager().closeAudioConnection();
			}
		}
	}



	//Commands for music
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		String[] command = event.getMessage().getContentRaw().split(" ", 2);
		if(command[0].equals("/play")){
			lastMessageChannel = event.getChannel();
			app.lastMessageChannel = event.getChannel();
			getGuildAudioPlayer(event.getGuild()).scheduler.sendGuild(event.getGuild());
			getGuildAudioPlayer(event.getGuild()).scheduler.sendMessageChannel(event.getTextChannel());
		}
		//initialize variables
		Guild guild = event.getGuild();
		User user = event.getAuthor();
		MessageChannel channel = event.getChannel();
		Boolean inVoiceChannel = false;

		//Check to see if the message sender is in a voice channel or not
		for(VoiceChannel voiceChannel : guild.getVoiceChannels()) {
			for(Member m : voiceChannel.getMembers()) {
				if(m.getUser().equals(user)) {
					inVoiceChannel = true;
					break;
				}
			}
		}

		//dont let robin use music commands
		if (guild != null && !user.getId().equals("196409654048325643")) {

			//You must be in voice channel to use these commands
			if(inVoiceChannel == true) {
				if ("/play".equals(command[0].toLowerCase()) && command.length == 2) {
					if(command[1].startsWith("http")) {
						loadAndPlay(event.getTextChannel(), command[1], event.getAuthor());
					} else {
						String s = "ytsearch:search ";
						s += command[1];
						loadAndPlay(event.getTextChannel(), s, event.getAuthor());
					}

					if(getGuildAudioPlayer(guild).player.isPaused() == true) {
						//resume(guild, channel);
					}


				} else if ("/skip".equals(command[0].toLowerCase())) {
					channel.sendMessage(":notes: Skipped song :notes:").queue();
					skipTrack(event.getTextChannel());

				
				} else if("/join".equals(command[0].toLowerCase())) {
					
					if(getGuildAudioPlayer(guild).player.isPaused() == true) {
						resume(guild, channel);
					}
					join(guild.getAudioManager(), user);


				} else if("/stop".equals(command[0].toLowerCase()) || "/leave".equals(command[0].toLowerCase())){
					if(getGuildAudioPlayer(guild).player.isPaused() == true) {
						guild.getAudioManager().closeAudioConnection();
					} else {
						pause(guild, channel);
						guild.getAudioManager().closeAudioConnection();
					}


				} else if("/pause".equals(command[0].toLowerCase())) {
					if(getGuildAudioPlayer(guild).player.isPaused() == true) {
						channel.sendMessage(":notes: Music is already paused! :notes:").queue();
					} else {
						getGuildAudioPlayer(guild).player.setPaused(true);
						channel.sendMessage(":pause_button: Music has been paused").queue();
					}


				} else if("/resume".equals(command[0].toLowerCase()) || "/play".equals(command[0].toLowerCase())) {
					if(getGuildAudioPlayer(guild).player.isPaused() == false) {
						channel.sendMessage(":notes: Music is not paused! :notes:").queue();
					} else if(guild.getAudioManager().isConnected()){
						resume(guild, channel);
					} else if(getGuildAudioPlayer(guild).player.getPlayingTrack() != null){
						join(guild.getAudioManager(), user);
						resume(guild, channel);
					} else {
						channel.sendMessage(":notes: Nothing is currently playing :notes:").queue();
					}


				} else if("/skipto".equals(command[0].toLowerCase())) {
					if(command.length == 1) {
						channel.sendMessage(":notes: Please specify which song in queue to skip to :notes:").queue();
					} else {
						if(Integer.parseInt(command[1]) <= getGuildAudioPlayer(guild).scheduler.getQueue().size()) {
							getGuildAudioPlayer(guild).scheduler.removeFromQueue(Integer.parseInt(command[1]) - 1);
							skipTrack(event.getTextChannel());
							channel.sendMessage(
									":notes: Skipped to: " + getGuildAudioPlayer(guild).player.getPlayingTrack().getInfo().title + " :notes:"
									).queue();
						} else {
							channel.sendMessage(":notes: Please enter a valid position to skip to :notes:").queue();
						}
					}
					
				} else if("/clearqueue".equals(command[0].toLowerCase())) {
					if(!getGuildAudioPlayer(guild).scheduler.getQueue().isEmpty()) {
						getGuildAudioPlayer(guild).scheduler.clearQueue();
						channel.sendMessage(":notes: Queue has been cleared! :notes:").queue();
					} else {
						channel.sendMessage(":notes: Nothing currently in the queue! :notes:").queue();
					}
				}
			} else {


				//If you use the must-be-in-voice-channel commands while not in a voice channel, you will be told to join a voice channel
				if("/play".equals(command[0].toLowerCase())
						|| "/skip".equals(command[0].toLowerCase())
						|| "/join".equals(command[0].toLowerCase())
						|| "/stop".equals(command[0].toLowerCase())
						|| "/leave".equals(command[0].toLowerCase())
						|| "/pause".equals(command[0].toLowerCase())
						|| "/resume".equals(command[0].toLowerCase())
						|| "/skipto".equals(command[0].toLowerCase())
						|| "/clearqueue".equals(command[0].toLowerCase())) {
					channel.sendMessage(":x: Please join a voice channel to use this command " + user.getAsMention() + " :x:").queue();
				}
			}

			//You don't have to be in a voice channel to use these commands
			if("/queue".equals(command[0].toLowerCase())
					|| "/q".equals(command[0].toLowerCase())){
				String queue = "";
				List<String> durations = new ArrayList<String>();

				for(int i = 0; i < getQueue(guild).size(); i++) {
					long length = getQueue(guild).get(i).getDuration();

					durations.add(formatTime(length));
				}

				for(int i = 0; i < getQueue(guild).size(); i++) {
					queue += Integer.toString(i + 1) + ": " 
							+ getQueue(guild).get(i).getInfo().title + " | " 
							+ durations.get(i)
							+ "\n";
				}

				MessageEmbed embed = new EmbedBuilder()
						.setColor(new Color(0, 175, 255))
						.setTitle(":notes: Song Queue :notes:")
						.addField("__Next songs:__", queue, true)
						.build();

				if(queue.isEmpty()) {
					channel.sendMessage(":notes: Queue is empty! :notes:").queue();
				} else {
					channel.sendMessage(embed).queue();
				}


			//send song info message including: title, length, channel and yt url
			} else if("/songinfo".equals(command[0].toLowerCase())) {
				if(!(getGuildAudioPlayer(guild).player.getPlayingTrack() == null)) {
					MessageEmbed embed = new EmbedBuilder()
							.setColor(new Color(0, 175, 255))
							.setTitle(":notes: Song Information For: :notes:")
							.setDescription(getGuildAudioPlayer(guild).player.getPlayingTrack().getInfo().title)
							.addField("Length:", formatTime(getGuildAudioPlayer(guild).player.getPlayingTrack().getDuration()), true)
							.addField("Channel:", getGuildAudioPlayer(guild).player.getPlayingTrack().getInfo().author, true)
							.addField("Youtube URL:", getGuildAudioPlayer(guild).player.getPlayingTrack().getInfo().uri, false)
							.build();
					channel.sendMessage(embed).queue();
				} else {
					channel.sendMessage(":notes: Nothing currently playing :notes:").queue();
				}
			}
		}

		super.onMessageReceived(event);
	}

	//the main function for grabbing a song from youtube, loading it and playing it over voice chat
	private void loadAndPlay(final TextChannel channel, final String trackUrl, final User user) {
		final GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());

		playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
			// @Override
			public void trackLoaded(AudioTrack track) {
				channel.sendMessage("Adding to queue " + track.getInfo().title).queue();

				play(channel.getGuild(), musicManager, track, user);
			}

			// @Override
			public void playlistLoaded(AudioPlaylist playlist) {
				AudioTrack firstTrack = playlist.getSelectedTrack();

				if (firstTrack == null) {
					firstTrack = playlist.getTracks().get(0);
				}

				channel.sendMessage(firstTrack.getInfo().title  + " added to queue!").queue();

				play(channel.getGuild(), musicManager, firstTrack, user);
			}

			// @Override
			public void noMatches() {
				channel.sendMessage("Nothing found by " + trackUrl).queue();
			}

			// @Override
			public void loadFailed(FriendlyException exception) {
				channel.sendMessage("Could not play: " + exception.getMessage()).queue();
			}
		});
	}

	//joins voice channel and plays the next song in queue or unpauses a song if theres already one playing
	private void play(Guild guild, GuildMusicManager musicManager, AudioTrack track, User user) {
		join(guild.getAudioManager(), user);
		musicManager.scheduler.queue(track);
	}

	private void skipTrack(TextChannel channel) {
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
		musicManager.scheduler.nextTrack();


	}

	//connects to first voice channel in a server
	//Who would use this tbh, came with demo
	// private void connectToFirstVoiceChannel(AudioManager audioManager) {
	// 	if (!audioManager.isConnected() && !audioManager.isAttemptingToConnect()) {
	// 		for (VoiceChannel voiceChannel : audioManager.getGuild().getVoiceChannels()) {
	// 			audioManager.openAudioConnection(voiceChannel);
	// 			break;
	// 		}
	// 	}
	// }

	//Connects to the voice channel that the user who requested the command is in
	private void join(AudioManager audioManager, User user) {
		//not sure if i need this or not
		//if(!audioManager.isConnected() && !audioManager.isAttemptingToConnect()) {
		for(VoiceChannel voiceChannel : audioManager.getGuild().getVoiceChannels()) {
			for(Member m : voiceChannel.getMembers()) {
				if(m.getUser().equals(user)) {
					audioManager.openAudioConnection(voiceChannel);
					break;
				}
			}
		}

		//}
	}

	private void resume(Guild g, MessageChannel c) {
		getGuildAudioPlayer(g).player.setPaused(false);
		c.sendMessage(":arrow_forward: Music has been resumed").queue();
	}

	private void pause(Guild g, MessageChannel c) {
		getGuildAudioPlayer(g).player.setPaused(true);
		c.sendMessage(":pause_button: Music has been paused").queue();
	}

	private LinkedList<AudioTrack> getQueue(Guild g) {
		return getGuildAudioPlayer(g).scheduler.getQueue();
	}

	//format the time so that it actually looks decent when you send it in chat
	private String formatTime(long length) {
		String hms = "";
		if(length < 3600000) {
			hms = String.format("%02d:%02d",
					TimeUnit.MILLISECONDS.toMinutes(length) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(length)),
					TimeUnit.MILLISECONDS.toSeconds(length) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(length)));
		} else {
			hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(length),
					TimeUnit.MILLISECONDS.toMinutes(length) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(length)),
					TimeUnit.MILLISECONDS.toSeconds(length) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(length)));
		}

		return hms;
	}

	//return last message channel that a message in a guild was sent in
	public MessageChannel getLastMessageChannel() {
		return this.lastMessageChannel;
	}
}
