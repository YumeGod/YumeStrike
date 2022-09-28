package org.apache.fop.render.awt.viewer;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GoToPageDialog extends JDialog {
   private JTextField pageNumberField;
   private int pageNumber = -1;

   public GoToPageDialog(Frame frame, String title, Translator translator) {
      super(frame, title, true);
      this.jbInit(translator);
      this.pack();
   }

   private void jbInit(Translator translator) {
      JPanel panel1 = new JPanel();
      GridBagLayout gridBagLayout1 = new GridBagLayout();
      JLabel pgNbLabel = new JLabel();
      this.pageNumberField = new JTextField();
      JButton okButton = new JButton();
      JButton cancelButton = new JButton();
      panel1.setLayout(gridBagLayout1);
      pgNbLabel.setText(translator.getString("Label.Page.number"));
      okButton.setText(translator.getString("Button.Ok"));
      okButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            GoToPageDialog.this.okButtonActionPerformed(e);
         }
      });
      cancelButton.setText(translator.getString("Button.Cancel"));
      cancelButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            GoToPageDialog.this.cancelButtonActionPerformed(e);
         }
      });
      panel1.setMinimumSize(new Dimension(250, 78));
      this.getContentPane().add(panel1);
      panel1.add(pgNbLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 17, 0, new Insets(10, 10, 10, 5), 0, 0));
      panel1.add(this.pageNumberField, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, 17, 1, new Insets(10, 5, 10, 10), 0, 0));
      panel1.add(okButton, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, 13, 0, new Insets(0, 0, 10, 5), 0, 0));
      panel1.add(cancelButton, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, 17, 0, new Insets(0, 10, 10, 10), 0, 0));
   }

   private void okButtonActionPerformed(ActionEvent e) {
      try {
         this.pageNumber = Integer.parseInt(this.pageNumberField.getText());
         this.dispose();
      } catch (NumberFormatException var3) {
         this.pageNumberField.setText("???");
      }

   }

   private void cancelButtonActionPerformed(ActionEvent e) {
      this.pageNumber = -1;
      this.dispose();
   }

   public int getPageNumber() {
      return this.pageNumber;
   }
}
