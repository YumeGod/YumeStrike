package org.apache.xml.res;

import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class XMLErrorResources_ru extends ListResourceBundle {
   public static final int MAX_CODE = 61;
   public static final int MAX_WARNING = 0;
   public static final int MAX_OTHERS = 4;
   public static final int MAX_MESSAGES = 62;
   public static final String ER_FUNCTION_NOT_SUPPORTED = "ER_FUNCTION_NOT_SUPPORTED";
   public static final String ER_CANNOT_OVERWRITE_CAUSE = "ER_CANNOT_OVERWRITE_CAUSE";
   public static final String ER_NO_DEFAULT_IMPL = "ER_NO_DEFAULT_IMPL";
   public static final String ER_CHUNKEDINTARRAY_NOT_SUPPORTED = "ER_CHUNKEDINTARRAY_NOT_SUPPORTED";
   public static final String ER_OFFSET_BIGGER_THAN_SLOT = "ER_OFFSET_BIGGER_THAN_SLOT";
   public static final String ER_COROUTINE_NOT_AVAIL = "ER_COROUTINE_NOT_AVAIL";
   public static final String ER_COROUTINE_CO_EXIT = "ER_COROUTINE_CO_EXIT";
   public static final String ER_COJOINROUTINESET_FAILED = "ER_COJOINROUTINESET_FAILED";
   public static final String ER_COROUTINE_PARAM = "ER_COROUTINE_PARAM";
   public static final String ER_PARSER_DOTERMINATE_ANSWERS = "ER_PARSER_DOTERMINATE_ANSWERS";
   public static final String ER_NO_PARSE_CALL_WHILE_PARSING = "ER_NO_PARSE_CALL_WHILE_PARSING";
   public static final String ER_TYPED_ITERATOR_AXIS_NOT_IMPLEMENTED = "ER_TYPED_ITERATOR_AXIS_NOT_IMPLEMENTED";
   public static final String ER_ITERATOR_AXIS_NOT_IMPLEMENTED = "ER_ITERATOR_AXIS_NOT_IMPLEMENTED";
   public static final String ER_ITERATOR_CLONE_NOT_SUPPORTED = "ER_ITERATOR_CLONE_NOT_SUPPORTED";
   public static final String ER_UNKNOWN_AXIS_TYPE = "ER_UNKNOWN_AXIS_TYPE";
   public static final String ER_AXIS_NOT_SUPPORTED = "ER_AXIS_NOT_SUPPORTED";
   public static final String ER_NO_DTMIDS_AVAIL = "ER_NO_DTMIDS_AVAIL";
   public static final String ER_NOT_SUPPORTED = "ER_NOT_SUPPORTED";
   public static final String ER_NODE_NON_NULL = "ER_NODE_NON_NULL";
   public static final String ER_COULD_NOT_RESOLVE_NODE = "ER_COULD_NOT_RESOLVE_NODE";
   public static final String ER_STARTPARSE_WHILE_PARSING = "ER_STARTPARSE_WHILE_PARSING";
   public static final String ER_STARTPARSE_NEEDS_SAXPARSER = "ER_STARTPARSE_NEEDS_SAXPARSER";
   public static final String ER_COULD_NOT_INIT_PARSER = "ER_COULD_NOT_INIT_PARSER";
   public static final String ER_EXCEPTION_CREATING_POOL = "ER_EXCEPTION_CREATING_POOL";
   public static final String ER_PATH_CONTAINS_INVALID_ESCAPE_SEQUENCE = "ER_PATH_CONTAINS_INVALID_ESCAPE_SEQUENCE";
   public static final String ER_SCHEME_REQUIRED = "ER_SCHEME_REQUIRED";
   public static final String ER_NO_SCHEME_IN_URI = "ER_NO_SCHEME_IN_URI";
   public static final String ER_NO_SCHEME_INURI = "ER_NO_SCHEME_INURI";
   public static final String ER_PATH_INVALID_CHAR = "ER_PATH_INVALID_CHAR";
   public static final String ER_SCHEME_FROM_NULL_STRING = "ER_SCHEME_FROM_NULL_STRING";
   public static final String ER_SCHEME_NOT_CONFORMANT = "ER_SCHEME_NOT_CONFORMANT";
   public static final String ER_HOST_ADDRESS_NOT_WELLFORMED = "ER_HOST_ADDRESS_NOT_WELLFORMED";
   public static final String ER_PORT_WHEN_HOST_NULL = "ER_PORT_WHEN_HOST_NULL";
   public static final String ER_INVALID_PORT = "ER_INVALID_PORT";
   public static final String ER_FRAG_FOR_GENERIC_URI = "ER_FRAG_FOR_GENERIC_URI";
   public static final String ER_FRAG_WHEN_PATH_NULL = "ER_FRAG_WHEN_PATH_NULL";
   public static final String ER_FRAG_INVALID_CHAR = "ER_FRAG_INVALID_CHAR";
   public static final String ER_PARSER_IN_USE = "ER_PARSER_IN_USE";
   public static final String ER_CANNOT_CHANGE_WHILE_PARSING = "ER_CANNOT_CHANGE_WHILE_PARSING";
   public static final String ER_SELF_CAUSATION_NOT_PERMITTED = "ER_SELF_CAUSATION_NOT_PERMITTED";
   public static final String ER_NO_USERINFO_IF_NO_HOST = "ER_NO_USERINFO_IF_NO_HOST";
   public static final String ER_NO_PORT_IF_NO_HOST = "ER_NO_PORT_IF_NO_HOST";
   public static final String ER_NO_QUERY_STRING_IN_PATH = "ER_NO_QUERY_STRING_IN_PATH";
   public static final String ER_NO_FRAGMENT_STRING_IN_PATH = "ER_NO_FRAGMENT_STRING_IN_PATH";
   public static final String ER_CANNOT_INIT_URI_EMPTY_PARMS = "ER_CANNOT_INIT_URI_EMPTY_PARMS";
   public static final String ER_METHOD_NOT_SUPPORTED = "ER_METHOD_NOT_SUPPORTED";
   public static final String ER_INCRSAXSRCFILTER_NOT_RESTARTABLE = "ER_INCRSAXSRCFILTER_NOT_RESTARTABLE";
   public static final String ER_XMLRDR_NOT_BEFORE_STARTPARSE = "ER_XMLRDR_NOT_BEFORE_STARTPARSE";
   public static final String ER_AXIS_TRAVERSER_NOT_SUPPORTED = "ER_AXIS_TRAVERSER_NOT_SUPPORTED";
   public static final String ER_ERRORHANDLER_CREATED_WITH_NULL_PRINTWRITER = "ER_ERRORHANDLER_CREATED_WITH_NULL_PRINTWRITER";
   public static final String ER_SYSTEMID_UNKNOWN = "ER_SYSTEMID_UNKNOWN";
   public static final String ER_LOCATION_UNKNOWN = "ER_LOCATION_UNKNOWN";
   public static final String ER_PREFIX_MUST_RESOLVE = "ER_PREFIX_MUST_RESOLVE";
   public static final String ER_CREATEDOCUMENT_NOT_SUPPORTED = "ER_CREATEDOCUMENT_NOT_SUPPORTED";
   public static final String ER_CHILD_HAS_NO_OWNER_DOCUMENT = "ER_CHILD_HAS_NO_OWNER_DOCUMENT";
   public static final String ER_CHILD_HAS_NO_OWNER_DOCUMENT_ELEMENT = "ER_CHILD_HAS_NO_OWNER_DOCUMENT_ELEMENT";
   public static final String ER_CANT_OUTPUT_TEXT_BEFORE_DOC = "ER_CANT_OUTPUT_TEXT_BEFORE_DOC";
   public static final String ER_CANT_HAVE_MORE_THAN_ONE_ROOT = "ER_CANT_HAVE_MORE_THAN_ONE_ROOT";
   public static final String ER_ARG_LOCALNAME_NULL = "ER_ARG_LOCALNAME_NULL";
   public static final String ER_ARG_LOCALNAME_INVALID = "ER_ARG_LOCALNAME_INVALID";
   public static final String ER_ARG_PREFIX_INVALID = "ER_ARG_PREFIX_INVALID";
   public static final String ER_RESOURCE_COULD_NOT_FIND = "ER_RESOURCE_COULD_NOT_FIND";
   public static final String ER_RESOURCE_COULD_NOT_LOAD = "ER_RESOURCE_COULD_NOT_LOAD";
   public static final String ER_BUFFER_SIZE_LESSTHAN_ZERO = "ER_BUFFER_SIZE_LESSTHAN_ZERO";
   public static final String ER_INVALID_UTF16_SURROGATE = "ER_INVALID_UTF16_SURROGATE";
   public static final String ER_OIERROR = "ER_OIERROR";
   public static final String ER_NAMESPACE_PREFIX = "ER_NAMESPACE_PREFIX";
   public static final String ER_STRAY_ATTRIBUTE = "ER_STRAY_ATTIRBUTE";
   public static final String ER_STRAY_NAMESPACE = "ER_STRAY_NAMESPACE";
   public static final String ER_COULD_NOT_LOAD_RESOURCE = "ER_COULD_NOT_LOAD_RESOURCE";
   public static final String ER_COULD_NOT_LOAD_METHOD_PROPERTY = "ER_COULD_NOT_LOAD_METHOD_PROPERTY";
   public static final String ER_SERIALIZER_NOT_CONTENTHANDLER = "ER_SERIALIZER_NOT_CONTENTHANDLER";
   public static final String ER_ILLEGAL_ATTRIBUTE_POSITION = "ER_ILLEGAL_ATTRIBUTE_POSITION";

   public Object[][] getContents() {
      return new Object[][]{{"ER0000", "{0}"}, {"ER_FUNCTION_NOT_SUPPORTED", "Функция не поддерживается!"}, {"ER_CANNOT_OVERWRITE_CAUSE", "Невозможно перезаписать причину"}, {"ER_NO_DEFAULT_IMPL", "Не найдена реализация по умолчанию"}, {"ER_CHUNKEDINTARRAY_NOT_SUPPORTED", "ChunkedIntArray({0}) в настоящее время не поддерживается"}, {"ER_OFFSET_BIGGER_THAN_SLOT", "Смещение больше диапазона"}, {"ER_COROUTINE_NOT_AVAIL", "Coroutine недоступна, ИД={0}"}, {"ER_COROUTINE_CO_EXIT", "CoroutineManager получил запрос co_exit()"}, {"ER_COJOINROUTINESET_FAILED", "Ошибка co_joinCoroutineSet()"}, {"ER_COROUTINE_PARAM", "Ошибка параметра Coroutine ({0})"}, {"ER_PARSER_DOTERMINATE_ANSWERS", "\nНепредвиденная ошибка: Ответ анализатора doTerminate: {0}"}, {"ER_NO_PARSE_CALL_WHILE_PARSING", "Нельзя вызывать анализатор во время анализа"}, {"ER_TYPED_ITERATOR_AXIS_NOT_IMPLEMENTED", "Ошибка: типизированный итератор для оси {0} не реализован"}, {"ER_ITERATOR_AXIS_NOT_IMPLEMENTED", "Ошибка: итератор для оси {0} не реализован"}, {"ER_ITERATOR_CLONE_NOT_SUPPORTED", "Копия итератора не поддерживается"}, {"ER_UNKNOWN_AXIS_TYPE", "Неизвестный тип Traverser для оси: {0}"}, {"ER_AXIS_NOT_SUPPORTED", "Traverser для оси не поддерживается: {0}"}, {"ER_NO_DTMIDS_AVAIL", "Нет доступных ИД DTM"}, {"ER_NOT_SUPPORTED", "Не поддерживается: {0}"}, {"ER_NODE_NON_NULL", "Для getDTMHandleFromNode узел должен быть непустым"}, {"ER_COULD_NOT_RESOLVE_NODE", "Не удалось преобразовать узел в дескриптор"}, {"ER_STARTPARSE_WHILE_PARSING", "Нельзя вызывать startParse во время анализа"}, {"ER_STARTPARSE_NEEDS_SAXPARSER", "Для startParse необходим непустой SAXParser"}, {"ER_COULD_NOT_INIT_PARSER", "Не удалось инициализировать анализатор с"}, {"ER_EXCEPTION_CREATING_POOL", "Исключительная ситуация при создании нового экземпляра пула"}, {"ER_PATH_CONTAINS_INVALID_ESCAPE_SEQUENCE", "В имени пути встречается недопустимая Esc-последовательность"}, {"ER_SCHEME_REQUIRED", "Необходима схема!"}, {"ER_NO_SCHEME_IN_URI", "В URI не найдена схема: {0}"}, {"ER_NO_SCHEME_INURI", "В URI не найдена схема"}, {"ER_PATH_INVALID_CHAR", "В имени пути обнаружен недопустимый символ: {0}"}, {"ER_SCHEME_FROM_NULL_STRING", "Невозможно задать схему для пустой строки"}, {"ER_SCHEME_NOT_CONFORMANT", "Схема не конформативна."}, {"ER_HOST_ADDRESS_NOT_WELLFORMED", "Неправильно сформирован адрес хоста"}, {"ER_PORT_WHEN_HOST_NULL", "Невозможно задать порт для пустого адреса хоста"}, {"ER_INVALID_PORT", "Недопустимый номер порта"}, {"ER_FRAG_FOR_GENERIC_URI", "Фрагмент можно задать только для шаблона URI"}, {"ER_FRAG_WHEN_PATH_NULL", "Невозможно задать фрагмент для пустого пути"}, {"ER_FRAG_INVALID_CHAR", "Фрагмент содержит недопустимый символ"}, {"ER_PARSER_IN_USE", "Анализатор уже используется"}, {"ER_CANNOT_CHANGE_WHILE_PARSING", "Невозможно изменить {0} {1} во время анализа"}, {"ER_SELF_CAUSATION_NOT_PERMITTED", "Самоприсвоение недопустимо"}, {"ER_NO_USERINFO_IF_NO_HOST", "Нельзя указывать информацию о пользователе, если не задан хост"}, {"ER_NO_PORT_IF_NO_HOST", "Нельзя указывать порт, если не задан хост"}, {"ER_NO_QUERY_STRING_IN_PATH", "Нельзя указывать строку запроса в строке пути и запроса"}, {"ER_NO_FRAGMENT_STRING_IN_PATH", "Невозможно задать фрагмент одновременно для пути и фрагмента"}, {"ER_CANNOT_INIT_URI_EMPTY_PARMS", "Невозможно инициализировать URI с пустыми параметрами"}, {"ER_METHOD_NOT_SUPPORTED", "Метод еще не поддерживается"}, {"ER_INCRSAXSRCFILTER_NOT_RESTARTABLE", "Перезапуск IncrementalSAXSource_Filter в настоящее время невозможен"}, {"ER_XMLRDR_NOT_BEFORE_STARTPARSE", "Нельзя применять XMLReader до startParse"}, {"ER_AXIS_TRAVERSER_NOT_SUPPORTED", "Traverser для оси не поддерживается: {0}"}, {"ER_ERRORHANDLER_CREATED_WITH_NULL_PRINTWRITER", "ListingErrorHandler создан с пустым PrintWriter!"}, {"ER_SYSTEMID_UNKNOWN", "Неизвестный ИД системы"}, {"ER_LOCATION_UNKNOWN", "Неизвестное расположение или ошибка"}, {"ER_PREFIX_MUST_RESOLVE", "Префикс должен обеспечивать преобразование в пространство имен: {0}"}, {"ER_CREATEDOCUMENT_NOT_SUPPORTED", "createDocument() не поддерживается XPathContext!"}, {"ER_CHILD_HAS_NO_OWNER_DOCUMENT", "У атрибута child нет документа-владельца!"}, {"ER_CHILD_HAS_NO_OWNER_DOCUMENT_ELEMENT", "У атрибута child нет элемента документа-владельца!"}, {"ER_CANT_OUTPUT_TEXT_BEFORE_DOC", "Предупреждение: Невозможно вывести текст перед элементом документа! Проигнорирован..."}, {"ER_CANT_HAVE_MORE_THAN_ONE_ROOT", "В DOM может быть только один корневой элемент!"}, {"ER_ARG_LOCALNAME_NULL", "Пустой аргумент 'localName'"}, {"ER_ARG_LOCALNAME_INVALID", "Локальное имя в QNAME должно быть допустимым именем NCName"}, {"ER_ARG_PREFIX_INVALID", "Префикс в QNAME должен быть допустимым именем NCName"}, {"BAD_CODE", "Параметр createMessage лежит вне допустимого диапазона"}, {"FORMAT_FAILED", "Исключительная ситуация при вызове messageFormat"}, {"line", "Номер строки"}, {"column", "Номер столбца"}, {"ER_SERIALIZER_NOT_CONTENTHANDLER", "Класс сериализатора ''{0}'' не реализует org.xml.sax.ContentHandler."}, {"ER_RESOURCE_COULD_NOT_FIND", "Ресурс [ {0} ] не найден.\n{1}"}, {"ER_RESOURCE_COULD_NOT_LOAD", "Не удалось загрузить ресурс [ {0} ]: {1} \n {2} \t {3}"}, {"ER_BUFFER_SIZE_LESSTHAN_ZERO", "Размер буфера <=0"}, {"ER_INVALID_UTF16_SURROGATE", "Обнаружено недопустимое значение UTF-16: {0} ?"}, {"ER_OIERROR", "Ошибка ввода-вывода"}, {"ER_ILLEGAL_ATTRIBUTE_POSITION", "Невозможно добавить атрибут {0} после дочерних узлов или перед созданием элемента. Атрибут будет проигнорирован. "}, {"ER_NAMESPACE_PREFIX", "Пространство имен для префикса ''{0}'' не объявлено."}, {"ER_STRAY_ATTIRBUTE", "Атрибут ''{0}'' вне элемента."}, {"ER_STRAY_NAMESPACE", "Объявление пространства имен ''{0}''=''{1}'' вне элемента."}, {"ER_COULD_NOT_LOAD_RESOURCE", "Не удалось загрузить ''{0}'' (проверьте CLASSPATH), применяются значения по умолчанию"}, {"ER_COULD_NOT_LOAD_METHOD_PROPERTY", "Не удалось загрузить файл свойств ''{0}'' для метода вывода ''{1}'' (проверьте CLASSPATH)"}};
   }

   public static final XMLErrorResources loadResourceBundle(String className) throws MissingResourceException {
      Locale locale = Locale.getDefault();
      String suffix = getResourceSuffix(locale);

      try {
         return (XMLErrorResources)ResourceBundle.getBundle(className + suffix, locale);
      } catch (MissingResourceException var6) {
         try {
            return (XMLErrorResources)ResourceBundle.getBundle(className, new Locale("en", "US"));
         } catch (MissingResourceException var5) {
            throw new MissingResourceException("Could not load any resource bundles.", className, "");
         }
      }
   }

   private static final String getResourceSuffix(Locale locale) {
      String suffix = "_" + locale.getLanguage();
      String country = locale.getCountry();
      if (country.equals("TW")) {
         suffix = suffix + "_" + country;
      }

      return suffix;
   }
}
