package se.kth.castor.wiwu;

import fr.dutra.tools.maven.deptree.core.ParseException;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GitRepoTest {

    static GitRepo gitRepo;
    static String repoName;
    static File clonedRepoDir;
    static Cmd cmd;

    @BeforeAll
    static void setUp() {
        gitRepo = new GitRepo("giltene/jHiccup");
        repoName = "giltene/jHiccup";
        clonedRepoDir = new File(
                "src/test/resources/" + repoName.split("/")[1]
        );
        cmd = new Cmd(clonedRepoDir);
    }

    @Test
    @Order(1)
    void cloneRepository() throws IOException {
        GitHubRepo gitHubRepo = new GitHubRepo(repoName);
        gitHubRepo.remoteRepository(clonedRepoDir);
        Assertions.assertTrue(clonedRepoDir.exists());
    }

    @Test
    @Order(2)
    void compileProject() {
        Assertions.assertTrue(cmd.compileProject());
    }

    @Test
    @Order(3)
    void obtainDependencyTree() {
        File file = new File(clonedRepoDir.getAbsolutePath() + "/" + "dependency-tree.txt");
        cmd.dependencyTree(file);
        Assertions.assertTrue(file.exists());
    }

    @AfterAll
    static void afterAll() throws IOException {
        FileUtils.deleteDirectory(clonedRepoDir);
    }

    @Test
    @Order(5)
    void obtainDepCleanResult() {
        Map<String, Set<String>> result = cmd.depCleanResult();
        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    @Order(4)
    void toJSON() throws IOException, ParseException {
        DepTree depTree = new DepTree(clonedRepoDir.getAbsolutePath() + "/" + "dependency-tree.txt");
        String jsonResult = "{\"coordinates\":\"org.jhiccup:jHiccup:jar:2.0.11-SNAPSHOT\",\"children\":" +
                "[{\"coordinates\":\"junit:junit:jar:4.10:test\",\"children\":[{\"coordinates\":" +
                "\"org.hamcrest:hamcrest-core:jar:1.1:test\",\"children\":[]}]},{\"coordinates\":" +
                "\"org.hdrhistogram:HdrHistogram:jar:2.1.11:compile\",\"children\":[]}]}";
        Assertions.assertEquals(jsonResult, depTree.parseTreeToJSON());
    }
}