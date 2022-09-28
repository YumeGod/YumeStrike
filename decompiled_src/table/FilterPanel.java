package table;

import common.CommonUtils;
import filter.DataFilter;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;
import ui.ATable;
import ui.GenericTableModel;

public class FilterPanel extends JPanel implements ActionListener {
   protected JTextField filter = null;
   protected JLabel status = null;
   protected ATable table = null;
   protected JComboBox cols = null;
   protected StringBuffer desc = new StringBuffer();
   protected JToggleButton negate = new JToggleButton(" ! ");
   protected DataFilter action = new DataFilter();

   public String getColumn() {
      return this.cols.getSelectedItem().toString();
   }

   public void actionPerformed(ActionEvent var1) {
      if (!"".equals(this.filter.getText())) {
         if (CommonUtils.contains("internal, external, host, address, fhost", this.getColumn())) {
            this.action.checkNetwork(this.getColumn(), this.filter.getText(), this.negate.isSelected());
         } else if (CommonUtils.contains("rx, tx, port, fport, Size, size, pid, last", this.getColumn())) {
            this.action.checkNumber(this.getColumn(), this.filter.getText(), this.negate.isSelected());
         } else {
            this.action.checkWildcard(this.getColumn(), "*" + this.filter.getText() + "*", this.negate.isSelected());
         }

         ((GenericTableModel)this.table.getModel()).apply(this.action);
         this.filter.setText("");
         this.negate.setSelected(false);
         this.status.setText(this.action.toString() + " applied.");
      }
   }

   public void requestFocus() {
      this.filter.requestFocus();
   }

   public void clear() {
      this.status.setText("");
      this.desc = new StringBuffer();
      ((GenericTableModel)this.table.getModel()).reset();
   }

   public FilterPanel(ATable var1) {
      this.table = var1;
      this.setLayout(new BorderLayout());
      this.setBorder(new EmptyBorder(1, 1, 1, 1));
      List var2 = CommonUtils.toList((Object[])((GenericTableModel)var1.getModel()).getColumnNames());
      var2.remove(" ");
      var2.remove("D");
      var2.remove("date");
      var2.remove("Modified");
      this.cols = new JComboBox(CommonUtils.toArray((Collection)var2));
      this.filter = new JTextField(15);
      this.filter.addActionListener(this);
      JButton var3 = new JButton("Reset");
      var3.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            FilterPanel.this.action.reset();
            FilterPanel.this.clear();
         }
      });
      JPanel var4 = new JPanel();
      var4.setLayout(new FlowLayout());
      var4.add(new JLabel("Filter: "));
      var4.add(this.negate);
      var4.add(this.filter);
      var4.add(this.cols);
      this.add(var4, "West");
      JPanel var5 = new JPanel();
      var5.setLayout(new FlowLayout());
      var5.add(var3);
      this.add(var5, "East");
      this.status = new JLabel("");
      this.add(this.status, "Center");
      this.negate.setToolTipText("Negate this filter.");
   }
}
