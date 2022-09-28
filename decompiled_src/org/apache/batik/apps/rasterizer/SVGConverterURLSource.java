package org.apache.batik.apps.rasterizer;

import java.io.IOException;
import java.io.InputStream;
import org.apache.batik.util.ParsedURL;

public class SVGConverterURLSource implements SVGConverterSource {
   protected static final String SVG_EXTENSION = ".svg";
   protected static final String SVGZ_EXTENSION = ".svgz";
   public static final String ERROR_INVALID_URL = "SVGConverterURLSource.error.invalid.url";
   ParsedURL purl;
   String name;

   public SVGConverterURLSource(String var1) throws SVGConverterException {
      this.purl = new ParsedURL(var1);
      String var2 = this.purl.getPath();
      int var3 = var2.lastIndexOf(47);
      String var4 = var2;
      if (var3 != -1) {
         var4 = var2.substring(var3 + 1);
      }

      if (var4.length() == 0) {
         int var5 = var2.lastIndexOf(47, var3 - 1);
         var4 = var2.substring(var5 + 1, var3);
      }

      if (var4.length() == 0) {
         throw new SVGConverterException("SVGConverterURLSource.error.invalid.url", new Object[]{var1});
      } else {
         var3 = var4.indexOf(63);
         String var7 = "";
         if (var3 != -1) {
            var7 = var4.substring(var3 + 1);
            var4 = var4.substring(0, var3);
         }

         this.name = var4;
         String var6 = this.purl.getRef();
         if (var6 != null && var6.length() != 0) {
            this.name = this.name + "_" + var6.hashCode();
         }

         if (var7 != null && var7.length() != 0) {
            this.name = this.name + "_" + var7.hashCode();
         }

      }
   }

   public String toString() {
      return this.purl.toString();
   }

   public String getURI() {
      return this.toString();
   }

   public boolean equals(Object var1) {
      return var1 != null && var1 instanceof SVGConverterURLSource ? this.purl.equals(((SVGConverterURLSource)var1).purl) : false;
   }

   public int hashCode() {
      return this.purl.hashCode();
   }

   public InputStream openStream() throws IOException {
      return this.purl.openStream();
   }

   public boolean isSameAs(String var1) {
      return this.toString().equals(var1);
   }

   public boolean isReadable() {
      return true;
   }

   public String getName() {
      return this.name;
   }
}
