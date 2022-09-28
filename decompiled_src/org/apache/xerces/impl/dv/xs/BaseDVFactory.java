package org.apache.xerces.impl.dv.xs;

import org.apache.xerces.impl.dv.SchemaDVFactory;
import org.apache.xerces.impl.dv.XSFacets;
import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.util.SymbolHash;
import org.apache.xerces.xs.XSObjectList;

public class BaseDVFactory extends SchemaDVFactory {
   static final String URI_SCHEMAFORSCHEMA = "http://www.w3.org/2001/XMLSchema";
   static SymbolHash fBaseTypes = new SymbolHash(53);

   public XSSimpleType getBuiltInType(String var1) {
      return (XSSimpleType)fBaseTypes.get(var1);
   }

   public SymbolHash getBuiltInTypes() {
      return fBaseTypes.makeClone();
   }

   public XSSimpleType createTypeRestriction(String var1, String var2, short var3, XSSimpleType var4, XSObjectList var5) {
      return new XSSimpleTypeDecl((XSSimpleTypeDecl)var4, var1, var2, var3, false, var5);
   }

   public XSSimpleType createTypeList(String var1, String var2, short var3, XSSimpleType var4, XSObjectList var5) {
      return new XSSimpleTypeDecl(var1, var2, var3, (XSSimpleTypeDecl)var4, false, var5);
   }

   public XSSimpleType createTypeUnion(String var1, String var2, short var3, XSSimpleType[] var4, XSObjectList var5) {
      int var6 = var4.length;
      XSSimpleTypeDecl[] var7 = new XSSimpleTypeDecl[var6];
      System.arraycopy(var4, 0, var7, 0, var6);
      return new XSSimpleTypeDecl(var1, var2, var3, var7, var5);
   }

