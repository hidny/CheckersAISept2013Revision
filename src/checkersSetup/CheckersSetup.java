package checkersSetup;

import java.util.*;

//This is a model/abstraction of a checker board with the pieces on it.

//Note: if a piece jumped to become king, it can't jump again. (My game goes by that rule because the wiki page I read said it.)

public class CheckersSetup {
	
	private int piecesOnBoard[][];
	
	//created sept 2011:
	public int[][] getPiecesOnBoard() {
		return piecesOnBoard;
	
	}
	
	//Board size must be even >=4
	public static final int NORMAL_SIZE_OF_BOARD = 8; //The board size now is 8x8 .
	public static final int NUM_WHITE_PIECES = (NORMAL_SIZE_OF_BOARD*(NORMAL_SIZE_OF_BOARD-2))/4;
	
	
	public static final int EMPTY_SPACE = 0;
	public static final int DARK_PIECE = 1;
	public static final int DARK_KING = 2;
	public static final int WHITE_PIECE = -1;
	public static final int WHITE_KING = -2;
	
	public static boolean isDarkPiece(int piece) {
		return piece > 0;
	}
	
	public static boolean isWhitePiece(int piece) {
		return piece < 0;
	}
	
	public static boolean isEmpty(int piece) {
		return EMPTY_SPACE == piece;
	}
	
	public static boolean isKing(int piece) {
		return piece == DARK_KING || piece == WHITE_KING;
	}
	
	
	public CheckersSetup() {
		this(NORMAL_SIZE_OF_BOARD, NORMAL_SIZE_OF_BOARD);
		
	}
	
	//pre: rows is a multiple of 2 greater than or equal to 4
	//		and columns is greater than 1
	public CheckersSetup(int rows, int cols) {
		//This could hold only half the spaces... but I'm lazy...
		if(rows % 2 != 0) {
			System.out.println("ERROR: Cannot accept an odd number of rows");
			System.exit(1);
		} else if( rows <=2 ) {
			System.out.println("ERROR: too few rows.");
			System.exit(1);
		}
		
		piecesOnBoard= new int[rows][cols];
		
		//Add Black pieces
		int addIndex =0;
		
		for(int i=rows/2+1; i<rows; i++) {
		      for(int j=0; j<cols; j++) {
		        //Dark squares are where the pieces are:
		    	if((i+j)%2 == 1) {
		    		
		    		//Put the piece on the board array:
		    		piecesOnBoard[i][j] = DARK_PIECE;
		    		
		    		addIndex++;
		        }
		      }
		 }
		//Add White pieces
		addIndex=0;
		
		 for(int i=0; i<rows/2 -1; i++) {
		      for(int j=0; j<cols; j++) {
		        //Dark squares are where the pieces are:
		    	if((i+j)%2 == 1) {
		    		//Put the piece on the board array:
		    		piecesOnBoard[i][j] = WHITE_PIECE;
		    		
		    		addIndex++;
		        }
		      }
		    }
		 
	}
	
	
	public ArrayList<Move> getMoves(boolean isDarkPlayer) {

		ArrayList<Move> moves = getAttackMoves(isDarkPlayer);
		
		if(moves.isEmpty()) {
			moves = getNonAttackMoves(isDarkPlayer);
		}
		
		return moves;
	}
	
	
	ArrayList<Coordinate> getPiecesFromBoard(boolean isDarkPieces) {
		ArrayList<Coordinate> pieces = new ArrayList<Coordinate>();
		for(int i=0; i<piecesOnBoard.length; i++) {
			for(int j=0; j<piecesOnBoard[0].length; j++) {
				if( (isDarkPieces && isDarkPiece(piecesOnBoard[i][j])) ||( !isDarkPieces && isWhitePiece(piecesOnBoard[i][j])) ) {
					pieces.add(new Coordinate(i, j));
				}
			}
		}
		return pieces;
		
	}
	
	
	public ArrayList<Move> getAttackMoves(boolean isDarkPlayer) {
		ArrayList<Move> moves = new ArrayList<Move>();
		
		ArrayList<Coordinate> pieces = getPiecesFromBoard(isDarkPlayer);
		
		//loop to see how many jumping choices there are.
		for(int i=0; i<pieces.size(); i++) {
			moves.addAll(getJumpsFromCoordinate(pieces.get(i)));
		}
		
		return moves;
	}
	
	
	public ArrayList<Move> getNonAttackMoves(boolean isDarkPlayer) {
		ArrayList<Move> moves = new ArrayList<Move>();
		ArrayList<Coordinate> pieces = getPiecesFromBoard(isDarkPlayer);
		
		for(int i=0; i<pieces.size(); i++) {
			
			moves.addAll(getNonJumpMovesFromCoordinate(pieces.get(i)));
			
		}
		
		return moves;
	}
	
	
	//NOTE: so far, this function is only used by the human class.
	//pre: A list of all legal moves.
	//post: return the number of moves available if at the coordinates the human player selected.
	ArrayList<Move> getMovesFromStartLocation(ArrayList<Move> choices, Coordinate begin) {
		ArrayList<Move> choicesFromClick = new ArrayList<Move>();
		
	    for(int i=0; i<choices.size(); i++) {
	    	if(choices.get(i).getStartPos().getCol() == begin.getCol() && choices.get(i).getStartPos().getRow() == begin.getRow()) {
	    		choicesFromClick.add(choices.get(i));
	    	}
	    }
	   
	    return choicesFromClick;
	}
	
	
	//pre: must be a valid move.
	//Makes a move
	//returns other moves options if the move was a jump and the piece could do a multi-jump.
	public ArrayList<Move> makeMove(Move move) {
		Coordinate startP = move.getStartPos();
		Coordinate endP = move.getEndPos();
		Coordinate deadPiece;
		
		//Copy the piece over to another square
		piecesOnBoard[endP.getRow()][endP.getCol()] = piecesOnBoard[startP.getRow()][startP.getCol()];
		
		//set previous square to null
		piecesOnBoard[startP.getRow()][startP.getCol()] = 0;
		
		//Check if the piece jumped and if so, take away captured piece.
		if(move.isJump()) {
			deadPiece = Move.getCapturedLocation(move.getStartPos(), move.getEndPos());
			piecesOnBoard[deadPiece.getRow()][deadPiece.getCol()] = 0;
		}
		
		//Turn promoted piece into a king:
		if(move.gotPromotedThisMove()) {
			piecesOnBoard[endP.getRow()][endP.getCol()] *=2;
		}
		
		//SANITY CHECK
		if(this.piecesOnBoard[endP.getRow()][endP.getCol()] > 2 || this.piecesOnBoard[endP.getRow()][endP.getCol()] < -2) {
			System.out.println("ERROR: promoted a king or something and the pieces on board table got non-applicable values!");
		}
		//END SANITY CHECK
		
		
		//return next possible moves. note: You can't jump after getting promoted!
		if(move.isJump() && move.gotPromotedThisMove() == false) {
			return this.getJumpsFromCoordinate(new Coordinate(endP.getRow(), endP.getCol()));
		} else {
			return new ArrayList<Move>();
		}
		
	}
	
	

