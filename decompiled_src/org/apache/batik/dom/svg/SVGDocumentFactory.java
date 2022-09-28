package org.apache.batik.dom.svg;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import org.apache.batik.dom.util.DocumentFactory;
import org.w3c.dom.svg.SVGDocument;

public interface SVGDocumentFactory extends DocumentFactory {
   SVGDocument createSVGDocument(String var1) throws IOException;

   SVGDocument createSVGDocument(String var1, InputStream var2) throws IOException;

   SVGDocument createSVGDocument(String var1, Reader var2) throws IOException;
}
