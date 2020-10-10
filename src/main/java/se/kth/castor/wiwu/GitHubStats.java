package se.kth.castor.wiwu;

import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHTag;
import org.kohsuke.github.GitHub;

import java.io.IOException;
import java.util.List;

public class GitHubStats {

    private String repositoryName;
    private GHRepository ghRepository;

    public GitHubStats(String repositoryName) throws IOException {
        this.repositoryName = repositoryName;
        GitHub github = GitHub.connect();
        ghRepository = github.getRepository(repositoryName);
    }

    public int nbForks() {
        return ghRepository.getForksCount();
    }

    public List<GHTag> tags() throws IOException {
        return ghRepository.listTags().toList();
    }

    public String url() {
        return ghRepository.getHtmlUrl().toString();
    }

}
