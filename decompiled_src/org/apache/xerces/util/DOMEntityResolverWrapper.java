package org.apache.xerces.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.grammars.XMLGrammarDescription;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

public class DOMEntityResolverWrapper implements XMLEntityResolver {
   private static final String XML_TYPE = "http://www.w3.org/TR/REC-xml";
   private static final String XSD_TYPE = "http://www.w3.org/2001/XMLSchema";
   protected LSResourceResolver fEntityResolver;

   public DOMEntityResolverWrapper() {
   }

   public DOMEntityResolverWrapper(LSResourceResolver var1) {
      this.setEntityResolver(var1);
   }

   public void setEntityResolver(LSResourceResolver var1) {
      this.fEntityResolver = var1;
   }

   public LSResourceResolver getEntityResolver() {
      return this.fEntityResolver;
   }

   public XMLInputSource resolveEntity(XMLResourceIdentifier var1) throws XNIException, IOException {
      if (this.fEntityResolver != null) {
         LSInput var2 = var1 == null ? this.fEntityResolver.resolveResource((String)null, (String)null, (String)null, (String)null, (String)null) : this.fEntityResolver.resolveResource(this.getType(var1), var1.getNamespace(), var1.getPublicId(), var1.getLiteralSystemId(), var1.getBaseSystemId());
         if (var2 != null) {
            String var3 = var2.getPublicId();
            String var4 = var2.getSystemId();
            String var5 = var2.getBaseURI();
            InputStream var6 = var2.getByteStream();
            Reader var7 = var2.getCharacterStream();
            String var8 = var2.getEncoding();
            String var9 = var2.getStringData();
            XMLInputSource var10 = new XMLInputSource(var3, var4, var5);
            if (var7 != null) {
               var10.setCharacterStream(var7);
            } else if (var6 != null) {
               var10.setByteStream(var6);
            } else if (var9 != null && var9.length() != 0) {
               var10.setCharacterStream(new StringReader(var9));
            }

            var10.setEncoding(var8);
            return var10;
         }
      }

      return null;
   }

   private String getType(XMLResourceIdentifier var1) {
      if (var1 instanceof XMLGrammarDescription) {
         XMLGrammarDescription var2 = (XMLGrammarDescription)var1;
         if ("http://www.w3.org/2001/XMLSchema".equals(var2.getGrammarType())) {
            return "http://www.w3.org/2001/XMLSchema";
         }
      }

      return "http://www.w3.org/TR/REC-xml";
   }
}
