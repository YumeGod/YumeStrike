package org.apache.fop.render.awt.viewer;

import java.util.Locale;
import java.util.ResourceBundle;

public class Translator {
   private ResourceBundle bundle;
   private static String bundleBaseName = "org/apache/fop/render/awt/viewer/resources/Viewer";

   public Translator() {
      this(Locale.getDefault());
   }

   public Translator(Locale locale) {
      this.bundle = ResourceBundle.getBundle(bundleBaseName, locale);
   }

   public String getString(String key) {
      return this.bundle.getString(key);
   }
}
