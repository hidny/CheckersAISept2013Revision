package pastPositionRecord;

import checkersSetup.CheckersSetup;

public class AnalysedPositionForDraw extends AnalysedPosition {
	private int numOccurances;
	
	public AnalysedPositionForDraw(CheckersSetup setup, boolean isDarkTurn) {
		super(setup, 0, isDarkTurn, 0.0);
		this.numOccurances = 1;
	}
			
	public AnalysedPositionForDraw(CheckersSetup setup, int depth, boolean isDarkTurn, double utility) {
		super(setup, depth, isDarkTurn, utility);
		this.numOccurances = 1;
	}
	
	public AnalysedPositionForDraw(int depth, boolean isDarkTurn, double utility, int key[]) {
		super(depth, isDarkTurn, utility, key);
		this.numOccurances = 1;
	}
	
	public void addOneOccurance() {
		numOccurances++;
	}
	
	//returns the number of of the position has occured in the game.
	public int getNumOccurances() {
		return numOccurances;
	}
	
	public boolean isDraw() {
		if(numOccurances >= 3) {
			return true;
		} else {
			return false;
		}
	}
}
