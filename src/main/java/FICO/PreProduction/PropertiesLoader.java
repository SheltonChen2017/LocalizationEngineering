package FICO.PreProduction;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public interface PropertiesLoader {
     ArrayList<File> loadVariant(String folder, String lan, ArrayList<File> lanList);
     void sortFiles(ArrayList<File> list, HashMap<String, File> map, String regex);
     ArrayList<File> loadPropertiesFilesFromFolder(String path, ArrayList<File> resultList);
}
