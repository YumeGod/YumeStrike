package org.apache.fop.afp.modca;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.fop.afp.modca.triplets.DescriptorPositionTriplet;
import org.apache.fop.afp.modca.triplets.MeasurementUnitsTriplet;
import org.apache.fop.afp.modca.triplets.ObjectAreaSizeTriplet;
import org.apache.fop.afp.util.BinaryUtils;

public class ObjectAreaDescriptor extends AbstractDescriptor {
   private static final byte OBJECT_AREA_POSITION_ID = 1;

   public ObjectAreaDescriptor(int width, int height, int widthRes, int heightRes) {
      super(width, height, widthRes, heightRes);
   }

   public void writeToStream(OutputStream os) throws IOException {
      byte[] data = new byte[9];
      this.copySF(data, (byte)-90, (byte)107);
      this.addTriplet(new DescriptorPositionTriplet((byte)1));
      this.addTriplet(new MeasurementUnitsTriplet(this.widthRes, this.heightRes));
      this.addTriplet(new ObjectAreaSizeTriplet(this.width, this.height));
      int tripletDataLength = this.getTripletDataLength();
      byte[] len = BinaryUtils.convert(data.length + tripletDataLength - 1, 2);
      data[1] = len[0];
      data[2] = len[1];
      os.write(data);
      this.writeTriplets(os);
   }
}
