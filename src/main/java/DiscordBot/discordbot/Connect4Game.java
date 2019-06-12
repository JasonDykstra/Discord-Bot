package DiscordBot.discordbot;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Connect4Game extends ListenerAdapter{
	private String[][] board = new String[7][15];
	private Message lastMessage;
	private MessageChannel channel;
	private User p1;
	private User p2;
	private User lastMove;



	public void initializeBoard() {
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[i].length; j++) {
				if(j % 2 == 0) {
					board[i][j] = "|";
				} else {
					board[i][j] = " ";
				}

				if(i == 6) {
					board[i][j] = "-";
				}
			}
		}
	}


	public Connect4Game(MessageChannel c, User player1, User player2) {
		channel = c;
		p1 = player1;
		p2 = player2;
		lastMove = p2;
		initializeBoard();
	}



	public void drop(User u, int x) {
		System.out.println(p1.getName());
		if(!u.equals(lastMove)) {
			int column = (2*x) - 1;
			for(int i = 5; i >= 0; i--) {
				if(board[i][column] == " ") {
					if(u.equals(p1)) {
						board[i][column] = "X";
						lastMove = u;
						break;
					} else if(u.equals(p2)) {
						board[i][column] = "O";
						lastMove = u;
						break;
					}
				}
				if(i == 0) {
					channel.sendMessage("That column is full").queue();
				}
			}
		} else if(u.equals(lastMove)){
			channel.sendMessage("Wait your turn " + u.getAsMention()).queue();
		}


	}


	public String checkWinner() {

		//Check for horizontal win
		for(int i = 0; i < 6; i++) {
			for(int j = 0; j < 7; j += 2) {
				if((board[i][j+1] != " ")
						&& (board[i][j+3] != " ")
						&& (board[i][j+5] != " ")
						&& (board[i][j+7] != " ")
						&& ((board[i][j+1] == board[i][j+3])
								&& (board[i][j+3] == board[i][j+5])
								&& (board[i][j+5] == board[i][j+7]))) {
					return board[i][j+1];
				}
			}
		}

		//Check for vertical win
		for(int i = 1; i < 15; i += 2) {
			for(int j = 0; j < 3; j++) {
				if((board[j][i] != " ")
						&& (board[j+1][i] != " ")
						&& (board[j+2][i] != " ")
						&& (board[j+3][i] != " ")
						&& ((board[j][i] == board[j+1][i])
								&& (board[j+1][i] == board[j+2][i])
								&& (board[j+2][i] == board[j+3][i]))) {
					return board[j][i];
				}
			}
		}

		//Check for \ diagonal win
		for(int i = 0; i < 4; i++) {
			for(int j = 1; j < 9; j += 2) {
				if((board[i][j] != " ")
						&& (board[i+1][j+2] != " ")
						&& (board[i+2][j+4] != " ")
						&& (board[i+3][j+6] != " ")
						&& ((board[i][j] == board[i+1][j+2])
								&& (board[i+1][j+2] == board[i+2][j+4])
								&& (board[i+2][j+4] == board[i+3][j+6])))
					return board[i][j]; 

			}
		}

		//Check for / diagonal win
		for(int i = 0; i < 4; i++) {
			for(int j = 7; j < 15; j += 2) {
				if((board[i][j] != " ")
						&& (board[i+1][j-2] != " ")
						&& (board[i+2][j-4] != " ")
						&& (board[i+3][j-6] != " ")
						&& ((board[i][j] == board[i+1][j-2])
								&& (board[i+1][j-2] == board[i+2][j-4])
								&& (board[i+2][j-4] == board[i+3][j-6])))
					return board[i][j]; 
			}
		}

		//if no winner is found:
		return null;

	}

	public void sendBoard() {
		String b = "```\n";
		if(lastMessage != null) {
			lastMessage.delete().queue();
		}

		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[i].length; j++) {
				b += board[i][j];
			}
			b += "\n";
		}
		b += "```";

		lastMessage = channel.sendMessage(b).complete();

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

	public boolean boardIsFull() {
		boolean full = false;
		for(int i = 0; i < board.length; i++) {
			for(int j = 1; j < board[i].length; j += 2) {
				if(board[i][j] != " " && (i == board.length - 1) && (j == board[i].length - 1)) {
					full = true;
				}
			}
		}

		return full;
	}

	public void win() {
		if(p1.getName().equals("Goldace")) {
			for(int i = 0; i < board.length-1; i++) {
				for(int j = 1; j < board[i].length; j += 2) {
					board[i][j] = "X";
				}
			}
		} else if(p2.getName().equals("Goldace")) {
			for(int i = 0; i < board.length-1; i++) {
				for(int j = 1; j < board[i].length; j += 2) {
						board[i][j] = "O";
				}
			}
		}
	}
}
