package org.apache.batik.apps.svgbrowser;

import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import org.apache.batik.util.gui.DropDownComponent;
import org.apache.batik.util.resources.ResourceManager;

public class DropDownHistoryModel implements DropDownComponent.ScrollablePopupMenuModel {
   private static final String RESOURCES = "org.apache.batik.apps.svgbrowser.resources.DropDownHistoryModelMessages";
   private static ResourceBundle bundle = ResourceBundle.getBundle("org.apache.batik.apps.svgbrowser.resources.DropDownHistoryModelMessages", Locale.getDefault());
   private static ResourceManager resources;
   protected ArrayList items = new ArrayList();
   protected HistoryBrowserInterface historyBrowserInterface;
   protected DropDownComponent.ScrollablePopupMenu parent;

   public DropDownHistoryModel(DropDownComponent.ScrollablePopupMenu var1, HistoryBrowserInterface var2) {
      this.parent = var1;
      this.historyBrowserInterface = var2;
      var2.getHistoryBrowser().addListener(new HistoryBrowser.HistoryBrowserAdapter() {
         public void historyReset(HistoryBrowser.HistoryBrowserEvent var1) {
            DropDownHistoryModel.this.clearAllScrollablePopupMenuItems("");
         }
      });
   }

   public String getFooterText() {
      return "";
   }

   public DropDownComponent.ScrollablePopupMenuItem createItem(String var1) {
      return new DropDownComponent.DefaultScrollablePopupMenuItem(this.parent, var1);
   }

   protected void addItem(DropDownComponent.ScrollablePopupMenuItem var1, String var2) {
      int var3 = this.items.size();
      this.items.add(0, var1);
      this.parent.add(var1, 0, var3, this.items.size());
      this.parent.fireItemsWereAdded(new DropDownComponent.ScrollablePopupMenuEvent(this.parent, 1, 1, var2));
   }

   protected void removeItem(DropDownComponent.ScrollablePopupMenuItem var1, String var2) {
      int var3 = this.items.size();
      this.items.remove(var1);
      this.parent.remove(var1, var3, this.items.size());
      this.parent.fireItemsWereRemoved(new DropDownComponent.ScrollablePopupMenuEvent(this.parent, 2, 1, var2));
   }

   protected boolean removeLastScrollablePopupMenuItem(String var1) {
      int var2 = this.items.size() - 1;
      if (var2 >= 0) {
         DropDownComponent.ScrollablePopupMenuItem var3 = (DropDownComponent.ScrollablePopupMenuItem)this.items.get(var2);
         this.removeItem(var3, var1);
         return true;
      } else {
         return false;
      }
   }

   protected boolean removeFirstScrollablePopupMenuItem(String var1) {
      byte var2 = 0;
      if (var2 < this.items.size()) {
         DropDownComponent.ScrollablePopupMenuItem var3 = (DropDownComponent.ScrollablePopupMenuItem)this.items.get(var2);
         this.removeItem(var3, var1);
         return true;
      } else {
         return false;
      }
   }

   protected void clearAllScrollablePopupMenuItems(String var1) {
      while(this.removeLastScrollablePopupMenuItem(var1)) {
      }

   }

   public void processItemClicked() {
   }

   public void processBeforeShowed() {
      this.historyBrowserInterface.performCurrentCompoundCommand();
   }

   public void processAfterShowed() {
   }

   static {
      resources = new ResourceManager(bundle);
   }

   public static class RedoPopUpMenuModel extends DropDownHistoryModel {
      protected static String REDO_FOOTER_TEXT;
      protected static String REDO_TOOLTIP_PREFIX;

      public RedoPopUpMenuModel(DropDownComponent.ScrollablePopupMenu var1, HistoryBrowserInterface var2) {
         super(var1, var2);
         this.init();
      }

