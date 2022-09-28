package org.apache.batik.swing;

import java.awt.Image;
import java.beans.SimpleBeanInfo;

public class JSVGCanvasBeanInfo extends SimpleBeanInfo {
   protected Image iconColor16x16 = this.loadImage("resources/batikColor16x16.gif");
   protected Image iconMono16x16 = this.loadImage("resources/batikMono16x16.gif");
   protected Image iconColor32x32 = this.loadImage("resources/batikColor32x32.gif");
   protected Image iconMono32x32 = this.loadImage("resources/batikMono32x32.gif");

   public Image getIcon(int var1) {
      switch (var1) {
         case 1:
            return this.iconColor16x16;
         case 2:
            return this.iconColor32x32;
         case 3:
            return this.iconMono16x16;
         case 4:
            return this.iconMono32x32;
         default:
            return null;
      }
   }
}
