package org.apache.bcel.verifier;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;

public class VerifierAppFrame extends JFrame {
   JPanel contentPane;
   JSplitPane jSplitPane1 = new JSplitPane();
   JPanel jPanel1 = new JPanel();
   JPanel jPanel2 = new JPanel();
   JSplitPane jSplitPane2 = new JSplitPane();
   JPanel jPanel3 = new JPanel();
   JList classNamesJList = new JList();
   GridLayout gridLayout1 = new GridLayout();
   JPanel messagesPanel = new JPanel();
   GridLayout gridLayout2 = new GridLayout();
   JMenuBar jMenuBar1 = new JMenuBar();
   JMenu jMenu1 = new JMenu();
   JScrollPane jScrollPane1 = new JScrollPane();
   JScrollPane messagesScrollPane = new JScrollPane();
   JScrollPane jScrollPane3 = new JScrollPane();
   GridLayout gridLayout4 = new GridLayout();
   JScrollPane jScrollPane4 = new JScrollPane();
   CardLayout cardLayout1 = new CardLayout();
   private String JUSTICE_VERSION = "JustIce by Enver Haase";
   private String current_class;
   GridLayout gridLayout3 = new GridLayout();
   JTextPane pass1TextPane = new JTextPane();
   JTextPane pass2TextPane = new JTextPane();
   JTextPane messagesTextPane = new JTextPane();
   JMenuItem newFileMenuItem = new JMenuItem();
   JSplitPane jSplitPane3 = new JSplitPane();
   JSplitPane jSplitPane4 = new JSplitPane();
   JScrollPane jScrollPane2 = new JScrollPane();
   JScrollPane jScrollPane5 = new JScrollPane();
   JScrollPane jScrollPane6 = new JScrollPane();
   JScrollPane jScrollPane7 = new JScrollPane();
   JList pass3aJList = new JList();
   JList pass3bJList = new JList();
   JTextPane pass3aTextPane = new JTextPane();
   JTextPane pass3bTextPane = new JTextPane();
   JMenu jMenu2 = new JMenu();
   JMenuItem whatisMenuItem = new JMenuItem();
   JMenuItem aboutMenuItem = new JMenuItem();

