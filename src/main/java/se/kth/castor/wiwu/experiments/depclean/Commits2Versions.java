package se.kth.castor.wiwu.experiments.depclean;

import lombok.Data;

@Data
public class Commits2Versions {
    private String commit;
    private String order;
    private String version;
    private String projectName;

    public Commits2Versions(String commit, String order, String version, String projectName) {
        this.commit = commit;
        this.order = order;
        this.version = version;
        this.projectName = projectName;
    }
}
