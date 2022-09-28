package org.apache.xalan.xsltc.runtime;

import java.util.ListResourceBundle;

public class ErrorMessages_pl extends ListResourceBundle {
   public Object[][] getContents() {
      return new Object[][]{{"RUN_TIME_INTERNAL_ERR", "Wewnętrzny błąd czasu wykonania w ''{0}''"}, {"RUN_TIME_COPY_ERR", "Błąd czasu wykonania podczas wykonywania <xsl:copy>."}, {"DATA_CONVERSION_ERR", "Niepoprawna konwersja z ''{0}'' do ''{1}''."}, {"EXTERNAL_FUNC_ERR", "Funkcja zewnętrzna ''{0}'' nieobsługiwana przez XSLTC."}, {"EQUALITY_EXPR_ERR", "Nieznany typ argumentu w wyrażeniu równości."}, {"INVALID_ARGUMENT_ERR", "Niepoprawny typ argumentu ''{0}'' w wywołaniu ''{1}''"}, {"FORMAT_NUMBER_ERR", "Próba sformatowania liczby ''{0}'' za pomocą wzorca ''{1}''."}, {"ITERATOR_CLONE_ERR", "Nie można utworzyć kopii iteratora ''{0}''."}, {"AXIS_SUPPORT_ERR", "Iterator osi ''{0}'' nie jest obsługiwany."}, {"TYPED_AXIS_SUPPORT_ERR", "Iterator osi ''{0}'' określonego typu nie jest obsługiwany."}, {"STRAY_ATTRIBUTE_ERR", "Atrybut ''{0}'' poza elementem."}, {"STRAY_NAMESPACE_ERR", "Deklaracja przestrzeni nazw ''{0}''=''{1}'' poza elementem."}, {"NAMESPACE_PREFIX_ERR", "Nie zadeklarowano przestrzeni nazw dla przedrostka ''{0}''."}, {"DOM_ADAPTER_INIT_ERR", "Utworzono DOMAdapter za pomocą źródłowego DOM o błędnym typie."}, {"PARSER_DTD_SUPPORT_ERR", "Używany analizator składni SAX nie obsługuje zdarzeń deklaracji DTD."}, {"NAMESPACES_SUPPORT_ERR", "Używany analizator składni SAX nie obsługuje przestrzeni nazw XML."}, {"CANT_RESOLVE_RELATIVE_URI_ERR", "Nie można przetłumaczyć odniesienia do URI ''{0}''."}};
   }
}
