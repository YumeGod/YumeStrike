package de.javasoft.plaf.synthetica;

import de.javasoft.plaf.synthetica.painter.MenuPainter;
import de.javasoft.util.JavaVersion;
import de.javasoft.util.OS;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.WeakHashMap;
import javax.swing.BoxLayout;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicMenuItemUI;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthConstants;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.text.View;

public class SyntheticaMenuItemUI extends BasicMenuItemUI implements PropertyChangeListener, SynthConstants {
   private static boolean paintDebugRects = UIManager.getBoolean("Synthetica.menuItem.paintDebugRectangles");
   private static WeakHashMap maxWidths = new WeakHashMap();
   private String acceleratorDelimiter;
   private static Rectangle iconRect = new Rectangle();
   private static Rectangle textRect = new Rectangle();
   private static Rectangle acceleratorRect = new Rectangle();
   private static Rectangle checkIconRect = new Rectangle();
   private static Rectangle arrowIconRect = new Rectangle();
   private static Rectangle viewRect = new Rectangle(32767, 32767);
   private static Rectangle r = new Rectangle();

   public static ComponentUI createUI(JComponent var0) {
      return new SyntheticaMenuItemUI();
   }

   protected void installDefaults() {
      this.updateStyle(this.menuItem);
   }

   protected void installListeners() {
      super.installListeners();
      this.menuItem.addPropertyChangeListener(this);
   }

   private void updateStyle(JMenuItem var1) {
      String var2 = this.getPropertyPrefix();
      SynthContext var3 = getContext(var1, 1);
      SynthStyle var4 = getStyle(var1);
      var4.installDefaults(var3);
      Object var5 = var4.get(var3, "MenuItem.textIconGap");
      if (var5 != null) {
         LookAndFeel.installProperty(var1, "iconTextGap", var5);
      }

      this.defaultTextIconGap = var1.getIconTextGap();
      if (this.menuItem.getMargin() == null || this.menuItem.getMargin() instanceof UIResource) {
         Object var6 = (Insets)var4.get(var3, var2 + ".margin");
         if (var6 == null) {
            var6 = new InsetsUIResource(0, 0, 0, 0);
         }

         this.menuItem.setMargin((Insets)var6);
      }

      this.acceleratorDelimiter = var4.getString(var3, var2 + ".acceleratorDelimiter", getAcceleratorDelimiter());
      this.arrowIcon = var4.getIcon(var3, var2 + ".arrowIcon");
      this.arrowIcon = (Icon)SyntheticaLookAndFeel.getClientProperty("Synthetica.menuItem.arrowIcon", this.menuItem, this.arrowIcon);
      this.checkIcon = var4.getIcon(var3, var2 + ".checkIcon");
   }

   protected void uninstallDefaults() {
      super.uninstallDefaults();
   }

   protected void uninstallListeners() {
      super.uninstallListeners();
      this.menuItem.removePropertyChangeListener(this);
   }

   protected Dimension getPreferredMenuItemSize(JComponent var1, Icon var2, Icon var3, int var4) {
      SynthContext var5 = getMenuItemContext(var1);
      SynthContext var6 = getContext(var1, Region.MENU_ITEM_ACCELERATOR);
      Dimension var7 = getPreferredMenuItemSize(var5, var6, false, var1, var2, var3, var4, this.acceleratorDelimiter);
      return var7;
   }

