package FICO.PreProduction;

import java.io.File;
import java.util.ArrayList;

public class FileRemover {

    String folder;

    ArrayList<File> files = new ArrayList<File>();

    public FileRemover(String folder) {
        this.folder = folder;

        this.loadFiles(folder, files);

    }

    private void loadFiles(String folder, ArrayList<File> fileList) {

        File[] files = new File(folder).listFiles();

        for(File f: files){

            if(!f.isDirectory()){
                fileList.add(f);
            }else {

                this.loadFiles(f.getPath(),fileList);

            }

        }

    }

    public void produce(){

        for(File f: this.files){

            if(!f.getName().toUpperCase().contains("FR_FR")){
                f.delete();
                System.out.println(f.getName());
            }

        }


    }


    public static void main(String[] args) {
        FileRemover remover = new FileRemover("C:\\Users\\trunk\\OneDrive\\桌面\\Career Related Access\\workaholic\\1120111_FicoDevelopment\\DM Files - working_adapted - forTranslation\\New\\DMLocalizationFiles_ToBeTranslated_2-9-21");

        remover.produce();


    }


}
