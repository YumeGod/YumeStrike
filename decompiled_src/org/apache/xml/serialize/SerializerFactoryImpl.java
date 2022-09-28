package org.apache.xml.serialize;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import org.apache.xerces.dom.DOMMessageFormatter;

final class SerializerFactoryImpl extends SerializerFactory {
   private String _method;

   SerializerFactoryImpl(String var1) {
      this._method = var1;
      if (!this._method.equals("xml") && !this._method.equals("html") && !this._method.equals("xhtml") && !this._method.equals("text")) {
         String var2 = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "MethodNotSupported", new Object[]{var1});
         throw new IllegalArgumentException(var2);
      }
   }

   public Serializer makeSerializer(OutputFormat var1) {
      Serializer var2 = this.getSerializer(var1);
      var2.setOutputFormat(var1);
      return var2;
   }

   public Serializer makeSerializer(Writer var1, OutputFormat var2) {
      Serializer var3 = this.getSerializer(var2);
      var3.setOutputCharStream(var1);
      return var3;
   }

   public Serializer makeSerializer(OutputStream var1, OutputFormat var2) throws UnsupportedEncodingException {
      Serializer var3 = this.getSerializer(var2);
      var3.setOutputByteStream(var1);
      return var3;
   }

   private Serializer getSerializer(OutputFormat var1) {
      if (this._method.equals("xml")) {
         return new XMLSerializer(var1);
      } else if (this._method.equals("html")) {
         return new HTMLSerializer(var1);
      } else if (this._method.equals("xhtml")) {
         return new XHTMLSerializer(var1);
      } else if (this._method.equals("text")) {
         return new TextSerializer();
      } else {
         String var2 = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "MethodNotSupported", new Object[]{this._method});
         throw new IllegalStateException(var2);
      }
   }

   protected String getSupportedMethod() {
      return this._method;
   }
}
