package ZimmerBiomet.WarsawLabel.Version3;

import ZimmerBiomet.WarsawLabel.Version3.FilePrep.FilePrepper;
import ZimmerBiomet.WarsawLabel.Version3.postQa.AdvancedCompiler;
import ZimmerBiomet.WarsawLabel.Version3.preQa.QaPreparer;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AppInitiator {
    public static void main(String[] args) throws IOException, InvalidFormatException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Press 'A' if you want me to prep file. Press 'B' to generate documents for QA. Press 'C' to compile the finalized translation back into the master spread sheet");
        String purpose = br.readLine();

        if (purpose.equals("A")) {

            FilePrepper.produce();

        } else if (purpose.equals("B")) {
            QaPreparer.produce();
        } else if (purpose.equals("C")) {
            AdvancedCompiler.produce();
        } else {

            System.out.println("Not a Valid Input! Please start over");

        }

    }
}
/*
 *
 *
 * */