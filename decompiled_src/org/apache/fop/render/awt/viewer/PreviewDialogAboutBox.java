package org.apache.fop.render.awt.viewer;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.apache.fop.Version;

public class PreviewDialogAboutBox extends Dialog implements ActionListener {
   private JButton okButton;

   public PreviewDialogAboutBox(Frame parent, Translator translator) {
      super(parent, true);
      this.enableEvents(64L);
      this.setTitle(translator.getString("About.Title"));
      this.setResizable(false);
      JPanel panel1 = new JPanel();
      JPanel panel2 = new JPanel();
      JPanel insetsPanel1 = new JPanel();
      JPanel insetsPanel2 = new JPanel();
      JPanel insetsPanel3 = new JPanel();
      this.okButton = new JButton();
      JLabel imageControl1 = new JLabel();
      imageControl1.setIcon(new ImageIcon(this.getClass().getResource("images/fop.gif")));
      JLabel label1 = new JLabel(translator.getString("About.Product"));
      JLabel label2 = new JLabel(translator.getString("About.Version") + " " + Version.getVersion());
      JLabel label3 = new JLabel(translator.getString("About.Copyright"));
      panel1.setLayout(new BorderLayout());
      panel2.setLayout(new BorderLayout());
      insetsPanel1.setLayout(new FlowLayout());
      insetsPanel2.setLayout(new FlowLayout());
      insetsPanel2.setBorder(new EmptyBorder(10, 10, 10, 10));
      insetsPanel3.setLayout(new GridLayout(3, 1));
      insetsPanel3.setBorder(new EmptyBorder(10, 10, 10, 10));
      this.okButton.setText(translator.getString("Button.Ok"));
      this.okButton.addActionListener(this);
      insetsPanel2.add(imageControl1, (Object)null);
      panel2.add(insetsPanel2, "West");
      insetsPanel3.add(label1);
      insetsPanel3.add(label2);
      insetsPanel3.add(label3);
      panel2.add(insetsPanel3, "Center");
      insetsPanel1.add(this.okButton);
      panel1.add(insetsPanel1, "South");
      panel1.add(panel2, "North");
      this.add(panel1);
      this.pack();
   }

   protected void processWindowEvent(WindowEvent e) {
      if (e.getID() == 201) {
         this.cancel();
      }

      super.processWindowEvent(e);
   }

   private void cancel() {
      this.dispose();
   }

   public void actionPerformed(ActionEvent e) {
      if (e.getSource() == this.okButton) {
         this.cancel();
      }

   }
}
