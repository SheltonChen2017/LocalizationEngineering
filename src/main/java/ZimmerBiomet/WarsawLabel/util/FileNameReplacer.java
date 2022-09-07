package ZimmerBiomet.WarsawLabel.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileNameReplacer {

    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String path = br.readLine();

        File[] files = new File(path).listFiles();

        for(File f: files){
            String s = f.getName().replaceAll(".dita", ".xml");
            f.renameTo(new File(f.getParent()+"\\"+s));
        }
    }

//

}
