package org.apache.fop.render;

import org.w3c.dom.Document;

public interface XMLHandler {
   String HANDLE_ALL = "*";

   void handleXML(RendererContext var1, Document var2, String var3) throws Exception;

   boolean supportsRenderer(Renderer var1);

   String getNamespace();
}
