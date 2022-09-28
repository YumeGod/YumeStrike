package org.apache.xerces.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import org.apache.xerces.impl.ExternalSubsetResolver;
import org.apache.xerces.impl.XMLEntityDescription;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.grammars.XMLDTDDescription;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ext.EntityResolver2;

public class EntityResolver2Wrapper implements ExternalSubsetResolver {
   protected EntityResolver2 fEntityResolver;

   public EntityResolver2Wrapper() {
   }

   public EntityResolver2Wrapper(EntityResolver2 var1) {
      this.setEntityResolver(var1);
   }

   public void setEntityResolver(EntityResolver2 var1) {
      this.fEntityResolver = var1;
   }

   public EntityResolver2 getEntityResolver() {
      return this.fEntityResolver;
   }

   public XMLInputSource getExternalSubset(XMLDTDDescription var1) throws XNIException, IOException {
      if (this.fEntityResolver != null) {
         String var2 = var1.getRootName();
         String var3 = var1.getBaseSystemId();

         try {
            InputSource var4 = this.fEntityResolver.getExternalSubset(var2, var3);
            return var4 != null ? this.createXMLInputSource(var4, var3) : null;
         } catch (SAXException var6) {
            Object var5 = var6.getException();
            if (var5 == null) {
               var5 = var6;
            }

            throw new XNIException((Exception)var5);
         }
      } else {
         return null;
      }
   }

   public XMLInputSource resolveEntity(XMLResourceIdentifier var1) throws XNIException, IOException {
      if (this.fEntityResolver != null) {
         String var2 = var1.getPublicId();
         String var3 = var1.getLiteralSystemId();
         String var4 = var1.getBaseSystemId();
         String var5 = null;
         if (var1 instanceof XMLDTDDescription) {
            var5 = "[dtd]";
         } else if (var1 instanceof XMLEntityDescription) {
            var5 = ((XMLEntityDescription)var1).getEntityName();
         }

         if (var2 == null && var3 == null) {
            return null;
         } else {
            try {
               InputSource var6 = this.fEntityResolver.resolveEntity(var5, var2, var4, var3);
               return var6 != null ? this.createXMLInputSource(var6, var4) : null;
            } catch (SAXException var8) {
               Object var7 = var8.getException();
               if (var7 == null) {
                  var7 = var8;
               }

               throw new XNIException((Exception)var7);
            }
         }
      } else {
         return null;
      }
   }

   private XMLInputSource createXMLInputSource(InputSource var1, String var2) {
      String var3 = var1.getPublicId();
      String var4 = var1.getSystemId();
      InputStream var6 = var1.getByteStream();
      Reader var7 = var1.getCharacterStream();
      String var8 = var1.getEncoding();
      XMLInputSource var9 = new XMLInputSource(var3, var4, var2);
      var9.setByteStream(var6);
      var9.setCharacterStream(var7);
      var9.setEncoding(var8);
      return var9;
   }
}
