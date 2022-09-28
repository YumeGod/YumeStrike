package de.javasoft.plaf.synthetica;

import de.javasoft.util.OS;
import java.awt.Component;
import java.awt.Frame;
import java.awt.KeyEventPostProcessor;
import java.awt.Window;
import java.awt.event.KeyEvent;
import javax.swing.FocusManager;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JRootPane;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.RootPaneUI;
import javax.swing.plaf.basic.ComboPopup;

class ExtKeyEventProcessor implements KeyEventPostProcessor {
   private static boolean altKeyPressed = false;
   private static boolean menuActivated = false;
   private static boolean winKeyArmed = false;

   public boolean postProcessKeyEvent(KeyEvent var1) {
      if (var1.isConsumed()) {
         return false;
      } else {
         this.winMetaKeySupport(var1);
         if (!activateMenuByAltKey()) {
            return false;
         } else {
            int var2 = var1.getModifiers();
            if (var1.getKeyCode() == 18 && var2 != 10 & var2 != 32) {
               Component var3 = var1.getComponent();
               Window var4 = SwingUtilities.getWindowAncestor(var3);
               JRootPane var5 = SwingUtilities.getRootPane(var3);
               JMenuBar var6 = var5 != null ? var5.getJMenuBar() : null;
               if (var6 == null && var4 instanceof JFrame) {
                  var6 = ((JFrame)var4).getJMenuBar();
               }

               if (var1.getID() == 401) {
                  if (!altKeyPressed) {
                     this.altPressed(var1, var3, var5, var6);
                  }

                  altKeyPressed = true;
                  return true;
               }

               if (var1.getID() == 402) {
                  if (altKeyPressed) {
                     this.altReleased(var1, var3, var5, var6);
                  }

                  altKeyPressed = false;
               }
            } else {
               altKeyPressed = false;
            }

            return false;
         }
      }
   }

   static void resetWinMetaKey() {
      winKeyArmed = false;
   }

   private void winMetaKeySupport(KeyEvent var1) {
      int var2 = var1.getKeyCode();
      boolean var3 = var1.isMetaDown();
      boolean var4 = var1.isControlDown();
      Window var5 = FocusManager.getCurrentManager().getActiveWindow();
      JFrame var6 = var5 instanceof JFrame ? (JFrame)var5 : null;
      RootPaneUI var7;
      if (var2 == 70 && var3 && var4 && var1.getID() == 401 && var6 != null && var6.isResizable() && SyntheticaTitlePane.isFullScreenOnMacSupported(var6)) {
         var7 = var6.getRootPane().getUI();
         if (var7 instanceof SyntheticaRootPaneUI && ((SyntheticaRootPaneUI)var7).getTitlePane() instanceof SyntheticaTitlePane) {
            SyntheticaTitlePane var8 = (SyntheticaTitlePane)((SyntheticaRootPaneUI)var7).getTitlePane();
            if (this.isMaximized(var5)) {
               var8.restore();
            } else {
               var8.maximize();
            }
         }
      }

      if (var2 == 524 && var1.getID() == 401) {
         winKeyArmed = true;
      } else if (!winKeyArmed || var2 != 38) {
         if (var3 && var2 == 38 && !this.isMaximized(var5) && var6 != null) {
            var7 = var6.getRootPane().getUI();
            if (var7 instanceof SyntheticaRootPaneUI && ((SyntheticaRootPaneUI)var7).isSnapEnabled(var5) && ((SyntheticaRootPaneUI)var7).getTitlePane() instanceof SyntheticaTitlePane) {
               ((SyntheticaTitlePane)((SyntheticaRootPaneUI)var7).getTitlePane()).maximize();
            }
         } else if ((winKeyArmed || var3) && var2 == 40 && this.isMaximized(var5) && var6 != null) {
            var7 = var6.getRootPane().getUI();
            if (var7 instanceof SyntheticaRootPaneUI) {
               SyntheticaRootPaneUI var9 = (SyntheticaRootPaneUI)var7;
               if (this.isMaximized(var5) && var9.getTitlePane() instanceof SyntheticaTitlePane) {
                  ((SyntheticaTitlePane)var9.getTitlePane()).restore();
                  var9.restoreSnappedWindow(var5, var5.getGraphicsConfiguration(), true);
               }
            }
         } else if (!winKeyArmed || var2 != 40) {
            if (var3 && var2 == 40 && !this.isMaximized(var5) && var6 != null) {
               var7 = var6.getRootPane().getUI();
               if (var7 instanceof SyntheticaRootPaneUI && ((SyntheticaRootPaneUI)var7).isSnapEnabled(var5) && ((SyntheticaRootPaneUI)var7).getTitlePane() instanceof SyntheticaTitlePane) {
                  ((SyntheticaTitlePane)((SyntheticaRootPaneUI)var7).getTitlePane()).iconify();
               }
            } else if ((winKeyArmed || var3) && (var2 == 37 || var2 == 39) && var6 != null) {
               var7 = var6.getRootPane().getUI();
               if (var7 instanceof SyntheticaRootPaneUI && ((SyntheticaRootPaneUI)var7).isSnapEnabled(var5)) {
                  if (var2 == 37) {
                     ((SyntheticaRootPaneUI)var7).snapPrev(var5);
                  } else {
                     ((SyntheticaRootPaneUI)var7).snapNext(var5);
                  }
               }
            } else {
               winKeyArmed = false;
            }
         }
      }

   }