      private void init() {
         this.historyBrowserInterface.getHistoryBrowser().addListener(new HistoryBrowser.HistoryBrowserAdapter() {
            public void executePerformed(HistoryBrowser.HistoryBrowserEvent var1) {
               HistoryBrowser.CommandNamesInfo var2 = (HistoryBrowser.CommandNamesInfo)var1.getSource();
               String var3 = DropDownHistoryModel.RedoPopUpMenuModel.REDO_TOOLTIP_PREFIX + var2.getLastRedoableCommandName();
               RedoPopUpMenuModel.this.clearAllScrollablePopupMenuItems(var3);
            }

            public void undoPerformed(HistoryBrowser.HistoryBrowserEvent var1) {
               HistoryBrowser.CommandNamesInfo var2 = (HistoryBrowser.CommandNamesInfo)var1.getSource();
               String var3 = DropDownHistoryModel.RedoPopUpMenuModel.REDO_TOOLTIP_PREFIX + var2.getLastRedoableCommandName();
               RedoPopUpMenuModel.this.addItem(RedoPopUpMenuModel.this.createItem(var2.getCommandName()), var3);
            }

            public void redoPerformed(HistoryBrowser.HistoryBrowserEvent var1) {
               HistoryBrowser.CommandNamesInfo var2 = (HistoryBrowser.CommandNamesInfo)var1.getSource();
               String var3 = DropDownHistoryModel.RedoPopUpMenuModel.REDO_TOOLTIP_PREFIX + var2.getLastRedoableCommandName();
               RedoPopUpMenuModel.this.removeFirstScrollablePopupMenuItem(var3);
            }

            public void doCompoundEdit(HistoryBrowser.HistoryBrowserEvent var1) {
               if (RedoPopUpMenuModel.this.parent.isEnabled()) {
                  RedoPopUpMenuModel.this.parent.setEnabled(false);
               }

            }

            public void compoundEditPerformed(HistoryBrowser.HistoryBrowserEvent var1) {
            }
         });
      }

      public String getFooterText() {
         return REDO_FOOTER_TEXT;
      }

      public void processItemClicked() {
         this.historyBrowserInterface.getHistoryBrowser().compoundRedo(this.parent.getSelectedItemsCount());
      }

      static {
         REDO_FOOTER_TEXT = DropDownHistoryModel.resources.getString("RedoModel.footerText");
         REDO_TOOLTIP_PREFIX = DropDownHistoryModel.resources.getString("RedoModel.tooltipPrefix");
      }
   }

   public static class UndoPopUpMenuModel extends DropDownHistoryModel {
      protected static String UNDO_FOOTER_TEXT;
      protected static String UNDO_TOOLTIP_PREFIX;

      public UndoPopUpMenuModel(DropDownComponent.ScrollablePopupMenu var1, HistoryBrowserInterface var2) {
         super(var1, var2);
         this.init();
      }

      private void init() {
         this.historyBrowserInterface.getHistoryBrowser().addListener(new HistoryBrowser.HistoryBrowserAdapter() {
            public void executePerformed(HistoryBrowser.HistoryBrowserEvent var1) {
               HistoryBrowser.CommandNamesInfo var2 = (HistoryBrowser.CommandNamesInfo)var1.getSource();
               String var3 = DropDownHistoryModel.UndoPopUpMenuModel.UNDO_TOOLTIP_PREFIX + var2.getLastUndoableCommandName();
               UndoPopUpMenuModel.this.addItem(UndoPopUpMenuModel.this.createItem(var2.getCommandName()), var3);
            }

            public void undoPerformed(HistoryBrowser.HistoryBrowserEvent var1) {
               HistoryBrowser.CommandNamesInfo var2 = (HistoryBrowser.CommandNamesInfo)var1.getSource();
               String var3 = DropDownHistoryModel.UndoPopUpMenuModel.UNDO_TOOLTIP_PREFIX + var2.getLastUndoableCommandName();
               UndoPopUpMenuModel.this.removeFirstScrollablePopupMenuItem(var3);
            }

            public void redoPerformed(HistoryBrowser.HistoryBrowserEvent var1) {
               HistoryBrowser.CommandNamesInfo var2 = (HistoryBrowser.CommandNamesInfo)var1.getSource();
               String var3 = DropDownHistoryModel.UndoPopUpMenuModel.UNDO_TOOLTIP_PREFIX + var2.getLastUndoableCommandName();
               UndoPopUpMenuModel.this.addItem(UndoPopUpMenuModel.this.createItem(var2.getCommandName()), var3);
            }

            public void doCompoundEdit(HistoryBrowser.HistoryBrowserEvent var1) {
               if (!UndoPopUpMenuModel.this.parent.isEnabled()) {
                  UndoPopUpMenuModel.this.parent.setEnabled(true);
               }

            }

            public void compoundEditPerformed(HistoryBrowser.HistoryBrowserEvent var1) {
            }
         });
      }

      public String getFooterText() {
         return UNDO_FOOTER_TEXT;
      }

      public void processItemClicked() {
         this.historyBrowserInterface.getHistoryBrowser().compoundUndo(this.parent.getSelectedItemsCount());
      }

      static {
         UNDO_FOOTER_TEXT = DropDownHistoryModel.resources.getString("UndoModel.footerText");
         UNDO_TOOLTIP_PREFIX = DropDownHistoryModel.resources.getString("UndoModel.tooltipPrefix");
      }
   }
}
