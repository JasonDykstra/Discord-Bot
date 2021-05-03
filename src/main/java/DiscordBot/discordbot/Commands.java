package DiscordBot.discordbot;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

//import net.dv8tion.jda.core.entities.Guild;
//import net.dv8tion.jda.core.entities.Member;
//import net.dv8tion.jda.core.entities.Message;
//import net.dv8tion.jda.core.entities.MessageChannel;
//import net.dv8tion.jda.core.entities.MessageHistory;
//import net.dv8tion.jda.core.entities.TextChannel;
//import net.dv8tion.jda.core.entities.User;
//import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
//import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
//import net.dv8tion.jda.core.hooks.ListenerAdapter;

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

/*
 * all the commands for the bot go in here, thats all the responses to anything starting with "/"
 */

public class Commands extends ListenerAdapter {

	public char commandPrefix = '/';
	private List<User> muted = new ArrayList<User>();
	private boolean safeMode = false;


	//when a message is sent in chat, check to see if its a command
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

		//gets properties of received message
		Message objMsg = e.getMessage();
		Guild objGuild = e.getGuild();
		TextChannel objChannel = e.getChannel();
		User objUser = e.getAuthor();

		//make sure the user isnt muted, if they are, delete their message immediately
		for(User u : muted) {
			if(u.equals(objUser)) {
				objMsg.delete().queue();
			}
		}
		
		System.out.println("content raw: " + objMsg.getContentRaw());

