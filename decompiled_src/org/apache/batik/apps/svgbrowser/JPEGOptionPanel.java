package org.apache.batik.apps.svgbrowser;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Hashtable;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JSlider;
import org.apache.batik.util.gui.ExtendedGridBagConstraints;

public class JPEGOptionPanel extends OptionPanel {
   protected JSlider quality;

   public JPEGOptionPanel() {
      super(new GridBagLayout());
      ExtendedGridBagConstraints var1 = new ExtendedGridBagConstraints();
      var1.insets = new Insets(5, 5, 5, 5);
      var1.weightx = 0.0;
      var1.weighty = 0.0;
      var1.fill = 0;
      var1.setGridBounds(0, 0, 1, 1);
      this.add(new JLabel(resources.getString("JPEGOptionPanel.label")), var1);
      this.quality = new JSlider();
      this.quality.setMinimum(0);
      this.quality.setMaximum(100);
      this.quality.setMajorTickSpacing(10);
      this.quality.setMinorTickSpacing(5);
      this.quality.setPaintTicks(true);
      this.quality.setPaintLabels(true);
      this.quality.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
      Hashtable var2 = new Hashtable();

      for(int var3 = 0; var3 < 100; var3 += 10) {
         var2.put(new Integer(var3), new JLabel("0." + var3 / 10));
      }

      var2.put(new Integer(100), new JLabel("1"));
      this.quality.setLabelTable(var2);
      Dimension var4 = this.quality.getPreferredSize();
      this.quality.setPreferredSize(new Dimension(350, var4.height));
      var1.weightx = 1.0;
      var1.fill = 2;
      var1.setGridBounds(1, 0, 1, 1);
      this.add(this.quality, var1);
   }

   public float getQuality() {
      return (float)this.quality.getValue() / 100.0F;
   }

   public static float showDialog(Component var0) {
      String var1 = resources.getString("JPEGOptionPanel.dialog.title");
      JPEGOptionPanel var2 = new JPEGOptionPanel();
      OptionPanel.Dialog var3 = new OptionPanel.Dialog(var0, var1, var2);
      var3.pack();
      var3.setVisible(true);
      return var2.getQuality();
   }
}
