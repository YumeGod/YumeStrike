package org.apache.xerces.impl.xs.traversers;

import org.apache.xerces.impl.xs.opti.ElementImpl;
import org.w3c.dom.Element;

final class XSAnnotationInfo {
   String fAnnotation;
   int fLine;
   int fColumn;
   int fCharOffset;
   XSAnnotationInfo next;

   XSAnnotationInfo(String var1, int var2, int var3, int var4) {
      this.fAnnotation = var1;
      this.fLine = var2;
      this.fColumn = var3;
      this.fCharOffset = var4;
   }

   XSAnnotationInfo(String var1, Element var2) {
      this.fAnnotation = var1;
      if (var2 instanceof ElementImpl) {
         ElementImpl var3 = (ElementImpl)var2;
         this.fLine = var3.getLineNumber();
         this.fColumn = var3.getColumnNumber();
         this.fCharOffset = var3.getCharacterOffset();
      } else {
         this.fLine = -1;
         this.fColumn = -1;
         this.fCharOffset = -1;
      }

   }
}
