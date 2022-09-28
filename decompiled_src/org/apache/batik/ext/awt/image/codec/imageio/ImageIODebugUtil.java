package org.apache.batik.ext.awt.image.codec.imageio;

import javax.imageio.metadata.IIOMetadata;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Node;

public class ImageIODebugUtil {
   public static void dumpMetadata(IIOMetadata var0) {
      String var1 = var0.getNativeMetadataFormatName();
      Node var2 = var0.getAsTree(var1);
      dumpNode(var2);
   }

   public static void dumpNode(Node var0) {
      try {
         TransformerFactory var1 = TransformerFactory.newInstance();
         Transformer var2 = var1.newTransformer();
         DOMSource var3 = new DOMSource(var0);
         StreamResult var4 = new StreamResult(System.out);
         var2.transform(var3, var4);
         System.out.println();
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }
}
