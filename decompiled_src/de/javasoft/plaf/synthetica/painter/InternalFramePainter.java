package de.javasoft.plaf.synthetica.painter;

import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import de.javasoft.util.java2d.DropShadow;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.WeakHashMap;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.plaf.synth.SynthStyle;

public class InternalFramePainter extends SyntheticaComponentPainter {
   public static final String UI_KEY = "Synthetica.InternalFramePainter";
   private static WeakHashMap opaqued = new WeakHashMap();

   protected InternalFramePainter() {
   }

   public static InternalFramePainter getInstance() {
      return getInstance((SynthContext)null);
   }

   public static InternalFramePainter getInstance(SynthContext var0) {
      SyntheticaComponentPainter var1 = (SyntheticaComponentPainter)instances.get(getPainterClassName(var0, InternalFramePainter.class, "Synthetica.InternalFramePainter"));
      if (var1 == null) {
         var1 = getInstance(var0, InternalFramePainter.class, "Synthetica.InternalFramePainter");
      }

      return (InternalFramePainter)var1;
   }

   public void paintInternalFrameTitlePaneBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      BasicInternalFrameTitlePane var7 = (BasicInternalFrameTitlePane)var1.getComponent();
      boolean var8 = (var1.getComponentState() & 512) > 0;
      this.setIcons(var7, var1);
      if (!opaqued.containsKey(var7)) {
         opaqued.put(var7, (Object)null);
         Component[] var9 = var7.getComponents();

         for(int var10 = 0; var10 < var9.length; ++var10) {
            Component var11 = var9[var10];
            if (var11 instanceof JButton) {
               JButton var12 = (JButton)var11;
               var12.setOpaque(false);
            }
         }
      }

      String var15 = "Synthetica.internalFrameTitlePane.background";
      if (var8) {
         var15 = var15 + ".selected";
      }

      var15 = SyntheticaLookAndFeel.getString(var15, var7);
      Insets var16 = SyntheticaLookAndFeel.getInsets("Synthetica.internalFrameTitlePane.background.insets", var7);
      byte var17 = 0;
      if (SyntheticaLookAndFeel.getBoolean("Synthetica.internalFrame.titlePane.background.horizontalTiled", var7)) {
         var17 = 1;
      }

      byte var13 = 0;
      if (SyntheticaLookAndFeel.getBoolean("Synthetica.internalFrame.titlePane.background.verticalTiled", var7)) {
         var13 = 1;
      }

      ImagePainter var14;
      if (var15 != null) {
         var14 = new ImagePainter(var2, var3, var4, var5, var6, var15, var16, var16, var17, var13);
         var14.draw();
      }

      var15 = "Synthetica.internalFrameTitlePane.background.light";
      if (var8) {
         var15 = var15 + ".selected";
      }

      var15 = SyntheticaLookAndFeel.getString(var15, var7);
      if (var15 != null) {
         var14 = new ImagePainter(var2, var3, var4, var5, var6, var15, var16, var16, 0, var13);
         var14.draw();
      }

