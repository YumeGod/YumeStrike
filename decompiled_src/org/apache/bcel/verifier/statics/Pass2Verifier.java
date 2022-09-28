package org.apache.bcel.verifier.statics;

import java.util.HashMap;
import java.util.HashSet;
import org.apache.bcel.Constants;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.CodeException;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantClass;
import org.apache.bcel.classfile.ConstantDouble;
import org.apache.bcel.classfile.ConstantFieldref;
import org.apache.bcel.classfile.ConstantFloat;
import org.apache.bcel.classfile.ConstantInteger;
import org.apache.bcel.classfile.ConstantInterfaceMethodref;
import org.apache.bcel.classfile.ConstantLong;
import org.apache.bcel.classfile.ConstantMethodref;
import org.apache.bcel.classfile.ConstantNameAndType;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.ConstantString;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.classfile.ConstantValue;
import org.apache.bcel.classfile.Deprecated;
import org.apache.bcel.classfile.DescendingVisitor;
import org.apache.bcel.classfile.EmptyVisitor;
import org.apache.bcel.classfile.ExceptionTable;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.InnerClass;
import org.apache.bcel.classfile.InnerClasses;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.LineNumber;
import org.apache.bcel.classfile.LineNumberTable;
import org.apache.bcel.classfile.LocalVariable;
import org.apache.bcel.classfile.LocalVariableTable;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.classfile.Node;
import org.apache.bcel.classfile.SourceFile;
import org.apache.bcel.classfile.Synthetic;
import org.apache.bcel.classfile.Unknown;
import org.apache.bcel.classfile.Visitor;
import org.apache.bcel.generic.ArrayType;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.Type;
import org.apache.bcel.verifier.PassVerifier;
import org.apache.bcel.verifier.VerificationResult;
import org.apache.bcel.verifier.Verifier;
import org.apache.bcel.verifier.VerifierFactory;
import org.apache.bcel.verifier.exc.AssertionViolatedException;
import org.apache.bcel.verifier.exc.ClassConstraintException;
import org.apache.bcel.verifier.exc.LocalVariableInfoInconsistentException;

public final class Pass2Verifier extends PassVerifier implements Constants {
   private LocalVariablesInfo[] localVariablesInfos;
   private Verifier myOwner;

   public Pass2Verifier(Verifier owner) {
      this.myOwner = owner;
   }

   public LocalVariablesInfo getLocalVariablesInfo(int method_nr) {
      if (this.verify() != VerificationResult.VR_OK) {
         return null;
      } else if (method_nr >= 0 && method_nr < this.localVariablesInfos.length) {
         return this.localVariablesInfos[method_nr];
      } else {
         throw new AssertionViolatedException("Method number out of range.");
      }
   }

   public VerificationResult do_verify() {
      VerificationResult vr1 = this.myOwner.doPass1();
      if (vr1.equals(VerificationResult.VR_OK)) {
         this.localVariablesInfos = new LocalVariablesInfo[Repository.lookupClass(this.myOwner.getClassName()).getMethods().length];
         VerificationResult vr = VerificationResult.VR_OK;

         try {
            this.constant_pool_entries_satisfy_static_constraints();
            this.field_and_method_refs_are_valid();
            this.every_class_has_an_accessible_superclass();
            this.final_methods_are_not_overridden();
         } catch (ClassConstraintException var4) {
            vr = new VerificationResult(2, var4.getMessage());
         }

         return vr;
      } else {
         return VerificationResult.VR_NOTYET;
      }
   }

   private void every_class_has_an_accessible_superclass() {
      HashSet hs = new HashSet();
      JavaClass jc = Repository.lookupClass(this.myOwner.getClassName());
      int supidx = -1;

      while(supidx != 0) {
         supidx = jc.getSuperclassNameIndex();
         if (supidx == 0) {
            if (jc != Repository.lookupClass(Type.OBJECT.getClassName())) {
               throw new ClassConstraintException("Superclass of '" + jc.getClassName() + "' missing but not " + Type.OBJECT.getClassName() + " itself!");
            }
         } else {
            String supername = jc.getSuperclassName();
            if (!hs.add(supername)) {
               throw new ClassConstraintException("Circular superclass hierarchy detected.");
            }

            Verifier v = VerifierFactory.getVerifier(supername);
            VerificationResult vr = v.doPass1();
            if (vr != VerificationResult.VR_OK) {
               throw new ClassConstraintException("Could not load in ancestor class '" + supername + "'.");
            }

            jc = Repository.lookupClass(supername);
            if (jc.isFinal()) {
               throw new ClassConstraintException("Ancestor class '" + supername + "' has the FINAL access modifier and must therefore not be subclassed.");
            }
         }
      }

   }

   private void final_methods_are_not_overridden() {
      HashMap hashmap = new HashMap();
      JavaClass jc = Repository.lookupClass(this.myOwner.getClassName());

      for(int supidx = -1; supidx != 0; jc = Repository.lookupClass(jc.getSuperclassName())) {
         supidx = jc.getSuperclassNameIndex();
         new ConstantPoolGen(jc.getConstantPool());
         Method[] methods = jc.getMethods();

         for(int i = 0; i < methods.length; ++i) {
            String name_and_sig = methods[i].getName() + methods[i].getSignature();
            if (hashmap.containsKey(name_and_sig)) {
               if (methods[i].isFinal()) {
                  throw new ClassConstraintException("Method '" + name_and_sig + "' in class '" + hashmap.get(name_and_sig) + "' overrides the final (not-overridable) definition in class '" + jc.getClassName() + "'.");
               }

               if (!methods[i].isStatic()) {
                  hashmap.put(name_and_sig, jc.getClassName());
               }
            } else if (!methods[i].isStatic()) {
               hashmap.put(name_and_sig, jc.getClassName());
            }
         }
      }

   }

   private void constant_pool_entries_satisfy_static_constraints() {
      JavaClass jc = Repository.lookupClass(this.myOwner.getClassName());
      new CPESSC_Visitor(jc);
   }

   private void field_and_method_refs_are_valid() {
      JavaClass jc = Repository.lookupClass(this.myOwner.getClassName());
      DescendingVisitor v = new DescendingVisitor(jc, new FAMRAV_Visitor(jc));
      v.visit();
   }

   private static final boolean validClassName(String name) {
      return true;
   }

   private static boolean validMethodName(String name, boolean allowStaticInit) {
      if (validJavaLangMethodName(name)) {
         return true;
      } else if (!allowStaticInit) {
         return name.equals("<init>");
      } else {
         return name.equals("<init>") || name.equals("<clinit>");
      }
   }

   private static boolean validClassMethodName(String name) {
      return validMethodName(name, false);
   }

   private static boolean validJavaLangMethodName(String name) {
      if (!Character.isJavaIdentifierStart(name.charAt(0))) {
         return false;
      } else {
         for(int i = 1; i < name.length(); ++i) {
            if (!Character.isJavaIdentifierPart(name.charAt(i))) {
               return false;
            }
         }

         return true;
      }
   }

