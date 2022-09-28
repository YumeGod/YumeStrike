package de.javasoft.plaf.synthetica;

import de.javasoft.plaf.synthetica.painter.ImagePainter;
import de.javasoft.plaf.synthetica.painter.TabbedPanePainter;
import de.javasoft.util.JavaVersion;
import de.javasoft.util.java2d.DropShadow;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.CellRendererPane;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthGraphicsUtils;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.plaf.synth.SynthStyleFactory;

public class GraphicsUtils extends SynthGraphicsUtils {
   public void paintText(SynthContext var1, Graphics var2, String var3, int var4, int var5, int var6) {
      JComponent var7 = var1.getComponent();
      Region var8 = var1.getRegion();
      String var9;
      Insets var10;
      FontMetrics var12;
      int var13;
      int var14;
      ImagePainter var15;
      if (var8 != Region.BUTTON && var8 != Region.TOGGLE_BUTTON) {
         if (var8 == Region.TABBED_PANE_TAB) {
            JTabbedPane var16 = (JTabbedPane)var7;
            int var18 = var16.getSelectedIndex();
            int var11 = (Integer)SyntheticaLookAndFeel.getClientProperty("Synthetica.tabbedPane.tabIndex", var16, -1);
            Color var20 = var11 >= 0 ? var16.getForegroundAt(var11) : null;
            if (var20 != null && !(var20 instanceof UIResource)) {
               var2.setColor(var20);
            }

            if (var18 != var11 && SyntheticaLookAndFeel.getBoolean("Synthetica.tabbedPane.tab.selected.bold", var16)) {
               var13 = var2.getFontMetrics().stringWidth(var3);
               var2.setFont(var2.getFont().deriveFont(0));
               var14 = var2.getFontMetrics().stringWidth(var3);
               var4 += (var13 - var14) / 2;
            }

            Object var21 = SyntheticaLookAndFeel.get("Synthetica.TabbedPanePainter", (Component)var7);
            if (var21 instanceof TabbedPanePainter) {
               TabbedPanePainter var22 = (TabbedPanePainter)var21;
               if (!var22.paintTabbedPaneTabText(var1, var2, var3, var4, var5, var6)) {
                  return;
               }
            }
         } else if (var8 == Region.PROGRESS_BAR) {
            var9 = (String)SyntheticaLookAndFeel.get("Synthetica.progressBar.textBackground", (Component)var7);
            if (var9 != null && var9.length() > 0 && var3 != null && var3.trim().length() > 0) {
               var10 = (Insets)SyntheticaLookAndFeel.get("Synthetica.progressBar.textBackground.insets", (Component)var7);
               var12 = var2.getFontMetrics();
               var13 = var12.getHeight();
               var14 = var12.stringWidth(var3);
               var15 = new ImagePainter(var2, var4 - 2, var5, var14 + 4, var13, var9, var10, var10, 0, 0);
               var15.draw();
            }
         } else if (var8 == Region.INTERNAL_FRAME_TITLE_PANE) {
            if (!JavaVersion.JAVA5 && var6 == -1) {
               return;
            }
         } else if ((var8 == Region.MENU || var8 == Region.MENU_ITEM || var8 == Region.CHECK_BOX_MENU_ITEM || var8 == Region.RADIO_BUTTON_MENU_ITEM) && !this.getPaintMenuMnemonics()) {
            var6 = -1;
         } else if (var8 == Region.MENU_ITEM_ACCELERATOR && UIManager.get("MenuItem.acceleratorDelimiter") != null) {
            var3 = var3.replace("+", UIManager.getString("MenuItem.acceleratorDelimiter"));
         }
      } else {
         if (!SyntheticaLookAndFeel.getBoolean("Button.showMnemonics", var7, this.getPaintButtonMnemonics())) {
            var6 = -1;
         }

         var9 = (String)SyntheticaLookAndFeel.get("Synthetica.button.textBackground", (Component)var7);
         if (var9 != null && var9.length() > 0 && var3 != null && var3.trim().length() > 0) {
            var10 = (Insets)SyntheticaLookAndFeel.get("Synthetica.button.textBackground.insets", (Component)var7);
            var12 = var2.getFontMetrics();
            var13 = var12.getHeight();
            var14 = var12.stringWidth(var3);
            var15 = new ImagePainter(var2, var4 - 2, var5, var14 + 4, var13, var9, var10, var10, 0, 0);
            var15.draw();
         }
      }

      if (!SyntheticaLookAndFeel.getAntiAliasEnabled() && !SyntheticaLookAndFeel.getBoolean("Synthetica.text.antialias", var7)) {
         super.paintText(var1, var2, var3, var4, var5, var6);
      } else {
         Graphics2D var17 = (Graphics2D)var2;
         Object var19 = var17.getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING);
         if (JavaVersion.JAVA5) {
            var17.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
         } else {
            var17.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
         }

         super.paintText(var1, var17, var3, var4, var5, var6);
         var17.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, var19);
      }

   }

   public void paintText(SynthContext var1, Graphics var2, String var3, Icon var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11) {
      if (!SyntheticaLookAndFeel.getAntiAliasEnabled() && !SyntheticaLookAndFeel.getBoolean("Synthetica.text.antialias", var1.getComponent())) {
         super.paintText(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11);
      } else {
         Graphics2D var12 = (Graphics2D)var2;
         Object var13 = var12.getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING);
         if (JavaVersion.JAVA5) {
            var12.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
         } else {
            var12.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
         }

         super.paintText(var1, var12, var3, var4, var5, var6, var7, var8, var9, var10, var11);
         var12.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, var13);
      }

   }

   /** @deprecated */
   protected boolean getPaintMnemonics() {
      return this.getPaintMenuMnemonics();
   }

   protected boolean getPaintMenuMnemonics() {
      return ExtKeyEventProcessor.showMnemonicsOnAltKeyOnly() ? ExtKeyEventProcessor.isMenuActive() : true;
   }

   protected boolean getPaintButtonMnemonics() {
      return ExtKeyEventProcessor.showMnemonicsOnAltKeyOnly() ? ExtKeyEventProcessor.isAltKeyPressed() : true;
   }

   public void drawLine(SynthContext var1, Object var2, Graphics var3, int var4, int var5, int var6, int var7) {
      JComponent var8 = var1.getComponent();
      if (var2 instanceof String && ((String)var2).startsWith("Tree.")) {
         Color var9 = null;
         if (var2.equals("Tree.horizontalLine")) {
            var9 = SyntheticaLookAndFeel.getColor("Synthetica.tree.line.color.horizontal", var8);
         } else if (var2.equals("Tree.verticalLine")) {
            var9 = SyntheticaLookAndFeel.getColor("Synthetica.tree.line.color.vertical", var8);
         }

         if (var9 != null) {
            var3.setColor(var9);
         }

         String var10 = SyntheticaLookAndFeel.getString("Synthetica.tree.line.type", var8);
         if ("SOLID".equals(var10)) {
            var3.drawLine(var4, var5, var6, var7);
         } else if ("DASHED".equals(var10)) {
            int var11 = SyntheticaLookAndFeel.getInt("Synthetica.tree.line.dashed.line", var8);
            if (var11 <= 0) {
               var11 = 2;
            }

            int var12 = SyntheticaLookAndFeel.getInt("Synthetica.tree.line.dashed.space", var8);
            if (var12 <= 0) {
               var12 = 1;
            }

            int var13;
            if (var2.equals("Tree.horizontalLine")) {
               for(var13 = var4; var13 < var6; var13 += var11 + var12) {
                  var3.drawLine(var13, var5, Math.min(var13 + var11 - 1, var6), var7);
               }
            } else {
               for(var13 = var5; var13 < var7; var13 += var11 + var12) {
                  var3.drawLine(var4, var13, var4, Math.min(var13 + var11 - 1, var7));
               }
            }
         }

      } else if (var4 != var6 || !(var2 instanceof String) || !((String)var2).equals("Table.grid") || SyntheticaLookAndFeel.getInt("Synthetica.table.columnMargin", var1.getComponent(), 1) != 0) {
         if (var5 != var7 || !(var2 instanceof String) || !((String)var2).equals("Table.grid") || SyntheticaLookAndFeel.getInt("Synthetica.table.rowMargin", var1.getComponent(), 1) != 0) {
            super.drawLine(var1, var2, var3, var4, var5, var6, var7);
         }
      }
   }

   public String layoutText(SynthContext var1, FontMetrics var2, String var3, Icon var4, int var5, int var6, int var7, int var8, Rectangle var9, Rectangle var10, Rectangle var11, int var12) {
      JComponent var13 = var1.getComponent();
      if (var13 instanceof JTabbedPane) {
         if (SyntheticaLookAndFeel.getBoolean("Synthetica.tabbedPane.tab.text.position.leading", var13)) {
            var7 = 10;
         } else {
            var7 = 11;
         }

         SynthStyleFactory var14 = SynthLookAndFeel.getStyleFactory();
         if (var14 != null) {
            SynthStyle var15 = var14.getStyle(var1.getComponent(), Region.TABBED_PANE);
            if (var15.get(var1, "TabbedPane.textIconGap") == null) {
               var12 = 4;
            }
         }
      }

      String var16 = super.layoutText(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12);
      if (var13 instanceof JTabbedPane && ((JTabbedPane)var13).getTabCount() > 0) {
         this.tabbedPaneTabCorrection(var1, (JTabbedPane)var13, var2, var3, var4, var9, var11, var10);
      }

      return var16;
   }

   private void tabbedPaneTabCorrection(SynthContext var1, JTabbedPane var2, FontMetrics var3, String var4, Icon var5, Rectangle var6, Rectangle var7, Rectangle var8) {
      int var9 = var2.getSelectedIndex();
      int var10 = (Integer)SyntheticaLookAndFeel.getClientProperty("Synthetica.tabbedPane.tabIndex", var2, -1);
      boolean var11 = var9 == var10;
      int var12 = var2.getTabPlacement();
      if (var10 >= 0 && var2.getIconAt(var10) != null) {
         int var13 = var3.getHeight();
         int var14 = Math.max(var13, var2.getIconAt(var10).getIconHeight());
         var7.y += (var13 - var14) / 2;
      }

      String var16 = "tabbedPane.tab";
      if (var11) {
         var16 = var16 + ".selected";
      }

      if (var12 == 1) {
         var16 = var16 + ".top";
      } else if (var12 == 2) {
         var16 = var16 + ".left";
      } else if (var12 == 3) {
         var16 = var16 + ".bottom";
      } else if (var12 == 4) {
         var16 = var16 + ".right";
      }

      String var17 = SyntheticaLookAndFeel.getStyleName(var2);
      Insets var15 = SyntheticaLookAndFeel.getInsets(var16, "text.insets", var17, true);
      if (var15 != null) {
         var7.x += var15.left;
         switch (var2.getTabPlacement()) {
            case 1:
               var7.y += var15.top;
            case 2:
            default:
               break;
            case 3:
               var7.y -= var15.bottom;
         }
      }

      if (var5 != null) {
         var15 = SyntheticaLookAndFeel.getInsets(var16, "icon.insets", var17, true);
         if (var15 == null) {
            var15 = new Insets(0, 0, 0, 0);
         }

         var8.x += var15.left;
         if (var2.getTabPlacement() == 1) {
            var8.y += var15.top;
         } else if (var2.getTabPlacement() == 3) {
            var8.y -= var15.bottom;
         }
      }

   }

   public static Image iconToImage(SynthContext var0, Icon var1) {
      BufferedImage var2 = new BufferedImage(var1.getIconWidth(), var1.getIconHeight(), 2);
      Graphics var3 = var2.getGraphics();
      JComponent var4 = var0 == null ? null : var0.getComponent();
      var1.paintIcon(var4, var3, 0, 0);
      var3.dispose();
      return var2;
   }

   public void paintListCellRendererSelectionBackground(JList var1, Component var2, Graphics var3, int var4, int var5, int var6, int var7) {
      boolean var8 = this.cellRendererRespectsFocus(var2) ? var1.hasFocus() : true;
      String var9 = SyntheticaLookAndFeel.getString(var8 ? "Synthetica.list.selectionBackground" : "Synthetica.list.selectionBackground.inactive", var2);
      if (var9 != null) {
         Insets var10 = SyntheticaLookAndFeel.getInsets("Synthetica.list.selectionBackground.insets", var2, false);
         ImagePainter var11 = new ImagePainter(var3, var4, var5, var6, var7, var9, var10, var10, 0, 0);
         var11.draw();
      }

   }

   public void paintTreeCellRendererSelectionBackground(JTree var1, Component var2, Graphics var3, int var4, int var5, int var6, int var7) {
      boolean var8 = true;
      if (this.cellRendererRespectsFocus(var2)) {
         if (var1.getParent() instanceof CellRendererPane) {
            var8 = var1.getParent().getParent().hasFocus();
         } else {
            var8 = var1.hasFocus();
         }
      }

      String var9 = SyntheticaLookAndFeel.getString(var8 ? "Synthetica.tree.selectionBackground" : "Synthetica.tree.selectionBackground.inactive", var2);
      if (var9 != null) {
         Insets var10 = SyntheticaLookAndFeel.getInsets("Synthetica.tree.selectionBackground.insets", var2, false);
         ImagePainter var11 = new ImagePainter(var3, var4, var5, var6, var7, var9, var10, var10, 0, 0);
         var11.draw();
      }

   }

   public void paintTableCellRendererSelectionBackground(JTable var1, Component var2, Graphics var3, int var4, int var5, int var6, int var7) {
      boolean var8 = this.cellRendererRespectsFocus(var2) ? var1.hasFocus() : true;
      if (var1.getCellEditor() != null) {
         var8 = true;
      }

      String var9 = SyntheticaLookAndFeel.getString(var8 ? "Synthetica.table.selectionBackground" : "Synthetica.table.selectionBackground.inactive", var2);
      if (var9 != null) {
         Insets var10 = SyntheticaLookAndFeel.getInsets("Synthetica.table.selectionBackground.insets", var2, false);
         ImagePainter var11 = new ImagePainter(var3, var4, var5, var6, var7, var9, var10, var10, 0, 0);
         var11.draw();
      }

   }

   private boolean cellRendererRespectsFocus(Component var1) {
      return SyntheticaLookAndFeel.getBoolean("Synthetica.cellRenderer.respectFocus", var1, false);
   }

   protected void paintTextShadow(JComponent var1, Graphics var2, int var3, int var4, String var5, boolean var6, int var7, int var8, float var9, Color var10, int var11, int var12, boolean var13, int var14) {
      if (var5 != null && var5.length() != 0) {
         Font var15 = var13 ? var2.getFont() : var1.getFont();
         FontMetrics var16 = var2.getFontMetrics(var15);
         int var17 = var16.stringWidth(var5);
         int var18 = var16.getHeight() + var7 * 2;
         BufferedImage var19 = this.createCompatibleImage(var17, var18);
         Graphics2D var20 = var19.createGraphics();
         var20.setFont(var15);
         var20.drawString(var5, 0, var16.getAscent());
         if (this.getPaintMenuMnemonics() && var14 >= 0 && var14 < var5.length()) {
            int var21 = var16.stringWidth(var5.substring(0, var14));
            int var22 = var16.getHeight() - 1;
            int var23 = var16.charWidth(var5.charAt(var14));
            var20.fillRect(var21, var22, var23, 1);
         }

         var20.dispose();
         DropShadow var24 = new DropShadow(var19);
         var24.setQuality(var6);
         var24.setShadowSize(var7);
         var24.setDistance(var8);
         var24.setShadowOpacity(var9);
         var24.setShadowColor(var10);
         var24.paintShadow(var2, var3 + var11, var4 + var12);
      }
   }

   private BufferedImage createCompatibleImage(int var1, int var2) {
      GraphicsEnvironment var3 = GraphicsEnvironment.getLocalGraphicsEnvironment();
      GraphicsConfiguration var4 = var3.getDefaultScreenDevice().getDefaultConfiguration();
      return var4.createCompatibleImage(var1, var2, 3);
   }
}
