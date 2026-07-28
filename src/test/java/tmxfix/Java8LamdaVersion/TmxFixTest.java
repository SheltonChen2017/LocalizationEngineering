package tmxfix.Java8LamdaVersion;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TmxFixTest {
    @TempDir
    Path temp;

    @Test
    void duplicateTrackingDoesNotLeakAcrossFiles() throws Exception {
        writeTmx("one.tmx", "<tu changedate=\"20240101\" changeid=\"a\"><prop type=\"x-ID\">shared</prop></tu>");
        writeTmx("two.tmx", "<tu changedate=\"20240201\" changeid=\"b\"><prop type=\"x-ID\">shared</prop></tu>");

        new TmxFix(temp.toString()).produce();

        Document second = new SAXReader().read(temp.resolve("twofixed.tmx").toFile());
        assertEquals(1, second.selectNodes("//tu").size());
        Document backup = new SAXReader().read(temp.resolve("twobackup.tmx").toFile());
        assertEquals(0, backup.selectNodes("//tu").size());
    }

    @Test
    void supportsTextualIdsWithoutDynamicXpath() throws Exception {
        writeTmx("text.tmx",
                "<tu changedate=\"20230101\" changeid=\"a\"><prop type=\"x-ID\">id-with-text</prop></tu>"
                        + "<tu changedate=\"20240101\" changeid=\"b\"><prop type=\"x-ID\">id-with-text</prop></tu>");

        new TmxFix(temp.toString()).produce();

        Document fixed = new SAXReader().read(temp.resolve("textfixed.tmx").toFile());
        assertEquals(1, fixed.selectNodes("//tu").size());
    }

    private void writeTmx(String name, String units) throws Exception {
        Files.write(temp.resolve(name),
                ("<tmx><body>" + units + "</body></tmx>").getBytes(StandardCharsets.UTF_8));
    }
}
