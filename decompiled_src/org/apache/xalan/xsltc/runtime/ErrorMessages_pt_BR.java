package org.apache.xalan.xsltc.runtime;

import java.util.ListResourceBundle;

public class ErrorMessages_pt_BR extends ListResourceBundle {
   public Object[][] getContents() {
      return new Object[][]{{"RUN_TIME_INTERNAL_ERR", "Erro interno de tempo de execução em ''{0}''"}, {"RUN_TIME_COPY_ERR", "Erro de tempo de execução ao executar <xsl:copy>."}, {"DATA_CONVERSION_ERR", "Conversão inválida de ''{0}'' em ''{1}''."}, {"EXTERNAL_FUNC_ERR", "Função externa ''{0}'' não suportada por XSLTC."}, {"EQUALITY_EXPR_ERR", "Tipo de argumento desconhecido na expressão de igualdade. "}, {"INVALID_ARGUMENT_ERR", "Tipo de argumento inválido ''{0}'' na chamada para ''{1}''"}, {"FORMAT_NUMBER_ERR", "Tentando formatar o número ''{0}'' utilizando o padrão ''{1}''."}, {"ITERATOR_CLONE_ERR", "Impossível clonar iterador ''{0}''."}, {"AXIS_SUPPORT_ERR", "Iterador para eixo ''{0}'' não suportado. "}, {"TYPED_AXIS_SUPPORT_ERR", "Iterador para eixo digitado ''{0}'' não suportado. "}, {"STRAY_ATTRIBUTE_ERR", "Atributo ''{0}'' fora do elemento. "}, {"STRAY_NAMESPACE_ERR", "Declaração de namespace ''{0}''=''{1}'' fora do elemento. "}, {"NAMESPACE_PREFIX_ERR", "Namespace para prefixo ''{0}'' não foi declarado. "}, {"DOM_ADAPTER_INIT_ERR", "DOMAdapter criado utilizando tipo incorreto de DOM de origem."}, {"PARSER_DTD_SUPPORT_ERR", "O analisador SAX que está sendo utilizado não trata de eventos de declaração de DTD."}, {"NAMESPACES_SUPPORT_ERR", "O analisador SAX que está sendo utilizado não possui suporte para Namespaces XML."}, {"CANT_RESOLVE_RELATIVE_URI_ERR", "Impossível resolver a referência de URI ''{0}''."}};
   }
}