   private static boolean validInterfaceMethodName(String name) {
      return name.startsWith("<") ? false : validJavaLangMethodName(name);
   }

   private static boolean validJavaIdentifier(String name) {
      if (!Character.isJavaIdentifierStart(name.charAt(0))) {
         return false;
      } else {
         for(int i = 1; i < name.length(); ++i) {
            if (!Character.isJavaIdentifierPart(name.charAt(i))) {
               return false;
            }
         }

         return true;
      }
   }

   private static boolean validFieldName(String name) {
      return validJavaIdentifier(name);
   }

   private static String tostring(Node n) {
      return (new StringRepresentation(n)).toString();
   }

   private class InnerClassDetector extends EmptyVisitor {
      private boolean hasInnerClass = false;
      private JavaClass jc;
      private ConstantPool cp;

      private InnerClassDetector() {
      }

      public InnerClassDetector(JavaClass _jc) {
         this.jc = _jc;
         this.cp = this.jc.getConstantPool();
         (new DescendingVisitor(this.jc, this)).visit();
      }

      public boolean innerClassReferenced() {
         return this.hasInnerClass;
      }

      public void visitConstantClass(ConstantClass obj) {
         Constant c = this.cp.getConstant(obj.getNameIndex());
         if (c instanceof ConstantUtf8) {
            String classname = ((ConstantUtf8)c).getBytes();
            if (classname.startsWith(this.jc.getClassName().replace('.', '/') + "$")) {
               this.hasInnerClass = true;
            }
         }

      }
   }

   private class FAMRAV_Visitor extends EmptyVisitor implements Visitor {
      private final JavaClass jc;
      private final ConstantPool cp;

      private FAMRAV_Visitor(JavaClass _jc) {
         this.jc = _jc;
         this.cp = _jc.getConstantPool();
      }

      public void visitConstantFieldref(ConstantFieldref obj) {
         if (obj.getTag() != 9) {
            throw new ClassConstraintException("ConstantFieldref '" + Pass2Verifier.tostring(obj) + "' has wrong tag!");
         } else {
            int name_and_type_index = obj.getNameAndTypeIndex();
            ConstantNameAndType cnat = (ConstantNameAndType)this.cp.getConstant(name_and_type_index);
            String name = ((ConstantUtf8)this.cp.getConstant(cnat.getNameIndex())).getBytes();
            if (!Pass2Verifier.validFieldName(name)) {
               throw new ClassConstraintException("Invalid field name '" + name + "' referenced by '" + Pass2Verifier.tostring(obj) + "'.");
            } else {
               int class_index = obj.getClassIndex();
               ConstantClass cc = (ConstantClass)this.cp.getConstant(class_index);
               String className = ((ConstantUtf8)this.cp.getConstant(cc.getNameIndex())).getBytes();
               if (!Pass2Verifier.validClassName(className)) {
                  throw new ClassConstraintException("Illegal class name '" + className + "' used by '" + Pass2Verifier.tostring(obj) + "'.");
               } else {
                  String sig = ((ConstantUtf8)this.cp.getConstant(cnat.getSignatureIndex())).getBytes();

                  try {
                     Type var9 = Type.getType(sig);
                  } catch (ClassFormatError var10) {
                     throw new ClassConstraintException("Illegal descriptor (==signature) '" + sig + "' used by '" + Pass2Verifier.tostring(obj) + "'.");
                  }
               }
            }
         }
      }

      public void visitConstantMethodref(ConstantMethodref obj) {
         if (obj.getTag() != 10) {
            throw new ClassConstraintException("ConstantMethodref '" + Pass2Verifier.tostring(obj) + "' has wrong tag!");
         } else {
            int name_and_type_index = obj.getNameAndTypeIndex();
            ConstantNameAndType cnat = (ConstantNameAndType)this.cp.getConstant(name_and_type_index);
            String name = ((ConstantUtf8)this.cp.getConstant(cnat.getNameIndex())).getBytes();
            if (!Pass2Verifier.validClassMethodName(name)) {
               throw new ClassConstraintException("Invalid (non-interface) method name '" + name + "' referenced by '" + Pass2Verifier.tostring(obj) + "'.");
            } else {
               int class_index = obj.getClassIndex();
               ConstantClass cc = (ConstantClass)this.cp.getConstant(class_index);
               String className = ((ConstantUtf8)this.cp.getConstant(cc.getNameIndex())).getBytes();
               if (!Pass2Verifier.validClassName(className)) {
                  throw new ClassConstraintException("Illegal class name '" + className + "' used by '" + Pass2Verifier.tostring(obj) + "'.");
               } else {
                  String sig = ((ConstantUtf8)this.cp.getConstant(cnat.getSignatureIndex())).getBytes();

                  try {
                     Type t = Type.getReturnType(sig);
                     Type[] ts = Type.getArgumentTypes(sig);
                     if (name.equals("<init>") && t != Type.VOID) {
                        throw new ClassConstraintException("Instance initialization method must have VOID return type.");
                     }
                  } catch (ClassFormatError var11) {
                     throw new ClassConstraintException("Illegal descriptor (==signature) '" + sig + "' used by '" + Pass2Verifier.tostring(obj) + "'.");
                  }
               }
            }
         }
      }

      public void visitConstantInterfaceMethodref(ConstantInterfaceMethodref obj) {
         if (obj.getTag() != 11) {
            throw new ClassConstraintException("ConstantInterfaceMethodref '" + Pass2Verifier.tostring(obj) + "' has wrong tag!");
         } else {
            int name_and_type_index = obj.getNameAndTypeIndex();
            ConstantNameAndType cnat = (ConstantNameAndType)this.cp.getConstant(name_and_type_index);
            String name = ((ConstantUtf8)this.cp.getConstant(cnat.getNameIndex())).getBytes();
            if (!Pass2Verifier.validInterfaceMethodName(name)) {
               throw new ClassConstraintException("Invalid (interface) method name '" + name + "' referenced by '" + Pass2Verifier.tostring(obj) + "'.");
            } else {
               int class_index = obj.getClassIndex();
               ConstantClass cc = (ConstantClass)this.cp.getConstant(class_index);
               String className = ((ConstantUtf8)this.cp.getConstant(cc.getNameIndex())).getBytes();
               if (!Pass2Verifier.validClassName(className)) {
                  throw new ClassConstraintException("Illegal class name '" + className + "' used by '" + Pass2Verifier.tostring(obj) + "'.");
               } else {
                  String sig = ((ConstantUtf8)this.cp.getConstant(cnat.getSignatureIndex())).getBytes();

                  try {
                     Type t = Type.getReturnType(sig);
                     Type[] ts = Type.getArgumentTypes(sig);
                     if (name.equals("<clinit>") && t != Type.VOID) {
                        Pass2Verifier.this.addMessage("Class or interface initialization method '<clinit>' usually has VOID return type instead of '" + t + "'. Note this is really not a requirement of The Java Virtual Machine Specification, Second Edition.");
                     }

                  } catch (ClassFormatError var11) {
                     throw new ClassConstraintException("Illegal descriptor (==signature) '" + sig + "' used by '" + Pass2Verifier.tostring(obj) + "'.");
                  }
               }
            }
         }
      }

