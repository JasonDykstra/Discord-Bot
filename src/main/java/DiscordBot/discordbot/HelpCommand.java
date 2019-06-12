package DiscordBot.discordbot;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/*
 * the text that is sent when you type /help in a text channel with the bot in it
 */


public class HelpCommand extends ListenerAdapter{
	
	public void sendPrivateMessage(User user, String msg, MessageReceivedEvent e) {
		user.openPrivateChannel().queue((channel) -> channel.sendMessage(msg).queue());
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent e) {
		Message objMsg = e.getMessage();
		User objUser = e.getAuthor();

		if(objMsg.getContent().charAt(0) == '/') {
			String[] strArgs = objMsg.getContent().substring(1).split(" ");
			if(strArgs[0].equals("help")) {
				sendPrivateMessage(objUser, 
						"Command Prefix: /"
						+ "\n\n__**Commands:**__"
						+ "\n- ping : returns \"Pong!\""
						+ "\n- hello : returns \"Hello, @user!\""
						+ "\n- roll x : rolls a dice of x number of sides (default of 6 if no number of sides is specified)"
						+ "\n- bestgirl : reveals name of true bestgirl"
						+ "\n- userinfo (name) : gets the user info of the requested user, if no user is specified then user info for sender will be retrieved"
						+ "\n- profilepic (name) : gets a url for the profile pic of the requested user, if no user is specified then profile pic url for sending will be retrieved"
						+ "\n- clearbotandcommands x : clears last x messages of bot messages and user commands (default and maximum 99)"
						+ "\n- clearchat x : cleats last x messages sent in chat (default and maximum 99)"
						+ "\n- patchnotes : view patch notes history"
						+ "\n- seig heil : Jack asked me to make this one..."
						//+ "\n- pout"
						//+ "\n- smug"
						//+ "\n- lewd"
						//+ "\n- fuko"
						//+ "\n- thighs : all soon to be functional"
						+ "\n"
						+ "\n"
						+ "\n__**Game Commands:**__"
						+ "\n- tictactoe (user) : starts a tictactoe game with someone"
						+ "\n		- move (row) (column) : makes a move on the board"
						+ "\n		- board : sends the board in chat"
						+ "\n		- endtictactoegame : stops current tictactoe game if 1 or less moves has been made"
						+ "\n"
						+ "\n"
						+ "\n- connect4 (user) : starts a connect four game with someone"
						+ "\n		- drop (column) : makes a move on the board"
						+ "\n		- board : sends the board in chat"
						+ "\n		- endconnect4game : ends current connect four game"
						+ "\n"
						+ "\n"
						+ "\n- bigconnect4 (user) : starts a visually larger connect four game with someone, best if on a laptop or pc"
						+ "\n		- same commands as connect4"
						+ "\n"
						+ "\n"
						+ "\n"
						+ "__**Music Commands:**__", e);
						
						
						sendPrivateMessage(objUser,
						 "- play : searches youtube for requested song or plays any linked youtube video (can be from playlist)"
						+ "\n- pause : pauses music"
						+ "\n- resume/play : resumes paused music and makes bot rejoin channel if it left"
						+ "\n- leave : pauses music and makes bot leave channel"
						+ "\n- join : resumes music and makes bot join channel"
						+ "\n- queue: shows list of queued songs"
						+ "\n- clearqueue : clears queue"
						+ "\n- songinfo : displays info for currently playing song"
						+ "\n- skip : skips current song"
						+ "\n- skipto x : skips to song in position x in queue"
						+ "\n"
						+ "\n"
						+ "\n__**Coding Specific Information:**__"
						+ "\n- Priority of searches for users: exact name > beginning of name > name contains (Name is of higher priority than username)"
						+ "\n- Music bot interface used: Lavaplayer"
						, e);
			}
		}
	}
}
