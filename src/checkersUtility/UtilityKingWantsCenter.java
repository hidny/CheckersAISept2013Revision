package checkersUtility;

import checkersSetup.CheckersSetup;

public class UtilityKingWantsCenter implements Utility {
private Utility pieceCount = new SimpleUtility();
	
	//Some lazy test cases:
	public static void main(String args[]) {
		CheckersSetup setup = new CheckersSetup();
		UtilityKingWantsCenter test = new UtilityKingWantsCenter();
		if(test.getEstimatedUtilityForDarkPlayer(setup, false) != 0) {
			System.out.println("Test1 failed!");
		}
		int array[][] = setup.getPiecesOnBoard();
		
		array[3][3] = 2;
		
		double num = test.getEstimatedUtilityForDarkPlayer(setup, false);
		if(num >= 0.5 + 1.6 - 0.00001 && num <= 0.5 + 1.6 + 0.00001) {
			
		} else {
			System.out.println(test.getEstimatedUtilityForDarkPlayer(setup, false));
			System.out.println("Test2 failed!");
		}
		array[2][3] = 2;
		num = test.getEstimatedUtilityForDarkPlayer(setup, false);
		if(num >= 5.2 - 0.00001 && num <= 5.2 + 0.00001) {
			
		} else {
			System.out.println(test.getEstimatedUtilityForDarkPlayer(setup, false));
			System.out.println("Test3 failed!");
		}
		array[1][3] = 2;
		num = test.getEstimatedUtilityForDarkPlayer(setup, false);
		if(num >= 7.2 - 0.00001 && num <= 7.2 + 0.00001) {
			
		} else {
			System.out.println(test.getEstimatedUtilityForDarkPlayer(setup, false));
			System.out.println("Test4 failed!");
		}
		
		array[7][5] = 2;
		num = test.getEstimatedUtilityForDarkPlayer(setup, false);
		if(num >= 8.9 - 0.00001 && num <= 8.9 + 0.00001) {
			
		} else {
			System.out.println(test.getEstimatedUtilityForDarkPlayer(setup, false));
			System.out.println("Test5 failed!");
		}
		
	}
	public double getEstimatedUtilityForDarkPlayer(CheckersSetup setup, boolean isDarkTurn) {
		double ret = pieceCount.getEstimatedUtilityForDarkPlayer(setup, isDarkTurn);
		
		double utilityOfDarkKingPos = utilityOfKingsTowardsCenter(setup);
		
		ret += utilityOfDarkKingPos;
		
		return ret;
	}
	
	public static double utilityOfKingsTowardsCenter(CheckersSetup setup) {
		double utility = 0.0;
		
		int array[][] = setup.getPiecesOnBoard();
		for(int i=0; i<array.length; i++) {
			for(int j=0; j<array[0].length; j++) {
				if(CheckersSetup.isDarkPiece(array[i][j]) && CheckersSetup.isKing(array[i][j])) {
					if(i==0 || i == array.length - 1 || j==0 || j == array[0].length - 1) {
						utility += 0.1;
					} else if(i==1 || i == array.length - 2 || j==1 || j == array[0].length - 2) {
						utility += 0.4;
					} else {
						utility += 0.5;
					}
				} else if (CheckersSetup.isWhitePiece(array[i][j]) && CheckersSetup.isKing(array[i][j])) {
					if(i==0 || i == array.length - 1 || j==0 || j == array[0].length - 1) {
						utility -= 0.1;
					} else if(i==1 || i == array.length - 2 || j==1 || j == array[0].length - 2) {
						utility -= 0.4;
					} else {
						utility -= 0.5;
					}
				}
			}
		}
		
		return utility;
	}
	
}
