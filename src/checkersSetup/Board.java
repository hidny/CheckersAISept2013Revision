package checkersSetup;

import java.awt.*;

import javax.swing.JFrame;

/*TO DO: Create a board listener and implement (kind of) the getClick() method.*/
public class Board
{
  // Some colour presets
  public static final Color YELLOW = Color.YELLOW;
  public static final Color BLUE = Color.BLUE;
  public static final Color CYAN = Color.CYAN;
  public static final Color GREEN = Color.GREEN;
  public static final Color PINK = Color.PINK;
  public static final Color BLACK = Color.BLACK;
  public static final Color WHITE = Color.WHITE;
  public static final Color RED = Color.RED;
  public static final Color ORANGE = Color.ORANGE;
  
  // Instance variables
  Frame frame;
  BoardPanel panel;
  
	public static int TITLE_BAR_HEIGHT = 25;/*The approximate Title bar size*/
	
	
  // Other instance variables
  int rows; /*****Maybe I could refer to the Board Panels rows and cols... (maybe)*/
  int columns;
  
  public static void main(String args[]) {
   Board b = new Board(10, 10);
   b.displayMessage("Hello World! I wish I was a different font...");
   b.putPeg(CYAN, 7, 1);
   b.putPeg(GREEN, 1, 2);
   b.removePeg(1, 2);
   for(int i=0; i<2; i++) {
     for(int j=0; j<5; j++) {
       b.putPeg(RED, i, j);
     }
   }
   
   boolean imNotBored = true;
   
   while(imNotBored == true) {
         Coordinate c =  b.waitForClick();
         System.out.println("You have clicked on ("+ c.getRow() + ", "+ c.getCol() + ")");
   }
  }
  
  
  public Board(int rows, int columns)
  {
    this.rows = rows;
    this.columns = columns;
    
	frame = new JFrame("Michael's Checkers AI");
	
	panel = new BoardPanel(rows, columns);
	frame.add(panel);
	
	
	/*sets the size of the window.*/
	frame.setSize(2 * BoardPanel.X_OFFSET + columns * BoardPanel.X_DIM, 2 * BoardPanel.Y_OFFSET + rows * BoardPanel.Y_DIM + TITLE_BAR_HEIGHT);
    
	//2013: exit on close buttom for the win!
	((JFrame) frame).setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	frame.setResizable(false);
	frame.setVisible(true);
	
	
  }
  
  public void displayMessage(String theMessage)
  {
    panel.displayMessage(theMessage);
  }
  
  public void putPeg(Color theColour, int row, int col)
  {
	  panel.putPeg(theColour, row, col);
  }
  
  public void putKing(Color theColour, int row, int col)
  {
	  panel.putKing(theColour, row, col);
  }
  
  public void removePeg(int row, int col)
  {
	  panel.removePeg(row, col);
  }
  
  public int getColumns()
  {
    return this.columns;
  }
  
  public int getRows()
  {
    return this.rows;
  }
  
  
  /*
  * Calls on the listeners to wait for the user to click on a square.
  * Once the user has clicked on a square
  * 
  * I honestly don't understand how I should use the Board Listener... :(
  * I just use the mouse listener..
  * 
  * @return the coordinates of the square clicked.
  */
  
  public Coordinate waitForClick() {
     /*adding the mouse listener*/
    
    //this.addBoardListener(this); ???
    
	  panel.addMouseListener(panel);
    
    /*This while loop simply waits for the user to click on a coordinate.*/
    while(panel.hasClickedonCoordinate() == false) {
      /*Do nothing.*/
    	try {
    		Thread.sleep(100);
    	} catch(Exception e) {
    		System.err.println("Tread sleep didn't work.");
    		System.exit(1);
    	}
    }
    
    /*Since the user has clicked on coordinate, the program should no longer be listening for another click. Therefore, i'll turn the listener off.*/
    panel.removeMouseListener(panel);
    
   //this.removeBoardListener(this); ??
    
    if(panel.getCoordinateThatTheUserClickedOn() != null) { /*This Condition that should always be true (unless there's a bug):*/
      
    	Coordinate coordinateClicked = panel.getCoordinateThatTheUserClickedOn(); /*gets the coordinate clicked*/
     	panel.CoordinateUsed(); /*Tells the board panel that the coordinate that the user clicked has been saved.*/
     
     	return coordinateClicked; 
    
    } else {
    	System.out.println("ERROR: The coordinate is equal to null!"); 
    	panel.CoordinateUsed();
    	return new Coordinate(-1, -1); /*These coordinates indicate that there's a problem.*/
    }
  }
  
  public void displayBoard(CheckersSetup setup) {  
		
	  int piecesOnBoard[][] = setup.getPiecesOnBoard();
	  
		//remove all pegs from prev board.
		for(int i=0; i<this.rows; i++) {
			for(int j=0; j<this.columns; j++) {
				this.removePeg(i, j);
			}
		}
		
		//Display based on board array:
		for(int i=0; i<this.rows; i++) {
			for(int j=0; j<this.columns; j++) {
				if(CheckersSetup.isEmpty(piecesOnBoard[i][j])) continue;
				
				if(CheckersSetup.isDarkPiece(piecesOnBoard[i][j])) {
					if(CheckersSetup.isKing(piecesOnBoard[i][j])) {
						this.putKing(Board.RED, i, j);
					} else {
						this.putPeg(Board.RED, i, j);
					}
				} else {
					if(CheckersSetup.isKing(piecesOnBoard[i][j])) {
						this.putKing(Board.WHITE, i, j);
					} else {
						this.putPeg(Board.WHITE, i, j);
					}
				}
				
			}
		}
	}
  
  public void closeWindow() {
	  frame.dispose();
  }
  
}

