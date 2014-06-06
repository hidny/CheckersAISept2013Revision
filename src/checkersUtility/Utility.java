package checkersUtility;

import checkersSetup.CheckersSetup;

//Seeing that I will probably make many utility functions,
//This interfaces forces them all to have the same
//signature for the utility function:

public interface Utility {
	
	public double getEstimatedUtilityForDarkPlayer(CheckersSetup setup, boolean isDarkTurn);
}
