package sleep.bridges;

public class Semaphore {
   private long count;

   public Semaphore(long var1) {
      this.count = var1;
   }

   public void P() {
      synchronized(this) {
         try {
            while(this.count <= 0L) {
               this.wait();
            }

            --this.count;
         } catch (InterruptedException var4) {
            var4.printStackTrace();
            this.notifyAll();
         }

      }
   }

   public long getCount() {
      return this.count;
   }

   public void V() {
      synchronized(this) {
         ++this.count;
         this.notifyAll();
      }
   }

   public String toString() {
      return "[Semaphore: " + this.count + "]";
   }
}
