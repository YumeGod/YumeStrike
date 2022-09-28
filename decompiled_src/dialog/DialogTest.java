package dialog;

import aggressor.ui.UseSynthetica;
import java.awt.event.ActionEvent;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class DialogTest {
   public static void main(String[] var0) {
      (new UseSynthetica()).setup();
      JFrame var1 = DialogUtils.dialog("Hello World", 640, 480);
      DialogManager var2 = new DialogManager(var1);
      var2.addDialogListener(new DialogListener() {
         public void dialogAction(ActionEvent var1, Map var2) {
            System.err.println(var2);
         }
      });
      var2.set("user", "msf");
      var2.set("pass", "test");
      var2.set("host", "127.0.0.1");
      var2.set("port", "55553");
      var2.text("user", "User:", 20);
      var2.text("pass", "Password:", 20);
      var2.text("host", "Host:", 20);
      var2.text("port", "Port:", 10);
      JButton var3 = var2.action("OK");
      var1.add(DialogUtils.description("This is the connect dialog"), "North");
      var1.add(var2.layout(), "Center");
      var1.add(DialogUtils.center((JComponent)var3), "South");
      var1.pack();
      var1.setVisible(true);
   }
}
