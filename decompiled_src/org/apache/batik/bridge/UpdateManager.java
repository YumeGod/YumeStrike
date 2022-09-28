package org.apache.batik.bridge;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.batik.bridge.svg12.DefaultXBLManager;
import org.apache.batik.bridge.svg12.SVG12BridgeContext;
import org.apache.batik.bridge.svg12.SVG12ScriptingEnvironment;
import org.apache.batik.dom.events.AbstractEvent;
import org.apache.batik.dom.svg.SVGOMDocument;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.gvt.RootGraphicsNode;
import org.apache.batik.gvt.UpdateTracker;
import org.apache.batik.gvt.renderer.ImageRenderer;
import org.apache.batik.util.EventDispatcher;
import org.apache.batik.util.RunnableQueue;
import org.w3c.dom.Document;
import org.w3c.dom.events.DocumentEvent;
import org.w3c.dom.events.EventTarget;

public class UpdateManager {
   static final int MIN_REPAINT_TIME;
   protected BridgeContext bridgeContext;
   protected Document document;
   protected RunnableQueue updateRunnableQueue;
   protected RunnableQueue.RunHandler runHandler;
   protected volatile boolean running;
   protected volatile boolean suspendCalled;
   protected List listeners = Collections.synchronizedList(new LinkedList());
   protected ScriptingEnvironment scriptingEnvironment;
   protected RepaintManager repaintManager;
   protected UpdateTracker updateTracker;
   protected GraphicsNode graphicsNode;
   protected boolean started;
   protected BridgeContext[] secondaryBridgeContexts;
   protected ScriptingEnvironment[] secondaryScriptingEnvironments;
   protected int minRepaintTime;
   long outOfDateTime = 0L;
   List suspensionList = new ArrayList();
   int nextSuspensionIndex = 1;
   long allResumeTime = -1L;
   Timer repaintTriggerTimer = null;
   TimerTask repaintTimerTask = null;
   static EventDispatcher.Dispatcher startedDispatcher;
   static EventDispatcher.Dispatcher stoppedDispatcher;
   static EventDispatcher.Dispatcher suspendedDispatcher;
   static EventDispatcher.Dispatcher resumedDispatcher;
   static EventDispatcher.Dispatcher updateStartedDispatcher;
   static EventDispatcher.Dispatcher updateCompletedDispatcher;
   static EventDispatcher.Dispatcher updateFailedDispatcher;

   public UpdateManager(BridgeContext var1, GraphicsNode var2, Document var3) {
      this.bridgeContext = var1;
      this.bridgeContext.setUpdateManager(this);
      this.document = var3;
      this.updateRunnableQueue = RunnableQueue.createRunnableQueue();
      this.runHandler = this.createRunHandler();
      this.updateRunnableQueue.setRunHandler(this.runHandler);
      this.graphicsNode = var2;
      this.scriptingEnvironment = this.initializeScriptingEnvironment(this.bridgeContext);
      this.secondaryBridgeContexts = (BridgeContext[])var1.getChildContexts().clone();
      this.secondaryScriptingEnvironments = new ScriptingEnvironment[this.secondaryBridgeContexts.length];

      for(int var4 = 0; var4 < this.secondaryBridgeContexts.length; ++var4) {
         BridgeContext var5 = this.secondaryBridgeContexts[var4];
         if (((SVGOMDocument)var5.getDocument()).isSVG12()) {
            var5.setUpdateManager(this);
            ScriptingEnvironment var6 = this.initializeScriptingEnvironment(var5);
            this.secondaryScriptingEnvironments[var4] = var6;
         }
      }

      this.minRepaintTime = MIN_REPAINT_TIME;
   }

   public int getMinRepaintTime() {
      return this.minRepaintTime;
   }

   public void setMinRepaintTime(int var1) {
      this.minRepaintTime = var1;
   }

   protected ScriptingEnvironment initializeScriptingEnvironment(BridgeContext var1) {
      SVGOMDocument var2 = (SVGOMDocument)var1.getDocument();
      Object var3;
      if (var2.isSVG12()) {
         var3 = new SVG12ScriptingEnvironment(var1);
         var1.xblManager = new DefaultXBLManager(var2, var1);
         var2.setXBLManager(var1.xblManager);
      } else {
         var3 = new ScriptingEnvironment(var1);
      }

      return (ScriptingEnvironment)var3;
   }

