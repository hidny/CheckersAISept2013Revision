package checkersAI;
//TODO: test this!
//WHITE DONE!

import java.util.ArrayList;
import checkersSetup.CheckersSetup;
import checkersSetup.Game;
import checkersSetup.Move;
import java.util.Scanner;

import pastPositionRecord.AnalysedPosition;
import pastPositionRecord.AnalysedPositionForDraw;

public class ComputerAIAlphaBetaPruneDrawAvoider  extends ComputerAIAlphaBetaPrune {
		int potentialPositions[][];
		Game currentGame;
		
		public ComputerAIAlphaBetaPruneDrawAvoider() {
			this(false, 6, new checkersUtility.SimpleUtility(), false);
		}
		
		public ComputerAIAlphaBetaPruneDrawAvoider(boolean deterministic, int depth, checkersUtility.Utility utility, boolean debug) {
			super(deterministic, depth, utility, debug);
			potentialPositions = new int[depth][];
		}
		
		/*protected static final int NO_DRAW_POTENTIAL = 10020;
		protected static final int DRAW_POTENTIAL_UTILITY = 0;
		*/
		
		public Move get1stMove(Game game, CheckersSetup setup, checkersSetup.Board board, ArrayList<Move> choices) {
			currentGame = game;
			return super.get1stMove(game, setup, board, choices);
		}
		
		public double getMaxDarkUtility(CheckersSetup setup, int curdepth, double alpha, double beta) {
			//The first position seen should NEVER return a 0.0!
			if(curdepth != this.depth) {
				if(hasDrawPotential(setup, super.DARK_TURN, curdepth) == true) {
					//TODO: should I only return a min of 0?
					//Should it only depend on wether the position has occured twice?
					return 0.0;
				} else {
					
					addPostionToDrawPotential(setup, super.DARK_TURN, curdepth);
					return super.getMaxDarkUtility(setup, curdepth, alpha, beta);
				}
			} else {
				
				return super.getMaxDarkUtility(setup, curdepth, alpha, beta);
			}
		}
		
		public double getMinDarkUtility(CheckersSetup setup, int curdepth, double alpha, double beta) {
			//The first position seen should NEVER return a 0.0!
			if(curdepth != this.depth) {
				if(hasDrawPotential(setup, !super.DARK_TURN, curdepth) == true) {
					//TODO: should I only return a min of 0?
					//Should it only depend on wether the position has occured twice?
					return 0.0;
				} else {
					
					addPostionToDrawPotential(setup, !super.DARK_TURN, curdepth);
					return super.getMinDarkUtility(setup, curdepth, alpha, beta);
				}
			} else {
				
				return super.getMinDarkUtility(setup, curdepth, alpha, beta);
			}
		}
		
		//TODO: the AI should know the diff between having seen the pos once or twice.
		
		public boolean hasDrawPotential(CheckersSetup setup, boolean isDarkPlayerCurrentTurn, int curdepth) {
			
			int currentKey[] = AnalysedPosition.makeKey(setup, isDarkPlayerCurrentTurn);
			//System.out.println("Depth: " + depth);
			//System.out.println("CurDepth: " + curdepth);
			
			
			for(int i=0; i<depth - curdepth - 1; i++) {
				//System.out.println("i="+i);
				if(BalancedSearchTree.compare(potentialPositions[i], currentKey) == 0 ) {
					//System.out.println("FOUND PREV POTENTIAL POS!");
					//System.exit(1);
					return true;
				}
			}
			
			if(currentGame.getPositionsAttainedToCheckForDraw() != null) {
				AnalysedPositionForDraw search = (AnalysedPositionForDraw)(currentGame.getPositionsAttainedToCheckForDraw().search(currentKey));				
				if(search != null) {
					//System.out.println("FOUND PREV ATTAINED POS!");
					//System.exit(1);
					
					//ADD position to the list of analysed position because
					//alphaBetaSearch might try to find this position!
					Double util = this.getUtilityIfAlreadyFound(currentKey, curdepth);
					if(util != null) {
						if(util.doubleValue() != 0) {
							System.out.println("ERROR: draw position is already there with " + util.doubleValue());
							System.exit(1);
						}
					} else {
						this.addPositionToListOfAnalysedPositions(currentKey, isDarkPlayerCurrentTurn, curdepth, 0.0);
					}
					
					return true;
				}
			}
			
			return false;
		}
		
		//Scanner in = new Scanner(System.in);
		public void addPostionToDrawPotential(CheckersSetup setup, boolean isDarkPlayerCurrentTurn, int curdepth) {
			potentialPositions[depth - curdepth - 1] = AnalysedPosition.makeKey(setup, isDarkPlayerCurrentTurn);
		}
		
	}

