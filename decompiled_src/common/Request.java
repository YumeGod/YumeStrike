package common;

import java.io.Serializable;

public class Request implements Serializable {
   protected String call;
   protected Object[] args;
   protected long callback_ref;

   public Request(String var1, Object[] var2, long var3) {
      this.call = var1;
      this.args = var2;
      this.callback_ref = var3;
   }

   public Reply reply(Object var1) {
      Reply var2 = new Reply(this.call, this.callback_ref, var1);
      return var2;
   }

   public Request derive(String var1, Object[] var2) {
      return new Request(var1, var2, this.callback_ref);
   }

   public Request derive(String var1) {
      return new Request(var1, this.args, this.callback_ref);
   }

   public String getCall() {
      return this.call;
   }

   public boolean is(String var1) {
      return this.call.equals(var1);
   }

   public boolean is(String var1, int var2) {
      return this.getCall().equals(var1) && this.size() == var2;
   }

   public Object[] getArgs() {
      return this.args;
   }

   public Object arg(int var1) {
      return this.args[var1];
   }

   public String argz(int var1) {
      return (String)this.arg(var1);
   }

   public int size() {
      return this.args == null ? 0 : this.args.length;
   }

   public String toString() {
      return "Request '" + this.getCall() + "' with " + this.size() + " args";
   }
}
