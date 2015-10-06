package ch.fhnw.bayes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * This class is a map of words. The words are mapped to a numeric value. The
 * numeric value represents in how many mails this word exists.
 * 
 * @author tobias
 *
 */
@SuppressWarnings("serial")
public class WordMap extends HashMap<String, Double> implements Comparator<String>{

	/**
	 * Constructor
	 * 
	 */
	public WordMap() {
		super();
	}

	/**
	 * Gets the numeric value mapped to a word.
	 * 
	 * @param inWord
	 *            the word for which the numeric value should be given.
	 * 
	 * @return the numeric value mapped to the word.
	 */
	public Double getCount(String inWord) {
		return get(inWord);
	}

	/**
	 * Adds a word to the word map and raises the value by 1.
	 * 
	 * @param inWord
	 *            the word.
	 */
	public void addWord(String inWord) {
		Double anAlreadySavedWordValue = get(inWord);
		put(inWord, anAlreadySavedWordValue + 1);
	}

	/**
	 * Reads a text file and adds all words in this text file to the word map.
	 * 
	 * @param inFile
	 *            the text file for which the words should be extracted and
	 *            added to this word map.
	 * @throws IOException
	 *             if an I/O Exception occurs
	 */
	public void addWords(String[] inArrayOfWords) throws IOException {

		// list of words of the current email that has
		// been added to this map.
		List<String> aListOfAddedWords = new ArrayList<>();

		// add to map
		for (String aWord : inArrayOfWords) {
		    
		    // Ignore single letters
		    if( aWord.length() <= 1) {
		        continue;
		    }

			Double anAlreadySavedWordValue = get(aWord);

			// if the word has not been added yet to this map
			if (anAlreadySavedWordValue == null) {
				put(aWord, 1.0);
				aListOfAddedWords.add(aWord);
				continue;
			}

			// if the word of this current email has not been
			// added to this map yet, but the word does already
			// exist, then increment the integer value.
			if (!aListOfAddedWords.contains(aWord)) {
				put(aWord, anAlreadySavedWordValue + 1);
				aListOfAddedWords.add(aWord);
				continue;
			}

			// in any other cases the word from this email
			// has already been added once. Do nothing.

		}
	}

	/**
	 * Prints out a word map to console.
	 */
	public void print() {
		for (Entry<String, Double> anEntry : entrySet()) {
			System.out.format("%32s%16d", anEntry.getKey(), 10, anEntry.getValue());
			System.out.println();
		}
		System.out.println();
	}

	/**
	 * ${@inheritDoc}
	 */
	@Override
	public int compare(String o1, String o2) {
		if (get(o1) >= get(o2)) {
            return -1;
        } else {
            return 1;
        } 
		// returning 0 would merge keys
	}
}
