package com.xmlmind.fo.converter.docx;

import com.xmlmind.fo.properties.Color;
import java.util.Hashtable;

public final class ColorTable {
   private static final Color black = new Color("black", 0, 0, 0);
   private static final Color[] colors = new Color[]{new Color("aqua", 127, 255, 212), new Color("black", 0, 0, 0), new Color("blue", 0, 0, 255), new Color("fuchsia", 255, 0, 255), new Color("gray", 190, 190, 190), new Color("green", 0, 255, 0), new Color("lime", 50, 205, 50), new Color("maroon", 176, 48, 96), new Color("navy", 0, 0, 128), new Color("olive", 85, 107, 47), new Color("purple", 160, 32, 240), new Color("red", 255, 0, 0), new Color("silver", 192, 192, 192), new Color("teal", 0, 128, 128), new Color("white", 255, 255, 255), new Color("yellow", 255, 255, 0)};
   private static Hashtable table = new Hashtable();

   public static Color get(String var0) {
      Color var1 = (Color)table.get(var0);
      if (var1 == null) {
         var1 = black;
      }

      return var1;
   }

   static {
      for(int var0 = 0; var0 < colors.length; ++var0) {
         table.put(colors[var0].name, colors[var0]);
      }

   }
}
