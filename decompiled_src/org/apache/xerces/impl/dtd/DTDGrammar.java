package org.apache.xerces.impl.dtd;

import java.util.Hashtable;
import java.util.Vector;
import org.apache.xerces.impl.dtd.models.CMAny;
import org.apache.xerces.impl.dtd.models.CMBinOp;
import org.apache.xerces.impl.dtd.models.CMLeaf;
import org.apache.xerces.impl.dtd.models.CMNode;
import org.apache.xerces.impl.dtd.models.CMUniOp;
import org.apache.xerces.impl.dtd.models.ContentModelValidator;
import org.apache.xerces.impl.dtd.models.DFAContentModel;
import org.apache.xerces.impl.dtd.models.MixedContentModel;
import org.apache.xerces.impl.dtd.models.SimpleContentModel;
import org.apache.xerces.impl.dv.DatatypeValidator;
import org.apache.xerces.impl.validation.EntityState;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLDTDContentModelHandler;
import org.apache.xerces.xni.XMLDTDHandler;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.grammars.XMLGrammarDescription;
import org.apache.xerces.xni.parser.XMLDTDContentModelSource;
import org.apache.xerces.xni.parser.XMLDTDSource;

public class DTDGrammar implements XMLDTDHandler, XMLDTDContentModelHandler, EntityState, Grammar {
   public static final int TOP_LEVEL_SCOPE = -1;
   private static final int CHUNK_SHIFT = 8;
   private static final int CHUNK_SIZE = 256;
   private static final int CHUNK_MASK = 255;
   private static final int INITIAL_CHUNK_COUNT = 4;
   private static final short LIST_FLAG = 128;
   private static final short LIST_MASK = -129;
   private static final boolean DEBUG = false;
   protected XMLDTDSource fDTDSource = null;
   protected XMLDTDContentModelSource fDTDContentModelSource = null;
   protected int fCurrentElementIndex;
   protected int fCurrentAttributeIndex;
   protected boolean fReadingExternalDTD = false;
   private SymbolTable fSymbolTable;
   protected XMLDTDDescription fGrammarDescription = null;
   private int fElementDeclCount = 0;
   private QName[][] fElementDeclName = new QName[4][];
   private short[][] fElementDeclType = new short[4][];
   private int[][] fElementDeclContentSpecIndex = new int[4][];
   private ContentModelValidator[][] fElementDeclContentModelValidator = new ContentModelValidator[4][];
   private int[][] fElementDeclFirstAttributeDeclIndex = new int[4][];
   private int[][] fElementDeclLastAttributeDeclIndex = new int[4][];
   private int fAttributeDeclCount = 0;
   private QName[][] fAttributeDeclName = new QName[4][];
   private boolean fIsImmutable = false;
   private short[][] fAttributeDeclType = new short[4][];
   private String[][][] fAttributeDeclEnumeration = new String[4][][];
   private short[][] fAttributeDeclDefaultType = new short[4][];
   private DatatypeValidator[][] fAttributeDeclDatatypeValidator = new DatatypeValidator[4][];
   private String[][] fAttributeDeclDefaultValue = new String[4][];
   private String[][] fAttributeDeclNonNormalizedDefaultValue = new String[4][];
   private int[][] fAttributeDeclNextAttributeDeclIndex = new int[4][];
   private int fContentSpecCount = 0;
   private short[][] fContentSpecType = new short[4][];
   private Object[][] fContentSpecValue = new Object[4][];
   private Object[][] fContentSpecOtherValue = new Object[4][];
   private int fEntityCount = 0;
   private String[][] fEntityName = new String[4][];
   private String[][] fEntityValue = new String[4][];
   private String[][] fEntityPublicId = new String[4][];
   private String[][] fEntitySystemId = new String[4][];
   private String[][] fEntityBaseSystemId = new String[4][];
   private String[][] fEntityNotation = new String[4][];
   private byte[][] fEntityIsPE = new byte[4][];
   private byte[][] fEntityInExternal = new byte[4][];
   private int fNotationCount = 0;
   private String[][] fNotationName = new String[4][];
   private String[][] fNotationPublicId = new String[4][];
   private String[][] fNotationSystemId = new String[4][];
   private String[][] fNotationBaseSystemId = new String[4][];
   private QNameHashtable fElementIndexMap = new QNameHashtable();
   private QNameHashtable fEntityIndexMap = new QNameHashtable();
   private QNameHashtable fNotationIndexMap = new QNameHashtable();
   private boolean fMixed;
   private QName fQName = new QName();
   private QName fQName2 = new QName();
   protected XMLAttributeDecl fAttributeDecl = new XMLAttributeDecl();
   private int fLeafCount = 0;
   private int fEpsilonIndex = -1;
   private XMLElementDecl fElementDecl = new XMLElementDecl();
   private XMLEntityDecl fEntityDecl = new XMLEntityDecl();
   private XMLSimpleType fSimpleType = new XMLSimpleType();
   private XMLContentSpec fContentSpec = new XMLContentSpec();
   Hashtable fElementDeclTab = new Hashtable();
   private short[] fOpStack = null;
   private int[] fNodeIndexStack = null;
   private int[] fPrevNodeIndexStack = null;
   private int fDepth = 0;
   private boolean[] fPEntityStack = new boolean[4];
   private int fPEDepth = 0;
   private int[][] fElementDeclIsExternal = new int[4][];
   private int[][] fAttributeDeclIsExternal = new int[4][];
   int valueIndex = -1;
   int prevNodeIndex = -1;
   int nodeIndex = -1;

   public DTDGrammar(SymbolTable var1, XMLDTDDescription var2) {
      this.fSymbolTable = var1;
      this.fGrammarDescription = var2;
   }

   public XMLGrammarDescription getGrammarDescription() {
      return this.fGrammarDescription;
   }

   public boolean getElementDeclIsExternal(int var1) {
      if (var1 < 0) {
         return false;
      } else {
         int var2 = var1 >> 8;
         int var3 = var1 & 255;
         return this.fElementDeclIsExternal[var2][var3] != 0;
      }
   }

   public boolean getAttributeDeclIsExternal(int var1) {
      if (var1 < 0) {
         return false;
      } else {
         int var2 = var1 >> 8;
         int var3 = var1 & 255;
         return this.fAttributeDeclIsExternal[var2][var3] != 0;
      }
   }

   public int getAttributeDeclIndex(int var1, String var2) {
      if (var1 == -1) {
         return -1;
      } else {
         for(int var3 = this.getFirstAttributeDeclIndex(var1); var3 != -1; var3 = this.getNextAttributeDeclIndex(var3)) {
            this.getAttributeDecl(var3, this.fAttributeDecl);
            if (this.fAttributeDecl.name.rawname == var2 || var2.equals(this.fAttributeDecl.name.rawname)) {
               return var3;
            }
         }

         return -1;
      }
   }

   public void startDTD(XMLLocator var1, Augmentations var2) throws XNIException {
      this.fOpStack = null;
      this.fNodeIndexStack = null;
      this.fPrevNodeIndexStack = null;
   }

   public void startParameterEntity(String var1, XMLResourceIdentifier var2, String var3, Augmentations var4) throws XNIException {
      if (this.fPEDepth == this.fPEntityStack.length) {
         boolean[] var5 = new boolean[this.fPEntityStack.length * 2];
         System.arraycopy(this.fPEntityStack, 0, var5, 0, this.fPEntityStack.length);
         this.fPEntityStack = var5;
      }

      this.fPEntityStack[this.fPEDepth] = this.fReadingExternalDTD;
      ++this.fPEDepth;
   }

