package org.apache.james.mime4j.dom;

import org.apache.james.mime4j.codec.DecodeMonitor;
import org.apache.james.mime4j.dom.field.ParsedField;
import org.apache.james.mime4j.stream.Field;

public interface FieldParser {
   ParsedField parse(Field var1, DecodeMonitor var2);
}
