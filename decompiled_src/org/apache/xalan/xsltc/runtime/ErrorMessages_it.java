package org.apache.xalan.xsltc.runtime;

import java.util.ListResourceBundle;

public class ErrorMessages_it extends ListResourceBundle {
   public Object[][] getContents() {
      return new Object[][]{{"RUN_TIME_INTERNAL_ERR", "Errore run-time interno in ''{0}''"}, {"RUN_TIME_COPY_ERR", "Errore run-time durante l'esecuzione di <xsl:copy>."}, {"DATA_CONVERSION_ERR", "Conversione non valida da ''{0}'' a ''{1}''."}, {"EXTERNAL_FUNC_ERR", "Funzione esterna ''{0}'' non supportata da XSLTC."}, {"EQUALITY_EXPR_ERR", "Tipo di argomento sconosciuto nell'espressione di uguaglianza. "}, {"INVALID_ARGUMENT_ERR", "Tipo di argomento ''{0}'' non valido nella chiamata a ''{1}''"}, {"FORMAT_NUMBER_ERR", "Tentativo di formattazione del numero ''{0}'' utilizzando il modello ''{1}''."}, {"ITERATOR_CLONE_ERR", "Impossibile clonare l''iteratore ''{0}''."}, {"AXIS_SUPPORT_ERR", "Iteratore per l''asse ''{0}'' non supportato. "}, {"TYPED_AXIS_SUPPORT_ERR", "Iteratore per l''asse immesso ''{0}'' non supportato. "}, {"STRAY_ATTRIBUTE_ERR", "Attributo ''{0}'' al di fuori dell''elemento. "}, {"STRAY_NAMESPACE_ERR", "Dichiarazione dello spazio nome ''{0}''=''{1}'' al di fuori dell''elemento. "}, {"NAMESPACE_PREFIX_ERR", "Lo spazio nomi per il prefisso ''{0}'' non è stato dichiarato. "}, {"DOM_ADAPTER_INIT_ERR", "DOMAdapter creato utilizzando il tipo di origine DOM errato."}, {"PARSER_DTD_SUPPORT_ERR", "Il parser SAX utilizzato non gestisce gli eventi di dichiarazione DTD. "}, {"NAMESPACES_SUPPORT_ERR", "Il parser SAX utilizzato non dispone del supporto per gli spazi nome XML. "}, {"CANT_RESOLVE_RELATIVE_URI_ERR", "Impossibile risolvere il riferimento URI ''{0}''."}};
   }
}
