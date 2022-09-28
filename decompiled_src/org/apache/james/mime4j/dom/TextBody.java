package org.apache.james.mime4j.dom;

import java.io.IOException;
import java.io.Reader;

public abstract class TextBody extends SingleBody {
   protected TextBody() {
   }

   public abstract String getMimeCharset();

   public abstract Reader getReader() throws IOException;
}
