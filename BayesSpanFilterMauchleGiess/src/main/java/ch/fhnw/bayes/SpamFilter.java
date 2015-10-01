package ch.fhnw.bayes;

import java.io.File;
import java.io.IOException;
import java.util.Map.Entry;

import org.jsoup.Jsoup;

public class SpamFilter {

	/**
	 * A map of ham words.
	 */
	private static WordMap ham;

	/**
	 * The number of total ham mails;
	 */
	private static int totalHamMails;

	/**
	 * A map of spam words.
	 */
	private static WordMap spam;

	/**
	 * The amount of total spam mails;
	 */
	private static int totalSpamMails;
	
	/**
	 * The alpha value for equalizing the word maps.
	 */
	private final static double ALPHA = 0.1;

	/**
	 * Starter method.
	 * 
	 * @param args
	 *            an array of arguments.
	 * @throws IOException
	 *             when an I/O Problem occurs.
	 */
	public static void main(String[] args) throws IOException {

		// initialize word maps.
		ham = new WordMap();
		spam = new WordMap();

		// learn phase
		System.out.println("========= Learn Phase =========");
		learn();
		// If words exist only in ham or spam, word maps must be manipulated.
		// For more info see javadoc of this method.
		equalizeWordMaps();
		
		// calibrate phase
		System.out.println("========= Calibration Phase =========");
		calibrate();
		
		
	}

	/**
	 * Fill word maps with example words.
	 * 
	 * @throws IOException
	 *             when an I/O Problem occurs.
	 */
	private static void learn() throws IOException {
		// ham examples
		System.out.print("Learning hams...\t");
		File aHamLearnFolder = new File("src/main/resources/ham-anlern");
		for (File aMail : aHamLearnFolder.listFiles()) {
			ham.addWords(getWordsFromFile(aMail));
			totalHamMails++;
		}
		System.out.println(totalHamMails + "\tham mails read.");

		// spam examples
		System.out.print("Learning spams...\t");
		File aSpamLearnFolder = new File("src/main/resources/spam-anlern");
		for (File aMail : aSpamLearnFolder.listFiles()) {
			spam.addWords(getWordsFromFile(aMail));
			totalSpamMails++;
		}
		System.out.println(totalSpamMails + "\tspam mails read.");

		System.out.println();
	}
	
	/**
	 * 
	 * @throws IOException
	 */
	private static void calibrate() throws IOException {
		System.out.println("Calculation spam probability of hams...\t");
		File aHamLearnFolder = new File("src/main/resources/ham-kalibrierung");
		for (File aMail : aHamLearnFolder.listFiles()) {
			Double p = calculateProbabiltity(aMail);
			System.out.println(p*100 + " %");
		}
		
		System.out.println();
		
		System.out.println("Calculation spam probability of spams...\t");
		File aSpamLearnFolder = new File("src/main/resources/spam-kalibrierung");
		for (File aMail : aSpamLearnFolder.listFiles()) {
			Double p = calculateProbabiltity(aMail);
			System.out.println(p*100 + " %");
		}
	}

	/**
	 * Calculates the spam probability of an email.
	 * 
	 * @param inMail
	 *            the email file.
	 * @return the probability.
	 * @throws IOException
	 *             if an I/O Exception occurs
	 */
	private static Double calculateProbabiltity(File inMail) throws IOException {
		Double probabilityOfSpam = 0.5;

		// Get all words in this mail.
		String[] aListOfWords = getWordsFromFile(inMail);
		
		// check if there are words that are neither in ham nor in spam
		for (String aWord : aListOfWords) {
			if(ham.get(aWord) == null && spam.get(aWord) == null){
				ham.put(aWord, (double) totalHamMails);
				spam.put(aWord, (double) totalSpamMails);
			}
		}

		Double counter = 1.0;
		for (String aWord : aListOfWords) {
			counter *= spam.getCount(aWord) / totalSpamMails;
		}
		
		Double denominator = 0.0;
		
		Double denominatorPart1 = counter;
		
		Double denominatorPart2 = 1.0;
		for (String aWord : aListOfWords) {
			denominatorPart2 *= ham.getCount(aWord) / totalHamMails;
		}
		
		denominator = denominatorPart1 + denominatorPart2;
		
		probabilityOfSpam = counter / denominator;
		
		return probabilityOfSpam;
	}

	/**
	 * Reads a text file and returns an array of containing words.
	 * 
	 * @param inFile
	 *            the text file for which the words should be extracted.
	 * @throws IOException
	 *             if an I/O Exception occurs
	 * @return the array of words containing in this file.
	 */
	private static String[] getWordsFromFile(File inFile) throws IOException {
		// Convert html to plain text.
		String aPlainText = Jsoup.parse(inFile, "UTF-8").text();

		// remove all special characters and punctuations
		aPlainText = aPlainText.replaceAll("[^A-Za-z]", " ");

		// split words by space
		String[] anArrayOfWords = aPlainText.split("\\s+");

		return anArrayOfWords;
	}

	/**
	 * If a single word is only in the hams word list, then the counter of the
	 * equation will be 0, thus the hole equation will be zero. If a single word
	 * is only in the spams word list, then the second summand of the
	 * denominator will be zero, thus the whole equation will be one. <br>
	 * So this method will equalize the word maps so each word map has the same
	 * words.
	 */
	private static void equalizeWordMaps() {

		// for each word only existing in ham add same word to spam with alpha
		// value
		for (Entry<String, Double> anEntry : ham.entrySet()) {
			if (spam.get(anEntry.getKey()) == null) {
				spam.put(anEntry.getKey(), ALPHA);
			}
		}

		// for each word only existing in spam add same word to ham with alpha
		// value
		for (Entry<String, Double> anEntry : spam.entrySet()) {
			if (ham.get(anEntry.getKey()) == null) {
				ham.put(anEntry.getKey(), ALPHA);
			}
		}
		
		assert(ham.size() == spam.size());

	}

}