   public synchronized void dispatchSVGLoadEvent() throws InterruptedException {
      this.dispatchSVGLoadEvent(this.bridgeContext, this.scriptingEnvironment);

      for(int var1 = 0; var1 < this.secondaryScriptingEnvironments.length; ++var1) {
         BridgeContext var2 = this.secondaryBridgeContexts[var1];
         if (((SVGOMDocument)var2.getDocument()).isSVG12()) {
            ScriptingEnvironment var3 = this.secondaryScriptingEnvironments[var1];
            this.dispatchSVGLoadEvent(var2, var3);
         }
      }

      this.secondaryBridgeContexts = null;
      this.secondaryScriptingEnvironments = null;
   }

   protected void dispatchSVGLoadEvent(BridgeContext var1, ScriptingEnvironment var2) {
      var2.loadScripts();
      var2.dispatchSVGLoadEvent();
      if (var1.isSVG12() && var1.xblManager != null) {
         SVG12BridgeContext var3 = (SVG12BridgeContext)var1;
         var3.addBindingListener();
         var3.xblManager.startProcessing();
      }

   }

   public void dispatchSVGZoomEvent() throws InterruptedException {
      this.scriptingEnvironment.dispatchSVGZoomEvent();
   }

   public void dispatchSVGScrollEvent() throws InterruptedException {
      this.scriptingEnvironment.dispatchSVGScrollEvent();
   }

   public void dispatchSVGResizeEvent() throws InterruptedException {
      this.scriptingEnvironment.dispatchSVGResizeEvent();
   }

   public void manageUpdates(final ImageRenderer var1) {
      this.updateRunnableQueue.preemptLater(new Runnable() {
         public void run() {
            synchronized(UpdateManager.this) {
               UpdateManager.this.running = true;
               UpdateManager.this.updateTracker = new UpdateTracker();
               RootGraphicsNode var2 = UpdateManager.this.graphicsNode.getRoot();
               if (var2 != null) {
                  var2.addTreeGraphicsNodeChangeListener(UpdateManager.this.updateTracker);
               }

               UpdateManager.this.repaintManager = new RepaintManager(var1);
               UpdateManagerEvent var3 = new UpdateManagerEvent(UpdateManager.this, (BufferedImage)null, (List)null);
               UpdateManager.this.fireEvent(UpdateManager.startedDispatcher, var3);
               UpdateManager.this.started = true;
            }
         }
      });
      this.resume();
   }

   public BridgeContext getBridgeContext() {
      return this.bridgeContext;
   }

   public RunnableQueue getUpdateRunnableQueue() {
      return this.updateRunnableQueue;
   }

   public RepaintManager getRepaintManager() {
      return this.repaintManager;
   }

   public UpdateTracker getUpdateTracker() {
      return this.updateTracker;
   }

   public Document getDocument() {
      return this.document;
   }

   public ScriptingEnvironment getScriptingEnvironment() {
      return this.scriptingEnvironment;
   }

   public synchronized boolean isRunning() {
      return this.running;
   }

   public synchronized void suspend() {
      if (this.updateRunnableQueue.getQueueState() == RunnableQueue.RUNNING) {
         this.updateRunnableQueue.suspendExecution(false);
      }

      this.suspendCalled = true;
   }

   public synchronized void resume() {
      if (this.updateRunnableQueue.getQueueState() != RunnableQueue.RUNNING) {
         this.updateRunnableQueue.resumeExecution();
      }

   }

   public void interrupt() {
      Runnable var1 = new Runnable() {
         public void run() {
            synchronized(UpdateManager.this) {
               if (UpdateManager.this.started) {
                  UpdateManager.this.dispatchSVGUnLoadEvent();
               } else {
                  UpdateManager.this.running = false;
                  UpdateManager.this.scriptingEnvironment.interrupt();
                  UpdateManager.this.updateRunnableQueue.getThread().halt();
               }

            }
         }
      };

      try {
         this.updateRunnableQueue.preemptLater(var1);
         this.updateRunnableQueue.resumeExecution();
      } catch (IllegalStateException var3) {
      }

   }

