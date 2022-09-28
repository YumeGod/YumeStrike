package org.apache.xerces.impl.dtd;

import org.apache.xerces.xni.parser.XMLComponentManager;

public class XML11DTDValidator extends XMLDTDValidator {
   protected static final String DTD_VALIDATOR_PROPERTY = "http://apache.org/xml/properties/internal/validator/dtd";

   public void reset(XMLComponentManager var1) {
      XMLDTDValidator var2 = null;
      if ((var2 = (XMLDTDValidator)var1.getProperty("http://apache.org/xml/properties/internal/validator/dtd")) != null && var2 != this) {
         super.fGrammarBucket = var2.getGrammarBucket();
      }

      super.reset(var1);
   }

   protected void init() {
      if (super.fValidation || super.fDynamicValidation) {
         super.init();

         try {
            super.fValID = super.fDatatypeValidatorFactory.getBuiltInDV("XML11ID");
            super.fValIDRef = super.fDatatypeValidatorFactory.getBuiltInDV("XML11IDREF");
            super.fValIDRefs = super.fDatatypeValidatorFactory.getBuiltInDV("XML11IDREFS");
            super.fValNMTOKEN = super.fDatatypeValidatorFactory.getBuiltInDV("XML11NMTOKEN");
            super.fValNMTOKENS = super.fDatatypeValidatorFactory.getBuiltInDV("XML11NMTOKENS");
         } catch (Exception var2) {
            var2.printStackTrace(System.err);
         }
      }

   }
}
