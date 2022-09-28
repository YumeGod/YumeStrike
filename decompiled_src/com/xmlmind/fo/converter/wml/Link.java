package com.xmlmind.fo.converter.wml;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.converter.MsTranslator;
import com.xmlmind.fo.properties.PropertyValues;
import com.xmlmind.fo.util.Encoder;

public final class Link {
   public static final int TYPE_INTERNAL = 0;
   public static final int TYPE_EXTERNAL = 1;
   public int type;
   public String target;

   public Link(Context var1) {
      PropertyValues var2 = var1.properties;
      short var3 = 132;
      if (var2.isSpecified(var3)) {
         this.type = 0;
         this.target = ((MsTranslator)var1.translator).checkBookmark(var2.values[var3].idref());
      } else {
         byte var4 = 100;
         if (var2.isSpecified(var4)) {
            this.type = 1;
            this.target = var2.values[var4].uriSpecification();
         }
      }

   }

   public String start(Encoder var1) {
      StringBuffer var2 = new StringBuffer("<w:hlink");
      if (this.target != null) {
         if (this.type == 1) {
            var2.append(" w:dest=\"");
         } else {
            var2.append(" w:bookmark=\"");
         }

         var2.append(Wml.escape(this.target, var1) + "\"");
      }

      var2.append(">");
      return var2.toString();
   }

   public String end() {
      return "</w:hlink>";
   }
}
