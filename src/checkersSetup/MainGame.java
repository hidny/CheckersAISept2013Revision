package checkersSetup;

//TODO: Add a record class that keeps a record of all moves.
//TODO: Add a toString method in the record class.
//TODO: Add a computerAI.
//class Computer:
//method: run
//method: shuffle choices (make for varying games even amongs 2 computerAI.
//class Program:
//Holds a whole simple program that WILL terminate and WILL decide on a move.
//Change Rules
//This should deal with how the AI program should change.
//Some ideas:
//Only change after a loss.
//Only change things the AI has touched.
//??Change things where Comp believes he has made a mistake? (What humans do...)
//TODO: HumanIntuitionMethods (static class with things that I would see when gazing at a checkers board.
//TODO: Add option in args to cut off expensive display of Board when it's computers vs computers.

//FUTURE:
//TODO: In far future: changeRulesOfChangeRules
//TODO: try to make comps use very different schools of thought about checkers. (make it interesting.)
//TODO: make computer "master" the game. (will need to cut off ALOT of redundancy.)

public class MainGame
{
  public static void main(String args[])
  {
	  //TODO: make sure that the AI with tree always does the same thing as the AI with mem. (As it should!)
	  //MachineVsMachine();
	  ManVsMachine();
  }
  
  public static void AIvsAI() {
	  Game game = new Game();
	  	
	  	Player p2 = new checkersAI.SimpleComputerAIwithTree(false, 6, new checkersUtility.UtilityAtEquilibriumPos(new checkersUtility.UtilityKingWantsCenter()), false);	
	  	Player p1 = new checkersAI.ComputerAIAlphaBetaPrune(false, 10, new checkersUtility.UtilityAtEquilibriumPos(new checkersUtility.UtilityKingWantsCenter()), false);
	  	Player winner;
	    
	    int p1wins = 0;
	    int p2wins = 0;
	    int draws = 0;
	   for(int i=0; i<10; i++) {
		    //if(i%2 == 0) {
		    	winner = game.playGame(p1, p2);
		    //} else {
		    	//winner = game.playGame(p2, p1);
		    //}
		    if(winner == p1) {
		    	System.out.println("Player 1 wins!");
		    	p1wins++;
		    } else if(winner == p2 ) {
		    	System.out.println("Player 2 wins!");
		    	p2wins++;
		    } else {
		    	System.out.println("Draw!");
		    	draws++;
		    }
	    }
	    
	    System.out.println("P1 wins: " + p1wins);
	    System.out.println("P2 wins: " + p2wins);
	    System.out.println("draws: " + draws);

  }
  
