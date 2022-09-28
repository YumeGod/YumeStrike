package org.apache.wml.dom;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;
import org.apache.wml.WMLDocument;
import org.apache.xerces.dom.DocumentImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;

public class WMLDocumentImpl extends DocumentImpl implements WMLDocument {
   private static final long serialVersionUID = 3257844364091929145L;
   private static Hashtable _elementTypesWML;
   private static final Class[] _elemClassSigWML;
   // $FF: synthetic field
   static Class class$org$apache$wml$dom$WMLDocumentImpl;
   // $FF: synthetic field
   static Class class$java$lang$String;
   // $FF: synthetic field
   static Class class$org$apache$wml$dom$WMLBElementImpl;
   // $FF: synthetic field
   static Class class$org$apache$wml$dom$WMLNoopElementImpl;
   // $FF: synthetic field
   static Class class$org$apache$wml$dom$WMLAElementImpl;
   // $FF: synthetic field
   static Class class$org$apache$wml$dom$WMLSetvarElementImpl;
   // $FF: synthetic field
   static Class class$org$apache$wml$dom$WMLAccessElementImpl;
   // $FF: synthetic field
   static Class class$org$apache$wml$dom$WMLStrongElementImpl;
   // $FF: synthetic field
   static Class class$org$apache$wml$dom$WMLPostfieldElementImpl;
   // $FF: synthetic field
   static Class class$org$apache$wml$dom$WMLDoElementImpl;
   // $FF: synthetic field
   static Class class$org$apache$wml$dom$WMLWmlElementImpl;
   // $FF: synthetic field
   static Class class$org$apache$wml$dom$WMLTrElementImpl;
   // $FF: synthetic field
   static Class class$org$apache$wml$dom$WMLGoElementImpl;
   // $FF: synthetic field
   static Class class$org$apache$wml$dom$WMLBigElementImpl;
   // $FF: synthetic field
   static Class class$org$apache$wml$dom$WMLAnchorElementImpl;
   // $FF: synthetic field
   static Class class$org$apache$wml$dom$WMLTimerElementImpl;
   // $FF: synthetic field
   static Class class$org$apache$wml$dom$WMLSmallElementImpl;
   // $FF: synthetic field
   static Class class$org$apache$wml$dom$WMLOptgroupElementImpl;
   // $FF: synthetic field
   static Class class$org$apache$wml$dom$WMLHeadElementImpl;
   // $FF: synthetic field
   static Class class$org$apache$wml$dom$WMLTdElementImpl;
   // $FF: synthetic field
   static Class class$org$apache$wml$dom$WMLFieldsetElementImpl;
   // $FF: synthetic field
   static Class class$org$apache$wml$dom$WMLImgElementImpl;
   // $FF: synthetic field
   static Class class$org$apache$wml$dom$WMLRefreshElementImpl;
   // $FF: synthetic field
   static Class class$org$apache$wml$dom$WMLOneventElementImpl;
   // $FF: synthetic field
   static Class class$org$apache$wml$dom$WMLInputElementImpl;
   // $FF: synthetic field
   static Class class$org$apache$wml$dom$WMLPrevElementImpl;
   // $FF: synthetic field
   static Class class$org$apache$wml$dom$WMLTableElementImpl;
   // $FF: synthetic field
   static Class class$org$apache$wml$dom$WMLMetaElementImpl;
   // $FF: synthetic field
   static Class class$org$apache$wml$dom$WMLTemplateElementImpl;
   // $FF: synthetic field
   static Class class$org$apache$wml$dom$WMLBrElementImpl;
   // $FF: synthetic field
   static Class class$org$apache$wml$dom$WMLOptionElementImpl;
   // $FF: synthetic field
   static Class class$org$apache$wml$dom$WMLUElementImpl;
   // $FF: synthetic field
   static Class class$org$apache$wml$dom$WMLPElementImpl;
   // $FF: synthetic field
   static Class class$org$apache$wml$dom$WMLSelectElementImpl;
   // $FF: synthetic field
   static Class class$org$apache$wml$dom$WMLEmElementImpl;
   // $FF: synthetic field
   static Class class$org$apache$wml$dom$WMLIElementImpl;
   // $FF: synthetic field
   static Class class$org$apache$wml$dom$WMLCardElementImpl;

