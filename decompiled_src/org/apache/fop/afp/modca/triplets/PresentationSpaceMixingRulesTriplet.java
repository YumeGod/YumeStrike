package org.apache.fop.afp.modca.triplets;

import java.io.IOException;
import java.io.OutputStream;

public class PresentationSpaceMixingRulesTriplet extends AbstractTriplet {
   public static final byte RULE_BACK_ON_BACK = 112;
   public static final byte RULE_BACK_ON_FORE = 113;
   public static final byte RULE_FORE_ON_BACK = 114;
   public static final byte RULE_FORE_ON_FORE = 115;
   public static final byte OVERPAINT = 1;
   public static final byte UNDERPAINT = 2;
   public static final byte BLEND = 3;
   public static final byte DEFAULT = -1;
   private final byte[] rules;

   public PresentationSpaceMixingRulesTriplet(byte[] rules) {
      super((byte)113);
      this.rules = rules;
   }

   public int getDataLength() {
      return 2 + this.rules.length;
   }

   public void writeToStream(OutputStream os) throws IOException {
      byte[] data = this.getData();
      System.arraycopy(this.rules, 0, data, 2, this.rules.length);
      os.write(data);
   }
}
