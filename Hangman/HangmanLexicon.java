/*
 * File: HangmanLexicon.java
 * -------------------------
 * This file contains a stub implementation of the HangmanLexicon
 * class that you will reimplement for Part III of the assignment.
 */

import acm.util.*;

import java.io.*;
import java.util.*;

public class HangmanLexicon {
	
/** Initialises the class.
 *  Reads the text file for words
 *  Creates the wordList Array. */
	public HangmanLexicon() {
		wordList = new ArrayList<String>();
		try{
			BufferedReader br = new BufferedReader(new FileReader("HangmanLexicon.txt"));
			while(true){
				String word = br.readLine();
				if(word==null) break;
				wordList.add(word);
			}
			br.close();
		} catch (Exception ex){
			throw new ErrorException(ex);
		}
	}

/** Returns the number of words in the lexicon. */
	public int getWordCount() {
		return wordList.size();
	}

/** Returns the word at the specified index. */
	public String getWord(int index) {
		return wordList.get(index);
	}
	
	private ArrayList<String> wordList;
}
