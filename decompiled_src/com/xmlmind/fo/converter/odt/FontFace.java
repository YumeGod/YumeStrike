package com.xmlmind.fo.converter.odt;

import java.io.PrintWriter;

public final class FontFace {
   public static final String FAMILY_ROMAN = "roman";
   public static final String FAMILY_SWISS = "swiss";
   public static final String FAMILY_MODERN = "modern";
   public static final String FAMILY_SCRIPT = "script";
   public static final String FAMILY_DECORATIVE = "decorative";
   public static final String FAMILY_SYSTEM = "system";
   public String name;
   public String family;
   public String charset;
   public String id;

   public FontFace(String var1) {
      this(var1, (String)null, (String)null);
   }

   public FontFace(String var1, String var2) {
      this(var1, var2, (String)null);
   }

   public FontFace(String var1, String var2, String var3) {
      // $FF: Couldn't be decompiled
   }

   public void print(PrintWriter var1) {
      var1.print("<style:font-face");
      var1.print(" style:name=\"" + this.id + "\"");
      var1.print(" svg:font-family=\"" + this.name + "\"");
      if (this.family != null) {
         var1.print(" style:font-family-generic=\"" + this.family + "\"");
      }

      if (this.charset != null) {
         var1.print(" style:font-charset=\"" + this.charset + "\"");
      }

      var1.println("/>");
   }

   public int hashCode() {
      return this.name.toLowerCase().hashCode();
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof FontFace)) {
         return false;
      } else {
         FontFace var2 = (FontFace)var1;
         return this.name.equalsIgnoreCase(var2.name);
      }
   }
}
