package DiscordBot.test;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TicTacToeTest {
    
    @Test
    public static void initializationTest() {
        int x = 3;
        assertEquals(3, x);
    }

    public static void main(String args[]){
        initializationTest();
    }
}
