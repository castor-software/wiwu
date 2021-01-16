package se.kth.castor.wiwu.experiments.depclean.dependabot;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import se.kth.castor.wiwu.experiments.jdbl.ResultFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
public class Main {

   private static final String RESULTS_DEPENDABOT = "/Users/cesarsv/Documents/Experiments/resultsDependabot_with_dates";

   public static void main(String[] args) throws IOException {

      ResultFolder resultFolder = new ResultFolder(
              new File(RESULTS_DEPENDABOT).toPath()
      );

      StringBuilder sb = new StringBuilder();

      String[] arr = resultFolder.listSubFolders();
      for (String projectFolder : arr) {
         log.info("Processing: " + projectFolder);

         File json = new File(RESULTS_DEPENDABOT + File.separator + projectFolder + File.separator + "info.json");
         if (json.exists()) {

            String content = Files.readString(Paths.get(json.getAbsolutePath()));

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(content);
            String repo = jsonNode.get("repo").asText();

            //another way
            Map<String,String> commitDateMap = objectMapper.readValue(jsonNode.get("commit_dates").toPrettyString(),
                    new TypeReference<HashMap<String,String>>() {});


            // System.exit(-1);


            Iterator<JsonNode> itDependencies = jsonNode.at("/dependencies").elements();
            Map<Project, List<Dependency>> map = new HashMap<>();
            while (itDependencies.hasNext()) {
               for (JsonNode node : itDependencies.next()) {
                  Project project = new Project(node.get("commit").asText(), node.get("projectRelease").asText());
                  Dependency dependency;
                  if (node.get("bloated").asBoolean()) { // is bloated
                     if (node.get("transitive").asBoolean()) { // is transitive
                        dependency = new Dependency(true, true);
                     } else { // is direct
                        dependency = new Dependency(true, false);
                     }
                  } else { // is used
                     if (node.get("transitive").asBoolean()) { // is transitive
                        dependency = new Dependency(false, true);
                     } else { // is direct
                        dependency = new Dependency(false, false);
                     }
                  }

                  if (map.containsKey(project)) {
                     map.get(project).add(dependency);
                  } else {
                     List<Dependency> dependencies = new ArrayList<>();
                     dependencies.add(dependency);
                     map.put(project, dependencies);
                  }

               }
            }

            for (Map.Entry<Project, List<Dependency>> entry : map.entrySet()) {
               Project project = entry.getKey();
               int total = entry.getValue().size();
               int bloatedDirect = 0;
               int bloatedTransitive = 0;
               int usedDirect = 0;
               int usedTransitive = 0;
               List<Dependency> dependencies = entry.getValue();
               for (Dependency dependency : dependencies) {
                  if (dependency.isBloated()) {// is bloated
                     if (dependency.isTransitive()) { // is transitive
                        bloatedTransitive++;
                     } else { // is direct
                        bloatedDirect++;
                     }
                  } else { // is used
                     if (dependency.isTransitive()) { // is transitive
                        usedTransitive++;

                     } else { // is direct
                        usedDirect++;
                     }
                  }
               }

               sb.append(repo)
                       .append(",")
                       .append(project.getSha())
                       .append(",")
                       .append(commitDateMap.get(project.getSha()))
                       .append(",")
                       .append(project.getVersion().replace(",", "."))
                       .append(",")
                       .append(total)
                       .append(",")
                       .append(bloatedDirect)
                       .append(",")
                       .append(bloatedTransitive)
                       .append(",")
                       .append(usedDirect)
                       .append(",")
                       .append(usedTransitive)
                       .append("\n");
            }

         }

      }

      // Write the CSV file
      String body = sb.toString();
      File csv = new File("/Users/cesarsv/IdeaProjects/wiwu/R/Data/results_dependabot_with_commit_dates.csv");
      writeToCSV(body, csv);

   }

   public static void writeToCSV(String body, File file) throws IOException {
      String header = "Project," +
              "Commit," +
              "Date," +
              "Version," +
              "NbDependencies," +
              "BloatedDirect," +
              "BloatedTransitive," +
              "UsedDirect," +
              "UsedTransitive\n";
      // Write CSV header
      FileUtils.writeStringToFile(file, header, true);
      // Write CSV body
      FileUtils.writeStringToFile(file, body, true);
   }
}