   public VerifierAppFrame() {
      this.enableEvents(64L);

      try {
         this.jbInit();
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   private void jbInit() throws Exception {
      this.contentPane = (JPanel)this.getContentPane();
      this.contentPane.setLayout(this.cardLayout1);
      this.setJMenuBar(this.jMenuBar1);
      this.setSize(new Dimension(708, 451));
      this.setTitle("JustIce");
      this.jPanel1.setMinimumSize(new Dimension(100, 100));
      this.jPanel1.setPreferredSize(new Dimension(100, 100));
      this.jPanel1.setLayout(this.gridLayout1);
      this.jSplitPane2.setOrientation(0);
      this.jPanel2.setLayout(this.gridLayout2);
      this.jPanel3.setMinimumSize(new Dimension(200, 100));
      this.jPanel3.setPreferredSize(new Dimension(400, 400));
      this.jPanel3.setLayout(this.gridLayout4);
      this.messagesPanel.setMinimumSize(new Dimension(100, 100));
      this.messagesPanel.setLayout(this.gridLayout3);
      this.jPanel2.setMinimumSize(new Dimension(200, 100));
      this.jMenu1.setText("File");
      this.jScrollPane1.getViewport().setBackground(Color.red);
      this.messagesScrollPane.getViewport().setBackground(Color.red);
      this.messagesScrollPane.setPreferredSize(new Dimension(10, 10));
      this.classNamesJList.addListSelectionListener(new ListSelectionListener() {
         public void valueChanged(ListSelectionEvent e) {
            VerifierAppFrame.this.classNamesJList_valueChanged(e);
         }
      });
      this.classNamesJList.setSelectionMode(0);
      this.jScrollPane3.setBorder(BorderFactory.createLineBorder(Color.black));
      this.jScrollPane3.setPreferredSize(new Dimension(100, 100));
      this.gridLayout4.setRows(4);
      this.gridLayout4.setColumns(1);
      this.gridLayout4.setHgap(1);
      this.jScrollPane4.setBorder(BorderFactory.createLineBorder(Color.black));
      this.jScrollPane4.setPreferredSize(new Dimension(100, 100));
      this.pass1TextPane.setBorder(BorderFactory.createRaisedBevelBorder());
      this.pass1TextPane.setToolTipText("");
      this.pass1TextPane.setEditable(false);
      this.pass2TextPane.setBorder(BorderFactory.createRaisedBevelBorder());
      this.pass2TextPane.setEditable(false);
      this.messagesTextPane.setBorder(BorderFactory.createRaisedBevelBorder());
      this.messagesTextPane.setEditable(false);
      this.newFileMenuItem.setText("New...");
      this.newFileMenuItem.setAccelerator(KeyStroke.getKeyStroke(78, 2, true));
      this.newFileMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            VerifierAppFrame.this.newFileMenuItem_actionPerformed(e);
         }
      });
      this.pass3aTextPane.setEditable(false);
      this.pass3bTextPane.setEditable(false);
      this.pass3aJList.addListSelectionListener(new ListSelectionListener() {
         public void valueChanged(ListSelectionEvent e) {
            VerifierAppFrame.this.pass3aJList_valueChanged(e);
         }
      });
      this.pass3bJList.addListSelectionListener(new ListSelectionListener() {
         public void valueChanged(ListSelectionEvent e) {
            VerifierAppFrame.this.pass3bJList_valueChanged(e);
         }
      });
      this.jMenu2.setText("Help");
      this.whatisMenuItem.setText("What is...");
      this.whatisMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            VerifierAppFrame.this.whatisMenuItem_actionPerformed(e);
         }
      });
      this.aboutMenuItem.setText("About");
      this.aboutMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            VerifierAppFrame.this.aboutMenuItem_actionPerformed(e);
         }
      });
      this.jSplitPane2.add(this.messagesPanel, "bottom");
      this.messagesPanel.add(this.messagesScrollPane, (Object)null);
      this.messagesScrollPane.getViewport().add(this.messagesTextPane, (Object)null);
      this.jSplitPane2.add(this.jPanel3, "top");
      this.jPanel3.add(this.jScrollPane3, (Object)null);
      this.jScrollPane3.getViewport().add(this.pass1TextPane, (Object)null);
      this.jPanel3.add(this.jScrollPane4, (Object)null);
      this.jPanel3.add(this.jSplitPane3, (Object)null);
      this.jSplitPane3.add(this.jScrollPane2, "left");
      this.jScrollPane2.getViewport().add(this.pass3aJList, (Object)null);
      this.jSplitPane3.add(this.jScrollPane5, "right");
      this.jScrollPane5.getViewport().add(this.pass3aTextPane, (Object)null);
      this.jPanel3.add(this.jSplitPane4, (Object)null);
      this.jSplitPane4.add(this.jScrollPane6, "left");
      this.jScrollPane6.getViewport().add(this.pass3bJList, (Object)null);
      this.jSplitPane4.add(this.jScrollPane7, "right");
      this.jScrollPane7.getViewport().add(this.pass3bTextPane, (Object)null);
      this.jScrollPane4.getViewport().add(this.pass2TextPane, (Object)null);
      this.jSplitPane1.add(this.jPanel2, "top");
      this.jPanel2.add(this.jScrollPane1, (Object)null);
      this.jSplitPane1.add(this.jPanel1, "bottom");
      this.jPanel1.add(this.jSplitPane2, (Object)null);
      this.jScrollPane1.getViewport().add(this.classNamesJList, (Object)null);
      this.jMenuBar1.add(this.jMenu1);
      this.jMenuBar1.add(this.jMenu2);
      this.contentPane.add(this.jSplitPane1, "jSplitPane1");
      this.jMenu1.add(this.newFileMenuItem);
      this.jMenu2.add(this.whatisMenuItem);
      this.jMenu2.add(this.aboutMenuItem);
      this.jSplitPane2.setDividerLocation(300);
      this.jSplitPane3.setDividerLocation(150);
      this.jSplitPane4.setDividerLocation(150);
   }

   protected void processWindowEvent(WindowEvent e) {
      super.processWindowEvent(e);
      if (e.getID() == 201) {
         System.exit(0);
      }

   }

   synchronized void classNamesJList_valueChanged(ListSelectionEvent e) {
      if (!e.getValueIsAdjusting()) {
         this.current_class = this.classNamesJList.getSelectedValue().toString();
         this.verify();
         this.classNamesJList.setSelectedValue(this.current_class, true);
      }
   }

   private void verify() {
      this.setTitle("PLEASE WAIT");
      Verifier v = VerifierFactory.getVerifier(this.current_class);
      v.flush();
      VerificationResult vr = v.doPass1();
      if (vr.getStatus() == 2) {
         this.pass1TextPane.setText(vr.getMessage());
         this.pass1TextPane.setBackground(Color.red);
         this.pass2TextPane.setText("");
         this.pass2TextPane.setBackground(Color.yellow);
         this.pass3aTextPane.setText("");
         this.pass3aJList.setListData(new Object[0]);
         this.pass3aTextPane.setBackground(Color.yellow);
         this.pass3bTextPane.setText("");
         this.pass3bJList.setListData(new Object[0]);
         this.pass3bTextPane.setBackground(Color.yellow);
      } else {
         this.pass1TextPane.setBackground(Color.green);
         this.pass1TextPane.setText(vr.getMessage());
         vr = v.doPass2();
         if (vr.getStatus() == 2) {
            this.pass2TextPane.setText(vr.getMessage());
            this.pass2TextPane.setBackground(Color.red);
            this.pass3aTextPane.setText("");
            this.pass3aTextPane.setBackground(Color.yellow);
            this.pass3aJList.setListData(new Object[0]);
            this.pass3bTextPane.setText("");
            this.pass3bTextPane.setBackground(Color.yellow);
            this.pass3bJList.setListData(new Object[0]);
         } else {
            this.pass2TextPane.setText(vr.getMessage());
            this.pass2TextPane.setBackground(Color.green);
            JavaClass jc = Repository.lookupClass(this.current_class);
            boolean all3aok = true;
            boolean all3bok = true;
            String all3amsg = "";
            String all3bmsg = "";
            String[] methodnames = new String[jc.getMethods().length];

            for(int i = 0; i < jc.getMethods().length; ++i) {
               methodnames[i] = jc.getMethods()[i].toString().replace('\n', ' ').replace('\t', ' ');
            }

            this.pass3aJList.setListData(methodnames);
            this.pass3aJList.setSelectionInterval(0, jc.getMethods().length - 1);
            this.pass3bJList.setListData(methodnames);
            this.pass3bJList.setSelectionInterval(0, jc.getMethods().length - 1);
         }
      }

      String[] msgs = v.getMessages();
      this.messagesTextPane.setBackground(msgs.length == 0 ? Color.green : Color.yellow);
      String allmsgs = "";

      for(int i = 0; i < msgs.length; ++i) {
         msgs[i] = msgs[i].replace('\n', ' ');
         allmsgs = allmsgs + msgs[i] + "\n\n";
      }

      this.messagesTextPane.setText(allmsgs);
      this.setTitle(this.current_class + " - " + this.JUSTICE_VERSION);
   }

   void newFileMenuItem_actionPerformed(ActionEvent e) {
      String classname = JOptionPane.showInputDialog("Please enter the fully qualified name of a class or interface to verify:");
      if (classname != null && !classname.equals("")) {
         VerifierFactory.getVerifier(classname);
         this.classNamesJList.setSelectedValue(classname, true);
      }
   }

   synchronized void pass3aJList_valueChanged(ListSelectionEvent e) {
      if (!e.getValueIsAdjusting()) {
         Verifier v = VerifierFactory.getVerifier(this.current_class);
         String all3amsg = "";
         boolean all3aok = true;
         boolean rejected = false;

         for(int i = 0; i < this.pass3aJList.getModel().getSize(); ++i) {
            if (this.pass3aJList.isSelectedIndex(i)) {
               VerificationResult vr = v.doPass3a(i);
               if (vr.getStatus() == 2) {
                  all3aok = false;
                  rejected = true;
               }

               all3amsg = all3amsg + "Method '" + Repository.lookupClass(v.getClassName()).getMethods()[i] + "': " + vr.getMessage().replace('\n', ' ') + "\n\n";
            }
         }

         this.pass3aTextPane.setText(all3amsg);
         this.pass3aTextPane.setBackground(all3aok ? Color.green : (rejected ? Color.red : Color.yellow));
      }
   }

   synchronized void pass3bJList_valueChanged(ListSelectionEvent e) {
      if (!e.getValueIsAdjusting()) {
         Verifier v = VerifierFactory.getVerifier(this.current_class);
         String all3bmsg = "";
         boolean all3bok = true;
         boolean rejected = false;

         for(int i = 0; i < this.pass3bJList.getModel().getSize(); ++i) {
            if (this.pass3bJList.isSelectedIndex(i)) {
               VerificationResult vr = v.doPass3b(i);
               if (vr.getStatus() == 2) {
                  all3bok = false;
                  rejected = true;
               }

               all3bmsg = all3bmsg + "Method '" + Repository.lookupClass(v.getClassName()).getMethods()[i] + "': " + vr.getMessage().replace('\n', ' ') + "\n\n";
            }
         }

         this.pass3bTextPane.setText(all3bmsg);
         this.pass3bTextPane.setBackground(all3bok ? Color.green : (rejected ? Color.red : Color.yellow));
      }
   }

   void aboutMenuItem_actionPerformed(ActionEvent e) {
      JOptionPane.showMessageDialog(this, "JustIce is a Java class file verifier.\nIt was implemented by Enver Haase in 2001.\nhttp://bcel.sourceforge.net", this.JUSTICE_VERSION, 1);
   }

   void whatisMenuItem_actionPerformed(ActionEvent e) {
      JOptionPane.showMessageDialog(this, "The upper four boxes to the right reflect verification passes according to The Java Virtual Machine Specification.\nThese are (in that order): Pass one, Pass two, Pass three (before data flow analysis), Pass three (data flow analysis).\nThe bottom box to the right shows (warning) messages; warnings do not cause a class to be rejected.", this.JUSTICE_VERSION, 1);
   }
}
