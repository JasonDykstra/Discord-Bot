package DiscordBot.discordbot;

import java.awt.Color;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//import net.dv8tion.jda.core.EmbedBuilder;
//import net.dv8tion.jda.core.entities.Guild;
//import net.dv8tion.jda.core.entities.Member;
//import net.dv8tion.jda.core.entities.Message;
//import net.dv8tion.jda.core.entities.MessageChannel;
//import net.dv8tion.jda.core.entities.MessageEmbed;
//import net.dv8tion.jda.core.entities.User;
//import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
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
 * class that gets user info of someone
 */

public class UserInfo extends ListenerAdapter{


	public MessageEmbed userInfo(User user) {
		String months[] = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
		String[] userCreated = user.getTimeCreated().toString().split("-");
		String finalString = months[Integer.valueOf(userCreated[1]) - 1] + " " + userCreated[2].substring(0,2) + ", " + userCreated[0];

		System.out.println(user.getTimeCreated().toString());

		//create new embed that sends user info
		MessageEmbed embed = new EmbedBuilder()
				.setColor(new Color(0, 175, 255))
				.setThumbnail(user.getAvatarUrl())
				.setTitle(user.getName())
				.setDescription("Id: " + user.getId())
				.addField("Date Created", finalString, true)
				.build();

		return embed;
	}


	@Override
	public void onMessageReceived(MessageReceivedEvent e) {
		Message objMsg = e.getMessage();
		User objUser = e.getAuthor();
		Guild objGuild = e.getGuild();
		MessageChannel objChannel = e.getChannel();

		if(objMsg.getContentRaw().charAt(0) == '/') {
			String[] strArgs = objMsg.getContentRaw().split(" ", 2);
			if(strArgs[0].equals("/userinfo") && strArgs.length == 1) {
				objChannel.sendMessage(userInfo(objUser)).queue();
			} else if(strArgs[0].equals("/userinfo")){
				if(!searchMembers(strArgs[1], objGuild).isEmpty()) {
					objChannel.sendMessage(userInfo(searchMembers(strArgs[1], objGuild).get(0).getUser())).queue();
				} else {
					objChannel.sendMessage("No users found with that name or nickname").queue();
				}
			}
		}
	}

	//Priority list for which user to return when searching; exact name > name starts with > name contains
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
