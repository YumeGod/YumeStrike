package org.apache.batik.util;

public abstract class AbstractParsedURLProtocolHandler implements ParsedURLProtocolHandler {
   protected String protocol;

   public AbstractParsedURLProtocolHandler(String var1) {
      this.protocol = var1;
   }

   public String getProtocolHandled() {
      return this.protocol;
   }
}