      // $FF: synthetic method
      FAMRAV_Visitor(JavaClass x1, Object x2) {
         this(x1);
      }
   }

   private class CPESSC_Visitor extends EmptyVisitor implements Visitor {
      private Class CONST_Class;
      private Class CONST_Fieldref;
      private Class CONST_Methodref;
      private Class CONST_InterfaceMethodref;
      private Class CONST_String;
      private Class CONST_Integer;
      private Class CONST_Float;
      private Class CONST_Long;
      private Class CONST_Double;
      private Class CONST_NameAndType;
      private Class CONST_Utf8;
      private final JavaClass jc;
      private final ConstantPool cp;
      private final int cplen;
      private DescendingVisitor carrier;
      private HashSet field_names;
      private HashSet field_names_and_desc;
      private HashSet method_names_and_desc;
      // $FF: synthetic field
      static Class class$org$apache$bcel$classfile$ConstantClass;
      // $FF: synthetic field
      static Class class$org$apache$bcel$classfile$ConstantFieldref;
      // $FF: synthetic field
      static Class class$org$apache$bcel$classfile$ConstantMethodref;
      // $FF: synthetic field
      static Class class$org$apache$bcel$classfile$ConstantInterfaceMethodref;
      // $FF: synthetic field
      static Class class$org$apache$bcel$classfile$ConstantString;
      // $FF: synthetic field
      static Class class$org$apache$bcel$classfile$ConstantInteger;
      // $FF: synthetic field
      static Class class$org$apache$bcel$classfile$ConstantFloat;
      // $FF: synthetic field
      static Class class$org$apache$bcel$classfile$ConstantLong;
      // $FF: synthetic field
      static Class class$org$apache$bcel$classfile$ConstantDouble;
      // $FF: synthetic field
      static Class class$org$apache$bcel$classfile$ConstantNameAndType;
      // $FF: synthetic field
      static Class class$org$apache$bcel$classfile$ConstantUtf8;

      private CPESSC_Visitor(JavaClass _jc) {
         this.field_names = new HashSet();
         this.field_names_and_desc = new HashSet();
         this.method_names_and_desc = new HashSet();
         this.jc = _jc;
         this.cp = _jc.getConstantPool();
         this.cplen = this.cp.getLength();
         this.CONST_Class = class$org$apache$bcel$classfile$ConstantClass == null ? (class$org$apache$bcel$classfile$ConstantClass = class$("org.apache.bcel.classfile.ConstantClass")) : class$org$apache$bcel$classfile$ConstantClass;
         this.CONST_Fieldref = class$org$apache$bcel$classfile$ConstantFieldref == null ? (class$org$apache$bcel$classfile$ConstantFieldref = class$("org.apache.bcel.classfile.ConstantFieldref")) : class$org$apache$bcel$classfile$ConstantFieldref;
         this.CONST_Methodref = class$org$apache$bcel$classfile$ConstantMethodref == null ? (class$org$apache$bcel$classfile$ConstantMethodref = class$("org.apache.bcel.classfile.ConstantMethodref")) : class$org$apache$bcel$classfile$ConstantMethodref;
         this.CONST_InterfaceMethodref = class$org$apache$bcel$classfile$ConstantInterfaceMethodref == null ? (class$org$apache$bcel$classfile$ConstantInterfaceMethodref = class$("org.apache.bcel.classfile.ConstantInterfaceMethodref")) : class$org$apache$bcel$classfile$ConstantInterfaceMethodref;
         this.CONST_String = class$org$apache$bcel$classfile$ConstantString == null ? (class$org$apache$bcel$classfile$ConstantString = class$("org.apache.bcel.classfile.ConstantString")) : class$org$apache$bcel$classfile$ConstantString;
         this.CONST_Integer = class$org$apache$bcel$classfile$ConstantInteger == null ? (class$org$apache$bcel$classfile$ConstantInteger = class$("org.apache.bcel.classfile.ConstantInteger")) : class$org$apache$bcel$classfile$ConstantInteger;
         this.CONST_Float = class$org$apache$bcel$classfile$ConstantFloat == null ? (class$org$apache$bcel$classfile$ConstantFloat = class$("org.apache.bcel.classfile.ConstantFloat")) : class$org$apache$bcel$classfile$ConstantFloat;
         this.CONST_Long = class$org$apache$bcel$classfile$ConstantLong == null ? (class$org$apache$bcel$classfile$ConstantLong = class$("org.apache.bcel.classfile.ConstantLong")) : class$org$apache$bcel$classfile$ConstantLong;
         this.CONST_Double = class$org$apache$bcel$classfile$ConstantDouble == null ? (class$org$apache$bcel$classfile$ConstantDouble = class$("org.apache.bcel.classfile.ConstantDouble")) : class$org$apache$bcel$classfile$ConstantDouble;
         this.CONST_NameAndType = class$org$apache$bcel$classfile$ConstantNameAndType == null ? (class$org$apache$bcel$classfile$ConstantNameAndType = class$("org.apache.bcel.classfile.ConstantNameAndType")) : class$org$apache$bcel$classfile$ConstantNameAndType;
         this.CONST_Utf8 = class$org$apache$bcel$classfile$ConstantUtf8 == null ? (class$org$apache$bcel$classfile$ConstantUtf8 = class$("org.apache.bcel.classfile.ConstantUtf8")) : class$org$apache$bcel$classfile$ConstantUtf8;
         this.carrier = new DescendingVisitor(_jc, this);
         this.carrier.visit();
      }

      private void checkIndex(Node referrer, int index, Class shouldbe) {
         if (index >= 0 && index < this.cplen) {
            Constant c = this.cp.getConstant(index);
            if (!shouldbe.isInstance(c)) {
               String isnot = shouldbe.toString().substring(shouldbe.toString().lastIndexOf(".") + 1);
               throw new ClassCastException("Illegal constant '" + Pass2Verifier.tostring(c) + "' at index '" + index + "'. '" + Pass2Verifier.tostring(referrer) + "' expects a '" + shouldbe + "'.");
            }
         } else {
            throw new ClassConstraintException("Invalid index '" + index + "' used by '" + Pass2Verifier.tostring(referrer) + "'.");
         }
      }

