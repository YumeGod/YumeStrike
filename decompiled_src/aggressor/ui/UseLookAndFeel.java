package aggressor.ui;

import javax.swing.UIManager;

public abstract class UseLookAndFeel {
   public static void set(String var0, boolean var1) {
      if (var1) {
         UIManager.put(var0, Boolean.TRUE);
      } else {
         UIManager.put(var0, Boolean.FALSE);
      }

   }

   public abstract void setup();
}
