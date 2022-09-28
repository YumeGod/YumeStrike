package org.apache.xerces.xni;

import org.apache.xerces.xni.parser.XMLDTDContentModelSource;

public interface XMLDTDContentModelHandler {
   short SEPARATOR_CHOICE = 0;
   short SEPARATOR_SEQUENCE = 1;
   short OCCURS_ZERO_OR_ONE = 2;
   short OCCURS_ZERO_OR_MORE = 3;
   short OCCURS_ONE_OR_MORE = 4;

   void startContentModel(String var1, Augmentations var2) throws XNIException;

   void any(Augmentations var1) throws XNIException;

   void empty(Augmentations var1) throws XNIException;

   void startGroup(Augmentations var1) throws XNIException;

   void pcdata(Augmentations var1) throws XNIException;

   void element(String var1, Augmentations var2) throws XNIException;

   void separator(short var1, Augmentations var2) throws XNIException;

   void occurrence(short var1, Augmentations var2) throws XNIException;

   void endGroup(Augmentations var1) throws XNIException;

   void endContentModel(Augmentations var1) throws XNIException;

   void setDTDContentModelSource(XMLDTDContentModelSource var1);

   XMLDTDContentModelSource getDTDContentModelSource();
}
