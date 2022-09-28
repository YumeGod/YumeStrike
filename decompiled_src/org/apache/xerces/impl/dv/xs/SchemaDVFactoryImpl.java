package org.apache.xerces.impl.dv.xs;

import org.apache.xerces.impl.dv.SchemaDVFactory;
import org.apache.xerces.impl.dv.XSFacets;
import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.impl.xs.XSDeclarationPool;
import org.apache.xerces.util.SymbolHash;
import org.apache.xerces.xs.XSObjectList;

public class SchemaDVFactoryImpl extends SchemaDVFactory {
   static final String URI_SCHEMAFORSCHEMA = "http://www.w3.org/2001/XMLSchema";
   static SymbolHash fBuiltInTypes = new SymbolHash();
   protected XSDeclarationPool fDeclPool = null;

   public XSSimpleType getBuiltInType(String var1) {
      return (XSSimpleType)fBuiltInTypes.get(var1);
   }

   public SymbolHash getBuiltInTypes() {
      return fBuiltInTypes.makeClone();
   }

   public XSSimpleType createTypeRestriction(String var1, String var2, short var3, XSSimpleType var4, XSObjectList var5) {
      if (this.fDeclPool != null) {
         XSSimpleTypeDecl var6 = this.fDeclPool.getSimpleTypeDecl();
         return var6.setRestrictionValues((XSSimpleTypeDecl)var4, var1, var2, var3, var5);
      } else {
         return new XSSimpleTypeDecl((XSSimpleTypeDecl)var4, var1, var2, var3, false, var5);
      }
   }

   public XSSimpleType createTypeList(String var1, String var2, short var3, XSSimpleType var4, XSObjectList var5) {
      if (this.fDeclPool != null) {
         XSSimpleTypeDecl var6 = this.fDeclPool.getSimpleTypeDecl();
         return var6.setListValues(var1, var2, var3, (XSSimpleTypeDecl)var4, var5);
      } else {
         return new XSSimpleTypeDecl(var1, var2, var3, (XSSimpleTypeDecl)var4, false, var5);
      }
   }

   public XSSimpleType createTypeUnion(String var1, String var2, short var3, XSSimpleType[] var4, XSObjectList var5) {
      int var6 = var4.length;
      XSSimpleTypeDecl[] var7 = new XSSimpleTypeDecl[var6];
      System.arraycopy(var4, 0, var7, 0, var6);
      if (this.fDeclPool != null) {
         XSSimpleTypeDecl var8 = this.fDeclPool.getSimpleTypeDecl();
         return var8.setUnionValues(var1, var2, var3, var7, var5);
      } else {
         return new XSSimpleTypeDecl(var1, var2, var3, var7, var5);
      }
   }

