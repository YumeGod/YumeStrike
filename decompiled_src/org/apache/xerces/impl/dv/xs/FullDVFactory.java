package org.apache.xerces.impl.dv.xs;

import org.apache.xerces.impl.dv.XSFacets;
import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.util.SymbolHash;
import org.apache.xerces.xs.XSObjectList;

public class FullDVFactory extends BaseDVFactory {
   static final String URI_SCHEMAFORSCHEMA = "http://www.w3.org/2001/XMLSchema";
   static SymbolHash fFullTypes = new SymbolHash(89);

   public XSSimpleType getBuiltInType(String var1) {
      return (XSSimpleType)fFullTypes.get(var1);
   }

   public SymbolHash getBuiltInTypes() {
      return fFullTypes.makeClone();
   }

   static void createBuiltInTypes(SymbolHash var0) {
      BaseDVFactory.createBuiltInTypes(var0);
      XSFacets var20 = new XSFacets();
      XSSimpleTypeDecl var21 = XSSimpleTypeDecl.fAnySimpleType;
      XSSimpleTypeDecl var22 = (XSSimpleTypeDecl)var0.get("string");
      var0.put("float", new XSSimpleTypeDecl(var21, "float", (short)4, (short)1, true, true, true, true, (short)5));
      var0.put("double", new XSSimpleTypeDecl(var21, "double", (short)5, (short)1, true, true, true, true, (short)6));
      var0.put("duration", new XSSimpleTypeDecl(var21, "duration", (short)6, (short)1, false, false, false, true, (short)7));
      var0.put("hexBinary", new XSSimpleTypeDecl(var21, "hexBinary", (short)15, (short)0, false, false, false, true, (short)16));
      var0.put("QName", new XSSimpleTypeDecl(var21, "QName", (short)18, (short)0, false, false, false, true, (short)19));
      var0.put("NOTATION", new XSSimpleTypeDecl(var21, "NOTATION", (short)20, (short)0, false, false, false, true, (short)20));
      var20.whiteSpace = 1;
      XSSimpleTypeDecl var23 = new XSSimpleTypeDecl(var22, "normalizedString", "http://www.w3.org/2001/XMLSchema", (short)0, false, (XSObjectList)null, (short)21);
      var23.applyFacets1(var20, (short)16, (short)0);
      var0.put("normalizedString", var23);
      var20.whiteSpace = 2;
      XSSimpleTypeDecl var24 = new XSSimpleTypeDecl(var23, "token", "http://www.w3.org/2001/XMLSchema", (short)0, false, (XSObjectList)null, (short)22);
      var24.applyFacets1(var20, (short)16, (short)0);
      var0.put("token", var24);
      var20.whiteSpace = 2;
      var20.pattern = "([a-zA-Z]{1,8})(-[a-zA-Z0-9]{1,8})*";
      XSSimpleTypeDecl var25 = new XSSimpleTypeDecl(var24, "language", "http://www.w3.org/2001/XMLSchema", (short)0, false, (XSObjectList)null, (short)23);
      var25.applyFacets1(var20, (short)24, (short)0);
      var0.put("language", var25);
      var20.whiteSpace = 2;
      XSSimpleTypeDecl var26 = new XSSimpleTypeDecl(var24, "Name", "http://www.w3.org/2001/XMLSchema", (short)0, false, (XSObjectList)null, (short)25);
      var26.applyFacets1(var20, (short)16, (short)0, (short)2);
      var0.put("Name", var26);
      var20.whiteSpace = 2;
      XSSimpleTypeDecl var27 = new XSSimpleTypeDecl(var26, "NCName", "http://www.w3.org/2001/XMLSchema", (short)0, false, (XSObjectList)null, (short)26);
      var27.applyFacets1(var20, (short)16, (short)0, (short)3);
      var0.put("NCName", var27);
      var0.put("ID", new XSSimpleTypeDecl(var27, "ID", (short)21, (short)0, false, false, false, true, (short)27));
      XSSimpleTypeDecl var28 = new XSSimpleTypeDecl(var27, "IDREF", (short)22, (short)0, false, false, false, true, (short)28);
      var0.put("IDREF", var28);
      var20.minLength = 1;
      XSSimpleTypeDecl var29 = new XSSimpleTypeDecl((String)null, "http://www.w3.org/2001/XMLSchema", (short)0, var28, true, (XSObjectList)null);
      XSSimpleTypeDecl var30 = new XSSimpleTypeDecl(var29, "IDREFS", "http://www.w3.org/2001/XMLSchema", (short)0, false, (XSObjectList)null);
      var30.applyFacets1(var20, (short)2, (short)0);
      var0.put("IDREFS", var30);
      XSSimpleTypeDecl var31 = new XSSimpleTypeDecl(var27, "ENTITY", (short)23, (short)0, false, false, false, true, (short)29);
      var0.put("ENTITY", var31);
      var20.minLength = 1;
      var29 = new XSSimpleTypeDecl((String)null, "http://www.w3.org/2001/XMLSchema", (short)0, var31, true, (XSObjectList)null);
      XSSimpleTypeDecl var32 = new XSSimpleTypeDecl(var29, "ENTITIES", "http://www.w3.org/2001/XMLSchema", (short)0, false, (XSObjectList)null);
      var32.applyFacets1(var20, (short)2, (short)0);
      var0.put("ENTITIES", var32);
      var20.whiteSpace = 2;
      XSSimpleTypeDecl var33 = new XSSimpleTypeDecl(var24, "NMTOKEN", "http://www.w3.org/2001/XMLSchema", (short)0, false, (XSObjectList)null, (short)24);
      var33.applyFacets1(var20, (short)16, (short)0, (short)1);
      var0.put("NMTOKEN", var33);
      var20.minLength = 1;
      var29 = new XSSimpleTypeDecl((String)null, "http://www.w3.org/2001/XMLSchema", (short)0, var33, true, (XSObjectList)null);
      XSSimpleTypeDecl var34 = new XSSimpleTypeDecl(var29, "NMTOKENS", "http://www.w3.org/2001/XMLSchema", (short)0, false, (XSObjectList)null);
      var34.applyFacets1(var20, (short)2, (short)0);
      var0.put("NMTOKENS", var34);
   }

   static {
      createBuiltInTypes(fFullTypes);
   }
}
