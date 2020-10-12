package se.kth.castor.wiwu;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Map;
import java.util.Set;

class CmdTest {

    Cmd cmd;
    File clonedRepoDir;

    @BeforeEach
    void setUp() {
        String repoName = "giltene/jHiccup";
        clonedRepoDir = new File(
                "src/test/resources/cloned" + "/" + repoName.split("/")[1]
        );
        cmd = new Cmd(clonedRepoDir);
    }

    @Test
    void obtainDepCleanResult() {
        Map<String, Set<String>> result = cmd.depCleanResult();
        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    void obtainDependencyTree() {
        // File file = new File(clonedRepoDir.getAbsolutePath() + "/" + "dependency-tree.txt");
        // cmd.dependencyTree(file);
        // Assertions.assertTrue(file.exists());
    }
}