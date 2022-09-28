package org.apache.xml.utils;

import java.util.Hashtable;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.ref.DTMNodeProxy;
import org.apache.xml.res.XMLMessages;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/** @deprecated */
public class DOMHelper {
   Hashtable m_NSInfos = new Hashtable();
   protected static final NSInfo m_NSInfoUnProcWithXMLNS = new NSInfo(false, true);
   protected static final NSInfo m_NSInfoUnProcWithoutXMLNS = new NSInfo(false, false);
   protected static final NSInfo m_NSInfoUnProcNoAncestorXMLNS = new NSInfo(false, false, 2);
   protected static final NSInfo m_NSInfoNullWithXMLNS = new NSInfo(true, true);
   protected static final NSInfo m_NSInfoNullWithoutXMLNS = new NSInfo(true, false);
   protected static final NSInfo m_NSInfoNullNoAncestorXMLNS = new NSInfo(true, false, 2);
   protected Vector m_candidateNoAncestorXMLNS = new Vector();
   protected Document m_DOMFactory = null;

   public static Document createDocument(boolean isSecureProcessing) {
      try {
         DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
         dfactory.setNamespaceAware(true);
         dfactory.setValidating(true);
         if (isSecureProcessing) {
            try {
               dfactory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
            } catch (ParserConfigurationException var4) {
            }
         }

         DocumentBuilder docBuilder = dfactory.newDocumentBuilder();
         Document outNode = docBuilder.newDocument();
         return outNode;
      } catch (ParserConfigurationException var5) {
         throw new RuntimeException(XMLMessages.createXMLMessage("ER_CREATEDOCUMENT_NOT_SUPPORTED", (Object[])null));
      }
   }

   public static Document createDocument() {
      return createDocument(false);
   }

   public boolean shouldStripSourceNode(Node textNode) throws TransformerException {
      return false;
   }

   public String getUniqueID(Node node) {
      return "N" + Integer.toHexString(node.hashCode()).toUpperCase();
   }

   public static boolean isNodeAfter(Node node1, Node node2) {
      if (node1 != node2 && !isNodeTheSame(node1, node2)) {
         boolean isNodeAfter = true;
         Node parent1 = getParentOfNode(node1);
         Node parent2 = getParentOfNode(node2);
         if (parent1 != parent2 && !isNodeTheSame(parent1, parent2)) {
            int nParents1 = 2;

            int nParents2;
            for(nParents2 = 2; parent1 != null; parent1 = getParentOfNode(parent1)) {
               ++nParents1;
            }

            while(parent2 != null) {
               ++nParents2;
               parent2 = getParentOfNode(parent2);
            }

            Node startNode1 = node1;
            Node startNode2 = node2;
            int adjust;
            int i;
            if (nParents1 < nParents2) {
               adjust = nParents2 - nParents1;

               for(i = 0; i < adjust; ++i) {
                  startNode2 = getParentOfNode(startNode2);
               }
            } else if (nParents1 > nParents2) {
               adjust = nParents1 - nParents2;

               for(i = 0; i < adjust; ++i) {
                  startNode1 = getParentOfNode(startNode1);
               }
            }

            Node prevChild1 = null;

            for(Node prevChild2 = null; null != startNode1; startNode2 = getParentOfNode(startNode2)) {
               if (startNode1 == startNode2 || isNodeTheSame(startNode1, startNode2)) {
                  if (null == prevChild1) {
                     isNodeAfter = nParents1 < nParents2;
                  } else {
                     isNodeAfter = isNodeAfterSibling(startNode1, prevChild1, prevChild2);
                  }
                  break;
               }

               prevChild1 = startNode1;
               startNode1 = getParentOfNode(startNode1);
               prevChild2 = startNode2;
            }
         } else if (null != parent1) {
            isNodeAfter = isNodeAfterSibling(parent1, node1, node2);
         }

         return isNodeAfter;
      } else {
         return true;
      }
   }

