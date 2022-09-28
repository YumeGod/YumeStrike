package de.javasoft.plaf.synthetica.painter;

import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import de.javasoft.plaf.synthetica.SyntheticaState;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.text.JTextComponent;

public class ScrollPanePainter extends SyntheticaComponentPainter {
   public static final String UI_KEY = "Synthetica.ScrollPanePainter";

   protected ScrollPanePainter() {
   }

   public static ScrollPanePainter getInstance() {
      return getInstance((SynthContext)null);
   }

   public static ScrollPanePainter getInstance(SynthContext var0) {
      SyntheticaComponentPainter var1 = (SyntheticaComponentPainter)instances.get(getPainterClassName(var0, ScrollPanePainter.class, "Synthetica.ScrollPanePainter"));
      if (var1 == null) {
         var1 = getInstance(var0, ScrollPanePainter.class, "Synthetica.ScrollPanePainter");
      }

      return (ScrollPanePainter)var1;
   }

   public void paintScrollPaneBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      JScrollPane var7 = (JScrollPane)var1.getComponent();
      if (var7.getViewport() != null && var7.getViewport().getView() != null && SyntheticaLookAndFeel.isOpaque(var7)) {
         Component var8 = var7.getViewport().getView();
         Border var9 = var7.getBorder();
         Border var10 = SyntheticaLookAndFeel.findDefaultBorder(var9);
         boolean var11 = var10 == null ? false : var10.getClass().getName().equals("javax.swing.plaf.synth.SynthBorder");
         boolean var12 = var8 instanceof JTextComponent;
         if (!var12 || SyntheticaLookAndFeel.isOpaque((JTextComponent)var8)) {
            if (!var12 && !var11) {
               if (var8.isOpaque()) {
                  this.fillBackground(var7, var9, var10, var11, var8, var2, var8.getBackground(), var3, var4, var5, var6);
               }

            } else {
               boolean var13 = var9 instanceof CompoundBorder && ((CompoundBorder)var9).getInsideBorder() == var10;
               if (var13) {
                  Insets var14 = var9.getBorderInsets(var7);
                  Insets var15 = var10.getBorderInsets(var7);
                  var3 += var14.left - var15.left;
                  var4 += var14.top - var15.top;
                  var5 -= var14.left - var15.left + var14.right - var15.right;
                  var6 -= var14.top - var15.top + var14.bottom - var15.bottom;
               }

               boolean var16;
               Color var34;
               if (!var12) {
                  Color var33 = var7.getBackground();
                  if (var33 instanceof UIResource) {
                     var33 = var8.getBackground();
                  }

                  var34 = var2.getColor();
                  if (var34 == null || var34 != null && !var34.equals(var33)) {
                     var16 = (!(var8 instanceof JTable) || SyntheticaLookAndFeel.getBoolean("Synthetica.scrollPane.table.fillBackground", var8, true)) && (!(var8 instanceof JList) || SyntheticaLookAndFeel.getBoolean("Synthetica.scrollPane.list.fillBackground", var8, true)) && (!(var8 instanceof JTree) || SyntheticaLookAndFeel.getBoolean("Synthetica.scrollPane.tree.fillBackground", var8, true)) && (!(var8 instanceof JPanel) || SyntheticaLookAndFeel.getBoolean("Synthetica.scrollPane.panel.fillBackground", var8, true));
                     if (!var11) {
                        if (var16) {
                           this.fillBackground(var7, (Border)null, var10, var11, var8, var2, var33, var3, var4, var5, var6);
                        }

                        return;
                     }

                     if (var16) {
                        this.fillBackground(var7, var9, var10, var11, var8, var2, var33, var3, var4, var5, var6);
                     }
                  }

                  String var35 = null;
                  if (var8.isEnabled()) {
                     var35 = SyntheticaLookAndFeel.getString("Synthetica.scrollPane.border", var7);
                  } else {
                     var35 = SyntheticaLookAndFeel.getString("Synthetica.scrollPane.border.disabled", var7);
                  }

                  Insets var36 = SyntheticaLookAndFeel.getInsets("Synthetica.scrollPane.border.insets", var7);
                  ImagePainter var37 = new ImagePainter(var2, var3, var4, var5, var6, var35, var36, var36, 0, 0);
                  var37.drawBorder();
               } else {
                  JTextComponent var32 = (JTextComponent)var8;
                  var34 = var32.getBackground();
                  var16 = var34 == null || var34 instanceof ColorUIResource;
                  boolean var17 = SyntheticaLookAndFeel.getBoolean("Synthetica.scrollPane.keepLockedBorderIfColored", var32);
                  boolean var18 = var32.isEnabled();
                  boolean var19 = !var32.isEditable();
                  String var20 = "textField";
                  if (var32 instanceof JTextArea) {
                     var20 = "textArea";
                  } else if (var32 instanceof JEditorPane) {
                     var20 = "editorPane";
                  } else if (var32 instanceof JTextPane) {
                     var20 = "textPane";
                  }

                  int var21 = var19 ? SyntheticaState.State.LOCKED.toInt() : 0;
                  var21 |= !var18 ? SyntheticaState.State.DISABLED.toInt() : 0;
                  SyntheticaPainterState var22 = new SyntheticaPainterState(var1, var21, false);
                  if (!var16 && !var17) {
                     var22.resetState(SyntheticaState.State.LOCKED);
                  }

                  UIKey var23 = new UIKey(var20 + ".border", var22);
                  Boolean var24 = (Boolean)SyntheticaLookAndFeel.get(var23.get("fillBackground"), (Component)var32);
                  if (var24 == null || var24) {
                     this.fillBackground(var7, var9, var10, var11, var32, var2, var34, var3, var4, var5, var6);
                  }

                  boolean var25 = SyntheticaLookAndFeel.get(var23.get(), (Component)var32) != null;
                  if (var19 && !var25) {
                     var22.resetState(SyntheticaState.State.LOCKED);
                     var23 = new UIKey(var20 + ".border", var22);
                  }

                  var25 = SyntheticaLookAndFeel.get(var23.get(), (Component)var32) != null;
                  if (!var25) {
                     var23 = new UIKey("scrollPane.border", var22);
                  }

                  boolean var26 = SyntheticaLookAndFeel.getBoolean(var23.get("solid"), var32);
                  String var27 = SyntheticaLookAndFeel.getString(var23.get(), var32);
                  Insets var28 = SyntheticaLookAndFeel.getInsets(var23.get("insets"), var32);
                  Insets var29 = var28 == null ? SyntheticaLookAndFeel.getInsets("Synthetica.scrollPane.border.insets", var32) : var28;
                  Insets var30 = var29;
                  if (!var11) {
                     Insets var31 = var7.getInsets();
                     var3 += var31.left;
                     var4 += var31.top;
                     var5 += -var31.left - var31.right;
                     var6 += -var31.top - var31.bottom;
                     var30 = new Insets(0, 0, 0, 0);
                  }

                  ImagePainter var38 = new ImagePainter(var32, var2, var3, var4, var5, var6, var27, var29, var30, 0, 0);
                  if (var26) {
                     var38.draw();
                  } else {
                     var38.drawBorder();
                  }

               }
            }
         }
      }
   }

   public void paintScrollPaneBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      JScrollPane var7 = (JScrollPane)var1.getComponent();
      if (var7.getViewport() != null && var7.getViewport().getView() != null) {
         Component var8 = var7.getViewport().getView();
         boolean var9 = SyntheticaLookAndFeel.getBoolean("Synthetica.focus.textComponents.enabled", var8, true);
         boolean var10 = SyntheticaLookAndFeel.getBoolean("Synthetica.focus.scrollPane.enabled", var8, true);
         if (var8.hasFocus() && (!(var8 instanceof JTextComponent) && var10 || var8 instanceof JTextComponent && var9)) {
            FocusPainter.paintFocus("focus.scrollPane", var1, var2, var3, var4, var5, var6);
         }

      }
   }

   private void fillBackground(JScrollPane var1, Border var2, Border var3, boolean var4, Component var5, Graphics var6, Color var7, int var8, int var9, int var10, int var11) {
      Color var13;
      if (var7 instanceof ColorUIResource && var5 instanceof JTextComponent) {
         JTextComponent var12 = (JTextComponent)var5;
         if (var12.isEnabled() && (Boolean)SyntheticaLookAndFeel.getClientProperty("Synthetica.MOUSE_OVER", var12, false)) {
            Region var21 = var12 instanceof JTextArea ? Region.TEXT_AREA : (var12 instanceof JTextPane ? Region.TEXT_PANE : Region.EDITOR_PANE);
            SynthStyle var14 = SynthLookAndFeel.getStyle(var12, var21);
            SynthContext var15 = new SynthContext(var12, var21, var14, 2);
            var7 = var15.getStyle().getColor(var15, ColorType.BACKGROUND);
         } else if (!var12.isEditable() || !var12.isEnabled()) {
            if (!var12.isEditable()) {
               var13 = null;
               if (var12 instanceof JTextArea) {
                  var13 = SyntheticaLookAndFeel.getColor("Synthetica.textArea.lockedColor", var12);
               } else if (var12 instanceof JTextPane) {
                  var13 = SyntheticaLookAndFeel.getColor("Synthetica.textPane.lockedColor", var12);
               } else if (var12 instanceof JEditorPane) {
                  var13 = SyntheticaLookAndFeel.getColor("Synthetica.editorPane.lockedColor", var12);
               }

               if (var13 == null && !var12.isEnabled()) {
                  var13 = this.getDisabledBackgroundColor(var12);
               }

               if (var13 != null) {
                  var7 = var13;
               }
            } else if (!var12.isEnabled()) {
               var7 = this.getDisabledBackgroundColor(var12);
            }
         }
      }

      Graphics2D var19 = (Graphics2D)var6;
      var13 = var19.getColor();
      var19.setColor(var7);
      Insets var20 = var1.getInsets();
      if (var2 != null && !var20.equals(new Insets(0, 0, 0, 0))) {
         if (!var4) {
            var6.fillRect(var8 + var20.left, var9 + var20.top, var10 - var20.left - var20.right, var11 - var20.top - var20.bottom);
         } else {
            RenderingHints var22 = var19.getRenderingHints();
            var19.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Insets var16 = SyntheticaLookAndFeel.getInsets("Synthetica.scrollPane.border.fillInsets", var1, false);
            int var17 = SyntheticaLookAndFeel.getInt("Synthetica.scrollPane.border.arcWidth", var1, 8);
            int var18 = SyntheticaLookAndFeel.getInt("Synthetica.scrollPane.border.arcHeight", var1, 8);
            var19.fillRoundRect(var8 + var16.left, var9 + var16.top, var10 - var16.left - var16.right, var11 - var16.top - var16.bottom, var17, var18);
            var19.setRenderingHints(var22);
         }
      } else {
         var19.fillRect(var8, var9, var10, var11);
      }

      var19.setColor(var13);
   }

   private Color getDisabledBackgroundColor(JTextComponent var1) {
      Color var2 = null;
      if (var1 instanceof JTextArea) {
         var2 = SyntheticaLookAndFeel.getColor("Synthetica.textArea.disabledColor", var1);
      } else if (var1 instanceof JTextPane) {
         var2 = SyntheticaLookAndFeel.getColor("Synthetica.textPane.disabledColor", var1);
      } else if (var1 instanceof JEditorPane) {
         var2 = SyntheticaLookAndFeel.getColor("Synthetica.editorPane.disabledColor", var1);
      }

      return var2;
   }

   public void paintScrollPaneCorner(JScrollPane var1, Graphics var2, int var3, int var4, int var5, int var6, String var7) {
   }

   public Cacheable.ScaleType getCacheScaleType(String var1) {
      return Cacheable.ScaleType.NINE_SQUARE;
   }

   public int getCacheHash(SynthContext var1, int var2, int var3, int var4, String var5) {
      JScrollPane var6 = (JScrollPane)var1.getComponent();
      JComponent var7 = (JComponent)var6.getViewport().getView();
      int var8 = super.getCacheHash(var1, var2, var3, var4, var5);
      if (var7 != null) {
         boolean var9 = var7.isEnabled();
         boolean var10 = var7.hasFocus();
         boolean var11 = var7 instanceof JTextComponent ? !((JTextComponent)var7).isEditable() : false;
         Color var12 = var7.getBackground();
         Boolean var13 = (Boolean)var7.getClientProperty("Synthetica.opaque");
         var8 = 31 * var8 + var7.getClass().hashCode();
         var8 = var12 == null ? var8 : 31 * var8 + var12.getRGB();
         var8 = var13 == null ? var8 : 31 * var8 + (var13 ? 0 : 1);
         var8 = 31 * var8 + (var9 ? 0 : 1);
         var8 = 31 * var8 + (var10 ? 0 : 1);
         var8 = 31 * var8 + (var11 ? 0 : 1);
      }

      return var8;
   }

   public Insets getCacheScaleInsets(SynthContext var1, String var2) {
      if (var2.equals("paintScrollPaneBorder")) {
         Insets var3 = SyntheticaLookAndFeel.getInsets("Synthetica.scrollPane.border.insets", var1.getComponent());
         if (var3 != null) {
            return var3;
         }
      }

      return super.getCacheScaleInsets(var1, var2);
   }
}
