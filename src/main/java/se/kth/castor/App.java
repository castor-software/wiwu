package se.kth.castor;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.IOException;

public class App {
    private static final String repositoryName = "jcabi/jcabi-github";
    private static final File clonedRepositoryDir = new File(
            "/src/main/resources/cloned-repository" + "/" + repositoryName.split("/")[1]
    );

    public static void main(String[] args) throws IOException, GitAPIException {
        GitHubStats gitHubStats = new GitHubStats(repositoryName);
        RemoteRepository remoteRepository;

        if (!clonedRepositoryDir.exists()) {
            remoteRepository = remoteRepository(repositoryName, gitHubStats);
        }

        Git git = Git.open(clonedRepositoryDir);

        git.tagList()
                .call()
                .forEach(a -> System.out.println(a.getName()));

        git.tagList()
                .call()
                .forEach(a -> System.out.println(a.getObjectId()));

        // TODO checkout tag on the tag and apply DepClean there
        git.checkout()
                .setName("0.11.1")
                .call();
    }

    private static RemoteRepository remoteRepository(String repositoryName, GitHubStats gitHubStats)
            throws IOException {
        // clone the repository
        var remoteRepository = new RemoteRepository(gitHubStats.url());
        try {
            File clonedRepositoryDir = new File(
                    "cloned-repository" + "/" + repositoryName.split("/")[1]
            );
            FileUtils.forceMkdir(clonedRepositoryDir);
            remoteRepository.cloneRepository(clonedRepositoryDir.getAbsolutePath());
        } catch (GitAPIException e) {
            System.out.println("Unable to clone the repository");
        }
        return remoteRepository;
    }
}
