package checkersSetup;

//TODO: I think that there's a bug in the combo moves where the gui does 2 moves at once and confuses the user.
// I dont think so anymore...
public class Move {
	
	Coordinate spos;
	Coordinate fpos;
	boolean jump;
	boolean kingMeThisMove;
	int captured;
	
	//A jump:
	public Move(Coordinate spos, Coordinate fpos, int captured, CheckersSetup setup, boolean isKing) {
		this.jump=true;
		this.spos=spos;
		this.fpos=fpos;
		this.captured = captured;
		this.kingMeThisMove = false;
		if(isKing == false) {
			this.kingMeThisMove = isPieceAtEdgeOfBoard(setup, this);
		}
	}
	
	//A move:
	public Move(Coordinate spos, Coordinate fpos, CheckersSetup setup, boolean isKing) {
		this.jump=false;
		this.spos=spos;
		this.fpos=fpos;
		this.kingMeThisMove = false;
		if(isKing == false) {
			this.kingMeThisMove = isPieceAtEdgeOfBoard(setup, this);
		}
	}
	
	public int getCaptured() {
		return captured;
	}
	
	public boolean isJump() {
		return jump;
	}
	
	public Coordinate getStartPos() {
		return spos;
	}
	
	public Coordinate getEndPos() {
		return fpos;
	}
	
	public boolean gotPromotedThisMove() {
		return kingMeThisMove;
	}
	
	public static Coordinate getCapturedLocation(Coordinate spos, Coordinate fpos) {
		return new Coordinate((spos.getRow() + fpos.getRow())/2, (spos.getCol() + fpos.getCol())/2);
	}
	
	
	//post: returns true if the piece lands on either end of the board
	// and return true if a piece was promoted. (false otherwise)
	private static boolean isPieceAtEdgeOfBoard(CheckersSetup setup, Move move) {
		boolean isPromoted = false;
		
		//If a piece moves into the egde of the board, then it must be king.
		//(either it's already a king or it just got promoted as one)
		if(( move.fpos.getRow() == 0 || move.fpos.getRow() == setup.getPiecesOnBoard().length-1)) {
			isPromoted = true;
		}
		
		return isPromoted;
	}
	
	public String toString() {
		String ret = "(row " + (this.getStartPos().getRow()+1) + ", col " + (this.getStartPos().getCol()+1) + ")" + " ";
		ret += " to (row " + (this.getEndPos().getRow()+1) + ", col " + (this.getEndPos().getCol()+1) + ")";
		return ret;
	}
	
	//Checks if 2 moves are equal:
	public boolean isEqual(Move other) {
		if(this.spos.isEqual(other.spos) && this.fpos.isEqual(other.fpos) && this.jump == other.jump && this.kingMeThisMove == other.kingMeThisMove && this.captured == other.captured) {
			return true;
		} else {
			return false;
		}
	}
}
