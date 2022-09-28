package org.apache.xerces.xs;

public interface ItemPSVI {
   short VALIDITY_NOTKNOWN = 0;
   short VALIDITY_INVALID = 1;
   short VALIDITY_VALID = 2;
   short VALIDATION_NONE = 0;
   short VALIDATION_PARTIAL = 1;
   short VALIDATION_FULL = 2;

   String getValidationContext();

   short getValidity();

   short getValidationAttempted();

   StringList getErrorCodes();

   String getSchemaNormalizedValue();

   Object getActualNormalizedValue() throws XSException;

   short getActualNormalizedValueType() throws XSException;

   ShortList getItemValueTypes() throws XSException;

   XSTypeDefinition getTypeDefinition();

   XSSimpleTypeDefinition getMemberTypeDefinition();

   String getSchemaDefault();

   boolean getIsSchemaSpecified();
}
