package ZimmerBiomet.SpineLabel.TestCaseForJoe;

import ZimmerBiomet.SpineLabel.PostQa.PostQaCompiler;
import ZimmerBiomet.SpineLabel.PreQa.WSFilesQaGenerator;
import ZimmerBiomet.SpineLabel.Prep.FilePrepper_new;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RunApp {

    public static void main(String[] args) throws IOException, InvalidFormatException {

        System.out.println("What do you want me to do? Enter 1 for File Prep, 2 for Pre Qa Compilation, 3 for Post Qa Compilation");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String input = br.readLine();

        if(input.equalsIgnoreCase("1")){

            System.out.println("Please input the folder where the source file is located.");
            String filePrepFolder = br.readLine();

            new FilePrepper_new(filePrepFolder).produce();

        } else if(input.equals("2")){

//            new BufferedReader(new InputStreamReader(System.in)).readLine();
            System.out.println("Please input the folder path where the source file is located");
            String source = br.readLine();

            System.out.println("Please input the folder path where translations downloaded from World Server are located");
            String translations = br.readLine();

            System.out.println("Please input the folder path where you want the QA files saved");
            String qaPath = br.readLine();

            new WSFilesQaGenerator(source, translations, qaPath).produce();

        }else if (input.equals("3")){

            System.out.println("Please state the folder path where you saved the source file");
            String master = br.readLine();

            System.out.println("Please state the folder path where you saved all the files back from the QA (Review)");
            String translations = br.readLine();

            System.out.println("Please state the folder where you want me to save the compiled Master Translation");
            String compile = br.readLine();

            new PostQaCompiler(master, translations, compile).produce();

        }else {

            System.out.println("not a valid input. Please start again.");

        }

    }

}
