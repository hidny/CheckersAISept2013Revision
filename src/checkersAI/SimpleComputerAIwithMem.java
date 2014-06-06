package checkersAI;

import java.util.ArrayList;
import java.lang.Double;

import checkersSetup.CheckersSetup;
import checkersSetup.Game;
import checkersSetup.Move;
import checkersSetup.Turn;

import java.util.Scanner;

import pastPositionRecord.AnalysedPosition;

//************THIS FILE IS COMPLETED.
public class SimpleComputerAIwithMem implements checkersSetup.Player{

	//(I could watch 2 comps duke it out!) DONE!
	
	private Turn desiredTurn;
	private int numJumpsAlreadyDoneThisTurn = 0;
	private int depth;
	private checkersUtility.Utility utilityFunc;
	
	private ArrayList <AnalysedPosition>viewedPos;
	
	private int numFavMoves = 0;
	private Turn favTurn[];
	
	
	
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
	
	public int getDepth() {
		return depth;
	}
	
	public SimpleComputerAIwithMem() {
		this(false, 6, new checkersUtility.SimpleUtility(), false);
	}
	
	public SimpleComputerAIwithMem(boolean deterministic, int depth, checkersUtility.Utility utility, boolean debug) {
		this.deterministic = deterministic;
		this.depth = depth;
		this.utilityFunc = utility;
		this.debug = debug;
	}
	
