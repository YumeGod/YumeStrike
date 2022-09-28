package org.apache.batik.gvt.text;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.gvt.RootGraphicsNode;
import org.apache.batik.gvt.Selectable;
import org.apache.batik.gvt.Selector;
import org.apache.batik.gvt.TextNode;
import org.apache.batik.gvt.event.GraphicsNodeChangeEvent;
import org.apache.batik.gvt.event.GraphicsNodeEvent;
import org.apache.batik.gvt.event.GraphicsNodeKeyEvent;
import org.apache.batik.gvt.event.GraphicsNodeMouseEvent;
import org.apache.batik.gvt.event.SelectionEvent;
import org.apache.batik.gvt.event.SelectionListener;

public class ConcreteTextSelector implements Selector {
   private ArrayList listeners;
   private GraphicsNode selectionNode;
   private RootGraphicsNode selectionNodeRoot;

   public void mouseClicked(GraphicsNodeMouseEvent var1) {
      this.checkSelectGesture(var1);
   }

   public void mouseDragged(GraphicsNodeMouseEvent var1) {
      this.checkSelectGesture(var1);
   }

   public void mouseEntered(GraphicsNodeMouseEvent var1) {
      this.checkSelectGesture(var1);
   }

   public void mouseExited(GraphicsNodeMouseEvent var1) {
      this.checkSelectGesture(var1);
   }

   public void mouseMoved(GraphicsNodeMouseEvent var1) {
   }

   public void mousePressed(GraphicsNodeMouseEvent var1) {
      this.checkSelectGesture(var1);
   }

   public void mouseReleased(GraphicsNodeMouseEvent var1) {
      this.checkSelectGesture(var1);
   }

   public void keyPressed(GraphicsNodeKeyEvent var1) {
      this.report(var1, "keyPressed");
   }

   public void keyReleased(GraphicsNodeKeyEvent var1) {
      this.report(var1, "keyReleased");
   }

   public void keyTyped(GraphicsNodeKeyEvent var1) {
      this.report(var1, "keyTyped");
   }

   public void changeStarted(GraphicsNodeChangeEvent var1) {
   }

   public void changeCompleted(GraphicsNodeChangeEvent var1) {
      if (this.selectionNode != null) {
         Shape var2 = ((Selectable)this.selectionNode).getHighlightShape();
         this.dispatchSelectionEvent(new SelectionEvent(this.getSelection(), 1, var2));
      }
   }

   public void setSelection(Mark var1, Mark var2) {
      TextNode var3 = var1.getTextNode();
      if (var3 != var2.getTextNode()) {
         throw new Error("Markers not from same TextNode");
      } else {
         var3.setSelection(var1, var2);
         this.selectionNode = var3;
         this.selectionNodeRoot = var3.getRoot();
         Object var4 = this.getSelection();
         Shape var5 = var3.getHighlightShape();
         this.dispatchSelectionEvent(new SelectionEvent(var4, 2, var5));
      }
   }

   public void clearSelection() {
      if (this.selectionNode != null) {
         this.dispatchSelectionEvent(new SelectionEvent((Object)null, 3, (Shape)null));
         this.selectionNode = null;
         this.selectionNodeRoot = null;
      }
   }

