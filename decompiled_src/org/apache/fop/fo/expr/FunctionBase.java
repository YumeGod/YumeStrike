package org.apache.fop.fo.expr;

import org.apache.fop.datatypes.PercentBase;

public abstract class FunctionBase implements Function {
   public PercentBase getPercentBase() {
      return null;
   }

   public boolean padArgsWithPropertyName() {
      return false;
   }
}
