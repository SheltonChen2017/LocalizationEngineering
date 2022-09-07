package ZimmerBiomet.WarsawLabel.util.TranslationMemory;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TMCombiner {

    String folder;

    public static final String identifier = "(_)(.*?)(\\.)(xlsx)";

    List<File> list;

    public TMCombiner(String folder) {
        this.folder = folder;
        this.list = Arrays.asList(new File(folder).listFiles());
    }

    public ArrayList<ArrayList<File>> groupFiles(List<File> files) {

        ArrayList<ArrayList<File>> groupedFiles = new ArrayList<>();

        for (int i = 1; i < files.size(); i++) {

            if (this.extractLanguage(files.get(i)).equals(this.extractLanguage(files.get(i - 1)))) {
                ArrayList<File> subGroup = new ArrayList<>();

                subGroup.add(files.get(i));
                subGroup.add(files.get(i - 1));

                groupedFiles.add(subGroup);

            }

        }


        return groupedFiles;

    }

    private String extractLanguage(File file) {

        String fileName = file.getName();

        Pattern p = Pattern.compile(this.identifier);

        Matcher matcher = p.matcher(fileName);

        matcher.find();

        String lan = matcher.group(2);

        return lan;
    }

    public void combine(ArrayList<ArrayList<File>> files) throws DocumentException {

        for (ArrayList<File> list : files) {

            this.combineTmx(list);
        }

    }

    private void combineTmx(ArrayList<File> list) throws DocumentException {

        File position0 = list.get(0);

        SAXReader reader = new SAXReader();

        Document doc = reader.read(position0);


        if (list.size() <= 1) {
            this.writeDoc(doc);
        } else {

            for (int i = 1; i < list.size(); i++) {

                this.combineDoc(doc, list.get(i));

            }

            this.writeDoc(doc);

        }

    }

    private void writeDoc(Document doc) {

    }

    private void combineDoc(Document master, File file) throws DocumentException {

        SAXReader r = new SAXReader();

        Document doc = r.read(file);

        Element root = master.getRootElement();

        Element body = root.element("body");

        List<Element> tus = doc.getRootElement().element("body").elements("tu");

        for (Element tu : tus) {

            body.add(tu);

        }

    }

}