   protected void checkSelectGesture(GraphicsNodeEvent var1) {
      GraphicsNodeMouseEvent var2 = null;
      if (var1 instanceof GraphicsNodeMouseEvent) {
         var2 = (GraphicsNodeMouseEvent)var1;
      }

      GraphicsNode var3 = var1.getGraphicsNode();
      if (this.isDeselectGesture(var1)) {
         if (this.selectionNode != null) {
            this.selectionNodeRoot.removeTreeGraphicsNodeChangeListener(this);
         }

         this.clearSelection();
      } else if (var2 != null) {
         Point2D.Double var4 = new Point2D.Double((double)var2.getX(), (double)var2.getY());
         AffineTransform var5 = var3.getGlobalTransform();
         if (var5 == null) {
            var5 = new AffineTransform();
         } else {
            try {
               var5 = var5.createInverse();
            } catch (NoninvertibleTransformException var8) {
            }
         }

         Point2D var9 = var5.transform(var4, (Point2D)null);
         if (var3 instanceof Selectable && this.isSelectStartGesture(var1)) {
            if (this.selectionNode != var3) {
               if (this.selectionNode != null) {
                  this.selectionNodeRoot.removeTreeGraphicsNodeChangeListener(this);
               }

               this.selectionNode = var3;
               if (var3 != null) {
                  this.selectionNodeRoot = var3.getRoot();
                  this.selectionNodeRoot.addTreeGraphicsNodeChangeListener(this);
               }
            }

            ((Selectable)var3).selectAt(var9.getX(), var9.getY());
            this.dispatchSelectionEvent(new SelectionEvent((Object)null, 4, (Shape)null));
         } else {
            Object var6;
            Shape var7;
            if (this.isSelectEndGesture(var1)) {
               if (this.selectionNode == var3) {
                  ((Selectable)var3).selectTo(var9.getX(), var9.getY());
               }

               var6 = this.getSelection();
               if (this.selectionNode != null) {
                  var7 = ((Selectable)this.selectionNode).getHighlightShape();
                  this.dispatchSelectionEvent(new SelectionEvent(var6, 2, var7));
               }
            } else if (this.isSelectContinueGesture(var1)) {
               if (this.selectionNode == var3) {
                  boolean var10 = ((Selectable)var3).selectTo(var9.getX(), var9.getY());
                  if (var10) {
                     var7 = ((Selectable)this.selectionNode).getHighlightShape();
                     this.dispatchSelectionEvent(new SelectionEvent((Object)null, 1, var7));
                  }
               }
            } else if (var3 instanceof Selectable && this.isSelectAllGesture(var1)) {
               if (this.selectionNode != var3) {
                  if (this.selectionNode != null) {
                     this.selectionNodeRoot.removeTreeGraphicsNodeChangeListener(this);
                  }

                  this.selectionNode = var3;
                  if (var3 != null) {
                     this.selectionNodeRoot = var3.getRoot();
                     this.selectionNodeRoot.addTreeGraphicsNodeChangeListener(this);
                  }
               }

               ((Selectable)var3).selectAll(var9.getX(), var9.getY());
               var6 = this.getSelection();
               var7 = ((Selectable)var3).getHighlightShape();
               this.dispatchSelectionEvent(new SelectionEvent(var6, 2, var7));
            }
         }
      }

   }

   private boolean isDeselectGesture(GraphicsNodeEvent var1) {
      return var1.getID() == 500 && ((GraphicsNodeMouseEvent)var1).getClickCount() == 1;
   }

   private boolean isSelectStartGesture(GraphicsNodeEvent var1) {
      return var1.getID() == 501;
   }

   private boolean isSelectEndGesture(GraphicsNodeEvent var1) {
      return var1.getID() == 502;
   }

   private boolean isSelectContinueGesture(GraphicsNodeEvent var1) {
      return var1.getID() == 506;
   }

   private boolean isSelectAllGesture(GraphicsNodeEvent var1) {
      return var1.getID() == 500 && ((GraphicsNodeMouseEvent)var1).getClickCount() == 2;
   }

   public Object getSelection() {
      Object var1 = null;
      if (this.selectionNode instanceof Selectable) {
         var1 = ((Selectable)this.selectionNode).getSelection();
      }

      return var1;
   }

   public boolean isEmpty() {
      return this.getSelection() == null;
   }

   public void dispatchSelectionEvent(SelectionEvent var1) {
      if (this.listeners != null) {
         Iterator var2 = this.listeners.iterator();
         switch (var1.getID()) {
            case 1:
               while(var2.hasNext()) {
                  ((SelectionListener)var2.next()).selectionChanged(var1);
               }

               return;
            case 2:
               while(var2.hasNext()) {
                  ((SelectionListener)var2.next()).selectionDone(var1);
               }

               return;
            case 3:
               while(var2.hasNext()) {
                  ((SelectionListener)var2.next()).selectionCleared(var1);
               }

               return;
            case 4:
               while(var2.hasNext()) {
                  ((SelectionListener)var2.next()).selectionStarted(var1);
               }
         }
      }

   }

   public void addSelectionListener(SelectionListener var1) {
      if (this.listeners == null) {
         this.listeners = new ArrayList();
      }

      this.listeners.add(var1);
   }

   public void removeSelectionListener(SelectionListener var1) {
      if (this.listeners != null) {
         this.listeners.remove(var1);
      }

   }

   private void report(GraphicsNodeEvent var1, String var2) {
      GraphicsNode var3 = var1.getGraphicsNode();
      String var4 = "(non-text node)";
      if (var3 instanceof TextNode) {
         AttributedCharacterIterator var6 = ((TextNode)var3).getAttributedCharacterIterator();
         char[] var5 = new char[var6.getEndIndex()];
         if (var5.length > 0) {
            var5[0] = var6.first();
         }

         for(int var7 = 1; var7 < var5.length; ++var7) {
            var5[var7] = var6.next();
         }

         var4 = new String(var5);
      }

      System.out.println("Mouse " + var2 + " in " + var4);
   }
}
