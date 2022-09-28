package org.apache.xerces.xs;

public interface XSNamespaceItem {
   String getSchemaNamespace();

   XSNamedMap getComponents(short var1);

   XSObjectList getAnnotations();

   XSElementDeclaration getElementDeclaration(String var1);

   XSAttributeDeclaration getAttributeDeclaration(String var1);

   XSTypeDefinition getTypeDefinition(String var1);

   XSAttributeGroupDefinition getAttributeGroup(String var1);

   XSModelGroupDefinition getModelGroupDefinition(String var1);

   XSNotationDeclaration getNotationDeclaration(String var1);

   StringList getDocumentLocations();
}
