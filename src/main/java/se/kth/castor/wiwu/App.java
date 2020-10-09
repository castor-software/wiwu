package se.kth.castor.wiwu;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.IOException;

public class App {
    private static final String repositoryName = "INRIA/spoon";
    private static final File clonedRepositoryDir = new File(
            "src/main/resources/cloned-repository" + "/" + repositoryName.split("/")[1]
    );

    public static void main(String[] args) throws IOException, GitAPIException {
        GitHubStats gitHubStats = new GitHubStats(repositoryName);
        RemoteRepository remoteRepository;

        if (!clonedRepositoryDir.exists()) {
            remoteRepository = remoteRepository(gitHubStats);
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

    /**
     * Clones a repository
     * @param gitHubStats
     * @return
     * @throws IOException
     */
    private static RemoteRepository remoteRepository(GitHubStats gitHubStats) throws IOException {
        // clone the repository
        var remoteRepository = new RemoteRepository(gitHubStats.url());
        try {
            FileUtils.forceMkdir(clonedRepositoryDir);
            remoteRepository.cloneRepository(clonedRepositoryDir.getAbsolutePath());
        } catch (GitAPIException e) {
            System.out.println("Unable to clone the repository");
        }
        return remoteRepository;
    }
}
