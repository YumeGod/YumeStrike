package server;

import common.Request;

public class PendingRequest {
   protected Request request;
   protected ManageUser client;

   public PendingRequest(Request var1, ManageUser var2) {
      this.request = var1;
      this.client = var2;
   }

   public void action(String var1) {
      this.client.write(this.request.reply(var1));
   }
}
