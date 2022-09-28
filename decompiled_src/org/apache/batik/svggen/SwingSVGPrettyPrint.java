package org.apache.batik.svggen;

import java.awt.Component;
import java.awt.Rectangle;
import javax.swing.AbstractButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollBar;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import org.w3c.dom.Element;

public abstract class SwingSVGPrettyPrint implements SVGSyntax {
   public static void print(JComponent var0, SVGGraphics2D var1) {
      if (!(var0 instanceof JComboBox) && !(var0 instanceof JScrollBar)) {
         SVGGraphics2D var2 = (SVGGraphics2D)var1.create();
         var2.setColor(var0.getForeground());
         var2.setFont(var0.getFont());
         Element var3 = var2.getTopLevelGroup();
         if (var0.getWidth() > 0 && var0.getHeight() > 0) {
            Rectangle var4 = var2.getClipBounds();
            if (var4 == null) {
               var2.setClip(0, 0, var0.getWidth(), var0.getHeight());
            }

            paintComponent(var0, var2);
            paintBorder(var0, var2);
            paintChildren(var0, var2);
            Element var5 = var2.getTopLevelGroup();
            var5.setAttributeNS((String)null, "id", var1.getGeneratorContext().idGenerator.generateID(var0.getClass().getName()));
            var3.appendChild(var5);
            var1.setTopLevelGroup(var3);
         }
      } else {
         printHack(var0, var1);
      }
   }

   private static void printHack(JComponent var0, SVGGraphics2D var1) {
      SVGGraphics2D var2 = (SVGGraphics2D)var1.create();
      var2.setColor(var0.getForeground());
      var2.setFont(var0.getFont());
      Element var3 = var2.getTopLevelGroup();
      if (var0.getWidth() > 0 && var0.getHeight() > 0) {
         Rectangle var4 = var2.getClipBounds();
         if (var4 == null) {
            var2.setClip(0, 0, var0.getWidth(), var0.getHeight());
         }

         var0.paint(var2);
         Element var5 = var2.getTopLevelGroup();
         var5.setAttributeNS((String)null, "id", var1.getGeneratorContext().idGenerator.generateID(var0.getClass().getName()));
         var3.appendChild(var5);
         var1.setTopLevelGroup(var3);
      }
   }

   private static void paintComponent(JComponent var0, SVGGraphics2D var1) {
      ComponentUI var2 = UIManager.getUI(var0);
      if (var2 != null) {
         var2.installUI(var0);
         var2.update(var1, var0);
      }

   }

   private static void paintBorder(JComponent var0, SVGGraphics2D var1) {
      Border var2 = var0.getBorder();
      if (var2 != null) {
         if (!(var0 instanceof AbstractButton) && !(var0 instanceof JPopupMenu) && !(var0 instanceof JToolBar) && !(var0 instanceof JMenuBar) && !(var0 instanceof JProgressBar)) {
            var2.paintBorder(var0, var1, 0, 0, var0.getWidth(), var0.getHeight());
         } else if (var0 instanceof AbstractButton && ((AbstractButton)var0).isBorderPainted() || var0 instanceof JPopupMenu && ((JPopupMenu)var0).isBorderPainted() || var0 instanceof JToolBar && ((JToolBar)var0).isBorderPainted() || var0 instanceof JMenuBar && ((JMenuBar)var0).isBorderPainted() || var0 instanceof JProgressBar && ((JProgressBar)var0).isBorderPainted()) {
            var2.paintBorder(var0, var1, 0, 0, var0.getWidth(), var0.getHeight());
         }
      }

   }

   private static void paintChildren(JComponent var0, SVGGraphics2D var1) {
      int var2 = var0.getComponentCount() - 1;

      for(Rectangle var3 = new Rectangle(); var2 >= 0; --var2) {
         Component var4 = var0.getComponent(var2);
         if (var4 != null && JComponent.isLightweightComponent(var4) && var4.isVisible()) {
            Rectangle var5 = null;
            boolean var6 = var4 instanceof JComponent;
            if (var6) {
               var5 = var3;
               ((JComponent)var4).getBounds(var3);
            } else {
               var5 = var4.getBounds();
            }

            boolean var7 = var1.hitClip(var5.x, var5.y, var5.width, var5.height);
            if (var7) {
               SVGGraphics2D var8 = (SVGGraphics2D)var1.create(var5.x, var5.y, var5.width, var5.height);
               var8.setColor(var4.getForeground());
               var8.setFont(var4.getFont());
               if (var4 instanceof JComponent) {
                  print((JComponent)var4, var8);
               } else {
                  var4.paint(var8);
               }
            }
         }
      }

   }
}
