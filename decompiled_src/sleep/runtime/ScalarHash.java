package sleep.runtime;

import java.io.Serializable;
import java.util.Map;

public interface ScalarHash extends Serializable {
   Scalar getAt(Scalar var1);

   ScalarArray keys();

   void remove(Scalar var1);

   Map getData();
}