	public Move get1stMove(Game game, CheckersSetup setup, checkersSetup.Board board, ArrayList<Move> choices) {
		//BasicCheckersAIFunctions.delayCompALittle();
		
		//BasicCheckersAIFunctions.printAllPlayerOptions(setup, isDarkPlayer);
		
		//start fresh after every turn:
		viewedPos = new ArrayList<AnalysedPosition>();
		numJumpsAlreadyDoneThisTurn = 0;
		
		desiredTurn = getBestMoveBasedOnSearchOfDepthN(setup, isDarkPlayer, this.depth);
		
		ArrayList<Move> desiredMoves= desiredTurn.getMovesFor1Turn();
		
		 BasicCheckersAIFunctions.PrintDesiredMoves(desiredMoves);
		
		 //TESTING:
		 if(debug) {
			 sanityCheckNoDups();
			 System.out.println("Number of recorded positions for AIwithMem: " + viewedPos.size());
		 }
		 //END TESTING
		 
		if(desiredMoves.get(0).isJump()) {
			numJumpsAlreadyDoneThisTurn++;
		}
		
		return desiredMoves.get(0);
	}
	
	
	public Move getNextJumps(CheckersSetup setup, checkersSetup.Board board, ArrayList<Move> multiJumpChoices) {
		//BasicCheckersAIFunctions.delayCompALittle();
		
		//get the next move in the desired multi jump choice:
		Move move = desiredTurn.getMovesFor1Turn().get(numJumpsAlreadyDoneThisTurn);
		
		//TESTING
		BasicCheckersAIFunctions.doSanityCheckMakeSureCompMoveIsRealMove(desiredTurn.getMovesFor1Turn().get(numJumpsAlreadyDoneThisTurn), multiJumpChoices);
		//END TESTING
		
		numJumpsAlreadyDoneThisTurn++;
		
		return move;
	}
	
	
	//pre: this position MUST be at the begining of a turn
	//post: returns the best move assuming that the opponent plays perfectly.
		//This search only looks (depth) moves ahead and is very inefficient.
	public checkersSetup.Turn getBestMoveBasedOnSearchOfDepthN(CheckersSetup setup, boolean isDarkPlayer, int curdepth) {
		numFavMoves=0;
		
		if(curdepth > 0) {
			
			ArrayList<checkersSetup.Turn> turnChoices =  BasicCheckersAIFunctions.getAllPossiblePlayerOptionsFor1Turn(setup, isDarkPlayer);
			
			favTurn = new Turn[turnChoices.size()];			
			favTurn[0] = turnChoices.get(0);
			numFavMoves = 1;
			
			setup.playTurn(turnChoices.get(0));
			//note: op = OPPONENT!
			double opWorstPosUtil = getEstUtilityOfPosition(setup, !isDarkPlayer, curdepth-1);
			setup.reverseTurn(turnChoices.get(0));
			
			double opPosUtil;
		
			for(int i=1; i<turnChoices.size(); i++) {
				setup.playTurn(turnChoices.get(i));
				
				opPosUtil = getEstUtilityOfPosition(setup, !isDarkPlayer, curdepth-1);
				
				if(opPosUtil < opWorstPosUtil) {
					opWorstPosUtil = opPosUtil;
					favTurn[0] = turnChoices.get(i);
					numFavMoves = 1;
				
				} else if(opPosUtil == opWorstPosUtil) {
					numFavMoves++;
					favTurn[numFavMoves - 1] = turnChoices.get(i);
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
			numFavMoves = 1;
			System.exit(1);
		}
		
		//if AI is NOT deterministic AND
		//	there's several choices that seem similarly good, the AI randomly choses a move 
		if(numFavMoves > 1 && this.deterministic == false) {
			System.out.println("AIMEM is randomly choosing beetween " + (numFavMoves) + " ways to do his turn");
			return favTurn[(int)(Math.random()*(numFavMoves))];
		} else {
			return favTurn[0];
		}
		
	}
	
	//pre: this position MUST be at the beginning of a turn.
	//post: returns the utility of the checkers position for whoever's turn it happens to be.
	double getEstUtilityOfPosition(CheckersSetup setup, boolean isDarkPlayerCurrentTurn, int curdepth) {
		double ret;
		
		Double utilityContainer = getAlreadyDoneCalcIfApplicable(setup, isDarkPlayerCurrentTurn, curdepth);
		
		if(utilityContainer != null) {
			return utilityContainer.doubleValue();
		}
		
		
		if(curdepth ==0) {
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
			double opWorstPosUtil = getEstUtilityOfPosition(setup, !isDarkPlayerCurrentTurn, curdepth-1);
			setup.reverseTurn(turnChoices.get(0));
			
			double currOpPosUtil;
		
			for(int i=1; i<turnChoices.size(); i++) {
				setup.playTurn(turnChoices.get(i));
				
				currOpPosUtil = getEstUtilityOfPosition(setup, !isDarkPlayerCurrentTurn, curdepth-1);
				
				if(currOpPosUtil < opWorstPosUtil) {
					opWorstPosUtil = currOpPosUtil;
				}
				
				setup.reverseTurn(turnChoices.get(i));
			}
			
			ret = -opWorstPosUtil;
			
			//ADD POSITION TO LIST:
			addPositionToListOfAnalysedPositions(setup, isDarkPlayerCurrentTurn, curdepth, ret);
			
			return ret;
		}
	}
	
	//Adds the position and it's estimated utility to the table.
	// If the position is already in the table (which will happen sometimes), It will either update.
	public void addPositionToListOfAnalysedPositions(CheckersSetup setup, boolean isDarkPlayerCurrentTurn, int curdepth, double utility) {
		int index = getIndexOfAlreadySeenPos(setup, isDarkPlayerCurrentTurn);
		
		//update position:
		if(index != -1) {
			
			viewedPos.get(index).updatePositionWithImprovedCalculation(curdepth, utility);
			//Update position if it's already in the list: (if we're doing some complex utility estimation algorithm...)
			
		//Add position:
		} else {
			viewedPos.add(new AnalysedPosition(setup, curdepth, isDarkPlayerCurrentTurn, utility));
		}
	}
	
	
	public Double getAlreadyDoneCalcIfApplicable(CheckersSetup setup, boolean isDarkPlayerCurrentTurn, int curdepth) {
		Double ret = null;
		int index = getIndexOfAlreadySeenPos(setup, isDarkPlayerCurrentTurn);
		
		if(index != -1) {
			if(curdepth <= viewedPos.get(index).getDepth()) {
				ret = new Double(viewedPos.get(index).getUtility());
			}
		}
		return ret;
	}
	
	public int getIndexOfAlreadySeenPos(CheckersSetup setup, boolean isDarkPlayerCurrentTurn) {
		for(int i=0; i<viewedPos.size(); i++) {
			if(viewedPos.get(i).isSamePosition(setup, isDarkPlayerCurrentTurn)) {
				return i;
			}
		}
		return -1;
	}
	
	//TESTING:
	//NOTE: this function takes LONG!
	public void sanityCheckNoDups() {
		System.out.println("number of position analysed(AIMem): " + viewedPos.size());
		System.out.println("Sanity check no dups:");
		for(int i=0; i<viewedPos.size(); i++) {
			for(int j=i+1; j<viewedPos.size(); j++) {
				if(viewedPos.get(i).isSamePosition(viewedPos.get(j))) {
					System.out.println("ERROR: there are duplicate positions in the list of positions!");
					System.exit(1);
				}
			}
		}
		System.out.println("good");
	}
	
	
	private Scanner in = new Scanner(System.in);
	public void sanityCheckTurnMoveChoiceIsInFavTurnArray(Turn turnChoice) {
		for(int i=0; i<numFavMoves; i++) {
			if(favTurn[i].isEqual(turnChoice)) {
				//good
				return;
			}
		}
		System.out.println("********");
		System.out.println("ERROR: The turn choice was not found in the list of fav turns in SimpleComputerAIwithMem!");
		for(int i=0; i<numFavMoves; i++) {
			System.out.println("Fav turn " + i + ":");
			System.out.println(favTurn[i]);
		}
		
		System.out.println("turnChoice given to sanity check:");
		System.out.println(turnChoice);
		System.out.println("********");
		//DON'T exit because the state of the board is important:
		in.next();
		
	}
	
	private Turn[] getFavTurn() {
		return favTurn;
	}
	
	public int getNumPositionsRecorded() {
		return viewedPos.size();
	}
//END TESTING
}