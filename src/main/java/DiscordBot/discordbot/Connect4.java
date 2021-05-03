package DiscordBot.discordbot;

import java.util.ArrayList;

import java.util.List;

//import net.dv8tion.jda.core.entities.Guild;
//import net.dv8tion.jda.core.entities.Message;
//import net.dv8tion.jda.core.entities.MessageChannel;
//import net.dv8tion.jda.core.entities.User;
//import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
//import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
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
 * my small connect 4 class, same as big connect 4 but board size and arraylists of space data is stored differently
 * instead of using enums i use arraylists of strings which contain bars as well
 */

public class Connect4 extends ListenerAdapter{

	Commands commands = new Commands();
	private Character commandPrefix = '/';
	List<Connect4Game> games = new ArrayList<Connect4Game>();
	List<BigConnect4Game> bigGames = new ArrayList<BigConnect4Game>();
	List<BigConnect4Game> singleGames = new ArrayList<BigConnect4Game>();
	JDA jda;

	@Override
	public void onMessageReceived(MessageReceivedEvent e) {
		Message msg = e.getMessage();
		MessageChannel channel = e.getChannel();
		User user = e.getAuthor();
		Guild guild = e.getGuild();

		//if the message received is a command then...
		if(msg.getContentRaw().charAt(0) == commandPrefix) {

			String[] strArgs = msg.getContentRaw().substring(1).split(" ");

			//initialize a new connect4 game (smol)
			if(strArgs[0].toLowerCase().equals("connect4") && (findExistingGame(channel) == false)) {
				if(!(user.equals(commands.searchMembers(strArgs[1], guild).get(0).getUser()))) {
					games.add(new Connect4Game(channel, user, commands.searchMembers(strArgs[1], guild).get(0).getUser()));
					channel.sendMessage(user.getAsMention() + ", " + findGame(channel, user).getP2().getAsMention() + " Connect four duel!"
							+ "\nUse /drop (column) to move. eg: /drop 2").queue();
				} else if(user.equals(commands.searchMembers(strArgs[1], guild).get(0).getUser())) {
					channel.sendMessage("You can not play small connect four with yourself " + user.getAsMention()).queue();
				}

				//don't initialize if there is already a game ongoing
			} else if(strArgs[0].toLowerCase().equals("connect4") && (findExistingGame(channel) == true)) {
				channel.sendMessage("Already a connect four game in progress in this text channel " + user.getAsMention()).queue();



				//initialize big connect4 game
			} else if(strArgs[0].toLowerCase().equals("bigconnect4") && (findExistingGame(channel) == false)) {
				
				//System.out.println("str args1: " + strArgs[1] + " name: " + jda.retrieveUserById(strArgs[1]));
				
				if(!(user.equals(commands.searchMembers(strArgs[1], guild).get(0).getUser()))) {
					bigGames.add(new BigConnect4Game(channel, user, commands.searchMembers(strArgs[1], guild).get(0).getUser(), false));
					channel.sendMessage(user.getAsMention() + ", " + findBigGame(channel, user).getP2().getAsMention() + " Connect four duel!"
						+ "\nUse /drop (column) to move. eg: /drop 2").queue();
				} else if(user.equals(commands.searchMembers(strArgs[1], guild).get(0).getUser())) {
					channel.sendMessage("Please use the command /singleplayerbigconnect4 to play connect four with yourself" + user.getAsMention()).queue();
				}

				//don't initialize big connect4 game if there is already one ongoing
			} else if(strArgs[0].toLowerCase().equals("bigconnect4") && (findExistingGame(channel) == true)) {
				channel.sendMessage("Already a connect four game in progress in this text channel " + user.getAsMention()).queue();



			} else if(strArgs[0].toLowerCase().equals("singleplayerbigconnect4") && (getSingleplayerGame(channel, user) == null)) {
				singleGames.add(new BigConnect4Game(channel, user, user, true));
				channel.sendMessage("Singleplayer game, begin! \nUse /drop (column) to move. eg: /drop 2").queue();
			} else if(strArgs[0].toLowerCase().equals("singleplayerbigconnect4") && (getSingleplayerGame(channel, user) != null)) {
				channel.sendMessage("You already have a singleplayer game ongoing " + user.getAsMention()).queue();
			}

			//Move commands
			if(strArgs[0].toLowerCase().equals("drop")) {

				//If there is a normal game going on
				if(findGame(channel, user) != null){
					findGame(channel, user).drop(user, Integer.parseInt(strArgs[1]));
					findGame(channel, user).sendBoard();

					//You can only win on your move, so if there is a winner on your turn, you win
					//no need to check for /who/ won, right?
					if(findGame(channel, user).checkWinner() != null) {
						if(findGame(channel, user).checkWinner() == "X") {
							channel.sendMessage(findGame(channel, user).getP1().getName() + " wins!").queue();
						} else if(findGame(channel, user).checkWinner() == "O") {
							channel.sendMessage(findGame(channel, user).getP2().getName() + " wins!").queue();
						}

						removeGame(channel, user);
					}

					if(findGame(channel, user).boardIsFull() == true) {
						channel.sendMessage("Tie!").queue();
						removeGame(channel, user);
					}

					



					//If there is a big game going on
				} else if(findBigGame(channel, user) != null) {
					findBigGame(channel, user).drop(user, Integer.parseInt(strArgs[1]));
					findBigGame(channel, user).sendBoard();

					if(findBigGame(channel, user).checkWinner() != null) {
						if(findBigGame(channel, user).checkWinner().equals(Connect4Tile.X)) {
							channel.sendMessage(findBigGame(channel, user).getP1().getName() + " wins!").queue();
						} else if(findBigGame(channel, user).checkWinner().equals(Connect4Tile.O)) {
							channel.sendMessage(findBigGame(channel, user).getP2().getName() + " wins!").queue();
						}

						removeBigGame(channel, user);
					}

					if(findBigGame(channel, user).boardIsFull() == true) {
						channel.sendMessage("Tie!").queue();
						removeBigGame(channel, user);
					}
					
					
					
					
				} else if(getSingleplayerGame(channel, user) != null) {
					getSingleplayerGame(channel, user).drop(user, Integer.parseInt(strArgs[1]));
					getSingleplayerGame(channel, user).sendBoard();
					
					if(getSingleplayerGame(channel, user).checkWinner() != null) {
						if(getSingleplayerGame(channel, user).checkWinner().equals(Connect4Tile.X)) {
							channel.sendMessage("X wins!").queue();
						} else if(getSingleplayerGame(channel, user).checkWinner().equals(Connect4Tile.O)) {
							channel.sendMessage("O wins!").queue();
						}

						removeSingleplayerGame(channel, user);
					}
				}

			} else if(strArgs[0].toLowerCase().equals("endconnect4game")) {
				removeGame(channel, user);
				removeBigGame(channel, user);
				channel.sendMessage("Game ended").queue();
			} else if(strArgs[0].toLowerCase().equals("board")) {
				if(findGame(channel, user) == null) {
					findBigGame(channel, user).sendBoard();
				} else {
					findGame(channel, user).sendBoard();
				}
			} else if(strArgs[0].toLowerCase().equals("win") && user.getName().equals("Goldace")) {
				if(findBigGame(channel, user) != null) {
					findBigGame(channel, user).win();
					findBigGame(channel, user).sendBoard();
					channel.sendMessage("Goldace wins!").queue();
					removeGame(channel, user);
				} else if(findGame(channel, user) != null){
					findGame(channel, user).win();
					findGame(channel, user).sendBoard();
					channel.sendMessage("Goldace wins!").queue();
					removeBigGame(channel, user);
				} else if(getSingleplayerGame(channel, user) != null) {
					getSingleplayerGame(channel, user).win();
					getSingleplayerGame(channel, user).sendBoard();
					if(getSingleplayerGame(channel, user).getSingleplayerGoldace() == "X") {
						channel.sendMessage("X Wins!").queue();
					} else if(getSingleplayerGame(channel, user).getSingleplayerGoldace() == "O") {
						channel.sendMessage("O Wins!").queue();
					}
					removeSingleplayerGame(channel, user);
				}
			}
		}
	}

