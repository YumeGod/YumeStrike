package org.apache.batik.util.gui.resource;

import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import org.apache.batik.util.resources.ResourceFormatException;
import org.apache.batik.util.resources.ResourceManager;

public class ButtonFactory extends ResourceManager {
   private static final String ICON_SUFFIX = ".icon";
   private static final String TEXT_SUFFIX = ".text";
   private static final String MNEMONIC_SUFFIX = ".mnemonic";
   private static final String ACTION_SUFFIX = ".action";
   private static final String SELECTED_SUFFIX = ".selected";
   private static final String TOOLTIP_SUFFIX = ".tooltip";
   private ActionMap actions;

   public ButtonFactory(ResourceBundle var1, ActionMap var2) {
      super(var1);
      this.actions = var2;
   }

   public JButton createJButton(String var1) throws MissingResourceException, ResourceFormatException, MissingListenerException {
      JButton var2;
      try {
         var2 = new JButton(this.getString(var1 + ".text"));
      } catch (MissingResourceException var4) {
         var2 = new JButton();
      }

      this.initializeButton(var2, var1);
      return var2;
   }

   public JButton createJToolbarButton(String var1) throws MissingResourceException, ResourceFormatException, MissingListenerException {
      JToolbarButton var2;
      try {
         var2 = new JToolbarButton(this.getString(var1 + ".text"));
      } catch (MissingResourceException var4) {
         var2 = new JToolbarButton();
      }

      this.initializeButton(var2, var1);
      return var2;
   }

   public JToggleButton createJToolbarToggleButton(String var1) throws MissingResourceException, ResourceFormatException, MissingListenerException {
      JToolbarToggleButton var2;
      try {
         var2 = new JToolbarToggleButton(this.getString(var1 + ".text"));
      } catch (MissingResourceException var4) {
         var2 = new JToolbarToggleButton();
      }

      this.initializeButton(var2, var1);
      return var2;
   }

   public JRadioButton createJRadioButton(String var1) throws MissingResourceException, ResourceFormatException, MissingListenerException {
      JRadioButton var2 = new JRadioButton(this.getString(var1 + ".text"));
      this.initializeButton(var2, var1);

      try {
         var2.setSelected(this.getBoolean(var1 + ".selected"));
      } catch (MissingResourceException var4) {
      }

      return var2;
   }

   public JCheckBox createJCheckBox(String var1) throws MissingResourceException, ResourceFormatException, MissingListenerException {
      JCheckBox var2 = new JCheckBox(this.getString(var1 + ".text"));
      this.initializeButton(var2, var1);

      try {
         var2.setSelected(this.getBoolean(var1 + ".selected"));
      } catch (MissingResourceException var4) {
      }

      return var2;
   }

   private void initializeButton(AbstractButton var1, String var2) throws ResourceFormatException, MissingListenerException {
      try {
         Action var3 = this.actions.getAction(this.getString(var2 + ".action"));
         if (var3 == null) {
            throw new MissingListenerException("", "Action", var2 + ".action");
         }

         var1.setAction(var3);

         try {
            var1.setText(this.getString(var2 + ".text"));
         } catch (MissingResourceException var8) {
         }

         if (var3 instanceof JComponentModifier) {
            ((JComponentModifier)var3).addJComponent(var1);
         }
      } catch (MissingResourceException var9) {
      }

      String var10;
      try {
         var10 = this.getString(var2 + ".icon");
         URL var4 = this.actions.getClass().getResource(var10);
         if (var4 != null) {
            var1.setIcon(new ImageIcon(var4));
         }
      } catch (MissingResourceException var7) {
      }

      try {
         var10 = this.getString(var2 + ".mnemonic");
         if (var10.length() != 1) {
            throw new ResourceFormatException("Malformed mnemonic", this.bundle.getClass().getName(), var2 + ".mnemonic");
         }

         var1.setMnemonic(var10.charAt(0));
      } catch (MissingResourceException var6) {
      }

      try {
         var10 = this.getString(var2 + ".tooltip");
         if (var10 != null) {
            var1.setToolTipText(var10);
         }
      } catch (MissingResourceException var5) {
      }

   }
}
