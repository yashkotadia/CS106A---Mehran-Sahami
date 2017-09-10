/*
 * File: Hangman.java
 * ------------------
 * This program will eventually play the Hangman game from
 * Assignment #4.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.awt.*;

public class Hangman extends ConsoleProgram {
	
/** The number of guesses allowed */
	private int guessesLeft = 8;
	
	
/* Method : init() */
/** Initialises the canvas */
	public void init() {
		canvas = new HangmanCanvas();
		add(canvas);
	}

/* Method : run() */
/** Runs the Hangman Program */
    public void run() {
		initialise();
		
		while (guessesLeft>0){
			play();
			if (wordGuessed.equals(wordToGuess)){
				canvas.displayWord(wordToGuess);
				println("You have guessed the word " +wordToGuess +" correctly.");
				println("Congratulations ! You Win");
				break;
			}
		}
		
		if(guessesLeft == 0 ){
			println("You're completely hung.");
			println("The word was: " +wordToGuess);
			println("You Lose!");
		}
	}
    
    
/* Method : initialise() */
/** Sets up the word to be guessed & screen . */
    private void initialise() {
    	canvas.reset();
    	HangmanLexicon hangLex = new HangmanLexicon();
    	wordCount = hangLex.getWordCount();
		int wordNumber = rgen.nextInt(0, wordCount - 1);
		wordToGuess = hangLex.getWord(wordNumber);
		lWordToGuess = wordToGuess.length();
		for(int i=0 ; i < lWordToGuess ; i++) wordGuessed += "-";
		println("Welcome To Hangman");
    }

/* Method : play() */
/** Controls the playing experience */
    private void play() {
    	displayMessage();
    	char input = getLegalInput();
    	wordGuessed = updateWordGuessed( input );
    	canvas.displayWord(wordGuessed);
    	
    }
    
/* Method : displayMessage() */
/** Displays the required information and gets the input for next Letter */
    private void displayMessage() {
    	println("The word now looks like this : " +wordGuessed);
    	println("You have " +guessesLeft +" guesses left.");
    }
    
/* Method : getLegalInput() */
/** Takes a legal character from user. 
 * @return ch Returns the Legal input character.*/
    private char getLegalInput() {
    	char ch;
    	while(true) {
    		String str = readLine("Your Guess : ");
    		if(str.length()==0) ch = 0;
    		else{
    			ch = str.charAt(0);
    			ch = Character.toUpperCase(ch);
    		}
    		if(ch < 'A' || ch > 'Z'){
    			println("Illegal Entry. Please try again.");
    		}
    		else break;
    	}
    	return ch;
    }
   
/* Method : updateWordGuessed ( char ch ) */
/** Compares the character entered by user with the wordToGuess
 *  And returns the updated word. 
 *  @param ch stores the Character to be compared
 *  @return result The updated wordGuessed */
    private String updateWordGuessed ( char ch ) {
    	String result = "";
    	for (int i=0 ; i < lWordToGuess ; i++){
    		// Checks whether the word has already been guessed.
    		if (wordGuessed.charAt(i)==ch){
    			println("The Letter " +ch +" has already been guessed");
    			return wordGuessed;
    		}
    		if(ch == wordToGuess.charAt(i)) result += ch; // Adds the newly guessed letters
    		else if(wordGuessed.charAt(i)!='-') result += wordGuessed.charAt(i); // Adds the letters already guessed
    		else result += "-";// Adds the - for letters yet to be guessed.
    	}
    	if(result.equals(wordGuessed)) {
    		println("There are no " +ch +"'s in the word.");
    		canvas.noteIncorrectGuess(ch);
    		guessesLeft--;
    	}
    	else println("Your guess is correct");
    	return result;
    }
    
/** Word Count */
    private int wordCount;
    
/** The actual word to be guessed. **/
    String wordToGuess;
    
/** Instance variable for random generator */
    RandomGenerator rgen = new RandomGenerator();
    
/** Length of the word to guess */
    int lWordToGuess;
    
/** Stores what of the word has yet been guessed */
    String wordGuessed = "";
    
/** Instance variable of the HangmanCanvas class. */
    HangmanCanvas canvas;

}
