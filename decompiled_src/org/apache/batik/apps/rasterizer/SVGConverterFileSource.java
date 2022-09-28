package org.apache.batik.apps.rasterizer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;

public class SVGConverterFileSource implements SVGConverterSource {
   File file;
   String ref;

   public SVGConverterFileSource(File var1) {
      this.file = var1;
   }

   public SVGConverterFileSource(File var1, String var2) {
      this.file = var1;
      this.ref = var2;
   }

   public String getName() {
      String var1 = this.file.getName();
      if (this.ref != null && !"".equals(this.ref)) {
         var1 = var1 + '#' + this.ref;
      }

      return var1;
   }

   public File getFile() {
      return this.file;
   }

   public String toString() {
      return this.getName();
   }

   public String getURI() {
      try {
         String var1 = this.file.toURL().toString();
         if (this.ref != null && !"".equals(this.ref)) {
            var1 = var1 + '#' + this.ref;
         }

         return var1;
      } catch (MalformedURLException var2) {
         throw new Error(var2.getMessage());
      }
   }

   public boolean equals(Object var1) {
      return var1 != null && var1 instanceof SVGConverterFileSource ? this.file.equals(((SVGConverterFileSource)var1).file) : false;
   }

   public int hashCode() {
      return this.file.hashCode();
   }

   public InputStream openStream() throws FileNotFoundException {
      return new FileInputStream(this.file);
   }

   public boolean isSameAs(String var1) {
      return this.file.toString().equals(var1);
   }

   public boolean isReadable() {
      return this.file.canRead();
   }
}
