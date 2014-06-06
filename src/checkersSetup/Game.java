package checkersSetup;

/**
 * This class manages the interactions between the different pieces of 
 * the game: the Board, the Daleks, and the Doctor. It determines when
 * the game is over and whether the Doctor won or lost.
 * 
 * @author CS133 Staff
 */

  // Fix up your version from Assignment 2.  You will
  // have to alter it since you will no longer have the
  // getClick() method.  Instead, your game must interact
  // with the board using the BoardListener interface
  // provided in Assignment 5.

/**This class manages the interactions between the different pieces of 
 * the game: the Board, the Daleks, and the Doctor. It determines when
 * the game is over and whether the Doctor won or lost.
 * @author Michael Tardibuono
 */

//TODO: make the piece that the comp choose to move very clear

import java.util.*;

import pastPositionRecord.AnalysedPositionForDraw;
import checkersAI.BalancedSearchTree;
//Math.random()
//TODO: ADD THE DRAW CONDITION

public class Game
{
	
	private Player dark, white;
	
	public ArrayList<Move> moveList;
	
	//TODO: after a capture, set this tree to null...
	private BalancedSearchTree positionsAttainedToCheckForDraw;
    
	
  public Player playGame(Player dark, Player white)
  {
	  //Setting or reseting the state vars:
	this.dark = dark;
    this.white = white; 
    this.dark.setDark();
    this.white.setWhite();
    this.moveList = new ArrayList<Move>();
    this.positionsAttainedToCheckForDraw = null;
    
    
    CheckersSetup setup = new CheckersSetup(8, 8);
    Board board = new Board(8, 8);
    
    /*The game keeps playing until the game is over.*/
    
    ArrayList<Move> choices = new ArrayList<Move>();
    
    boolean isDarkTurn = true;
    boolean draw = false;
    //Done setting/resetting the state vars.
    
    Player currentPlayer;
    //Every iteration of the loop is a turn.
    do {
    	board.displayBoard(setup);
    	
    	if(isADraw(setup, isDarkTurn)) {
    		draw = true;
    		break;
    	}
    	
    	currentPlayer = getPlayer(isDarkTurn);
    	choices = setup.getMoves(isDarkTurn);
    	
    	if(getPlayer(isDarkTurn) instanceof Human) {    		
    		
    		Human.printMoveDetails(isDarkTurn, choices);	
    	} else {
    		System.out.println("Computer's Turn:");
    		//printAllPossibleMoves(setup, isDarkTurn);
    		
    	}
    	
    	//If there's no choices left for moves for 1 player, game over.
    	if(choices.isEmpty()) {
    		break;
    	}
    	getPlayerToMove(setup, board, choices, currentPlayer);
    	
    	
    	isDarkTurn = !isDarkTurn;
    } while( true);
    
    boolean winner = !isDarkTurn;
    
    if(getPlayer(true) instanceof Human || getPlayer(false) instanceof Human) {
    	System.out.println("GAME OVER!\nPress anything to continue.");
    	in.next();
    }
    board.closeWindow();
    
    //reverseAllMoves(setup, isDarkTurn, board);
    
    if(draw == false) {
    	return currentPlayer = getPlayer(winner);
    } else {
    	return null;
    }
 }

void getPlayerToMove(CheckersSetup setup, Board board, ArrayList<Move> choices, Player currentPlayer) {
   
		Move nextmove = currentPlayer.get1stMove(this, setup, board, choices);
	
		//TESTING (SLOW DOWN...)
		if(currentPlayer instanceof checkersAI.SimpleComputerAIwithTree && ((checkersAI.SimpleComputerAIwithTree)currentPlayer).isDebug()) {
			compareAIwithTreevsAIMem(setup, board, choices, currentPlayer);
		}
		
		//TESTING
		
		Move lastMove;
		//Make the next move:
		
		ArrayList<Move> nextJumpChoices =setup.makeMove(nextmove);
		
		board.displayBoard(setup);
		
		//Add last move to move list:
		lastMove = nextmove;
		moveList.add(lastMove);
		
		
		//While loop that keeps asking the player to move until he/she could keep jumping:
			//If a piece gets promoted, the turn is over!
			//FIXME: the check for promotion is redundant because CheckersSetup.makeMove() already does that...
		while(!lastMove.gotPromotedThisMove() && nextJumpChoices.isEmpty() == false) {
			
			if(currentPlayer instanceof Human) {
				//print move detail. 1st param determines the colour of the mover.
				Human.printMoveDetails(currentPlayer.isDarkPlayer(), nextJumpChoices);
				//Repaint the board appropriately:
			}
			
			nextmove = currentPlayer.getNextJumps(setup, board, nextJumpChoices);
		    
			
			nextJumpChoices = setup.makeMove(nextmove);
		    	    
		    board.displayBoard(setup);
		    
		    //Add last move to move list:
		    lastMove = nextmove;  
		    moveList.add(lastMove);
			
	    }  

}


	private Player getPlayer(boolean isDark) {
		if(isDark) {
			return dark;
		} else {
			return white;
		}
	}
	
