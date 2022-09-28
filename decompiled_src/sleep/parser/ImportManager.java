package sleep.parser;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ImportManager {
   protected List imports = new LinkedList();
   protected HashMap classes = new HashMap();

   public void importPackage(String var1, String var2) {
      String var3;
      String var4;
      if (var1.indexOf(".") > -1) {
         var4 = var1.substring(var1.lastIndexOf(".") + 1, var1.length());
         var3 = var1.substring(0, var1.lastIndexOf("."));
      } else {
         var4 = var1;
         var3 = null;
      }

      if (var2 != null) {
         File var5 = null;
         var5 = ParserConfig.findJarFile(var2);
         if (var5 == null || !var5.exists()) {
            throw new RuntimeException("jar file to import package from was not found!");
         }

         this.addFile(var5);
      }

      if (var4.equals("*")) {
         this.imports.add(var3);
      } else {
         Class var6;
         if (var3 == null) {
            this.imports.add(var1);
            var6 = this.resolveClass((String)null, var1);
            this.classes.put(var1, var6);
            if (var6 == null) {
               throw new RuntimeException("imported class was not found");
            }
         } else {
            this.imports.add(var1);
            var6 = this.findImportedClass(var1);
            this.classes.put(var4, var6);
            if (var6 == null) {
               throw new RuntimeException("imported class was not found");
            }
         }
      }

   }

   private Class resolveClass(String var1, String var2) {
      StringBuffer var3 = new StringBuffer();
      if (var1 != null) {
         var3.append(var1);
         var3.append(".");
      }

      var3.append(var2);

      try {
         return Class.forName(var3.toString());
      } catch (Exception var5) {
         return null;
      }
   }

   private void addFile(File var1) {
      try {
         URL var2 = var1.toURL();
         URLClassLoader var3 = (URLClassLoader)ClassLoader.getSystemClassLoader();
         Class var4 = URLClassLoader.class;
         Method var5 = var4.getDeclaredMethod("addURL", URL.class);
         var5.setAccessible(true);
         var5.invoke(var3, var2);
      } catch (Throwable var6) {
         var6.printStackTrace();
         throw new RuntimeException("Error, could not add " + var1 + " to system classloader - " + var6.getMessage());
      }
   }

   public Class findImportedClass(String var1) {
      if (this.classes.get(var1) == null) {
         Class var2 = null;
         if (var1.indexOf(".") > -1) {
            String var3 = var1.substring(var1.lastIndexOf(".") + 1, var1.length());
            String var4 = var1.substring(0, var1.lastIndexOf("."));
            var2 = this.resolveClass(var4, var3);
         } else {
            var2 = this.resolveClass((String)null, var1);

            for(Iterator var5 = this.imports.iterator(); var5.hasNext() && var2 == null; var2 = this.resolveClass((String)var5.next(), var1)) {
            }
         }

         this.classes.put(var1, var2);
      }

      return (Class)this.classes.get(var1);
   }
}
