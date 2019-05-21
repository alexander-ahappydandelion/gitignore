package main;

import filters.ExcludeFileFilter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

import java.io.*;
import java.net.URISyntaxException;
import java.util.*;

public class Main {
    private static String IGNORE_FILE_PATH;
    private static String ROOT_DIRECTORY;

    private static String COMMENT_START = "#";
    private static String EMPTY_LINE = "";

    private static String DIRECTORY_END = "/";

    static {
        try {
            ROOT_DIRECTORY = new File(Main.class.getProtectionDomain().getCodeSource().getLocation()
                    .toURI()).getParent() + FilenameUtils.separatorsToSystem("/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        IGNORE_FILE_PATH = ROOT_DIRECTORY +
                FilenameUtils.separatorsToSystem("ignore");
    }

    public static void main(String[] args) throws IOException {
        List<String> lines = getLinesOfIgnoreFile();

        Set<String> excludedFiles = getExcludedFiles(lines);
        Set<String> excludedDirectories = getExcludedDirectories(lines);

        File directory = new File(ROOT_DIRECTORY);
        IOFileFilter fileFilter = new ExcludeFileFilter(excludedFiles);
        IOFileFilter dirFilter = new ExcludeFileFilter(excludedDirectories);

        Collection<File> files = FileUtils.listFiles(directory, fileFilter, dirFilter);

        for (File file : files) {
            System.out.println(file.getPath().substring(2));
        }
    }

    private static Set<String> getExcludedFiles(List<String> lines) {
        Set<String> excludedFiles = new HashSet<>();

        for (String line : lines) {
            if (line.equals(EMPTY_LINE) || line.startsWith(COMMENT_START)) {
                continue;
            }

            if (!line.endsWith("/")) {
                String path = ROOT_DIRECTORY + line;
                excludedFiles.add(FilenameUtils.separatorsToSystem(path));
            }
        }

        return excludedFiles;
    }

    private static Set<String> getExcludedDirectories(List<String> lines) {
        Set<String> excludedDirectories = new HashSet<>();

        for (String line : lines) {
            if (line.equals(EMPTY_LINE) || line.startsWith(COMMENT_START)) {
                continue;
            }

            if (line.endsWith("/")) {
                String path = ROOT_DIRECTORY + line.substring(0, line.length() - 1);
                excludedDirectories.add(FilenameUtils.separatorsToSystem(path));
            }
        }

        return excludedDirectories;
    }

    private static List<String> getLinesOfIgnoreFile() throws IOException {
        File file = new File(IGNORE_FILE_PATH);
        BufferedReader reader = new BufferedReader(new FileReader(file));

        List<String> lines = new LinkedList<>();
        String line;

        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }

        return lines;
    }
}