   static Dimension getPreferredMenuItemSize(SynthContext var0, SynthContext var1, boolean var2, JComponent var3, Icon var4, Icon var5, int var6, String var7) {
      JMenuItem var8 = (JMenuItem)var3;
      Icon var9 = var8.getIcon();
      String var10 = var8.getText();
      KeyStroke var11 = var8.getAccelerator();
      String var12 = "";
      JPopupMenu var13 = findPopup(var8);
      int var14 = var0.getStyle().getInt(var0, "MenuItem.textIconGap", var6);
      int var15 = SyntheticaLookAndFeel.getInt("Synthetica.menuItem.textIconGap", var13, var14);
      int var16 = SyntheticaLookAndFeel.getInt("Synthetica.menuItem.acceleratorGap", var13, var14 * 4);
      int var17 = SyntheticaLookAndFeel.getInt("Synthetica.popupMenu.iconSeparatorGap", var13, 0);
      if (var11 != null) {
         int var18 = var11.getModifiers();
         if (var18 > 0) {
            var12 = modifiersAsText(var18);
            var12 = var12 + var7;
         }

         int var19 = var11.getKeyCode();
         if (var19 != 0) {
            var12 = var12 + KeyEvent.getKeyText(var19);
         } else {
            var12 = var12 + var11.getKeyChar();
         }
      }

      Font var25 = var0.getStyle().getFont(var0);
      FontMetrics var26 = var8.getFontMetrics(var25);
      FontMetrics var20 = var8.getFontMetrics(var1.getStyle().getFont(var1));
      resetRects();
      layoutMenuItem(var0, var26, var1, var10, var20, var12, var9, var4, var5, var8.getVerticalAlignment(), var8.getHorizontalAlignment(), var8.getVerticalTextPosition(), var8.getHorizontalTextPosition(), viewRect, iconRect, textRect, acceleratorRect, checkIconRect, arrowIconRect, var10 == null ? 0 : var6, var6, var2);
      r.setBounds(textRect);
      r = SwingUtilities.computeUnion(var2 ? iconRect.x : textRect.x, iconRect.y, var2 ? iconRect.width : 0, iconRect.height, r);
      Rectangle var10000;
      if (var13 != null) {
         MaxWidth var21 = (MaxWidth)maxWidths.get(var13);
         if (var21 == null) {
            maxWidths.put(var13, new MaxWidth((MaxWidth)null));
            var21 = (MaxWidth)maxWidths.get(var13);
         } else if (isHorizontalLayout(var13)) {
            var21.clear();
         }

         var21.maxTextWidth = Math.max(var21.maxTextWidth, r.width);
         r.width = var21.maxTextWidth;
         var21.maxAcceleratorWidth = Math.max(var21.maxAcceleratorWidth, acceleratorRect.width);
         var10000 = r;
         var10000.width += var21.maxAcceleratorWidth;
         if (var21.maxAcceleratorWidth > 0) {
            var10000 = r;
            var10000.width += var16;
         }

         var10000 = r;
         var10000.width += var21.maxIconWidth;
         if (var21.maxIconWidth > 0) {
            var10000 = r;
            var10000.width += var15;
         }

         if (!var2) {
            if (SyntheticaLookAndFeel.popupHasCheckRadioWithIcon(var13)) {
               var10000 = r;
               var10000.width += var21.maxCheckIconWidth;
               if (var21.maxCheckIconWidth > 0) {
                  var10000 = r;
                  var10000.width += var14;
               }
            } else if (SyntheticaLookAndFeel.popupHasCheckRadio(var13)) {
               var10000 = r;
               var10000.width -= var21.maxIconWidth;
               var10000 = r;
               var10000.width += Math.max(var21.maxIconWidth, var21.maxCheckIconWidth);
               if (var21.maxIconWidth > 0) {
                  var10000 = r;
                  var10000.width -= var15;
               }

               var10000 = r;
               var10000.width += var14;
            }

            if (var21.maxAcceleratorWidth == 0) {
               var10000 = r;
               var10000.width += var21.maxArrowIconWidth;
               if (var21.maxArrowIconWidth > 0) {
                  var10000 = r;
                  var10000.width += var14;
               }
            }
         }
      } else if (!var2) {
         var10000 = r;
         var10000.width += acceleratorRect.width;
         if (acceleratorRect.width > 0) {
            var10000 = r;
            var10000.width += var16;
         }

         var10000 = r;
         var10000.width += iconRect.width;
         if (iconRect.width > 0) {
            var10000 = r;
            var10000.width += var15;
         }

         var10000 = r;
         var10000.width += checkIconRect.width;
         if (checkIconRect.width > 0) {
            var10000 = r;
            var10000.width += var14;
         }

         int var27 = var5 == null ? 0 : SyntheticaLookAndFeel.getInt("Synthetica.menuItem.arrowIconGap", var3, var14);
         var10000 = r;
         var10000.width += arrowIconRect.width + var27;
      }

      var10000 = r;
      var10000.width += var2 ? var14 : var14 * 2;
      if (!var2 && var13 != null && (SyntheticaLookAndFeel.preservePopupIconSpace(var13) || SyntheticaLookAndFeel.popupHasCheckRadio(var13))) {
         var10000 = r;
         var10000.width += var17;
      }

      Insets var28 = var0.getStyle().getInsets(var0, (Insets)null);
      Insets var22 = var8.getMargin();
      if (var22 != null) {
         var28 = new Insets(var28.top + var22.top, var28.left + var22.left, var28.bottom + var22.bottom, var28.right + var22.right);
      }

      var10000 = r;
      var10000.width += (var2 ? 0 : var28.left) + var28.right;
      var10000 = r;
      var10000.width += var2 && var9 != null ? var28.left + var15 : 0;
      var10000 = r;
      var10000.height += var28.top + var28.bottom;
      int var23 = SyntheticaLookAndFeel.getInt("Menu.menuPopupOffsetX", (Component)null, 0);
      boolean var24 = SyntheticaLookAndFeel.getBoolean("Synthetica.popupMenu.respectTopLevelMenuWidth", var13, true);
      if (var24 && var13 != null && SyntheticaLookAndFeel.isToplevelPopupMenu(var13) && r.width + var23 < var13.getInvoker().getWidth()) {
         r.width = var13.getInvoker().getWidth() - var23;
      }

      if (SyntheticaLookAndFeel.getBoolean("Synthetica.menuItem.justifySize", var13, true)) {
         if (r.width % 2 == 0) {
            ++r.width;
         }

         if (r.height % 2 == 0) {
            ++r.height;
         }
      }

      return r.getSize();
   }

