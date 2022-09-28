package org.apache.xalan.extensions;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.dtm.ref.DTMNodeIterator;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XRTreeFrag;
import org.apache.xpath.objects.XString;
import org.w3c.dom.Node;

public class MethodResolver {
   public static final int STATIC_ONLY = 1;
   public static final int INSTANCE_ONLY = 2;
   public static final int STATIC_AND_INSTANCE = 3;
   public static final int DYNAMIC = 4;
   private static final int SCOREBASE = 1;
   private static final ConversionInfo[] m_javaObjConversions;
   private static final ConversionInfo[] m_booleanConversions;
   private static final ConversionInfo[] m_numberConversions;
   private static final ConversionInfo[] m_stringConversions;
   private static final ConversionInfo[] m_rtfConversions;
   private static final ConversionInfo[] m_nodesetConversions;
   private static final ConversionInfo[][] m_conversions;
   // $FF: synthetic field
   static Class class$org$apache$xalan$extensions$ExpressionContext;
   // $FF: synthetic field
   static Class class$org$apache$xalan$templates$ElemExtensionCall;
   // $FF: synthetic field
   static Class class$org$apache$xalan$extensions$XSLProcessorContext;
   // $FF: synthetic field
   static Class class$java$lang$String;
   // $FF: synthetic field
   static Class class$java$lang$Boolean;
   // $FF: synthetic field
   static Class class$java$lang$Object;
   // $FF: synthetic field
   static Class class$java$lang$Double;
   // $FF: synthetic field
   static Class class$org$w3c$dom$traversal$NodeIterator;
   // $FF: synthetic field
   static Class class$org$w3c$dom$NodeList;
   // $FF: synthetic field
   static Class class$org$w3c$dom$Node;
   // $FF: synthetic field
   static Class class$java$lang$Class;

   public static Constructor getConstructor(Class classObj, Object[] argsIn, Object[][] argsOut, ExpressionContext exprContext) throws NoSuchMethodException, SecurityException, TransformerException {
      Constructor bestConstructor = null;
      Class[] bestParamTypes = null;
      Constructor[] constructors = classObj.getConstructors();
      int nMethods = constructors.length;
      int bestScore = Integer.MAX_VALUE;
      int bestScoreCount = 0;

      for(int i = 0; i < nMethods; ++i) {
         Constructor ctor = constructors[i];
         Class[] paramTypes = ctor.getParameterTypes();
         int numberMethodParams = paramTypes.length;
         int paramStart = 0;
         boolean isFirstExpressionContext = false;
         short scoreStart;
         if (numberMethodParams == argsIn.length + 1) {
            Class javaClass = paramTypes[0];
            if (!(class$org$apache$xalan$extensions$ExpressionContext == null ? (class$org$apache$xalan$extensions$ExpressionContext = class$("org.apache.xalan.extensions.ExpressionContext")) : class$org$apache$xalan$extensions$ExpressionContext).isAssignableFrom(javaClass)) {
               continue;
            }

            isFirstExpressionContext = true;
            scoreStart = 0;
            ++paramStart;
         } else {
            scoreStart = 1000;
         }

         if (argsIn.length == numberMethodParams - paramStart) {
            int score = scoreMatch(paramTypes, paramStart, argsIn, scoreStart);
            if (-1 != score) {
               if (score < bestScore) {
                  bestConstructor = ctor;
                  bestParamTypes = paramTypes;
                  bestScore = score;
                  bestScoreCount = 1;
               } else if (score == bestScore) {
                  ++bestScoreCount;
               }
            }
         }
      }

      if (null == bestConstructor) {
         throw new NoSuchMethodException(errString("function", "constructor", classObj, "", 0, argsIn));
      } else {
         convertParams(argsIn, argsOut, bestParamTypes, exprContext);
         return bestConstructor;
      }
   }