      this.paintTitle(var1, var2, var5);
   }

   protected void paintTitle(SynthContext var1, Graphics var2, int var3) {
      BasicInternalFrameTitlePane var4 = (BasicInternalFrameTitlePane)var1.getComponent();
      boolean var5 = (var1.getComponentState() & 512) > 0;
      String var6 = this.getTitle(var4.getParent());
      FontMetrics var7 = var4.getFontMetrics(var4.getFont());
      int var8 = var7.getHeight();
      int var9 = var7.stringWidth(var6);
      int var10 = (var4.getSize().height - var8) / 2;
      byte var11 = 4;
      boolean var12 = this.isRTL(var4);
      Rectangle var13 = this.getMenuButtonBounds(var4);
      Rectangle var14 = this.getControlButtonsBounds(var4);
      int var15 = var12 ? var14.x + var14.width + var11 : var13.x + var13.width + var11;
      int var16 = var12 ? var13.x - var11 - var9 : var14.x - var11 - var9;
      var16 = Math.max(var15, var16);
      int var17 = var12 ? var16 : var15;
      var17 = SyntheticaLookAndFeel.getBoolean("Synthetica.internalFrame.titlePane.title.center", var4) ? var3 / 2 - var7.stringWidth(var6) / 2 : var17;
      var17 = var12 ? Math.min(var17, var16) : Math.max(var17, var15);
      var17 = var12 ? Math.max(var17, var15) : Math.min(var17, var16);
      Rectangle var18 = var4.getBounds();
      var18.width -= var13.width + var14.width + var11 * 4;
      var6 = var1.getStyle().getGraphicsUtils(var1).layoutText(var1, var2.getFontMetrics(), var6, (Icon)null, 0, 0, 0, 0, var18, new Rectangle(0, 0), new Rectangle(0, 0), 0);
      if (SyntheticaLookAndFeel.getBoolean("Synthetica.internalFrame.titlePane.dropShadow", var4) && var5 && var9 > 0 && var8 > 0) {
         BufferedImage var19 = new BufferedImage(var9, var8, 2);
         Graphics2D var20 = var19.createGraphics();
         var20.setFont(var2.getFont());
         var20.drawString(var6, 0, var7.getAscent());
         var20.dispose();
         DropShadow var21 = new DropShadow(var19);
         var21.setDistance(SyntheticaLookAndFeel.getInt("Synthetica.internalFrame.titlePane.dropShadow.distance", var4, -5));
         if (SyntheticaLookAndFeel.getColor("Synthetica.internalFrame.titlePane.dropShadow.color", var4) != null) {
            var21.setShadowColor(SyntheticaLookAndFeel.getColor("Synthetica.internalFrame.titlePane.dropShadow.color", var4));
         }

         if (SyntheticaLookAndFeel.get("Synthetica.internalFrame.titlePane.dropShadow.highQuality", (Component)var4) != null) {
            var21.setQuality(SyntheticaLookAndFeel.getBoolean("Synthetica.internalFrame.titlePane.dropShadow.highQuality", var4));
         }

         var21.paintShadow(var2, var17, var10);
      }

      Color var23 = var4.getForeground();
      Color var24 = var23 != null && !(var23 instanceof ColorUIResource) ? var23 : var1.getStyle().getColor(var1, ColorType.FOREGROUND);
      SynthStyle var25 = var1.getStyle();
      if (var4.getParent() instanceof JInternalFrame) {
         JInternalFrame var22 = (JInternalFrame)var4.getParent();
         var25 = SynthLookAndFeel.getStyle(var22, Region.INTERNAL_FRAME_TITLE_PANE);
         var1 = new SynthContext(var22, Region.INTERNAL_FRAME_TITLE_PANE, var25, var1.getComponentState());
      }

      if (SyntheticaLookAndFeel.getBoolean("Synthetica.internalFrame.titlePane.title.etchedTop", var4)) {
         var2.setColor(Color.BLACK);
         var25.getGraphicsUtils(var1).paintText(var1, var2, var6, var17, var10 - 1, -2);
      }

      if (SyntheticaLookAndFeel.getBoolean("Synthetica.internalFrame.titlePane.title.etchedBottom", var4)) {
         var2.setColor(Color.WHITE);
         var25.getGraphicsUtils(var1).paintText(var1, var2, var6, var17, var10 + 1, -2);
      }

      var2.setColor(var24);
      var25.getGraphicsUtils(var1).paintText(var1, var2, var6, var17, var10, -2);
   }

   private String getTitle(Container var1) {
      String var2 = null;
      if (var1 instanceof JInternalFrame) {
         var2 = ((JInternalFrame)var1).getTitle();
      } else if (var1 instanceof JInternalFrame.JDesktopIcon) {
         var2 = ((JInternalFrame.JDesktopIcon)var1).getInternalFrame().getTitle();
      }

      var2 = var2 == null ? "" : var2;
      return var2;
   }

   public void paintInternalFrameTitlePaneBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
   }

   public void paintInternalFrameBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
   }

   public void paintInternalFrameBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      JInternalFrame var7 = (JInternalFrame)var1.getComponent();
      String var8 = "Synthetica.internalFrame.border";
      if (var7.isSelected()) {
         var8 = var8 + ".selected";
      }

      var8 = SyntheticaLookAndFeel.getString(var8, var7);
      Insets var9 = SyntheticaLookAndFeel.getInsets("Synthetica.internalFrame.border.insets", var7);
      ImagePainter var11 = new ImagePainter(var7, var2, var3, var4, var5, var6, var8, var9, var9, 0, 0);
      var11.drawBorder();
   }

   private void setIcons(BasicInternalFrameTitlePane var1, SynthContext var2) {
      String var3 = (String)var1.getClientProperty("Synthetica.MOUSE_OVER");
      boolean var4 = var3 != null;
      String var5 = (String)var1.getClientProperty("Synthetica.MOUSE_PRESSED");
      boolean var6 = var5 != null;
      SynthStyle var7 = var2.getStyle();
      SynthContext var8 = new SynthContext(var1, var2.getRegion(), var7, 2);
      SynthContext var9 = new SynthContext(var1, var2.getRegion(), var7, 4);
      Container var10 = var1.getParent();
      String var11 = "InternalFrameTitlePane.iconifyIcon";
      Icon var12 = var7.getIcon(var2, var11);
      if (var6 && var5.endsWith("iconifyButton")) {
         var12 = var7.getIcon(var9, var11) == var12 ? var7.getIcon(var8, var11) : var7.getIcon(var9, var11);
      } else if (var4 && var3.endsWith("iconifyButton")) {
         var12 = var7.getIcon(var8, var11);
      }

      var11 = "InternalFrameTitlePane.maximizeIcon";
      Icon var13 = var7.getIcon(var2, var11);
      if (var6 && var5.endsWith("maximizeButton")) {
         var13 = var7.getIcon(var9, var11) == var13 ? var7.getIcon(var8, var11) : var7.getIcon(var9, var11);
      } else if (var4 && var3.endsWith("maximizeButton")) {
         var13 = var7.getIcon(var8, var11);
      }

      var11 = "InternalFrameTitlePane.closeIcon";
      Icon var14 = var7.getIcon(var2, var11);
      if (var6 && var5.endsWith("closeButton")) {
         var14 = var7.getIcon(var9, var11) == var14 ? var7.getIcon(var8, var11) : var7.getIcon(var9, var11);
      } else if (var4 && var3.endsWith("closeButton")) {
         var14 = var7.getIcon(var8, var11);
      }

      var11 = "InternalFrameTitlePane.minimizeIcon";
      Icon var15 = var7.getIcon(var2, var11);
      if (!var6 || (!var5.endsWith("maximizeButton") || !(var10 instanceof JInternalFrame)) && (!var5.endsWith("iconifyButton") || !(var10 instanceof JInternalFrame.JDesktopIcon))) {
         if (var4 && (var3.endsWith("maximizeButton") && var10 instanceof JInternalFrame || var3.endsWith("iconifyButton") && var10 instanceof JInternalFrame.JDesktopIcon)) {
            var15 = var7.getIcon(var8, var11);
         }
      } else {
         var15 = var7.getIcon(var9, var11) == var15 ? var7.getIcon(var8, var11) : var7.getIcon(var9, var11);
      }

      JButton var16 = null;
      JButton var17 = null;
      JButton var18 = null;
      Component[] var19 = var1.getComponents();

      for(int var20 = 0; var20 < var19.length; ++var20) {
         Component var21 = var19[var20];
         if (var21 instanceof JButton) {
            String var22 = var21.getName();
            if (var22.endsWith("closeButton")) {
               var18 = (JButton)var21;
            }

            if (var22.endsWith("iconifyButton")) {
               var16 = (JButton)var21;
            }

            if (var22.endsWith("maximizeButton")) {
               var17 = (JButton)var21;
            }
         }
      }

      if (var10 instanceof JInternalFrame) {
         JInternalFrame var23 = (JInternalFrame)var10;
         if (var23.isMaximum()) {
            if (var16 != null) {
               var16.setIcon(var12);
            }

            if (var17 != null) {
               var17.setIcon(var15);
            }
         } else if (var23.isIcon()) {
            if (var16 != null) {
               var16.setIcon(var15);
            }

            if (var17 != null) {
               var17.setIcon(var13);
            }
         } else {
            if (var16 != null) {
               var16.setIcon(var12);
            }

            if (var17 != null) {
               var17.setIcon(var13);
            }
         }
      } else if (var10 instanceof JInternalFrame.JDesktopIcon) {
         if (var16 != null) {
            var16.setIcon(var15);
         }

         if (var17 != null) {
            var17.setIcon(var13);
         }
      }

      if (var18 != null) {
         var18.setIcon(var14);
      }

   }

   protected Rectangle getControlButtonsBounds(BasicInternalFrameTitlePane var1) {
      JButton var2 = null;
      JButton var3 = null;
      JButton var4 = null;
      boolean var5 = this.isRTL(var1);
      Component[] var6 = var1.getComponents();

      for(int var7 = 0; var7 < var6.length; ++var7) {
         Component var8 = var6[var7];
         if (var8 instanceof JButton) {
            String var9 = var8.getName();
            if (var9 != null) {
               if (var9.endsWith("closeButton")) {
                  var4 = (JButton)var8;
               }

               if (var9.endsWith("iconifyButton")) {
                  var2 = (JButton)var8;
               }

               if (var9.endsWith("maximizeButton")) {
                  var3 = (JButton)var8;
               }
            }
         }
      }

      JInternalFrame var10 = null;
      if (var1.getParent() instanceof JInternalFrame) {
         var10 = (JInternalFrame)var1.getParent();
         if (!var10.isClosable() && !var10.isMaximizable() && !var10.isIconifiable()) {
            return new Rectangle(var5 ? 0 : var1.getBounds().width - 1, 0, 0, 0);
         }
      }

      Rectangle var11 = new Rectangle(0, 0, 0, 0);
      if (var4 != null && !var4.getBounds().equals(var11)) {
         var11 = var4.getBounds();
      } else if (var3 != null && !var3.getBounds().equals(var11)) {
         var11 = var3.getBounds();
      } else if (var2 != null && !var2.getBounds().equals(var11)) {
         var11 = var2.getBounds();
      }

      Rectangle var12;
      if ((var10 == null || !var10.isIconifiable()) && (var10 != null || var2 == null)) {
         if (var10 != null && var10.isMaximizable() || var10 == null && var3 != null) {
            var12 = var3.getBounds();
            if (var5) {
               var11.width = var12.x + var12.width - var11.x;
            } else {
               var11.width = var11.x + var11.width - var12.x;
               var11.x = var12.x;
            }
         }
      } else {
         var12 = var2.getBounds();
         if (var5) {
            var11.width = var12.x + var12.width - var11.x;
         } else {
            var11.width = var11.x + var11.width - var12.x;
            var11.x = var12.x;
         }
      }

      return var11;
   }

   protected Rectangle getMenuButtonBounds(BasicInternalFrameTitlePane var1) {
      JButton var2 = (JButton)SyntheticaLookAndFeel.findComponent((String)"InternalFrameTitlePane.menuButton", var1);
      Rectangle var3 = var2 == null ? new Rectangle(0, 0, 0, 0) : var2.getBounds();
      if (var2 == null) {
         var3.x = this.isRTL(var1) ? var1.getBounds().width - 1 : 0;
         var3.y = var1.getBounds().y;
      }

      return var3;
   }

   protected boolean isRTL(BasicInternalFrameTitlePane var1) {
      return !var1.getRootPane().getComponentOrientation().isLeftToRight();
   }

   public Cacheable.ScaleType getCacheScaleType(String var1) {
      return !var1.equals("paintInternalFrameBorder") && !var1.equals("paintInternalFrameBackground") ? super.getCacheScaleType(var1) : Cacheable.ScaleType.NINE_SQUARE;
   }
}
