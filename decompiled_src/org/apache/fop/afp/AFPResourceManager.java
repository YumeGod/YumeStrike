package org.apache.fop.afp;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.afp.fonts.AFPFont;
import org.apache.fop.afp.fonts.CharacterSet;
import org.apache.fop.afp.modca.AbstractAFPObject;
import org.apache.fop.afp.modca.AbstractNamedAFPObject;
import org.apache.fop.afp.modca.AbstractPageObject;
import org.apache.fop.afp.modca.IncludeObject;
import org.apache.fop.afp.modca.IncludedResourceObject;
import org.apache.fop.afp.modca.PageSegment;
import org.apache.fop.afp.modca.Registry;
import org.apache.fop.afp.modca.ResourceGroup;
import org.apache.fop.afp.modca.ResourceObject;
import org.apache.fop.afp.util.ResourceAccessor;

public class AFPResourceManager {
   private static Log log;
   private DataStream dataStream;
   private final Factory factory = new Factory();
   private final AFPStreamer streamer;
   private final AFPDataObjectFactory dataObjectFactory;
   private int instreamObjectCount = 0;
   private final Map includeNameMap = new HashMap();
   private Map pageSegmentMap = new HashMap();
   private AFPResourceLevelDefaults resourceLevelDefaults = new AFPResourceLevelDefaults();

   public AFPResourceManager() {
      this.streamer = new AFPStreamer(this.factory);
      this.dataObjectFactory = new AFPDataObjectFactory(this.factory);
   }

   public DataStream createDataStream(AFPPaintingState paintingState, OutputStream outputStream) throws IOException {
      this.dataStream = this.streamer.createDataStream(paintingState);
      this.streamer.setOutputStream(outputStream);
      return this.dataStream;
   }

   public DataStream getDataStream() {
      return this.dataStream;
   }

   public void writeToStream() throws IOException {
      this.streamer.close();
   }

   public void setDefaultResourceGroupFilePath(String filePath) {
      this.streamer.setDefaultResourceGroupFilePath(filePath);
   }

   public void createObject(AFPDataObjectInfo dataObjectInfo) throws IOException {
      AbstractNamedAFPObject namedObj = null;
      AFPResourceInfo resourceInfo = dataObjectInfo.getResourceInfo();
      this.updateResourceInfoUri(resourceInfo);
      String objectName = (String)this.includeNameMap.get(resourceInfo);
      if (objectName != null) {
         this.includeObject(dataObjectInfo, objectName);
      } else {
         objectName = (String)this.pageSegmentMap.get(resourceInfo);
         if (objectName != null) {
            this.includePageSegment(dataObjectInfo, objectName);
         } else {
            boolean useInclude = true;
            Registry.ObjectType objectType = null;
            if (dataObjectInfo instanceof AFPImageObjectInfo) {
               AFPImageObjectInfo imageObjectInfo = (AFPImageObjectInfo)dataObjectInfo;
               namedObj = this.dataObjectFactory.createImage(imageObjectInfo);
            } else if (dataObjectInfo instanceof AFPGraphicsObjectInfo) {
               AFPGraphicsObjectInfo graphicsObjectInfo = (AFPGraphicsObjectInfo)dataObjectInfo;
               namedObj = this.dataObjectFactory.createGraphic(graphicsObjectInfo);
            } else {
               namedObj = this.dataObjectFactory.createObjectContainer(dataObjectInfo);
               objectType = dataObjectInfo.getObjectType();
               useInclude = objectType != null && objectType.isIncludable();
            }

            AFPResourceLevel resourceLevel = resourceInfo.getLevel();
            ResourceGroup resourceGroup = this.streamer.getResourceGroup(resourceLevel);
            useInclude &= resourceGroup != null;
            if (useInclude) {
               boolean usePageSegment = dataObjectInfo.isCreatePageSegment();
               if (resourceLevel.isPrintFile() || resourceLevel.isExternal()) {
                  if (usePageSegment) {
                     String pageSegmentName = "S10" + ((AbstractNamedAFPObject)namedObj).getName().substring(3);
                     ((AbstractNamedAFPObject)namedObj).setName(pageSegmentName);
                     PageSegment seg = new PageSegment(pageSegmentName);
                     seg.addObject((AbstractAFPObject)namedObj);
                     namedObj = seg;
                  }

                  namedObj = this.dataObjectFactory.createResource((AbstractNamedAFPObject)namedObj, resourceInfo, objectType);
               }

               resourceGroup.addObject((AbstractNamedAFPObject)namedObj);
               objectName = ((AbstractNamedAFPObject)namedObj).getName();
               if (usePageSegment) {
                  this.includePageSegment(dataObjectInfo, objectName);
                  this.pageSegmentMap.put(resourceInfo, objectName);
               } else {
                  this.includeObject(dataObjectInfo, objectName);
                  this.includeNameMap.put(resourceInfo, objectName);
               }
            } else {
               this.dataStream.getCurrentPage().addObject(namedObj);
            }

         }
      }
   }

