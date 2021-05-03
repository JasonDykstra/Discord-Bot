package DiscordBot.discordbot;

//import net.dv8tion.jda.core.entities.Channel;
//
//import net.dv8tion.jda.core.entities.Guild;
//import net.dv8tion.jda.core.entities.Message;
//import net.dv8tion.jda.core.entities.MessageChannel;
//import net.dv8tion.jda.core.entities.User;
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
import net.dv8tion.jda.api.exceptions.RateLimitedException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

/*
 * tic tac toe game mechanics class
 */

public class TicTacToeGame extends ListenerAdapter{
	Commands commands = new Commands();
	private Character commandPrefix = '/';
	String[][] board = {{"   ", "   ", "   "},
			{"   ", "   ", "   "},
			{"   ", "   ", "   "}};
	private Message lastMsg;
	private User p2;
	private User p1;
	private MessageChannel channel;
	private User lastMove;
	private int spacesUsed = 0;
	String winner;


	public static void main(String[] args) {

	}

	public TicTacToeGame(MessageChannel c, User player1, User player2) {
		channel = c;
		p1 = player1;
		p2 = player2;
		lastMove = p2;
	}

	public void move(User u, int row, int column){
		

		if(u.equals(lastMove)) {
			channel.sendMessage("Wait your turn " + u.getAsMention()).queue();
		} else {
			
			
			if(row > 0 && row < 4 && column > 0 && column < 4) {
				if(u.equals(p1)) {
					board[row-1][column-1] = " X ";
				} else if(u.equals(p2)) {
					board[row-1][column-1] = " O ";
				} else {
					System.out.println("No users match for this game");
				}
			} else {
				System.out.println("bad input, can't move there");
			}
			
			//if it's a tie
			for(int i = 0; i < 3; i++) {
				for(int n = 0; n < 3; n++) {
					if(board[i][n].contains("X") || board[i][n].contains("O")) {
						spacesUsed++;
					}
				}
			}

			if(spacesUsed == 9){
				winner = "tie";
			}	
			
			
			//if someone wins
			for(int i = 0; i < 3; i++) {
				//horizontal win
				if(((board[i][0].contains("X") && board[i][1].contains("X") && board[i][2].contains("X"))
						|| (board[i][0].contains("O") && board[i][1].contains("O") && board[i][2].contains("O")))
						//vertical win
						|| ((board[0][i].contains("X") && board[1][i].contains("X") && board[2][i].contains("X"))
								|| (board[0][i].contains("O") && board[1][i].contains("O") && board[2][i].contains("O")))

						//diagonals
						|| (board[0][0].contains("X") && board[1][1].contains("X") && board[2][2].contains("X"))
						|| (board[0][2].contains("X") && board[1][1].contains("X") && board[2][0].contains("X"))
						|| (board[0][0].contains("O") && board[1][1].contains("O") && board[2][2].contains("O"))
						|| (board[0][2].contains("O") && board[1][1].contains("O") && board[2][0].contains("O"))
						) {
					
					//why would i set the upper left square to say p1 or p2 what?
					if(u.equals(p1)) {
						//board[0][0] = "p1";
						winner = "p1";
					} else {
						//board[0][0] = "p2";
						winner = "p2";
					}
					
				}
			}
			
			
		
			
			spacesUsed = 0;
			lastMove = u;
		}
	}
	
	public void sendBoard() {
		
		if(lastMsg != null) {
			lastMsg.delete().queue();
		}
		
		
		lastMsg = channel.sendMessage("```"
				+ "\n"
				+ board[0][0] + "|" + board[0][1] + "|" + board[0][2]
						+ "\n"
						+ "-----------"
						+ "\n"
						+ board[1][0] + "|" + board[1][1] + "|" + board[1][2]
								+ "\n"
								+ "-----------"
								+ "\n"
								+ board[2][0] + "|" + board[2][1] + "|" + board[2][2]
										+ "\n"
										+ "```"
				).complete();
	}
	
	public String[][] getBoard(){
		return board;
	}
	
	public MessageChannel getChannel() {
		return channel;
	}
	
	public User getP1() {
		return p1;
	}
	
	public User getP2() {
		return p2;
	}
	
	public User getLastMove() {
		return lastMove;
	}
	
	public void win() {
		for(int i = 0; i < 3; i++) {
			for(int n = 0; n < 3; n++) {
				board[i][n] = " X ";
			}
		}
	}
	
	public String getWinner() {
		return winner;
	}
}