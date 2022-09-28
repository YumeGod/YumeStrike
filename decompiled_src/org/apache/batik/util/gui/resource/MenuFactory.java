package org.apache.batik.util.gui.resource;

import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import org.apache.batik.util.resources.ResourceFormatException;
import org.apache.batik.util.resources.ResourceManager;

public class MenuFactory extends ResourceManager {
   private static final String TYPE_MENU = "MENU";
   private static final String TYPE_ITEM = "ITEM";
   private static final String TYPE_RADIO = "RADIO";
   private static final String TYPE_CHECK = "CHECK";
   private static final String SEPARATOR = "-";
   private static final String TYPE_SUFFIX = ".type";
   private static final String TEXT_SUFFIX = ".text";
   private static final String MNEMONIC_SUFFIX = ".mnemonic";
   private static final String ACCELERATOR_SUFFIX = ".accelerator";
   private static final String ACTION_SUFFIX = ".action";
   private static final String SELECTED_SUFFIX = ".selected";
   private static final String ENABLED_SUFFIX = ".enabled";
   private static final String ICON_SUFFIX = ".icon";
   private ActionMap actions;
   private ButtonGroup buttonGroup;

   public MenuFactory(ResourceBundle var1, ActionMap var2) {
      super(var1);
      this.actions = var2;
      this.buttonGroup = null;
   }

   public JMenuBar createJMenuBar(String var1) throws MissingResourceException, ResourceFormatException, MissingListenerException {
      return this.createJMenuBar(var1, (String)null);
   }

   public JMenuBar createJMenuBar(String var1, String var2) throws MissingResourceException, ResourceFormatException, MissingListenerException {
      JMenuBar var3 = new JMenuBar();
      List var4 = this.getSpecializedStringList(var1, var2);
      Iterator var5 = var4.iterator();

      while(var5.hasNext()) {
         var3.add(this.createJMenuComponent((String)var5.next(), var2));
      }

      return var3;
   }

   protected String getSpecializedString(String var1, String var2) {
      String var3;
      try {
         var3 = this.getString(var1 + '.' + var2);
      } catch (MissingResourceException var5) {
         var3 = this.getString(var1);
      }

      return var3;
   }

   protected List getSpecializedStringList(String var1, String var2) {
      List var3;
      try {
         var3 = this.getStringList(var1 + '.' + var2);
      } catch (MissingResourceException var5) {
         var3 = this.getStringList(var1);
      }

      return var3;
   }

   protected boolean getSpecializedBoolean(String var1, String var2) {
      boolean var3;
      try {
         var3 = this.getBoolean(var1 + '.' + var2);
      } catch (MissingResourceException var5) {
         var3 = this.getBoolean(var1);
      }

      return var3;
   }

   protected JComponent createJMenuComponent(String var1, String var2) throws MissingResourceException, ResourceFormatException, MissingListenerException {
      if (var1.equals("-")) {
         this.buttonGroup = null;
         return new JSeparator();
      } else {
         String var3 = this.getSpecializedString(var1 + ".type", var2);
         Object var4 = null;
         if (var3.equals("RADIO")) {
            if (this.buttonGroup == null) {
               this.buttonGroup = new ButtonGroup();
            }
         } else {
            this.buttonGroup = null;
         }

         if (var3.equals("MENU")) {
            var4 = this.createJMenu(var1, var2);
         } else if (var3.equals("ITEM")) {
            var4 = this.createJMenuItem(var1, var2);
         } else if (var3.equals("RADIO")) {
            var4 = this.createJRadioButtonMenuItem(var1, var2);
            this.buttonGroup.add((AbstractButton)var4);
         } else {
            if (!var3.equals("CHECK")) {
               throw new ResourceFormatException("Malformed resource", this.bundle.getClass().getName(), var1 + ".type");
            }

            var4 = this.createJCheckBoxMenuItem(var1, var2);
         }

         return (JComponent)var4;
      }
   }

   public JMenu createJMenu(String var1) throws MissingResourceException, ResourceFormatException, MissingListenerException {
      return this.createJMenu(var1, (String)null);
   }

