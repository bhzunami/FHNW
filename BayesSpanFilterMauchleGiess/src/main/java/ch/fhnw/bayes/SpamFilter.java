package ch.fhnw.bayes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.jsoup.Jsoup;

import com.sun.mail.util.DecodingException;

public class SpamFilter {

    /**
     * The alpha value for equalizing the word maps.
     */
    private final static double ALPHA = 0.2;
    
	/**
	 * A map of ham words.
	 */
	private WordMap ham;

	/**
	 * The number of total ham mails;
	 */
	private int totalHamMails;

	/**
	 * A map of spam words.
	 */
	private WordMap spam;

	/**
	 * The amount of total spam mails;
	 */
	private int totalSpamMails;
	

	
	public SpamFilter() {
	    this.ham = new WordMap();
	    this.spam = new WordMap();
	    this.totalHamMails = 0;
	    this.totalSpamMails = 0;
	}

	/**
	 * Starter method.
	 * 
	 * @param args         an array of arguments.
	 * @throws IOException when an I/O Problem occurs.
	 */
	public static void main(String[] args) throws IOException {
	    
	    SpamFilter sf = new SpamFilter();

		// learn phase
		System.out.println("========= Learn Phase =========");
		sf.learn();
		
		// print top spam words
		System.out.println(sf.spam.getTop(50));
		System.out.println(sf.getMostPossibleSpamWords(50));
		
		// If words exist only in ham or spam, word maps must be manipulated.
		// For more info see javadoc of this method.
		sf.equalizeWordMaps();
		
		// calibrate phase
		System.out.println("========= Calibration Phase =========");
		sf.calibrate();
		
		
	}

	/**
	 * Fill word maps with example words.
	 * 
	 * @throws IOException
	 *             when an I/O Problem occurs.
	 */
	private void learn() throws IOException {
		// ham examples
		System.out.print("Learning hams...\t");
		File aHamLearnFolder = new File("src/main/resources/ham-anlern");
		for (File aMail : aHamLearnFolder.listFiles()) {
		    this.ham.addWords(readMail(aMail).replaceAll("[^A-Za-z]", " ").split("\\s+"));
			this.totalHamMails++;
		}
		System.out.println(totalHamMails + "\tham mails read.");

		// spam examples
		System.out.print("Learning spams...\t");
		File aSpamLearnFolder = new File("src/main/resources/spam-anlern");
		for (File aMail : aSpamLearnFolder.listFiles()) {
			this.spam.addWords(readMail(aMail).replaceAll("[^A-Za-z]", " ").split("\\s+"));
			this.totalSpamMails++;
		}
		System.out.println(totalSpamMails + "\tspam mails read.");

		System.out.println();
	}
	
	/**
	 * 
	 * @throws IOException
	 */
	private void calibrate() throws IOException {
		
		// to words from mails to word list
		File aHamLearnFolder = new File("src/main/resources/ham-kalibrierung");
		for (File aMail : aHamLearnFolder.listFiles()) {
			this.ham.addWords(getWordsFromFile(aMail));
			this.totalHamMails++;
		}
		File aSpamLearnFolder = new File("src/main/resources/spam-kalibrierung");
		for (File aMail : aSpamLearnFolder.listFiles()) {
			this.spam.addWords(getWordsFromFile(aMail));
			this.totalSpamMails++;
		}
		
		equalizeWordMaps();
		
		System.out.println("Calculation spam probability of hams...\t");
		for (File aMail : aHamLearnFolder.listFiles()) {
			Double p = calculateProbabiltity(aMail);
			System.out.println(p*100 + " %");
		}
		
		System.out.println();
		
		System.out.println("Calculation spam probability of spams...\t");
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
	private Double calculateProbabiltity(File inMail) throws IOException {
		Double probabilityOfSpam = 0.5;

		// Get all words in this mail.
		String[] aListOfWords = getWordsFromFile(inMail);
		
		Double counter = 1.0;
		Double denominatorPart2 = 1.0;
		// check if there are words that are neither in ham nor in spam
		for (String aWord : aListOfWords) {
			if(ham.get(aWord) == null && spam.get(aWord) == null){
				continue;
			}
			
			counter *= spam.getCount(aWord) / totalSpamMails;
			denominatorPart2 *= ham.getCount(aWord) / totalHamMails;
		}
		
		Double denominator = 0.0;
		
		Double denominatorPart1 = counter;
		
//		for (String aWord : aListOfWords) {
//			denominatorPart2 *= ham.getCount(aWord) / totalHamMails;
//		}
		
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
	private String[] getWordsFromFile(File inFile) throws IOException {
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
	private void equalizeWordMaps() {

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
	/**
	 * Read the mail and get the content of the mail as string back
	 * @param mail The Mail to read
	 * @return message of the mail
	 */
    private String readMail(File mail) {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props);
        MimeMessage email = null;
        String message = "";
        try {
            FileInputStream fis = new FileInputStream(mail);
            email = new MimeMessage(session, fis);
            
            Object content = email.getContent();
            
            if (content instanceof Multipart) {
                BodyPart clearTextPart = null;
                BodyPart htmlTextPart = null;

                Multipart c = (Multipart)content;
                // Get the types
                int count = c.getCount();

                // Iterate over the Mimetypes
                for(int i=0; i<count; i++) {
                    BodyPart part =  c.getBodyPart(i);
                    if(part.isMimeType("text/plain")) {
                        clearTextPart = part;
                        break;
                    }
                    else if(part.isMimeType("text/html")) {
                        htmlTextPart = part;
                    }
                }

                if (clearTextPart != null) {
                    message = (String) clearTextPart.getContent();
                } else if (htmlTextPart != null) {
                    String html = (String) htmlTextPart.getContent();
                    message = Jsoup.parse(html).text();

                 // a simple text message
                } else if (content instanceof String) {
                    message = (String) content;
                }
            }

        } catch (MessagingException e) {
            System.out.println("TEST");
        } catch (FileNotFoundException e) {
            System.out.println("TEST");
        } catch (DecodingException e) {
            System.out.println("Wrong Base64");
        } catch (UnsupportedEncodingException e) {
            System.out.println("Unsupported Encoding");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return message;
	}
    
    private List<String> getMostPossibleSpamWords(int count) {
        List<String> mostPossibleSpamWords = new ArrayList<>();
        PQsort pqs = new PQsort();
        PriorityQueue<Entry<String,Double>> pq = new PriorityQueue<>(10, pqs);
        for (Map.Entry<String, Double> entry : spam.entrySet()) {
            if(ham.get(entry.getKey()) != null) {
                continue;
            }
            pq.add(entry);
        }
        
        for(int i = 0; i < count; i++) {
            mostPossibleSpamWords.add(pq.poll().getKey());
        }
                
        return mostPossibleSpamWords;
    }
    
    static class PQsort implements Comparator<Entry<String,Double>> {
        public int compare(Entry<String,Double> one, Entry<String,Double> two) {
            return Double.compare(one.getValue(), two.getValue()) * -1;
        }
    }


}
