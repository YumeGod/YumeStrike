package com.mxgraph.swing.view;

import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.EventObject;
import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;

public class mxCellEditor implements mxICellEditor {
   public static int DEFAULT_MIN_WIDTH = 100;
   public static int DEFAULT_MIN_HEIGHT = 60;
   public static double DEFAULT_MINIMUM_EDITOR_SCALE = 1.0;
   protected mxGraphComponent graphComponent;
   protected double minimumEditorScale;
   protected int minimumWidth;
   protected int minimumHeight;
   protected transient Object editingCell;
   protected transient EventObject trigger;
   protected transient JScrollPane scrollPane;
   protected transient JTextArea textArea;
   protected transient JEditorPane editorPane;
   protected transient KeyAdapter keyListener;

   public mxCellEditor(mxGraphComponent var1) {
      this.minimumEditorScale = DEFAULT_MINIMUM_EDITOR_SCALE;
      this.minimumWidth = DEFAULT_MIN_WIDTH;
      this.minimumHeight = DEFAULT_MIN_HEIGHT;
      this.keyListener = new KeyAdapter() {
         protected transient boolean ignoreEnter = false;

         public void keyPressed(KeyEvent var1) {
            if (mxCellEditor.this.graphComponent.isEnterStopsCellEditing() && var1.getKeyCode() == 10) {
               if (!var1.isShiftDown() && !var1.isControlDown() && !var1.isAltDown()) {
                  if (!this.ignoreEnter) {
                     mxCellEditor.this.stopEditing(false);
                  }
               } else if (!this.ignoreEnter) {
                  this.ignoreEnter = true;

                  try {
                     KeyEvent var2 = new KeyEvent((Component)var1.getSource(), var1.getID(), var1.getWhen(), 0, var1.getKeyCode(), var1.getKeyChar());
                     ((Component)var1.getSource()).dispatchEvent(var2);
                  } finally {
                     this.ignoreEnter = false;
                  }
               }
            } else if (var1.getKeyCode() == 27) {
               mxCellEditor.this.stopEditing(true);
            }

         }
      };
      this.graphComponent = var1;
      this.textArea = new JTextArea();
      this.textArea.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
      this.textArea.setOpaque(false);
      this.editorPane = new JEditorPane();
      this.editorPane.setOpaque(false);
      this.editorPane.setContentType("text/html");
      this.installKeyHandler();
      this.scrollPane = new JScrollPane();
      this.scrollPane.setBorder(BorderFactory.createEmptyBorder());
      this.scrollPane.getViewport().setOpaque(false);
      this.scrollPane.setVisible(false);
      this.scrollPane.setOpaque(false);
   }

   protected void installKeyHandler() {
      this.textArea.addKeyListener(this.keyListener);
      this.editorPane.addKeyListener(this.keyListener);
   }

   public Component getEditor() {
      if (this.textArea.getParent() != null) {
         return this.textArea;
      } else {
         return this.editingCell != null ? this.editorPane : null;
      }
   }

   protected boolean useLabelBounds(mxCellState var1) {
      mxIGraphModel var2 = var1.getView().getGraph().getModel();
      mxGeometry var3 = var2.getGeometry(var1.getCell());
      return var3 != null && var3.getOffset() != null && !var3.isRelative() && (var3.getOffset().getX() != 0.0 || var3.getOffset().getY() != 0.0) || var2.isEdge(var1.getCell());
   }

   public Rectangle getEditorBounds(mxCellState var1, double var2) {
      mxIGraphModel var4 = var1.getView().getGraph().getModel();
      Rectangle var5 = null;
      if (this.useLabelBounds(var1)) {
         var5 = var1.getLabelBounds().getRectangle();
         var5.height += 10;
      } else {
         var5 = var1.getRectangle();
      }

      if (var4.isVertex(var1.getCell())) {
         String var6 = mxUtils.getString(var1.getStyle(), mxConstants.STYLE_LABEL_POSITION, "center");
         if (var6.equals("left")) {
            var5.x = (int)((double)var5.x - var1.getWidth());
         } else if (var6.equals("right")) {
            var5.x = (int)((double)var5.x + var1.getWidth());
         }

         String var7 = mxUtils.getString(var1.getStyle(), mxConstants.STYLE_VERTICAL_LABEL_POSITION, "middle");
         if (var7.equals("top")) {
            var5.y = (int)((double)var5.y - var1.getHeight());
         } else if (var7.equals("bottom")) {
            var5.y = (int)((double)var5.y + var1.getHeight());
         }
      }

      var5.setSize((int)Math.max(var5.getWidth(), (double)Math.round((double)this.minimumWidth * var2)), (int)Math.max(var5.getHeight(), (double)Math.round((double)this.minimumHeight * var2)));
      return var5;
   }

