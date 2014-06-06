package checkersAI;

import java.util.ArrayList;


import checkersSetup.CheckersSetup;
import checkersSetup.Game;
import checkersSetup.Move;
import checkersSetup.Turn;
import java.util.Scanner;

import pastPositionRecord.AnalysedPosition;

//************THIS FILE IS COMPLETED.
public class SimpleComputerAIwithTree implements checkersSetup.Player{


	Turn desiredTurn;
	int numJumpsAlreadyDoneThisTurn = 0;
	int depth;
	private checkersUtility.Utility utilityFunc;
	
	BalancedSearchTree viewedPos;
	int numPosRecorded = 0;
	
	//TODO:
	int numRecalls = 0;
	int numFailedRecalls = 0;
	//END TODO
	
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
	
	public boolean isDebug() {
		return debug;
	}
	
	//FOR TESTING ONLY:
	public checkersUtility.Utility getUtilityFunc() {
		return utilityFunc;
	}
	
	public SimpleComputerAIwithTree() {
		this(false, 6, new checkersUtility.SimpleUtility(), false);
	}
	
	public SimpleComputerAIwithTree(boolean deterministic, int depth, checkersUtility.Utility utility, boolean debug) {
		this.deterministic = deterministic;
		this.depth = depth;
		this.utilityFunc = utility;
		this.debug = debug;
	}
	
	
	
	public Move get1stMove(Game game, CheckersSetup setup, checkersSetup.Board board, ArrayList<Move> choices) {
		//BasicCheckersAIFunctions.delayCompALittle();
		
		//start fresh after every turn:
		viewedPos = null;
		//TESTING
		numPosRecorded = 0;
		//END TESTING
		
		numJumpsAlreadyDoneThisTurn = 0;
		
		BasicCheckersAIFunctions.printAllAIPlayerOptions(setup, isDarkPlayer);
		
		desiredTurn = getBestMoveBasedOnSearchOfDepthN(setup, isDarkPlayer, this.depth);
		
		ArrayList<Move> desiredMoves= desiredTurn.getMovesFor1Turn();
		
		 BasicCheckersAIFunctions.PrintDesiredMoves(desiredMoves);
		
		if(desiredMoves.get(0).isJump()) {
			numJumpsAlreadyDoneThisTurn++;
		}
		
		//TESTING (Display)
		System.out.println("Number of positions analysed for AI with Tree: " + numPosRecorded);
		if(viewedPos!=null) {
			System.out.println("Height of tree: " + viewedPos.getHeightOfTree());
		}
		System.out.println();
		//END TESTING
		
		return desiredMoves.get(0);
	}
	
	public Move getNextJumps(CheckersSetup setup, checkersSetup.Board board, ArrayList<Move> multiJumpChoices) {
		//BasicCheckersAIFunctions.delayCompALittle();
		
		//get the next move in the desired multi jump choice:
		Move move = desiredTurn.getMovesFor1Turn().get(numJumpsAlreadyDoneThisTurn);
		
		//Testing
		BasicCheckersAIFunctions.doSanityCheckMakeSureCompMoveIsRealMove(desiredTurn.getMovesFor1Turn().get(numJumpsAlreadyDoneThisTurn), multiJumpChoices);
		//Testing
		
		numJumpsAlreadyDoneThisTurn++;
		
		return move;
	}
	
	//FOR TESTING:
	private Turn favTurn[];
	private int numFavMoves;
	
	//pre: this position MUST be at the beginning of a turn
	//post: returns the best move assuming that the opponent plays perfectly.
		//This search only looks (depth) moves ahead and is very inefficient.
	public checkersSetup.Turn getBestMoveBasedOnSearchOfDepthN(CheckersSetup setup, boolean isDarkPlayer, int curdepth) {
		
		int numAlternativelyGoodMoves=0;
		
		if(curdepth > 0) {
			
			ArrayList<checkersSetup.Turn> turnChoices =  BasicCheckersAIFunctions.getAllPossiblePlayerOptionsFor1Turn(setup, isDarkPlayer);
			
			favTurn = new Turn[turnChoices.size()];
			
			favTurn[0] = turnChoices.get(0);
			
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
			
			//TODO
			//opUtilityOfLastMove = -opWorstPosUtil;
		} else {
			System.out.println("Error: the depth should be bigger than 0!");
			System.exit(1);
			//stub to stop java from complaining:
			favTurn = new Turn[0];
		}
		
		//if AI is NOT deterministic AND
		//	there's several choices that seem similarly good, the AI randomly choses a move 
		if(numAlternativelyGoodMoves > 0 && this.deterministic == false) {
			System.out.println("aitree is randomly choosing beetween " + (numAlternativelyGoodMoves+1) + " ways to do his turn");
			numFavMoves = numAlternativelyGoodMoves+1;
			return favTurn[(int)(Math.random()*(numAlternativelyGoodMoves+1))];
		} else {
			numFavMoves = 1;
			return favTurn[0];
		}
		
	}
	
