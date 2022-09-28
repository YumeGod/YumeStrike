package org.apache.xerces.impl.dtd;

import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XML11Char;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.parser.XMLEntityResolver;

public class XML11DTDProcessor extends XMLDTDLoader {
   public XML11DTDProcessor() {
   }

   public XML11DTDProcessor(SymbolTable var1) {
      super(var1);
   }

   public XML11DTDProcessor(SymbolTable var1, XMLGrammarPool var2) {
      super(var1, var2);
   }

   XML11DTDProcessor(SymbolTable var1, XMLGrammarPool var2, XMLErrorReporter var3, XMLEntityResolver var4) {
      super(var1, var2, var3, var4);
   }

   protected boolean isValidNmtoken(String var1) {
      return XML11Char.isXML11ValidNmtoken(var1);
   }

   protected boolean isValidName(String var1) {
      return XML11Char.isXML11ValidName(var1);
   }
}
