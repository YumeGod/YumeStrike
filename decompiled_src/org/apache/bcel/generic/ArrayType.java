package org.apache.bcel.generic;

public final class ArrayType extends ReferenceType {
   private int dimensions;
   private Type basic_type;

   public ArrayType(byte type, int dimensions) {
      this((Type)BasicType.getType(type), dimensions);
   }

   public ArrayType(String class_name, int dimensions) {
      this((Type)(new ObjectType(class_name)), dimensions);
   }

   public ArrayType(Type type, int dimensions) {
      super((byte)13, "<dummy>");
      if (dimensions >= 1 && dimensions <= 255) {
         switch (type.getType()) {
            case 12:
               throw new ClassGenException("Invalid type: void[]");
            case 13:
               ArrayType array = (ArrayType)type;
               this.dimensions = dimensions + array.dimensions;
               this.basic_type = array.basic_type;
               break;
            default:
               this.dimensions = dimensions;
               this.basic_type = type;
         }

         StringBuffer buf = new StringBuffer();

         for(int i = 0; i < this.dimensions; ++i) {
            buf.append('[');
         }

         buf.append(this.basic_type.getSignature());
         super.signature = buf.toString();
      } else {
         throw new ClassGenException("Invalid number of dimensions: " + dimensions);
      }
   }

   public Type getBasicType() {
      return this.basic_type;
   }

   public Type getElementType() {
      return (Type)(this.dimensions == 1 ? this.basic_type : new ArrayType(this.basic_type, this.dimensions - 1));
   }

   public int getDimensions() {
      return this.dimensions;
   }

   public int hashcode() {
      return this.basic_type.hashCode() ^ this.dimensions;
   }

   public boolean equals(Object type) {
      if (!(type instanceof ArrayType)) {
         return false;
      } else {
         ArrayType array = (ArrayType)type;
         return array.dimensions == this.dimensions && array.basic_type.equals(this.basic_type);
      }
   }
}
