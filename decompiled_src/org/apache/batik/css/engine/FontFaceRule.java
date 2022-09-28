package org.apache.batik.css.engine;

import org.apache.batik.util.ParsedURL;

public class FontFaceRule implements Rule {
   public static final short TYPE = 3;
   StyleMap sm;
   ParsedURL purl;

   public FontFaceRule(StyleMap var1, ParsedURL var2) {
      this.sm = var1;
      this.purl = var2;
   }

   public short getType() {
      return 3;
   }

   public ParsedURL getURL() {
      return this.purl;
   }

   public StyleMap getStyleMap() {
      return this.sm;
   }

   public String toString(CSSEngine var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append("@font-face { ");
      var2.append(this.sm.toString(var1));
      var2.append(" }\n");
      return var2.toString();
   }
}
