package checkersAI;

import java.util.ArrayList;

import checkersSetup.CheckersSetup;
import checkersSetup.Move;

public class BasicCheckersAIFunctions {
	
	public static final double DARK_WIN_UTILITY = 9999;
	//SLOW
	
	public static void delayCompALittle() {
		try {
			//wait 2 seconds, then move? ... Give the human the chance to see the board before the comp moves?
			java.lang.Thread.sleep(500);
			
		} catch(Exception e) {
			System.err.println("Thread sleep didn't work.");
			e.printStackTrace();
			System.exit(1);
		}
	}
	

	public static void PrintDesiredMoves(ArrayList<Move> desiredMoves) { 
		System.out.println("Desired Moves:");
		Move firstMove = desiredMoves.get(0);
		Move lastMove = desiredMoves.get(desiredMoves.size() - 1);
		
		System.out.print("(row " + (firstMove.getStartPos().getRow()+1) + ", col " + (firstMove.getStartPos().getCol()+1) + ")");
		System.out.println(" to (row " + (lastMove.getEndPos().getRow()+1) + ", col " + (lastMove.getEndPos().getCol()+1) + ")");
		System.out.println("(done desire Moves)");
	}
	
	
	//post: Returns all checkers positions after 1 turn.
	// if there no force capture, just return the list of moves,
	// else return the list of attacks followed by null then the next list of attacks followed by null... and so on.
	public static ArrayList<checkersSetup.Turn> getAllPossiblePlayerOptionsFor1Turn(CheckersSetup setup, boolean isDarkPlayer) {
		ArrayList <checkersSetup.Turn>choices = new ArrayList<checkersSetup.Turn>();
		
		ArrayList<Move> firstAttackMoves = setup.getAttackMoves(isDarkPlayer);
		ArrayList<Move> moveList;
		if(firstAttackMoves.isEmpty()) {
			moveList = setup.getNonAttackMoves(isDarkPlayer);
			for(int i=0; i<moveList.size(); i++) {
				choices.add(new checkersSetup.Turn(moveList.get(i)));
			}
			
		} else {
			for(int i=0; i<firstAttackMoves.size(); i++) {
				choices.addAll(getChoiceFromJump(setup, firstAttackMoves.get(i)));
			}
		}
		
		return choices;
	}
	
