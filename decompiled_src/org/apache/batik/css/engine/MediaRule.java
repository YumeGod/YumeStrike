package org.apache.batik.css.engine;

import org.w3c.css.sac.SACMediaList;

public class MediaRule extends StyleSheet implements Rule {
   public static final short TYPE = 1;
   protected SACMediaList mediaList;

   public short getType() {
      return 1;
   }

   public void setMediaList(SACMediaList var1) {
      this.mediaList = var1;
   }

   public SACMediaList getMediaList() {
      return this.mediaList;
   }

   public String toString(CSSEngine var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append("@media");
      int var3;
      if (this.mediaList != null) {
         for(var3 = 0; var3 < this.mediaList.getLength(); ++var3) {
            var2.append(' ');
            var2.append(this.mediaList.item(var3));
         }
      }

      var2.append(" {\n");

      for(var3 = 0; var3 < this.size; ++var3) {
         var2.append(this.rules[var3].toString(var1));
      }

      var2.append("}\n");
      return var2.toString();
   }
}
