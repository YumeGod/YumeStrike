package org.apache.xalan.xsltc;

import org.apache.xalan.xsltc.runtime.Hashtable;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.serializer.SerializationHandler;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public interface DOM {
   int FIRST_TYPE = 0;
   int NO_TYPE = -1;
   int NULL = 0;
   int RETURN_CURRENT = 0;
   int RETURN_PARENT = 1;
   int SIMPLE_RTF = 0;
   int ADAPTIVE_RTF = 1;
   int TREE_RTF = 2;

   DTMAxisIterator getIterator();

   String getStringValue();

   DTMAxisIterator getChildren(int var1);

   DTMAxisIterator getTypedChildren(int var1);

   DTMAxisIterator getAxisIterator(int var1);

   DTMAxisIterator getTypedAxisIterator(int var1, int var2);

   DTMAxisIterator getNthDescendant(int var1, int var2, boolean var3);

   DTMAxisIterator getNamespaceAxisIterator(int var1, int var2);

   DTMAxisIterator getNodeValueIterator(DTMAxisIterator var1, int var2, String var3, boolean var4);

   DTMAxisIterator orderNodes(DTMAxisIterator var1, int var2);

   String getNodeName(int var1);

   String getNodeNameX(int var1);

   String getNamespaceName(int var1);

   int getExpandedTypeID(int var1);

   int getNamespaceType(int var1);

   int getParent(int var1);

   int getAttributeNode(int var1, int var2);

   String getStringValueX(int var1);

   void copy(int var1, SerializationHandler var2) throws TransletException;

   void copy(DTMAxisIterator var1, SerializationHandler var2) throws TransletException;

   String shallowCopy(int var1, SerializationHandler var2) throws TransletException;

   boolean lessThan(int var1, int var2);

   void characters(int var1, SerializationHandler var2) throws TransletException;

   Node makeNode(int var1);

   Node makeNode(DTMAxisIterator var1);

   NodeList makeNodeList(int var1);

   NodeList makeNodeList(DTMAxisIterator var1);

   String getLanguage(int var1);

   int getSize();

   String getDocumentURI(int var1);

   void setFilter(StripFilter var1);

   void setupMapping(String[] var1, String[] var2, int[] var3, String[] var4);

   boolean isElement(int var1);

   boolean isAttribute(int var1);

   String lookupNamespace(int var1, String var2) throws TransletException;

   int getNodeIdent(int var1);

   int getNodeHandle(int var1);

   DOM getResultTreeFrag(int var1, int var2);

   DOM getResultTreeFrag(int var1, int var2, boolean var3);

   SerializationHandler getOutputDomBuilder();

   int getNSType(int var1);

   int getDocument();

   String getUnparsedEntityURI(String var1);

   Hashtable getElementsWithIDs();
}
