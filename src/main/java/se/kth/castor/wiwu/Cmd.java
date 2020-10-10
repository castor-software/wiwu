package se.kth.castor.wiwu;

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
public class Cmd {

    private File path;

    public Cmd(File path) {
        this.path = path;
    }

    public Map<String, Set<String>> execProcess(String[] cmd) {
        Map<String, Set<String>> result = new HashMap<>();
        result.put("UsedDirect", new HashSet<>());
        result.put("UsedTransitive", new HashSet<>());
        result.put("BloatedDirect", new HashSet<>());
        result.put("BloatedTransitive", new HashSet<>());
        try {
            String line;
            Process p = Runtime.getRuntime().exec(cmd, null, path);
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
            System.out.println("Failed to run: " + e);
        }
        return result;
    }
}
