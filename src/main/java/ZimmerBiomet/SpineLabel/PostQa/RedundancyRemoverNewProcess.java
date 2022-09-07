package ZimmerBiomet.SpineLabel.PostQa;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RedundancyRemoverNewProcess {

    String srcFolder;
    String trFolder;
    String tgtFolder;

    List<File> srcFiles = new ArrayList<>();
    List<File> trFiles = new ArrayList<>();


    public RedundancyRemoverNewProcess(String srcFolder, String trFolder, String tgtFolder) {
        this.srcFolder = srcFolder;
        this.trFolder = trFolder;
        this.tgtFolder = tgtFolder;

        this.srcFiles = Arrays.asList(new File(srcFolder).listFiles());

        this.trFiles = Arrays.asList(new File(trFolder).listFiles());

    }

    public void produce() throws IOException, InvalidFormatException {

        for (int i = 0; i < this.srcFiles.size(); i++) {

            this.produce(this.srcFiles.get(i), this.trFiles.get(i));

        }


    }

    private void produce(File src, File tr) throws IOException, InvalidFormatException {

        XSSFWorkbook srcBook = new XSSFWorkbook(src);

        XSSFWorkbook trBook = new XSSFWorkbook(tr);

        XSSFSheet srcSheet = srcBook.getSheetAt(0);

        XSSFSheet trSheet = trBook.getSheetAt(0);

        for (int i = 0; i < srcSheet.getPhysicalNumberOfRows(); i++) {

            XSSFRow rowS = srcSheet.getRow(i);
            XSSFRow rowT = trSheet.getRow(i);
            if (rowS != null) {


                for (int j = 0; j < rowS.getPhysicalNumberOfCells(); j++) {

                    XSSFCell cellS = rowS.getCell(j);
                    XSSFCell cellT = rowT.getCell(j);

//                    System.out.println(cellS);
//                    System.out.println(cellT);

                    if (cellS == null || cellS.getStringCellValue().trim().equals("")) {
//                        System.out.println(cellS.getStringCellValue());
                        if(cellT!=null) {
                            System.out.println("row is "+i+" while j is "+j);
                            cellT.setCellValue("");
                        }
                    }
                }
            }
        }

        File file = new File(tgtFolder + "\\" + "removed.xlsx");

        trBook.write(new FileOutputStream(file));

//        XSSFCell cell = srcSheet.getRow(3).getCell(25);
//        System.out.println(cell);

    }

    public static void main(String[] args) throws IOException, InvalidFormatException {
        new RedundancyRemoverNewProcess("C:\\Users\\trunk\\OneDrive\\桌面\\workaholic\\2003126\\FINALcompile\\removeRedundancy\\master",
                "C:\\Users\\trunk\\OneDrive\\桌面\\workaholic\\2003126\\FINALcompile\\removeRedundancy\\translation",
                "C:\\Users\\trunk\\OneDrive\\桌面\\workaholic\\2003126\\FINALcompile\\removeRedundancy\\result").produce();
    }

}
