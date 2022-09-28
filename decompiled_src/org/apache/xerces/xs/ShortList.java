package org.apache.xerces.xs;

public interface ShortList {
   int getLength();

   boolean contains(short var1);

   short item(int var1) throws XSException;
}
