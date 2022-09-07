package Deprecated;

import java.io.*;
import java.util.*;

public class HelpNick extends Properties{


    private static final long serialVersionUID = -4627607243846121965L;
    private final LinkedHashSet<Object> keys = new LinkedHashSet<Object>();

    public Enumeration<Object> keys() {
        return Collections.<Object> enumeration(keys);
    }

    public Object put(Object key, Object value) {
        keys.add(key);
        return super.put(key, value);
    }

    public synchronized Object remove(Object key) {
        keys.remove(key);
        return super.remove(key);
    }

    public LinkedHashSet<Object> keySet() {
        return keys;
    }

    public Set<String> stringPropertyNames() {
        Set<String> set = new LinkedHashSet<String>();
        for (Object key : this.keys) {
            set.add((String) key);
        }
        return set;

    }

    public static Properties readPropertiesFileObj(String filename) {
        HelpNick properties =new HelpNick();
        try {
            InputStream inputStream = new FileInputStream(filename);
            BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            properties.load(bf);
            inputStream.close(); // 关闭流
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
    public static void writePropertiesFileObj(String filename, Properties properties) {
        if (properties == null) {
            properties = new HelpNick();
        }

        LinkedHashSet<Object> keys = (LinkedHashSet<Object>) properties.keySet();


        try {
            OutputStream outputStream = new FileOutputStream(filename);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream, "utf-8"));

            for(Object key: keys){

                bw.write((String)key);
                bw.write("=");
                bw.write(properties.getProperty((String)key));
                bw.write("\r\n");

            }

            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {

        Properties properties = HelpNick.readPropertiesFileObj(new File("C:\\Users\\shelton\\Desktop\\helpNick\\4_eqa_20200212").listFiles()[0].getPath());

//        HelpNick.writePropertiesFileObj("C:\\Users\\shelton\\De);

        File newFile = new File("C:\\Users\\shelton\\Desktop\\helpNick\\4_eqa_20200212\\fixed.properties");

        HelpNick.writePropertiesFileObj(newFile.getPath(), properties);

    }

}
