package ch.fhnw.bayes;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    
    public static SpamFilter initalize(Boolean debug) throws IOException {
        SpamFilter sf = new SpamFilter();
        System.out.println("========= Train Phase =========");
        sf.train();
        
        // calibrate phase
        if(debug) {
//            System.out.println("========= Calibration Phase =========");
//            sf.calibrate();
            
            System.out.println("========== Test Phase ===========");
            sf.test();
        }
        return sf;
        
    }

    /**
     * Main Mehtod: Start the Application
     * @param args 
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        
        Boolean debug = true;
        SpamFilter sf = initalize(debug);
        
        // Read manually
        System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ");
        System.out.println("Manually input.");
        System.out.print("Add file (full path):");
        try{
            String input;
            Double p;
            File mail;
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));  
            while(!(input=br.readLine()).equals("quit")){
                mail = new File(input);
                p = sf.calculateProbabiltity(mail);
                System.out.println("This mail has the probability " +p*100 +" % to be a spam Mail");
                System.out.println("Do you want to mark this mail as SPAM Mail? (y / n)");
                switch (input=br.readLine()) {
                    case "y":
                        sf.learnSpam(mail);
                        break;
                    case "n":
                        sf.learnHam(mail);
                        break;
                    default:
                        System.out.println("Nothing specified.");
                }
                sf.equalizeWordMaps();
                System.out.print("Add a additional file (full path) or 'quit':");

            }
                
        } catch(IOException io){
            io.printStackTrace();
        }  
        System.out.println("Programm finished!");
        
        

    }

}
