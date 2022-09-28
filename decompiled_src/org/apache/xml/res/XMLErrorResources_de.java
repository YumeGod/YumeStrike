package org.apache.xml.res;

import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class XMLErrorResources_de extends ListResourceBundle {
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
      return new Object[][]{{"ER0000", "{0}"}, {"ER_FUNCTION_NOT_SUPPORTED", "Funktion nicht unterstützt!"}, {"ER_CANNOT_OVERWRITE_CAUSE", "cause kann nicht überschrieben werden."}, {"ER_NO_DEFAULT_IMPL", "Keine Standardimplementierung gefunden. "}, {"ER_CHUNKEDINTARRAY_NOT_SUPPORTED", "ChunkedIntArray({0}) momentan nicht unterstützt."}, {"ER_OFFSET_BIGGER_THAN_SLOT", "Offset ist größer als Bereich."}, {"ER_COROUTINE_NOT_AVAIL", "Koroutine nicht verfügbar, ID: {0}."}, {"ER_COROUTINE_CO_EXIT", "CoroutineManager hat Anforderung co_exit() empfangen."}, {"ER_COJOINROUTINESET_FAILED", "co_joinCoroutineSet() ist fehlgeschlagen."}, {"ER_COROUTINE_PARAM", "Parameterfehler der Koroutine ({0})"}, {"ER_PARSER_DOTERMINATE_ANSWERS", "\nUNERWARTET: Parser doTerminate antwortet {0}"}, {"ER_NO_PARSE_CALL_WHILE_PARSING", "parse darf während der Syntaxanalyse nicht aufgerufen werden."}, {"ER_TYPED_ITERATOR_AXIS_NOT_IMPLEMENTED", "Fehler: Iterator mit Typangabe für Achse {0} ist nicht implementiert."}, {"ER_ITERATOR_AXIS_NOT_IMPLEMENTED", "Fehler: Iterator für Achse {0} ist nicht implementiert. "}, {"ER_ITERATOR_CLONE_NOT_SUPPORTED", "Iterator 'clone' ist nicht unterstützt."}, {"ER_UNKNOWN_AXIS_TYPE", "Unbekannter Achsentraversiertyp: {0}"}, {"ER_AXIS_NOT_SUPPORTED", "Achsentraversierer nicht unterstützt: {0}"}, {"ER_NO_DTMIDS_AVAIL", "Keine weiteren Dokumenttypmodell-IDs verfügbar"}, {"ER_NOT_SUPPORTED", "Nicht unterstützt: {0}"}, {"ER_NODE_NON_NULL", "Knoten muss ungleich Null sein für getDTMHandleFromNode."}, {"ER_COULD_NOT_RESOLVE_NODE", "Der Knoten konnte nicht in eine Kennung aufgelöst werden."}, {"ER_STARTPARSE_WHILE_PARSING", "startParse kann während der Syntaxanalyse nicht aufgerufen werden."}, {"ER_STARTPARSE_NEEDS_SAXPARSER", "startParse erfordert SAXParser ungleich Null."}, {"ER_COULD_NOT_INIT_PARSER", "Konnte Parser nicht initialisieren mit"}, {"ER_EXCEPTION_CREATING_POOL", "Ausnahmebedingung beim Erstellen eines neuen Exemplars für Pool."}, {"ER_PATH_CONTAINS_INVALID_ESCAPE_SEQUENCE", "Der Pfad enthält eine ungültige Escapezeichenfolge."}, {"ER_SCHEME_REQUIRED", "Schema ist erforderlich!"}, {"ER_NO_SCHEME_IN_URI", "Kein Schema gefunden in URI: {0}."}, {"ER_NO_SCHEME_INURI", "Kein Schema gefunden in URI"}, {"ER_PATH_INVALID_CHAR", "Pfad enthält ungültiges Zeichen: {0}."}, {"ER_SCHEME_FROM_NULL_STRING", "Schema kann nicht von Nullzeichenfolge festgelegt werden."}, {"ER_SCHEME_NOT_CONFORMANT", "Das Schema ist nicht angepasst."}, {"ER_HOST_ADDRESS_NOT_WELLFORMED", "Der Host ist keine syntaktisch korrekte Adresse."}, {"ER_PORT_WHEN_HOST_NULL", "Der Port kann nicht festgelegt werden, wenn der Host gleich Null ist."}, {"ER_INVALID_PORT", "Ungültige Portnummer"}, {"ER_FRAG_FOR_GENERIC_URI", "Fragment kann nur für eine generische URI (Uniform Resource Identifier) festgelegt werden."}, {"ER_FRAG_WHEN_PATH_NULL", "Fragment kann nicht festgelegt werden, wenn der Pfad gleich Null ist."}, {"ER_FRAG_INVALID_CHAR", "Fragment enthält ein ungültiges Zeichen."}, {"ER_PARSER_IN_USE", "Der Parser wird bereits verwendet."}, {"ER_CANNOT_CHANGE_WHILE_PARSING", "{0} {1} kann während der Syntaxanalyse nicht geändert werden."}, {"ER_SELF_CAUSATION_NOT_PERMITTED", "Selbstverursachung ist nicht zulässig."}, {"ER_NO_USERINFO_IF_NO_HOST", "Benutzerinformationen können nicht angegeben werden, wenn der Host nicht angegeben wurde."}, {"ER_NO_PORT_IF_NO_HOST", "Der Port kann nicht angegeben werden, wenn der Host nicht angegeben wurde."}, {"ER_NO_QUERY_STRING_IN_PATH", "Abfragezeichenfolge kann nicht im Pfad und in der Abfragezeichenfolge angegeben werden."}, {"ER_NO_FRAGMENT_STRING_IN_PATH", "Fragment kann nicht im Pfad und im Fragment angegeben werden."}, {"ER_CANNOT_INIT_URI_EMPTY_PARMS", "URI (Uniform Resource Identifier) kann nicht mit leeren Parametern initialisiert werden."}, {"ER_METHOD_NOT_SUPPORTED", "Die Methode wird noch nicht unterstützt. "}, {"ER_INCRSAXSRCFILTER_NOT_RESTARTABLE", "IncrementalSAXSource_Filter ist momentan nicht wieder anlauffähig."}, {"ER_XMLRDR_NOT_BEFORE_STARTPARSE", "XMLReader nicht vor Anforderung startParse"}, {"ER_AXIS_TRAVERSER_NOT_SUPPORTED", "Achsentraversierer nicht unterstützt: {0}"}, {"ER_ERRORHANDLER_CREATED_WITH_NULL_PRINTWRITER", "ListingErrorHandler erstellt ohne Druckausgabeprogramm!"}, {"ER_SYSTEMID_UNKNOWN", "System-ID unbekannt"}, {"ER_LOCATION_UNKNOWN", "Position des Fehlers unbekannt"}, {"ER_PREFIX_MUST_RESOLVE", "Das Präfix muss in einen Namensbereich aufgelöst werden: {0}"}, {"ER_CREATEDOCUMENT_NOT_SUPPORTED", "createDocument() wird nicht in XPathContext unterstützt!"}, {"ER_CHILD_HAS_NO_OWNER_DOCUMENT", "Das Attribut child weist kein Eignerdokument auf!"}, {"ER_CHILD_HAS_NO_OWNER_DOCUMENT_ELEMENT", "Das Attribut child weist kein Eignerdokumentelement auf!"}, {"ER_CANT_OUTPUT_TEXT_BEFORE_DOC", "Warnung: Vor dem Dokumentelement kann kein Text ausgegeben werden!  Wird ignoriert..."}, {"ER_CANT_HAVE_MORE_THAN_ONE_ROOT", "Mehr als ein Root für ein Dokumentobjektmodell ist nicht möglich!"}, {"ER_ARG_LOCALNAME_NULL", "Das Argument 'localName' ist Null."}, {"ER_ARG_LOCALNAME_INVALID", "Der lokale Name (Localname) in QNAME muss ein gültiger NCName sein."}, {"ER_ARG_PREFIX_INVALID", "Das Präfix in QNAME muss ein gültiger NCName sein."}, {"BAD_CODE", "Der Parameter für createMessage lag außerhalb des gültigen Bereichs"}, {"FORMAT_FAILED", "Während des Aufrufs von messageFormat wurde eine Ausnahmebedingung ausgelöst"}, {"line", "Zeilennummer"}, {"column", "Spaltennummer"}, {"ER_SERIALIZER_NOT_CONTENTHANDLER", "Die Parallel-Seriell-Umsetzerklasse ''{0}'' implementiert org.xml.sax.ContentHandler nicht."}, {"ER_RESOURCE_COULD_NOT_FIND", "Die Ressource [ {0} ] konnte nicht gefunden werden.\n {1}"}, {"ER_RESOURCE_COULD_NOT_LOAD", "Die Ressource [ {0} ] konnte nicht geladen werden: {1} \n {2} \t {3}"}, {"ER_BUFFER_SIZE_LESSTHAN_ZERO", "Puffergröße <=0"}, {"ER_INVALID_UTF16_SURROGATE", "Ungültige UTF-16-Ersetzung festgestellt: {0} ?"}, {"ER_OIERROR", "E/A-Fehler"}, {"ER_ILLEGAL_ATTRIBUTE_POSITION", "Attribut {0} kann nicht nach Kindknoten oder vor dem Erstellen eines Elements hinzugefügt werden.  Das Attribut wird ignoriert."}, {"ER_NAMESPACE_PREFIX", "Der Namensbereich für Präfix ''{0}'' wurde nicht deklariert."}, {"ER_STRAY_ATTIRBUTE", "Attribut ''{0}'' befindet sich nicht in einem Element."}, {"ER_STRAY_NAMESPACE", "Namensbereichsdeklaration ''{0}''=''{1}'' befindet sich nicht in einem Element."}, {"ER_COULD_NOT_LOAD_RESOURCE", "''{0}'' konnte nicht geladen werden (CLASSPATH prüfen); es werden die Standardwerte verwendet"}, {"ER_COULD_NOT_LOAD_METHOD_PROPERTY", "Merkmaldatei ''{0}'' konnte für Ausgabemethode ''{1}'' nicht geladen werden (CLASSPATH prüfen)"}};
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
