package dialog;

import common.CommonUtils;
import java.awt.Color;
import java.awt.Component;
import java.io.File;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class SafeDialogs {
   protected static File lastSaveDirectory = null;
   protected static File lastOpenDirectory = null;

   public static void askYesNo(final String var0, final String var1, final SafeDialogCallback var2) {
      CommonUtils.runSafe(new Runnable() {
         public void run() {
            int var1x = JOptionPane.showConfirmDialog((Component)null, var0, var1, 0);
            if (var1x == 0 || var1x == 0) {
               SafeDialogs.post(var2, "yes");
            }

         }
      });
   }

   private static void post(final SafeDialogCallback var0, final String var1) {
      (new Thread(new Runnable() {
         public void run() {
            var0.dialogResult(var1);
         }
      }, "dialog result thread")).start();
   }

   public static void ask(final String var0, final String var1, final SafeDialogCallback var2) {
      CommonUtils.runSafe(new Runnable() {
         public void run() {
            String var1x = JOptionPane.showInputDialog(var0, var1);
            if (var1x != null) {
               SafeDialogs.post(var2, var1x);
            }

         }
      });
   }

   public static void saveFile(final JFrame var0, final String var1, final SafeDialogCallback var2) {
      CommonUtils.runSafe(new Runnable() {
         public void run() {
            JFileChooser var1x = new JFileChooser();
            if (var1 != null) {
               var1x.setSelectedFile(new File(var1));
            }

            if (SafeDialogs.lastSaveDirectory != null) {
               var1x.setCurrentDirectory(SafeDialogs.lastSaveDirectory);
            }

            if (var1x.showSaveDialog(var0) == 0) {
               File var2x = var1x.getSelectedFile();
               if (var2x != null) {
                  if (var2x.isDirectory()) {
                     SafeDialogs.lastSaveDirectory = var2x;
                  } else {
                     SafeDialogs.lastSaveDirectory = var2x.getParentFile();
                  }

                  SafeDialogs.post(var2, var2x + "");
                  return;
               }
            }

         }
      });
   }

   public static void chooseColor(final String var0, final Color var1, final SafeDialogCallback var2) {
      CommonUtils.runSafe(new Runnable() {
         public void run() {
            Color var1x = JColorChooser.showDialog((Component)null, var0, var1);
            if (var1x != null) {
               SafeDialogs.post(var2, DialogUtils.encodeColor(var1x));
            }

         }
      });
   }

   public static void openFile(final String var0, final String var1, final String var2, final boolean var3, final boolean var4, final SafeDialogCallback var5) {
      CommonUtils.runSafe(new Runnable() {
         public void run() {
            JFileChooser var1x = new JFileChooser();
            if (var0 != null) {
               var1x.setDialogTitle(var0);
            }

            if (var1 != null) {
               var1x.setSelectedFile(new File(var1));
            }

            if (var2 != null) {
               var1x.setCurrentDirectory(new File(var2));
            } else if (SafeDialogs.lastOpenDirectory != null) {
               var1x.setCurrentDirectory(SafeDialogs.lastOpenDirectory);
            }

            var1x.setMultiSelectionEnabled(var3);
            if (var4) {
               var1x.setFileSelectionMode(1);
            }

            if (var1x.showOpenDialog((Component)null) == 0) {
               if (var3) {
                  StringBuffer var2x = new StringBuffer();
                  File[] var3x = var1x.getSelectedFiles();

                  for(int var4x = 0; var4x < var3x.length; ++var4x) {
                     if (var3x[var4x] != null && var3x[var4x].exists()) {
                        var2x.append(var3x[var4x]);
                        if (var4x + 1 < var3x.length) {
                           var2x.append(",");
                        }
                     }
                  }

                  SafeDialogs.post(var5, var2x.toString());
               } else {
                  if (var1x.getSelectedFile() != null && var1x.getSelectedFile().exists()) {
                     if (var1x.getSelectedFile().isDirectory()) {
                        SafeDialogs.lastOpenDirectory = var1x.getSelectedFile();
                     } else {
                        SafeDialogs.lastOpenDirectory = var1x.getSelectedFile().getParentFile();
                     }
                  }

                  SafeDialogs.post(var5, var1x.getSelectedFile() + "");
               }

            }
         }
      });
   }
}
