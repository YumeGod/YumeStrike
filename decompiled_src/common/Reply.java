package common;

import java.io.Serializable;

public class Reply implements Serializable {
   protected String call;
   protected Object reply;
   protected long callback_ref;

   public Reply(String var1, long var2, Object var4) {
      this.call = var1;
      this.reply = var4;
      this.callback_ref = var2;
   }

   public String getCall() {
      return this.call;
   }

   public Object getCallbackReference() {
      return new Long(this.callback_ref);
   }

   public Object getContent() {
      return this.reply;
   }

   public boolean hasCallback() {
      return this.callback_ref != 0L;
   }

   public String toString() {
      return "Reply '" + this.getCall() + "': " + this.getContent();
   }
}
