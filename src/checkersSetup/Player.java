package checkersSetup;

import java.util.ArrayList;

public interface Player {
	
	//Note: the reason game is a param is to deal with draws
	
	Move get1stMove(Game game, CheckersSetup setup, Board board, ArrayList<Move> choices);
	
	Move getNextJumps(CheckersSetup setup, Board board, ArrayList<Move> multiJumpChoices);
	
	//I don't like this because what if 1 player plays multiple games at once?
	void setDark();
	void setWhite();
	boolean isDarkPlayer();
	
	//TODO: string names
}
