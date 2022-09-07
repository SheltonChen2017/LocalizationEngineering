package ZimmerBiomet.WarsawLabel.Version3.postQa;

//import jdk.internal.util.xml.impl.Input;

import ZimmerBiomet.WarsawLabel.Version3.TMUpdate.TMUpdater;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.util.*;

public class AdvancedCompiler {

    String folderForMasters;
    final static ResourceBundle bundle = ResourceBundle.getBundle("LANGUAGE");
    final static ResourceBundle tmxLangInfo = ResourceBundle.getBundle("tmxLangs");
    String folderForTranslations;
    final static String forImport = "TV-Import File";

    public AdvancedCompiler(String folderForMasters, String folderForTranslations) {
        this.folderForMasters = folderForMasters;
        this.folderForTranslations = folderForTranslations;
    }

    public static void produce() throws IOException, InvalidFormatException {
//        new AdvancedCompiler()
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Give me the full path for all Master Spread Sheets");
        String master = br.readLine();
        System.out.println("Give me the full path for all finalized translations");
        String translations = br.readLine();
        AdvancedCompiler compiler = new AdvancedCompiler(master, translations);
        HashMap map = compiler.compile();
        compiler.fix(map);
        compiler.writeDocument(map);

    }

    public HashMap compile() throws IOException, InvalidFormatException {

        List<File> sources = Arrays.asList(new File(this.folderForMasters).listFiles());


        List<File> translations = Arrays.asList(new File(this.folderForTranslations).listFiles());

        HashMap<File, XSSFWorkbook> map = new HashMap<>();

        for (File f : sources) {
//            System.out.println(f.getPath());
            XSSFWorkbook sourceBook = new XSSFWorkbook(f);

            for (File t : translations) {

                this.compile(f, sourceBook, t);

//                System.out.println(t.getPath());

            }

            map.put(f, sourceBook);
        }

        for (File t : translations) {

            Document document = this.generateTmx(new XSSFWorkbook(t), t.getName());

            this.writeTMX(document, t);

        }

//        this.writeBook();
        return map;
    }

    private void writeTMX(Document document, File f) throws IOException {

        File folder = new File(f.getParent() + "\\" + "tmx");

        if (!folder.isDirectory()) {
            folder.mkdir();
        }

        File file = new File(f.getParent() + "\\" + "tmx" + "\\" + this.parseLanguage(f.getName()) + f.getName() + ".tmx");

        OutputFormat format = OutputFormat.createPrettyPrint();

        format.setEncoding("UTF-8");
        XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
        writer.write(document);
    }

    private Document generateTmx(XSSFWorkbook sheets, String fileName) {

        Document doc = TMUpdater.generateTemplate();

        XSSFSheet workSheet = sheets.getSheetAt(0);

        for (int i = 0; i < workSheet.getPhysicalNumberOfRows(); i++) {

            try {
                this.generateTmx(doc, workSheet.getRow(i).getCell(0).getStringCellValue(), workSheet.getRow(i).getCell(4).getStringCellValue(), fileName);
            } catch (NullPointerException e) {
                e.printStackTrace();
                System.out.println("There is a Null Pointer Exception being caught");
                continue;

            }
        }

        return doc;

    }

    private void generateTmx(Document doc, String src, String tgt, String fileName) {

        String fileLang = this.parseLanguage(fileName);

        String lang = this.tmxLangInfo.getString(fileLang);

        Element tmx = doc.getRootElement();

        Element body = tmx.element("body");

        Element tu = body.addElement("tu");

        Element tuv = tu.addElement("tuv");

//        Element tu`1` = tuv.addElement("tu");

        tuv.addAttribute("xml:lang", "en-US");
        Element seg = tuv.addElement("seg");
        seg.setText(src);

        Element tuv2 = tu.addElement("tuv");

        tuv2.addAttribute("xml:lang", lang);
        Element seg2 = tuv2.addElement("seg");
        seg2.setText(tgt);


    }

    public void writeDocument(HashMap<File, XSSFWorkbook> map) throws IOException {

        Set<File> sets = map.keySet();

        for (File f : sets) {

            this.write(f, map.get(f));

        }


    }

    private void write(File f, XSSFWorkbook sheets) throws IOException {
        String name = f.getName();
        String newpath = this.folderForTranslations + "\\" + "compiled";
        if (!new File(newpath).exists()) {
            new File(newpath).mkdir();
        } else {
        }

        String newname = newpath + "\\" + name;

        FileOutputStream fos = new FileOutputStream(new File(newname));

        sheets.write(fos);

    }