   public void dispatchSVGUnLoadEvent() {
      if (!this.started) {
         throw new IllegalStateException("UpdateManager not started.");
      } else {
         this.updateRunnableQueue.preemptLater(new Runnable() {
            public void run() {
               synchronized(UpdateManager.this) {
                  AbstractEvent var2 = (AbstractEvent)((DocumentEvent)UpdateManager.this.document).createEvent("SVGEvents");
                  String var3;
                  if (UpdateManager.this.bridgeContext.isSVG12()) {
                     var3 = "unload";
                  } else {
                     var3 = "SVGUnload";
                  }

                  var2.initEventNS("http://www.w3.org/2001/xml-events", var3, false, false);
                  ((EventTarget)UpdateManager.this.document.getDocumentElement()).dispatchEvent(var2);
                  UpdateManager.this.running = false;
                  UpdateManager.this.scriptingEnvironment.interrupt();
                  UpdateManager.this.updateRunnableQueue.getThread().halt();
                  UpdateManager.this.bridgeContext.dispose();
                  UpdateManagerEvent var4 = new UpdateManagerEvent(UpdateManager.this, (BufferedImage)null, (List)null);
                  UpdateManager.this.fireEvent(UpdateManager.stoppedDispatcher, var4);
               }
            }
         });
         this.resume();
      }
   }

   public void updateRendering(AffineTransform var1, boolean var2, Shape var3, int var4, int var5) {
      this.repaintManager.setupRenderer(var1, var2, var3, var4, var5);
      ArrayList var6 = new ArrayList(1);
      var6.add(var3);
      this.updateRendering(var6, false);
   }

   public void updateRendering(AffineTransform var1, boolean var2, boolean var3, Shape var4, int var5, int var6) {
      this.repaintManager.setupRenderer(var1, var2, var4, var5, var6);
      ArrayList var7 = new ArrayList(1);
      var7.add(var4);
      this.updateRendering(var7, var3);
   }

   protected void updateRendering(List var1, boolean var2) {
      UpdateManagerEvent var4;
      try {
         UpdateManagerEvent var3 = new UpdateManagerEvent(this, this.repaintManager.getOffScreen(), (List)null);
         this.fireEvent(updateStartedDispatcher, var3);
         Collection var8 = this.repaintManager.updateRendering(var1);
         ArrayList var5 = new ArrayList(var8);
         var3 = new UpdateManagerEvent(this, this.repaintManager.getOffScreen(), var5, var2);
         this.fireEvent(updateCompletedDispatcher, var3);
      } catch (ThreadDeath var6) {
         var4 = new UpdateManagerEvent(this, (BufferedImage)null, (List)null);
         this.fireEvent(updateFailedDispatcher, var4);
         throw var6;
      } catch (Throwable var7) {
         var4 = new UpdateManagerEvent(this, (BufferedImage)null, (List)null);
         this.fireEvent(updateFailedDispatcher, var4);
      }

   }

   protected void repaint() {
      if (!this.updateTracker.hasChanged()) {
         this.outOfDateTime = 0L;
      } else {
         long var1 = System.currentTimeMillis();
         if (var1 < this.allResumeTime) {
            this.createRepaintTimer();
         } else {
            if (this.allResumeTime > 0L) {
               this.releaseAllRedrawSuspension();
            }

            if (var1 - this.outOfDateTime < (long)this.minRepaintTime) {
               synchronized(this.updateRunnableQueue.getIteratorLock()) {
                  Iterator var4 = this.updateRunnableQueue.iterator();

                  while(var4.hasNext()) {
                     if (!(var4.next() instanceof NoRepaintRunnable)) {
                        return;
                     }
                  }
               }
            }

            List var3 = this.updateTracker.getDirtyAreas();
            this.updateTracker.clear();
            if (var3 != null) {
               this.updateRendering(var3, false);
            }

            this.outOfDateTime = 0L;
         }
      }
   }