   public Element createElement(String var1) throws DOMException {
      Class var2 = (Class)_elementTypesWML.get(var1);
      if (var2 != null) {
         try {
            Constructor var3 = var2.getConstructor(_elemClassSigWML);
            return (Element)var3.newInstance(this, var1);
         } catch (Exception var6) {
            Object var5;
            if (var6 instanceof InvocationTargetException) {
               var5 = ((InvocationTargetException)var6).getTargetException();
            } else {
               var5 = var6;
            }

            System.out.println("Exception " + var5.getClass().getName());
            System.out.println(((Throwable)var5).getMessage());
            throw new IllegalStateException("Tag '" + var1 + "' associated with an Element class that failed to construct.");
         }
      } else {
         return new WMLElementImpl(this, var1);
      }
   }

   public WMLDocumentImpl(DocumentType var1) {
      super(var1, false);
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   static {
      _elemClassSigWML = new Class[]{class$org$apache$wml$dom$WMLDocumentImpl == null ? (class$org$apache$wml$dom$WMLDocumentImpl = class$("org.apache.wml.dom.WMLDocumentImpl")) : class$org$apache$wml$dom$WMLDocumentImpl, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String};
      _elementTypesWML = new Hashtable();
      _elementTypesWML.put("b", class$org$apache$wml$dom$WMLBElementImpl == null ? (class$org$apache$wml$dom$WMLBElementImpl = class$("org.apache.wml.dom.WMLBElementImpl")) : class$org$apache$wml$dom$WMLBElementImpl);
      _elementTypesWML.put("noop", class$org$apache$wml$dom$WMLNoopElementImpl == null ? (class$org$apache$wml$dom$WMLNoopElementImpl = class$("org.apache.wml.dom.WMLNoopElementImpl")) : class$org$apache$wml$dom$WMLNoopElementImpl);
      _elementTypesWML.put("a", class$org$apache$wml$dom$WMLAElementImpl == null ? (class$org$apache$wml$dom$WMLAElementImpl = class$("org.apache.wml.dom.WMLAElementImpl")) : class$org$apache$wml$dom$WMLAElementImpl);
      _elementTypesWML.put("setvar", class$org$apache$wml$dom$WMLSetvarElementImpl == null ? (class$org$apache$wml$dom$WMLSetvarElementImpl = class$("org.apache.wml.dom.WMLSetvarElementImpl")) : class$org$apache$wml$dom$WMLSetvarElementImpl);
      _elementTypesWML.put("access", class$org$apache$wml$dom$WMLAccessElementImpl == null ? (class$org$apache$wml$dom$WMLAccessElementImpl = class$("org.apache.wml.dom.WMLAccessElementImpl")) : class$org$apache$wml$dom$WMLAccessElementImpl);
      _elementTypesWML.put("strong", class$org$apache$wml$dom$WMLStrongElementImpl == null ? (class$org$apache$wml$dom$WMLStrongElementImpl = class$("org.apache.wml.dom.WMLStrongElementImpl")) : class$org$apache$wml$dom$WMLStrongElementImpl);
      _elementTypesWML.put("postfield", class$org$apache$wml$dom$WMLPostfieldElementImpl == null ? (class$org$apache$wml$dom$WMLPostfieldElementImpl = class$("org.apache.wml.dom.WMLPostfieldElementImpl")) : class$org$apache$wml$dom$WMLPostfieldElementImpl);
      _elementTypesWML.put("do", class$org$apache$wml$dom$WMLDoElementImpl == null ? (class$org$apache$wml$dom$WMLDoElementImpl = class$("org.apache.wml.dom.WMLDoElementImpl")) : class$org$apache$wml$dom$WMLDoElementImpl);
      _elementTypesWML.put("wml", class$org$apache$wml$dom$WMLWmlElementImpl == null ? (class$org$apache$wml$dom$WMLWmlElementImpl = class$("org.apache.wml.dom.WMLWmlElementImpl")) : class$org$apache$wml$dom$WMLWmlElementImpl);
      _elementTypesWML.put("tr", class$org$apache$wml$dom$WMLTrElementImpl == null ? (class$org$apache$wml$dom$WMLTrElementImpl = class$("org.apache.wml.dom.WMLTrElementImpl")) : class$org$apache$wml$dom$WMLTrElementImpl);
      _elementTypesWML.put("go", class$org$apache$wml$dom$WMLGoElementImpl == null ? (class$org$apache$wml$dom$WMLGoElementImpl = class$("org.apache.wml.dom.WMLGoElementImpl")) : class$org$apache$wml$dom$WMLGoElementImpl);
      _elementTypesWML.put("big", class$org$apache$wml$dom$WMLBigElementImpl == null ? (class$org$apache$wml$dom$WMLBigElementImpl = class$("org.apache.wml.dom.WMLBigElementImpl")) : class$org$apache$wml$dom$WMLBigElementImpl);
      _elementTypesWML.put("anchor", class$org$apache$wml$dom$WMLAnchorElementImpl == null ? (class$org$apache$wml$dom$WMLAnchorElementImpl = class$("org.apache.wml.dom.WMLAnchorElementImpl")) : class$org$apache$wml$dom$WMLAnchorElementImpl);
      _elementTypesWML.put("timer", class$org$apache$wml$dom$WMLTimerElementImpl == null ? (class$org$apache$wml$dom$WMLTimerElementImpl = class$("org.apache.wml.dom.WMLTimerElementImpl")) : class$org$apache$wml$dom$WMLTimerElementImpl);
      _elementTypesWML.put("small", class$org$apache$wml$dom$WMLSmallElementImpl == null ? (class$org$apache$wml$dom$WMLSmallElementImpl = class$("org.apache.wml.dom.WMLSmallElementImpl")) : class$org$apache$wml$dom$WMLSmallElementImpl);
      _elementTypesWML.put("optgroup", class$org$apache$wml$dom$WMLOptgroupElementImpl == null ? (class$org$apache$wml$dom$WMLOptgroupElementImpl = class$("org.apache.wml.dom.WMLOptgroupElementImpl")) : class$org$apache$wml$dom$WMLOptgroupElementImpl);
      _elementTypesWML.put("head", class$org$apache$wml$dom$WMLHeadElementImpl == null ? (class$org$apache$wml$dom$WMLHeadElementImpl = class$("org.apache.wml.dom.WMLHeadElementImpl")) : class$org$apache$wml$dom$WMLHeadElementImpl);
      _elementTypesWML.put("td", class$org$apache$wml$dom$WMLTdElementImpl == null ? (class$org$apache$wml$dom$WMLTdElementImpl = class$("org.apache.wml.dom.WMLTdElementImpl")) : class$org$apache$wml$dom$WMLTdElementImpl);
      _elementTypesWML.put("fieldset", class$org$apache$wml$dom$WMLFieldsetElementImpl == null ? (class$org$apache$wml$dom$WMLFieldsetElementImpl = class$("org.apache.wml.dom.WMLFieldsetElementImpl")) : class$org$apache$wml$dom$WMLFieldsetElementImpl);
      _elementTypesWML.put("img", class$org$apache$wml$dom$WMLImgElementImpl == null ? (class$org$apache$wml$dom$WMLImgElementImpl = class$("org.apache.wml.dom.WMLImgElementImpl")) : class$org$apache$wml$dom$WMLImgElementImpl);
      _elementTypesWML.put("refresh", class$org$apache$wml$dom$WMLRefreshElementImpl == null ? (class$org$apache$wml$dom$WMLRefreshElementImpl = class$("org.apache.wml.dom.WMLRefreshElementImpl")) : class$org$apache$wml$dom$WMLRefreshElementImpl);
      _elementTypesWML.put("onevent", class$org$apache$wml$dom$WMLOneventElementImpl == null ? (class$org$apache$wml$dom$WMLOneventElementImpl = class$("org.apache.wml.dom.WMLOneventElementImpl")) : class$org$apache$wml$dom$WMLOneventElementImpl);
      _elementTypesWML.put("input", class$org$apache$wml$dom$WMLInputElementImpl == null ? (class$org$apache$wml$dom$WMLInputElementImpl = class$("org.apache.wml.dom.WMLInputElementImpl")) : class$org$apache$wml$dom$WMLInputElementImpl);
      _elementTypesWML.put("prev", class$org$apache$wml$dom$WMLPrevElementImpl == null ? (class$org$apache$wml$dom$WMLPrevElementImpl = class$("org.apache.wml.dom.WMLPrevElementImpl")) : class$org$apache$wml$dom$WMLPrevElementImpl);
      _elementTypesWML.put("table", class$org$apache$wml$dom$WMLTableElementImpl == null ? (class$org$apache$wml$dom$WMLTableElementImpl = class$("org.apache.wml.dom.WMLTableElementImpl")) : class$org$apache$wml$dom$WMLTableElementImpl);
      _elementTypesWML.put("meta", class$org$apache$wml$dom$WMLMetaElementImpl == null ? (class$org$apache$wml$dom$WMLMetaElementImpl = class$("org.apache.wml.dom.WMLMetaElementImpl")) : class$org$apache$wml$dom$WMLMetaElementImpl);
      _elementTypesWML.put("template", class$org$apache$wml$dom$WMLTemplateElementImpl == null ? (class$org$apache$wml$dom$WMLTemplateElementImpl = class$("org.apache.wml.dom.WMLTemplateElementImpl")) : class$org$apache$wml$dom$WMLTemplateElementImpl);
      _elementTypesWML.put("br", class$org$apache$wml$dom$WMLBrElementImpl == null ? (class$org$apache$wml$dom$WMLBrElementImpl = class$("org.apache.wml.dom.WMLBrElementImpl")) : class$org$apache$wml$dom$WMLBrElementImpl);
      _elementTypesWML.put("option", class$org$apache$wml$dom$WMLOptionElementImpl == null ? (class$org$apache$wml$dom$WMLOptionElementImpl = class$("org.apache.wml.dom.WMLOptionElementImpl")) : class$org$apache$wml$dom$WMLOptionElementImpl);
      _elementTypesWML.put("u", class$org$apache$wml$dom$WMLUElementImpl == null ? (class$org$apache$wml$dom$WMLUElementImpl = class$("org.apache.wml.dom.WMLUElementImpl")) : class$org$apache$wml$dom$WMLUElementImpl);
      _elementTypesWML.put("p", class$org$apache$wml$dom$WMLPElementImpl == null ? (class$org$apache$wml$dom$WMLPElementImpl = class$("org.apache.wml.dom.WMLPElementImpl")) : class$org$apache$wml$dom$WMLPElementImpl);
      _elementTypesWML.put("select", class$org$apache$wml$dom$WMLSelectElementImpl == null ? (class$org$apache$wml$dom$WMLSelectElementImpl = class$("org.apache.wml.dom.WMLSelectElementImpl")) : class$org$apache$wml$dom$WMLSelectElementImpl);
      _elementTypesWML.put("em", class$org$apache$wml$dom$WMLEmElementImpl == null ? (class$org$apache$wml$dom$WMLEmElementImpl = class$("org.apache.wml.dom.WMLEmElementImpl")) : class$org$apache$wml$dom$WMLEmElementImpl);
      _elementTypesWML.put("i", class$org$apache$wml$dom$WMLIElementImpl == null ? (class$org$apache$wml$dom$WMLIElementImpl = class$("org.apache.wml.dom.WMLIElementImpl")) : class$org$apache$wml$dom$WMLIElementImpl);
      _elementTypesWML.put("card", class$org$apache$wml$dom$WMLCardElementImpl == null ? (class$org$apache$wml$dom$WMLCardElementImpl = class$("org.apache.wml.dom.WMLCardElementImpl")) : class$org$apache$wml$dom$WMLCardElementImpl);
   }
}
