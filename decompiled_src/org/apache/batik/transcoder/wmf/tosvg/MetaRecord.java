package org.apache.batik.transcoder.wmf.tosvg;

import java.util.ArrayList;
import java.util.List;

public class MetaRecord {
   public int functionId;
   public int numPoints;
   private final List ptVector = new ArrayList();

   public void EnsureCapacity(int var1) {
   }

   public void AddElement(Object var1) {
      this.ptVector.add(var1);
   }

   public final void addElement(int var1) {
      this.ptVector.add(new Integer(var1));
   }

   public Integer ElementAt(int var1) {
      return (Integer)this.ptVector.get(var1);
   }

   public final int elementAt(int var1) {
      return (Integer)this.ptVector.get(var1);
   }

   public static class StringRecord extends MetaRecord {
      public final String text;

      public StringRecord(String var1) {
         this.text = var1;
      }
   }

   public static class ByteRecord extends MetaRecord {
      public final byte[] bstr;

      public ByteRecord(byte[] var1) {
         this.bstr = var1;
      }
   }
}