   public static Method getMethod(Class classObj, String name, Object[] argsIn, Object[][] argsOut, ExpressionContext exprContext, int searchMethod) throws NoSuchMethodException, SecurityException, TransformerException {
      if (name.indexOf("-") > 0) {
         name = replaceDash(name);
      }

      Method bestMethod = null;
      Class[] bestParamTypes = null;
      Method[] methods = classObj.getMethods();
      int nMethods = methods.length;
      int bestScore = Integer.MAX_VALUE;
      int bestScoreCount = 0;

      for(int i = 0; i < nMethods; ++i) {
         Method method = methods[i];
         int xsltParamStart = 0;
         if (method.getName().equals(name)) {
            boolean isStatic = Modifier.isStatic(method.getModifiers());
            switch (searchMethod) {
               case 1:
                  if (!isStatic) {
                     continue;
                  }
                  break;
               case 2:
                  if (isStatic) {
                     continue;
                  }
               case 3:
               default:
                  break;
               case 4:
                  if (!isStatic) {
                     xsltParamStart = 1;
                  }
            }

            int javaParamStart = 0;
            Class[] paramTypes = method.getParameterTypes();
            int numberMethodParams = paramTypes.length;
            boolean isFirstExpressionContext = false;
            int argsLen = null != argsIn ? argsIn.length : 0;
            short scoreStart;
            if (numberMethodParams == argsLen - xsltParamStart + 1) {
               Class javaClass = paramTypes[0];
               if (!(class$org$apache$xalan$extensions$ExpressionContext == null ? (class$org$apache$xalan$extensions$ExpressionContext = class$("org.apache.xalan.extensions.ExpressionContext")) : class$org$apache$xalan$extensions$ExpressionContext).isAssignableFrom(javaClass)) {
                  continue;
               }

               isFirstExpressionContext = true;
               scoreStart = 0;
               ++javaParamStart;
            } else {
               scoreStart = 1000;
            }

            if (argsLen - xsltParamStart == numberMethodParams - javaParamStart) {
               int score = scoreMatch(paramTypes, javaParamStart, argsIn, scoreStart);
               if (-1 != score) {
                  if (score < bestScore) {
                     bestMethod = method;
                     bestParamTypes = paramTypes;
                     bestScore = score;
                     bestScoreCount = 1;
                  } else if (score == bestScore) {
                     ++bestScoreCount;
                  }
               }
            }
         }
      }

      if (null == bestMethod) {
         throw new NoSuchMethodException(errString("function", "method", classObj, name, searchMethod, argsIn));
      } else {
         convertParams(argsIn, argsOut, bestParamTypes, exprContext);
         return bestMethod;
      }
   }

   private static String replaceDash(String name) {
      char dash = 45;
      StringBuffer buff = new StringBuffer("");

      for(int i = 0; i < name.length(); ++i) {
         if (name.charAt(i) != dash) {
            if (i > 0 && name.charAt(i - 1) == dash) {
               buff.append(Character.toUpperCase(name.charAt(i)));
            } else {
               buff.append(name.charAt(i));
            }
         }
      }

      return buff.toString();
   }

   public static Method getElementMethod(Class classObj, String name) throws NoSuchMethodException, SecurityException, TransformerException {
      Method bestMethod = null;
      Method[] methods = classObj.getMethods();
      int nMethods = methods.length;
      int bestScoreCount = 0;

      for(int i = 0; i < nMethods; ++i) {
         Method method = methods[i];
         if (method.getName().equals(name)) {
            Class[] paramTypes = method.getParameterTypes();
            if (paramTypes.length == 2 && paramTypes[1].isAssignableFrom(class$org$apache$xalan$templates$ElemExtensionCall == null ? (class$org$apache$xalan$templates$ElemExtensionCall = class$("org.apache.xalan.templates.ElemExtensionCall")) : class$org$apache$xalan$templates$ElemExtensionCall) && paramTypes[0].isAssignableFrom(class$org$apache$xalan$extensions$XSLProcessorContext == null ? (class$org$apache$xalan$extensions$XSLProcessorContext = class$("org.apache.xalan.extensions.XSLProcessorContext")) : class$org$apache$xalan$extensions$XSLProcessorContext)) {
               ++bestScoreCount;
               if (bestScoreCount != 1) {
                  break;
               }

               bestMethod = method;
            }
         }
      }

      if (null == bestMethod) {
         throw new NoSuchMethodException(errString("element", "method", classObj, name, 0, (Object[])null));
      } else if (bestScoreCount > 1) {
         throw new TransformerException(XSLMessages.createMessage("ER_MORE_MATCH_ELEMENT", new Object[]{name}));
      } else {
         return bestMethod;
      }
   }

