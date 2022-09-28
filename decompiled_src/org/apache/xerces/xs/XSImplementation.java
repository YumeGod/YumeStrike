package org.apache.xerces.xs;

public interface XSImplementation {
   StringList getRecognizedVersions();

   XSLoader createXSLoader(StringList var1) throws XSException;
}
