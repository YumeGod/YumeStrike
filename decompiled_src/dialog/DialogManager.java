package dialog;

import aggressor.AggressorClient;
import aggressor.DataManager;
import aggressor.DataUtils;
import aggressor.dialogs.BeaconChooser;
import aggressor.dialogs.CredentialChooser;
import aggressor.dialogs.InterfaceDialog;
import aggressor.dialogs.MailServerDialog;
import aggressor.dialogs.ProxyServerDialog;
import aggressor.dialogs.ScListenerChooser;
import aggressor.dialogs.SiteChooser;
import common.BeaconEntry;
import common.Callback;
import common.CommonUtils;
import common.TeamQueue;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileSystemView;
import ui.APasswordField;
import ui.ATable;
import ui.ATextField;
import ui.GenericTableModel;
import ui.ListCopyPopup;

public class DialogManager {
   protected HashMap options = new HashMap();
   protected LinkedList listeners = new LinkedList();
   protected LinkedList listeners2 = new LinkedList();
   protected LinkedList rows = new LinkedList();
   protected JFrame dialog = null;
   protected LinkedList group = null;
   protected Map groups = new HashMap();

   public void addDialogListener(DialogListener var1) {
      this.listeners2.add(var1);
   }

   public void addDialogListenerInternal(DialogListener var1) {
      this.listeners.add(var1);
   }

   public LinkedList getRows() {
      return new LinkedList(this.rows);
   }

   public void startGroup(String var1) {
      this.group = new LinkedList();
      this.groups.put(var1, this.group);
   }

   public void endGroup() {
      this.group = null;
   }

   public DialogManager(JFrame var1) {
      this.dialog = var1;
   }

   public void set(String var1, String var2) {
      this.options.put(var1, var2);
   }

