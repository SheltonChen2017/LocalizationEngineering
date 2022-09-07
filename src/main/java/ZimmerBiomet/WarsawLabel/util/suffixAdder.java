package ZimmerBiomet.WarsawLabel.util;

import java.io.File;
import java.nio.file.Files;

public class suffixAdder {
    String folderpath;

    File[] files;
    
    public suffixAdder() {
    }

    public suffixAdder(String folderpath) {
        this.folderpath = folderpath;
        this.files = new File(folderpath).listFiles();
    }
    
    public void execute(){
        
        for(File f: this.files){
            
            this.execute(f.getName(), f.listFiles());
            
        }
        
    }

    private void execute(String lanName, File[] listFiles) {

        for(File f: listFiles){
            String path = f.getPath();
//            System.out.println(path);
            String original = f.getPath();

            String prefix = original.substring(0, original.lastIndexOf("."));

            String newName = prefix+"_"+lanName+".xlsx";

            System.out.println(newName);

            boolean b = f.renameTo(new File(newName));

            System.out.println(b);

        }


    }

    public static void main(String[] args) {

    }

}
