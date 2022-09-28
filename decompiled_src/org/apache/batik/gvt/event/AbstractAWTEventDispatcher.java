package org.apache.batik.gvt.event;

import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.lang.reflect.Array;
import java.util.EventListener;
import java.util.EventObject;
import java.util.LinkedList;
import java.util.List;
import javax.swing.event.EventListenerList;
import org.apache.batik.gvt.GraphicsNode;

public abstract class AbstractAWTEventDispatcher implements EventDispatcher, MouseListener, MouseMotionListener, KeyListener {
   protected GraphicsNode root;
   protected AffineTransform baseTransform;
   protected EventListenerList glisteners;
   protected GraphicsNode lastHit;
   protected GraphicsNode currentKeyEventTarget;
   protected List eventQueue = new LinkedList();
   protected boolean eventDispatchEnabled = true;
   protected int eventQueueMaxSize = 10;
   static final int MAX_QUEUE_SIZE = 10;
   private int nodeIncrementEventID = 401;
   private int nodeIncrementEventCode = 9;
   private int nodeIncrementEventModifiers = 0;
   private int nodeDecrementEventID = 401;
   private int nodeDecrementEventCode = 9;
   private int nodeDecrementEventModifiers = 1;
   // $FF: synthetic field
   static Class class$org$apache$batik$gvt$event$GraphicsNodeMouseListener;
   // $FF: synthetic field
   static Class class$org$apache$batik$gvt$event$GraphicsNodeMouseWheelListener;
   // $FF: synthetic field
   static Class class$org$apache$batik$gvt$event$GraphicsNodeKeyListener;

   public void setRootNode(GraphicsNode var1) {
      if (this.root != var1) {
         this.eventQueue.clear();
      }

      this.root = var1;
   }

   public GraphicsNode getRootNode() {
      return this.root;
   }

   public void setBaseTransform(AffineTransform var1) {
      if (this.baseTransform != var1 && (this.baseTransform == null || !this.baseTransform.equals(var1))) {
         this.eventQueue.clear();
      }

      this.baseTransform = var1;
   }

   public AffineTransform getBaseTransform() {
      return new AffineTransform(this.baseTransform);
   }

   public void mousePressed(MouseEvent var1) {
      this.dispatchEvent(var1);
   }

   public void mouseReleased(MouseEvent var1) {
      this.dispatchEvent(var1);
   }

   public void mouseEntered(MouseEvent var1) {
      this.dispatchEvent(var1);
   }

   public void mouseExited(MouseEvent var1) {
      this.dispatchEvent(var1);
   }

   public void mouseClicked(MouseEvent var1) {
      this.dispatchEvent(var1);
   }

   public void mouseMoved(MouseEvent var1) {
      this.dispatchEvent(var1);
   }

   public void mouseDragged(MouseEvent var1) {
      this.dispatchEvent(var1);
   }

   public void keyPressed(KeyEvent var1) {
      this.dispatchEvent(var1);
   }

   public void keyReleased(KeyEvent var1) {
      this.dispatchEvent(var1);
   }

   public void keyTyped(KeyEvent var1) {
      this.dispatchEvent(var1);
   }

   public void addGraphicsNodeMouseListener(GraphicsNodeMouseListener var1) {
      if (this.glisteners == null) {
         this.glisteners = new EventListenerList();
      }

      this.glisteners.add(class$org$apache$batik$gvt$event$GraphicsNodeMouseListener == null ? (class$org$apache$batik$gvt$event$GraphicsNodeMouseListener = class$("org.apache.batik.gvt.event.GraphicsNodeMouseListener")) : class$org$apache$batik$gvt$event$GraphicsNodeMouseListener, var1);
   }

   public void removeGraphicsNodeMouseListener(GraphicsNodeMouseListener var1) {
      if (this.glisteners != null) {
         this.glisteners.remove(class$org$apache$batik$gvt$event$GraphicsNodeMouseListener == null ? (class$org$apache$batik$gvt$event$GraphicsNodeMouseListener = class$("org.apache.batik.gvt.event.GraphicsNodeMouseListener")) : class$org$apache$batik$gvt$event$GraphicsNodeMouseListener, var1);
      }

   }

