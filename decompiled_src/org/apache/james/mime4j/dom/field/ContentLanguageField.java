package org.apache.james.mime4j.dom.field;

import java.util.List;

public interface ContentLanguageField extends ParsedField {
   List getLanguages();
}
