package ADP;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class ADPTmxExtract {
    //the xpath used to identify translation unit.
    public static String xPathTu = "//tu";
    //list used to store TUs that meet the condition.
    ArrayList<Element> validElments = new ArrayList<>();

    //Path to the folder where tmx files are saved.
    String folderPath;
    //list used to store all the files in the folder path.
    ArrayList<File> list = new ArrayList();

    Integer startDate = 20210101;
//    Integer endDate = 20211231;

    //List of creation and change IDs.
    List<String> authorIds = Arrays.asList("22701", "26095", "639452", "639212");

    //Constructor used to load files.
    public ADPTmxExtract(String folderPath) {
        this.folderPath = folderPath;
        File[] files = new File(this.folderPath).listFiles();
        for (File f : files) {
            this.list.add(f);
        }
    }

    public void produce() throws DocumentException, IOException {
        for (File f : this.list) {

            if (f.getName().endsWith("tmx")) {
                //ooperations start
                this.produce(f);
            }
        }
    }

    private void produce(File f) throws DocumentException, IOException {

        Document doc = new SAXReader().read(f);
        Element root = doc.getRootElement();

        List<Element> elements = root.selectNodes(this.xPathTu).stream().map(node -> (Element) node).collect(Collectors.toList());

        for (Element e : elements) {
            e.detach();
            int creationDate = Integer.valueOf(e.attribute("creationdate").getText().substring(0, 8));
            int changeDate = Integer.valueOf(e.attribute("changedate").getText().substring(0, 8));


            String changeid = e.attribute("changeid").getText().split(",").length >= 2 ? e.attribute("changeid").getText().split(",")[1] : "00000";
            String creationid = e.attribute("creationid").getText().split(",").length >= 2 ? e.attribute("creationid").getText().split(",")[1] : "00000";


            if ((creationDate >= this.startDate || changeDate >= this.startDate) && (this.authorIds.contains(changeid) || this.authorIds.contains(creationid))) {
                e.detach();
                this.validElments.add(e);
            }

        }

        for (Element e : this.validElments) {
            root.add(e);
        }
        OutputFormat format2 = OutputFormat.createPrettyPrint();
        XMLWriter writer2 = new XMLWriter(new FileOutputStream(f.getPath().substring(0, f.getPath().lastIndexOf(".")) + "backup.tmx"), format2);
        writer2.write(doc);
    }

    public static void main(String[] args) throws DocumentException, IOException {
        new ADPTmxExtract("C:\\Users\\trunk\\OneDrive\\desktop\\tm manipulate adp\\French France").produce();
    }

}
