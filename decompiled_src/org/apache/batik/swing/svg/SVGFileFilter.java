package org.apache.batik.swing.svg;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class SVGFileFilter extends FileFilter {
   public boolean accept(File var1) {
      boolean var2 = false;
      String var3 = null;
      if (var1 != null) {
         if (var1.isDirectory()) {
            var2 = true;
         } else {
            var3 = var1.getPath().toLowerCase();
            if (var3.endsWith(".svg") || var3.endsWith(".svgz")) {
               var2 = true;
            }
         }
      }

      return var2;
   }

   public String getDescription() {
      return ".svg, .svgz";
   }
}
