package de.javasoft.plaf.synthetica;

import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.plaf.UIResource;

public class TreeCollapsedIcon implements Icon, UIResource {
   protected static Icon icon;
   protected static Icon selectedIcon;

   public void paintIcon(Component var1, Graphics var2, int var3, int var4) {
      boolean var5 = var1 instanceof JTree && ((JTree)var1).getSelectionModel().isRowSelected(((JTree)var1).getClosestRowForLocation(var3, var4));
      if (var5 && selectedIcon != null) {
         selectedIcon.paintIcon(var1, var2, var3, var4);
      } else if (icon != null) {
         icon.paintIcon(var1, var2, var3, var4);
      }

   }

   public int getIconWidth() {
      if (icon == null) {
         icon = SyntheticaLookAndFeel.loadIcon("Synthetica.tree.collapsedIcon");
         selectedIcon = SyntheticaLookAndFeel.loadIcon("Synthetica.tree.selected.collapsedIcon");
      }

      return icon == null ? 0 : icon.getIconWidth();
   }

   public int getIconHeight() {
      if (icon == null) {
         icon = SyntheticaLookAndFeel.loadIcon("Synthetica.tree.collapsedIcon");
         selectedIcon = SyntheticaLookAndFeel.loadIcon("Synthetica.tree.selected.collapsedIcon");
      }

      return icon == null ? 0 : icon.getIconHeight();
   }
}
