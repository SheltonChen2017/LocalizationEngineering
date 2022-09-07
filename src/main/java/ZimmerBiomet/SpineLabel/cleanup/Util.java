package ZimmerBiomet.SpineLabel.cleanup;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    public static int findMaxNumOfRows(XSSFSheet sheet) {


        return sheet.getLastRowNum();
    }


    public static ArrayList<Integer> findRelevantColumns(XSSFSheet sheet) {
        ArrayList<Integer> list = new ArrayList<>();
        XSSFRow row = sheet.getRow(0);
//    System.out.println(row);
        for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
            if (row.getCell(j).getStringCellValue().toLowerCase().contains("description")) {
                list.add(j);
            }
        }
        return list;
    }


    public static int findLanColumn(XSSFSheet sheet, String lan) {


//        XSSFSheet sheet = sheets.getSheetAt(0);

        XSSFRow row = sheet.getRow(0);

        for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {

            try {
                XSSFCell cell = row.getCell(j);
                System.out.println(lan.toLowerCase());
                System.out.println(cell.getStringCellValue().toLowerCase().contains(lan.toLowerCase()));
//                System.out.println(cell.getStringCellValue().toLowerCase().trim());
//                cell.getStringCellValue().toLowerCase().contains(lan.toLowerCase())
                if (cell.getStringCellValue().toLowerCase().contains(lan.toLowerCase())) {
                    return j;
                }

            } catch (NullPointerException e) {

                System.out.println("nullPointerException at findLanColumn");
//                e.printStackTrace();
                System.out.println("the number of j is " + j);
                continue;

            }

        }

        return 0;
    }


    public static String parseLanguage(String name) {
        Pattern p = Pattern.compile("-([a-zA-Z]+)\\s");
        Matcher matcher = p.matcher(name);

        matcher.find();

        return matcher.group(1).trim();
    }

    public static String replaceDotToPeriodForNumbers(String valueTrans) {

        String regex = "(\\.)(\\d)";

        Pattern p = Pattern.compile(regex);

        Matcher matcher = p.matcher(valueTrans);


        while (matcher.find()) {
            String numString = matcher.group(0);
            valueTrans.replace(numString, "," + matcher.group(2));
        }

        return valueTrans;
    }
}
