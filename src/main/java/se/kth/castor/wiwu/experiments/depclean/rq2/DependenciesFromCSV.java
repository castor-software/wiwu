package se.kth.castor.wiwu.experiments.depclean.rq2;

import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;

@Data
public class DependenciesFromCSV {

   @CsvBindByPosition(position = 0)
   private String project;

   @CsvBindByPosition(position = 1)
   private String date;

   @CsvBindByPosition(position = 2)
   private String dependency;

   @CsvBindByPosition(position = 3)
   private String isBloated;

   @CsvBindByPosition(position = 4)
   private String isTransitive;
}
