package org.apache.james.mime4j.dom;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.james.mime4j.stream.Field;

public interface MessageWriter {
   void writeMessage(Message var1, OutputStream var2) throws IOException;

   void writeBody(Body var1, OutputStream var2) throws IOException;

   void writeEntity(Entity var1, OutputStream var2) throws IOException;

   void writeMultipart(Multipart var1, OutputStream var2) throws IOException;

   void writeField(Field var1, OutputStream var2) throws IOException;

   void writeHeader(Header var1, OutputStream var2) throws IOException;
}
