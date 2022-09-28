package de.javasoft.plaf.synthetica.painter;

import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import de.javasoft.plaf.synthetica.SyntheticaRootPaneUI;
import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.awt.image.VolatileImage;
import java.lang.reflect.Method;
import javax.swing.JComponent;
import javax.swing.JRootPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.plaf.RootPaneUI;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthPainter;

public class SyntheticaPainter extends SynthPainter {
   private static SynthPainter instance;

   public SyntheticaPainter() {
      if (instance == null) {
         instance = this;
      }

   }

   public static SynthPainter getInstance() {
      if (instance == null) {
         instance = new SyntheticaPainter();
      }

      return instance;
   }

   public static void clearCache() {
      SyntheticaSoftCache.getInstance().clear();
   }

   private boolean paintCachedImage(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6, int var7, SyntheticaComponentPainter var8, String var9, Class... var10) {
      boolean var11 = false;
      Cacheable.ScaleType var12 = var8.getCacheScaleType(var9);
      int var18;
      if (UIManager.getBoolean("Synthetica.cache.enabled") && (var18 = var8.getCacheHash(var1, var5, var6, var7, var9)) != -1 && (var12 == Cacheable.ScaleType.NINE_SQUARE || SyntheticaSoftCache.getInstance().isCacheable(var5, var6))) {
         Insets var13 = null;
         int var14 = var5;
         int var15 = var6;
         if (var12 == Cacheable.ScaleType.NINE_SQUARE) {
            var13 = var8.getCacheScaleInsets(var1, var9);
            var14 = var13.left + var13.right + 24;
            var15 = var13.top + var13.bottom + 24;
         }

         VolatileImage var16 = this.getCachedImage(var18, var1, var2, var14, var15, var7, var8, var9, var10);
         if (var16 == null) {
            return false;
         } else if (var12 == Cacheable.ScaleType.NONE) {
            return var2.drawImage(var16, var3, var4, (ImageObserver)null);
         } else if (var12 == Cacheable.ScaleType.NINE_SQUARE) {
            ImagePainter var17 = new ImagePainter(var16, var2, var3, var4, var5, var6, var13, var13);
            if (var9.endsWith("Border")) {
               var17.drawBorder();
            } else {
               var17.draw();
            }

            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private VolatileImage getCachedImage(int var1, SynthContext var2, Graphics var3, int var4, int var5, int var6, SyntheticaComponentPainter var7, String var8, Class... var9) {
      GraphicsConfiguration var10 = ((Graphics2D)var3).getDeviceConfiguration();
      if (var10.getDevice().getType() == 1) {
         return null;
      } else {
         var1 = 31 * var1 + var10.hashCode();
         SyntheticaSoftCache var11 = SyntheticaSoftCache.getInstance();
         VolatileImage var12 = (VolatileImage)var11.getImage(var1);

         do {
            int var13 = var12 == null ? 2 : var12.validate(var10);
            if (var13 == 2 || var13 == 1) {
               if (var13 == 2 || var12.getWidth() != var4 || var12.getHeight() != var5) {
                  if (var12 != null) {
                     var12.flush();
                     var12 = null;
                  }

                  var12 = var10.createCompatibleVolatileImage(var4, var5, 3);
                  var11.setImage(var12, var1);
               }

               Graphics2D var14 = var12.createGraphics();
               var14.setComposite(AlphaComposite.Clear);
               var14.fillRect(0, 0, var4, var5);
               var14.setComposite(AlphaComposite.SrcOver);

               try {
                  Method var15 = var7.getClass().getMethod(var8, var9);
                  if (var6 == -1) {
                     var15.invoke(var7, var2, var14, 0, 0, var4, var5);
                  } else {
                     var15.invoke(var7, var2, var14, 0, 0, var4, var5, var6);
                  }
               } catch (Exception var19) {
                  throw new RuntimeException(var19);
               } finally {
                  var14.dispose();
               }
            }
         } while(var12.contentsLost());

         return var12;
      }
   }

   public void paintArrowButtonBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, -1, ArrowButtonPainter.getInstance(var1), "paintArrowButtonBackground", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         ArrowButtonPainter.getInstance(var1).paintArrowButtonBackground(var1, var2, var3, var4, var5, var6);
      }

   }

   public void paintArrowButtonBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, -1, ArrowButtonPainter.getInstance(var1), "paintArrowButtonBorder", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         ArrowButtonPainter.getInstance(var1).paintArrowButtonBorder(var1, var2, var3, var4, var5, var6);
      }

   }

   public void paintArrowButtonForeground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6, int var7) {
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, var7, ArrowButtonPainter.getInstance(var1), "paintArrowButtonForeground", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         ArrowButtonPainter.getInstance(var1).paintArrowButtonForeground(var1, var2, var3, var4, var5, var6, var7);
      }

   }

   public void paintButtonBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, -1, ButtonPainter.getInstance(var1), "paintButtonBackground", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         ButtonPainter.getInstance(var1).paintButtonBackground(var1, var2, var3, var4, var5, var6);
      }

   }

   public void paintButtonBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, -1, ButtonPainter.getInstance(var1), "paintButtonBorder", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         ButtonPainter.getInstance(var1).paintButtonBorder(var1, var2, var3, var4, var5, var6);
      }

   }

   public void paintCheckBoxBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      CheckBoxPainter.getInstance(var1).paintCheckBoxBackground(var1, var2, var3, var4, var5, var6);
   }

   public void paintCheckBoxBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      CheckBoxPainter.getInstance(var1).paintCheckBoxBorder(var1, var2, var3, var4, var5, var6);
   }

   public void paintComboBoxBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, -1, ComboBoxPainter.getInstance(var1), "paintComboBoxBackground", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         ComboBoxPainter.getInstance(var1).paintComboBoxBackground(var1, var2, var3, var4, var5, var6);
      }

   }

   public void paintComboBoxBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, -1, ComboBoxPainter.getInstance(var1), "paintComboBoxBorder", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         ComboBoxPainter.getInstance(var1).paintComboBoxBorder(var1, var2, var3, var4, var5, var6);
      }

   }

   public void paintDesktopPaneBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      DesktopPanePainter.getInstance(var1).paintDesktopPaneBackground(var1, var2, var3, var4, var5, var6);
   }

   public void paintDesktopPaneBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      DesktopPanePainter.getInstance(var1).paintDesktopPaneBorder(var1, var2, var3, var4, var5, var6);
   }

   public void paintEditorPaneBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, -1, EditorPanePainter.getInstance(var1), "paintEditorPaneBackground", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         EditorPanePainter.getInstance(var1).paintEditorPaneBackground(var1, var2, var3, var4, var5, var6);
      }

   }

   public void paintEditorPaneBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      EditorPanePainter.getInstance(var1).paintEditorPaneBorder(var1, var2, var3, var4, var5, var6);
   }

   public void paintFormattedTextFieldBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, -1, FormattedTextFieldPainter.getInstance(var1), "paintFormattedTextFieldBackground", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         FormattedTextFieldPainter.getInstance(var1).paintFormattedTextFieldBackground(var1, var2, var3, var4, var5, var6);
      }

   }

   public void paintFormattedTextFieldBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, -1, FormattedTextFieldPainter.getInstance(var1), "paintFormattedTextFieldBorder", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         FormattedTextFieldPainter.getInstance(var1).paintFormattedTextFieldBorder(var1, var2, var3, var4, var5, var6);
      }

   }

   public void paintInternalFrameBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      InternalFramePainter.getInstance(var1).paintInternalFrameBackground(var1, var2, var3, var4, var5, var6);
   }

   public void paintInternalFrameBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, -1, InternalFramePainter.getInstance(var1), "paintInternalFrameBorder", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         InternalFramePainter.getInstance(var1).paintInternalFrameBorder(var1, var2, var3, var4, var5, var6);
      }

   }

   public void paintInternalFrameTitlePaneBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      InternalFramePainter.getInstance(var1).paintInternalFrameTitlePaneBackground(var1, var2, var3, var4, var5, var6);
   }

   public void paintInternalFrameTitlePaneBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, -1, InternalFramePainter.getInstance(var1), "paintInternalFrameTitlePaneBorder", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         InternalFramePainter.getInstance(var1).paintInternalFrameTitlePaneBorder(var1, var2, var3, var4, var5, var6);
      }

   }

   public void paintLabelBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      LabelPainter.getInstance(var1).paintLabelBackground(var1, var2, var3, var4, var5, var6);
   }

   public void paintLabelBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      LabelPainter.getInstance(var1).paintLabelBorder(var1, var2, var3, var4, var5, var6);
   }

   public void paintListBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      ListPainter.getInstance(var1).paintListBackground(var1, var2, var3, var4, var5, var6);
   }

   public void paintListBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      ListPainter.getInstance(var1).paintListBorder(var1, var2, var3, var4, var5, var6);
   }

   public void paintMenuBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      MenuPainter.getInstance(var1).paintMenuBackground(var1, var2, var3, var4, var5, var6);
   }

   public void paintMenuBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      MenuPainter.getInstance(var1).paintMenuBorder(var1, var2, var3, var4, var5, var6);
   }

   public void paintMenuBarBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, -1, MenuPainter.getInstance(var1), "paintMenuBarBackground", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         MenuPainter.getInstance(var1).paintMenuBarBackground(var1, var2, var3, var4, var5, var6);
      }

   }

   public void paintMenuBarBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, -1, MenuPainter.getInstance(var1), "paintMenuBarBorder", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         MenuPainter.getInstance(var1).paintMenuBarBorder(var1, var2, var3, var4, var5, var6);
      }

   }

   public void paintMenuItemBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      MenuPainter.getInstance(var1).paintMenuItemBackground(var1, var2, var3, var4, var5, var6);
   }

   public void paintMenuItemBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      MenuPainter.getInstance(var1).paintMenuItemBorder(var1, var2, var3, var4, var5, var6);
   }

   public void paintRadioButtonMenuItemBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      MenuPainter.getInstance(var1).paintRadioButtonMenuItemBackground(var1, var2, var3, var4, var5, var6);
   }

   public void paintRadioButtonMenuItemBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      MenuPainter.getInstance(var1).paintRadioButtonMenuItemBorder(var1, var2, var3, var4, var5, var6);
   }

   public void paintCheckBoxMenuItemBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      MenuPainter.getInstance(var1).paintCheckBoxMenuItemBackground(var1, var2, var3, var4, var5, var6);
   }

   public void paintCheckBoxMenuItemBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      MenuPainter.getInstance(var1).paintCheckBoxMenuItemBorder(var1, var2, var3, var4, var5, var6);
   }

   public void paintOptionPaneBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      OptionPanePainter.getInstance(var1).paintOptionPaneBackground(var1, var2, var3, var4, var5, var6);
   }

   public void paintOptionPaneBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      OptionPanePainter.getInstance(var1).paintOptionPaneBorder(var1, var2, var3, var4, var5, var6);
   }

   public void paintPopupMenuBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      MenuPainter.getInstance(var1).paintPopupMenuBackground(var1, var2, var3, var4, var5, var6);
   }

   public void paintPopupMenuBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      MenuPainter.getInstance(var1).paintPopupMenuBorder(var1, var2, var3, var4, var5, var6);
   }

   public void paintPanelBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      PanelPainter.getInstance(var1).paintPanelBackground(var1, var2, var3, var4, var5, var6);
   }

   public void paintPanelBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      PanelPainter.getInstance(var1).paintPanelBorder(var1, var2, var3, var4, var5, var6);
   }

   public void paintPasswordFieldBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, -1, PasswordFieldPainter.getInstance(var1), "paintPasswordFieldBackground", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         PasswordFieldPainter.getInstance(var1).paintPasswordFieldBackground(var1, var2, var3, var4, var5, var6);
      }

   }

   public void paintPasswordFieldBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, -1, PasswordFieldPainter.getInstance(var1), "paintPasswordFieldBorder", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         PasswordFieldPainter.getInstance(var1).paintPasswordFieldBorder(var1, var2, var3, var4, var5, var6);
      }

   }

   public void paintProgressBarForeground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6, int var7) {
      ProgressBarPainter.getInstance(var1).paintProgressBarForeground(var1, var2, var3, var4, var5, var6, var7);
   }

   public void paintProgressBarBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, -1, ProgressBarPainter.getInstance(var1), "paintProgressBarBackground", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         ProgressBarPainter.getInstance(var1).paintProgressBarBackground(var1, var2, var3, var4, var5, var6);
      }

   }

   public void paintProgressBarBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, -1, ProgressBarPainter.getInstance(var1), "paintProgressBarBorder", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         ProgressBarPainter.getInstance(var1).paintProgressBarBorder(var1, var2, var3, var4, var5, var6);
      }

   }

   public void paintRadioButtonBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      RadioButtonPainter.getInstance(var1).paintRadioButtonBackground(var1, var2, var3, var4, var5, var6);
   }

   public void paintRadioButtonBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      RadioButtonPainter.getInstance(var1).paintRadioButtonBorder(var1, var2, var3, var4, var5, var6);
   }

   public void paintRootPaneBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
   }

   public void paintRootPaneBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, -1, RootPanePainter.getInstance(var1), "paintRootPaneBorder", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         RootPanePainter.getInstance(var1).paintRootPaneBorder(var1, var2, var3, var4, var5, var6);
      }

   }

   public void paintRootPaneTitlePaneBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, -1, RootPanePainter.getInstance(var1), "paintTitlePaneBackground", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         RootPanePainter.getInstance(var1).paintTitlePaneBackground(var1, var2, var3, var4, var5, var6);
      }

   }

   public void paintRootPaneButtonAreaBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, -1, RootPanePainter.getInstance(var1), "paintButtonAreaBackground", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         RootPanePainter.getInstance(var1).paintButtonAreaBackground(var1, var2, var3, var4, var5, var6);
      }

   }

   public void paintScrollBarBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, -1, ScrollBarPainter.getInstance(var1), "paintScrollBarBackground", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         ScrollBarPainter.getInstance(var1).paintScrollBarBackground(var1, var2, var3, var4, var5, var6);
      }

   }

   public void paintScrollBarBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, -1, ScrollBarPainter.getInstance(var1), "paintScrollBarBorder", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         ScrollBarPainter.getInstance(var1).paintScrollBarBorder(var1, var2, var3, var4, var5, var6);
      }

   }

   public void paintScrollBarThumbBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6, int var7) {
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, var7, ScrollBarPainter.getInstance(var1), "paintScrollBarThumbBackground", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         ScrollBarPainter.getInstance(var1).paintScrollBarThumbBackground(var1, var2, var3, var4, var5, var6, var7);
      }

   }

   public void paintScrollBarThumbBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6, int var7) {
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, var7, ScrollBarPainter.getInstance(var1), "paintScrollBarThumbBorder", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         ScrollBarPainter.getInstance(var1).paintScrollBarThumbBorder(var1, var2, var3, var4, var5, var6, var7);
      }

   }

   public void paintScrollBarTrackBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, -1, ScrollBarPainter.getInstance(var1), "paintScrollBarTrackBackground", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         ScrollBarPainter.getInstance(var1).paintScrollBarTrackBackground(var1, var2, var3, var4, var5, var6);
      }

   }

   public void paintScrollBarTrackBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, -1, ScrollBarPainter.getInstance(var1), "paintScrollBarTrackBorder", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         ScrollBarPainter.getInstance(var1).paintScrollBarTrackBorder(var1, var2, var3, var4, var5, var6);
      }

   }

   public void paintScrollPaneBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, -1, ScrollPanePainter.getInstance(var1), "paintScrollPaneBackground", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         ScrollPanePainter.getInstance(var1).paintScrollPaneBackground(var1, var2, var3, var4, var5, var6);
      }

   }

   public void paintScrollPaneBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, -1, ScrollPanePainter.getInstance(var1), "paintScrollPaneBorder", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         ScrollPanePainter.getInstance(var1).paintScrollPaneBorder(var1, var2, var3, var4, var5, var6);
      }

   }

   public void paintSeparatorBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      SeparatorPainter.getInstance(var1).paintSeparatorBackground(var1, var2, var3, var4, var5, var6);
   }

   public void paintSeparatorBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      SeparatorPainter.getInstance(var1).paintSeparatorBorder(var1, var2, var3, var4, var5, var6);
   }

   public void paintSeparatorForeground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6, int var7) {
      SeparatorPainter.getInstance(var1).paintSeparatorForeground(var1, var2, var3, var4, var5, var6, var7);
   }

   public void paintSliderBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      SliderPainter.getInstance(var1).paintSliderBackground(var1, var2, var3, var4, var5, var6);
   }

   public void paintSliderBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, -1, SliderPainter.getInstance(var1), "paintSliderBorder", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         SliderPainter.getInstance(var1).paintSliderBorder(var1, var2, var3, var4, var5, var6);
      }

   }

   public void paintSliderTrackBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, -1, SliderPainter.getInstance(var1), "paintSliderTrackBackground", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         SliderPainter.getInstance(var1).paintSliderTrackBackground(var1, var2, var3, var4, var5, var6);
      }

   }

   public void paintSliderTrackBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, -1, SliderPainter.getInstance(var1), "paintSliderTrackBorder", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         SliderPainter.getInstance(var1).paintSliderTrackBorder(var1, var2, var3, var4, var5, var6);
      }

   }

   public void paintSliderThumbBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6, int var7) {
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, var7, SliderPainter.getInstance(var1), "paintSliderThumbBackground", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         SliderPainter.getInstance(var1).paintSliderThumbBackground(var1, var2, var3, var4, var5, var6, var7);
      }

   }

   public void paintSliderThumbBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6, int var7) {
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, var7, SliderPainter.getInstance(var1), "paintSliderThumbBorder", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         SliderPainter.getInstance(var1).paintSliderThumbBorder(var1, var2, var3, var4, var5, var6, var7);
      }

   }

   public void paintSpinnerBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      SyntheticaLookAndFeel.setChildrenOpaque(var1.getComponent(), false);
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, -1, SpinnerPainter.getInstance(var1), "paintSpinnerBackground", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         SpinnerPainter.getInstance(var1).paintSpinnerBackground(var1, var2, var3, var4, var5, var6);
      }

   }

   public void paintSpinnerBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, -1, SpinnerPainter.getInstance(var1), "paintSpinnerBorder", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         SpinnerPainter.getInstance(var1).paintSpinnerBorder(var1, var2, var3, var4, var5, var6);
      }

   }

   public void paintSplitPaneBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      SplitPanePainter.getInstance(var1).paintSplitPaneBackground(var1, var2, var3, var4, var5, var6);
   }

   public void paintSplitPaneBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      SplitPanePainter.getInstance(var1).paintSplitPaneBorder(var1, var2, var3, var4, var5, var6);
   }

   public void paintSplitPaneDividerForeground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6, int var7) {
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, var7, SplitPanePainter.getInstance(var1), "paintSplitPaneDividerForeground", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         SplitPanePainter.getInstance(var1).paintSplitPaneDividerForeground(var1, var2, var3, var4, var5, var6, var7);
      }

   }

   public void paintSplitPaneDividerBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, -1, SplitPanePainter.getInstance(var1), "paintSplitPaneDividerBackground", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         SplitPanePainter.getInstance(var1).paintSplitPaneDividerBackground(var1, var2, var3, var4, var5, var6);
      }

   }

   public void paintSplitPaneDragDivider(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6, int var7) {
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, var7, SplitPanePainter.getInstance(var1), "paintSplitPaneDragDivider", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         SplitPanePainter.getInstance(var1).paintSplitPaneDragDivider(var1, var2, var3, var4, var5, var6, var7);
      }

   }

   public void paintTabbedPaneBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      TabbedPanePainter.getInstance(var1).paintTabbedPaneBackground(var1, var2, var3, var4, var5, var6);
   }

   public void paintTabbedPaneBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      TabbedPanePainter.getInstance(var1).paintTabbedPaneBorder(var1, var2, var3, var4, var5, var6);
   }

   public void paintTabbedPaneContentBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      TabbedPanePainter.getInstance(var1).paintTabbedPaneContentBackground(var1, var2, var3, var4, var5, var6);
   }

   public void paintTabbedPaneContentBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      TabbedPanePainter.getInstance(var1).paintTabbedPaneContentBorder(var1, var2, var3, var4, var5, var6);
   }

   public void paintTabbedPaneTabAreaBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      Rectangle var7 = this.tabAreaBoundsCorretion(var1, var3, var4, var5, var6);
      var3 = var7.x;
      var4 = var7.y;
      var5 = var7.width;
      var6 = var7.height;
      TabbedPanePainter.getInstance(var1).paintTabbedPaneTabAreaBackground(var1, var2, var3, var4, var5, var6);
   }

   public void paintTabbedPaneTabAreaBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      Rectangle var7 = this.tabAreaBoundsCorretion(var1, var3, var4, var5, var6);
      var3 = var7.x;
      var4 = var7.y;
      var5 = var7.width;
      var6 = var7.height;
      TabbedPanePainter.getInstance(var1).paintTabbedPaneTabAreaBorder(var1, var2, var3, var4, var5, var6);
   }

   private Rectangle tabAreaBoundsCorretion(SynthContext var1, int var2, int var3, int var4, int var5) {
      JTabbedPane var6 = (JTabbedPane)var1.getComponent();
      if (var6.getTabLayoutPolicy() == 1) {
         Insets var7 = var6.getInsets();
         Insets var8 = var1.getStyle().getInsets(var1, (Insets)null);
         int var9 = var6.getTabCount();
         int var10;
         if (var6.getTabPlacement() != 2 && var6.getTabPlacement() != 4) {
            var2 -= var7.left;
            var3 -= var7.top;
            var4 = var8.left + var8.right;

            for(var10 = 0; var10 < var9; ++var10) {
               var4 += var6.getBoundsAt(var10).width;
            }

            var4 = Math.max(var4, var6.getWidth());
            var5 = (var9 > 0 ? var6.getBoundsAt(0).height : 0) + var8.top + var8.bottom;
         } else {
            var2 -= var7.top;
            var3 -= var7.left;
            var4 = (var9 > 0 ? var6.getBoundsAt(0).width : 0) + var8.top + var8.bottom;
            var5 = var8.left + var8.right;

            for(var10 = 0; var10 < var9; ++var10) {
               var5 += var6.getBoundsAt(var10).height;
            }

            var5 = Math.max(var5, var6.getHeight());
         }
      }

      return new Rectangle(var2, var3, var4, var5);
   }

   public void paintTabbedPaneTabBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6, int var7) {
      var1.getComponent().putClientProperty("Synthetica.tabbedPane.tabIndex", var7);
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, var7, TabbedPanePainter.getInstance(var1), "paintTabbedPaneTabBackground", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         TabbedPanePainter.getInstance(var1).paintTabbedPaneTabBackground(var1, var2, var3, var4, var5, var6, var7);
      }

   }

   public void paintTabbedPaneTabBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6, int var7) {
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, var7, TabbedPanePainter.getInstance(var1), "paintTabbedPaneTabBorder", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         TabbedPanePainter.getInstance(var1).paintTabbedPaneTabBorder(var1, var2, var3, var4, var5, var6, var7);
      }

   }

   public void paintTableBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      TablePainter.getInstance(var1).paintTableBackground(var1, var2, var3, var4, var5, var6);
   }

   public void paintTableBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      TablePainter.getInstance(var1).paintTableBorder(var1, var2, var3, var4, var5, var6);
   }

   public void paintTableHeaderBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, -1, TablePainter.getInstance(var1), "paintTableHeaderBackground", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         TablePainter.getInstance(var1).paintTableHeaderBackground(var1, var2, var3, var4, var5, var6);
      }

   }

   public void paintTableHeaderBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      TablePainter.getInstance(var1).paintTableHeaderBorder(var1, var2, var3, var4, var5, var6);
   }

   public void paintTextAreaBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, -1, TextAreaPainter.getInstance(var1), "paintTextAreaBackground", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         TextAreaPainter.getInstance(var1).paintTextAreaBackground(var1, var2, var3, var4, var5, var6);
      }

   }

   public void paintTextAreaBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      TextAreaPainter.getInstance(var1).paintTextAreaBorder(var1, var2, var3, var4, var5, var6);
   }

   public void paintTextFieldBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, -1, TextFieldPainter.getInstance(var1), "paintTextFieldBackground", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         TextFieldPainter.getInstance(var1).paintTextFieldBackground(var1, var2, var3, var4, var5, var6);
      }

   }

   public void paintTextFieldBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, -1, TextFieldPainter.getInstance(var1), "paintTextFieldBorder", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         TextFieldPainter.getInstance(var1).paintTextFieldBorder(var1, var2, var3, var4, var5, var6);
      }

   }

   public void paintTextPaneBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, -1, TextPanePainter.getInstance(var1), "paintTextPaneBackground", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         TextPanePainter.getInstance(var1).paintTextPaneBackground(var1, var2, var3, var4, var5, var6);
      }

   }

   public void paintTextPaneBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      TextPanePainter.getInstance(var1).paintTextPaneBorder(var1, var2, var3, var4, var5, var6);
   }

   public void paintToggleButtonBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, -1, ToggleButtonPainter.getInstance(var1), "paintToggleButtonBackground", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         ToggleButtonPainter.getInstance(var1).paintToggleButtonBackground(var1, var2, var3, var4, var5, var6);
      }

   }

   public void paintToggleButtonBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, -1, ToggleButtonPainter.getInstance(var1), "paintToggleButtonBorder", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         ToggleButtonPainter.getInstance(var1).paintToggleButtonBorder(var1, var2, var3, var4, var5, var6);
      }

   }

   public void paintToolBarBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      JComponent var7 = var1.getComponent();
      JRootPane var8 = var7.getRootPane();
      RootPaneUI var9 = var8.getUI();
      if (var9 instanceof SyntheticaRootPaneUI && ((SyntheticaRootPaneUI)var9).isHeaderShadowEnabled()) {
         Insets var10 = var8.getInsets();
         boolean var11 = (Boolean)SyntheticaLookAndFeel.getClientProperty("Synthetica.headerShadowSupport", var7, true);
         if (var11 && var5 == var8.getWidth() - var10.left - var10.right && var7 != ((SyntheticaRootPaneUI)var9).getHeaderShadowComponent() && var7.getLocationOnScreen().y + var7.getHeight() < var8.getLocationOnScreen().y + var8.getHeight() - var10.bottom) {
            ((SyntheticaRootPaneUI)var9).setHeaderShadowComponent(var7);
         }
      }

      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, -1, ToolBarPainter.getInstance(var1), "paintToolBarBackground", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         ToolBarPainter.getInstance(var1).paintToolBarBackground(var1, var2, var3, var4, var5, var6);
      }

   }

   public void paintToolBarBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, -1, ToolBarPainter.getInstance(var1), "paintToolBarBorder", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         ToolBarPainter.getInstance(var1).paintToolBarBorder(var1, var2, var3, var4, var5, var6);
      }

   }

   public void paintToolBarContentBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, -1, ToolBarPainter.getInstance(var1), "paintToolBarContentBackground", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         ToolBarPainter.getInstance(var1).paintToolBarContentBackground(var1, var2, var3, var4, var5, var6);
      }

   }

   public void paintToolBarContentBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, -1, ToolBarPainter.getInstance(var1), "paintToolBarContentBorder", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         ToolBarPainter.getInstance(var1).paintToolBarContentBorder(var1, var2, var3, var4, var5, var6);
      }

   }

   public void paintToolTipBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, -1, ToolTipPainter.getInstance(var1), "paintToolTipBackground", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         ToolTipPainter.getInstance(var1).paintToolTipBackground(var1, var2, var3, var4, var5, var6);
      }

   }

   public void paintToolTipBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6, -1, ToolTipPainter.getInstance(var1), "paintToolTipBorder", SynthContext.class, Graphics.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)) {
         ToolTipPainter.getInstance(var1).paintToolTipBorder(var1, var2, var3, var4, var5, var6);
      }

   }

   public void paintTreeBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      TreePainter.getInstance(var1).paintTreeBackground(var1, var2, var3, var4, var5, var6);
   }

   public void paintTreeBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      TreePainter.getInstance(var1).paintTreeBorder(var1, var2, var3, var4, var5, var6);
   }

   public void paintTreeCellBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      TreePainter.getInstance(var1).paintTreeCellBackground(var1, var2, var3, var4, var5, var6);
   }

   public void paintTreeCellBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      TreePainter.getInstance(var1).paintTreeCellBorder(var1, var2, var3, var4, var5, var6);
   }

   public void paintTreeCellFocus(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
   }

   public void paintViewportBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      ViewportPainter.getInstance(var1).paintViewportBackground(var1, var2, var3, var4, var5, var6);
   }

   public void paintViewportBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      ViewportPainter.getInstance(var1).paintViewportBorder(var1, var2, var3, var4, var5, var6);
   }
}
