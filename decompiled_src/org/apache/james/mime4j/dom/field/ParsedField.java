package org.apache.james.mime4j.dom.field;

import org.apache.james.mime4j.stream.Field;

public interface ParsedField extends Field {
   boolean isValidField();

   ParseException getParseException();
}
