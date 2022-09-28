package org.apache.xml.utils;

import java.util.Hashtable;

class ElemDesc {
   Hashtable m_attrs = null;
   int m_flags;
   static final int EMPTY = 2;
   static final int FLOW = 4;
   static final int BLOCK = 8;
   static final int BLOCKFORM = 16;
   static final int BLOCKFORMFIELDSET = 32;
   static final int CDATA = 64;
   static final int PCDATA = 128;
   static final int RAW = 256;
   static final int INLINE = 512;
   static final int INLINEA = 1024;
   static final int INLINELABEL = 2048;
   static final int FONTSTYLE = 4096;
   static final int PHRASE = 8192;
   static final int FORMCTRL = 16384;
   static final int SPECIAL = 32768;
   static final int ASPECIAL = 65536;
   static final int HEADMISC = 131072;
   static final int HEAD = 262144;
   static final int LIST = 524288;
   static final int PREFORMATTED = 1048576;
   static final int WHITESPACESENSITIVE = 2097152;
   static final int ATTRURL = 2;
   static final int ATTREMPTY = 4;

   ElemDesc(int flags) {
      this.m_flags = flags;
   }

   boolean is(int flags) {
      return (this.m_flags & flags) != 0;
   }

   void setAttr(String name, int flags) {
      if (null == this.m_attrs) {
         this.m_attrs = new Hashtable();
      }

      this.m_attrs.put(name, new Integer(flags));
   }

   boolean isAttrFlagSet(String name, int flags) {
      if (null != this.m_attrs) {
         Integer _flags = (Integer)this.m_attrs.get(name);
         if (null != _flags) {
            return (_flags & flags) != 0;
         }
      }

      return false;
   }
}
