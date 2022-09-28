package org.apache.xerces.dom3.as;

/** @deprecated */
public interface CharacterDataEditAS extends NodeEditAS {
   boolean getIsWhitespaceOnly();

   boolean canSetData(int var1, int var2);

   boolean canAppendData(String var1);

   boolean canReplaceData(int var1, int var2, String var3);

   boolean canInsertData(int var1, String var2);

   boolean canDeleteData(int var1, int var2);
}