   public void addGraphicsNodeMouseWheelListener(GraphicsNodeMouseWheelListener var1) {
      if (this.glisteners == null) {
         this.glisteners = new EventListenerList();
      }

      this.glisteners.add(class$org$apache$batik$gvt$event$GraphicsNodeMouseWheelListener == null ? (class$org$apache$batik$gvt$event$GraphicsNodeMouseWheelListener = class$("org.apache.batik.gvt.event.GraphicsNodeMouseWheelListener")) : class$org$apache$batik$gvt$event$GraphicsNodeMouseWheelListener, var1);
   }

   public void removeGraphicsNodeMouseWheelListener(GraphicsNodeMouseWheelListener var1) {
      if (this.glisteners != null) {
         this.glisteners.remove(class$org$apache$batik$gvt$event$GraphicsNodeMouseWheelListener == null ? (class$org$apache$batik$gvt$event$GraphicsNodeMouseWheelListener = class$("org.apache.batik.gvt.event.GraphicsNodeMouseWheelListener")) : class$org$apache$batik$gvt$event$GraphicsNodeMouseWheelListener, var1);
      }

   }

   public void addGraphicsNodeKeyListener(GraphicsNodeKeyListener var1) {
      if (this.glisteners == null) {
         this.glisteners = new EventListenerList();
      }

      this.glisteners.add(class$org$apache$batik$gvt$event$GraphicsNodeKeyListener == null ? (class$org$apache$batik$gvt$event$GraphicsNodeKeyListener = class$("org.apache.batik.gvt.event.GraphicsNodeKeyListener")) : class$org$apache$batik$gvt$event$GraphicsNodeKeyListener, var1);
   }

   public void removeGraphicsNodeKeyListener(GraphicsNodeKeyListener var1) {
      if (this.glisteners != null) {
         this.glisteners.remove(class$org$apache$batik$gvt$event$GraphicsNodeKeyListener == null ? (class$org$apache$batik$gvt$event$GraphicsNodeKeyListener = class$("org.apache.batik.gvt.event.GraphicsNodeKeyListener")) : class$org$apache$batik$gvt$event$GraphicsNodeKeyListener, var1);
      }

   }

   public EventListener[] getListeners(Class var1) {
      Object var2 = Array.newInstance(var1, this.glisteners.getListenerCount(var1));
      Object[] var3 = this.glisteners.getListenerList();
      int var4 = 0;

      for(int var5 = 0; var4 < var3.length - 1; var4 += 2) {
         if (var3[var4].equals(var1)) {
            Array.set(var2, var5, var3[var4 + 1]);
            ++var5;
         }
      }

      return (EventListener[])var2;
   }

   public void setEventDispatchEnabled(boolean var1) {
      this.eventDispatchEnabled = var1;
      if (this.eventDispatchEnabled) {
         while(this.eventQueue.size() > 0) {
            EventObject var2 = (EventObject)this.eventQueue.remove(0);
            this.dispatchEvent(var2);
         }
      }

   }

   public void setEventQueueMaxSize(int var1) {
      this.eventQueueMaxSize = var1;
      if (var1 == 0) {
         this.eventQueue.clear();
      }

      while(this.eventQueue.size() > this.eventQueueMaxSize) {
         this.eventQueue.remove(0);
      }

   }

   public void dispatchEvent(EventObject var1) {
      if (this.root != null) {
         if (this.eventDispatchEnabled) {
            if (var1 instanceof MouseEvent) {
               this.dispatchMouseEvent((MouseEvent)var1);
            } else if (var1 instanceof KeyEvent) {
               InputEvent var2 = (InputEvent)var1;
               if (this.isNodeIncrementEvent(var2)) {
                  this.incrementKeyTarget();
               } else if (this.isNodeDecrementEvent(var2)) {
                  this.decrementKeyTarget();
               } else {
                  this.dispatchKeyEvent((KeyEvent)var1);
               }
            }

         } else {
            if (this.eventQueueMaxSize > 0) {
               this.eventQueue.add(var1);

               while(this.eventQueue.size() > this.eventQueueMaxSize) {
                  this.eventQueue.remove(0);
               }
            }

         }
      }
   }

