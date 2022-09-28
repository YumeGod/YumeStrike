package org.apache.batik.ext.awt.image.spi;

import java.io.InputStream;
import java.io.StreamCorruptedException;
import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.util.ParsedURL;

public interface StreamRegistryEntry extends RegistryEntry {
   int getReadlimit();

   boolean isCompatibleStream(InputStream var1) throws StreamCorruptedException;

   Filter handleStream(InputStream var1, ParsedURL var2, boolean var3);
}
