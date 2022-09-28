package org.apache.xml.dtm.ref.sax2dtm;

import java.util.Vector;
import javax.xml.transform.Source;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.dtm.DTMWSFilter;
import org.apache.xml.utils.IntStack;
import org.apache.xml.utils.IntVector;
import org.apache.xml.utils.StringVector;
import org.apache.xml.utils.XMLStringFactory;
import org.xml.sax.SAXException;

public class SAX2RTFDTM extends SAX2DTM {
   private static final boolean DEBUG = false;
   private int m_currentDocumentNode = -1;
   IntStack mark_size = new IntStack();
   IntStack mark_data_size = new IntStack();
   IntStack mark_char_size = new IntStack();
   IntStack mark_doq_size = new IntStack();
   IntStack mark_nsdeclset_size = new IntStack();
   IntStack mark_nsdeclelem_size = new IntStack();
   int m_emptyNodeCount;
   int m_emptyNSDeclSetCount;
   int m_emptyNSDeclSetElemsCount;
   int m_emptyDataCount;
   int m_emptyCharsCount;
   int m_emptyDataQNCount;

   public SAX2RTFDTM(DTMManager mgr, Source source, int dtmIdentity, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory, boolean doIndexing) {
      super(mgr, source, dtmIdentity, whiteSpaceFilter, xstringfactory, doIndexing);
      super.m_useSourceLocationProperty = false;
      super.m_sourceSystemId = super.m_useSourceLocationProperty ? new StringVector() : null;
      super.m_sourceLine = super.m_useSourceLocationProperty ? new IntVector() : null;
      super.m_sourceColumn = super.m_useSourceLocationProperty ? new IntVector() : null;
      this.m_emptyNodeCount = super.m_size;
      this.m_emptyNSDeclSetCount = super.m_namespaceDeclSets == null ? 0 : super.m_namespaceDeclSets.size();
      this.m_emptyNSDeclSetElemsCount = super.m_namespaceDeclSetElements == null ? 0 : super.m_namespaceDeclSetElements.size();
      this.m_emptyDataCount = super.m_data.size();
      this.m_emptyCharsCount = super.m_chars.size();
      this.m_emptyDataQNCount = super.m_dataOrQName.size();
   }

   public int getDocument() {
      return this.makeNodeHandle(this.m_currentDocumentNode);
   }

   public int getDocumentRoot(int nodeHandle) {
      for(int id = this.makeNodeIdentity(nodeHandle); id != -1; id = this._parent(id)) {
         if (this._type(id) == 9) {
            return this.makeNodeHandle(id);
         }
      }

      return -1;
   }

   protected int _documentRoot(int nodeIdentifier) {
      if (nodeIdentifier == -1) {
         return -1;
      } else {
         for(int parent = this._parent(nodeIdentifier); parent != -1; parent = this._parent(parent)) {
            nodeIdentifier = parent;
         }

         return nodeIdentifier;
      }
   }

   public void startDocument() throws SAXException {
      super.m_endDocumentOccured = false;
      super.m_prefixMappings = new Vector();
      super.m_contextIndexes = new IntStack();
      super.m_parents = new IntStack();
      this.m_currentDocumentNode = super.m_size;
      super.startDocument();
   }

   public void endDocument() throws SAXException {
      this.charactersFlush();
      super.m_nextsib.setElementAt(-1, this.m_currentDocumentNode);
      if (super.m_firstch.elementAt(this.m_currentDocumentNode) == -2) {
         super.m_firstch.setElementAt(-1, this.m_currentDocumentNode);
      }

      if (-1 != super.m_previous) {
         super.m_nextsib.setElementAt(-1, super.m_previous);
      }

      super.m_parents = null;
      super.m_prefixMappings = null;
      super.m_contextIndexes = null;
      this.m_currentDocumentNode = -1;
      super.m_endDocumentOccured = true;
   }

   public void pushRewindMark() {
      if (!super.m_indexing && super.m_elemIndexes == null) {
         this.mark_size.push(super.m_size);
         this.mark_nsdeclset_size.push(super.m_namespaceDeclSets == null ? 0 : super.m_namespaceDeclSets.size());
         this.mark_nsdeclelem_size.push(super.m_namespaceDeclSetElements == null ? 0 : super.m_namespaceDeclSetElements.size());
         this.mark_data_size.push(super.m_data.size());
         this.mark_char_size.push(super.m_chars.size());
         this.mark_doq_size.push(super.m_dataOrQName.size());
      } else {
         throw new NullPointerException("Coding error; Don't try to mark/rewind an indexed DTM");
      }
   }

   public boolean popRewindMark() {
      boolean top = this.mark_size.empty();
      super.m_size = top ? this.m_emptyNodeCount : this.mark_size.pop();
      super.m_exptype.setSize(super.m_size);
      super.m_firstch.setSize(super.m_size);
      super.m_nextsib.setSize(super.m_size);
      super.m_prevsib.setSize(super.m_size);
      super.m_parent.setSize(super.m_size);
      super.m_elemIndexes = null;
      int ds = top ? this.m_emptyNSDeclSetCount : this.mark_nsdeclset_size.pop();
      if (super.m_namespaceDeclSets != null) {
         super.m_namespaceDeclSets.setSize(ds);
      }

      int ds1 = top ? this.m_emptyNSDeclSetElemsCount : this.mark_nsdeclelem_size.pop();
      if (super.m_namespaceDeclSetElements != null) {
         super.m_namespaceDeclSetElements.setSize(ds1);
      }

      super.m_data.setSize(top ? this.m_emptyDataCount : this.mark_data_size.pop());
      super.m_chars.setLength(top ? this.m_emptyCharsCount : this.mark_char_size.pop());
      super.m_dataOrQName.setSize(top ? this.m_emptyDataQNCount : this.mark_doq_size.pop());
      return super.m_size == 0;
   }

   public boolean isTreeIncomplete() {
      return !super.m_endDocumentOccured;
   }
}
