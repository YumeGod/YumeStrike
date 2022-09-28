package org.apache.xalan.xsltc.runtime;

import java.util.ListResourceBundle;

public class ErrorMessages_tr extends ListResourceBundle {
   public Object[][] getContents() {
      return new Object[][]{{"RUN_TIME_INTERNAL_ERR", "''{0}'' sınıfında çalışma zamanı iç hatası "}, {"RUN_TIME_COPY_ERR", "<xsl:copy> yürütülürken çalışma zamanı hatası."}, {"DATA_CONVERSION_ERR", "''{0}'' tipinden ''{1}'' tipine dönüştürme geçersiz."}, {"EXTERNAL_FUNC_ERR", "''{0}'' dış işlevi XSLTC tarafından desteklenmiyor."}, {"EQUALITY_EXPR_ERR", "Eşitlik ifadesinde bilinmeyen bağımsız değişken tipi."}, {"INVALID_ARGUMENT_ERR", "''{1}'' işlevi çağrısında ''{0}'' bağımsız değişken tipi geçersiz "}, {"FORMAT_NUMBER_ERR", "''{0}'' sayısını ''{1}'' örüntüsünü kullanarak biçimleme girişimi."}, {"ITERATOR_CLONE_ERR", "''{0}'' yineleyicisinin eşkopyası yaratılamıyor."}, {"AXIS_SUPPORT_ERR", "''{0}'' ekseni için yineleyici desteklenmiyor."}, {"TYPED_AXIS_SUPPORT_ERR", "Tip atanmış ''{0}'' ekseni için yineleyici desteklenmiyor."}, {"STRAY_ATTRIBUTE_ERR", "''{0}'' özniteliği öğenin dışında."}, {"STRAY_NAMESPACE_ERR", "''{0}''=''{1}'' ad alanı bildirimi öğenin dışında."}, {"NAMESPACE_PREFIX_ERR", "''{0}'' önekine ilişkin ad alanı bildirilmedi."}, {"DOM_ADAPTER_INIT_ERR", "DOMAdapter, yanlış tipte kaynak DOM kullanılarak yaratıldı."}, {"PARSER_DTD_SUPPORT_ERR", "Kullandığınız SAX ayrıştırıcısı DTD bildirim olaylarını işlemiyor."}, {"NAMESPACES_SUPPORT_ERR", "Kullandığınız SAX ayrıştırıcısının XML ad alanları desteği yok."}, {"CANT_RESOLVE_RELATIVE_URI_ERR", "''{0}'' URI başvurusu çözülemedi."}};
   }
}
