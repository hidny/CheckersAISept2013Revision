package checkersSetup;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/*TO DO: Create a board listener and implement (kind of) the getClick() method.*/

//2011 TODO: make the gui better by showing the user that he/she selected a block by highlighting it.
  
public class BoardPanel extends JPanel implements MouseListener
  
{
	

	  /**
	 * I hate warnings and this suppresses them:
	 */
	private static final long serialVersionUID = 1L;
	//End suppressing warnings
	
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
  
  // Some constants for drawing the board.
  // You may need more, and may need to alter
  // these values for some of the extensions.
  public static final int X_DIM = 30;//30
  public static final int Y_DIM = 30;//30
  public static final int X_OFFSET = 20;
  public static final int Y_OFFSET = 20;
  
  private int rows;
  private int cols;

  private String messageBoard; /*The message board on the bottom of the board*/
  private Color pegsColour[][];/*Records the colours of the pegs*/
  
  private boolean pegPlaced[][];/*Says if there's a peg placed at every area on the board.*/
  
  private boolean kingPlaced[][];/*Says if there's a peg placed at every area on the board.*/
  
  
// Grid colours
  public static final Color GRID_COLOR_A = new Color(84,137,139);
  public static final Color GRID_COLOR_B = new Color(103,156,158);
  
  //I don't use it... but I might in the future...
  //private Graphics canvas;
  
  public BoardPanel(int rows, int columns)
  {
    this.setSize(2 * X_OFFSET + columns * X_DIM, 2 * Y_OFFSET + rows * Y_DIM);
    this.rows =  rows;
    this.cols = columns;
    
    /*Initialising the pegs colours.*/
    pegsColour = new Color[rows][cols];
    
    for(int i=0; i<rows; i++) {
      for(int j=0; j<cols; j++) {
        pegsColour[i][j] = BLACK; //Default colour
      }
    }
    
    pegPlaced = new boolean[rows][cols];
    kingPlaced = new boolean[rows][cols];
    
    for(int i=0; i<rows; i++) {
      for(int j=0; j<cols; j++) {
        pegPlaced[i][j] = false; //Default colour
        kingPlaced[i][j] = false;
      }
    }
    
    
  }
  
  public void paintComponent(Graphics canvas)
  {
	 canvas.setColor(Color.WHITE);
	 
	 //Make sure the outside of the board is white.
	 canvas.fillRect(0, 0, this.cols * X_DIM + 2*X_OFFSET, this.rows * Y_DIM + 2*Y_OFFSET);
	 
    /*Placing the squares on the board*/
    for(int i=0; i<this.rows; i++) {
      for(int j=0; j<this.cols; j++) {
        if((i+j)%2 == 1) {
          canvas.setColor(GRID_COLOR_A);
        } else {
          canvas.setColor(GRID_COLOR_B);
        }
        canvas.fillRect(X_OFFSET + j * X_DIM, Y_OFFSET + i * Y_DIM, X_DIM, Y_DIM);
      }
    }
   
    /*Placing the pegs: */
    // CHANGE THIS DISPLAY FUNCTION
    for(int i=0; i<this.rows; i++) {
      for(int j=0; j<this.cols; j++) {
        if(this.pegPlaced[i][j]) {
          canvas.setColor(this.pegsColour[i][j]);
          canvas.fillOval(X_OFFSET + j * X_DIM + X_DIM/4, Y_OFFSET + i * Y_DIM + Y_DIM/4, X_DIM/2, Y_DIM/2);
        } else if(this.kingPlaced[i][j]) {
        	
        	//Draw something that makes it more king looking...:
        	//Encircle it with black! (lazy solution...)
        	canvas.setColor(BLACK);
        	canvas.fillOval(X_OFFSET + j * X_DIM + X_DIM/8, Y_OFFSET + i * Y_DIM + Y_DIM/8, (X_DIM * 6)/8, (Y_DIM*6)/8);
        	//TODO: Make kings look better. (optional)
        	
        	canvas.setColor(this.pegsColour[i][j]);
        	canvas.fillOval(X_OFFSET + j * X_DIM + X_DIM/4, Y_OFFSET + i * Y_DIM + Y_DIM/4, X_DIM/2, Y_DIM/2);
            
        }
      }
    }
    
    
    /*I want the Turkey */ //canvas.setFont(
    /*Printing the display board*/
    canvas.setColor(BLACK);
    if(messageBoard != null) {
      canvas.drawString(messageBoard, X_OFFSET, Y_OFFSET + this.rows * Y_DIM + Y_DIM);
    }
    
  }
  
