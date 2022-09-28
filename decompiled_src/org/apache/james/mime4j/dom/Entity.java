package org.apache.james.mime4j.dom;

public interface Entity extends Disposable {
   Entity getParent();

   void setParent(Entity var1);

   Header getHeader();

   void setHeader(Header var1);

   Body getBody();

   void setBody(Body var1);

   Body removeBody();

   boolean isMultipart();

   String getMimeType();

   String getCharset();

   String getContentTransferEncoding();

   String getDispositionType();

   String getFilename();
}
