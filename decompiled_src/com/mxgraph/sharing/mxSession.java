package com.mxgraph.sharing;

import com.mxgraph.util.mxUtils;

public class mxSession implements mxSharedDiagram.mxDiagramChangeListener {
   public static int DEFAULT_TIMEOUT = 10000;
   protected String id;
   protected mxSharedDiagram diagram;
   protected StringBuffer buffer = new StringBuffer();
   protected long lastTimeMillis = 0L;

   public mxSession(String var1, mxSharedDiagram var2) {
      this.id = var1;
      this.diagram = var2;
      this.diagram.addDiagramChangeListener(this);
      this.lastTimeMillis = System.currentTimeMillis();
   }

   public String getId() {
      return this.id;
   }

   public String getInitialState() {
      String var1 = mxUtils.getMd5Hash(this.id);
      StringBuffer var2 = new StringBuffer("<state session-id=\"" + this.id + "\" namespace=\"" + var1 + "\">");
      var2.append(this.diagram.getInitialState());
      var2.append("<delta>");
      var2.append(this.diagram.getDelta());
      var2.append("</delta>");
      var2.append("</state>");
      return var2.toString();
   }

   public synchronized String init() {
      synchronized(this) {
         this.buffer = new StringBuffer();
         this.notify();
      }

      return this.getInitialState();
   }

   public void post(String var1) {
      this.diagram.dispatch(this, var1);
   }

   public String poll() throws InterruptedException {
      return this.poll((long)DEFAULT_TIMEOUT);
   }

   public String poll(long var1) throws InterruptedException {
      this.lastTimeMillis = System.currentTimeMillis();
      String var3 = "<delta/>";
      synchronized(this) {
         if (this.buffer.length() == 0) {
            this.wait(var1);
         }

         if (this.buffer.length() > 0) {
            var3 = "<delta>" + this.buffer.toString() + "</delta>";
            this.buffer = new StringBuffer();
         }

         this.notify();
         return var3;
      }
   }

   public long inactiveTimeMillis() {
      return System.currentTimeMillis() - this.lastTimeMillis;
   }

   public synchronized void diagramChanged(Object var1, String var2) {
      if (var1 != this) {
         synchronized(this) {
            this.buffer.append(var2);
            this.notify();
         }
      }

   }

   public void destroy() {
      this.diagram.removeDiagramChangeListener(this);
   }
}
