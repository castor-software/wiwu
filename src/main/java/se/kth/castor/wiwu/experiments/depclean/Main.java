package se.kth.castor.wiwu.experiments.depclean;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import se.kth.castor.wiwu.experiments.jdbl.ResultFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class Main {

    public static void main(String[] args) throws IOException {

        String dir = "/Users/cesarsv/Documents/Experiments/history_results/";

        String commit2versionsPath = "/Users/cesarsv/Documents/Experiments/history_results/commit2versions.csv";
        Map<String, Commits2Versions> map = getStringCommits2VersionsMap(commit2versionsPath);

        ResultFolder resultFolder = new ResultFolder(
                new File(dir).toPath()
        );

        String[] projectNames = resultFolder.listSubFolders();

        File results = new File("/Users/cesarsv/Documents/Experiments/history_results/results_depclean.csv");
        FileUtils.writeStringToFile(results,
                "Project,Commit,Dependency,Version,Order,Status\n",
                true
                                   );

        List<Project> projectList = new ArrayList<>();
        for (String projectName : projectNames) {
            resultFolder = new ResultFolder(
                    new File(dir + "/" + projectName).toPath()
            );
            String[] commits = resultFolder.listSubFolders();

            List<Version> versionList = new ArrayList<>();
            // print the commits
            for (String commit : commits) {
                if (Files.exists(new File(dir + "/" + projectName + "/" + commit + "/" + "dependency_tree.json").toPath())) {
                    File reportFile = new File(dir + "/" + projectName + "/" + commit + "/" + "depclean.json");
                    if (Files.exists(reportFile.toPath())) {
                        DepCleanReport depCleanReport = new DepCleanReport(reportFile);
                        depCleanReport.processReport();
                        versionList.add(new Version(commit, depCleanReport.getReport()));

                        FileUtils.writeStringToFile(results,
                                printReport(
                                        projectName,
                                        commit,
                                        depCleanReport.getReport().getUnused_direct_dependencies(),
                                        map.get(commit).getVersion(),
                                        map.get(commit).getOrder(),
                                        "unused_direct"
                                           ),
                                true
                                                   );
                        FileUtils.writeStringToFile(results,
                                printReport(
                                        projectName,
                                        commit,
                                        depCleanReport.getReport().getUnused_transitive_dependencies(),
                                        map.get(commit).getVersion(),
                                        map.get(commit).getOrder(),
                                        "unused_transitive"
                                           ),
                                true
                                                   );
                        FileUtils.writeStringToFile(results,
                                printReport(
                                        projectName,
                                        commit,
                                        depCleanReport.getReport().getUsed_direct_dependencies(),
                                        map.get(commit).getVersion(),
                                        map.get(commit).getOrder(),
                                        "used_direct"
                                           ),
                                true
                                                   );
                        FileUtils.writeStringToFile(results,
                                printReport(
                                        projectName,
                                        commit,
                                        depCleanReport.getReport().getUsed_transitive_dependencies(),
                                        map.get(commit).getVersion(),
                                        map.get(commit).getOrder(),
                                        "used_transitive"
                                           ),
                                true
                                                   );
                    } else {
                        continue;
                    }
                }
            }
            // Add the project
            projectList.add(new Project(projectName, versionList));

        }
        //
        // for (String project : projects) {
        //     File reportFile = new File(dir + "/" + project + "/" + "debloat" + "/" + "debloat-report.csv");
        //     if (Files.exists(reportFile.toPath())) {
        //         JDBLReport report = new JDBLReport(reportFile);
        //         report.processReport();
        //         Version libVersion = new Version(project, report);
        //         libVersions.add(libVersion);
        //     } else {
        //         continue;
        //     }
        // }
        // Lib lib = new Lib(libVersions, dir.split("/")[dir.split("/").length - 1]);
        //
        // System.out.println("AUC" + " (" + lib.alwaysUsedClasses().size() + ")");
        // System.out.println(lib.alwaysUsedClasses());
        // System.out.println("ABC" + " (" + lib.alwaysBloatedClasses().size() + ")");
        // System.out.println(lib.alwaysBloatedClasses());
        //
        // System.out.println("---------------------------------------------------");
        //
        // System.out.println("AUM" + " (" + lib.alwaysUsedMethods().size() + ")");
        // System.out.println(lib.alwaysUsedMethods());
        // System.out.println("ABM" + " (" + lib.alwaysBloatedMethods().size() + ")");
        // System.out.println(lib.alwaysBloatedMethods());
        //
        // System.out.println("---------------------------------------------------");
        //
        // System.out.println("UTABC" + " (" + lib.usedThenAlwaysBloatedClasses().size() + ")");
        // System.out.println(lib.usedThenAlwaysBloatedClasses());
        // System.out.println("UTABM" + " (" + lib.usedThenAlwaysBloatedMethods().size() + ")");
        // System.out.println(lib.usedThenAlwaysBloatedMethods());
        //
        // System.out.println("---------------------------------------------------");
        //
        // System.out.println("BTAUC" + " (" + lib.bloatedThenAlwaysUsedClasses().size() + ")");
        // System.out.println(lib.bloatedThenAlwaysUsedClasses());
        // System.out.println("BTAUM" + " (" + lib.bloatedThenAlwaysUsedMethods().size() + ")");
        // System.out.println(lib.bloatedThenAlwaysUsedMethods());
        //
        // System.out.println("---------------------------------------------------");
        //
        // System.out.println("OCSC" + " (" + lib.oscillateClasses().size() + ")");
        // System.out.println(lib.oscillateClasses());
        // System.out.println("OCSM" + " (" + lib.oscillateMethods().size() + ")");
        // System.out.println(lib.oscillateMethods());
        //
        // System.out.println("---------------------------------------------------");
        //
        // printCSV(lib);

    }

    public static Map<String, Commits2Versions> getStringCommits2VersionsMap(String commit2versionsPath) {
        Map<String, Commits2Versions> map = new HashMap<>();
        List<String> allLines = new ArrayList<>();
        try {
            allLines = Files.readAllLines(Paths.get(new File(commit2versionsPath).getAbsolutePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String line : allLines) {
            String[] split = line.split(",");
            map.put(split[0], new Commits2Versions(split[0], split[1], split[2], split[3]));
        }
        return map;
    }

    public static String printReport(String project, String commit, Set<String> report, String version, String order,
                                     String status) {
        String output = "";
        for (String dependency : report) {
            output += project + "," + commit + "," + dependency + "," + version + "," + order + "," + status + "\n";
        }
        return output;
    }

    // private static void printCSV(Project project) {
    //
    //     StringBuilder sb = new StringBuilder();
    //
    //     sb.append(project.getName() + "," + project.getVersions().size() + "," + "AUC" + "," + project
    //     .alwaysUsedClasses().size() + "\n")
    //             .append(project.getName() + "," + project.getVersions().size() + "," + "ABC" + "," + project
    //             .alwaysBloatedClasses().size() + "\n")
    //             .append(project.getName() + "," + project.getVersions().size() + "," + "AUM" + "," + project
    //             .alwaysUsedMethods().size() + "\n")
    //             .append(project.getName() + "," + project.getVersions().size() + "," + "ABM" + "," + project
    //             .alwaysBloatedMethods().size() + "\n")
    //
    //             .append(project.getName() + "," + project.getVersions().size() + "," + "UTABC" + "," + project
    //             .usedThenAlwaysBloatedClasses().size() + "\n")
    //             .append(project.getName() + "," + project.getVersions().size() + "," + "UTABM" + "," + project
    //             .usedThenAlwaysBloatedMethods().size() + "\n")
    //             .append(project.getName() + "," + project.getVersions().size() + "," + "BTAUC" + "," + project
    //             .bloatedThenAlwaysUsedClasses().size() + "\n")
    //             .append(project.getName() + "," + project.getVersions().size() + "," + "BTAUM" + "," + project
    //             .bloatedThenAlwaysUsedMethods().size() + "\n")
    //
    //             .append(project.getName() + "," + project.getVersions().size() + "," + "OCSC" + "," + project
    //             .oscillateClasses().size() + "\n")
    //             .append(project.getName() + "," + project.getVersions().size() + "," + "OCSM" + "," + project
    //             .oscillateMethods().size());
    //
    //     System.out.println(sb.toString());
    // }

}
