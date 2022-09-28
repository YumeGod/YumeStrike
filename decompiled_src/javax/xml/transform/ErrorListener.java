package javax.xml.transform;

public interface ErrorListener {
   void warning(TransformerException var1) throws TransformerException;

   void error(TransformerException var1) throws TransformerException;

   void fatalError(TransformerException var1) throws TransformerException;
}
