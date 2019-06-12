package DiscordBot.discordbot;

import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

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
			if(objMsg.getContent().replaceAll("\\s+","").toLowerCase().indexOf("nigger") != -1) {
				objMsg.delete().queue();
				objChannel.sendMessage("Watch your language " + objUser.getAsMention()).queue();
			} else if(objMsg.getContent().toLowerCase().equals("cuttah")
					|| objMsg.getContent().toLowerCase().equals("cuttuh")
					|| objMsg.getContent().toLowerCase().equals("cutta")) {
				Message msg = e.getTextChannel().sendMessage("http://www.reviversoft.com/blog/wp-content/uploads/2014/08/fix-printer-problems.jpg").complete();
				msg.delete().queueAfter(500, TimeUnit.MILLISECONDS);	
			} else if(objMsg.getContent().toLowerCase().equals("uh")
					|| objMsg.getContent().toLowerCase().equals("uhh")
					|| objMsg.getContent().toLowerCase().equals("uhhh")
					|| objMsg.getContent().toLowerCase().equals("uhhhh")) {
				//objChannel.sendMessage("Curtis").queue();
			} else if(objMsg.getContent().toLowerCase().equals("noot noot")) {
				Message msg = e.getTextChannel().sendMessage("https://cdn.discordapp.com/attachments/327295095017701386/345399475428261890/noot.gif").complete();
				msg.delete().queueAfter(5, TimeUnit.SECONDS);
			} else if(objMsg.getContent().toLowerCase().equals("fix")) {
				Message msg = e.getTextChannel().sendMessage("https://cdn.discordapp.com/attachments/193793211989229578/353248225194409984/fix.jpg").complete();
				msg.delete().queueAfter(500, TimeUnit.MILLISECONDS);
			} else if(objMsg.getContent().toLowerCase().equals("frank")) {
				Message msg = e.getTextChannel().sendMessage("https://cdn.discordapp.com/attachments/193793211989229578/353248249466847232/dankfrank.png").complete();
				msg.delete().queueAfter(500, TimeUnit.MILLISECONDS);
			}  else if(objMsg.getContent().toLowerCase().contains("lenny") 
					|| objMsg.getContent().toLowerCase().contains("lenny face") 
					|| objMsg.getContent().toLowerCase().contains("insert lenny")
					|| objMsg.getContent().toLowerCase().contains("insert lenny face")) {
				objChannel.sendMessage("( ͡° ͜ʖ ͡°)").queue();
			} else if(objMsg.getContent().toLowerCase().contains("shrug")
					|| objMsg.getContent().toLowerCase().contains("shrug lenny")
					|| objMsg.getContent().toLowerCase().contains("insert shrug lenny")
					|| objMsg.getContent().toLowerCase().contains("dunno")) {
				objChannel.sendMessage("¯\\_(ツ)_/¯").queue();
			} else if(objMsg.getContent().toLowerCase().contains("take my energy")
					|| objMsg.getContent().toLowerCase().equals("energy")) {
				objChannel.sendMessage("༼ つ ◕\\_◕ ༽つ TAKE MY ENERGY ༼ つ ◕\\_◕ ༽つ").queue();
			} else if(objMsg.getContent().toLowerCase().equals("rem")) {
				objChannel.sendMessage("Mhm?").queue();
			} else if(objMsg.getContent().toLowerCase().contains("whos rem")
					|| objMsg.getContent().toLowerCase().contains("who's rem")) {
				objChannel.sendMessage("Fuck you " + objUser.getAsMention()).queue();
			} else if(objMsg.getContent().toLowerCase().contains("emilia")) {
				Message msg = objChannel.sendMessage("https://pbs.twimg.com/media/C46h_CTWQAEQAEY.jpg").complete();
				Message msg2 = objChannel.sendMessage("lmao").complete();
				msg.delete().queueAfter(5, TimeUnit.SECONDS);
				msg2.delete().queueAfter(5, TimeUnit.SECONDS);
			} else if(objMsg.getContent().toLowerCase().equals("fellas")){
					objMsg.addReaction(objGuild.getEmotesByName("fatThink", true).get(0)).queue();
					objMsg.addReaction("\uD83C\uDFF3\uFE0F\u200D\uD83C\uDF08").queue();
			}

			//Commands/text phrases only I can use
			if(objUser.getName().equals("Goldace")) {
				if(objMsg.getContent().toLowerCase().equals("isnt that right rem") 
						|| objMsg.getContent().toLowerCase().equals("isn't that right rem")) {
					if(dice == 0) {
						objChannel.sendMessage("Indeed Goldace-kun").queue();
					} else {
						objChannel.sendMessage("That is correct Goldace-kun").queue();
					}
				} else if(objMsg.getContent().toLowerCase().equals("can you believe this guy rem")){
					objChannel.sendMessage("Simply ridiculous").queue();
				} else if(objMsg.getContent().toLowerCase().equals("rem leave server")) {
					objGuild.leave().queue();
				} else if(objMsg.getContent().toLowerCase().contains("fuck me")) {
					objChannel.sendMessage("A-again Goldace-kun?").queue();
				} else if(objMsg.getContent().toLowerCase().contains("rem no")) {
					objChannel.sendMessage("aw...").queue();
				} else if(objMsg.getContent().toLowerCase().equals("hah")) {
					objChannel.sendMessage("goteem").queue();
				} else if(objMsg.getContent().toLowerCase().contains("is jack gay")) {
					objChannel.sendMessage("I believe so").queue();
				} else if(objMsg.getContent().toLowerCase().contains("is anton gay")) {
					objChannel.sendMessage("Yep").queue();
				}
			}
		} else if(objUser.getName().equals("haiemz")) {
			//objMsg.delete().queue();
		}
	}
}
