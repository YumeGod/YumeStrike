package org.apache.batik.swing.gvt;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import org.apache.batik.gvt.Selectable;
import org.apache.batik.gvt.event.EventDispatcher;
import org.apache.batik.gvt.event.GraphicsNodeMouseEvent;
import org.apache.batik.gvt.event.GraphicsNodeMouseListener;
import org.apache.batik.gvt.event.SelectionEvent;
import org.apache.batik.gvt.event.SelectionListener;
import org.apache.batik.gvt.text.ConcreteTextSelector;
import org.apache.batik.gvt.text.Mark;

public class TextSelectionManager {
   public static final Cursor TEXT_CURSOR = new Cursor(2);
   protected ConcreteTextSelector textSelector;
   protected AbstractJGVTComponent component;
   protected Overlay selectionOverlay = new SelectionOverlay();
   protected MouseListener mouseListener;
   protected Cursor previousCursor;
   protected Shape selectionHighlight;
   protected SelectionListener textSelectionListener;
   protected Color selectionOverlayColor = new Color(100, 100, 255, 100);
   protected Color selectionOverlayStrokeColor;
   protected boolean xorMode;
   Object selection;

   public TextSelectionManager(AbstractJGVTComponent var1, EventDispatcher var2) {
      this.selectionOverlayStrokeColor = Color.white;
      this.xorMode = false;
      this.selection = null;
      this.textSelector = new ConcreteTextSelector();
      this.textSelectionListener = new TextSelectionListener();
      this.textSelector.addSelectionListener(this.textSelectionListener);
      this.mouseListener = new MouseListener();
      this.component = var1;
      this.component.getOverlays().add(this.selectionOverlay);
      var2.addGraphicsNodeMouseListener(this.mouseListener);
   }

   public void addSelectionListener(SelectionListener var1) {
      this.textSelector.addSelectionListener(var1);
   }

   public void removeSelectionListener(SelectionListener var1) {
      this.textSelector.removeSelectionListener(var1);
   }

   public void setSelectionOverlayColor(Color var1) {
      this.selectionOverlayColor = var1;
   }

   public Color getSelectionOverlayColor() {
      return this.selectionOverlayColor;
   }

   public void setSelectionOverlayStrokeColor(Color var1) {
      this.selectionOverlayStrokeColor = var1;
   }

   public Color getSelectionOverlayStrokeColor() {
      return this.selectionOverlayStrokeColor;
   }

   public void setSelectionOverlayXORMode(boolean var1) {
      this.xorMode = var1;
   }

   public boolean isSelectionOverlayXORMode() {
      return this.xorMode;
   }

   public Overlay getSelectionOverlay() {
      return this.selectionOverlay;
   }

   public Object getSelection() {
      return this.selection;
   }

   public void setSelection(Mark var1, Mark var2) {
      this.textSelector.setSelection(var1, var2);
   }

   public void clearSelection() {
      this.textSelector.clearSelection();
   }

   protected Rectangle outset(Rectangle var1, int var2) {
      var1.x -= var2;
      var1.y -= var2;
      var1.width += 2 * var2;
      var1.height += 2 * var2;
      return var1;
   }

   protected Rectangle getHighlightBounds() {
      AffineTransform var1 = this.component.getRenderingTransform();
      Shape var2 = var1.createTransformedShape(this.selectionHighlight);
      return this.outset(var2.getBounds(), 1);
   }

   protected class SelectionOverlay implements Overlay {
      public void paint(Graphics var1) {
         if (TextSelectionManager.this.selectionHighlight != null) {
            AffineTransform var2 = TextSelectionManager.this.component.getRenderingTransform();
            Shape var3 = var2.createTransformedShape(TextSelectionManager.this.selectionHighlight);
            Graphics2D var4 = (Graphics2D)var1;
            if (TextSelectionManager.this.xorMode) {
               var4.setColor(Color.black);
               var4.setXORMode(Color.white);
               var4.fill(var3);
            } else {
               var4.setColor(TextSelectionManager.this.selectionOverlayColor);
               var4.fill(var3);
               if (TextSelectionManager.this.selectionOverlayStrokeColor != null) {
                  var4.setStroke(new BasicStroke(1.0F));
                  var4.setColor(TextSelectionManager.this.selectionOverlayStrokeColor);
                  var4.draw(var3);
               }
            }
         }

      }
   }