   public static void convertParams(Object[] argsIn, Object[][] argsOut, Class[] paramTypes, ExpressionContext exprContext) throws TransformerException {
      if (paramTypes == null) {
         argsOut[0] = null;
      } else {
         int nParams = paramTypes.length;
         argsOut[0] = new Object[nParams];
         int paramIndex = 0;
         if (nParams > 0 && (class$org$apache$xalan$extensions$ExpressionContext == null ? (class$org$apache$xalan$extensions$ExpressionContext = class$("org.apache.xalan.extensions.ExpressionContext")) : class$org$apache$xalan$extensions$ExpressionContext).isAssignableFrom(paramTypes[0])) {
            argsOut[0][0] = exprContext;
            ++paramIndex;
         }

         if (argsIn != null) {
            for(int i = argsIn.length - nParams + paramIndex; paramIndex < nParams; ++paramIndex) {
               argsOut[0][paramIndex] = convert(argsIn[i], paramTypes[paramIndex]);
               ++i;
            }
         }
      }

   }

   public static int scoreMatch(Class[] javaParamTypes, int javaParamsStart, Object[] xsltArgs, int score) {
      if (xsltArgs != null && javaParamTypes != null) {
         int nParams = xsltArgs.length;
         int i = nParams - javaParamTypes.length + javaParamsStart;

         for(int javaParamTypesIndex = javaParamsStart; i < nParams; ++javaParamTypesIndex) {
            Object xsltObj = xsltArgs[i];
            int xsltClassType = xsltObj instanceof XObject ? ((XObject)xsltObj).getType() : 0;
            Class javaClass = javaParamTypes[javaParamTypesIndex];
            if (xsltClassType == -1) {
               if (javaClass.isPrimitive()) {
                  return -1;
               }

               score += 10;
            } else {
               ConversionInfo[] convInfo = m_conversions[xsltClassType];
               int nConversions = convInfo.length;

               int k;
               ConversionInfo realClass;
               for(k = 0; k < nConversions; ++k) {
                  realClass = convInfo[k];
                  if (javaClass.isAssignableFrom(realClass.m_class)) {
                     score += realClass.m_score;
                     break;
                  }
               }

               if (k == nConversions) {
                  label72: {
                     if (0 != xsltClassType) {
                        return -1;
                     }

                     realClass = null;
                     Class realClass;
                     if (xsltObj instanceof XObject) {
                        Object realObj = ((XObject)xsltObj).object();
                        if (null == realObj) {
                           score += 10;
                           break label72;
                        }

                        realClass = realObj.getClass();
                     } else {
                        realClass = xsltObj.getClass();
                     }

                     if (!javaClass.isAssignableFrom(realClass)) {
                        return -1;
                     }

                     score += 0;
                  }
               }
            }

            ++i;
         }

         return score;
      } else {
         return score;
      }
   }

