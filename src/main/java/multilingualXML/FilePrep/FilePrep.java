package multilingualXML.FilePrep;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class FilePrep {

    String path;
    final static ResourceBundle bundle = ResourceBundle.getBundle("multilingualLanSelector");
    List<File> files;
    private String sourceLanguage = "EN";

    public FilePrep(String path) {
        this.path = path;
        this.files = Arrays.asList(new File(path).listFiles());
    }

    public void produce() throws DocumentException, IOException {

        for (File f : this.files) {
            if (f.getName().endsWith("xml")) {
                this.produce(f);
            }
        }

    }

    private void produce(File f) throws DocumentException, IOException {
        Document multilingualXML = new SAXReader().read(f);
        Element root = multilingualXML.getRootElement();

        System.out.println(root.getName());

        List<Element> elements = root.elements();

        Set<String> lans = bundle.keySet();

        for (String lan : lans) {
            XSSFWorkbook sheets = new XSSFWorkbook();
            Document xliff = this.generateRawXliff();
            XSSFSheet sheet = sheets.createSheet();
//      row.createCell(1).setCellValue(locale.getName());
            int i = 1;
            for (Element question : elements) {
 System.out.println(question.getName());
                String questioID = question.getName();
                Element engLocale = question.element(this.sourceLanguage);
                Element locale = question.element(lan);
                String sourceText = engLocale.getText();
//                System.out.println(sourceText);
                String translatedText = locale.getText();
                XSSFRow row = sheet.createRow(i++);
                row.createCell(0).setCellValue(sourceText);
                row.createCell(1).setCellValue(translatedText);

                Element body = xliff.getRootElement().element("file").element("body");

                Element tu = body.addElement("trans-unit").addAttribute("id", questioID);

                Element source = tu.addElement("source");
                Element target = tu.addElement("target");
//                System.out.println(target);
                source.addAttribute("xml:lang",this.bundle.getString(this.sourceLanguage));
                source.setText(sourceText);
                target.addAttribute("xml:lang", this.bundle.getString(lan));
                target.setText(translatedText);
                System.out.println(target.getText());

            }
            sheets.write(new FileOutputStream(new File(f.getPath() + "_" + lan + ".xlsx")));
//            xliff.write(new FileWriter(new File(f.getPath()+"_"+lan+".xliff")));
            this.writeXML(xliff,f.getName(),f.getParent(),lan);
        }


    }

    private void writeXML(Document xliff, String name, String parent, String lan) throws IOException {

        OutputFormat print = OutputFormat.createPrettyPrint();
        print.setEncoding("utf-8");
        XMLWriter writer = new XMLWriter(new FileOutputStream(new File(parent+"\\"+name+"_"+lan+".xliff")), print);
        writer.write(xliff);
    }

    private Document generateRawXliff() {
        Document xliff = DocumentHelper.createDocument();

        Element xlf = xliff.addElement("xliff");

        xlf.addAttribute("xmlns", "urn:oasis:names:tc:xliff:document:1.2");
        xlf.addAttribute("version", "1.2");
        Element fileEle = xlf.addElement("file");
        fileEle.addAttribute("original", "global");
        fileEle.addAttribute("datatype", "plaintext");
        fileEle.addAttribute("source-language", this.bundle.getString(this.sourceLanguage));
        fileEle.addElement("body");

        return xliff;

    }

    public static void main(String[] args) throws IOException, DocumentException {
        new FilePrep("C:\\Users\\trunk\\OneDrive\\桌面\\Translations\\Translations").produce();
    }
}
