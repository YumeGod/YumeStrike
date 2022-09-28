package org.apache.batik.transcoder;

import java.io.IOException;
import org.apache.batik.dom.util.DocumentFactory;
import org.apache.batik.dom.util.SAXDocumentFactory;
import org.apache.batik.transcoder.keys.BooleanKey;
import org.apache.batik.transcoder.keys.DOMImplementationKey;
import org.apache.batik.transcoder.keys.StringKey;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

public abstract class XMLAbstractTranscoder extends AbstractTranscoder {
   public static final TranscodingHints.Key KEY_XML_PARSER_CLASSNAME = new StringKey();
   public static final TranscodingHints.Key KEY_XML_PARSER_VALIDATING = new BooleanKey();
   public static final TranscodingHints.Key KEY_DOCUMENT_ELEMENT = new StringKey();
   public static final TranscodingHints.Key KEY_DOCUMENT_ELEMENT_NAMESPACE_URI = new StringKey();
   public static final TranscodingHints.Key KEY_DOM_IMPLEMENTATION = new DOMImplementationKey();

   protected XMLAbstractTranscoder() {
      this.hints.put(KEY_XML_PARSER_VALIDATING, Boolean.FALSE);
   }

   public void transcode(TranscoderInput var1, TranscoderOutput var2) throws TranscoderException {
      Document var3 = null;
      String var4 = var1.getURI();
      if (var1.getDocument() != null) {
         var3 = var1.getDocument();
      } else {
         String var5 = (String)this.hints.get(KEY_XML_PARSER_CLASSNAME);
         String var6 = (String)this.hints.get(KEY_DOCUMENT_ELEMENT_NAMESPACE_URI);
         String var7 = (String)this.hints.get(KEY_DOCUMENT_ELEMENT);
         DOMImplementation var8 = (DOMImplementation)this.hints.get(KEY_DOM_IMPLEMENTATION);
         if (var5 == null) {
            var5 = XMLResourceDescriptor.getXMLParserClassName();
         }

         if (var8 == null) {
            this.handler.fatalError(new TranscoderException("Unspecified transcoding hints: KEY_DOM_IMPLEMENTATION"));
            return;
         }

         if (var6 == null) {
            this.handler.fatalError(new TranscoderException("Unspecified transcoding hints: KEY_DOCUMENT_ELEMENT_NAMESPACE_URI"));
            return;
         }

         if (var7 == null) {
            this.handler.fatalError(new TranscoderException("Unspecified transcoding hints: KEY_DOCUMENT_ELEMENT"));
            return;
         }

         DocumentFactory var9 = this.createDocumentFactory(var8, var5);
         boolean var10 = (Boolean)this.hints.get(KEY_XML_PARSER_VALIDATING);
         var9.setValidating(var10);

         try {
            if (var1.getInputStream() != null) {
               var3 = var9.createDocument(var6, var7, var1.getURI(), var1.getInputStream());
            } else if (var1.getReader() != null) {
               var3 = var9.createDocument(var6, var7, var1.getURI(), var1.getReader());
            } else if (var1.getXMLReader() != null) {
               var3 = var9.createDocument(var6, var7, var1.getURI(), var1.getXMLReader());
            } else if (var4 != null) {
               var3 = var9.createDocument(var6, var7, var4);
            }
         } catch (DOMException var13) {
            this.handler.fatalError(new TranscoderException(var13));
         } catch (IOException var14) {
            this.handler.fatalError(new TranscoderException(var14));
         }
      }

      if (var3 != null) {
         try {
            this.transcode(var3, var4, var2);
         } catch (TranscoderException var12) {
            this.handler.fatalError(var12);
            return;
         }
      }

   }

   protected DocumentFactory createDocumentFactory(DOMImplementation var1, String var2) {
      return new SAXDocumentFactory(var1, var2);
   }

   protected abstract void transcode(Document var1, String var2, TranscoderOutput var3) throws TranscoderException;
}
