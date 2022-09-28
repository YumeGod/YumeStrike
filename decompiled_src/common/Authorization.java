package common;

import java.io.File;

public class Authorization {
   protected int watermark = 0;
   protected String validto = "";
   protected String error = null;
   protected boolean valid = false;

   public Authorization() {
      String var1 = CommonUtils.canonicalize("cobaltstrike.auth");
      if (!(new File(var1)).exists()) {
         try {
            File var2 = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
            if (var2.getName().toLowerCase().endsWith(".jar")) {
               var2 = var2.getParentFile();
            }

            var1 = (new File(var2, "cobaltstrike.auth")).getAbsolutePath();
         } catch (Exception var11) {
            MudgeSanity.logException("trouble locating auth file", var11, false);
         }
      }

      byte[] var12 = CommonUtils.readFile(var1);
      if (var12.length == 0) {
         this.error = "Could not read " + var1;
      } else {
         AuthCrypto var3 = new AuthCrypto();
         byte[] var4 = var3.decrypt(var12);
         if (var4.length == 0) {
            this.error = var3.error();
         } else {
            try {
               DataParser var5 = new DataParser(var4);
               var5.big();
               int var6 = var5.readInt();
               this.watermark = var5.readInt();
               byte var7 = var5.readByte();
               byte var8 = var5.readByte();
               byte[] var9 = var5.readBytes(var8);
               if (var7 < 40) {
                  this.error = "Authorization file is not for Cobalt Strike 4.0+";
                  return;
               }

               if (29999999 == var6) {
                  this.validto = "forever";
                  MudgeSanity.systemDetail("valid to", "perpetual");
               } else {
                  this.validto = "20" + var6;
                  CommonUtils.print_stat("Valid to is: '" + this.validto + "'");
                  MudgeSanity.systemDetail("valid to", CommonUtils.formatDateAny("MMMMM d, YYYY", this.getExpirationDate()));
               }

               this.valid = true;
               MudgeSanity.systemDetail("id", this.watermark + "");
               SleevedResource.Setup(var9);
            } catch (Exception var10) {
               MudgeSanity.logException("auth file parsing", var10, false);
            }

         }
      }
   }

   public boolean isPerpetual() {
      return "forever".equals(this.validto);
   }

   public boolean isValid() {
      return this.valid;
   }

   public String getError() {
      return this.error;
   }

   public String getWatermark() {
      return this.watermark + "";
   }

   public long getExpirationDate() {
      return CommonUtils.parseDate(this.validto, "yyyyMMdd");
   }

   public boolean isExpired() {
      return System.currentTimeMillis() > this.getExpirationDate() + CommonUtils.days(1);
   }

   public String whenExpires() {
      long var1 = (this.getExpirationDate() + CommonUtils.days(1) - System.currentTimeMillis()) / CommonUtils.days(1);
      if (var1 == 1L) {
         return "1 day (" + CommonUtils.formatDateAny("MMMMM d, YYYY", this.getExpirationDate()) + ")";
      } else {
         return var1 <= 0L ? "TODAY (" + CommonUtils.formatDateAny("MMMMM d, YYYY", this.getExpirationDate()) + ")" : var1 + " days (" + CommonUtils.formatDateAny("MMMMM d, YYYY", this.getExpirationDate()) + ")";
      }
   }

   public boolean isAlmostExpired() {
      long var1 = System.currentTimeMillis() + CommonUtils.days(30);
      return var1 > this.getExpirationDate();
   }
}
