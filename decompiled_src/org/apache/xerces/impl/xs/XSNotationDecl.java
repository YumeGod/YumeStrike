package org.apache.xerces.impl.xs;

import org.apache.xerces.xs.XSAnnotation;
import org.apache.xerces.xs.XSNamespaceItem;
import org.apache.xerces.xs.XSNotationDeclaration;

public class XSNotationDecl implements XSNotationDeclaration {
   public String fName = null;
   public String fTargetNamespace = null;
   public String fPublicId = null;
   public String fSystemId = null;
   public XSAnnotationImpl fAnnotation = null;

   public short getType() {
      return 11;
   }

   public String getName() {
      return this.fName;
   }

   public String getNamespace() {
      return this.fTargetNamespace;
   }

   public String getSystemId() {
      return this.fSystemId;
   }

   public String getPublicId() {
      return this.fPublicId;
   }

   public XSAnnotation getAnnotation() {
      return this.fAnnotation;
   }

   public XSNamespaceItem getNamespaceItem() {
      return null;
   }
}
