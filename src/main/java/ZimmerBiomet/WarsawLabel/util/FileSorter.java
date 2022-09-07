package ZimmerBiomet.WarsawLabel.util;

import java.io.*;
import java.util.ArrayList;

public class FileSorter {
    String path;

    ArrayList<File> list = new ArrayList<File>();

    public void read(String path) throws IOException {

        File file = new File(path);

        if (file == null) {
            return;
        }

        File[] files = file.listFiles();

        for (File f : files) {

            if (f.isDirectory()) {

                this.read(f.getPath());

            } else {

                System.out.println(f.getPath());

                File newFile = new File("P:\\Projects\\1909\\164\\2-Production\\C-Engineering\\2-Build\\20190930\\JSON" + "\\" + f.getName());

                FileOutputStream fos = new FileOutputStream(newFile);

                FileInputStream fis = new FileInputStream(f);

                byte[] buf = new byte[99999];
                int len = fis.read(buf);

                String str = new String(buf, 0,len);

                fos.write(str.getBytes());



            }


        }


    }

    public FileSorter() {
    }

    public static void main(String[] args) throws IOException {
        FileSorter fs = new FileSorter();

        fs.read("P:\\Projects\\1909\\164\\2-Production\\C-Engineering\\2-Build\\20190930\\New folder\\Target-Spanish (Spain) - UTF8");
    }
}
