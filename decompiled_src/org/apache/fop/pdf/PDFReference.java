package org.apache.fop.pdf;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;

public class PDFReference implements PDFWritable {
   private int objectNumber;
   private int generation;
   private Reference objReference;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   public PDFReference(PDFObject obj) {
      this.objectNumber = obj.getObjectNumber();
      this.generation = obj.getGeneration();
      this.objReference = new SoftReference(obj);
   }

   public PDFReference(String ref) {
      if (ref == null) {
         throw new NullPointerException("ref must not be null");
      } else {
         String[] parts = ref.split(" ");
         if (!$assertionsDisabled && parts.length != 3) {
            throw new AssertionError();
         } else {
            this.objectNumber = Integer.parseInt(parts[0]);
            this.generation = Integer.parseInt(parts[1]);
            if (!$assertionsDisabled && !"R".equals(parts[2])) {
               throw new AssertionError();
            }
         }
      }
   }

   public PDFObject getObject() {
      if (this.objReference != null) {
         PDFObject obj = (PDFObject)this.objReference.get();
         if (obj == null) {
            this.objReference = null;
         }

         return obj;
      } else {
         return null;
      }
   }

   public int getObjectNumber() {
      return this.objectNumber;
   }

   public int getGeneration() {
      return this.generation;
   }

   public String toString() {
      return this.getObjectNumber() + " " + this.getGeneration() + " R";
   }

   public void outputInline(OutputStream out, Writer writer) throws IOException {
      writer.write(this.toString());
   }

   static {
      $assertionsDisabled = !PDFReference.class.desiredAssertionStatus();
   }
}
