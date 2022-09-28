package de.javasoft.io;

import java.io.File;
import java.util.EventObject;

public class FileOperationEvent extends EventObject {
   private static final long serialVersionUID = -7123971027594023024L;
   public static final int CREATE = 1;
   public static final int COPY = 2;
   public static final int DELETE = 3;
   public static final int PROPERTIES = 4;
   private File file;
   private int fileOperation;

   public FileOperationEvent(Object var1, File var2, int var3) {
      super(var1);
      this.file = var2;
      this.fileOperation = var3;
   }

   public File getFile() {
      return this.file;
   }

   public int getOperation() {
      return this.fileOperation;
   }
}
