package org.apache.fop.afp.modca;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.fop.afp.modca.triplets.AttributeQualifierTriplet;
import org.apache.fop.afp.modca.triplets.AttributeValueTriplet;
import org.apache.fop.afp.util.BinaryUtils;

public class TagLogicalElement extends AbstractTripletStructuredObject {
   private String name = null;
   private String value = null;
   private int tleID;

   public TagLogicalElement(String name, String value, int tleID) {
      this.name = name;
      this.value = value;
      this.tleID = tleID;
   }

   public void setAttributeValue(String value) {
      this.addTriplet(new AttributeValueTriplet(value));
   }

   public void setAttributeQualifier(int seqNumber, int levNumber) {
      this.addTriplet(new AttributeQualifierTriplet(seqNumber, levNumber));
   }

   public void writeToStream(OutputStream os) throws IOException {
      this.setFullyQualifiedName((byte)11, (byte)0, this.name);
      this.setAttributeValue(this.value);
      this.setAttributeQualifier(this.tleID, 1);
      byte[] data = new byte[SF_HEADER.length];
      this.copySF(data, (byte)-96, (byte)-112);
      int tripletDataLength = this.getTripletDataLength();
      byte[] l = BinaryUtils.convert(data.length + tripletDataLength - 1, 2);
      data[1] = l[0];
      data[2] = l[1];
      os.write(data);
      this.writeTriplets(os);
   }
}