   private boolean isMaximized(Window var1) {
      return var1 instanceof Frame && (((Frame)var1).getExtendedState() & 6) == 6;
   }

   private void altPressed(KeyEvent var1, Component var2, JRootPane var3, JMenuBar var4) {
      if (UIManager.get("Button.showMnemonics") == null && !(var2 instanceof JMenuBar)) {
         var3.repaint();
      }

      this.altPressed(var1, var4);
   }

   private void altPressed(KeyEvent var1, JMenuBar var2) {
      boolean var3 = UIManager.getBoolean("Synthetica.forcedAltKeyEventConsumption");
      MenuSelectionManager var4 = MenuSelectionManager.defaultManager();
      MenuElement[] var5 = var4.getSelectedPath();
      if (var5.length > 0) {
         if (!(var5[0] instanceof ComboPopup)) {
            var4.clearSelectedPath();
         }

         menuActivated = false;
         if (!var3) {
            var1.consume();
         }
      } else {
         if (var2 != null) {
            menuActivated = true;
         }

         if (var2 != null && var2.getMenu(0) != null && !var3) {
            var1.consume();
         }
      }

      if (var3) {
         var1.consume();
      }

      if (var2 != null && menuActivated) {
         var2.repaint();
      }

   }

   public static void setMenuActive(boolean var0) {
      menuActivated = var0;
   }

   public static boolean isMenuActive() {
      return menuActivated;
   }

   public static boolean isAltKeyPressed() {
      return altKeyPressed;
   }

   public static boolean showMnemonicsOnAltKeyOnly() {
      return OS.getCurrentOS() == OS.Windows && SyntheticaLookAndFeel.getBoolean("Synthetica.showMnemonicsOnAltKeyOnly", (Component)null, true) ? true : SyntheticaLookAndFeel.getBoolean("Synthetica.forceShowMnemonicsOnAltKeyOnly", (Component)null);
   }

   public static boolean activateMenuByAltKey() {
      return SyntheticaLookAndFeel.getBoolean("Synthetica.activateMenuByAltKey", (Component)null, true);
   }

   private void altReleased(KeyEvent var1, Component var2, JRootPane var3, JMenuBar var4) {
      if (UIManager.get("Button.showMnemonics") == null && !(var2 instanceof JMenuBar)) {
         var3.repaint();
      }

      this.altReleased(var1, var4);
   }

   private void altReleased(KeyEvent var1, JMenuBar var2) {
      MenuSelectionManager var3 = MenuSelectionManager.defaultManager();
      if (var3.getSelectedPath().length == 0 && menuActivated) {
         JMenu var4 = var2 != null ? var2.getMenu(0) : null;
         if (var4 != null) {
            MenuElement[] var5 = new MenuElement[]{var2, var4};
            var3.setSelectedPath(var5);
         }
      }

   }
}
