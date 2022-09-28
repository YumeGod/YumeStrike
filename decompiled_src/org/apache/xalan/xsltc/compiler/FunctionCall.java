package org.apache.xalan.xsltc.compiler;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.IFEQ;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.util.BooleanType;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.IntType;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodType;
import org.apache.xalan.xsltc.compiler.util.MultiHashtable;
import org.apache.xalan.xsltc.compiler.util.ObjectType;
import org.apache.xalan.xsltc.compiler.util.ReferenceType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

class FunctionCall extends Expression {
   private QName _fname;
   private final Vector _arguments;
   private static final Vector EMPTY_ARG_LIST = new Vector(0);
   protected static final String EXT_XSLTC = "http://xml.apache.org/xalan/xsltc";
   protected static final String JAVA_EXT_XSLTC = "http://xml.apache.org/xalan/xsltc/java";
   protected static final String EXT_XALAN = "http://xml.apache.org/xalan";
   protected static final String JAVA_EXT_XALAN = "http://xml.apache.org/xalan/java";
   protected static final String JAVA_EXT_XALAN_OLD = "http://xml.apache.org/xslt/java";
   protected static final String EXSLT_COMMON = "http://exslt.org/common";
   protected static final String EXSLT_MATH = "http://exslt.org/math";
   protected static final String EXSLT_SETS = "http://exslt.org/sets";
   protected static final String EXSLT_DATETIME = "http://exslt.org/dates-and-times";
   protected static final String EXSLT_STRINGS = "http://exslt.org/strings";
   protected static final int NAMESPACE_FORMAT_JAVA = 0;
   protected static final int NAMESPACE_FORMAT_CLASS = 1;
   protected static final int NAMESPACE_FORMAT_PACKAGE = 2;
   protected static final int NAMESPACE_FORMAT_CLASS_OR_PACKAGE = 3;
   private int _namespace_format;
   Expression _thisArgument;
   private String _className;
   private Class _clazz;
   private Method _chosenMethod;
   private Constructor _chosenConstructor;
   private MethodType _chosenMethodType;
   private boolean unresolvedExternal;
   private boolean _isExtConstructor;
   private boolean _isStatic;
   private static final MultiHashtable _internal2Java = new MultiHashtable();
   private static final Hashtable _java2Internal = new Hashtable();
   private static final Hashtable _extensionNamespaceTable = new Hashtable();
   private static final Hashtable _extensionFunctionTable = new Hashtable();
   // $FF: synthetic field
   static Class class$java$lang$Boolean;
   // $FF: synthetic field
   static Class class$java$lang$Object;
   // $FF: synthetic field
   static Class class$java$lang$Double;
   // $FF: synthetic field
   static Class class$java$lang$String;

   public FunctionCall(QName fname, Vector arguments) {
      this._namespace_format = 0;
      this._thisArgument = null;
      this._isExtConstructor = false;
      this._isStatic = false;
      this._fname = fname;
      this._arguments = arguments;
      super._type = null;
   }

   public FunctionCall(QName fname) {
      this(fname, EMPTY_ARG_LIST);
   }

   public String getName() {
      return this._fname.toString();
   }

   public void setParser(Parser parser) {
      super.setParser(parser);
      if (this._arguments != null) {
         int n = this._arguments.size();

         for(int i = 0; i < n; ++i) {
            Expression exp = (Expression)this._arguments.elementAt(i);
            exp.setParser(parser);
            exp.setParent(this);
         }
      }

   }

