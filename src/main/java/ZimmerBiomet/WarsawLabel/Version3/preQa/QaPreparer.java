package ZimmerBiomet.WarsawLabel.Version3.preQa;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.text.NumberFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QaPreparer {

    String translationFolderPath;
    String qaDocumentPath;

    final static ResourceBundle bundle = ResourceBundle.getBundle("LANGUAGE");

    File[] transFolders;


    //Temp:
    String masterPath;

    public QaPreparer(String translationFolderPath, String qaDocumentPath, String masterPath) {
        this.translationFolderPath = translationFolderPath;
        this.qaDocumentPath = qaDocumentPath;
        this.masterPath = masterPath;
        this.transFolders = new File(translationFolderPath).listFiles();
        if (this.transFolders == null) {
            throw new IllegalArgumentException("Translation folder does not exist or is not readable: " + translationFolderPath);
        }

//        System.out.println(this.transFolders[1].getPath());
    }

    public static void produce() throws IOException, InvalidFormatException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//        System.out.println("Give me the full path where the WS-downloaded translations are saved");
        String translations = br.readLine();
//        System.out.println("Give me the full path where the master spread sheets are stored");
        String master = br.readLine();
//        System.out.println("Give me the full path where you want me to put all the QA documents");
        String qaPath = br.readLine();

        new QaPreparer(translations, qaPath, master).generate();

    }

    public String parseLanguage(String foldername) {

        Pattern p = Pattern.compile("-[a-zA-Z]+ ");

        Matcher matcher = p.matcher(foldername);

        if (matcher.find()) {
            return matcher.group(0).trim().substring(1);
        }

        return null;
    }


    public void generate() throws IOException, InvalidFormatException {

        //Identify English File:

        String enFolder = null;

        ArrayList<File> files = new ArrayList<>();

        for (File folder : this.transFolders) {

            String lan = this.parseLanguage(folder.getPath());
//            System.out.println(folder.getPath());
//            System.out.println(lan + "----------");

            if (lan == null) {
                throw new IllegalArgumentException("Could not determine language from folder name: " + folder.getName());
            }
            if (lan.equalsIgnoreCase("English")) {
                enFolder = folder.getAbsolutePath();

            } else {

                files.add(folder);

            }

        }


        if (enFolder == null) {
            throw new IllegalArgumentException("No English translation folder was found in " + translationFolderPath);
        }
        for (File f : files) {

            this.generateQa(new File(enFolder), f);


        }


    }

    private void generateQa(File sourceFolder, File transFolder) throws IOException, InvalidFormatException {
        Map<String, File> sources = filesByName(sourceFolder);
        Map<String, File> translations = filesByName(transFolder);
        Set<String> missingTranslations = new TreeSet<>(sources.keySet());
        missingTranslations.removeAll(translations.keySet());
        Set<String> unexpectedTranslations = new TreeSet<>(translations.keySet());
        unexpectedTranslations.removeAll(sources.keySet());
        if (!missingTranslations.isEmpty() || !unexpectedTranslations.isEmpty()) {
            throw new IOException("File mismatch for " + transFolder.getName()
                    + "; missing translations=" + missingTranslations
                    + ", unexpected translations=" + unexpectedTranslations);
        }
        for (Map.Entry<String, File> entry : sources.entrySet()) {
            this.doGenerate(entry.getValue(), translations.get(entry.getKey()));
        }


    }

    private void doGenerate(File source, File trans) throws IOException, InvalidFormatException {

        try (XSSFWorkbook sWorkbook = new XSSFWorkbook(source);
             XSSFWorkbook tWorkbook = new XSSFWorkbook(trans);
             XSSFWorkbook qaWorkbook = new XSSFWorkbook()) {

        XSSFSheet workSheet = qaWorkbook.createSheet();

        XSSFSheet sSheet = sWorkbook.getSheetAt(0);

        XSSFSheet tSheet = tWorkbook.getSheetAt(0);
        if (sSheet.getLastRowNum() != tSheet.getLastRowNum()) {
            throw new IOException("Row-count mismatch between " + source + " and " + trans);
        }


        for (int i = 0; i < sSheet.getPhysicalNumberOfRows(); i++) {
//            System.out.println("source number line is" + sSheet.getPhysicalNumberOfRows());
//            System.out.println("target number line is " + tSheet.getPhysicalNumberOfRows());
//            System.out.println(i);
//            System.out.println(trans.getPath());
//            System.out.println(source.getPath());
            XSSFRow row = sSheet.getRow(i);
//            System.out.println(row);
            if (row == null || tSheet.getRow(i) == null || row.getCell(0) == null
                    || tSheet.getRow(i).getCell(0) == null) {
                throw new IOException("Missing source/target cell at row " + (i + 1)
                        + " in " + source.getName() + " / " + trans.getName());
            }
            XSSFCell cell = row.getCell(0);
//            XSSFFont srcFont = cell.getCellStyle().getFont();
//            XSSFCellStyle newStyle = qaWorkbook.createCellStyle();
//            newStyle.setFont(srcFont);
            System.out.println(i);
            String stringCellValue = cell.getStringCellValue();

            workSheet.createRow(i).createCell(0).setCellValue(stringCellValue);

//            workSheet.getRow(i).getCell(0).setCellStyle(newStyle);

            workSheet.getRow(i).createCell(4).setCellValue(tSheet.getRow(i).getCell(0).getStringCellValue());

//            workSheet.getRow(i).getCell(4).setCellStyle(newStyle);

            workSheet.getRow(i).createCell(6).setCellValue(tSheet.getRow(i).getCell(0).getStringCellValue().length());
            String stringCellValue1 = workSheet.getRow(i).getCell(4).getStringCellValue();
//            System.out.println(stringCellValue1 + "------------------");
        }


        String lan = this.parseLanguage(trans.getParent());
            this.lengthCheck(qaWorkbook, source, lan);
        }


    }


    private void lengthCheck(XSSFWorkbook qaWorkbook, File source, String lan) throws IOException, InvalidFormatException {

        File folder = new File(this.qaDocumentPath + "\\" + "lengthCheck");

        if (!folder.isDirectory() && !folder.mkdirs()) {
            throw new IOException("Could not create QA output folder: " + folder);
        }

        File subFolder = new File(this.qaDocumentPath + "\\" + "lengthCheck" + "\\" + lan);

        if (!subFolder.isDirectory() && !subFolder.mkdirs()) {
            throw new IOException("Could not create language output folder: " + subFolder);
        }
        File fileExcel = new File(subFolder.getPath() + "\\" + source.getName().substring(0, source.getName().lastIndexOf(".")) + "_" + lan + ".xlsx");
        this.addInformation(qaWorkbook, source.getName(), lan, this.masterPath);
        try (FileOutputStream fos = new FileOutputStream(fileExcel)) {
            qaWorkbook.write(fos);
        }

        this.generateUnicodeText(qaWorkbook, source, lan);


    }

    private void addInformation(XSSFWorkbook qaWorkbook, String filename, String lan, String masterPath) throws IOException, InvalidFormatException {
        File[] masters = new File(masterPath).listFiles(File::isFile);
        if (masters == null) {
            throw new IOException("Master folder does not exist or is not readable: " + masterPath);
        }

        for (File master : masters) {

            this.doAddInformation(master, qaWorkbook, filename, lan);

        }


    }

    private void doAddInformation(File master, XSSFWorkbook qaWorkbook, String filename, String lan) throws IOException, InvalidFormatException {
        String masterPrefix = master.getName().substring(0, master.getName().lastIndexOf("."));
        String targetPrefix = filename.substring(0, filename.lastIndexOf("."));

        String abbrev = this.bundle.getString(lan);

        try (XSSFWorkbook masterBook = new XSSFWorkbook(master)) {
        XSSFSheet masterSheet = masterBook.getSheetAt(0);

        XSSFSheet workSheet = qaWorkbook.getSheetAt(0);

        if (masterPrefix.equals(targetPrefix)) {

//            System.out.println(masterPrefix);
//            System.out.println(targetPrefix);

            ArrayList<Cell> cells = new ArrayList<>();

            int n = this.findMaxRowNum(masterSheet);

            for (int i = 1; i < n - 1; i++) {
//                System.out.println(i);
//                System.out.println("masterSheet in total has " + n + " rows.");
                XSSFRow row = masterSheet.getRow(i);
                XSSFCell cell = row.getCell(1);
                if (cell.getStringCellValue().equals(abbrev)) {
                    cells.add(row.getCell(0));
                }

            }

            if (cells.size() != workSheet.getPhysicalNumberOfRows()) {
                throw new IOException("Master/QA row mismatch for " + filename + " and language " + lan
                        + ": master=" + cells.size() + ", QA=" + workSheet.getPhysicalNumberOfRows());
            }
            for (int j = 0; j < workSheet.getPhysicalNumberOfRows(); j++) {
//                System.out.println(workSheet.getPhysicalNumberOfRows() + " is the number");
//                System.out.println("Size of the cells is " + cells.size() + " and numbers of rows is " + workSheet.getPhysicalNumberOfRows());
//                System.out.println(filename + "           " + j);
                Cell cell = cells.get(j);
//                System.out.println(cell);
//                System.out.println(cell.getRowIndex());
                CellType cellType = cell.getCellType();
//                System.out.println(lan);
//                System.out.println(cellType);

                if (cellType == CellType.NUMERIC) {

                    double value = cells.get(j).getNumericCellValue();

                    NumberFormat instance = NumberFormat.getInstance();

                    instance.setGroupingUsed(false);

                    String stringValue = instance.format(value);

                    workSheet.getRow(j).createCell(8).setCellValue(stringValue);
                } else {
                    workSheet.getRow(j).createCell(8).setCellValue(cells.get(j).getStringCellValue());
                }


//

            }


        }
        }

    }

    private int findMaxRowNum(XSSFSheet masterSheet) {
        int m = 1;
        for (int i = 0; i < masterSheet.getPhysicalNumberOfRows(); i++) {
            XSSFRow row = masterSheet.getRow(i);

            if (row != null) {
                XSSFCell cell = row.getCell(0);
                if (cell != null) {

                    if (cell.getCellType() == CellType.NUMERIC) {
                        m++;
                    } else if (!cell.getStringCellValue().isEmpty()) {
                        m++;
                    }

                }
            }

        }

        return m;
    }

    private void generateUnicodeText(XSSFWorkbook qaWorkbook, File source, String lan) throws IOException {

        File folder = new File(this.qaDocumentPath + "\\" + "unicode");

        if (!folder.isDirectory() && !folder.mkdirs()) {
            throw new IOException("Could not create Unicode output folder: " + folder);
        }
        XSSFSheet sheet = qaWorkbook.getSheetAt(0);

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {


            sb.append(sheet.getRow(i).getCell(0).getStringCellValue()).append("\t").append(sheet.getRow(i).getCell(4).getStringCellValue()).append("\r\n");

        }

        File unicode = new File(folder.getPath() + "\\" + source.getName().substring(0, source.getName().lastIndexOf(".")) + "_" + lan + ".txt");

        try (Writer writer = new OutputStreamWriter(new FileOutputStream(unicode), java.nio.charset.StandardCharsets.UTF_8)) {
            writer.write(sb.toString());
        }

    }

    private void sort(List<File> translations) {

        Collections.sort(translations);

    }

    private static Map<String, File> filesByName(File folder) throws IOException {
        File[] files = folder.listFiles(File::isFile);
        if (files == null) {
            throw new IOException("Folder does not exist or is not readable: " + folder);
        }
        Map<String, File> byName = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        for (File file : files) {
            if (byName.put(file.getName(), file) != null) {
                throw new IOException("Duplicate filename ignoring case in " + folder + ": " + file.getName());
            }
        }
        return byName;
    }

    //temp


    public static void main(String[] args) throws IOException, InvalidFormatException {
        QaPreparer prepper = new QaPreparer("C:\\Users\\trunk\\OneDrive\\desktop\\DEMO\\Automation\\Project1_non-typical-excel-demo1-automation\\fromCAT",
                "C:\\Users\\trunk\\OneDrive\\desktop\\DEMO\\Automation\\Project1_non-typical-excel-demo1-automation\\FROMQA",
                "C:\\Users\\trunk\\OneDrive\\desktop\\DEMO\\Automation\\Project1_non-typical-excel-demo1-automation\\SRC");

        prepper.generate();

    }

}
//
