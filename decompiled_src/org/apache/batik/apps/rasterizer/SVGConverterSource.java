package org.apache.batik.apps.rasterizer;

import java.io.IOException;
import java.io.InputStream;

public interface SVGConverterSource {
   String getName();

   InputStream openStream() throws IOException;

   boolean isSameAs(String var1);

   boolean isReadable();

   String getURI();
}
