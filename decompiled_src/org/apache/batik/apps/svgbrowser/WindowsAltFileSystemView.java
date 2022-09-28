package org.apache.batik.apps.svgbrowser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.filechooser.FileSystemView;

class WindowsAltFileSystemView extends FileSystemView {
   public static final String EXCEPTION_CONTAINING_DIR_NULL = "AltFileSystemView.exception.containing.dir.null";
   public static final String EXCEPTION_DIRECTORY_ALREADY_EXISTS = "AltFileSystemView.exception.directory.already.exists";
   public static final String NEW_FOLDER_NAME = " AltFileSystemView.new.folder.name";
   public static final String FLOPPY_DRIVE = "AltFileSystemView.floppy.drive";

   public boolean isRoot(File var1) {
      if (!var1.isAbsolute()) {
         return false;
      } else {
         String var2 = var1.getParent();
         if (var2 == null) {
            return true;
         } else {
            File var3 = new File(var2);
            return var3.equals(var1);
         }
      }
   }

   public File createNewFolder(File var1) throws IOException {
      if (var1 == null) {
         throw new IOException(Resources.getString("AltFileSystemView.exception.containing.dir.null"));
      } else {
         File var2 = null;
         var2 = this.createFileObject(var1, Resources.getString(" AltFileSystemView.new.folder.name"));

         for(int var3 = 2; var2.exists() && var3 < 100; ++var3) {
            var2 = this.createFileObject(var1, Resources.getString(" AltFileSystemView.new.folder.name") + " (" + var3 + ')');
         }

         if (var2.exists()) {
            throw new IOException(Resources.formatMessage("AltFileSystemView.exception.directory.already.exists", new Object[]{var2.getAbsolutePath()}));
         } else {
            var2.mkdirs();
            return var2;
         }
      }
   }

   public boolean isHiddenFile(File var1) {
      return false;
   }

   public File[] getRoots() {
      ArrayList var1 = new ArrayList();
      FileSystemRoot var2 = new FileSystemRoot(Resources.getString("AltFileSystemView.floppy.drive") + "\\");
      var1.add(var2);

      for(char var3 = 'C'; var3 <= 'Z'; ++var3) {
         char[] var4 = new char[]{var3, ':', '\\'};
         String var5 = new String(var4);
         FileSystemRoot var6 = new FileSystemRoot(var5);
         if (var6 != null && var6.exists()) {
            var1.add(var6);
         }
      }

      File[] var7 = new File[var1.size()];
      var1.toArray(var7);
      return var7;
   }

   class FileSystemRoot extends File {
      public FileSystemRoot(File var2) {
         super(var2, "");
      }

      public FileSystemRoot(String var2) {
         super(var2);
      }

      public boolean isDirectory() {
         return true;
      }
   }
}
