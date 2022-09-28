package sleep.interfaces;

import java.io.Serializable;
import sleep.runtime.Scalar;

public interface Variable extends Serializable {
   boolean scalarExists(String var1);

   Scalar getScalar(String var1);

   Scalar putScalar(String var1, Scalar var2);

   void removeScalar(String var1);

   Variable createLocalVariableContainer();

   Variable createInternalVariableContainer();
}
