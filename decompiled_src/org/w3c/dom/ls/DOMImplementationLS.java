package org.w3c.dom.ls;

import org.w3c.dom.DOMException;

public interface DOMImplementationLS {
   short MODE_SYNCHRONOUS = 1;
   short MODE_ASYNCHRONOUS = 2;

   LSParser createLSParser(short var1, String var2) throws DOMException;

   LSSerializer createLSSerializer();

   LSInput createLSInput();

   LSOutput createLSOutput();
}
