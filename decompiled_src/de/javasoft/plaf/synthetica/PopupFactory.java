package de.javasoft.plaf.synthetica;

import java.awt.Component;
import java.lang.reflect.Method;
import javax.swing.JComponent;

public class PopupFactory extends javax.swing.PopupFactory {
   private static PopupFactory popupFactory = new PopupFactory();
   private static javax.swing.PopupFactory storedPopupFactory;

   private PopupFactory() {
   }

   public static void install() {
      if (storedPopupFactory == null) {
         storedPopupFactory = getSharedInstance();
         setSharedInstance(popupFactory);
      }

   }

   public static void uninstall() {
      if (storedPopupFactory != null) {
         setSharedInstance(storedPopupFactory);
         storedPopupFactory = null;
      }
   }

   public javax.swing.Popup getPopup(Component var1, Component var2, int var3, int var4) {
      if (var1 instanceof JComponent) {
         JComponent var5 = (JComponent)var1;
         Integer var6 = (Integer)var5.getClientProperty("Synthetica.popupType");
         if (var6 == null) {
            var6 = SyntheticaLookAndFeel.get("Synthetica.popupType", (Component)var5) == null ? null : SyntheticaLookAndFeel.getInt("Synthetica.popupType", var5);
         }

         if (var6 != null && SyntheticaLookAndFeel.getJVMCompatibilityMode() == SyntheticaLookAndFeel.JVMCompatibilityMode.SUN) {
            try {
               Method var7 = javax.swing.PopupFactory.class.getDeclaredMethod("setPopupType", Integer.TYPE);
               var7.setAccessible(true);
               var7.invoke(this, var6);
            } catch (Exception var8) {
               new RuntimeException(var8);
            }
         }
      }

      javax.swing.Popup var9 = super.getPopup(var1, var2, var3, var4);
      return new Popup(var1, var2, var3, var4, var9);
   }
}
