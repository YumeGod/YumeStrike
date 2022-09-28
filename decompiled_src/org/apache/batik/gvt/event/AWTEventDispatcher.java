package org.apache.batik.gvt.event;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.EventObject;
import org.apache.batik.gvt.GraphicsNode;

public class AWTEventDispatcher extends AbstractAWTEventDispatcher implements MouseWheelListener {
   // $FF: synthetic field
   static Class class$org$apache$batik$gvt$event$GraphicsNodeMouseWheelListener;

   public void mouseWheelMoved(MouseWheelEvent var1) {
      this.dispatchEvent(var1);
   }

   public void dispatchEvent(EventObject var1) {
      if (var1 instanceof MouseWheelEvent) {
         if (this.root == null) {
            return;
         }

         if (!this.eventDispatchEnabled) {
            if (this.eventQueueMaxSize > 0) {
               this.eventQueue.add(var1);

               while(this.eventQueue.size() > this.eventQueueMaxSize) {
                  this.eventQueue.remove(0);
               }
            }

            return;
         }

         this.dispatchMouseWheelEvent((MouseWheelEvent)var1);
      } else {
         super.dispatchEvent(var1);
      }

   }

   protected void dispatchMouseWheelEvent(MouseWheelEvent var1) {
      if (this.lastHit != null) {
         this.processMouseWheelEvent(new GraphicsNodeMouseWheelEvent(this.lastHit, var1.getID(), var1.getWhen(), var1.getModifiersEx(), this.getCurrentLockState(), var1.getWheelRotation()));
      }

   }

   protected void processMouseWheelEvent(GraphicsNodeMouseWheelEvent var1) {
      if (this.glisteners != null) {
         GraphicsNodeMouseWheelListener[] var2 = (GraphicsNodeMouseWheelListener[])this.getListeners(class$org$apache$batik$gvt$event$GraphicsNodeMouseWheelListener == null ? (class$org$apache$batik$gvt$event$GraphicsNodeMouseWheelListener = class$("org.apache.batik.gvt.event.GraphicsNodeMouseWheelListener")) : class$org$apache$batik$gvt$event$GraphicsNodeMouseWheelListener);

         for(int var3 = 0; var3 < var2.length; ++var3) {
            var2[var3].mouseWheelMoved(var1);
         }
      }

   }

   protected void dispatchKeyEvent(KeyEvent var1) {
      this.currentKeyEventTarget = this.lastHit;
      GraphicsNode var2 = this.currentKeyEventTarget == null ? this.root : this.currentKeyEventTarget;
      this.processKeyEvent(new GraphicsNodeKeyEvent(var2, var1.getID(), var1.getWhen(), var1.getModifiersEx(), this.getCurrentLockState(), var1.getKeyCode(), var1.getKeyChar(), var1.getKeyLocation()));
   }

   protected int getModifiers(InputEvent var1) {
      return var1.getModifiersEx();
   }

   protected int getButton(MouseEvent var1) {
      return var1.getButton();
   }

   protected static boolean isMetaDown(int var0) {
      return (var0 & 256) != 0;
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