   static void createBuiltInTypes(SymbolHash var0) {
      XSFacets var28 = new XSFacets();
      XSSimpleTypeDecl var29 = XSSimpleTypeDecl.fAnySimpleType;
      var0.put("anySimpleType", var29);
      XSSimpleTypeDecl var30 = new XSSimpleTypeDecl(var29, "string", (short)1, (short)0, false, false, false, true, (short)2);
      var0.put("string", var30);
      var0.put("boolean", new XSSimpleTypeDecl(var29, "boolean", (short)2, (short)0, false, true, false, true, (short)3));
      XSSimpleTypeDecl var31 = new XSSimpleTypeDecl(var29, "decimal", (short)3, (short)2, false, false, true, true, (short)4);
      var0.put("decimal", var31);
      var0.put("anyURI", new XSSimpleTypeDecl(var29, "anyURI", (short)17, (short)0, false, false, false, true, (short)18));
      var0.put("base64Binary", new XSSimpleTypeDecl(var29, "base64Binary", (short)16, (short)0, false, false, false, true, (short)17));
      var0.put("dateTime", new XSSimpleTypeDecl(var29, "dateTime", (short)7, (short)1, false, false, false, true, (short)8));
      var0.put("time", new XSSimpleTypeDecl(var29, "time", (short)8, (short)1, false, false, false, true, (short)9));
      var0.put("date", new XSSimpleTypeDecl(var29, "date", (short)9, (short)1, false, false, false, true, (short)10));
      var0.put("gYearMonth", new XSSimpleTypeDecl(var29, "gYearMonth", (short)10, (short)1, false, false, false, true, (short)11));
      var0.put("gYear", new XSSimpleTypeDecl(var29, "gYear", (short)11, (short)1, false, false, false, true, (short)12));
      var0.put("gMonthDay", new XSSimpleTypeDecl(var29, "gMonthDay", (short)12, (short)1, false, false, false, true, (short)13));
      var0.put("gDay", new XSSimpleTypeDecl(var29, "gDay", (short)13, (short)1, false, false, false, true, (short)14));
      var0.put("gMonth", new XSSimpleTypeDecl(var29, "gMonth", (short)14, (short)1, false, false, false, true, (short)15));
      XSSimpleTypeDecl var32 = new XSSimpleTypeDecl(var31, "integer", (short)24, (short)2, false, false, true, true, (short)30);
      var0.put("integer", var32);
      var28.maxInclusive = "0";
      XSSimpleTypeDecl var33 = new XSSimpleTypeDecl(var32, "nonPositiveInteger", "http://www.w3.org/2001/XMLSchema", (short)0, false, (XSObjectList)null, (short)31);
      var33.applyFacets1(var28, (short)32, (short)0);
      var0.put("nonPositiveInteger", var33);
      var28.maxInclusive = "-1";
      XSSimpleTypeDecl var34 = new XSSimpleTypeDecl(var32, "negativeInteger", "http://www.w3.org/2001/XMLSchema", (short)0, false, (XSObjectList)null, (short)32);
      var34.applyFacets1(var28, (short)32, (short)0);
      var0.put("negativeInteger", var34);
      var28.maxInclusive = "9223372036854775807";
      var28.minInclusive = "-9223372036854775808";
      XSSimpleTypeDecl var35 = new XSSimpleTypeDecl(var32, "long", "http://www.w3.org/2001/XMLSchema", (short)0, false, (XSObjectList)null, (short)33);
      var35.applyFacets1(var28, (short)288, (short)0);
      var0.put("long", var35);
      var28.maxInclusive = "2147483647";
      var28.minInclusive = "-2147483648";
      XSSimpleTypeDecl var36 = new XSSimpleTypeDecl(var35, "int", "http://www.w3.org/2001/XMLSchema", (short)0, false, (XSObjectList)null, (short)34);
      var36.applyFacets1(var28, (short)288, (short)0);
      var0.put("int", var36);
      var28.maxInclusive = "32767";
      var28.minInclusive = "-32768";
      XSSimpleTypeDecl var37 = new XSSimpleTypeDecl(var36, "short", "http://www.w3.org/2001/XMLSchema", (short)0, false, (XSObjectList)null, (short)35);
      var37.applyFacets1(var28, (short)288, (short)0);
      var0.put("short", var37);
      var28.maxInclusive = "127";
      var28.minInclusive = "-128";
      XSSimpleTypeDecl var38 = new XSSimpleTypeDecl(var37, "byte", "http://www.w3.org/2001/XMLSchema", (short)0, false, (XSObjectList)null, (short)36);
      var38.applyFacets1(var28, (short)288, (short)0);
      var0.put("byte", var38);
      var28.minInclusive = "0";
      XSSimpleTypeDecl var39 = new XSSimpleTypeDecl(var32, "nonNegativeInteger", "http://www.w3.org/2001/XMLSchema", (short)0, false, (XSObjectList)null, (short)37);
      var39.applyFacets1(var28, (short)256, (short)0);
      var0.put("nonNegativeInteger", var39);
      var28.maxInclusive = "18446744073709551615";
      XSSimpleTypeDecl var40 = new XSSimpleTypeDecl(var39, "unsignedLong", "http://www.w3.org/2001/XMLSchema", (short)0, false, (XSObjectList)null, (short)38);
      var40.applyFacets1(var28, (short)32, (short)0);
      var0.put("unsignedLong", var40);
      var28.maxInclusive = "4294967295";
      XSSimpleTypeDecl var41 = new XSSimpleTypeDecl(var40, "unsignedInt", "http://www.w3.org/2001/XMLSchema", (short)0, false, (XSObjectList)null, (short)39);
      var41.applyFacets1(var28, (short)32, (short)0);
      var0.put("unsignedInt", var41);
      var28.maxInclusive = "65535";
      XSSimpleTypeDecl var42 = new XSSimpleTypeDecl(var41, "unsignedShort", "http://www.w3.org/2001/XMLSchema", (short)0, false, (XSObjectList)null, (short)40);
      var42.applyFacets1(var28, (short)32, (short)0);
      var0.put("unsignedShort", var42);
      var28.maxInclusive = "255";
      XSSimpleTypeDecl var43 = new XSSimpleTypeDecl(var42, "unsignedByte", "http://www.w3.org/2001/XMLSchema", (short)0, false, (XSObjectList)null, (short)41);
      var43.applyFacets1(var28, (short)32, (short)0);
      var0.put("unsignedByte", var43);
      var28.minInclusive = "1";
      XSSimpleTypeDecl var44 = new XSSimpleTypeDecl(var39, "positiveInteger", "http://www.w3.org/2001/XMLSchema", (short)0, false, (XSObjectList)null, (short)42);
      var44.applyFacets1(var28, (short)256, (short)0);
      var0.put("positiveInteger", var44);
   }

   static {
      createBuiltInTypes(fBaseTypes);
   }
}
