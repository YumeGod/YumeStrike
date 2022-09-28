package org.apache.fop.afp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.afp.modca.ResourceGroup;
import org.apache.fop.afp.modca.StreamedResourceGroup;

public class AFPStreamer implements Streamable {
   private static final Log log;
   private static final String AFPDATASTREAM_TEMP_FILE_PREFIX = "AFPDataStream_";
   private static final int BUFFER_SIZE = 4096;
   private static final String DEFAULT_EXTERNAL_RESOURCE_FILENAME = "resources.afp";
   private final Factory factory;
   private final Map pathResourceGroupMap = new HashMap();
   private StreamedResourceGroup printFileResourceGroup;
   private String defaultResourceGroupFilePath = "resources.afp";
   private File tempFile;
   private OutputStream documentOutputStream;
   private OutputStream outputStream;
   private RandomAccessFile documentFile;
   private DataStream dataStream;

   public AFPStreamer(Factory factory) {
      this.factory = factory;
   }

   public DataStream createDataStream(AFPPaintingState paintingState) throws IOException {
      this.tempFile = File.createTempFile("AFPDataStream_", (String)null);
      this.documentFile = new RandomAccessFile(this.tempFile, "rw");
      this.documentOutputStream = new BufferedOutputStream(new FileOutputStream(this.documentFile.getFD()));
      this.dataStream = this.factory.createDataStream(paintingState, this.documentOutputStream);
      return this.dataStream;
   }

   public void setDefaultResourceGroupFilePath(String filePath) {
      this.defaultResourceGroupFilePath = filePath;
   }

   public ResourceGroup getResourceGroup(AFPResourceLevel level) {
      ResourceGroup resourceGroup = null;
      if (level.isInline()) {
         return null;
      } else {
         if (level.isExternal()) {
            String filePath = level.getExternalFilePath();
            if (filePath == null) {
               log.warn("No file path provided for external resource, using default.");
               filePath = this.defaultResourceGroupFilePath;
            }

            resourceGroup = (ResourceGroup)this.pathResourceGroupMap.get(filePath);
            if (resourceGroup == null) {
               OutputStream os = null;

               try {
                  os = new BufferedOutputStream(new FileOutputStream(filePath));
               } catch (FileNotFoundException var10) {
                  log.error("Failed to create/open external resource group file '" + filePath + "'");
               } finally {
                  if (os != null) {
                     resourceGroup = this.factory.createStreamedResourceGroup(os);
                     this.pathResourceGroupMap.put(filePath, resourceGroup);
                  }

               }
            }
         } else if (level.isPrintFile()) {
            if (this.printFileResourceGroup == null) {
               this.printFileResourceGroup = this.factory.createStreamedResourceGroup(this.outputStream);
            }

            resourceGroup = this.printFileResourceGroup;
         } else {
            resourceGroup = this.dataStream.getResourceGroup(level);
         }

         return (ResourceGroup)resourceGroup;
      }
   }

   public void close() throws IOException {
      Iterator it = this.pathResourceGroupMap.entrySet().iterator();

      while(it.hasNext()) {
         StreamedResourceGroup resourceGroup = (StreamedResourceGroup)it.next();
         resourceGroup.close();
      }

      if (this.printFileResourceGroup != null) {
         this.printFileResourceGroup.close();
      }

      this.writeToStream(this.outputStream);
      this.outputStream.close();
      this.tempFile.delete();
   }

   public void setOutputStream(OutputStream outputStream) {
      this.outputStream = outputStream;
   }

   public void writeToStream(OutputStream os) throws IOException {
      int len = (int)this.documentFile.length();
      int numChunks = len / 4096;
      int remainingChunkSize = len % 4096;
      this.documentFile.seek(0L);
      byte[] buffer;
      if (numChunks > 0) {
         buffer = new byte[4096];

         for(int i = 0; i < numChunks; ++i) {
            this.documentFile.read(buffer, 0, 4096);
            os.write(buffer, 0, 4096);
         }
      } else {
         buffer = new byte[remainingChunkSize];
      }

      if (remainingChunkSize > 0) {
         this.documentFile.read(buffer, 0, remainingChunkSize);
         os.write(buffer, 0, remainingChunkSize);
      }

      os.flush();
   }

   static {
      log = LogFactory.getLog(AFPStreamer.class);
   }
}
