package DiscordBot.discordbot;

import java.util.concurrent.TimeUnit;


//import net.dv8tion.jda.core.entities.Emote;
//import net.dv8tion.jda.core.entities.Guild;
//import net.dv8tion.jda.core.entities.Message;
//import net.dv8tion.jda.core.entities.MessageChannel;
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
 * text phrases the bot will respond to, some specific to only me
 */

public class TextPhrases extends ListenerAdapter{

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public void onMessageReceived(MessageReceivedEvent e) {

		MessageChannel objChannel = e.getChannel();
		Message objMsg = e.getMessage();
		User objUser = e.getAuthor();
		Guild objGuild = e.getGuild();
		int dice = (int)(Math.random()*2);

		//Don't respond to your own messages, or emily, or robin
		//updated, respond to emilys text phrases
		if(!objUser.getId().equals("352599996635545612") && /*!objUser.getName().equals("haiemz") && */!objUser.getId().equals("196409654048325643")) {
			
			
			if(objUser.getName().toLowerCase().equals("rythm")) {
				//objChannel.sendMessage("You wanna fucking go " + objUser.getAsMention() + "?").queue();
			}

			//filter bad words
			
			if(objMsg.getContentRaw().toLowerCase().equals("cuttah")
					|| objMsg.getContentRaw().toLowerCase().equals("cuttuh")
					|| objMsg.getContentRaw().toLowerCase().equals("cutta")) {
				Message msg = e.getTextChannel().sendMessage("http://www.reviversoft.com/blog/wp-content/uploads/2014/08/fix-printer-problems.jpg").complete();
				msg.delete().queueAfter(500, TimeUnit.MILLISECONDS);	
			} else if(objMsg.getContentRaw().toLowerCase().equals("uh")
					|| objMsg.getContentRaw().toLowerCase().equals("uhh")
					|| objMsg.getContentRaw().toLowerCase().equals("uhhh")
					|| objMsg.getContentRaw().toLowerCase().equals("uhhhh")) {
				//objChannel.sendMessage("Curtis").queue();
			} else if(objMsg.getContentRaw().toLowerCase().equals("fix")) {
				Message msg = e.getTextChannel().sendMessage("https://cdn.discordapp.com/attachments/193793211989229578/353248225194409984/fix.jpg").complete();
				msg.delete().queueAfter(500, TimeUnit.MILLISECONDS);
			} else if(objMsg.getContentRaw().toLowerCase().equals("frank")) {
				Message msg = e.getTextChannel().sendMessage("https://cdn.discordapp.com/attachments/193793211989229578/353248249466847232/dankfrank.png").complete();
				msg.delete().queueAfter(500, TimeUnit.MILLISECONDS);
			}  else if(objMsg.getContentRaw().toLowerCase().contains("lenny") 
					|| objMsg.getContentRaw().toLowerCase().contains("lenny face") 
					|| objMsg.getContentRaw().toLowerCase().contains("insert lenny")
					|| objMsg.getContentRaw().toLowerCase().contains("insert lenny face")) {
				objChannel.sendMessage("( ͡° ͜ʖ ͡°)").queue();
			} else if(objMsg.getContentRaw().toLowerCase().contains("shrug")
					|| objMsg.getContentRaw().toLowerCase().contains("shrug lenny")
					|| objMsg.getContentRaw().toLowerCase().contains("insert shrug lenny")
					|| objMsg.getContentRaw().toLowerCase().contains("dunno")) {
				objChannel.sendMessage("¯\\_(ツ)_/¯").queue();
			} else if(objMsg.getContentRaw().toLowerCase().contains("take my energy")
					|| objMsg.getContentRaw().toLowerCase().equals("energy")) {
				objChannel.sendMessage("༼ つ ◕\\_◕ ༽つ TAKE MY ENERGY ༼ つ ◕\\_◕ ༽つ").queue();
			} else if(objMsg.getContentRaw().toLowerCase().equals("rem")) {
				objChannel.sendMessage("Mhm?").queue();
			} else if(objMsg.getContentRaw().toLowerCase().contains("whos rem")
					|| objMsg.getContentRaw().toLowerCase().contains("who's rem")) {
				objChannel.sendMessage("Fuck you " + objUser.getAsMention()).queue();
			} else if(objMsg.getContentRaw().toLowerCase().contains("emilia")) {
				Message msg = objChannel.sendMessage("https://pbs.twimg.com/media/C46h_CTWQAEQAEY.jpg").complete();
				Message msg2 = objChannel.sendMessage("lmao").complete();
				msg.delete().queueAfter(5, TimeUnit.SECONDS);
				msg2.delete().queueAfter(5, TimeUnit.SECONDS);
			} else if(objMsg.getContentRaw().toLowerCase().equals("fellas")){
					objMsg.addReaction(objGuild.getEmotesByName("fatThink", true).get(0)).queue();
					objMsg.addReaction("\uD83C\uDFF3\uFE0F\u200D\uD83C\uDF08").queue();
			}

			//Commands/text phrases only I can use
			// removed because cringe
//			if(objUser.getName().equals("Goldace")) {
//				if(objMsg.getContentRaw().toLowerCase().equals("isnt that right rem") 
//						|| objMsg.getContentRaw().toLowerCase().equals("isn't that right rem")) {
//					if(dice == 0) {
//						objChannel.sendMessage("Indeed Goldace-kun").queue();
//					} else {
//						objChannel.sendMessage("That is correct Goldace-kun").queue();
//					}
//				} else if(objMsg.getContentRaw().toLowerCase().equals("can you believe this guy rem")){
//					objChannel.sendMessage("Simply ridiculous").queue();
//				}
//			}
		} else if(objUser.getName().equals("haiemz")) {
			//objMsg.delete().queue();
		}
	}
}
