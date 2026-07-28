package FICO.PreProduction;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FileRemover {
    private final Path root;
    private final List<Path> files = new ArrayList<>();

    public FileRemover(String folder) {
        root = Paths.get(folder).toAbsolutePath().normalize();
        validateRoot(root);
        try {
            Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    if (attrs.isRegularFile() && !attrs.isSymbolicLink()) {
                        files.add(file);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not enumerate files under " + root, e);
        }
    }

    /** Safe default: only prints the files that would be removed. */
    public void produce() throws IOException {
        produce(false);
    }

    public void produce(boolean confirmedDelete) throws IOException {
        for (Path file : files) {
            Path normalized = file.toAbsolutePath().normalize();
            if (!normalized.startsWith(root)) {
                throw new IOException("Refusing path outside configured root: " + normalized);
            }
            if (!file.getFileName().toString().toUpperCase(Locale.ROOT).contains("FR_FR")) {
                if (confirmedDelete) {
                    if (!Files.deleteIfExists(file)) {
                        throw new IOException("File disappeared before deletion: " + file);
                    }
                    System.out.println("Deleted: " + file);
                } else {
                    System.out.println("Would delete: " + file);
                }
            }
        }
    }

    private static void validateRoot(Path root) {
        if (!Files.isDirectory(root)) {
            throw new IllegalArgumentException("Folder does not exist or is not a directory: " + root);
        }
        if (root.getParent() == null || root.getNameCount() < 2) {
            throw new IllegalArgumentException("Refusing broad filesystem root: " + root);
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 1 || args.length > 2 || (args.length == 2 && !"--delete".equals(args[1]))) {
            throw new IllegalArgumentException("Usage: FileRemover <folder> [--delete]");
        }
        new FileRemover(args[0]).produce(args.length == 2);
    }
}
