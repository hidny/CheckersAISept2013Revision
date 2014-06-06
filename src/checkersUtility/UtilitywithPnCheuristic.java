package checkersUtility;

import checkersSetup.CheckersSetup;

//Utility with Promotion and kings to center heuristic:
//Note: when it comes to the decimals used, they're pretty arbitrary and could probably be improved.

public class UtilitywithPnCheuristic implements Utility {
	private Utility pieceCount = new SimpleUtility();
	
	public double getEstimatedUtilityForDarkPlayer(CheckersSetup setup, boolean isDarkTurn) {
		double ret = pieceCount.getEstimatedUtilityForDarkPlayer(setup, isDarkTurn);
		
		int distanceDarkComparedToLight = WantPromotionUtility.totalDistanceFromDarkPromotion(setup) - WantPromotionUtility.totalDistanceFromLightPromotion(setup);
		
		ret -= 0.01 * distanceDarkComparedToLight;
		
		ret += UtilityKingWantsCenter.utilityOfKingsTowardsCenter(setup);
		
		return ret;
	}

}
