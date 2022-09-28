package org.apache.batik.util.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.apache.batik.util.resources.ResourceManager;

public class LocationBar extends JPanel {
   protected static final String RESOURCES = "org.apache.batik.util.gui.resources.LocationBar";
   protected static ResourceBundle bundle = ResourceBundle.getBundle("org.apache.batik.util.gui.resources.LocationBar", Locale.getDefault());
   protected static ResourceManager rManager;
   protected JComboBox comboBox;

   public LocationBar() {
      super(new BorderLayout(5, 5));
      JLabel var1 = new JLabel(rManager.getString("Panel.label"));
      this.add("West", var1);

      try {
         String var2 = rManager.getString("Panel.icon");
         URL var3 = this.getClass().getResource(var2);
         if (var3 != null) {
            var1.setIcon(new ImageIcon(var3));
         }
      } catch (MissingResourceException var4) {
      }

      this.add("Center", this.comboBox = new JComboBox());
      this.comboBox.setEditable(true);
   }

   public void addActionListener(ActionListener var1) {
      this.comboBox.addActionListener(var1);
   }

   public String getText() {
      return (String)this.comboBox.getEditor().getItem();
   }

   public void setText(String var1) {
      this.comboBox.getEditor().setItem(var1);
   }

   public void addToHistory(String var1) {
      this.comboBox.addItem(var1);
      this.comboBox.setPreferredSize(new Dimension(0, this.comboBox.getPreferredSize().height));
   }

   static {
      rManager = new ResourceManager(bundle);
   }
}
