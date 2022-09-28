package org.apache.batik.ext.awt.image.spi;

import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.util.ParsedURL;

public interface URLRegistryEntry extends RegistryEntry {
   boolean isCompatibleURL(ParsedURL var1);

   Filter handleURL(ParsedURL var1, boolean var2);
}
