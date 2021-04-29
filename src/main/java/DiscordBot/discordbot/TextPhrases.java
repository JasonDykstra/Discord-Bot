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

			// Removed old text phrases
			
		} else if(objUser.getName().equals("haiemz")) {
			//objMsg.delete().queue();
		}
	}
}
