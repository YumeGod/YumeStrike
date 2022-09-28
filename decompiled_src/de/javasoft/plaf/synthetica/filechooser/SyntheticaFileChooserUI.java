package de.javasoft.plaf.synthetica.filechooser;

import de.javasoft.io.FileProperties;
import de.javasoft.io.FileUtils;
import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import de.javasoft.util.JavaVersion;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.RGBImageFilter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.security.AccessControlException;
import java.security.AccessController;
import java.text.Collator;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.prefs.Preferences;
import javax.swing.AbstractAction;
import javax.swing.AbstractListModel;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.Spring;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicDirectoryModel;
import javax.swing.plaf.basic.BasicFileChooserUI;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.text.Position;
import javax.swing.text.Position.Bias;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import sun.awt.shell.ShellFolder;
import sun.security.action.GetPropertyAction;
import sun.swing.plaf.synth.DefaultSynthStyle;

public class SyntheticaFileChooserUI extends BasicFileChooserUI implements ActionListener {
   private static final int LIST_VIEW = 0;
   private static final int TABLE_VIEW = 1;
   private static final int TREE_VIEW = 2;
   private JButton newFolderButton;
   private JButton upFolderButton;
   private JComboBox directoryComboBox;
   private JLabel lookInLabel;
   private JButton approveButton;
   private JButton cancelButton;
   private JPanel controlButtonPanel;
   private JTextField fileNameTextField;
   private Color comboListBackground;
   private FilePane filePane;
   private boolean readOnly;
   private HashSet cutBuffer = new HashSet();
   private HashSet copyBuffer = new HashSet();
   private boolean useSystemFileIcons = SyntheticaLookAndFeel.getUseSystemFileIcons();
   private boolean directoryChooser = false;
   private boolean treePanelEnabled = false;
   private boolean sortEnabled = true;
   private int xGap = 10;
   private int yGap = 10;

   public SyntheticaFileChooserUI(JFileChooser var1) {
      super(var1);
   }

   protected SyntheticaFileChooserUI(JFileChooser var1, boolean var2) {
      super(var1);
      this.directoryChooser = var2;
      this.treePanelEnabled = var2;
      if (var2) {
         var1.setFileSelectionMode(1);
      }

   }

   public static ComponentUI createUI(JComponent var0) {
      return new SyntheticaFileChooserUI((JFileChooser)var0);
   }

   public void installUI(JComponent var1) {
      super.installUI(var1);
      this.comboListBackground = SyntheticaLookAndFeel.getColor("Synthetica.fileChooser.comboListBackground", var1);
      String var2 = "Synthetica.extendedFileChooser.sortEnabled";
      this.sortEnabled = SyntheticaLookAndFeel.getBoolean(var2, var1, true);
      if (SyntheticaLookAndFeel.getRememberFileChooserPreferences() && !this.directoryChooser) {
         final JFileChooser var3 = this.getFileChooser();
         Thread var4 = new Thread() {
            public void run() {
               Window var1 = null;

               for(int var2 = 200; ((var1 = SyntheticaFileChooserUI.this.getWindow()) == null || !var1.isShowing()) && var2 > 0; --var2) {
                  try {
                     sleep(10L);
                  } catch (InterruptedException var18) {
                  }
               }

               if (var1 != null) {
                  String var3x = "";
                  Frame[] var7;
                  int var6 = (var7 = Frame.getFrames()).length;

                  for(int var5 = 0; var5 < var6; ++var5) {
                     Frame var4 = var7[var5];
                     if (var4 instanceof JFrame && var4.getTitle() != null) {
                        var3x = var4.getTitle();
                        break;
                     }
                  }

                  byte var19 = 50;
                  byte var20 = 7;
                  String var21 = var3.getClientProperty("Synthetica.fileChooser.id") == null ? "" : "." + (String)var3.getClientProperty("Synthetica.fileChooser.id");
                  var21 = var21.length() <= var20 ? var21 : var21.substring(0, var20 + 1);
                  String var22 = ".SyntheticaFileChooser";
                  var3x = var3x.length() <= var19 ? var3x : var3x.substring(0, var19);
                  var3x = var3x.replace("//", "/");
                  final Preferences var8 = Preferences.userRoot().node(var3x + var21 + var22);
                  int var9 = var8.getInt("xPos", var1.getLocation().x);
                  int var10 = var8.getInt("yPos", var1.getLocation().y);
                  int var11 = var8.getInt("width", var1.getSize().width);
                  int var12 = var8.getInt("height", var1.getSize().height);
                  Rectangle var13 = SyntheticaLookAndFeel.validateWindowBounds(new Rectangle(var9, var10, var11, var12));
                  var1.setBounds(var13);
                  final File var14 = new File(var8.get("directory" + var3.getDialogType(), var3.getCurrentDirectory().getAbsolutePath()));
                  String var15 = "Synthetica.extendedFileChooser.rememberLastDirectory";
                  boolean var16 = var3.getFileSystemView().getDefaultDirectory().equals(var3.getCurrentDirectory());
                  final boolean var17 = SyntheticaLookAndFeel.getBoolean(var15, var3, true) && var16;
                  if (var17 && var14 != null && var14.exists()) {
                     EventQueue.invokeLater(new Runnable() {
                        public void run() {
                           var3.setCurrentDirectory(var14);
                        }
                     });
                  }

                  EventQueue.invokeLater(new Runnable() {
                     public void run() {
                        SyntheticaFileChooserUI.this.filePane.setView(var8.getInt("view", SyntheticaFileChooserUI.this.filePane.getView()));
                        SyntheticaFileChooserUI.this.filePane.sortColumn = var8.getInt("sortColumn", SyntheticaFileChooserUI.this.filePane.sortColumn);
                        SyntheticaFileChooserUI.this.filePane.sortOrder = var8.getInt("sortOrder", SyntheticaFileChooserUI.this.filePane.sortOrder);
                     }
                  });
                  var1.addWindowListener(new WindowAdapter() {
                     public void windowClosed(WindowEvent var1) {
                        Dialog var2 = (Dialog)var1.getWindow();
                        int var3x = var2.getLocation().x;
                        int var4 = var2.getLocation().y;
                        int var5 = var2.getSize().width;
                        int var6 = var2.getSize().height;
                        var8.putInt("xPos", var3x);
                        var8.putInt("yPos", var4);
                        var8.putInt("width", var5);
                        var8.putInt("height", var6);
                        var8.putInt("view", SyntheticaFileChooserUI.this.filePane.getView());
                        var8.putInt("sortColumn", SyntheticaFileChooserUI.this.filePane.sortColumn);
                        var8.putInt("sortOrder", SyntheticaFileChooserUI.this.filePane.sortOrder);
                        if (var17) {
                           var8.put("directory" + var3.getDialogType(), var3.getCurrentDirectory().getAbsolutePath());
                        }

                     }
                  });
               }
            }
         };
         var4.setDaemon(true);
         var4.setPriority(10);
         var4.start();
      }

   }

   public void installComponents(final JFileChooser var1) {
      if (this.directoryChooser) {
         var1.setDialogTitle(UIManager.getString("DirectoryChooser.title"));
      }

      Locale var2 = var1.getLocale();
      var1.setLayout(new BorderLayout(0, this.yGap));
      var1.setBorder(new EmptyBorder(this.yGap, this.xGap, this.yGap, this.xGap));
      JPanel var3 = new JPanel(new GridBagLayout());
      var3.setName("JFileChooser.NorthPanel");
      GridBagConstraints var4 = new GridBagConstraints();
      this.lookInLabel = new JLabel(UIManager.getString("FileChooser.lookInLabelText", var2));
      this.lookInLabel.setDisplayedMnemonic(UIManager.getInt("FileChooser.lookInLabelMnemonic"));
      var4.weightx = 0.0;
      var4.insets = new Insets(0, 0, 0, this.xGap);
      var3.add(this.lookInLabel, var4);
      this.directoryComboBox = new JComboBox();
      this.directoryComboBox.setName("JFileChooser.DirectoryComboBox");
      final DirectoryComboBoxModel var5 = new DirectoryComboBoxModel();
      this.directoryComboBox.setModel(var5);
      final ListCellRenderer var6 = this.directoryComboBox.getRenderer();
      this.directoryComboBox.setRenderer(new DefaultListCellRenderer() {
         IndentIcon ii = SyntheticaFileChooserUI.this.new IndentIcon((IndentIcon)null);

         public Component getListCellRendererComponent(JList var1x, Object var2, int var3, boolean var4, boolean var5x) {
            if (SyntheticaFileChooserUI.this.comboListBackground != null && (var1x.getBorder() == null || var1x.getBorder().getClass().getName().contains("SynthBorder"))) {
               var1x.setBackground(SyntheticaFileChooserUI.this.comboListBackground);
            }

            JLabel var6x = (JLabel)var6.getListCellRendererComponent(var1x, var2, var3, var4, var5x);
            if (var2 == null) {
               var6x.setText("");
               return this;
            } else {
               File var7 = (File)var2;
               var6x.setText(var1.getName(var7));
               if (SyntheticaFileChooserUI.this.useSystemFileIcons) {
                  FileSystemView var8 = var1.getFileSystemView();
                  this.ii.icon = var8.getSystemIcon(var7);
               }

               if (this.ii.icon == null || !SyntheticaFileChooserUI.this.useSystemFileIcons) {
                  this.ii.icon = SyntheticaFileChooserUI.this.getFileChooser().getIcon(var7);
               }

               this.ii.depth = var5.getDepth(var3);
               var6x.setIcon(this.ii);
               var6x.setOpaque(var4);
               return var6x;
            }
         }
      });
      this.directoryComboBox.addActionListener(this);
      this.directoryComboBox.setActionCommand("directoryComboBox.select");
      this.directoryComboBox.setMaximumRowCount(15);
      this.directoryComboBox.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE);
      var4.gridx = 2;
      var4.fill = 2;
      var4.weightx = 1.0;
      var3.add(this.directoryComboBox, var4);
      Box var7 = new Box(2);
      var7.add(Box.createHorizontalStrut(this.xGap));
      boolean var8 = UIManager.getBoolean("Synthetica.extendedFileChooser.actionButtons.paintBorder");
      this.upFolderButton = new JButton(this.upFolderIcon);
      if (SyntheticaLookAndFeel.get("FileChooser.upFolderIcon.disabled", (Component)var1) != null) {
         this.upFolderButton.setDisabledIcon(SyntheticaLookAndFeel.getIcon("FileChooser.upFolderIcon.disabled", var1));
      } else {
         this.upFolderButton.setDisabledIcon(this.createBrightIcon(this.upFolderIcon));
      }

      this.upFolderButton.setPressedIcon(SyntheticaLookAndFeel.getIcon("FileChooser.upFolderIcon.pressed", var1));
      this.upFolderButton.setToolTipText(UIManager.getString("FileChooser.upFolderToolTipText", var2));
      if (UIManager.getLookAndFeel() instanceof SyntheticaLookAndFeel) {
         this.upFolderButton.setMargin(new Insets(0, 0, 0, 0));
         if (!var8) {
            this.upFolderButton.setBorderPainted(false);
         }
      } else {
         this.upFolderButton.setMargin(new Insets(2, 2, 2, 2));
      }

      this.upFolderButton.addActionListener(this);
      this.upFolderButton.setActionCommand("upFolderAction");
      var7.add(this.upFolderButton);
      JButton var9 = new JButton(this.homeFolderIcon);
      var9.setDisabledIcon(SyntheticaLookAndFeel.getIcon("FileChooser.homeFolderIcon.disabled", var1));
      var9.setPressedIcon(SyntheticaLookAndFeel.getIcon("FileChooser.homeFolderIcon.pressed", var1));
      var9.setToolTipText(UIManager.getString("FileChooser.homeFolderToolTipText", var2));
      if (UIManager.getLookAndFeel() instanceof SyntheticaLookAndFeel) {
         var9.setMargin(new Insets(0, 0, 0, 0));
         if (!var8) {
            var9.setBorderPainted(false);
         }
      } else {
         var9.setMargin(new Insets(2, 2, 2, 2));
      }

      var9.addActionListener(this);
      var9.setActionCommand("goHomeAction");
      var7.add(var9);
      this.readOnly = UIManager.getBoolean("FileChooser.readOnly");
      if (!this.readOnly) {
         this.newFolderButton = new JButton(this.newFolderIcon);
         if (SyntheticaLookAndFeel.get("FileChooser.newFolderIcon.disabled", (Component)var1) != null) {
            this.newFolderButton.setDisabledIcon(SyntheticaLookAndFeel.getIcon("FileChooser.newFolderIcon.disabled", var1));
         } else {
            this.newFolderButton.setDisabledIcon(this.createBrightIcon(this.newFolderIcon));
         }

         this.newFolderButton.setPressedIcon(SyntheticaLookAndFeel.getIcon("FileChooser.newFolderIcon.pressed", var1));
         this.newFolderButton.setToolTipText(UIManager.getString("FileChooser.newFolderToolTipText", var2));
         if (UIManager.getLookAndFeel() instanceof SyntheticaLookAndFeel) {
            this.newFolderButton.setMargin(new Insets(0, 0, 0, 0));
            if (!var8) {
               this.newFolderButton.setBorderPainted(false);
            }
         } else {
            this.newFolderButton.setMargin(new Insets(2, 2, 2, 2));
         }

         this.newFolderButton.addActionListener(this);
         this.newFolderButton.setActionCommand("newFolderAction");
         var7.add(this.newFolderButton);
      }

