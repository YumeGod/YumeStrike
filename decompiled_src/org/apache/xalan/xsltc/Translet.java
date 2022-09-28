package org.apache.xalan.xsltc;

import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.serializer.SerializationHandler;

public interface Translet {
   void transform(DOM var1, SerializationHandler var2) throws TransletException;

   void transform(DOM var1, SerializationHandler[] var2) throws TransletException;

   void transform(DOM var1, DTMAxisIterator var2, SerializationHandler var3) throws TransletException;

   Object addParameter(String var1, Object var2);

   void buildKeys(DOM var1, DTMAxisIterator var2, SerializationHandler var3, int var4) throws TransletException;

   void addAuxiliaryClass(Class var1);

   Class getAuxiliaryClass(String var1);

   String[] getNamesArray();

   String[] getUrisArray();

   int[] getTypesArray();

   String[] getNamespaceArray();
}
