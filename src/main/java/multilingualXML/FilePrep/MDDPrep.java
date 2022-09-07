package multilingualXML.FilePrep;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
//import org.dom4j.*;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.*;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class MDDPrep {


    public static final String xPathExtract = "//labels";

//    public static ResourceBundle bundle = ResourceBundle.getBundle("mddMap");


    HashMap<String, XSSFWorkbook> map = new HashMap<>();

    {


        Set<String> keys = this.lansList.keySet();

        for (String key : keys) {
            this.map.put(key, new XSSFWorkbook());
        }


    }

    String path;
    public static final ResourceBundle lansList = ResourceBundle.getBundle("mddMap");
    List<File> files;
    private String sourceLanguage = "EN";

    public MDDPrep(String path) {
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

    private void produce(File f) throws DocumentException, DocumentException, IOException {


        Document doc = new SAXReader().read(f);

        Element root = doc.getRootElement();

        List<Element> labelsList = root.selectNodes(this.xPathExtract).stream().map(node -> (Element)node).collect(Collectors.toList());
        int i = 0;
        int n = 0;
        for (Element label : labelsList) {

            label.addAttribute("translationId", String.valueOf(i++));
            Attribute idAttribute = label.attribute("translationId");
            String idValue = idAttribute.getText();
            Set<String> keys = this.lansList.keySet();
//karl@unity3d.com
            for (String lan : keys) {

                String xpathAnalysis = ".//text[@xml:lang=" + "'" + lan + "'" + " and " + "@context=" + "'" + "ANALYSIS" + "']";
                String xpathQuestion = ".//text[@xml:lang=" + "'" + lan + "'" + " and " + "@context=" + "'" + "QUESTION" + "']";
                String xpathAnalysisForEnglish = ".//text[@xml:lang=" + "'" + "en-US" + "'" + " and " + "@context=" + "'" + "ANALYSIS" + "']";
                String xpathQuesionForEnglish = ".//text[@xml:lang=" + "'" + "en-US" + "'" + " and " + "@context=" + "'" + "QUESTION" + "']";
                Node nodeAnalysis = label.selectSingleNode(xpathAnalysis);
                Node nodeQuestion = label.selectSingleNode(xpathQuestion);
                Node nodeAnalysisEnglish = label.selectSingleNode(xpathAnalysisForEnglish);
                Node nodeQuestionEnglish = label.selectSingleNode(xpathQuesionForEnglish);

                String nodeAnalysisTextEnglish = "";
                String nodeQuestionTextEnglish = "";
                if (nodeAnalysisEnglish != null) {
                    nodeAnalysisTextEnglish = nodeAnalysisEnglish.getText();
                }

                if (nodeQuestionEnglish != null) {
                    nodeQuestionTextEnglish = nodeQuestionEnglish.getText();
                }


                String nodeAnalysisText = "";

                if (nodeAnalysis != null && nodeAnalysis.getText() != null && !nodeAnalysis.getText().trim().equals("")) {
                    nodeAnalysisText = nodeAnalysis.getText();
                } else {
                    nodeAnalysisText = nodeAnalysisTextEnglish;
                    Element newAnalysisElement = label.addElement("text");
                    newAnalysisElement.addAttribute("xml:lang", lan);
                    newAnalysisElement.addAttribute("context", "ANALYSIS");
                }

                String nodeQuestionText = "";

                if (nodeQuestion != null && nodeQuestion.getText() != null && !nodeQuestion.getText().trim().equals("")) {
                    nodeQuestionText = nodeQuestion.getText();
                } else {
                    nodeQuestionText = nodeQuestionTextEnglish;
                    Element newQuestionElement = label.addElement("text");
                    newQuestionElement.addAttribute("xml:lang", lan);
                    newQuestionElement.addAttribute("context", "QUESTION");
                }

                System.out.println(idValue);

//                System.out.println(nodeAnalysisText);
//                System.out.println(nodeQuestionText);

                XSSFWorkbook book = this.map.get(lan);

                XSSFSheet sheet;

                if (book.getNumberOfSheets() != 0) {
                    sheet = book.getSheetAt(0);
                } else {
                    sheet = book.createSheet();
                }

                XSSFCellStyle ignoreStyle = book.createCellStyle();

                XSSFFont ignoreFont = book.createFont();

                ignoreFont.setColor(IndexedColors.RED.getIndex());
                ignoreStyle.setFont(ignoreFont);

                XSSFRow row0 = sheet.createRow(0);

                XSSFCell cell0 = row0.createCell(0);
                cell0.setCellValue("translationId");
                XSSFCell cell1 = row0.createCell(1);
                cell1.setCellValue("ANALYSIS");
                XSSFCell cell2 = row0.createCell(2);
                cell2.setCellValue("QUESTION");

                cell0.setCellStyle(ignoreStyle);
                cell1.setCellStyle(ignoreStyle);
                cell2.setCellStyle(ignoreStyle);

                int m = sheet.getPhysicalNumberOfRows();

                XSSFRow row = sheet.createRow(m);
                XSSFCell cellT0 = row.createCell(0);
                cellT0.setCellValue(idValue);
                XSSFCell cellT1 = row.createCell(1);
                cellT1.setCellValue(nodeAnalysisText);
                XSSFCell cellT2 = row.createCell(2);
                cellT2.setCellValue(nodeQuestionText);

                cellT0.setCellStyle(ignoreStyle);

                if (nodeAnalysis != null && nodeAnalysis.getText() != null && !nodeAnalysis.getText().trim().equals("")) {
                    cellT1.setCellStyle(ignoreStyle);
                }
                if (nodeQuestion != null && nodeQuestion.getText() != null && !nodeQuestion.getText().trim().equals("")) {
                    cellT2.setCellStyle(ignoreStyle);
                }


            }


        }

        OutputFormat format = OutputFormat.createPrettyPrint();

        XMLWriter writer = new XMLWriter(new FileOutputStream(f.getPath().substring(0, f.getPath().lastIndexOf(".")) + "_prepped.xml"), format);

        writer.write(doc);

        Set<String> languages = this.map.keySet();

        for (String language : languages) {

            XSSFWorkbook book = this.map.get(language);

            FileOutputStream fos = new FileOutputStream(new File(f.getPath().substring(0, f.getPath().lastIndexOf("."))) + "_" + language + ".xlsx");

            book.write(fos);

        }
    }

    public static void main(String[] args) throws IOException, DocumentException {
        new MDDPrep("C:\\Users\\trunk\\OneDrive\\桌面\\Translations\\test(automaticFillNodes)").produce();
    }

}
