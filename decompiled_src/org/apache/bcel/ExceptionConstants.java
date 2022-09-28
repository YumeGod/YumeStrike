package org.apache.bcel;

public interface ExceptionConstants {
   Class THROWABLE = null.class$java$lang$Throwable == null ? (null.class$java$lang$Throwable = null.class$("java.lang.Throwable")) : null.class$java$lang$Throwable;
   Class RUNTIME_EXCEPTION = null.class$java$lang$RuntimeException == null ? (null.class$java$lang$RuntimeException = null.class$("java.lang.RuntimeException")) : null.class$java$lang$RuntimeException;
   Class LINKING_EXCEPTION = null.class$java$lang$LinkageError == null ? (null.class$java$lang$LinkageError = null.class$("java.lang.LinkageError")) : null.class$java$lang$LinkageError;
   Class CLASS_CIRCULARITY_ERROR = null.class$java$lang$ClassCircularityError == null ? (null.class$java$lang$ClassCircularityError = null.class$("java.lang.ClassCircularityError")) : null.class$java$lang$ClassCircularityError;
   Class CLASS_FORMAT_ERROR = null.class$java$lang$ClassFormatError == null ? (null.class$java$lang$ClassFormatError = null.class$("java.lang.ClassFormatError")) : null.class$java$lang$ClassFormatError;
   Class EXCEPTION_IN_INITIALIZER_ERROR = null.class$java$lang$ExceptionInInitializerError == null ? (null.class$java$lang$ExceptionInInitializerError = null.class$("java.lang.ExceptionInInitializerError")) : null.class$java$lang$ExceptionInInitializerError;
   Class INCOMPATIBLE_CLASS_CHANGE_ERROR = null.class$java$lang$IncompatibleClassChangeError == null ? (null.class$java$lang$IncompatibleClassChangeError = null.class$("java.lang.IncompatibleClassChangeError")) : null.class$java$lang$IncompatibleClassChangeError;
   Class ABSTRACT_METHOD_ERROR = null.class$java$lang$AbstractMethodError == null ? (null.class$java$lang$AbstractMethodError = null.class$("java.lang.AbstractMethodError")) : null.class$java$lang$AbstractMethodError;
   Class ILLEGAL_ACCESS_ERROR = null.class$java$lang$IllegalAccessError == null ? (null.class$java$lang$IllegalAccessError = null.class$("java.lang.IllegalAccessError")) : null.class$java$lang$IllegalAccessError;
   Class INSTANTIATION_ERROR = null.class$java$lang$InstantiationError == null ? (null.class$java$lang$InstantiationError = null.class$("java.lang.InstantiationError")) : null.class$java$lang$InstantiationError;
   Class NO_SUCH_FIELD_ERROR = null.class$java$lang$NoSuchFieldError == null ? (null.class$java$lang$NoSuchFieldError = null.class$("java.lang.NoSuchFieldError")) : null.class$java$lang$NoSuchFieldError;
   Class NO_SUCH_METHOD_ERROR = null.class$java$lang$NoSuchMethodError == null ? (null.class$java$lang$NoSuchMethodError = null.class$("java.lang.NoSuchMethodError")) : null.class$java$lang$NoSuchMethodError;
   Class NO_CLASS_DEF_FOUND_ERROR = null.class$java$lang$NoClassDefFoundError == null ? (null.class$java$lang$NoClassDefFoundError = null.class$("java.lang.NoClassDefFoundError")) : null.class$java$lang$NoClassDefFoundError;
   Class UNSATISFIED_LINK_ERROR = null.class$java$lang$UnsatisfiedLinkError == null ? (null.class$java$lang$UnsatisfiedLinkError = null.class$("java.lang.UnsatisfiedLinkError")) : null.class$java$lang$UnsatisfiedLinkError;
   Class VERIFY_ERROR = null.class$java$lang$VerifyError == null ? (null.class$java$lang$VerifyError = null.class$("java.lang.VerifyError")) : null.class$java$lang$VerifyError;
   Class NULL_POINTER_EXCEPTION = null.class$java$lang$NullPointerException == null ? (null.class$java$lang$NullPointerException = null.class$("java.lang.NullPointerException")) : null.class$java$lang$NullPointerException;
   Class ARRAY_INDEX_OUT_OF_BOUNDS_EXCEPTION = null.class$java$lang$ArrayIndexOutOfBoundsException == null ? (null.class$java$lang$ArrayIndexOutOfBoundsException = null.class$("java.lang.ArrayIndexOutOfBoundsException")) : null.class$java$lang$ArrayIndexOutOfBoundsException;
   Class ARITHMETIC_EXCEPTION = null.class$java$lang$ArithmeticException == null ? (null.class$java$lang$ArithmeticException = null.class$("java.lang.ArithmeticException")) : null.class$java$lang$ArithmeticException;
   Class NEGATIVE_ARRAY_SIZE_EXCEPTION = null.class$java$lang$NegativeArraySizeException == null ? (null.class$java$lang$NegativeArraySizeException = null.class$("java.lang.NegativeArraySizeException")) : null.class$java$lang$NegativeArraySizeException;
   Class CLASS_CAST_EXCEPTION = null.class$java$lang$ClassCastException == null ? (null.class$java$lang$ClassCastException = null.class$("java.lang.ClassCastException")) : null.class$java$lang$ClassCastException;
   Class ILLEGAL_MONITOR_STATE = null.class$java$lang$IllegalMonitorStateException == null ? (null.class$java$lang$IllegalMonitorStateException = null.class$("java.lang.IllegalMonitorStateException")) : null.class$java$lang$IllegalMonitorStateException;
   Class[] EXCS_CLASS_AND_INTERFACE_RESOLUTION = new Class[]{NO_CLASS_DEF_FOUND_ERROR, CLASS_FORMAT_ERROR, VERIFY_ERROR, ABSTRACT_METHOD_ERROR, EXCEPTION_IN_INITIALIZER_ERROR, ILLEGAL_ACCESS_ERROR};
   Class[] EXCS_FIELD_AND_METHOD_RESOLUTION = new Class[]{NO_SUCH_FIELD_ERROR, ILLEGAL_ACCESS_ERROR, NO_SUCH_METHOD_ERROR};
   Class[] EXCS_INTERFACE_METHOD_RESOLUTION = new Class[0];
   Class[] EXCS_STRING_RESOLUTION = new Class[0];
   Class[] EXCS_ARRAY_EXCEPTION = new Class[]{NULL_POINTER_EXCEPTION, ARRAY_INDEX_OUT_OF_BOUNDS_EXCEPTION};
}
