package se.kth.castor.wiwu;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.IOException;

public class App {

    private static final String repositoryName = "giltene/jHiccup";
    private static final File clonedRepositoryDir = new File(
            "cloned-repository" + "/" + repositoryName.split("/")[1]
    );

    public static void main(String[] args) throws IOException, GitAPIException {
        GitHubRepo gitHubRepo = new GitHubRepo(repositoryName);
        GitRepo gitRepo;

        if (!clonedRepositoryDir.exists()) {
            gitRepo = gitHubRepo.remoteRepository(clonedRepositoryDir);
        }

        Git git = Git.open(clonedRepositoryDir);

        git.tagList()
                .call()
                .forEach(a -> System.out.println(a.getName()));

        // TODO checkout the tag and apply DepClean there
        git.checkout()
                .setName("spoon-core-8.1.0")
                .call();

        Cmd cmd = new Cmd(clonedRepositoryDir);

    }
}
