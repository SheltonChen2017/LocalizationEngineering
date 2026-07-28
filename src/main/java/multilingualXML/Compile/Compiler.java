package multilingualXML.Compile;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Compiler {
    private final String finalPath;
    private final Document sourceDoc;
    private final List<File> translatedList;
    private final String nameRegex = "_(.*?)\\.xliff$";

    public Compiler(String sourcePath, String translationPath, String finalPath) throws DocumentException {
        this.finalPath = finalPath;
        File[] sourceFiles = new File(sourcePath).listFiles(File::isFile);
        File[] translationFiles = new File(translationPath).listFiles(File::isFile);
        if (sourceFiles == null || sourceFiles.length != 1) {
            throw new IllegalArgumentException("Source folder must contain exactly one source XML file: " + sourcePath);
        }
        if (translationFiles == null) {
            throw new IllegalArgumentException("Translation folder is not readable: " + translationPath);
        }
        this.sourceDoc = newSecureReader().read(sourceFiles[0]);
        this.translatedList = Arrays.asList(translationFiles);
    }

    public void produce() throws DocumentException, IOException {
        for (File file : translatedList) {
            Document translationDoc = newSecureReader().read(file);
            applyTranslation(sourceDoc, translationDoc, file.getName());
        }
        writeDoc(sourceDoc, finalPath);
    }

    private void applyTranslation(Document source, Document translation, String filename) {
        String language = parseName(filename);
        Element xliffFile = translation.getRootElement().element("file");
        Element body = xliffFile == null ? null : xliffFile.element("body");
        if (body == null) {
            throw new IllegalArgumentException("XLIFF has no file/body: " + filename);
        }

        Map<String, String> targetsById = new LinkedHashMap<>();
        for (Object item : body.elements("trans-unit")) {
            Element unit = (Element) item;
            String id = unit.attributeValue("id");
            Element target = unit.element("target");
            if (id == null || target == null) {
                throw new IllegalArgumentException("Each trans-unit must have an id and target: " + filename);
            }
            if (targetsById.put(id, target.getText()) != null) {
                throw new IllegalArgumentException("Duplicate trans-unit id '" + id + "' in " + filename);
            }
        }

        Set<String> applied = new HashSet<>();
        for (Object item : source.getRootElement().elements()) {
            Element sourceUnit = (Element) item;
            String id = sourceUnit.getName();
            if (!targetsById.containsKey(id)) {
                continue;
            }
            Element languageElement = sourceUnit.element(language);
            if (languageElement != null) {
                languageElement.setText(targetsById.get(id));
                applied.add(id);
            }
        }

        Set<String> unmatched = new LinkedHashSet<>(targetsById.keySet());
        unmatched.removeAll(applied);
        if (!unmatched.isEmpty()) {
            throw new IllegalArgumentException("No matching source/language element for IDs " + unmatched + " in " + filename);
        }
    }

    private void writeDoc(Document source, String outputPath) throws IOException {
        File outputDirectory = new File(outputPath);
        if (!outputDirectory.isDirectory() && !outputDirectory.mkdirs()) {
            throw new IOException("Could not create output directory: " + outputDirectory);
        }
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");
        try (FileOutputStream output = new FileOutputStream(new File(outputDirectory, "final.xml"))) {
            XMLWriter writer = new XMLWriter(output, format);
            writer.write(source);
            writer.flush();
        }
    }

    private String parseName(String filename) {
        Matcher matcher = Pattern.compile(nameRegex).matcher(filename);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Translation filename does not match '<name>_<language>.xliff': " + filename);
        }
        return matcher.group(1);
    }

    private static SAXReader newSecureReader() throws DocumentException {
        SAXReader reader = new SAXReader();
        try {
            reader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            reader.setFeature("http://xml.org/sax/features/external-general-entities", false);
            reader.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        } catch (SAXException e) {
            throw new DocumentException("Unable to configure secure XML parser", e);
        }
        return reader;
    }

    public static void main(String[] args) throws DocumentException, IOException {
        if (args.length != 3) {
            throw new IllegalArgumentException("Usage: Compiler <source-folder> <translation-folder> <output-folder>");
        }
        new Compiler(args[0], args[1], args[2]).produce();
    }
}