   public void startExternalSubset(XMLResourceIdentifier var1, Augmentations var2) throws XNIException {
      this.fReadingExternalDTD = true;
   }

   public void endParameterEntity(String var1, Augmentations var2) throws XNIException {
      --this.fPEDepth;
      this.fReadingExternalDTD = this.fPEntityStack[this.fPEDepth];
   }

   public void endExternalSubset(Augmentations var1) throws XNIException {
      this.fReadingExternalDTD = false;
   }

   public void elementDecl(String var1, String var2, Augmentations var3) throws XNIException {
      XMLElementDecl var4 = (XMLElementDecl)this.fElementDeclTab.get(var1);
      if (var4 != null) {
         if (var4.type != -1) {
            return;
         }

         this.fCurrentElementIndex = this.getElementDeclIndex(var1);
      } else {
         this.fCurrentElementIndex = this.createElementDecl();
      }

      XMLElementDecl var5 = new XMLElementDecl();
      this.fQName.setValues((String)null, var1, var1, (String)null);
      var5.name.setValues(this.fQName);
      var5.contentModelValidator = null;
      var5.scope = -1;
      if (var2.equals("EMPTY")) {
         var5.type = 1;
      } else if (var2.equals("ANY")) {
         var5.type = 0;
      } else if (var2.startsWith("(")) {
         if (var2.indexOf("#PCDATA") > 0) {
            var5.type = 2;
         } else {
            var5.type = 3;
         }
      }

      this.fElementDeclTab.put(var1, var5);
      this.fElementDecl = var5;
      int var6;
      if ((this.fDepth == 0 || this.fDepth == 1 && var5.type == 2) && this.fNodeIndexStack != null) {
         if (var5.type == 2) {
            var6 = this.addUniqueLeafNode((String)null);
            if (this.fNodeIndexStack[0] == -1) {
               this.fNodeIndexStack[0] = var6;
            } else {
               this.fNodeIndexStack[0] = this.addContentSpecNode((short)4, var6, this.fNodeIndexStack[0]);
            }
         }

         this.setContentSpecIndex(this.fCurrentElementIndex, this.fNodeIndexStack[this.fDepth]);
      }

      this.setElementDecl(this.fCurrentElementIndex, this.fElementDecl);
      var6 = this.fCurrentElementIndex >> 8;
      int var7 = this.fCurrentElementIndex & 255;
      this.ensureElementDeclCapacity(var6);
      this.fElementDeclIsExternal[var6][var7] = this.fReadingExternalDTD ? 1 : 0;
   }

   public void attributeDecl(String var1, String var2, String var3, String[] var4, String var5, XMLString var6, XMLString var7, Augmentations var8) throws XNIException {
      if (!this.fElementDeclTab.containsKey(var1)) {
         this.fCurrentElementIndex = this.createElementDecl();
         XMLElementDecl var9 = new XMLElementDecl();
         var9.name.setValues((String)null, var1, var1, (String)null);
         var9.scope = -1;
         this.fElementDeclTab.put(var1, var9);
         this.setElementDecl(this.fCurrentElementIndex, var9);
      }

      int var12 = this.getElementDeclIndex(var1);
      if (this.getAttributeDeclIndex(var12, var2) == -1) {
         this.fCurrentAttributeIndex = this.createAttributeDecl();
         this.fSimpleType.clear();
         if (var5 != null) {
            if (var5.equals("#FIXED")) {
               this.fSimpleType.defaultType = 1;
            } else if (var5.equals("#IMPLIED")) {
               this.fSimpleType.defaultType = 0;
            } else if (var5.equals("#REQUIRED")) {
               this.fSimpleType.defaultType = 2;
            }
         }

         this.fSimpleType.defaultValue = var6 != null ? var6.toString() : null;
         this.fSimpleType.nonNormalizedDefaultValue = var7 != null ? var7.toString() : null;
         this.fSimpleType.enumeration = var4;
         if (var3.equals("CDATA")) {
            this.fSimpleType.type = 0;
         } else if (var3.equals("ID")) {
            this.fSimpleType.type = 3;
         } else if (var3.startsWith("IDREF")) {
            this.fSimpleType.type = 4;
            if (var3.indexOf("S") > 0) {
               this.fSimpleType.list = true;
            }
         } else if (var3.equals("ENTITIES")) {
            this.fSimpleType.type = 1;
            this.fSimpleType.list = true;
         } else if (var3.equals("ENTITY")) {
            this.fSimpleType.type = 1;
         } else if (var3.equals("NMTOKENS")) {
            this.fSimpleType.type = 5;
            this.fSimpleType.list = true;
         } else if (var3.equals("NMTOKEN")) {
            this.fSimpleType.type = 5;
         } else if (var3.startsWith("NOTATION")) {
            this.fSimpleType.type = 6;
         } else if (var3.startsWith("ENUMERATION")) {
            this.fSimpleType.type = 2;
         } else {
            System.err.println("!!! unknown attribute type " + var3);
         }

         this.fQName.setValues((String)null, var2, var2, (String)null);
         this.fAttributeDecl.setValues(this.fQName, this.fSimpleType, false);
         this.setAttributeDecl(var12, this.fCurrentAttributeIndex, this.fAttributeDecl);
         int var10 = this.fCurrentAttributeIndex >> 8;
         int var11 = this.fCurrentAttributeIndex & 255;
         this.ensureAttributeDeclCapacity(var10);
         this.fAttributeDeclIsExternal[var10][var11] = this.fReadingExternalDTD ? 1 : 0;
      }
   }

   public void internalEntityDecl(String var1, XMLString var2, XMLString var3, Augmentations var4) throws XNIException {
      int var5 = this.getEntityDeclIndex(var1);
      if (var5 == -1) {
         var5 = this.createEntityDecl();
         boolean var6 = var1.startsWith("%");
         boolean var7 = this.fReadingExternalDTD;
         XMLEntityDecl var8 = new XMLEntityDecl();
         var8.setValues(var1, (String)null, (String)null, (String)null, (String)null, var2.toString(), var6, var7);
         this.setEntityDecl(var5, var8);
      }

   }

   public void externalEntityDecl(String var1, XMLResourceIdentifier var2, Augmentations var3) throws XNIException {
      int var4 = this.getEntityDeclIndex(var1);
      if (var4 == -1) {
         var4 = this.createEntityDecl();
         boolean var5 = var1.startsWith("%");
         boolean var6 = this.fReadingExternalDTD;
         XMLEntityDecl var7 = new XMLEntityDecl();
         var7.setValues(var1, var2.getPublicId(), var2.getLiteralSystemId(), var2.getBaseSystemId(), (String)null, (String)null, var5, var6);
         this.setEntityDecl(var4, var7);
      }

   }

   public void unparsedEntityDecl(String var1, XMLResourceIdentifier var2, String var3, Augmentations var4) throws XNIException {
      XMLEntityDecl var5 = new XMLEntityDecl();
      boolean var6 = var1.startsWith("%");
      boolean var7 = this.fReadingExternalDTD;
      var5.setValues(var1, var2.getPublicId(), var2.getLiteralSystemId(), var2.getBaseSystemId(), var3, (String)null, var6, var7);
      int var8 = this.getEntityDeclIndex(var1);
      if (var8 == -1) {
         var8 = this.createEntityDecl();
         this.setEntityDecl(var8, var5);
      }

   }

