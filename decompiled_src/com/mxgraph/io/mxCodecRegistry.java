package com.mxgraph.io;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class mxCodecRegistry {
   protected static Hashtable codecs = new Hashtable();
   protected static Hashtable aliases = new Hashtable();
   protected static List packages = new ArrayList();

   public static mxObjectCodec register(mxObjectCodec var0) {
      if (var0 != null) {
         String var1 = var0.getName();
         codecs.put(var1, var0);
         String var2 = getName(var0.getTemplate());
         if (!var2.equals(var1)) {
            addAlias(var2, var1);
         }
      }

      return var0;
   }

   public static void addAlias(String var0, String var1) {
      aliases.put(var0, var1);
   }

   public static mxObjectCodec getCodec(String var0) {
      String var1 = (String)aliases.get(var0);
      if (var1 != null) {
         var0 = var1;
      }

      mxObjectCodec var2 = (mxObjectCodec)codecs.get(var0);
      if (var2 == null) {
         Object var3 = getInstanceForName(var0);
         if (var3 != null) {
            try {
               var2 = new mxObjectCodec(var3);
               register(var2);
            } catch (Exception var5) {
            }
         }
      }

      return var2;
   }

   public static void addPackage(String var0) {
      packages.add(var0);
   }

   public static Object getInstanceForName(String var0) {
      Class var1 = getClassForName(var0);
      if (var1 != null) {
         if (var1.isEnum()) {
            return var1.getEnumConstants()[0];
         }

         try {
            return var1.newInstance();
         } catch (Exception var3) {
         }
      }

      return null;
   }

   public static Class getClassForName(String var0) {
      try {
         return Class.forName(var0);
      } catch (Exception var4) {
         int var1 = 0;

         while(var1 < packages.size()) {
            try {
               String var2 = (String)packages.get(var1);
               return Class.forName(var2 + "." + var0);
            } catch (Exception var3) {
               ++var1;
            }
         }

         return null;
      }
   }

   public static String getName(Object var0) {
      Class var1 = var0.getClass();
      if (!var1.isArray() && !Collection.class.isAssignableFrom(var1) && !Map.class.isAssignableFrom(var1)) {
         return packages.contains(var1.getPackage().getName()) ? var1.getSimpleName() : var1.getName();
      } else {
         return "Array";
      }
   }

   static {
      addPackage("com.mxgraph");
      addPackage("com.mxgraph.util");
      addPackage("com.mxgraph.model");
      addPackage("com.mxgraph.view");
      addPackage("java.lang");
      addPackage("java.util");
      register(new mxObjectCodec(new ArrayList()));
      register(new mxModelCodec());
      register(new mxCellCodec());
      register(new mxStylesheetCodec());
   }
}