	//Overloaded method made for convenience:
	//Post: Returns an array list of turns that can come out of the first jump.
	//This function uses recursion!
	// Also: this function should NOT change setup after it is done with it.
	public static ArrayList<checkersSetup.Turn> getChoiceFromJump(CheckersSetup setup, Move jump) {
		ArrayList<Move> moves = new ArrayList<Move>();
		return getChoiceFromJump(setup, moves, jump);
	}
	
	
	//pre: the setup did all the moves in the jumps and jump is the next jump to be done.
	//post: returns all the possible ways the computer can play out his turn given the jumps already done
	//	AND: setup is return returned to the way it was when it was called.
	public static ArrayList<checkersSetup.Turn> getChoiceFromJump(CheckersSetup setup, ArrayList<Move> jumps, Move jump) {
		//Make the move in the computers head and Add the jump to the list of jumps done:
		ArrayList<Move> nextJumpChoices = setup.makeMove(jump);
		jumps.add(jump);
		
		ArrayList<checkersSetup.Turn> ret = new ArrayList<checkersSetup.Turn>();
		
		if(nextJumpChoices.isEmpty()) {
			//No more jumping; the turn is over:
			ret.add(new checkersSetup.Turn(jumps));	
		} else {
			//There's still more jumping to do. Recurse:
			for(int i=0; i<nextJumpChoices.size(); i++) {
				ret.addAll(getChoiceFromJump(setup, jumps, nextJumpChoices.get(i)));
			}
		}
		
		//Undo the jump this method did to please 
		setup.reverseMove(jumps.get(jumps.size() - 1));
		jumps.remove(jumps.size()-1);
		
		return ret;
	}
	/*
	//Maybe valuing the king based on what he could do would be better in the future...
	//return the estimated advantage of the Dark player with a lame heuristic.
	public static double getEstimatedDarkAdvantage(CheckersSetup setup) {
		double result = 0.0;
		int piecesOnBoard[][] = setup.getPiecesOnBoard();
		
		for(int i=0; i<CheckersSetup.SIZE_OF_BOARD; i++) {
			for(int j=0; j<CheckersSetup.SIZE_OF_BOARD; j++) {
				if(CheckersSetup.isKing(piecesOnBoard[i][j])) {
					//this adds 1.6 to the result
					result += 0.8 * piecesOnBoard[i][j];
				} else {
					result += piecesOnBoard[i][j];
				}
			}
		}
		
		return result;
	}
	
	public static double getEstimatedUtilityOnEquiPos(CheckersSetup setup, boolean isDarkTurn) {
		int piecesOnBoard[][] = setup.getPiecesOnBoard();
		
		ArrayList<checkersSetup.Turn> turns = getAllPossiblePlayerOptionsFor1Turn(setup, isDarkTurn);
		if(turns.size() == 0) {
			if(isDarkTurn) {
				return -WIN_UTILITY;
			} else {
				return WIN_UTILITY;
			}
		} else if(turns.get(0).getMovesFor1Turn().get(0).isJump()) {
			double minUtility;
			double currentUtility;
			
			//TODO: TEST THIS!!
			if(isDarkTurn) {
				minUtility =  WIN_UTILITY;
				for(int i=0; i<turns.size(); i++) {
					setup.playTurn(turns.get(i));
					currentUtility = getEstimatedUtilityOnEquiPos(setup, !isDarkTurn);
					
					if(currentUtility < minUtility) {
						minUtility = currentUtility;
					}
					
					setup.reverseTurn(turns.get(i));
				}
			} else {
				minUtility =  -WIN_UTILITY;
				for(int i=0; i<turns.size(); i++) {
					setup.playTurn(turns.get(i));
					currentUtility = getEstimatedUtilityOnEquiPos(setup, !isDarkTurn);
					
					if(currentUtility > minUtility) {
						minUtility = currentUtility;
					}
					
					setup.reverseTurn(turns.get(i));
				}
			}
			//END TEST
			return minUtility;
		} else {
			return getEstimatedDarkAdvantage(setup);
		}
	}*/
	
	public static void printAllAIPlayerOptions(CheckersSetup setup, boolean isDarkPlayer) {
		ArrayList<checkersSetup.Turn> possibleOutcomes = getAllPossiblePlayerOptionsFor1Turn(setup, isDarkPlayer);
		
		ArrayList<Move> possibleCompMoves;
		Move firstMove;
		Move lastMove;
		//System.out.println("CompsTurn:");
		System.out.println("Manual Testing!:");
		System.out.println("Possible Piece movement:");
		for(int i=0; i<possibleOutcomes.size(); i++) {
			possibleCompMoves =possibleOutcomes.get(i).getMovesFor1Turn();
			firstMove = possibleCompMoves.get(0);
			lastMove = possibleCompMoves.get(possibleCompMoves.size() - 1);
			
			System.out.print("#" + (i+1) + ": (row " + (firstMove.getStartPos().getRow()+1) + ", col " + (firstMove.getStartPos().getCol()+1) + ")");
			System.out.println(" to (row " + (lastMove.getEndPos().getRow()+1) + ", col " + (lastMove.getEndPos().getCol()+1) + ")");
		}
	}
	
	public static void doSanityCheckMakeSureCompMoveIsRealMove(Move desiredMove, ArrayList<Move> moveChoices) {
		boolean good = false;
		for(int i=0; i<moveChoices.size(); i++) {
			if(desiredMove.isEqual(moveChoices.get(i))) {
				good = true;
				break;
			}
		}
		
		if(good == false) {
			System.out.println("***Error: The desired move the comp wants to make is NOT available.");
			System.out.println("Desired move: " + desiredMove);
			System.out.println("Choices:");
			for(int i=0; i<moveChoices.size(); i++) {
				System.out.println( moveChoices.get(i));
			}
			System.out.println("****");
			System.exit(1);
		}
	}
}
