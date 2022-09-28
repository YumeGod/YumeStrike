package de.javasoft.plaf.synthetica.painter;

import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.plaf.synth.SynthContext;

public class TreeExpandedIconPainter extends SyntheticaBasicIconPainter {
   private Icon regularIcon;
   private Icon selectedIcon;

   public TreeExpandedIconPainter() {
      super((SynthContext)null, SyntheticaLookAndFeel.getInt("Synthetica.tree.expandedIcon.size", (Component)null, 12), SyntheticaLookAndFeel.getInt("Synthetica.tree.expandedIcon.size", (Component)null, 12));
   }

   public void paintIcon(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      JComponent var7 = var1 == null ? null : var1.getComponent();
      boolean var8 = var7 instanceof JTree && ((JTree)var7).getSelectionModel().isRowSelected(((JTree)var7).getClosestRowForLocation(var3, var4));
      if (this.regularIcon == null && !var8) {
         this.regularIcon = SyntheticaLookAndFeel.loadIcon("Synthetica.tree.expandedIcon");
      } else if (this.selectedIcon == null && var8) {
         this.selectedIcon = SyntheticaLookAndFeel.loadIcon("Synthetica.tree.selected.expandedIcon");
      }

      Icon var9 = var8 ? this.selectedIcon : this.regularIcon;
      var9.paintIcon(var7, var2, var3, var4);
   }

   public int getCacheHash(SynthContext var1, int var2, int var3, int var4, String var5) {
      return -1;
   }
}
