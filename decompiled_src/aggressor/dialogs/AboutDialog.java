package aggressor.dialogs;

import aggressor.Aggressor;
import common.AObject;
import common.CommonUtils;
import dialog.DialogUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

public class AboutDialog extends AObject {
   public void show() {
      JFrame var1 = DialogUtils.dialog("About", 320, 200);
      var1.setLayout(new BorderLayout());
      JLabel var2 = new JLabel(DialogUtils.getIcon("resources/armitage-logo.gif"));
      var2.setBackground(Color.black);
      var2.setForeground(Color.gray);
      var2.setOpaque(true);
      JTextArea var3 = new JTextArea();
      var3.setBackground(Color.black);
      var3.setForeground(Color.gray);
      var3.setEditable(false);
      var3.setFocusable(false);
      var3.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
      var3.setOpaque(false);
      var3.setLineWrap(true);
      var3.setWrapStyleWord(true);
      String var4 = CommonUtils.bString(CommonUtils.readResource("resources/about.html"));
      var2.setText(var4);
      var3.scrollRectToVisible(new Rectangle(0, 0, 1, 1));
      ((DefaultCaret)var3.getCaret()).setUpdatePolicy(1);
      JScrollPane var5 = new JScrollPane(var3, 22, 31);
      var5.setPreferredSize(new Dimension(var5.getWidth(), 100));
      String var6 = CommonUtils.bString(CommonUtils.readResource("resources/credits.txt"));
      var3.setText(var6);
      var5.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
      var1.add(var2, "Center");
      var1.add(var5, "South");
      var1.pack();
      var1.setLocationRelativeTo(Aggressor.getFrame());
      var1.setVisible(true);
   }
}
