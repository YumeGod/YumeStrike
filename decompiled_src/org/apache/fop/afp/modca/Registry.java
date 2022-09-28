package org.apache.fop.afp.modca;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Registry {
   private static final byte COMPID_IOCA_FS10 = 5;
   private static final byte COMPID_IOCA_FS11 = 11;
   private static final byte COMPID_IOCA_FS40 = 55;
   private static final byte COMPID_IOCA_FS45 = 12;
   private static final byte COMPID_EPS = 13;
   private static final byte COMPID_TIFF = 14;
   private static final byte COMPID_GIF = 22;
   private static final byte COMPID_JFIF = 23;
   private static final byte COMPID_PDF_SINGLE_PAGE = 25;
   private static final byte COMPID_PCL_PAGE_OBJECT = 34;
   private static final byte COMPID_TRUETYPE_OPENTYPE_FONT_RESOURCE_OBJECT = 51;
   private static final byte COMPID_TRUETYPE_OPENTYPE_FONT_COLLECTION_RESOURCE_OBJECT = 53;
   private final Map mimeObjectTypeMap = Collections.synchronizedMap(new HashMap());
   private static Registry instance = null;

   public static Registry getInstance() {
      synchronized(Registry.class) {
         if (instance == null) {
            instance = new Registry();
         }
      }

      return instance;
   }

   private Registry() {
      this.init();
   }

   private void init() {
      this.mimeObjectTypeMap.put("image/x-afp+fs10", new ObjectType((byte)5, new byte[]{6, 7, 43, 18, 0, 4, 1, 1, 5}, "IOCA FS10", true, "image/x-afp+fs10"));
      this.mimeObjectTypeMap.put("image/x-afp+fs11", new ObjectType((byte)11, new byte[]{6, 7, 43, 18, 0, 4, 1, 1, 17}, "IOCA FS11", true, "image/x-afp+fs11"));
      this.mimeObjectTypeMap.put("image/x-afp+fs45", new ObjectType((byte)12, new byte[]{6, 7, 43, 18, 0, 4, 1, 1, 18}, "IOCA FS45", true, "image/x-afp+fs45"));
      this.mimeObjectTypeMap.put("application/postscript", new ObjectType((byte)13, new byte[]{6, 7, 43, 18, 0, 4, 1, 1, 13}, "Encapsulated Postscript", true, "application/postscript"));
      this.mimeObjectTypeMap.put("image/tiff", new ObjectType((byte)14, new byte[]{6, 7, 43, 18, 0, 4, 1, 1, 14}, "TIFF", true, "image/tiff"));
      this.mimeObjectTypeMap.put("image/gif", new ObjectType((byte)22, new byte[]{6, 7, 43, 18, 0, 4, 1, 1, 22}, "GIF", true, "image/gif"));
      this.mimeObjectTypeMap.put("image/jpeg", new ObjectType((byte)23, new byte[]{6, 7, 43, 18, 0, 4, 1, 1, 23}, "JFIF", true, "image/jpeg"));
      this.mimeObjectTypeMap.put("application/pdf", new ObjectType((byte)25, new byte[]{6, 7, 43, 18, 0, 4, 1, 1, 25}, "PDF Single-page Object", true, "application/pdf"));
      this.mimeObjectTypeMap.put("application/x-pcl", new ObjectType((byte)34, new byte[]{6, 7, 43, 18, 0, 4, 1, 1, 34}, "PCL Page Object", true, "application/x-pcl"));
   }

   public ObjectType getObjectType(String mimeType) {
      return (ObjectType)this.mimeObjectTypeMap.get(mimeType);
   }

   public class ObjectType {
      private final byte componentId;
      private final byte[] oid;
      private final String name;
      private final boolean includable;
      private final String mimeType;

      public ObjectType(byte componentId, byte[] oid, String name, boolean includable, String mimeType) {
         this.componentId = componentId;
         this.oid = oid;
         this.name = name;
         this.includable = includable;
         this.mimeType = mimeType;
      }

      public byte[] getOID() {
         return this.oid;
      }

      public String getName() {
         return this.name;
      }

      public byte getComponentId() {
         return this.componentId;
      }

      public boolean isIncludable() {
         return this.includable;
      }

      public String getMimeType() {
         return this.mimeType;
      }

      public String toString() {
         return this.getName();
      }
   }
}