   protected int getCurrentLockState() {
      Toolkit var1 = Toolkit.getDefaultToolkit();
      int var2 = 0;

      try {
         if (var1.getLockingKeyState(262)) {
            ++var2;
         }
      } catch (UnsupportedOperationException var7) {
      }

      var2 <<= 1;

      try {
         if (var1.getLockingKeyState(145)) {
            ++var2;
         }
      } catch (UnsupportedOperationException var6) {
      }

      var2 <<= 1;

      try {
         if (var1.getLockingKeyState(144)) {
            ++var2;
         }
      } catch (UnsupportedOperationException var5) {
      }

      var2 <<= 1;

      try {
         if (var1.getLockingKeyState(20)) {
            ++var2;
         }
      } catch (UnsupportedOperationException var4) {
      }

      return var2;
   }

   protected abstract void dispatchKeyEvent(KeyEvent var1);

   protected abstract int getModifiers(InputEvent var1);

   protected abstract int getButton(MouseEvent var1);

   protected void dispatchMouseEvent(MouseEvent var1) {
      Point2D.Float var3 = new Point2D.Float((float)var1.getX(), (float)var1.getY());
      Object var4 = var3;
      if (this.baseTransform != null) {
         var4 = this.baseTransform.transform(var3, (Point2D)null);
      }

      GraphicsNode var5 = this.root.nodeHitAt((Point2D)var4);
      Point var6;
      if (!var1.getComponent().isShowing()) {
         var6 = new Point(0, 0);
      } else {
         var6 = var1.getComponent().getLocationOnScreen();
         var6.x += var1.getX();
         var6.y += var1.getY();
      }

      int var7 = this.getCurrentLockState();
      GraphicsNodeMouseEvent var2;
      if (this.lastHit != var5) {
         if (this.lastHit != null) {
            var2 = new GraphicsNodeMouseEvent(this.lastHit, 505, var1.getWhen(), this.getModifiers(var1), var7, this.getButton(var1), (float)((Point2D)var4).getX(), (float)((Point2D)var4).getY(), (int)Math.floor(var3.getX()), (int)Math.floor(var3.getY()), var6.x, var6.y, var1.getClickCount(), var5);
            this.processMouseEvent(var2);
         }

         if (var5 != null) {
            var2 = new GraphicsNodeMouseEvent(var5, 504, var1.getWhen(), this.getModifiers(var1), var7, this.getButton(var1), (float)((Point2D)var4).getX(), (float)((Point2D)var4).getY(), (int)Math.floor(var3.getX()), (int)Math.floor(var3.getY()), var6.x, var6.y, var1.getClickCount(), this.lastHit);
            this.processMouseEvent(var2);
         }
      }

      if (var5 != null) {
         var2 = new GraphicsNodeMouseEvent(var5, var1.getID(), var1.getWhen(), this.getModifiers(var1), var7, this.getButton(var1), (float)((Point2D)var4).getX(), (float)((Point2D)var4).getY(), (int)Math.floor(var3.getX()), (int)Math.floor(var3.getY()), var6.x, var6.y, var1.getClickCount(), (GraphicsNode)null);
         this.processMouseEvent(var2);
      } else {
         var2 = new GraphicsNodeMouseEvent(this.root, var1.getID(), var1.getWhen(), this.getModifiers(var1), var7, this.getButton(var1), (float)((Point2D)var4).getX(), (float)((Point2D)var4).getY(), (int)Math.floor(var3.getX()), (int)Math.floor(var3.getY()), var6.x, var6.y, var1.getClickCount(), (GraphicsNode)null);
         this.processMouseEvent(var2);
      }

      this.lastHit = var5;
   }

