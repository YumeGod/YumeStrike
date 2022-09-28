package org.apache.bcel.verifier;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.LayoutManager;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;

public class VerifyDialog extends JDialog {
   private JPanel ivjJDialogContentPane = null;
   private JPanel ivjPass1Panel = null;
   private JPanel ivjPass2Panel = null;
   private JPanel ivjPass3Panel = null;
   private JButton ivjPass1Button = null;
   private JButton ivjPass2Button = null;
   private JButton ivjPass3Button = null;
   IvjEventHandler ivjEventHandler = new IvjEventHandler();
   private String class_name = "java.lang.Object";
   private static int classes_to_verify;
   private JButton ivjFlushButton = null;

   public VerifyDialog() {
      this.initialize();
   }

   public VerifyDialog(Dialog owner) {
      super(owner);
   }

   public VerifyDialog(Dialog owner, String title) {
      super(owner, title);
   }

   public VerifyDialog(Dialog owner, String title, boolean modal) {
      super(owner, title, modal);
   }

   public VerifyDialog(Dialog owner, boolean modal) {
      super(owner, modal);
   }

   public VerifyDialog(Frame owner) {
      super(owner);
   }

   public VerifyDialog(Frame owner, String title) {
      super(owner, title);
   }

   public VerifyDialog(Frame owner, String title, boolean modal) {
      super(owner, title, modal);
   }

   public VerifyDialog(Frame owner, boolean modal) {
      super(owner, modal);
   }

   public VerifyDialog(String fully_qualified_class_name) {
      int dotclasspos = fully_qualified_class_name.lastIndexOf(".class");
      if (dotclasspos != -1) {
         fully_qualified_class_name = fully_qualified_class_name.substring(0, dotclasspos);
      }

      fully_qualified_class_name = fully_qualified_class_name.replace('/', '.');
      this.class_name = fully_qualified_class_name;
      this.initialize();
   }

   private void connEtoC1(ActionEvent arg1) {
      try {
         this.pass1Button_ActionPerformed(arg1);
      } catch (Throwable var3) {
         this.handleException(var3);
      }

   }

   private void connEtoC2(ActionEvent arg1) {
      try {
         this.pass2Button_ActionPerformed(arg1);
      } catch (Throwable var3) {
         this.handleException(var3);
      }

   }

   private void connEtoC3(ActionEvent arg1) {
      try {
         this.pass4Button_ActionPerformed(arg1);
      } catch (Throwable var3) {
         this.handleException(var3);
      }

   }

   private void connEtoC4(ActionEvent arg1) {
      try {
         this.flushButton_ActionPerformed(arg1);
      } catch (Throwable var3) {
         this.handleException(var3);
      }

   }

   public void flushButton_ActionPerformed(ActionEvent actionEvent) {
      VerifierFactory.getVerifier(this.class_name).flush();
      Repository.removeClass(this.class_name);
      this.getPass1Panel().setBackground(Color.gray);
      this.getPass1Panel().repaint();
      this.getPass2Panel().setBackground(Color.gray);
      this.getPass2Panel().repaint();
      this.getPass3Panel().setBackground(Color.gray);
      this.getPass3Panel().repaint();
   }

   private JButton getFlushButton() {
      if (this.ivjFlushButton == null) {
         try {
            this.ivjFlushButton = new JButton();
            this.ivjFlushButton.setName("FlushButton");
            this.ivjFlushButton.setText("Flush: Forget old verification results");
            this.ivjFlushButton.setBackground(SystemColor.controlHighlight);
            this.ivjFlushButton.setBounds(60, 215, 300, 30);
            this.ivjFlushButton.setForeground(Color.red);
            this.ivjFlushButton.setActionCommand("FlushButton");
         } catch (Throwable var2) {
            this.handleException(var2);
         }
      }

      return this.ivjFlushButton;
   }

