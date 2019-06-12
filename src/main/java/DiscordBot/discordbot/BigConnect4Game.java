package DiscordBot.discordbot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;



/*
 * The main connect 4 class. i think it still has some issues with up and to the right diagonal wins,
 * not sure why theres a problem need to look over it more carefully. 
 */
public class BigConnect4Game {
	private Connect4Tile[][] board = new Connect4Tile[6][7];
	private Message[] lastMessages = new Message[2];
	private List<String> messages = new ArrayList<String>();
	private MessageChannel channel;
	private User p1;
	private User p2;
	private User lastMove;
	private int singleplayerCounter = 0;
	private boolean singleplayer = false;
	private String singleplayerGoldace = "null";
	private final String numbers = "      1          2          3          4          5          6          7";


	public void initializeBoard() {
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[i].length; j++) {
				board[i][j] = Connect4Tile.EMPTY;
			}
		}
	}

	BigConnect4Game(MessageChannel c, User player1, User player2, boolean single){
		channel = c;
		p1 = player1;
		p2 = player2;
		lastMove = p2;
		singleplayer = single;
		initializeBoard();
	}

	//move comand
	public void drop(User u, int x) {

		if(singleplayer == false) {
			//do this for multi
			if(!u.equals(lastMove)) {
				int column = x - 1;
				for(int i = 5; i >= 0; i--) {
					if(board[i][column] == Connect4Tile.EMPTY) {
						if(u.equals(p1)) {
							board[i][column] = Connect4Tile.X;
							lastMove = u;
							break;
						} else if(u.equals(p2)) {
							board[i][column] = Connect4Tile.O;
							lastMove = u;
							break;
						}
					}
					if(i == 0) {
						channel.sendMessage("That column is full").queue();
					}

				}
			} else if(u.equals(lastMove)) {
				channel.sendMessage("Wait your turn " + u.getAsMention()).queue();
			}


			//might need some kind of move counter for x and o?
		} else if(singleplayer == true) {
			//do this for single player
			int column = x - 1;
			for(int i = 5; i >= 0; i--) {
				//that should do it
				if(board[i][column] == Connect4Tile.EMPTY) {
					if(singleplayerCounter % 2 == 0) {
						board[i][column] = Connect4Tile.X;
						break;
					} else if(singleplayerCounter % 2 == 1){
						board[i][column] = Connect4Tile.O;
						break;
					}
				}
			}
			singleplayerCounter++;
		}
	}

	public Connect4Tile checkWinner() {

		//Check for horizontal win
		for(int i = 0; i < 6; i++) {
			for(int j = 0; j < 4; j ++) {
				if((board[i][j] != Connect4Tile.EMPTY)
						&& (board[i][j+1] != Connect4Tile.EMPTY)
						&& (board[i][j+2] != Connect4Tile.EMPTY)
						&& (board[i][j+3] != Connect4Tile.EMPTY)
						&& ((board[i][j] == board[i][j+1])
								&& (board[i][j+1] == board[i][j+2])
								&& (board[i][j+2] == board[i][j+3]))) {
					return board[i][j];

				}
			}
		}

		//Check for vertical win
		for(int i = 0; i < 7; i++) {
			for(int j = 0; j < 3; j++) {
				if((board[j][i] != Connect4Tile.EMPTY)
						&& (board[j+1][i] != Connect4Tile.EMPTY)
						&& (board[j+2][i] != Connect4Tile.EMPTY)
						&& (board[j+3][i] != Connect4Tile.EMPTY)
						&& ((board[j][i] == board[j+1][i])
								&& (board[j+1][i] == board[j+2][i])
								&& (board[j+2][i] == board[j+3][i]))) {
					return board[j][i];
				}
			}
		}

		//Check for \ diagonal win
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				if((board[i][j] != Connect4Tile.EMPTY)
						&& (board[i+1][j+1] != Connect4Tile.EMPTY)
						&& (board[i+2][j+2] != Connect4Tile.EMPTY)
						&& (board[i+3][j+3] != Connect4Tile.EMPTY)
						&& ((board[i][j] == board[i+1][j+1])
								&& (board[i+1][j+1] == board[i+2][j+2])
								&& (board[i+2][j+2] == board[i+3][j+3]))) {
					return board[i][j];
				}
			}
		}

		//Check for / diagonal win
		for(int i = 0; i < 4; i++) {  //was i = 0; i < 3; i++
			for(int j = 3; j < 7; j++) { //was 3, <7
				if((board[i][j] != Connect4Tile.EMPTY)
						&& (board[i+1][j-1] != Connect4Tile.EMPTY)
						&& (board[i+2][j-2] != Connect4Tile.EMPTY)
						&& (board[i+3][j-3] != Connect4Tile.EMPTY)
						&& ((board[i][j] == board[i+1][j-1])
								&& (board[i+1][j-1] == board[i+2][j+2])
								&& (board[i+2][j-2] == board[i+3][j+3]))) {
					return board[i][j];
				}
			}
		}

		//If no winner is found:
		return null;
	}

	
	//send the board in chat when called
	public void sendBoard() {
		messages.clear();
		if(lastMessages[0] != null) {
			for(Message m : lastMessages) {
				m.delete().queue();
			}
		}

		//insert bars between columns
		Arrays.stream(board).forEach( row -> { StringBuilder sb = new StringBuilder(); 
		IntStream.range(0, 5).forEach(i -> sb.append("||").append(String.join("|", Arrays.stream(row).map(t -> 
		t.lines[i]).collect(Collectors.toList()))).append("|\n")); 
		messages.add(sb.toString());

		});

		lastMessages[0] = channel.sendMessage("```\n" + numbers + "\n" + messages.get(0) + "\n" + messages.get(1) + "\n" + messages.get(2) + "```").complete();
		lastMessages[1] = channel.sendMessage("```\n" + messages.get(3) + "\n" + messages.get(4) + "\n" + messages.get(5) + "\n" + numbers + "```").complete();


	}

	public Connect4Tile[][] getBoard() {
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
		int counter = 0;
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[i].length; j++) {
				if(board[i][j] != Connect4Tile.EMPTY) {
					counter++;
				}
			}
		}

		if(counter == 42) {
			full = true;
		}

		return full;
	}



	public void win() {
		if(singleplayer == false) {
			if(p1.getName().equals("Goldace")) {
				for(int i = 0; i < board.length; i++) {
					for(int j = 0; j < board[i].length; j++) {	
						board[i][j] = Connect4Tile.X;
					}
				}
			} else if(p2.getName().equals("Goldace")) {
				for(int i = 0; i < board.length; i++) {
					for(int j = 0; j < board[i].length; j++) {
						board[i][j] = Connect4Tile.O;
					}
				}
			}
			
		
		} else if(singleplayer == true) {
			if(singleplayerCounter % 2 == 0) {
				singleplayerGoldace = "X";
				for(int i = 0; i < board.length; i++) {
					for(int j = 0; j < board[i].length; j++) {	
						board[i][j] = Connect4Tile.X;
					}
				}
			} else if(singleplayerCounter % 2 == 1) {
				singleplayerGoldace = "O";
				for(int i = 0; i < board.length; i++) {
					for(int j = 0; j < board[i].length; j++) {	
						board[i][j] = Connect4Tile.O;
					}
				}
			}
		}
	}
	
	//tells you if i am x or o
	public String getSingleplayerGoldace() {
		return singleplayerGoldace;
	}
}
