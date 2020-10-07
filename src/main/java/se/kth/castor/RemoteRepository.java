package se.kth.castor;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ProgressMonitor;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Simple snippet which shows how to clone a repository from a remote source
 *
 * @author dominik.stadler at gmx.at
 */
public class RemoteRepository {

    private String remoteURL;
    private Git git;

    public RemoteRepository(String remoteURL) {
        this.remoteURL = remoteURL;
    }

    public void cloneRepository(String destinationDir)
            throws IOException, GitAPIException {
        // prepare a new folder for the cloned repository
        URL fileUrl = new URL("file://" + destinationDir);
        File destinationFile = FileUtils.toFile(fileUrl);

        // then clone
        System.out.println("Cloning from " + remoteURL + " to " + fileUrl);
        this.git = Git.cloneRepository()
                .setURI(remoteURL)
                .setDirectory(destinationFile)
                .setBranch("master")
                .setProgressMonitor(new CloneProgressMonitor())
                .call();
        // Note: the call() returns an opened repository already which needs to be closed to avoid file handle leaks!
        System.out.println("Having repository: " + git.getRepository().getDirectory());

        // clean up here to not keep using more and more disk-space for these samples
        // FileUtils.deleteDirectory(localPath);
    }

    public void listTags() throws GitAPIException {
        git.tagList().call().forEach(a -> System.out.println(a.getName()));
    }

    private static class CloneProgressMonitor implements ProgressMonitor {
        @Override
        public void start(int totalTasks) {
            System.out.println("Starting work on " + totalTasks + " tasks");
        }

        @Override
        public void beginTask(String title, int totalWork) {
            System.out.println("Start " + title + ": " + totalWork);
        }

        @Override
        public void update(int completed) {
        }

        @Override
        public void endTask() {
            System.out.println("Done!");
        }

        @Override
        public boolean isCancelled() {
            return false;
        }
    }
}

