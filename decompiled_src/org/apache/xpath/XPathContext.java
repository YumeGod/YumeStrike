package org.apache.xpath;

import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Source;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import org.apache.xalan.extensions.ExpressionContext;
import org.apache.xalan.res.XSLMessages;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMFilter;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.dtm.DTMWSFilter;
import org.apache.xml.dtm.ref.DTMNodeIterator;
import org.apache.xml.dtm.ref.sax2dtm.SAX2RTFDTM;
import org.apache.xml.utils.DefaultErrorHandler;
import org.apache.xml.utils.IntStack;
import org.apache.xml.utils.NodeVector;
import org.apache.xml.utils.ObjectStack;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.QName;
import org.apache.xml.utils.SAXSourceLocator;
import org.apache.xml.utils.XMLString;
import org.apache.xpath.axes.OneStepIteratorForward;
import org.apache.xpath.axes.SubContextList;
import org.apache.xpath.objects.DTMXRTreeFrag;
import org.apache.xpath.objects.XMLStringFactoryImpl;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XString;
import org.apache.xpath.res.XPATHMessages;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.XMLReader;

public class XPathContext extends DTMManager {
   IntStack m_last_pushed_rtfdtm = new IntStack();
   private Vector m_rtfdtm_stack = null;
   private int m_which_rtfdtm = -1;
   private SAX2RTFDTM m_global_rtfdtm = null;
   private HashMap m_DTMXRTreeFrags = null;
   private boolean m_isSecureProcessing = false;
   protected DTMManager m_dtmManager = DTMManager.newInstance(XMLStringFactoryImpl.getFactory());
   ObjectStack m_saxLocations = new ObjectStack(4096);
   private Object m_owner;
   private Method m_ownerGetErrorListener;
   private VariableStack m_variableStacks = new VariableStack();
   private SourceTreeManager m_sourceTreeManager = new SourceTreeManager();
   private ErrorListener m_errorListener;
   private ErrorListener m_defaultErrorListener;
   private URIResolver m_uriResolver;
   public XMLReader m_primaryReader;
   private Stack m_contextNodeLists = new Stack();
   public static final int RECURSIONLIMIT = 4096;
   private IntStack m_currentNodes = new IntStack(4096);
   private NodeVector m_iteratorRoots = new NodeVector();
   private NodeVector m_predicateRoots = new NodeVector();
   private IntStack m_currentExpressionNodes = new IntStack(4096);
   private IntStack m_predicatePos = new IntStack();
   private ObjectStack m_prefixResolvers = new ObjectStack(4096);
   private Stack m_axesIteratorStack = new Stack();
   XPathExpressionContext expressionContext = new XPathExpressionContext();

   public DTMManager getDTMManager() {
      return this.m_dtmManager;
   }

   public void setSecureProcessing(boolean flag) {
      this.m_isSecureProcessing = flag;
   }

   public boolean isSecureProcessing() {
      return this.m_isSecureProcessing;
   }

   public DTM getDTM(Source source, boolean unique, DTMWSFilter wsfilter, boolean incremental, boolean doIndexing) {
      return this.m_dtmManager.getDTM(source, unique, wsfilter, incremental, doIndexing);
   }

   public DTM getDTM(int nodeHandle) {
      return this.m_dtmManager.getDTM(nodeHandle);
   }

   public int getDTMHandleFromNode(Node node) {
      return this.m_dtmManager.getDTMHandleFromNode(node);
   }

   public int getDTMIdentity(DTM dtm) {
      return this.m_dtmManager.getDTMIdentity(dtm);
   }

   public DTM createDocumentFragment() {
      return this.m_dtmManager.createDocumentFragment();
   }

   public boolean release(DTM dtm, boolean shouldHardDelete) {
      return this.m_rtfdtm_stack != null && this.m_rtfdtm_stack.contains(dtm) ? false : this.m_dtmManager.release(dtm, shouldHardDelete);
   }

