package Deprecated;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import java.io.File;
import java.io.IOException;

public class PDFReader {
    public static void main(String[] args) {

        try (PDDocument document = PDDocument.load(new File("P:\\dls\\Quotes\\0220\\16-DLS\\prep_20200213\\Moxy Hotels Global Playbook.pdf"))) {

            document.getClass();

            if(!document.isEncrypted()) {
                PDFTextStripperByArea stripper = new PDFTextStripperByArea();
                stripper.setSortByPosition(true);
                PDFTextStripper tStripper = new PDFTextStripper();

                String pdfFileInText = tStripper.getText(document);

                String[] lines = pdfFileInText.split("\\r?\\n");
                for(String line : lines) {
                    System.out.println(line);
                }

            }

        } catch (InvalidPasswordException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }

    }

}
