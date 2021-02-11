package se.kth.castor.wiwu.experiments.depclean.rq2;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

   public static void main(String[] args) throws IOException, CsvException {

      List<DependenciesFromCSV> dependenciesList = getDependenciesFromCSVS(
              "/Users/cesarsv/IdeaProjects/wiwu/R/Data/rq2_tmp.csv");

      Map<String, String> directMap = new HashMap<>();
      Map<String, String> transitiveMap = new HashMap<>();

      for (DependenciesFromCSV dependency : dependenciesList) {
         String key = dependency.getProject() + "/" + dependency.getDependency();
         String currentValue = dependency.getIsBloated();
         if (dependency.getIsTransitive().equals("true")) {
            if (!transitiveMap.containsKey(key)) {
               transitiveMap.put(key, currentValue);
            } else {
               String previousValue = transitiveMap.get(key);
               transitiveMap.put(key, previousValue + currentValue);
            }
         } else {
            if (!directMap.containsKey(key)) {
               directMap.put(key, currentValue);
            } else {
               String previousValue = directMap.get(key);
               directMap.put(key, previousValue + currentValue);
            }
         }
      }

      directMap.forEach((key, value) -> System.out.println(key + " -> " + value));
      
      
      

     /* // Compress direct and transitive patterns
      List<String> directCompressedPatternsList = new ArrayList<>();
      directMap.forEach((key, value) -> directCompressedPatternsList.add(compress(value)));

      List<String> transitiveCompressedPatternsList = new ArrayList<>();
      transitiveMap.forEach((key, value) -> transitiveCompressedPatternsList.add(compress(value)));

      // Write direct patterns to file
      String directPatternsToWrite = "";
      for (String s : directCompressedPatternsList) {
         directPatternsToWrite = directPatternsToWrite.concat(s).concat("\n");
      }
      FileUtils.writeStringToFile(
              new File("/Users/cesarsv/IdeaProjects/wiwu/R/Data/direct_deps_patterns.csv"),
              directPatternsToWrite
                                 );

      // Write transitive patterns to file
      String transitivePatternsToWrite = "";
      for (String s : transitiveCompressedPatternsList) {
         transitivePatternsToWrite = transitivePatternsToWrite.concat(s).concat("\n");
      }
      FileUtils.writeStringToFile(
              new File("/Users/cesarsv/IdeaProjects/wiwu/R/Data/transitive_deps_patterns.csv"),
              transitivePatternsToWrite
                                 );*/

   }

   private static String compress(String pattern) {
      StringBuilder compressedPattern = new StringBuilder("" + pattern.charAt(0));
      char previous = pattern.charAt(0);
      for (int i = 1; i < pattern.length(); i++) {
         char currentChar = pattern.charAt(i);
         if (currentChar != previous) {
            compressedPattern.append(currentChar);
            previous = currentChar;
         }
      }
      return compressedPattern.toString();
   }

   private static List<DependenciesFromCSV> getDependenciesFromCSVS(String fileName) throws FileNotFoundException {
      List<DependenciesFromCSV> projectsList = new CsvToBeanBuilder(new FileReader(fileName))
              .withType(DependenciesFromCSV.class)
              .build()
              .parse();
      return projectsList;
   }
}