   public DTMIterator createDTMIterator(Object xpathCompiler, int pos) {
      return this.m_dtmManager.createDTMIterator(xpathCompiler, pos);
   }

   public DTMIterator createDTMIterator(String xpathString, PrefixResolver presolver) {
      return this.m_dtmManager.createDTMIterator(xpathString, presolver);
   }

   public DTMIterator createDTMIterator(int whatToShow, DTMFilter filter, boolean entityReferenceExpansion) {
      return this.m_dtmManager.createDTMIterator(whatToShow, filter, entityReferenceExpansion);
   }

   public DTMIterator createDTMIterator(int node) {
      DTMIterator iter = new OneStepIteratorForward(13);
      iter.setRoot(node, this);
      return iter;
   }

   public XPathContext() {
      this.m_prefixResolvers.push((Object)null);
      this.m_currentNodes.push(-1);
      this.m_currentExpressionNodes.push(-1);
      this.m_saxLocations.push((Object)null);
   }

   public XPathContext(Object owner) {
      this.m_owner = owner;

      try {
         this.m_ownerGetErrorListener = this.m_owner.getClass().getMethod("getErrorListener");
      } catch (NoSuchMethodException var3) {
      }

      this.m_prefixResolvers.push((Object)null);
      this.m_currentNodes.push(-1);
      this.m_currentExpressionNodes.push(-1);
      this.m_saxLocations.push((Object)null);
   }

   public void reset() {
      this.releaseDTMXRTreeFrags();
      if (this.m_rtfdtm_stack != null) {
         Enumeration e = this.m_rtfdtm_stack.elements();

         while(e.hasMoreElements()) {
            this.m_dtmManager.release((DTM)e.nextElement(), true);
         }
      }

      this.m_rtfdtm_stack = null;
      this.m_which_rtfdtm = -1;
      if (this.m_global_rtfdtm != null) {
         this.m_dtmManager.release(this.m_global_rtfdtm, true);
      }

      this.m_global_rtfdtm = null;
      this.m_dtmManager = DTMManager.newInstance(XMLStringFactoryImpl.getFactory());
      this.m_saxLocations.removeAllElements();
      this.m_axesIteratorStack.removeAllElements();
      this.m_contextNodeLists.removeAllElements();
      this.m_currentExpressionNodes.removeAllElements();
      this.m_currentNodes.removeAllElements();
      this.m_iteratorRoots.RemoveAllNoClear();
      this.m_predicatePos.removeAllElements();
      this.m_predicateRoots.RemoveAllNoClear();
      this.m_prefixResolvers.removeAllElements();
      this.m_prefixResolvers.push((Object)null);
      this.m_currentNodes.push(-1);
      this.m_currentExpressionNodes.push(-1);
      this.m_saxLocations.push((Object)null);
   }

   public void setSAXLocator(SourceLocator location) {
      this.m_saxLocations.setTop(location);
   }

   public void pushSAXLocator(SourceLocator location) {
      this.m_saxLocations.push(location);
   }

   public void pushSAXLocatorNull() {
      this.m_saxLocations.push((Object)null);
   }

   public void popSAXLocator() {
      this.m_saxLocations.pop();
   }

   public SourceLocator getSAXLocator() {
      return (SourceLocator)this.m_saxLocations.peek();
   }

   public Object getOwnerObject() {
      return this.m_owner;
   }

   public final VariableStack getVarStack() {
      return this.m_variableStacks;
   }

   public final void setVarStack(VariableStack varStack) {
      this.m_variableStacks = varStack;
   }

   public final SourceTreeManager getSourceTreeManager() {
      return this.m_sourceTreeManager;
   }

   public void setSourceTreeManager(SourceTreeManager mgr) {
      this.m_sourceTreeManager = mgr;
   }

