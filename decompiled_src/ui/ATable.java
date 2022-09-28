package ui;

import common.CommonUtils;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.EventObject;
import java.util.HashSet;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.text.JTextComponent;

public class ATable extends JTable {
   public static final String indicator = " ▪";
   protected boolean alternateBackground = false;
   protected TableClickListener clickl = new TableClickListener();
   protected int[] selected = null;
   public static final Color BACK_NEUTRAL = new Color(255, 235, 156);
   public static final Color BACK_GOOD = new Color(198, 239, 206);
   public static final Color BACK_BAD = new Color(255, 199, 206);
   public static final Color BACK_IGNORE = new Color(165, 165, 165);
   public static final Color BACK_CANCEL = new Color(61, 87, 158);
   public static final Color FORE_NEUTRAL = new Color(181, 107, 6);
   public static final Color FORE_GOOD = new Color(47, 75, 47);
   public static final Color FORE_BAD = new Color(173, 32, 40);
   public static final Color FORE_IGNORE;
   public static final Color FORE_CANCEL;

   public void markSelections() {
      this.selected = this.getSelectedRows();
   }

   public void setPopupMenu(TablePopup var1) {
      this.clickl.setPopup(var1);
   }

   public void fixSelection() {
      if (this.selected.length != 0) {
         this.getSelectionModel().setValueIsAdjusting(true);
         int var1 = this.getModel().getRowCount();

         for(int var2 = 0; var2 < this.selected.length; ++var2) {
            if (this.selected[var2] < var1) {
               this.getSelectionModel().addSelectionInterval(this.selected[var2], this.selected[var2]);
            }
         }

         this.getSelectionModel().setValueIsAdjusting(false);
      }
   }

   public void restoreSelections() {
      CommonUtils.runSafe(new Runnable() {
         public void run() {
            ATable.this.fixSelection();
         }
      });
   }

   public static TableCellRenderer getDefaultTableRenderer(JTable var0, final TableModel var1) {
      final HashSet var2 = new HashSet();
      var2.add("Wordlist");
      var2.add("PAYLOAD");
      var2.add("RHOST");
      var2.add("RHOSTS");
      var2.add("Template");
      var2.add("DICTIONARY");
      var2.add("NAMELIST");
      var2.add("SigningKey");
      var2.add("SigningCert");
      var2.add("WORDLIST");
      var2.add("SESSION");
      var2.add("REXE");
      var2.add("EXE::Custom");
      var2.add("EXE::Template");
      var2.add("USERNAME");
      var2.add("PASSWORD");
      var2.add("SMBUser");
      var2.add("SMBPass");
      var2.add("INTERFACE");
      var2.add("URL");
      var2.add("PATH");
      var2.add("SCRIPT");
      var2.add("KEY_PATH");
      return new TableCellRenderer() {
         public Component getTableCellRendererComponent(JTable var1x, Object var2x, boolean var3, boolean var4, int var5, int var6) {
            TableCellRenderer var7 = var1x.getDefaultRenderer(String.class);
            String var8 = (var2x != null ? var2x : "") + "";
            if (var2.contains(var8) || var8.indexOf("FILE") != -1) {
               var8 = var8 + " ▪";
            }

            JComponent var9 = (JComponent)var7.getTableCellRendererComponent(var1x, var8, var3, false, var5, var6);
            var9.setToolTipText(((GenericTableModel)var1).getValueAtColumn(var1x, var5, "Tooltip") + "");
            return var9;
         }
      };
   }

