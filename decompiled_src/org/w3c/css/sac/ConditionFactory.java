package org.w3c.css.sac;

public interface ConditionFactory {
   CombinatorCondition createAndCondition(Condition var1, Condition var2) throws CSSException;

   CombinatorCondition createOrCondition(Condition var1, Condition var2) throws CSSException;

   NegativeCondition createNegativeCondition(Condition var1) throws CSSException;

   PositionalCondition createPositionalCondition(int var1, boolean var2, boolean var3) throws CSSException;

   AttributeCondition createAttributeCondition(String var1, String var2, boolean var3, String var4) throws CSSException;

   AttributeCondition createIdCondition(String var1) throws CSSException;

   LangCondition createLangCondition(String var1) throws CSSException;

   AttributeCondition createOneOfAttributeCondition(String var1, String var2, boolean var3, String var4) throws CSSException;

   AttributeCondition createBeginHyphenAttributeCondition(String var1, String var2, boolean var3, String var4) throws CSSException;

   AttributeCondition createClassCondition(String var1, String var2) throws CSSException;

   AttributeCondition createPseudoClassCondition(String var1, String var2) throws CSSException;

   Condition createOnlyChildCondition() throws CSSException;

   Condition createOnlyTypeCondition() throws CSSException;

   ContentCondition createContentCondition(String var1) throws CSSException;
}