  public static void AIDrawAvoiderwithPrunevsAIWithDrawAvoider() {
	  Game game = new Game();
	  	
	  	Player p1 = new checkersAI.ComputerAIAlphaBetaPrune(false, 12, new checkersUtility.UtilityAtEquilibriumPos(new checkersUtility.UtilityKingWantsCenter()), false);
	  	Player p2 = new checkersAI.SimpleComputerAIwithTreeDrawAvoider(false, 5, new checkersUtility.UtilityAtEquilibriumPos(new checkersUtility.UtilityKingWantsCenter()), false);
	    Player winner;
	    
	    int p1wins = 0;
	    int p2wins = 0;
	    int draws = 0;
	    for(int i=0; i<10; i++) {
		    if(i%2 == 0) {
		    	winner = game.playGame(p1, p2);
		    } else {
		    	winner = game.playGame(p2, p1);
		    }
		    if(winner == p1) {
		    	System.out.println("Player 1 wins!");
		    	p1wins++;
		    } else if(winner == p2 ) {
		    	System.out.println("Player 2 wins!");
		    	p2wins++;
		    } else {
		    	System.out.println("Draw!");
		    	draws++;
		    }
	    }
	    
	    System.out.println("P1 wins (a/b Draw Avoider): " + p1wins);
	    System.out.println("P2 wins: (Simple Draw Avoider)" + p2wins);
	    System.out.println("draws: " + draws);

	   
  }
  public static void AIvsAIWithAlphaBetaPrune() {
	  	Game game = new Game();
	  	
	  	Player p1 = new checkersAI.ComputerAIAlphaBetaPrune(false, 10, new checkersUtility.UtilityAtEquilibriumPos(new checkersUtility.UtilityKingWantsCenter()), false);
	  	Player p2 = new checkersAI.SimpleComputerAIwithTree(false, 8, new checkersUtility.UtilityAtEquilibriumPos(new checkersUtility.UtilityKingWantsCenter()), false);
	    Player winner;
	    
	    int p1wins = 0;
	    int p2wins = 0;
	    int draws = 0;
	    for(int i=0; i<10; i++) {
		   // if(i%2 == 0) {
		    //	winner = game.playGame(p1, p2);
		    //} else {
		    	winner = game.playGame(p2, p1);
		    //}
		    if(winner == p1) {
		    	System.out.println("Player 1 wins!");
		    	p1wins++;
		    } else if(winner == p2 ) {
		    	System.out.println("Player 2 wins!");
		    	p2wins++;
		    } else {
		    	System.out.println("Draw!");
		    	draws++;
		    }
	    }
	    
	    System.out.println("P1 wins: " + p1wins);
	    System.out.println("P2 wins: " + p2wins);
	    System.out.println("draws: " + draws);

	    
  }
  
  public static void ManVsMan() {
	  Game game = new Game();
	    Player p1 = new Human();
	    Player p2 = new Human();
	    
	    Player winner = game.playGame(p1, p2);
	    

	    if(winner instanceof Human) {
	    	System.out.println("Man beats the machine!");
	    } else if(winner == p2 ) {
	    	System.out.println("The machine takes dominance over man!");
	    } else {
	    	System.out.println("Draw!");
	    }
  }
  
  public static void ManVsMachine() {
	  Game game = new Game();
	  	Player p1 = new Human();
	  	Player p2 = new checkersAI.ComputerAIAlphaBetaPruneDrawAvoider(false, 12, new checkersUtility.UtilityAtEquilibriumPos(new checkersUtility.UtilityKingWantsCenter()), false); 
		   
	    
	    Player winner = game.playGame(p1, p2);
	    

	    if(winner instanceof Human) {
	    	System.out.println("Player 1 wins!");
	    } else if(winner == p2 ) {
	    	System.out.println("Player 2 wins!");
	    } else {
	    	System.out.println("Draw!");
	    }
	    
  }

 /* public static void ManVsBadMachine() {
	  Game game = new Game();
	    Player p1 = new Human();
	    Player p2 = new checkersAI.SimpleAIwithMemory();
	    
	    Player winner = game.playGame(p1, p2);
	    

	    if(winner instanceof Human) {
	    	System.out.println("Man beats the machine!");
	    } else {
	    	System.out.println("The machine takes dominance over man!");
	    } else {
	    	System.out.println("Draw!");
	    }
	    
  }*/
  
  public static void MachineVsMachine() {
	  Game game = new Game();
	    Player p1 = new checkersAI.SimpleComputerAI();
	    Player p2 = new checkersAI.SimpleComputerAI();
	    
	    Player winner = game.playGame(p1, p2);
	    

	    if(winner == p1) {
	    	System.out.println("Player 1 wins!");
	    } else if(winner == p2 ) {
	    	System.out.println("Player 2 wins!");
	    } else {
	    	System.out.println("Draw!");
	    }
	    
  }
  
  public static void MachineVsRandom() {
	  Game game = new Game();
	    Player p1 = new checkersAI.SimpleComputerAI();
	    Player p2 = new randomAI.ComputerAIRand();
	    
	    Player winner = game.playGame(p1, p2);
	    

	    if(winner == p1) {
	    	System.out.println("Player 1 wins!");
	    } else if(winner == p2 ) {
	    	System.out.println("Player 2 wins!");
	    } else {
	    	System.out.println("Draw!");
	    }
	    
  }
  