   private JPanel getJDialogContentPane() {
      if (this.ivjJDialogContentPane == null) {
         try {
            this.ivjJDialogContentPane = new JPanel();
            this.ivjJDialogContentPane.setName("JDialogContentPane");
            this.ivjJDialogContentPane.setLayout((LayoutManager)null);
            this.getJDialogContentPane().add(this.getPass1Panel(), this.getPass1Panel().getName());
            this.getJDialogContentPane().add(this.getPass3Panel(), this.getPass3Panel().getName());
            this.getJDialogContentPane().add(this.getPass2Panel(), this.getPass2Panel().getName());
            this.getJDialogContentPane().add(this.getPass1Button(), this.getPass1Button().getName());
            this.getJDialogContentPane().add(this.getPass2Button(), this.getPass2Button().getName());
            this.getJDialogContentPane().add(this.getPass3Button(), this.getPass3Button().getName());
            this.getJDialogContentPane().add(this.getFlushButton(), this.getFlushButton().getName());
         } catch (Throwable var2) {
            this.handleException(var2);
         }
      }

      return this.ivjJDialogContentPane;
   }

   private JButton getPass1Button() {
      if (this.ivjPass1Button == null) {
         try {
            this.ivjPass1Button = new JButton();
            this.ivjPass1Button.setName("Pass1Button");
            this.ivjPass1Button.setText("Pass1: Verify binary layout of .class file");
            this.ivjPass1Button.setBackground(SystemColor.controlHighlight);
            this.ivjPass1Button.setBounds(100, 40, 300, 30);
            this.ivjPass1Button.setActionCommand("Button1");
         } catch (Throwable var2) {
            this.handleException(var2);
         }
      }

      return this.ivjPass1Button;
   }

   private JPanel getPass1Panel() {
      if (this.ivjPass1Panel == null) {
         try {
            this.ivjPass1Panel = new JPanel();
            this.ivjPass1Panel.setName("Pass1Panel");
            this.ivjPass1Panel.setLayout((LayoutManager)null);
            this.ivjPass1Panel.setBackground(SystemColor.controlShadow);
            this.ivjPass1Panel.setBounds(30, 30, 50, 50);
         } catch (Throwable var2) {
            this.handleException(var2);
         }
      }

      return this.ivjPass1Panel;
   }

   private JButton getPass2Button() {
      if (this.ivjPass2Button == null) {
         try {
            this.ivjPass2Button = new JButton();
            this.ivjPass2Button.setName("Pass2Button");
            this.ivjPass2Button.setText("Pass 2: Verify static .class file constraints");
            this.ivjPass2Button.setBackground(SystemColor.controlHighlight);
            this.ivjPass2Button.setBounds(100, 100, 300, 30);
            this.ivjPass2Button.setActionCommand("Button2");
         } catch (Throwable var2) {
            this.handleException(var2);
         }
      }

      return this.ivjPass2Button;
   }

   private JPanel getPass2Panel() {
      if (this.ivjPass2Panel == null) {
         try {
            this.ivjPass2Panel = new JPanel();
            this.ivjPass2Panel.setName("Pass2Panel");
            this.ivjPass2Panel.setLayout((LayoutManager)null);
            this.ivjPass2Panel.setBackground(SystemColor.controlShadow);
            this.ivjPass2Panel.setBounds(30, 90, 50, 50);
         } catch (Throwable var2) {
            this.handleException(var2);
         }
      }

      return this.ivjPass2Panel;
   }

   private JButton getPass3Button() {
      if (this.ivjPass3Button == null) {
         try {
            this.ivjPass3Button = new JButton();
            this.ivjPass3Button.setName("Pass3Button");
            this.ivjPass3Button.setText("Passes 3a+3b: Verify code arrays");
            this.ivjPass3Button.setBackground(SystemColor.controlHighlight);
            this.ivjPass3Button.setBounds(100, 160, 300, 30);
            this.ivjPass3Button.setActionCommand("Button2");
         } catch (Throwable var2) {
            this.handleException(var2);
         }
      }

      return this.ivjPass3Button;
   }

   private JPanel getPass3Panel() {
      if (this.ivjPass3Panel == null) {
         try {
            this.ivjPass3Panel = new JPanel();
            this.ivjPass3Panel.setName("Pass3Panel");
            this.ivjPass3Panel.setLayout((LayoutManager)null);
            this.ivjPass3Panel.setBackground(SystemColor.controlShadow);
            this.ivjPass3Panel.setBounds(30, 150, 50, 50);
         } catch (Throwable var2) {
            this.handleException(var2);
         }
      }

      return this.ivjPass3Panel;
   }

