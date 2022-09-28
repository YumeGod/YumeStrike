package server;

import common.CommonUtils;
import common.MudgeSanity;
import common.Reply;
import common.Request;
import java.util.LinkedList;
import java.util.Map;

public class ServerBus implements Runnable {
   protected LinkedList requests = new LinkedList();
   protected Map calls;

   protected ServerRequest grabRequest() {
      synchronized(this) {
         return (ServerRequest)this.requests.pollFirst();
      }
   }

   protected void addRequest(ManageUser var1, Request var2) {
      synchronized(this) {
         while(this.requests.size() > 100000) {
            this.requests.removeFirst();
         }

         this.requests.add(new ServerRequest(var1, var2));
      }
   }

   public ServerBus(Map var1) {
      this.calls = var1;
      (new Thread(this, "server call bus")).start();
   }

   public void run() {
      try {
         while(true) {
            ServerRequest var1 = this.grabRequest();
            if (var1 != null) {
               Request var2 = var1.request;
               if (this.calls.containsKey(var2.getCall())) {
                  ServerHook var3 = (ServerHook)this.calls.get(var2.getCall());
                  var3.call(var2, var1.client);
               } else if (var1.client != null) {
                  var1.client.write(new Reply("server_error", 0L, var2 + ": unknown call [or bad arguments]"));
               } else {
                  CommonUtils.print_error("server_error " + var1 + ": unknown call " + var2.getCall() + " [or bad arguments]");
               }

               Thread.yield();
            } else {
               Thread.sleep(25L);
            }
         }
      } catch (Exception var4) {
         MudgeSanity.logException("server call bus loop", var4, false);
      }
   }

   private static class ServerRequest {
      public ManageUser client;
      public Request request;

      public ServerRequest(ManageUser var1, Request var2) {
         this.client = var1;
         this.request = var2;
      }
   }
}
