package net.jsign.bouncycastle.util;

public interface StringList extends Iterable {
   boolean add(String var1);

   String get(int var1);

   int size();

   String[] toStringArray();

   String[] toStringArray(int var1, int var2);
}
