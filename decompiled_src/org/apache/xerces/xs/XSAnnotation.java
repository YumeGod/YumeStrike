package org.apache.xerces.xs;

public interface XSAnnotation extends XSObject {
   short W3C_DOM_ELEMENT = 1;
   short SAX_CONTENTHANDLER = 2;
   short W3C_DOM_DOCUMENT = 3;

   boolean writeAnnotation(Object var1, short var2);

   String getAnnotationString();
}