      public void visitJavaClass(JavaClass obj) {
         Attribute[] atts = obj.getAttributes();
         boolean foundSourceFile = false;
         boolean foundInnerClasses = false;
         boolean hasInnerClass = (Pass2Verifier.this.new InnerClassDetector(this.jc)).innerClassReferenced();

         for(int i = 0; i < atts.length; ++i) {
            if (!(atts[i] instanceof SourceFile) && !(atts[i] instanceof Deprecated) && !(atts[i] instanceof InnerClasses) && !(atts[i] instanceof Synthetic)) {
               Pass2Verifier.this.addMessage("Attribute '" + Pass2Verifier.tostring(atts[i]) + "' as an attribute of the ClassFile structure '" + Pass2Verifier.tostring(obj) + "' is unknown and will therefore be ignored.");
            }

            if (atts[i] instanceof SourceFile) {
               if (foundSourceFile) {
                  throw new ClassConstraintException("A ClassFile structure (like '" + Pass2Verifier.tostring(obj) + "') may have no more than one SourceFile attribute.");
               }

               foundSourceFile = true;
            }

            if (atts[i] instanceof InnerClasses) {
               if (!foundInnerClasses) {
                  foundInnerClasses = true;
               } else if (hasInnerClass) {
                  throw new ClassConstraintException("A Classfile structure (like '" + Pass2Verifier.tostring(obj) + "') must have exactly one InnerClasses attribute if at least one Inner Class is referenced (which is the case). More than one InnerClasses attribute was found.");
               }

               if (!hasInnerClass) {
                  Pass2Verifier.this.addMessage("No referenced Inner Class found, but InnerClasses attribute '" + Pass2Verifier.tostring(atts[i]) + "' found. Strongly suggest removal of that attribute.");
               }
            }
         }

         if (hasInnerClass && !foundInnerClasses) {
            Pass2Verifier.this.addMessage("A Classfile structure (like '" + Pass2Verifier.tostring(obj) + "') must have exactly one InnerClasses attribute if at least one Inner Class is referenced (which is the case). No InnerClasses attribute was found.");
         }

      }

      public void visitConstantClass(ConstantClass obj) {
         if (obj.getTag() != 7) {
            throw new ClassConstraintException("Wrong constant tag in '" + Pass2Verifier.tostring(obj) + "'.");
         } else {
            this.checkIndex(obj, obj.getNameIndex(), this.CONST_Utf8);
         }
      }

      public void visitConstantFieldref(ConstantFieldref obj) {
         if (obj.getTag() != 9) {
            throw new ClassConstraintException("Wrong constant tag in '" + Pass2Verifier.tostring(obj) + "'.");
         } else {
            this.checkIndex(obj, obj.getClassIndex(), this.CONST_Class);
            this.checkIndex(obj, obj.getNameAndTypeIndex(), this.CONST_NameAndType);
         }
      }

      public void visitConstantMethodref(ConstantMethodref obj) {
         if (obj.getTag() != 10) {
            throw new ClassConstraintException("Wrong constant tag in '" + Pass2Verifier.tostring(obj) + "'.");
         } else {
            this.checkIndex(obj, obj.getClassIndex(), this.CONST_Class);
            this.checkIndex(obj, obj.getNameAndTypeIndex(), this.CONST_NameAndType);
         }
      }

      public void visitConstantInterfaceMethodref(ConstantInterfaceMethodref obj) {
         if (obj.getTag() != 11) {
            throw new ClassConstraintException("Wrong constant tag in '" + Pass2Verifier.tostring(obj) + "'.");
         } else {
            this.checkIndex(obj, obj.getClassIndex(), this.CONST_Class);
            this.checkIndex(obj, obj.getNameAndTypeIndex(), this.CONST_NameAndType);
         }
      }

      public void visitConstantString(ConstantString obj) {
         if (obj.getTag() != 8) {
            throw new ClassConstraintException("Wrong constant tag in '" + Pass2Verifier.tostring(obj) + "'.");
         } else {
            this.checkIndex(obj, obj.getStringIndex(), this.CONST_Utf8);
         }
      }

      public void visitConstantInteger(ConstantInteger obj) {
         if (obj.getTag() != 3) {
            throw new ClassConstraintException("Wrong constant tag in '" + Pass2Verifier.tostring(obj) + "'.");
         }
      }

      public void visitConstantFloat(ConstantFloat obj) {
         if (obj.getTag() != 4) {
            throw new ClassConstraintException("Wrong constant tag in '" + Pass2Verifier.tostring(obj) + "'.");
         }
      }

      public void visitConstantLong(ConstantLong obj) {
         if (obj.getTag() != 5) {
            throw new ClassConstraintException("Wrong constant tag in '" + Pass2Verifier.tostring(obj) + "'.");
         }
      }

      public void visitConstantDouble(ConstantDouble obj) {
         if (obj.getTag() != 6) {
            throw new ClassConstraintException("Wrong constant tag in '" + Pass2Verifier.tostring(obj) + "'.");
         }
      }

      public void visitConstantNameAndType(ConstantNameAndType obj) {
         if (obj.getTag() != 12) {
            throw new ClassConstraintException("Wrong constant tag in '" + Pass2Verifier.tostring(obj) + "'.");
         } else {
            this.checkIndex(obj, obj.getNameIndex(), this.CONST_Utf8);
            this.checkIndex(obj, obj.getSignatureIndex(), this.CONST_Utf8);
         }
      }

      public void visitConstantUtf8(ConstantUtf8 obj) {
         if (obj.getTag() != 1) {
            throw new ClassConstraintException("Wrong constant tag in '" + Pass2Verifier.tostring(obj) + "'.");
         }
      }

      public void visitField(Field obj) {
         if (this.jc.isClass()) {
            int maxone = 0;
            if (obj.isPrivate()) {
               ++maxone;
            }

            if (obj.isProtected()) {
               ++maxone;
            }

            if (obj.isPublic()) {
               ++maxone;
            }

            if (maxone > 1) {
               throw new ClassConstraintException("Field '" + Pass2Verifier.tostring(obj) + "' must only have at most one of its ACC_PRIVATE, ACC_PROTECTED, ACC_PUBLIC modifiers set.");
            }

            if (obj.isFinal() && obj.isVolatile()) {
               throw new ClassConstraintException("Field '" + Pass2Verifier.tostring(obj) + "' must only have at most one of its ACC_FINAL, ACC_VOLATILE modifiers set.");
            }
         } else {
            if (!obj.isPublic()) {
               throw new ClassConstraintException("Interface field '" + Pass2Verifier.tostring(obj) + "' must have the ACC_PUBLIC modifier set but hasn't!");
            }

            if (!obj.isStatic()) {
               throw new ClassConstraintException("Interface field '" + Pass2Verifier.tostring(obj) + "' must have the ACC_STATIC modifier set but hasn't!");
            }

            if (!obj.isFinal()) {
               throw new ClassConstraintException("Interface field '" + Pass2Verifier.tostring(obj) + "' must have the ACC_FINAL modifier set but hasn't!");
            }
         }

         if ((obj.getAccessFlags() & -224) > 0) {
            Pass2Verifier.this.addMessage("Field '" + Pass2Verifier.tostring(obj) + "' has access flag(s) other than ACC_PUBLIC, ACC_PRIVATE, ACC_PROTECTED, ACC_STATIC, ACC_FINAL, ACC_VOLATILE, ACC_TRANSIENT set (ignored).");
         }

         this.checkIndex(obj, obj.getNameIndex(), this.CONST_Utf8);
         String name = obj.getName();
         if (!Pass2Verifier.validFieldName(name)) {
            throw new ClassConstraintException("Field '" + Pass2Verifier.tostring(obj) + "' has illegal name '" + obj.getName() + "'.");
         } else {
            this.checkIndex(obj, obj.getSignatureIndex(), this.CONST_Utf8);
            String sig = ((ConstantUtf8)this.cp.getConstant(obj.getSignatureIndex())).getBytes();

            try {
               Type var4 = Type.getType(sig);
            } catch (ClassFormatError var7) {
               throw new ClassConstraintException("Illegal descriptor (==signature) '" + sig + "' used by '" + Pass2Verifier.tostring(obj) + "'.");
            }

            String nameanddesc = name + sig;
            if (this.field_names_and_desc.contains(nameanddesc)) {
               throw new ClassConstraintException("No two fields (like '" + Pass2Verifier.tostring(obj) + "') are allowed have same names and descriptors!");
            } else {
               if (this.field_names.contains(name)) {
                  Pass2Verifier.this.addMessage("More than one field of name '" + name + "' detected (but with different type descriptors). This is very unusual.");
               }

               this.field_names_and_desc.add(nameanddesc);
               this.field_names.add(name);
               Attribute[] atts = obj.getAttributes();

               for(int i = 0; i < atts.length; ++i) {
                  if (!(atts[i] instanceof ConstantValue) && !(atts[i] instanceof Synthetic) && !(atts[i] instanceof Deprecated)) {
                     Pass2Verifier.this.addMessage("Attribute '" + Pass2Verifier.tostring(atts[i]) + "' as an attribute of Field '" + Pass2Verifier.tostring(obj) + "' is unknown and will therefore be ignored.");
                  }

                  if (!(atts[i] instanceof ConstantValue)) {
                     Pass2Verifier.this.addMessage("Attribute '" + Pass2Verifier.tostring(atts[i]) + "' as an attribute of Field '" + Pass2Verifier.tostring(obj) + "' is not a ConstantValue and is therefore only of use for debuggers and such.");
                  }
               }

            }
         }
      }

