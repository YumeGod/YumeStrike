package org.apache.batik.swing.gvt;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class JGVTComponent extends AbstractJGVTComponent {
   public JGVTComponent() {
   }

   public JGVTComponent(boolean var1, boolean var2) {
      super(var1, var2);
   }

   protected void addAWTListeners() {
      super.addAWTListeners();
      this.addMouseWheelListener((ExtendedListener)this.listener);
   }

   protected AbstractJGVTComponent.Listener createListener() {
      return new ExtendedListener();
   }

   protected class ExtendedListener extends AbstractJGVTComponent.Listener implements MouseWheelListener {
      protected ExtendedListener() {
         super();
      }

      public void mouseWheelMoved(MouseWheelEvent var1) {
         if (JGVTComponent.this.eventDispatcher != null) {
            this.dispatchMouseWheelMoved(var1);
         }

      }

      protected void dispatchMouseWheelMoved(MouseWheelEvent var1) {
         JGVTComponent.this.eventDispatcher.mouseWheelMoved(var1);
      }
   }
}