   protected class TextSelectionListener implements SelectionListener {
      public void selectionDone(SelectionEvent var1) {
         this.selectionChanged(var1);
         TextSelectionManager.this.selection = var1.getSelection();
      }

      public void selectionCleared(SelectionEvent var1) {
         this.selectionStarted(var1);
      }

      public void selectionStarted(SelectionEvent var1) {
         if (TextSelectionManager.this.selectionHighlight != null) {
            Rectangle var2 = TextSelectionManager.this.getHighlightBounds();
            TextSelectionManager.this.selectionHighlight = null;
            TextSelectionManager.this.component.repaint(var2);
         }

         TextSelectionManager.this.selection = null;
      }

      public void selectionChanged(SelectionEvent var1) {
         Rectangle var2 = null;
         AffineTransform var3 = TextSelectionManager.this.component.getRenderingTransform();
         if (TextSelectionManager.this.selectionHighlight != null) {
            var2 = var3.createTransformedShape(TextSelectionManager.this.selectionHighlight).getBounds();
            TextSelectionManager.this.outset(var2, 1);
         }

         TextSelectionManager.this.selectionHighlight = var1.getHighlightShape();
         if (TextSelectionManager.this.selectionHighlight != null) {
            if (var2 != null) {
               Rectangle var4 = TextSelectionManager.this.getHighlightBounds();
               var4.add(var2);
               TextSelectionManager.this.component.repaint(var4);
            } else {
               TextSelectionManager.this.component.repaint(TextSelectionManager.this.getHighlightBounds());
            }
         } else if (var2 != null) {
            TextSelectionManager.this.component.repaint(var2);
         }

      }
   }

   protected class MouseListener implements GraphicsNodeMouseListener {
      public void mouseClicked(GraphicsNodeMouseEvent var1) {
         if (var1.getSource() instanceof Selectable) {
            TextSelectionManager.this.textSelector.mouseClicked(var1);
         }

      }

      public void mousePressed(GraphicsNodeMouseEvent var1) {
         if (var1.getSource() instanceof Selectable) {
            TextSelectionManager.this.textSelector.mousePressed(var1);
         } else if (TextSelectionManager.this.selectionHighlight != null) {
            TextSelectionManager.this.textSelector.clearSelection();
         }

      }

      public void mouseReleased(GraphicsNodeMouseEvent var1) {
         TextSelectionManager.this.textSelector.mouseReleased(var1);
      }

      public void mouseEntered(GraphicsNodeMouseEvent var1) {
         if (var1.getSource() instanceof Selectable) {
            TextSelectionManager.this.textSelector.mouseEntered(var1);
            TextSelectionManager.this.previousCursor = TextSelectionManager.this.component.getCursor();
            if (TextSelectionManager.this.previousCursor.getType() == 0) {
               TextSelectionManager.this.component.setCursor(TextSelectionManager.TEXT_CURSOR);
            }
         }

      }

      public void mouseExited(GraphicsNodeMouseEvent var1) {
         if (var1.getSource() instanceof Selectable) {
            TextSelectionManager.this.textSelector.mouseExited(var1);
            if (TextSelectionManager.this.component.getCursor() == TextSelectionManager.TEXT_CURSOR) {
               TextSelectionManager.this.component.setCursor(TextSelectionManager.this.previousCursor);
            }
         }

      }

      public void mouseDragged(GraphicsNodeMouseEvent var1) {
         if (var1.getSource() instanceof Selectable) {
            TextSelectionManager.this.textSelector.mouseDragged(var1);
         }

      }

      public void mouseMoved(GraphicsNodeMouseEvent var1) {
      }
   }
}
