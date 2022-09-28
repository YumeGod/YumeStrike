package org.apache.xerces.xs;

public interface XSModel {
   StringList getNamespaces();

   XSNamespaceItemList getNamespaceItems();

   XSNamedMap getComponents(short var1);

   XSNamedMap getComponentsByNamespace(short var1, String var2);

   XSObjectList getAnnotations();

   XSElementDeclaration getElementDeclaration(String var1, String var2);

   XSAttributeDeclaration getAttributeDeclaration(String var1, String var2);

   XSTypeDefinition getTypeDefinition(String var1, String var2);

   XSAttributeGroupDefinition getAttributeGroup(String var1, String var2);

   XSModelGroupDefinition getModelGroupDefinition(String var1, String var2);

   XSNotationDeclaration getNotationDeclaration(String var1, String var2);
}
