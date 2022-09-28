package org.apache.xalan.xsltc.runtime;

import java.util.ListResourceBundle;

public class ErrorMessages_ru extends ListResourceBundle {
   public Object[][] getContents() {
      return new Object[][]{{"RUN_TIME_INTERNAL_ERR", "Внутренняя ошибка времени выполнения в ''{0}''"}, {"RUN_TIME_COPY_ERR", "Ошибка времени выполнения при обработке <xsl:copy>."}, {"DATA_CONVERSION_ERR", "Недопустимое преобразование из ''{0}'' в ''{1}''."}, {"EXTERNAL_FUNC_ERR", "Внешняя функция ''{0}'' не поддерживается XSLTC."}, {"EQUALITY_EXPR_ERR", "Неизвестный тип аргумента в выражении равенства."}, {"INVALID_ARGUMENT_ERR", "Недопустимый тип аргумента ''{0}'' в вызове ''{1}''"}, {"FORMAT_NUMBER_ERR", "Попытка отформатировать число ''{0}'' с помощью шаблона ''{1}''."}, {"ITERATOR_CLONE_ERR", "Невозможно создать копию итератора ''{0}''."}, {"AXIS_SUPPORT_ERR", "Итератор для оси ''{0}'' не поддерживается."}, {"TYPED_AXIS_SUPPORT_ERR", "Итератор для типизированной оси ''{0}'' не поддерживается."}, {"STRAY_ATTRIBUTE_ERR", "Атрибут ''{0}'' вне элемента."}, {"STRAY_NAMESPACE_ERR", "Объявление пространства имен ''{0}''=''{1}'' вне элемента."}, {"NAMESPACE_PREFIX_ERR", "Пространство имен для префикса ''{0}'' не объявлено."}, {"DOM_ADAPTER_INIT_ERR", "DOMAdapter создан с неправильным типом исходного DOM."}, {"PARSER_DTD_SUPPORT_ERR", "Применяемый анализатор SAX не обрабатывает события объявления DTD."}, {"NAMESPACES_SUPPORT_ERR", "Применяемый анализатор SAX не поддерживает пространства имен XML."}, {"CANT_RESOLVE_RELATIVE_URI_ERR", "Невозможно обработать ссылку на URI ''{0}''."}};
   }
}
