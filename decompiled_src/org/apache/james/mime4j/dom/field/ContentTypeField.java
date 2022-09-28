package org.apache.james.mime4j.dom.field;

import java.util.Map;

public interface ContentTypeField extends ParsedField {
   String TYPE_MULTIPART_PREFIX = "multipart/";
   String TYPE_MULTIPART_DIGEST = "multipart/digest";
   String TYPE_TEXT_PLAIN = "text/plain";
   String TYPE_MESSAGE_RFC822 = "message/rfc822";
   String PARAM_BOUNDARY = "boundary";
   String PARAM_CHARSET = "charset";

   String getMimeType();

   String getMediaType();

   String getSubType();

   String getParameter(String var1);

   Map getParameters();

   boolean isMimeType(String var1);

   boolean isMultipart();

   String getBoundary();

   String getCharset();
}
