package se.kth.castor;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.IOException;

public class App {
    private static final String repositoryName = "jcabi/jcabi-github";
    private static File clonedRepositoryDir = new File(
            "cloned-repository" + "/" + repositoryName.split("/")[1]
    );

    public static void main(String[] args) throws IOException, GitAPIException {

        GitHubStats gitHubStats = new GitHubStats(repositoryName);
        RemoteRepository remoteRepository = remoteRepository(repositoryName, gitHubStats);

        if (!clonedRepositoryDir.exists()) {
            remoteRepository = remoteRepository(repositoryName, gitHubStats);
        }

        // TODO checkout tag on the tag and apply DepClean there
        System.out.println(remoteRepository.tagNames());
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
