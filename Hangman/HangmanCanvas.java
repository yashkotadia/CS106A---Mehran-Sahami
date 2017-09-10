/*
 * File: HangmanCanvas.java
 * ------------------------
 * This file keeps track of the Hangman display.
 */

import acm.graphics.*;

public class HangmanCanvas extends GCanvas {

/** Resets the display so that only the scaffold appears */
	public void reset() {
		removeAll();
		addScaffold();
		incorrectGuesses = "";
		nIncGuess = 0;
	}

/**
 * Updates the word on the screen to correspond to the current
 * state of the game.  The argument string shows what letters have
 * been guessed so far; unguessed letters are indicated by hyphens.
 */
	public void displayWord(String word) {
		double wordX = 72;
		double wordY = yRef + 150;
		GObject oldWord = getElementAt( wordX , wordY );
		if(oldWord != null) remove(oldWord);
		GLabel wordGuessed = new GLabel( word , wordX , wordY );
		wordGuessed.setFont("Serif-PLAIN-24");
		add(wordGuessed);
	}

/**
 * Updates the display to correspond to an incorrect guess by the
 * user.  Calling this method causes the next body part to appear
 * on the scaffold and adds the letter to the list of incorrect
 * guesses that appears at the bottom of the window.
 */
	public void noteIncorrectGuess(char letter) {
		nIncGuess++; // Increments number of incorrect guesses done
		double x = 72;
		double y = yRef + 216;
		incorrectGuesses += letter;
		GLabel incG = new GLabel( incorrectGuesses , x , y );
		incG.setFont("Serif-PLAIN-22");
		add(incG);
		
		switch(nIncGuess){
		case 1:
			GOval head = new GOval (xTemp-HEAD_RADIUS, yTemp , 2*HEAD_RADIUS , 2*HEAD_RADIUS);
			add(head);
			yTemp += 2*HEAD_RADIUS;
			break;
			
		case 2:
			GLine body = new GLine (xTemp , yTemp , xTemp , yTemp+BODY_LENGTH);
			add(body);
			yTemp += ARM_OFFSET_FROM_HEAD;
			break;
			
		case 3:
			GLine lArm = new GLine (xTemp , yTemp , xTemp-UPPER_ARM_LENGTH , yTemp);
			GLine lPalm = new GLine(xTemp-UPPER_ARM_LENGTH ,yTemp ,xTemp-UPPER_ARM_LENGTH ,yTemp+LOWER_ARM_LENGTH);
			add(lArm);
			add(lPalm);
			break;
			
		case 4:
			GLine rArm = new GLine (xTemp , yTemp , xTemp+UPPER_ARM_LENGTH , yTemp);
			GLine rPalm = new GLine(xTemp+UPPER_ARM_LENGTH ,yTemp ,xTemp+UPPER_ARM_LENGTH ,yTemp+LOWER_ARM_LENGTH);
			add(rArm);
			add(rPalm);
			yTemp += BODY_LENGTH - ARM_OFFSET_FROM_HEAD;
			break;
			
		case 5:
			GLine hip = new GLine (xTemp-HIP_WIDTH , yTemp , xTemp+HIP_WIDTH , yTemp);
			add(hip);
			xTemp -= HIP_WIDTH;
			GLine lLeg = new GLine (xTemp, yTemp , xTemp , yTemp+LEG_LENGTH);
			add(lLeg);
			xTemp += 2*HIP_WIDTH;
			break;
			
		case 6:
			GLine rLeg = new GLine (xTemp, yTemp , xTemp , yTemp+LEG_LENGTH);
			add(rLeg);
			xTemp -= 2*HIP_WIDTH;
			yTemp += LEG_LENGTH;
			break;
			
		case 7:
			GLine lFoot = new GLine (xTemp , yTemp , xTemp-FOOT_LENGTH , yTemp);
			add(lFoot);
			xTemp += 2*HIP_WIDTH;
			break;
			
		case 8:
			GLine rFoot = new GLine (xTemp , yTemp , xTemp+FOOT_LENGTH , yTemp);
			add(rFoot);
		}
		
	}
	
/* Creates and adds the scaffold to the canvas */
	private void addScaffold() {
		xTemp = xRef;
		yTemp = yRef;
		GLine scaffold = new GLine( xTemp , yTemp , xTemp , yTemp-SCAFFOLD_HEIGHT);
		yTemp -=SCAFFOLD_HEIGHT;
		GLine beam = new GLine( xTemp , yTemp , xTemp+BEAM_LENGTH , yTemp);
		xTemp += BEAM_LENGTH;
		GLine rope = new GLine( xTemp , yTemp , xTemp ,  yTemp + ROPE_LENGTH);
		yTemp += ROPE_LENGTH;
		add(scaffold);
		add(beam);
		add(rope);
	}

/* Constants for the simple version of the picture (in pixels) */
	private static final int SCAFFOLD_HEIGHT = 360;
	private static final int BEAM_LENGTH = 144;
	private static final int ROPE_LENGTH = 18;
	private static final int HEAD_RADIUS = 36;
	private static final int BODY_LENGTH = 144;
	private static final int ARM_OFFSET_FROM_HEAD = 28;
	private static final int UPPER_ARM_LENGTH = 72;
	private static final int LOWER_ARM_LENGTH = 44;
	private static final int HIP_WIDTH = 36;
	private static final int LEG_LENGTH = 108;
	private static final int FOOT_LENGTH = 28;
	
/* Coordinate of the bottom most point of the scaffold */
	private double xRef = getWidth()/2 - BEAM_LENGTH + 300 ;
	private double yRef = getHeight()/2 + LEG_LENGTH + 18 + 300 ;
	
/* Temporary coordinates of the end point of object last added. */
	private double xTemp , yTemp;
	
/* The number of incorrect guesses done. */
	private int nIncGuess;
	
/* Stores the sting of letters incorrectly guessed. */
	private String incorrectGuesses;
	
}
