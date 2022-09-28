package dialog;

import common.CommonUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class FontDialog implements ItemListener, DialogListener {
   protected Font font;
   protected JLabel preview;
   protected JComboBox size;
   protected JComboBox family;
   protected JComboBox style;
   protected LinkedList listeners = new LinkedList();
   protected JFrame dialog;

   public FontDialog(Font var1) {
      this.font = var1;
   }

   public void addFontChooseListener(SafeDialogCallback var1) {
      this.listeners.add(var1);
   }

   public JComboBox act(DialogManager.DialogRow var1) {
      JComboBox var2 = (JComboBox)var1.c[1];
      var2.addItemListener(this);
      return var2;
   }

   public String getResult() {
      return this.family.getSelectedItem() + "-" + this.style.getSelectedItem().toString().toUpperCase() + "-" + this.size.getSelectedItem();
   }

   public void itemStateChanged(ItemEvent var1) {
      Font var2 = Font.decode(this.getResult());
      this.preview.setFont(var2);
      this.preview.revalidate();
   }

   public void dialogAction(ActionEvent var1, Map var2) {
      Iterator var3 = this.listeners.iterator();

      while(var3.hasNext()) {
         SafeDialogCallback var4 = (SafeDialogCallback)var3.next();
         var4.dialogResult(this.getResult());
      }

      this.dialog.dispose();
   }

   public void show() {
      this.dialog = DialogUtils.dialog("Choose a font", 640, 240);
      this.dialog.setLayout(new BorderLayout());
      DialogManager var1 = new DialogManager(this.dialog);
      var1.addDialogListener(this);
      var1.set("size", this.font.getSize() + "");
      var1.set("family", this.font.getFamily());
      String var2 = "Plain";
      if (this.font.isItalic()) {
         var2 = "Italic";
      } else if (this.font.isBold()) {
         var2 = "Bold";
      }

      var1.set("style", var2);
      GraphicsEnvironment var3 = GraphicsEnvironment.getLocalGraphicsEnvironment();
      this.family = this.act(var1.combobox("family", "Family", var3.getAvailableFontFamilyNames()));
      this.style = this.act(var1.combobox("style", "Style", CommonUtils.toArray("Bold, Italic, Plain")));
      this.size = this.act(var1.combobox("size", "Size", CommonUtils.toArray("5, 8, 9, 10, 11, 12, 13, 14, 15, 16, 20, 23, 26, 30, 33, 38")));
      this.preview = new JLabel("nEWBS gET pWNED by km-r4d h4x0rz 肉鸡");
      this.preview.setFont(this.font);
      this.preview.setBackground(Color.white);
      this.preview.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
      this.preview.setOpaque(true);
      JButton var4 = var1.action("Choose");
      this.dialog.add(var1.layout(), "North");
      this.dialog.add(this.preview, "Center");
      this.dialog.add(DialogUtils.center((JComponent)var4), "South");
      this.dialog.pack();
      this.dialog.setVisible(true);
      this.dialog.show();
   }
}
