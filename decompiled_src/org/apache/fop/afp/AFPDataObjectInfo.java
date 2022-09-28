package org.apache.fop.afp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.afp.modca.Registry;

public class AFPDataObjectInfo {
   private static final Log log = LogFactory.getLog("org.apache.xmlgraphics.afp");
   private AFPObjectAreaInfo objectAreaInfo;
   private AFPResourceInfo resourceInfo;
   private int dataWidth;
   private int dataHeight;
   private String mimeType;
   private byte[] data;
   private int dataHeightRes;
   private int dataWidthRes;
   private boolean createPageSegment;
   private byte mappingOption = 96;

   public void setMimeType(String mimeType) {
      this.mimeType = mimeType;
   }

   public String getMimeType() {
      return this.mimeType;
   }

   public Registry.ObjectType getObjectType() {
      return Registry.getInstance().getObjectType(this.getMimeType());
   }

   public AFPResourceInfo getResourceInfo() {
      if (this.resourceInfo == null) {
         this.resourceInfo = new AFPResourceInfo();
      }

      return this.resourceInfo;
   }

   public void setResourceInfo(AFPResourceInfo resourceInfo) {
      this.resourceInfo = resourceInfo;
   }

   public void setObjectAreaInfo(AFPObjectAreaInfo objectAreaInfo) {
      this.objectAreaInfo = objectAreaInfo;
   }

   public AFPObjectAreaInfo getObjectAreaInfo() {
      return this.objectAreaInfo;
   }

   public String getUri() {
      return this.getResourceInfo().getUri();
   }

   public void setUri(String uri) {
      this.getResourceInfo().setUri(uri);
   }

   public int getDataWidth() {
      return this.dataWidth;
   }

   public void setDataWidth(int imageDataWidth) {
      this.dataWidth = imageDataWidth;
   }

   public int getDataHeight() {
      return this.dataHeight;
   }

   public void setDataHeight(int imageDataHeight) {
      this.dataHeight = imageDataHeight;
   }

   public int getDataHeightRes() {
      return this.dataHeightRes;
   }

   public void setDataHeightRes(int dataHeightRes) {
      this.dataHeightRes = dataHeightRes;
   }

   public int getDataWidthRes() {
      return this.dataWidthRes;
   }

   public void setDataWidthRes(int dataWidthRes) {
      this.dataWidthRes = dataWidthRes;
   }

   public void setData(byte[] data) {
      this.data = data;
   }

   public byte[] getData() {
      return this.data;
   }

   public void setCreatePageSegment(boolean value) {
      this.createPageSegment = value;
   }

   public boolean isCreatePageSegment() {
      return this.createPageSegment;
   }

   public void setMappingOption(byte mappingOption) {
      this.mappingOption = mappingOption;
   }

   public byte getMappingOption() {
      return this.mappingOption;
   }

   public String toString() {
      return "AFPDataObjectInfo{mimeType=" + this.mimeType + ", dataWidth=" + this.dataWidth + ", dataHeight=" + this.dataHeight + ", dataWidthRes=" + this.dataWidthRes + ", dataHeightRes=" + this.dataHeightRes + (this.objectAreaInfo != null ? ", objectAreaInfo=" + this.objectAreaInfo : "") + (this.resourceInfo != null ? ", resourceInfo=" + this.resourceInfo : "");
   }
}
