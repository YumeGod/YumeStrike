package org.apache.batik.dom.svg;

public interface ExtendedTraitAccess extends TraitAccess {
   boolean hasProperty(String var1);

   boolean hasTrait(String var1, String var2);

   boolean isPropertyAnimatable(String var1);

   boolean isAttributeAnimatable(String var1, String var2);

   boolean isPropertyAdditive(String var1);

   boolean isAttributeAdditive(String var1, String var2);

   boolean isTraitAnimatable(String var1, String var2);

   boolean isTraitAdditive(String var1, String var2);

   int getPropertyType(String var1);

   int getAttributeType(String var1, String var2);
}