   public void notationDecl(String var1, XMLResourceIdentifier var2, Augmentations var3) throws XNIException {
      XMLNotationDecl var4 = new XMLNotationDecl();
      var4.setValues(var1, var2.getPublicId(), var2.getLiteralSystemId(), var2.getBaseSystemId());
      int var5 = this.getNotationDeclIndex(var1);
      if (var5 == -1) {
         var5 = this.createNotationDecl();
         this.setNotationDecl(var5, var4);
      }

   }

   public void endDTD(Augmentations var1) throws XNIException {
      this.fIsImmutable = true;
      if (this.fGrammarDescription.getRootName() == null) {
         boolean var3 = false;
         String var4 = null;
         Vector var5 = new Vector();

         for(int var6 = 0; var6 < this.fElementDeclCount; ++var6) {
            int var2 = var6 >> 8;
            int var7 = var6 & 255;
            var4 = this.fElementDeclName[var2][var7].rawname;
            var5.addElement(var4);
         }

         this.fGrammarDescription.setPossibleRoots(var5);
      }

   }

   public void setDTDSource(XMLDTDSource var1) {
      this.fDTDSource = var1;
   }

   public XMLDTDSource getDTDSource() {
      return this.fDTDSource;
   }

   public void textDecl(String var1, String var2, Augmentations var3) throws XNIException {
   }

   public void comment(XMLString var1, Augmentations var2) throws XNIException {
   }

   public void processingInstruction(String var1, XMLString var2, Augmentations var3) throws XNIException {
   }

   public void startAttlist(String var1, Augmentations var2) throws XNIException {
   }

   public void endAttlist(Augmentations var1) throws XNIException {
   }

   public void startConditional(short var1, Augmentations var2) throws XNIException {
   }

   public void ignoredCharacters(XMLString var1, Augmentations var2) throws XNIException {
   }

   public void endConditional(Augmentations var1) throws XNIException {
   }

   public void setDTDContentModelSource(XMLDTDContentModelSource var1) {
      this.fDTDContentModelSource = var1;
   }

   public XMLDTDContentModelSource getDTDContentModelSource() {
      return this.fDTDContentModelSource;
   }

   public void startContentModel(String var1, Augmentations var2) throws XNIException {
      XMLElementDecl var3 = (XMLElementDecl)this.fElementDeclTab.get(var1);
      if (var3 != null) {
         this.fElementDecl = var3;
      }

      this.fDepth = 0;
      this.initializeContentModelStack();
   }

   public void startGroup(Augmentations var1) throws XNIException {
      ++this.fDepth;
      this.initializeContentModelStack();
      this.fMixed = false;
   }

   public void pcdata(Augmentations var1) throws XNIException {
      this.fMixed = true;
   }

   public void element(String var1, Augmentations var2) throws XNIException {
      if (this.fMixed) {
         if (this.fNodeIndexStack[this.fDepth] == -1) {
            this.fNodeIndexStack[this.fDepth] = this.addUniqueLeafNode(var1);
         } else {
            this.fNodeIndexStack[this.fDepth] = this.addContentSpecNode((short)4, this.fNodeIndexStack[this.fDepth], this.addUniqueLeafNode(var1));
         }
      } else {
         this.fNodeIndexStack[this.fDepth] = this.addContentSpecNode((short)0, var1);
      }

   }

   public void separator(short var1, Augmentations var2) throws XNIException {
      if (!this.fMixed) {
         if (this.fOpStack[this.fDepth] != 5 && var1 == 0) {
            if (this.fPrevNodeIndexStack[this.fDepth] != -1) {
               this.fNodeIndexStack[this.fDepth] = this.addContentSpecNode(this.fOpStack[this.fDepth], this.fPrevNodeIndexStack[this.fDepth], this.fNodeIndexStack[this.fDepth]);
            }

            this.fPrevNodeIndexStack[this.fDepth] = this.fNodeIndexStack[this.fDepth];
            this.fOpStack[this.fDepth] = 4;
         } else if (this.fOpStack[this.fDepth] != 4 && var1 == 1) {
            if (this.fPrevNodeIndexStack[this.fDepth] != -1) {
               this.fNodeIndexStack[this.fDepth] = this.addContentSpecNode(this.fOpStack[this.fDepth], this.fPrevNodeIndexStack[this.fDepth], this.fNodeIndexStack[this.fDepth]);
            }

            this.fPrevNodeIndexStack[this.fDepth] = this.fNodeIndexStack[this.fDepth];
            this.fOpStack[this.fDepth] = 5;
         }
      }

   }

   public void occurrence(short var1, Augmentations var2) throws XNIException {
      if (!this.fMixed) {
         if (var1 == 2) {
            this.fNodeIndexStack[this.fDepth] = this.addContentSpecNode((short)1, this.fNodeIndexStack[this.fDepth], -1);
         } else if (var1 == 3) {
            this.fNodeIndexStack[this.fDepth] = this.addContentSpecNode((short)2, this.fNodeIndexStack[this.fDepth], -1);
         } else if (var1 == 4) {
            this.fNodeIndexStack[this.fDepth] = this.addContentSpecNode((short)3, this.fNodeIndexStack[this.fDepth], -1);
         }
      }

   }

   public void endGroup(Augmentations var1) throws XNIException {
      if (!this.fMixed) {
         if (this.fPrevNodeIndexStack[this.fDepth] != -1) {
            this.fNodeIndexStack[this.fDepth] = this.addContentSpecNode(this.fOpStack[this.fDepth], this.fPrevNodeIndexStack[this.fDepth], this.fNodeIndexStack[this.fDepth]);
         }

         int var2 = this.fNodeIndexStack[this.fDepth--];
         this.fNodeIndexStack[this.fDepth] = var2;
      }

   }

   public void any(Augmentations var1) throws XNIException {
   }

   public void empty(Augmentations var1) throws XNIException {
   }

   public void endContentModel(Augmentations var1) throws XNIException {
   }

   public boolean isNamespaceAware() {
      return false;
   }

   public SymbolTable getSymbolTable() {
      return this.fSymbolTable;
   }

   public int getFirstElementDeclIndex() {
      return this.fElementDeclCount >= 0 ? 0 : -1;
   }

   public int getNextElementDeclIndex(int var1) {
      return var1 < this.fElementDeclCount - 1 ? var1 + 1 : -1;
   }

   public int getElementDeclIndex(String var1) {
      int var2 = this.fElementIndexMap.get(var1);
      return var2;
   }

   public int getElementDeclIndex(QName var1) {
      return this.getElementDeclIndex(var1.rawname);
   }

   public short getContentSpecType(int var1) {
      if (var1 >= 0 && var1 < this.fElementDeclCount) {
         int var2 = var1 >> 8;
         int var3 = var1 & 255;
         return this.fElementDeclType[var2][var3] == -1 ? -1 : (short)(this.fElementDeclType[var2][var3] & -129);
      } else {
         return -1;
      }
   }

   public boolean getElementDecl(int var1, XMLElementDecl var2) {
      if (var1 >= 0 && var1 < this.fElementDeclCount) {
         int var3 = var1 >> 8;
         int var4 = var1 & 255;
         var2.name.setValues(this.fElementDeclName[var3][var4]);
         if (this.fElementDeclType[var3][var4] == -1) {
            var2.type = -1;
            var2.simpleType.list = false;
         } else {
            var2.type = (short)(this.fElementDeclType[var3][var4] & -129);
            var2.simpleType.list = (this.fElementDeclType[var3][var4] & 128) != 0;
         }

         if (var2.type == 3 || var2.type == 2) {
            var2.contentModelValidator = this.getElementContentModelValidator(var1);
         }

         var2.simpleType.datatypeValidator = null;
         var2.simpleType.defaultType = -1;
         var2.simpleType.defaultValue = null;
         return true;
      } else {
         return false;
      }
   }

