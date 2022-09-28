package org.apache.xml.res;

import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class XMLErrorResources_fr extends ListResourceBundle {
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
      return new Object[][]{{"ER0000", "{0}"}, {"ER_FUNCTION_NOT_SUPPORTED", "Fonction non prise en charge !"}, {"ER_CANNOT_OVERWRITE_CAUSE", "Impossible de remplacer la cause"}, {"ER_NO_DEFAULT_IMPL", "Impossible de trouver une implémentation par défaut "}, {"ER_CHUNKEDINTARRAY_NOT_SUPPORTED", "ChunkedIntArray({0}) n''est pas pris en charge"}, {"ER_OFFSET_BIGGER_THAN_SLOT", "Décalage plus important que l'emplacement"}, {"ER_COROUTINE_NOT_AVAIL", "Coroutine non disponible, id={0}"}, {"ER_COROUTINE_CO_EXIT", "CoroutineManager a reçu une demande de co_exit()"}, {"ER_COJOINROUTINESET_FAILED", "Echec de co_joinCoroutineSet()"}, {"ER_COROUTINE_PARAM", "Erreur de paramètre de Coroutine ({0})"}, {"ER_PARSER_DOTERMINATE_ANSWERS", "\nRESULTAT INATTENDU : L''analyseur doTerminate répond {0}"}, {"ER_NO_PARSE_CALL_WHILE_PARSING", "parse ne peut être appelé lors de l'analyse"}, {"ER_TYPED_ITERATOR_AXIS_NOT_IMPLEMENTED", "Erreur : itérateur typé de l''axe {0} non implémenté"}, {"ER_ITERATOR_AXIS_NOT_IMPLEMENTED", "Erreur : itérateur de l''axe {0} non implémenté "}, {"ER_ITERATOR_CLONE_NOT_SUPPORTED", "Clone de l'itérateur non pris en charge"}, {"ER_UNKNOWN_AXIS_TYPE", "Type transversal d''axe inconnu : {0}"}, {"ER_AXIS_NOT_SUPPORTED", "Traverseur d''axe non pris en charge : {0}"}, {"ER_NO_DTMIDS_AVAIL", "Aucun autre ID de DTM disponible"}, {"ER_NOT_SUPPORTED", "Non pris en charge : {0}"}, {"ER_NODE_NON_NULL", "Le noeud ne doit pas être vide pour getDTMHandleFromNode"}, {"ER_COULD_NOT_RESOLVE_NODE", "Impossible de convertir le noeud en pointeur"}, {"ER_STARTPARSE_WHILE_PARSING", "startParse ne peut être appelé pendant l'analyse"}, {"ER_STARTPARSE_NEEDS_SAXPARSER", "startParse requiert un SAXParser non vide"}, {"ER_COULD_NOT_INIT_PARSER", "impossible d'initialiser l'analyseur"}, {"ER_EXCEPTION_CREATING_POOL", "exception durant la création d'une instance du pool"}, {"ER_PATH_CONTAINS_INVALID_ESCAPE_SEQUENCE", "Le chemin d'accès contient une séquence d'échappement non valide"}, {"ER_SCHEME_REQUIRED", "Processus requis !"}, {"ER_NO_SCHEME_IN_URI", "Processus introuvable dans l''URI : {0}"}, {"ER_NO_SCHEME_INURI", "Processus introuvable dans l'URI"}, {"ER_PATH_INVALID_CHAR", "Le chemin contient un caractère non valide : {0}"}, {"ER_SCHEME_FROM_NULL_STRING", "Impossible de définir le processus à partir de la chaîne vide"}, {"ER_SCHEME_NOT_CONFORMANT", "Le processus n'est pas conforme."}, {"ER_HOST_ADDRESS_NOT_WELLFORMED", "L'hôte n'est pas une adresse bien formée"}, {"ER_PORT_WHEN_HOST_NULL", "Le port ne peut être défini quand l'hôte est vide"}, {"ER_INVALID_PORT", "Numéro de port non valide"}, {"ER_FRAG_FOR_GENERIC_URI", "Le fragment ne peut être défini que pour un URI générique"}, {"ER_FRAG_WHEN_PATH_NULL", "Le fragment ne peut être défini quand le chemin d'accès est vide"}, {"ER_FRAG_INVALID_CHAR", "Le fragment contient un caractère non valide"}, {"ER_PARSER_IN_USE", "L'analyseur est déjà utilisé"}, {"ER_CANNOT_CHANGE_WHILE_PARSING", "Impossible de modifier {0} {1} durant l''analyse"}, {"ER_SELF_CAUSATION_NOT_PERMITTED", "Auto-causalité interdite"}, {"ER_NO_USERINFO_IF_NO_HOST", "Userinfo ne peut être spécifié si l'hôte ne l'est pas"}, {"ER_NO_PORT_IF_NO_HOST", "Le port peut ne pas être spécifié si l'hôte n'est pas spécifié"}, {"ER_NO_QUERY_STRING_IN_PATH", "La chaîne de requête ne doit pas figurer dans un chemin et une chaîne de requête"}, {"ER_NO_FRAGMENT_STRING_IN_PATH", "Le fragment ne doit pas être indiqué à la fois dans le chemin et dans le fragment"}, {"ER_CANNOT_INIT_URI_EMPTY_PARMS", "Impossible d'initialiser l'URI avec des paramètres vides"}, {"ER_METHOD_NOT_SUPPORTED", "Cette méthode n'est pas encore prise en charge "}, {"ER_INCRSAXSRCFILTER_NOT_RESTARTABLE", "IncrementalSAXSource_Filter ne peut redémarrer"}, {"ER_XMLRDR_NOT_BEFORE_STARTPARSE", "XMLReader ne figure pas avant la demande startParse"}, {"ER_AXIS_TRAVERSER_NOT_SUPPORTED", "Traverseur d''axe non pris en charge : {0}"}, {"ER_ERRORHANDLER_CREATED_WITH_NULL_PRINTWRITER", "ListingErrorHandler créé avec PrintWriter vide !"}, {"ER_SYSTEMID_UNKNOWN", "ID système inconnu"}, {"ER_LOCATION_UNKNOWN", "Emplacement inconnu de l'erreur"}, {"ER_PREFIX_MUST_RESOLVE", "Le préfixe doit se convertir en espace de noms : {0}"}, {"ER_CREATEDOCUMENT_NOT_SUPPORTED", "createDocument() non pris en charge dans XPathContext !"}, {"ER_CHILD_HAS_NO_OWNER_DOCUMENT", "L'enfant de l'attribut ne possède pas de document propriétaire !"}, {"ER_CHILD_HAS_NO_OWNER_DOCUMENT_ELEMENT", "Le contexte ne possède pas d'élément de document propriétaire !"}, {"ER_CANT_OUTPUT_TEXT_BEFORE_DOC", "Avertissement : impossible d'afficher du texte avant l'élément de document !  Traitement ignoré..."}, {"ER_CANT_HAVE_MORE_THAN_ONE_ROOT", "Un DOM ne peut posséder plusieurs racines !"}, {"ER_ARG_LOCALNAME_NULL", "L'argument ''localName'' est vide"}, {"ER_ARG_LOCALNAME_INVALID", "Dans QNAME, le nom local doit être un nom NCName valide"}, {"ER_ARG_PREFIX_INVALID", "Dans QNAME, le préfixe doit être un nom NCName valide"}, {"BAD_CODE", "Le paramètre de createMessage se trouve hors limites"}, {"FORMAT_FAILED", "Exception soulevée lors de l'appel de messageFormat"}, {"line", "Ligne #"}, {"column", "Colonne #"}, {"ER_SERIALIZER_NOT_CONTENTHANDLER", "La classe de la méthode de sérialisation ''{0}'' n''implémente pas org.xml.sax.ContentHandler."}, {"ER_RESOURCE_COULD_NOT_FIND", "La ressource [ {0} ] est introuvable.\n {1}"}, {"ER_RESOURCE_COULD_NOT_LOAD", "La ressource [ {0} ] n''a pas pu charger : {1} \n {2} \t {3}"}, {"ER_BUFFER_SIZE_LESSTHAN_ZERO", "Taille du tampon <=0"}, {"ER_INVALID_UTF16_SURROGATE", "Substitut UTF-16 non valide détecté : {0} ?"}, {"ER_OIERROR", "Erreur d''E-S"}, {"ER_ILLEGAL_ATTRIBUTE_POSITION", "Ajout impossible de l''attribut {0} après des noeuds enfants ou avant la production d''un élément.  L''attribut est ignoré."}, {"ER_NAMESPACE_PREFIX", "L''espace de noms du préfixe ''{0}'' n''a pas été déclaré."}, {"ER_STRAY_ATTIRBUTE", "L''attribut ''{0}'' est à l''extérieur de l''élément."}, {"ER_STRAY_NAMESPACE", "La déclaration d''espace de noms ''{0}''=''{1}'' est à l''extérieur de l''élément."}, {"ER_COULD_NOT_LOAD_RESOURCE", "Impossible de charger ''{0}'' (vérifier CLASSPATH), les valeurs par défaut sont donc employées "}, {"ER_COULD_NOT_LOAD_METHOD_PROPERTY", "Impossible de charger le fichier de propriétés ''{0}'' pour la méthode de sortie ''{1}'' (vérifier CLASSPATH)"}};
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
