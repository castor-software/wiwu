package se.kth.castor.wiwu.experiments.jdbl;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Main {

    public static void main(String[] args) {

        String dir = "/Users/cesarsv/Documents/Experiments/results/apache_commons-text";
        List<Version> libVersions = new ArrayList<>();

        ResultFolder resultFolder = new ResultFolder(
                new File(dir).toPath()
        );

        String versions[] = resultFolder.listSubFolders();

        for (String version : versions) {
            File reportFile = new File(dir + "/" + version + "/" + "debloat" + "/" + "debloat-report.csv");
            if (Files.exists(reportFile.toPath())) {
                JDBLReport report = new JDBLReport(reportFile);
                report.processReport();
                Version libVersion = new Version(version, report);
                libVersions.add(libVersion);
            } else {
                continue;
            }
        }
        Project project = new Project(libVersions, dir.split("/")[dir.split("/").length - 1]);

        System.out.println("AUC" + " (" + project.alwaysUsedClasses().size() + ")");
        System.out.println(project.alwaysUsedClasses());
        System.out.println("ABC" + " (" + project.alwaysBloatedClasses().size() + ")");
        System.out.println(project.alwaysBloatedClasses());

        System.out.println("---------------------------------------------------");

        System.out.println("AUM" + " (" + project.alwaysUsedMethods().size() + ")");
        System.out.println(project.alwaysUsedMethods());
        System.out.println("ABM" + " (" + project.alwaysBloatedMethods().size() + ")");
        System.out.println(project.alwaysBloatedMethods());

        System.out.println("---------------------------------------------------");

        System.out.println("UTABC" + " (" + project.usedThenAlwaysBloatedClasses().size() + ")");
        System.out.println(project.usedThenAlwaysBloatedClasses());
        System.out.println("UTABM" + " (" + project.usedThenAlwaysBloatedMethods().size() + ")");
        System.out.println(project.usedThenAlwaysBloatedMethods());

        System.out.println("---------------------------------------------------");

        System.out.println("BTAUC" + " (" + project.bloatedThenAlwaysUsedClasses().size() + ")");
        System.out.println(project.bloatedThenAlwaysUsedClasses());
        System.out.println("BTAUM" + " (" + project.bloatedThenAlwaysUsedMethods().size() + ")");
        System.out.println(project.bloatedThenAlwaysUsedMethods());

        System.out.println("---------------------------------------------------");

        System.out.println("OCSC" + " (" + project.oscillateClasses().size() + ")");
        System.out.println(project.oscillateClasses());
        System.out.println("OCSM" + " (" + project.oscillateMethods().size() + ")");
        System.out.println(project.oscillateMethods());

        System.out.println("---------------------------------------------------");

        printCSV(project);

    }

    private static void printCSV(Project project) {

        StringBuilder sb = new StringBuilder();

        sb.append(project.getName() + "," + project.getVersions().size() + "," + "AUC" + "," + project.alwaysUsedClasses().size() + "\n")
                .append(project.getName() + "," + project.getVersions().size() + "," + "ABC" + "," + project.alwaysBloatedClasses().size() + "\n")
                .append(project.getName() + "," + project.getVersions().size() + "," + "AUM" + "," + project.alwaysUsedMethods().size() + "\n")
                .append(project.getName() + "," + project.getVersions().size() + "," + "ABM" + "," + project.alwaysBloatedMethods().size() + "\n")

                .append(project.getName() + "," + project.getVersions().size() + "," + "UTABC" + "," + project.usedThenAlwaysBloatedClasses().size() + "\n")
                .append(project.getName() + "," + project.getVersions().size() + "," + "UTABM" + "," + project.usedThenAlwaysBloatedMethods().size() + "\n")
                .append(project.getName() + "," + project.getVersions().size() + "," + "BTAUC" + "," + project.bloatedThenAlwaysUsedClasses().size() + "\n")
                .append(project.getName() + "," + project.getVersions().size() + "," + "BTAUM" + "," + project.bloatedThenAlwaysUsedMethods().size() + "\n")

                .append(project.getName() + "," + project.getVersions().size() + "," + "OCSC" + "," + project.oscillateClasses().size() + "\n")
                .append(project.getName() + "," + project.getVersions().size() + "," + "OCSM" + "," + project.oscillateMethods().size());

        System.out.println(sb.toString());
    }

}
