package org.apache.batik.swing.svg;

import java.awt.event.MouseWheelEvent;
import org.apache.batik.swing.gvt.AbstractJGVTComponent;

public class JSVGComponent extends AbstractJSVGComponent {
   public JSVGComponent(SVGUserAgent var1, boolean var2, boolean var3) {
      super(var1, var2, var3);
   }

   protected AbstractJGVTComponent.Listener createListener() {
      return new ExtendedSVGListener();
   }

   protected class ExtendedSVGListener extends AbstractJSVGComponent.SVGListener {
      protected ExtendedSVGListener() {
         super();
      }

      protected void dispatchMouseWheelMoved(final MouseWheelEvent var1) {
         if (!JSVGComponent.this.isInteractiveDocument) {
            super.dispatchMouseWheelMoved(var1);
         } else {
            if (JSVGComponent.this.updateManager != null && JSVGComponent.this.updateManager.isRunning()) {
               JSVGComponent.this.updateManager.getUpdateRunnableQueue().invokeLater(new Runnable() {
                  public void run() {
                     JSVGComponent.this.eventDispatcher.mouseWheelMoved(var1);
                  }
               });
            }

         }
      }
   }
}
