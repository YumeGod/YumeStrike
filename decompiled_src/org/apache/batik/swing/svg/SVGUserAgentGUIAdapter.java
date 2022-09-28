package org.apache.batik.swing.svg;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import org.apache.batik.util.gui.JErrorPane;

public class SVGUserAgentGUIAdapter extends SVGUserAgentAdapter {
   public Component parentComponent;

   public SVGUserAgentGUIAdapter(Component var1) {
      this.parentComponent = var1;
   }

   public void displayError(String var1) {
      JOptionPane var2 = new JOptionPane(var1, 0);
      JDialog var3 = var2.createDialog(this.parentComponent, "ERROR");
      var3.setModal(false);
      var3.setVisible(true);
   }

   public void displayError(Exception var1) {
      JErrorPane var2 = new JErrorPane(var1, 0);
      JDialog var3 = var2.createDialog(this.parentComponent, "ERROR");
      var3.setModal(false);
      var3.setVisible(true);
   }

   public void displayMessage(String var1) {
   }

   public void showAlert(String var1) {
      String var2 = "Script alert:\n" + var1;
      JOptionPane.showMessageDialog(this.parentComponent, var2);
   }

   public String showPrompt(String var1) {
      String var2 = "Script prompt:\n" + var1;
      return JOptionPane.showInputDialog(this.parentComponent, var2);
   }

   public String showPrompt(String var1, String var2) {
      String var3 = "Script prompt:\n" + var1;
      return (String)JOptionPane.showInputDialog(this.parentComponent, var3, (String)null, -1, (Icon)null, (Object[])null, var2);
   }

   public boolean showConfirm(String var1) {
      String var2 = "Script confirm:\n" + var1;
      return JOptionPane.showConfirmDialog(this.parentComponent, var2, "Confirm", 0) == 0;
   }
}
