package org.apache.james.mime4j.stream;

import org.apache.james.mime4j.MimeException;

public interface BodyDescriptorBuilder {
   void reset();

   Field addField(RawField var1) throws MimeException;

   BodyDescriptor build();

   BodyDescriptorBuilder newChild();
}