   public static TableCellRenderer getFileTypeTableRenderer(final GenericTableModel var0) {
      return new TableCellRenderer() {
         public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
            TableCellRenderer var7 = var1.getDefaultRenderer(String.class);
            JComponent var8 = (JComponent)var7.getTableCellRendererComponent(var1, "", var3, false, var5, var6);
            ((JLabel)var8).setEnabled(true);
            if ("dir".equals(var2)) {
               ((JLabel)var8).setIcon(UIManager.getIcon("FileView.directoryIcon"));
               if (var0.getValueAt(var1, var5, "cache") == Boolean.FALSE) {
                  ((JLabel)var8).setEnabled(false);
               }
            } else if ("drive".equals(var2)) {
               ((JLabel)var8).setIcon(UIManager.getIcon("FileView.hardDriveIcon"));
            } else {
               ((JLabel)var8).setIcon(UIManager.getIcon("FileView.fileIcon"));
            }

            return var8;
         }
      };
   }

   public static TableCellRenderer getListenerStatusRenderer(final GenericTableModel var0) {
      return new TableCellRenderer() {
         public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
            TableCellRenderer var7 = var1.getDefaultRenderer(String.class);
            JLabel var8 = (JLabel)var7.getTableCellRendererComponent(var1, var2, var3, false, var5, var6);
            Object var9 = var0.getValueAt(var1, var5, "status");
            if (var9 != null && !"".equals(var9) && !"success".equals(var9)) {
               var8.setText("<html><body><font color=\"#8b0000\"><strong>ERROR!</strong></font> " + var8.getText() + " <font color=\"#8b0000\">" + var9 + "</font></body></html>");
            }

            return var8;
         }
      };
   }

   public static TableCellRenderer getBoldOnKeyRenderer(final GenericTableModel var0, final String var1) {
      return new TableCellRenderer() {
         public Component getTableCellRendererComponent(JTable var1x, Object var2, boolean var3, boolean var4, int var5, int var6) {
            TableCellRenderer var7 = var1x.getDefaultRenderer(String.class);
            JLabel var8 = (JLabel)var7.getTableCellRendererComponent(var1x, var2, var3, false, var5, var6);
            if (var0.getValueAt(var1x, var5, var1) == Boolean.TRUE) {
               var8.setFont(var8.getFont().deriveFont(1));
            } else {
               var8.setFont(var8.getFont().deriveFont(0));
            }

            return var8;
         }
      };
   }

   public static TableCellRenderer getSimpleTableRenderer() {
      return new TableCellRenderer() {
         public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
            TableCellRenderer var7 = var1.getDefaultRenderer(String.class);
            JComponent var8 = (JComponent)var7.getTableCellRendererComponent(var1, var2, var3, false, var5, var6);
            ((JLabel)var8).setIcon((Icon)null);
            return var8;
         }
      };
   }

   public static TableCellRenderer getSizeTableRenderer() {
      return new TableCellRenderer() {
         public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
            TableCellRenderer var7 = var1.getDefaultRenderer(String.class);
            JComponent var8 = (JComponent)var7.getTableCellRendererComponent(var1, "", var3, false, var5, var6);

            try {
               long var9 = Long.parseLong(var2 + "");
               String var11 = "b";
               if (var9 > 1024L) {
                  var9 /= 1024L;
                  var11 = "kb";
               }

               if (var9 > 1024L) {
                  var9 /= 1024L;
                  var11 = "mb";
               }

               if (var9 > 1024L) {
                  var9 /= 1024L;
                  var11 = "gb";
               }

               ((JLabel)var8).setText(var9 + var11);
            } catch (Exception var12) {
            }

            return var8;
         }
      };
   }

   public static TableCellRenderer getTimeTableRenderer() {
      return new TableCellRenderer() {
         public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
            TableCellRenderer var7 = var1.getDefaultRenderer(String.class);
            JComponent var8 = (JComponent)var7.getTableCellRendererComponent(var1, "", var3, false, var5, var6);

            try {
               long var9 = Long.parseLong(var2 + "");
               String var11 = "ms";
               if (var9 <= 1000L) {
                  ((JLabel)var8).setText(var9 + var11);
                  return var8;
               }

               var9 /= 1000L;
               var11 = "s";
               if (var9 > 60L) {
                  var9 /= 60L;
                  var11 = "m";
               }

               if (var9 > 60L) {
                  var9 /= 60L;
                  var11 = "h";
               }

               ((JLabel)var8).setText(var9 + var11);
            } catch (Exception var12) {
            }

            return var8;
         }
      };
   }

   public static TableCellRenderer getImageTableRenderer(final GenericTableModel var0, final String var1) {
      return new TableCellRenderer() {
         public Component getTableCellRendererComponent(JTable var1x, Object var2, boolean var3, boolean var4, int var5, int var6) {
            JLabel var7 = (JLabel)var1x.getDefaultRenderer(Object.class).getTableCellRendererComponent(var1x, var2, var3, false, var5, var6);
            ImageIcon var8 = (ImageIcon)var0.getValueAt(var1x, var5, var1);
            if (var8 != null) {
               var7.setIcon(var8);
               var7.setText("");
            } else {
               var7.setIcon((Icon)null);
               var7.setText("");
            }

            return var7;
         }
      };
   }

   public static TableCellRenderer getDateTableRenderer() {
      return new TableCellRenderer() {
         public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
            TableCellRenderer var7 = var1.getDefaultRenderer(String.class);
            JComponent var8 = (JComponent)var7.getTableCellRendererComponent(var1, "", var3, false, var5, var6);

            try {
               long var9 = Long.parseLong(var2 + "");
               ((JLabel)var8).setText(CommonUtils.formatDate(var9));
            } catch (Exception var11) {
            }

            return var8;
         }
      };
   }

   public void adjust() {
      this.setOpaque(true);
      this.addMouseListener(this.clickl);
      this.setShowGrid(false);
      this.setIntercellSpacing(new Dimension(0, 0));
      this.setRowHeight(this.getRowHeight() + 2);
      final TableCellEditor var1 = this.getDefaultEditor(Object.class);
      this.setDefaultEditor(Object.class, new TableCellEditor() {
         public Component getTableCellEditorComponent(JTable var1x, Object var2, boolean var3, int var4, int var5) {
            Component var6 = var1.getTableCellEditorComponent(var1x, var2, var3, var4, var5);
            if (var6 instanceof JTextComponent) {
               new CutCopyPastePopup((JTextComponent)var6);
            }

            return var6;
         }

         public void addCellEditorListener(CellEditorListener var1x) {
            var1.addCellEditorListener(var1x);
         }

         public void cancelCellEditing() {
            var1.cancelCellEditing();
         }

         public Object getCellEditorValue() {
            return var1.getCellEditorValue();
         }

         public boolean isCellEditable(EventObject var1x) {
            return var1.isCellEditable(var1x);
         }

         public void removeCellEditorListener(CellEditorListener var1x) {
            var1.removeCellEditorListener(var1x);
         }

         public boolean shouldSelectCell(EventObject var1x) {
            return var1.shouldSelectCell(var1x);
         }

         public boolean stopCellEditing() {
            return var1.stopCellEditing();
         }
      });
      final TableCellRenderer var2 = this.getDefaultRenderer(Object.class);
      this.setDefaultRenderer(Object.class, new TableCellRenderer() {
         public Component getTableCellRendererComponent(JTable var1, Object var2x, boolean var3, boolean var4, int var5, int var6) {
            if (var2x == null) {
               var2x = "";
            }

            Component var7 = var2.getTableCellRendererComponent(var1, var2x, var3, false, var5, var6);
            Object var8 = ((GenericTableModel)var1.getModel()).getValueAtColumn(var1, var5, "_accent");
            if (!var3) {
               if ("neutral".equals(var8)) {
                  var7.setForeground(ATable.FORE_NEUTRAL);
                  var7.setBackground(ATable.BACK_NEUTRAL);
               } else if ("bad".equals(var8)) {
                  var7.setForeground(ATable.FORE_BAD);
                  var7.setBackground(ATable.BACK_BAD);
               } else if ("good".equals(var8)) {
                  var7.setForeground(ATable.FORE_GOOD);
                  var7.setBackground(ATable.BACK_GOOD);
               } else if ("ignore".equals(var8)) {
                  var7.setForeground(ATable.FORE_IGNORE);
                  var7.setBackground(ATable.BACK_IGNORE);
               } else if ("cancel".equals(var8)) {
                  var7.setForeground(ATable.FORE_CANCEL);
                  var7.setBackground(ATable.BACK_CANCEL);
               } else {
                  var7.setForeground(Color.BLACK);
                  var7.setBackground(ATable.this.alternateBackground ? new Color(16250873) : Color.WHITE);
               }
            } else if ("neutral".equals(var8)) {
               var7.setForeground(ATable.BACK_NEUTRAL);
            } else if ("bad".equals(var8)) {
               var7.setForeground(ATable.BACK_BAD);
            } else if ("good".equals(var8)) {
               var7.setForeground(ATable.BACK_GOOD);
            } else if ("ignore".equals(var8)) {
               var7.setForeground(ATable.BACK_IGNORE);
            } else if ("cancel".equals(var8)) {
               var7.setForeground(ATable.BACK_CANCEL);
            }

            return var7;
         }
      });
   }

   public ATable() {
      this.adjust();
   }

   public ATable(TableModel var1) {
      super(var1);
      this.adjust();
   }

   public Component prepareRenderer(TableCellRenderer var1, int var2, int var3) {
      this.alternateBackground = var2 % 2 == 0;
      return super.prepareRenderer(var1, var2, var3);
   }

   public Color getComponentBackground() {
      return this.alternateBackground ? new Color(16250873) : Color.WHITE;
   }

   public void addActionForKeyStroke(KeyStroke var1, Action var2) {
      this.getActionMap().put(var1.toString(), var2);
      this.getInputMap().put(var1, var1.toString());
   }

   public void addActionForKey(String var1, Action var2) {
      this.addActionForKeyStroke(KeyStroke.getKeyStroke(var1), var2);
   }

   public BufferedImage getScreenshot() {
      BufferedImage var1 = new BufferedImage(this.getWidth(), this.getHeight(), 6);
      Graphics var2 = var1.getGraphics();
      this.paint(var2);
      var2.dispose();
      return var1;
   }

   static {
      FORE_IGNORE = Color.WHITE;
      FORE_CANCEL = Color.WHITE;
   }
}
