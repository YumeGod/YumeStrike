package org.w3c.dom;

public interface DOMError {
   short SEVERITY_WARNING = 1;
   short SEVERITY_ERROR = 2;
   short SEVERITY_FATAL_ERROR = 3;

   short getSeverity();

   String getMessage();

   String getType();

   Object getRelatedException();

   Object getRelatedData();

   DOMLocator getLocation();
}
