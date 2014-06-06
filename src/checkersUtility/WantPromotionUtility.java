package checkersUtility;

import checkersSetup.CheckersSetup;

public class WantPromotionUtility implements Utility {
	
	private Utility pieceCount = new SimpleUtility();
	
	public double getEstimatedUtilityForDarkPlayer(CheckersSetup setup, boolean isDarkTurn) {
		double ret = pieceCount.getEstimatedUtilityForDarkPlayer(setup, isDarkTurn);
		
		int distanceDarkCompLight = totalDistanceFromDarkPromotion(setup) - totalDistanceFromLightPromotion(setup);
		
		ret -= 0.01 * distanceDarkCompLight;
		return ret;
	}
	
	public static int totalDistanceFromDarkPromotion(CheckersSetup setup) {
		int dist = 0;
		
		int array[][] = setup.getPiecesOnBoard();
		for(int i=0; i<array.length; i++) {
			for(int j=0; j<array[0].length; j++) {
				if(CheckersSetup.isDarkPiece(array[i][j]) && CheckersSetup.isKing(array[i][j]) == false) {
					dist += i;
				}
			}
		}
		
		return dist;
	}
	
	public static int totalDistanceFromLightPromotion(CheckersSetup setup) {
		int dist = 0;
		
		int array[][] = setup.getPiecesOnBoard();
		for(int i=0; i<array.length; i++) {
			for(int j=0; j<array[0].length; j++) {
				if(CheckersSetup.isDarkPiece(array[i][j]) == false && CheckersSetup.isKing(array[i][j]) == false) {
					dist += array.length - 1 - i;
				}
			}
		}
		
		return dist;
	}
	
	
}
