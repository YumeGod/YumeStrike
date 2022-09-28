package de.javasoft.plaf.synthetica;

import de.javasoft.plaf.synthetica.painter.MenuPainter;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicMenuUI;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthConstants;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthStyle;

public class SyntheticaMenuUI extends BasicMenuUI implements PropertyChangeListener, SynthConstants {
   private String acceleratorDelimiter;

   public static ComponentUI createUI(JComponent var0) {
      return new SyntheticaMenuUI();
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
      SynthContext var3 = SyntheticaMenuItemUI.getContext(var1, 1);
      SynthStyle var4 = SyntheticaMenuItemUI.getStyle(var1);
      var4.installDefaults(var3);
      this.defaultTextIconGap = var4.getInt(var3, var2 + ".textIconGap", 4);
      if (this.menuItem.getMargin() == null || this.menuItem.getMargin() instanceof UIResource) {
         Object var5 = (Insets)var4.get(var3, var2 + ".margin");
         if (var5 == null) {
            var5 = new InsetsUIResource(0, 0, 0, 0);
         }

         this.menuItem.setMargin((Insets)var5);
      }

      this.acceleratorDelimiter = var4.getString(var3, var2 + ".acceleratorDelimiter", "+");
      this.arrowIcon = var4.getIcon(var3, var2 + ".arrowIcon");
      this.checkIcon = var4.getIcon(var3, var2 + ".checkIcon");
      ((JMenu)this.menuItem).setDelay(var4.getInt(var3, var2 + ".delay", 200));
   }

   protected void uninstallDefaults() {
      super.uninstallDefaults();
   }

   protected void uninstallListeners() {
      super.uninstallListeners();
      this.menuItem.removePropertyChangeListener(this);
   }

   protected Dimension getPreferredMenuItemSize(JComponent var1, Icon var2, Icon var3, int var4) {
      SynthContext var5 = SyntheticaMenuItemUI.getMenuItemContext(var1);
      SynthContext var6 = SyntheticaMenuItemUI.getContext(var1, Region.MENU_ITEM_ACCELERATOR);
      Dimension var7 = SyntheticaMenuItemUI.getPreferredMenuItemSize(var5, var6, this.isTopLevelMenu(), var1, var2, var3, var4, this.acceleratorDelimiter);
      return var7;
   }

   public void update(Graphics var1, JComponent var2) {
      SynthContext var3 = SyntheticaMenuItemUI.getContext(var2);
      this.paintBackground(var3, var1, 0, 0, var2.getWidth(), var2.getHeight());
      this.paint(var3, var1);
      this.paintBorder(var3, var1, 0, 0, var2.getWidth(), var2.getHeight());
   }

   private void paintBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      MenuPainter.getInstance(var1).paintMenuBackground(var1, var2, var3, var4, var5, var6);
   }

   private void paintBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      MenuPainter.getInstance(var1).paintMenuBorder(var1, var2, var3, var4, var5, var6);
   }

   public void paint(Graphics var1, JComponent var2) {
      this.paint(SyntheticaMenuItemUI.getContext(var2), var1);
   }

   private void paint(SynthContext var1, Graphics var2) {
      SynthStyle var3 = var1.getStyle();
      String var4 = this.getPropertyPrefix();
      Icon var5 = var3.getIcon(var1, var4 + ".arrowIcon");
      Icon var6 = var3.getIcon(var1, var4 + ".checkIcon");
      SynthContext var7 = SyntheticaMenuItemUI.getContext(this.menuItem, Region.MENU_ITEM_ACCELERATOR);
      var1 = SyntheticaMenuItemUI.getMenuItemContext(var1.getComponent());
      SyntheticaMenuItemUI.paint(var1, var7, var2, var6, var5, this.isTopLevelMenu(), this.acceleratorDelimiter, this.defaultTextIconGap);
   }

   private boolean isTopLevelMenu() {
      return ((JMenu)this.menuItem).isTopLevelMenu();
   }

   public void propertyChange(PropertyChangeEvent var1) {
      if (SyntheticaLookAndFeel.shouldUpdateStyle(var1)) {
         this.updateStyle((JMenu)var1.getSource());
      }

   }
}