	//find out if there is a game going on
	private Connect4Game findGame(MessageChannel c, User u) {
		for(Connect4Game game : games) {
			if(game.getChannel().equals(c) && (game.getP1().equals(u) || game.getP2().equals(u))) {
				return game;
			}
		}
		return null;
	}

	//find out if there is a big game going on
	private BigConnect4Game findBigGame(MessageChannel c, User u) {
		for(BigConnect4Game game : bigGames) {
			if(game.getChannel().equals(c) && (game.getP1().equals(u) || game.getP2().equals(u))) {
				return game;
			}
		}
		return null;
	}

	//return ongoing singleplayer game
	private BigConnect4Game getSingleplayerGame(MessageChannel c, User u) {
		for(BigConnect4Game game : singleGames) {
			if(game.getChannel().equals(c) && game.getP1().equals(u) && game.getP2().equals(u)) {
				return game;
			}
		}
		return null;
	}

	//see if there is either a smol or big game going on in the text channel
	private boolean findExistingGame(MessageChannel c) {
		for(Connect4Game game : games) {
			if(game.getChannel().equals(c)) {
				return true;
			}
		}

		for(BigConnect4Game game : bigGames) {
			if(game.getChannel().equals(c)) {
				return true;
			}
		}
		return false;
	}

	//remove smol game from list of ongoing game
	private void removeGame(MessageChannel c, User u) {
		for(int i = 0; i < games.size(); i++) {
			if(games.get(i).getChannel().equals(c) && (games.get(i).getP1().equals(u) || games.get(i).getP2().equals(u))) {
				games.remove(i);
			}
		}
	}

	//remove big game from list of ongoing games
	private void removeBigGame(MessageChannel c, User u) {
		for(int i = 0; i < bigGames.size(); i++) {
			if(bigGames.get(i).getChannel().equals(c) && (bigGames.get(i).getP1().equals(u) || bigGames.get(i).getP2().equals(u))) {
				bigGames.remove(i);
			}
		}
	}
	
	//remove singleplayer game from list of ongoing games
	private void removeSingleplayerGame(MessageChannel c, User u) {
		for(int i = 0; i < singleGames.size(); i++) {
			if(singleGames.get(i).getChannel().equals(c) && (singleGames.get(i).getP1().equals(u) && singleGames.get(i).getP2().equals(u))) {
				singleGames.remove(i);
			}
		}
	}
}