   public int getFirstAttributeDeclIndex(int var1) {
      int var2 = var1 >> 8;
      int var3 = var1 & 255;
      return this.fElementDeclFirstAttributeDeclIndex[var2][var3];
   }

   public int getNextAttributeDeclIndex(int var1) {
      int var2 = var1 >> 8;
      int var3 = var1 & 255;
      return this.fAttributeDeclNextAttributeDeclIndex[var2][var3];
   }

   public boolean getAttributeDecl(int var1, XMLAttributeDecl var2) {
      if (var1 >= 0 && var1 < this.fAttributeDeclCount) {
         int var3 = var1 >> 8;
         int var4 = var1 & 255;
         var2.name.setValues(this.fAttributeDeclName[var3][var4]);
         short var5;
         boolean var6;
         if (this.fAttributeDeclType[var3][var4] == -1) {
            var5 = -1;
            var6 = false;
         } else {
            var5 = (short)(this.fAttributeDeclType[var3][var4] & -129);
            var6 = (this.fAttributeDeclType[var3][var4] & 128) != 0;
         }

         var2.simpleType.setValues(var5, this.fAttributeDeclName[var3][var4].localpart, this.fAttributeDeclEnumeration[var3][var4], var6, this.fAttributeDeclDefaultType[var3][var4], this.fAttributeDeclDefaultValue[var3][var4], this.fAttributeDeclNonNormalizedDefaultValue[var3][var4], this.fAttributeDeclDatatypeValidator[var3][var4]);
         return true;
      } else {
         return false;
      }
   }

   public boolean isCDATAAttribute(QName var1, QName var2) {
      int var3 = this.getElementDeclIndex(var1);
      return !this.getAttributeDecl(var3, this.fAttributeDecl) || this.fAttributeDecl.simpleType.type == 0;
   }

   public int getEntityDeclIndex(String var1) {
      return var1 == null ? -1 : this.fEntityIndexMap.get(var1);
   }

   public boolean getEntityDecl(int var1, XMLEntityDecl var2) {
      if (var1 >= 0 && var1 < this.fEntityCount) {
         int var3 = var1 >> 8;
         int var4 = var1 & 255;
         var2.setValues(this.fEntityName[var3][var4], this.fEntityPublicId[var3][var4], this.fEntitySystemId[var3][var4], this.fEntityBaseSystemId[var3][var4], this.fEntityNotation[var3][var4], this.fEntityValue[var3][var4], this.fEntityIsPE[var3][var4] != 0, this.fEntityInExternal[var3][var4] != 0);
         return true;
      } else {
         return false;
      }
   }

   public int getNotationDeclIndex(String var1) {
      return var1 == null ? -1 : this.fNotationIndexMap.get(var1);
   }

   public boolean getNotationDecl(int var1, XMLNotationDecl var2) {
      if (var1 >= 0 && var1 < this.fNotationCount) {
         int var3 = var1 >> 8;
         int var4 = var1 & 255;
         var2.setValues(this.fNotationName[var3][var4], this.fNotationPublicId[var3][var4], this.fNotationSystemId[var3][var4], this.fNotationBaseSystemId[var3][var4]);
         return true;
      } else {
         return false;
      }
   }

   public boolean getContentSpec(int var1, XMLContentSpec var2) {
      if (var1 >= 0 && var1 < this.fContentSpecCount) {
         int var3 = var1 >> 8;
         int var4 = var1 & 255;
         var2.type = this.fContentSpecType[var3][var4];
         var2.value = this.fContentSpecValue[var3][var4];
         var2.otherValue = this.fContentSpecOtherValue[var3][var4];
         return true;
      } else {
         return false;
      }
   }

   public String getContentSpecAsString(int var1) {
      if (var1 >= 0 && var1 < this.fElementDeclCount) {
         int var2 = var1 >> 8;
         int var3 = var1 & 255;
         int var4 = this.fElementDeclContentSpecIndex[var2][var3];
         XMLContentSpec var5 = new XMLContentSpec();
         if (!this.getContentSpec(var4, var5)) {
            return null;
         } else {
            StringBuffer var6 = new StringBuffer();
            int var7 = var5.type & 15;
            short var8;
            switch (var7) {
               case 0:
                  var6.append('(');
                  if (var5.value == null && var5.otherValue == null) {
                     var6.append("#PCDATA");
                  } else {
                     var6.append(var5.value);
                  }

                  var6.append(')');
                  break;
               case 1:
                  this.getContentSpec(((int[])var5.value)[0], var5);
                  var8 = var5.type;
                  if (var8 == 0) {
                     var6.append('(');
                     var6.append(var5.value);
                     var6.append(')');
                  } else if (var8 != 3 && var8 != 2 && var8 != 1) {
                     this.appendContentSpec(var5, var6, true, var7);
                  } else {
                     var6.append('(');
                     this.appendContentSpec(var5, var6, true, var7);
                     var6.append(')');
                  }

                  var6.append('?');
                  break;
               case 2:
                  this.getContentSpec(((int[])var5.value)[0], var5);
                  var8 = var5.type;
                  if (var8 != 0) {
                     if (var8 != 3 && var8 != 2 && var8 != 1) {
                        this.appendContentSpec(var5, var6, true, var7);
                     } else {
                        var6.append('(');
                        this.appendContentSpec(var5, var6, true, var7);
                        var6.append(')');
                     }
                  } else {
                     var6.append('(');
                     if (var5.value == null && var5.otherValue == null) {
                        var6.append("#PCDATA");
                     } else if (var5.otherValue != null) {
                        var6.append("##any:uri=" + var5.otherValue);
                     } else if (var5.value == null) {
                        var6.append("##any");
                     } else {
                        this.appendContentSpec(var5, var6, true, var7);
                     }

                     var6.append(')');
                  }

                  var6.append('*');
                  break;
               case 3:
                  this.getContentSpec(((int[])var5.value)[0], var5);
                  var8 = var5.type;
                  if (var8 != 0) {
                     if (var8 != 3 && var8 != 2 && var8 != 1) {
                        this.appendContentSpec(var5, var6, true, var7);
                     } else {
                        var6.append('(');
                        this.appendContentSpec(var5, var6, true, var7);
                        var6.append(')');
                     }
                  } else {
                     var6.append('(');
                     if (var5.value == null && var5.otherValue == null) {
                        var6.append("#PCDATA");
                     } else if (var5.otherValue != null) {
                        var6.append("##any:uri=" + var5.otherValue);
                     } else if (var5.value == null) {
                        var6.append("##any");
                     } else {
                        var6.append(var5.value);
                     }

                     var6.append(')');
                  }

                  var6.append('+');
                  break;
               case 4:
               case 5:
                  this.appendContentSpec(var5, var6, true, var7);
                  break;
               case 6:
                  var6.append("##any");
                  if (var5.otherValue != null) {
                     var6.append(":uri=");
                     var6.append(var5.otherValue);
                  }
                  break;
               case 7:
                  var6.append("##other:uri=");
                  var6.append(var5.otherValue);
                  break;
               case 8:
                  var6.append("##local");
                  break;
               default:
                  var6.append("???");
            }

            return var6.toString();
         }
      } else {
         return null;
      }
   }

   public void printElements() {
      int var1 = 0;
      XMLElementDecl var2 = new XMLElementDecl();

      while(this.getElementDecl(var1++, var2)) {
         System.out.println("element decl: " + var2.name + ", " + var2.name.rawname);
      }

   }

