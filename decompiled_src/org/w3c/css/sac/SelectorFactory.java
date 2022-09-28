package org.w3c.css.sac;

public interface SelectorFactory {
   ConditionalSelector createConditionalSelector(SimpleSelector var1, Condition var2) throws CSSException;

   SimpleSelector createAnyNodeSelector() throws CSSException;

   SimpleSelector createRootNodeSelector() throws CSSException;

   NegativeSelector createNegativeSelector(SimpleSelector var1) throws CSSException;

   ElementSelector createElementSelector(String var1, String var2) throws CSSException;

   CharacterDataSelector createTextNodeSelector(String var1) throws CSSException;

   CharacterDataSelector createCDataSectionSelector(String var1) throws CSSException;

   ProcessingInstructionSelector createProcessingInstructionSelector(String var1, String var2) throws CSSException;

   CharacterDataSelector createCommentSelector(String var1) throws CSSException;

   ElementSelector createPseudoElementSelector(String var1, String var2) throws CSSException;

   DescendantSelector createDescendantSelector(Selector var1, SimpleSelector var2) throws CSSException;

   DescendantSelector createChildSelector(Selector var1, SimpleSelector var2) throws CSSException;

   SiblingSelector createDirectAdjacentSelector(short var1, Selector var2, SimpleSelector var3) throws CSSException;
}
