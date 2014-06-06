package checkersAI;

import java.util.ArrayList;

import pastPositionRecord.AnalysedPosition;
import pastPositionRecord.AnalysedPositionForDraw;
import checkersSetup.CheckersSetup;
import checkersSetup.Game;
import checkersSetup.Move;

//TODO: test this!
public class SimpleComputerAIwithTreeDrawAvoider extends SimpleComputerAIwithTree {

	int potentialPositions[][];
	Game currentGame;
	
	public SimpleComputerAIwithTreeDrawAvoider() {
		this(false, 6, new checkersUtility.SimpleUtility(), false);
	}
	
	public SimpleComputerAIwithTreeDrawAvoider(boolean deterministic, int depth, checkersUtility.Utility utility, boolean debug) {
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
	
	public double getEstUtilityOfPosition(CheckersSetup setup, boolean isDarkPlayerCurrentTurn, int curdepth) {
		if(hasDrawPotential(setup, isDarkPlayerCurrentTurn, curdepth) == true) {
			//TODO: should I only return a min of 0?
			//Should it only depend on wether the position has occured twice?
			return 0.0;
		} else {
			
			addPostionToDrawPotential(setup, isDarkPlayerCurrentTurn, curdepth);
			return super.getEstUtilityOfPosition(setup, isDarkPlayerCurrentTurn, curdepth);
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