   static void createBuiltInTypes() {
      XSFacets var49 = new XSFacets();
      XSSimpleTypeDecl var50 = XSSimpleTypeDecl.fAnySimpleType;
      XSSimpleTypeDecl var51 = XSSimpleTypeDecl.fAnyAtomicType;
      Object var52 = null;
      fBuiltInTypes.put("anySimpleType", var50);
      XSSimpleTypeDecl var53 = new XSSimpleTypeDecl(var50, "string", (short)1, (short)0, false, false, false, true, (short)2);
      fBuiltInTypes.put("string", var53);
      fBuiltInTypes.put("boolean", new XSSimpleTypeDecl(var50, "boolean", (short)2, (short)0, false, true, false, true, (short)3));
      XSSimpleTypeDecl var54 = new XSSimpleTypeDecl(var50, "decimal", (short)3, (short)2, false, false, true, true, (short)4);
      fBuiltInTypes.put("decimal", var54);
      fBuiltInTypes.put("anyURI", new XSSimpleTypeDecl(var50, "anyURI", (short)17, (short)0, false, false, false, true, (short)18));
      fBuiltInTypes.put("base64Binary", new XSSimpleTypeDecl(var50, "base64Binary", (short)16, (short)0, false, false, false, true, (short)17));
      XSSimpleTypeDecl var55 = new XSSimpleTypeDecl(var50, "duration", (short)6, (short)1, false, false, false, true, (short)7);
      fBuiltInTypes.put("duration", var55);
      fBuiltInTypes.put("dateTime", new XSSimpleTypeDecl(var50, "dateTime", (short)7, (short)1, false, false, false, true, (short)8));
      fBuiltInTypes.put("time", new XSSimpleTypeDecl(var50, "time", (short)8, (short)1, false, false, false, true, (short)9));
      fBuiltInTypes.put("date", new XSSimpleTypeDecl(var50, "date", (short)9, (short)1, false, false, false, true, (short)10));
      fBuiltInTypes.put("gYearMonth", new XSSimpleTypeDecl(var50, "gYearMonth", (short)10, (short)1, false, false, false, true, (short)11));
      fBuiltInTypes.put("gYear", new XSSimpleTypeDecl(var50, "gYear", (short)11, (short)1, false, false, false, true, (short)12));
      fBuiltInTypes.put("gMonthDay", new XSSimpleTypeDecl(var50, "gMonthDay", (short)12, (short)1, false, false, false, true, (short)13));
      fBuiltInTypes.put("gDay", new XSSimpleTypeDecl(var50, "gDay", (short)13, (short)1, false, false, false, true, (short)14));
      fBuiltInTypes.put("gMonth", new XSSimpleTypeDecl(var50, "gMonth", (short)14, (short)1, false, false, false, true, (short)15));
      XSSimpleTypeDecl var56 = new XSSimpleTypeDecl(var54, "integer", (short)24, (short)2, false, false, true, true, (short)30);
      fBuiltInTypes.put("integer", var56);
      var49.maxInclusive = "0";
      XSSimpleTypeDecl var57 = new XSSimpleTypeDecl(var56, "nonPositiveInteger", "http://www.w3.org/2001/XMLSchema", (short)0, false, (XSObjectList)null, (short)31);
      var57.applyFacets1(var49, (short)32, (short)0);
      fBuiltInTypes.put("nonPositiveInteger", var57);
      var49.maxInclusive = "-1";
      XSSimpleTypeDecl var58 = new XSSimpleTypeDecl(var56, "negativeInteger", "http://www.w3.org/2001/XMLSchema", (short)0, false, (XSObjectList)null, (short)32);
      var58.applyFacets1(var49, (short)32, (short)0);
      fBuiltInTypes.put("negativeInteger", var58);
      var49.maxInclusive = "9223372036854775807";
      var49.minInclusive = "-9223372036854775808";
      XSSimpleTypeDecl var59 = new XSSimpleTypeDecl(var56, "long", "http://www.w3.org/2001/XMLSchema", (short)0, false, (XSObjectList)null, (short)33);
      var59.applyFacets1(var49, (short)288, (short)0);
      fBuiltInTypes.put("long", var59);
      var49.maxInclusive = "2147483647";
      var49.minInclusive = "-2147483648";
      XSSimpleTypeDecl var60 = new XSSimpleTypeDecl(var59, "int", "http://www.w3.org/2001/XMLSchema", (short)0, false, (XSObjectList)null, (short)34);
      var60.applyFacets1(var49, (short)288, (short)0);
      fBuiltInTypes.put("int", var60);
      var49.maxInclusive = "32767";
      var49.minInclusive = "-32768";
      XSSimpleTypeDecl var61 = new XSSimpleTypeDecl(var60, "short", "http://www.w3.org/2001/XMLSchema", (short)0, false, (XSObjectList)null, (short)35);
      var61.applyFacets1(var49, (short)288, (short)0);
      fBuiltInTypes.put("short", var61);
      var49.maxInclusive = "127";
      var49.minInclusive = "-128";
      XSSimpleTypeDecl var62 = new XSSimpleTypeDecl(var61, "byte", "http://www.w3.org/2001/XMLSchema", (short)0, false, (XSObjectList)null, (short)36);
      var62.applyFacets1(var49, (short)288, (short)0);
      fBuiltInTypes.put("byte", var62);
      var49.minInclusive = "0";
      XSSimpleTypeDecl var63 = new XSSimpleTypeDecl(var56, "nonNegativeInteger", "http://www.w3.org/2001/XMLSchema", (short)0, false, (XSObjectList)null, (short)37);
      var63.applyFacets1(var49, (short)256, (short)0);
      fBuiltInTypes.put("nonNegativeInteger", var63);
      var49.maxInclusive = "18446744073709551615";
      XSSimpleTypeDecl var64 = new XSSimpleTypeDecl(var63, "unsignedLong", "http://www.w3.org/2001/XMLSchema", (short)0, false, (XSObjectList)null, (short)38);
      var64.applyFacets1(var49, (short)32, (short)0);
      fBuiltInTypes.put("unsignedLong", var64);
      var49.maxInclusive = "4294967295";
      XSSimpleTypeDecl var65 = new XSSimpleTypeDecl(var64, "unsignedInt", "http://www.w3.org/2001/XMLSchema", (short)0, false, (XSObjectList)null, (short)39);
      var65.applyFacets1(var49, (short)32, (short)0);
      fBuiltInTypes.put("unsignedInt", var65);
      var49.maxInclusive = "65535";
      XSSimpleTypeDecl var66 = new XSSimpleTypeDecl(var65, "unsignedShort", "http://www.w3.org/2001/XMLSchema", (short)0, false, (XSObjectList)null, (short)40);
      var66.applyFacets1(var49, (short)32, (short)0);
      fBuiltInTypes.put("unsignedShort", var66);
      var49.maxInclusive = "255";
      XSSimpleTypeDecl var67 = new XSSimpleTypeDecl(var66, "unsignedByte", "http://www.w3.org/2001/XMLSchema", (short)0, false, (XSObjectList)null, (short)41);
      var67.applyFacets1(var49, (short)32, (short)0);
      fBuiltInTypes.put("unsignedByte", var67);
      var49.minInclusive = "1";
      XSSimpleTypeDecl var68 = new XSSimpleTypeDecl(var63, "positiveInteger", "http://www.w3.org/2001/XMLSchema", (short)0, false, (XSObjectList)null, (short)42);
      var68.applyFacets1(var49, (short)256, (short)0);
      fBuiltInTypes.put("positiveInteger", var68);
      fBuiltInTypes.put("float", new XSSimpleTypeDecl(var50, "float", (short)4, (short)1, true, true, true, true, (short)5));
      fBuiltInTypes.put("double", new XSSimpleTypeDecl(var50, "double", (short)5, (short)1, true, true, true, true, (short)6));
      fBuiltInTypes.put("hexBinary", new XSSimpleTypeDecl(var50, "hexBinary", (short)15, (short)0, false, false, false, true, (short)16));
      fBuiltInTypes.put("NOTATION", new XSSimpleTypeDecl(var50, "NOTATION", (short)20, (short)0, false, false, false, true, (short)20));
      var49.whiteSpace = 1;
      XSSimpleTypeDecl var69 = new XSSimpleTypeDecl(var53, "normalizedString", "http://www.w3.org/2001/XMLSchema", (short)0, false, (XSObjectList)null, (short)21);
      var69.applyFacets1(var49, (short)16, (short)0);
      fBuiltInTypes.put("normalizedString", var69);
      var49.whiteSpace = 2;
      XSSimpleTypeDecl var70 = new XSSimpleTypeDecl(var69, "token", "http://www.w3.org/2001/XMLSchema", (short)0, false, (XSObjectList)null, (short)22);
      var70.applyFacets1(var49, (short)16, (short)0);
      fBuiltInTypes.put("token", var70);
      var49.whiteSpace = 2;
      var49.pattern = "([a-zA-Z]{1,8})(-[a-zA-Z0-9]{1,8})*";
      XSSimpleTypeDecl var71 = new XSSimpleTypeDecl(var70, "language", "http://www.w3.org/2001/XMLSchema", (short)0, false, (XSObjectList)null, (short)23);
      var71.applyFacets1(var49, (short)24, (short)0);
      fBuiltInTypes.put("language", var71);
      var49.whiteSpace = 2;
      XSSimpleTypeDecl var72 = new XSSimpleTypeDecl(var70, "Name", "http://www.w3.org/2001/XMLSchema", (short)0, false, (XSObjectList)null, (short)25);
      var72.applyFacets1(var49, (short)16, (short)0, (short)2);
      fBuiltInTypes.put("Name", var72);
      var49.whiteSpace = 2;
      XSSimpleTypeDecl var73 = new XSSimpleTypeDecl(var72, "NCName", "http://www.w3.org/2001/XMLSchema", (short)0, false, (XSObjectList)null, (short)26);
      var73.applyFacets1(var49, (short)16, (short)0, (short)3);
      fBuiltInTypes.put("NCName", var73);
      fBuiltInTypes.put("QName", new XSSimpleTypeDecl(var50, "QName", (short)18, (short)0, false, false, false, true, (short)19));
      fBuiltInTypes.put("ID", new XSSimpleTypeDecl(var73, "ID", (short)21, (short)0, false, false, false, true, (short)27));
      XSSimpleTypeDecl var74 = new XSSimpleTypeDecl(var73, "IDREF", (short)22, (short)0, false, false, false, true, (short)28);
      fBuiltInTypes.put("IDREF", var74);
      var49.minLength = 1;
      XSSimpleTypeDecl var75 = new XSSimpleTypeDecl((String)null, "http://www.w3.org/2001/XMLSchema", (short)0, var74, true, (XSObjectList)null);
      XSSimpleTypeDecl var76 = new XSSimpleTypeDecl(var75, "IDREFS", "http://www.w3.org/2001/XMLSchema", (short)0, false, (XSObjectList)null);
      var76.applyFacets1(var49, (short)2, (short)0);
      fBuiltInTypes.put("IDREFS", var76);
      XSSimpleTypeDecl var77 = new XSSimpleTypeDecl(var73, "ENTITY", (short)23, (short)0, false, false, false, true, (short)29);
      fBuiltInTypes.put("ENTITY", var77);
      var49.minLength = 1;
      var75 = new XSSimpleTypeDecl((String)null, "http://www.w3.org/2001/XMLSchema", (short)0, var77, true, (XSObjectList)null);
      XSSimpleTypeDecl var78 = new XSSimpleTypeDecl(var75, "ENTITIES", "http://www.w3.org/2001/XMLSchema", (short)0, false, (XSObjectList)null);
      var78.applyFacets1(var49, (short)2, (short)0);
      fBuiltInTypes.put("ENTITIES", var78);
      var49.whiteSpace = 2;
      XSSimpleTypeDecl var79 = new XSSimpleTypeDecl(var70, "NMTOKEN", "http://www.w3.org/2001/XMLSchema", (short)0, false, (XSObjectList)null, (short)24);
      var79.applyFacets1(var49, (short)16, (short)0, (short)1);
      fBuiltInTypes.put("NMTOKEN", var79);
      var49.minLength = 1;
      var75 = new XSSimpleTypeDecl((String)null, "http://www.w3.org/2001/XMLSchema", (short)0, var79, true, (XSObjectList)null);
      XSSimpleTypeDecl var80 = new XSSimpleTypeDecl(var75, "NMTOKENS", "http://www.w3.org/2001/XMLSchema", (short)0, false, (XSObjectList)null);
      var80.applyFacets1(var49, (short)2, (short)0);
      fBuiltInTypes.put("NMTOKENS", var80);
   }

   public void setDeclPool(XSDeclarationPool var1) {
      this.fDeclPool = var1;
   }

   static {
      createBuiltInTypes();
   }
}
