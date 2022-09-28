package org.apache.batik.css.engine;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public interface CSSNavigableDocumentListener {
   void nodeInserted(Node var1);

   void nodeToBeRemoved(Node var1);

   void subtreeModified(Node var1);

   void characterDataModified(Node var1);

   void attrModified(Element var1, Attr var2, short var3, String var4, String var5);

   void overrideStyleTextChanged(CSSStylableElement var1, String var2);

   void overrideStylePropertyRemoved(CSSStylableElement var1, String var2);

   void overrideStylePropertyChanged(CSSStylableElement var1, String var2, String var3, String var4);
}
