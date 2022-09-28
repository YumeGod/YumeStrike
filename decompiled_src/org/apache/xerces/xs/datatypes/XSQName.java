package org.apache.xerces.xs.datatypes;

import org.apache.xerces.xni.QName;

public interface XSQName {
   QName getXNIQName();

   javax.xml.namespace.QName getJAXPQName();
}