   protected void processMouseEvent(GraphicsNodeMouseEvent var1) {
      if (this.glisteners != null) {
         GraphicsNodeMouseListener[] var2 = (GraphicsNodeMouseListener[])this.getListeners(class$org$apache$batik$gvt$event$GraphicsNodeMouseListener == null ? (class$org$apache$batik$gvt$event$GraphicsNodeMouseListener = class$("org.apache.batik.gvt.event.GraphicsNodeMouseListener")) : class$org$apache$batik$gvt$event$GraphicsNodeMouseListener);
         int var3;
         switch (var1.getID()) {
            case 500:
               for(var3 = 0; var3 < var2.length; ++var3) {
                  var2[var3].mouseClicked(var1);
               }

               return;
            case 501:
               for(var3 = 0; var3 < var2.length; ++var3) {
                  var2[var3].mousePressed(var1);
               }

               return;
            case 502:
               for(var3 = 0; var3 < var2.length; ++var3) {
                  var2[var3].mouseReleased(var1);
               }

               return;
            case 503:
               for(var3 = 0; var3 < var2.length; ++var3) {
                  var2[var3].mouseMoved(var1);
               }

               return;
            case 504:
               for(var3 = 0; var3 < var2.length; ++var3) {
                  var2[var3].mouseEntered(var1);
               }

               return;
            case 505:
               for(var3 = 0; var3 < var2.length; ++var3) {
                  var2[var3].mouseExited(var1);
               }

               return;
            case 506:
               for(var3 = 0; var3 < var2.length; ++var3) {
                  var2[var3].mouseDragged(var1);
               }

               return;
            default:
               throw new IllegalArgumentException("Unknown Mouse Event type: " + var1.getID());
         }
      }
   }

   public void processKeyEvent(GraphicsNodeKeyEvent var1) {
      if (this.glisteners != null) {
         GraphicsNodeKeyListener[] var2 = (GraphicsNodeKeyListener[])this.getListeners(class$org$apache$batik$gvt$event$GraphicsNodeKeyListener == null ? (class$org$apache$batik$gvt$event$GraphicsNodeKeyListener = class$("org.apache.batik.gvt.event.GraphicsNodeKeyListener")) : class$org$apache$batik$gvt$event$GraphicsNodeKeyListener);
         int var3;
         label36:
         switch (var1.getID()) {
            case 400:
               var3 = 0;

               while(true) {
                  if (var3 >= var2.length) {
                     break label36;
                  }

                  var2[var3].keyTyped(var1);
                  ++var3;
               }
            case 401:
               var3 = 0;

               while(true) {
                  if (var3 >= var2.length) {
                     break label36;
                  }

                  var2[var3].keyPressed(var1);
                  ++var3;
               }
            case 402:
               var3 = 0;

               while(true) {
                  if (var3 >= var2.length) {
                     break label36;
                  }

                  var2[var3].keyReleased(var1);
                  ++var3;
               }
            default:
               throw new IllegalArgumentException("Unknown Key Event type: " + var1.getID());
         }
      }

      var1.consume();
   }

   private void incrementKeyTarget() {
      throw new UnsupportedOperationException("Increment not implemented.");
   }

   private void decrementKeyTarget() {
      throw new UnsupportedOperationException("Decrement not implemented.");
   }

   public void setNodeIncrementEvent(InputEvent var1) {
      this.nodeIncrementEventID = var1.getID();
      if (var1 instanceof KeyEvent) {
         this.nodeIncrementEventCode = ((KeyEvent)var1).getKeyCode();
      }

      this.nodeIncrementEventModifiers = var1.getModifiers();
   }

   public void setNodeDecrementEvent(InputEvent var1) {
      this.nodeDecrementEventID = var1.getID();
      if (var1 instanceof KeyEvent) {
         this.nodeDecrementEventCode = ((KeyEvent)var1).getKeyCode();
      }

      this.nodeDecrementEventModifiers = var1.getModifiers();
   }

   protected boolean isNodeIncrementEvent(InputEvent var1) {
      if (var1.getID() != this.nodeIncrementEventID) {
         return false;
      } else if (var1 instanceof KeyEvent && ((KeyEvent)var1).getKeyCode() != this.nodeIncrementEventCode) {
         return false;
      } else {
         return (var1.getModifiers() & this.nodeIncrementEventModifiers) != 0;
      }
   }

   protected boolean isNodeDecrementEvent(InputEvent var1) {
      if (var1.getID() != this.nodeDecrementEventID) {
         return false;
      } else if (var1 instanceof KeyEvent && ((KeyEvent)var1).getKeyCode() != this.nodeDecrementEventCode) {
         return false;
      } else {
         return (var1.getModifiers() & this.nodeDecrementEventModifiers) != 0;
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
}
