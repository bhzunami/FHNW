package ch.fhnw.bayes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map.Entry;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.mail.util.DecodingException;

public class SpamFilter {
    
    private static final Logger logger = LoggerFactory.getLogger(SpamFilter.class);
    
    /**
     * The alpha value for equalizing the word maps.
     */
    private final static double ALPHA = 0.5;
    
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
	
	/**
	 * Constructor of the Spamfilter
	 * Set the initials values for total Ham and total Spam
	 */
	public SpamFilter() {
	    this.ham = new WordMap();
	    this.spam = new WordMap();
	    this.totalHamMails = 0;
	    this.totalSpamMails = 0;
	}
	

	/**
	 * Fill word maps with example words.
	 * 
	 * @throws IOException
	 *             when an I/O Problem occurs.
	 */
	public void train() throws IOException {
		// ham
	    logger.debug("Learning hams");
		File aHamLearnFolder = new File("src/main/resources/ham-anlern");
		for (File aMail : aHamLearnFolder.listFiles()) {
		    this.learnHam(aMail);
		}
		
		aHamLearnFolder = new File("src/main/resources/ham-test");
        for (File aMail : aHamLearnFolder.listFiles()) {
            this.learnHam(aMail);
        }
        
        aHamLearnFolder = new File("src/main/resources/ham-kalibrierung");
        for (File aMail : aHamLearnFolder.listFiles()) {
            this.learnHam(aMail);
        }
		
		logger.debug("{} ham mails read", this.totalHamMails);
		logger.debug("Learning spams");
		
		// Spam
		File aSpamLearnFolder = new File("src/main/resources/spam-anlern");
		// Read the mails and remove all chars which are not a caractor or a number and 
        // split in words
		for (File aMail : aSpamLearnFolder.listFiles()) {
		    this.learnSpam(aMail);
		}
		logger.debug("{} spam mails read", this.totalSpamMails);
		
		
		
        // If words exist only in ham or spam, word maps must be manipulated.
        // For more info see javadoc of this method.
        equalizeWordMaps();
	}
	
	public void learnHam(File file) {
	    try {
	        this.ham.addWords( readMail(file).split("\\s+") );
	        this.totalHamMails++;
	    } catch (IOException e) {
	        logger.error("Could not add file to Ham list {}", file);
	    }
	    
	}
	
	public void learnSpam(File file) {
        try {
            this.spam.addWords( readMail(file).split("\\s+") );
            this.totalSpamMails++;
        } catch (IOException e) {
            logger.error("Could not add file to Spam list {}", file);
        }
        
    }
		
	/**
	 * Only in testing mode necessary for the calibration of
	 * - number of significant spam words
	 * - Alpha for a word who does not exists in ham or spam
	 * @throws IOException
	 */
	public void calibrate() throws IOException {
		// to words from mails to word list
		File aHamLearnFolder = new File("src/main/resources/ham-kalibrierung");
		for (File aMail : aHamLearnFolder.listFiles()) {
			this.ham.addWords(readMail(aMail).split("\\s+"));
			this.totalHamMails++;
		}
		
		File aSpamLearnFolder = new File("src/main/resources/spam-kalibrierung");
		for (File aMail : aSpamLearnFolder.listFiles()) {
			this.spam.addWords( readMail(aMail).split("\\s+") );
			this.totalSpamMails++;
		}
		
		equalizeWordMaps();
		
		logger.debug("Calculation spam probability of hams");
		for (File aMail : aHamLearnFolder.listFiles()) {
			Double p = calculateProbabiltity(aMail);
			logger.debug("Spam possibility: {}%", p*100);
		}
		
		logger.debug("Calculation spam probability of spams");
		for (File aMail : aSpamLearnFolder.listFiles()) {
			Double p = calculateProbabiltity(aMail);
			logger.debug("Spam possibility: {}%", p*100);
		}
	}
	
	/**
	 * 
	 * @throws IOException
	 */
	public void test() throws IOException {
		// to words from mails to word list
	    logger.debug("Calculation spam probability of hams...");
		File aHamLearnFolder = new File("src/main/resources/ham-test");
		for (File aMail : aHamLearnFolder.listFiles()) {
			Double p = calculateProbabiltity(aMail);
			logger.debug("[TEST] HAM: Spam possibility: {} %", p*100);			
		}
		
		logger.debug("Calculation spam probability of spams...");
	    File aSpamLearnFolder = new File("src/main/resources/spam-test");
		for (File aMail : aSpamLearnFolder.listFiles()) {
			Double p = calculateProbabiltity(aMail);
			logger.debug("[TEST] SPAM: Spam possibility: {} %", p*100);
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
	public Double calculateProbabiltity(File inMail) throws IOException {
		// Get all words in this mail.
		String[] aListOfWords = readMail(inMail).split("\\s+");
		Double s = 1.0;
		Double h = 1.0;
		Double denominatorPart2 = 1.0;
		
		// check if there are words that are neither in ham nor in spam
		for (String aWord : aListOfWords) {
			if(ham.get(aWord) == null && spam.get(aWord) == null){
				continue;
			}
			h = ham.getCount(aWord) / totalHamMails;
			s = spam.getCount(aWord) / totalSpamMails;
			
			denominatorPart2 *= h/s;
		}
				
		return 1 / (1 + denominatorPart2);
	}

	/**
	 * If a single word is only in the hams word list, then the counter of the
	 * equation will be 0, thus the hole equation will be zero. If a single word
	 * is only in the spams word list, then the second summand of the
	 * denominator will be zero, thus the whole equation will be one. <br>
	 * So this method will equalize the word maps so each word map has the same
	 * words.
	 */
	public void equalizeWordMaps() {

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
	 * @return message as string of the mail
	 */
    public String readMail(File mail) {
        // Used for Session
        Properties props = new Properties();
        // Create Mail Session
        Session session = Session.getDefaultInstance(props);
        
        MimeMessage email = null;
        String message = "";
        try {
            // Read Mail
            FileInputStream fis = new FileInputStream(mail);
            email = new MimeMessage(session, fis);
            
            // Get Content to check if Multipart or text only
            Object content = email.getContent();
            
            if (content instanceof Multipart) {
                // Prepare Content
                BodyPart clearTextPart = null;
                BodyPart htmlTextPart = null;
                
                // Get the content
                Multipart c = (Multipart) content;
                // Get the types
                int count = c.getCount();

                // Iterate over the Mimetypes
                for(int i=0; i<count; i++) {
                    BodyPart part =  c.getBodyPart(i);
                    
                    // If we are in the plain mode get the plain text
                    if(part.isMimeType("text/plain")) {
                        clearTextPart = part;
                        break;
                    }
                    // else get the html part
                    else if(part.isMimeType("text/html")) {
                        htmlTextPart = part;
                    }
                }
                // We only use one Part
                // If we have both we use the text part
                if (clearTextPart != null) {
                    message = (String) clearTextPart.getContent();
                } else if (htmlTextPart != null) {
                    String html = (String) htmlTextPart.getContent();
                    message = Jsoup.parse(html).text();

                
                } 
            // a simple text message
            } else if (content instanceof String) {
                message = (String) content;
            }

        } catch (MessagingException e) {
            logger.error("nMessage could not be read", mail);
        } catch (FileNotFoundException e) {
            logger.error("File {} not found on system", mail);
        } catch (DecodingException e) {
            logger.error("Could not decode the Mail", mail);
        } catch (UnsupportedEncodingException e) {
            logger.error("Unsupported Encoding", mail);
        } catch (IOException e) {
            logger.error("nUnexpected Error in Reading mail", mail);
        }
        return message;
	}


}