   public final ErrorListener getErrorListener() {
      if (null != this.m_errorListener) {
         return this.m_errorListener;
      } else {
         ErrorListener retval = null;

         try {
            if (null != this.m_ownerGetErrorListener) {
               retval = (ErrorListener)this.m_ownerGetErrorListener.invoke(this.m_owner);
            }
         } catch (Exception var3) {
         }

         if (null == retval) {
            if (null == this.m_defaultErrorListener) {
               this.m_defaultErrorListener = new DefaultErrorHandler();
            }

            retval = this.m_defaultErrorListener;
         }

         return retval;
      }
   }

   public void setErrorListener(ErrorListener listener) throws IllegalArgumentException {
      if (listener == null) {
         throw new IllegalArgumentException(XPATHMessages.createXPATHMessage("ER_NULL_ERROR_HANDLER", (Object[])null));
      } else {
         this.m_errorListener = listener;
      }
   }

   public final URIResolver getURIResolver() {
      return this.m_uriResolver;
   }

   public void setURIResolver(URIResolver resolver) {
      this.m_uriResolver = resolver;
   }

   public final XMLReader getPrimaryReader() {
      return this.m_primaryReader;
   }

   public void setPrimaryReader(XMLReader reader) {
      this.m_primaryReader = reader;
   }

   private void assertion(boolean b, String msg) throws TransformerException {
      if (!b) {
         ErrorListener errorHandler = this.getErrorListener();
         if (errorHandler != null) {
            errorHandler.fatalError(new TransformerException(XSLMessages.createMessage("ER_INCORRECT_PROGRAMMER_ASSERTION", new Object[]{msg}), (SAXSourceLocator)this.getSAXLocator()));
         }
      }

   }

   public Stack getContextNodeListsStack() {
      return this.m_contextNodeLists;
   }

   public void setContextNodeListsStack(Stack s) {
      this.m_contextNodeLists = s;
   }

   public final DTMIterator getContextNodeList() {
      return this.m_contextNodeLists.size() > 0 ? (DTMIterator)this.m_contextNodeLists.peek() : null;
   }

   public final void pushContextNodeList(DTMIterator nl) {
      this.m_contextNodeLists.push(nl);
   }

   public final void popContextNodeList() {
      if (this.m_contextNodeLists.isEmpty()) {
         System.err.println("Warning: popContextNodeList when stack is empty!");
      } else {
         this.m_contextNodeLists.pop();
      }

   }

   public IntStack getCurrentNodeStack() {
      return this.m_currentNodes;
   }

   public void setCurrentNodeStack(IntStack nv) {
      this.m_currentNodes = nv;
   }

   public final int getCurrentNode() {
      return this.m_currentNodes.peek();
   }

   public final void pushCurrentNodeAndExpression(int cn, int en) {
      this.m_currentNodes.push(cn);
      this.m_currentExpressionNodes.push(cn);
   }

   public final void popCurrentNodeAndExpression() {
      this.m_currentNodes.quickPop(1);
      this.m_currentExpressionNodes.quickPop(1);
   }

   public final void pushExpressionState(int cn, int en, PrefixResolver nc) {
      this.m_currentNodes.push(cn);
      this.m_currentExpressionNodes.push(cn);
      this.m_prefixResolvers.push(nc);
   }

   public final void popExpressionState() {
      this.m_currentNodes.quickPop(1);
      this.m_currentExpressionNodes.quickPop(1);
      this.m_prefixResolvers.pop();
   }

   public final void pushCurrentNode(int n) {
      this.m_currentNodes.push(n);
   }

   public final void popCurrentNode() {
      this.m_currentNodes.quickPop(1);
   }

   public final void pushPredicateRoot(int n) {
      this.m_predicateRoots.push(n);
   }

   public final void popPredicateRoot() {
      this.m_predicateRoots.popQuick();
   }

   public final int getPredicateRoot() {
      return this.m_predicateRoots.peepOrNull();
   }

   public final void pushIteratorRoot(int n) {
      this.m_iteratorRoots.push(n);
   }

   public final void popIteratorRoot() {
      this.m_iteratorRoots.popQuick();
   }

