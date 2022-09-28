package org.apache.batik.apps.svgbrowser;

import java.util.ArrayList;

public abstract class AbstractCompoundCommand extends AbstractUndoableCommand {
   protected ArrayList atomCommands = new ArrayList();

   public void addCommand(UndoableCommand var1) {
      if (var1.shouldExecute()) {
         this.atomCommands.add(var1);
      }

   }

   public void execute() {
      int var1 = this.atomCommands.size();

      for(int var2 = 0; var2 < var1; ++var2) {
         UndoableCommand var3 = (UndoableCommand)this.atomCommands.get(var2);
         var3.execute();
      }

   }

   public void undo() {
      int var1 = this.atomCommands.size();

      for(int var2 = var1 - 1; var2 >= 0; --var2) {
         UndoableCommand var3 = (UndoableCommand)this.atomCommands.get(var2);
         var3.undo();
      }

   }

   public void redo() {
      int var1 = this.atomCommands.size();

      for(int var2 = 0; var2 < var1; ++var2) {
         UndoableCommand var3 = (UndoableCommand)this.atomCommands.get(var2);
         var3.redo();
      }

   }

   public boolean shouldExecute() {
      boolean var1 = true;
      if (this.atomCommands.size() == 0) {
         var1 = false;
      }

      int var2 = this.atomCommands.size();

      for(int var3 = 0; var3 < var2 && var1; ++var3) {
         UndoableCommand var4 = (UndoableCommand)this.atomCommands.get(var3);
         var1 = var4.shouldExecute() && var1;
      }

      return var1;
   }

   public int getCommandNumber() {
      return this.atomCommands.size();
   }
}
