package org.apache.xmlgraphics.java2d.color;

import java.awt.Color;

public final class DefaultColorConverter implements ColorConverter {
   private static final DefaultColorConverter SINGLETON = new DefaultColorConverter();

   private DefaultColorConverter() {
   }

   public static DefaultColorConverter getInstance() {
      return SINGLETON;
   }

   public Color convert(Color color) {
      return color;
   }
}
