package ADP;

import java.io.*;
import java.util.*;

public class PropertiesQa {

    String sourceFolder;
    String targetFolder;

    List<File> targetFiles = new ArrayList<>();

//    InputStream inputStreamSource;
    BufferedReader sourceReader;


    public PropertiesQa(String sourceFolder, String targetFolder) throws FileNotFoundException {
        this.sourceFolder = sourceFolder;
        this.targetFolder = targetFolder;
System.out.println(sourceFolder);
System.out.println(targetFolder);
        File[] folders = new File(this.sourceFolder).listFiles();

        for (File f : folders) {

            File[] files = f.listFiles();

            for (File target : files) {

                if (target.getName().endsWith(".properties")) {
                    this.targetFiles.add(target);
                }

            }

        }

        File source = new File(this.sourceFolder).listFiles()[1];

//        this.inputStreamSource = new BufferedInputStream(new FileInputStream(source));

        this.sourceReader = new BufferedReader(new InputStreamReader(new FileInputStream(source)));


    }

    public void produce() throws IOException {

        Properties propertiesSource = new Properties();

        propertiesSource.load(this.sourceReader);

        for (File f : this.targetFiles) {
//System.out.println("...");
            this.produce(f, propertiesSource);


        }


    }

    private void produce(File target, Properties propertiesSource) throws IOException {

        StringBuilder sb = new StringBuilder();

        Set<Object> keys = propertiesSource.keySet();

//        BufferedInputStream targetInputStream = new BufferedInputStream(new FileInputStream(target));

        BufferedReader targetReader = new BufferedReader(new InputStreamReader(new FileInputStream(target)));

        Properties propertiesTarget = new Properties();

        propertiesTarget.load(targetReader);

        for (Object key : keys) {

            String keyString = (String) key;

            String sourceString = propertiesSource.getProperty(keyString);
            System.out.println(sourceString);
            String targetString = propertiesTarget.getProperty(keyString);
            System.out.println(targetString);
            sb.append(sourceString).append("\t").append(targetString).append("\r\n");

        }

        String uniocodeTexts = sb.toString();

        String parent = target.getParent();

        File file = new File(new File(parent).getParent() + "\\" + new File(parent).getName() + "properties.txt");

        FileOutputStream fos = new FileOutputStream(file);

        fos.write(uniocodeTexts.getBytes());

    }

    public static void main(String[] args) throws IOException {

        new PropertiesQa("C:\\Users\\trunk\\OneDrive\\桌面\\workaholic\\200548\\eqa\\AIM-June-2020",
                "C:\\Users\\trunk\\OneDrive\\桌面\\workaholic\\200548\\eqa\\target").produce();

    }

}
