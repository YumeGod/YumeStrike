package org.apache.batik.gvt.font;

import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class FontFamilyResolver {
   public static final AWTFontFamily defaultFont = new AWTFontFamily("SansSerif");
   protected static final Map fonts = new HashMap();
   protected static final List awtFontFamilies = new ArrayList();
   protected static final List awtFonts = new ArrayList();
   protected static final Map resolvedFontFamilies;

   public static String lookup(String var0) {
      return (String)fonts.get(var0.toLowerCase());
   }

   public static GVTFontFamily resolve(String var0) {
      var0 = var0.toLowerCase();
      Object var1 = (GVTFontFamily)resolvedFontFamilies.get(var0);
      if (var1 == null) {
         String var2 = (String)fonts.get(var0);
         if (var2 != null) {
            var1 = new AWTFontFamily(var2);
         }

         resolvedFontFamilies.put(var0, var1);
      }

      return (GVTFontFamily)var1;
   }

   public static GVTFontFamily resolve(UnresolvedFontFamily var0) {
      return resolve(var0.getFamilyName());
   }

   public static GVTFontFamily getFamilyThatCanDisplay(char var0) {
      for(int var1 = 0; var1 < awtFontFamilies.size(); ++var1) {
         AWTFontFamily var2 = (AWTFontFamily)awtFontFamilies.get(var1);
         AWTGVTFont var3 = (AWTGVTFont)awtFonts.get(var1);
         if (var3.canDisplay(var0) && var2.getFamilyName().indexOf("Song") == -1) {
            return var2;
         }
      }

      return null;
   }

   static {
      fonts.put("sans-serif", "SansSerif");
      fonts.put("serif", "Serif");
      fonts.put("times", "Serif");
      fonts.put("times new roman", "Serif");
      fonts.put("cursive", "Dialog");
      fonts.put("fantasy", "Symbol");
      fonts.put("monospace", "Monospaced");
      fonts.put("monospaced", "Monospaced");
      fonts.put("courier", "Monospaced");
      GraphicsEnvironment var0 = GraphicsEnvironment.getLocalGraphicsEnvironment();
      String[] var1 = var0.getAvailableFontFamilyNames();
      int var2 = var1 != null ? var1.length : 0;

      String var5;
      for(int var3 = 0; var3 < var2; ++var3) {
         fonts.put(var1[var3].toLowerCase(), var1[var3]);
         StringTokenizer var4 = new StringTokenizer(var1[var3]);

         for(var5 = ""; var4.hasMoreTokens(); var5 = var5 + var4.nextToken()) {
         }

         fonts.put(var5.toLowerCase(), var1[var3]);
         String var6 = var1[var3].replace(' ', '-');
         if (!var6.equals(var1[var3])) {
            fonts.put(var6.toLowerCase(), var1[var3]);
         }
      }

      awtFontFamilies.add(defaultFont);
      awtFonts.add(new AWTGVTFont(defaultFont.getFamilyName(), 0, 12));
      Collection var8 = fonts.values();
      Iterator var9 = var8.iterator();

      while(var9.hasNext()) {
         var5 = (String)var9.next();
         AWTFontFamily var10 = new AWTFontFamily(var5);
         awtFontFamilies.add(var10);
         AWTGVTFont var7 = new AWTGVTFont(var5, 0, 12);
         awtFonts.add(var7);
      }

      resolvedFontFamilies = new HashMap();
   }
}
