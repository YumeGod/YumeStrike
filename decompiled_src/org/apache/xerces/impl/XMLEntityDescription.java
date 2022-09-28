package org.apache.xerces.impl;

import org.apache.xerces.xni.XMLResourceIdentifier;

public interface XMLEntityDescription extends XMLResourceIdentifier {
   void setEntityName(String var1);

   String getEntityName();
}