   public final int getIteratorRoot() {
      return this.m_iteratorRoots.peepOrNull();
   }

   public IntStack getCurrentExpressionNodeStack() {
      return this.m_currentExpressionNodes;
   }

   public void setCurrentExpressionNodeStack(IntStack nv) {
      this.m_currentExpressionNodes = nv;
   }

   public final int getPredicatePos() {
      return this.m_predicatePos.peek();
   }

   public final void pushPredicatePos(int n) {
      this.m_predicatePos.push(n);
   }

   public final void popPredicatePos() {
      this.m_predicatePos.pop();
   }

   public final int getCurrentExpressionNode() {
      return this.m_currentExpressionNodes.peek();
   }

   public final void pushCurrentExpressionNode(int n) {
      this.m_currentExpressionNodes.push(n);
   }

   public final void popCurrentExpressionNode() {
      this.m_currentExpressionNodes.quickPop(1);
   }

   public final PrefixResolver getNamespaceContext() {
      return (PrefixResolver)this.m_prefixResolvers.peek();
   }

   public final void setNamespaceContext(PrefixResolver pr) {
      this.m_prefixResolvers.setTop(pr);
   }

   public final void pushNamespaceContext(PrefixResolver pr) {
      this.m_prefixResolvers.push(pr);
   }

   public final void pushNamespaceContextNull() {
      this.m_prefixResolvers.push((Object)null);
   }

   public final void popNamespaceContext() {
      this.m_prefixResolvers.pop();
   }

   public Stack getAxesIteratorStackStacks() {
      return this.m_axesIteratorStack;
   }

   public void setAxesIteratorStackStacks(Stack s) {
      this.m_axesIteratorStack = s;
   }

   public final void pushSubContextList(SubContextList iter) {
      this.m_axesIteratorStack.push(iter);
   }

   public final void popSubContextList() {
      this.m_axesIteratorStack.pop();
   }

   public SubContextList getSubContextList() {
      return this.m_axesIteratorStack.isEmpty() ? null : (SubContextList)this.m_axesIteratorStack.peek();
   }

   public SubContextList getCurrentNodeList() {
      return this.m_axesIteratorStack.isEmpty() ? null : (SubContextList)this.m_axesIteratorStack.elementAt(0);
   }

   public final int getContextNode() {
      return this.getCurrentNode();
   }

   public final DTMIterator getContextNodes() {
      try {
         DTMIterator cnl = this.getContextNodeList();
         return null != cnl ? cnl.cloneWithReset() : null;
      } catch (CloneNotSupportedException var2) {
         return null;
      }
   }

   public ExpressionContext getExpressionContext() {
      return this.expressionContext;
   }

   public DTM getGlobalRTFDTM() {
      if (this.m_global_rtfdtm == null || this.m_global_rtfdtm.isTreeIncomplete()) {
         this.m_global_rtfdtm = (SAX2RTFDTM)this.m_dtmManager.getDTM((Source)null, true, (DTMWSFilter)null, false, false);
      }

      return this.m_global_rtfdtm;
   }

   public DTM getRTFDTM() {
      SAX2RTFDTM rtfdtm;
      if (this.m_rtfdtm_stack == null) {
         this.m_rtfdtm_stack = new Vector();
         rtfdtm = (SAX2RTFDTM)this.m_dtmManager.getDTM((Source)null, true, (DTMWSFilter)null, false, false);
         this.m_rtfdtm_stack.addElement(rtfdtm);
         ++this.m_which_rtfdtm;
      } else if (this.m_which_rtfdtm < 0) {
         rtfdtm = (SAX2RTFDTM)this.m_rtfdtm_stack.elementAt(++this.m_which_rtfdtm);
      } else {
         rtfdtm = (SAX2RTFDTM)this.m_rtfdtm_stack.elementAt(this.m_which_rtfdtm);
         if (rtfdtm.isTreeIncomplete()) {
            if (++this.m_which_rtfdtm < this.m_rtfdtm_stack.size()) {
               rtfdtm = (SAX2RTFDTM)this.m_rtfdtm_stack.elementAt(this.m_which_rtfdtm);
            } else {
               rtfdtm = (SAX2RTFDTM)this.m_dtmManager.getDTM((Source)null, true, (DTMWSFilter)null, false, false);
               this.m_rtfdtm_stack.addElement(rtfdtm);
            }
         }
      }

      return rtfdtm;
   }

