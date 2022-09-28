package org.apache.fop.afp.modca;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.fop.afp.modca.triplets.ResourceObjectTypeTriplet;
import org.apache.fop.afp.util.BinaryUtils;

public class ResourceObject extends AbstractNamedAFPObject {
   public static final byte TYPE_GRAPHIC = 3;
   public static final byte TYPE_BARCODE = 5;
   public static final byte TYPE_IMAGE = 6;
   public static final byte TYPE_FONT_CHARACTER_SET = 64;
   public static final byte TYPE_CODE_PAGE = 65;
   public static final byte TYPE_CODED_FONT = 66;
   public static final byte TYPE_OBJECT_CONTAINER = -110;
   public static final byte TYPE_DOCUMENT = -88;
   public static final byte TYPE_PAGE_SEGMENT = -5;
   public static final byte TYPE_OVERLAY_OBJECT = -4;
   public static final byte TYPE_PAGEDEF = -3;
   public static final byte TYPE_FORMDEF = -2;
   private AbstractNamedAFPObject namedObject;

   public ResourceObject(String name) {
      super(name);
   }

   public void setDataObject(AbstractNamedAFPObject namedObject) {
      this.namedObject = namedObject;
   }

   public AbstractNamedAFPObject getDataObject() {
      return this.namedObject;
   }

   protected void writeStart(OutputStream os) throws IOException {
      super.writeStart(os);
      byte[] data = new byte[19];
      this.copySF(data, (byte)-88, (byte)-50);
      int tripletDataLength = this.getTripletDataLength();
      byte[] len = BinaryUtils.convert(18 + tripletDataLength, 2);
      data[1] = len[0];
      data[2] = len[1];
      data[17] = 0;
      data[18] = 0;
      os.write(data);
      this.writeTriplets(os);
   }

   protected void writeContent(OutputStream os) throws IOException {
      if (this.namedObject != null) {
         this.namedObject.writeToStream(os);
      }

   }

   protected void writeEnd(OutputStream os) throws IOException {
      byte[] data = new byte[17];
      this.copySF(data, (byte)-87, (byte)-50);
      os.write(data);
   }

   public String toString() {
      return this.getName();
   }

   public void setType(byte type) {
      this.getTriplets().add(new ResourceObjectTypeTriplet(type));
   }
}
