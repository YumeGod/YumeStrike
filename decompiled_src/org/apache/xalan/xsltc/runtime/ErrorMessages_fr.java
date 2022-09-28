package org.apache.xalan.xsltc.runtime;

import java.util.ListResourceBundle;

public class ErrorMessages_fr extends ListResourceBundle {
   public Object[][] getContents() {
      return new Object[][]{{"RUN_TIME_INTERNAL_ERR", "Erreur interne d''exécution dans ''{0}''"}, {"RUN_TIME_COPY_ERR", "Erreur d'exécution de <xsl:copy>."}, {"DATA_CONVERSION_ERR", "Conversion incorrecte de ''{0}'' en ''{1}''."}, {"EXTERNAL_FUNC_ERR", "Fonction externe ''{0}'' non prise en charge par XSLTC."}, {"EQUALITY_EXPR_ERR", "Type d'argument inconnu dans l'expression d'égalité."}, {"INVALID_ARGUMENT_ERR", "Type d''argument inconnu ''{0}'' dans l''appel à ''{1}''"}, {"FORMAT_NUMBER_ERR", "Tentative de formatage du nombre ''{0}'' avec le modèle ''{1}''."}, {"ITERATOR_CLONE_ERR", "Clonage impossible de l''itérateur ''{0}''."}, {"AXIS_SUPPORT_ERR", "Itérateur non pris en charge pour l''axe ''{0}''."}, {"TYPED_AXIS_SUPPORT_ERR", "Itérateur non pris en charge pour l''axe indiqué ''{0}''."}, {"STRAY_ATTRIBUTE_ERR", "L''attribut ''{0}'' est à l''extérieur de l''élément."}, {"STRAY_NAMESPACE_ERR", "La déclaration d''espace de noms ''{0}''=''{1}'' est à l''extérieur de l''élément."}, {"NAMESPACE_PREFIX_ERR", "L''espace de noms du préfixe ''{0}'' n''a pas été déclaré."}, {"DOM_ADAPTER_INIT_ERR", "DOMAdapter a été créé avec un type incorrect de source de DOM."}, {"PARSER_DTD_SUPPORT_ERR", "L''analyseur SAX que vous utilisez ne traite pas les événements de déclaration DTD."}, {"NAMESPACES_SUPPORT_ERR", "L'analyseur SAX que vous utilisez ne prend pas en charge les espaces de nom XML."}, {"CANT_RESOLVE_RELATIVE_URI_ERR", "Résolution impossible de la référence à l''URI ''{0}''."}};
   }
}
