package org.apache.fop.afp.modca;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.fop.afp.AFPDataObjectInfo;
import org.apache.fop.afp.AFPObjectAreaInfo;
import org.apache.fop.afp.AFPResourceInfo;
import org.apache.fop.afp.AFPResourceLevel;
import org.apache.fop.afp.Completable;
import org.apache.fop.afp.Factory;
import org.apache.fop.afp.Startable;

public abstract class AbstractDataObject extends AbstractNamedAFPObject implements Startable, Completable {
   protected ObjectEnvironmentGroup objectEnvironmentGroup = null;
   protected final Factory factory;
   private boolean complete;
   private boolean started;

   public AbstractDataObject(Factory factory, String name) {
      super(name);
      this.factory = factory;
   }

   public void setViewport(AFPDataObjectInfo dataObjectInfo) {
      AFPObjectAreaInfo objectAreaInfo = dataObjectInfo.getObjectAreaInfo();
      int width = objectAreaInfo.getWidth();
      int height = objectAreaInfo.getHeight();
      int widthRes = objectAreaInfo.getWidthRes();
      int heightRes = objectAreaInfo.getHeightRes();
      ObjectAreaDescriptor objectAreaDescriptor = this.factory.createObjectAreaDescriptor(width, height, widthRes, heightRes);
      this.getObjectEnvironmentGroup().setObjectAreaDescriptor(objectAreaDescriptor);
      AFPResourceInfo resourceInfo = dataObjectInfo.getResourceInfo();
      AFPResourceLevel resourceLevel = resourceInfo.getLevel();
      ObjectAreaPosition objectAreaPosition = null;
      int rotation = objectAreaInfo.getRotation();
      if (resourceLevel.isInline()) {
         int x = objectAreaInfo.getX();
         int y = objectAreaInfo.getY();
         objectAreaPosition = this.factory.createObjectAreaPosition(x, y, rotation);
      } else {
         objectAreaPosition = this.factory.createObjectAreaPosition(0, 0, rotation);
      }

      objectAreaPosition.setReferenceCoordinateSystem((byte)0);
      this.getObjectEnvironmentGroup().setObjectAreaPosition(objectAreaPosition);
   }

   public ObjectEnvironmentGroup getObjectEnvironmentGroup() {
      if (this.objectEnvironmentGroup == null) {
         this.objectEnvironmentGroup = this.factory.createObjectEnvironmentGroup();
      }

      return this.objectEnvironmentGroup;
   }

   protected void writeStart(OutputStream os) throws IOException {
      this.setStarted(true);
   }

   protected void writeContent(OutputStream os) throws IOException {
      this.writeTriplets(os);
      if (this.objectEnvironmentGroup != null) {
         this.objectEnvironmentGroup.writeToStream(os);
      }

   }

   public void setStarted(boolean started) {
      this.started = started;
   }

   public boolean isStarted() {
      return this.started;
   }

   public void setComplete(boolean complete) {
      this.complete = complete;
   }

   public boolean isComplete() {
      return this.complete;
   }
}
