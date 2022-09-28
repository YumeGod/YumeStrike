package sleep.runtime;

import java.io.Serializable;

public interface ScalarType extends Serializable {
   ScalarType copyValue();

   int intValue();

   long longValue();

   double doubleValue();

   String toString();

   Object objectValue();

   Class getType();
}
