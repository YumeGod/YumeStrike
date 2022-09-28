package org.apache.xerces.jaxp.validation;

import javax.xml.validation.Schema;
import javax.xml.validation.Validator;
import javax.xml.validation.ValidatorHandler;
import org.apache.xerces.xni.grammars.XMLGrammarPool;

abstract class AbstractXMLSchema extends Schema implements XSGrammarPoolContainer {
   public final Validator newValidator() {
      return new ValidatorImpl(this);
   }

   public final ValidatorHandler newValidatorHandler() {
      return new ValidatorHandlerImpl(this);
   }

   public abstract boolean isFullyComposed();

   public abstract XMLGrammarPool getGrammarPool();
}