   public void pushRTFContext() {
      this.m_last_pushed_rtfdtm.push(this.m_which_rtfdtm);
      if (null != this.m_rtfdtm_stack) {
         ((SAX2RTFDTM)this.getRTFDTM()).pushRewindMark();
      }

   }

   public void popRTFContext() {
      int previous = this.m_last_pushed_rtfdtm.pop();
      if (null != this.m_rtfdtm_stack) {
         boolean isEmpty;
         if (this.m_which_rtfdtm == previous) {
            if (previous >= 0) {
               isEmpty = ((SAX2RTFDTM)this.m_rtfdtm_stack.elementAt(previous)).popRewindMark();
            }
         } else {
            while(this.m_which_rtfdtm != previous) {
               isEmpty = ((SAX2RTFDTM)this.m_rtfdtm_stack.elementAt(this.m_which_rtfdtm)).popRewindMark();
               --this.m_which_rtfdtm;
            }
         }

      }
   }

   public DTMXRTreeFrag getDTMXRTreeFrag(int dtmIdentity) {
      if (this.m_DTMXRTreeFrags == null) {
         this.m_DTMXRTreeFrags = new HashMap();
      }

      if (this.m_DTMXRTreeFrags.containsKey(new Integer(dtmIdentity))) {
         return (DTMXRTreeFrag)this.m_DTMXRTreeFrags.get(new Integer(dtmIdentity));
      } else {
         DTMXRTreeFrag frag = new DTMXRTreeFrag(dtmIdentity, this);
         this.m_DTMXRTreeFrags.put(new Integer(dtmIdentity), frag);
         return frag;
      }
   }

   private final void releaseDTMXRTreeFrags() {
      if (this.m_DTMXRTreeFrags != null) {
         Iterator iter = this.m_DTMXRTreeFrags.values().iterator();

         while(iter.hasNext()) {
            DTMXRTreeFrag frag = (DTMXRTreeFrag)iter.next();
            frag.destruct();
            iter.remove();
         }

         this.m_DTMXRTreeFrags = null;
      }
   }

   public class XPathExpressionContext implements ExpressionContext {
      public XPathContext getXPathContext() {
         return XPathContext.this;
      }

      public DTMManager getDTMManager() {
         return XPathContext.this.m_dtmManager;
      }

      public Node getContextNode() {
         int context = XPathContext.this.getCurrentNode();
         return XPathContext.this.getDTM(context).getNode(context);
      }

      public NodeIterator getContextNodes() {
         return new DTMNodeIterator(XPathContext.this.getContextNodeList());
      }

      public ErrorListener getErrorListener() {
         return XPathContext.this.getErrorListener();
      }

      public double toNumber(Node n) {
         int nodeHandle = XPathContext.this.getDTMHandleFromNode(n);
         DTM dtm = XPathContext.this.getDTM(nodeHandle);
         XString xobj = (XString)dtm.getStringValue(nodeHandle);
         return xobj.num();
      }

      public String toString(Node n) {
         int nodeHandle = XPathContext.this.getDTMHandleFromNode(n);
         DTM dtm = XPathContext.this.getDTM(nodeHandle);
         XMLString strVal = dtm.getStringValue(nodeHandle);
         return strVal.toString();
      }

      public final XObject getVariableOrParam(QName qname) throws TransformerException {
         return XPathContext.this.m_variableStacks.getVariableOrParam(XPathContext.this, qname);
      }
   }
}
