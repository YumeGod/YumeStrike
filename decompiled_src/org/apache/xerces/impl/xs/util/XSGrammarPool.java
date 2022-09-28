package org.apache.xerces.impl.xs.util;

import java.util.Vector;
import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.impl.xs.XSModelImpl;
import org.apache.xerces.util.XMLGrammarPoolImpl;
import org.apache.xerces.xs.XSModel;

public class XSGrammarPool extends XMLGrammarPoolImpl {
   public XSModel toXSModel() {
      Vector var1 = new Vector();

      for(int var2 = 0; var2 < super.fGrammars.length; ++var2) {
         for(XMLGrammarPoolImpl.Entry var3 = super.fGrammars[var2]; var3 != null; var3 = var3.next) {
            if (var3.desc.getGrammarType().equals("http://www.w3.org/2001/XMLSchema")) {
               var1.addElement(var3.grammar);
            }
         }
      }

      int var6 = var1.size();
      if (var6 == 0) {
         return null;
      } else {
         SchemaGrammar[] var4 = new SchemaGrammar[var6];

         for(int var5 = 0; var5 < var6; ++var5) {
            var4[var5] = (SchemaGrammar)var1.elementAt(var5);
         }

         return new XSModelImpl(var4);
      }
   }
}