   public void printAttributes(int var1) {
      int var2 = this.getFirstAttributeDeclIndex(var1);
      System.out.print(var1);
      System.out.print(" [");

      while(var2 != -1) {
         System.out.print(' ');
         System.out.print(var2);
         this.printAttribute(var2);
         var2 = this.getNextAttributeDeclIndex(var2);
         if (var2 != -1) {
            System.out.print(",");
         }
      }

      System.out.println(" ]");
   }

   protected ContentModelValidator getElementContentModelValidator(int var1) {
      int var2 = var1 >> 8;
      int var3 = var1 & 255;
      ContentModelValidator var4 = this.fElementDeclContentModelValidator[var2][var3];
      if (var4 != null) {
         return var4;
      } else {
         short var5 = this.fElementDeclType[var2][var3];
         if (var5 == 4) {
            return null;
         } else {
            int var6 = this.fElementDeclContentSpecIndex[var2][var3];
            XMLContentSpec var7 = new XMLContentSpec();
            this.getContentSpec(var6, var7);
            Object var9;
            if (var5 == 2) {
               ChildrenList var8 = new ChildrenList();
               this.contentSpecTree(var6, var7, var8);
               var9 = new MixedContentModel(var8.qname, var8.type, 0, var8.length, false);
            } else {
               if (var5 != 3) {
                  throw new RuntimeException("Unknown content type for a element decl in getElementContentModelValidator() in AbstractDTDGrammar class");
               }

               var9 = this.createChildModel(var6);
            }

            this.fElementDeclContentModelValidator[var2][var3] = (ContentModelValidator)var9;
            return (ContentModelValidator)var9;
         }
      }
   }

   protected int createElementDecl() {
      int var1 = this.fElementDeclCount >> 8;
      int var2 = this.fElementDeclCount & 255;
      this.ensureElementDeclCapacity(var1);
      this.fElementDeclName[var1][var2] = new QName();
      this.fElementDeclType[var1][var2] = -1;
      this.fElementDeclContentModelValidator[var1][var2] = null;
      this.fElementDeclFirstAttributeDeclIndex[var1][var2] = -1;
      this.fElementDeclLastAttributeDeclIndex[var1][var2] = -1;
      return this.fElementDeclCount++;
   }

   protected void setElementDecl(int var1, XMLElementDecl var2) {
      if (var1 >= 0 && var1 < this.fElementDeclCount) {
         int var3 = var1 >> 8;
         int var4 = var1 & 255;
         this.fElementDeclName[var3][var4].setValues(var2.name);
         this.fElementDeclType[var3][var4] = var2.type;
         this.fElementDeclContentModelValidator[var3][var4] = var2.contentModelValidator;
         if (var2.simpleType.list) {
            short[] var10000 = this.fElementDeclType[var3];
            var10000[var4] = (short)(var10000[var4] | 128);
         }

         this.fElementIndexMap.put(var2.name.rawname, var1);
      }
   }

   protected void putElementNameMapping(QName var1, int var2, int var3) {
   }

   protected void setFirstAttributeDeclIndex(int var1, int var2) {
      if (var1 >= 0 && var1 < this.fElementDeclCount) {
         int var3 = var1 >> 8;
         int var4 = var1 & 255;
         this.fElementDeclFirstAttributeDeclIndex[var3][var4] = var2;
      }
   }

   protected void setContentSpecIndex(int var1, int var2) {
      if (var1 >= 0 && var1 < this.fElementDeclCount) {
         int var3 = var1 >> 8;
         int var4 = var1 & 255;
         this.fElementDeclContentSpecIndex[var3][var4] = var2;
      }
   }

   protected int createAttributeDecl() {
      int var1 = this.fAttributeDeclCount >> 8;
      int var2 = this.fAttributeDeclCount & 255;
      this.ensureAttributeDeclCapacity(var1);
      this.fAttributeDeclName[var1][var2] = new QName();
      this.fAttributeDeclType[var1][var2] = -1;
      this.fAttributeDeclDatatypeValidator[var1][var2] = null;
      this.fAttributeDeclEnumeration[var1][var2] = null;
      this.fAttributeDeclDefaultType[var1][var2] = 0;
      this.fAttributeDeclDefaultValue[var1][var2] = null;
      this.fAttributeDeclNonNormalizedDefaultValue[var1][var2] = null;
      this.fAttributeDeclNextAttributeDeclIndex[var1][var2] = -1;
      return this.fAttributeDeclCount++;
   }

   protected void setAttributeDecl(int var1, int var2, XMLAttributeDecl var3) {
      int var4 = var2 >> 8;
      int var5 = var2 & 255;
      this.fAttributeDeclName[var4][var5].setValues(var3.name);
      this.fAttributeDeclType[var4][var5] = var3.simpleType.type;
      if (var3.simpleType.list) {
         short[] var10000 = this.fAttributeDeclType[var4];
         var10000[var5] = (short)(var10000[var5] | 128);
      }

      this.fAttributeDeclEnumeration[var4][var5] = var3.simpleType.enumeration;
      this.fAttributeDeclDefaultType[var4][var5] = var3.simpleType.defaultType;
      this.fAttributeDeclDatatypeValidator[var4][var5] = var3.simpleType.datatypeValidator;
      this.fAttributeDeclDefaultValue[var4][var5] = var3.simpleType.defaultValue;
      this.fAttributeDeclNonNormalizedDefaultValue[var4][var5] = var3.simpleType.nonNormalizedDefaultValue;
      int var6 = var1 >> 8;
      int var7 = var1 & 255;

      int var8;
      for(var8 = this.fElementDeclFirstAttributeDeclIndex[var6][var7]; var8 != -1 && var8 != var2; var8 = this.fAttributeDeclNextAttributeDeclIndex[var4][var5]) {
         var4 = var8 >> 8;
         var5 = var8 & 255;
      }

      if (var8 == -1) {
         if (this.fElementDeclFirstAttributeDeclIndex[var6][var7] == -1) {
            this.fElementDeclFirstAttributeDeclIndex[var6][var7] = var2;
         } else {
            var8 = this.fElementDeclLastAttributeDeclIndex[var6][var7];
            var4 = var8 >> 8;
            var5 = var8 & 255;
            this.fAttributeDeclNextAttributeDeclIndex[var4][var5] = var2;
         }

         this.fElementDeclLastAttributeDeclIndex[var6][var7] = var2;
      }

   }

   protected int createContentSpec() {
      int var1 = this.fContentSpecCount >> 8;
      int var2 = this.fContentSpecCount & 255;
      this.ensureContentSpecCapacity(var1);
      this.fContentSpecType[var1][var2] = -1;
      this.fContentSpecValue[var1][var2] = null;
      this.fContentSpecOtherValue[var1][var2] = null;
      return this.fContentSpecCount++;
   }

   protected void setContentSpec(int var1, XMLContentSpec var2) {
      int var3 = var1 >> 8;
      int var4 = var1 & 255;
      this.fContentSpecType[var3][var4] = var2.type;
      this.fContentSpecValue[var3][var4] = var2.value;
      this.fContentSpecOtherValue[var3][var4] = var2.otherValue;
   }

   protected int createEntityDecl() {
      int var1 = this.fEntityCount >> 8;
      int var2 = this.fEntityCount & 255;
      this.ensureEntityDeclCapacity(var1);
      this.fEntityIsPE[var1][var2] = 0;
      this.fEntityInExternal[var1][var2] = 0;
      return this.fEntityCount++;
   }