   static Object convert(Object xsltObj, Class javaClass) throws TransformerException {
      if (xsltObj instanceof XObject) {
         XObject xobj = (XObject)xsltObj;
         int xsltClassType = xobj.getType();
         DTMIterator iter;
         int rootHandle;
         DTM dtm;
         Node child;
         switch (xsltClassType) {
            case -1:
               return null;
            case 0:
            default:
               xsltObj = xobj.object();
               break;
            case 1:
               if (javaClass == (class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String)) {
                  return xobj.str();
               }

               return new Boolean(xobj.bool());
            case 2:
               if (javaClass == (class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String)) {
                  return xobj.str();
               }

               if (javaClass == Boolean.TYPE) {
                  return new Boolean(xobj.bool());
               }

               return convertDoubleToNumber(xobj.num(), javaClass);
            case 3:
               if (javaClass != (class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String) && javaClass != (class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object)) {
                  if (javaClass == Character.TYPE) {
                     String str = xobj.str();
                     if (str.length() > 0) {
                        return new Character(str.charAt(0));
                     }

                     return null;
                  }

                  if (javaClass == Boolean.TYPE) {
                     return new Boolean(xobj.bool());
                  }

                  return convertDoubleToNumber(xobj.num(), javaClass);
               }

               return xobj.str();
            case 4:
               if (javaClass != (class$org$w3c$dom$traversal$NodeIterator == null ? (class$org$w3c$dom$traversal$NodeIterator = class$("org.w3c.dom.traversal.NodeIterator")) : class$org$w3c$dom$traversal$NodeIterator) && javaClass != (class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object)) {
                  if (javaClass == (class$org$w3c$dom$NodeList == null ? (class$org$w3c$dom$NodeList = class$("org.w3c.dom.NodeList")) : class$org$w3c$dom$NodeList)) {
                     return xobj.nodelist();
                  }

                  if (javaClass == (class$org$w3c$dom$Node == null ? (class$org$w3c$dom$Node = class$("org.w3c.dom.Node")) : class$org$w3c$dom$Node)) {
                     iter = xobj.iter();
                     rootHandle = iter.nextNode();
                     if (rootHandle != -1) {
                        return iter.getDTM(rootHandle).getNode(rootHandle);
                     }

                     return null;
                  }

                  if (javaClass == (class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String)) {
                     return xobj.str();
                  }

                  if (javaClass == Boolean.TYPE) {
                     return new Boolean(xobj.bool());
                  }

                  if (javaClass.isPrimitive()) {
                     return convertDoubleToNumber(xobj.num(), javaClass);
                  }

                  iter = xobj.iter();
                  rootHandle = iter.nextNode();
                  dtm = iter.getDTM(rootHandle);
                  child = dtm.getNode(rootHandle);
                  if (javaClass.isAssignableFrom(child.getClass())) {
                     return child;
                  }

                  return null;
               }

               return xobj.nodeset();
            case 5:
               if (javaClass != (class$org$w3c$dom$traversal$NodeIterator == null ? (class$org$w3c$dom$traversal$NodeIterator = class$("org.w3c.dom.traversal.NodeIterator")) : class$org$w3c$dom$traversal$NodeIterator) && javaClass != (class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object)) {
                  if (javaClass == (class$org$w3c$dom$NodeList == null ? (class$org$w3c$dom$NodeList = class$("org.w3c.dom.NodeList")) : class$org$w3c$dom$NodeList)) {
                     return ((XRTreeFrag)xobj).convertToNodeset();
                  }

                  if (javaClass == (class$org$w3c$dom$Node == null ? (class$org$w3c$dom$Node = class$("org.w3c.dom.Node")) : class$org$w3c$dom$Node)) {
                     iter = ((XRTreeFrag)xobj).asNodeIterator();
                     rootHandle = iter.nextNode();
                     dtm = iter.getDTM(rootHandle);
                     return dtm.getNode(dtm.getFirstChild(rootHandle));
                  }

                  if (javaClass == (class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String)) {
                     return xobj.str();
                  }

                  if (javaClass == Boolean.TYPE) {
                     return new Boolean(xobj.bool());
                  }

                  if (javaClass.isPrimitive()) {
                     return convertDoubleToNumber(xobj.num(), javaClass);
                  }

                  iter = ((XRTreeFrag)xobj).asNodeIterator();
                  rootHandle = iter.nextNode();
                  dtm = iter.getDTM(rootHandle);
                  child = dtm.getNode(dtm.getFirstChild(rootHandle));
                  if (javaClass.isAssignableFrom(child.getClass())) {
                     return child;
                  }

                  return null;
               }

               iter = ((XRTreeFrag)xobj).asNodeIterator();
               return new DTMNodeIterator(iter);
         }
      }

      if (null != xsltObj) {
         if (javaClass == (class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String)) {
            return xsltObj.toString();
         } else if (javaClass.isPrimitive()) {
            XString xstr = new XString(xsltObj.toString());
            double num = xstr.num();
            return convertDoubleToNumber(num, javaClass);
         } else {
            return javaClass == (class$java$lang$Class == null ? (class$java$lang$Class = class$("java.lang.Class")) : class$java$lang$Class) ? xsltObj.getClass() : xsltObj;
         }
      } else {
         return xsltObj;
      }
   }

