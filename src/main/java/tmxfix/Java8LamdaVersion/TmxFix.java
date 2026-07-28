package tmxfix.Java8LamdaVersion;

import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xml.sax.SAXException;

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
    ArrayList<File> list = new ArrayList<>();


    public TmxFix(String path) {
        this.path = path;
        File[] files = new File(this.path).listFiles();
        if (files == null) {
            throw new IllegalArgumentException("TMX folder does not exist or is not readable: " + path);
        }

        for (File f : files) {

            this.list.add(f);

        }
    }

    public void produce() throws DocumentException, IOException {

        for (File f : this.list) {

            if (f.isFile() && f.getName().toLowerCase(Locale.ROOT).endsWith(".tmx")
                    && !f.getName().endsWith("fixed.tmx") && !f.getName().endsWith("backup.tmx")) {
                //ooperations start
                this.produce(f);
            }
        }

    }

    private void produce(File f) throws DocumentException, IOException {
        Set<String> seenIds = new HashSet<>();
        Set<String> duplicateIds = new LinkedHashSet<>();
        List<Element> backupList = new ArrayList<>();
        //load the whole xml(tmx) document
        Document doc = newSecureReader().read(f);

        Element root = doc.getRootElement();
        //gather all elements with xids into a single container.
        List<Element> elements = root.selectNodes(this.xpath).stream().map(node -> (Element) node).collect(Collectors.toList());

        for (Element e : elements) {
            //get xid value
            String value = e.getStringValue();
            //put all xid into the filter map. duplicate xids will be put into the duplicate map.
            if (!seenIds.add(value)) {
                //this way, all duplicate xids will be in the duplicate map.
                duplicateIds.add(value);
            }

        }

        //grab the container for all duplicate xids.
        for (String stringId : duplicateIds) {
            System.out.println("Extracting entries with duplicate xid " + stringId + "...");
            //xpath to select translation units with duplicate xids as the value of the prop element
            List<Element> elementsDup = root.selectNodes(xpathTU).stream()
                    .map(node -> (Element) node)
                    .filter(tu -> tu.elements("prop").stream().anyMatch(prop ->
                            "x-ID".equals(prop.attributeValue("type"))
                                    && stringId.equals(prop.getStringValue())))
                    .collect(Collectors.toList());


            System.out.println("There are " + elementsDup.size() + " elements in this duplicate container with xid "+ stringId);

            //sort the entries based on the change date.
            Collections.sort(elementsDup, (e1, e2) -> {

                return normalizedChangeDate(e1).compareTo(normalizedChangeDate(e2));

            });

            //if there are more than 2 entries with duplicate xid, remove until there are only two of them.
            if (elementsDup.size() > 2) {
                for (int i = 0; i < elementsDup.size() - 2; i++) {

                    Element elementToBeRemoved = elementsDup.get(i);
                    System.out.println("For xid "+stringId+ " more than 2 entries are found. Process initiated to remove until there are only two.");
                    //remove the entry
                    elementToBeRemoved.detach();
                    //add the removed entry to the backup container.
                    backupList.add(elementToBeRemoved);

                }

            }

            Element e1 = elementsDup.get(elementsDup.size()-2);

            Element e2 = elementsDup.get(elementsDup.size()-1);

            String changeIdE1 = e1.attributeValue("changeid", "");
            String changeIdE2 = e2.attributeValue("changeid", "");
            //if the author is different, remove the older entry.
            if (!changeIdE1.equals(changeIdE2)) {
                System.out.println("For xid "+stringId+" a duplicate entry with different author is found. Removing the older one now.");
                e1.detach();
                backupList.add(e1);

            }


        }


        //generate the fixed file.
        OutputFormat format = OutputFormat.createPrettyPrint();
        try (FileOutputStream output = new FileOutputStream(outputPath(f, "fixed.tmx"))) {
            XMLWriter writer = new XMLWriter(output, format);
            writer.write(doc);
            writer.flush();
        }

        //create a clean xml
        List<Element> allTUs = root.selectNodes(this.xpathTU).stream().map(node -> (Element) node).collect(Collectors.toList());
        for (Element tu : allTUs) {
            tu.detach();
        }


        Element body = root.element("body");
        //attach all the backup unit to the clean xml
        for (Element backup : backupList) {
            backup.detach();
            body.add(backup);
        }

        //generate the backup file
        OutputFormat format2 = OutputFormat.createPrettyPrint();
        try (FileOutputStream output = new FileOutputStream(outputPath(f, "backup.tmx"))) {
            XMLWriter writer = new XMLWriter(output, format2);
            writer.write(doc);
            writer.flush();
        }

    }

    private static SAXReader newSecureReader() throws DocumentException {
        SAXReader reader = new SAXReader();
        try {
            reader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            reader.setFeature("http://xml.org/sax/features/external-general-entities", false);
            reader.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        } catch (SAXException e) {
            throw new DocumentException("Unable to configure secure XML parser", e);
        }
        return reader;
    }

    private static String normalizedChangeDate(Element element) {
        String value = element.attributeValue("changedate", "");
        return value.length() >= 8 ? value.substring(0, 8) : value;
    }

    private static String outputPath(File input, String suffix) {
        String path = input.getPath();
        int extension = path.lastIndexOf('.');
        return (extension < 0 ? path : path.substring(0, extension)) + suffix;
    }

    public static void main(String[] args) throws DocumentException, IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("please put in the path");
        String path = br.readLine();

        new TmxFix(path).produce();


    }


}
