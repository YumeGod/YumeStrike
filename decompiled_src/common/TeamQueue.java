package common;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class TeamQueue {
   protected TeamSocket socket;
   protected Map callbacks = new HashMap();
   protected long reqno = 0L;
   protected TeamReader reader = null;
   protected TeamWriter writer = null;
   protected Callback subscriber = null;

   public TeamQueue(TeamSocket var1) {
      this.socket = var1;
      this.reader = new TeamReader();
      this.writer = new TeamWriter();
      (new Thread(this.writer, "TeamQueue Writer")).start();
      (new Thread(this.reader, "TeamQueue Reader")).start();
   }

   public void call(String var1, Callback var2) {
      this.call(var1, (Object[])null, var2);
   }

   public void call(String var1) {
      this.call(var1, (Object[])null, (Callback)null);
   }

   public void call(String var1, Object[] var2) {
      this.call(var1, var2, (Callback)null);
   }

   public void call(String var1, Object[] var2, Callback var3) {
      if (var3 == null) {
         Request var4 = new Request(var1, var2, 0L);
         this.writer.addRequest(var4);
      } else {
         synchronized(this.callbacks) {
            ++this.reqno;
            this.callbacks.put(new Long(this.reqno), var3);
            Request var5 = new Request(var1, var2, this.reqno);
            this.writer.addRequest(var5);
         }
      }

   }

   public boolean isConnected() {
      return this.socket.isConnected();
   }

   public void close() {
      this.socket.close();
   }

   public void addDisconnectListener(DisconnectListener var1) {
      this.socket.addDisconnectListener(var1);
   }

   public void setSubscriber(Callback var1) {
      synchronized(this) {
         this.subscriber = var1;
      }
   }

   protected void processRead(Reply var1) {
      Callback var2 = null;
      if (var1.hasCallback()) {
         synchronized(this.callbacks) {
            var2 = (Callback)this.callbacks.get(var1.getCallbackReference());
            this.callbacks.remove(var1.getCallbackReference());
         }

         if (var2 != null) {
            var2.result(var1.getCall(), var1.getContent());
         }
      } else {
         synchronized(this) {
            if (this.subscriber != null) {
               this.subscriber.result(var1.getCall(), var1.getContent());
            }
         }
      }

   }

   private class TeamWriter implements Runnable {
      protected LinkedList requests = new LinkedList();

      protected Request grabRequest() {
         synchronized(this) {
            return (Request)this.requests.pollFirst();
         }
      }

      protected void addRequest(Request var1) {
         synchronized(this) {
            if (var1.size() > 100000) {
               this.requests.removeFirst();
            }

            this.requests.add(var1);
         }
      }

      public TeamWriter() {
      }

      public void run() {
         while(TeamQueue.this.socket.isConnected()) {
            Request var1 = this.grabRequest();
            if (var1 != null) {
               TeamQueue.this.socket.writeObject(var1);
               Thread.yield();
            } else {
               try {
                  Thread.sleep(25L);
               } catch (InterruptedException var3) {
                  MudgeSanity.logException("teamwriter sleep", var3, false);
               }
            }
         }

      }
   }

   private class TeamReader implements Runnable {
      public TeamReader() {
      }

      public void run() {
         while(true) {
            try {
               if (TeamQueue.this.socket.isConnected()) {
                  Reply var1 = (Reply)TeamQueue.this.socket.readObject();
                  TeamQueue.this.processRead(var1);
                  Thread.yield();
                  continue;
               }
            } catch (Exception var2) {
               MudgeSanity.logException("team reader", var2, false);
               TeamQueue.this.close();
            }

            return;
         }
      }
   }
}
