package se.kth.castor.wiwu.experiments;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Main {

    public static void main(String[] args) {

        String dir = "/Users/cesarsv/Documents/Experiments/results/jhy_jsoup";
        List<Version> libVersions = new ArrayList<>();

        ResultFolder resultFolder = new ResultFolder(
                new File(dir).toPath()
        );

        String versions[] = resultFolder.listSubFolders();

        for (String version : versions) {
            File reportFile = new File(dir + "/" + version + "/" + "debloat" + "/" + "debloat-report.csv");
            if (Files.exists(reportFile.toPath())) {
                Report report = new Report(reportFile);
                report.processReport();
                Version libVersion = new Version(version, report);
                libVersions.add(libVersion);
            } else {
                continue;
            }
        }
        Lib lib = new Lib(libVersions);

        System.out.println("AUC" + " (" + lib.alwaysUsedClasses().size() + ")");
        System.out.println(lib.alwaysUsedClasses());
        System.out.println("ABC" + " (" + lib.alwaysBloatedClasses().size() + ")");
        System.out.println(lib.alwaysBloatedClasses());

        System.out.println("---------------------------------------------------");

        System.out.println("AUM" + " (" + lib.alwaysUsedMethods().size() + ")");
        System.out.println(lib.alwaysUsedMethods());
        System.out.println("ABM" + " (" + lib.alwaysBloatedMethods().size() + ")");
        System.out.println(lib.alwaysBloatedMethods());

        System.out.println("---------------------------------------------------");

        System.out.println("UTABC" + " (" + lib.usedThenAlwaysBloatedClasses().size() + ")");
        System.out.println(lib.usedThenAlwaysBloatedClasses());
        System.out.println("UTABM" + " (" + lib.usedThenAlwaysBloatedMethods().size() + ")");
        System.out.println(lib.usedThenAlwaysBloatedMethods());

        System.out.println("---------------------------------------------------");

        System.out.println("BTAUC" + " (" + lib.bloatedThenAlwaysUsedClasses().size() + ")");
        System.out.println(lib.bloatedThenAlwaysUsedClasses());
        System.out.println("BTAUM" + " (" + lib.bloatedThenAlwaysUsedMethods().size() + ")");
        System.out.println(lib.bloatedThenAlwaysUsedMethods());

        System.out.println("---------------------------------------------------");

        System.out.println("OCSC" + " (" + lib.oscillateClasses().size() + ")");
        System.out.println(lib.oscillateClasses());
        System.out.println("OCSM" + " (" + lib.oscillateMethods().size() + ")");
        System.out.println(lib.oscillateMethods());

    }
}
