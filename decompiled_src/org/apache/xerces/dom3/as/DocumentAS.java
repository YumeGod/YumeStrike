package org.apache.xerces.dom3.as;

import org.w3c.dom.DOMException;

/** @deprecated */
public interface DocumentAS {
   ASModel getActiveASModel();

   void setActiveASModel(ASModel var1);

   ASObjectList getBoundASModels();

   void setBoundASModels(ASObjectList var1);

   ASModel getInternalAS();

   void setInternalAS(ASModel var1);

   void addAS(ASModel var1);

   void removeAS(ASModel var1);

   ASElementDeclaration getElementDeclaration() throws DOMException;

   void validate() throws DOMASException;
}
