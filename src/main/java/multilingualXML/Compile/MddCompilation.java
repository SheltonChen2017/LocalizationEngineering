package multilingualXML.Compile;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MddCompilation {
    String compilePath;
    String lanFilesPath;
    Document doc;
    ArrayList<File> lanFiles = new ArrayList<>();

    public static String regex = "Translation Test - 1_(.+)\\.xlsx";
    public static String xPath1 = "//labels[@translationId]";


    public MddCompilation(String compilePath, String lanFilesPath) throws DocumentException {
        this.compilePath = compilePath;
        this.lanFilesPath = lanFilesPath;
        this.doc = new SAXReader().read(new File(this.compilePath).listFiles()[0]);
        File[] files = new File(this.lanFilesPath).listFiles();
        for (File f : files) {
            this.lanFiles.add(f);
        }
    }

    public void produce() throws IOException, InvalidFormatException {

        for (File f : this.lanFiles) {
            this.produce(f, this.doc);
        }

        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter writer = new XMLWriter(new FileOutputStream(this.compilePath + "_prepped.xml"), format);
        writer.write(doc);

    }

    private void produce(File f, Document doc) throws IOException, InvalidFormatException {
        XSSFWorkbook book = new XSSFWorkbook(f);
        XSSFSheet sheet = book.getSheetAt(0);
        Pattern p = Pattern.compile(this.regex);
        Matcher matcher = p.matcher(f.getName());
        matcher.find();
        String lanName = matcher.group(1);

        Element root = doc.getRootElement();

        List<Element> labels = root.selectNodes(this.xPath1).stream().map(node -> (Element)node).collect(Collectors.toList());

        String xPath2 = ".//text[@xml:lang='" + lanName + "']";

        System.out.println(xPath2);

        for (Element label : labels) {

            List<Element> textNodes = root.selectNodes(xPath2).stream().map(node -> (Element)node).collect(Collectors.toList());
//            System.out.println(textNodes.size());

            Attribute idAttr = label.attribute("translationId");

            String idValue = idAttr.getText();

            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {

                XSSFRow row = sheet.getRow(i);

                XSSFCell cell0 = row.getCell(0);
                XSSFCell cell1 = row.getCell(1);
                XSSFCell cell2 = row.getCell(2);


                String idValueMatch = cell0.getStringCellValue();
//                System.out.println(idValueMatch);
//                System.out.println(idValue);
                if (idValueMatch.equals(idValue)) {



                    for(Element textNode: textNodes){
                        System.out.println("here");
//                        System.out.println(textNode.attribute("context").getText());

                        if(textNode.attribute("context").getText().equals("QUESTION")){
                            textNode.setText(cell2.getStringCellValue());
                        }
                        if(textNode.attribute("context").getText().equals("ANALYSIS")){
                            textNode.setText(cell1.getStringCellValue());
                        }


                    }

                }


            }


        }


    }

    public static void main(String[] args) throws DocumentException, IOException, InvalidFormatException {
        new MddCompilation("C:\\Users\\trunk\\OneDrive\\桌面\\Translations\\compilationTest\\compilation",
                "C:\\Users\\trunk\\OneDrive\\桌面\\Translations\\compilationTest\\language specific files"
        ).produce();

    }
}
