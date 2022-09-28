package org.apache.batik.css.engine;

import org.apache.batik.util.ParsedURL;

public class ImportRule extends MediaRule {
   public static final short TYPE = 2;
   protected ParsedURL uri;

   public short getType() {
      return 2;
   }

   public void setURI(ParsedURL var1) {
      this.uri = var1;
   }

   public ParsedURL getURI() {
      return this.uri;
   }

   public String toString(CSSEngine var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append("@import \"");
      var2.append(this.uri);
      var2.append("\"");
      if (this.mediaList != null) {
         for(int var3 = 0; var3 < this.mediaList.getLength(); ++var3) {
            var2.append(' ');
            var2.append(this.mediaList.item(var3));
         }
      }

      var2.append(";\n");
      return var2.toString();
   }
}