   public static boolean isNodeTheSame(Node node1, Node node2) {
      if (node1 instanceof DTMNodeProxy && node2 instanceof DTMNodeProxy) {
         return ((DTMNodeProxy)node1).equals((Node)((DTMNodeProxy)node2));
      } else {
         return node1 == node2;
      }
   }

   private static boolean isNodeAfterSibling(Node parent, Node child1, Node child2) {
      boolean isNodeAfterSibling = false;
      short child1type = child1.getNodeType();
      short child2type = child2.getNodeType();
      if (2 != child1type && 2 == child2type) {
         isNodeAfterSibling = false;
      } else if (2 == child1type && 2 != child2type) {
         isNodeAfterSibling = true;
      } else {
         boolean found2;
         if (2 == child1type) {
            NamedNodeMap children = parent.getAttributes();
            int nNodes = children.getLength();
            found2 = false;
            boolean found2 = false;

            for(int i = 0; i < nNodes; ++i) {
               Node child = children.item(i);
               if (child1 != child && !isNodeTheSame(child1, child)) {
                  if (child2 == child || isNodeTheSame(child2, child)) {
                     if (found2) {
                        isNodeAfterSibling = true;
                        break;
                     }

                     found2 = true;
                  }
               } else {
                  if (found2) {
                     isNodeAfterSibling = false;
                     break;
                  }

                  found2 = true;
               }
            }
         } else {
            Node child = parent.getFirstChild();
            boolean found1 = false;

            for(found2 = false; null != child; child = child.getNextSibling()) {
               if (child1 != child && !isNodeTheSame(child1, child)) {
                  if (child2 == child || isNodeTheSame(child2, child)) {
                     if (found1) {
                        isNodeAfterSibling = true;
                        break;
                     }

                     found2 = true;
                  }
               } else {
                  if (found2) {
                     isNodeAfterSibling = false;
                     break;
                  }

                  found1 = true;
               }
            }
         }
      }

      return isNodeAfterSibling;
   }

   public short getLevel(Node n) {
      short level;
      for(level = 1; null != (n = getParentOfNode(n)); ++level) {
      }

      return level;
   }

   public String getNamespaceForPrefix(String prefix, Element namespaceContext) {
      Node parent = namespaceContext;
      String namespace = null;
      if (prefix.equals("xml")) {
         namespace = "http://www.w3.org/XML/1998/namespace";
      } else {
         short type;
         if (prefix.equals("xmlns")) {
            namespace = "http://www.w3.org/2000/xmlns/";
         } else {
            for(String declname = prefix == "" ? "xmlns" : "xmlns:" + prefix; null != parent && null == namespace && ((type = ((Node)parent).getNodeType()) == 1 || type == 5); parent = getParentOfNode((Node)parent)) {
               if (type == 1) {
                  Attr attr = ((Element)parent).getAttributeNode(declname);
                  if (attr != null) {
                     namespace = attr.getNodeValue();
                     break;
                  }
               }
            }
         }
      }

      return namespace;
   }

