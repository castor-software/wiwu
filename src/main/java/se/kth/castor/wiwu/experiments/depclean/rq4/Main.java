package se.kth.castor.wiwu.experiments.depclean.rq4;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import se.kth.castor.wiwu.experiments.depclean.rq1.Project;
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
            Map<String, String> commitDateMap = objectMapper.readValue(jsonNode.get("commit_dates").toPrettyString(),
                    new TypeReference<HashMap<String, String>>() {
                    }
                                                                      );

            Iterator<JsonNode> itDependencies = jsonNode.at("/dependencies").elements();
            Iterator<String> itDependenciesNames = jsonNode.at("/dependencies").fieldNames();

            Map<Project, List<DependencyWithVersion>> map = new HashMap<>();
            while (itDependencies.hasNext()) {

               String dependencyName = itDependenciesNames.next();
               // System.out.println(dependencyName);

               JsonNode dep = itDependencies.next();
               for (JsonNode node : dep) {
                  Project project = new Project(node.get("commit").asText(), node.get("projectRelease").asText());
                  DependencyWithVersion dependency;
                  if (node.get("bloated").asBoolean()) { // is bloated
                     if (node.get("transitive").asBoolean()) { // is transitive
                        dependency = new DependencyWithVersion(true, true, node.get("version").asText(),
                                dependencyName);
                     } else { // is direct
                        dependency = new DependencyWithVersion(true, false, node.get("version").asText(),
                                dependencyName);
                     }
                  } else { // is used
                     if (node.get("transitive").asBoolean()) { // is transitive
                        dependency = new DependencyWithVersion(false, true, node.get("version").asText(),
                                dependencyName);
                     } else { // is direct
                        dependency = new DependencyWithVersion(false, false, node.get("version").asText(),
                                dependencyName);
                     }
                  }

                  if (map.containsKey(project)) {
                     map.get(project).add(dependency);
                  } else {
                     List<DependencyWithVersion> dependencies = new ArrayList<>();
                     dependencies.add(dependency);
                     map.put(project, dependencies);
                  }

               }
            }

            // map.forEach((key, value) -> System.out.println(key + " -> " + value));

            for (Map.Entry<Project, List<DependencyWithVersion>> entry : map.entrySet()) {
               Project project = entry.getKey();
               List<DependencyWithVersion> dependencies = entry.getValue();
               for (DependencyWithVersion dependency : dependencies) {
                  sb.append(repo)
                          .append(",")
                          .append(project.getSha())
                          .append(",")
                          .append(commitDateMap.get(project.getSha()))
                          .append(",")
                          .append(project.getVersion().replace(",", "."))
                          .append(",")
                          .append(dependency.getGav())
                          .append(",")
                          .append(dependency.getVersion().replace(",", "."))
                          .append(",")
                          .append(dependency.isTransitive())
                          .append(",")
                          .append(dependency.isBloated())
                          .append("\n");
               }
            }

         }

      }

      // Write the CSV file
      String body = sb.toString();
      File csv = new File("/Users/cesarsv/IdeaProjects/wiwu/R/Data/results_rq2_dependabot_with_commit_dates.csv");
      writeToCSV(body, csv);

   }

   public static void writeToCSV(String body, File file) throws IOException {
      String header = "Project," +
              "Commit," +
              "Date," +
              "ProjectVersion," +
              "Dependency," +
              "DependencyVersion," +
              "IsTransitive," +
              "IsBloated" + "\n";
      // Write CSV header
      FileUtils.writeStringToFile(file, header, true);
      // Write CSV body
      FileUtils.writeStringToFile(file, body, true);
   }
}
