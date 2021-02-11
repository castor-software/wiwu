package se.kth.castor.wiwu.experiments.depclean.rq2;

import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;

@Data
public class ProjectFromCSV {

   @CsvBindByPosition(position = 0)
   private String project;

   @CsvBindByPosition(position = 1)
   private String nbCommits;
}
