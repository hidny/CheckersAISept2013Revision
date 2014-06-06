package checkersAI;

import java.util.ArrayList;

import checkersSetup.CheckersSetup;
import checkersSetup.Game;
import checkersSetup.Move;
import checkersSetup.Turn;

//************THIS FILE IS COMPLETED.

//TODO: hard copy or soft copy?
	//Solution: hard copy for all.

public class SimpleComputerAI implements checkersSetup.Player{

	//TODO:Computers should have a name
	
	//Note: the AI doesn't actually have to use the board!
	//but it implements a funciton that uses a board. (sept 2011)
	
	Turn desiredTurn;
	int numJumpsAlreadyDoneThisTurn = 0;
	int depth;
	checkersUtility.Utility utilityFunc;
	
	//Default both to false:
	boolean isDarkPlayer = false;
	boolean deterministic = false;
	boolean debug = false;

	
	public void setDark() {
		isDarkPlayer = true;
	}
	public void setWhite() {
		isDarkPlayer = false;
	}
	
	public boolean isDarkPlayer() {
		return isDarkPlayer;
	}
	
	public SimpleComputerAI() {
		this(false, 6, new checkersUtility.SimpleUtility(), false);
	}
	
	public SimpleComputerAI(boolean deterministic, int depth, checkersUtility.Utility utility, boolean debug) {
		this.deterministic = deterministic;
		this.depth = depth;
		this.utilityFunc = utility;
		this.debug = debug;
	}
	
	public Move get1stMove(Game game, CheckersSetup setup, checkersSetup.Board board, ArrayList<Move> choices) {
		//BasicCheckersAIFunctions.delayCompALittle();
		
		numJumpsAlreadyDoneThisTurn = 0;
		
		BasicCheckersAIFunctions.printAllAIPlayerOptions(setup, isDarkPlayer);
		
		desiredTurn = getBestMoveBasedOnSearchOfDepthN(setup, isDarkPlayer, this.depth);
		
		ArrayList<Move> desiredMoves= desiredTurn.getMovesFor1Turn();
		
		 BasicCheckersAIFunctions.PrintDesiredMoves(desiredMoves);
		
		if(desiredMoves.get(0).isJump()) {
			numJumpsAlreadyDoneThisTurn++;
		}
		return desiredMoves.get(0);
	}
	
	
	public Move getNextJumps(CheckersSetup setup, checkersSetup.Board board, ArrayList<Move> multiJumpChoices) {
		//BasicCheckersAIFunctions.delayCompALittle();
		
		//get the next move in the desired multi jump choice:
		Move move = desiredTurn.getMovesFor1Turn().get(numJumpsAlreadyDoneThisTurn);
		
		 BasicCheckersAIFunctions.doSanityCheckMakeSureCompMoveIsRealMove(desiredTurn.getMovesFor1Turn().get(numJumpsAlreadyDoneThisTurn), multiJumpChoices);
		
		numJumpsAlreadyDoneThisTurn++;
		
		return move;
	}
	
	
	//post: returns the best move assuming that the opponent plays perfectly.
		//This search only looks (depth) moves ahead and is very inefficient.
	public checkersSetup.Turn getBestMoveBasedOnSearchOfDepthN(CheckersSetup setup, boolean isDarkPlayer, int depth) {
		Turn favTurn[];
		int numAlternativelyGoodMoves=0;
		
		if(depth > 0) {
			
			ArrayList<checkersSetup.Turn> turnChoices =  BasicCheckersAIFunctions.getAllPossiblePlayerOptionsFor1Turn(setup, isDarkPlayer);
			
			favTurn = new Turn[turnChoices.size()];
			
			//TESTING:
			/*System.out.println("Computer's turn choices:");
			for(int i=0; i<turnChoices.size(); i++) {
				System.out.println(turnChoices.get(i));
			}*/
			//END TESTING
			
			favTurn[0] = turnChoices.get(0);
			
			setup.playTurn(turnChoices.get(0));
			//note: op = OPPONENT!
			double opWorstPosUtil = getEstUtilityOfPosition(setup, !isDarkPlayer, depth-1);
			setup.reverseTurn(turnChoices.get(0));
			
			double opPosUtil;
		
			for(int i=1; i<turnChoices.size(); i++) {
				setup.playTurn(turnChoices.get(i));
				
				opPosUtil = getEstUtilityOfPosition(setup, !isDarkPlayer, depth-1);
				
				if(opPosUtil < opWorstPosUtil) {
					opWorstPosUtil = opPosUtil;
					favTurn[0] = turnChoices.get(i);
					numAlternativelyGoodMoves = 0;
				
				} else if(opPosUtil == opWorstPosUtil) {
					numAlternativelyGoodMoves++;
					favTurn[numAlternativelyGoodMoves] = turnChoices.get(i);
				}
				
				setup.reverseTurn(turnChoices.get(i));
			}
			if(opWorstPosUtil <= -BasicCheckersAIFunctions.DARK_WIN_UTILITY) {
				System.out.println("YOU ARE FUCKED!!");
			}
		} else {
			System.out.println("Error: the depth should be bigger than 0!");
			//stub to stop java from complaining:
			favTurn = new Turn[0];
		}
		
		//if AI is NOT deterministic AND
		//	there's several choices that seem similarly good, the AI randomly choses a move 
		if(numAlternativelyGoodMoves > 0 && this.deterministic == false) {
			System.out.println("AI is randomly choosing beetween " + (numAlternativelyGoodMoves+1) + " ways to do his turn");
			return favTurn[(int)(Math.random()*(numAlternativelyGoodMoves+1))];
		} else {
			return favTurn[0];
		}
		
	}
	
	//post: returns the utility of the checkers position for whoever's turn it happens to be.
	double getEstUtilityOfPosition(CheckersSetup setup, boolean isDarkPlayerCurrentTurn, int depth) {
		if(depth ==0) {
			if(isDarkPlayerCurrentTurn) {
				return utilityFunc.getEstimatedUtilityForDarkPlayer(setup, isDarkPlayerCurrentTurn);
			} else {
				return -utilityFunc.getEstimatedUtilityForDarkPlayer(setup, isDarkPlayerCurrentTurn);
			}
		} else {
			ArrayList<checkersSetup.Turn> turnChoices =  BasicCheckersAIFunctions.getAllPossiblePlayerOptionsFor1Turn(setup, isDarkPlayerCurrentTurn);
			
			if(turnChoices.size() == 0) {
				//horrible position:
				return -BasicCheckersAIFunctions.DARK_WIN_UTILITY;
			}
			
			setup.playTurn(turnChoices.get(0));
			//op = OPPONENT!
			double opWorstPosUtil = getEstUtilityOfPosition(setup, !isDarkPlayerCurrentTurn, depth-1);
			setup.reverseTurn(turnChoices.get(0));
			
			double opPosUtil;
		
			for(int i=1; i<turnChoices.size(); i++) {
				setup.playTurn(turnChoices.get(i));
				
				opPosUtil = getEstUtilityOfPosition(setup, !isDarkPlayerCurrentTurn, depth-1);
				
				if(opPosUtil < opWorstPosUtil) {
					opWorstPosUtil = opPosUtil;
				}
				
				setup.reverseTurn(turnChoices.get(i));
			}
			
			return -opWorstPosUtil;
		}
	}
	
}