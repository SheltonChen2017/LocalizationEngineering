package tmxfix.Java7Version;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class TmxFix_deprecated {

    String path;

    public static String xpath = "//prop[@type='x-ID']";

    public static String xpathTU = "//tu";

    HashMap<String, String> testMap = new HashMap<>();
    HashMap<String, String> duplicateIdMap = new HashMap<>();
    ArrayList<Element> backupList = new ArrayList<>();
    ArrayList<File> list = new ArrayList<>();

//    public static

    public TmxFix_deprecated(String path) {
        this.path = path;
        File[] files = new File(this.path).listFiles();

        for (File f : files) {

            this.list.add(f);

        }
    }

    public void produce() throws DocumentException, IOException {

        for (File f : this.list) {

            if (f.getName().endsWith("tmx")) {
                this.produce(f);
            }
        }

    }

    private void produce(File f) throws DocumentException, IOException {
        Document doc = new SAXReader().read(f);

        Element root = doc.getRootElement();

        List<Element> elements = root.selectNodes(this.xpath).stream().map(node -> (Element)node).collect(Collectors.toList());

        for (Element e : elements) {

            String value = e.getStringValue();

            if (!testMap.containsKey(value)) {
                testMap.put(value, "");
            } else {
                duplicateIdMap.put(value, "");
            }

        }

        Set<String> dupIds = duplicateIdMap.keySet();


        for (String stringId : dupIds) {


            String xPathSelect = "//tu[prop=" + stringId + "]";

            List<Element> elementsDup = root.selectNodes(xPathSelect).stream().map(node -> (Element)node).collect(Collectors.toList());

            System.out.println(stringId);
            System.out.println(elementsDup.size());

            Collections.sort(elementsDup, (e1, e2) -> {

                String changeDate1 = e1.attribute("changedate").getText().substring(0, 8);
                String changeDate2 = e2.attribute("changedate").getText().substring(0, 8);

                return Integer.valueOf(changeDate1) - Integer.valueOf(changeDate2);

            });

            if (elementsDup.size() > 2) {
                for (int i = 0; i < elementsDup.size() - 1; i++) {

                    Element elementToBeRemoved = elementsDup.get(i);

                    this.backupList.add(elementToBeRemoved);

                    elementToBeRemoved.detach();
                }
            } else if (elementsDup.size() == 2) {

                Element e1 = elementsDup.get(0);

                Element e2 = elementsDup.get(1);

                String creationIdE1 = e1.attribute("creationid").getValue();

                String creationIdE2 = e2.attribute("creationid").getValue();

                if (!creationIdE1.equals(creationIdE2)) {
                    this.backupList.add(e1);
                    e1.detach();
                }
            }


        }


//root.selectNodes("//tu")

        OutputFormat format = OutputFormat.createPrettyPrint();

        XMLWriter writer = new XMLWriter(new FileOutputStream(f.getPath().substring(0, f.getPath().lastIndexOf(".")) + "fixed.xml"), format);

        System.out.println("the size of the back up list is " + this.backupList.size());

        writer.write(doc);

        List<Element> allTUs = root.selectNodes(this.xpathTU).stream().map(node -> (Element)node).collect(Collectors.toList());

        for (Element tu : allTUs) {
            tu.detach();
        }

        Element body = root.element("body");

        for (Element backup : this.backupList) {

            body.add(backup);

        }

        OutputFormat format2 = OutputFormat.createPrettyPrint();

        XMLWriter writer2 = new XMLWriter(new FileOutputStream(f.getPath().substring(0, f.getPath().lastIndexOf(".")) + "backup.xml"), format2);

        writer2.write(doc);

    }

    public static void main(String[] args) throws DocumentException, IOException {
        new TmxFix_deprecated("D:\\New folder (3)").produce();
    }


}
