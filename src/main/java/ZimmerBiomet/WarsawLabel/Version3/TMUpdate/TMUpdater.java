package ZimmerBiomet.WarsawLabel.Version3.TMUpdate;

//import jdk.internal.util.xml.XMLStreamException;
//import jdk.internal.util.xml.impl.XMLWriter;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.*;

public class TMUpdater {

    public static Document generateTemplate() {

        Document doc = DocumentHelper.createDocument();

        Element tmx = doc.addElement("tmx");

        Element header = tmx.addElement("header");

        header.addAttribute("creationtool", "WarsawLabelTNUpdater");
        header.addAttribute("segtype", "sentence");
        header.addAttribute("adminlang", "en-US");
        header.addAttribute("srclang", "en-US");
        header.addAttribute("creationtoolversion", "1.0");
        header.addAttribute("o-tmf", "WarsawProcessor");
        header.addAttribute("datatype", "xml");

        Element body = tmx.addElement("body");
        body.addAttribute("id", "001");

        return doc;

    }

    public static void main(String[] args) throws IOException, InvalidFormatException {
        Document doc = TMUpdater.generateTemplate();

        Element tmx = doc.getRootElement();
        Element body = tmx.addElement("body");


        XSSFWorkbook workbook = new XSSFWorkbook(new File("P:\\Projects\\1909\\140\\2-Production\\E-Ling_QA\\New folder\\MC217903 Translations_Swedish.xlsx"));

        XSSFSheet sheet = workbook.getSheetAt(0);


        for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
            XSSFRow row = sheet.getRow(i);
            Element tu = body.addElement("tu");

            Element tuv = tu.addElement("tuv");

//        Element tu`1` = tuv.addElement("tu");

            tuv.addAttribute("xml:lang", "en-US");
            Element seg = tuv.addElement("seg");
            seg.setText(row.getCell(1).getStringCellValue());

            Element tuv2 = tu.addElement("tuv");

            tuv2.addAttribute("xml:lang", "sv-SV");
            Element seg2 = tuv2.addElement("seg");
            seg2.setText(row.getCell(2).getStringCellValue());
        }


        File file = new File("swedish2.tmx");

        OutputFormat format = OutputFormat.createPrettyPrint();

        format.setEncoding("UTF-8");
        XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);

        writer.write(doc);

    }
}
