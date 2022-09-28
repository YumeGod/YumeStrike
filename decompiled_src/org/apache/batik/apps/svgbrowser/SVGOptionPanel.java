package org.apache.batik.apps.svgbrowser;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JLabel;

public class SVGOptionPanel extends OptionPanel {
   protected JCheckBox xmlbaseCB;
   protected JCheckBox prettyPrintCB;

   public SVGOptionPanel() {
      super(new BorderLayout());
      this.add(new JLabel(resources.getString("SVGOptionPanel.label")), "North");
      this.xmlbaseCB = new JCheckBox(resources.getString("SVGOptionPanel.UseXMLBase"));
      this.xmlbaseCB.setSelected(resources.getBoolean("SVGOptionPanel.UseXMLBaseDefault"));
      this.add(this.xmlbaseCB, "Center");
      this.prettyPrintCB = new JCheckBox(resources.getString("SVGOptionPanel.PrettyPrint"));
      this.prettyPrintCB.setSelected(resources.getBoolean("SVGOptionPanel.PrettyPrintDefault"));
      this.add(this.prettyPrintCB, "South");
   }

   public boolean getUseXMLBase() {
      return this.xmlbaseCB.isSelected();
   }

   public boolean getPrettyPrint() {
      return this.prettyPrintCB.isSelected();
   }

   public static SVGOptionPanel showDialog(Component var0) {
      String var1 = resources.getString("SVGOptionPanel.dialog.title");
      SVGOptionPanel var2 = new SVGOptionPanel();
      OptionPanel.Dialog var3 = new OptionPanel.Dialog(var0, var1, var2);
      var3.pack();
      var3.setVisible(true);
      return var2;
   }
}
