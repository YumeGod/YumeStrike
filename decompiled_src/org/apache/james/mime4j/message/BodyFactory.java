package org.apache.james.mime4j.message;

import java.io.IOException;
import java.io.InputStream;
import org.apache.james.mime4j.dom.BinaryBody;
import org.apache.james.mime4j.dom.TextBody;

public interface BodyFactory {
   BinaryBody binaryBody(InputStream var1) throws IOException;

   TextBody textBody(InputStream var1, String var2) throws IOException;
}
