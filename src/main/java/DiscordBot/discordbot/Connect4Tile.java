package DiscordBot.discordbot;


/*
 * enum class of big connect 4 tiles
 */

//Initialize the tile enums to use in big connect4 game
public enum Connect4Tile {
	EMPTY("         |\n"
		+ "         |\n"
		+ "         |\n"
		+ "         |\n"
		+ "         |"), 
	
	X("  X   X  |\n"
	+ "   X X   |\n"
	+ "    X    |\n"
	+ "   X X   |\n"
	+ "  X   X  |"), 
	
	O("   OOO   |\n"
	+ "  O   O  |\n"
	+ "  O   O  |\n"
	+ "  O   O  |\n"
	+ "   OOO   |");
	
	
	public final String[] lines;

    Connect4Tile(String lines) {
        this.lines = lines.split("\n");
    }
}