   protected void setEntityDecl(int var1, XMLEntityDecl var2) {
      int var3 = var1 >> 8;
      int var4 = var1 & 255;
      this.fEntityName[var3][var4] = var2.name;
      this.fEntityValue[var3][var4] = var2.value;
      this.fEntityPublicId[var3][var4] = var2.publicId;
      this.fEntitySystemId[var3][var4] = var2.systemId;
      this.fEntityBaseSystemId[var3][var4] = var2.baseSystemId;
      this.fEntityNotation[var3][var4] = var2.notation;
      this.fEntityIsPE[var3][var4] = (byte)(var2.isPE ? 1 : 0);
      this.fEntityInExternal[var3][var4] = (byte)(var2.inExternal ? 1 : 0);
      this.fEntityIndexMap.put(var2.name, var1);
   }

   protected int createNotationDecl() {
      int var1 = this.fNotationCount >> 8;
      this.ensureNotationDeclCapacity(var1);
      return this.fNotationCount++;
   }

   protected void setNotationDecl(int var1, XMLNotationDecl var2) {
      int var3 = var1 >> 8;
      int var4 = var1 & 255;
      this.fNotationName[var3][var4] = var2.name;
      this.fNotationPublicId[var3][var4] = var2.publicId;
      this.fNotationSystemId[var3][var4] = var2.systemId;
      this.fNotationBaseSystemId[var3][var4] = var2.baseSystemId;
      this.fNotationIndexMap.put(var2.name, var1);
   }

   protected int addContentSpecNode(short var1, String var2) {
      int var3 = this.createContentSpec();
      this.fContentSpec.setValues(var1, var2, (Object)null);
      this.setContentSpec(var3, this.fContentSpec);
      return var3;
   }

   protected int addUniqueLeafNode(String var1) {
      int var2 = this.createContentSpec();
      this.fContentSpec.setValues((short)0, var1, (Object)null);
      this.setContentSpec(var2, this.fContentSpec);
      return var2;
   }

   protected int addContentSpecNode(short var1, int var2, int var3) {
      int var4 = this.createContentSpec();
      int[] var5 = new int[1];
      int[] var6 = new int[1];
      var5[0] = var2;
      var6[0] = var3;
      this.fContentSpec.setValues(var1, var5, var6);
      this.setContentSpec(var4, this.fContentSpec);
      return var4;
   }

   protected void initializeContentModelStack() {
      if (this.fOpStack == null) {
         this.fOpStack = new short[8];
         this.fNodeIndexStack = new int[8];
         this.fPrevNodeIndexStack = new int[8];
      } else if (this.fDepth == this.fOpStack.length) {
         short[] var1 = new short[this.fDepth * 2];
         System.arraycopy(this.fOpStack, 0, var1, 0, this.fDepth);
         this.fOpStack = var1;
         int[] var2 = new int[this.fDepth * 2];
         System.arraycopy(this.fNodeIndexStack, 0, var2, 0, this.fDepth);
         this.fNodeIndexStack = var2;
         var2 = new int[this.fDepth * 2];
         System.arraycopy(this.fPrevNodeIndexStack, 0, var2, 0, this.fDepth);
         this.fPrevNodeIndexStack = var2;
      }

      this.fOpStack[this.fDepth] = -1;
      this.fNodeIndexStack[this.fDepth] = -1;
      this.fPrevNodeIndexStack[this.fDepth] = -1;
   }

   boolean isImmutable() {
      return this.fIsImmutable;
   }

   private void appendContentSpec(XMLContentSpec var1, StringBuffer var2, boolean var3, int var4) {
      int var5 = var1.type & 15;
      switch (var5) {
         case 0:
            if (var1.value == null && var1.otherValue == null) {
               var2.append("#PCDATA");
            } else if (var1.value == null && var1.otherValue != null) {
               var2.append("##any:uri=" + var1.otherValue);
            } else if (var1.value == null) {
               var2.append("##any");
            } else {
               var2.append(var1.value);
            }
            break;
         case 1:
            if (var4 != 3 && var4 != 2 && var4 != 1) {
               this.getContentSpec(((int[])var1.value)[0], var1);
               this.appendContentSpec(var1, var2, true, var5);
            } else {
               this.getContentSpec(((int[])var1.value)[0], var1);
               var2.append('(');
               this.appendContentSpec(var1, var2, true, var5);
               var2.append(')');
            }

            var2.append('?');
            break;
         case 2:
            if (var4 != 3 && var4 != 2 && var4 != 1) {
               this.getContentSpec(((int[])var1.value)[0], var1);
               this.appendContentSpec(var1, var2, true, var5);
            } else {
               this.getContentSpec(((int[])var1.value)[0], var1);
               var2.append('(');
               this.appendContentSpec(var1, var2, true, var5);
               var2.append(')');
            }

            var2.append('*');
            break;
         case 3:
            if (var4 != 3 && var4 != 2 && var4 != 1) {
               this.getContentSpec(((int[])var1.value)[0], var1);
               this.appendContentSpec(var1, var2, true, var5);
            } else {
               var2.append('(');
               this.getContentSpec(((int[])var1.value)[0], var1);
               this.appendContentSpec(var1, var2, true, var5);
               var2.append(')');
            }

            var2.append('+');
            break;
         case 4:
         case 5:
            if (var3) {
               var2.append('(');
            }

            short var6 = var1.type;
            int var7 = ((int[])var1.otherValue)[0];
            this.getContentSpec(((int[])var1.value)[0], var1);
            this.appendContentSpec(var1, var2, var1.type != var6, var5);
            if (var6 == 4) {
               var2.append('|');
            } else {
               var2.append(',');
            }

            this.getContentSpec(var7, var1);
            this.appendContentSpec(var1, var2, true, var5);
            if (var3) {
               var2.append(')');
            }
            break;
         case 6:
            var2.append("##any");
            if (var1.otherValue != null) {
               var2.append(":uri=");
               var2.append(var1.otherValue);
            }
            break;
         case 7:
            var2.append("##other:uri=");
            var2.append(var1.otherValue);
            break;
         case 8:
            var2.append("##local");
            break;
         default:
            var2.append("???");
      }

   }

   private void printAttribute(int var1) {
      XMLAttributeDecl var2 = new XMLAttributeDecl();
      if (this.getAttributeDecl(var1, var2)) {
         System.out.print(" { ");
         System.out.print(var2.name.localpart);
         System.out.print(" }");
      }

   }

   private ContentModelValidator createChildModel(int var1) {
      XMLContentSpec var2 = new XMLContentSpec();
      this.getContentSpec(var1, var2);
      if ((var2.type & 15) != 6 && (var2.type & 15) != 7 && (var2.type & 15) != 8) {
         if (var2.type == 0) {
            if (var2.value == null && var2.otherValue == null) {
               throw new RuntimeException("ImplementationMessages.VAL_NPCD");
            }

            this.fQName.setValues((String)null, (String)var2.value, (String)var2.value, (String)var2.otherValue);
            return new SimpleContentModel(var2.type, this.fQName, (QName)null);
         }

         XMLContentSpec var3;
         if (var2.type != 4 && var2.type != 5) {
            if (var2.type != 1 && var2.type != 2 && var2.type != 3) {
               throw new RuntimeException("ImplementationMessages.VAL_CST");
            }

            var3 = new XMLContentSpec();
            this.getContentSpec(((int[])var2.value)[0], var3);
            if (var3.type == 0) {
               this.fQName.setValues((String)null, (String)var3.value, (String)var3.value, (String)var3.otherValue);
               return new SimpleContentModel(var2.type, this.fQName, (QName)null);
            }
         } else {
            var3 = new XMLContentSpec();
            XMLContentSpec var4 = new XMLContentSpec();
            this.getContentSpec(((int[])var2.value)[0], var3);
            this.getContentSpec(((int[])var2.otherValue)[0], var4);
            if (var3.type == 0 && var4.type == 0) {
               this.fQName.setValues((String)null, (String)var3.value, (String)var3.value, (String)var3.otherValue);
               this.fQName2.setValues((String)null, (String)var4.value, (String)var4.value, (String)var4.otherValue);
               return new SimpleContentModel(var2.type, this.fQName, this.fQName2);
            }
         }
      }

      this.fLeafCount = 0;
      this.fLeafCount = 0;
      CMNode var5 = this.buildSyntaxTree(var1, var2);
      return new DFAContentModel(var5, this.fLeafCount, false);
   }

