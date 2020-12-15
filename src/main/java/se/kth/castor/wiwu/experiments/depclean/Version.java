package se.kth.castor.wiwu.experiments.depclean;

import lombok.Data;

@Data
public class Version {

    private String name = "";
    private Report report;

    public Version(String name, Report report) {
        this.name = name;
        this.report = report;
    }
}
