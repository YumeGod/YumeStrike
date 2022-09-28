package org.apache.fop.afp.goca;

import org.apache.fop.afp.StructuredData;
import org.apache.fop.afp.modca.AbstractAFPObject;

public abstract class AbstractGraphicsDrawingOrder extends AbstractAFPObject implements StructuredData {
   abstract byte getOrderCode();

   byte[] getData() {
      int len = this.getDataLength();
      byte[] data = new byte[len];
      data[0] = this.getOrderCode();
      data[1] = (byte)(len - 2);
      return data;
   }

   public String getName() {
      String className = this.getClass().getName();
      return className.substring(className.lastIndexOf(".") + 1);
   }
}
