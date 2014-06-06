package checkersAI;

//TODO: this function doesn't work!
//Fix it!

import java.util.ArrayList;
//


//TODO: Impatience factor should only work if YOU ARE WINNING!
//TODO: test it! does this work???

// TEST DARK and Light SEPERATELY

//TODO: fix the case where at first you want to delay because you are losing, but then you want to rush because you are winning!!!
import checkersSetup.CheckersSetup;
/*import checkersSetup.Game;	//For Testing
import checkersSetup.Move;*/

public class ComputerAIAlphaBetaPruneTimeIsMoney extends ComputerAIAlphaBetaPrune{
	//For Testing:
	/*private checkersUtility.SimpleUtility pieceCount = new checkersUtility.SimpleUtility();
	*/
	public ComputerAIAlphaBetaPruneTimeIsMoney() {
		this(false, 6, new checkersUtility.SimpleUtility(), false);
	}
	
	public ComputerAIAlphaBetaPruneTimeIsMoney(boolean deterministic, int depth, checkersUtility.Utility utility, boolean debug) {
		super(deterministic, depth, utility, debug);
	}
	
	public double getMaxDarkUtility(CheckersSetup setup, int curdepth, double alpha, double beta) {
		return getMaxDarkUtility(setup, curdepth, alpha, beta, true);
	}
	
	public double getMaxDarkUtility(CheckersSetup setup, int curdepth, double alpha, double beta, boolean noCapturesYet) {
		double impatienceFactor = 0.0;
		if(noCapturesYet && this.playerColour == this.DARK_TURN) {
			impatienceFactor = getImpatienceFactor(setup, this.DARK_TURN);
			if(impatienceFactor == 0.0) {
				noCapturesYet = false;
			}
		}
		return super.getMaxDarkUtility(setup, curdepth, alpha, beta) - impatienceFactor;
		
	}
	
	public double getMinDarkUtility(CheckersSetup setup, int curdepth, double alpha, double beta) {
		return getMinDarkUtility(setup, curdepth, alpha, beta, true);
	}
	
	public double getMinDarkUtility(CheckersSetup setup, int curdepth, double alpha, double beta, boolean noCapturesYet) {
		double impatienceFactor = 0.0;
		if(noCapturesYet && this.playerColour != this.DARK_TURN) {
			impatienceFactor = getImpatienceFactor(setup, !this.DARK_TURN);
			if(impatienceFactor == 0.0) {
				noCapturesYet = false;
			}
		}
		return super.getMinDarkUtility(setup, curdepth, alpha, beta) + impatienceFactor;
	}
	
	public boolean isDying() {
		return false;
	}
	
	//post: returns the impatience factor.
	//If you can't make a jump, just return the impatience_factor until you can.
	
	//Note: this multiple is small because I don't want impatience getting in the way of good play.
	
	
	public final double IMPATIENTCE_FACTOR = 2 * super.CLOSE_ENOUGN_BOUND;
	private double getImpatienceFactor(CheckersSetup setup, boolean isDarkTurn) {
		
		ArrayList<checkersSetup.Turn> turns = BasicCheckersAIFunctions.getAllPossiblePlayerOptionsFor1Turn(setup, isDarkTurn);
		
		//Bad position:
		if(turns.size() == 0) {
			return 0.0;
		
		//ignore jump moves are good.
		} else if(turns.get(0).getMovesFor1Turn().get(0).isJump()) {
			return 0.0;
		
		//Return the impatience factor of another BORING position:
		} else {
		
			//the turn doesn't matter when you are only doing a piece count.
			return IMPATIENTCE_FACTOR;
		}
	}
	/*	//For Testing:
	private double getPieceCount(CheckersSetup setup) {
		double numPieces = 0.0;
		for(int i=0; i<setup.getPiecesOnBoard().length; i++) {
			for(int j=0; j<setup.getPiecesOnBoard()[0].length; j++) {
				numPieces++;
			}
		}
		return numPieces;
	}*/
}
