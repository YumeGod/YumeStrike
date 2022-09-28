package org.apache.fop.afp.ioca;

public class ImageRasterPattern {
   private static final byte[] GREYSCALE16 = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
   private static final byte[] GREYSCALE15 = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 17, 17, 17, 17, 17, 17, 17, 17};
   private static final byte[] GREYSCALE14 = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 17, 17, 17, 17, 17, 17, 17, 17, 0, 0, 0, 0, 0, 0, 0, 0, 68, 68, 68, 68, 68, 68, 68, 68};
   private static final byte[] GREYSCALE13 = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 17, 17, 17, 17, 17, 17, 17, 17, 0, 0, 0, 0, 0, 0, 0, 0, -86, -86, -86, -86, -86, -86, -86, -86};
   private static final byte[] GREYSCALE12 = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 85, 85, 85, 85, 85, 85, 85, 85, 0, 0, 0, 0, 0, 0, 0, 0, -86, -86, -86, -86, -86, -86, -86, -86};
   private static final byte[] GREYSCALE11 = new byte[]{17, 17, 17, 17, 17, 17, 17, 17, 68, 68, 68, 68, 68, 68, 68, 68, 17, 17, 17, 17, 17, 17, 17, 17, -86, -86, -86, -86, -86, -86, -86, -86};
   private static final byte[] GREYSCALE10 = new byte[]{17, 17, 17, 17, 17, 17, 17, 17, -86, -86, -86, -86, -86, -86, -86, -86, 68, 68, 68, 68, 68, 68, 68, 68, -86, -86, -86, -86, -86, -86, -86, -86};
   private static final byte[] GREYSCALE09 = new byte[]{17, 17, 17, 17, 17, 17, 17, 17, -86, -86, -86, -86, -86, -86, -86, -86, 85, 85, 85, 85, 85, 85, 85, 85, -86, -86, -86, -86, -86, -86, -86, -86};
   private static final byte[] GREYSCALE08 = new byte[]{-86, -86, -86, -86, -86, -86, -86, -86, -86, -86, -86, -86, -86, -86, -86, -86, -86, -86, -86, -86, -86, -86, -86, -86, -86, -86, -86, -86, -86, -86, -86, -86};
   private static final byte[] GREYSCALE07 = new byte[]{85, 85, 85, 85, 85, 85, 85, 85, -86, -86, -86, -86, -86, -86, -86, -86, 85, 85, 85, 85, 85, 85, 85, 85, -69, -69, -69, -69, -69, -69, -69, -69};
   private static final byte[] GREYSCALE06 = new byte[]{85, 85, 85, 85, 85, 85, 85, 85, -69, -69, -69, -69, -69, -69, -69, -69, 85, 85, 85, 85, 85, 85, 85, 85, -18, -18, -18, -18, -18, -18, -18, -18};
   private static final byte[] GREYSCALE05 = new byte[]{85, 85, 85, 85, 85, 85, 85, 85, -69, -69, -69, -69, -69, -69, -69, -69, -18, -18, -18, -18, -18, -18, -18, -18, -69, -69, -69, -69, -69, -69, -69, -69};
   private static final byte[] GREYSCALE04 = new byte[]{85, 85, 85, 85, 85, 85, 85, 85, -1, -1, -1, -1, -1, -1, -1, -1, -86, -86, -86, -86, -86, -86, -86, -86, -1, -1, -1, -1, -1, -1, -1, -1};
   private static final byte[] GREYSCALE03 = new byte[]{85, 85, 85, 85, 85, 85, 85, 85, -1, -1, -1, -1, -1, -1, -1, -1, -69, -69, -69, -69, -69, -69, -69, -69, -1, -1, -1, -1, -1, -1, -1, -1};
   private static final byte[] GREYSCALE02 = new byte[]{119, 119, 119, 119, 119, 119, 119, 119, -1, -1, -1, -1, -1, -1, -1, -1, -35, -35, -35, -35, -35, -35, -35, -35, -1, -1, -1, -1, -1, -1, -1, -1};
   private static final byte[] GREYSCALE01 = new byte[]{119, 119, 119, 119, 119, 119, 119, 119, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
   private static final byte[] GREYSCALE00 = new byte[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};

   public static byte[] getRasterData(int greyscale) {
      int repeat = 16;
      byte[] greypattern = new byte[32];
      byte[] rasterdata = new byte[32 * repeat];
      switch (greyscale) {
         case 0:
            System.arraycopy(GREYSCALE00, 0, greypattern, 0, 32);
            break;
         case 1:
            System.arraycopy(GREYSCALE01, 0, greypattern, 0, 32);
            break;
         case 2:
            System.arraycopy(GREYSCALE02, 0, greypattern, 0, 32);
            break;
         case 3:
            System.arraycopy(GREYSCALE03, 0, greypattern, 0, 32);
            break;
         case 4:
            System.arraycopy(GREYSCALE04, 0, greypattern, 0, 32);
            break;
         case 5:
            System.arraycopy(GREYSCALE05, 0, greypattern, 0, 32);
            break;
         case 6:
            System.arraycopy(GREYSCALE06, 0, greypattern, 0, 32);
            break;
         case 7:
            System.arraycopy(GREYSCALE07, 0, greypattern, 0, 32);
            break;
         case 8:
            System.arraycopy(GREYSCALE08, 0, greypattern, 0, 32);
            break;
         case 9:
            System.arraycopy(GREYSCALE09, 0, greypattern, 0, 32);
            break;
         case 10:
            System.arraycopy(GREYSCALE10, 0, greypattern, 0, 32);
            break;
         case 11:
            System.arraycopy(GREYSCALE11, 0, greypattern, 0, 32);
            break;
         case 12:
            System.arraycopy(GREYSCALE12, 0, greypattern, 0, 32);
            break;
         case 13:
            System.arraycopy(GREYSCALE13, 0, greypattern, 0, 32);
            break;
         case 14:
            System.arraycopy(GREYSCALE14, 0, greypattern, 0, 32);
            break;
         case 15:
            System.arraycopy(GREYSCALE15, 0, greypattern, 0, 32);
            break;
         case 16:
            System.arraycopy(GREYSCALE16, 0, greypattern, 0, 32);
            break;
         default:
            System.arraycopy(GREYSCALE00, 0, greypattern, 0, 32);
      }

      for(int i = 0; i < repeat; ++i) {
         System.arraycopy(greypattern, 0, rasterdata, i * 32, 32);
      }

      return rasterdata;
   }
}
