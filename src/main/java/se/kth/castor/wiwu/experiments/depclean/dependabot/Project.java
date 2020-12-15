package se.kth.castor.wiwu.experiments.depclean.dependabot;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Project {
   private String sha;
   private String version;

}
