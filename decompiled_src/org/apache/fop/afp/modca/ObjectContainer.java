package org.apache.fop.afp.modca;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.fop.afp.AFPDataObjectInfo;
import org.apache.fop.afp.AFPObjectAreaInfo;
import org.apache.fop.afp.AFPResourceInfo;
import org.apache.fop.afp.AFPResourceLevel;
import org.apache.fop.afp.Factory;
import org.apache.fop.afp.util.BinaryUtils;

public class ObjectContainer extends AbstractDataObject {
   private static final int MAX_DATA_LEN = 32759;
   private byte[] data;

   public ObjectContainer(Factory factory, String name) {
      super(factory, name);
   }

   protected void writeStart(OutputStream os) throws IOException {
      byte[] headerData = new byte[17];
      this.copySF(headerData, (byte)-88, (byte)-110);
      int containerLen = headerData.length + this.getTripletDataLength() - 1;
      byte[] len = BinaryUtils.convert(containerLen, 2);
      headerData[1] = len[0];
      headerData[2] = len[1];
      os.write(headerData);
   }

   protected void writeContent(OutputStream os) throws IOException {
      super.writeContent(os);
      byte[] dataHeader = new byte[9];
      copySF(dataHeader, (byte)-45, (byte)-18, (byte)-110);
      int lengthOffset = true;
      if (this.data != null) {
         writeChunksToStream(this.data, dataHeader, 1, 32759, os);
      }

   }

   protected void writeEnd(OutputStream os) throws IOException {
      byte[] data = new byte[17];
      this.copySF(data, (byte)-87, (byte)-110);
      os.write(data);
   }

   public void setViewport(AFPDataObjectInfo dataObjectInfo) {
      AFPResourceInfo resourceInfo = dataObjectInfo.getResourceInfo();
      AFPResourceLevel resourceLevel = resourceInfo.getLevel();
      if (resourceLevel.isInline()) {
         super.setViewport(dataObjectInfo);
         MapContainerData mapContainerData = this.factory.createMapContainerData((byte)32);
         this.getObjectEnvironmentGroup().setMapContainerData(mapContainerData);
         int dataWidth = dataObjectInfo.getDataWidth();
         int dataHeight = dataObjectInfo.getDataHeight();
         AFPObjectAreaInfo objectAreaInfo = dataObjectInfo.getObjectAreaInfo();
         int widthRes = objectAreaInfo.getWidthRes();
         int heightRes = objectAreaInfo.getHeightRes();
         ContainerDataDescriptor containerDataDescriptor = this.factory.createContainerDataDescriptor(dataWidth, dataHeight, widthRes, heightRes);
         this.getObjectEnvironmentGroup().setDataDescriptor(containerDataDescriptor);
      }

   }

   public void setData(byte[] data) {
      this.data = data;
   }
}
