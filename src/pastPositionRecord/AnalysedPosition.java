package pastPositionRecord;

import checkersAI.BalancedSearchTree;
import checkersSetup.CheckersSetup;

public class AnalysedPosition {
	private int depth;
	
	private double utility;
	
	private int key[];
	
	//DELETE THIS LATER:
	//IT'S ONLY FOR TESTING:
	private boolean darkTurn;
	//END DELETE THIS.
	
	public AnalysedPosition(CheckersSetup setup, int depth, boolean isDarkTurn, double utility) {
		this(depth, isDarkTurn, utility, makeKey(setup, isDarkTurn));
	}
	
	public AnalysedPosition(int depth, boolean isDarkTurn, double utility, int key[]) {
		this.depth = depth;
		this.darkTurn = isDarkTurn;
		this.utility = utility;	
		this.key = key;
	}
	
	
	//TODO: TEST THIS!
	//TODO: First sanity check keys vs AnalysedPositions!
	
	//pre: setup.getPiecesOnBoard is not null.
	//post: returns an int array of length 3 that uniquely identifies a checkers position and whose turn it is.
		//This array will be used for sorting and to save space. (instead of using the whole array every time)
	public static int[] makeKey(CheckersSetup setup, boolean isDarkTurn) {
		int key[] = new int[3];
	
		//2013: 12 indexs is too big for a 32 bit number... so use 11. 5^11 < 2^32???
		int NUM_PIECES_FOR_EACH_INDEX = 11;
		int arraySize = ((setup.getPiecesOnBoard().length * setup.getPiecesOnBoard()[0].length - 1) / NUM_PIECES_FOR_EACH_INDEX) + 1;
		key = new int[arraySize];
		for(int i=0; i<arraySize; i++) {
			key[i] = 0;
		}
		int currentIndex = 0;
		int numPiecesRecordedForCurrentIndex = 0;
		
		//Add info about whose turn it is in the key:
		int mult = 1;
		if(isDarkTurn) {
			key[currentIndex] = 0;
		} else {
			key[currentIndex] = 1;
		}
		mult *= 2;
		
		for(int i=0; i<setup.getPiecesOnBoard().length; i++) {
			for(int j=0; j<setup.getPiecesOnBoard()[0].length; j++) {
				//if((i+j)%2 == 1) {
		    		
		    		//Put the piece on the board array:
					key[currentIndex] += mult * setup.getPiecesOnBoard()[i][j];
					mult *=5;
					numPiecesRecordedForCurrentIndex++;
					
					if(numPiecesRecordedForCurrentIndex >= NUM_PIECES_FOR_EACH_INDEX) {
						currentIndex++;
						numPiecesRecordedForCurrentIndex = 0;
						mult = 1;
					}
		        //}
			}
		}
			
		
		
		return key;
	}

	public int getDepth() {
		return depth;
	}


	public boolean isDarkTurn() {
		return darkTurn;
	}


	public double getUtility() {
		return utility;
	}
	
	public int[]getKey() {
		return key;
	}
	
	//post: returns true if the two positions are the same.
	//		returns false otherwise.
	public boolean isSamePosition(AnalysedPosition other) {
		return isSamePosition(other.getKey());
	}
	
	//pre: both boards are assumed to be the same size AND isDarkTurn == this.isDarkTurn
	//post: returns true if this position is the same as the other position
	public boolean isSamePosition(CheckersSetup other, boolean isDarkTurn) {
		return isSamePosition(makeKey(other, isDarkTurn));
	}
	
	public boolean isSamePosition(int otherKey[]) {
		return BalancedSearchTree.compare(this.getKey(), otherKey) == 0;
	}
	
	//pre: depth > this.depth
	//post: records improved calculation of utility 
	public void updatePositionWithImprovedCalculation(int depth, double utility) {
		//Testing precondition:
		if(this.depth >= depth) {
			System.out.println("Error: trying to update utility of a position with the same or less accuracy.");
			System.exit(1);
		}
		//End testing precondition.
		this.depth = depth;
		this.utility = utility;
	}
	
	
}
