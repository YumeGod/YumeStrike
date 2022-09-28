package org.apache.batik.apps.svgbrowser;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import javax.swing.event.EventListenerList;

public class HistoryBrowser {
   public static final int EXECUTING = 1;
   public static final int UNDOING = 2;
   public static final int REDOING = 3;
   public static final int IDLE = 4;
   protected EventListenerList eventListeners = new EventListenerList();
   protected ArrayList history = new ArrayList();
   protected int currentCommandIndex = -1;
   protected int historySize = 1000;
   protected int state = 4;
   protected CommandController commandController;
   // $FF: synthetic field
   static Class class$org$apache$batik$apps$svgbrowser$HistoryBrowser$HistoryBrowserListener;

   public HistoryBrowser(CommandController var1) {
      this.commandController = var1;
   }

   public HistoryBrowser(int var1) {
      this.setHistorySize(var1);
   }

   protected void setHistorySize(int var1) {
      this.historySize = var1;
   }

   public void setCommandController(CommandController var1) {
      this.commandController = var1;
   }

   public void addCommand(UndoableCommand var1) {
      int var2 = this.history.size();

      for(int var3 = var2 - 1; var3 > this.currentCommandIndex; --var3) {
         this.history.remove(var3);
      }

      if (this.commandController != null) {
         this.commandController.execute(var1);
      } else {
         this.state = 1;
         var1.execute();
         this.state = 4;
      }

      this.history.add(var1);
      this.currentCommandIndex = this.history.size() - 1;
      if (this.currentCommandIndex >= this.historySize) {
         this.history.remove(0);
         --this.currentCommandIndex;
      }

      this.fireExecutePerformed(new HistoryBrowserEvent(new CommandNamesInfo(var1.getName(), this.getLastUndoableCommandName(), this.getLastRedoableCommandName())));
   }

   public void undo() {
      if (!this.history.isEmpty() && this.currentCommandIndex >= 0) {
         UndoableCommand var1 = (UndoableCommand)this.history.get(this.currentCommandIndex);
         if (this.commandController != null) {
            this.commandController.undo(var1);
         } else {
            this.state = 2;
            var1.undo();
            this.state = 4;
         }

         --this.currentCommandIndex;
         this.fireUndoPerformed(new HistoryBrowserEvent(new CommandNamesInfo(var1.getName(), this.getLastUndoableCommandName(), this.getLastRedoableCommandName())));
      }
   }

   public void redo() {
      if (!this.history.isEmpty() && this.currentCommandIndex != this.history.size() - 1) {
         UndoableCommand var1 = (UndoableCommand)this.history.get(++this.currentCommandIndex);
         if (this.commandController != null) {
            this.commandController.redo(var1);
         } else {
            this.state = 3;
            var1.redo();
            this.state = 4;
         }

         this.fireRedoPerformed(new HistoryBrowserEvent(new CommandNamesInfo(var1.getName(), this.getLastUndoableCommandName(), this.getLastRedoableCommandName())));
      }
   }

   public void compoundUndo(int var1) {
      for(int var2 = 0; var2 < var1; ++var2) {
         this.undo();
      }

   }

   public void compoundRedo(int var1) {
      for(int var2 = 0; var2 < var1; ++var2) {
         this.redo();
      }

   }

   public String getLastUndoableCommandName() {
      return !this.history.isEmpty() && this.currentCommandIndex >= 0 ? ((UndoableCommand)this.history.get(this.currentCommandIndex)).getName() : "";
   }

   public String getLastRedoableCommandName() {
      return !this.history.isEmpty() && this.currentCommandIndex != this.history.size() - 1 ? ((UndoableCommand)this.history.get(this.currentCommandIndex + 1)).getName() : "";
   }

   public void resetHistory() {
      this.history.clear();
      this.currentCommandIndex = -1;
      this.fireHistoryReset(new HistoryBrowserEvent(new Object()));
   }

   public int getState() {
      return this.commandController != null ? this.commandController.getState() : this.state;
   }