   public void startEditing(Object var1, EventObject var2) {
      if (this.editingCell != null) {
         this.stopEditing(true);
      }

      mxCellState var3 = this.graphComponent.getGraph().getView().getState(var1);
      if (var3 != null) {
         double var4 = Math.max(this.minimumEditorScale, this.graphComponent.getGraph().getView().getScale());
         Object var6 = null;
         this.trigger = var2;
         this.editingCell = var1;
         this.scrollPane.setBounds(this.getEditorBounds(var3, var4));
         this.scrollPane.setVisible(true);
         String var7 = this.getInitialValue(var3, var2);
         if (this.graphComponent.getGraph().isHtmlLabel(var1)) {
            this.editorPane.setDocument(mxUtils.createHtmlDocumentObject(var3.getStyle(), var4));
            this.editorPane.setText(mxUtils.getBodyMarkup(var7, true));
            JPanel var8 = new JPanel(new BorderLayout());
            var8.setOpaque(false);
            var8.add(this.editorPane, "Center");
            this.scrollPane.setViewportView(var8);
            var6 = this.editorPane;
         } else {
            this.textArea.setFont(mxUtils.getFont(var3.getStyle(), var4));
            Color var9 = mxUtils.getColor(var3.getStyle(), mxConstants.STYLE_FONTCOLOR, Color.black);
            this.textArea.setForeground(var9);
            this.textArea.setText(var7);
            this.scrollPane.setViewportView(this.textArea);
            var6 = this.textArea;
         }

         this.graphComponent.getGraphControl().add(this.scrollPane, 0);
         if (this.isHideLabel(var3)) {
            this.graphComponent.redraw(var3);
         }

         ((JTextComponent)var6).revalidate();
         ((JTextComponent)var6).requestFocusInWindow();
         ((JTextComponent)var6).selectAll();
      }

   }

   protected boolean isHideLabel(mxCellState var1) {
      return true;
   }

   public void stopEditing(boolean var1) {
      if (this.editingCell != null) {
         this.scrollPane.transferFocusUpCycle();
         Object var2 = this.editingCell;
         this.editingCell = null;
         if (!var1) {
            EventObject var3 = this.trigger;
            this.trigger = null;
            this.graphComponent.labelChanged(var2, this.getCurrentValue(), var3);
         } else {
            mxCellState var4 = this.graphComponent.getGraph().getView().getState(var2);
            this.graphComponent.redraw(var4);
         }

         if (this.scrollPane.getParent() != null) {
            this.scrollPane.setVisible(false);
            this.scrollPane.getParent().remove(this.scrollPane);
         }

         this.graphComponent.requestFocusInWindow();
      }

   }

   protected String getInitialValue(mxCellState var1, EventObject var2) {
      return this.graphComponent.getEditingValue(var1.getCell(), var2);
   }

   public String getCurrentValue() {
      return this.textArea.getParent() != null ? this.textArea.getText() : this.editorPane.getText();
   }

   public Object getEditingCell() {
      return this.editingCell;
   }

   public double getMinimumEditorScale() {
      return this.minimumEditorScale;
   }

   public void setMinimumEditorScale(double var1) {
      this.minimumEditorScale = var1;
   }

   public int getMinimumWidth() {
      return this.minimumWidth;
   }

   public void setMinimumWidth(int var1) {
      this.minimumWidth = var1;
   }

   public int getMinimumHeight() {
      return this.minimumHeight;
   }

   public void setMinimumHeight(int var1) {
      this.minimumHeight = var1;
   }
}
