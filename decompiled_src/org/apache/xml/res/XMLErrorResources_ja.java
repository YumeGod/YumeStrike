package org.apache.xml.res;

import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class XMLErrorResources_ja extends ListResourceBundle {
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
      return new Object[][]{{"ER0000", "{0}"}, {"ER_FUNCTION_NOT_SUPPORTED", "機能はサポートされていません!"}, {"ER_CANNOT_OVERWRITE_CAUSE", "cause を上書きできません"}, {"ER_NO_DEFAULT_IMPL", "デフォルト・インプリメンテーションが見つかりません "}, {"ER_CHUNKEDINTARRAY_NOT_SUPPORTED", "現在 ChunkedIntArray({0}) はサポートされていません"}, {"ER_OFFSET_BIGGER_THAN_SLOT", "オフセットがスロットより大です"}, {"ER_COROUTINE_NOT_AVAIL", "連携ルーチンが使用可能でありません。id={0}"}, {"ER_COROUTINE_CO_EXIT", "CoroutineManager が co_exit() 要求を受け取りました"}, {"ER_COJOINROUTINESET_FAILED", "co_joinCoroutineSet() が失敗しました"}, {"ER_COROUTINE_PARAM", "連携ルーチン・パラメーター・エラー ({0})"}, {"ER_PARSER_DOTERMINATE_ANSWERS", "\n予想外: パーサー doTerminate が {0} を応答しています"}, {"ER_NO_PARSE_CALL_WHILE_PARSING", "parse は構文解析中に呼び出してはいけません"}, {"ER_TYPED_ITERATOR_AXIS_NOT_IMPLEMENTED", "エラー: 軸 {0} の型付きイテレーターはインプリメントされていません"}, {"ER_ITERATOR_AXIS_NOT_IMPLEMENTED", "エラー: 軸 {0} のイテレーターはインプリメントされていません "}, {"ER_ITERATOR_CLONE_NOT_SUPPORTED", "イテレーターの複製はサポートされていません"}, {"ER_UNKNOWN_AXIS_TYPE", "不明の軸トラバース・タイプ: {0}"}, {"ER_AXIS_NOT_SUPPORTED", "軸トラバーサーはサポートされていません: {0}"}, {"ER_NO_DTMIDS_AVAIL", "使用可能な DTM ID はこれ以上ありません"}, {"ER_NOT_SUPPORTED", "サポートされていません: {0}"}, {"ER_NODE_NON_NULL", "getDTMHandleFromNode のノードは非ヌルでなければなりません"}, {"ER_COULD_NOT_RESOLVE_NODE", "ノードをハンドルに解決できませんでした"}, {"ER_STARTPARSE_WHILE_PARSING", "startParse は構文解析中に呼び出してはいけません"}, {"ER_STARTPARSE_NEEDS_SAXPARSER", "startParse にはヌル以外の SAXParser が必要です"}, {"ER_COULD_NOT_INIT_PARSER", "パーサーを次で初期化できませんでした:"}, {"ER_EXCEPTION_CREATING_POOL", "プールの新規インスタンスを作成中に例外"}, {"ER_PATH_CONTAINS_INVALID_ESCAPE_SEQUENCE", "パスに無効なエスケープ・シーケンスが含まれています"}, {"ER_SCHEME_REQUIRED", "スキームが必要です!"}, {"ER_NO_SCHEME_IN_URI", "スキームは URI {0} で見つかりません"}, {"ER_NO_SCHEME_INURI", "スキームは URI で見つかりません"}, {"ER_PATH_INVALID_CHAR", "パスに無効文字: {0} が含まれています"}, {"ER_SCHEME_FROM_NULL_STRING", "ヌル・ストリングからはスキームを設定できません"}, {"ER_SCHEME_NOT_CONFORMANT", "スキームは一致していません。"}, {"ER_HOST_ADDRESS_NOT_WELLFORMED", "ホストはうまく構成されたアドレスでありません"}, {"ER_PORT_WHEN_HOST_NULL", "ホストがヌルであるとポートを設定できません"}, {"ER_INVALID_PORT", "無効なポート番号"}, {"ER_FRAG_FOR_GENERIC_URI", "総称 URI のフラグメントしか設定できません"}, {"ER_FRAG_WHEN_PATH_NULL", "パスがヌルであるとフラグメントを設定できません"}, {"ER_FRAG_INVALID_CHAR", "フラグメントに無効文字が含まれています"}, {"ER_PARSER_IN_USE", "パーサーはすでに使用中です"}, {"ER_CANNOT_CHANGE_WHILE_PARSING", "構文解析中に {0} {1} を変更できません"}, {"ER_SELF_CAUSATION_NOT_PERMITTED", "自己原因は許可されません"}, {"ER_NO_USERINFO_IF_NO_HOST", "ホストが指定されていない場合は Userinfo を指定してはいけません"}, {"ER_NO_PORT_IF_NO_HOST", "ホストが指定されていない場合はポートを指定してはいけません"}, {"ER_NO_QUERY_STRING_IN_PATH", "照会ストリングはパスおよび照会ストリング内に指定できません"}, {"ER_NO_FRAGMENT_STRING_IN_PATH", "フラグメントはパスとフラグメントの両方に指定できません"}, {"ER_CANNOT_INIT_URI_EMPTY_PARMS", "URI は空のパラメーターを使用して初期化できません"}, {"ER_METHOD_NOT_SUPPORTED", "メソッドはまだサポートされていません "}, {"ER_INCRSAXSRCFILTER_NOT_RESTARTABLE", "現在 IncrementalSAXSource_Filter は再始動可能でありません"}, {"ER_XMLRDR_NOT_BEFORE_STARTPARSE", "XMLReader が startParse 要求の前でありません"}, {"ER_AXIS_TRAVERSER_NOT_SUPPORTED", "軸トラバーサーはサポートされていません: {0}"}, {"ER_ERRORHANDLER_CREATED_WITH_NULL_PRINTWRITER", "ListingErrorHandler がヌル PrintWriter で作成されました!"}, {"ER_SYSTEMID_UNKNOWN", "SystemId は不明"}, {"ER_LOCATION_UNKNOWN", "エラーのロケーションは不明"}, {"ER_PREFIX_MUST_RESOLVE", "接頭部はネーム・スペースに解決されなければなりません: {0}"}, {"ER_CREATEDOCUMENT_NOT_SUPPORTED", "createDocument() は XPathContext 内でサポートされません!"}, {"ER_CHILD_HAS_NO_OWNER_DOCUMENT", "属性の子に所有者文書がありません!"}, {"ER_CHILD_HAS_NO_OWNER_DOCUMENT_ELEMENT", "属性の子に所有者文書エレメントがありません!"}, {"ER_CANT_OUTPUT_TEXT_BEFORE_DOC", "警告: 文書エレメントの前にテキストを出力できません!  無視しています..."}, {"ER_CANT_HAVE_MORE_THAN_ONE_ROOT", "DOM では複数のルートをもてません!"}, {"ER_ARG_LOCALNAME_NULL", "引き数 'localName' がヌルです。"}, {"ER_ARG_LOCALNAME_INVALID", "QNAME 内のローカル名は有効な NCName であるはずです"}, {"ER_ARG_PREFIX_INVALID", "QNAME 内の接頭部は有効な NCName であるはずです"}, {"BAD_CODE", "createMessage へのパラメーターが境界外でした。"}, {"FORMAT_FAILED", "messageFormat 呼び出し中に例外がスローされました。"}, {"line", "行 #"}, {"column", "桁 #"}, {"ER_SERIALIZER_NOT_CONTENTHANDLER", "シリアライザー・クラス ''{0}'' は org.xml.sax.ContentHandler をインプリメントしません。"}, {"ER_RESOURCE_COULD_NOT_FIND", "リソース [ {0} ] は見つかりませんでした。\n {1}"}, {"ER_RESOURCE_COULD_NOT_LOAD", "リソース [ {0} ] をロードできませんでした: {1} \n {2} \t {3}"}, {"ER_BUFFER_SIZE_LESSTHAN_ZERO", "バッファー・サイズ <=0"}, {"ER_INVALID_UTF16_SURROGATE", "無効な UTF-16 サロゲートが検出されました: {0} ?"}, {"ER_OIERROR", "入出力エラー"}, {"ER_ILLEGAL_ATTRIBUTE_POSITION", "下位ノードの後またはエレメントが生成される前に属性 {0} を追加できません。属性は無視されます。"}, {"ER_NAMESPACE_PREFIX", "接頭部 ''{0}'' のネーム・スペースが宣言されていません。"}, {"ER_STRAY_ATTIRBUTE", "属性 ''{0}'' がエレメントの外側です。"}, {"ER_STRAY_NAMESPACE", "ネーム・スペース宣言 ''{0}''=''{1}'' がエレメントの外側です。"}, {"ER_COULD_NOT_LOAD_RESOURCE", "''{0}'' をロードできませんでした (CLASSPATH を調べてください)。現在は単にデフォルトを使用しています。"}, {"ER_COULD_NOT_LOAD_METHOD_PROPERTY", "出力メソッド ''{1}'' のプロパティー・ファイル ''{0}'' をロードできませんでした (CLASSPATH を確認)"}};
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
