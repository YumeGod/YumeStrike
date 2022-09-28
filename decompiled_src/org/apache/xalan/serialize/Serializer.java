package org.apache.xalan.serialize;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Properties;
import org.xml.sax.ContentHandler;

/** @deprecated */
public interface Serializer {
   /** @deprecated */
   void setOutputStream(OutputStream var1);

   /** @deprecated */
   OutputStream getOutputStream();

   /** @deprecated */
   void setWriter(Writer var1);

   /** @deprecated */
   Writer getWriter();

   /** @deprecated */
   void setOutputFormat(Properties var1);

   /** @deprecated */
   Properties getOutputFormat();

   /** @deprecated */
   ContentHandler asContentHandler() throws IOException;

   /** @deprecated */
   DOMSerializer asDOMSerializer() throws IOException;

   /** @deprecated */
   boolean reset();
}
