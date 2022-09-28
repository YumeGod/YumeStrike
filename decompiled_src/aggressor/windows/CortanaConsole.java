package aggressor.windows;

import console.Console;
import console.ConsolePopup;
import cortana.ConsoleInterface;
import cortana.Cortana;
import cortana.CortanaPipe;
import cortana.CortanaTabCompletion;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Stack;
import javax.swing.JTextField;

public class CortanaConsole implements CortanaPipe.CortanaPipeListener, ActionListener, ConsolePopup {
   protected Console console = null;
   protected Cortana engine = null;
   protected ConsoleInterface myinterface = null;

   public CortanaConsole(Cortana var1) {
      this.console = new Console();
      this.console.updatePrompt("\u001faggressor\u000f> ");
      var1.addTextListener(this);
      this.console.getInput().addActionListener(this);
      this.engine = var1;
      this.myinterface = var1.getConsoleInterface();
      new CortanaTabCompletion(this.console, var1);
      this.console.setPopupMenu(this);
   }

   public Console getConsole() {
      return this.console;
   }

   public void showPopup(String var1, MouseEvent var2) {
      this.engine.getMenuBuilder().installMenu(var2, "aggressor", new Stack());
   }

   public void actionPerformed(ActionEvent var1) {
      String var2 = var1.getActionCommand();
      this.console.append("\u001faggressor\u000f> " + var2 + "\n");
      ((JTextField)var1.getSource()).setText("");
      if (!"".equals(var2)) {
         this.myinterface.processCommand(var2);
      }

   }

   public void read(String var1) {
      this.console.append(var1 + "\n");
   }
}
