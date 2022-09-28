package table;

import dialog.DialogUtils;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import ui.ATable;

public class FilterAndScroll extends JPanel {
   protected ATable table;
   protected JPanel myPanel;

   public FilterAndScroll(ATable var1) {
      this(var1, (JPanel)null);
   }

   public FilterAndScroll(ATable var1, JPanel var2) {
      this.table = var1;
      if (var2 != null) {
         this.myPanel = var2;
      } else {
         this.myPanel = this;
      }

      this.setLayout(new BorderLayout());
      this.add(new JScrollPane(var1), "Center");
      this.setupFindShortcutFeature();
   }

   private void setupFindShortcutFeature() {
      this.table.addActionForKey("ctrl pressed F", new AbstractAction() {
         public void actionPerformed(ActionEvent var1) {
            final FilterPanel var2 = new FilterPanel(FilterAndScroll.this.table);
            final JPanel var3 = new JPanel();
            JButton var4 = new JButton("X ");
            DialogUtils.removeBorderFromButton(var4);
            var4.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent var1) {
                  var2.clear();
                  FilterAndScroll.this.myPanel.remove(var3);
                  FilterAndScroll.this.myPanel.validate();
               }
            });
            var3.setLayout(new BorderLayout());
            var3.add(var2, "Center");
            var3.add(var4, "East");
            FilterAndScroll.this.myPanel.add(var3, "South");
            FilterAndScroll.this.myPanel.validate();
            var2.requestFocusInWindow();
            var2.requestFocus();
         }
      });
   }
}