   public String getClassNameFromUri(String uri) {
      String className = (String)_extensionNamespaceTable.get(uri);
      if (className != null) {
         return className;
      } else {
         int index;
         if (uri.startsWith("http://xml.apache.org/xalan/xsltc/java")) {
            index = "http://xml.apache.org/xalan/xsltc/java".length() + 1;
            return uri.length() > index ? uri.substring(index) : "";
         } else if (uri.startsWith("http://xml.apache.org/xalan/java")) {
            index = "http://xml.apache.org/xalan/java".length() + 1;
            return uri.length() > index ? uri.substring(index) : "";
         } else if (uri.startsWith("http://xml.apache.org/xslt/java")) {
            index = "http://xml.apache.org/xslt/java".length() + 1;
            return uri.length() > index ? uri.substring(index) : "";
         } else {
            index = uri.lastIndexOf(47);
            return index > 0 ? uri.substring(index + 1) : uri;
         }
      }
   }

   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
      if (super._type != null) {
         return super._type;
      } else {
         String namespace = this._fname.getNamespace();
         String local = this._fname.getLocalPart();
         if (this.isExtension()) {
            this._fname = new QName((String)null, (String)null, local);
            return this.typeCheckStandard(stable);
         } else if (this.isStandard()) {
            return this.typeCheckStandard(stable);
         } else {
            try {
               this._className = this.getClassNameFromUri(namespace);
               int pos = local.lastIndexOf(46);
               if (pos > 0) {
                  this._isStatic = true;
                  if (this._className != null && this._className.length() > 0) {
                     this._namespace_format = 2;
                     this._className = this._className + "." + local.substring(0, pos);
                  } else {
                     this._namespace_format = 0;
                     this._className = local.substring(0, pos);
                  }

                  this._fname = new QName(namespace, (String)null, local.substring(pos + 1));
               } else {
                  if (this._className != null && this._className.length() > 0) {
                     try {
                        this._clazz = ObjectFactory.findProviderClass(this._className, ObjectFactory.findClassLoader(), true);
                        this._namespace_format = 1;
                     } catch (ClassNotFoundException var7) {
                        this._namespace_format = 2;
                     }
                  } else {
                     this._namespace_format = 0;
                  }

                  if (local.indexOf(45) > 0) {
                     local = replaceDash(local);
                  }

                  String extFunction = (String)_extensionFunctionTable.get(namespace + ":" + local);
                  if (extFunction != null) {
                     this._fname = new QName((String)null, (String)null, extFunction);
                     return this.typeCheckStandard(stable);
                  }

                  this._fname = new QName(namespace, (String)null, local);
               }

               return this.typeCheckExternal(stable);
            } catch (TypeCheckError var8) {
               ErrorMsg errorMsg = var8.getErrorMsg();
               if (errorMsg == null) {
                  String name = this._fname.getLocalPart();
                  errorMsg = new ErrorMsg("METHOD_NOT_FOUND_ERR", name);
               }

               this.getParser().reportError(3, errorMsg);
               return super._type = Type.Void;
            }
         }
      }
   }

   public Type typeCheckStandard(SymbolTable stable) throws TypeCheckError {
      this._fname.clearNamespace();
      int n = this._arguments.size();
      Vector argsType = this.typeCheckArgs(stable);
      MethodType args = new MethodType(Type.Void, argsType);
      MethodType ptype = this.lookupPrimop(stable, this._fname.getLocalPart(), args);
      if (ptype != null) {
         for(int i = 0; i < n; ++i) {
            Type argType = (Type)ptype.argsType().elementAt(i);
            Expression exp = (Expression)this._arguments.elementAt(i);
            if (!argType.identicalTo(exp.getType())) {
               try {
                  this._arguments.setElementAt(new CastExpr(exp, argType), i);
               } catch (TypeCheckError var10) {
                  throw new TypeCheckError(this);
               }
            }
         }

         this._chosenMethodType = ptype;
         return super._type = ptype.resultType();
      } else {
         throw new TypeCheckError(this);
      }
   }

   public Type typeCheckConstructor(SymbolTable stable) throws TypeCheckError {
      Vector constructors = this.findConstructors();
      if (constructors == null) {
         throw new TypeCheckError("CONSTRUCTOR_NOT_FOUND", this._className);
      } else {
         int nConstructors = constructors.size();
         int nArgs = this._arguments.size();
         Vector argsType = this.typeCheckArgs(stable);
         int bestConstrDistance = Integer.MAX_VALUE;
         super._type = null;

         for(int i = 0; i < nConstructors; ++i) {
            Constructor constructor = (Constructor)constructors.elementAt(i);
            Class[] paramTypes = constructor.getParameterTypes();
            Class extType = null;
            int currConstrDistance = 0;

            int j;
            for(j = 0; j < nArgs; ++j) {
               extType = paramTypes[j];
               Type intType = (Type)argsType.elementAt(j);
               Object match = _internal2Java.maps(intType, extType);
               if (match != null) {
                  currConstrDistance += ((JavaType)match).distance;
               } else {
                  if (!(intType instanceof ObjectType)) {
                     currConstrDistance = Integer.MAX_VALUE;
                     break;
                  }

                  ObjectType objectType = (ObjectType)intType;
                  if (objectType.getJavaClass() != extType) {
                     if (!extType.isAssignableFrom(objectType.getJavaClass())) {
                        currConstrDistance = Integer.MAX_VALUE;
                        break;
                     }

                     ++currConstrDistance;
                  }
               }
            }

            if (j == nArgs && currConstrDistance < bestConstrDistance) {
               this._chosenConstructor = constructor;
               this._isExtConstructor = true;
               bestConstrDistance = currConstrDistance;
               super._type = this._clazz != null ? Type.newObjectType(this._clazz) : Type.newObjectType(this._className);
            }
         }

         if (super._type != null) {
            return super._type;
         } else {
            throw new TypeCheckError("ARGUMENT_CONVERSION_ERR", this.getMethodSignature(argsType));
         }
      }
   }

   public Type typeCheckExternal(SymbolTable stable) throws TypeCheckError {
      int nArgs = this._arguments.size();
      String name = this._fname.getLocalPart();
      if (this._fname.getLocalPart().equals("new")) {
         return this.typeCheckConstructor(stable);
      } else {
         boolean hasThisArgument = false;
         if (nArgs == 0) {
            this._isStatic = true;
         }

         Expression extType;
         if (!this._isStatic) {
            if (this._namespace_format == 0 || this._namespace_format == 2) {
               hasThisArgument = true;
            }

            extType = (Expression)this._arguments.elementAt(0);
            Type firstArgType = extType.typeCheck(stable);
            if (this._namespace_format == 1 && firstArgType instanceof ObjectType && this._clazz != null && this._clazz.isAssignableFrom(((ObjectType)firstArgType).getJavaClass())) {
               hasThisArgument = true;
            }

            if (hasThisArgument) {
               this._thisArgument = (Expression)this._arguments.elementAt(0);
               this._arguments.remove(0);
               --nArgs;
               if (!(firstArgType instanceof ObjectType)) {
                  throw new TypeCheckError("NO_JAVA_FUNCT_THIS_REF", name);
               }

               this._className = ((ObjectType)firstArgType).getJavaClassName();
            }
         } else if (this._className.length() == 0) {
            Parser parser = this.getParser();
            if (parser != null) {
               this.reportWarning(this, parser, "FUNCTION_RESOLVE_ERR", this._fname.toString());
            }

            this.unresolvedExternal = true;
            return super._type = Type.Int;
         }

         Vector methods = this.findMethods();
         if (methods == null) {
            throw new TypeCheckError("METHOD_NOT_FOUND_ERR", this._className + "." + name);
         } else {
            extType = null;
            int nMethods = methods.size();
            Vector argsType = this.typeCheckArgs(stable);
            int bestMethodDistance = Integer.MAX_VALUE;
            super._type = null;

            for(int i = 0; i < nMethods; ++i) {
               Method method = (Method)methods.elementAt(i);
               Class[] paramTypes = method.getParameterTypes();
               int currMethodDistance = 0;

               int j;
               Class extType;
               for(j = 0; j < nArgs; ++j) {
                  extType = paramTypes[j];
                  Type intType = (Type)argsType.elementAt(j);
                  Object match = _internal2Java.maps(intType, extType);
                  if (match != null) {
                     currMethodDistance += ((JavaType)match).distance;
                  } else if (intType instanceof ReferenceType) {
                     ++currMethodDistance;
                  } else {
                     if (!(intType instanceof ObjectType)) {
                        currMethodDistance = Integer.MAX_VALUE;
                        break;
                     }

                     ObjectType object = (ObjectType)intType;
                     if (extType.getName().equals(object.getJavaClassName())) {
                        currMethodDistance += 0;
                     } else {
                        if (!extType.isAssignableFrom(object.getJavaClass())) {
                           currMethodDistance = Integer.MAX_VALUE;
                           break;
                        }

                        ++currMethodDistance;
                     }
                  }
               }

               if (j == nArgs) {
                  extType = method.getReturnType();
                  super._type = (Type)_java2Internal.get(extType);
                  if (super._type == null) {
                     super._type = Type.newObjectType(extType);
                  }

                  if (super._type != null && currMethodDistance < bestMethodDistance) {
                     this._chosenMethod = method;
                     bestMethodDistance = currMethodDistance;
                  }
               }
            }

            if (this._chosenMethod != null && this._thisArgument == null && !Modifier.isStatic(this._chosenMethod.getModifiers())) {
               throw new TypeCheckError("NO_JAVA_FUNCT_THIS_REF", this.getMethodSignature(argsType));
            } else if (super._type != null) {
               if (super._type == Type.NodeSet) {
                  this.getXSLTC().setMultiDocument(true);
               }

               return super._type;
            } else {
               throw new TypeCheckError("ARGUMENT_CONVERSION_ERR", this.getMethodSignature(argsType));
            }
         }
      }
   }

   public Vector typeCheckArgs(SymbolTable stable) throws TypeCheckError {
      Vector result = new Vector();
      Enumeration e = this._arguments.elements();

      while(e.hasMoreElements()) {
         Expression exp = (Expression)e.nextElement();
         result.addElement(exp.typeCheck(stable));
      }

      return result;
   }

   protected final Expression argument(int i) {
      return (Expression)this._arguments.elementAt(i);
   }

   protected final Expression argument() {
      return this.argument(0);
   }

   protected final int argumentCount() {
      return this._arguments.size();
   }

   protected final void setArgument(int i, Expression exp) {
      this._arguments.setElementAt(exp, i);
   }

   public void translateDesynthesized(ClassGenerator classGen, MethodGenerator methodGen) {
      Type type = Type.Boolean;
      if (this._chosenMethodType != null) {
         type = this._chosenMethodType.resultType();
      }

      InstructionList il = methodGen.getInstructionList();
      this.translate(classGen, methodGen);
      if (type instanceof BooleanType || type instanceof IntType) {
         super._falseList.add(il.append((BranchInstruction)(new IFEQ((InstructionHandle)null))));
      }

   }

   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
      int n = this.argumentCount();
      ConstantPoolGen cpg = classGen.getConstantPool();
      InstructionList il = methodGen.getInstructionList();
      boolean isSecureProcessing = classGen.getParser().getXSLTC().isSecureProcessing();
      int index;
      if (!this.isStandard() && !this.isExtension()) {
         if (this.unresolvedExternal) {
            index = cpg.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "unresolved_externalF", "(Ljava/lang/String;)V");
            il.append((CompoundInstruction)(new PUSH(cpg, this._fname.toString())));
            il.append((org.apache.bcel.generic.Instruction)(new INVOKESTATIC(index)));
         } else {
            String clazz;
            Class[] paramTypes;
            int i;
            if (this._isExtConstructor) {
               if (isSecureProcessing) {
                  this.translateUnallowedExtension(cpg, il);
               }

               clazz = this._chosenConstructor.getDeclaringClass().getName();
               paramTypes = this._chosenConstructor.getParameterTypes();
               LocalVariableGen[] paramTemp = new LocalVariableGen[n];

               for(int i = 0; i < n; ++i) {
                  Expression exp = this.argument(i);
                  Type expType = exp.getType();
                  exp.translate(classGen, methodGen);
                  exp.startIterator(classGen, methodGen);
                  expType.translateTo(classGen, methodGen, paramTypes[i]);
                  paramTemp[i] = methodGen.addLocalVariable("function_call_tmp" + i, expType.toJCType(), il.getEnd(), (InstructionHandle)null);
                  il.append(expType.STORE(paramTemp[i].getIndex()));
               }

               il.append((org.apache.bcel.generic.Instruction)(new NEW(cpg.addClass(this._className))));
               il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);

               for(i = 0; i < n; ++i) {
                  Expression arg = this.argument(i);
                  il.append(arg.getType().LOAD(paramTemp[i].getIndex()));
               }

               StringBuffer buffer = new StringBuffer();
               buffer.append('(');

               for(int i = 0; i < paramTypes.length; ++i) {
                  buffer.append(getSignature(paramTypes[i]));
               }

               buffer.append(')');
               buffer.append("V");
               index = cpg.addMethodref(clazz, "<init>", buffer.toString());
               il.append((org.apache.bcel.generic.Instruction)(new INVOKESPECIAL(index)));
               Type.Object.translateFrom(classGen, methodGen, this._chosenConstructor.getDeclaringClass());
            } else {
               if (isSecureProcessing) {
                  this.translateUnallowedExtension(cpg, il);
               }

               clazz = this._chosenMethod.getDeclaringClass().getName();
               paramTypes = this._chosenMethod.getParameterTypes();
               if (this._thisArgument != null) {
                  this._thisArgument.translate(classGen, methodGen);
               }

               for(int i = 0; i < n; ++i) {
                  Expression exp = this.argument(i);
                  exp.translate(classGen, methodGen);
                  exp.startIterator(classGen, methodGen);
                  exp.getType().translateTo(classGen, methodGen, paramTypes[i]);
               }

               StringBuffer buffer = new StringBuffer();
               buffer.append('(');

               for(i = 0; i < paramTypes.length; ++i) {
                  buffer.append(getSignature(paramTypes[i]));
               }

               buffer.append(')');
               buffer.append(getSignature(this._chosenMethod.getReturnType()));
               if (this._thisArgument != null && this._clazz.isInterface()) {
                  index = cpg.addInterfaceMethodref(clazz, this._fname.getLocalPart(), buffer.toString());
                  il.append((org.apache.bcel.generic.Instruction)(new INVOKEINTERFACE(index, n + 1)));
               } else {
                  index = cpg.addMethodref(clazz, this._fname.getLocalPart(), buffer.toString());
                  il.append((org.apache.bcel.generic.Instruction)(this._thisArgument != null ? new INVOKEVIRTUAL(index) : new INVOKESTATIC(index)));
               }

               super._type.translateFrom(classGen, methodGen, this._chosenMethod.getReturnType());
            }
         }
      } else {
         for(int i = 0; i < n; ++i) {
            Expression exp = this.argument(i);
            exp.translate(classGen, methodGen);
            exp.startIterator(classGen, methodGen);
         }

         String name = this._fname.toString().replace('-', '_') + "F";
         String args = "";
         if (name.equals("sumF")) {
            args = "Lorg/apache/xalan/xsltc/DOM;";
            il.append(methodGen.loadDOM());
         } else if (name.equals("normalize_spaceF") && this._chosenMethodType.toSignature(args).equals("()Ljava/lang/String;")) {
            args = "ILorg/apache/xalan/xsltc/DOM;";
            il.append(methodGen.loadContextNode());
            il.append(methodGen.loadDOM());
         }

         index = cpg.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", name, this._chosenMethodType.toSignature(args));
         il.append((org.apache.bcel.generic.Instruction)(new INVOKESTATIC(index)));
      }

   }

   public String toString() {
      return "funcall(" + this._fname + ", " + this._arguments + ')';
   }

   public boolean isStandard() {
      String namespace = this._fname.getNamespace();
      return namespace == null || namespace.equals("");
   }

   public boolean isExtension() {
      String namespace = this._fname.getNamespace();
      return namespace != null && namespace.equals("http://xml.apache.org/xalan/xsltc");
   }

   private Vector findMethods() {
      Vector result = null;
      String namespace = this._fname.getNamespace();
      if (this._className != null && this._className.length() > 0) {
         int nArgs = this._arguments.size();

         try {
            if (this._clazz == null) {
               this._clazz = ObjectFactory.findProviderClass(this._className, ObjectFactory.findClassLoader(), true);
               if (this._clazz == null) {
                  ErrorMsg msg = new ErrorMsg("CLASS_NOT_FOUND_ERR", this._className);
                  this.getParser().reportError(3, msg);
               }
            }

            String methodName = this._fname.getLocalPart();
            Method[] methods = this._clazz.getMethods();

            for(int i = 0; i < methods.length; ++i) {
               int mods = methods[i].getModifiers();
               if (Modifier.isPublic(mods) && methods[i].getName().equals(methodName) && methods[i].getParameterTypes().length == nArgs) {
                  if (result == null) {
                     result = new Vector();
                  }

                  result.addElement(methods[i]);
               }
            }
         } catch (ClassNotFoundException var8) {
            ErrorMsg msg = new ErrorMsg("CLASS_NOT_FOUND_ERR", this._className);
            this.getParser().reportError(3, msg);
         }
      }

      return result;
   }

   private Vector findConstructors() {
      Vector result = null;
      String namespace = this._fname.getNamespace();
      int nArgs = this._arguments.size();

      try {
         if (this._clazz == null) {
            this._clazz = ObjectFactory.findProviderClass(this._className, ObjectFactory.findClassLoader(), true);
            if (this._clazz == null) {
               ErrorMsg msg = new ErrorMsg("CLASS_NOT_FOUND_ERR", this._className);
               this.getParser().reportError(3, msg);
            }
         }

         Constructor[] constructors = this._clazz.getConstructors();

         for(int i = 0; i < constructors.length; ++i) {
            int mods = constructors[i].getModifiers();
            if (Modifier.isPublic(mods) && constructors[i].getParameterTypes().length == nArgs) {
               if (result == null) {
                  result = new Vector();
               }

               result.addElement(constructors[i]);
            }
         }
      } catch (ClassNotFoundException var7) {
         ErrorMsg msg = new ErrorMsg("CLASS_NOT_FOUND_ERR", this._className);
         this.getParser().reportError(3, msg);
      }

      return result;
   }

   static final String getSignature(Class clazz) {
      if (!clazz.isArray()) {
         if (clazz.isPrimitive()) {
            if (clazz == Integer.TYPE) {
               return "I";
            } else if (clazz == Byte.TYPE) {
               return "B";
            } else if (clazz == Long.TYPE) {
               return "J";
            } else if (clazz == Float.TYPE) {
               return "F";
            } else if (clazz == Double.TYPE) {
               return "D";
            } else if (clazz == Short.TYPE) {
               return "S";
            } else if (clazz == Character.TYPE) {
               return "C";
            } else if (clazz == Boolean.TYPE) {
               return "Z";
            } else if (clazz == Void.TYPE) {
               return "V";
            } else {
               String name = clazz.toString();
               ErrorMsg err = new ErrorMsg("UNKNOWN_SIG_TYPE_ERR", name);
               throw new Error(err.toString());
            }
         } else {
            return "L" + clazz.getName().replace('.', '/') + ';';
         }
      } else {
         StringBuffer sb = new StringBuffer();

         Class cl;
         for(cl = clazz; cl.isArray(); cl = cl.getComponentType()) {
            sb.append("[");
         }

         sb.append(getSignature(cl));
         return sb.toString();
      }
   }

   static final String getSignature(Method meth) {
      StringBuffer sb = new StringBuffer();
      sb.append('(');
      Class[] params = meth.getParameterTypes();

      for(int j = 0; j < params.length; ++j) {
         sb.append(getSignature(params[j]));
      }

      return sb.append(')').append(getSignature(meth.getReturnType())).toString();
   }

   static final String getSignature(Constructor cons) {
      StringBuffer sb = new StringBuffer();
      sb.append('(');
      Class[] params = cons.getParameterTypes();

      for(int j = 0; j < params.length; ++j) {
         sb.append(getSignature(params[j]));
      }

      return sb.append(")V").toString();
   }

   private String getMethodSignature(Vector argsType) {
      StringBuffer buf = new StringBuffer(this._className);
      buf.append('.').append(this._fname.getLocalPart()).append('(');
      int nArgs = argsType.size();

      for(int i = 0; i < nArgs; ++i) {
         Type intType = (Type)argsType.elementAt(i);
         buf.append(intType.toString());
         if (i < nArgs - 1) {
            buf.append(", ");
         }
      }

      buf.append(')');
      return buf.toString();
   }

   protected static String replaceDash(String name) {
      char dash = 45;
      StringBuffer buff = new StringBuffer("");

      for(int i = 0; i < name.length(); ++i) {
         if (i > 0 && name.charAt(i - 1) == dash) {
            buff.append(Character.toUpperCase(name.charAt(i)));
         } else if (name.charAt(i) != dash) {
            buff.append(name.charAt(i));
         }
      }

      return buff.toString();
   }

   private void translateUnallowedExtension(ConstantPoolGen cpg, InstructionList il) {
      int index = cpg.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "unallowed_extension_functionF", "(Ljava/lang/String;)V");
      il.append((CompoundInstruction)(new PUSH(cpg, this._fname.toString())));
      il.append((org.apache.bcel.generic.Instruction)(new INVOKESTATIC(index)));
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
      try {
         Class nodeClass = Class.forName("org.w3c.dom.Node");
         Class nodeListClass = Class.forName("org.w3c.dom.NodeList");
         _internal2Java.put(Type.Boolean, new JavaType(Boolean.TYPE, 0));
         _internal2Java.put(Type.Boolean, new JavaType(class$java$lang$Boolean == null ? (class$java$lang$Boolean = class$("java.lang.Boolean")) : class$java$lang$Boolean, 1));
         _internal2Java.put(Type.Boolean, new JavaType(class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object, 2));
         _internal2Java.put(Type.Real, new JavaType(Double.TYPE, 0));
         _internal2Java.put(Type.Real, new JavaType(class$java$lang$Double == null ? (class$java$lang$Double = class$("java.lang.Double")) : class$java$lang$Double, 1));
         _internal2Java.put(Type.Real, new JavaType(Float.TYPE, 2));
         _internal2Java.put(Type.Real, new JavaType(Long.TYPE, 3));
         _internal2Java.put(Type.Real, new JavaType(Integer.TYPE, 4));
         _internal2Java.put(Type.Real, new JavaType(Short.TYPE, 5));
         _internal2Java.put(Type.Real, new JavaType(Byte.TYPE, 6));
         _internal2Java.put(Type.Real, new JavaType(Character.TYPE, 7));
         _internal2Java.put(Type.Real, new JavaType(class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object, 8));
         _internal2Java.put(Type.Int, new JavaType(Double.TYPE, 0));
         _internal2Java.put(Type.Int, new JavaType(class$java$lang$Double == null ? (class$java$lang$Double = class$("java.lang.Double")) : class$java$lang$Double, 1));
         _internal2Java.put(Type.Int, new JavaType(Float.TYPE, 2));
         _internal2Java.put(Type.Int, new JavaType(Long.TYPE, 3));
         _internal2Java.put(Type.Int, new JavaType(Integer.TYPE, 4));
         _internal2Java.put(Type.Int, new JavaType(Short.TYPE, 5));
         _internal2Java.put(Type.Int, new JavaType(Byte.TYPE, 6));
         _internal2Java.put(Type.Int, new JavaType(Character.TYPE, 7));
         _internal2Java.put(Type.Int, new JavaType(class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object, 8));
         _internal2Java.put(Type.String, new JavaType(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String, 0));
         _internal2Java.put(Type.String, new JavaType(class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object, 1));
         _internal2Java.put(Type.NodeSet, new JavaType(nodeListClass, 0));
         _internal2Java.put(Type.NodeSet, new JavaType(nodeClass, 1));
         _internal2Java.put(Type.NodeSet, new JavaType(class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object, 2));
         _internal2Java.put(Type.NodeSet, new JavaType(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String, 3));
         _internal2Java.put(Type.Node, new JavaType(nodeListClass, 0));
         _internal2Java.put(Type.Node, new JavaType(nodeClass, 1));
         _internal2Java.put(Type.Node, new JavaType(class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object, 2));
         _internal2Java.put(Type.Node, new JavaType(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String, 3));
         _internal2Java.put(Type.ResultTree, new JavaType(nodeListClass, 0));
         _internal2Java.put(Type.ResultTree, new JavaType(nodeClass, 1));
         _internal2Java.put(Type.ResultTree, new JavaType(class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object, 2));
         _internal2Java.put(Type.ResultTree, new JavaType(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String, 3));
         _internal2Java.put(Type.Reference, new JavaType(class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object, 0));
         _java2Internal.put(Boolean.TYPE, Type.Boolean);
         _java2Internal.put(Void.TYPE, Type.Void);
         _java2Internal.put(Character.TYPE, Type.Real);
         _java2Internal.put(Byte.TYPE, Type.Real);
         _java2Internal.put(Short.TYPE, Type.Real);
         _java2Internal.put(Integer.TYPE, Type.Real);
         _java2Internal.put(Long.TYPE, Type.Real);
         _java2Internal.put(Float.TYPE, Type.Real);
         _java2Internal.put(Double.TYPE, Type.Real);
         _java2Internal.put(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String, Type.String);
         _java2Internal.put(class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object, Type.Reference);
         _java2Internal.put(nodeListClass, Type.NodeSet);
         _java2Internal.put(nodeClass, Type.NodeSet);
         _extensionNamespaceTable.put("http://xml.apache.org/xalan", "org.apache.xalan.lib.Extensions");
         _extensionNamespaceTable.put("http://exslt.org/common", "org.apache.xalan.lib.ExsltCommon");
         _extensionNamespaceTable.put("http://exslt.org/math", "org.apache.xalan.lib.ExsltMath");
         _extensionNamespaceTable.put("http://exslt.org/sets", "org.apache.xalan.lib.ExsltSets");
         _extensionNamespaceTable.put("http://exslt.org/dates-and-times", "org.apache.xalan.lib.ExsltDatetime");
         _extensionNamespaceTable.put("http://exslt.org/strings", "org.apache.xalan.lib.ExsltStrings");
         _extensionFunctionTable.put("http://exslt.org/common:nodeSet", "nodeset");
         _extensionFunctionTable.put("http://exslt.org/common:objectType", "objectType");
         _extensionFunctionTable.put("http://xml.apache.org/xalan:nodeset", "nodeset");
      } catch (ClassNotFoundException var2) {
         System.err.println(var2);
      }

   }

   static class JavaType {
      public Class type;
      public int distance;

      public JavaType(Class type, int distance) {
         this.type = type;
         this.distance = distance;
      }

      public boolean equals(Object query) {
         return query.equals(this.type);
      }
   }
}
