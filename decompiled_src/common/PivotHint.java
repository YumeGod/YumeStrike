package common;

public class PivotHint {
   public static final long HINT_REVERSE = 65536L;
   public static final long HINT_FORWARD = 0L;
   public static final long HINT_PROTO_PIPE = 0L;
   public static final long HINT_PROTO_TCP = 1048576L;
   protected int hint;

   public PivotHint(int var1) {
      this.hint = var1;
   }

   public PivotHint(String var1) {
      this.hint = CommonUtils.toNumber(var1, 0);
   }

   public int getPort() {
      return this.hint & '\uffff';
   }

   public boolean isReverse() {
      return ((long)this.hint & 65536L) == 65536L;
   }

   public boolean isForward() {
      return !this.isReverse();
   }

   public boolean isTCP() {
      return ((long)this.hint & 1048576L) == 1048576L;
   }

   public String getProtocol() {
      return this.isTCP() ? "TCP" : "SMB";
   }

   public String toString() {
      return this.isForward() ? this.getPort() + ", " + this.getProtocol() + " (FWD)" : this.getPort() + ", " + this.getProtocol() + " (RVR)";
   }
}
