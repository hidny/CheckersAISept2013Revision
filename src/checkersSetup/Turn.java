package checkersSetup;

import java.util.ArrayList;
//Simple class to clarify my code and to have a well defined meaning for 'Turn'

public class Turn {
	private ArrayList<Move> moves = new ArrayList<Move>();
	
	public Turn(Move move) {
		moves.add(move);
	}
	
	public Turn(ArrayList<Move> moves) {
		for(int i=0; i<moves.size(); i++) {
			this.moves.add(moves.get(i));
		}
	}
	
	public ArrayList<Move> getMovesFor1Turn() {
		return moves;
	}
	
	//pre: the turn must contain at least 1 move.
	public String toString() {
		String ret = "";
		ret += "(row " + (moves.get(0).getStartPos().getRow()+1) + ", col " + (moves.get(0).getStartPos().getCol()+1) + ")" + " ";
		
		for(int i=0; i<moves.size(); i++) {
			ret += " to (row " + (moves.get(i).getEndPos().getRow()+1) + ", col " + (moves.get(i).getEndPos().getCol()+1) + ")";
		}
		return ret;
	}
	
	//Checks if 2 turns are equal:
	//So far, (on Sept 13 2011), this is only used in a sanity test.
	public boolean isEqual(Turn other) {
		if(other.getMovesFor1Turn().size() == moves.size()) {
		
			for(int i=0; i<moves.size(); i++) {
				if(this.moves.get(i).isEqual(other.getMovesFor1Turn().get(i))) {
					continue;
				} else {
					return false;
				}
			}
			
			return true;
		} else {
			return false;
		}
		
	}
}
