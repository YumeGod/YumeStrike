package org.apache.xerces.xs.datatypes;

import org.apache.xerces.xs.XSException;

public interface ByteList {
   int getLength();

   boolean contains(byte var1);

   byte item(int var1) throws XSException;
}
