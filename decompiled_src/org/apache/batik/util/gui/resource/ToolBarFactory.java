package org.apache.batik.util.gui.resource;

import java.util.Iterator;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JToolBar;
import org.apache.batik.util.resources.ResourceFormatException;
import org.apache.batik.util.resources.ResourceManager;

public class ToolBarFactory extends ResourceManager {
   private static final String SEPARATOR = "-";
   private ButtonFactory buttonFactory;

   public ToolBarFactory(ResourceBundle var1, ActionMap var2) {
      super(var1);
      this.buttonFactory = new ButtonFactory(var1, var2);
   }

   public JToolBar createJToolBar(String var1) throws MissingResourceException, ResourceFormatException, MissingListenerException {
      JToolBar var2 = new JToolBar();
      List var3 = this.getStringList(var1);
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();
         if (var5.equals("-")) {
            var2.add(new JToolbarSeparator());
         } else {
            var2.add(this.createJButton(var5));
         }
      }

      return var2;
   }

   public JButton createJButton(String var1) throws MissingResourceException, ResourceFormatException, MissingListenerException {
      return this.buttonFactory.createJToolbarButton(var1);
   }
}