   public void forceRepaint() {
      if (!this.updateTracker.hasChanged()) {
         this.outOfDateTime = 0L;
      } else {
         List var1 = this.updateTracker.getDirtyAreas();
         this.updateTracker.clear();
         if (var1 != null) {
            this.updateRendering(var1, false);
         }

         this.outOfDateTime = 0L;
      }
   }

   void createRepaintTimer() {
      if (this.repaintTimerTask == null) {
         if (this.allResumeTime >= 0L) {
            if (this.repaintTriggerTimer == null) {
               this.repaintTriggerTimer = new Timer(true);
            }

            long var1 = this.allResumeTime - System.currentTimeMillis();
            if (var1 < 0L) {
               var1 = 0L;
            }

            this.repaintTimerTask = new RepaintTimerTask(this);
            this.repaintTriggerTimer.schedule(this.repaintTimerTask, var1);
         }
      }
   }

   void resetRepaintTimer() {
      if (this.repaintTimerTask != null) {
         if (this.allResumeTime >= 0L) {
            if (this.repaintTriggerTimer == null) {
               this.repaintTriggerTimer = new Timer(true);
            }

            long var1 = this.allResumeTime - System.currentTimeMillis();
            if (var1 < 0L) {
               var1 = 0L;
            }

            this.repaintTimerTask = new RepaintTimerTask(this);
            this.repaintTriggerTimer.schedule(this.repaintTimerTask, var1);
         }
      }
   }

   int addRedrawSuspension(int var1) {
      long var2 = System.currentTimeMillis() + (long)var1;
      SuspensionInfo var4 = new SuspensionInfo(this.nextSuspensionIndex++, var2);
      if (var2 > this.allResumeTime) {
         this.allResumeTime = var2;
         this.resetRepaintTimer();
      }

      this.suspensionList.add(var4);
      return var4.getIndex();
   }

   void releaseAllRedrawSuspension() {
      this.suspensionList.clear();
      this.allResumeTime = -1L;
      this.resetRepaintTimer();
   }

   boolean releaseRedrawSuspension(int var1) {
      if (var1 > this.nextSuspensionIndex) {
         return false;
      } else if (this.suspensionList.size() == 0) {
         return true;
      } else {
         int var2 = 0;
         int var3 = this.suspensionList.size() - 1;

         while(var2 < var3) {
            int var4 = var2 + var3 >> 1;
            SuspensionInfo var5 = (SuspensionInfo)this.suspensionList.get(var4);
            int var6 = var5.getIndex();
            if (var6 == var1) {
               var3 = var4;
               var2 = var4;
            } else if (var6 < var1) {
               var2 = var4 + 1;
            } else {
               var3 = var4 - 1;
            }
         }

         SuspensionInfo var8 = (SuspensionInfo)this.suspensionList.get(var2);
         int var9 = var8.getIndex();
         if (var9 != var1) {
            return true;
         } else {
            this.suspensionList.remove(var2);
            if (this.suspensionList.size() == 0) {
               this.allResumeTime = -1L;
               this.resetRepaintTimer();
            } else {
               long var10 = var8.getResumeMilli();
               if (var10 == this.allResumeTime) {
                  this.allResumeTime = this.findNewAllResumeTime();
                  this.resetRepaintTimer();
               }
            }

            return true;
         }
      }
   }

   long findNewAllResumeTime() {
      long var1 = -1L;
      Iterator var3 = this.suspensionList.iterator();

      while(var3.hasNext()) {
         SuspensionInfo var4 = (SuspensionInfo)var3.next();
         long var5 = var4.getResumeMilli();
         if (var5 > var1) {
            var1 = var5;
         }
      }

      return var1;
   }

   public void addUpdateManagerListener(UpdateManagerListener var1) {
      this.listeners.add(var1);
   }

   public void removeUpdateManagerListener(UpdateManagerListener var1) {
      this.listeners.remove(var1);
   }

   protected void fireEvent(EventDispatcher.Dispatcher var1, Object var2) {
      EventDispatcher.fireEvent(var1, this.listeners, var2, false);
   }

   protected RunnableQueue.RunHandler createRunHandler() {
      return new UpdateManagerRunHander();
   }

