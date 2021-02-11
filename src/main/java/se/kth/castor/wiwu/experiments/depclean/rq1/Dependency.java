package se.kth.castor.wiwu.experiments.depclean.rq1;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Dependency {
   private boolean isBloated;
   private boolean isTransitive;
}
