package org.apache.batik.bridge;

import java.awt.color.ICC_Profile;
import java.io.IOException;
import org.apache.batik.dom.AbstractNode;
import org.apache.batik.dom.util.XLinkSupport;
import org.apache.batik.ext.awt.color.ICCColorSpaceExt;
import org.apache.batik.ext.awt.color.NamedProfileCache;
import org.apache.batik.util.ParsedURL;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SVGColorProfileElementBridge extends AbstractSVGBridge implements ErrorConstants {
   public NamedProfileCache cache = new NamedProfileCache();

   public String getLocalName() {
      return "color-profile";
   }

   public ICCColorSpaceExt createICCColorSpaceExt(BridgeContext var1, Element var2, String var3) {
      ICCColorSpaceExt var4 = this.cache.request(var3.toLowerCase());
      if (var4 != null) {
         return var4;
      } else {
         Document var5 = var2.getOwnerDocument();
         NodeList var6 = var5.getElementsByTagNameNS("http://www.w3.org/2000/svg", "color-profile");
         int var7 = var6.getLength();
         Element var8 = null;

         for(int var9 = 0; var9 < var7; ++var9) {
            Node var10 = var6.item(var9);
            if (var10.getNodeType() == 1) {
               Element var11 = (Element)var10;
               String var12 = var11.getAttributeNS((String)null, "name");
               if (var3.equalsIgnoreCase(var12)) {
                  var8 = var11;
               }
            }
         }

         if (var8 == null) {
            return null;
         } else {
            String var17 = XLinkSupport.getXLinkHref(var8);
            ICC_Profile var18 = null;
            if (var17 != null) {
               String var19 = ((AbstractNode)var8).getBaseURI();
               ParsedURL var21 = null;
               if (var19 != null) {
                  var21 = new ParsedURL(var19);
               }

               ParsedURL var13 = new ParsedURL(var21, var17);
               if (!var13.complete()) {
                  throw new BridgeException(var1, var2, "uri.malformed", new Object[]{var17});
               }

               try {
                  var1.getUserAgent().checkLoadExternalResource(var13, var21);
                  var18 = ICC_Profile.getInstance(var13.openStream());
               } catch (IOException var15) {
                  throw new BridgeException(var1, var2, var15, "uri.io", new Object[]{var17});
               } catch (SecurityException var16) {
                  throw new BridgeException(var1, var2, var16, "uri.unsecure", new Object[]{var17});
               }
            }

            if (var18 == null) {
               return null;
            } else {
               int var20 = convertIntent(var8, var1);
               var4 = new ICCColorSpaceExt(var18, var20);
               this.cache.put(var3.toLowerCase(), var4);
               return var4;
            }
         }
      }
   }

   private static int convertIntent(Element var0, BridgeContext var1) {
      String var2 = var0.getAttributeNS((String)null, "rendering-intent");
      if (var2.length() == 0) {
         return 4;
      } else if ("perceptual".equals(var2)) {
         return 0;
      } else if ("auto".equals(var2)) {
         return 4;
      } else if ("relative-colorimetric".equals(var2)) {
         return 1;
      } else if ("absolute-colorimetric".equals(var2)) {
         return 2;
      } else if ("saturation".equals(var2)) {
         return 3;
      } else {
         throw new BridgeException(var1, var0, "attribute.malformed", new Object[]{"rendering-intent", var2});
      }
   }
}
