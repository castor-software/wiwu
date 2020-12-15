package se.kth.castor.wiwu.experiments.depclean;

import lombok.Data;

import java.util.List;

@Data
public class Project {

   private String name = "";
   private List<Version> versions;

   public Project(String name, List<Version> versions) {
      this.name = name;
      this.versions = versions;
   }
}