   private static boolean isHorizontalLayout(JPopupMenu var0) {
      if (!JavaVersion.JAVA5 && var0.getLayout() instanceof BoxLayout) {
         int var1 = ((BoxLayout)var0.getLayout()).getAxis();
         return var1 == 0 || var1 == 2;
      } else {
         return false;
      }
   }

   private static String layoutMenuItem(SynthContext var0, FontMetrics var1, SynthContext var2, String var3, FontMetrics var4, String var5, Icon var6, Icon var7, Icon var8, int var9, int var10, int var11, int var12, Rectangle var13, Rectangle var14, Rectangle var15, Rectangle var16, Rectangle var17, Rectangle var18, int var19, int var20, boolean var21) {
      JComponent var22 = var0.getComponent();
      JPopupMenu var23 = findPopup(var22);
      boolean var24 = var22.getComponentOrientation().isLeftToRight();
      int var25 = var0.getStyle().getInt(var0, "MenuItem.textIconGap", var20);
      int var26 = SyntheticaLookAndFeel.getInt("Synthetica.menuItem.textIconGap", var23, var25);
      int var27 = SyntheticaLookAndFeel.getInt("Synthetica.menuItem.acceleratorGap", var23, var25 * 4);
      int var28 = SyntheticaLookAndFeel.getInt("Synthetica.popupMenu.iconSeparatorGap", var23, 0);
      boolean var29 = SyntheticaLookAndFeel.getBoolean("Synthetica.popupMenu.centerText", var23, false);
      boolean var30 = SyntheticaLookAndFeel.getBoolean("Synthetica.popupMenu.forceIconSpace", var23, false);
      int var31 = var6 == null ? (var30 ? SyntheticaLookAndFeel.getInt("Synthetica.popupMenu.defaultIconWidth", var23, 16) : 0) : var6.getIconWidth();
      int var32 = var7 == null ? 0 : var7.getIconWidth();
      int var33 = var8 == null ? 0 : var8.getIconWidth();
      if (var23 != null) {
         MaxWidth var34 = (MaxWidth)maxWidths.get(var23);
         if (var34 == null) {
            maxWidths.put(var23, new MaxWidth((MaxWidth)null));
            var34 = (MaxWidth)maxWidths.get(var23);
         }

         var34.maxIconWidth = Math.max(var34.maxIconWidth, var31);
         var31 = var34.maxIconWidth;
         var34.maxCheckIconWidth = Math.max(var34.maxCheckIconWidth, var32);
         var32 = var34.maxCheckIconWidth;
         var34.maxArrowIconWidth = Math.max(var34.maxArrowIconWidth, var33);
         var33 = var34.maxArrowIconWidth;
         var23.putClientProperty("Synthetica.menuItem.maxIconWidth", var31);
      }

      boolean var40 = var23 == null ? var6 != null : SyntheticaLookAndFeel.preservePopupIconSpace(var23);
      boolean var35 = var23 == null ? var7 != null : SyntheticaLookAndFeel.popupHasCheckRadio(var23);
      boolean var36 = var23 == null ? var35 && var40 : SyntheticaLookAndFeel.popupHasCheckRadioWithIcon(var23);
      var0.getStyle().getGraphicsUtils(var0).layoutText(var0, var1, var3, var6, var10, var9, var12, var11, var13, var14, var15, var26);
      if (!var21) {
         if (var7 != null) {
            var17.width = SyntheticaIcon.getIconWidth(var7, var0);
            var17.height = SyntheticaIcon.getIconHeight(var7, var0);
         } else {
            var17.width = var17.height = 0;
         }

         if (var8 != null) {
            var18.width = SyntheticaIcon.getIconWidth(var8, var0);
            var18.height = SyntheticaIcon.getIconHeight(var8, var0);
         } else {
            var18.width = var18.height = 0;
         }
      }

      if (var21) {
         Insets var37 = var0.getStyle().getInsets(var0, (Insets)null);
         if (var24) {
            var15.x += var25 + var37.left;
         } else {
            var15.x -= var25 + var37.left;
         }

         if (var14.width > 0) {
            var14.x += (var37.left + var26) * (var24 ? 1 : -1);
         }
      } else if (var36) {
         if (var24) {
            var17.x = var13.x + var25;
            var14.x = var13.x + var25 + var32 + var25;
            var15.x = var13.x + var25 + var32 + var25 + var31 + var26 + var28;
         } else {
            var17.x = var13.x + var13.width - var25 - var32;
            var14.x = var17.x - var25 - var31;
            var15.x = var14.x - var26 - var28 - var15.width;
         }
      } else if (var40 && var35) {
         int var41 = Math.max(var32, var31);
         if (var24) {
            var17.x = var13.x + var25 + (var41 - var17.width) / 2;
            var14.x = var13.x + var25 + (var41 - var14.width) / 2;
            var15.x = var13.x + var25 + var41 + var25 + var28;
         } else {
            var17.x = var13.x + var13.width - var25 - var32 - (var41 - var17.width) / 2;
            var14.x = var13.x + var13.width - var25 - var31 - (var41 - var14.width) / 2;
            var15.x = var13.x + var13.width - var25 - var41 - var25 - var28 - var15.width;
         }
      } else if (var40 && !var35) {
         if (var24) {
            var14.x = var13.x + var25;
            var15.x = var13.x + var25 + var31 + var26 + (var23 == null ? 0 : var28);
         } else {
            var14.x = var13.x + var13.width - var25 - var31;
            var15.x = var14.x - var26 - (var23 == null ? 0 : var28) - var15.width;
         }
      } else if (!var40 && var35) {
         if (var24) {
            var17.x = var13.x + var25;
            var15.x = var13.x + var25 + var32 + var25 + var28;
         } else {
            var17.x = var13.x + var13.width - var25 - var32;
            var15.x = var17.x - var25 - var28 - var15.width;
         }
      } else if (var24) {
         var15.x = var13.x + var25;
      } else if (var13.width < 32767) {
         var15.x = var13.x + var13.width - var15.width - var25;
         var14.x = 0;
      }

      if (!var21) {
         if (var24) {
            var18.x = var13.x + var13.width - var25 - var18.width;
         } else {
            var18.x = var13.x + var25;
         }
      }

      if (var5 != null && !var5.equals("")) {
         SynthStyle var42 = var2.getStyle();
         var16.width = var42.getGraphicsUtils(var2).computeStringWidth(var2, var4.getFont(), var4, var5);
         var16.height = var4.getHeight();
      } else {
         var16.width = var16.height = 0;
         var5 = "";
      }

      Rectangle var43 = var14.union(var15);
      int var38 = maxWidths.get(var23) == null ? var16.width : ((MaxWidth)maxWidths.get(var23)).maxAcceleratorWidth;
      if (var24) {
         var16.x = var13.x + var13.width - var18.width - var25 - var38;
      } else {
         var16.x = var13.x + var18.width + var25 + var38 - var16.width;
      }

      var16.y = var43.y + (var43.height / 2 - var16.height / 2);
      if (!var21) {
         var18.y = var43.y + (var43.height / 2 - var18.height / 2);
         var17.y = var43.y + (var43.height / 2 - var17.height / 2);
      }

      if (!var21 && var29) {
         int var39 = var13.width - Math.max(var38, var33) - var25 * 2;
         if (var36) {
            var39 -= var31 + var25 + var32 + var26 + var28;
         } else if (var40 && var35) {
            var39 -= Math.max(var31, var32) + var25 + var28;
         } else if (var40 && !var35) {
            var39 -= var31 + var26 + var28;
         } else if (!var40 && var35) {
            var39 -= var32 + var25 + var28;
         }

         if (var38 > 0) {
            var39 -= var27;
         } else if (var33 > 0) {
            var39 -= var25;
         }

         var15.x += var24 ? (var39 - var15.width) / 2 : -(var39 - var15.width) / 2;
      }

      return var3;
   }