      public void visitMethod(Method obj) {
         this.checkIndex(obj, obj.getNameIndex(), this.CONST_Utf8);
         String name = obj.getName();
         if (!Pass2Verifier.validMethodName(name, true)) {
            throw new ClassConstraintException("Method '" + Pass2Verifier.tostring(obj) + "' has illegal name '" + name + "'.");
         } else {
            this.checkIndex(obj, obj.getSignatureIndex(), this.CONST_Utf8);
            String sig = ((ConstantUtf8)this.cp.getConstant(obj.getSignatureIndex())).getBytes();

            Type t;
            Type[] ts;
            try {
               t = Type.getReturnType(sig);
               ts = Type.getArgumentTypes(sig);
            } catch (ClassFormatError var12) {
               throw new ClassConstraintException("Illegal descriptor (==signature) '" + sig + "' used by Method '" + Pass2Verifier.tostring(obj) + "'.");
            }

            Type act = t;
            if (t instanceof ArrayType) {
               act = ((ArrayType)t).getBasicType();
            }

            if (act instanceof ObjectType) {
               Verifier vx = VerifierFactory.getVerifier(((ObjectType)act).getClassName());
               VerificationResult vr = vx.doPass1();
               if (vr != VerificationResult.VR_OK) {
                  throw new ClassConstraintException("Method '" + Pass2Verifier.tostring(obj) + "' has a return type that does not pass verification pass 1: '" + vr + "'.");
               }
            }

            for(int ix = 0; ix < ts.length; ++ix) {
               act = ts[ix];
               if (act instanceof ArrayType) {
                  act = ((ArrayType)act).getBasicType();
               }

               if (act instanceof ObjectType) {
                  Verifier v = VerifierFactory.getVerifier(((ObjectType)act).getClassName());
                  VerificationResult vrx = v.doPass1();
                  if (vrx != VerificationResult.VR_OK) {
                     throw new ClassConstraintException("Method '" + Pass2Verifier.tostring(obj) + "' has an argument type that does not pass verification pass 1: '" + vrx + "'.");
                  }
               }
            }

            if (name.equals("<clinit>") && ts.length != 0) {
               throw new ClassConstraintException("Method '" + Pass2Verifier.tostring(obj) + "' has illegal name '" + name + "'. It's name resembles the class or interface initialization method which it isn't because of its arguments (==descriptor).");
            } else {
               if (this.jc.isClass()) {
                  int maxone = 0;
                  if (obj.isPrivate()) {
                     ++maxone;
                  }

                  if (obj.isProtected()) {
                     ++maxone;
                  }

                  if (obj.isPublic()) {
                     ++maxone;
                  }

                  if (maxone > 1) {
                     throw new ClassConstraintException("Method '" + Pass2Verifier.tostring(obj) + "' must only have at most one of its ACC_PRIVATE, ACC_PROTECTED, ACC_PUBLIC modifiers set.");
                  }

                  if (obj.isAbstract()) {
                     if (obj.isFinal()) {
                        throw new ClassConstraintException("Abstract method '" + Pass2Verifier.tostring(obj) + "' must not have the ACC_FINAL modifier set.");
                     }

                     if (obj.isNative()) {
                        throw new ClassConstraintException("Abstract method '" + Pass2Verifier.tostring(obj) + "' must not have the ACC_NATIVE modifier set.");
                     }

                     if (obj.isPrivate()) {
                        throw new ClassConstraintException("Abstract method '" + Pass2Verifier.tostring(obj) + "' must not have the ACC_PRIVATE modifier set.");
                     }

                     if (obj.isStatic()) {
                        throw new ClassConstraintException("Abstract method '" + Pass2Verifier.tostring(obj) + "' must not have the ACC_STATIC modifier set.");
                     }

                     if (obj.isStrictfp()) {
                        throw new ClassConstraintException("Abstract method '" + Pass2Verifier.tostring(obj) + "' must not have the ACC_STRICT modifier set.");
                     }

                     if (obj.isSynchronized()) {
                        throw new ClassConstraintException("Abstract method '" + Pass2Verifier.tostring(obj) + "' must not have the ACC_SYNCHRONIZED modifier set.");
                     }
                  }
               } else if (!name.equals("<clinit>")) {
                  if (!obj.isPublic()) {
                     throw new ClassConstraintException("Interface method '" + Pass2Verifier.tostring(obj) + "' must have the ACC_PUBLIC modifier set but hasn't!");
                  }

                  if (!obj.isAbstract()) {
                     throw new ClassConstraintException("Interface method '" + Pass2Verifier.tostring(obj) + "' must have the ACC_STATIC modifier set but hasn't!");
                  }

                  if (obj.isPrivate() || obj.isProtected() || obj.isStatic() || obj.isFinal() || obj.isSynchronized() || obj.isNative() || obj.isStrictfp()) {
                     throw new ClassConstraintException("Interface method '" + Pass2Verifier.tostring(obj) + "' must not have any of the ACC_PRIVATE, ACC_PROTECTED, ACC_STATIC, ACC_FINAL, ACC_SYNCHRONIZED, ACC_NATIVE, ACC_ABSTRACT, ACC_STRICT modifiers set.");
                  }
               }

               if (!name.equals("<init>") || !obj.isStatic() && !obj.isFinal() && !obj.isSynchronized() && !obj.isNative() && !obj.isAbstract()) {
                  if (name.equals("<clinit>")) {
                     if ((obj.getAccessFlags() & -2049) > 0) {
                        Pass2Verifier.this.addMessage("Class or interface initialization method '" + Pass2Verifier.tostring(obj) + "' has superfluous access modifier(s) set: everything but ACC_STRICT is ignored.");
                     }

                     if (obj.isAbstract()) {
                        throw new ClassConstraintException("Class or interface initialization method '" + Pass2Verifier.tostring(obj) + "' must not be abstract. This contradicts the Java Language Specification, Second Edition (which omits this constraint) but is common practice of existing verifiers.");
                     }
                  }

                  if ((obj.getAccessFlags() & -3392) > 0) {
                     Pass2Verifier.this.addMessage("Method '" + Pass2Verifier.tostring(obj) + "' has access flag(s) other than ACC_PUBLIC, ACC_PRIVATE, ACC_PROTECTED, ACC_STATIC, ACC_FINAL, ACC_SYNCHRONIZED, ACC_NATIVE, ACC_ABSTRACT, ACC_STRICT set (ignored).");
                  }

                  String nameanddesc = name + sig;
                  if (this.method_names_and_desc.contains(nameanddesc)) {
                     throw new ClassConstraintException("No two methods (like '" + Pass2Verifier.tostring(obj) + "') are allowed have same names and desciptors!");
                  } else {
                     this.method_names_and_desc.add(nameanddesc);
                     Attribute[] atts = obj.getAttributes();
                     int num_code_atts = 0;

                     for(int i = 0; i < atts.length; ++i) {
                        if (!(atts[i] instanceof Code) && !(atts[i] instanceof ExceptionTable) && !(atts[i] instanceof Synthetic) && !(atts[i] instanceof Deprecated)) {
                           Pass2Verifier.this.addMessage("Attribute '" + Pass2Verifier.tostring(atts[i]) + "' as an attribute of Method '" + Pass2Verifier.tostring(obj) + "' is unknown and will therefore be ignored.");
                        }

                        if (!(atts[i] instanceof Code) && !(atts[i] instanceof ExceptionTable)) {
                           Pass2Verifier.this.addMessage("Attribute '" + Pass2Verifier.tostring(atts[i]) + "' as an attribute of Method '" + Pass2Verifier.tostring(obj) + "' is neither Code nor Exceptions and is therefore only of use for debuggers and such.");
                        }

                        if (atts[i] instanceof Code && (obj.isNative() || obj.isAbstract())) {
                           throw new ClassConstraintException("Native or abstract methods like '" + Pass2Verifier.tostring(obj) + "' must not have a Code attribute like '" + Pass2Verifier.tostring(atts[i]) + "'.");
                        }

                        if (atts[i] instanceof Code) {
                           ++num_code_atts;
                        }
                     }

                     if (!obj.isNative() && !obj.isAbstract() && num_code_atts != 1) {
                        throw new ClassConstraintException("Non-native, non-abstract methods like '" + Pass2Verifier.tostring(obj) + "' must have exactly one Code attribute (found: " + num_code_atts + ").");
                     }
                  }
               } else {
                  throw new ClassConstraintException("Instance initialization method '" + Pass2Verifier.tostring(obj) + "' must not have any of the ACC_STATIC, ACC_FINAL, ACC_SYNCHRONIZED, ACC_NATIVE, ACC_ABSTRACT modifiers set.");
               }
            }
         }
      }

