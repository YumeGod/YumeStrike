package com.xmlmind.fo.converter;

import java.util.Hashtable;

public abstract class MsTranslator extends Translator {
   protected static final int BOOKMARK_MAX_LENGTH = 39;
   protected Hashtable nameToChecked = new Hashtable();
   protected Hashtable checkedToName = new Hashtable();

   protected MsTranslator() {
   }

   public String checkBookmark(String var1) {
      String var2 = (String)this.nameToChecked.get(var1);
      if (var2 == null) {
         int var4;
         if (var1.indexOf(45) < 0 && var1.indexOf(46) < 0 && var1.length() <= 39) {
            var2 = var1;
         } else {
            StringBuffer var3 = new StringBuffer();
            var4 = Math.min(var1.length(), 39);

            for(int var5 = 0; var5 < var4; ++var5) {
               char var6 = var1.charAt(var5);
               switch (var6) {
                  case '-':
                  case '.':
                     var3.append('_');
                     break;
                  default:
                     var3.append(var6);
               }
            }

            var2 = var3.toString();
         }

         String var7 = var2.length() <= 36 ? var2 : var2.substring(0, 36);

         StringBuffer var10000;
         for(var4 = 1; this.checkedToName.containsKey(var2) && var4 < 999; var2 = var10000.append(Integer.toString(var4)).toString()) {
            var10000 = (new StringBuffer()).append(var7);
            ++var4;
         }

         this.nameToChecked.put(var1, var2);
         this.checkedToName.put(var2, var1);
      }

      return var2;
   }
}
