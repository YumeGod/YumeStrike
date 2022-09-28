package common;

import java.io.Serializable;

public class PlaybackStatus implements Serializable {
   protected String message;
   protected int total = 1;
   protected int sent = 0;

   public PlaybackStatus copy() {
      PlaybackStatus var1 = new PlaybackStatus(this.message, this.total);
      var1.sent = this.sent;
      return var1;
   }

   public PlaybackStatus(String var1, int var2) {
      this.total = var2;
      this.message = var1;
   }

   public String getMessage() {
      return this.message;
   }

   public void message(String var1) {
      this.message = var1;
   }

   public void more(int var1) {
      this.total += var1;
   }

   public int getSent() {
      return this.sent;
   }

   public int getTotal() {
      return this.total;
   }

   public void sent() {
      ++this.sent;
   }

   public int percentage() {
      return (int)((double)this.sent / (double)this.total * 100.0);
   }

   public boolean isDone() {
      return this.sent == this.total;
   }

   public boolean isStart() {
      return this.sent == 0;
   }
}
