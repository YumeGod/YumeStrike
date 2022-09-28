package org.apache.xmlgraphics.xmp;

import java.util.HashMap;
import java.util.Map;
import org.apache.xmlgraphics.xmp.schemas.DublinCoreSchema;
import org.apache.xmlgraphics.xmp.schemas.XMPBasicSchema;
import org.apache.xmlgraphics.xmp.schemas.pdf.AdobePDFSchema;
import org.apache.xmlgraphics.xmp.schemas.pdf.PDFAOldXMPSchema;
import org.apache.xmlgraphics.xmp.schemas.pdf.PDFAXMPSchema;

public class XMPSchemaRegistry {
   private static XMPSchemaRegistry instance = null;
   private Map schemas = new HashMap();

   private XMPSchemaRegistry() {
      this.init();
   }

   public static XMPSchemaRegistry getInstance() {
      if (instance == null) {
         instance = new XMPSchemaRegistry();
      }

      return instance;
   }

   private void init() {
      this.addSchema(new DublinCoreSchema());
      this.addSchema(new PDFAXMPSchema());
      this.addSchema(new PDFAOldXMPSchema());
      this.addSchema(new XMPBasicSchema());
      this.addSchema(new AdobePDFSchema());
   }

   public void addSchema(XMPSchema schema) {
      this.schemas.put(schema.getNamespace(), schema);
   }

   public XMPSchema getSchema(String namespace) {
      return (XMPSchema)this.schemas.get(namespace);
   }
}