  public static void SimpleAIvsSimpleAI() {
	  Game game = new Game();
	    Player p1 = new checkersAI.SimpleComputerAI();
	    Player p2 = new checkersAI.SimpleComputerAI();
	    
	    Player winner = game.playGame(p1, p2);
	    

	    if(winner == p1) {
	    	System.out.println("Player 1 wins!");
	    } else if(winner == p2 ) {
	    	System.out.println("Player 2 wins!");
	    } else {
	    	System.out.println("Draw!");
	    }
  }
  
  public static void AIvsAIWithEquilibriumUtil() {
	  Game game = new Game();
	    //Player p1 = new checkersAI.SimpleComputerAIwithTree(false, 6, new checkersUtility.SimpleUtility(), true);
	    
	    //TODO: when I compare this to AIwithMem, it doesn't give the same results. Why?
	    Player p1 = new checkersAI.SimpleComputerAIwithTree(false, 5, new checkersUtility.UtilityAtEquilibriumPos(new checkersUtility.SimpleUtility()), true);
	    Player p2 = new checkersAI.SimpleComputerAIwithTree(false, 5, new checkersUtility.SimpleUtility(), false);
	    Player winner;
	    
	    int p1wins = 0;
	    int p2wins = 0;
	    int draws = 0;
	    for(int i=0; i<10; i++) {
		    if(i%2 == 0) {
		    	winner = game.playGame(p1, p2);
		    } else {
		    	winner = game.playGame(p2, p1);
		    }
		    if(winner == p1) {
		    	System.out.println("Player 1 wins! UNEXPECTED!");
		    	System.err.println("WTFFFFF");
		    	p1wins++;
		    } else if(winner == p2 ) {
		    	System.out.println("Player 2 wins!");
		    	p2wins++;
		    } else {
		    	System.out.println("Draw!");
		    	draws++;
		    }
	    }
	    
	    System.out.println("P1 wins: " + p1wins);
	    System.out.println("P2 wins: " + p2wins);
	    System.out.println("draws: " + draws);

	   
  }
  
  public static void simpleAIvsComplexAI() {
	  Game game = new Game();
	    Player p1 = new checkersAI.SimpleComputerAI(false, 5, new checkersUtility.SimpleUtility(), false);
	    Player p2 = new checkersAI.SimpleComputerAIwithTree(false, 5, new checkersUtility.UtilityAtEquilibriumPos(new checkersUtility.UtilityKingWantsCenter()), false);
	    
	    Player winner = game.playGame(p1, p2);
	    

	    if(winner == p1) {
	    	System.out.println("Player 1 wins! UNEXPECTED! All that work making it better means nothing!");
	    	System.err.println("WTFFFFF");
	    } else if(winner == p2 ){
	    	System.out.println("Player 2 wins!");
	    } else {
	    	System.out.println("Draw!");
	    }
  }
  
  public static void AIwithAllHeuristicvsAIwantcenter() {
	  Game game = new Game();
	    Player p1 = new checkersAI.SimpleComputerAIwithTree(false, 8, new checkersUtility.UtilityAtEquilibriumPos(new checkersUtility.UtilitywithPnCheuristic()), false);
	    Player p2 = new checkersAI.SimpleComputerAIwithTree(false, 8, new checkersUtility.UtilityAtEquilibriumPos(new checkersUtility.UtilityKingWantsCenter()), false);
	    
	    Player winner = game.playGame(p1, p2);
	    

	    if(winner == p1) {
	    	System.out.println("Player 1 wins! UNEXPECTED! All that work making it better means nothing!");
	    	System.err.println("WTFFFFF");
	    } else if(winner == p2 ){
	    	System.out.println("Player 2 wins!");
	    } else {
	    	System.out.println("Draw!");
	    }
  }
  
