package se.kth.castor.wiwu.experiments.depclean.rq4;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DependencyWithVersion {
   public String version;
   private boolean isBloated;
   private boolean isTransitive;
   private String gav;
}
