package org.apache.batik.apps.svgbrowser;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class SquiggleInputHandlerFilter extends FileFilter {
   protected SquiggleInputHandler handler;

   public SquiggleInputHandlerFilter(SquiggleInputHandler var1) {
      this.handler = var1;
   }

   public boolean accept(File var1) {
      return var1.isDirectory() || this.handler.accept(var1);
   }

   public String getDescription() {
      StringBuffer var1 = new StringBuffer();
      String[] var2 = this.handler.getHandledExtensions();
      int var3 = var2 != null ? var2.length : 0;

      for(int var4 = 0; var4 < var3; ++var4) {
         if (var4 > 0) {
            var1.append(", ");
         }

         var1.append(var2[var4]);
      }

      if (var3 > 0) {
         var1.append(' ');
      }

      var1.append(this.handler.getDescription());
      return var1.toString();
   }
}
