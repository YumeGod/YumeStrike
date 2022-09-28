package org.apache.james.mime4j.dom.field;

public interface MimeVersionField extends ParsedField {
   int getMinorVersion();

   int getMajorVersion();
}
