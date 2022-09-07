package ADP;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class JsonQa {
    String sourceFolder;
    String targetFolder;

    List<File> targetFiles = new ArrayList<>();

File source;


    public JsonQa(String sourceFolder, String targetFolder) throws FileNotFoundException {
        this.sourceFolder = sourceFolder;
        this.targetFolder = targetFolder;


        File[] folders = new File(this.targetFolder).listFiles();

        for(File f:folders){

            File[] files = f.listFiles();

            for(File target: files){
                System.out.println(target.getName());
                if(target.getName().endsWith(".json")){

                    this.targetFiles.add(target);
                }

            }

        }



        this.source = new File(this.sourceFolder).listFiles()[0];
    }

    public void produce() throws IOException {

        String sourceContent = FileUtils.readFileToString(source, "UTF-8");
        JSONObject sourceObject = new JSONObject(sourceContent);

        for(File target: this.targetFiles){
            this.produce(target, sourceObject);
        }

    }

    private void produce(File target, JSONObject sourceObject) throws IOException {

        JSONObject targetObject = new JSONObject(FileUtils.readFileToString(target, "utf-8"));

        Set<String> keys = sourceObject.keySet();

        StringBuilder sb = new StringBuilder();

        for(String key: keys){

            System.out.println("key");
            String sourceString  = (String) sourceObject.get(key);

            String targetString = (String) targetObject.get(key);


            sb.append(sourceString).append("\t").append(targetString).append("\r\n");


        }

        String uniocodeTexts = sb.toString();

        String parent = target.getParent();

        File file = new File(new File(parent).getParent() + "\\" + new File(parent).getName()+ "jsons.txt");

        FileOutputStream fos = new FileOutputStream(file);

        fos.write(uniocodeTexts.getBytes());
    }

    public static void main(String[] args) throws IOException {
        new JsonQa("C:\\Users\\trunk\\OneDrive\\桌面\\workaholic\\200548\\eqa\\AIM-June-2020",
                "C:\\Users\\trunk\\OneDrive\\桌面\\workaholic\\200548\\eqa\\target").produce();
    }

}