	//If the same position is reached 3 times, it's a draw!
	public boolean isADraw(CheckersSetup setup, boolean isDarkTurn) {
		AnalysedPositionForDraw currentPos;
	    AnalysedPositionForDraw searchedPos;
	    
	    currentPos = new AnalysedPositionForDraw(setup, 0, isDarkTurn, 0.0);
	    
		if(positionsAttainedToCheckForDraw != null) {
    		searchedPos = (AnalysedPositionForDraw)positionsAttainedToCheckForDraw.search(currentPos.getKey());
    		if(searchedPos == null) {
    			positionsAttainedToCheckForDraw = BalancedSearchTree.addValueToTree(positionsAttainedToCheckForDraw, currentPos);
    		} else {
    			searchedPos.addOneOccurance();
    			if(searchedPos.isDraw()) {
    				return true;
    			}
    		}
    	} else {
    		positionsAttainedToCheckForDraw = BalancedSearchTree.addValueToTree(positionsAttainedToCheckForDraw, currentPos);
    	}
		
		return false;
	}
	
	public BalancedSearchTree getPositionsAttainedToCheckForDraw() {
		return this.positionsAttainedToCheckForDraw;
	}
	
	//TESTING:
	Scanner in = new Scanner(System.in);
	public void reverseAllMoves(CheckersSetup setup, boolean isDarkTurn, Board board) {
		AnalysedPositionForDraw lastPos = new AnalysedPositionForDraw(setup, isDarkTurn);
		AnalysedPositionForDraw currPos;
		System.out.println("Just for fun, lets reverse the moves and pause when it's the same Pos as the last position:");
		System.out.println("Please enter anything to comply");
		in.next();
		
		for(int i=0; i< moveList.size(); i++) {
			System.out.println("move: " + (moveList.size()-i));
			
			
			checkersAI.BasicCheckersAIFunctions.delayCompALittle();
			setup.reverseMove(moveList.get(moveList.size()-1-i));
			
			currPos= new AnalysedPositionForDraw(setup, isDarkTurn);
			
			board.displayBoard(setup);
			
			if(currPos.isSamePosition(lastPos)) {
				in.next();
			}
			
		}	 
	}

	public void printAllPossibleMoves(CheckersSetup setup, boolean isDarkTurn) {
		System.out.println("###Testing All possible moves:");
		ArrayList<Move> moves = setup.getAllPosibleMovesForCurrentPlayer(isDarkTurn);
		
		
		int numJumps= 0;
		if(moves.size() > 0 && moves.get(0)!= null && moves.get(0).isJump()) {
			numJumps++;
			System.out.println("Set of Jumps " + numJumps + ":");
		}
		
		for(int i=0; i<moves.size(); i++) {
			if(moves.get(i) != null) {
				System.out.print("#" + (i+1) + ": (row " + (moves.get(i).getStartPos().getRow()+1) + ", col " + (moves.get(i).getStartPos().getCol()+1) + ")");
				System.out.println(" to (row " + (moves.get(i).getEndPos().getRow()+1) + ", col " + (moves.get(i).getEndPos().getCol()+1) + ")");
			} else {
				System.out.println();
				
				numJumps++;
				System.out.println("Set of Jumps " + numJumps + ":");
			}
		}
		System.out.println("####Done test.");
	}
	
	public void compareAIwithTreevsAIMem(CheckersSetup setup, Board board, ArrayList<Move> choices, Player currentPlayer) {
		if(currentPlayer instanceof checkersAI.SimpleComputerAIwithTree) {
    		Player sanityPlayer = new checkersAI.SimpleComputerAIwithMem(false, ((checkersAI.SimpleComputerAIwithTree)currentPlayer).getDepth(), ((checkersAI.SimpleComputerAIwithTree) currentPlayer).getUtilityFunc(), false);
    		
    		if(currentPlayer.isDarkPlayer()) {
    			sanityPlayer.setDark();
    		} else {
    			sanityPlayer.setWhite();
    		}
    		sanityPlayer.get1stMove(this, setup, board, choices);
    		
    		if(	((checkersAI.SimpleComputerAIwithMem)sanityPlayer).getNumPositionsRecorded() != ((checkersAI.SimpleComputerAIwithTree) currentPlayer).getNumPositionsRecorded()) {
    			System.out.println("ERROR: the number of nodes recorded for the tree is not equal to the number of nodes recorded for AI with mem");
    			System.exit(1);
    		}
    		
    		((checkersAI.SimpleComputerAIwithMem)sanityPlayer).sanityCheckTurnMoveChoiceIsInFavTurnArray(((checkersAI.SimpleComputerAIwithTree) currentPlayer).getDesiredTurn());
    		
		} else {
			System.out.println("ERROR: invalid use of sanity check.");
			System.exit(1);
		}
	}
	
	
	//FIXME: this thing doesn't work because it the AIs don't record the same amount of positions!
	/*public void compareAIwithAlphaBetavsTree(CheckersSetup setup, Board board, ArrayList<Move> choices, Player currentPlayer) {
		if(currentPlayer instanceof checkersAI.ComputerAIAlphaBetaPrune) {
			Player sanityPlayer = new checkersAI.SimpleComputerAIwithTree(false, ((checkersAI.ComputerAIAlphaBetaPrune)currentPlayer).getDepth(), ((checkersAI.ComputerAIAlphaBetaPrune) currentPlayer).getUtilityFunc(), false);
    		
    		if(currentPlayer.isDarkPlayer()) {
    			sanityPlayer.setDark();
    		} else {
    			sanityPlayer.setWhite();
    		}
    		sanityPlayer.get1stMove(this, setup, board, choices);
    		
    		((checkersAI.SimpleComputerAIwithTree)sanityPlayer).sanityCheckTurnMoveChoiceIsInFavTurnArray(((checkersAI.ComputerAIAlphaBetaPrune) currentPlayer).getDesiredTurn());
		
		} else {
			System.out.println("ERROR: invalid use of sanity check.(2)");
			System.exit(1);
		}
	}*/
	
	
	//END TESTING
}
  