	//pre: this position MUST be at the beginning of a turn.
	//post: returns the utility of the checkers position for whoever's turn it happens to be.
	public double getEstUtilityOfPosition(CheckersSetup setup, boolean isDarkPlayerCurrentTurn, int curdepth) {
		double ret;
		
		//this key identifies the setup of the board and who's turn it is in 3 numbers:
		int key[] = AnalysedPosition.makeKey(setup, isDarkPlayerCurrentTurn);
		
		//ret pos!!!!
		//Check to see if the position has already been analysed
		Double alreadyFoundUtility = getUtilityIfAlreadyFound(key, curdepth);
		
		if(alreadyFoundUtility != null) {
				return new Double(alreadyFoundUtility).doubleValue();
		}
		//end check
		
		
		//Find the utility from scratch and recursion:
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
			//End finding utility from scratch
			
			//TESTING SLOW
			if(debug) {
				sanityTestKeyBeforeAndAfter(setup, isDarkPlayerCurrentTurn, key);
			}
			//END TESTING
			
			//ADD analysed position to tree:
			addPositionToListOfAnalysedPositions(key, isDarkPlayerCurrentTurn, curdepth, ret);
			
			return ret;
		}
	}
	
	public Double getUtilityIfAlreadyFound(int key[], int curdepth) {
		
		if(viewedPos != null) {
			AnalysedPosition alreadyAnalysedPos = viewedPos.search(key);
			if(alreadyAnalysedPos != null && alreadyAnalysedPos.getDepth() >= curdepth) {
					return new Double(alreadyAnalysedPos.getUtility());
			}
		}
		return null;
	}
	
	//Adds the position and it's estimated utility to the table.
	// If the position is already in the table (which will happen sometimes), It will either update.
	public void addPositionToListOfAnalysedPositions(int key[], boolean isDarkPlayerCurrentTurn, int curdepth, double utility) {
		
		boolean alreadyDidPos = false;
		AnalysedPosition alreadyAnalysedPos = null;
		
		//check if the position has been inserted after doing the recursion:
		if(viewedPos != null) {
			alreadyAnalysedPos = viewedPos.search(key);
			if(alreadyAnalysedPos != null) {
				alreadyDidPos = true;
			}
		}
		
		//If the position is not already done:
		if(alreadyDidPos == false) {
				AnalysedPosition pos = new AnalysedPosition(curdepth, isDarkPlayerCurrentTurn, utility, key);
				viewedPos = BalancedSearchTree.addValueToTree(viewedPos, pos);
				
				//FOR TEST PURPOSES
				numPosRecorded++;
				//TESTING SLOW
				if(debug) {
					sanityCheckNumNodes();
					viewedPos.sanityCheckSorted();
				}
				//END TESTING
	
		//if we only need to update:
		} else {
				alreadyAnalysedPos.updatePositionWithImprovedCalculation(curdepth, utility);
		}
		
	}
	
	
	//TESTING:
	//returns the whole desired turn at once for testing:
	public Turn getDesiredTurn() {
		return desiredTurn;
	}
	
	//Testing: make sure the number of nodes in the tree is the same as the amount put in:
	public void sanityCheckNumNodes() {
		int sanity = viewedPos.sanityCheckGetNumberOfNodes();
		if(sanity != numPosRecorded) {
			System.err.println("ERROR: the number of nodes in the tree is not equal to the number of nodes added. #Added: " + numPosRecorded + " number received: " + sanity);
			System.exit(1);
		}
	}
	
	//testing: MAKE SURE THAT THE KEY STAYS THE SAME!!!
	public void sanityTestKeyBeforeAndAfter(CheckersSetup setup, boolean isDarkPlayerCurrentTurn, int key[]) {
		 int key2[] = AnalysedPosition.makeKey(setup, isDarkPlayerCurrentTurn);
			int compare = BalancedSearchTree.compare(key, key2);
			if(compare != 0) {
				System.out.println("ERROR: the board changes through the getEstUtilityOfPosition function.");
				System.exit(1);
			}
	}
	
	//testing: make sure that the position didn't suddenly get on the list. 
	public void sanityTestPositionDidntDissapearOffTree(AnalysedPosition alreadyAnalysedPos, int key[], boolean alreadyDidPos, int curdepth) {
	//note: this should not happen within this function.
		if(viewedPos != null) {
			alreadyAnalysedPos = viewedPos.search(key);
			if(alreadyAnalysedPos != null && alreadyDidPos == false) {
				if(alreadyAnalysedPos.getDepth() >= curdepth) {
					System.out.println("ERROR: Position should have been viewable earlier");
					System.exit(1);
				}
			}
		}
	}
	
	public int getNumPositionsRecorded() {
		return numPosRecorded;
	}
	
	Scanner in = new Scanner(System.in);
	public void sanityCheckTurnMoveChoiceIsInFavTurnArray(Turn turnChoice) {
		for(int i=0; i<numFavMoves; i++) {
			if(favTurn[i].isEqual(turnChoice)) {
				//good
				return;
			}
		}
		System.out.println("********");
		System.out.println("ERROR: The turn choice was not found in the list of fav turns in SimpleComputerAIwithTree!");
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
	
	//END TESTING
	
}