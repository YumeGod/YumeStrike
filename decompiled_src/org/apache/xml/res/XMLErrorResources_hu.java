package org.apache.xml.res;

import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class XMLErrorResources_hu extends ListResourceBundle {
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
      return new Object[][]{{"ER0000", "{0}"}, {"ER_FUNCTION_NOT_SUPPORTED", "A függvény nem támogatott!"}, {"ER_CANNOT_OVERWRITE_CAUSE", "Nem lehet felülírni az okot"}, {"ER_NO_DEFAULT_IMPL", "Nem találtunk alapértelmezett megvalósítást "}, {"ER_CHUNKEDINTARRAY_NOT_SUPPORTED", "A ChunkedIntArray({0}) jelenleg nem támogatott"}, {"ER_OFFSET_BIGGER_THAN_SLOT", "Az eltolás nagyobb mint a nyílás"}, {"ER_COROUTINE_NOT_AVAIL", "Társ-szubrutin nem érhető el, id={0}"}, {"ER_COROUTINE_CO_EXIT", "CoroutineManager érkezett a co_exit() kérésre"}, {"ER_COJOINROUTINESET_FAILED", "A co_joinCoroutineSet() nem sikerült"}, {"ER_COROUTINE_PARAM", "Társ-szubrutin paraméter hiba ({0})"}, {"ER_PARSER_DOTERMINATE_ANSWERS", "\nVÁRATLAN: elemző doTerminate válaszok {0}"}, {"ER_NO_PARSE_CALL_WHILE_PARSING", "A parse-t nem hívhatja meg a elemzés közben"}, {"ER_TYPED_ITERATOR_AXIS_NOT_IMPLEMENTED", "Hiba: A típusos iterátor a(z) {0} tengelyhez nincs megvalósítva"}, {"ER_ITERATOR_AXIS_NOT_IMPLEMENTED", "Hiba: Az iterátor a(z) {0} tengelyhez nincs megvalósítva "}, {"ER_ITERATOR_CLONE_NOT_SUPPORTED", "Az iterátor klónok nem támogatottak"}, {"ER_UNKNOWN_AXIS_TYPE", "Ismeretlen tengelytraverzál-típus: {0}"}, {"ER_AXIS_NOT_SUPPORTED", "Tengelytraverzál nem támogatott: {0}"}, {"ER_NO_DTMIDS_AVAIL", "Nincs több DTM ID"}, {"ER_NOT_SUPPORTED", "Nem támogatott: {0}"}, {"ER_NODE_NON_NULL", "A csomópint nem-null kell legyen a getDTMHandleFromNode-hoz"}, {"ER_COULD_NOT_RESOLVE_NODE", "Nem lehet a csomópontot hivatkozásra feloldani"}, {"ER_STARTPARSE_WHILE_PARSING", "A startParse-t nem hívhatja elemzés közben"}, {"ER_STARTPARSE_NEEDS_SAXPARSER", "A startParse-nak nem-null SAXParser kell"}, {"ER_COULD_NOT_INIT_PARSER", "Nem lehet inicializálni az elemzőt ezzel"}, {"ER_EXCEPTION_CREATING_POOL", "kivétel egy új pool példány létrehozásánál"}, {"ER_PATH_CONTAINS_INVALID_ESCAPE_SEQUENCE", "Az eléréi út érvénytelen eszképszekvenciát tartalmaz"}, {"ER_SCHEME_REQUIRED", "Sémára szükség van!"}, {"ER_NO_SCHEME_IN_URI", "Nem található séma az URI-ban: {0}"}, {"ER_NO_SCHEME_INURI", "Nem található séma az URI-ban"}, {"ER_PATH_INVALID_CHAR", "Az elérési út érvénytelen karaktert tartalmaz: {0}"}, {"ER_SCHEME_FROM_NULL_STRING", "Nem lehet beállítani a sémát null karakterláncból"}, {"ER_SCHEME_NOT_CONFORMANT", "A séma nem megfelelő."}, {"ER_HOST_ADDRESS_NOT_WELLFORMED", "A host nem jól formázott cím"}, {"ER_PORT_WHEN_HOST_NULL", "A port-t nem állíthatja be, ha a host null"}, {"ER_INVALID_PORT", "Érvénytelen portszám"}, {"ER_FRAG_FOR_GENERIC_URI", "Darabot csak egy általános URI-hoz állíthat be"}, {"ER_FRAG_WHEN_PATH_NULL", "A darabot csak nem állíthatja be, hí az elérési út null"}, {"ER_FRAG_INVALID_CHAR", "A darab érvénytelen karaktert tartalmaz"}, {"ER_PARSER_IN_USE", "Az elemző már használatban van"}, {"ER_CANNOT_CHANGE_WHILE_PARSING", "Nem változtatható meg a(z) {0} {1} elemzés közben"}, {"ER_SELF_CAUSATION_NOT_PERMITTED", "Az ön-megokolás nem megengedett"}, {"ER_NO_USERINFO_IF_NO_HOST", "Nem adhat meg userinfo-t, ha nem adott meg host-ot"}, {"ER_NO_PORT_IF_NO_HOST", "Nem adhat meg port-ot, ha nem adott meg host-ot"}, {"ER_NO_QUERY_STRING_IN_PATH", "Lekérdezési karakterláncot nem adhat meg elérési útban és lekérdezési karakterláncban"}, {"ER_NO_FRAGMENT_STRING_IN_PATH", "Darabot nem adhat meg sem az elérési útban sem a darabban"}, {"ER_CANNOT_INIT_URI_EMPTY_PARMS", "Nem inicializálhatja az URI-t üres paraméterekkel"}, {"ER_METHOD_NOT_SUPPORTED", "A metódus még nem támogatott "}, {"ER_INCRSAXSRCFILTER_NOT_RESTARTABLE", "Az IncrementalSAXSource_Filter jelenleg nem índítható újra"}, {"ER_XMLRDR_NOT_BEFORE_STARTPARSE", "XMLReader nem a startParse kérés előtt"}, {"ER_AXIS_TRAVERSER_NOT_SUPPORTED", "Tengelytraverzál nem támogatott: {0}"}, {"ER_ERRORHANDLER_CREATED_WITH_NULL_PRINTWRITER", "A ListingErrorHandler létrejött null PrintWriter-rel!"}, {"ER_SYSTEMID_UNKNOWN", "Ismeretlen SystemId"}, {"ER_LOCATION_UNKNOWN", "A hiba helye ismeretlen"}, {"ER_PREFIX_MUST_RESOLVE", "Az előtagnak egy névtérre kell feloldódnia: {0}"}, {"ER_CREATEDOCUMENT_NOT_SUPPORTED", "A createDocument() nem támogatott az XPathContext-ben!"}, {"ER_CHILD_HAS_NO_OWNER_DOCUMENT", "Az attribútum leszármazottnak nincs tulajdonos dokumentuma!"}, {"ER_CHILD_HAS_NO_OWNER_DOCUMENT_ELEMENT", "Az attribútum leszármazottnak nincs tulajdonos dokumentum eleme!"}, {"ER_CANT_OUTPUT_TEXT_BEFORE_DOC", "Figyelmeztetés: nem lehet szöveget kiírni dokumentum elem előtt!  Figyelmen kívül marad..."}, {"ER_CANT_HAVE_MORE_THAN_ONE_ROOT", "Nem lehet egynél több gyökér a DOM-on!"}, {"ER_ARG_LOCALNAME_NULL", "A 'localName' argumentum null"}, {"ER_ARG_LOCALNAME_INVALID", "A QNAME-beli helyi névnek egy érvényes NCName-nek kell lenni"}, {"ER_ARG_PREFIX_INVALID", "A QNAME-beli prefixnek egy érvényes NCName-nek kell lenni"}, {"BAD_CODE", "A createMessage paramétere nincs a megfelelő tartományban"}, {"FORMAT_FAILED", "Kivétel történt a messageFormat hívás alatt"}, {"line", "Sor #"}, {"column", "Oszlop #"}, {"ER_SERIALIZER_NOT_CONTENTHANDLER", "A(z) ''{0}'' serializer osztály nem valósítja meg az org.xml.sax.ContentHandler funkciót."}, {"ER_RESOURCE_COULD_NOT_FIND", "A(z) [ {0} ] erőforrás nem található.\n {1}"}, {"ER_RESOURCE_COULD_NOT_LOAD", "Az erőforrást [ {0} ] nem lehet betölteni: {1} \n {2} \t {3}"}, {"ER_BUFFER_SIZE_LESSTHAN_ZERO", "Pufferméret <= 0"}, {"ER_INVALID_UTF16_SURROGATE", "Érvénytelen UTF-16 helyettesítés: {0} ?"}, {"ER_OIERROR", "IO hiba"}, {"ER_ILLEGAL_ATTRIBUTE_POSITION", "Nem lehet {0} attribútumat felvenni a gyermek node-ok után vagy mielőtt egy elem létrejönne.  Az attribútum figyelmen kívül marad."}, {"ER_NAMESPACE_PREFIX", "A(z) ''{0}'' előtag névtere nem definiált."}, {"ER_STRAY_ATTIRBUTE", "A(z) ''{0}'' attribútum kívül esik az elemen."}, {"ER_STRAY_NAMESPACE", "A(z) ''{0}''=''{1}'' névtér-deklaráció kívül esik az elemen."}, {"ER_COULD_NOT_LOAD_RESOURCE", "Nem lehet betölteni ''{0}''-t (ellenőrizze a CLASSPATH beállítást), az alapértelmezéseket használom"}, {"ER_COULD_NOT_LOAD_METHOD_PROPERTY", "Nem lehet betölteni a(z) ''{0}'' tulajdonság-fájlt a(z) ''{1}''  (ellenőrizze a CLASSPATH beállítást)"}};
   }

   public static final XMLErrorResources loadResourceBundle(String className) throws MissingResourceException {
      Locale locale = Locale.getDefault();
      String suffix = getResourceSuffix(locale);

      try {
         return (XMLErrorResources)ResourceBundle.getBundle(className + suffix, locale);
      } catch (MissingResourceException var6) {
         try {
            return (XMLErrorResources)ResourceBundle.getBundle(className, new Locale("hu", "HU"));
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
