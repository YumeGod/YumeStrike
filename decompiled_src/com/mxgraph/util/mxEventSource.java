package com.mxgraph.util;

import java.util.ArrayList;
import java.util.List;

public class mxEventSource {
   protected transient List eventListeners;
   protected Object eventSource;
   protected boolean eventsEnabled;

   public mxEventSource() {
      this((Object)null);
   }

   public mxEventSource(Object var1) {
      this.eventListeners = null;
      this.eventsEnabled = true;
      this.setEventSource(var1);
   }

   public Object getEventSource() {
      return this.eventSource;
   }

   public void setEventSource(Object var1) {
      this.eventSource = var1;
   }

   public boolean isEventsEnabled() {
      return this.eventsEnabled;
   }

   public void setEventsEnabled(boolean var1) {
      this.eventsEnabled = var1;
   }

   public void addListener(String var1, mxIEventListener var2) {
      if (this.eventListeners == null) {
         this.eventListeners = new ArrayList();
      }

      this.eventListeners.add(var1);
      this.eventListeners.add(var2);
   }

   public void removeListener(mxIEventListener var1) {
      this.removeListener(var1, (String)null);
   }

   public void removeListener(mxIEventListener var1, String var2) {
      if (this.eventListeners != null) {
         for(int var3 = this.eventListeners.size() - 2; var3 > 1; var3 -= 2) {
            if (this.eventListeners.get(var3 + 1) == var1 && (var2 == null || String.valueOf(this.eventListeners.get(var3)).equals(var2))) {
               this.eventListeners.remove(var3 + 1);
               this.eventListeners.remove(var3);
            }
         }
      }

   }

   public void fireEvent(mxEventObject var1) {
      this.fireEvent(var1, (Object)null);
   }

   public void fireEvent(mxEventObject var1, Object var2) {
      if (this.eventListeners != null && !this.eventListeners.isEmpty() && this.isEventsEnabled()) {
         if (var2 == null) {
            var2 = this.getEventSource();
         }

         if (var2 == null) {
            var2 = this;
         }

         for(int var3 = 0; var3 < this.eventListeners.size(); var3 += 2) {
            String var4 = (String)this.eventListeners.get(var3);
            if (var4 == null || var4.equals(var1.getName())) {
               ((mxIEventListener)this.eventListeners.get(var3 + 1)).invoke(var2, var1);
            }
         }
      }

   }

   public interface mxIEventListener {
      void invoke(Object var1, mxEventObject var2);
   }
}