   public void addListener(HistoryBrowserListener var1) {
      this.eventListeners.add(class$org$apache$batik$apps$svgbrowser$HistoryBrowser$HistoryBrowserListener == null ? (class$org$apache$batik$apps$svgbrowser$HistoryBrowser$HistoryBrowserListener = class$("org.apache.batik.apps.svgbrowser.HistoryBrowser$HistoryBrowserListener")) : class$org$apache$batik$apps$svgbrowser$HistoryBrowser$HistoryBrowserListener, var1);
   }

   public void fireExecutePerformed(HistoryBrowserEvent var1) {
      Object[] var2 = this.eventListeners.getListenerList();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; var4 += 2) {
         if (var2[var4] == (class$org$apache$batik$apps$svgbrowser$HistoryBrowser$HistoryBrowserListener == null ? (class$org$apache$batik$apps$svgbrowser$HistoryBrowser$HistoryBrowserListener = class$("org.apache.batik.apps.svgbrowser.HistoryBrowser$HistoryBrowserListener")) : class$org$apache$batik$apps$svgbrowser$HistoryBrowser$HistoryBrowserListener)) {
            ((HistoryBrowserListener)var2[var4 + 1]).executePerformed(var1);
         }
      }

   }

   public void fireUndoPerformed(HistoryBrowserEvent var1) {
      Object[] var2 = this.eventListeners.getListenerList();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; var4 += 2) {
         if (var2[var4] == (class$org$apache$batik$apps$svgbrowser$HistoryBrowser$HistoryBrowserListener == null ? (class$org$apache$batik$apps$svgbrowser$HistoryBrowser$HistoryBrowserListener = class$("org.apache.batik.apps.svgbrowser.HistoryBrowser$HistoryBrowserListener")) : class$org$apache$batik$apps$svgbrowser$HistoryBrowser$HistoryBrowserListener)) {
            ((HistoryBrowserListener)var2[var4 + 1]).undoPerformed(var1);
         }
      }

   }

   public void fireRedoPerformed(HistoryBrowserEvent var1) {
      Object[] var2 = this.eventListeners.getListenerList();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; var4 += 2) {
         if (var2[var4] == (class$org$apache$batik$apps$svgbrowser$HistoryBrowser$HistoryBrowserListener == null ? (class$org$apache$batik$apps$svgbrowser$HistoryBrowser$HistoryBrowserListener = class$("org.apache.batik.apps.svgbrowser.HistoryBrowser$HistoryBrowserListener")) : class$org$apache$batik$apps$svgbrowser$HistoryBrowser$HistoryBrowserListener)) {
            ((HistoryBrowserListener)var2[var4 + 1]).redoPerformed(var1);
         }
      }

   }

   public void fireHistoryReset(HistoryBrowserEvent var1) {
      Object[] var2 = this.eventListeners.getListenerList();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; var4 += 2) {
         if (var2[var4] == (class$org$apache$batik$apps$svgbrowser$HistoryBrowser$HistoryBrowserListener == null ? (class$org$apache$batik$apps$svgbrowser$HistoryBrowser$HistoryBrowserListener = class$("org.apache.batik.apps.svgbrowser.HistoryBrowser$HistoryBrowserListener")) : class$org$apache$batik$apps$svgbrowser$HistoryBrowser$HistoryBrowserListener)) {
            ((HistoryBrowserListener)var2[var4 + 1]).historyReset(var1);
         }
      }

   }

   public void fireDoCompoundEdit(HistoryBrowserEvent var1) {
      Object[] var2 = this.eventListeners.getListenerList();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; var4 += 2) {
         if (var2[var4] == (class$org$apache$batik$apps$svgbrowser$HistoryBrowser$HistoryBrowserListener == null ? (class$org$apache$batik$apps$svgbrowser$HistoryBrowser$HistoryBrowserListener = class$("org.apache.batik.apps.svgbrowser.HistoryBrowser$HistoryBrowserListener")) : class$org$apache$batik$apps$svgbrowser$HistoryBrowser$HistoryBrowserListener)) {
            ((HistoryBrowserListener)var2[var4 + 1]).doCompoundEdit(var1);
         }
      }

   }

   public void fireCompoundEditPerformed(HistoryBrowserEvent var1) {
      Object[] var2 = this.eventListeners.getListenerList();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; var4 += 2) {
         if (var2[var4] == (class$org$apache$batik$apps$svgbrowser$HistoryBrowser$HistoryBrowserListener == null ? (class$org$apache$batik$apps$svgbrowser$HistoryBrowser$HistoryBrowserListener = class$("org.apache.batik.apps.svgbrowser.HistoryBrowser$HistoryBrowserListener")) : class$org$apache$batik$apps$svgbrowser$HistoryBrowser$HistoryBrowserListener)) {
            ((HistoryBrowserListener)var2[var4 + 1]).compoundEditPerformed(var1);
         }
      }

   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   public static class DocumentCommandController implements CommandController {
      protected DOMViewerController controller;
      protected int state = 4;

      public DocumentCommandController(DOMViewerController var1) {
         this.controller = var1;
      }

      public void execute(final UndoableCommand var1) {
         Runnable var2 = new Runnable() {
            public void run() {
               DocumentCommandController.this.state = 1;
               var1.execute();
               DocumentCommandController.this.state = 4;
            }
         };
         this.controller.performUpdate(var2);
      }

      public void undo(final UndoableCommand var1) {
         Runnable var2 = new Runnable() {
            public void run() {
               DocumentCommandController.this.state = 2;
               var1.undo();
               DocumentCommandController.this.state = 4;
            }
         };
         this.controller.performUpdate(var2);
      }

      public void redo(final UndoableCommand var1) {
         Runnable var2 = new Runnable() {
            public void run() {
               DocumentCommandController.this.state = 3;
               var1.redo();
               DocumentCommandController.this.state = 4;
            }
         };
         this.controller.performUpdate(var2);
      }

      public int getState() {
         return this.state;
      }
   }

   public interface CommandController {
      void execute(UndoableCommand var1);

      void undo(UndoableCommand var1);

      void redo(UndoableCommand var1);

      int getState();
   }

   public static class CommandNamesInfo {
      private String lastUndoableCommandName;
      private String lastRedoableCommandName;
      private String commandName;

      public CommandNamesInfo(String var1, String var2, String var3) {
         this.lastUndoableCommandName = var2;
         this.lastRedoableCommandName = var3;
         this.commandName = var1;
      }

      public String getLastRedoableCommandName() {
         return this.lastRedoableCommandName;
      }

      public String getLastUndoableCommandName() {
         return this.lastUndoableCommandName;
      }

      public String getCommandName() {
         return this.commandName;
      }
   }

   public static class HistoryBrowserAdapter implements HistoryBrowserListener {
      public void executePerformed(HistoryBrowserEvent var1) {
      }

      public void undoPerformed(HistoryBrowserEvent var1) {
      }

      public void redoPerformed(HistoryBrowserEvent var1) {
      }

      public void historyReset(HistoryBrowserEvent var1) {
      }

      public void compoundEditPerformed(HistoryBrowserEvent var1) {
      }

      public void doCompoundEdit(HistoryBrowserEvent var1) {
      }
   }

   public interface HistoryBrowserListener extends EventListener {
      void executePerformed(HistoryBrowserEvent var1);

      void undoPerformed(HistoryBrowserEvent var1);

      void redoPerformed(HistoryBrowserEvent var1);

      void historyReset(HistoryBrowserEvent var1);

      void doCompoundEdit(HistoryBrowserEvent var1);

      void compoundEditPerformed(HistoryBrowserEvent var1);
   }

   public static class HistoryBrowserEvent extends EventObject {
      public HistoryBrowserEvent(Object var1) {
         super(var1);
      }
   }
}
