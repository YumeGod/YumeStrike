package org.apache.xalan.xsltc;

public interface DOMEnhancedForDTM extends DOM {
   short[] getMapping(String[] var1, String[] var2, int[] var3);

   int[] getReverseMapping(String[] var1, String[] var2, int[] var3);

   short[] getNamespaceMapping(String[] var1);

   short[] getReverseNamespaceMapping(String[] var1);

   String getDocumentURI();

   void setDocumentURI(String var1);

   int getExpandedTypeID2(int var1);

   boolean hasDOMSource();

   int getElementById(String var1);
}