      public void visitSourceFile(SourceFile obj) {
         this.checkIndex(obj, obj.getNameIndex(), this.CONST_Utf8);
         String name = ((ConstantUtf8)this.cp.getConstant(obj.getNameIndex())).getBytes();
         if (!name.equals("SourceFile")) {
            throw new ClassConstraintException("The SourceFile attribute '" + Pass2Verifier.tostring(obj) + "' is not correctly named 'SourceFile' but '" + name + "'.");
         } else {
            this.checkIndex(obj, obj.getSourceFileIndex(), this.CONST_Utf8);
            String sourcefilename = ((ConstantUtf8)this.cp.getConstant(obj.getSourceFileIndex())).getBytes();
            String sourcefilenamelc = sourcefilename.toLowerCase();
            if (sourcefilename.indexOf(47) != -1 || sourcefilename.indexOf(92) != -1 || sourcefilename.indexOf(58) != -1 || sourcefilenamelc.lastIndexOf(".java") == -1) {
               Pass2Verifier.this.addMessage("SourceFile attribute '" + Pass2Verifier.tostring(obj) + "' has a funny name: remember not to confuse certain parsers working on javap's output. Also, this name ('" + sourcefilename + "') is considered an unqualified (simple) file name only.");
            }

         }
      }

      public void visitDeprecated(Deprecated obj) {
         this.checkIndex(obj, obj.getNameIndex(), this.CONST_Utf8);
         String name = ((ConstantUtf8)this.cp.getConstant(obj.getNameIndex())).getBytes();
         if (!name.equals("Deprecated")) {
            throw new ClassConstraintException("The Deprecated attribute '" + Pass2Verifier.tostring(obj) + "' is not correctly named 'Deprecated' but '" + name + "'.");
         }
      }

      public void visitSynthetic(Synthetic obj) {
         this.checkIndex(obj, obj.getNameIndex(), this.CONST_Utf8);
         String name = ((ConstantUtf8)this.cp.getConstant(obj.getNameIndex())).getBytes();
         if (!name.equals("Synthetic")) {
            throw new ClassConstraintException("The Synthetic attribute '" + Pass2Verifier.tostring(obj) + "' is not correctly named 'Synthetic' but '" + name + "'.");
         }
      }

      public void visitInnerClasses(InnerClasses obj) {
         this.checkIndex(obj, obj.getNameIndex(), this.CONST_Utf8);
         String name = ((ConstantUtf8)this.cp.getConstant(obj.getNameIndex())).getBytes();
         if (!name.equals("InnerClasses")) {
            throw new ClassConstraintException("The InnerClasses attribute '" + Pass2Verifier.tostring(obj) + "' is not correctly named 'InnerClasses' but '" + name + "'.");
         } else {
            InnerClass[] ics = obj.getInnerClasses();

            for(int i = 0; i < ics.length; ++i) {
               this.checkIndex(obj, ics[i].getInnerClassIndex(), this.CONST_Class);
               int outer_idx = ics[i].getOuterClassIndex();
               if (outer_idx != 0) {
                  this.checkIndex(obj, outer_idx, this.CONST_Class);
               }

               int innername_idx = ics[i].getInnerNameIndex();
               if (innername_idx != 0) {
                  this.checkIndex(obj, innername_idx, this.CONST_Utf8);
               }

               int acc = ics[i].getInnerAccessFlags();
               acc &= -1568;
               if (acc != 0) {
                  Pass2Verifier.this.addMessage("Unknown access flag for inner class '" + Pass2Verifier.tostring(ics[i]) + "' set (InnerClasses attribute '" + Pass2Verifier.tostring(obj) + "').");
               }
            }

         }
      }

