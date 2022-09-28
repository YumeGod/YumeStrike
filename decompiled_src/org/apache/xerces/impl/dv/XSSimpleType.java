package org.apache.xerces.impl.dv;

import org.apache.xerces.xs.XSSimpleTypeDefinition;

public interface XSSimpleType extends XSSimpleTypeDefinition {
   short WS_PRESERVE = 0;
   short WS_REPLACE = 1;
   short WS_COLLAPSE = 2;
   short PRIMITIVE_STRING = 1;
   short PRIMITIVE_BOOLEAN = 2;
   short PRIMITIVE_DECIMAL = 3;
   short PRIMITIVE_FLOAT = 4;
   short PRIMITIVE_DOUBLE = 5;
   short PRIMITIVE_DURATION = 6;
   short PRIMITIVE_DATETIME = 7;
   short PRIMITIVE_TIME = 8;
   short PRIMITIVE_DATE = 9;
   short PRIMITIVE_GYEARMONTH = 10;
   short PRIMITIVE_GYEAR = 11;
   short PRIMITIVE_GMONTHDAY = 12;
   short PRIMITIVE_GDAY = 13;
   short PRIMITIVE_GMONTH = 14;
   short PRIMITIVE_HEXBINARY = 15;
   short PRIMITIVE_BASE64BINARY = 16;
   short PRIMITIVE_ANYURI = 17;
   short PRIMITIVE_QNAME = 18;
   short PRIMITIVE_PRECISIONDECIMAL = 19;
   short PRIMITIVE_NOTATION = 20;

   short getPrimitiveKind();

   Object validate(String var1, ValidationContext var2, ValidatedInfo var3) throws InvalidDatatypeValueException;

   Object validate(Object var1, ValidationContext var2, ValidatedInfo var3) throws InvalidDatatypeValueException;

   void validate(ValidationContext var1, ValidatedInfo var2) throws InvalidDatatypeValueException;

   void applyFacets(XSFacets var1, short var2, short var3, ValidationContext var4) throws InvalidDatatypeFacetException;

   boolean isEqual(Object var1, Object var2);

   boolean isIDType();

   short getWhitespace() throws DatatypeException;
}
