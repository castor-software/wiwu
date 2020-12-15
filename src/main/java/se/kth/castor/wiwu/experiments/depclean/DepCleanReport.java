package se.kth.castor.wiwu.experiments.depclean;

import com.google.gson.Gson;
import lombok.Data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.Set;

@Data
public class DepCleanReport {

   private File reportFile;
   private Report report;

   public DepCleanReport(File reportFile) {
      this.reportFile = reportFile;
   }

   public void processReport() {
      Gson gson = new Gson();
      try {
         // process JSON report here
         Reader reader = new FileReader(reportFile);
         this.report = gson.fromJson(reader, Report.class);
      } catch (FileNotFoundException e) {
         e.printStackTrace();
      }
   }
}

@Data
class Report {
   private Set<String> direct_dependencies;
   private Set<String> transitive_dependencies;
   private Set<String> used_artifacts;
   private Set<String> used_direct_dependencies;
   private Set<String> used_transitive_dependencies;
   private Set<String> unused_direct_dependencies;
   private Set<String> unused_transitive_dependencies;
}
