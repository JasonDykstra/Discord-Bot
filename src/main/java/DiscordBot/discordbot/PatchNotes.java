package DiscordBot.discordbot;

//import net.dv8tion.jda.core.entities.Message;
//
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
 * list of patch notes accessible by /patchnotes so that i can keep track of what ive updated and when
 */

public class PatchNotes extends ListenerAdapter {
	
	public void sendPrivateMessage(User user, String msg, MessageReceivedEvent e) {
		user.openPrivateChannel().queue((channel) -> channel.sendMessage(msg).queue());
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent e) {
		Message objMsg = e.getMessage();
		User objUser = e.getAuthor();

		if(objMsg.getContentRaw().charAt(0) == '/') {
			String[] strArgs = objMsg.getContentRaw().substring(1).split(" ");
			if(strArgs[0].toLowerCase().equals("patchnotes")) {
				sendPrivateMessage(objUser, "__**Patch Notes**__"
						+ "\nv1.0 - (Added bot to pi) Added bot to rasberry pi and started running 24/7. Features included: Basic text commands, Music, Faulty tictactoe"
						+ "\nv1.0.1 - (Added patch notes) Added patch notes"
						+ "\nv1.0.2 - (Fixed tictactoe) Fixed bug in tictactoe where if a game was canceled then re-initiated, the initiator of the game was forced to move second"
						+ "\nv1.0.3 - (Fixed tictactoe) Fixed formatting so that board was in code block. Easier to print with monospacing"
						+ "\nv1.0.4 - (Fixed tictactoe) Fixed an issue where you could not use other commands while in a tictactoe game without being told a game is already in progress"
						+ "\nv1.1 - (Optimized tictactoe) Made it possible to play tictactoe on multiple guilds or text channels at once, changed tictactoe to object oriented"
						+ "\nv2.0 - (Added connect four) Now introducing connect four as a playable game! Also large connect four, for a more visually ehanced experience while on laptop or pc"
						+ "\nv2.0.1 (Fixed connect four) Fixed connect four win conditions"
						+ "\nv2.0.2 (Bug fixes) Fixed various visual bugs"
						+ "\nv2.0.3 (Numbered columns in big connect four) Added visual improvement (numbered columns) to big connect four"
						+ "\nv2.0.4 (Fixed connect four) Fixed the bug where it would say the game is a tie when the board is not yet full"
						+ "\nv2.0.5 (Added my win command to connect four) Added a win command just for me for connect four hehe"
						+ "\nv2.0.6 (Fixed connect four) Fixed the newly implemented win command which was causing many issues"
						+ "\nv2.1.0 (Added local multiplayer connect four) Added local multiplayer connect four"
						+ "\nv2.1.1 (Fixed message deleting) Fixed an issue where no messages would delete if one was older than 2 weeks"
						+ "\nv2.1.2 (Actually fixed message deleting) Sigh, coding is tough"
						+ "\nv2.1.3 (Fixed tic tac toe and connect 4) Connect 4 wasn't checking for up-right diagonals starting in the 4th column, and tic tac toe would print p1 or p2 in the upper left square when someone would win"
						+ "\nv3.0 (Migrated to JDA 4.x) Resurrected the bot from the dead!"
						+ "\n", e);
			}
		}
	}
}