   public String getNamespaceOfNode(Node n) {
      short ntype = n.getNodeType();
      boolean hasProcessedNS;
      NSInfo nsInfo;
      if (2 != ntype) {
         Object nsObj = this.m_NSInfos.get(n);
         nsInfo = nsObj == null ? null : (NSInfo)nsObj;
         hasProcessedNS = nsInfo == null ? false : nsInfo.m_hasProcessedNS;
      } else {
         hasProcessedNS = false;
         nsInfo = null;
      }

      String namespaceOfPrefix;
      if (hasProcessedNS) {
         namespaceOfPrefix = nsInfo.m_namespace;
      } else {
         namespaceOfPrefix = null;
         String nodeName = n.getNodeName();
         int indexOfNSSep = nodeName.indexOf(58);
         String prefix;
         if (2 == ntype) {
            if (indexOfNSSep <= 0) {
               return namespaceOfPrefix;
            }

            prefix = nodeName.substring(0, indexOfNSSep);
         } else {
            prefix = indexOfNSSep >= 0 ? nodeName.substring(0, indexOfNSSep) : "";
         }

         boolean ancestorsHaveXMLNS = false;
         boolean nHasXMLNS = false;
         if (prefix.equals("xml")) {
            namespaceOfPrefix = "http://www.w3.org/XML/1998/namespace";
         } else {
            Node parent = n;

            while(null != parent && null == namespaceOfPrefix && (null == nsInfo || nsInfo.m_ancestorHasXMLNSAttrs != 2)) {
               int parentType = parent.getNodeType();
               if (null == nsInfo || nsInfo.m_hasXMLNSAttrs) {
                  boolean elementHasXMLNS = false;
                  if (parentType == 1) {
                     NamedNodeMap nnm = parent.getAttributes();

                     for(int i = 0; i < nnm.getLength(); ++i) {
                        Node attr = nnm.item(i);
                        String aname = attr.getNodeName();
                        if (aname.charAt(0) == 'x') {
                           boolean isPrefix = aname.startsWith("xmlns:");
                           if (aname.equals("xmlns") || isPrefix) {
                              if (n == parent) {
                                 nHasXMLNS = true;
                              }

                              elementHasXMLNS = true;
                              ancestorsHaveXMLNS = true;
                              String p = isPrefix ? aname.substring(6) : "";
                              if (p.equals(prefix)) {
                                 namespaceOfPrefix = attr.getNodeValue();
                                 break;
                              }
                           }
                        }
                     }
                  }

                  if (2 != parentType && null == nsInfo && n != parent) {
                     nsInfo = elementHasXMLNS ? m_NSInfoUnProcWithXMLNS : m_NSInfoUnProcWithoutXMLNS;
                     this.m_NSInfos.put(parent, nsInfo);
                  }
               }

               if (2 == parentType) {
                  parent = getParentOfNode(parent);
               } else {
                  this.m_candidateNoAncestorXMLNS.addElement(parent);
                  this.m_candidateNoAncestorXMLNS.addElement(nsInfo);
                  parent = parent.getParentNode();
               }

               if (null != parent) {
                  Object nsObj = this.m_NSInfos.get(parent);
                  nsInfo = nsObj == null ? null : (NSInfo)nsObj;
               }
            }

            int nCandidates = this.m_candidateNoAncestorXMLNS.size();
            if (nCandidates > 0) {
               if (!ancestorsHaveXMLNS && null == parent) {
                  for(int i = 0; i < nCandidates; i += 2) {
                     Object candidateInfo = this.m_candidateNoAncestorXMLNS.elementAt(i + 1);
                     if (candidateInfo == m_NSInfoUnProcWithoutXMLNS) {
                        this.m_NSInfos.put(this.m_candidateNoAncestorXMLNS.elementAt(i), m_NSInfoUnProcNoAncestorXMLNS);
                     } else if (candidateInfo == m_NSInfoNullWithoutXMLNS) {
                        this.m_NSInfos.put(this.m_candidateNoAncestorXMLNS.elementAt(i), m_NSInfoNullNoAncestorXMLNS);
                     }
                  }
               }

               this.m_candidateNoAncestorXMLNS.removeAllElements();
            }
         }

         if (2 != ntype) {
            if (null == namespaceOfPrefix) {
               if (ancestorsHaveXMLNS) {
                  if (nHasXMLNS) {
                     this.m_NSInfos.put(n, m_NSInfoNullWithXMLNS);
                  } else {
                     this.m_NSInfos.put(n, m_NSInfoNullWithoutXMLNS);
                  }
               } else {
                  this.m_NSInfos.put(n, m_NSInfoNullNoAncestorXMLNS);
               }
            } else {
               this.m_NSInfos.put(n, new NSInfo(namespaceOfPrefix, nHasXMLNS));
            }
         }
      }

      return namespaceOfPrefix;
   }

   public String getLocalNameOfNode(Node n) {
      String qname = n.getNodeName();
      int index = qname.indexOf(58);
      return index < 0 ? qname : qname.substring(index + 1);
   }

   public String getExpandedElementName(Element elem) {
      String namespace = this.getNamespaceOfNode(elem);
      return null != namespace ? namespace + ":" + this.getLocalNameOfNode(elem) : this.getLocalNameOfNode(elem);
   }

