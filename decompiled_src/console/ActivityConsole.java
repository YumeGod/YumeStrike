package console;

import aggressor.Prefs;
import java.awt.Color;
import java.util.Properties;
import javax.swing.JLabel;

public class ActivityConsole extends Console implements Activity {
   protected JLabel label;
   protected Color original;

   public void registerLabel(JLabel var1) {
      this.label = var1;
      this.original = var1.getForeground();
   }

   public void resetNotification() {
      this.label.setForeground(this.original);
   }

   protected void appendToConsole(String var1) {
      super.appendToConsole(var1);
      if (var1.length() > 0 && this.label != null && !this.isShowing()) {
         this.label.setForeground(Prefs.getPreferences().getColor("tab.highlight.color", "#0000ff"));
      }

   }

   public ActivityConsole(boolean var1) {
      super(new Properties(), var1);
   }

   public ActivityConsole(Properties var1, boolean var2) {
      super(var1, var2);
   }
}
