package DiscordBot.discordbot;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
//import net.dv8tion.jda.core.entities.Guild;
//import net.dv8tion.jda.core.entities.MessageChannel;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.LinkedList;


/**
 * Don't worry about this class, auto generated.
 * 
 * 
 * 
 * 
 * This class schedules tracks for the audio player. It contains the queue of tracks.
 */
public class TrackScheduler extends AudioEventAdapter {
	//App app = new App();
	private final AudioPlayer player;
	private final LinkedList<AudioTrack> queue;
	private Guild guild;
	private MessageChannel lastChannel;

	/**
	 * @param player The audio player this scheduler uses
	 */
	public TrackScheduler(AudioPlayer player) {
		this.player = player;
		this.queue = new LinkedList<>();
	}

	/**
	 * Add the next track to queue or play right away if nothing is in the queue.
	 *
	 * @param track The track to play or add to queue.
	 */
	public void queue(AudioTrack track) {
		// Calling startTrack with the noInterrupt set to true will start the track only if nothing is currently playing. If
		// something is playing, it returns false and does nothing. In that case the player was already playing so this
		// track goes to the queue instead.
		if (!player.startTrack(track, true)) {
			queue.offer(track);
		}
	}

	/**
	 * Start the next track, stopping the current one if it is playing.
	 */
	public void nextTrack() {
		// Start the next track, regardless of if something is already playing or not. In case queue was empty, we are
		// giving null to startTrack, which is a valid argument and will simply stop the player.
		if(!queue.isEmpty()) {
			App.app.lastMessageChannel.sendMessage("Now playing: " + queue.get(0).getInfo().title).queue();
		} else {
			//leave voice channel if nothing is playing... maybe add timer?
			//something wrong here? Bot leaving every time song is skipped and there is 1 in queue hm
			
			guild.getAudioManager().closeAudioConnection();
		}

		player.startTrack(queue.poll(), false);
	}

	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		// Only start the next track if the end reason is suitable for it (FINISHED or LOAD_FAILED)
		if (endReason.mayStartNext) {
			nextTrack();
		} 
	}

	public LinkedList<AudioTrack> getQueue(){
		return queue;
	}

	public void removeFromQueue(int x) {
		for(int i = 0; i < x; i++) {
			queue.poll();
		}
	}

	public void clearQueue() {
		queue.clear();
	}
	
	public void sendGuild(Guild g) {
		guild = g;
	}
	
	public void sendMessageChannel(MessageChannel c) {
		lastChannel = c;
	}
}
