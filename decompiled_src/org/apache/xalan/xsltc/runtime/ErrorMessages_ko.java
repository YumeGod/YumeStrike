package org.apache.xalan.xsltc.runtime;

import java.util.ListResourceBundle;

public class ErrorMessages_ko extends ListResourceBundle {
   public Object[][] getContents() {
      return new Object[][]{{"RUN_TIME_INTERNAL_ERR", "''{0}''의 런타임 내부 오류"}, {"RUN_TIME_COPY_ERR", "<xsl:copy> 실행시 런타임 오류가 발생했습니다."}, {"DATA_CONVERSION_ERR", "''{0}''에서 ''{1}''의 변환이 올바르지 않습니다."}, {"EXTERNAL_FUNC_ERR", "XSLTC에서 ''{0}'' 외부 함수를 지원하지 않습니다."}, {"EQUALITY_EXPR_ERR", "등식에 알 수 없는 인수 유형이 있습니다."}, {"INVALID_ARGUMENT_ERR", "''{1}''에 대한 호출에서 잘못된 인수 유형 ''{0}''"}, {"FORMAT_NUMBER_ERR", "''{1}'' 패턴을 사용하여 ''{0}'' 숫자 포맷을 시도 중입니다."}, {"ITERATOR_CLONE_ERR", "''{0}'' 반복기를 복제할 수 없습니다."}, {"AXIS_SUPPORT_ERR", "''{0}'' 축에 대한 반복기가 지원되지 않습니다."}, {"TYPED_AXIS_SUPPORT_ERR", "''{0}'' 유형화된 축에 대한 반복기가 지원되지 않습니다."}, {"STRAY_ATTRIBUTE_ERR", "''{0}'' 속성이 요소의 외부에 있습니다."}, {"STRAY_NAMESPACE_ERR", "''{0}''=''{1}'' 이름 공간 선언이 요소의 외부에 있습니다."}, {"NAMESPACE_PREFIX_ERR", "''{0}'' 접두부에 대한 이름 공간이 선언되지 않았습니다."}, {"DOM_ADAPTER_INIT_ERR", "Source DOM의 잘못된 유형을 사용하여 DOMAdapter가 작성되었습니다."}, {"PARSER_DTD_SUPPORT_ERR", "사용 중인 SAX 구문 분석기가 DTD 선언 이벤트를 처리하지 않습니다."}, {"NAMESPACES_SUPPORT_ERR", "사용 중인 SAX 구문 분석기가 XML 이름 공간을 지원하지 않습니다."}, {"CANT_RESOLVE_RELATIVE_URI_ERR", "''{0}'' URI 참조를 분석할 수 없습니다."}};
   }
}
