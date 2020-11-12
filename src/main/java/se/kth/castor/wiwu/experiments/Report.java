package se.kth.castor.wiwu.experiments;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Report {

    private File reportFile;

    private Set<String> usedClasses = new HashSet<>();
    private Set<String> bloatedClasses = new HashSet<>();
    private Set<String> usedMethods = new HashSet<>();
    private Set<String> bloatedMethods = new HashSet<>();

    public Report(File reportFile) {
        this.reportFile = reportFile;
    }

    public void processReport() {
        List<String> lines = getLines();
        for (String line : lines) {
            String[] split = line.split(",");
            if (split[0].equals("UsedClass")) {
                usedClasses.add(split[1]);
            } else if (split[0].equals("BloatedClass")) {
                bloatedClasses.add(split[1]);
            } else if (split[0].equals("UsedMethod")) {
                usedMethods.add(split[1]);
            } else if (split[0].equals("BloatedMethod")) {
                bloatedMethods.add(split[1]);
            }
        }
    }

    private List<String> getLines() {
        List<String> allLines = new ArrayList<>();
        try {
            allLines = Files.readAllLines(Paths.get(reportFile.getAbsolutePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allLines;
    }

    public Set<String> getUsedClasses() {
        return usedClasses;
    }

    public Set<String> getBloatedClasses() {
        return bloatedClasses;
    }

    public Set<String> getUsedMethods() {
        return usedMethods;
    }

    public Set<String> getBloatedMethods() {
        return bloatedMethods;
    }
}