   private void handleException(Throwable exception) {
      System.out.println("--------- UNCAUGHT EXCEPTION ---------");
      exception.printStackTrace(System.out);
   }

   private void initConnections() throws Exception {
      this.getPass1Button().addActionListener(this.ivjEventHandler);
      this.getPass2Button().addActionListener(this.ivjEventHandler);
      this.getPass3Button().addActionListener(this.ivjEventHandler);
      this.getFlushButton().addActionListener(this.ivjEventHandler);
   }

   private void initialize() {
      try {
         this.setName("VerifyDialog");
         this.setDefaultCloseOperation(2);
         this.setSize(430, 280);
         this.setVisible(true);
         this.setModal(true);
         this.setResizable(false);
         this.setContentPane(this.getJDialogContentPane());
         this.initConnections();
      } catch (Throwable var2) {
         this.handleException(var2);
      }

      this.setTitle("'" + this.class_name + "' verification - JustIce / BCEL");
   }

   public static void main(String[] args) {
      classes_to_verify = args.length;

      for(int i = 0; i < args.length; ++i) {
         try {
            VerifyDialog aVerifyDialog = new VerifyDialog(args[i]);
            aVerifyDialog.setModal(true);
            aVerifyDialog.addWindowListener(new WindowAdapter() {
               public void windowClosing(WindowEvent e) {
                  VerifyDialog.classes_to_verify--;
                  if (VerifyDialog.classes_to_verify == 0) {
                     System.exit(0);
                  }

               }
            });
            aVerifyDialog.setVisible(true);
         } catch (Throwable var3) {
            System.err.println("Exception occurred in main() of javax.swing.JDialog");
            var3.printStackTrace(System.out);
         }
      }

   }

   public void pass1Button_ActionPerformed(ActionEvent actionEvent) {
      Verifier v = VerifierFactory.getVerifier(this.class_name);
      VerificationResult vr = v.doPass1();
      if (vr.getStatus() == 1) {
         this.getPass1Panel().setBackground(Color.green);
         this.getPass1Panel().repaint();
      }

      if (vr.getStatus() == 2) {
         this.getPass1Panel().setBackground(Color.red);
         this.getPass1Panel().repaint();
      }

   }

   public void pass2Button_ActionPerformed(ActionEvent actionEvent) {
      this.pass1Button_ActionPerformed(actionEvent);
      Verifier v = VerifierFactory.getVerifier(this.class_name);
      VerificationResult vr = v.doPass2();
      if (vr.getStatus() == 1) {
         this.getPass2Panel().setBackground(Color.green);
         this.getPass2Panel().repaint();
      }

      if (vr.getStatus() == 0) {
         this.getPass2Panel().setBackground(Color.yellow);
         this.getPass2Panel().repaint();
      }

      if (vr.getStatus() == 2) {
         this.getPass2Panel().setBackground(Color.red);
         this.getPass2Panel().repaint();
      }

   }

   public void pass4Button_ActionPerformed(ActionEvent actionEvent) {
      this.pass2Button_ActionPerformed(actionEvent);
      Color color = Color.green;
      Verifier v = VerifierFactory.getVerifier(this.class_name);
      VerificationResult vr = v.doPass2();
      if (vr.getStatus() == 1) {
         JavaClass jc = Repository.lookupClass(this.class_name);
         int nr = jc.getMethods().length;

         for(int i = 0; i < nr; ++i) {
            vr = v.doPass3b(i);
            if (vr.getStatus() != 1) {
               color = Color.red;
               break;
            }
         }
      } else {
         color = Color.yellow;
      }

      this.getPass3Panel().setBackground(color);
      this.getPass3Panel().repaint();
   }

   class IvjEventHandler implements ActionListener {
      public void actionPerformed(ActionEvent e) {
         if (e.getSource() == VerifyDialog.this.getPass1Button()) {
            VerifyDialog.this.connEtoC1(e);
         }

         if (e.getSource() == VerifyDialog.this.getPass2Button()) {
            VerifyDialog.this.connEtoC2(e);
         }

         if (e.getSource() == VerifyDialog.this.getPass3Button()) {
            VerifyDialog.this.connEtoC3(e);
         }

         if (e.getSource() == VerifyDialog.this.getFlushButton()) {
            VerifyDialog.this.connEtoC4(e);
         }

      }
   }
}
