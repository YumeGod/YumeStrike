package org.apache.fop.afp.modca.triplets;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class FullyQualifiedNameTriplet extends AbstractTriplet {
   public static final byte TYPE_REPLACE_FIRST_GID_NAME = 1;
   public static final byte TYPE_FONT_FAMILY_NAME = 7;
   public static final byte TYPE_FONT_TYPEFACE_NAME = 8;
   public static final byte TYPE_MODCA_RESOURCE_HIERARCHY_REF = 9;
   public static final byte TYPE_BEGIN_RESOURCE_GROUP_REF = 10;
   public static final byte TYPE_ATTRIBUTE_GID = 11;
   public static final byte TYPE_PROCESS_ELEMENT_GID = 12;
   public static final byte TYPE_BEGIN_PAGE_GROUP_REF = 13;
   public static final byte TYPE_MEDIA_TYPE_REF = 17;
   public static final byte TYPE_COLOR_MANAGEMENT_RESOURCE_REF = 65;
   public static final byte TYPE_DATA_OBJECT_FONT_BASE_FONT_ID = 110;
   public static final byte TYPE_DATA_OBJECT_FONT_LINKED_FONT_ID = 126;
   public static final byte TYPE_BEGIN_DOCUMENT_REF = -125;
   public static final byte TYPE_BEGIN_RESOURCE_OBJECT_REF = -124;
   public static final byte TYPE_CODE_PAGE_NAME_REF = -123;
   public static final byte TYPE_FONT_CHARSET_NAME_REF = -122;
   public static final byte TYPE_BEGIN_PAGE_REF = -121;
   public static final byte TYPE_BEGIN_MEDIUM_MAP_REF = -115;
   public static final byte TYPE_CODED_FONT_NAME_REF = -114;
   public static final byte TYPE_BEGIN_DOCUMENT_INDEX_REF = -104;
   public static final byte TYPE_BEGIN_OVERLAY_REF = -80;
   public static final byte TYPE_DATA_OBJECT_INTERNAL_RESOURCE_REF = -66;
   public static final byte TYPE_INDEX_ELEMENT_GID = -54;
   public static final byte TYPE_OTHER_OBJECT_DATA_REF = -50;
   public static final byte TYPE_DATA_OBJECT_EXTERNAL_RESOURCE_REF = -34;
   public static final byte FORMAT_CHARSTR = 0;
   public static final byte FORMAT_OID = 16;
   public static final byte FORMAT_URL = 32;
   private final byte type;
   private final byte format;
   private final String fqName;

   public FullyQualifiedNameTriplet(byte type, byte format, String fqName) {
      super((byte)2);
      this.type = type;
      this.format = format;
      this.fqName = fqName;
   }

   public String getFullyQualifiedName() {
      return this.fqName;
   }

   public String toString() {
      return this.fqName;
   }

   public int getDataLength() {
      return 4 + this.fqName.length();
   }

   public void writeToStream(OutputStream os) throws IOException {
      byte[] data = this.getData();
      data[2] = this.type;
      data[3] = this.format;
      String encoding = "Cp1146";
      if (this.format == 32) {
         encoding = "US-ASCII";
      }

      byte[] fqNameBytes;
      try {
         fqNameBytes = this.fqName.getBytes(encoding);
      } catch (UnsupportedEncodingException var6) {
         throw new IllegalArgumentException(encoding + " encoding failed");
      }

      System.arraycopy(fqNameBytes, 0, data, 4, fqNameBytes.length);
      os.write(data);
   }
}
