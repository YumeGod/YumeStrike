package org.apache.xml.res;

import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class XMLErrorResources_sk extends ListResourceBundle {
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
      return new Object[][]{{"ER0000", "{0}"}, {"ER_FUNCTION_NOT_SUPPORTED", "Funkcia nie je podporovaná!"}, {"ER_CANNOT_OVERWRITE_CAUSE", "Nie je možné prepísať príčinu"}, {"ER_NO_DEFAULT_IMPL", "Nebola nájdená žiadna predvolená implementácia "}, {"ER_CHUNKEDINTARRAY_NOT_SUPPORTED", "ChunkedIntArray({0}) nie je momentálne podporovaný"}, {"ER_OFFSET_BIGGER_THAN_SLOT", "Offset väčší, než zásuvka"}, {"ER_COROUTINE_NOT_AVAIL", "Korutina nie je dostupná, id={0}"}, {"ER_COROUTINE_CO_EXIT", "CoroutineManager obdržal požiadavku co_exit()"}, {"ER_COJOINROUTINESET_FAILED", "zlyhal co_joinCoroutineSet()"}, {"ER_COROUTINE_PARAM", "Chyba parametra korutiny ({0})"}, {"ER_PARSER_DOTERMINATE_ANSWERS", "\nNEOČAKÁVANÉ: Analyzátor doTerminate odpovedá {0}"}, {"ER_NO_PARSE_CALL_WHILE_PARSING", "syntaktický analyzátor nemôže byť volaný počas vykonávania analýzy"}, {"ER_TYPED_ITERATOR_AXIS_NOT_IMPLEMENTED", "Chyba: napísaný iterátor pre os {0} nie je implementovaný"}, {"ER_ITERATOR_AXIS_NOT_IMPLEMENTED", "Chyba: iterátor pre os {0} nie je implementovaný "}, {"ER_ITERATOR_CLONE_NOT_SUPPORTED", "Klon iterátora nie je podporovaný"}, {"ER_UNKNOWN_AXIS_TYPE", "Neznámy typ pretínania osí: {0}"}, {"ER_AXIS_NOT_SUPPORTED", "Pretínanie osí nie je podporované: {0}"}, {"ER_NO_DTMIDS_AVAIL", "Žiadne ďalšie DTM ID nie sú dostupné"}, {"ER_NOT_SUPPORTED", "Nie je podporované: {0}"}, {"ER_NODE_NON_NULL", "Pre getDTMHandleFromNode musí byť uzol nenulový"}, {"ER_COULD_NOT_RESOLVE_NODE", "Nebolo možné určiť uzol na spracovanie"}, {"ER_STARTPARSE_WHILE_PARSING", "startParse nemôže byť volaný počas vykonávania analýzy"}, {"ER_STARTPARSE_NEEDS_SAXPARSER", "startParse potrebuje nenulový SAXParser"}, {"ER_COULD_NOT_INIT_PARSER", "nebolo možné inicializovať syntaktický analyzátor pomocou"}, {"ER_EXCEPTION_CREATING_POOL", "výnimka vytvárania novej inštancie oblasti"}, {"ER_PATH_CONTAINS_INVALID_ESCAPE_SEQUENCE", "Cesta obsahuje neplatnú únikovú sekvenciu"}, {"ER_SCHEME_REQUIRED", "Je požadovaná schéma!"}, {"ER_NO_SCHEME_IN_URI", "V URI sa nenašla žiadna schéma: {0}"}, {"ER_NO_SCHEME_INURI", "V URI nebola nájdená žiadna schéma"}, {"ER_PATH_INVALID_CHAR", "Cesta obsahuje neplatný znak: {0}"}, {"ER_SCHEME_FROM_NULL_STRING", "Nie je možné stanoviť schému z nulového reťazca"}, {"ER_SCHEME_NOT_CONFORMANT", "Nezhodná schéma."}, {"ER_HOST_ADDRESS_NOT_WELLFORMED", "Hostiteľ nie je správne formátovaná adresa"}, {"ER_PORT_WHEN_HOST_NULL", "Nemôže byť stanovený port, ak je hostiteľ null"}, {"ER_INVALID_PORT", "Neplatné číslo portu"}, {"ER_FRAG_FOR_GENERIC_URI", "Fragment môže byť stanovený len pre všeobecné URI"}, {"ER_FRAG_WHEN_PATH_NULL", "Ak je cesta nulová, nemôže byť stanovený fragment"}, {"ER_FRAG_INVALID_CHAR", "Fragment obsahuje neplatný znak"}, {"ER_PARSER_IN_USE", "Syntaktický analyzátor je už používaný"}, {"ER_CANNOT_CHANGE_WHILE_PARSING", "Nie je možné zmeniť {0} {1} počas vykonávania analýzy"}, {"ER_SELF_CAUSATION_NOT_PERMITTED", "Samozapríčinenie nie je povolené"}, {"ER_NO_USERINFO_IF_NO_HOST", "Ak nebol zadaný hostiteľ, možno nebolo zadané userinfo"}, {"ER_NO_PORT_IF_NO_HOST", "Ak nebol zadaný hostiteľ, možno nebol zadaný port"}, {"ER_NO_QUERY_STRING_IN_PATH", "Reťazec dotazu nemôže byť zadaný v ceste a reťazci dotazu"}, {"ER_NO_FRAGMENT_STRING_IN_PATH", "Fragment nemôže byť zadaný v ceste, ani vo fragmente"}, {"ER_CANNOT_INIT_URI_EMPTY_PARMS", "Nie je možné inicializovať URI s prázdnymi parametrami"}, {"ER_METHOD_NOT_SUPPORTED", "Metóda ešte nie je podporovaná "}, {"ER_INCRSAXSRCFILTER_NOT_RESTARTABLE", "IncrementalSAXSource_Filter nie je momentálne reštartovateľný"}, {"ER_XMLRDR_NOT_BEFORE_STARTPARSE", "XMLReader nepredchádza požiadavke na startParse"}, {"ER_AXIS_TRAVERSER_NOT_SUPPORTED", "Pretínanie osí nie je podporované: {0}"}, {"ER_ERRORHANDLER_CREATED_WITH_NULL_PRINTWRITER", "ListingErrorHandler vytvorený s nulovým PrintWriter!"}, {"ER_SYSTEMID_UNKNOWN", "Neznáme SystemId"}, {"ER_LOCATION_UNKNOWN", "Neznáme miesto výskytu chyby"}, {"ER_PREFIX_MUST_RESOLVE", "Predpona sa musí rozlíšiť do názvového priestoru: {0}"}, {"ER_CREATEDOCUMENT_NOT_SUPPORTED", "createDocument() nie je podporované XPathContext!"}, {"ER_CHILD_HAS_NO_OWNER_DOCUMENT", "Potomok atribútu nemá dokument vlastníka!"}, {"ER_CHILD_HAS_NO_OWNER_DOCUMENT_ELEMENT", "Potomok atribútu nemá súčasť dokumentu vlastníka!"}, {"ER_CANT_OUTPUT_TEXT_BEFORE_DOC", "Upozornenie: nemožno vypustiť text pred elementom dokumentu!  Ignorovanie..."}, {"ER_CANT_HAVE_MORE_THAN_ONE_ROOT", "Nie je možné mať viac, než jeden koreň DOM!"}, {"ER_ARG_LOCALNAME_NULL", "Argument 'localName' je null"}, {"ER_ARG_LOCALNAME_INVALID", "Lokálny názov v QNAME by mal byť platným NCName"}, {"ER_ARG_PREFIX_INVALID", "Predpona v QNAME by mala byť platným NCName"}, {"BAD_CODE", "Parameter na createMessage bol mimo ohraničenia"}, {"FORMAT_FAILED", "Výnimka počas volania messageFormat"}, {"line", "Riadok #"}, {"column", "Stĺpec #"}, {"ER_SERIALIZER_NOT_CONTENTHANDLER", "Trieda serializátora ''{0}'' neimplementuje org.xml.sax.ContentHandler."}, {"ER_RESOURCE_COULD_NOT_FIND", "Prostriedok [ {0} ] nemohol byť nájdený.\n {1}"}, {"ER_RESOURCE_COULD_NOT_LOAD", "Prostriedok [ {0} ] sa nedal načítať: {1} \n {2} \t {3}"}, {"ER_BUFFER_SIZE_LESSTHAN_ZERO", "Veľkosť vyrovnávacej pamäte <=0"}, {"ER_INVALID_UTF16_SURROGATE", "Bolo zistené neplatné nahradenie UTF-16: {0} ?"}, {"ER_OIERROR", "chyba IO"}, {"ER_ILLEGAL_ATTRIBUTE_POSITION", "Nie je možné pridať atribút {0} po uzloch potomka alebo pred vytvorením elementu.  Atribút bude ignorovaný."}, {"ER_NAMESPACE_PREFIX", "Názvový priestor pre predponu ''{0}'' nebol deklarovaný."}, {"ER_STRAY_ATTIRBUTE", "Atribút ''{0}'' je mimo elementu."}, {"ER_STRAY_NAMESPACE", "Deklarácia názvového priestoru ''{0}''=''{1}'' je mimo elementu."}, {"ER_COULD_NOT_LOAD_RESOURCE", "Nedalo sa načítať ''{0}'' (skontrolujte CLASSPATH), používajú sa predvolené hodnoty"}, {"ER_COULD_NOT_LOAD_METHOD_PROPERTY", "Nedal sa načítať súbor vlastností ''{0}'' pre výstupnú metódu ''{1}'' (skontrolujte CLASSPATH)"}};
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
