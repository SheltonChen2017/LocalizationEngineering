package ADP;

import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class ADPTmxTruncator {

    String path;
    ArrayList<File> tmxFiles = new ArrayList<>();

    public ADPTmxTruncator(String path) {
        this.path = path;

        loadFiles(path);

    }

    private void loadFiles(String path) {
        File[] files = new File(path).listFiles();

        for (File f : files) {

            if (f.isDirectory()) {
                this.loadFiles(f.getPath());
            } else {
                this.tmxFiles.add(f);
            }

        }
    }

    public void produce() throws DocumentException, IOException {

        for (File f : this.tmxFiles) {
            this.produce(f);
        }

    }

    private void produce(File f) throws DocumentException, IOException {


        Document doc = new SAXReader().read(f);

        Element root = doc.getRootElement();
        List<Element> elements = root.selectNodes(ADPTmxExtract.xPathTu).stream().map(node -> (Element) node).collect(Collectors.toList());

        List<Element> validElements = new ArrayList();

        int count = 0;

        for (Element e : elements) {
            e.detach();
            String tuPath = "./tuv[@xml:lang='en-US']/seg";

            Node node = e.selectSingleNode(tuPath);

            String content = node.getText();

            if (content.split(" ").length > 5) {
                e.detach();
                validElements.add(e);
            }
            count += content.split("\\s+").length;

            if (count >= 2000) break;

        }
        System.out.println(f.getName() + count);
        this.writeFile(validElements, f);

    }

    private void writeFile(List<Element> validElements, File f) throws IOException {
        OutputFormat format2 = OutputFormat.createPrettyPrint();

        Document newDoc = DocumentHelper.createDocument();
        Element root = newDoc.addElement("tmx");

        root.addAttribute("version", "1.4");

        for (Element e : validElements) {
            e.detach();
            root.add(e);
        }
        XMLWriter writer2 = new XMLWriter(new FileOutputStream(f.getPath().substring(0, f.getPath().lastIndexOf(".")) + "truncated.tmx"), format2);
        writer2.write(newDoc);
    }

    public static void main(String[] args) throws DocumentException, IOException {
        new ADPTmxTruncator("C:\\Users\\trunk\\OneDrive\\desktop\\tm manipulate adp\\forLQA\\truncatedTMX").produce();
    }

}
