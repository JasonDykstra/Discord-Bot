import org.junit.jupiter.api.Test;
import DiscordBot.discordbot.TicTacToeGame;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.internal.entities.UserById;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Arrays;

public class TicTacToeTest {

    MessageChannel dummyChannel = null;
    User dummyUser1 = new UserById(1);
    User dummyUser2 = new UserById(2);
    
    @Test
    public void boardInitializationTest() {
        TicTacToeGame testGame = new TicTacToeGame(dummyChannel, dummyUser1, dummyUser2);
        String[][] correctBoard = {{"   ", "   ", "   "},
        {"   ", "   ", "   "},
        {"   ", "   ", "   "}};
        assertEquals(testGame.getBoard(), correctBoard);
    }

    @Test
    public void moveTest(){
        TicTacToeGame testGame = new TicTacToeGame(dummyChannel, dummyUser1, dummyUser2);
        testGame.move(dummyUser1, 1, 1);
        String[][] correctBoard = {{" X ", "   ", "   "},
        {"   ", "   ", "   "},
        {"   ", "   ", "   "}};
        assertTrue(Arrays.deepEquals(testGame.getBoard(), correctBoard));
    }

    @Test
    public void getLastMoveTest(){
        TicTacToeGame testGame = new TicTacToeGame(dummyChannel, dummyUser1, dummyUser2);
        User correctLastMove = dummyUser2;
        
        // Test to see if last move is initialized correctly
        assertEquals(testGame.getLastMove(), correctLastMove);

        // Make a move
        testGame.move(dummyUser1, 1, 1);

        // Test to see if last move was updated properly
        correctLastMove = dummyUser1;
        assertEquals(testGame.getLastMove(), correctLastMove);
    }

    @Test
    public void winnerInitializationTest(){
        TicTacToeGame testGame = new TicTacToeGame(dummyChannel, dummyUser1, dummyUser2);
        String correctWinner = "";

        assertEquals(testGame.getWinner(), correctWinner);
    }

    @Test
    public void user1WinnerTest(){
        TicTacToeGame testGame = new TicTacToeGame(dummyChannel, dummyUser1, dummyUser2);
        String correctWinner = "p1";
        
        // Make moves so that user 1 wins
        testGame.move(dummyUser1, 1, 1);
        testGame.move(dummyUser2, 2, 1);
        testGame.move(dummyUser1, 1, 2);
        testGame.move(dummyUser2, 2, 2);
        testGame.move(dummyUser1, 1, 3);

        assertEquals(testGame.getWinner(), correctWinner);
    }

    @Test
    public void user2WinnerTest(){
        TicTacToeGame testGame = new TicTacToeGame(dummyChannel, dummyUser1, dummyUser2);
        String correctWinner = "p2";
        
        // Make moves so that user 2 wins
        testGame.move(dummyUser1, 1, 1);
        testGame.move(dummyUser2, 2, 1);
        testGame.move(dummyUser1, 1, 2);
        testGame.move(dummyUser2, 2, 2);
        testGame.move(dummyUser1, 3, 3);
        testGame.move(dummyUser2, 2, 3);

        assertEquals(testGame.getWinner(), correctWinner);
    }

    @Test
    public void tieGameTest(){
        TicTacToeGame testGame = new TicTacToeGame(dummyChannel, dummyUser1, dummyUser2);
        String correctWinner = "tie";
        
        // Make moves so that the game ends in a tie
        testGame.move(dummyUser1, 2, 2);
        testGame.move(dummyUser2, 1, 1);
        testGame.move(dummyUser1, 3, 3);
        testGame.move(dummyUser2, 1, 3);
        testGame.move(dummyUser1, 1, 2);
        testGame.move(dummyUser2, 3, 2);
        testGame.move(dummyUser1, 3, 1);
        testGame.move(dummyUser2, 2, 3);
        testGame.move(dummyUser1, 2, 1);

        assertEquals(testGame.getWinner(), correctWinner);
    }
}
