package checkersUtility;

import java.util.ArrayList;

import checkersSetup.CheckersSetup;

import checkersAI.BasicCheckersAIFunctions;

public class UtilityAtEquilibriumPos implements Utility {
	
	private Utility innerUtility;
	
	public UtilityAtEquilibriumPos(Utility innerUtility) {
		this.innerUtility = innerUtility;
	}
	
	//This utility function only counts the utility of "equilibrium positions"
	//	OR: positions that don't force the current player to jump.
	// If the position given forces the player to jump, it on itself.
	// Once it found a position that is at equilibrium, it runs another utility function to get the EstimatedUtility.
	
	public double getEstimatedUtilityForDarkPlayer(CheckersSetup setup, boolean isDarkTurn) {
		ArrayList<checkersSetup.Turn> turns = BasicCheckersAIFunctions.getAllPossiblePlayerOptionsFor1Turn(setup, isDarkTurn);
		if(turns.size() == 0) {
			if(isDarkTurn) {
				return -BasicCheckersAIFunctions.DARK_WIN_UTILITY;
			} else {
				return BasicCheckersAIFunctions.DARK_WIN_UTILITY;
			}
		} else if(turns.get(0).getMovesFor1Turn().get(0).isJump()) {
			double minUtility;
			double currentUtility;
			
			//TODO: TEST THIS!!
			if(isDarkTurn) {
				minUtility =  BasicCheckersAIFunctions.DARK_WIN_UTILITY;
				for(int i=0; i<turns.size(); i++) {
					setup.playTurn(turns.get(i));
					currentUtility = innerUtility.getEstimatedUtilityForDarkPlayer(setup, !isDarkTurn);
					
					if(currentUtility < minUtility) {
						minUtility = currentUtility;
					}
					
					setup.reverseTurn(turns.get(i));
				}
			} else {
				minUtility =  -BasicCheckersAIFunctions.DARK_WIN_UTILITY;
				for(int i=0; i<turns.size(); i++) {
					setup.playTurn(turns.get(i));
					currentUtility = innerUtility.getEstimatedUtilityForDarkPlayer(setup, !isDarkTurn);
					
					if(currentUtility > minUtility) {
						minUtility = currentUtility;
					}
					
					setup.reverseTurn(turns.get(i));
				}
			}
			//END TEST
			return minUtility;
		} else {
			return innerUtility.getEstimatedUtilityForDarkPlayer(setup, isDarkTurn);
		}
	}
}
