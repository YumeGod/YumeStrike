package com.mxgraph.sharing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class mxSharedDiagram {
   protected List diagramChangeListeners;
   protected String initialState;
   protected StringBuffer history = new StringBuffer();

   public mxSharedDiagram(String var1) {
      this.initialState = var1;
   }

   public String getInitialState() {
      return this.initialState;
   }

   public synchronized void clearHistory() {
      this.history = new StringBuffer();
   }

   public synchronized String getDelta() {
      return this.history.toString();
   }

   public void dispatch(Object var1, String var2) {
      synchronized(this) {
         this.history.append(var2);
      }

      this.dispatchDiagramChangeEvent(var1, var2);
   }

   public void addDiagramChangeListener(mxDiagramChangeListener var1) {
      if (this.diagramChangeListeners == null) {
         this.diagramChangeListeners = new ArrayList();
      }

      this.diagramChangeListeners.add(var1);
   }

   public void removeDiagramChangeListener(mxDiagramChangeListener var1) {
      if (this.diagramChangeListeners != null) {
         this.diagramChangeListeners.remove(var1);
      }

   }

   void dispatchDiagramChangeEvent(Object var1, String var2) {
      if (this.diagramChangeListeners != null) {
         Iterator var3 = this.diagramChangeListeners.iterator();

         while(var3.hasNext()) {
            mxDiagramChangeListener var4 = (mxDiagramChangeListener)var3.next();
            var4.diagramChanged(var1, var2);
         }
      }

   }

   public interface mxDiagramChangeListener {
      void diagramChanged(Object var1, String var2);
   }
}
