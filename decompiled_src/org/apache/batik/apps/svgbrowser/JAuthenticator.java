package org.apache.batik.apps.svgbrowser;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class JAuthenticator extends Authenticator {
   public static final String TITLE = "JAuthenticator.title";
   public static final String LABEL_SITE = "JAuthenticator.label.site";
   public static final String LABEL_REQ = "JAuthenticator.label.req";
   public static final String LABEL_USERID = "JAuthenticator.label.userID";
   public static final String LABEL_PASSWORD = "JAuthenticator.label.password";
   public static final String LABEL_CANCEL = "JAuthenticator.label.cancel";
   public static final String LABEL_OK = "JAuthenticator.label.ok";
   protected JDialog window;
   protected JButton cancelButton;
   protected JButton okButton;
   protected JLabel label1;
   protected JLabel label2;
   protected JTextField JUserID;
   protected JPasswordField JPassword;
   final Object lock = new Object();
   private boolean result;
   private volatile boolean wasNotified;
   private String userID;
   private char[] password;
   ActionListener okListener = new ActionListener() {
      public void actionPerformed(ActionEvent var1) {
         synchronized(JAuthenticator.this.lock) {
            JAuthenticator.this.window.setVisible(false);
            JAuthenticator.this.userID = JAuthenticator.this.JUserID.getText();
            JAuthenticator.this.password = JAuthenticator.this.JPassword.getPassword();
            JAuthenticator.this.JPassword.setText("");
            JAuthenticator.this.result = true;
            JAuthenticator.this.wasNotified = true;
            JAuthenticator.this.lock.notifyAll();
         }
      }
   };
   ActionListener cancelListener = new ActionListener() {
      public void actionPerformed(ActionEvent var1) {
         synchronized(JAuthenticator.this.lock) {
            JAuthenticator.this.window.setVisible(false);
            JAuthenticator.this.userID = null;
            JAuthenticator.this.JUserID.setText("");
            JAuthenticator.this.password = null;
            JAuthenticator.this.JPassword.setText("");
            JAuthenticator.this.result = false;
            JAuthenticator.this.wasNotified = true;
            JAuthenticator.this.lock.notifyAll();
         }
      }
   };

   public JAuthenticator() {
      this.initWindow();
   }

   protected void initWindow() {
      String var1 = Resources.getString("JAuthenticator.title");
      this.window = new JDialog((Frame)null, var1, true);
      Container var2 = this.window.getContentPane();
      var2.setLayout(new BorderLayout());
      var2.add(this.buildAuthPanel(), "Center");
      var2.add(this.buildButtonPanel(), "South");
      this.window.pack();
      this.window.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent var1) {
            JAuthenticator.this.cancelListener.actionPerformed(new ActionEvent(var1.getWindow(), 1001, "Close"));
         }
      });
   }

   protected JComponent buildAuthPanel() {
      GridBagLayout var1 = new GridBagLayout();
      GridBagConstraints var2 = new GridBagConstraints();
      JPanel var3 = new JPanel(var1);
      var2.fill = 1;
      var2.weightx = 1.0;
      var2.gridwidth = 1;
      JLabel var4 = new JLabel(Resources.getString("JAuthenticator.label.site"));
      var4.setHorizontalAlignment(2);
      var1.setConstraints(var4, var2);
      var3.add(var4);
      var2.gridwidth = 0;
      this.label1 = new JLabel("");
      this.label1.setHorizontalAlignment(2);
      var1.setConstraints(this.label1, var2);
      var3.add(this.label1);
      var2.gridwidth = 1;
      JLabel var5 = new JLabel(Resources.getString("JAuthenticator.label.req"));
      var5.setHorizontalAlignment(2);
      var1.setConstraints(var5, var2);
      var3.add(var5);
      var2.gridwidth = 0;
      this.label2 = new JLabel("");
      this.label2.setHorizontalAlignment(2);
      var1.setConstraints(this.label2, var2);
      var3.add(this.label2);
      var2.gridwidth = 1;
      JLabel var6 = new JLabel(Resources.getString("JAuthenticator.label.userID"));
      var6.setHorizontalAlignment(2);
      var1.setConstraints(var6, var2);
      var3.add(var6);
      var2.gridwidth = 0;
      this.JUserID = new JTextField(20);
      var1.setConstraints(this.JUserID, var2);
      var3.add(this.JUserID);
      var2.gridwidth = 1;
      JLabel var7 = new JLabel(Resources.getString("JAuthenticator.label.password"));
      var7.setHorizontalAlignment(2);
      var1.setConstraints(var7, var2);
      var3.add(var7);
      var2.gridwidth = 0;
      this.JPassword = new JPasswordField(20);
      this.JPassword.setEchoChar('*');
      this.JPassword.addActionListener(this.okListener);
      var1.setConstraints(this.JPassword, var2);
      var3.add(this.JPassword);
      return var3;
   }

   protected JComponent buildButtonPanel() {
      JPanel var1 = new JPanel();
      this.cancelButton = new JButton(Resources.getString("JAuthenticator.label.cancel"));
      this.cancelButton.addActionListener(this.cancelListener);
      var1.add(this.cancelButton);
      this.okButton = new JButton(Resources.getString("JAuthenticator.label.ok"));
      this.okButton.addActionListener(this.okListener);
      var1.add(this.okButton);
      return var1;
   }

   public PasswordAuthentication getPasswordAuthentication() {
      synchronized(this.lock) {
         EventQueue.invokeLater(new Runnable() {
            public void run() {
               JAuthenticator.this.label1.setText(JAuthenticator.this.getRequestingSite().getHostName());
               JAuthenticator.this.label2.setText(JAuthenticator.this.getRequestingPrompt());
               JAuthenticator.this.window.setVisible(true);
            }
         });
         this.wasNotified = false;

         while(!this.wasNotified) {
            try {
               this.lock.wait();
            } catch (InterruptedException var4) {
            }
         }

         return !this.result ? null : new PasswordAuthentication(this.userID, this.password);
      }
   }
}
