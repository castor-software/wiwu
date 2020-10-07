package se.kth.castor;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.kohsuke.github.GHTag;
import org.kohsuke.github.GitHub;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Hello world!
 *
 */
public class App {

    /**
     * @param githubRemoteUrl Remote git http url which ends with .git.
     * @param branchName Name of the branch which should be downloaded
     * @param destinationDir  Destination directory where the downloaded files should be present.
     * @return
     * @throws Exception
     */
    private static boolean downloadRepoContent(String githubRemoteUrl, String branchName,
                                               String destinationDir) throws Exception {
        //String githubSourceUrl, String accessToken
        CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(
                "cesarsotovalero", "cesar*89091931786");
        URL fileUrl = new URL("file://" + destinationDir);
        File destinationFile = FileUtils.toFile(fileUrl);
        //delete any existing file
        FileUtils.deleteDirectory(destinationFile);
        Git git = Git.cloneRepository()
                .setURI(githubRemoteUrl)
                .setBranch(branchName)
                .setDirectory(destinationFile)
                .setCredentialsProvider(credentialsProvider)
                .call();

        if (destinationFile.length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) throws IOException {
        GitHub github = GitHub.connect();
        var ghRepository = github.getRepository("jcabi/jcabi-github");
        var remoteRepository = new RemoteRepository(ghRepository.getHtmlUrl().toString());
        try {
            remoteRepository.cloneRepository("/Users/cesarsv/IdeaProjects/wiwu/cloned");
            remoteRepository.listTags();
        } catch (GitAPIException e) {
            System.out.println("Unable to clone the repository");
        }

        for (GHTag tag : ghRepository.listTags().toList()) {
            System.out.println(tag.getName());
            tag.getCommit();

        }

    }
}