   private final CMNode buildSyntaxTree(int var1, XMLContentSpec var2) {
      Object var3 = null;
      this.getContentSpec(var1, var2);
      if ((var2.type & 15) == 6) {
         var3 = new CMAny(var2.type, (String)var2.otherValue, this.fLeafCount++);
      } else if ((var2.type & 15) == 7) {
         var3 = new CMAny(var2.type, (String)var2.otherValue, this.fLeafCount++);
      } else if ((var2.type & 15) == 8) {
         var3 = new CMAny(var2.type, (String)null, this.fLeafCount++);
      } else if (var2.type == 0) {
         this.fQName.setValues((String)null, (String)var2.value, (String)var2.value, (String)var2.otherValue);
         var3 = new CMLeaf(this.fQName, this.fLeafCount++);
      } else {
         int var4 = ((int[])var2.value)[0];
         int var5 = ((int[])var2.otherValue)[0];
         if (var2.type != 4 && var2.type != 5) {
            if (var2.type == 2) {
               var3 = new CMUniOp(var2.type, this.buildSyntaxTree(var4, var2));
            } else {
               if (var2.type != 2 && var2.type != 1 && var2.type != 3) {
                  throw new RuntimeException("ImplementationMessages.VAL_CST");
               }

               var3 = new CMUniOp(var2.type, this.buildSyntaxTree(var4, var2));
            }
         } else {
            var3 = new CMBinOp(var2.type, this.buildSyntaxTree(var4, var2), this.buildSyntaxTree(var5, var2));
         }
      }

      return (CMNode)var3;
   }

   private void contentSpecTree(int var1, XMLContentSpec var2, ChildrenList var3) {
      this.getContentSpec(var1, var2);
      if (var2.type != 0 && (var2.type & 15) != 6 && (var2.type & 15) != 8 && (var2.type & 15) != 7) {
         int var6 = var2.value != null ? ((int[])var2.value)[0] : -1;
         boolean var7 = true;
         if (var2.otherValue != null) {
            int var8 = ((int[])var2.otherValue)[0];
            if (var2.type != 4 && var2.type != 5) {
               if (var2.type != 1 && var2.type != 2 && var2.type != 3) {
                  throw new RuntimeException("Invalid content spec type seen in contentSpecTree() method of AbstractDTDGrammar class : " + var2.type);
               } else {
                  this.contentSpecTree(var6, var2, var3);
               }
            } else {
               this.contentSpecTree(var6, var2, var3);
               this.contentSpecTree(var8, var2, var3);
            }
         }
      } else {
         if (var3.length == var3.qname.length) {
            QName[] var4 = new QName[var3.length * 2];
            System.arraycopy(var3.qname, 0, var4, 0, var3.length);
            var3.qname = var4;
            int[] var5 = new int[var3.length * 2];
            System.arraycopy(var3.type, 0, var5, 0, var3.length);
            var3.type = var5;
         }

         var3.qname[var3.length] = new QName((String)null, (String)var2.value, (String)var2.value, (String)var2.otherValue);
         var3.type[var3.length] = var2.type;
         ++var3.length;
      }
   }

   private void ensureElementDeclCapacity(int var1) {
      if (var1 >= this.fElementDeclName.length) {
         this.fElementDeclIsExternal = resize(this.fElementDeclIsExternal, this.fElementDeclIsExternal.length * 2);
         this.fElementDeclName = resize(this.fElementDeclName, this.fElementDeclName.length * 2);
         this.fElementDeclType = resize(this.fElementDeclType, this.fElementDeclType.length * 2);
         this.fElementDeclContentModelValidator = resize(this.fElementDeclContentModelValidator, this.fElementDeclContentModelValidator.length * 2);
         this.fElementDeclContentSpecIndex = resize(this.fElementDeclContentSpecIndex, this.fElementDeclContentSpecIndex.length * 2);
         this.fElementDeclFirstAttributeDeclIndex = resize(this.fElementDeclFirstAttributeDeclIndex, this.fElementDeclFirstAttributeDeclIndex.length * 2);
         this.fElementDeclLastAttributeDeclIndex = resize(this.fElementDeclLastAttributeDeclIndex, this.fElementDeclLastAttributeDeclIndex.length * 2);
      } else if (this.fElementDeclName[var1] != null) {
         return;
      }

      this.fElementDeclIsExternal[var1] = new int[256];
      this.fElementDeclName[var1] = new QName[256];
      this.fElementDeclType[var1] = new short[256];
      this.fElementDeclContentModelValidator[var1] = new ContentModelValidator[256];
      this.fElementDeclContentSpecIndex[var1] = new int[256];
      this.fElementDeclFirstAttributeDeclIndex[var1] = new int[256];
      this.fElementDeclLastAttributeDeclIndex[var1] = new int[256];
   }

   private void ensureAttributeDeclCapacity(int var1) {
      if (var1 >= this.fAttributeDeclName.length) {
         this.fAttributeDeclIsExternal = resize(this.fAttributeDeclIsExternal, this.fAttributeDeclIsExternal.length * 2);
         this.fAttributeDeclName = resize(this.fAttributeDeclName, this.fAttributeDeclName.length * 2);
         this.fAttributeDeclType = resize(this.fAttributeDeclType, this.fAttributeDeclType.length * 2);
         this.fAttributeDeclEnumeration = resize(this.fAttributeDeclEnumeration, this.fAttributeDeclEnumeration.length * 2);
         this.fAttributeDeclDefaultType = resize(this.fAttributeDeclDefaultType, this.fAttributeDeclDefaultType.length * 2);
         this.fAttributeDeclDatatypeValidator = resize(this.fAttributeDeclDatatypeValidator, this.fAttributeDeclDatatypeValidator.length * 2);
         this.fAttributeDeclDefaultValue = resize(this.fAttributeDeclDefaultValue, this.fAttributeDeclDefaultValue.length * 2);
         this.fAttributeDeclNonNormalizedDefaultValue = resize(this.fAttributeDeclNonNormalizedDefaultValue, this.fAttributeDeclNonNormalizedDefaultValue.length * 2);
         this.fAttributeDeclNextAttributeDeclIndex = resize(this.fAttributeDeclNextAttributeDeclIndex, this.fAttributeDeclNextAttributeDeclIndex.length * 2);
      } else if (this.fAttributeDeclName[var1] != null) {
         return;
      }

      this.fAttributeDeclIsExternal[var1] = new int[256];
      this.fAttributeDeclName[var1] = new QName[256];
      this.fAttributeDeclType[var1] = new short[256];
      this.fAttributeDeclEnumeration[var1] = new String[256][];
      this.fAttributeDeclDefaultType[var1] = new short[256];
      this.fAttributeDeclDatatypeValidator[var1] = new DatatypeValidator[256];
      this.fAttributeDeclDefaultValue[var1] = new String[256];
      this.fAttributeDeclNonNormalizedDefaultValue[var1] = new String[256];
      this.fAttributeDeclNextAttributeDeclIndex[var1] = new int[256];
   }

