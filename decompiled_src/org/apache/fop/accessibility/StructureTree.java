package org.apache.fop.accessibility;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public final class StructureTree {
   private final List pageSequenceStructures = new ArrayList();
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   StructureTree() {
   }

   private static boolean flowOrStaticContentNodes(NodeList nodes) {
      for(int i = 0; i < nodes.getLength(); ++i) {
         Node node = nodes.item(i);
         if (node.getNodeType() != 1) {
            return false;
         }

         String name = node.getLocalName();
         if (!name.equals("flow") && !name.equals("static-content")) {
            return false;
         }
      }

      return true;
   }

   void addPageSequenceStructure(NodeList structureTree) {
      if (!$assertionsDisabled && !flowOrStaticContentNodes(structureTree)) {
         throw new AssertionError();
      } else {
         this.pageSequenceStructures.add(structureTree);
      }
   }

   public NodeList getPageSequence(int index) {
      return (NodeList)this.pageSequenceStructures.get(index);
   }

   public String toString() {
      try {
         Transformer t = TransformerFactory.newInstance().newTransformer();
         Writer str = new StringWriter();
         Iterator iter = this.pageSequenceStructures.iterator();

         while(iter.hasNext()) {
            NodeList nodes = (NodeList)iter.next();
            int i = 0;

            for(int c = nodes.getLength(); i < c; ++i) {
               t.transform(new DOMSource(nodes.item(i)), new StreamResult(str));
            }
         }

         return str.toString();
      } catch (Exception var7) {
         return var7.toString();
      }
   }

   static {
      $assertionsDisabled = !StructureTree.class.desiredAssertionStatus();
   }
}
