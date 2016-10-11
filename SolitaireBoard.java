/* @author: Jianyang Zhang */

package projectB;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;


/*
class SolitaireBoard
The board for Bulgarian Solitaire.  You can change what the total number of cards is for the game
by changing NUM_FINAL_PILES, below.  Don't change CARD_TOTAL directly, because there are only some values
for CARD_TOTAL that result in a game that terminates.
(See comments below next to named constant declarations for more details on this.)
*/


public class SolitaireBoard {

public static final int NUM_FINAL_PILES = 9;
// number of piles in a final configuration
// (note: if NUM_FINAL_PILES is 9, then CARD_TOTAL below will be 45)

public static final int CARD_TOTAL = NUM_FINAL_PILES * (NUM_FINAL_PILES + 1) / 2;
// bulgarian solitaire only terminates if CARD_TOTAL is a triangular number.
// see: http://en.wikipedia.org/wiki/Bulgarian_solitaire for more details
// the above formula is the closed form for 1 + 2 + 3 + . . . + NUM_FINAL_PILES


/**
config is an array containing enough elements for each card
numPiles is the number of piles with value 0 before game starting
Once game starts, 0 < numPiles <= CARD_TOTAL, each piles is stored in config[0] ¨C config[numPiles ¨C 1],
and the sum of all elements in config = CARD_TOTAL.  
*/

private int[] config = new int[CARD_TOTAL+5];
private int numPiles = 0;



/**
  Creates a solitaire board with the given configuration.
  PRE: SolitaireBoard.isValidConfigString(numberString)
*/
public SolitaireBoard(String numberString) {
   // sample assert statement (you will be adding more of these calls)   
   Scanner in = new Scanner(numberString);
   while(in.hasNext()){	   
	   config[numPiles]=in.nextInt();
	   numPiles++;
   }      
   System.out.println("Your initial configuration: " + configString()); 
   assert isValidSolitaireBoard();
}


/**
   Creates a solitaire board with a random initial configuration.
*/
public SolitaireBoard() {
	Random rand = new Random();
	int balance = CARD_TOTAL;
	while(balance!=0){
		int pile = rand.nextInt(balance)+1; // the amount of cards drawn by this step
		balance = balance - pile;// remaining cards after this step
		// Store the pile drawn by this step and go on
		config[numPiles]=pile;	
		numPiles++;				
	}
	System.out.println("Your initial configuration: " + configString());	
	assert isValidSolitaireBoard();
}


/**
   Plays one round of Bulgarian solitaire.  Updates the configuration according to the rules
   of Bulgarian solitaire: Takes one card from each pile, and puts them all together in a new pile.
   The old piles that are left will be in the same relative order as before, 
   and the new pile will be at the end.
 */
public void playRound() {
	int[] playRound = nonZero(config); // Current configuration without zeros
	int count = 0; // Record how many cards are taken	
	
	/* Take one card from each pile*/
	for(int i = 0; i < playRound.length; i++){
		playRound[i] = playRound[i] - 1;
		if(playRound[i] == 0){
			numPiles--;
		}
		removeZero(playRound);
		count ++;
	}
	numPiles++;

	/*Update the configuration*/
	for(int j = 0; j < playRound.length; j++){
		config[j] = playRound[j];
	}
	for(int k = playRound.length; k < config.length;k++){
		config[k] = 0;
	}
	
	config[playRound.length] = count; //Put the drawn cards in a new pile	
	assert isValidSolitaireBoard();
}

/**
   Returns true if the current board is at the end of the game.  That is, there are NUM_FINAL_PILES
   piles that are of sizes 1, 2, 3, . . . , NUM_FINAL_PILES, in any order.
 */

public boolean isDone() {		
	if(numPiles==NUM_FINAL_PILES){  //check if numPiles has reached the NUM_FINAL_PILES
		/*check if the configuration is 1, 2, 3, 4, 5, 6 ... NUM_FINAL_PILES*/
		int[] check = nonZero(config);
		Arrays.sort(check);//remove all zeros then sort it from small to large.
		for(int i = 1; i < numPiles; i++){
			if(check[i]-check[i-1]!=1){
				assert isValidSolitaireBoard();
				return false;
			}
		}
		assert isValidSolitaireBoard();
		return true;
	}else{
		assert isValidSolitaireBoard();
		return false;		
	}	
}

/**
   Returns current board configuration as a string with the format of
   a space-separated list of numbers with no leading or trailing spaces.
   The numbers represent the number of cards in each non-empty pile.
 */
public String configString() {
	assert isValidSolitaireBoard();
	return Arrays.toString(nonZero(config));   
}

/**Print the current configuration and number of piles**/
public void configToString(){
	System.out.println("Current Configuration: " + configString());
	assert isValidSolitaireBoard();
}

/**
   Returns true if configString is a space-separated list of numbers that
   is a valid Bulgarian solitaire board with card total SolitaireBoard.CARD_TOTAL
*/
public static boolean isValidConfigString(String configString) {
	int[] user = new int[CARD_TOTAL];	
	/*First Check: if the list of numbers entered are actually all integers, and if the number entered is zero*/
	Scanner firstCheck = new Scanner(configString);
	while(firstCheck.hasNext()){ 
		String nextInput = firstCheck.next();
		if(!isInt(nextInput) || nextInput.equals("0")){		
			return false;
		}
	}		
	/* Second Check: if the amount of integers exceeds CARD_TOTAL */
	Scanner secondCheck = new Scanner(configString);
	int i = 0, j = 0, sum = 0;
	while(secondCheck.hasNext()){
		String nextInput = secondCheck.next();
		i++;
		if(i > CARD_TOTAL){			
			return false;
		}
	}
	/* Store all inputs into the array*/
	Scanner userInput = new Scanner(configString);
	while(userInput.hasNext()){	   
		user[j] = userInput.nextInt(); 
		j++;
	}	
	/* Check if all nonzero integers are positive, and calculate the sum of all integers*/
	for(int k = 0; k < nonZero(user).length; k++){
		if(nonZero(user)[k] < 0){			
			return false;
		}
		sum += nonZero(user)[k];
	}	
	/* Check if the sum of all integers equals to CARD_TOTAL*/
	if(sum == 45){
		return true;
	}else{
		return false;
	}
}

/**
   Returns true if the solitaire board data is in a valid state
   (See representation invariant comment for more details.)
 */
private boolean isValidSolitaireBoard() {
	int sum = 0; 
	/* calculate the sum of all elements in config*/
	for(int i = 0; i < config.length; i++){
		sum+=config[i];
	}
	if(numPiles > 0 && numPiles <=CARD_TOTAL && sum == CARD_TOTAL){
		return true;
	}else{
		return false;
	}
}

/*return an array that contains all non-zero elements of the original array*/

private static int[] nonZero(int[] origin){	
	// count the index of the non-zero array
	int count = 0; 
	for(int i = 0; i < origin.length; i++){
		if(origin[i]!=0){
			count++;
		}	
	}
	
	int[] nonZero = new int[count]; // the new non-zero array
	count = 0;	
	// Store all non-zero elements to the new array
	for(int i = 0; i < origin.length; i++){ 
		if(origin[i]!=0){
			nonZero[count]=origin[i];
			count++;
		}
	}
	return nonZero; 
}

/**remove all zeros from the input array**/

private void removeZero(int[] origin){
	origin = nonZero(origin);
}

private static boolean isInt(String input) {
    try {  
        Integer.parseInt(input);  
    } catch (NumberFormatException e) { 
        return false;  
    }  
    return true;  
}  

}
