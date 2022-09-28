package ui;

import aggressor.ui.UseSynthetica;
import dialog.DialogManager;
import dialog.DialogUtils;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Navigator extends JComponent implements ListSelectionListener {
   protected CardLayout options = new CardLayout();
   protected JList navigator = new JList();
   protected JPanel switcher = new JPanel();
   protected Map icons = new HashMap();

   public Navigator() {
      this.switcher.setLayout(this.options);
      this.navigator.setFixedCellWidth(125);
      this.setLayout(new BorderLayout());
      this.add(DialogUtils.wrapComponent(new JScrollPane(this.navigator), 5), "West");
      this.add(DialogUtils.wrapComponent(this.switcher, 5), "Center");
      this.navigator.setCellRenderer(new CellRenderer());
      this.navigator.addListSelectionListener(this);
      this.navigator.setModel(new DefaultListModel());
   }

   public void valueChanged(ListSelectionEvent var1) {
      this.options.show(this.switcher, (String)this.navigator.getSelectedValue());
   }

   public void set(String var1) {
      this.navigator.setSelectedValue(var1, true);
      this.options.show(this.switcher, var1);
   }

   public void addPage(String var1, Icon var2, String var3, JComponent var4) {
      JPanel var5 = new JPanel();
      var5.setLayout(new BorderLayout());
      var5.add(DialogUtils.description(var3), "North");
      var5.add(DialogUtils.top(var4), "Center");
      this.icons.put(var1, var2);
      DefaultListModel var6 = (DefaultListModel)((DefaultListModel)this.navigator.getModel());
      var6.addElement(var1);
      this.switcher.add(var5, var1);
   }

   public static void main(String[] var0) {
      (new UseSynthetica()).setup();
      JFrame var1 = DialogUtils.dialog("Hello World", 640, 480);
      Navigator var2 = new Navigator();
      DialogManager var3 = new DialogManager(var1);
      var3.startGroup("console");
      var3.text("user", "User:", 20);
      var3.text("pass", "Password:", 20);
      var3.text("host", "Host:", 20);
      var3.text("port", "Port:", 10);
      var3.endGroup();
      var2.addPage("Console", new ImageIcon("./resources/cc/black/png/monitor_icon&16.png"), "This is your opportunity to edit console preferences", var3.layout("console"));
      var3.startGroup("console2");
      var3.text("user", "User A:", 20);
      var3.text("pass", "Password:", 20);
      var3.text("host", "Host:", 20);
      var3.text("port", "Port:", 10);
      var3.text("port", "Port:", 10);
      var3.text("port", "Port:", 10);
      var3.endGroup();
      var2.addPage("Console II", new ImageIcon("./resources/cc/black/png/monitor_icon&16.png"), "This is another opportunity to edit stuff. I think you know the drill by now.", var3.layout("console2"));
      var1.add(var2, "Center");
      var1.add(DialogUtils.center((JComponent)var3.action("Close")), "South");
      var1.setVisible(true);
   }

   private class CellRenderer extends JLabel implements ListCellRenderer {
      private CellRenderer() {
      }

      public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
         String var6 = var2.toString();
         this.setText(var6);
         this.setIcon((Icon)Navigator.this.icons.get(var2));
         if (var4) {
            this.setBackground(var1.getSelectionBackground());
            this.setForeground(var1.getSelectionForeground());
         } else {
            this.setBackground(var1.getBackground());
            this.setForeground(var1.getForeground());
         }

         this.setEnabled(var1.isEnabled());
         this.setFont(var1.getFont());
         this.setOpaque(true);
         return this;
      }

      // $FF: synthetic method
      CellRenderer(Object var2) {
         this();
      }
   }
}