      if (!this.directoryChooser) {
         var7.add(Box.createHorizontalStrut(this.xGap * 2));
      }

      ButtonGroup var10 = new ButtonGroup();
      final JToggleButton var11 = new JToggleButton(this.listViewIcon);
      var11.setToolTipText(UIManager.getString("FileChooser.listViewButtonToolTipText", var2));
      if (UIManager.getLookAndFeel() instanceof SyntheticaLookAndFeel) {
         var11.setMargin(new Insets(0, 0, 0, 0));
         if (!var8) {
            var11.setBorderPainted(false);
         }
      } else {
         var11.setMargin(new Insets(2, 2, 2, 2));
      }

      var11.addActionListener(this);
      var11.setActionCommand("view.listAction");
      var10.add(var11);
      if (!this.directoryChooser) {
         var7.add(var11);
      }

      final JToggleButton var12 = new JToggleButton(this.detailsViewIcon);
      var12.setToolTipText(UIManager.getString("FileChooser.detailsViewButtonToolTipText", var2));
      if (UIManager.getLookAndFeel() instanceof SyntheticaLookAndFeel) {
         var12.setMargin(new Insets(0, 0, 0, 0));
         if (!var8) {
            var12.setBorderPainted(false);
         }
      } else {
         var12.setMargin(new Insets(2, 2, 2, 2));
      }

      var12.addActionListener(this);
      var12.setActionCommand("view.detailsAction");
      var10.add(var12);
      if (!this.directoryChooser) {
         var7.add(var12);
      }

      var4.gridx = 3;
      var4.fill = 0;
      var4.weightx = 0.0;
      var4.insets = new Insets(0, 0, 0, 0);
      var3.add(var7, var4);
      var1.add(var3, "North");
      this.filePane = this.createFilePane();
      this.filePane.addPropertyChangeListener(new PropertyChangeListener() {
         public void propertyChange(PropertyChangeEvent var1) {
            if (var1.getPropertyName().equals("SET_VIEW")) {
               int var2 = (Integer)var1.getNewValue();
               if (var2 == 0) {
                  var11.setSelected(true);
               } else if (var2 == 1) {
                  var12.setSelected(true);
               }
            }

         }
      });
      if (this.directoryChooser) {
         this.filePane.setView(2);
      } else {
         this.filePane.setView(0);
      }

      var1.addPropertyChangeListener(this.filePane);
      var1.add(this.filePane, "Center");
      JComponent var13 = var1.getAccessory();
      if (var13 != null) {
         this.getAccessoryPanel().add(var13);
      }

      var1.add(this.getAccessoryPanel(), "After");
      JPanel var14 = new JPanel(new GridBagLayout());
      var14.setName("JFileChooser.SouthPanel");
      GridBagConstraints var15 = new GridBagConstraints();
      var15.anchor = SyntheticaLookAndFeel.getBoolean("Synthetica.extendedFileChooser.rightLabelAlignment", var1) ? 13 : 17;
      var15.weighty = 0.0;
      var15.gridwidth = 1;
      var15.gridheight = 1;
      var15.insets = new Insets(0, 0, this.yGap / 2, this.xGap);
      var15.gridx = 0;
      var15.gridy = 0;
      var15.weightx = 0.0;
      var15.fill = 0;
      JLabel var16 = new JLabel(UIManager.getString("FileChooser.fileNameLabelText", var2));
      if (this.directoryChooser) {
         var16.setText(UIManager.getString("DirectoryChooser.directoryNameLabelText", var2));
      }

      var14.add(var16, var15);
      var15.gridx = 1;
      var15.weightx = 1.0;
      var15.fill = 2;
      var15.insets = new Insets(0, 0, this.yGap / 2, 0);
      this.fileNameTextField = new JTextField();
      this.fileNameTextField.setName("JFileChooser.FileNameTextField");
      var14.add(this.fileNameTextField, var15);
      if (!this.directoryChooser) {
         var15.insets = new Insets(0, 0, 0, this.xGap);
         var15.gridx = 0;
         var15.gridy = 1;
         var15.weightx = 0.0;
         var15.fill = 0;
         JLabel var17 = new JLabel(UIManager.getString("FileChooser.filesOfTypeLabelText", var2));
         var14.add(var17, var15);
         var15.gridx = 1;
         var15.weightx = 1.0;
         var15.fill = 2;
         var15.insets = new Insets(0, 0, 0, 0);
         JComboBox var18 = new JComboBox();
         var18.setName("JFileChooser.FilterComboBox");
         var18.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
               super.getListCellRendererComponent(var1, var2, var3, var4, var5);
               if (var1.getFixedCellHeight() != -1) {
                  var1.setFixedCellHeight(-1);
               }

               if (SyntheticaFileChooserUI.this.comboListBackground != null && (var1.getBorder() == null || var1.getBorder().getClass().getName().contains("SynthBorder"))) {
                  var1.setBackground(SyntheticaFileChooserUI.this.comboListBackground);
               }

               if (var2 != null && var2 instanceof FileFilter) {
                  this.setText(((FileFilter)var2).getDescription());
               }

               this.setOpaque(var4);
               return this;
            }

