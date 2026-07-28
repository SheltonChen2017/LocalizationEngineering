package ADP;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public class JsonQa {
    private final List<File> targetFiles = new ArrayList<>();
    private final File source;

    public JsonQa(String sourceFolder, String targetFolder) throws IOException {
        File[] sources = new File(sourceFolder).listFiles(
                file -> file.isFile() && file.getName().toLowerCase(Locale.ROOT).endsWith(".json"));
        if (sources == null || sources.length != 1) {
            throw new IllegalArgumentException("Source folder must contain exactly one JSON file: " + sourceFolder);
        }
        source = sources[0];
        collectJsonFiles(new File(targetFolder), targetFiles);
        targetFiles.sort(Comparator.comparing(File::getPath, String.CASE_INSENSITIVE_ORDER));
    }

    public void produce() throws IOException {
        JSONObject sourceObject = new JSONObject(FileUtils.readFileToString(source, StandardCharsets.UTF_8));
        for (File target : targetFiles) {
            produce(target, sourceObject);
        }
    }

    private void produce(File target, JSONObject sourceObject) throws IOException {
        JSONObject targetObject = new JSONObject(FileUtils.readFileToString(target, StandardCharsets.UTF_8));
        StringBuilder report = new StringBuilder("key\tsource\ttarget\tstatus\r\n");

        Set<String> allKeys = new TreeSet<>();
        allKeys.addAll(sourceObject.keySet());
        allKeys.addAll(targetObject.keySet());
        for (String key : allKeys) {
            boolean hasSource = sourceObject.has(key) && !sourceObject.isNull(key);
            boolean hasTarget = targetObject.has(key) && !targetObject.isNull(key);
            Object sourceValue = hasSource ? sourceObject.get(key) : "";
            Object targetValue = hasTarget ? targetObject.get(key) : "";
            String status = !hasSource ? "EXTRA_TARGET_KEY"
                    : !hasTarget ? "MISSING_TARGET_KEY"
                    : sourceValue instanceof String && targetValue instanceof String ? "OK"
                    : "NON_STRING_VALUE";
            report.append(tsv(key)).append('\t').append(tsv(sourceValue)).append('\t')
                    .append(tsv(targetValue)).append('\t').append(status).append("\r\n");
        }

        String basename = target.getName().replaceFirst("(?i)\\.json$", "");
        File output = new File(target.getParentFile(), basename + "_json_qa.tsv");
        Files.write(output.toPath(), report.toString().getBytes(StandardCharsets.UTF_8));
    }

    private static void collectJsonFiles(File folder, List<File> output) throws IOException {
        File[] children = folder.listFiles();
        if (children == null) {
            throw new IOException("Target folder does not exist or is not readable: " + folder);
        }
        for (File child : children) {
            if (child.isDirectory()) {
                collectJsonFiles(child, output);
            } else if (child.getName().toLowerCase(Locale.ROOT).endsWith(".json")) {
                output.add(child);
            }
        }
    }

    private static String tsv(Object value) {
        return String.valueOf(value).replace("\t", "\\t")
                .replace("\r", "\\r").replace("\n", "\\n");
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            throw new IllegalArgumentException("Usage: JsonQa <source-folder> <target-folder>");
        }
        new JsonQa(args[0], args[1]).produce();
    }
}
