package org.apache.batik.apps.svgbrowser;

public interface UndoableCommand {
   void execute();

   void undo();

   void redo();

   String getName();

   boolean shouldExecute();
}