      public void visitConstantValue(ConstantValue obj) {
         this.checkIndex(obj, obj.getNameIndex(), this.CONST_Utf8);
         String name = ((ConstantUtf8)this.cp.getConstant(obj.getNameIndex())).getBytes();
         if (!name.equals("ConstantValue")) {
            throw new ClassConstraintException("The ConstantValue attribute '" + Pass2Verifier.tostring(obj) + "' is not correctly named 'ConstantValue' but '" + name + "'.");
         } else {
            Object pred = this.carrier.predecessor();
            if (pred instanceof Field) {
               Field f = (Field)pred;
               Type field_type = Type.getType(((ConstantUtf8)this.cp.getConstant(f.getSignatureIndex())).getBytes());
               int index = obj.getConstantValueIndex();
               if (index >= 0 && index < this.cplen) {
                  Constant c = this.cp.getConstant(index);
                  if (!this.CONST_Long.isInstance(c) || !field_type.equals(Type.LONG)) {
                     if (!this.CONST_Float.isInstance(c) || !field_type.equals(Type.FLOAT)) {
                        if (!this.CONST_Double.isInstance(c) || !field_type.equals(Type.DOUBLE)) {
                           if (!this.CONST_Integer.isInstance(c) || !field_type.equals(Type.INT) && !field_type.equals(Type.SHORT) && !field_type.equals(Type.CHAR) && !field_type.equals(Type.BYTE) && !field_type.equals(Type.BOOLEAN)) {
                              if (!this.CONST_String.isInstance(c) || !field_type.equals(Type.STRING)) {
                                 throw new ClassConstraintException("Illegal type of ConstantValue '" + obj + "' embedding Constant '" + c + "'. It is referenced by field '" + Pass2Verifier.tostring(f) + "' expecting a different type: '" + field_type + "'.");
                              }
                           }
                        }
                     }
                  }
               } else {
                  throw new ClassConstraintException("Invalid index '" + index + "' used by '" + Pass2Verifier.tostring(obj) + "'.");
               }
            }
         }
      }

      public void visitCode(Code obj) {
         this.checkIndex(obj, obj.getNameIndex(), this.CONST_Utf8);
         String name = ((ConstantUtf8)this.cp.getConstant(obj.getNameIndex())).getBytes();
         if (!name.equals("Code")) {
            throw new ClassConstraintException("The Code attribute '" + Pass2Verifier.tostring(obj) + "' is not correctly named 'Code' but '" + name + "'.");
         } else {
            Method m = null;
            if (!(this.carrier.predecessor() instanceof Method)) {
               Pass2Verifier.this.addMessage("Code attribute '" + Pass2Verifier.tostring(obj) + "' is not declared in a method_info structure but in '" + this.carrier.predecessor() + "'. Ignored.");
            } else {
               m = (Method)this.carrier.predecessor();
               if (obj.getCode().length == 0) {
                  throw new ClassConstraintException("Code array of Code attribute '" + Pass2Verifier.tostring(obj) + "' (method '" + m + "') must not be empty.");
               } else {
                  CodeException[] exc_table = obj.getExceptionTable();

                  int method_number;
                  for(int i = 0; i < exc_table.length; ++i) {
                     method_number = exc_table[i].getCatchType();
                     if (method_number != 0) {
                        this.checkIndex(obj, method_number, this.CONST_Class);
                        ConstantClass cc = (ConstantClass)this.cp.getConstant(method_number);
                        this.checkIndex(cc, cc.getNameIndex(), this.CONST_Utf8);
                        String cname = ((ConstantUtf8)this.cp.getConstant(cc.getNameIndex())).getBytes().replace('/', '.');
                        Verifier v = VerifierFactory.getVerifier(cname);
                        VerificationResult vr = v.doPass1();
                        if (vr != VerificationResult.VR_OK) {
                           throw new ClassConstraintException("Code attribute '" + Pass2Verifier.tostring(obj) + "' (method '" + m + "') has an exception_table entry '" + Pass2Verifier.tostring(exc_table[i]) + "' that references '" + cname + "' as an Exception but it does not pass verification pass 1: " + vr);
                        }

                        JavaClass e = Repository.lookupClass(cname);
                        JavaClass t = Repository.lookupClass(Type.THROWABLE.getClassName());

                        for(JavaClass o = Repository.lookupClass(Type.OBJECT.getClassName()); e != o && e != t; e = Repository.lookupClass(e.getSuperclassName())) {
                           v = VerifierFactory.getVerifier(e.getSuperclassName());
                           vr = v.doPass1();
                           if (vr != VerificationResult.VR_OK) {
                              throw new ClassConstraintException("Code attribute '" + Pass2Verifier.tostring(obj) + "' (method '" + m + "') has an exception_table entry '" + Pass2Verifier.tostring(exc_table[i]) + "' that references '" + cname + "' as an Exception but '" + e.getSuperclassName() + "' in the ancestor hierachy does not pass verification pass 1: " + vr);
                           }
                        }

                        if (e != t) {
                           throw new ClassConstraintException("Code attribute '" + Pass2Verifier.tostring(obj) + "' (method '" + m + "') has an exception_table entry '" + Pass2Verifier.tostring(exc_table[i]) + "' that references '" + cname + "' as an Exception but it is not a subclass of '" + t.getClassName() + "'.");
                        }
                     }
                  }

                  method_number = -1;
                  Method[] ms = Repository.lookupClass(Pass2Verifier.this.myOwner.getClassName()).getMethods();

                  for(int mn = 0; mn < ms.length; ++mn) {
                     if (m == ms[mn]) {
                        method_number = mn;
                        break;
                     }
                  }

                  if (method_number < 0) {
                     throw new AssertionViolatedException("Could not find a known BCEL Method object in the corresponding BCEL JavaClass object.");
                  } else {
                     Pass2Verifier.this.localVariablesInfos[method_number] = new LocalVariablesInfo(obj.getMaxLocals());
                     int num_of_lvt_attribs = 0;
                     Attribute[] atts = obj.getAttributes();

                     for(int a = 0; a < atts.length; ++a) {
                        if (!(atts[a] instanceof LineNumberTable) && !(atts[a] instanceof LocalVariableTable)) {
                           Pass2Verifier.this.addMessage("Attribute '" + Pass2Verifier.tostring(atts[a]) + "' as an attribute of Code attribute '" + Pass2Verifier.tostring(obj) + "' (method '" + m + "') is unknown and will therefore be ignored.");
                        } else {
                           Pass2Verifier.this.addMessage("Attribute '" + Pass2Verifier.tostring(atts[a]) + "' as an attribute of Code attribute '" + Pass2Verifier.tostring(obj) + "' (method '" + m + "') will effectively be ignored and is only useful for debuggers and such.");
                        }

                        if (atts[a] instanceof LocalVariableTable) {
                           LocalVariableTable lvt = (LocalVariableTable)atts[a];
                           this.checkIndex(lvt, lvt.getNameIndex(), this.CONST_Utf8);
                           String lvtname = ((ConstantUtf8)this.cp.getConstant(lvt.getNameIndex())).getBytes();
                           if (!lvtname.equals("LocalVariableTable")) {
                              throw new ClassConstraintException("The LocalVariableTable attribute '" + Pass2Verifier.tostring(lvt) + "' is not correctly named 'LocalVariableTable' but '" + lvtname + "'.");
                           }

                           Code code = obj;
                           int max_locals = obj.getMaxLocals();
                           LocalVariable[] localvariables = lvt.getLocalVariableTable();

                           for(int ix = 0; ix < localvariables.length; ++ix) {
                              this.checkIndex(lvt, localvariables[ix].getNameIndex(), this.CONST_Utf8);
                              String localname = ((ConstantUtf8)this.cp.getConstant(localvariables[ix].getNameIndex())).getBytes();
                              if (!Pass2Verifier.validJavaIdentifier(localname)) {
                                 throw new ClassConstraintException("LocalVariableTable '" + Pass2Verifier.tostring(lvt) + "' references a local variable by the name '" + localname + "' which is not a legal Java simple name.");
                              }

                              this.checkIndex(lvt, localvariables[ix].getSignatureIndex(), this.CONST_Utf8);
                              String localsig = ((ConstantUtf8)this.cp.getConstant(localvariables[ix].getSignatureIndex())).getBytes();

                              Type tx;
                              try {
                                 tx = Type.getType(localsig);
                              } catch (ClassFormatError var23) {
                                 throw new ClassConstraintException("Illegal descriptor (==signature) '" + localsig + "' used by LocalVariable '" + Pass2Verifier.tostring(localvariables[ix]) + "' referenced by '" + Pass2Verifier.tostring(lvt) + "'.");
                              }

                              int localindex = localvariables[ix].getIndex();
                              if ((tx != Type.LONG && tx != Type.DOUBLE ? localindex : localindex + 1) >= code.getMaxLocals()) {
                                 throw new ClassConstraintException("LocalVariableTable attribute '" + Pass2Verifier.tostring(lvt) + "' references a LocalVariable '" + Pass2Verifier.tostring(localvariables[ix]) + "' with an index that exceeds the surrounding Code attribute's max_locals value of '" + code.getMaxLocals() + "'.");
                              }

                              try {
                                 Pass2Verifier.this.localVariablesInfos[method_number].add(localindex, localname, localvariables[ix].getStartPC(), localvariables[ix].getLength(), tx);
                              } catch (LocalVariableInfoInconsistentException var24) {
                                 throw new ClassConstraintException("Conflicting information in LocalVariableTable '" + Pass2Verifier.tostring(lvt) + "' found in Code attribute '" + Pass2Verifier.tostring(obj) + "' (method '" + Pass2Verifier.tostring(m) + "'). " + var24.getMessage());
                              }
                           }

                           ++num_of_lvt_attribs;
                           if (num_of_lvt_attribs > obj.getMaxLocals()) {
                              throw new ClassConstraintException("Number of LocalVariableTable attributes of Code attribute '" + Pass2Verifier.tostring(obj) + "' (method '" + Pass2Verifier.tostring(m) + "') exceeds number of local variable slots '" + obj.getMaxLocals() + "' ('There may be no more than one LocalVariableTable attribute per local variable in the Code attribute.').");
                           }
                        }
                     }

                  }
               }
            }
         }
      }

