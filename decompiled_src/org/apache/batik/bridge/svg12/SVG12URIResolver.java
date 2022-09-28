package org.apache.batik.bridge.svg12;

import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.URIResolver;
import org.apache.batik.dom.AbstractNode;
import org.apache.batik.dom.xbl.NodeXBL;
import org.apache.batik.dom.xbl.XBLShadowTreeElement;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;

public class SVG12URIResolver extends URIResolver {
   public SVG12URIResolver(SVGDocument var1, DocumentLoader var2) {
      super(var1, var2);
   }

   protected String getRefererBaseURI(Element var1) {
      AbstractNode var2 = (AbstractNode)var1;
      return var2.getXblBoundElement() != null ? null : var2.getBaseURI();
   }

   protected Node getNodeByFragment(String var1, Element var2) {
      NodeXBL var3 = (NodeXBL)var2;
      NodeXBL var4 = (NodeXBL)var3.getXblBoundElement();
      if (var4 != null) {
         XBLShadowTreeElement var5 = (XBLShadowTreeElement)var4.getXblShadowTree();
         Element var6 = var5.getElementById(var1);
         if (var6 != null) {
            return var6;
         }

         NodeList var7 = var3.getXblDefinitions();

         for(int var8 = 0; var8 < var7.getLength(); ++var8) {
            var6 = var7.item(var8).getOwnerDocument().getElementById(var1);
            if (var6 != null) {
               return var6;
            }
         }
      }

      return super.getNodeByFragment(var1, var2);
   }
}
