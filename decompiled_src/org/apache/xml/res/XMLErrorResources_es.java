package org.apache.xml.res;

import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class XMLErrorResources_es extends ListResourceBundle {
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
      return new Object[][]{{"ER0000", "{0}"}, {"ER_FUNCTION_NOT_SUPPORTED", "¡Función no soportada!"}, {"ER_CANNOT_OVERWRITE_CAUSE", "No se puede escribir encima de la causa"}, {"ER_NO_DEFAULT_IMPL", "No se ha encontrado una implementación por omisión"}, {"ER_CHUNKEDINTARRAY_NOT_SUPPORTED", "ChunkedIntArray({0}) no soportada actualmente"}, {"ER_OFFSET_BIGGER_THAN_SLOT", "El desplazamiento es mayor que el espacio"}, {"ER_COROUTINE_NOT_AVAIL", "Corrutina no disponible, id={0}"}, {"ER_COROUTINE_CO_EXIT", "CoroutineManager ha recibido una petición co_exit()"}, {"ER_COJOINROUTINESET_FAILED", "Anomalía de co_joinCoroutineSet()"}, {"ER_COROUTINE_PARAM", "Error del parámetro de corrutina ({0})"}, {"ER_PARSER_DOTERMINATE_ANSWERS", "\nINESPERADO: Respuestas doTerminate del analizador {0}"}, {"ER_NO_PARSE_CALL_WHILE_PARSING", "No se puede llamar a parse mientras se está analizando"}, {"ER_TYPED_ITERATOR_AXIS_NOT_IMPLEMENTED", "Error: El iterador escrito para el eje {0} no está implementado"}, {"ER_ITERATOR_AXIS_NOT_IMPLEMENTED", "Error: El iterador para el eje {0} no está implementado"}, {"ER_ITERATOR_CLONE_NOT_SUPPORTED", "La réplica del iterador no está soportada"}, {"ER_UNKNOWN_AXIS_TYPE", "Tipo de cruce de eje desconocido: {0}"}, {"ER_AXIS_NOT_SUPPORTED", "Cruzador de eje no soportado: {0}"}, {"ER_NO_DTMIDS_AVAIL", "No hay más ID de DTM disponibles"}, {"ER_NOT_SUPPORTED", "No soportado: {0}"}, {"ER_NODE_NON_NULL", "El nodo no debe ser nulo para getDTMHandleFromNode"}, {"ER_COULD_NOT_RESOLVE_NODE", "No se puede resolver el nodo como un manejador"}, {"ER_STARTPARSE_WHILE_PARSING", "No se puede llamar a startParse mientras se está analizando"}, {"ER_STARTPARSE_NEEDS_SAXPARSER", "startParse necesita un SAXParser no nulo"}, {"ER_COULD_NOT_INIT_PARSER", "No se ha podido inicializar el analizador con "}, {"ER_EXCEPTION_CREATING_POOL", "Se ha producido una excepción al crear la nueva instancia de la agrupación"}, {"ER_PATH_CONTAINS_INVALID_ESCAPE_SEQUENCE", "La vía de acceso contiene una secuencia de escape no válida"}, {"ER_SCHEME_REQUIRED", "¡Se necesita un esquema!"}, {"ER_NO_SCHEME_IN_URI", "No se ha encontrado un esquema en el URI: {0}"}, {"ER_NO_SCHEME_INURI", "No se ha encontrado un esquema en el URI"}, {"ER_PATH_INVALID_CHAR", "La vía de acceso contiene un carácter no válido: {0}"}, {"ER_SCHEME_FROM_NULL_STRING", "No se puede establecer un esquema de una serie nula"}, {"ER_SCHEME_NOT_CONFORMANT", "El esquema no es compatible."}, {"ER_HOST_ADDRESS_NOT_WELLFORMED", "El sistema principal no es una dirección bien formada"}, {"ER_PORT_WHEN_HOST_NULL", "No se puede establecer el puerto si el sistema principal es nulo"}, {"ER_INVALID_PORT", "Número de puerto no válido"}, {"ER_FRAG_FOR_GENERIC_URI", "Sólo se puede establecer el fragmento para un URI genérico"}, {"ER_FRAG_WHEN_PATH_NULL", "No se puede establecer el fragmento si la vía de acceso es nula"}, {"ER_FRAG_INVALID_CHAR", "El fragmento contiene un carácter no válido"}, {"ER_PARSER_IN_USE", "El analizador ya está en uso"}, {"ER_CANNOT_CHANGE_WHILE_PARSING", "No se puede cambiar {0} {1} mientras se analiza"}, {"ER_SELF_CAUSATION_NOT_PERMITTED", "Autocausalidad no permitida"}, {"ER_NO_USERINFO_IF_NO_HOST", "No se puede especificar la información de usuario si no se ha especificado el sistema principal"}, {"ER_NO_PORT_IF_NO_HOST", "No se puede especificar el puerto si no se ha especificado el sistema principal"}, {"ER_NO_QUERY_STRING_IN_PATH", "No se puede especificar la serie de consulta en la vía de acceso y en la serie de consulta"}, {"ER_NO_FRAGMENT_STRING_IN_PATH", "No se puede especificar el fragmento en la vía de acceso y en el fragmento"}, {"ER_CANNOT_INIT_URI_EMPTY_PARMS", "No se puede inicializar el URI con parámetros vacíos"}, {"ER_METHOD_NOT_SUPPORTED", "El método no está aún soportado"}, {"ER_INCRSAXSRCFILTER_NOT_RESTARTABLE", "IncrementalSAXSource_Filter no es actualmente reiniciable"}, {"ER_XMLRDR_NOT_BEFORE_STARTPARSE", "XMLReader no debe ir antes que la petición startParse"}, {"ER_AXIS_TRAVERSER_NOT_SUPPORTED", "Cruzador de eje no soportado: {0}"}, {"ER_ERRORHANDLER_CREATED_WITH_NULL_PRINTWRITER", "¡Se ha creado ListingErrorHandler con PrintWriter nulo!"}, {"ER_SYSTEMID_UNKNOWN", "SystemId desconocido"}, {"ER_LOCATION_UNKNOWN", "Ubicación del error desconocida"}, {"ER_PREFIX_MUST_RESOLVE", "El prefijo debe resolverse como un espacio de nombres: {0}"}, {"ER_CREATEDOCUMENT_NOT_SUPPORTED", "¡createDocument() no soportada en XPathContext!"}, {"ER_CHILD_HAS_NO_OWNER_DOCUMENT", "¡El hijo atributo no tiene un documento propietario!"}, {"ER_CHILD_HAS_NO_OWNER_DOCUMENT_ELEMENT", "¡El hijo atributo no tiene un elemento documento propietario!"}, {"ER_CANT_OUTPUT_TEXT_BEFORE_DOC", "¡Aviso: no puede haber salida de texto antes del elemento documento! Ignorando..."}, {"ER_CANT_HAVE_MORE_THAN_ONE_ROOT", "¡No puede haber más de una raíz en DOM!"}, {"ER_ARG_LOCALNAME_NULL", "El argumento 'localName' es nulo"}, {"ER_ARG_LOCALNAME_INVALID", "Localname en QNAME debe ser un NCName válido"}, {"ER_ARG_PREFIX_INVALID", "Prefix en QNAME debe ser un NCName válido"}, {"BAD_CODE", "El parámetro para createMessage estaba fuera de los límites"}, {"FORMAT_FAILED", "Se ha generado una excepción durante la llamada messageFormat"}, {"line", "Línea núm."}, {"column", "Columna núm."}, {"ER_SERIALIZER_NOT_CONTENTHANDLER", "La clase serializer ''{0}'' no implementa org.xml.sax.ContentHandler."}, {"ER_RESOURCE_COULD_NOT_FIND", "No se ha podido cargar el recurso [ {0} ].\n{1}"}, {"ER_RESOURCE_COULD_NOT_LOAD", "No se ha podido cargar el recurso [ {0} ]: {1} \n {2} \t {3}"}, {"ER_BUFFER_SIZE_LESSTHAN_ZERO", "Tamaño de almacenamiento intermedio <=0"}, {"ER_INVALID_UTF16_SURROGATE", "¿Se ha detectado un sustituto UTF-16 no válido: {0}?"}, {"ER_OIERROR", "Error de ES"}, {"ER_ILLEGAL_ATTRIBUTE_POSITION", "No se puede añadir el atributo {0} después de nodos hijo o antes de que se produzca un elemento. Se ignorará el atributo."}, {"ER_NAMESPACE_PREFIX", "No se ha declarado el espacio de nombres para el prefijo ''{0}''."}, {"ER_STRAY_ATTIRBUTE", "Atributo ''{0}'' fuera del elemento."}, {"ER_STRAY_NAMESPACE", "Declaración del espacio de nombres ''{0}''=''{1}'' fuera del elemento."}, {"ER_COULD_NOT_LOAD_RESOURCE", "No se ha podido cargar ''{0}'' (compruebe la CLASSPATH), ahora sólo se están utilizando los valores por omisión"}, {"ER_COULD_NOT_LOAD_METHOD_PROPERTY", "No se ha podido cargar el archivo de propiedades ''{0}'' para el método de salida ''{1}'' (compruebe la CLASSPATH)"}};
   }

   public static final XMLErrorResources loadResourceBundle(String className) throws MissingResourceException {
      Locale locale = Locale.getDefault();
      String suffix = getResourceSuffix(locale);

      try {
         return (XMLErrorResources)ResourceBundle.getBundle(className + suffix, locale);
      } catch (MissingResourceException var6) {
         try {
            return (XMLErrorResources)ResourceBundle.getBundle(className, new Locale("es", "ES"));
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
