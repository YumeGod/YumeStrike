package de.javasoft.plaf.synthetica.styles;

import de.javasoft.plaf.synthetica.GraphicsUtils;
import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import de.javasoft.util.java2d.Synthetica2DUtils;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Window;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.SwingUtilities;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthStyle;

public class MenuStyle extends StyleWrapper {
   private static MenuStyle instance = new MenuStyle();

   private MenuStyle() {
   }

   public static SynthStyle getStyle(SynthStyle var0, JComponent var1, Region var2) {
      var1.setOpaque(false);
      if (SyntheticaLookAndFeel.getStyleName(var1) == null) {
         instance.setStyle(var0);
         return instance;
      } else {
         MenuStyle var3 = new MenuStyle();
         var3.setStyle(var0);
         return var3;
      }
   }

   public Font getFont(SynthContext var1) {
      MenuItemStyle.menuItemHtmlColorCorrection(var1);
      Font var2 = this.synthStyle.getFont(var1);
      JMenu var3 = (JMenu)var1.getComponent();
      Object var4 = SyntheticaLookAndFeel.get("Synthetica.menu.toplevel.fontSize", (Component)var3);
      if (var4 != null && var3.isTopLevelMenu()) {
         float var5 = SyntheticaLookAndFeel.scaleFontSize(new Float(var4.toString()));
         var2 = var2.deriveFont(var5);
      }

      return var2;
   }

   public Color getColor(SynthContext var1, ColorType var2) {
      JMenu var3 = (JMenu)var1.getComponent();
      Boolean var4 = (Boolean)var3.getClientProperty("Synthetica.MOUSE_OVER");
      boolean var5 = var4 == null ? false : var4;
      boolean var6 = (var1.getComponentState() & 512) > 0;
      boolean var7 = (var1.getComponentState() & 1) > 0;
      if (var3.isTopLevelMenu() && var2.equals(ColorType.TEXT_FOREGROUND)) {
         String var8 = "Synthetica.menu.toplevel.textColor";
         Window var9 = SwingUtilities.getWindowAncestor(var3);
         if (!var9.isActive() && SyntheticaLookAndFeel.getBoolean("Synthetica.window.decoration", var9) && SyntheticaLookAndFeel.get(var8 + ".inactive", (Component)var9) != null && !var6 && !var5) {
            var8 = var8 + ".inactive";
         } else if (!var7) {
            var8 = var8 + ".disabled";
         } else if (var6 || var5) {
            var8 = var8 + ".selected";
         }

         Color var10 = SyntheticaLookAndFeel.getColor(var8, var3);
         if (var10 != null) {
            return var10;
         }
      }

      return this.synthStyle.getColor(var1, var2);
   }

   public Icon getIcon(SynthContext var1, Object var2) {
      Object var3 = super.getIcon(var1, var2);
      if ("Menu.arrowIcon".equals(var2) && !var1.getComponent().getComponentOrientation().isLeftToRight()) {
         var3 = new ImageIcon(Synthetica2DUtils.flipHorizontal(GraphicsUtils.iconToImage(var1, (Icon)var3)));
      }

      return (Icon)var3;
   }
}
