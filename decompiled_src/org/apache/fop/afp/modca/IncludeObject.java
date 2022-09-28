package org.apache.fop.afp.modca;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.fop.afp.modca.triplets.MappingOptionTriplet;
import org.apache.fop.afp.modca.triplets.MeasurementUnitsTriplet;
import org.apache.fop.afp.modca.triplets.ObjectAreaSizeTriplet;
import org.apache.fop.afp.util.BinaryUtils;

public class IncludeObject extends AbstractNamedAFPObject {
   public static final byte TYPE_PAGE_SEGMENT = 95;
   public static final byte TYPE_OTHER = -110;
   public static final byte TYPE_GRAPHIC = -69;
   public static final byte TYPE_BARCODE = -21;
   public static final byte TYPE_IMAGE = -5;
   private byte objectType = -110;
   private int xoaOset = 0;
   private int yoaOset = 0;
   private int oaOrent = 0;
   private int xocaOset = -1;
   private int yocaOset = -1;

   public IncludeObject(String name) {
      super(name);
   }

   public void setObjectAreaOrientation(int orientation) {
      if (orientation != 0 && orientation != 90 && orientation != 180 && orientation != 270) {
         throw new IllegalArgumentException("The orientation must be one of the values 0, 90, 180, 270");
      } else {
         this.oaOrent = orientation;
      }
   }

   public void setObjectAreaOffset(int x, int y) {
      this.xoaOset = x;
      this.yoaOset = y;
   }

   public void setContentAreaOffset(int x, int y) {
      this.xocaOset = x;
      this.yocaOset = y;
   }

   public void setObjectType(byte type) {
      this.objectType = type;
   }

   public void writeToStream(OutputStream os) throws IOException {
      byte[] data = new byte[36];
      super.copySF(data, (byte)-81, (byte)-61);
      int tripletDataLength = this.getTripletDataLength();
      byte[] len = BinaryUtils.convert(35 + tripletDataLength, 2);
      data[1] = len[0];
      data[2] = len[1];
      data[17] = 0;
      data[18] = this.objectType;
      byte[] y;
      if (this.xoaOset > -1) {
         y = BinaryUtils.convert(this.xoaOset, 3);
         data[19] = y[0];
         data[20] = y[1];
         data[21] = y[2];
      } else {
         data[19] = -1;
         data[20] = -1;
         data[21] = -1;
      }

      if (this.yoaOset > -1) {
         y = BinaryUtils.convert(this.yoaOset, 3);
         data[22] = y[0];
         data[23] = y[1];
         data[24] = y[2];
      } else {
         data[22] = -1;
         data[23] = -1;
         data[24] = -1;
      }

      switch (this.oaOrent) {
         case -1:
            data[25] = -1;
            data[26] = -1;
            data[27] = -1;
            data[28] = -1;
            break;
         case 90:
            data[25] = 45;
            data[26] = 0;
            data[27] = 90;
            data[28] = 0;
            break;
         case 180:
            data[25] = 90;
            data[25] = 0;
            data[27] = -121;
            data[28] = 0;
            break;
         case 270:
            data[25] = -121;
            data[26] = 0;
            data[27] = 0;
            data[28] = 0;
            break;
         default:
            data[25] = 0;
            data[26] = 0;
            data[27] = 45;
            data[28] = 0;
      }

      if (this.xocaOset > -1) {
         y = BinaryUtils.convert(this.xocaOset, 3);
         data[29] = y[0];
         data[30] = y[1];
         data[31] = y[2];
      } else {
         data[29] = -1;
         data[30] = -1;
         data[31] = -1;
      }

      if (this.yocaOset > -1) {
         y = BinaryUtils.convert(this.yocaOset, 3);
         data[32] = y[0];
         data[33] = y[1];
         data[34] = y[2];
      } else {
         data[32] = -1;
         data[33] = -1;
         data[34] = -1;
      }

      data[35] = 1;
      os.write(data);
      this.writeTriplets(os);
   }

   private String getObjectTypeName() {
      String objectTypeName = null;
      if (this.objectType == 95) {
         objectTypeName = "page segment";
      } else if (this.objectType == -110) {
         objectTypeName = "other";
      } else if (this.objectType == -69) {
         objectTypeName = "graphic";
      } else if (this.objectType == -21) {
         objectTypeName = "barcode";
      } else if (this.objectType == -5) {
         objectTypeName = "image";
      }

      return objectTypeName;
   }

   public String toString() {
      return "IncludeObject{name=" + this.getName() + ", objectType=" + this.getObjectTypeName() + ", xoaOset=" + this.xoaOset + ", yoaOset=" + this.yoaOset + ", oaOrent" + this.oaOrent + ", xocaOset=" + this.xocaOset + ", yocaOset=" + this.yocaOset + "}";
   }

   public void setMappingOption(byte optionValue) {
      this.addTriplet(new MappingOptionTriplet(optionValue));
   }

   public void setObjectAreaSize(int x, int y) {
      this.addTriplet(new ObjectAreaSizeTriplet(x, y));
   }

   public void setMeasurementUnits(int xRes, int yRes) {
      this.addTriplet(new MeasurementUnitsTriplet(xRes, xRes));
   }
}
