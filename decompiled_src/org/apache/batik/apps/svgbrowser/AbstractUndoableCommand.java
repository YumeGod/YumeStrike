package org.apache.batik.apps.svgbrowser;

public abstract class AbstractUndoableCommand implements UndoableCommand {
   protected String name;

   public void execute() {
   }

   public void undo() {
   }

   public void redo() {
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public boolean shouldExecute() {
      return true;
   }
}
