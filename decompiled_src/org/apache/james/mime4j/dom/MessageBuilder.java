package org.apache.james.mime4j.dom;

import java.io.IOException;
import java.io.InputStream;
import org.apache.james.mime4j.MimeException;

public interface MessageBuilder {
   Header newHeader();

   Header newHeader(Header var1);

   Multipart newMultipart(String var1);

   Multipart newMultipart(Multipart var1);

   Message newMessage();

   Message newMessage(Message var1);

   Header parseHeader(InputStream var1) throws MimeException, IOException;

   Message parseMessage(InputStream var1) throws MimeException, IOException;
}
