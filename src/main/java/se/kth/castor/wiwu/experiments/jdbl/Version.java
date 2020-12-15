package se.kth.castor.wiwu.experiments.jdbl;

import lombok.Data;

@Data
public class Version {

   private String name = "";
   private JDBLReport report;

   public Version(String name, JDBLReport report) {
      this.name = name;
      this.report = report;
   }
}
