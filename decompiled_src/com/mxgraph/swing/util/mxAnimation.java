package com.mxgraph.swing.util;

import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class mxAnimation extends mxEventSource {
   public static int DEFAULT_DELAY = 20;
   protected int delay;
   protected Timer timer;

   public mxAnimation() {
      this(DEFAULT_DELAY);
   }

   public mxAnimation(int var1) {
      this.delay = var1;
   }

   public int getDelay() {
      return this.delay;
   }

   public void setDelay(int var1) {
      this.delay = var1;
   }

   public void startAnimation() {
      if (this.timer == null) {
         this.timer = new Timer(this.delay, new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               mxAnimation.this.updateAnimation();
            }
         });
         this.timer.start();
      }

   }

   public void updateAnimation() {
      this.fireEvent(new mxEventObject("execute"));
   }

   public void stopAnimation() {
      if (this.timer != null) {
         this.timer.stop();
         this.timer = null;
         this.fireEvent(new mxEventObject("done"));
      }

   }
}
