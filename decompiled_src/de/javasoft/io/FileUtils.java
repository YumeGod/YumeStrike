package de.javasoft.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {
   public static boolean delete(File var0, boolean var1, FileOperationListener var2) {
      boolean var4 = false;
      File[] var3;
      if (var0.isDirectory() && (var3 = var0.listFiles()) != null) {
         for(int var5 = 0; var5 < var3.length; ++var5) {
            if (var3[var5].isDirectory() && var1) {
               var4 = !delete(var3[var5], var1, var2);
               if (var4) {
                  return !var4;
               }
            } else {
               if (var2 != null) {
                  var4 = !var2.processFileOperationEvent(new FileOperationEvent(FileUtils.class, var3[var5], 3));
               }

               if (var4) {
                  return !var4;
               }

               var3[var5].delete();
            }
         }
      }

      if (var2 != null) {
         var4 = !var2.processFileOperationEvent(new FileOperationEvent(FileUtils.class, var0, 3));
      }

      if (var4) {
         return !var4;
      } else {
         var0.delete();
         return !var4;
      }
   }

   public static boolean copy(File var0, File var1, boolean var2, boolean var3, FileOperationListener var4) throws FileNotFoundException, IOException {
      boolean var5 = false;
      if (var4 != null) {
         var5 = !var4.processFileOperationEvent(new FileOperationEvent(FileUtils.class, var0, 2));
      }

      if (var5) {
         return !var5;
      } else {
         FileInputStream var6 = new FileInputStream(var0);
         FileOutputStream var7 = new FileOutputStream(var1);
         byte[] var8 = new byte[65536];
         boolean var9 = false;

         int var10;
         while((var10 = var6.read(var8)) != -1) {
            var7.write(var8, 0, var10);
         }

         var7.close();
         var6.close();
         if (var2) {
            var1.setLastModified(var0.lastModified());
         }

         if (var3) {
            var5 = !var4.processFileOperationEvent(new FileOperationEvent(FileUtils.class, var0, 3));
            if (var5) {
               return !var5;
            }

            var0.delete();
         }

         return !var5;
      }
   }

   public static boolean copy(File var0, File var1, boolean var2, boolean var3, boolean var4, FileOperationListener var5) throws IOException {
      boolean var6 = false;
      if (!var0.exists()) {
         throw new IOException("Source directory not found: " + var0.getAbsolutePath());
      } else {
         File[] var7;
         if (var0.isDirectory() && (var7 = var0.listFiles()) != null) {
            if (!var1.exists()) {
               if (var5 != null) {
                  var6 = !var5.processFileOperationEvent(new FileOperationEvent(FileUtils.class, var1, 1));
               }

               if (var6) {
                  return !var6;
               }

               var1.mkdir();
            }

            for(int var8 = 0; var8 < var7.length; ++var8) {
               File var9;
               if (var7[var8].isDirectory() && var2) {
                  var9 = new File(var1, var7[var8].getName());
                  var6 = !copy(var7[var8], var9, var2, var3, var4, var5);
                  if (var6) {
                     return !var6;
                  }
               } else if (var7[var8].isFile()) {
                  var9 = new File(var1, var7[var8].getName());
                  if (var3 || !var9.exists() || var9.lastModified() < var0.lastModified()) {
                     var6 = !copy(var7[var8], var9, true, var4, var5);
                     if (var6) {
                        return !var6;
                     }
                  }
               }
            }
         } else if (var3 || !var1.exists() || var1.lastModified() < var0.lastModified()) {
            var6 = !copy(var0, var1, true, var4, var5);
            if (var6) {
               return !var6;
            }
         }

         if (var0.isDirectory() && var2 && var4) {
            var6 = !delete(var0, false, var5);
         }

         return !var6;
      }
   }

   public static boolean determineProperties(FileProperties var0, File var1, boolean var2, FileOperationListener var3) throws IOException {
      boolean var4 = false;
      if (!var1.exists()) {
         throw new IOException("File not found: " + var1.getAbsolutePath());
      } else {
         if (var0.location == null) {
            var0.location = var1.getParent();
         }

         File[] var5;
         if (var1.isDirectory() && var2 && (var5 = var1.listFiles()) != null && var5.length > 0) {
            ++var0.directories;

            for(int var6 = 0; var6 < var5.length; ++var6) {
               if (var5[var6].isDirectory() && var2) {
                  var4 = !determineProperties(var0, var5[var6], var2, var3);
                  if (var4) {
                     return !var4;
                  }
               } else if (var5[var6].isFile()) {
                  ++var0.files;
                  var0.size += var5[var6].length();
                  if (var3 != null) {
                     var4 = !var3.processFileOperationEvent(new FileOperationEvent(var0, var1, 4));
                  }

                  if (var4) {
                     return !var4;
                  }
               }
            }
         } else {
            if (var1.isDirectory()) {
               ++var0.directories;
            } else {
               ++var0.files;
               var0.size += var1.length();
            }

            var0.lastModified = var1.lastModified();
            if (var3 != null) {
               var4 = !var3.processFileOperationEvent(new FileOperationEvent(var0, var1, 4));
            }
         }

         return !var4;
      }
   }
}
