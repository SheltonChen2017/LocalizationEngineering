package ADP;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class JsonQaTest {
    @TempDir
    Path temp;

    @Test
    void reportsMissingExtraAndNonStringValuesWithoutOverwritingReports() throws Exception {
        Path source = Files.createDirectory(temp.resolve("source"));
        Path targets = Files.createDirectory(temp.resolve("targets"));
        Path locale = Files.createDirectory(targets.resolve("fr-FR"));
        write(source.resolve("source.json"), "{\"hello\":\"Hello\",\"count\":1,\"missing\":\"value\"}");
        write(locale.resolve("first.json"), "{\"hello\":\"Bonjour\",\"count\":2,\"extra\":\"x\"}");
        write(locale.resolve("second.json"), "{\"hello\":\"Salut\"}");

        new JsonQa(source.toString(), targets.toString()).produce();

        String first = read(locale.resolve("first_json_qa.tsv"));
        assertTrue(first.contains("MISSING_TARGET_KEY"));
        assertTrue(first.contains("EXTRA_TARGET_KEY"));
        assertTrue(first.contains("NON_STRING_VALUE"));
        assertTrue(Files.exists(locale.resolve("second_json_qa.tsv")));
    }

    private static void write(Path path, String value) throws Exception {
        Files.write(path, value.getBytes(StandardCharsets.UTF_8));
    }

    private static String read(Path path) throws Exception {
        return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
    }
}
