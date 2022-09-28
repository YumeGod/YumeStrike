package sleep.bridges;

import java.io.File;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Stack;
import sleep.interfaces.Function;
import sleep.interfaces.Loadable;
import sleep.interfaces.Predicate;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptInstance;
import sleep.runtime.SleepUtils;

public class FileSystemBridge implements Loadable, Function, Predicate {
   public void scriptUnloaded(ScriptInstance var1) {
   }

   public void scriptLoaded(ScriptInstance var1) {
      Hashtable var2 = var1.getScriptEnvironment().getEnvironment();
      var2.put("-exists", this);
      var2.put("-canread", this);
      var2.put("-canwrite", this);
      var2.put("-isDir", this);
      var2.put("-isFile", this);
      var2.put("-isHidden", this);
      var2.put("&createNewFile", this);
      var2.put("&deleteFile", this);
      var2.put("&chdir", this);
      var2.put("&cwd", this);
      var2.put("&getCurrentDirectory", this);
      var2.put("&getFileName", new getFileName());
      var2.put("&getFileProper", new getFileProper());
      var2.put("&getFileParent", new getFileParent());
      var2.put("&lastModified", new lastModified());
      var2.put("&lof", new lof());
      var2.put("&ls", new listFiles());
      var2.put("&listRoots", var2.get("&ls"));
      var2.put("&mkdir", this);
      var2.put("&rename", this);
      var2.put("&setLastModified", this);
      var2.put("&setReadOnly", this);
   }

   public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
      File var4;
      if (var1.equals("&createNewFile")) {
         try {
            var4 = BridgeUtilities.getFile(var3, var2);
            if (var4.createNewFile()) {
               return SleepUtils.getScalar((int)1);
            }
         } catch (Exception var7) {
            var2.getScriptEnvironment().flagError(var7);
         }
      } else {
         if (var1.equals("&cwd") || var1.equals("&getCurrentDirectory")) {
            return SleepUtils.getScalar((Object)var2.cwd());
         }

         if (var1.equals("&chdir")) {
            var2.chdir(BridgeUtilities.getFile(var3, var2));
         } else if (var1.equals("&deleteFile")) {
            var4 = BridgeUtilities.getFile(var3, var2);
            if (var4.delete()) {
               return SleepUtils.getScalar((int)1);
            }
         } else if (var1.equals("&mkdir")) {
            var4 = BridgeUtilities.getFile(var3, var2);
            if (var4.mkdirs()) {
               return SleepUtils.getScalar((int)1);
            }
         } else if (var1.equals("&rename")) {
            var4 = BridgeUtilities.getFile(var3, var2);
            File var5 = BridgeUtilities.getFile(var3, var2);
            if (var4.renameTo(var5)) {
               return SleepUtils.getScalar((int)1);
            }
         } else if (var1.equals("&setLastModified")) {
            var4 = BridgeUtilities.getFile(var3, var2);
            long var8 = BridgeUtilities.getLong(var3);
            if (var4.setLastModified(var8)) {
               return SleepUtils.getScalar((int)1);
            }
         } else if (var1.equals("&setReadOnly")) {
            var4 = BridgeUtilities.getFile(var3, var2);
            if (var4.setReadOnly()) {
               return SleepUtils.getScalar((int)1);
            }

            return SleepUtils.getEmptyScalar();
         }
      }

      return SleepUtils.getEmptyScalar();
   }

   public boolean decide(String var1, ScriptInstance var2, Stack var3) {
      File var4 = BridgeUtilities.getFile(var3, var2);
      if (var1.equals("-canread")) {
         return var4.canRead();
      } else if (var1.equals("-canwrite")) {
         return var4.canWrite();
      } else if (var1.equals("-exists")) {
         return var4.exists();
      } else if (var1.equals("-isDir")) {
         return var4.isDirectory();
      } else if (var1.equals("-isFile")) {
         return var4.isFile();
      } else {
         return var1.equals("-isHidden") ? var4.isHidden() : false;
      }
   }

   private static class listFiles implements Function {
      private listFiles() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         File[] var4;
         if (var3.isEmpty() && var1.equals("&listRoots")) {
            var4 = File.listRoots();
         } else {
            File var5 = BridgeUtilities.getFile(var3, var2);
            var4 = var5.listFiles();
         }

         LinkedList var7 = new LinkedList();
         if (var4 != null) {
            for(int var6 = 0; var6 < var4.length; ++var6) {
               var7.add(var4[var6].getAbsolutePath());
            }
         }

         return SleepUtils.getArrayWrapper(var7);
      }

      // $FF: synthetic method
      listFiles(Object var1) {
         this();
      }
   }

   private static class lof implements Function {
      private lof() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         File var4 = BridgeUtilities.getFile(var3, var2);
         return SleepUtils.getScalar(var4.length());
      }

      // $FF: synthetic method
      lof(Object var1) {
         this();
      }
   }

   private static class lastModified implements Function {
      private lastModified() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         File var4 = BridgeUtilities.getFile(var3, var2);
         return SleepUtils.getScalar(var4.lastModified());
      }

      // $FF: synthetic method
      lastModified(Object var1) {
         this();
      }
   }

   private static class getFileParent implements Function {
      private getFileParent() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         File var4 = BridgeUtilities.getFile(var3, var2);
         return SleepUtils.getScalar(var4.getParent());
      }

      // $FF: synthetic method
      getFileParent(Object var1) {
         this();
      }
   }

   private static class getFileProper implements Function {
      private getFileProper() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         File var4;
         for(var4 = BridgeUtilities.getFile(var3, var2); !var3.isEmpty(); var4 = new File(var4, var3.pop().toString())) {
         }

         return SleepUtils.getScalar(var4.getAbsolutePath());
      }

      // $FF: synthetic method
      getFileProper(Object var1) {
         this();
      }
   }

   private static class getFileName implements Function {
      private getFileName() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         File var4 = BridgeUtilities.getFile(var3, var2);
         return SleepUtils.getScalar(var4.getName());
      }

      // $FF: synthetic method
      getFileName(Object var1) {
         this();
      }
   }
}
