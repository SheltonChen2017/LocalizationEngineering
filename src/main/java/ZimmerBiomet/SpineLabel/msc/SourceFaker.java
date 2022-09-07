package ZimmerBiomet.SpineLabel.msc;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SourceFaker {

    public static final String path = "C:\\Users\\trunk\\OneDrive\\桌面\\workaholic\\spineLabelTesting\\test2\\source\\New Text Document.txt";

    public static final String regex = "\\[\\d\\]";


    public SourceFaker() {
    }

    public void produce() throws IOException {

        FileInputStream fis = new FileInputStream(new File(this.path));

        BufferedReader br = new BufferedReader(new InputStreamReader(fis));

        String str = null;

        while((str=br.readLine())!=null){

            System.out.println(str);

            this.changeBack(str);

        }

    }

    private void changeBack(String str) {

        Pattern pattern = Pattern.compile(this.regex);

        Matcher matcher = pattern.matcher(str);

        while(matcher.find()!=false){

        }

    }

    public static void main(String[] args) {

//        SourceFaker faker = new SourceFaker();



    }

}
