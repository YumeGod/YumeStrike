package org.w3c.dom.traversal;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public interface DocumentTraversal {
   NodeIterator createNodeIterator(Node var1, int var2, NodeFilter var3, boolean var4) throws DOMException;

   TreeWalker createTreeWalker(Node var1, int var2, NodeFilter var3, boolean var4) throws DOMException;
}
