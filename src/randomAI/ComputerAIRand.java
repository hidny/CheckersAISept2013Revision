package randomAI;

import java.util.ArrayList;

import checkersSetup.CheckersSetup;
import checkersSetup.Game;
import checkersSetup.Move;

public class ComputerAIRand  implements checkersSetup.Player {

	//Default to false:
	//TODO: maybe we should force the colour to be set before allowing any other function to work...
	boolean isDarkPlayer = false;
	
	
	public void setDark() {
		isDarkPlayer = true;
	}
	public void setWhite() {
		isDarkPlayer = false;
	}
	
	public boolean isDarkPlayer() {
		return isDarkPlayer;
	}
	
	public Move get1stMove(Game game, CheckersSetup setup, checkersSetup.Board board, ArrayList<Move> choices) {
		int randIndex =(int) (choices.size() * Math.random());
		//BasicCheckersAIFunctions.delayCompALittle();
		return choices.get(randIndex);
		
	}
	
	public Move getNextJumps(CheckersSetup setup, checkersSetup.Board board, ArrayList<Move> multiJumpChoices) {
		int randIndex =(int) (multiJumpChoices.size() * Math.random());
		//BasicCheckersAIFunctions.delayCompALittle();
		return multiJumpChoices.get(randIndex);
		
	}
	
	
}