   private void updateResourceInfoUri(AFPResourceInfo resourceInfo) {
      String uri = resourceInfo.getUri();
      if (uri == null) {
         uri = "/";
      }

      if (uri.endsWith("/")) {
         uri = uri + "#" + ++this.instreamObjectCount;
         resourceInfo.setUri(uri);
      }

   }

   private void includeObject(AFPDataObjectInfo dataObjectInfo, String objectName) {
      IncludeObject includeObject = this.dataObjectFactory.createInclude(objectName, dataObjectInfo);
      this.dataStream.getCurrentPage().addObject(includeObject);
   }

   public void embedFont(AFPFont afpFont, CharacterSet charSet) throws IOException {
      if (afpFont.isEmbeddable() && charSet.getResourceAccessor() != null) {
         ResourceAccessor accessor = charSet.getResourceAccessor();
         this.createIncludedResource(charSet.getName(), accessor, (byte)64);
         this.createIncludedResource(charSet.getCodePage(), accessor, (byte)65);
      }

   }

   private void includePageSegment(AFPDataObjectInfo dataObjectInfo, String pageSegmentName) {
      int x = dataObjectInfo.getObjectAreaInfo().getX();
      int y = dataObjectInfo.getObjectAreaInfo().getY();
      AbstractPageObject currentPage = this.dataStream.getCurrentPage();
      boolean createHardPageSegments = true;
      currentPage.createIncludePageSegment(pageSegmentName, x, y, createHardPageSegments);
   }

   public void createIncludedResource(String resourceName, ResourceAccessor accessor, byte resourceObjectType) throws IOException {
      URI uri;
      try {
         uri = new URI(resourceName.trim());
      } catch (URISyntaxException var6) {
         throw new IOException("Could not create URI from resource name: " + resourceName + " (" + var6.getMessage() + ")");
      }

      this.createIncludedResource(resourceName, uri, accessor, resourceObjectType);
   }

   public void createIncludedResource(String resourceName, URI uri, ResourceAccessor accessor, byte resourceObjectType) throws IOException {
      AFPResourceLevel resourceLevel = new AFPResourceLevel(4);
      AFPResourceInfo resourceInfo = new AFPResourceInfo();
      resourceInfo.setLevel(resourceLevel);
      resourceInfo.setName(resourceName);
      resourceInfo.setUri(uri.toASCIIString());
      String objectName = (String)this.includeNameMap.get(resourceInfo);
      if (objectName == null) {
         if (log.isDebugEnabled()) {
            log.debug("Adding included resource: " + resourceName);
         }

         IncludedResourceObject resourceContent = new IncludedResourceObject(resourceName, accessor, uri);
         ResourceObject resourceObject = this.factory.createResource(resourceName);
         resourceObject.setDataObject(resourceContent);
         resourceObject.setType(resourceObjectType);
         ResourceGroup resourceGroup = this.streamer.getResourceGroup(resourceLevel);
         resourceGroup.addObject(resourceObject);
         this.includeNameMap.put(resourceInfo, resourceName);
      }

   }

   public void setResourceLevelDefaults(AFPResourceLevelDefaults defaults) {
      this.resourceLevelDefaults.mergeFrom(defaults);
   }

   public AFPResourceLevelDefaults getResourceLevelDefaults() {
      return this.resourceLevelDefaults;
   }

   static {
      log = LogFactory.getLog(AFPResourceManager.class);
   }
}
