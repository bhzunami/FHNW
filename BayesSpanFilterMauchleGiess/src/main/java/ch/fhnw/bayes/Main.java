package ch.fhnw.bayes;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        SpamFilter sf = new SpamFilter();

        // learn phase
        System.out.println("========= Learn Phase =========");
        sf.learn();
        
        // If words exist only in ham or spam, word maps must be manipulated.
        // For more info see javadoc of this method.
        sf.equalizeWordMaps();
        
        // calibrate phase
//        System.out.println("========= Calibration Phase =========");
//        sf.calibrate();
        
        System.out.println("========== Test Phase ===========");
        sf.test();

    }

}