   public String getExpandedAttributeName(Attr attr) {
      String namespace = this.getNamespaceOfNode(attr);
      return null != namespace ? namespace + ":" + this.getLocalNameOfNode(attr) : this.getLocalNameOfNode(attr);
   }

   /** @deprecated */
   public boolean isIgnorableWhitespace(Text node) {
      boolean isIgnorable = false;
      return isIgnorable;
   }

   /** @deprecated */
   public Node getRoot(Node node) {
      Node root;
      for(root = null; node != null; node = getParentOfNode(node)) {
         root = node;
      }

      return root;
   }

   public Node getRootNode(Node n) {
      int nt = n.getNodeType();
      return (Node)(9 != nt && 11 != nt ? n.getOwnerDocument() : n);
   }

   public boolean isNamespaceNode(Node n) {
      if (2 != n.getNodeType()) {
         return false;
      } else {
         String attrName = n.getNodeName();
         return attrName.startsWith("xmlns:") || attrName.equals("xmlns");
      }
   }

   public static Node getParentOfNode(Node node) throws RuntimeException {
      short nodeType = node.getNodeType();
      Node parent;
      if (2 == nodeType) {
         Document doc = node.getOwnerDocument();
         DOMImplementation impl = doc.getImplementation();
         if (impl != null && impl.hasFeature("Core", "2.0")) {
            Node parent = ((Attr)node).getOwnerElement();
            return parent;
         }

         Element rootElem = doc.getDocumentElement();
         if (null == rootElem) {
            throw new RuntimeException(XMLMessages.createXMLMessage("ER_CHILD_HAS_NO_OWNER_DOCUMENT_ELEMENT", (Object[])null));
         }

         parent = locateAttrParent(rootElem, node);
      } else {
         parent = node.getParentNode();
      }

      return parent;
   }

   public Element getElementByID(String id, Document doc) {
      return null;
   }

   public String getUnparsedEntityURI(String name, Document doc) {
      String url = "";
      DocumentType doctype = doc.getDoctype();
      if (null != doctype) {
         NamedNodeMap entities = doctype.getEntities();
         if (null == entities) {
            return url;
         }

         Entity entity = (Entity)entities.getNamedItem(name);
         if (null == entity) {
            return url;
         }

         String notationName = entity.getNotationName();
         if (null != notationName) {
            url = entity.getSystemId();
            if (null == url) {
               url = entity.getPublicId();
            }
         }
      }

      return url;
   }

   private static Node locateAttrParent(Element elem, Node attr) {
      Node parent = null;
      Attr check = elem.getAttributeNode(attr.getNodeName());
      if (check == attr) {
         parent = elem;
      }

      if (null == parent) {
         for(Node node = elem.getFirstChild(); null != node; node = node.getNextSibling()) {
            if (1 == node.getNodeType()) {
               parent = locateAttrParent((Element)node, attr);
               if (null != parent) {
                  break;
               }
            }
         }
      }

      return (Node)parent;
   }

   public void setDOMFactory(Document domFactory) {
      this.m_DOMFactory = domFactory;
   }

   public Document getDOMFactory() {
      if (null == this.m_DOMFactory) {
         this.m_DOMFactory = createDocument();
      }

      return this.m_DOMFactory;
   }

   public static String getNodeData(Node node) {
      FastStringBuffer buf = StringBufferPool.get();

      String s;
      try {
         getNodeData(node, buf);
         s = buf.length() > 0 ? buf.toString() : "";
      } finally {
         StringBufferPool.free(buf);
      }

      return s;
   }

   public static void getNodeData(Node node, FastStringBuffer buf) {
      switch (node.getNodeType()) {
         case 1:
         case 9:
         case 11:
            for(Node child = node.getFirstChild(); null != child; child = child.getNextSibling()) {
               getNodeData(child, buf);
            }

            return;
         case 2:
            buf.append(node.getNodeValue());
            break;
         case 3:
         case 4:
            buf.append(node.getNodeValue());
         case 5:
         case 6:
         case 7:
         case 8:
         case 10:
      }

   }
}
