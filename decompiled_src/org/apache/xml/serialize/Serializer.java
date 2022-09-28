package org.apache.xml.serialize;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import org.xml.sax.ContentHandler;
import org.xml.sax.DocumentHandler;

public interface Serializer {
   void setOutputByteStream(OutputStream var1);

   void setOutputCharStream(Writer var1);

   void setOutputFormat(OutputFormat var1);

   DocumentHandler asDocumentHandler() throws IOException;

   ContentHandler asContentHandler() throws IOException;

   DOMSerializer asDOMSerializer() throws IOException;
}
