package checkersUtility;

import checkersSetup.CheckersSetup;

public class SimpleUtility implements Utility {
	
	//The simplest utility function I can think of.
	//It Basically counts the pieces on the board with kings worth 1.6.
	public double getEstimatedUtilityForDarkPlayer(CheckersSetup setup, boolean isDarkTurn) {
		double result = 0.0;
		int piecesOnBoard[][] = setup.getPiecesOnBoard();
		
		for(int i=0; i<setup.getPiecesOnBoard().length; i++) {
			for(int j=0; j<setup.getPiecesOnBoard()[0].length; j++) {
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
}
