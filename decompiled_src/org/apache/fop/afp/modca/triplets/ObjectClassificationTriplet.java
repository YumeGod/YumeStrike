package org.apache.fop.afp.modca.triplets;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.fop.afp.modca.Registry;
import org.apache.fop.afp.util.StringUtils;

public class ObjectClassificationTriplet extends AbstractTriplet {
   public static final byte CLASS_TIME_INVARIANT_PAGINATED_PRESENTATION_OBJECT = 1;
   public static final byte CLASS_TIME_VARIANT_PRESENTATION_OBJECT = 16;
   public static final byte CLASS_EXECUTABLE_PROGRAM = 32;
   public static final byte CLASS_SETUP_FILE = 48;
   public static final byte CLASS_SECONDARY_RESOURCE = 64;
   public static final byte CLASS_DATA_OBJECT_FONT = 65;
   private final byte objectClass;
   private final Registry.ObjectType objectType;
   private final boolean containerHasOEG;
   private final boolean dataInContainer;
   private final boolean dataInOCD;
   private final String objectLevel;
   private final String companyName;
   private static final int OBJECT_LEVEL_LEN = 8;
   private static final int OBJECT_TYPE_NAME_LEN = 32;
   private static final int COMPANY_NAME_LEN = 32;

   public ObjectClassificationTriplet(byte objectClass, Registry.ObjectType objectType, boolean dataInContainer, boolean containerHasOEG, boolean dataInOCD) {
      this(objectClass, objectType, dataInContainer, containerHasOEG, dataInOCD, (String)null, (String)null);
   }

   public ObjectClassificationTriplet(byte objectClass, Registry.ObjectType objectType, boolean dataInContainer, boolean containerHasOEG, boolean dataInOCD, String objLev, String compName) {
      super((byte)16);
      this.objectClass = objectClass;
      if (objectType == null) {
         throw new IllegalArgumentException("MO:DCA Registry object type is null");
      } else {
         this.objectType = objectType;
         this.dataInContainer = dataInContainer;
         this.containerHasOEG = containerHasOEG;
         this.dataInOCD = dataInOCD;
         this.objectLevel = objLev;
         this.companyName = compName;
      }
   }

   public byte[] getStructureFlagsAsBytes(boolean dataInContainer, boolean containerHasOEG, boolean dataInOCD) {
      byte[] strucFlgs = new byte[2];
      if (dataInContainer) {
         strucFlgs[0] = (byte)(strucFlgs[0] | 192);
      } else {
         strucFlgs[0] = (byte)(strucFlgs[0] | 64);
      }

      if (containerHasOEG) {
         strucFlgs[0] = (byte)(strucFlgs[0] | 48);
      } else {
         strucFlgs[0] = (byte)(strucFlgs[0] | 16);
      }

      if (dataInOCD) {
         strucFlgs[0] = (byte)(strucFlgs[0] | 12);
      } else {
         strucFlgs[0] = (byte)(strucFlgs[0] | 4);
      }

      strucFlgs[1] = 0;
      return strucFlgs;
   }

   public int getDataLength() {
      return 96;
   }

   public void writeToStream(OutputStream os) throws IOException {
      byte[] data = this.getData();
      data[2] = 0;
      data[3] = this.objectClass;
      data[4] = 0;
      data[5] = 0;
      byte[] structureFlagsBytes = this.getStructureFlagsAsBytes(this.dataInContainer, this.containerHasOEG, this.dataInOCD);
      data[6] = structureFlagsBytes[0];
      data[7] = structureFlagsBytes[1];
      byte[] objectIdBytes = this.objectType.getOID();
      System.arraycopy(objectIdBytes, 0, data, 8, objectIdBytes.length);
      byte[] objectTypeNameBytes = StringUtils.rpad(this.objectType.getName(), ' ', 32).getBytes("Cp1146");
      System.arraycopy(objectTypeNameBytes, 0, data, 24, objectTypeNameBytes.length);
      byte[] objectLevelBytes = StringUtils.rpad(this.objectLevel, ' ', 8).getBytes("Cp1146");
      System.arraycopy(objectLevelBytes, 0, data, 56, objectLevelBytes.length);
      byte[] companyNameBytes = StringUtils.rpad(this.companyName, ' ', 32).getBytes("Cp1146");
      System.arraycopy(companyNameBytes, 0, data, 64, companyNameBytes.length);
      os.write(data);
   }
}
