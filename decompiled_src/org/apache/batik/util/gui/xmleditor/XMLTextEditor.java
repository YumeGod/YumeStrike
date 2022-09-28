package org.apache.batik.util.gui.xmleditor;

import java.awt.Color;
import javax.swing.JEditorPane;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Element;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

public class XMLTextEditor extends JEditorPane {
   protected UndoManager undoManager;

   public XMLTextEditor() {
      XMLEditorKit var1 = new XMLEditorKit();
      this.setEditorKitForContentType("text/xml", var1);
      this.setContentType("text/xml");
      this.setBackground(Color.white);
      this.undoManager = new UndoManager();
      UndoableEditListener var2 = new UndoableEditListener() {
         public void undoableEditHappened(UndoableEditEvent var1) {
            XMLTextEditor.this.undoManager.addEdit(var1.getEdit());
         }
      };
      this.getDocument().addUndoableEditListener(var2);
   }

   public void setText(String var1) {
      super.setText(var1);
      this.undoManager.discardAllEdits();
   }

   public void undo() {
      try {
         this.undoManager.undo();
      } catch (CannotUndoException var2) {
      }

   }

   public void redo() {
      try {
         this.undoManager.redo();
      } catch (CannotRedoException var2) {
      }

   }

   public void gotoLine(int var1) {
      Element var2 = this.getDocument().getDefaultRootElement().getElement(var1);
      if (var2 != null) {
         int var3 = var2.getStartOffset();
         this.setCaretPosition(var3);
      }
   }
}
