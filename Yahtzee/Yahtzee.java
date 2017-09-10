/*
 * File: Yahtzee.java
 * ------------------
 * This program will eventually play the Yahtzee game.
 */

import acm.io.*;
import acm.program.*;
import acm.util.*;

public class Yahtzee extends GraphicsProgram implements YahtzeeConstants {
	
	public static void main(String[] args) {
		new Yahtzee().start(args);
	}
	
	public void run() {
		IODialog dialog = getDialog();
		nPlayers = dialog.readInt("Enter number of players");
		playerNames = new String[nPlayers];
		for (int i = 1; i <= nPlayers; i++) {
			playerNames[i - 1] = dialog.readLine("Enter name for player " + i);
		}
		display = new YahtzeeDisplay(getGCanvas(), playerNames);
		scores = new int[N_CATEGORIES+1][nPlayers+1];
		usedCategory = new int[N_CATEGORIES+1][nPlayers+1];
		initialiseArray();
		playGame();
	}

	private void playGame() {
		
		// Handles the 13 turns.
		for(int i = 1; i<=N_SCORING_CATEGORIES ; i++){
			for(int j = 1; j<=nPlayers ; j++){
				
				display.printMessage(playerNames[j-1] +"'s, Click the Roll Dice button to roll the dice.");
				display.waitForPlayerToClickRoll(j);
				rollDice();
				display.displayDice(dice);
				
				for(int k = 0 ; k<2 ; k++){
					display.printMessage("Select the dice you wish to re-roll and Click Roll Again");
					display.waitForPlayerToSelectDice();
					rollSelectedDice();
					display.displayDice(dice);
				}
				
				display.printMessage("Select a category for this roll.");
				int category;
				while(true){
					category =display.waitForPlayerToSelectCategory();
					if(usedCategory[category][j] == 1){
						display.printMessage("You already picked that category. Please choose another category.");
					}
					else{
						usedCategory[category][j] = 1;
						break;
					}
				}
				
				sortDice();
				
				int categoryScore = getScore(category);
				
				scores[category][j] = categoryScore;
				display.updateScorecard(category, j, categoryScore);
				
				scores[TOTAL][j] += categoryScore;
				display.updateScorecard(TOTAL, j, scores[TOTAL][j]);
				
				if(category>=1 && category<=6) scores[UPPER_SCORE][j] += categoryScore;
				else scores[LOWER_SCORE][j] += categoryScore;
				
			}
		}
		
		// Handles the final totalling of all scores.
		for(int i = 1; i<=nPlayers ; i++){
			if(scores[UPPER_SCORE][i]>=63){
				scores[UPPER_BONUS][i] = 35;
			}
			scores[TOTAL][i] = scores[UPPER_SCORE][i] + scores[LOWER_SCORE][i] + scores[UPPER_BONUS][i];
			
			display.updateScorecard(UPPER_BONUS, i, scores[UPPER_BONUS][i]);
			display.updateScorecard(UPPER_SCORE, i, scores[UPPER_SCORE][i]);
			display.updateScorecard(LOWER_SCORE, i, scores[LOWER_SCORE][i]);
			display.updateScorecard(TOTAL, i, scores[TOTAL][i]);
			
		}
		
		int winner = findWinner();
		display.printMessage("Congtatulations!, " +playerNames[winner-1] +" , You're the winner with a total score of " +scores[TOTAL][winner] +"!");
	}
	
/* Method : getScore(int category) */
/** Returns the score obtained for the selected category
 * @return The calculated score for the given category. */
	private int getScore(int category){
		int score = 0;
		String str="";
		for(int i = 0; i<N_DICE; i++ ) str+= dice[i];
		
		switch(category){
		
		case ONES:for(int i = 0; i<N_DICE; i++ ) if(dice[i]==1) score += 1;break;
		case TWOS:for(int i = 0; i<N_DICE; i++ ) if(dice[i]==2) score += 2;break;
		case THREES:for(int i = 0; i<N_DICE; i++ ) if(dice[i]==3) score += 3;break;
		case FOURS:for(int i = 0; i<N_DICE; i++ ) if(dice[i]==4) score += 4;break;
		case FIVES:for(int i = 0; i<N_DICE; i++ ) if(dice[i]==5) score += 5;break;
		case SIXES:for(int i = 0; i<N_DICE; i++ ) if(dice[i]==6) score += 6;break;
		case CHANCE:for(int i = 0; i<N_DICE; i++ ) score += dice[i];break;
		
		case THREE_OF_A_KIND: if(nOfAKind(3)) for(int i = 0; i<N_DICE; i++ ) score += dice[i];break;
		case FOUR_OF_A_KIND: if(nOfAKind(4)) for(int i = 0; i<N_DICE; i++ ) score += dice[i];break;
		case YAHTZEE: if(nOfAKind(5)) score = 50;break;
		
		case LARGE_STRAIGHT:
			if(str.contains("12345") || str.contains("23456")) score = 40;
			break;
			
		case SMALL_STRAIGHT:
			if(str.contains("1234") || str.contains("2345") || str.contains("3456")) 
				score = 30;
			break;
			
		case FULL_HOUSE:
			int[] c = new int[6];
			for(int i = 0; i<N_DICE; i++ ){
				if(dice[i]==1) c[0]++;
				else if(dice[i]==2) c[1]++;
				else if(dice[i]==3) c[2]++;
				else if(dice[i]==4) c[3]++;
				else if(dice[i]==5) c[4]++;
				else c[5]++;
			}
			String cstr = "";
			for(int i=0; i<c.length;i++) cstr+=c[i];
			if(cstr.contains("23") || cstr.contains("32") || cstr.contains("5")) score = 25;
			break;
		}
		return score;
	}
	
/* Method : sortDice() */
/** Sorts the dice in ascending order */
	private void sortDice() {
		for(int i = 0; i < 4 ;i++){
			for(int j = 0; j < 4 ;j++){
				if(dice[j]>dice[j+1]){
					int temp = dice[j];
					dice[j] = dice[j+1];
					dice[j+1] = temp;
				}
			}
		}
	}
 	
/* Method : findWinner() */
/** Finds the winner 
 *  @return The index of winner from 1 to nPlayers.*/
	private int findWinner() {
		int winner = 1;
		
		for(int i = 1; i<nPlayers ; i++){
			if(scores[TOTAL][i] < scores[TOTAL][i+1]){
				winner = i;
			}
		}
		
		return winner;
		
	}
	
/* Method : nOfAKind */
/** Returns true if the dice contain n of a kind */
	private boolean nOfAKind(int n){
		int[] c = new int[6];
		for(int i = 0; i<N_DICE; i++ ){
			if(dice[i]==1) c[0]++;
			else if(dice[i]==2) c[1]++;
			else if(dice[i]==3) c[2]++;
			else if(dice[i]==4) c[3]++;
			else if(dice[i]==5) c[4]++;
			else c[5]++;
		}
		
		int cMax=0;
		for(int i = 0; i<6; i++ ) if(c[i]>cMax) cMax=c[i]; // Finds max of c
		if(cMax>=n) return true;
		return false;
	}
	
/* Method : rollSelectedDice() */
/** Rolls only the dice selected */
	private void rollSelectedDice(){
		for( int i=0 ; i<N_DICE ; i++){
			if(display.isDieSelected(i)){
				dice[i] = rgen.nextInt(1, 6);
			}
		}
	}
	
/* Method : rollDice() */
/** Rolls the whole set of dice */
	private void rollDice() {
		for(int i = 0 ; i < N_DICE ; i++){
			dice[i] = rgen.nextInt(1, 6);
		}
	}
	
/* Method : initialiseArray() */
/** Sets all entries in score matrix to 0 */
	private void initialiseArray() {
		for(int i = 0 ; i<=N_CATEGORIES ; i++){
			for(int j = 0 ; j<=nPlayers ; j++){
				scores[i][j] = 0;
				usedCategory[i][j] = 0;
			}
		}
	}
		
/* Private instance variables */
	private int nPlayers;
	private String[] playerNames;
	private YahtzeeDisplay display;
	private RandomGenerator rgen = new RandomGenerator();
	private int[][] scores;
	private int[] dice = new int[N_DICE];
	private int[][] usedCategory;
}
