package beacon.setup;

import beacon.Settings;
import c2profile.Profile;
import common.CommonUtils;
import common.Packer;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ProcessInject {
   protected Profile c2profile;
   protected String allocator;
   protected boolean userwx;
   protected boolean startrwx;
   protected int min_alloc;
   protected byte[] prepended_x86;
   protected byte[] appended_x86;
   protected byte[] prepended_x64;
   protected byte[] appended_x64;
   protected List errors = new LinkedList();
   protected List warnings = new LinkedList();
   protected String defaultl = "CreateThread, SetThreadContext, CreateRemoteThread, RtlCreateUserThread";

   public ProcessInject(Profile var1) {
      this.c2profile = var1;
      this.parse();
   }

   protected void setupProcessInjectTransform(Settings var1, int var2, byte[] var3, byte[] var4) {
      Packer var5 = new Packer();
      var5.big();
      var5.addInt(var3.length);
      var5.append(var3);
      var5.addInt(var4.length);
      var5.append(var4);
      var1.addData(var2, var5.getBytes(), 256);
   }

   protected List getExecuteList() {
      List var1 = this.c2profile.getList(".process-inject.execute");
      return var1.size() == 0 ? CommonUtils.toList(this.defaultl) : var1;
   }

   public void checkExecuteList() {
      HashSet var1 = new HashSet();
      Iterator var2 = this.getExecuteList().iterator();

      while(true) {
         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            if (!"CreateThread".equals(var3) && !var3.startsWith("CreateThread ")) {
               if ("SetThreadContext".equals(var3)) {
                  var1.add("x64 -> x86 (suspended)");
                  var1.add("x64 -> x64 (suspended)");
                  var1.add("x86 -> x86 (suspended)");
               } else if ("CreateRemoteThread".equals(var3)) {
                  var1.add("x86 -> x86");
                  var1.add("x64 -> x86");
                  var1.add("x64 -> x64");
                  var1.add("self-inject");
                  var1.add("x64 -> x86 (suspended)");
                  var1.add("x64 -> x64 (suspended)");
                  var1.add("x86 -> x86 (suspended)");
               } else if ("RtlCreateUserThread".equals(var3)) {
                  var1.add("x64 -> x86");
                  var1.add("x64 -> x64");
                  var1.add("x86 -> x64");
                  var1.add("x86 -> x86");
                  var1.add("x64 -> x86 (cross session)");
                  var1.add("x64 -> x64 (cross session)");
                  var1.add("x86 -> x64 (cross session)");
                  var1.add("x86 -> x86 (cross session)");
                  var1.add("x64 -> x86 (suspended)");
                  var1.add("x64 -> x64 (suspended)");
                  var1.add("x86 -> x64 (suspended)");
                  var1.add("x86 -> x86 (suspended)");
                  var1.add("self-inject");
               } else if ("NtQueueApcThread".equals(var3)) {
                  var1.add("x86 -> x86");
                  var1.add("x86 -> x86 (cross session)");
                  var1.add("x64 -> x64");
                  var1.add("x64 -> x64 (cross session)");
               } else if (var3.startsWith("CreateRemoteThread ")) {
                  var1.add("x86 -> x86");
                  var1.add("x64 -> x64");
                  var1.add("self-inject");
                  var1.add("x64 -> x64 (suspended)");
                  var1.add("x86 -> x86 (suspended)");
                  if (!CommonUtils.isin(" ntdll", var3.toLowerCase()) && !CommonUtils.isin(" kernel32", var3.toLowerCase())) {
                     this.warnings.add(".process-injext.execute " + var3 + " should reference an ntdll or kernel32 function.");
                  }
               } else if ("NtQueueApcThread-s".equals(var3)) {
                  var1.add("x64 -> x64 (suspended)");
                  var1.add("x86 -> x86 (suspended)");
               }
            } else {
               var1.add("self-inject");
            }
         }

         if (!var1.contains("self-inject")) {
            this.warnings.add(".process-inject.execute injection into current process will fail.");
         }

         if (!var1.contains("x86 -> x86")) {
            this.warnings.add(".process-inject.execute x86 -> x86 injection will fail.");
         } else if (!var1.contains("x86 -> x86 (cross session)")) {
            this.warnings.add(".process-inject.execute x86 -> x86 (cross session) injection will fail.");
         }

         if (!var1.contains("x86 -> x64")) {
            this.warnings.add(".process-inject.execute x86 -> x64 injection will fail.");
         } else if (!var1.contains("x86 -> x64 (cross session)")) {
            this.warnings.add(".process-inject.execute x86 -> x64 (cross session) injection will fail.");
         }

         if (!var1.contains("x64 -> x86")) {
            this.warnings.add(".process-inject.execute x64 -> x86 injection will fail.");
         } else if (!var1.contains("x64 -> x86 (cross session)")) {
            this.warnings.add(".process-inject.execute x64 -> x86 (cross session) injection will fail.");
         }

         if (!var1.contains("x64 -> x64")) {
            this.warnings.add(".process-inject.execute x64 -> x64 injection will fail.");
         } else if (!var1.contains("x64 -> x64 (cross session)")) {
            this.warnings.add(".process-inject.execute x64 -> x64 (cross session) injection will fail.");
         }

         if (!var1.contains("x86 -> x86 (suspended)")) {
            this.warnings.add(".process-inject.execute x86 -> x86 (suspended) injection will fail. This affects most post-ex features.");
         }

         if (!var1.contains("x64 -> x64 (suspended)")) {
            this.warnings.add(".process-inject.execute x64 -> x64 (suspended) injection will fail. This affects most post-ex features.");
         }

         if (!var1.contains("x64 -> x86 (suspended)")) {
            this.warnings.add(".process-inject.execute x64 -> x86 (suspended) injection will fail. This affects some post-ex features.");
         }

         if (!var1.contains("x86 -> x64 (suspended)")) {
            this.warnings.add(".process-inject.execute x86 -> x64 (suspended) injection will fail. This affects some post-ex features.");
         }

         byte[] var4 = this.getExecuteValue();
         if (var4.length > 128) {
            this.errors.add(".process-inject.execute block is " + var4.length + " bytes. Reduce its size to <128b or Beacon will crash.");
         }

         return;
      }
   }

   protected void special(int var1, String var2, Packer var3) {
      String[] var4 = var2.split(" ");
      String var5 = var4[1];
      String var6 = var4[2];
      String var7 = var4[3];
      var3.addByte(var1);
      var3.addShort(CommonUtils.toNumber(var7, 0));
      var3.addLengthAndStringASCIIZ(var5);
      var3.addLengthAndStringASCIIZ(var6);
   }

   protected void setupExecuteList(Settings var1) {
      var1.addData(51, this.getExecuteValue(), 128);
   }

   protected byte[] getExecuteValue() {
      Packer var1 = new Packer();
      Iterator var2 = this.getExecuteList().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         if ("CreateThread".equals(var3)) {
            var1.addByte(1);
         } else if ("SetThreadContext".equals(var3)) {
            var1.addByte(2);
         } else if ("CreateRemoteThread".equals(var3)) {
            var1.addByte(3);
         } else if ("RtlCreateUserThread".equals(var3)) {
            var1.addByte(4);
         } else if ("NtQueueApcThread".equals(var3)) {
            var1.addByte(5);
         } else if (var3.startsWith("CreateThread ")) {
            this.special(6, var3, var1);
         } else if (var3.startsWith("CreateRemoteThread ")) {
            this.special(7, var3, var1);
         } else if ("NtQueueApcThread-s".equals(var3)) {
            var1.addByte(8);
         } else {
            CommonUtils.print_error(".process-inject unknown function \"" + var3 + "\"");
         }
      }

      var1.addByte(0);
      return var1.getBytes();
   }

   public List getErrors() {
      return this.errors;
   }

   public List getWarnings() {
      return this.warnings;
   }

   public ProcessInject check() {
      boolean var1 = this.c2profile.option(".process-inject.CreateRemoteThread");
      boolean var2 = this.c2profile.option(".process-inject.SetThreadContext");
      boolean var3 = this.c2profile.option(".process-inject.RtlCreateUserThread");
      if (!var1) {
         this.errors.add(".process-inject disable \"CreateRemoteThread\" is deprecated and has no effect. Use process-inject -> execute instead.");
      }

      if (!var3) {
         this.errors.add(".process-inject disable \"RtlCreateUserThread\" is deprecated and has no effect. Use process-inject -> execute instead.");
      }

      if (!var2) {
         this.errors.add(".process-inject disable \"SetTreadContext\" is deprecated and has no effect. Use process-inject -> execute instead.");
      }

      this.checkExecuteList();
      return this;
   }

   public void parse() {
      this.userwx = this.c2profile.option(".process-inject.userwx");
      this.startrwx = this.c2profile.option(".process-inject.startrwx");
      this.min_alloc = this.c2profile.getInt(".process-inject.min_alloc");
      this.prepended_x86 = this.c2profile.getPrependedData(".process-inject.transform-x86");
      this.appended_x86 = this.c2profile.getAppendedData(".process-inject.transform-x86");
      this.prepended_x64 = this.c2profile.getPrependedData(".process-inject.transform-x64");
      this.appended_x64 = this.c2profile.getAppendedData(".process-inject.transform-x64");
      this.allocator = this.c2profile.getString(".process-inject.allocator");
   }

   public ProcessInject apply(Settings var1) {
      var1.addShort(43, this.startrwx ? 64 : 4);
      var1.addShort(44, this.userwx ? 64 : 32);
      var1.addInt(45, this.min_alloc);
      this.setupProcessInjectTransform(var1, 46, this.prepended_x86, this.appended_x86);
      this.setupProcessInjectTransform(var1, 47, this.prepended_x64, this.appended_x64);
      var1.addData(53, this.c2profile.getByteArray(".self"), 16);
      this.setupExecuteList(var1);
      if ("VirtualAllocEx".equals(this.allocator)) {
         var1.addShort(52, 0);
      } else if ("NtMapViewOfSection".equals(this.allocator)) {
         var1.addShort(52, 1);
      } else {
         CommonUtils.print_error("Unknown allocator: '" + this.allocator + "'");
      }

      return this;
   }
}