   public JMenu createJMenu(String var1, String var2) throws MissingResourceException, ResourceFormatException, MissingListenerException {
      JMenu var3 = new JMenu(this.getSpecializedString(var1 + ".text", var2));
      this.initializeJMenuItem(var3, var1, var2);
      List var4 = this.getSpecializedStringList(var1, var2);
      Iterator var5 = var4.iterator();

      while(var5.hasNext()) {
         var3.add(this.createJMenuComponent((String)var5.next(), var2));
      }

      return var3;
   }

   public JMenuItem createJMenuItem(String var1) throws MissingResourceException, ResourceFormatException, MissingListenerException {
      return this.createJMenuItem(var1, (String)null);
   }

   public JMenuItem createJMenuItem(String var1, String var2) throws MissingResourceException, ResourceFormatException, MissingListenerException {
      JMenuItem var3 = new JMenuItem(this.getSpecializedString(var1 + ".text", var2));
      this.initializeJMenuItem(var3, var1, var2);
      return var3;
   }

   public JRadioButtonMenuItem createJRadioButtonMenuItem(String var1) throws MissingResourceException, ResourceFormatException, MissingListenerException {
      return this.createJRadioButtonMenuItem(var1, (String)null);
   }

   public JRadioButtonMenuItem createJRadioButtonMenuItem(String var1, String var2) throws MissingResourceException, ResourceFormatException, MissingListenerException {
      JRadioButtonMenuItem var3 = new JRadioButtonMenuItem(this.getSpecializedString(var1 + ".text", var2));
      this.initializeJMenuItem(var3, var1, var2);

      try {
         var3.setSelected(this.getSpecializedBoolean(var1 + ".selected", var2));
      } catch (MissingResourceException var5) {
      }

      return var3;
   }

   public JCheckBoxMenuItem createJCheckBoxMenuItem(String var1) throws MissingResourceException, ResourceFormatException, MissingListenerException {
      return this.createJCheckBoxMenuItem(var1, (String)null);
   }

   public JCheckBoxMenuItem createJCheckBoxMenuItem(String var1, String var2) throws MissingResourceException, ResourceFormatException, MissingListenerException {
      JCheckBoxMenuItem var3 = new JCheckBoxMenuItem(this.getSpecializedString(var1 + ".text", var2));
      this.initializeJMenuItem(var3, var1, var2);

      try {
         var3.setSelected(this.getSpecializedBoolean(var1 + ".selected", var2));
      } catch (MissingResourceException var5) {
      }

      return var3;
   }

   protected void initializeJMenuItem(JMenuItem var1, String var2, String var3) throws ResourceFormatException, MissingListenerException {
      try {
         Action var4 = this.actions.getAction(this.getSpecializedString(var2 + ".action", var3));
         if (var4 == null) {
            throw new MissingListenerException("", "Action", var2 + ".action");
         }

         var1.setAction(var4);
         var1.setText(this.getSpecializedString(var2 + ".text", var3));
         if (var4 instanceof JComponentModifier) {
            ((JComponentModifier)var4).addJComponent(var1);
         }
      } catch (MissingResourceException var10) {
      }

      String var11;
      try {
         var11 = this.getSpecializedString(var2 + ".icon", var3);
         URL var5 = this.actions.getClass().getResource(var11);
         if (var5 != null) {
            var1.setIcon(new ImageIcon(var5));
         }
      } catch (MissingResourceException var9) {
      }

      try {
         var11 = this.getSpecializedString(var2 + ".mnemonic", var3);
         if (var11.length() != 1) {
            throw new ResourceFormatException("Malformed mnemonic", this.bundle.getClass().getName(), var2 + ".mnemonic");
         }

         var1.setMnemonic(var11.charAt(0));
      } catch (MissingResourceException var8) {
      }

      try {
         if (!(var1 instanceof JMenu)) {
            var11 = this.getSpecializedString(var2 + ".accelerator", var3);
            KeyStroke var12 = KeyStroke.getKeyStroke(var11);
            if (var12 == null) {
               throw new ResourceFormatException("Malformed accelerator", this.bundle.getClass().getName(), var2 + ".accelerator");
            }

            var1.setAccelerator(var12);
         }
      } catch (MissingResourceException var7) {
      }

      try {
         var1.setEnabled(this.getSpecializedBoolean(var2 + ".enabled", var3));
      } catch (MissingResourceException var6) {
      }

   }
}