   public void set(Map var1) {
      Iterator var2 = var1.entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry var3 = (Map.Entry)var2.next();
         this.options.put(var3.getKey() + "", var3.getValue() + "");
      }

   }

   private static void setEnabledSafe(final JComponent var0, final boolean var1) {
      CommonUtils.runSafe(new Runnable() {
         public void run() {
            var0.setEnabled(var1);
         }
      });
   }

   public JButton action_noclose(final String var1) {
      JButton var2 = new JButton(var1);
      var2.addActionListener(new ActionListener() {
         public void actionPerformed(final ActionEvent var1x) {
            ((JComponent)var1x.getSource()).setEnabled(false);
            (new Thread(new Runnable() {
               public void run() {
                  Iterator var1xx = DialogManager.this.listeners.iterator();

                  DialogListener var2;
                  while(var1xx.hasNext()) {
                     var2 = (DialogListener)var1xx.next();
                     var2.dialogAction(var1x, DialogManager.this.options);
                  }

                  var1xx = DialogManager.this.listeners2.iterator();

                  while(var1xx.hasNext()) {
                     var2 = (DialogListener)var1xx.next();
                     var2.dialogAction(var1x, DialogManager.this.options);
                  }

                  DialogManager.setEnabledSafe((JComponent)var1x.getSource(), true);
               }
            }, "dialog action: " + var1)).start();
         }
      });
      return var2;
   }

   public DialogRow beacon_disabled(String var1, String var2, AggressorClient var3) {
      DialogRow var4 = this.beacon(var1, var2, var3);
      var4.get(2).setEnabled(false);
      return var4;
   }

   public DialogRow beacon(final String var1, String var2, final AggressorClient var3) {
      final DialogRow var4 = this.text(var1 + ".title", var2);
      ((JTextField)var4.c[1]).setEditable(false);
      if (this.options.containsKey(var1)) {
         BeaconEntry var5 = DataUtils.getBeacon(var3.getData(), this.options.get(var1) + "");
         if (var5 != null) {
            ((JTextField)var4.c[1]).setText(var5.getUser() + var5.title(" via "));
         }
      }

      JButton var6 = new JButton("...");
      var6.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1x) {
            (new BeaconChooser(var3, new SafeDialogCallback() {
               public void dialogResult(String var1x) {
                  DialogManager.this.options.put(var1, var1x);
                  BeaconEntry var2 = DataUtils.getBeacon(var3.getData(), var1x);
                  if (var2 != null) {
                     ((JTextField)var4.c[1]).setText(var2.getUser() + var2.title(" via "));
                  }

               }
            })).show();
         }
      });
      var4.c[2] = var6;
      return var4;
   }

   public DialogRow interfaces(String var1, String var2, TeamQueue var3, DataManager var4) {
      List var5 = DataUtils.getInterfaceList(var4);
      DialogRow var6 = this.combobox(var1, var2, CommonUtils.toArray((Collection)var5));
      JButton var7 = new JButton("Add");
      var7.addActionListener(new InterfaceAdd((JComboBox)var6.c[1], var3, var4));
      var6.last(var7);
      return var6;
   }

   public DialogRow exploits(String var1, String var2, AggressorClient var3) {
      List var4 = DataUtils.getBeaconExploits(var3.getData()).exploits();
      DialogRow var5 = this.combobox(var1, var2, CommonUtils.toArray((Collection)var4));
      return var5;
   }

   public DialogRow krbtgt(String var1, String var2, final AggressorClient var3) {
      DialogRow var4 = this.text(var1, var2);
      final JTextField var5 = (JTextField)var4.c[1];
      JButton var6 = new JButton("...");
      var6.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            CredentialChooser var2 = new CredentialChooser(var3, new SafeDialogCallback() {
               public void dialogResult(String var1) {
                  String[] var2 = var1.split(" ");
                  var5.setText(var2[1]);
               }
            });
            var2.getFilter().checkLiteral("user", "krbtgt");
            var2.getFilter().checkNTLMHash("password", false);
            var2.show();
         }
      });
      var4.c[2] = var6;
      return var4;
   }

   public DialogRow label(String var1) {
      DialogRow var2 = new DialogRow(new JPanel(), new JLabel(var1), new JPanel());
      this.rows.add(var2);
      if (this.group != null) {
         this.group.add(var2);
      }

      return var2;
   }

   public DialogRow combobox(final String var1, String var2, Object[] var3) {
      new JLabel(var2);
      final JComboBox var5 = new JComboBox(var3);
      var5.setPreferredSize(new Dimension(240, 0));
      if (this.options.containsKey(var1)) {
         var5.setSelectedItem(this.options.get(var1));
      }

      this.addDialogListenerInternal(new DialogListener() {
         public void dialogAction(ActionEvent var1x, Map var2) {
            var2.put(var1, var5.getSelectedItem());
         }
      });
      DialogRow var6 = new DialogRow(new JLabel(var2), var5, new JPanel());
      this.rows.add(var6);
      if (this.group != null) {
         this.group.add(var6);
      }

      return var6;
   }

   public DialogRow attack(String var1, String var2) {
      DialogRow var3 = this.text(var1, var2);
      JTextField var4 = (JTextField)var3.c[1];
      JButton var5 = new JButton("...");
      var5.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
         }
      });
      var3.c[2] = var5;
      return var3;
   }

   public DialogRow site(String var1, String var2, final TeamQueue var3, final DataManager var4) {
      DialogRow var5 = this.text(var1, var2);
      final JTextField var6 = (JTextField)var5.c[1];
      JButton var7 = new JButton("...");
      var7.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            (new SiteChooser(var3, var4, new SafeDialogCallback() {
               public void dialogResult(String var1) {
                  var6.setText(var1 + "?id=%TOKEN%");
               }
            })).show();
         }
      });
      var5.c[2] = var7;
      return var5;
   }

   public DialogRow sc_listener_all(String var1, String var2, final AggressorClient var3) {
      DialogRow var4 = this.text(var1, var2);
      final JTextField var5 = (JTextField)var4.c[1];
      var5.setEditable(false);
      JButton var6 = new JButton("...");
      var6.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ScListenerChooser.ListenersAll(var3, new SafeDialogCallback() {
               public void dialogResult(String var1) {
                  var5.setText(var1);
               }
            }).show();
         }
      });
      var4.c[2] = var6;
      return var4;
   }

   public DialogRow sc_listener_stagers(String var1, String var2, final AggressorClient var3) {
      DialogRow var4 = this.text(var1, var2);
      final JTextField var5 = (JTextField)var4.c[1];
      var5.setEditable(false);
      JButton var6 = new JButton("...");
      var6.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ScListenerChooser.ListenersWithStagers(var3, new SafeDialogCallback() {
               public void dialogResult(String var1) {
                  var5.setText(var1);
               }
            }).show();
         }
      });
      var4.c[2] = var6;
      return var4;
   }

   public DialogRow proxyserver(String var1, String var2, final AggressorClient var3) {
      DialogRow var4 = this.text(var1, var2);
      final JTextField var5 = (JTextField)var4.c[1];
      JButton var6 = new JButton("...");
      var6.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            (new ProxyServerDialog(var5.getText(), new SafeDialogCallback() {
               public void dialogResult(String var1) {
                  var3.getConnection().call("armitage.broadcast", CommonUtils.args("manproxy", var1));
                  var5.setText(var1);
               }
            })).show();
         }
      });
      var4.c[2] = var6;
      return var4;
   }

   public DialogRow mailserver(String var1, String var2) {
      DialogRow var3 = this.text(var1, var2);
      final JTextField var4 = (JTextField)var3.c[1];
      JButton var5 = new JButton("...");
      var5.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            (new MailServerDialog(var4.getText(), new SafeDialogCallback() {
               public void dialogResult(String var1) {
                  var4.setText(var1);
               }
            })).show();
         }
      });
      var3.c[2] = var5;
      return var3;
   }

   public DialogRow file(String var1, String var2) {
      DialogRow var3 = this.text(var1, var2);
      final JTextField var4 = (JTextField)var3.c[1];
      JButton var5 = new JButton(FileSystemView.getFileSystemView().getSystemIcon(new File(".")));
      var5.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            SafeDialogs.openFile("Choose file", (String)null, (String)null, false, false, new SafeDialogCallback() {
               public void dialogResult(String var1) {
                  var4.setText(var1);
               }
            });
         }
      });
      var3.c[2] = var5;
      return var3;
   }

   public DialogRow font(String var1, String var2) {
      DialogRow var3 = this.text(var1, var2);
      JButton var4 = new JButton("...");
      final JTextField var5 = (JTextField)var3.c[1];
      var4.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            FontDialog var2 = new FontDialog(Font.decode(var5.getText()));
            var2.addFontChooseListener(new SafeDialogCallback() {
               public void dialogResult(String var1) {
                  var5.setText(var1);
               }
            });
            var2.show();
         }
      });
      var3.c[2] = var4;
      return var3;
   }

   public DialogRow color(String var1, String var2) {
      DialogRow var3 = this.text(var1, var2);
      final JTextField var4 = (JTextField)var3.c[1];
      final Color var5 = Color.black;
      if (var4.getText() != null && var4.getText().length() > 0) {
         var5 = Color.decode(var4.getText());
      }

      final SolidIcon var6 = new SolidIcon(var5, 16, 16);
      JButton var7 = new JButton(var6);
      var7.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            SafeDialogs.chooseColor("pick a color", var5, new SafeDialogCallback() {
               public void dialogResult(String var1) {
                  var4.setText(var1);
                  var6.setColor(Color.decode(var1));
               }
            });
         }
      });
      var3.c[2] = var7;
      return var3;
   }

   public DialogRow file_import(final String var1, String var2, ATable var3, final GenericTableModel var4) {
      DialogRow var5 = this.file("_" + var1, var2);
      final JTextField var6 = (JTextField)var5.c[1];
      DocumentListener var7 = new DocumentListener() {
         public void changedUpdate(DocumentEvent var1) {
            this.check();
         }

         public void insertUpdate(DocumentEvent var1) {
            this.check();
         }

         public void removeUpdate(DocumentEvent var1) {
            this.check();
         }

         public void check() {
            var4.clear(128);
            File var1 = new File(var6.getText().trim());
            if (var1.exists() && var1.canRead() && !var1.isDirectory()) {
               String[] var2 = CommonUtils.bString(CommonUtils.strrep(CommonUtils.readFile(var1.getAbsolutePath()), "\r", "")).split("\n");

               for(int var3 = 0; var3 < var2.length; ++var3) {
                  if (var2[var3].length() > 0) {
                     String[] var4x = var2[var3].split("\t");
                     if (var4x.length == 1) {
                        var4.addEntry(CommonUtils.toMap("To", var4x[0], "To_Name", ""));
                     } else if (var4x.length >= 2) {
                        var4.addEntry(CommonUtils.toMap("To", var4x[0], "To_Name", var4x[1]));
                     }
                  }
               }
            }

            var4.fireListeners();
         }
      };
      var6.getDocument().addDocumentListener(var7);
      this.addDialogListenerInternal(new DialogListener() {
         public void dialogAction(ActionEvent var1x, Map var2) {
            var2.put(var1, var4.export());
         }
      });
      var7.insertUpdate((DocumentEvent)null);
      return var5;
   }

   public JButton action(String var1) {
      JButton var2 = this.action_noclose(var1);
      var2.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            if (!DialogUtils.isShift(var1)) {
               DialogManager.this.dialog.setVisible(false);
               DialogManager.this.dialog.dispose();
            }

         }
      });
      return var2;
   }

   public JButton help(String var1) {
      JButton var2 = new JButton("Help");
      var2.addActionListener(DialogUtils.gotoURL(var1));
      return var2;
   }

   public DialogRow text(String var1, String var2) {
      return this.text(var1, var2, 20);
   }

   public DialogRow text_disabled(String var1, String var2) {
      DialogRow var3 = this.text(var1, var2, 20);
      var3.get(1).setEnabled(false);
      return var3;
   }

   public DialogRow text(final String var1, String var2, int var3) {
      final ATextField var4 = new ATextField(var3);
      if (this.options.containsKey(var1)) {
         var4.setText(this.options.get(var1) + "");
      }

      this.addDialogListenerInternal(new DialogListener() {
         public void dialogAction(ActionEvent var1x, Map var2) {
            if ("".equals(var4.getText())) {
               var2.put(var1, "");
            } else {
               var2.put(var1, var4.getText());
            }

         }
      });
      DialogRow var5 = new DialogRow(new JLabel(var2), var4, new JPanel());
      this.rows.add(var5);
      if (this.group != null) {
         this.group.add(var5);
      }

      return var5;
   }

   public DialogRow list_file(String var1, String var2) {
      return this.list(var1, var2, "file", 64);
   }

   public DialogRow list_text(String var1, String var2) {
      return this.list(var1, var2, "text", 160);
   }

   public DialogRow list_csv(final String var1, String var2, final String var3, final String var4, int var5) {
      final JList var6 = new JList();
      JScrollPane var7 = new JScrollPane(var6, 20, 30);
      var7.setPreferredSize(new Dimension(240, var5));
      if (this.options.containsKey(var1)) {
         String var8 = this.options.get(var1) + "";
         if (!"".equals(var8)) {
            var6.setListData(CommonUtils.toArray(var8));
         }
      }

      this.addDialogListenerInternal(new DialogListener() {
         public void dialogAction(ActionEvent var1x, Map var2) {
            if (var6.getModel().getSize() == 0) {
               var2.put(var1, "");
            } else {
               LinkedList var3 = new LinkedList();

               for(int var4 = 0; var4 < var6.getModel().getSize(); ++var4) {
                  var3.add(var6.getModel().getElementAt(var4));
               }

               var2.put(var1, CommonUtils.join((Collection)var3, (String)", "));
            }

         }
      });
      JButton var12 = new JButton(DialogUtils.getIcon("resources/cc/black/png/delete_icon&16.png"));
      var12.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1x) {
            var6.setListData(new String[0]);
            DialogManager.this.options.put(var1, "");
         }
      });
      JButton var9 = new JButton(DialogUtils.getIcon("resources/cc/black/png/sq_minus_icon&16.png"));
      var9.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1x) {
            LinkedList var2 = new LinkedList();

            for(int var3 = 0; var3 < var6.getModel().getSize(); ++var3) {
               boolean var4 = false;

               for(int var5 = 0; var5 < var6.getSelectedIndices().length; ++var5) {
                  if (var3 == var6.getSelectedIndices()[var5]) {
                     var4 = true;
                  }
               }

               if (!var4) {
                  var2.add(var6.getModel().getElementAt(var3));
               }
            }

            var6.setListData(CommonUtils.toArray((Collection)var2));
            DialogManager.this.options.put(var1, CommonUtils.join((Collection)var2, (String)", "));
         }
      });
      JButton var10 = new JButton(DialogUtils.getIcon("resources/cc/black/png/sq_plus_icon&16.png"));
      var10.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1x) {
            SafeDialogs.ask(var3, var4, new SafeDialogCallback() {
               public void dialogResult(String var1x) {
                  LinkedList var2 = new LinkedList();

                  for(int var3x = 0; var3x < var6.getModel().getSize(); ++var3x) {
                     var2.add(var6.getModel().getElementAt(var3x));
                  }

                  String[] var5 = CommonUtils.toArray(var1x);

                  for(int var4x = 0; var4x < var5.length; ++var4x) {
                     var2.add(var5[var4x]);
                  }

                  var6.setListData(CommonUtils.toArray((Collection)var2));
                  DialogManager.this.options.put(var1, CommonUtils.join((Collection)var2, (String)", "));
               }
            });
         }
      });
      new ListCopyPopup(var6);
      DialogRow var11 = new DialogRow(new JLabel(var2), var7, DialogUtils.stack(var10, var9, var12));
      this.rows.add(var11);
      if (this.group != null) {
         this.group.add(var11);
      }

      return var11;
   }

   public DialogRow list(final String var1, String var2, String var3, int var4) {
      final JList var5 = new JList();
      JScrollPane var6 = new JScrollPane(var5, 20, 30);
      var6.setPreferredSize(new Dimension(240, var4));
      if (this.options.containsKey(var1)) {
         String var7 = this.options.get(var1) + "";
         if (!"".equals(var7)) {
            var5.setListData(var7.split("!!"));
         }
      }

      this.addDialogListenerInternal(new DialogListener() {
         public void dialogAction(ActionEvent var1x, Map var2) {
            if (var5.getModel().getSize() == 0) {
               var2.put(var1, "");
            } else {
               LinkedList var3 = new LinkedList();

               for(int var4 = 0; var4 < var5.getModel().getSize(); ++var4) {
                  var3.add(var5.getModel().getElementAt(var4));
               }

               var2.put(var1, CommonUtils.join((Collection)var3, (String)"!!"));
            }

         }
      });
      JButton var10 = new JButton(DialogUtils.getIcon("resources/cc/black/png/sq_minus_icon&16.png"));
      var10.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1x) {
            LinkedList var2 = new LinkedList();

            for(int var3 = 0; var3 < var5.getModel().getSize(); ++var3) {
               boolean var4 = false;

               for(int var5x = 0; var5x < var5.getSelectedIndices().length; ++var5x) {
                  if (var3 == var5.getSelectedIndices()[var5x]) {
                     var4 = true;
                  }
               }

               if (!var4) {
                  var2.add(var5.getModel().getElementAt(var3));
               }
            }

            var5.setListData(CommonUtils.toArray((Collection)var2));
            DialogManager.this.options.put(var1, CommonUtils.join((Collection)var2, (String)"!!"));
         }
      });
      JComponent var8 = null;
      if ("file".equals(var3)) {
         JButton var9 = new JButton(FileSystemView.getFileSystemView().getSystemIcon(new File(".")));
         var9.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1x) {
               SafeDialogs.openFile("Choose a file", (String)null, (String)null, false, false, new SafeDialogCallback() {
                  public void dialogResult(String var1x) {
                     LinkedList var2 = new LinkedList();

                     for(int var3 = 0; var3 < var5.getModel().getSize(); ++var3) {
                        var2.add(var5.getModel().getElementAt(var3));
                     }

                     var2.add(var1x);
                     var5.setListData(CommonUtils.toArray((Collection)var2));
                     DialogManager.this.options.put(var1, CommonUtils.join((Collection)var2, (String)"!!"));
                  }
               });
            }
         });
         var8 = DialogUtils.stack(var9, var10);
      } else {
         var8 = DialogUtils.stack((JComponent)var10);
      }

      DialogRow var11 = new DialogRow(new JLabel(var2), var6, var8);
      this.rows.add(var11);
      if (this.group != null) {
         this.group.add(var11);
      }

      return var11;
   }

   public DialogRow text_big(String var1, String var2) {
      return this.text_big(var1, var2, 20);
   }

   public DialogRow text_big(final String var1, String var2, int var3) {
      final JTextArea var4 = new JTextArea();
      var4.setRows(3);
      var4.setColumns(var3);
      var4.setLineWrap(true);
      var4.setWrapStyleWord(true);
      if (this.options.containsKey(var1)) {
         var4.setText(this.options.get(var1) + "");
      }

      this.addDialogListenerInternal(new DialogListener() {
         public void dialogAction(ActionEvent var1x, Map var2) {
            if ("".equals(var4.getText())) {
               var2.put(var1, "");
            } else {
               var2.put(var1, var4.getText());
            }

         }
      });
      DialogRow var5 = new DialogRow(new JLabel(var2), new JScrollPane(var4), new JPanel());
      this.rows.add(var5);
      if (this.group != null) {
         this.group.add(var5);
      }

      return var5;
   }

   public DialogRow password(final String var1, String var2, int var3) {
      final APasswordField var4 = new APasswordField(var3);
      if (this.options.containsKey(var1)) {
         var4.setText(this.options.get(var1) + "");
      }

      this.addDialogListenerInternal(new DialogListener() {
         public void dialogAction(ActionEvent var1x, Map var2) {
            if ("".equals(var4.getText())) {
               var2.remove(var1);
            } else {
               var2.put(var1, var4.getText());
            }

         }
      });
      DialogRow var5 = new DialogRow(new JLabel(var2), var4, new JPanel());
      this.rows.add(var5);
      if (this.group != null) {
         this.group.add(var5);
      }

      return var5;
   }

   public JComponent layout() {
      return this.layout(this.rows);
   }

   public JComponent layout(String var1) {
      LinkedList var2 = (LinkedList)this.groups.get(var1);
      return this.layout(var2);
   }

   public JComponent row() {
      if (this.rows.size() != 1) {
         throw new RuntimeException("Can only layout a row with one component!");
      } else {
         DialogRow var1 = (DialogRow)this.rows.get(0);
         JPanel var2 = new JPanel();
         var2.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
         var2.setLayout(new BorderLayout(5, 5));
         var2.add(var1.get(0), "West");
         var2.add(var1.get(1), "Center");
         var2.add(var1.get(2), "East");
         return var2;
      }
   }

   public JComponent layout(LinkedList var1) {
      JPanel var2 = new JPanel();
      GroupLayout var3 = new GroupLayout(var2);
      var3.setAutoCreateGaps(true);
      var3.setAutoCreateContainerGaps(true);
      var2.setLayout(var3);
      GroupLayout.SequentialGroup var4 = var3.createSequentialGroup();

      for(int var5 = 0; var5 < 3; ++var5) {
         GroupLayout.ParallelGroup var6 = var3.createParallelGroup();
         Iterator var7 = var1.iterator();

         while(var7.hasNext()) {
            DialogRow var8 = (DialogRow)var7.next();
            var6.addComponent(var8.get(var5));
         }

         var4.addGroup(var6);
      }

      var3.setHorizontalGroup(var4);
      GroupLayout.SequentialGroup var10 = var3.createSequentialGroup();
      Iterator var11 = var1.iterator();

      while(var11.hasNext()) {
         DialogRow var12 = (DialogRow)var11.next();
         GroupLayout.ParallelGroup var13 = var3.createParallelGroup(Alignment.BASELINE);

         for(int var9 = 0; var9 < 3; ++var9) {
            var13.addComponent(var12.get(var9));
         }

         var10.addGroup(var13);
      }

      var3.setVerticalGroup(var10);
      return var2;
   }

   public DialogRow checkbox_add(String var1, String var2, String var3) {
      return this.checkbox_add(var1, var2, var3, true);
   }

   public DialogRow checkbox_add(String var1, String var2, String var3, boolean var4) {
      JLabel var5 = new JLabel(var2);
      JCheckBox var6 = this.checkbox(var1, var3);
      if (!var4) {
         var5.setEnabled(false);
         var6.setEnabled(false);
      }

      DialogRow var7 = new DialogRow(var5, var6, new JPanel());
      this.rows.add(var7);
      if (this.group != null) {
         this.group.add(var7);
      }

      return var7;
   }

   public JCheckBox checkbox(final String var1, String var2) {
      final JCheckBox var3 = new JCheckBox(var2);
      if ("true".equals(this.options.get(var1))) {
         var3.setSelected(true);
      } else {
         var3.setSelected(false);
      }

      this.addDialogListenerInternal(new DialogListener() {
         public void dialogAction(ActionEvent var1x, Map var2) {
            if (var3.isSelected()) {
               var2.put(var1, "true");
            } else {
               var2.put(var1, "false");
            }

         }
      });
      return var3;
   }

   private static class InterfaceAdd implements ActionListener, Callback {
      protected JComboBox mybox;
      protected TeamQueue conn;
      protected DataManager data;

      public InterfaceAdd(JComboBox var1, TeamQueue var2, DataManager var3) {
         this.mybox = var1;
         this.conn = var2;
         this.data = var3;
      }

      public void actionPerformed(ActionEvent var1) {
         InterfaceDialog var2 = new InterfaceDialog(this.conn, this.data);
         var2.notify(this);
         var2.show();
      }

      public void result(String var1, final Object var2) {
         CommonUtils.runSafe(new Runnable() {
            public void run() {
               InterfaceAdd.this.mybox.addItem(var2 + "");
               InterfaceAdd.this.mybox.setSelectedItem(var2 + "");
            }
         });
      }
   }

   public static final class DialogRow {
      public JComponent[] c = new JComponent[3];

      public DialogRow(JComponent var1, JComponent var2, JComponent var3) {
         this.c[0] = var1;
         this.c[1] = var2;
         this.c[2] = var3;
      }

      public JComponent get(int var1) {
         return this.c[var1];
      }

      public void last(JComponent var1) {
         this.c[2] = var1;
      }
   }
}