    private void compile(File s, XSSFWorkbook sourceBook, File t) throws IOException, InvalidFormatException {
        String sPath = s.getName().substring(0, s.getName().lastIndexOf("."));
        String tPath = t.getName();
        String transformedTname = tPath.substring(0, tPath.lastIndexOf("_"));
        if (sPath.equals(transformedTname)) {
            this.doCompile(sourceBook, t);

        }

    }

    private void doCompile(XSSFWorkbook sourceBook, File t) throws IOException, InvalidFormatException {

        String lan = this.parseLanguage(t.getName());
        String abbrev = this.bundle.getString(lan);

//        System.out.println("The language is " + lan + " and the language abbrev is " + abbrev);

        XSSFWorkbook transbook = new XSSFWorkbook(t);

        XSSFSheet compilePage = sourceBook.getSheet(this.forImport);

        XSSFSheet workSheet = transbook.getSheetAt(0);

        ArrayList<XSSFCell> sourceCells = new ArrayList<>();

        int max = this.findMaxRowIndex(compilePage);

        for (int i = 1; i <= max; i++) {


            XSSFRow row = compilePage.getRow(i);
//            System.out.println(i);
            String columnLan = row.getCell(1).getStringCellValue();
            System.out.println(t.getName());
            if (columnLan.equals(abbrev)) {

                XSSFCell cellBlank = row.createCell(2);
                sourceCells.add(cellBlank);
            }

        }

        ArrayList<XSSFCell> targetCells = new ArrayList<>();

        for (int j = 0; j < workSheet.getPhysicalNumberOfRows(); j++) {
//System.out.println();
            XSSFRow row = workSheet.getRow(j);
//            System.out.println(j);
            XSSFCell transCell = row.getCell(4);
// check here here here here
            targetCells.add(transCell);

        }

        for (int m = 0; m < targetCells.size(); m++) {
            System.out.println(m);
            System.out.println(t.getName());
            XSSFCell tCell = targetCells.get(m);
//            System.out.println(m);
//            System.out.println(t.getPath());
            String value = tCell.getStringCellValue();

//            System.out.println("--------------------------------------------------------------------------" + value);

            sourceCells.get(m).setCellValue(value);


        }

    }

    private int findMaxRowIndex(XSSFSheet compilePage) {

        int physicalNum = compilePage.getPhysicalNumberOfRows();

        int n = 0;

        for (int i = 0; i < physicalNum; i++) {
            XSSFRow row = compilePage.getRow(i);

            if (row == null || row.getCell(0) == null || (row.getCell(0).getCellType() == CellType.STRING && row.getCell(0).getStringCellValue().equals(""))) {
                return i - 1;
            }

            n = i;

        }

        return n;
    }

    public String parseLanguage(String name) {

        return name.substring(name.lastIndexOf("_") + 1, name.lastIndexOf("."));


    }

    public void fix(HashMap<File, XSSFWorkbook> map) {
        Set<File> files = map.keySet();

        for (File f : files) {

            XSSFWorkbook book = map.get(f);

            this.fix(book);

        }
/*I just couldn't help but wonder that after everything she's done to me - the unappreciative way*/
    }

    private void fix(XSSFWorkbook book) {
        XSSFSheet sheet = book.getSheet(this.forImport);

        int max = this.findMaxRowIndex(sheet);

        for (int i = 0; i < max; i++) {
//            System.out.println(i);
            String lanValue = sheet.getRow(i).getCell(1).getStringCellValue();

            if (lanValue.equals("CS")) {

                sheet.getRow(i).createCell(2).setCellValue(sheet.getRow(i + 1).getCell(2).getStringCellValue());

            }
            if (lanValue.equals("PO")) {
                sheet.getRow(i).createCell(2).setCellValue(sheet.getRow(i - 1).getCell(2).getStringCellValue());
            }

//
        }
    }

    public AdvancedCompiler() {
    }

    public static void main(String[] args) throws IOException, InvalidFormatException {
        AdvancedCompiler compiler = new AdvancedCompiler(
                "C:\\Users\\trunk\\OneDrive\\desktop\\DEMO\\Automation\\Project1_non-typical-excel-demo1-automation\\SRC",
                "C:\\Users\\trunk\\OneDrive\\desktop\\DEMO\\Automation\\Project1_non-typical-excel-demo1-automation\\fromQA");
        HashMap map = compiler.compile();
        compiler.fix(map);
        compiler.writeDocument(map);
//compiler.writeDocument;

        //
    }
}
