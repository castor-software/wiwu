package se.kth.castor.wiwu;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This class represents a command executed in the terminal.
 */
@Slf4j
public class Cmd {

    private final File path;

    public Cmd(File path) {
        this.path = path;
    }

    /**
     * Executes DepClean and return a hash map with the results of the analysis.
     *
     * @return A map of [dependency-status] -> [dependencies]
     */
    public Map<String, Set<String>> depCleanResult() {
        Map<String, Set<String>> result = new HashMap<>();
        String[] str = new String[]{
                "mvn",
                "se.kth.castor:depclean-maven-plugin:1.1.0:depclean"
        };
        result.put("UsedDirect", new HashSet<>());
        result.put("UsedTransitive", new HashSet<>());
        result.put("BloatedDirect", new HashSet<>());
        result.put("BloatedTransitive", new HashSet<>());
        try {
            String line;
            Process p = Runtime.getRuntime().exec(str, null, path);
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            boolean[] dependencyUsageType = new boolean[]{false, false, false, false};
            while ((line = input.readLine()) != null) {
                if (line.startsWith("Used direct dependencies")) {
                    dependencyUsageType[0] = true;
                    dependencyUsageType[1] = false;
                    dependencyUsageType[2] = false;
                    dependencyUsageType[3] = false;
                }
                if (line.startsWith("Used transitive dependencies")) {
                    dependencyUsageType[0] = false;
                    dependencyUsageType[1] = true;
                    dependencyUsageType[2] = false;
                    dependencyUsageType[3] = false;
                } else if (line.startsWith("Potentially unused direct dependencies")) {
                    dependencyUsageType[0] = false;
                    dependencyUsageType[1] = false;
                    dependencyUsageType[2] = true;
                    dependencyUsageType[3] = false;
                } else if (line.startsWith("Potentially unused transitive dependencies")) {
                    dependencyUsageType[0] = false;
                    dependencyUsageType[1] = false;
                    dependencyUsageType[2] = false;
                    dependencyUsageType[3] = true;
                }
                if (line.startsWith("\t")) {
                    if (dependencyUsageType[0]) {
                        result.get("UsedDirect").add(line.split("\t")[1]);
                    } else if (dependencyUsageType[1]) {
                        result.get("UsedTransitive").add(line.split("\t")[1]);
                    } else if (dependencyUsageType[2]) {
                        result.get("BloatedDirect").add(line.split("\t")[1]);
                    } else if (dependencyUsageType[3]) {
                        result.get("BloatedTransitive").add(line.split("\t")[1]);
                    }
                }
            }
            input.close();
        } catch (Exception e) {
            log.error("Failed to run: " + e);
        }
        return result;
    }

    /**
     * Write the dependency tree of a Maven project to a file.
     *
     * @param outputFile A file with the dependency tree.
     * @return True if the dependency tree was obtained, false otherwise.
     */
    public boolean dependencyTree(File outputFile) {
        String[] str = new String[]{
                "mvn",
                "dependency:tree",
                "-DoutputFile=" + outputFile.getAbsolutePath(),
                "-Dverbose=true"
        };
        try {
            String line;
            Process p = Runtime.getRuntime().exec(str, null, path);
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                log.info(line);
            }
            input.close();
        } catch (Exception e) {
            log.error("Failed to run: " + e);
            return false;
        }
        return true;
    }

    /**
     * Execute the maven compile.
     */
    public boolean compileProject() {
        String[] str = new String[]{
                "mvn",
                "test"
        };
        try {
            String line;
            Process p = Runtime.getRuntime().exec(str, null, path);
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                log.info(line);
            }
            input.close();
        } catch (Exception e) {
            log.error("Failed to run: " + e);
            return false;
        }
        return true;
    }
}