      public void visitExceptionTable(ExceptionTable obj) {
         this.checkIndex(obj, obj.getNameIndex(), this.CONST_Utf8);
         String name = ((ConstantUtf8)this.cp.getConstant(obj.getNameIndex())).getBytes();
         if (!name.equals("Exceptions")) {
            throw new ClassConstraintException("The Exceptions attribute '" + Pass2Verifier.tostring(obj) + "' is not correctly named 'Exceptions' but '" + name + "'.");
         } else {
            int[] exc_indices = obj.getExceptionIndexTable();

            for(int i = 0; i < exc_indices.length; ++i) {
               this.checkIndex(obj, exc_indices[i], this.CONST_Class);
               ConstantClass cc = (ConstantClass)this.cp.getConstant(exc_indices[i]);
               this.checkIndex(cc, cc.getNameIndex(), this.CONST_Utf8);
               String cname = ((ConstantUtf8)this.cp.getConstant(cc.getNameIndex())).getBytes().replace('/', '.');
               Verifier v = VerifierFactory.getVerifier(cname);
               VerificationResult vr = v.doPass1();
               if (vr != VerificationResult.VR_OK) {
                  throw new ClassConstraintException("Exceptions attribute '" + Pass2Verifier.tostring(obj) + "' references '" + cname + "' as an Exception but it does not pass verification pass 1: " + vr);
               }

               JavaClass e = Repository.lookupClass(cname);
               JavaClass t = Repository.lookupClass(Type.THROWABLE.getClassName());

               for(JavaClass o = Repository.lookupClass(Type.OBJECT.getClassName()); e != o && e != t; e = Repository.lookupClass(e.getSuperclassName())) {
                  v = VerifierFactory.getVerifier(e.getSuperclassName());
                  vr = v.doPass1();
                  if (vr != VerificationResult.VR_OK) {
                     throw new ClassConstraintException("Exceptions attribute '" + Pass2Verifier.tostring(obj) + "' references '" + cname + "' as an Exception but '" + e.getSuperclassName() + "' in the ancestor hierachy does not pass verification pass 1: " + vr);
                  }
               }

               if (e != t) {
                  throw new ClassConstraintException("Exceptions attribute '" + Pass2Verifier.tostring(obj) + "' references '" + cname + "' as an Exception but it is not a subclass of '" + t.getClassName() + "'.");
               }
            }

         }
      }

      public void visitLineNumberTable(LineNumberTable obj) {
         this.checkIndex(obj, obj.getNameIndex(), this.CONST_Utf8);
         String name = ((ConstantUtf8)this.cp.getConstant(obj.getNameIndex())).getBytes();
         if (!name.equals("LineNumberTable")) {
            throw new ClassConstraintException("The LineNumberTable attribute '" + Pass2Verifier.tostring(obj) + "' is not correctly named 'LineNumberTable' but '" + name + "'.");
         }
      }

      public void visitLocalVariableTable(LocalVariableTable obj) {
      }

      public void visitUnknown(Unknown obj) {
         this.checkIndex(obj, obj.getNameIndex(), this.CONST_Utf8);
         Pass2Verifier.this.addMessage("Unknown attribute '" + Pass2Verifier.tostring(obj) + "'. This attribute is not known in any context!");
      }

      public void visitLocalVariable(LocalVariable obj) {
      }

      public void visitCodeException(CodeException obj) {
      }

      public void visitConstantPool(ConstantPool obj) {
      }

      public void visitInnerClass(InnerClass obj) {
      }

      public void visitLineNumber(LineNumber obj) {
      }

      // $FF: synthetic method
      CPESSC_Visitor(JavaClass x1, Object x2) {
         this(x1);
      }

      // $FF: synthetic method
      static Class class$(String x0) {
         try {
            return Class.forName(x0);
         } catch (ClassNotFoundException var2) {
            throw new NoClassDefFoundError(var2.getMessage());
         }
      }
   }
}