   static Object convertDoubleToNumber(double num, Class javaClass) {
      if (javaClass != Double.TYPE && javaClass != (class$java$lang$Double == null ? (class$java$lang$Double = class$("java.lang.Double")) : class$java$lang$Double)) {
         if (javaClass == Float.TYPE) {
            return new Float(num);
         } else if (javaClass == Long.TYPE) {
            return new Long((long)num);
         } else if (javaClass == Integer.TYPE) {
            return new Integer((int)num);
         } else if (javaClass == Short.TYPE) {
            return new Short((short)((int)num));
         } else if (javaClass == Character.TYPE) {
            return new Character((char)((int)num));
         } else {
            return javaClass == Byte.TYPE ? new Byte((byte)((int)num)) : new Double(num);
         }
      } else {
         return new Double(num);
      }
   }

   private static String errString(String callType, String searchType, Class classObj, String funcName, int searchMethod, Object[] xsltArgs) {
      String resultString = "For extension " + callType + ", could not find " + searchType + " ";
      switch (searchMethod) {
         case 1:
            return resultString + "static " + classObj.getName() + "." + funcName + "([ExpressionContext,] " + errArgs(xsltArgs, 0) + ").";
         case 2:
            return resultString + classObj.getName() + "." + funcName + "([ExpressionContext,] " + errArgs(xsltArgs, 0) + ").";
         case 3:
            return resultString + classObj.getName() + "." + funcName + "([ExpressionContext,] " + errArgs(xsltArgs, 0) + ").\n" + "Checked both static and instance methods.";
         case 4:
            return resultString + "static " + classObj.getName() + "." + funcName + "([ExpressionContext, ]" + errArgs(xsltArgs, 0) + ") nor\n" + classObj + "." + funcName + "([ExpressionContext,] " + errArgs(xsltArgs, 1) + ").";
         default:
            return callType.equals("function") ? resultString + classObj.getName() + "([ExpressionContext,] " + errArgs(xsltArgs, 0) + ")." : resultString + classObj.getName() + "." + funcName + "(org.apache.xalan.extensions.XSLProcessorContext, " + "org.apache.xalan.templates.ElemExtensionCall).";
      }
   }

