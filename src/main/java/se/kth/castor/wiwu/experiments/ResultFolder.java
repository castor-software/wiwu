package se.kth.castor.wiwu.experiments;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Path;

public class ResultFolder {

    private Path path;

    public ResultFolder(Path path) {
        this.path = path;
    }

    public String[] listSubFolders() {
        File file = path.toFile();
        String[] directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        return directories;
    }
}
