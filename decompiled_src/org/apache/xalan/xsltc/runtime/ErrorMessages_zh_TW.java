package org.apache.xalan.xsltc.runtime;

import java.util.ListResourceBundle;

public class ErrorMessages_zh_TW extends ListResourceBundle {
   public Object[][] getContents() {
      return new Object[][]{{"RUN_TIME_INTERNAL_ERR", "''{0}'' 發生執行時期內部錯誤"}, {"RUN_TIME_COPY_ERR", "執行 <xsl:copy> 時，發生執行時期錯誤。"}, {"DATA_CONVERSION_ERR", "無法將 ''{0}'' 轉換為 ''{1}''。"}, {"EXTERNAL_FUNC_ERR", "XSLTC 不支援外部函數 ''{0}''。"}, {"EQUALITY_EXPR_ERR", "相等表示式中包含不明的引數類型。"}, {"INVALID_ARGUMENT_ERR", "在呼叫 ''{1}'' 中的引數類型 ''{0}'' 無效"}, {"FORMAT_NUMBER_ERR", "嘗試使用型樣 ''{1}'' 格式化數字 ''{0}''。"}, {"ITERATOR_CLONE_ERR", "無法複製重複項目 ''{0}''。"}, {"AXIS_SUPPORT_ERR", "軸 ''{0}'' 的重複項目未受支援。"}, {"TYPED_AXIS_SUPPORT_ERR", "輸入軸 ''{0}'' 的重複項目未受支援。"}, {"STRAY_ATTRIBUTE_ERR", "屬性 ''{0}'' 超出元素外。"}, {"STRAY_NAMESPACE_ERR", "名稱空間宣告 ''{0}''=''{1}'' 超出元素外。"}, {"NAMESPACE_PREFIX_ERR", "字首 ''{0}'' 的名稱空間尚未宣告。"}, {"DOM_ADAPTER_INIT_ERR", "建立 DOMAdapter 時使用的原始檔 DOM 類型錯誤。"}, {"PARSER_DTD_SUPPORT_ERR", "您使用的 SAX 剖析器無法處理 DTD 宣告事件。"}, {"NAMESPACES_SUPPORT_ERR", "您使用的 SAX 剖析器不支援 XML 名稱空間。"}, {"CANT_RESOLVE_RELATIVE_URI_ERR", "無法解析 URI 參照 ''{0}''。"}};
   }
}
