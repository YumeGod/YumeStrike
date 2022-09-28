package org.apache.fop.image.loader.batik;

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.util.DOMUtilities;
import org.w3c.dom.Document;

public class BatikUtil {
   public static boolean isBatikAvailable() {
      try {
         Class.forName("org.apache.batik.dom.svg.SVGDOMImplementation");
         return true;
      } catch (Exception var1) {
         return false;
      }
   }

   public static Document cloneSVGDocument(Document doc) {
      Document clonedDoc = DOMUtilities.deepCloneDocument(doc, doc.getImplementation());
      if (clonedDoc instanceof AbstractDocument) {
         ((AbstractDocument)clonedDoc).setDocumentURI(((AbstractDocument)doc).getDocumentURI());
      }

      return clonedDoc;
   }
}
