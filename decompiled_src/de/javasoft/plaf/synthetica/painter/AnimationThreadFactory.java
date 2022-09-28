package de.javasoft.plaf.synthetica.painter;

import java.awt.Container;
import java.awt.Rectangle;
import java.awt.Window;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.HashMap;
import javax.swing.JComponent;

class AnimationThreadFactory {
   static final int DISABLED_TYPE = -1;
   static final int DEFAULT_TYPE = 0;
   static final int HOVER_IN_TYPE = 1;
   static final int HOVER_OUT_TYPE = 2;
   static final int FOCUSED_TYPE = 3;
   static final int DISPOSABLE_TYPE = 4;
   static final int ACTIVE_TYPE = 5;
   static final int INACTIVE_TYPE = 6;
   private static HashMap threads = new HashMap();
   private static AnimationThreadFactory instance = new AnimationThreadFactory();

   private AnimationThreadFactory() {
   }

   public static AnimationThread createThread(int var0) {
      return threads.containsKey(var0) ? (AnimationThread)threads.get(var0) : instance.newThread(var0);
   }

   private AnimationThread newThread(int var1) {
      AnimationThread var2 = new AnimationThread(var1);
      threads.put(var1, var2);
      var2.setDaemon(true);
      var2.start();
      return var2;
   }

   static class AnimationThread extends Thread {
      private int delay = 0;
      private HashMap componentAnimations = new HashMap();
      private ReferenceQueue queue = new ReferenceQueue();

      public AnimationThread(int var1) {
         this.delay = var1;
         this.setPriority(7);
         this.setName("SyntheticaAnimation " + var1);
      }

      public void run() {
         // $FF: Couldn't be decompiled
      }

      private void repaint(JComponent var1, Rectangle var2) {
         if (var2 == null) {
            var1.repaint();
         } else {
            var1.repaint(0L, var2.x, var2.y, var2.width, var2.height);
         }

      }

      private boolean isActive(JComponent var1) {
         Container var2 = var1.getTopLevelAncestor();
         return var2 instanceof Window && !"###focusableSwingPopup###".equals(var2.getName()) ? ((Window)var2).isActive() : true;
      }

      public void addComponent(JComponent var1, String var2, int var3, int var4, String[] var5, int var6, Rectangle var7) {
         boolean var8 = (var3 & 2) > 0;
         ComponentKey var9 = new ComponentKey(var1, var2);
         ComponentAnimation var10;
         if (!this.componentAnimations.containsKey(var9)) {
            var9.setComponent(var1);
            var10 = new ComponentAnimation((ComponentAnimation)null);
            var10.imagePaths = var5;
            var10.cyles = (long)var6;
            var10.repaintRect = var7;
            var10.type = var4;
            if (var8) {
               var10.state = var3;
            } else if (var4 == 6) {
               var10.cylesDone = (long)var6;
            }

            synchronized(this.componentAnimations) {
               this.componentAnimations.put(var9, var10);
            }
         } else {
            var10 = (ComponentAnimation)this.componentAnimations.get(var9);
            var10.repaintRect = var7;
            if (var10.type != var4 || !Arrays.toString(var5).equals(Arrays.toString(var10.imagePaths))) {
               var10.type = var4;
               var10.imagePaths = var5;
               var10.cyles = (long)var6;
               var10.reset();
            }

            if (var10.state != -1 || var8) {
               var10.state = var3;
            }

         }
      }

      void rotateRepaintRect(JComponent var1, String var2) {
         ComponentKey var3 = new ComponentKey(var1, var2);
         ComponentAnimation var4 = (ComponentAnimation)this.componentAnimations.get(var3);
         if (var4 != null) {
            int var5 = var4.repaintRect.height;
            var4.repaintRect.height = var4.repaintRect.width;
            var4.repaintRect.width = var5;
         }

      }

      public String getImagePath(JComponent var1, String var2) {
         ComponentAnimation var3 = (ComponentAnimation)this.componentAnimations.get(new ComponentKey(var1, var2));
         String var4 = null;
         synchronized(var3) {
            if (var3.type != 2 || var3.state != -1 && !var3.isComplete()) {
               if (var3.type == 6 && var3.isComplete()) {
                  var4 = var3.imagePaths[var3.imagePaths.length - 1];
               } else {
                  var4 = var3.imagePaths[var3.index];
               }
            } else {
               var4 = var3.imagePaths[var3.imagePaths.length - 1];
            }

            return var4;
         }
      }

      private static class ComponentAnimation {
         private int state;
         private int type;
         private int index;
         private String[] imagePaths;
         private Rectangle repaintRect;
         private long cyles;
         private long cylesDone;

         private ComponentAnimation() {
            this.state = -1;
            this.type = 0;
            this.index = 0;
            this.cyles = 0L;
            this.cylesDone = 0L;
         }

         boolean next() {
            if (this.cyles != 0L && this.cylesDone != this.cyles && this.imagePaths.length != 1) {
               ++this.index;
               if (this.index == this.imagePaths.length - 1) {
                  ++this.cylesDone;
               } else if (this.index == this.imagePaths.length) {
                  this.index = 0;
               }

               return true;
            } else {
               return false;
            }
         }

         void reset() {
            this.index = 0;
            this.cylesDone = 0L;
         }

         boolean isComplete() {
            return this.cyles == this.cylesDone;
         }

         // $FF: synthetic method
         ComponentAnimation(ComponentAnimation var1) {
            this();
         }
      }

      private class ComponentKey {
         private int hashCode;
         private WeakComponent wc;

         ComponentKey(JComponent var2, String var3) {
            this.hashCode = var2.hashCode() * 31 + var3.hashCode();
         }

         void setComponent(JComponent var1) {
            this.wc = AnimationThread.this.new WeakComponent(var1, this);
         }

         public boolean equals(Object var1) {
            ComponentKey var2 = (ComponentKey)var1;
            return var2.hashCode == this.hashCode;
         }

         public int hashCode() {
            return this.hashCode;
         }

         // $FF: synthetic method
         static WeakComponent access$0(ComponentKey var0) {
            return var0.wc;
         }
      }

      private class WeakComponent extends WeakReference {
         private ComponentKey key;

         public WeakComponent(JComponent var2, ComponentKey var3) {
            super(var2, AnimationThread.this.queue);
            this.key = var3;
         }

         // $FF: synthetic method
         static ComponentKey access$0(WeakComponent var0) {
            return var0.key;
         }
      }
   }
}
