package ui;

import common.CommonUtils;
import dialog.SolidIcon;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ColorPanel extends JPanel {
   protected LinkedList buttons = new LinkedList();
   protected Map fcolors = new HashMap();
   protected Map bcolors = new HashMap();

   public ColorPanel() {
      this.setLayout(new FlowLayout(0));
      this.add(" ", "", Color.BLACK, Color.WHITE);
      this.add("G", "good", ATable.FORE_GOOD, ATable.BACK_GOOD);
      this.add("B", "bad", ATable.FORE_BAD, ATable.BACK_BAD);
      this.add("N", "neutral", ATable.FORE_NEUTRAL, ATable.BACK_NEUTRAL);
      this.add("I", "ignore", ATable.FORE_IGNORE, ATable.BACK_IGNORE);
      this.add("C", "cancel", ATable.FORE_CANCEL, ATable.BACK_CANCEL);
   }

   public static boolean isColorAction(String var0) {
      Set var1 = CommonUtils.toSet("good, bad, neutral, ignore, cancel");
      var1.add("");
      return var1.contains(var0);
   }

   public Color getForeColor(String var1) {
      return var1 != null && !"".equals(var1) ? (Color)this.fcolors.get(var1) : null;
   }

   public Color getBackColor(String var1) {
      return var1 != null && !"".equals(var1) ? (Color)this.bcolors.get(var1) : null;
   }

   public void addActionListener(ActionListener var1) {
      Iterator var2 = this.buttons.iterator();

      while(var2.hasNext()) {
         JButton var3 = (JButton)var2.next();
         var3.addActionListener(var1);
      }

   }

   public void add(String var1, String var2, Color var3, Color var4) {
      JButton var5 = new JButton("");
      var5.setIcon(new SolidIcon(var4, 16, 16));
      var5.setForeground(var3);
      var5.setBackground(var4);
      var5.setOpaque(false);
      var5.setActionCommand(var2);
      this.fcolors.put(var2, var3);
      this.bcolors.put(var2, var4);
      this.add(var5);
      this.buttons.add(var5);
   }
}
