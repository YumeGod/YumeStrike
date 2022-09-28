package pe;

import common.CommonUtils;
import java.io.File;

public class PostExObfuscator {
   protected PEEditor editor = null;

   public static byte[] setupSmartInject(byte[] var0) {
      var0[1023] = -12;
      var0[1022] = -12;
      var0[1021] = -12;
      var0[1020] = -12;
      return var0;
   }

   public void enableEvasions() {
      this.maskSection(".data");
      this.maskSection(".rdata");
      this.editor.setCharacteristic(2048, true);
      this.fixMZCheck();
   }

   public void fixMZCheck() {
      byte[] var1;
      byte[] var2;
      if (this.editor.getInfo().is64()) {
         var1 = CommonUtils.Bytes("b9 4d 5a 00 00 66 39 08 74 03 33 c0");
         var2 = CommonUtils.Bytes("b9 4d 5a 00 00 90 90 90 90 90");
         if (this.editor.patchCode(var1, var2)) {
            return;
         }

         CommonUtils.print_error("Could not find x64 failure sequence in " + this.editor.getInfo().getDLLName() + " (job will crash)");
      } else {
         var1 = CommonUtils.Bytes("b8 4d 5a 00 00 66 39 01 74 04 33 c0");
         var2 = CommonUtils.Bytes("b8 4d 5a 00 00 90 90 90 90 90");
         if (this.editor.patchCode(var1, var2)) {
            return;
         }

         var1 = CommonUtils.Bytes("b9 4d 5a 00 00 66 39 08 74 04 33 c0");
         var2 = CommonUtils.Bytes("b9 4d 5a 00 00 90 90 90 90 90");
         if (this.editor.patchCode(var1, var2)) {
            return;
         }

         CommonUtils.print_error("Could not find x86 failure sequence in " + this.editor.getInfo().getDLLName() + " (job will crash)");
      }

   }

   public void deleteExportName() {
      int var1 = this.editor.getInfo().getLocation("Export.Name");
      int var2 = this.editor.getInt(var1);
      this.editor.stomp(var2);
   }

   public void deleteExportedFunctionNames() {
      int var1 = this.editor.getInfo().get("Export.NumberOfNames");

      for(int var2 = 0; var2 < var1; ++var2) {
         int var3 = this.editor.getInfo().getLocation("Export.AddressOfName." + var2);
         this.editor.stomp(var3);
      }

   }

   public void deleteSectionNames() {
      int var1 = this.editor.getInfo().get("Sections");

      for(int var2 = 0; var2 < var1; ++var2) {
         int var3 = this.editor.getInfo().getLocation("Sections.AddressOfName." + var2);
         this.editor.stomp(var3);
      }

   }

   public void maskSection(String var1) {
      if (!this.editor.getInfo().hasSection(var1)) {
         CommonUtils.print_stat("Will not mask '" + var1 + "'");
      } else {
         int var2 = this.editor.getInfo().get(var1 + ".PointerToRawData");
         int var3 = this.editor.getInfo().get(var1 + ".SizeOfRawData");
         byte var4 = -50;
         this.editor.mask(var2, var3, var4);
         this.editor.setShort(this.editor.getInfo().getLocation(var1 + ".NumberOfRelocations"), (long)var4);
      }
   }

   public byte[] getImage() {
      return this.editor.getImage();
   }

   public void process(byte[] var1) {
      this.editor = new PEEditor(var1);
      this.editor.checkAssertions();
      this.editor.obfuscatePEHeader();
      this.deleteExportName();
      this.deleteExportedFunctionNames();
      this.deleteSectionNames();
   }

   public static void main(String[] var0) {
      byte[] var1 = CommonUtils.readFile(var0[0]);
      PostExObfuscator var2 = new PostExObfuscator();
      var2.process(var1);
      var1 = var2.getImage();
      CommonUtils.writeToFile(new File("out.bin"), var1);
      PEParser var3 = PEParser.load(var1);
      System.out.println(var3.toString());
      var3.stringWalk();
   }
}
