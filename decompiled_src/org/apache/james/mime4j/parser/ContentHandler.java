package org.apache.james.mime4j.parser;

import java.io.IOException;
import java.io.InputStream;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.stream.BodyDescriptor;
import org.apache.james.mime4j.stream.Field;

public interface ContentHandler {
   void startMessage() throws MimeException;

   void endMessage() throws MimeException;

   void startBodyPart() throws MimeException;

   void endBodyPart() throws MimeException;

   void startHeader() throws MimeException;

   void field(Field var1) throws MimeException;

   void endHeader() throws MimeException;

   void preamble(InputStream var1) throws MimeException, IOException;

   void epilogue(InputStream var1) throws MimeException, IOException;

   void startMultipart(BodyDescriptor var1) throws MimeException;

   void endMultipart() throws MimeException;

   void body(BodyDescriptor var1, InputStream var2) throws MimeException, IOException;

   void raw(InputStream var1) throws MimeException, IOException;
}
