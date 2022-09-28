package aggressor.dialogs;

import aggressor.MultiFrame;
import aggressor.Prefs;
import dialog.DialogUtils;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Iterator;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JFrame;
import ui.Navigator;

public class ConnectDialog {
   protected MultiFrame window;
   protected Navigator options = null;

   public ConnectDialog(MultiFrame var1) {
      this.window = var1;
   }

   public void show() {
      String var1 = "New Profile";
      JFrame var2 = DialogUtils.dialog("Connect", 640, 480);
      var2.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent var1) {
            ConnectDialog.this.window.closeConnect();
         }
      });
      this.options = new Navigator();
      this.options.addPage("New Profile", (Icon)null, "Thanks for using YumeStrike!", (new Connect(this.window)).getContent(var2, "neo", "password", "127.0.0.1", "50050"));
      List var3 = Prefs.getPreferences().getList("connection.profiles");

      String var5;
      for(Iterator var4 = var3.iterator(); var4.hasNext(); var1 = var5) {
         var5 = (String)var4.next();
         String var6 = Prefs.getPreferences().getString("connection.profiles." + var5 + ".user", "neo");
         String var7 = Prefs.getPreferences().getString("connection.profiles." + var5 + ".password", "password");
         String var8 = Prefs.getPreferences().getString("connection.profiles." + var5 + ".port", "50050");
         this.options.addPage(var5, (Icon)null, "Thanks for using YumeStrike!", (new Connect(this.window)).getContent(var2, var6, var7, var5, var8));
      }

      this.options.set(var1);
      var2.add(this.options, "Center");
      var2.pack();
      var2.setVisible(true);
   }
}