  public static void AIDrawAvoiderWantCentervsAIwantcenterTourney() {
	  	Game game = new Game();
	    Player p1 = new checkersAI.SimpleComputerAIwithTreeDrawAvoider(false, 5, new checkersUtility.UtilityAtEquilibriumPos(new checkersUtility.UtilityKingWantsCenter()), false);
	    Player p2 = new checkersAI.SimpleComputerAIwithTree(false, 5, new checkersUtility.UtilityAtEquilibriumPos(new checkersUtility.UtilityKingWantsCenter()), false);
	    
	    int p1win=0;
	    int p2win=0;
	    int draw=0;
	    Player winner;
	    
	    for(int i=0; i<100; i++) {
	    	if(i%2 == 0) {
	    		winner = game.playGame(p1, p2);
	    	} else {
	    		winner = game.playGame(p2, p1);
	    	}
	
		    if(winner == p1) {
		    	System.out.println("Player 1 wins! UNEXPECTED! All that work making it better means nothing!");
		    	p1win++;
		    } else if(winner == p2 ){
		    	System.out.println("Player 2 wins!");
		    	p2win++;
		    } else {
		    	System.out.println("Draw!");
		    	draw++;
		    }
	    }
	    System.out.println("Tourney over!");
	    System.out.println("P1 wins: " + p1win);
	    System.out.println("P2 wins: " + p2win);
	    System.out.println("Draws: " + draw);
	    
  }
  
  public static void AIwithAllHeuristicvsAIwantpromotion() {
	  Game game = new Game();
	    Player p1 = new checkersAI.SimpleComputerAIwithTree(false, 8, new checkersUtility.UtilityAtEquilibriumPos(new checkersUtility.UtilitywithPnCheuristic()), false);
	    Player p2 = new checkersAI.SimpleComputerAIwithTree(false, 8, new checkersUtility.UtilityAtEquilibriumPos(new checkersUtility.WantPromotionUtility()), false);
	    
	    Player winner = game.playGame(p1, p2);
	    

	    if(winner == p1) {
	    	System.out.println("Player 1 wins! UNEXPECTED! All that work making it better means nothing!");
	    	System.err.println("WTFFFFF");
	    } else if(winner == p2 ){
	    	System.out.println("Player 2 wins!");
	    } else {
	    	System.out.println("Draw!");
	    }
  }
  
  public static void AIwithAllHeuristicvsSimpleAI() {
	  Game game = new Game();
	    Player p1 = new checkersAI.SimpleComputerAIwithTree(false, 9, new checkersUtility.UtilityAtEquilibriumPos(new checkersUtility.UtilitywithPnCheuristic()), false);
	    Player p2 = new checkersAI.SimpleComputerAI(false, 7, new checkersUtility.SimpleUtility(), false);
	    
	    Player winner = game.playGame(p1, p2);
	    

	    if(winner == p1) {
	    	System.out.println("Player 1 wins! UNEXPECTED! All that work making it better means nothing!");
	    	System.err.println("WTFFFFF");
	    } else if(winner == p2 ){
	    	System.out.println("Player 2 wins!");
	    } else {
	    	System.out.println("Draw!");
	    }
  }
  
  public static void AIDrawAvoidervsAI() {
	  Game game = new Game();
	    Player p1 = new checkersAI.SimpleComputerAIwithTreeDrawAvoider(false, 8, new checkersUtility.UtilityAtEquilibriumPos(new checkersUtility.UtilityKingWantsCenter()), true);
	    Player p2 = new checkersAI.SimpleComputerAIwithTree(false, 8, new checkersUtility.UtilityAtEquilibriumPos(new checkersUtility.UtilityKingWantsCenter()), false);
	    
	   Player winner;
	    
	   	winner = game.playGame(p1, p2);
	    
	   	if(winner == p1) {
		    System.out.println("Player 1 wins!");
		} else if(winner == p2 ){
		    System.out.println("Player 2 wins!");
		} else { 	
		    System.out.println("Draw!");
		}
  }
}