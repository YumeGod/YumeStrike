package org.apache.fop.afp.modca;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.fop.afp.util.BinaryUtils;

public final class ObjectEnvironmentGroup extends AbstractNamedAFPObject {
   private PresentationEnvironmentControl presentationEnvironmentControl;
   private ObjectAreaDescriptor objectAreaDescriptor;
   private ObjectAreaPosition objectAreaPosition;
   private MapImageObject mapImageObject;
   private AbstractDescriptor dataDescriptor;
   private MapDataResource mapDataResource;
   private MapContainerData mapContainerData;

   public ObjectEnvironmentGroup(String name) {
      super(name);
   }

   public void setObjectAreaDescriptor(ObjectAreaDescriptor objectAreaDescriptor) {
      this.objectAreaDescriptor = objectAreaDescriptor;
   }

   public void setObjectAreaPosition(ObjectAreaPosition objectAreaPosition) {
      this.objectAreaPosition = objectAreaPosition;
   }

   public void setMapImageObject(MapImageObject mapImageObject) {
      this.mapImageObject = mapImageObject;
   }

   protected void writeStart(OutputStream os) throws IOException {
      byte[] data = new byte[17];
      this.copySF(data, (byte)-88, (byte)-57);
      int tripletDataLength = this.getTripletDataLength();
      int sfLen = data.length + tripletDataLength - 1;
      byte[] len = BinaryUtils.convert(sfLen, 2);
      data[1] = len[0];
      data[2] = len[1];
      os.write(data);
      this.writeTriplets(os);
   }

   protected void writeContent(OutputStream os) throws IOException {
      super.writeContent(os);
      if (this.presentationEnvironmentControl != null) {
         this.presentationEnvironmentControl.writeToStream(os);
      }

      if (this.objectAreaDescriptor != null) {
         this.objectAreaDescriptor.writeToStream(os);
      }

      if (this.objectAreaPosition != null) {
         this.objectAreaPosition.writeToStream(os);
      }

      if (this.mapImageObject != null) {
         this.mapImageObject.writeToStream(os);
      }

      if (this.mapContainerData != null) {
         this.mapContainerData.writeToStream(os);
      }

      if (this.mapDataResource != null) {
         this.mapDataResource.writeToStream(os);
      }

      if (this.dataDescriptor != null) {
         this.dataDescriptor.writeToStream(os);
      }

   }

   protected void writeEnd(OutputStream os) throws IOException {
      byte[] data = new byte[17];
      this.copySF(data, (byte)-87, (byte)-57);
      os.write(data);
   }

   public void setPresentationEnvironmentControl(PresentationEnvironmentControl presentationEnvironmentControl) {
      this.presentationEnvironmentControl = presentationEnvironmentControl;
   }

   public void setDataDescriptor(AbstractDescriptor dataDescriptor) {
      this.dataDescriptor = dataDescriptor;
   }

   public void setMapDataResource(MapDataResource mapDataResource) {
      this.mapDataResource = mapDataResource;
   }

   public void setMapContainerData(MapContainerData mapContainerData) {
      this.mapContainerData = mapContainerData;
   }

   public ObjectAreaDescriptor getObjectAreaDescriptor() {
      return this.objectAreaDescriptor;
   }
}
