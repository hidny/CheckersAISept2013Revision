package checkersAI;

import java.util.ArrayList;

import pastPositionRecord.AnalysedPosition;

import checkersSetup.CheckersSetup;
import checkersSetup.Game;
import checkersSetup.Move;

//************THIS FILE IS COMPLETED.
//This class is not actually very useful...

/*NEW AI:
	-A slightly cleverer AI that has several position trees to make recalling positions faster:
		-seperate darkturn from light turn
		-Only look for previous done pos 3 moves in.
	-I Tested it against simpleAIwithMEM and it gives the same results.
	
	-This is just slightly worse than SimpleComputerAIwithTree.
		I made it just to see if I can... and I can!
*/
	
public class SimpleComputerAIwithTree2 extends SimpleComputerAIwithTree{
	
	BalancedSearchTree viewedOpponentPos;
	
	public SimpleComputerAIwithTree2() {
		super(false, 6, new checkersUtility.SimpleUtility(), false);
	}
	
	public SimpleComputerAIwithTree2(boolean deterministic, int depth, checkersUtility.Utility utility, boolean debug) {
		super(deterministic, depth, utility,  debug);
	}
	
	//This function is here in order to removed viewOpponentPos:
	public Move get1stMove(Game game, CheckersSetup setup, checkersSetup.Board board, ArrayList<Move> choices) {
		viewedOpponentPos = null;
		return super.get1stMove(game, setup, board, choices);
	}
	
	public Double getUtilityIfAlreadyFound(AnalysedPosition alreadyAnalysedPos, int key[], int curdepth) {
		//we can only find the same position after 2 moves:
		BalancedSearchTree relevantPositionTree = getRelevantTree(curdepth);
		
		if(depth - curdepth > 2) {
			if(relevantPositionTree != null) {
				alreadyAnalysedPos = relevantPositionTree.search(key);
			}
			
			if(alreadyAnalysedPos != null) {
				if(alreadyAnalysedPos.getDepth() >= curdepth) {
					return new Double(alreadyAnalysedPos.getUtility());
				}
			}
		}
		return null;
	}
	
	//Adds the position and it's estimated utility to the table.
	// If the position is already in the table (which will happen sometimes), It will either update.
	public void addPositionToListOfAnalysedPositions(int key[], boolean isDarkPlayerCurrentTurn, int curdepth, double utility, boolean alreadyDidPosB4Recursion, AnalysedPosition alreadyAnalysedPos) {
		boolean alreadyDidPos = alreadyDidPosB4Recursion;
		BalancedSearchTree relevantPositionTree = getRelevantTree(curdepth);
		
		//check if the position has been inserted after doing the recursion:
		if(alreadyDidPosB4Recursion == false && relevantPositionTree != null) {
			alreadyAnalysedPos = relevantPositionTree.search(key);
			if(alreadyAnalysedPos != null) {
				alreadyDidPos = true;
			}
		}
		
		//If the root of relevantPositionTree is null or the position is not already done:
		if(relevantPositionTree == null || alreadyDidPos == false) {
				AnalysedPosition pos = new AnalysedPosition(curdepth, isDarkPlayerCurrentTurn, utility, key);
				relevantPositionTree = BalancedSearchTree.addValueToTree(relevantPositionTree, pos);
				setRelevantTree(curdepth, relevantPositionTree);
				
				//FOR TEST PURPOSES
				numPosRecorded++;
				
				//TESTING SLOW
				if(debug) {
					sanityCheckNumNodes();
					relevantPositionTree.sanityCheckSorted();
				}
				//END TESTING
	
		//if we only need to update:
		} else {
				alreadyAnalysedPos.updatePositionWithImprovedCalculation(curdepth, utility);
		}
		
	}
	
	private BalancedSearchTree getRelevantTree(int curdepth) {
		if( (depth - curdepth) % 2 == 0) {
			return viewedPos;
		} else {
			return viewedOpponentPos;
		}
	}
	//Sets the relevant Positon tree to newtree
	private void setRelevantTree(int curdepth, BalancedSearchTree newtree) {
		if( (depth - curdepth) % 2 == 0) {
			viewedPos = newtree;
		} else {
			viewedOpponentPos = newtree;
		}
	}
	
	public void sanityCheckNumNodes() {
		int numNodesAI;
		int numNodesOpp;
		if(this.viewedPos != null) {
			numNodesAI = this.viewedPos.sanityCheckGetNumberOfNodes();
		} else {
			numNodesAI = 0;
		}
		
		if(this.viewedOpponentPos != null) {
			numNodesOpp = this.viewedOpponentPos.sanityCheckGetNumberOfNodes();
		} else {
			numNodesOpp = 0;
		}
		
		if(this.numPosRecorded != numNodesAI + numNodesOpp) {
			System.out.println("ERROR: in SimpleAITree2, the number of Positions recorded is not equal to the number of nodes in both trees");
			System.out.println("Recorded: " +this.numPosRecorded);
			System.out.println("In trees: " + (numNodesAI + numNodesOpp));
			System.out.println("ViewedPos: " + numNodesAI);
			System.out.println("ViewedOPPos: " + numNodesOpp);
			
			System.exit(1);
		}
	}
		
	public void sanityTestPositionDidntDissapearOffTree(AnalysedPosition alreadyAnalysedPos, int key[], boolean alreadyDidPos, int curdepth) {
		BalancedSearchTree relevantPositionTree = getRelevantTree(curdepth);
		if(relevantPositionTree != null) {
			alreadyAnalysedPos = relevantPositionTree.search(key);
			if(alreadyAnalysedPos != null && alreadyDidPos == false) {
				if(alreadyAnalysedPos.getDepth() >= curdepth) {
					System.out.println("ERROR: Position should have been viewable earlier");
					System.exit(1);
				}
			}
		}
	}
}