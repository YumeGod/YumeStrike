package org.apache.xml.serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Properties;
import org.xml.sax.ContentHandler;

public interface Serializer {
   void setOutputStream(OutputStream var1);

   OutputStream getOutputStream();

   void setWriter(Writer var1);

   Writer getWriter();

   void setOutputFormat(Properties var1);

   Properties getOutputFormat();

   ContentHandler asContentHandler() throws IOException;

   DOMSerializer asDOMSerializer() throws IOException;

   boolean reset();
}