   private static String errArgs(Object[] xsltArgs, int startingArg) {
      StringBuffer returnArgs = new StringBuffer();

      for(int i = startingArg; i < xsltArgs.length; ++i) {
         if (i != startingArg) {
            returnArgs.append(", ");
         }

         if (xsltArgs[i] instanceof XObject) {
            returnArgs.append(((XObject)xsltArgs[i]).getTypeString());
         } else {
            returnArgs.append(xsltArgs[i].getClass().getName());
         }
      }

      return returnArgs.toString();
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   static {
      m_javaObjConversions = new ConversionInfo[]{new ConversionInfo(Double.TYPE, 11), new ConversionInfo(Float.TYPE, 12), new ConversionInfo(Long.TYPE, 13), new ConversionInfo(Integer.TYPE, 14), new ConversionInfo(Short.TYPE, 15), new ConversionInfo(Character.TYPE, 16), new ConversionInfo(Byte.TYPE, 17), new ConversionInfo(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String, 18)};
      m_booleanConversions = new ConversionInfo[]{new ConversionInfo(Boolean.TYPE, 0), new ConversionInfo(class$java$lang$Boolean == null ? (class$java$lang$Boolean = class$("java.lang.Boolean")) : class$java$lang$Boolean, 1), new ConversionInfo(class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object, 2), new ConversionInfo(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String, 3)};
      m_numberConversions = new ConversionInfo[]{new ConversionInfo(Double.TYPE, 0), new ConversionInfo(class$java$lang$Double == null ? (class$java$lang$Double = class$("java.lang.Double")) : class$java$lang$Double, 1), new ConversionInfo(Float.TYPE, 3), new ConversionInfo(Long.TYPE, 4), new ConversionInfo(Integer.TYPE, 5), new ConversionInfo(Short.TYPE, 6), new ConversionInfo(Character.TYPE, 7), new ConversionInfo(Byte.TYPE, 8), new ConversionInfo(Boolean.TYPE, 9), new ConversionInfo(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String, 10), new ConversionInfo(class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object, 11)};
      m_stringConversions = new ConversionInfo[]{new ConversionInfo(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String, 0), new ConversionInfo(class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object, 1), new ConversionInfo(Character.TYPE, 2), new ConversionInfo(Double.TYPE, 3), new ConversionInfo(Float.TYPE, 3), new ConversionInfo(Long.TYPE, 3), new ConversionInfo(Integer.TYPE, 3), new ConversionInfo(Short.TYPE, 3), new ConversionInfo(Byte.TYPE, 3), new ConversionInfo(Boolean.TYPE, 4)};
      m_rtfConversions = new ConversionInfo[]{new ConversionInfo(class$org$w3c$dom$traversal$NodeIterator == null ? (class$org$w3c$dom$traversal$NodeIterator = class$("org.w3c.dom.traversal.NodeIterator")) : class$org$w3c$dom$traversal$NodeIterator, 0), new ConversionInfo(class$org$w3c$dom$NodeList == null ? (class$org$w3c$dom$NodeList = class$("org.w3c.dom.NodeList")) : class$org$w3c$dom$NodeList, 1), new ConversionInfo(class$org$w3c$dom$Node == null ? (class$org$w3c$dom$Node = class$("org.w3c.dom.Node")) : class$org$w3c$dom$Node, 2), new ConversionInfo(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String, 3), new ConversionInfo(class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object, 5), new ConversionInfo(Character.TYPE, 6), new ConversionInfo(Double.TYPE, 7), new ConversionInfo(Float.TYPE, 7), new ConversionInfo(Long.TYPE, 7), new ConversionInfo(Integer.TYPE, 7), new ConversionInfo(Short.TYPE, 7), new ConversionInfo(Byte.TYPE, 7), new ConversionInfo(Boolean.TYPE, 8)};
      m_nodesetConversions = new ConversionInfo[]{new ConversionInfo(class$org$w3c$dom$traversal$NodeIterator == null ? (class$org$w3c$dom$traversal$NodeIterator = class$("org.w3c.dom.traversal.NodeIterator")) : class$org$w3c$dom$traversal$NodeIterator, 0), new ConversionInfo(class$org$w3c$dom$NodeList == null ? (class$org$w3c$dom$NodeList = class$("org.w3c.dom.NodeList")) : class$org$w3c$dom$NodeList, 1), new ConversionInfo(class$org$w3c$dom$Node == null ? (class$org$w3c$dom$Node = class$("org.w3c.dom.Node")) : class$org$w3c$dom$Node, 2), new ConversionInfo(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String, 3), new ConversionInfo(class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object, 5), new ConversionInfo(Character.TYPE, 6), new ConversionInfo(Double.TYPE, 7), new ConversionInfo(Float.TYPE, 7), new ConversionInfo(Long.TYPE, 7), new ConversionInfo(Integer.TYPE, 7), new ConversionInfo(Short.TYPE, 7), new ConversionInfo(Byte.TYPE, 7), new ConversionInfo(Boolean.TYPE, 8)};
      m_conversions = new ConversionInfo[][]{m_javaObjConversions, m_booleanConversions, m_numberConversions, m_stringConversions, m_nodesetConversions, m_rtfConversions};
   }

   static class ConversionInfo {
      Class m_class;
      int m_score;

      ConversionInfo(Class cl, int score) {
         this.m_class = cl;
         this.m_score = score;
      }
   }
}
