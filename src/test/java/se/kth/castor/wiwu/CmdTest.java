package se.kth.castor.wiwu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Map;
import java.util.Set;

class CmdTest {

    Cmd cmd;

    @BeforeEach
    void setUp() {
        String repoName = "giltene/jHiccup";
        cmd = new Cmd(new File(
                "src/test/resources/cloned" + "/" + repoName.split("/")[1]
        ));
    }

    @Test
    void obtainDepCleanResult() {
        Map<String, Set<String>> result = cmd.depCleanResult();
        System.out.println(result);
    }

    @Test
    void obtainDependencyTree() {
        cmd.dependencyTree(new File("./target/dependency-tree.txt"));
    }
}