package org.apache.xalan.xsltc.runtime;

import java.util.ListResourceBundle;

public class ErrorMessages_hu extends ListResourceBundle {
   public Object[][] getContents() {
      return new Object[][]{{"RUN_TIME_INTERNAL_ERR", "Futásidejű belső hiba; helye: ''{0}''"}, {"RUN_TIME_COPY_ERR", "Futásidejű belső hiba <xsl:copy> végrehajtásakor."}, {"DATA_CONVERSION_ERR", "Érvénytelen konverzió: ''{0}'' típusról ''{1}'' típusra."}, {"EXTERNAL_FUNC_ERR", "A(z) ''{0}'' külső függvényt nem támogatja az XSLTC."}, {"EQUALITY_EXPR_ERR", "Ismeretlen argumentumtípus található az egyenlőségi kifejezésben."}, {"INVALID_ARGUMENT_ERR", "Érvénytelen argumentumtípust (''{0}'') használt ''{1}'' hívásában."}, {"FORMAT_NUMBER_ERR", "A(z) ''{0}'' számot ''{1}'' minta alapján akarta formázni."}, {"ITERATOR_CLONE_ERR", "Nem lehet klónozni a(z) ''{0}'' iterátort."}, {"AXIS_SUPPORT_ERR", "A(z) ''{0}'' tengely iterátora nem támogatott."}, {"TYPED_AXIS_SUPPORT_ERR", "A tipizált ''{0}'' tengelyre iterátor nem támogatott."}, {"STRAY_ATTRIBUTE_ERR", "A(z) ''{0}'' attribútum kívül van az elemen."}, {"STRAY_NAMESPACE_ERR", "A(z) ''{0}''=''{1}'' névtér-deklaráció kívül esik az elemen."}, {"NAMESPACE_PREFIX_ERR", "A(z) ''{0}'' előtag névtere nem definiált."}, {"DOM_ADAPTER_INIT_ERR", "Nem megfelelő típusú forrás DOM használatával jött létre a DOMAdapter."}, {"PARSER_DTD_SUPPORT_ERR", "Az Ön által használt SAX elemző nem kezeli a DTD-deklarációs eseményeket."}, {"NAMESPACES_SUPPORT_ERR", "Az Ön által használt SAX elemző nem támogatja az XML névtereket."}, {"CANT_RESOLVE_RELATIVE_URI_ERR", "Nem lehet feloldani a(z) ''{0}'' URI hivatkozást."}};
   }
}
