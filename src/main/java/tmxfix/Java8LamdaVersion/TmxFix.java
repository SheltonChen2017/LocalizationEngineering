package tmxfix.Java8LamdaVersion;

import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class TmxFix {
    //path of the folder where tmxs are put
    String path;
    //xpath for elements with xids
    public static String xpath = "//prop[@type='x-ID']";
    //xpath for translation units
    public static String xpathTU = "//tu";
    //filter container
    HashMap<String, String> testMap = new HashMap<>();
    //container for duplicate IDs
    HashMap<String, String> duplicateIdMap = new HashMap<>();
    //container for units that need to be moved to backup file
    ArrayList<Element> backupList = new ArrayList<>();

    ArrayList<File> list = new ArrayList<>();


    public TmxFix(String path) {
        this.path = path;
        File[] files = new File(this.path).listFiles();

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
        //load the whole xml(tmx) document
        Document doc = new SAXReader().read(f);

        Element root = doc.getRootElement();
        //gather all elements with xids into a single container.
        List<Element> elements = root.selectNodes(this.xpath).stream().map(node -> (Element) node).collect(Collectors.toList());

        for (Element e : elements) {
            //get xid value
            String value = e.getStringValue();
            //put all xid into the filter map. duplicate xids will be put into the duplicate map.
            if (!testMap.containsKey(value)) {
                testMap.put(value, "");
            } else {
                //this way, all duplicate xids will be in the duplicate map.
                duplicateIdMap.put(value, "");
            }

        }

        //grab the container for all duplicate xids.
        Set<String> dupIds = duplicateIdMap.keySet();


        for (String stringId : dupIds) {
            System.out.println("Extracting entries with duplicate xid " + stringId + "...");
            //xpath to select translation units with duplicate xids as the value of the prop element
            String xPathSelect = "//tu[prop=" + stringId + "]";
            //select all TU entries with duplicate xid
            List<Element> elementsDup = root.selectNodes(xPathSelect).stream().map(node -> (Element) node).collect(Collectors.toList());


            System.out.println("There are " + elementsDup.size() + " elements in this duplicate container with xid "+ stringId);

            //sort the entries based on the change date.
            Collections.sort(elementsDup, (e1, e2) -> {

                String changeDate1 = e1.attribute("changedate").getText().substring(0, 8);
                String changeDate2 = e2.attribute("changedate").getText().substring(0, 8);

                return Integer.valueOf(changeDate1) - Integer.valueOf(changeDate2);

            });

            //if there are more than 2 entries with duplicate xid, remove until there are only two of them.
            if (elementsDup.size() > 2) {
                for (int i = 0; i < elementsDup.size() - 2; i++) {

                    Element elementToBeRemoved = elementsDup.get(i);
                    System.out.println("For xid "+stringId+ " more than 2 entries are found. Process initiated to remove until there are only two.");
                    //remove the entry
                    elementToBeRemoved.detach();
                    //add the removed entry to the backup container.
                    this.backupList.add(elementToBeRemoved);

                }

            }

            Element e1 = elementsDup.get(elementsDup.size()-2);

            Element e2 = elementsDup.get(elementsDup.size()-1);

            String changeIdE1 = e1.attribute("changeid").getValue();

            String changeIdE2 = e2.attribute("changeid").getValue();
            //if the author is different, remove the older entry.
            if (!changeIdE1.equals(changeIdE2)) {
                System.out.println("For xid "+stringId+" a duplicate entry with different author is found. Removing the older one now.");
                e1.detach();
                this.backupList.add(e1);

            }


        }


        //generate the fixed file.
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter writer = new XMLWriter(new FileOutputStream(f.getPath().substring(0, f.getPath().lastIndexOf(".")) + "fixed.tmx"), format);
        writer.write(doc);

        //create a clean xml
        List<Element> allTUs = root.selectNodes(this.xpathTU).stream().map(node -> (Element) node).collect(Collectors.toList());
        for (Element tu : allTUs) {
            tu.detach();
        }


        Element body = root.element("body");
        //attach all the backup unit to the clean xml
        for (Element backup : this.backupList) {
            backup.detach();
            body.add(backup);
        }

        //generate the backup file
        OutputFormat format2 = OutputFormat.createPrettyPrint();
        XMLWriter writer2 = new XMLWriter(new FileOutputStream(f.getPath().substring(0, f.getPath().lastIndexOf(".")) + "backup.tmx"), format2);
        writer2.write(doc);

    }

    public static void main(String[] args) throws DocumentException, IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("please put in the path");
        String path = br.readLine();

        new TmxFix(path).produce();


    }


}
