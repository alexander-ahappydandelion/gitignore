package filters;

import org.apache.commons.io.filefilter.IOFileFilter;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class ExcludeFileFilter implements IOFileFilter {
    private final Set<String> excludedFiles;

    public ExcludeFileFilter(Set<String> excludedFiles) {
        if (excludedFiles == null) {
            excludedFiles = new HashSet<>();
        }

        this.excludedFiles = excludedFiles;
    }

    public boolean accept(File file) {
        return !excludedFiles.contains(file.getPath());
    }

    public boolean accept(File file, String s) {
        return !excludedFiles.contains(file.getPath());
    }
}
