package org.apache.batik.bridge;

public interface Bridge {
   String getNamespaceURI();

   String getLocalName();

   Bridge getInstance();
}