		//detect commands
		if(objMsg.getContentRaw().charAt(0) == commandPrefix) {
			String[] strArgs = objMsg.getContentRaw().substring(1).split(" ");
			
			System.out.println("command: " + strArgs[0]);

			//Don't let robin use commands
			//also dont let muted people use commands
			if(objUser.getId().equals("196409654048325643") || muted.contains(objUser)) {
				objChannel.sendMessage("stfu " + objUser.getAsMention()).queue();
			} else {


				//commands
				if(strArgs[0].equals("hello")) {
					objChannel.sendMessage("Hello, " + objUser.getAsMention() + "!").queue();
				} else if(strArgs[0].equals("roll")) {
					int intSides = 6;
					if(strArgs.length > 1) {
						intSides = Integer.valueOf(strArgs[1].replaceAll(",", ""));
					}

					if(intSides >= 1) {
						objChannel.sendMessage(objUser.getAsMention() + " rolls " + (int)(Math.random() * intSides + 1)).queue();
					} else {
						objChannel.sendMessage(objUser.getAsMention() + " please enter a valid number.").queue();
					}
				} else if(strArgs[0].toLowerCase().equals("ping")) {
					objChannel.sendMessage("Pong!").queue();
				} else if(strArgs[0].toLowerCase().equals("bestgirl")) {
					objChannel.sendMessage("__**#TeamRem**__").queue();
				} else if(strArgs[0].toLowerCase().equals("madeby")) {
					objChannel.sendMessage("Jason Dykstra... I made this!").queue();
				} else if(strArgs[0].toLowerCase().equals("profilepic")) {
					if(strArgs.length == 1) {
						objChannel.sendMessage(objUser.getAvatarUrl()).queue();
					} else if(strArgs.length == 2) {
						objChannel.sendMessage(searchMembers(strArgs[1], objGuild).get(0).getUser().getAvatarUrl()).queue();
					}
				} else if(strArgs[0].toLowerCase().equals("clearbotandcommands")) {
					MessageHistory history = new MessageHistory(objChannel);
					int limit = 100;

					if(strArgs.length == 2) {
						limit = Integer.parseInt(strArgs[1]) + 1;
					}

					//damn am I proud of this one liner...
					history.retrievePast(limit).queue(messages -> {
						List<Message> filtered = messages.stream().filter(message -> 
						("352599996635545612".equals(message.getAuthor().getId()) || message.getContentRaw().startsWith("/"))
						&& message.getTimeCreated().isAfter(OffsetDateTime.now().minusWeeks(2))).collect(Collectors.toList());
						objChannel.deleteMessages(filtered).queue();
					});



					objChannel.sendMessage(
							":white_check_mark: Last " + Integer.toString(limit - 1) + " messages that were commands or bot messages cleared! :white_check_mark:"
							).queue(m->m.delete().queueAfter(5, TimeUnit.SECONDS));



				} else if(strArgs[0].toLowerCase().equals("clearchat")) {
					MessageHistory history = new MessageHistory(objChannel);
					int limit = 100;

					if(strArgs.length == 2) {
						limit = Integer.parseInt(strArgs[1]) + 1;
					}

					history.retrievePast(limit).queue(messages -> {
						messages.stream().filter(message -> message.getTimeCreated().isAfter(OffsetDateTime.now().minusWeeks(2))).collect(Collectors.toList());
						objChannel.deleteMessages(messages.stream().filter(message -> message.getTimeCreated().isAfter(OffsetDateTime.now().minusWeeks(2))).collect(Collectors.toList())).queue();
					});



					objChannel.sendMessage(
							":white_check_mark: Last " + Integer.toString(limit - 1) + " messages cleared! :white_check_mark:"
							).queue(m->m.delete().queueAfter(5, TimeUnit.SECONDS));


					//OwO weeb commands
					//Find links
				} else if(strArgs[0].toLowerCase().equals("muted")) {
					String s = "Muted Members: ";
					if(muted.size() > 0) {
						for(User u : muted) {
							s += u.getName() + " ";
						}
					} else {
						s += "none";
					}
					objChannel.sendMessage(s).queue();
				} else if(strArgs[0].toLowerCase().equals("safemode")) {
					if(strArgs.length == 1) {
						safeMode = !safeMode;
						if(safeMode == true)
							objChannel.sendMessage("Safe mode is now toggled ON").queue();
						else 
							objChannel.sendMessage("Safe mode is now toggled OFF").queue();
					} else if(strArgs.length == 2) {
						if(strArgs[1].toLowerCase().equals("on")) {
							safeMode = true;
							objChannel.sendMessage("Safe mode is now toggled ON").queue();
						} else if(strArgs[1].toLowerCase().equals("off")) {
							safeMode = false;
							objChannel.sendMessage("Safe mode is now toggled OFF").queue();
						}
					}
				}
			}

			//commands specific to me hehheheehh
			if(objUser.getName().equals("Goldace")) {
				if(strArgs[0].toLowerCase().equals("mute")){
					boolean alreadyMuted = false;

					for(User u : muted) {
						if(u.equals(searchMembers(strArgs[1], objGuild).get(0).getUser())) {
							alreadyMuted = true;
						}
					}
					if(alreadyMuted == false) {
						muted.add(searchMembers(strArgs[1], objGuild).get(0).getUser());
						objChannel.sendMessage(searchMembers(strArgs[1], objGuild).get(0).getUser().getName() + " has been muted").queue();
					} else {
						objChannel.sendMessage("This member is already muted").queue();
					}
				} else if(strArgs[0].toLowerCase().equals("unmute")) {
					for(int i = 0; i < muted.size(); i++) {
						if(muted.get(i).equals(searchMembers(strArgs[1], objGuild).get(0).getUser())) {
							muted.remove(i);
							objChannel.sendMessage(searchMembers(strArgs[1], objGuild).get(0).getUser() + " has been unmuted").queue();
						}
					}
				}
			}
		}
	}

	
	//my custom search members command
	public List<Member> searchMembers(String search, Guild g){
		ArrayList<Member> exactNames = new ArrayList<>();
		ArrayList<Member> startsWithNames = new ArrayList<>();
		ArrayList<Member> containsNames = new ArrayList<>();

		g.getMembers().forEach(m -> {
			if(m.getUser().getName().equals(search) || m.getEffectiveName().equals(search)) {
				exactNames.add(m);
			} else if(m.getUser().getName().toLowerCase().startsWith(search) || m.getEffectiveName().toLowerCase().startsWith(search)) {
				startsWithNames.add(m);
			} else if(m.getUser().getName().toLowerCase().contains(search) || m.getEffectiveName().toLowerCase().contains(search)) {
				containsNames.add(m);
			}
		});
		if(!exactNames.isEmpty()) {
			return exactNames;
		} else if(!startsWithNames.isEmpty()) {
			return startsWithNames;
		} else {
			return containsNames;
		}
	}
}
