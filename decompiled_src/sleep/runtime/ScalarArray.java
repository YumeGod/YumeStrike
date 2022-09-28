package sleep.runtime;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;

public interface ScalarArray extends Serializable {
   Scalar pop();

   Scalar push(Scalar var1);

   int size();

   Scalar getAt(int var1);

   Iterator scalarIterator();

   Scalar add(Scalar var1, int var2);

   void remove(Scalar var1);

   Scalar remove(int var1);

   void sort(Comparator var1);

   ScalarArray sublist(int var1, int var2);
}
