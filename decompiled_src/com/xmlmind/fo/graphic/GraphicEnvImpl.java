package com.xmlmind.fo.graphic;

import com.xmlmind.fo.util.TempFile;
import java.io.File;
import java.util.Enumeration;
import java.util.Vector;

public class GraphicEnvImpl implements GraphicEnv {
   protected Vector tempFiles = new Vector();

   public File createTempFile(String var1) throws Exception {
      File var2 = TempFile.create((String)null, var1);
      this.tempFiles.addElement(var2);
      return var2;
   }

   public void reportWarning(String var1) {
      System.err.println(var1);
   }

   public void dispose() {
      Enumeration var1 = this.tempFiles.elements();

      while(var1.hasMoreElements()) {
         File var2 = (File)var1.nextElement();
         var2.delete();
      }

   }
}