	//Reverse a move
	//pre: must be a valid move to reverse.
	public void reverseMove(Move move) {
		Coordinate startP = move.getStartPos();
		Coordinate endP = move.getEndPos();
		Coordinate deadPiece;
		
		//Put the piece over to the original square
		piecesOnBoard[startP.getRow()][startP.getCol()] = piecesOnBoard[endP.getRow()][endP.getCol()];
		
		//set the jump square to null
		piecesOnBoard[endP.getRow()][endP.getCol()] = 0;
		
		
		//Check if the piece jumped and if so, put back the captured piece.
		if(move.isJump()) {
			deadPiece = Move.getCapturedLocation(move.getStartPos(), move.getEndPos());
			piecesOnBoard[deadPiece.getRow()][deadPiece.getCol()] = move.getCaptured();
		}
		
		//check for promotion
		if(move.gotPromotedThisMove() == true) {
			//Demote piece:
			piecesOnBoard[startP.getRow()][startP.getCol()] /= 2;
		}
	}
	
	//pre: the position is assumed to not be empty
	//post: returns all jumps from the coordinate and records if the piece get promoted after the jump.
	public ArrayList<Move> getJumpsFromCoordinate(Coordinate position) {
		
		
		ArrayList<Move> move = new ArrayList<Move>();
		
		int row = position.getRow();
		int col = position.getCol();
		boolean isDarkPlayer= isDarkPiece(piecesOnBoard[position.getRow()][position.getCol()]);
		boolean isKing = isKing(piecesOnBoard[position.getRow()][position.getCol()]);
		
		//Check if I could attack top left:
		if(col>1 && row>1 && isEmpty(piecesOnBoard[row-1][col-1]) == false  && isEmpty(piecesOnBoard[row-2][col-2]) && (isDarkPlayer || isKing)) {
			if(isDarkPiece(piecesOnBoard[row-1][col-1]) != isDarkPlayer) {
				move.add(new Move(new Coordinate(row, col), new Coordinate(row-2, col-2), piecesOnBoard[row-1][col-1], this, isKing));
			}
		}
		
		//Check if I could attack top right:
		if(col<piecesOnBoard[0].length-2 && row>1 && isEmpty(piecesOnBoard[row-1][col+1]) == false  && isEmpty(piecesOnBoard[row-2][col+2]) && (isDarkPlayer || isKing) ) {
			if(isDarkPiece(piecesOnBoard[row-1][col+1]) != isDarkPlayer) {
				move.add(new Move(new Coordinate(row, col), new Coordinate(row-2, col+2), piecesOnBoard[row-1][col+1], this, isKing));
			}
		}
		
		//Check if I could attack bottom left:
		if(col>1 && row<piecesOnBoard.length-2 && isEmpty(piecesOnBoard[row+1][col-1]) == false  && isEmpty(piecesOnBoard[row+2][col-2]) && (isDarkPlayer==false || isKing)) {
			if(isDarkPiece(piecesOnBoard[row+1][col-1]) != isDarkPlayer) {
				move.add(new Move(new Coordinate(row, col), new Coordinate(row+2, col-2), piecesOnBoard[row+1][col-1], this, isKing));
			}
		}
			
		//Check if I could attack bottom right:
		if(col<piecesOnBoard[0].length-2 && row<piecesOnBoard.length-2 && isEmpty(piecesOnBoard[row+1][col+1]) == false  && isEmpty(piecesOnBoard[row+2][col+2]) && (isDarkPlayer==false || isKing)) {
			if(isDarkPiece(piecesOnBoard[row+1][col+1]) != isDarkPlayer) {
				move.add(new Move(new Coordinate(row, col), new Coordinate(row+2, col+2), piecesOnBoard[row+1][col+1], this, isKing));
			}
		}
		
		return move;
	}
	
