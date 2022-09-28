package org.apache.xpath.objects;

import org.apache.xml.utils.XMLString;

abstract class Comparator {
   abstract boolean compareStrings(XMLString var1, XMLString var2);

   abstract boolean compareNumbers(double var1, double var3);
}