  public void putPeg(Color theColour, int row, int column)
  { 
    if(row >= 0 && row < this.rows && column >= 0 && column < this.cols) {
      this.pegPlaced[row][column] = true;
      //Demote a king to a peg... it might happen...
      this.kingPlaced[row][column] = false;
      this.pegsColour[row][column] = theColour;
      repaint();
    } else {
      System.out.println("The put peg method is out of bounds!");
    }    
  }
  
  //Promote a Peg to a King.
  public void putKing(Color theColour, int row, int column)
  { 
    if(row >= 0 && row < this.rows && column >= 0 && column < this.cols) {
      this.pegPlaced[row][column] = false;
      this.kingPlaced[row][column] = true;
      this.pegsColour[row][column] = theColour;
      repaint();
    } else {
      System.out.println("The put peg method is out of bounds!");
    }    
  }
  
  public void removePeg(int row, int column)
  {
    if(row >= 0 && row < this.rows && column >= 0 && column < this.cols) {
      this.pegPlaced[row][column] = false;
      this.kingPlaced[row][column] = false;
      repaint();
    } else {
      System.out.println("The remove peg method is out of bounds!");
    }
  }
  

  
  public void displayMessage(String theMessage)
  {
    messageBoard = theMessage;
    repaint();
  }
  
  Coordinate coordinateThatTheUserPressedOn;/*Coordinate of the mouse clicked*/  
  Coordinate coordinateThatTheUserReleasedOn;
  boolean coordinatePressed = false; /*A flag that says when the coordinate was been pressed.*/
  
  
   boolean coordinateClicked = false; /*A flag that says when the coordinate was been clicked.*/
   
   /* @returns the coordinate that the user clicked on... If the coordinate is null, there will be some problems.*/
   public Coordinate getCoordinateThatTheUserClickedOn() {
	   if(coordinateClicked) {
		   return coordinateThatTheUserPressedOn;
	   } else {
		   return null;
	   }
   }
   /*@returns true if the user clicked a legal coordinate
    * false otherwise
    */
   public boolean hasClickedonCoordinate() {
    return  coordinateClicked;
   }
   
   /*After the coordinate has been used, the fact that the user clicked on a specific coordinate
    * doesn't matter anymore.*/
   public void CoordinateUsed() {
	 this.coordinatePressed = false;
	 this.coordinateThatTheUserPressedOn = null;
     this.coordinateClicked = false;
     this.coordinateThatTheUserPressedOn = null;
   }
   /*
    * 
    * 
  /*Methods implementing the mouse listener:*/
  
  /* The most important one is mouse clicked*/

  /**
   * Invoked when a the user clicks on the window
   * @param The mouse listener must be on.
   */
   
  public void  mouseClicked(MouseEvent e) {
 
  }  
  public void  mouseEntered(MouseEvent e) {
          
  }
  public void  mouseExited(MouseEvent e) {
          
  }
  public void  mousePressed(MouseEvent e) {
	  int x = e.getX();/*gets the x and y coordinate of the click*/
	    int y = e.getY();
	    int col = (e.getX() - X_OFFSET) /X_DIM;
	    int row = (e.getY() - Y_OFFSET) /Y_DIM;
	    
	    System.out.println("MOUSE PRESSED!");
	    
	    /*those coordinates get transformed into the proper Coordinates:*/
	    if( x>=X_OFFSET && col< cols && y>=Y_OFFSET && row<rows ) { /*if it's a proper coordinate*/
	      coordinateThatTheUserPressedOn = new Coordinate(row, col); /*registering the coordinate*/
	      coordinatePressed = true;  /*instance that declares that the user has clicked on a coordinate. (if it is true)*/
	    }
  }
  public void  mouseReleased(MouseEvent e) {
	  int x = e.getX();/*gets the x and y coordinate of the click*/
	    int y = e.getY();
	    int col = (e.getX() - X_OFFSET) /X_DIM;
	    int row = (e.getY() - Y_OFFSET) /Y_DIM;
	    
	    System.out.println("MOUSE RELEASED!");
	    
	     /*those coordinates get transformed into the proper Coordinates:*/
	    if( coordinatePressed && x>=X_OFFSET && col< cols && y>=Y_OFFSET && row<rows ) { /*if it's a proper coordinate*/
	    	coordinateThatTheUserReleasedOn = new Coordinate(row, col);
	    	if(coordinateThatTheUserPressedOn.equals(coordinateThatTheUserPressedOn)) {
	    		coordinateClicked = true;  /*instance that declares that the user has clicked on a coordinate. (if it is true)*/
	    	}	
	    }
	    
	   /* System.out.println(coordinateThatTheUserPressedOn);
	    if(coordinateClicked) {
	    	System.out.println("You clicked!");
	    } else {
	    	System.out.println("You didn't click!");
	    }*/
  } 
  
}