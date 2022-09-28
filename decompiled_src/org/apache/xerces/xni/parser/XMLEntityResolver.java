package org.apache.xerces.xni.parser;

import java.io.IOException;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XNIException;

public interface XMLEntityResolver {
   XMLInputSource resolveEntity(XMLResourceIdentifier var1) throws XNIException, IOException;
}
