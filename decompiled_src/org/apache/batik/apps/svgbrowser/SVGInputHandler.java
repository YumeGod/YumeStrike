package org.apache.batik.apps.svgbrowser;

import java.io.File;
import org.apache.batik.util.ParsedURL;

public class SVGInputHandler implements SquiggleInputHandler {
   public static final String[] SVG_MIME_TYPES = new String[]{"image/svg+xml"};
   public static final String[] SVG_FILE_EXTENSIONS = new String[]{".svg", ".svgz"};

   public String[] getHandledMimeTypes() {
      return SVG_MIME_TYPES;
   }

   public String[] getHandledExtensions() {
      return SVG_FILE_EXTENSIONS;
   }

   public String getDescription() {
      return "";
   }

   public void handle(ParsedURL var1, JSVGViewerFrame var2) {
      var2.getJSVGCanvas().loadSVGDocument(var1.toString());
   }

   public boolean accept(File var1) {
      return var1 != null && var1.isFile() && this.accept(var1.getPath());
   }

   public boolean accept(ParsedURL var1) {
      if (var1 == null) {
         return false;
      } else {
         String var2 = var1.getPath();
         return var2 == null ? false : this.accept(var2);
      }
   }

   public boolean accept(String var1) {
      if (var1 == null) {
         return false;
      } else {
         for(int var2 = 0; var2 < SVG_FILE_EXTENSIONS.length; ++var2) {
            if (var1.endsWith(SVG_FILE_EXTENSIONS[var2])) {
               return true;
            }
         }

         return false;
      }
   }
}