	//pre: the position is assumed to not be empty
	//post: returns the non-jump moves and record if the piece has been promoted.
	public ArrayList<Move> getNonJumpMovesFromCoordinate(Coordinate position) {

		ArrayList<Move> moves= new ArrayList<Move>();
		int row = position.getRow();
		int col = position.getCol();
		boolean isDarkPlayer = isDarkPiece(piecesOnBoard[row][col]);
		boolean isKing = isKing(piecesOnBoard[row][col]);
		
		
		//Check if I could move to top left:
		if(col>0 && row>0 && isEmpty(piecesOnBoard[row-1][col-1])  && (isDarkPlayer || isKing)) {
				moves.add(new Move(new Coordinate(row, col), new Coordinate(row-1, col-1), this, isKing));
		}
		
		//Check if I could  move to top right:
		if(col<piecesOnBoard[0].length-1 && row>0 && isEmpty(piecesOnBoard[row-1][col+1]) && (isDarkPlayer || isKing) ) {
				moves.add(new Move(new Coordinate(row, col), new Coordinate(row-1, col+1), this, isKing));
		}
		
		//Check if I could move to bottom left:
		if(col>0 && row<piecesOnBoard.length-1 && isEmpty(piecesOnBoard[row+1][col-1]) && (isDarkPlayer==false || isKing)) {
				moves.add(new Move(new Coordinate(row, col), new Coordinate(row+1, col-1), this, isKing));
		}
			
		//Check if I could move to bottom right:
		if(col<piecesOnBoard[0].length-1 && row<piecesOnBoard.length-1 && isEmpty(piecesOnBoard[row+1][col+1]) && (isDarkPlayer==false || isKing)) {
				moves.add(new Move(new Coordinate(row, col), new Coordinate(row+1, col+1), this, isKing));
		}
		
		return moves;
	}
	
	//post: Returns all checkers positiofns after 1 turn.
	// if there no force capture, just return the list of moves,
	// else: returns all the possible 1st jumps.
	
	
	ArrayList<Move> getAllPosibleMovesForCurrentPlayer(boolean isDarkPlayer) {
		ArrayList<Move> firstAttackMoves = getAttackMoves(isDarkPlayer);
		ArrayList<Move> moveList;
		if(firstAttackMoves.isEmpty()) {
			moveList = getNonAttackMoves(isDarkPlayer);
			
		} else {
			return firstAttackMoves;
		}
		
		return moveList;
	}
	
	public void playTurn(Turn turn) {
		ArrayList<Move> moves = turn.getMovesFor1Turn();
		for(int i=0; i<moves.size();i++) {
			this.makeMove(moves.get(i));
		}
	}
	
	public void reverseTurn(Turn turn) {
		ArrayList<Move> moves = turn.getMovesFor1Turn();
		for(int i=0; i<moves.size();i++) {
			this.reverseMove(moves.get(moves.size() - 1 - i));
		}
	}
	
	public String toString() {
		checkersUtility.Utility utilityFunc = new checkersUtility.SimpleUtility();
		String ret = "";
		if(piecesOnBoard != null && piecesOnBoard.length > 0) {
			for(int i=0; i<piecesOnBoard.length; i++) {
				for(int j=0; j<piecesOnBoard[0].length; j++) {
					//Make the spacing good:
					if(piecesOnBoard[i][j] < 0) {
						ret += " " + piecesOnBoard[i][j];
					} else {
						ret += "  " + piecesOnBoard[i][j];
					}
				}
				ret += "\n";
			}
			boolean whatever= false;
			ret += "Dark advantage: " + utilityFunc.getEstimatedUtilityForDarkPlayer(this, whatever);
		} else {
			ret = "";
		}
		return ret;
	}
	
	//pre: setup is a board of size greater than 
	public static CheckersSetup makeHardCopy(CheckersSetup setup) {
		if(setup.piecesOnBoard.length == 0) {
			System.out.println("ERROR: trying to copy an empty board!");
			System.exit(1);
		}
		
		CheckersSetup ret = new CheckersSetup(setup.piecesOnBoard.length, setup.piecesOnBoard[0].length);
		for(int i=0; i<ret.piecesOnBoard.length; i++) {
			for(int j=0; j<ret.piecesOnBoard[0].length; j++) {
				ret.piecesOnBoard[i][j] = setup.piecesOnBoard[i][j];
			}
		}
		
		return ret;
	}
}


