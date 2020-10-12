package se.kth.castor.wiwu;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

class GitRepoTest {

    GitRepo gitRepo;
    String repoName;

    @BeforeEach
    void setUp() {
        gitRepo = new GitRepo("giltene/jHiccup");
        repoName = "giltene/jHiccup";
    }

    @Test
    void cloneRepository() throws IOException, GitAPIException, URISyntaxException {
        var clonedRepoDir = new File(
                "src/test/resources/cloned" + "/" + repoName.split("/")[1]
        );
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
}