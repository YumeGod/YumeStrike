package org.apache.james.mime4j.dom.field;

import java.util.Date;
import java.util.Map;

public interface ContentDispositionField extends ParsedField {
   String DISPOSITION_TYPE_INLINE = "inline";
   String DISPOSITION_TYPE_ATTACHMENT = "attachment";
   String PARAM_FILENAME = "filename";
   String PARAM_CREATION_DATE = "creation-date";
   String PARAM_MODIFICATION_DATE = "modification-date";
   String PARAM_READ_DATE = "read-date";
   String PARAM_SIZE = "size";

   String getDispositionType();

   String getParameter(String var1);

   Map getParameters();

   boolean isDispositionType(String var1);

   boolean isInline();

   boolean isAttachment();

   String getFilename();

   Date getCreationDate();

   Date getModificationDate();

   Date getReadDate();

   long getSize();
}
