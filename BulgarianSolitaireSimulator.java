/* @author: Jianyang Zhang */

package projectB;

import java.util.Scanner;

/** Simulate a solitaire board game in console */
public class BulgarianSolitaireSimulator {

   public static void main(String[] args) {
     
      boolean singleStep = false;
      boolean userConfig = false;

      for (int i = 0; i < args.length; i++) {
         if (args[i].equals("-u")) {
            userConfig = true;
         }
         else if (args[i].equals("-s")) {
            singleStep = true;
         }
      }
      playGame(singleStep,userConfig);      
   }
   
   /** Play the solitaire board game based on the modes that user chose  */

    private static void playGame(boolean singleStep, boolean userconfig){ 
    	/*Run from here if the user wants to give a specific configuration*/      	   	
    	if(userconfig == true){
    		System.out.print("Number of total cards is " + SolitaireBoard.CARD_TOTAL + ". ");
    		System.out.println("You will be entering the initial configuration of the cards (i.e., how many in each pile).");
    		System.out.print("Please enter a space-separated list of positive integers followed by newline: ");    		
    		Scanner in = new Scanner(System.in);
    		String user = in.nextLine();
    		while(SolitaireBoard.isValidConfigString(user) == false){
    			System.out.println("ERROR: Each pile must have at least one card and the total number of cards must be 45");
    			user = in.nextLine();
    		}    		
    		SolitaireBoard game = new SolitaireBoard(user);    		
    		if(singleStep == true){//Check if the user wants a single step mode
    			stepMode(game);	    			
    		}else{
    			autoMode(game);    				
    		}
    	}  	
    	else{/*Run from here if the initial configuration is random*/     		
    		SolitaireBoard game = new SolitaireBoard();
			if(singleStep == true){//Check if the user wants a single step mode
				stepMode(game);				
			}else{
				autoMode(game);						
			}
	
    	}
    }
    
    /** Run a solitaireBoard game in the mode that all steps will be taken automatically*/
    private static void autoMode(SolitaireBoard game){
		int round = 0;		
		while(!game.isDone()){
			game.playRound();
			round++;
			System.out.print("["+round+"]");
			game.configToString();
		}
		System.out.println("Done!"); 
    }
    /** Run a solitaireBoard game in the mode that one step will be taken when user hits enter*/
    private static void stepMode(SolitaireBoard game){
		int round = 0;		
		while(!game.isDone()){			
			game.playRound();
			round++;
			System.out.print("["+round+"]");
			game.configToString();
			System.out.print("<Type return to continue>");
			Scanner keyboard = new Scanner(System.in);
			String input = keyboard.nextLine();
			while(input == null){
				continue;
			}
		}
		System.out.println("Done!"); 
    }
}