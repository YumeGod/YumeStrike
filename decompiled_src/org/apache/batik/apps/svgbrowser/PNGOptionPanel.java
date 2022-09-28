package org.apache.batik.apps.svgbrowser;

import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import org.apache.batik.util.gui.ExtendedGridBagConstraints;

public class PNGOptionPanel extends OptionPanel {
   protected JCheckBox check;

   public PNGOptionPanel() {
      super(new GridBagLayout());
      ExtendedGridBagConstraints var1 = new ExtendedGridBagConstraints();
      var1.insets = new Insets(5, 5, 5, 5);
      var1.weightx = 0.0;
      var1.weighty = 0.0;
      var1.fill = 0;
      var1.setGridBounds(0, 0, 1, 1);
      this.add(new JLabel(resources.getString("PNGOptionPanel.label")), var1);
      this.check = new JCheckBox();
      var1.weightx = 1.0;
      var1.fill = 2;
      var1.setGridBounds(1, 0, 1, 1);
      this.add(this.check, var1);
   }

   public boolean isIndexed() {
      return this.check.isSelected();
   }

   public static boolean showDialog(Component var0) {
      String var1 = resources.getString("PNGOptionPanel.dialog.title");
      PNGOptionPanel var2 = new PNGOptionPanel();
      OptionPanel.Dialog var3 = new OptionPanel.Dialog(var0, var1, var2);
      var3.pack();
      var3.setVisible(true);
      return var2.isIndexed();
   }
}
