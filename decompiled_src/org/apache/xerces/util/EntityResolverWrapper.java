package org.apache.xerces.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class EntityResolverWrapper implements XMLEntityResolver {
   protected EntityResolver fEntityResolver;

   public EntityResolverWrapper() {
   }

   public EntityResolverWrapper(EntityResolver var1) {
      this.setEntityResolver(var1);
   }

   public void setEntityResolver(EntityResolver var1) {
      this.fEntityResolver = var1;
   }

   public EntityResolver getEntityResolver() {
      return this.fEntityResolver;
   }

   public XMLInputSource resolveEntity(XMLResourceIdentifier var1) throws XNIException, IOException {
      String var2 = var1.getPublicId();
      String var3 = var1.getExpandedSystemId();
      if (var2 == null && var3 == null) {
         return null;
      } else {
         if (this.fEntityResolver != null && var1 != null) {
            try {
               InputSource var4 = this.fEntityResolver.resolveEntity(var2, var3);
               if (var4 != null) {
                  String var13 = var4.getPublicId();
                  String var6 = var4.getSystemId();
                  String var7 = var1.getBaseSystemId();
                  InputStream var8 = var4.getByteStream();
                  Reader var9 = var4.getCharacterStream();
                  String var10 = var4.getEncoding();
                  XMLInputSource var11 = new XMLInputSource(var13, var6, var7);
                  var11.setByteStream(var8);
                  var11.setCharacterStream(var9);
                  var11.setEncoding(var10);
                  return var11;
               }
            } catch (SAXException var12) {
               Object var5 = var12.getException();
               if (var5 == null) {
                  var5 = var12;
               }

               throw new XNIException((Exception)var5);
            }
         }

         return null;
      }
   }
}
