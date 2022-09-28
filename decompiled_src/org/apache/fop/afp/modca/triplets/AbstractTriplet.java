package org.apache.fop.afp.modca.triplets;

import org.apache.fop.afp.modca.AbstractAFPObject;

public abstract class AbstractTriplet extends AbstractAFPObject implements Triplet {
   protected final byte id;

   public AbstractTriplet(byte id) {
      this.id = id;
   }

   public byte getId() {
      return this.id;
   }

   public byte[] getData() {
      int dataLen = this.getDataLength();
      byte[] data = new byte[dataLen];
      data[0] = (byte)dataLen;
      data[1] = this.id;
      return data;
   }
}
