package org.apache.xerces.dom;

import org.w3c.dom.DOMLocator;
import org.w3c.dom.Node;

public class DOMLocatorImpl implements DOMLocator {
   public int fColumnNumber = -1;
   public int fLineNumber = -1;
   public Node fRelatedNode = null;
   public String fUri = null;
   public int fByteOffset = -1;
   public int fUtf16Offset = -1;

   public DOMLocatorImpl() {
   }

   public DOMLocatorImpl(int var1, int var2, String var3) {
      this.fLineNumber = var1;
      this.fColumnNumber = var2;
      this.fUri = var3;
   }

   public DOMLocatorImpl(int var1, int var2, int var3, String var4) {
      this.fLineNumber = var1;
      this.fColumnNumber = var2;
      this.fUri = var4;
      this.fUtf16Offset = var3;
   }

   public DOMLocatorImpl(int var1, int var2, int var3, Node var4, String var5) {
      this.fLineNumber = var1;
      this.fColumnNumber = var2;
      this.fByteOffset = var3;
      this.fRelatedNode = var4;
      this.fUri = var5;
   }

   public DOMLocatorImpl(int var1, int var2, int var3, Node var4, String var5, int var6) {
      this.fLineNumber = var1;
      this.fColumnNumber = var2;
      this.fByteOffset = var3;
      this.fRelatedNode = var4;
      this.fUri = var5;
      this.fUtf16Offset = var6;
   }

   public int getLineNumber() {
      return this.fLineNumber;
   }

   public int getColumnNumber() {
      return this.fColumnNumber;
   }

   public String getUri() {
      return this.fUri;
   }

   public Node getRelatedNode() {
      return this.fRelatedNode;
   }

   public int getByteOffset() {
      return this.fByteOffset;
   }

   public int getUtf16Offset() {
      return this.fUtf16Offset;
   }
}
