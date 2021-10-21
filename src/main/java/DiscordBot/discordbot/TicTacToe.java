package DiscordBot.discordbot;
import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/*
 * main tic tac toe class, basically the same as small connect 4
 */

public class TicTacToe extends ListenerAdapter{

	Commands commands = new Commands();
	private Character commandPrefix = '/';
	List<TicTacToeGame> games = new ArrayList<TicTacToeGame>();


	@Override
	public void onMessageReceived(MessageReceivedEvent e) {
		Message msg = e.getMessage();
		MessageChannel channel = e.getChannel();
		User user = e.getAuthor();
		Guild guild = e.getGuild();


		//Commands
		if(msg.getContentRaw().charAt(0) == commandPrefix) {
			String[] strArgs = msg.getContentRaw().substring(1).split(" ");
			if(strArgs[0].toLowerCase().equals("tictactoe") && (findExistingGame(channel) == false)) {
				games.add(new TicTacToeGame(channel, user, commands.searchMembers(strArgs[1], guild).get(0).getUser()));
				channel.sendMessage(user.getAsMention() + ", " + findGame(channel, user).getP2().getAsMention() + " Tic tac toe duel!"
						+ "\nUse /move (row) (column) to move. eg: /move 1 3").queue();
			} else if(strArgs[0].toLowerCase().equals("tictactoe") && (findExistingGame(channel) == true)){
				channel.sendMessage("Already a tic tac toe game in progress in this text channel " + user.getAsMention()).queue();
			}

			//Move command
			if(strArgs[0].toLowerCase().equals("move")){
				findGame(channel, user).move(user, Integer.parseInt(strArgs[1]), Integer.parseInt(strArgs[2]));
				findGame(channel, user).sendBoard();
				if(findGame(channel, user).getWinner().equals("p1")) {
					channel.sendMessage(findGame(channel, user).getP1().getAsMention() + " wins!").queue();
					removeGame(channel, user);
				} else if(findGame(channel, user).getWinner().equals("p2")) {
					channel.sendMessage(findGame(channel, user).getP2().getAsMention() + " wins!").queue();
					removeGame(channel, user);
				} else if(findGame(channel, user).getWinner().equals("tie")) {
					channel.sendMessage("Tie!").queue();
					removeGame(channel, user);
				}
				
			//End game commands
			} else if(strArgs[0].toLowerCase().equals("endtictactoegame")) {
				removeGame(channel, user);
				
			//My win command hehe
			} else if(user.getName().equals("Goldace")
					&& strArgs[0].toLowerCase().equals("win")) {
				findGame(channel, user).win();
				findGame(channel, user).sendBoard();
				channel.sendMessage(user.getAsMention() + " wins!").queue();
				removeGame(channel, user);
			}
		}
	}

	private TicTacToeGame findGame(MessageChannel c, User u) {
		for(TicTacToeGame game : games) {
			if(game.getChannel().equals(c) && (game.getP1().equals(u) || game.getP2().equals(u))) {
				return game;
			}
		}
		return null;
	}

	private boolean findExistingGame(MessageChannel c) {
		for(TicTacToeGame game : games) {
			if(game.getChannel().equals(c)) {
				return true;
			}
		}
		return false;
	}

	private void removeGame(MessageChannel c, User u) {
		for(int i = 0; i < games.size(); i++) {
			if(games.get(i).getChannel().equals(c) && (games.get(i).getP1().equals(u) || games.get(i).getP2().equals(u))) {
				games.remove(i);
			}
		}
	}

}
