package com.xmlmind.fo.converter.docx;

import com.xmlmind.fo.converter.ErrorHandler;
import com.xmlmind.fo.graphic.GraphicEnv;
import com.xmlmind.fo.util.TempFile;
import java.io.File;
import java.util.Vector;

final class TmpGraphicEnv implements GraphicEnv {
   public final Vector tempFiles;
   public final ErrorHandler errorHandler;

   public TmpGraphicEnv(Vector var1, ErrorHandler var2) {
      this.tempFiles = var1;
      this.errorHandler = var2;
   }

   public File createTempFile(String var1) throws Exception {
      File var2 = TempFile.create((String)null, var1);
      this.tempFiles.addElement(var2);
      return var2;
   }

   public void reportWarning(String var1) {
      try {
         this.errorHandler.warning(new Exception(var1));
      } catch (Exception var3) {
      }

   }
}
