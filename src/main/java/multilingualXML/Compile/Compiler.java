package multilingualXML.Compile;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Compiler {

    String sourcePath;
    String translationPath;
    String finalPath;
    //    SAXReader translationReader;
    Document sourceDoc;

    public final String nameRegex = "_(.*?)\\.xliff";

    final static ResourceBundle bundle = ResourceBundle.getBundle("multilingualLanSelector");

    List<File> translatedList;

    public Compiler(String sourcePath, String translationPath, String finalPath) throws DocumentException {
        this.sourcePath = sourcePath;
        this.translationPath = translationPath;
        this.finalPath = finalPath;
        this.sourceDoc = new SAXReader().read(new File(sourcePath).listFiles()[0]);
        this.translatedList = Arrays.asList(new File(translationPath).listFiles());
    }

    public void produce() throws DocumentException, IOException {

        for(File f: this.translatedList){

            Document translationDoc = new SAXReader().read(f);

            this.produce(this.sourceDoc, translationDoc, f.getName());

        }


    }

    private void produce(Document sourceDoc, Document translationDoc, String transFileName) throws IOException {

        String lan = this.parseName(transFileName);
        String lanCode = this.bundle.getString(lan);
        Element sourceRoot = sourceDoc.getRootElement();
        List<Element> sourceunits = sourceRoot.elements();

        Element translationbody = translationDoc.getRootElement().element("file").element("body");

        List<Element> transunits = translationbody.elements();

        for(int i=0;i<transunits.size();i++){

            Element elementSource = sourceunits.get(i);
            Element target = transunits.get(i).element("target");
            String targetString = target.getText();
            List<Element> lanElements = elementSource.elements();

            for(Element lanElement: lanElements){

                if(lanElement.getName().equals(lan)){
                    System.out.println("------");
                    lanElement.setText(targetString);

                }

            }


        }

        this.writeDoc(sourceDoc, this.finalPath);

    }

    private void writeDoc(Document sourceDoc, String finalPath) throws IOException {

        OutputFormat print = OutputFormat.createPrettyPrint();
        print.setEncoding("utf-8");
        XMLWriter writer = new XMLWriter(new FileOutputStream(new File(finalPath+"\\"+"final"+".xml")), print);
        writer.write(sourceDoc);

    }

    private String parseName(String transFileName) {
        Pattern pattern = Pattern.compile(this.nameRegex);

        Matcher matcher = pattern.matcher(transFileName);

        matcher.find();

        return matcher.group(1);

    }

    public static void main(String[] args) throws DocumentException, IOException {
        new Compiler("C:\\Users\\trunk\\OneDrive\\桌面\\demo_project\\Project3_multilingualXML\\compile\\source",
                "C:\\Users\\trunk\\OneDrive\\桌面\\demo_project\\Project3_multilingualXML\\compile\\translated",
               "C:\\Users\\trunk\\OneDrive\\桌面\\demo_project\\Project3_multilingualXML\\compile\\final" ).produce();
    }

}
