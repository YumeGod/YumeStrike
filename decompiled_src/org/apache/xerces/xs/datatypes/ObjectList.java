package org.apache.xerces.xs.datatypes;

public interface ObjectList {
   int getLength();

   boolean contains(Object var1);

   Object item(int var1);
}
