package checkersSetup;

import java.util.ArrayList;

public class Human implements Player{
	
	boolean isDarkPlayer;
	//So far all human players are treated the same.
	//TODO: create dynamic humans (optional)
	//TODO: make a print method specifically for an instance of Human.
	
	/*Player's turn:*/
	public static void printMoveDetails(boolean isBlackTurn, ArrayList<Move> choices) {
	    
		if(isBlackTurn) {
			System.out.println("Dark's Turn!");
		} else {
			System.out.println("White's Turn!");
		}
		if(isBlackTurn) {
			System.out.println("Here are all the dark moves: ");
		} else {
			System.out.println("Here are all the white moves: ");
		}

		for(int i=0; i<choices.size(); i++) {
			System.out.print("#" + (i+1) + ": (row " + (choices.get(i).getStartPos().getRow()+1) + ", col " + (choices.get(i).getStartPos().getCol()+1) + ")");
			System.out.println(" to (row " + (choices.get(i).getEndPos().getRow()+1) + ", col " + (choices.get(i).getEndPos().getCol()+1) + ")");
		}
		
		//Make some space:
		System.out.println();
		
	}
	
	public Move get1stMove(Game game, CheckersSetup setup, Board board, ArrayList<Move> choices) {
		Coordinate begin;
		
		ArrayList<Move> choicesFromClick;
		Move moveChoice = null;
		
		label:
		while(true) {
		
			begin = board.waitForClick();
			
			choicesFromClick = setup.getMovesFromStartLocation(choices, begin);
			
			if(choicesFromClick.isEmpty()) {
				System.out.println("You can't move that try again!");
				continue;
			}
			
			
			Coordinate end = board.waitForClick();
			
			//check if end location is legal:
			for(int i=0; i<choicesFromClick.size(); i++) {
				if(choicesFromClick.get(i).getEndPos().getCol() == end.getCol() && choicesFromClick.get(i).getEndPos().getRow() == end.getRow()) {
					moveChoice = choicesFromClick.get(i);
					break label;
					
					//If the player is trying to move the piece to an illegal place:
				} else if(i+1 == choicesFromClick.size() ) {
					System.out.println("You can't move to there! Try again!");
					continue label;
				}
				
			}
		}
		
		return moveChoice;
	}

	public Move getNextJumps(CheckersSetup setup, Board board, ArrayList<Move> multiJumpChoices) {
		Move moveChoice = null;
		boolean gotLegalJump = false;
		
		while( gotLegalJump == false) {
			Coordinate nextJump = board.waitForClick();
				
			//check if end location is legal:
			for(int i=0; i<multiJumpChoices.size(); i++) {
				if(multiJumpChoices.get(i).getEndPos().getCol() == nextJump.getCol() && multiJumpChoices.get(i).getEndPos().getRow() == nextJump.getRow()) {
					
					moveChoice = multiJumpChoices.get(i);
					gotLegalJump = true;
					
				    //If the player is trying to move the piece to an illegal place:
				} else if(i+1 == multiJumpChoices.size() ) {
					System.out.println("Just click to the location that you want to jump");
				}
				
			}
		}	
			
		
		return moveChoice;
	}//EOM
	
	public void setDark() {
		isDarkPlayer = true;
	}
	public void setWhite() {
		isDarkPlayer = false;
	}
	
	public boolean isDarkPlayer() {
		return isDarkPlayer;
	}
}
