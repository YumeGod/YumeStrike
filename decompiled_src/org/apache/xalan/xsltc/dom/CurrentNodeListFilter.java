package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.runtime.AbstractTranslet;
import org.apache.xml.dtm.DTMAxisIterator;

public interface CurrentNodeListFilter {
   boolean test(int var1, int var2, int var3, int var4, AbstractTranslet var5, DTMAxisIterator var6);
}
