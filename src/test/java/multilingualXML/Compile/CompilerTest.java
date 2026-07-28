package multilingualXML.Compile;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CompilerTest {
    @TempDir
    Path temp;

    @Test
    void matchesTranslationsByIdWhenXliffIsReordered() throws Exception {
        Path source = Files.createDirectory(temp.resolve("source"));
        Path translated = Files.createDirectory(temp.resolve("translated"));
        Path output = temp.resolve("output");
        write(source.resolve("input.xml"),
                "<root><first><EN>One</EN><FR>old1</FR></first>"
                        + "<second><EN>Two</EN><FR>old2</FR></second></root>");
        write(translated.resolve("input_FR.xliff"),
                "<xliff><file><body>"
                        + "<trans-unit id=\"second\"><target>Deux</target></trans-unit>"
                        + "<trans-unit id=\"first\"><target>Un</target></trans-unit>"
                        + "</body></file></xliff>");

        new Compiler(source.toString(), translated.toString(), output.toString()).produce();

        Document result = new SAXReader().read(output.resolve("final.xml").toFile());
        assertEquals("Un", result.getRootElement().element("first").elementText("FR"));
        assertEquals("Deux", result.getRootElement().element("second").elementText("FR"));
    }

    private static void write(Path path, String value) throws Exception {
        Files.write(path, value.getBytes(StandardCharsets.UTF_8));
    }
}
