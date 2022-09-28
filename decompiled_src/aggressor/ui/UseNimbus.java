package aggressor.ui;

import javax.swing.UIManager;

public class UseNimbus extends UseLookAndFeel {
   public void setup() {
      try {
         UIManager.LookAndFeelInfo[] var1 = UIManager.getInstalledLookAndFeels();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            UIManager.LookAndFeelInfo var4 = var1[var3];
            if ("Nimbus".equals(var4.getName())) {
               UIManager.setLookAndFeel(var4.getClassName());
               break;
            }
         }
      } catch (Exception var5) {
      }

   }
}