   private void ensureEntityDeclCapacity(int var1) {
      if (var1 >= this.fEntityName.length) {
         this.fEntityName = resize(this.fEntityName, this.fEntityName.length * 2);
         this.fEntityValue = resize(this.fEntityValue, this.fEntityValue.length * 2);
         this.fEntityPublicId = resize(this.fEntityPublicId, this.fEntityPublicId.length * 2);
         this.fEntitySystemId = resize(this.fEntitySystemId, this.fEntitySystemId.length * 2);
         this.fEntityBaseSystemId = resize(this.fEntityBaseSystemId, this.fEntityBaseSystemId.length * 2);
         this.fEntityNotation = resize(this.fEntityNotation, this.fEntityNotation.length * 2);
         this.fEntityIsPE = resize(this.fEntityIsPE, this.fEntityIsPE.length * 2);
         this.fEntityInExternal = resize(this.fEntityInExternal, this.fEntityInExternal.length * 2);
      } else if (this.fEntityName[var1] != null) {
         return;
      }

      this.fEntityName[var1] = new String[256];
      this.fEntityValue[var1] = new String[256];
      this.fEntityPublicId[var1] = new String[256];
      this.fEntitySystemId[var1] = new String[256];
      this.fEntityBaseSystemId[var1] = new String[256];
      this.fEntityNotation[var1] = new String[256];
      this.fEntityIsPE[var1] = new byte[256];
      this.fEntityInExternal[var1] = new byte[256];
   }

   private void ensureNotationDeclCapacity(int var1) {
      if (var1 >= this.fNotationName.length) {
         this.fNotationName = resize(this.fNotationName, this.fNotationName.length * 2);
         this.fNotationPublicId = resize(this.fNotationPublicId, this.fNotationPublicId.length * 2);
         this.fNotationSystemId = resize(this.fNotationSystemId, this.fNotationSystemId.length * 2);
         this.fNotationBaseSystemId = resize(this.fNotationBaseSystemId, this.fNotationBaseSystemId.length * 2);
      } else if (this.fNotationName[var1] != null) {
         return;
      }

      this.fNotationName[var1] = new String[256];
      this.fNotationPublicId[var1] = new String[256];
      this.fNotationSystemId[var1] = new String[256];
      this.fNotationBaseSystemId[var1] = new String[256];
   }

   private void ensureContentSpecCapacity(int var1) {
      if (var1 >= this.fContentSpecType.length) {
         this.fContentSpecType = resize(this.fContentSpecType, this.fContentSpecType.length * 2);
         this.fContentSpecValue = resize(this.fContentSpecValue, this.fContentSpecValue.length * 2);
         this.fContentSpecOtherValue = resize(this.fContentSpecOtherValue, this.fContentSpecOtherValue.length * 2);
      } else if (this.fContentSpecType[var1] != null) {
         return;
      }

      this.fContentSpecType[var1] = new short[256];
      this.fContentSpecValue[var1] = new Object[256];
      this.fContentSpecOtherValue[var1] = new Object[256];
   }

   private static byte[][] resize(byte[][] var0, int var1) {
      byte[][] var2 = new byte[var1][];
      System.arraycopy(var0, 0, var2, 0, var0.length);
      return var2;
   }

   private static short[][] resize(short[][] var0, int var1) {
      short[][] var2 = new short[var1][];
      System.arraycopy(var0, 0, var2, 0, var0.length);
      return var2;
   }

   private static int[][] resize(int[][] var0, int var1) {
      int[][] var2 = new int[var1][];
      System.arraycopy(var0, 0, var2, 0, var0.length);
      return var2;
   }

   private static DatatypeValidator[][] resize(DatatypeValidator[][] var0, int var1) {
      DatatypeValidator[][] var2 = new DatatypeValidator[var1][];
      System.arraycopy(var0, 0, var2, 0, var0.length);
      return var2;
   }

   private static ContentModelValidator[][] resize(ContentModelValidator[][] var0, int var1) {
      ContentModelValidator[][] var2 = new ContentModelValidator[var1][];
      System.arraycopy(var0, 0, var2, 0, var0.length);
      return var2;
   }

   private static Object[][] resize(Object[][] var0, int var1) {
      Object[][] var2 = new Object[var1][];
      System.arraycopy(var0, 0, var2, 0, var0.length);
      return var2;
   }

   private static QName[][] resize(QName[][] var0, int var1) {
      QName[][] var2 = new QName[var1][];
      System.arraycopy(var0, 0, var2, 0, var0.length);
      return var2;
   }

   private static String[][] resize(String[][] var0, int var1) {
      String[][] var2 = new String[var1][];
      System.arraycopy(var0, 0, var2, 0, var0.length);
      return var2;
   }

   private static String[][][] resize(String[][][] var0, int var1) {
      String[][][] var2 = new String[var1][][];
      System.arraycopy(var0, 0, var2, 0, var0.length);
      return var2;
   }

   public boolean isEntityDeclared(String var1) {
      return this.getEntityDeclIndex(var1) != -1;
   }

   public boolean isEntityUnparsed(String var1) {
      int var2 = this.getEntityDeclIndex(var1);
      if (var2 > -1) {
         int var3 = var2 >> 8;
         int var4 = var2 & 255;
         return this.fEntityNotation[var3][var4] != null;
      } else {
         return false;
      }
   }

   protected static final class QNameHashtable {
      private static final int INITIAL_BUCKET_SIZE = 4;
      private static final int HASHTABLE_SIZE = 101;
      private Object[][] fHashTable = new Object[101][];

      public void put(String var1, int var2) {
         int var3 = (var1.hashCode() & Integer.MAX_VALUE) % 101;
         Object[] var4 = this.fHashTable[var3];
         if (var4 == null) {
            var4 = new Object[9];
            var4[0] = new int[]{1};
            var4[1] = var1;
            var4[2] = new int[]{var2};
            this.fHashTable[var3] = var4;
         } else {
            int var5 = ((int[])var4[0])[0];
            int var6 = 1 + 2 * var5;
            if (var6 == var4.length) {
               int var7 = var5 + 4;
               Object[] var8 = new Object[1 + 2 * var7];
               System.arraycopy(var4, 0, var8, 0, var6);
               var4 = var8;
               this.fHashTable[var3] = var8;
            }

            boolean var10 = false;
            int var11 = 1;

            for(int var9 = 0; var9 < var5; ++var9) {
               if ((String)var4[var11] == var1) {
                  ((int[])var4[var11 + 1])[0] = var2;
                  var10 = true;
                  break;
               }

               var11 += 2;
            }

            if (!var10) {
               var4[var6++] = var1;
               var4[var6] = new int[]{var2};
               int[] var10000 = (int[])var4[0];
               ++var5;
               var10000[0] = var5;
            }
         }

      }

      public int get(String var1) {
         int var2 = (var1.hashCode() & Integer.MAX_VALUE) % 101;
         Object[] var3 = this.fHashTable[var2];
         if (var3 == null) {
            return -1;
         } else {
            int var4 = ((int[])var3[0])[0];
            int var5 = 1;

            for(int var6 = 0; var6 < var4; ++var6) {
               if ((String)var3[var5] == var1) {
                  return ((int[])var3[var5 + 1])[0];
               }

               var5 += 2;
            }

            return -1;
         }
      }
   }

   private static class ChildrenList {
      public int length = 0;
      public QName[] qname = new QName[2];
      public int[] type = new int[2];

      public ChildrenList() {
      }
   }
}
