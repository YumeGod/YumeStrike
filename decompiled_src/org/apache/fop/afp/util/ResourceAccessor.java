package org.apache.fop.afp.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public interface ResourceAccessor {
   InputStream createInputStream(URI var1) throws IOException;
}
