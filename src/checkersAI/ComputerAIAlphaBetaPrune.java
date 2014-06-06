package checkersAI;

import java.util.ArrayList;

import pastPositionRecord.AnalysedPosition;

//TODO:

import checkersSetup.CheckersSetup;
import checkersSetup.Game;
import checkersSetup.Move;
import checkersSetup.Turn;

public class ComputerAIAlphaBetaPrune implements checkersSetup.Player{


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
		boolean playerColour = false;
		boolean deterministic = false;
		boolean debug = false;
		
		public void setDark() {
			playerColour = true;
		}
		public void setWhite() {
			playerColour = false;
		}
		
		public boolean isDarkPlayer() {
			return playerColour;
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
		
		public ComputerAIAlphaBetaPrune() {
			this(false, 6, new checkersUtility.SimpleUtility(), false);
		}
		
		public ComputerAIAlphaBetaPrune(boolean deterministic, int depth, checkersUtility.Utility utility, boolean debug) {
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
			
			BasicCheckersAIFunctions.printAllAIPlayerOptions(setup, playerColour);
			
			desiredTurn = alphaBetaSearch(setup, this.depth);
			
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
			BasicCheckersAIFunctions.delayCompALittle();
			
			//get the next move in the desired multi jump choice:
			Move move = desiredTurn.getMovesFor1Turn().get(numJumpsAlreadyDoneThisTurn);
			
			//Testing
			BasicCheckersAIFunctions.doSanityCheckMakeSureCompMoveIsRealMove(desiredTurn.getMovesFor1Turn().get(numJumpsAlreadyDoneThisTurn), multiJumpChoices);
			//Testing
			
			numJumpsAlreadyDoneThisTurn++;
			
			return move;
		}
		
		
		//A useful approximation:
		public static int PLUS_INF = 100000;
		public static int MINUS_INF = -PLUS_INF;
		public static int NOT_DETERMINED = 52809;
		
		//TODO: make a function that ajusts the utility to end game faster or lose slow
		
		//TODO: change this:
		
		//pre: this position MUST be at the beginning of a turn
		//post: returns the best move assuming that the opponent plays perfectly.
			//This search only looks (depth) moves ahead and is very inefficient.
		public checkersSetup.Turn alphaBetaSearch(CheckersSetup setup, int curdepth) {
			Turn favTurn[];
			int numGoodMoves=0;
			
			double optimalUtil;
			Double opContainer;
			double opPosUtilForTurnI;
			int key[];
			
			if(curdepth == 0) {
				System.out.println("Error(2013): the depth should be bigger than 0!");
				System.exit(1);
				//stub to stop java from complaining:
				favTurn = new Turn[0];
			}
			
			if(this.playerColour) {
				optimalUtil = getMaxDarkUtility(setup, curdepth, MINUS_INF, PLUS_INF);
			} else {
				optimalUtil = getMinDarkUtility(setup, curdepth, MINUS_INF, PLUS_INF);
			}
			
			if(optimalUtil == 0.0) {
				//2013: so... what did I want myself to test...
				System.out.println("Optimal util is draw! Test this!");
			}
			
			ArrayList<checkersSetup.Turn> turnChoices =  BasicCheckersAIFunctions.getAllPossiblePlayerOptionsFor1Turn(setup, playerColour);
				
			favTurn = new Turn[turnChoices.size()];
			
			System.out.println("optimal UTIL: " + optimalUtil);
			
			for(int i=0; i<turnChoices.size(); i++) {
				setup.playTurn(turnChoices.get(i));
				key = AnalysedPosition.makeKey(setup, !this.playerColour);
				
				System.out.println("i==" + i);
				//Check to see if the position has already been analysed
				
				opContainer = getUtilityIfAlreadyFound(key, curdepth-1);
				if(opContainer != null) {
					opPosUtilForTurnI = opContainer.doubleValue();
					System.out.println("opPosUtilForTurnI: " + opPosUtilForTurnI);
				} else if(i == 0){
					//TODO: explain why this makes sense.
					System.out.println("2013: TODO: WTF!!");
					System.out.println("2013: I don't think this code is ever supposed to be executed.");
					System.out.println(-1);
					opPosUtilForTurnI = optimalUtil;
				} else {
					System.out.println("opPosUtilForTurnI: Not needed to be determined.");
					opPosUtilForTurnI = NOT_DETERMINED;
				}
				
				if(closeEnough(opPosUtilForTurnI, optimalUtil) ) {
				
					favTurn[numGoodMoves] = turnChoices.get(i);
					numGoodMoves++;
				
				} else if(NOT_DETERMINED != opPosUtilForTurnI && opPosUtilForTurnI < optimalUtil && this.playerColour != this.DARK_TURN) {
					System.out.println("ERROR: impossibly low utility for light player");
					System.exit(1);
				} else if(NOT_DETERMINED != opPosUtilForTurnI && opPosUtilForTurnI > optimalUtil && this.playerColour == this.DARK_TURN) {
					System.out.println("ERROR: impossibly low utility for dark player");
					System.exit(1);
				}
				
				setup.reverseTurn(turnChoices.get(i));
			}
			
			//TODO: ajust this according to how you ajust the win utility of a delayed loss or faster win.
			//if(opWorstPosUtil <= -BasicCheckersAIFunctions.WIN_UTILITY) {
			//	System.out.println("YOU ARE FUCKED!!");
			//}
			
			
			//if AI is NOT deterministic AND
			//	there's several choices that seem similarly good, the AI randomly choses a move 
			if(numGoodMoves > 1 && this.deterministic == false) {
				System.out.println("aitree is randomly choosing beetween " + (numGoodMoves+1) + " ways to do his turn");
				return favTurn[(int)(Math.random()*(numGoodMoves))];
			} else if(numGoodMoves == 1 || this.deterministic == true){
				return favTurn[0];
			} else {
				System.out.println("ERROR(alpha/beta): couldnt find a the good move!");
				System.exit(1);
				return favTurn[0];
			}
		}
		
		protected double CLOSE_ENOUGN_BOUND = 0.000001;
		
		//hack to deal with the imprecision of double values.
		public boolean closeEnough(double a, double b) {
			if(a >= b - CLOSE_ENOUGN_BOUND && a <= b + CLOSE_ENOUGN_BOUND) {
				return true;
			} else {
				return false;
			}
		}
		
		boolean DARK_TURN = true;
		
		public double getMaxDarkUtility(CheckersSetup setup, int curdepth, double alpha, double beta) {
			double ret = MINUS_INF;
			
			//this key identifies the setup of the board and who's turn it is in 3 numbers:
			int key[] = AnalysedPosition.makeKey(setup, DARK_TURN);
			
			//Check to see if the position has already been analysed
			Double alreadyFoundUtility = getUtilityIfAlreadyFound(key, curdepth);
			
			if(alreadyFoundUtility != null) {
				return new Double(alreadyFoundUtility).doubleValue();
				
			}
			
			if(curdepth ==0) {
				return utilityFunc.getEstimatedUtilityForDarkPlayer(setup, DARK_TURN);
			} else {
				ArrayList<checkersSetup.Turn> turnChoices =  BasicCheckersAIFunctions.getAllPossiblePlayerOptionsFor1Turn(setup, DARK_TURN);
				
				if(turnChoices.size() == 0) {
					//horrible position for dark:
					ret = -BasicCheckersAIFunctions.DARK_WIN_UTILITY;
					addPositionToListOfAnalysedPositions(key, DARK_TURN, curdepth, ret);
					//TODO: ajust this value to make the AI delay death and quicken kill.
					return ret;
				}
				
				//op = OPPONENT!
				double opWorstPosUtil = ret;
				double opPosUtilForTurnI;
			
				for(int i=0; i<turnChoices.size(); i++) {
					setup.playTurn(turnChoices.get(i));
					
					opPosUtilForTurnI = getMinDarkUtility(setup, curdepth-1, alpha, beta);
					
					setup.reverseTurn(turnChoices.get(i));
					
					opWorstPosUtil = Math.max(opWorstPosUtil, opPosUtilForTurnI);
					
					if(opWorstPosUtil >= beta ) {
						ret = opWorstPosUtil;
						if(ret <= this.MINUS_INF) {
							System.err.println("ERROR(1): returning MINUS_INF or less.");
							System.exit(1);
						}
						//addPositionToListOfAnalysedPositions(key, DARK_TURN, curdepth, opWorstPosUtil);
						return opWorstPosUtil;
					}
					
					alpha = Math.max(alpha, opWorstPosUtil);
					
				}
				ret = opWorstPosUtil;
				
			}
			
			
			addPositionToListOfAnalysedPositions(key, DARK_TURN, curdepth, ret);
			
			if(ret <= this.MINUS_INF) {
				System.err.println("ERROR(2): returning MINUS_INF or less.");
				System.exit(1);
			}
			
			return ret;
		}
		
		public double getMinDarkUtility(CheckersSetup setup, int curdepth, double alpha, double beta) {
			double ret = PLUS_INF;
			
			//this key identifies the setup of the board and who's turn it is in 3 numbers:
			int key[] = AnalysedPosition.makeKey(setup, !DARK_TURN);
			
			//Check to see if the position has already been analysed
			Double alreadyFoundUtility = getUtilityIfAlreadyFound(key, curdepth);
			
			if(alreadyFoundUtility != null) {
				return new Double(alreadyFoundUtility).doubleValue();
				
			}
			if(curdepth ==0) {
				return utilityFunc.getEstimatedUtilityForDarkPlayer(setup, !DARK_TURN);
			} else {
				ArrayList<checkersSetup.Turn> turnChoices =  BasicCheckersAIFunctions.getAllPossiblePlayerOptionsFor1Turn(setup, !DARK_TURN);
				
				if(turnChoices.size() == 0) {
					//horrible position for white:
					//TODO: ajust this value to make the AI delay death and quicken kill.
					ret = BasicCheckersAIFunctions.DARK_WIN_UTILITY;
					
					addPositionToListOfAnalysedPositions(key, !DARK_TURN, curdepth, ret);
					
					return ret;
				}
				
				//op = OPPONENT!
				double opWorstPosUtil = ret;
				double opPosUtilForTurnI;
			
				for(int i=0; i<turnChoices.size(); i++) {
					setup.playTurn(turnChoices.get(i));
					
					opPosUtilForTurnI = getMaxDarkUtility(setup, curdepth-1, alpha, beta);
					
					setup.reverseTurn(turnChoices.get(i));
					
					opWorstPosUtil = Math.min(opWorstPosUtil, opPosUtilForTurnI);
					
					//TODO: take care of the case where opWorstPosUtil == alpha without breaking it!
					if(opWorstPosUtil <= alpha ) {
						ret = opWorstPosUtil;
						if(ret >= this.PLUS_INF) {
							System.err.println("ERROR(1): returning PLUS_INF or more.");
							System.exit(1);
						}
						//addPositionToListOfAnalysedPositions(key, !DARK_TURN, curdepth, ret);
						return opWorstPosUtil;
					}
					
					beta = Math.min(beta, opWorstPosUtil);
					
					
					
				}
				ret = opWorstPosUtil;
			}
			
			addPositionToListOfAnalysedPositions(key, !DARK_TURN, curdepth, ret);
			
			if(ret >= this.PLUS_INF) {
				System.err.println("ERROR(2): returning PLUS_INF or more.");
				System.exit(1);
			}
			return ret;
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
}
