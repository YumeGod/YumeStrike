package common;

public class AuthUtil {
   protected int watermark = 0;
   protected String licensekey = "";
   protected String validto = "";
   protected String error = null;
   protected boolean valid = false;

   public AuthUtil(String var1) {
      byte[] var3 = CommonUtils.readFile(var1);
      if (var3.length == 0) {
         CommonUtils.print_error("Could not read " + var1);
      } else {
         CommonUtils.print_stat("Read: " + var1);
         AuthCrypto var4 = new AuthCrypto();
         byte[] var5 = var4.decrypt(var3);
         if (var5.length == 0) {
            CommonUtils.print_error(var4.error());
         } else {
            String[] var6 = CommonUtils.toArray(CommonUtils.bString(var5));
            if (var6.length < 4) {
               CommonUtils.print_error("auth content is only " + var6.length + " items");
            } else {
               this.licensekey = var6[0];
               if ("forever".equals(var6[1])) {
                  this.validto = var6[1];
                  CommonUtils.print_info("valid to: perpetual");
               } else {
                  this.validto = "20" + var6[1];
                  CommonUtils.print_info("valid to: " + CommonUtils.formatDateAny("MMMMM d, YYYY", this.getExpirationDate()));
               }

               this.watermark = CommonUtils.toNumber(var6[2], 0);
               CommonUtils.print_info("id " + this.watermark + "");
            }
         }
      }
   }

   public long getExpirationDate() {
      return CommonUtils.parseDate(this.validto, "yyyyMMdd");
   }

   public static void main(String[] var0) {
      new AuthUtil(var0[0]);
   }
}
