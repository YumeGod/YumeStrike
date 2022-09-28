package org.apache.james.mime4j.stream;

public interface ContentDescriptor {
   String getMimeType();

   String getMediaType();

   String getSubType();

   String getCharset();

   String getTransferEncoding();

   long getContentLength();
}
