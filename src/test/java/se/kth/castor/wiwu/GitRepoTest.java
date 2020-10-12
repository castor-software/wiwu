package se.kth.castor.wiwu;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

class GitRepoTest {

    GitRepo gitRepo;
    String repoName;
    File clonedRepoDir;

    @BeforeEach
    void setUp() {
        gitRepo = new GitRepo("giltene/jHiccup");
        repoName = "giltene/jHiccup";
        clonedRepoDir = new File(
                "src/test/resources/cloned" + "/" + repoName.split("/")[1]
        );
    }

    @Test
    void cloneRepository() throws IOException {
        GitHubRepo gitHubRepo = new GitHubRepo(repoName);
        gitHubRepo.remoteRepository(clonedRepoDir);
        Assertions.assertTrue(clonedRepoDir.exists());
    }

    @Test
    void checkoutTag() {
    }

    @Test
    void tagNames() {
    }

    @AfterEach
    void tearDown() throws URISyntaxException, IOException {
        // FileUtils.deleteDirectory(clonedRepoDir);
    }
}