   static {
      int var0 = 20;

      try {
         String var1 = System.getProperty("org.apache.batik.min_repaint_time", "20");
         var0 = Integer.parseInt(var1);
      } catch (SecurityException var6) {
      } catch (NumberFormatException var7) {
      } finally {
         MIN_REPAINT_TIME = var0;
      }

      startedDispatcher = new EventDispatcher.Dispatcher() {
         public void dispatch(Object var1, Object var2) {
            ((UpdateManagerListener)var1).managerStarted((UpdateManagerEvent)var2);
         }
      };
      stoppedDispatcher = new EventDispatcher.Dispatcher() {
         public void dispatch(Object var1, Object var2) {
            ((UpdateManagerListener)var1).managerStopped((UpdateManagerEvent)var2);
         }
      };
      suspendedDispatcher = new EventDispatcher.Dispatcher() {
         public void dispatch(Object var1, Object var2) {
            ((UpdateManagerListener)var1).managerSuspended((UpdateManagerEvent)var2);
         }
      };
      resumedDispatcher = new EventDispatcher.Dispatcher() {
         public void dispatch(Object var1, Object var2) {
            ((UpdateManagerListener)var1).managerResumed((UpdateManagerEvent)var2);
         }
      };
      updateStartedDispatcher = new EventDispatcher.Dispatcher() {
         public void dispatch(Object var1, Object var2) {
            ((UpdateManagerListener)var1).updateStarted((UpdateManagerEvent)var2);
         }
      };
      updateCompletedDispatcher = new EventDispatcher.Dispatcher() {
         public void dispatch(Object var1, Object var2) {
            ((UpdateManagerListener)var1).updateCompleted((UpdateManagerEvent)var2);
         }
      };
      updateFailedDispatcher = new EventDispatcher.Dispatcher() {
         public void dispatch(Object var1, Object var2) {
            ((UpdateManagerListener)var1).updateFailed((UpdateManagerEvent)var2);
         }
      };
   }

   protected class UpdateManagerRunHander extends RunnableQueue.RunHandlerAdapter {
      public void runnableStart(RunnableQueue var1, Runnable var2) {
         if (UpdateManager.this.running && !(var2 instanceof NoRepaintRunnable) && UpdateManager.this.outOfDateTime == 0L) {
            UpdateManager.this.outOfDateTime = System.currentTimeMillis();
         }

      }

      public void runnableInvoked(RunnableQueue var1, Runnable var2) {
         if (UpdateManager.this.running && !(var2 instanceof NoRepaintRunnable)) {
            UpdateManager.this.repaint();
         }

      }

      public void executionSuspended(RunnableQueue var1) {
         synchronized(UpdateManager.this) {
            if (UpdateManager.this.suspendCalled) {
               UpdateManager.this.running = false;
               UpdateManagerEvent var3 = new UpdateManagerEvent(this, (BufferedImage)null, (List)null);
               UpdateManager.this.fireEvent(UpdateManager.suspendedDispatcher, var3);
            }

         }
      }

      public void executionResumed(RunnableQueue var1) {
         synchronized(UpdateManager.this) {
            if (UpdateManager.this.suspendCalled && !UpdateManager.this.running) {
               UpdateManager.this.running = true;
               UpdateManager.this.suspendCalled = false;
               UpdateManagerEvent var3 = new UpdateManagerEvent(this, (BufferedImage)null, (List)null);
               UpdateManager.this.fireEvent(UpdateManager.resumedDispatcher, var3);
            }

         }
      }
   }

   protected class RepaintTimerTask extends TimerTask {
      UpdateManager um;

      RepaintTimerTask(UpdateManager var2) {
         this.um = var2;
      }

      public void run() {
         RunnableQueue var1 = this.um.getUpdateRunnableQueue();
         if (var1 != null) {
            var1.invokeLater(new Runnable() {
               public void run() {
               }
            });
         }
      }
   }

   protected class SuspensionInfo {
      int index;
      long resumeMilli;

      public SuspensionInfo(int var2, long var3) {
         this.index = var2;
         this.resumeMilli = var3;
      }

      public int getIndex() {
         return this.index;
      }

      public long getResumeMilli() {
         return this.resumeMilli;
      }
   }
}