            public String getName() {
               return "ComboBox.listRenderer";
            }
         });
         FilterComboBoxModel var19 = new FilterComboBoxModel();
         var18.setModel(var19);
         var1.addPropertyChangeListener(var19);
         var14.add(var18, var15);
      }

      var15.insets = new Insets(this.yGap * 2, 0, 0, 0);
      var15.gridx = 0;
      var15.gridy = 2;
      var15.gridwidth = 2;
      var15.weightx = 1.0;
      var15.fill = 2;
      this.approveButton = this.directoryChooser ? new JButton(UIManager.getString("DirectoryChooser.approveButtonText")) : new JButton(this.getApproveButtonText(var1));
      this.approveButton.setName("JFileChooser.ApproveButton");
      this.cancelButton = new JButton(this.cancelButtonText);
      this.cancelButton.setName("JFileChooser.CancelButton");
      this.approveButton.setToolTipText(this.getApproveButtonToolTipText(var1));
      this.approveButton.addActionListener(this.getApproveSelectionAction());
      this.cancelButton.setToolTipText(this.cancelButtonToolTipText);
      this.cancelButton.addActionListener(this.getCancelSelectionAction());
      SpringLayout var24 = new SpringLayout();
      this.controlButtonPanel = new JPanel(var24);
      this.controlButtonPanel.add(this.approveButton);
      this.controlButtonPanel.add(this.cancelButton);
      var24.putConstraint("East", this.cancelButton, 0, "East", this.controlButtonPanel);
      var24.putConstraint("East", this.approveButton, -this.xGap, "West", this.cancelButton);
      Spring var25 = Spring.constant(0);
      Component[] var22;
      int var21 = (var22 = this.controlButtonPanel.getComponents()).length;

      int var20;
      Component var26;
      for(var20 = 0; var20 < var21; ++var20) {
         var26 = var22[var20];
         var25 = Spring.max(var25, var24.getConstraints(var26).getWidth());
      }

      var21 = (var22 = this.controlButtonPanel.getComponents()).length;

      for(var20 = 0; var20 < var21; ++var20) {
         var26 = var22[var20];
         SpringLayout.Constraints var23 = var24.getConstraints(var26);
         var23.setWidth(var25);
      }

      this.controlButtonPanel.setPreferredSize(this.cancelButton.getPreferredSize());
      this.controlButtonPanel.setMinimumSize(this.cancelButton.getPreferredSize());
      var14.add(this.controlButtonPanel, var15);
      var1.add(var14, "South");
   }

   protected FilePane createFilePane() {
      return new FilePane();
   }

   public void actionPerformed(ActionEvent var1) {
      String var2 = var1.getActionCommand();
      if (var2.startsWith("directoryComboBox.select")) {
         this.directoryComboBox.hidePopup();
         this.getFileChooser().setCurrentDirectory((File)this.directoryComboBox.getSelectedItem());
      } else if (var2.startsWith("upFolderAction")) {
         this.getChangeToParentDirectoryAction().actionPerformed(var1);
      } else if (var2.startsWith("goHomeAction")) {
         this.getGoHomeAction().actionPerformed(var1);
      } else if (var2.startsWith("newFolderAction")) {
         this.filePane.newCreatedFile = true;
         this.getNewFolderAction().actionPerformed(var1);
      } else if (var2.startsWith("view.")) {
         if (var2.endsWith("listAction")) {
            this.filePane.setView(0);
         } else if (var2.endsWith("detailsAction")) {
            this.filePane.setView(1);
         } else if (var2.endsWith("treeAction")) {
            this.filePane.setView(2);
         }
      } else if (var2.startsWith("approveAction")) {
         this.getApproveSelectionAction().actionPerformed(new ActionEvent(this, 0, ""));
      } else if (var2.startsWith("orderBy.")) {
         if (this.filePane == null) {
            return;
         }

         if (var2.endsWith("nameAction")) {
            if (this.filePane.sortColumn == 0 && this.filePane.sortOrder == 1) {
               return;
            }

            this.filePane.sortColumn = 0;
         } else if (var2.endsWith("sizeAction")) {
            if (this.filePane.sortColumn == 1 && this.filePane.sortOrder == 1) {
               return;
            }

            this.filePane.sortColumn = 1;
         } else if (var2.endsWith("dateAction")) {
            if (this.filePane.sortColumn == 3 && this.filePane.sortOrder == 1) {
               return;
            }

            this.filePane.sortColumn = 3;
         }

         this.filePane.sortOrder = 1;
         this.filePane.detailsTableSortModel.sort();
         this.filePane.viewPanel.repaint();
         if (this.filePane.view == 2) {
            this.rescanCurrentDirectory(this.getFileChooser());
         }
      } else if (var2.startsWith("refreshAction")) {
         this.rescanCurrentDirectory(this.getFileChooser());
      } else if (var2.startsWith("cutAction")) {
         this.fillFileBuffer(this.cutBuffer);
         this.copyBuffer.clear();
         this.filePane.clearSelection();
         this.filePane.repaint();
      } else if (var2.startsWith("copyAction")) {
         this.fillFileBuffer(this.copyBuffer);
         if (!this.cutBuffer.isEmpty()) {
            this.cutBuffer.clear();
            this.filePane.repaint();
         }
      } else {
         final HashSet var3;
         if (var2.startsWith("pasteAction")) {
            boolean var4 = false;
            byte var14;
            if (!this.cutBuffer.isEmpty()) {
               var3 = (HashSet)this.cutBuffer.clone();
               var14 = 2;
            } else {
               var3 = (HashSet)this.copyBuffer.clone();
               var14 = 1;
            }

            final File var5 = this.getFileChooser().getCurrentDirectory();
            Window var6 = this.getWindow();
            final FileOperationDialog var7 = var6 instanceof Dialog ? new FileOperationDialog((Dialog)var6, var14) : new FileOperationDialog((Frame)var6, var14);
            var7.applyComponentOrientation(this.getFileChooser().getComponentOrientation());
            final boolean var8 = var14 == 2;
            var7.setVisible(true);
            (new Thread() {
               public void run() {
                  Iterator var1 = var3.iterator();

                  while(var1.hasNext()) {
                     File var2 = (File)var1.next();
                     File var3x = new File(var5, var2.getName());
                     if (!var2.exists()) {
                        var1.remove();
                     } else if (!SyntheticaFileChooserUI.this.cutBuffer.isEmpty() && var2.equals(var3x)) {
                        var1.remove();
                     } else {
                        String var7x;
                        if (var2.equals(var3x)) {
                           for(int var4 = 0; var3x.exists(); var3x = new File(var3x.getParentFile(), var7x)) {
                              ++var4;
                              String var5x = var4 == 1 ? "" : "(" + var4 + ") ";
                              String var6 = UIManager.getString("FileChooser.copyAction.copyFilename", SyntheticaFileChooserUI.this.getFileChooser().getLocale());
                              var7x = MessageFormat.format(var6, var5x, var2.getName());
                           }
                        }

                        try {
                           boolean var9 = !FileUtils.copy(var2, var3x, true, true, SyntheticaFileChooserUI.this.copyBuffer.isEmpty(), var7);
                           if (var9) {
                              break;
                           }
                        } catch (IOException var8x) {
                           var8x.printStackTrace();
                        }
                     }
                  }

                  var7.dispose();
                  SwingUtilities.invokeLater(new Runnable() {
                     public void run() {
                        if (SyntheticaFileChooserUI.this.filePane.view == 2 && var8) {
                           HashSet var1 = new HashSet();
                           Iterator var3x = var3.iterator();

                           File var2;
                           while(var3x.hasNext()) {
                              var2 = (File)var3x.next();
                              var1.add(var2.getParentFile());
                           }

                           var3x = var1.iterator();

                           while(var3x.hasNext()) {
                              var2 = (File)var3x.next();
                              TreePath var4 = SyntheticaFileChooserUI.this.filePane.getPath(var2);
                              ((FilePane.FileNode)var4.getLastPathComponent()).prepareForUpdateChildren();
                              ((DefaultTreeModel)SyntheticaFileChooserUI.this.filePane.tree.getModel()).nodeStructureChanged((TreeNode)var4.getLastPathComponent());
                           }
                        }

                        SyntheticaFileChooserUI.this.rescanCurrentDirectory(SyntheticaFileChooserUI.this.getFileChooser());
                     }
                  });
               }
            }).start();
            if (!this.cutBuffer.isEmpty()) {
               this.cutBuffer.clear();
            }
         } else if (var2.startsWith("deleteAction")) {
            if (this.readOnly || this.filePane.isSelectionEmpty()) {
               return;
            }

            String var13 = UIManager.getString("FileChooser.deleteAction.confirmMessage");
            String var17 = UIManager.getString("FileChooser.deleteAction.confirmDialogTitle");
            JOptionPane var15 = new JOptionPane();
            SynthStyle var18 = SynthLookAndFeel.getStyleFactory().getStyle(var15, Region.OPTION_PANE);
            SynthContext var21 = new SynthContext(var15, Region.OPTION_PANE, var18, 0);
            Icon var22 = ((DefaultSynthStyle)var18).getIcon(var21, "OptionPane.stopIcon");
            int var9 = JOptionPane.showConfirmDialog(this.getFileChooser(), var13, var17, 2, 3, var22);
            if (var9 != 0) {
               return;
            }

            final HashSet var10 = new HashSet();
            this.fillFileBuffer(var10);
            Window var11 = this.getWindow();
            final FileOperationDialog var12 = var11 instanceof Dialog ? new FileOperationDialog((Dialog)var11, 3) : new FileOperationDialog((Frame)var11, 3);
            var12.applyComponentOrientation(this.getFileChooser().getComponentOrientation());
            var12.setVisible(true);
            (new Thread() {
               public void run() {
                  Iterator var1 = var10.iterator();

                  while(var1.hasNext()) {
                     File var2 = (File)var1.next();
                     boolean var3 = !FileUtils.delete(var2, true, var12);
                     if (var3) {
                        break;
                     }

                     var1.remove();
                  }

                  var12.dispose();
                  SwingUtilities.invokeLater(new Runnable() {
                     public void run() {
                        if (SyntheticaFileChooserUI.this.filePane.view == 2 && !SyntheticaFileChooserUI.this.getFileChooser().getCurrentDirectory().exists()) {
                           File var1 = SyntheticaFileChooserUI.this.getFileChooser().getCurrentDirectory().getParentFile();
                           SyntheticaFileChooserUI.this.getFileChooser().setSelectedFile(var1);
                           SyntheticaFileChooserUI.this.getFileChooser().setCurrentDirectory(var1);
                        }

                        SyntheticaFileChooserUI.this.rescanCurrentDirectory(SyntheticaFileChooserUI.this.getFileChooser());
                     }
                  });
               }
            }).start();
         } else if (var2.startsWith("propertiesAction")) {
            if (this.filePane.isSelectionEmpty()) {
               return;
            }

            var3 = new HashSet();
            this.fillFileBuffer(var3);
            Window var19 = this.getWindow();
            final FilePropertiesDialog var16 = var19 instanceof Dialog ? new FilePropertiesDialog((Dialog)var19) : new FilePropertiesDialog((Frame)var19);
            final FileProperties var20 = new FileProperties();
            var16.applyComponentOrientation(this.getFileChooser().getComponentOrientation());
            var16.setVisible(true);
            (new Thread() {
               public void run() {
                  Iterator var1 = var3.iterator();

                  while(var1.hasNext()) {
                     File var2 = (File)var1.next();
                     boolean var3x = false;

                     try {
                        var3x = !FileUtils.determineProperties(var20, var2, true, var16);
                     } catch (IOException var5) {
                        var5.printStackTrace();
                     }

                     if (var3x) {
                        break;
                     }
                  }

                  var16.refresh();
               }
            }).start();
         } else if (var2.startsWith("renameAction")) {
            if (this.filePane.view == 2) {
               this.filePane.tree.startEditingAtPath(this.filePane.tree.getSelectionModel().getSelectionPath());
            }

            this.filePane.editFileName();
         }
      }

   }

   private Window getWindow() {
      for(Container var1 = this.getFileChooser().getParent(); var1 != null; var1 = var1.getParent()) {
         if (var1 instanceof Window) {
            return (Window)var1;
         }
      }

      return null;
   }

   private synchronized void fillFileBuffer(Collection var1) {
      var1.clear();
      int var4;
      if (this.filePane.view == 2) {
         TreePath[] var5;
         var4 = (var5 = this.filePane.tree.getSelectionModel().getSelectionPaths()).length;

         for(int var3 = 0; var3 < var4; ++var3) {
            TreePath var2 = var5[var3];
            FilePane.FileNode var6 = (FilePane.FileNode)var2.getLastPathComponent();
            var1.add(var6.getFile());
         }
      } else {
         ListSelectionModel var7 = this.filePane.list.getSelectionModel();
         FilePane.DetailsTableSortModel var8 = this.filePane.detailsTableSortModel;

         for(var4 = 0; var4 < var8.getRowCount(); ++var4) {
            File var9 = (File)var8.getValueAt(var4, 0);
            if (var7.isSelectedIndex(var4)) {
               var1.add(var9);
            }
         }
      }

   }

   public void setFileName(String var1) {
      this.fileNameTextField.setText(var1);
   }

   public String getFileName() {
      return this.fileNameTextField.getText();
   }

   public PropertyChangeListener createPropertyChangeListener(JFileChooser var1) {
      return new PropertyChangeListener() {
         public void propertyChange(PropertyChangeEvent var1) {
            String var2 = var1.getPropertyName();
            if (var2.equals("SelectedFileChangedProperty")) {
               SyntheticaFileChooserUI.this.doSelectedFileChanged(var1);
            } else if (var2.equals("SelectedFilesChangedProperty")) {
               SyntheticaFileChooserUI.this.doSelectedFilesChanged(var1);
            } else if (var2.equals("directoryChanged")) {
               SyntheticaFileChooserUI.this.doDirectoryChanged(var1);
            } else if (!var2.equals("fileFilterChanged")) {
               if (var2.equals("fileSelectionChanged")) {
                  SyntheticaFileChooserUI.this.doFileSelectionModeChanged(var1);
               } else if (var2.equals("AccessoryChangedProperty")) {
                  SyntheticaFileChooserUI.this.doAccessoryChanged(var1);
               } else if (!var2.equals("ApproveButtonTextChangedProperty") && !var2.equals("ApproveButtonToolTipTextChangedProperty")) {
                  if (var2.equals("DialogTypeChangedProperty")) {
                     SyntheticaFileChooserUI.this.doDialogTypeChanged(var1);
                  } else if (!var2.equals("ApproveButtonMnemonicChangedProperty")) {
                     if (var2.equals("ControlButtonsAreShownChangedProperty")) {
                        if (SyntheticaFileChooserUI.this.getFileChooser().getControlButtonsAreShown()) {
                           SyntheticaFileChooserUI.this.controlButtonPanel.setVisible(true);
                        } else {
                           SyntheticaFileChooserUI.this.controlButtonPanel.setVisible(false);
                        }
                     } else if (var2.equals("componentOrientation")) {
                        ComponentOrientation var3 = (ComponentOrientation)var1.getNewValue();
                        JFileChooser var4 = (JFileChooser)var1.getSource();
                        if (var3 != (ComponentOrientation)var1.getOldValue()) {
                           var4.applyComponentOrientation(var3);
                           SyntheticaFileChooserUI.this.filePane.listPanel.applyComponentOrientation(var3);
                           SyntheticaFileChooserUI.this.filePane.tablePanel.applyComponentOrientation(var3);
                           SyntheticaFileChooserUI.this.filePane.table.setIntercellSpacing(new Dimension(0, 0));
                           SpringLayout var5 = new SpringLayout();
                           if (var4.getComponentOrientation() == ComponentOrientation.LEFT_TO_RIGHT) {
                              var5.putConstraint("East", SyntheticaFileChooserUI.this.cancelButton, 0, "East", SyntheticaFileChooserUI.this.controlButtonPanel);
                              var5.putConstraint("East", SyntheticaFileChooserUI.this.approveButton, -SyntheticaFileChooserUI.this.xGap, "West", SyntheticaFileChooserUI.this.cancelButton);
                           } else {
                              var5.putConstraint("West", SyntheticaFileChooserUI.this.cancelButton, 0, "West", SyntheticaFileChooserUI.this.controlButtonPanel);
                              var5.putConstraint("West", SyntheticaFileChooserUI.this.approveButton, SyntheticaFileChooserUI.this.xGap, "East", SyntheticaFileChooserUI.this.cancelButton);
                           }

                           SyntheticaFileChooserUI.this.controlButtonPanel.setLayout(var5);
                        }
                     } else if (var2 != "FileChooser.useShellFolder" && var2.equals("ancestor") && var1.getOldValue() == null) {
                        var1.getNewValue();
                     }
                  }
               } else {
                  SyntheticaFileChooserUI.this.doApproveButtonTextChanged(var1);
               }
            }

         }
      };
   }

   private void doSelectedFileChanged(PropertyChangeEvent var1) {
      File var2 = (File)var1.getNewValue();
      if (var2 != null) {
         ((FilePane.DetailsTableModel)this.filePane.detailsTableSortModel.model).doFileSelection();
         JFileChooser var3 = this.getFileChooser();
         if (!this.directoryChooser && var3.getFileSelectionMode() != 1) {
            if (var3.isDirectorySelectionEnabled() || !var2.isDirectory()) {
               this.setFileName(var2.getName());
            }
         } else if (var3.getFileSystemView().isFileSystem(var2)) {
            this.setFileName(var2.getPath());
         }
      }

   }

   private void doSelectedFilesChanged(PropertyChangeEvent var1) {
      File[] var2 = (File[])var1.getNewValue();
      if (var2 != null && var2.length > 1) {
         StringBuilder var3 = new StringBuilder();

         for(int var4 = 0; var4 < var2.length; ++var4) {
            var3.append("\"");
            var3.append(var2[var4].getName());
            var3.append("\" ");
         }

         this.setFileName(var3.toString().trim());
      }

   }

   private void doDirectoryChanged(PropertyChangeEvent var1) {
      JFileChooser var2 = this.getFileChooser();
      File var3 = var2.getCurrentDirectory();
      if (!this.readOnly) {
         if (var3.exists() && var3.canWrite()) {
            this.newFolderButton.setEnabled(true);
         } else {
            this.newFolderButton.setEnabled(false);
         }
      }

      ((DirectoryComboBoxModel)this.directoryComboBox.getModel()).addItem(var3);
      boolean var4 = false;
      StackTraceElement[] var8;
      int var7 = (var8 = (new Throwable()).getStackTrace()).length;

      for(int var6 = 0; var6 < var7; ++var6) {
         StackTraceElement var5 = var8[var6];
         if (var5.toString().contains("setSelectedFile")) {
            var4 = true;
            break;
         }
      }

      if (this.treePanelEnabled) {
         TreePath var9 = this.filePane.getPath(var3);
         if (this.filePane.tree.getSelectionPath() == null || var9 != null && !var9.equals(this.filePane.tree.getSelectionPath()) && !var4) {
            this.filePane.tree.setSelectionPath(var9);
            this.filePane.tree.scrollPathToVisible(var9);
         }
      }

      if ((this.directoryChooser || var2.getFileSelectionMode() == 1) && var3 != null && var2.getFileSystemView().isFileSystem(var3)) {
         this.setFileName(var3.getPath());
      }

      this.upFolderButton.setEnabled(!var2.getFileSystemView().isRoot(var3));
   }

   private void doFileSelectionModeChanged(PropertyChangeEvent var1) {
      JFileChooser var2 = this.getFileChooser();
      File var3 = var2.getCurrentDirectory();
      if (var3 != null && (Integer)var1.getNewValue() == 1 && var2.getFileSystemView().isFileSystem(var3)) {
         this.setFileName(var3.getPath());
      } else {
         this.setFileName((String)null);
      }

   }

   private void doAccessoryChanged(PropertyChangeEvent var1) {
      if (this.getAccessoryPanel() != null) {
         JComponent var2 = (JComponent)var1.getOldValue();
         if (var2 != null) {
            this.getAccessoryPanel().remove(var2);
         }

         var2 = (JComponent)var1.getNewValue();
         if (var2 != null) {
            this.getAccessoryPanel().add(var2, "Center");
         }

      }
   }

   private void doApproveButtonTextChanged(PropertyChangeEvent var1) {
      JFileChooser var2 = this.getFileChooser();
      this.approveButton.setText(this.getApproveButtonText(var2));
      this.approveButton.setToolTipText(this.getApproveButtonToolTipText(var2));
   }

   private void doDialogTypeChanged(PropertyChangeEvent var1) {
      JFileChooser var2 = this.getFileChooser();
      this.doApproveButtonTextChanged(var1);
      if (var2.getDialogType() == 0) {
         this.lookInLabel.setText(UIManager.getString("FileChooser.lookInLabelText", var2.getLocale()));
      } else if (var2.getDialogType() == 1) {
         this.lookInLabel.setText(UIManager.getString("FileChooser.saveInLabelText", var2.getLocale()));
      }

   }

   public void rescanCurrentDirectory(JFileChooser var1) {
      this.getModel().validateFileCache();
      if (this.treePanelEnabled) {
         File var2 = this.getFileChooser().getCurrentDirectory();
         TreePath var3 = this.filePane.getPath(var2);
         ((FilePane.FileNode)var3.getLastPathComponent()).prepareForUpdateChildren();
         ((DefaultTreeModel)this.filePane.tree.getModel()).nodeStructureChanged((TreeNode)var3.getLastPathComponent());
      }

   }

   private ImageIcon createBrightIcon(Icon var1) {
      BufferedImage var2 = new BufferedImage(var1.getIconWidth(), var1.getIconHeight(), 2);
      var1.paintIcon((Component)null, var2.getGraphics(), 0, 0);
      new ImageIcon(var2);
      ImageIcon var3 = new ImageIcon((new JPanel()).createImage(new FilteredImageSource(var2.getSource(), new RGBImageFilter() {
         public int filterRGB(int var1, int var2, int var3) {
            return 1627389951 & var3;
         }
      })));
      var2.getGraphics().dispose();
      return var3;
   }

   private class DirectoryComboBoxModel extends AbstractListModel implements ComboBoxModel {
      private ArrayList directories = new ArrayList();
      private ArrayList depths = new ArrayList();
      private File selectedDirectory = null;

      public DirectoryComboBoxModel() {
         File var2 = SyntheticaFileChooserUI.this.getFileChooser().getCurrentDirectory();
         if (var2 != null) {
            this.addItem(var2);
         }

      }

      private void addItem(File var1) {
         if (var1 != null) {
            this.directories.clear();
            this.depths.clear();
            boolean var2 = SyntheticaFileChooserUI.this.getFileChooser().getFileSystemView().equals(FileSystemView.getFileSystemView());
            Boolean var3 = (Boolean)SyntheticaFileChooserUI.this.getFileChooser().getClientProperty("FileChooser.useShellFolder");
            if (var3 != null) {
               var2 = var3;
            }

            File[] var4 = var2 ? (File[])ShellFolder.get("fileChooserComboBoxFolders") : SyntheticaFileChooserUI.this.getFileChooser().getFileSystemView().getRoots();
            this.directories.addAll(Arrays.asList(var4));

            try {
               var1 = ((File)var1).getCanonicalFile();
            } catch (IOException var14) {
            }

            try {
               var1 = ShellFolder.getShellFolder((File)var1);
            } catch (IOException var13) {
            }

            Object var5 = var1;
            ArrayList var6 = new ArrayList();

            do {
               var6.add(var5);
            } while((var5 = ((File)var5).getParentFile()) != null);

            ArrayList var7 = new ArrayList();

            label82:
            for(int var8 = 0; var8 < var6.size(); ++var8) {
               File var15 = (File)var6.get(var8);
               if (this.directories.contains(var15)) {
                  Iterator var9 = this.directories.iterator();

                  while(true) {
                     while(true) {
                        if (!var9.hasNext()) {
                           break label82;
                        }

                        File var10 = (File)var9.next();
                        if (var10.equals(var15) && !this.directories.contains(var1)) {
                           for(int var16 = var8; var16 >= 0; --var16) {
                              var7.add((File)var6.get(var16));
                              this.depths.add(new Integer(var6.size() - 1 - var16));
                           }
                        } else {
                           var7.add(var10);
                           File var11 = var10;

                           int var12;
                           for(var12 = 0; (var11 = var11.getParentFile()) != null && var7.contains(var11); ++var12) {
                           }

                           this.depths.add(new Integer(var12));
                        }
                     }
                  }
               }
            }

            this.directories = var7;
            this.setSelectedItem(var1);
         }
      }

      public int getDepth(int var1) {
         return var1 >= 0 && var1 < this.depths.size() ? (Integer)this.depths.get(var1) : 0;
      }

      public void setSelectedItem(Object var1) {
         this.selectedDirectory = (File)var1;
         this.fireContentsChanged(this, -1, -1);
      }

      public Object getSelectedItem() {
         return this.selectedDirectory;
      }

      public int getSize() {
         return this.directories.size();
      }

      public Object getElementAt(int var1) {
         return this.directories.get(var1);
      }
   }

   public class FilePane extends JPanel implements PropertyChangeListener {
      private static final int LIST_WIDTH = 600;
      private static final int LIST_HEIGHT = 205;
      private static final int COLUMN_FILE = 0;
      private static final int COLUMN_FILESIZE = 1;
      private static final int COLUMN_FILETYPE = 2;
      private static final int COLUMN_FILEDATE = 3;
      private static final int COLUMN_FILEATTR = 4;
      private static final int COLUMN_COLCOUNT = 5;
      private int[] COLUMN_WIDTHS = new int[]{175, 60, 100, 100, 30};
      private static final int ASCENDING = 1;
      private static final int DESCENDING = -1;
      private int sortColumn = 0;
      private int sortOrder = 1;
      private int view = -1;
      private JPanel viewPanel;
      private JPanel listPanel;
      private JPanel tablePanel;
      private JPanel treePanel;
      private JPopupMenu fileContextMenu;
      private JList list;
      private JTable table;
      private JTree tree;
      private DetailsTableSortModel detailsTableSortModel;
      private String fileSizeHeaderText;
      private File editFile;
      private JTextField editTextField;
      private boolean newCreatedFile;
      private int editX = 20;
      private int editIndex = -1;
      String[] columnNames;

      public FilePane() {
         super(new BorderLayout());
         this.setName("JFileChooser.FilePanel");
         this.editTextField = new JTextField();
         this.editTextField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent var1) {
            }

            public void focusLost(FocusEvent var1) {
               FilePane.this.applyEditFileName();
            }
         });
         this.editTextField.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent var1) {
               if (var1.getKeyChar() == 27) {
                  FilePane.this.cancelEditFileName();
               } else if (var1.getKeyChar() == '\n') {
                  FilePane.this.applyEditFileName();
               }

            }

            public void keyPressed(KeyEvent var1) {
            }

            public void keyReleased(KeyEvent var1) {
            }
         });
         this.listPanel = this.createListPanel();
         this.tablePanel = this.createTablePanel();
         if (SyntheticaFileChooserUI.this.treePanelEnabled) {
            this.treePanel = this.createTreePanel();
         }

         this.viewPanel = new JPanel(new BorderLayout());
         this.viewPanel.setName("JFileChooser.ViewPanel");
         this.add(this.viewPanel);
         SyntheticaFileChooserUI.this.getFileChooser().registerKeyboardAction(SyntheticaFileChooserUI.this, "deleteAction", KeyStroke.getKeyStroke(127, 0), 1);
         this.viewPanel.registerKeyboardAction(SyntheticaFileChooserUI.this, "renameAction", KeyStroke.getKeyStroke(113, 0), 2);
         this.table.registerKeyboardAction(SyntheticaFileChooserUI.this, "renameAction", KeyStroke.getKeyStroke(113, 0), 1);
      }

      private void setView(int var1) {
         if (this.view != var1) {
            this.firePropertyChange("SET_VIEW", this.view, var1);
            this.view = var1;
            this.viewPanel.removeAll();
            if (this.view == 0) {
               this.viewPanel.add(this.listPanel);
            } else if (this.view == 1) {
               this.viewPanel.add(this.tablePanel);
            } else if (this.view == 2) {
               this.viewPanel.add(this.treePanel);
            }

            this.setInheritPopupMenu(this.viewPanel, true);
            SyntheticaFileChooserUI.this.actionPerformed(new ActionEvent(this, 0, "orderBy.nameAction"));
            this.revalidate();
            this.viewPanel.repaint();
         }
      }

      private int getView() {
         return this.view;
      }

      private void setInheritPopupMenu(Container var1, boolean var2) {
         int var3 = var1.getComponentCount();
         if (var1 instanceof JComponent) {
            ((JComponent)var1).setInheritsPopupMenu(var2);
         }

         for(int var4 = 0; var4 < var3; ++var4) {
            this.setInheritPopupMenu((Container)var1.getComponent(var4), var2);
         }

      }

      protected JPanel createListPanel() {
         JPanel var1 = new JPanel(new BorderLayout());
         JFileChooser var2 = SyntheticaFileChooserUI.this.getFileChooser();
         this.list = new JList();
         this.list.setName("JFileChooser.List");
         this.list.putClientProperty("List.isFileList", Boolean.TRUE);
         this.list.setCellRenderer(new ListRenderer(this.list.getCellRenderer()));
         this.list.setLayoutOrientation(1);
         this.list.setVisibleRowCount(-1);
         this.list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent var1) {
               File var2 = (File)FilePane.this.list.getSelectedValue();
               if (SyntheticaFileChooserUI.this.treePanelEnabled && var2 != null) {
                  TreePath var3 = FilePane.this.getPath(var2);
                  FilePane.this.tree.setSelectionPath(var3);
                  FilePane.this.tree.scrollPathToVisible(var3);
               }

               if (((ListSelectionModel)var1.getSource()).isSelectionEmpty()) {
                  FilePane.this.editIndex = -1;
               }

            }
         });
         this.list.setModel(new ListModel() {
            public int getSize() {
               return FilePane.this.detailsTableSortModel.getRowCount();
            }

            public Object getElementAt(int var1) {
               return FilePane.this.detailsTableSortModel.getValueAt(var1, 0);
            }

            public void addListDataListener(ListDataListener var1) {
               SyntheticaFileChooserUI.this.getModel().addListDataListener(var1);
            }

            public void removeListDataListener(ListDataListener var1) {
               SyntheticaFileChooserUI.this.getModel().removeListDataListener(var1);
            }
         });
         this.list.addMouseListener(new FilePaneMouseListener(this.list));
         this.list.addListSelectionListener(SyntheticaFileChooserUI.this.createListSelectionListener(var2));
         this.list.setSelectionMode(0);
         KeyListener[] var6;
         int var5 = (var6 = this.list.getKeyListeners()).length;

         for(int var4 = 0; var4 < var5; ++var4) {
            KeyListener var3 = var6[var4];
            this.list.removeKeyListener(var3);
         }

         this.list.addKeyListener(SyntheticaFileChooserUI.this.new FileSelectHandler());
         JScrollPane var7 = new JScrollPane(this.list);
         var1.setPreferredSize(new Dimension(600, 205));
         var1.add(var7, "Center");
         return var1;
      }

      protected JPanel createTablePanel() {
         JPanel var1 = new JPanel(new BorderLayout());
         JFileChooser var2 = SyntheticaFileChooserUI.this.getFileChooser();
         Locale var3 = var2.getLocale();
         String var4 = UIManager.getString("FileChooser.fileNameHeaderText", var3);
         this.fileSizeHeaderText = UIManager.getString("FileChooser.fileSizeHeaderText", var3);
         String var5 = UIManager.getString("FileChooser.fileTypeHeaderText", var3);
         String var6 = UIManager.getString("FileChooser.fileDateHeaderText", var3);
         String var7 = UIManager.getString("FileChooser.fileAttrHeaderText", var3);
         this.columnNames = new String[]{var4, this.fileSizeHeaderText, var5, var6, var7};
         this.table = new JTable() {
            public boolean editCellAt(int var1, int var2, EventObject var3) {
               return !JavaVersion.JAVA5 && this.isEditing() ? true : super.editCellAt(var1, var2, var3);
            }
         };
         this.table.setName("JFileChooser.Table");
         this.table.putClientProperty("Table.isFileList", Boolean.TRUE);
         this.detailsTableSortModel = new DetailsTableSortModel(new DetailsTableModel(var2));
         this.table.setSelectionModel(this.list.getSelectionModel());
         this.table.setModel(this.detailsTableSortModel);
         this.table.setShowGrid(false);
         this.table.setIntercellSpacing(new Dimension(0, 0));
         int var8 = this.table.getRowHeight();
         Icon var9 = null;
         FileSystemView var10 = var2.getFileSystemView();

         try {
            var9 = var10.getSystemIcon(var10.getHomeDirectory());
         } catch (Exception var17) {
            try {
               var9 = var10.getSystemIcon(var10.getDefaultDirectory());
            } catch (Exception var16) {
               var9 = SyntheticaFileChooserUI.this.homeFolderIcon;
            }
         }

         if (var8 <= var9.getIconHeight()) {
            this.table.setRowHeight(var9.getIconHeight() + 1);
         }

         this.table.getTableHeader().setDefaultRenderer(new TableHeaderRenderer(this.table));
         this.table.getTableHeader().addMouseListener(new FilePaneMouseListener(this.table));
         TableColumnModel var11 = this.table.getColumnModel();
         TableColumn[] var12 = new TableColumn[5];

         for(int var13 = 0; var13 < 5; ++var13) {
            var12[var13] = var11.getColumn(var13);
            var12[var13].setPreferredWidth(this.COLUMN_WIDTHS[var13]);
         }

         String var18 = (String)AccessController.doPrivileged(new GetPropertyAction("os.name"));
         if (var18 == null || !var18.startsWith("Windows")) {
            var11.removeColumn(var12[2]);
            var11.removeColumn(var12[4]);
         }

         DetailsTableCellRenderer var14 = new DetailsTableCellRenderer(var2);
         this.table.setDefaultRenderer(File.class, var14);
         this.table.setDefaultRenderer(Long.class, var14);
         this.table.setDefaultRenderer(Date.class, var14);
         this.table.setDefaultRenderer(Object.class, var14);
         var12[0].setCellEditor(new DefaultCellEditor(this.editTextField));
         this.table.addMouseListener(new FilePaneMouseListener(this.table));
         this.table.addKeyListener(SyntheticaFileChooserUI.this.new FileSelectHandler());
         JScrollPane var15 = new JScrollPane(this.table);
         var15.getViewport().setBackground(this.table.getBackground());
         if (UIManager.get("Table.scrollPaneCornerComponent") == null) {
            var15.setCorner("UPPER_TRAILING_CORNER", new TableHeaderCorner(this.table));
         }

         var1.add(var15, "Center");
         return var1;
      }

      protected JPanel createTreePanel() {
         JPanel var1 = new JPanel(new BorderLayout());
         final JFileChooser var2 = SyntheticaFileChooserUI.this.getFileChooser();
         this.tree = new JTree();
         this.tree.setName("JFileChooser.Tree");
         this.tree.setEditable(true);
         FileNode var3 = new FileNode(var2.getFileSystemView().getRoots()[0]);
         this.tree.setModel(new DefaultTreeModel(var3));
         this.tree.setCellRenderer(new TreeRenderer((TreeRenderer)null));
         this.tree.setCellEditor(new DefaultTreeCellEditor(this.tree, (DefaultTreeCellRenderer)this.tree.getCellRenderer(), new FileCellEditor(new JTextField())));
         this.tree.getCellEditor().addCellEditorListener(new CellEditorListener() {
            public void editingStopped(ChangeEvent var1) {
               FileCellEditor var2 = (FileCellEditor)var1.getSource();
               String var3 = var2.getDelegateValue();
               FilePane.this.editTextField.setText(var3);
               FilePane.this.editFile = ((FileNode)FilePane.this.tree.getSelectionPath().getLastPathComponent()).getFile();
               File var4 = FilePane.this.applyEditFileName();
               TreePath var5 = SyntheticaFileChooserUI.this.filePane.getPath(var4);
               ((FileNode)var5.getLastPathComponent()).prepareForUpdateChildren();
               ((DefaultTreeModel)SyntheticaFileChooserUI.this.filePane.tree.getModel()).nodeStructureChanged((TreeNode)var5.getLastPathComponent());
            }

            public void editingCanceled(ChangeEvent var1) {
            }
         });
         this.tree.setVisibleRowCount(-1);
         this.tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent var1) {
               if (FilePane.this.tree.getSelectionPath() != null && FilePane.this.view == 2) {
                  final File var2x = ((FileNode)FilePane.this.tree.getSelectionPath().getLastPathComponent()).getFile().getAbsoluteFile();
                  File var3 = var2.getCurrentDirectory();
                  File var4 = var2x.isDirectory() && FilePane.this.view == 2 ? var2x : var2x.getParentFile();
                  if (!var3.equals(var4)) {
                     var2.setCurrentDirectory(var4);
                  }

                  if (!var2x.equals(var2.getSelectedFile())) {
                     var2.setSelectedFile(var2x);
                     if (!var3.equals(var2.getCurrentDirectory())) {
                        FilePane.this.detailsTableSortModel.addTableModelListener(new TableModelListener() {
                           public void tableChanged(TableModelEvent var1) {
                              FilePane.this.detailsTableSortModel.removeTableModelListener(this);
                              SwingUtilities.invokeLater(new Runnable() {
                                 public void run() {
                                    FilePane.this.list.setSelectedValue(var2x, true);
                                 }
                              });
                           }
                        });
                     } else {
                        FilePane.this.list.setSelectedValue(var2x, true);
                     }
                  }

               }
            }
         });
         this.tree.addMouseListener(new FilePaneMouseListener(this.list));
         this.tree.getSelectionModel().setSelectionMode(1);
         this.tree.addKeyListener(SyntheticaFileChooserUI.this.new FileSelectHandler());
         JScrollPane var4 = new JScrollPane(this.tree);
         var1.setPreferredSize(new Dimension(600, 205));
         var1.add(var4, "Center");
         return var1;
      }

      private TreePath getPath(File var1) {
         FileNode var2 = (FileNode)this.tree.getModel().getRoot();
         if (!var2.getFile().equals(var1) && var1 != null) {
            TreePath var3 = this.getPath(SyntheticaFileChooserUI.this.getFileChooser().getFileSystemView().getParentDirectory(var1));
            FileNode var4 = (FileNode)var3.getLastPathComponent();
            Enumeration var5 = var4.children();

            FileNode var6;
            do {
               if (!var5.hasMoreElements()) {
                  return var3;
               }

               var6 = (FileNode)var5.nextElement();
            } while(!var6.getFile().exists() || !var6.getFile().equals(var1));

            return var3.pathByAddingChild(var6);
         } else {
            return new TreePath(var2);
         }
      }

      public boolean isSelectionEmpty() {
         return this.view == 2 ? this.tree.getSelectionModel().isSelectionEmpty() : this.list.getSelectionModel().isSelectionEmpty();
      }

      public void clearSelection() {
         if (SyntheticaFileChooserUI.this.treePanelEnabled) {
            this.tree.getSelectionModel().clearSelection();
         }

         this.list.getSelectionModel().clearSelection();
      }

      public void propertyChange(PropertyChangeEvent var1) {
         String var2 = var1.getPropertyName();
         if (var2.equals("MultiSelectionEnabledChangedProperty")) {
            if (SyntheticaFileChooserUI.this.getFileChooser().isMultiSelectionEnabled()) {
               this.list.setSelectionMode(2);
               this.table.setSelectionMode(2);
            } else {
               this.list.setSelectionMode(0);
               this.table.setSelectionMode(0);
            }
         }

      }

      private void editFileName() {
         this.editIndex = this.list.getSelectedIndex();
         if (this.editIndex >= 0 && !SyntheticaFileChooserUI.this.directoryChooser) {
            this.editFile = (File)this.detailsTableSortModel.getValueAt(this.editIndex, 0);
            File var1 = SyntheticaFileChooserUI.this.getFileChooser().getCurrentDirectory();
            if (var1.canWrite() && this.editFile.canWrite() && !SyntheticaFileChooserUI.this.readOnly && !SyntheticaFileChooserUI.this.getFileChooser().getFileSystemView().isFileSystemRoot(this.editFile)) {
               Rectangle var2 = null;
               if (this.view == 0) {
                  var2 = this.list.getCellBounds(this.editIndex, this.editIndex);
                  this.list.add(this.editTextField);
               }

               ComponentOrientation var3 = this.list.getComponentOrientation();
               this.editTextField.setComponentOrientation(var3);
               if (this.view == 0) {
                  if (var3.isLeftToRight()) {
                     this.editTextField.setBounds(this.editX + var2.x, var2.y, var2.width - this.editX, var2.height);
                  } else {
                     this.editTextField.setBounds(var2.x, var2.y, var2.width - this.editX, var2.height);
                  }
               } else if (this.view == 1) {
                  this.table.editCellAt(this.editIndex, this.table.convertColumnIndexToView(0));
               }

               this.editTextField.setText(this.editFile.getName());
               this.editTextField.requestFocus();
               this.editTextField.selectAll();
               SyntheticaFileChooserUI.this.getFileChooser().getInputMap(1).put(KeyStroke.getKeyStroke(27, 0), "NO_ACTION");
               this.table.getActionMap().put("NO_ACTION_KEY", new AbstractAction() {
                  public void actionPerformed(ActionEvent var1) {
                  }
               });
               this.table.getInputMap(1).put(KeyStroke.getKeyStroke(27, 0), "NO_ACTION_KEY");
            } else {
               this.cancelEditFileName();
            }
         }
      }

      private File applyEditFileName() {
         if (this.editFile == null) {
            return null;
         } else {
            JFileChooser var1 = SyntheticaFileChooserUI.this.getFileChooser();
            String var2 = var1.getName(this.editFile);
            String var3 = this.editFile.getName();
            String var4 = this.editTextField.getText().trim();
            String var5 = var4;
            File var6 = this.editFile;
            if (!var4.equals(var2)) {
               int var7 = var3.length();
               int var8 = var2.length();
               if (var7 > var8 && var3.charAt(var8) == '.') {
                  var5 = var4 + var3.substring(var8);
               }

               FileSystemView var9 = var1.getFileSystemView();
               var6 = var9.createFileObject(this.editFile.getParentFile(), var5);
               TreePath var10 = null;
               if (SyntheticaFileChooserUI.this.treePanelEnabled) {
                  var10 = this.getPath(this.editFile);
               }

               if (var6.exists()) {
                  String var11 = UIManager.getString("FileRenameErrorDialog.title");
                  String var12 = String.format(UIManager.getString("FileRenameErrorDialog.message"), var6.getName());
                  JOptionPane.showMessageDialog(var1, var12, var11, 0);
               } else if (!var6.exists() && this.editFile.renameTo(var6)) {
                  final Object var13;
                  if (this.editFile.equals(this.list.getSelectedValue())) {
                     var13 = var6;
                  } else {
                     var13 = this.list.getSelectedValue();
                  }

                  SyntheticaFileChooserUI.this.getModel().validateFileCache();
                  if (SyntheticaFileChooserUI.this.treePanelEnabled) {
                     this.tree.getModel().valueForPathChanged(var10, var6);
                  }

                  (new Thread() {
                     public void run() {
                        try {
                           Thread.sleep(350L);
                        } catch (InterruptedException var2) {
                        }

                        SwingUtilities.invokeLater(new Runnable() {
                           public void run() {
                              FilePane.this.list.setSelectedValue(var13, true);
                           }
                        });
                     }
                  }).start();
               }
            }

            this.cancelEditFileName();
            return var6;
         }
      }

      private void cancelEditFileName() {
         this.editIndex = -1;
         if (this.editFile != null) {
            this.editFile = null;
            if (this.view == 0) {
               this.list.remove(this.editTextField);
            } else {
               this.table.editingCanceled(new ChangeEvent(this.table));
            }

            this.repaint();
            SyntheticaFileChooserUI.this.getFileChooser().getInputMap(1).put(KeyStroke.getKeyStroke(27, 0), "cancelSelection");
            this.table.getActionMap().remove("NO_ACTION_KEY");
         }
      }

      public JPopupMenu getComponentPopupMenu() {
         JPopupMenu var1 = SyntheticaFileChooserUI.this.getFileChooser().getComponentPopupMenu();
         if (var1 != null) {
            return var1;
         } else if (this.fileContextMenu != null && this.fileContextMenu.isVisible()) {
            return this.fileContextMenu;
         } else {
            Locale var2 = SyntheticaFileChooserUI.this.getFileChooser().getLocale();
            JFileChooser var3 = SyntheticaFileChooserUI.this.getFileChooser();
            File var4 = var3.getCurrentDirectory();
            String var5 = "";
            if (var3.getDialogType() == 0) {
               var5 = UIManager.getString("FileChooser.contextMenu.open", var2);
            } else if (var3.getDialogType() == 1) {
               var5 = UIManager.getString("FileChooser.contextMenu.save", var2);
            }

            JMenuItem var6 = new JMenuItem("<html><b>" + var5 + "</b>");
            var6.addActionListener(SyntheticaFileChooserUI.this);
            var6.setActionCommand("approveAction");
            if (var3.getSelectedFile() == null && !SyntheticaFileChooserUI.this.isDirectorySelected()) {
               var6.setEnabled(false);
            } else if (var3.getDialogType() != 0 && SyntheticaFileChooserUI.this.isDirectorySelected()) {
               var6.setEnabled(false);
            }

            JRadioButtonMenuItem var7 = new JRadioButtonMenuItem(UIManager.getString("FileChooser.contextMenu.listView", var2));
            if (this.view == 0) {
               var7.setSelected(true);
            }

            var7.addActionListener(SyntheticaFileChooserUI.this);
            var7.setActionCommand("view.listAction");
            JRadioButtonMenuItem var8 = new JRadioButtonMenuItem(UIManager.getString("FileChooser.contextMenu.detailsView", var2));
            if (this.view == 1) {
               var8.setSelected(true);
            }

            var8.addActionListener(SyntheticaFileChooserUI.this);
            var8.setActionCommand("view.detailsAction");
            JMenu var9 = new JMenu(UIManager.getString("FileChooser.contextMenu.view", var2));
            var9.add(var7);
            var9.add(var8);
            JRadioButtonMenuItem var10 = new JRadioButtonMenuItem(UIManager.getString("FileChooser.contextMenu.orderByName", var2));
            var10.addActionListener(SyntheticaFileChooserUI.this);
            var10.setActionCommand("orderBy.nameAction");
            if (this.sortColumn == 0) {
               var10.setSelected(true);
            }

            JRadioButtonMenuItem var11 = new JRadioButtonMenuItem(UIManager.getString("FileChooser.contextMenu.orderBySize", var2));
            var11.addActionListener(SyntheticaFileChooserUI.this);
            var11.setActionCommand("orderBy.sizeAction");
            if (this.sortColumn == 1) {
               var11.setSelected(true);
            }

            JRadioButtonMenuItem var12 = new JRadioButtonMenuItem(UIManager.getString("FileChooser.contextMenu.orderByDate", var2));
            var12.addActionListener(SyntheticaFileChooserUI.this);
            var12.setActionCommand("orderBy.dateAction");
            if (this.sortColumn == 3) {
               var12.setSelected(true);
            }

            JMenu var13 = new JMenu(UIManager.getString("FileChooser.contextMenu.orderBy", var2));
            var13.add(var10);
            var13.add(var11);
            var13.add(var12);
            JMenuItem var14 = new JMenuItem(UIManager.getString("FileChooser.contextMenu.refresh", var2));
            var14.addActionListener(SyntheticaFileChooserUI.this);
            var14.setActionCommand("refreshAction");
            JMenuItem var15 = new JMenuItem(UIManager.getString("FileChooser.contextMenu.newFolder", var2));
            var15.addActionListener(SyntheticaFileChooserUI.this);
            var15.setActionCommand("newFolderAction");
            if (!var4.canWrite()) {
               var15.setEnabled(false);
            }

            JMenuItem var16 = new JMenuItem(UIManager.getString("FileChooser.contextMenu.cut", var2));
            var16.addActionListener(SyntheticaFileChooserUI.this);
            var16.setActionCommand("cutAction");
            if (this.isSelectionEmpty()) {
               var16.setEnabled(false);
            }

            JMenuItem var17 = new JMenuItem(UIManager.getString("FileChooser.contextMenu.copy", var2));
            var17.addActionListener(SyntheticaFileChooserUI.this);
            var17.setActionCommand("copyAction");
            if (this.isSelectionEmpty()) {
               var17.setEnabled(false);
            }

            JMenuItem var18 = new JMenuItem(UIManager.getString("FileChooser.contextMenu.paste", var2));
            var18.addActionListener(SyntheticaFileChooserUI.this);
            var18.setActionCommand("pasteAction");
            if (SyntheticaFileChooserUI.this.cutBuffer.isEmpty() && SyntheticaFileChooserUI.this.copyBuffer.isEmpty() || !var4.canWrite()) {
               var18.setEnabled(false);
            }

            JMenuItem var19 = new JMenuItem(UIManager.getString("FileChooser.contextMenu.delete", var2));
            var19.addActionListener(SyntheticaFileChooserUI.this);
            var19.setActionCommand("deleteAction");
            if (this.isSelectionEmpty() || !var4.canWrite()) {
               var19.setEnabled(false);
            }

            JMenuItem var20 = new JMenuItem(UIManager.getString("FileChooser.contextMenu.rename", var2));
            var20.addActionListener(SyntheticaFileChooserUI.this);
            var20.setActionCommand("renameAction");
            if (this.isSelectionEmpty() || var3.getSelectedFile() == null && !SyntheticaFileChooserUI.this.isDirectorySelected() || !var4.canWrite()) {
               var20.setEnabled(false);
            }

            JMenuItem var21 = new JMenuItem(UIManager.getString("FileChooser.contextMenu.properties", var2));
            var21.addActionListener(SyntheticaFileChooserUI.this);
            var21.setActionCommand("propertiesAction");
            if (this.isSelectionEmpty()) {
               var21.setEnabled(false);
            }

            this.fileContextMenu = new JPopupMenu();
            this.fileContextMenu.setName("JFileChooser.FileContextMenu");
            if (var3.getDialogType() != 2) {
               this.fileContextMenu.add(var6);
               this.fileContextMenu.addSeparator();
            }

            if (!SyntheticaFileChooserUI.this.directoryChooser) {
               this.fileContextMenu.add(var9);
               this.fileContextMenu.addSeparator();
            }

            if (!SyntheticaFileChooserUI.this.directoryChooser && SyntheticaFileChooserUI.this.sortEnabled) {
               this.fileContextMenu.add(var13);
            }

            this.fileContextMenu.add(var14);
            this.fileContextMenu.addSeparator();
            if (!SyntheticaFileChooserUI.this.readOnly) {
               this.fileContextMenu.add(var15);
               this.fileContextMenu.addSeparator();
               this.fileContextMenu.add(var16);
               this.fileContextMenu.add(var17);
               this.fileContextMenu.add(var18);
               this.fileContextMenu.addSeparator();
               this.fileContextMenu.add(var19);
               this.fileContextMenu.add(var20);
               this.fileContextMenu.addSeparator();
            }

            this.fileContextMenu.add(var21);
            this.fileContextMenu.applyComponentOrientation(var3.getComponentOrientation());
            ContainerEvent var22 = new ContainerEvent(SyntheticaFileChooserUI.this.filePane, 300, this.fileContextMenu);
            ContainerListener[] var26;
            int var25 = (var26 = SyntheticaFileChooserUI.this.filePane.getContainerListeners()).length;

            for(int var24 = 0; var24 < var25; ++var24) {
               ContainerListener var23 = var26[var24];
               var23.componentAdded(var22);
            }

            return this.fileContextMenu;
         }
      }

      private Collator getFilenameCollator() {
         Locale var1 = SyntheticaFileChooserUI.this.getFileChooser().getLocale();
         return (Collator)(var1.getLanguage().equals(Locale.GERMAN.getLanguage()) ? GermanCollator.getInstance() : Collator.getInstance(var1));
      }

      private class DetailsTableCellRenderer extends DefaultTableCellRenderer {
         private DateFormat dateFormat;
         private Color sortColor = UIManager.getColor("Synthetica.fileChooser.tableView.sortColumnColor");
         private Color alternateBackground = UIManager.getColor("Table.alternateRowColor");
         private FileSystemView fsv = SyntheticaFileChooserUI.this.getFileChooser().getFileSystemView();

         DetailsTableCellRenderer(JFileChooser var2) {
            this.dateFormat = DateFormat.getDateTimeInstance(3, 3, var2.getLocale());
         }

         public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
            JComponent var7 = (JComponent)super.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
            if (!var3) {
               Color var8 = var5 % 2 == 0 && this.alternateBackground != null ? this.alternateBackground : var1.getBackground();
               if (var6 == var1.convertColumnIndexToView(FilePane.this.sortColumn) && this.sortColor != null) {
                  float var9 = (float)this.sortColor.getAlpha() / 255.0F;
                  int var10 = (int)((float)this.sortColor.getRed() * var9) + (int)((float)var8.getRed() * (1.0F - var9));
                  int var11 = (int)((float)this.sortColor.getGreen() * var9) + (int)((float)var8.getGreen() * (1.0F - var9));
                  int var12 = (int)((float)this.sortColor.getBlue() * var9) + (int)((float)var8.getBlue() * (1.0F - var9));
                  var7.setBackground(new Color(var10, var11, var12));
               } else {
                  var7.setBackground(var8);
               }
            }

            Object var13 = var7.getBorder() instanceof PaddingBorder ? ((PaddingBorder)this.getBorder()).getOutsideBorder() : var7.getBorder();
            if (((Border)var13).getBorderInsets(var7).left + ((Border)var13).getBorderInsets(var7).right <= 4) {
               var13 = FilePane.this.new PaddingBorder((Border)var13);
            }

            var7.setBorder((Border)var13);
            return var7;
         }

         public void setValue(Object var1) {
            this.setIcon((Icon)null);
            this.setHorizontalAlignment(2);
            if (var1 instanceof File) {
               File var2 = (File)var1;
               String var3 = SyntheticaFileChooserUI.this.getFileChooser().getName(var2);
               this.setText(var3);
               Icon var4 = null;
               if (SyntheticaFileChooserUI.this.useSystemFileIcons) {
                  var4 = this.fsv.getSystemIcon(var2);
               } else {
                  var4 = SyntheticaFileChooserUI.this.getFileChooser().getIcon(var2);
               }

               this.setIcon(var4);
               if (SyntheticaFileChooserUI.this.cutBuffer.contains(var1)) {
                  this.setIcon(SyntheticaFileChooserUI.this.createBrightIcon(var4));
               }
            } else if (var1 instanceof Date) {
               this.setText(var1 == null ? "" : this.dateFormat.format((Date)var1));
            } else if (var1 instanceof Long) {
               long var6 = (Long)var1;
               if (var6 == -1L) {
                  this.setText("");
                  return;
               }

               var6 /= 1024L;
               String var7 = "0";
               NumberFormat var5;
               if (var6 < 1024L) {
                  if (var6 == 0L) {
                     var6 = 1L;
                  }

                  var5 = NumberFormat.getInstance(SyntheticaFileChooserUI.this.getFileChooser().getLocale());
                  var7 = var5.format(var6) + " KB";
               } else {
                  var6 /= 1024L;
                  var5 = NumberFormat.getInstance(SyntheticaFileChooserUI.this.getFileChooser().getLocale());
                  var7 = var5.format(var6) + " MB";
               }

               this.setHorizontalAlignment(4);
               this.setText(var7);
            } else {
               super.setValue(var1);
            }

         }
      }

      private class DetailsTableModel extends AbstractTableModel {
         JFileChooser fileChooser;
         File newAddedEntry;
         ListModel listModel;

         DetailsTableModel(JFileChooser var2) {
            this.fileChooser = var2;
            this.listModel = SyntheticaFileChooserUI.this.getModel();
            this.listModel.addListDataListener(new ListDataListener() {
               public void contentsChanged(ListDataEvent var1) {
                  DetailsTableModel.this.fireTableDataChanged();
                  DetailsTableModel.this.doFileSelection();
               }

               public void intervalAdded(ListDataEvent var1) {
                  DetailsTableModel.this.newAddedEntry = (File)DetailsTableModel.this.listModel.getElementAt(var1.getIndex0());
                  final boolean var2 = FilePane.this.newCreatedFile;
                  DetailsTableModel.this.fireTableDataChanged();
                  if (!SyntheticaFileChooserUI.this.treePanelEnabled) {
                     EventQueue.invokeLater(new Runnable() {
                        public void run() {
                           DetailsTableModel.this.doFileSelection();
                           if (var2) {
                              int var1 = SyntheticaFileChooserUI.this.getModel().indexOf(DetailsTableModel.this.newAddedEntry);
                              SyntheticaFileChooserUI.this.filePane.editIndex = var1;
                              SyntheticaFileChooserUI.this.filePane.editFileName();
                           }

                        }
                     });
                  }

               }

               public void intervalRemoved(ListDataEvent var1) {
                  DetailsTableModel.this.fireTableDataChanged();
               }
            });
         }

         public int getRowCount() {
            return this.listModel.getSize();
         }

         public int getColumnCount() {
            return 5;
         }

         public String getColumnName(int var1) {
            return FilePane.this.columnNames[var1];
         }

         public Class getColumnClass(int var1) {
            switch (var1) {
               case 0:
                  return File.class;
               case 1:
                  return Long.class;
               case 2:
               default:
                  return super.getColumnClass(var1);
               case 3:
                  return Date.class;
            }
         }

         public Object getValueAt(int var1, int var2) {
            File var3 = (File)this.listModel.getElementAt(var1);
            switch (var2) {
               case 0:
                  return var3;
               case 1:
                  if (var3.exists() && !var3.isDirectory() && !this.fileChooser.getFileSystemView().isFileSystemRoot(var3)) {
                     return new Long(var3.length());
                  }

                  return new Long(-1L);
               case 2:
                  if (var3.exists() && !this.fileChooser.getFileSystemView().isFileSystemRoot(var3)) {
                     return this.fileChooser.getFileSystemView().getSystemTypeDescription(var3);
                  }

                  return null;
               case 3:
                  if (var3.exists() && !this.fileChooser.getFileSystemView().isFileSystemRoot(var3)) {
                     long var4 = var3.lastModified();
                     return var4 == 0L ? null : new Date(var4);
                  }

                  return null;
               case 4:
                  if (var3.exists() && !this.fileChooser.getFileSystemView().isFileSystemRoot(var3)) {
                     String var6 = "";

                     try {
                        if (!var3.canWrite()) {
                           var6 = var6 + "R";
                        }
                     } catch (AccessControlException var8) {
                     }

                     if (var3.isHidden()) {
                        var6 = var6 + "H";
                     }

                     return var6;
                  }

                  return null;
               default:
                  return null;
            }
         }

         public void setValueAt(Object var1, int var2, int var3) {
         }

         public boolean isCellEditable(int var1, int var2) {
            return var2 == 0 && FilePane.this.editFile != null;
         }

         public void doFileSelection() {
            File var1 = SyntheticaFileChooserUI.this.isDirectorySelected() ? SyntheticaFileChooserUI.this.getDirectory() : this.fileChooser.getSelectedFile();
            int var2 = ((BasicDirectoryModel)this.listModel).indexOf(var1);
            if (var1 != null && var2 >= 0) {
               FilePane.this.list.setSelectedValue(var1, true);
            } else {
               FilePane.this.list.clearSelection();
            }

         }
      }

      private class DetailsTableSortModel extends AbstractTableModel implements TableModelListener {
         TableModel model;
         Row[] rows;

         DetailsTableSortModel(TableModel var2) {
            this.model = var2;
            var2.addTableModelListener(this);
            this.reinit();
         }

         public void reinit() {
            this.rows = new Row[this.model.getRowCount()];

            for(int var1 = 0; var1 < this.rows.length; this.rows[var1].index = var1++) {
               this.rows[var1] = new Row((Row)null);
            }

            this.sort();
         }

         public void tableChanged(TableModelEvent var1) {
            this.reinit();
            File var2 = ((DetailsTableModel)this.model).newAddedEntry;
            if (var2 != null && FilePane.this.newCreatedFile) {
               for(int var3 = 0; var3 < this.getRowCount(); ++var3) {
                  if (var2.equals(this.getValueAt(var3, 0))) {
                     SyntheticaFileChooserUI.this.filePane.editIndex = var3;
                     SyntheticaFileChooserUI.this.filePane.editFileName();
                     break;
                  }
               }

               ((DetailsTableModel)this.model).newAddedEntry = null;
               FilePane.this.newCreatedFile = false;
            }

         }

         public void sort() {
            Cursor var1 = FilePane.this.table.getTableHeader().getCursor();
            FilePane.this.table.getTableHeader().setCursor((Cursor)null);
            SyntheticaFileChooserUI.this.getFileChooser().setCursor(Cursor.getPredefinedCursor(3));
            Arrays.sort(this.rows);
            this.fireTableDataChanged();
            SyntheticaFileChooserUI.this.getFileChooser().setCursor(Cursor.getPredefinedCursor(0));
            FilePane.this.table.getTableHeader().setCursor(var1);
         }

         public int getRowCount() {
            return this.model.getRowCount();
         }

         public int getColumnCount() {
            return this.model.getColumnCount();
         }

         public String getColumnName(int var1) {
            return this.model.getColumnName(var1);
         }

         public Class getColumnClass(int var1) {
            return this.model.getColumnClass(var1);
         }

         public Object getValueAt(int var1, int var2) {
            return this.rows.length == 0 ? null : this.model.getValueAt(this.rows[var1].index, var2);
         }

         public void setValueAt(Object var1, int var2, int var3) {
            this.model.setValueAt(var1, var2, var3);
         }

         public boolean isCellEditable(int var1, int var2) {
            return this.model.isCellEditable(var1, var2);
         }

         private class Row implements Comparable {
            public int index;

            private Row() {
            }

            public int compareTo(Object var1) {
               if (!SyntheticaFileChooserUI.this.sortEnabled) {
                  return 0;
               } else {
                  File var2 = (File)DetailsTableSortModel.this.model.getValueAt(this.index, 0);
                  File var3 = (File)DetailsTableSortModel.this.model.getValueAt(((Row)var1).index, 0);
                  if (var2 != null && var3 != null) {
                     FileSystemView var4 = SyntheticaFileChooserUI.this.getFileChooser().getFileSystemView();
                     if (!var4.isFileSystemRoot(var2) && !var4.isFileSystemRoot(var3)) {
                        if (var2.isDirectory() && var3.isFile()) {
                           return -1 * FilePane.this.sortOrder;
                        } else if (var2.isFile() && var3.isDirectory()) {
                           return 1 * FilePane.this.sortOrder;
                        } else if (FilePane.this.sortColumn == 0) {
                           Collator var11 = FilePane.this.getFilenameCollator();
                           int var12 = var11.compare(var2.getName(), var3.getName());
                           return var12 * FilePane.this.sortOrder;
                        } else {
                           long var10;
                           if (FilePane.this.sortColumn == 3) {
                              var10 = var2.lastModified() - var3.lastModified();
                              return (var10 < 0L ? 1 : (var10 > 0L ? -1 : 0)) * -FilePane.this.sortOrder;
                           } else if (FilePane.this.sortColumn == 1) {
                              var10 = var2.length() - var3.length();
                              return (var10 < 0L ? 1 : (var10 > 0L ? -1 : 0)) * -FilePane.this.sortOrder;
                           } else {
                              String var5;
                              String var6;
                              if (FilePane.this.sortColumn == 2) {
                                 var5 = var4.getSystemTypeDescription(var2);
                                 var6 = var4.getSystemTypeDescription(var3);
                                 return var5.compareTo(var6) * FilePane.this.sortOrder;
                              } else if (FilePane.this.sortColumn == 4) {
                                 var5 = "";

                                 try {
                                    if (!var2.canWrite()) {
                                       var5 = var5 + "R";
                                    }
                                 } catch (AccessControlException var9) {
                                 }

                                 if (var2.isHidden()) {
                                    var5 = var5 + "H";
                                 }

                                 var6 = "";

                                 try {
                                    if (!var3.canWrite()) {
                                       var6 = var6 + "R";
                                    }
                                 } catch (AccessControlException var8) {
                                 }

                                 if (var3.isHidden()) {
                                    var6 = var6 + "H";
                                 }

                                 return var5.compareTo(var6) * FilePane.this.sortOrder;
                              } else {
                                 return var2.toString().compareTo(var3.toString()) * FilePane.this.sortOrder;
                              }
                           }
                        }
                     } else {
                        return var2.toString().compareTo(var3.toString()) * FilePane.this.sortOrder;
                     }
                  } else {
                     return 0;
                  }
               }
            }

            // $FF: synthetic method
            Row(Row var2) {
               this();
            }
         }
      }

      private class FileCellEditor extends DefaultCellEditor {
         public FileCellEditor(JTextField var2) {
            super(var2);
         }

         public Object getCellEditorValue() {
            return ((FileNode)FilePane.this.tree.getSelectionPath().getLastPathComponent()).getFile();
         }

         public String getDelegateValue() {
            return (String)this.delegate.getCellEditorValue();
         }
      }

      private class FileNode extends DefaultMutableTreeNode {
         boolean refresh = false;
         boolean updateAllowed = true;

         FileNode(File var2) {
            super(var2);
         }

         public File getFile() {
            return (File)this.userObject;
         }

         public int getChildCount() {
            this.updateChildren();
            return super.getChildCount();
         }

         public Enumeration children() {
            this.updateChildren();
            return super.children();
         }

         public boolean isLeaf() {
            return !((File)this.userObject).isDirectory();
         }

         private void prepareForUpdateChildren() {
            this.refresh = true;
         }

         private void updateChildren() {
            if (this.refresh) {
               this.refresh = false;
               this.removeAllChildren();
               this.children = null;
            }

            if (this.children == null && this.updateAllowed) {
               final FileSystemView var1 = SyntheticaFileChooserUI.this.getFileChooser().getFileSystemView();
               File[] var2 = var1.getFiles(this.getFile(), true);
               Arrays.sort(var2, new Comparator() {
                  public int compare(Object var1x, Object var2) {
                     File var3 = (File)var1x;
                     File var4 = (File)var2;
                     if (var3 != null && var4 != null) {
                        if (!var1.isFileSystemRoot(var3) && !var1.isFileSystemRoot(var4)) {
                           if (var3.isDirectory() && var4.isFile()) {
                              return -1 * FilePane.this.sortOrder;
                           } else if (var3.isFile() && var4.isDirectory()) {
                              return 1 * FilePane.this.sortOrder;
                           } else if (FilePane.this.sortColumn == 0) {
                              Collator var7 = FilePane.this.getFilenameCollator();
                              int var6 = var7.compare(var3.getName(), var4.getName());
                              return var6 * FilePane.this.sortOrder;
                           } else {
                              long var5;
                              if (FilePane.this.sortColumn == 3) {
                                 var5 = var3.lastModified() - var4.lastModified();
                                 return (var5 < 0L ? 1 : (var5 > 0L ? -1 : 0)) * FilePane.this.sortOrder;
                              } else if (FilePane.this.sortColumn == 1) {
                                 var5 = var3.length() - var4.length();
                                 return (var5 < 0L ? 1 : (var5 > 0L ? -1 : 0)) * FilePane.this.sortOrder;
                              } else {
                                 return var3.toString().compareTo(var4.toString()) * FilePane.this.sortOrder;
                              }
                           }
                        } else {
                           return var3.toString().compareTo(var4.toString()) * FilePane.this.sortOrder;
                        }
                     } else {
                        return 0;
                     }
                  }
               });
               boolean var3 = SyntheticaFileChooserUI.this.getFileChooser().getFileSelectionMode() == 1;
               File[] var7 = var2;
               int var6 = var2.length;

               for(int var5 = 0; var5 < var6; ++var5) {
                  File var4 = var7[var5];
                  if (!var3 || var3 && var4.isDirectory()) {
                     this.insert(FilePane.this.new FileNode(var4), this.children == null ? 0 : this.children.size());
                  }
               }
            }

         }

         public String toString() {
            return SyntheticaFileChooserUI.this.getFileChooser().getFileSystemView().getSystemDisplayName(this.getFile());
         }
      }

      private class FilePaneMouseListener implements MouseListener {
         private MouseListener doubleClickListener;
         private boolean singleClick;
         private final int EDIT_DELAY = 250;

         public FilePaneMouseListener(JList var2) {
            this.doubleClickListener = SyntheticaFileChooserUI.this.createDoubleClickListener(SyntheticaFileChooserUI.this.getFileChooser(), var2);
         }

         public FilePaneMouseListener(JTable var2) {
            this.doubleClickListener = SyntheticaFileChooserUI.this.createDoubleClickListener(SyntheticaFileChooserUI.this.getFileChooser(), FilePane.this.list);
         }

         public void mouseClicked(MouseEvent var1) {
            JComponent var2 = (JComponent)var1.getSource();
            int var3;
            if (var2 instanceof JList) {
               var3 = this.loc2IndexFileList((JList)var2, var1.getPoint());
            } else {
               if (!(var2 instanceof JTable)) {
                  if (!(var2 instanceof JTableHeader)) {
                     return;
                  }

                  int var5 = ((JTableHeader)var2).columnAtPoint(var1.getPoint());
                  var5 = FilePane.this.table.convertColumnIndexToModel(var5);
                  if (var5 == FilePane.this.sortColumn) {
                     if (FilePane.this.sortOrder == 1) {
                        FilePane.this.sortOrder = -1;
                     } else {
                        FilePane.this.sortOrder = 1;
                     }
                  } else {
                     FilePane.this.sortOrder = var5 != 1 && var5 != 3 ? 1 : -1;
                     FilePane.this.sortColumn = var5;
                  }

                  FilePane.this.detailsTableSortModel.sort();
                  FilePane.this.viewPanel.repaint();
                  return;
               }

               var3 = ((JTable)var2).rowAtPoint(var1.getPoint());
            }

            if (var3 >= 0 && FilePane.this.list.getSelectionModel().isSelectedIndex(var3) && var2 instanceof JTable) {
               Rectangle var4 = FilePane.this.list.getCellBounds(var3, var3);
               var1 = new MouseEvent(FilePane.this.list, var1.getID(), var1.getWhen(), var1.getModifiers(), var4.x, var4.y, var1.getClickCount(), var1.isPopupTrigger(), var1.getButton());
            }

            if (var3 >= 0 && SwingUtilities.isLeftMouseButton(var1)) {
               if (var1.getClickCount() == 1) {
                  this.singleClick = true;
                  if (FilePane.this.editIndex == var3 && FilePane.this.editFile == null) {
                     (new Thread() {
                        public void run() {
                           try {
                              sleep(250L);
                           } catch (InterruptedException var2) {
                           }

                           if (FilePaneMouseListener.this.singleClick) {
                              SwingUtilities.invokeLater(new Runnable() {
                                 public void run() {
                                    FilePane.this.editFileName();
                                 }
                              });
                           }

                        }
                     }).start();
                  } else if (FilePane.this.editFile == null) {
                     FilePane.this.editIndex = var3;
                  }
               } else if (var1.getClickCount() != 1) {
                  this.singleClick = false;
                  FilePane.this.cancelEditFileName();
               }
            }

            if (var3 >= 0) {
               this.doubleClickListener.mouseClicked(var1);
            }

         }

         public void mouseEntered(MouseEvent var1) {
            this.doubleClickListener.mouseEntered(var1);
         }

         public void mouseExited(MouseEvent var1) {
            if (var1.getSource() instanceof JList) {
               this.doubleClickListener.mouseExited(var1);
            }

         }

         public void mousePressed(MouseEvent var1) {
            if (var1.getSource() instanceof JList) {
               this.doubleClickListener.mousePressed(var1);
            }

         }

         public void mouseReleased(MouseEvent var1) {
            if (var1.getSource() instanceof JList) {
               this.doubleClickListener.mouseReleased(var1);
            }

         }

         private int loc2IndexFileList(JList var1, Point var2) {
            int var3 = var1.locationToIndex(var2);
            if (var3 != -1 && !this.pointIsInActualBounds(var1, var3, var2)) {
               var3 = -1;
            }

            return var3;
         }

         private boolean pointIsInActualBounds(JList var1, int var2, Point var3) {
            ListCellRenderer var4 = var1.getCellRenderer();
            ListModel var5 = var1.getModel();
            Object var6 = var5.getElementAt(var2);
            Component var7 = var4.getListCellRendererComponent(var1, var6, var2, false, false);
            Dimension var8 = var7.getPreferredSize();
            Rectangle var9 = var1.getCellBounds(var2, var2);
            if (!var7.getComponentOrientation().isLeftToRight()) {
               var9.x += var9.width - var8.width;
            }

            var9.width = var8.width;
            var9.height = var8.height;
            return var9.contains(var3);
         }
      }

      private class IconBorder extends CompoundBorder {
         public IconBorder(Border var2, final Icon var3) {
            super(var2, new Border() {
               public void paintBorder(Component var1, Graphics var2, int var3x, int var4, int var5, int var6) {
                  var3.paintIcon(var1, var2, var3x + var5 - var3.getIconWidth(), var4 + (var6 - var3.getIconHeight()) / 2);
               }

               public Insets getBorderInsets(Component var1) {
                  return new Insets(0, 0, 0, var3.getIconWidth());
               }

               public boolean isBorderOpaque() {
                  return false;
               }
            });
         }
      }

      private class ListRenderer implements ListCellRenderer {
         private FileSystemView fsv = SyntheticaFileChooserUI.this.getFileChooser().getFileSystemView();
         ListCellRenderer delegate;

         public ListRenderer(ListCellRenderer var2) {
            this.delegate = var2;
         }

         public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
            JLabel var6 = (JLabel)this.delegate.getListCellRendererComponent(var1, var2, var3, var4, var5);
            File var7 = (File)var2;
            String var8 = SyntheticaFileChooserUI.this.getFileChooser().getName(var7);
            var6.setText(var8);
            Icon var9 = null;
            if (SyntheticaFileChooserUI.this.useSystemFileIcons) {
               var9 = this.fsv.getSystemIcon(var7);
            } else {
               var9 = SyntheticaFileChooserUI.this.getFileChooser().getIcon(var7);
            }

            var6.setIcon(var9);
            if (SyntheticaFileChooserUI.this.cutBuffer.contains(var7)) {
               var6.setIcon(SyntheticaFileChooserUI.this.createBrightIcon(var9));
            }

            return var6;
         }
      }

      private class PaddingBorder extends CompoundBorder {
         public PaddingBorder(Border var2) {
            super(var2, new Border() {
               public void paintBorder(Component var1, Graphics var2, int var3, int var4, int var5, int var6) {
               }

               public Insets getBorderInsets(Component var1) {
                  return new Insets(1, 4, 1, 4);
               }

               public boolean isBorderOpaque() {
                  return false;
               }
            });
         }
      }

      public class TableHeaderRenderer implements TableCellRenderer {
         private TableCellRenderer renderer;

         public TableHeaderRenderer(JTable var2) {
            this.renderer = var2.getTableHeader().getDefaultRenderer();
         }

         public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
            JLabel var7 = (JLabel)this.renderer.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
            Object var8 = var7.getBorder() instanceof IconBorder ? ((IconBorder)FilePane.this.getBorder()).getOutsideBorder() : var7.getBorder();
            if (((Border)var8).getBorderInsets(var7).left + ((Border)var8).getBorderInsets(var7).right <= 4) {
               var8 = new EmptyBorder(2, 4, 2, 4);
            }

            if (SyntheticaFileChooserUI.this.sortEnabled && var6 == var1.convertColumnIndexToView(FilePane.this.sortColumn)) {
               Icon var9 = SyntheticaLookAndFeel.loadIcon(FilePane.this.sortOrder == 1 ? "Synthetica.fileChooser.tableView.arrowUp" : "Synthetica.fileChooser.tableView.arrowDown");
               var7.setBorder(FilePane.this.new IconBorder((Border)var8, var9));
            } else {
               var7.setBorder((Border)var8);
            }

            return var7;
         }
      }

      private class TreeRenderer extends DefaultTreeCellRenderer {
         private FileSystemView fsv;

         private TreeRenderer() {
            this.fsv = SyntheticaFileChooserUI.this.getFileChooser().getFileSystemView();
         }

         public Component getTreeCellRendererComponent(JTree var1, Object var2, boolean var3, boolean var4, boolean var5, int var6, boolean var7) {
            super.getTreeCellRendererComponent(var1, var2, var3, var4, var5, var6, var7);
            File var8 = ((FileNode)var2).getFile();
            this.setText(this.fsv.getSystemDisplayName(var8));
            this.setIcon(this.getIcon(var8));
            return this;
         }

         public Icon getLeafIcon() {
            return FilePane.this.tree.getSelectionPath() == null ? super.getLeafIcon() : this.getIcon(((FileNode)FilePane.this.tree.getSelectionPath().getLastPathComponent()).getFile());
         }

         public Icon getOpenIcon() {
            return FilePane.this.tree.getSelectionPath() == null ? super.getOpenIcon() : this.getIcon(((FileNode)FilePane.this.tree.getSelectionPath().getLastPathComponent()).getFile());
         }

         public Icon getClosedIcon() {
            return FilePane.this.tree.getSelectionPath() == null ? super.getClosedIcon() : this.getIcon(((FileNode)FilePane.this.tree.getSelectionPath().getLastPathComponent()).getFile());
         }

         private Icon getIcon(File var1) {
            Object var2 = null;
            if (SyntheticaFileChooserUI.this.useSystemFileIcons) {
               var2 = this.fsv.getSystemIcon(var1);
            } else {
               var2 = SyntheticaFileChooserUI.this.getFileChooser().getIcon(var1);
            }

            if (SyntheticaFileChooserUI.this.cutBuffer.contains(var1)) {
               var2 = SyntheticaFileChooserUI.this.createBrightIcon((Icon)var2);
            }

            return (Icon)var2;
         }

         // $FF: synthetic method
         TreeRenderer(TreeRenderer var2) {
            this();
         }
      }
   }

   protected class FileSelectHandler implements KeyListener {
      private String prefix;
      private long lastTime;

      public void keyTyped(KeyEvent var1) {
         if (!var1.isAltDown() && !var1.isControlDown() && !var1.isMetaDown()) {
            char var2 = var1.getKeyChar();
            JComponent var3 = (JComponent)var1.getSource();
            int var4 = -1;
            if (var3 instanceof JList) {
               var4 = ((JList)var3).getLeadSelectionIndex();
            } else if (var3 instanceof JTable) {
               var4 = ((JTable)var3).getSelectedRow();
            }

            long var5 = var1.getWhen();
            if (var5 - this.lastTime > 1000L) {
               ++var4;
               this.prefix = String.valueOf(var2);
            } else if (this.prefix.length() == 1 && var2 == this.prefix.charAt(0)) {
               ++var4;
            } else {
               this.prefix = this.prefix + var2;
            }

            this.lastTime = var5;
            int var7 = this.getNextFileIndex(var3, this.prefix, var4, Bias.Forward);
            if (var7 < 0) {
               var7 = this.getNextFileIndex(var3, this.prefix, 0, Bias.Forward);
            }

            if (var7 >= 0) {
               if (var3 instanceof JList) {
                  ((JList)var3).setSelectedIndex(var7);
                  ((JList)var3).ensureIndexIsVisible(var7);
               } else if (var3 instanceof JTable) {
                  ((JTable)var3).setRowSelectionInterval(var7, var7);
                  ((JTable)var3).scrollRectToVisible(((JTable)var3).getCellRect(var7, 0, true));
               }
            }

         }
      }

      public void keyPressed(KeyEvent var1) {
      }

      public void keyReleased(KeyEvent var1) {
      }

      private int getNextFileIndex(JComponent var1, String var2, int var3, Position.Bias var4) {
         int var5 = -1;
         if (var1 instanceof JList) {
            var5 = ((JList)var1).getModel().getSize();
         } else if (var1 instanceof JTable) {
            var5 = ((JTable)var1).getModel().getRowCount();
         }

         if (var3 >= 0 && var3 < var5) {
            boolean var6 = var4 == Bias.Backward;
            int var7 = var3;

            while(true) {
               if (var6) {
                  if (var7 < 0) {
                     break;
                  }
               } else if (var7 >= var5) {
                  break;
               }

               String var8 = null;
               if (var1 instanceof JList) {
                  ListModel var9 = ((JList)var1).getModel();
                  var8 = SyntheticaFileChooserUI.this.getFileChooser().getName((File)var9.getElementAt(var7));
               } else if (var1 instanceof JTable) {
                  TableModel var10 = ((JTable)var1).getModel();
                  var8 = SyntheticaFileChooserUI.this.getFileChooser().getName((File)var10.getValueAt(var7, 0));
               }

               if (var8.regionMatches(true, 0, var2, 0, var2.length())) {
                  return var7;
               }

               var7 += var6 ? -1 : 1;
            }

            return -1;
         } else {
            return -1;
         }
      }
   }

   protected class FilterComboBoxModel extends AbstractListModel implements ComboBoxModel, PropertyChangeListener {
      protected FileFilter[] filters = SyntheticaFileChooserUI.this.getFileChooser().getChoosableFileFilters();

      public void propertyChange(PropertyChangeEvent var1) {
         String var2 = var1.getPropertyName();
         if (var2.equals("ChoosableFileFilterChangedProperty")) {
            this.filters = (FileFilter[])var1.getNewValue();
            this.fireContentsChanged(this, -1, -1);
         } else if (var2.equals("fileFilterChanged")) {
            this.fireContentsChanged(this, -1, -1);
         }

      }

      public void setSelectedItem(Object var1) {
         if (var1 != null) {
            JFileChooser var2 = SyntheticaFileChooserUI.this.getFileChooser();
            var2.setFileFilter((FileFilter)var1);
            if (var2.getDialogType() != 1) {
               SyntheticaFileChooserUI.this.setFileName((String)null);
            }

            this.fireContentsChanged(this, -1, -1);
         }

      }

      public Object getSelectedItem() {
         JFileChooser var1 = SyntheticaFileChooserUI.this.getFileChooser();
         FileFilter var2 = var1.getFileFilter();
         if (var2 != null && !Arrays.asList(this.filters).contains(var2)) {
            var1.addChoosableFileFilter(var2);
         }

         return var1.getFileFilter();
      }

      public int getSize() {
         return this.filters.length;
      }

      public Object getElementAt(int var1) {
         return this.filters.length > 0 ? this.filters[var1] : null;
      }
   }

   private class IndentIcon implements Icon {
      private Icon icon;
      private int depth;
      private int indent;

      private IndentIcon() {
         this.icon = null;
         this.depth = 0;
         this.indent = 10;
      }

      public void paintIcon(Component var1, Graphics var2, int var3, int var4) {
         if (var1.getComponentOrientation().isLeftToRight()) {
            this.icon.paintIcon(var1, var2, var3 + this.depth * this.indent, var4);
         } else {
            this.icon.paintIcon(var1, var2, var3, var4);
         }

      }

      public int getIconWidth() {
         return this.icon.getIconWidth() + this.depth * this.indent;
      }

      public int getIconHeight() {
         return this.icon.getIconHeight();
      }

      // $FF: synthetic method
      IndentIcon(IndentIcon var2) {
         this();
      }
   }

   private static class TableHeaderCorner extends JTableHeader {
      public TableHeaderCorner(JTable var1) {
         this.setTable(var1);
      }
   }
}