   public void update(Graphics var1, JComponent var2) {
      SynthContext var3 = getContext(var2);
      this.paintBackground(var3, var1, 0, 0, var2.getWidth(), var2.getHeight());
      this.paint(var3, var1);
      this.paintBorder(var3, var1, 0, 0, var2.getWidth(), var2.getHeight());
   }

   void paintBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      MenuPainter.getInstance(var1).paintMenuItemBackground(var1, var2, var3, var4, var5, var6);
   }

   void paintBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      MenuPainter.getInstance(var1).paintMenuItemBorder(var1, var2, var3, var4, var5, var6);
   }

   public void paint(Graphics var1, JComponent var2) {
      this.paint(getMenuItemContext(var2), var1);
   }

   private void paint(SynthContext var1, Graphics var2) {
      JComponent var3 = var1.getComponent();
      SynthStyle var4 = getStyle(var3);
      SynthContext var5 = getContext(this.menuItem, Region.MENU_ITEM_ACCELERATOR);
      String var6 = this.getPropertyPrefix();
      Icon var7 = var4.getIcon(var1, var6 + ".arrowIcon");
      var7 = (Icon)SyntheticaLookAndFeel.getClientProperty("Synthetica.menuItem.arrowIcon", var3, var7);
      if ((var1.getComponentState() & 2) > 0) {
         var7 = (Icon)SyntheticaLookAndFeel.getClientProperty("Synthetica.menuItem.hover.arrowIcon", var3, var7);
      }

      paint(var1, var5, var2, var4.getIcon(var1, var6 + ".checkIcon"), var7, false, this.acceleratorDelimiter, this.defaultTextIconGap);
   }

   static void paint(SynthContext var0, SynthContext var1, Graphics var2, Icon var3, Icon var4, boolean var5, String var6, int var7) {
      JComponent var8 = var0.getComponent();
      JMenuItem var9 = (JMenuItem)var8;
      SynthStyle var10 = var0.getStyle();
      ButtonModel var11 = var9.getModel();
      Insets var12 = var10.getInsets(var0, (Insets)null);
      Insets var13 = var9.getMargin();
      if (var13 != null) {
         var12 = new Insets(var12.top + var13.top, var12.left + var13.left, var12.bottom + var13.bottom, var12.right + var13.right);
      }

      boolean var14 = var8.getComponentOrientation().isLeftToRight();
      resetRects();
      viewRect.setBounds(0, 0, var9.getWidth(), var9.getHeight());
      Rectangle var10000 = viewRect;
      var10000.y += var12.top;
      var10000 = viewRect;
      var10000.height += -var12.bottom - var12.top;
      if (!var5) {
         var10000 = viewRect;
         var10000.x += var14 ? var12.left : var12.right;
         var10000 = viewRect;
         var10000.width += -var12.right - var12.left;
      }

      Font var15 = var10.getFont(var0);
      var2.setFont(var15);
      FontMetrics var16 = var2.getFontMetrics();
      KeyStroke var18 = var9.getAccelerator();
      String var19 = "";
      if (var18 != null) {
         int var20 = var18.getModifiers();
         if (var20 > 0) {
            var19 = modifiersAsText(var20);
            var19 = var19 + var6;
         }

         int var21 = var18.getKeyCode();
         if (var21 != 0) {
            var19 = var19 + KeyEvent.getKeyText(var21);
         } else {
            var19 = var19 + var18.getKeyChar();
         }
      }

      String var23 = layoutMenuItem(var0, var16, var1, var9.getText(), var16, var19, var9.getIcon(), var3, var4, var9.getVerticalAlignment(), var9.getHorizontalAlignment(), var9.getVerticalTextPosition(), var9.getHorizontalTextPosition(), viewRect, iconRect, textRect, acceleratorRect, checkIconRect, arrowIconRect, var9.getText() == null ? 0 : var7, var7, var5);
      if (var3 != null && !var5) {
         SyntheticaIcon.paintIcon(var3, var0, var2, checkIconRect.x, checkIconRect.y, checkIconRect.width, checkIconRect.height);
      }

      if (var8 instanceof JMenu && var0.getRegion() != Region.MENU) {
         var0 = getContext(var8, Region.MENU);
         var10 = var0.getStyle();
      }

      if (var9.getIcon() != null) {
         Icon var24 = null;
         if (!var11.isEnabled()) {
            var24 = var9.getDisabledIcon();
         } else if (var11.isPressed() && var11.isArmed()) {
            var24 = var9.getPressedIcon();
         } else if (var11.isRollover() && var11.isSelected()) {
            var24 = var9.getRolloverSelectedIcon();
         } else if (var11.isRollover()) {
            var24 = var9.getRolloverIcon();
         } else if (var11.isSelected()) {
            var24 = var9.getSelectedIcon();
         }

         if (var24 == null) {
            var24 = var9.getIcon();
         }

         if (var24 != null) {
            SyntheticaIcon.paintIcon(var24, var0, var2, iconRect.x, iconRect.y, iconRect.width, iconRect.height);
         }
      }

      if (var23 != null) {
         View var25 = (View)var8.getClientProperty("html");
         if (var25 != null) {
            var25.paint(var2, textRect);
         } else {
            var2.setColor(var10.getColor(var0, ColorType.TEXT_FOREGROUND));
            var2.setFont(var10.getFont(var0));
            var10.getGraphicsUtils(var0).paintText(var0, var2, var23, textRect.x, textRect.y, var9.getDisplayedMnemonicIndex());
         }
      }

      if (var19 != null && !var19.equals("")) {
         byte var26 = 0;
         SynthStyle var22 = var1.getStyle();
         var2.setColor(var22.getColor(var1, ColorType.TEXT_FOREGROUND));
         var2.setFont(var22.getFont(var1));
         var22.getGraphicsUtils(var1).paintText(var1, var2, var19, acceleratorRect.x - var26, acceleratorRect.y, -1);
      }

      if (var4 != null && !var5) {
         SyntheticaIcon.paintIcon(var4, var0, var2, arrowIconRect.x, arrowIconRect.y, arrowIconRect.width, arrowIconRect.height);
      }

      if (paintDebugRects) {
         var2.setColor(Color.ORANGE);
         var2.drawRect(viewRect.x, viewRect.y, viewRect.width - 1, viewRect.height - 1);
         var2.setColor(Color.RED);
         var2.drawRect(checkIconRect.x, checkIconRect.y, checkIconRect.width - 1, checkIconRect.height - 1);
         var2.setColor(Color.YELLOW);
         var2.drawRect(iconRect.x, iconRect.y, iconRect.width - 1, iconRect.height - 1);
         var2.setColor(Color.GREEN);
         var2.drawRect(textRect.x, textRect.y, textRect.width - 1, textRect.height - 1);
         var2.setColor(Color.CYAN);
         var2.drawRect(acceleratorRect.x, acceleratorRect.y, acceleratorRect.width - 1, acceleratorRect.height - 1);
         var2.setColor(Color.BLUE);
         var2.drawRect(arrowIconRect.x, arrowIconRect.y, arrowIconRect.width - 1, arrowIconRect.height - 1);
      }

   }

   private static String modifiersAsText(int var0) {
      return OS.getCurrentOS() == OS.Mac && SyntheticaLookAndFeel.getBoolean("Synthetica.menuItem.useMacAcceleratorChars", (Component)null, false) ? getMacKeyModifiersText(var0) : getKeyModifiersText(var0);
   }

   private static String getAcceleratorDelimiter() {
      String var0 = UIManager.getString("MenuItem.acceleratorDelimiter");
      return var0 == null ? "+" : var0;
   }

   private static String getKeyModifiersText(int var0) {
      String var1 = getAcceleratorDelimiter();
      StringBuffer var2 = new StringBuffer();
      if ((var0 & 4) != 0) {
         var2.append(Toolkit.getProperty("AWT.meta", "Meta") + var1);
      }

      if ((var0 & 2) != 0) {
         var2.append(Toolkit.getProperty("AWT.control", "Ctrl") + var1);
      }

      if ((var0 & 8) != 0) {
         var2.append(Toolkit.getProperty("AWT.alt", "Alt") + var1);
      }

      if ((var0 & 1) != 0) {
         var2.append(Toolkit.getProperty("AWT.shift", "Shift") + var1);
      }

      if ((var0 & 32) != 0) {
         var2.append(Toolkit.getProperty("AWT.altGraph", "Alt Graph") + var1);
      }

      if ((var0 & 16) != 0) {
         var2.append(Toolkit.getProperty("AWT.button1", "Button1") + var1);
      }

      if (var2.length() > 0) {
         var2.setLength(var2.length() - var1.length());
      }

      return var2.toString();
   }

   private static String getMacKeyModifiersText(int var0) {
      String var1 = getAcceleratorDelimiter();
      StringBuffer var2 = new StringBuffer();
      if ((var0 & 2) != 0) {
         var2.append("⌃" + var1);
      }

      if ((var0 & 1) != 0) {
         var2.append("⇧" + var1);
      }

      if ((var0 & 8) != 0) {
         var2.append("⌥" + var1);
      }

      if ((var0 & 32) != 0) {
         var2.append(Toolkit.getProperty("AWT.altGraph", "Alt Graph") + var1);
      }

      if ((var0 & 4) != 0) {
         var2.append("⌘" + var1);
      }

      if ((var0 & 16) != 0) {
         var2.append(Toolkit.getProperty("AWT.button1", "Button1") + var1);
      }

      if (var2.length() > 0) {
         var2.setLength(var2.length() - var1.length());
      }

      return var2.toString();
   }

   public static void resetPopupMenu(JPopupMenu var0) {
      maxWidths.remove(var0);
   }

   static SynthContext getMenuItemContext(JComponent var0) {
      boolean var1 = var0 instanceof JMenu && ((JMenu)var0).isTopLevelMenu();
      Region var2 = var1 ? Region.MENU : Region.MENU_ITEM;
      return getContext(var0, var2);
   }

   static SynthStyle getStyle(JComponent var0) {
      return SynthLookAndFeel.getStyle(var0, getRegion(var0));
   }

   static SynthContext getContext(JComponent var0) {
      return getContext(var0, getComponentState(var0));
   }

   static SynthContext getContext(JComponent var0, int var1) {
      return getContext(var0, getRegion(var0), var1);
   }

   static SynthContext getContext(JComponent var0, Region var1) {
      return getContext(var0, var1, getComponentState(var0));
   }

   private static SynthContext getContext(JComponent var0, Region var1, int var2) {
      SynthStyle var3 = SynthLookAndFeel.getStyle(var0, var1);
      SynthContext var4 = new SynthContext(var0, var1, var3, var2);
      return var4;
   }

   private static Region getRegion(JComponent var0) {
      return SynthLookAndFeel.getRegion(var0);
   }

   private static int getComponentState(JComponent var0) {
      boolean var1 = false;
      int var2;
      if (!var0.isEnabled()) {
         var2 = 8;
      } else if (((JMenuItem)var0).isArmed()) {
         var2 = 2;
      } else if (var0.isEnabled()) {
         if (var0.isFocusOwner()) {
            var2 = 257;
         } else {
            var2 = 1;
         }
      } else {
         var2 = 8;
      }

      if (((JMenuItem)var0).isSelected()) {
         var2 |= 512;
      }

      return var2;
   }

   private static JPopupMenu findPopup(Container var0) {
      return (JPopupMenu)SwingUtilities.getAncestorOfClass(JPopupMenu.class, var0);
   }

   private static void resetRects() {
      iconRect.setBounds(0, 0, 0, 0);
      textRect.setBounds(0, 0, 0, 0);
      acceleratorRect.setBounds(0, 0, 0, 0);
      checkIconRect.setBounds(0, 0, 0, 0);
      arrowIconRect.setBounds(0, 0, 0, 0);
      viewRect.setBounds(0, 0, 32767, 32767);
      r.setBounds(0, 0, 0, 0);
   }

   public void propertyChange(PropertyChangeEvent var1) {
      String var2 = var1.getPropertyName();
      if (SyntheticaLookAndFeel.shouldUpdateStyle(var1)) {
         this.updateStyle((JMenuItem)var1.getSource());
      } else if ("Synthetica.menuItem.arrowIcon".equals(var2)) {
         this.menuItem.updateUI();
      } else if (("text".equals(var2) || "icon".equals(var2)) && SyntheticaLookAndFeel.getBoolean("Synthetica.popupMenu.dynamicItemSupport", (Component)null)) {
         resetPopupMenu(findPopup((JMenuItem)var1.getSource()));
      }

   }

   private static class MaxWidth {
      int maxTextWidth;
      int maxAcceleratorWidth;
      int maxIconWidth;
      int maxCheckIconWidth;
      int maxArrowIconWidth;

      private MaxWidth() {
      }

      public void clear() {
         this.maxTextWidth = 0;
         this.maxAcceleratorWidth = 0;
         this.maxIconWidth = 0;
         this.maxCheckIconWidth = 0;
         this.maxArrowIconWidth = 0;
      }

      // $FF: synthetic method
      MaxWidth(MaxWidth var1) {
         this();
      }
   }
}
