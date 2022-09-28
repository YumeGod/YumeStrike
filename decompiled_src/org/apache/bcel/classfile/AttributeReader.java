package org.apache.bcel.classfile;

import java.io.DataInputStream;

public interface AttributeReader {
   Attribute createAttribute(int var1, int var2, DataInputStream var3, ConstantPool var4);
}
