package org.apache.xerces.impl.xs;

import org.apache.xerces.xs.XSAnnotation;
import org.apache.xerces.xs.XSModelGroup;
import org.apache.xerces.xs.XSModelGroupDefinition;
import org.apache.xerces.xs.XSNamespaceItem;

public class XSGroupDecl implements XSModelGroupDefinition {
   public String fName = null;
   public String fTargetNamespace = null;
   public XSModelGroupImpl fModelGroup = null;
   public XSAnnotationImpl fAnnotation = null;

   public short getType() {
      return 6;
   }

   public String getName() {
      return this.fName;
   }

   public String getNamespace() {
      return this.fTargetNamespace;
   }

   public XSModelGroup getModelGroup() {
      return this.fModelGroup;
   }

   public XSAnnotation getAnnotation() {
      return this.fAnnotation;
   }

   public XSNamespaceItem getNamespaceItem() {
      return null;
   }
}
