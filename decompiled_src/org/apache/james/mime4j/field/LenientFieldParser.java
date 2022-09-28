package org.apache.james.mime4j.field;

import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.codec.DecodeMonitor;
import org.apache.james.mime4j.dom.FieldParser;
import org.apache.james.mime4j.dom.field.ParsedField;
import org.apache.james.mime4j.stream.Field;
import org.apache.james.mime4j.stream.RawField;
import org.apache.james.mime4j.stream.RawFieldParser;
import org.apache.james.mime4j.util.ByteSequence;
import org.apache.james.mime4j.util.ContentUtil;

public class LenientFieldParser extends DelegatingFieldParser {
   private static final LenientFieldParser PARSER = new LenientFieldParser();

   public static LenientFieldParser getParser() {
      return PARSER;
   }

   public static ParsedField parse(ByteSequence raw, DecodeMonitor monitor) throws MimeException {
      Field rawField = RawFieldParser.DEFAULT.parseField(raw);
      return PARSER.parse((Field)rawField, monitor);
   }

   public static ParsedField parse(String rawStr, DecodeMonitor monitor) throws MimeException {
      ByteSequence raw = ContentUtil.encode(rawStr);
      RawField rawField = RawFieldParser.DEFAULT.parseField(raw);
      return PARSER.parse((Field)rawField, monitor);
   }

   public static ParsedField parse(String rawStr) throws MimeException {
      return parse(rawStr, DecodeMonitor.SILENT);
   }

   public LenientFieldParser() {
      super(UnstructuredFieldImpl.PARSER);
      this.setFieldParser("Content-Type", ContentTypeFieldLenientImpl.PARSER);
      this.setFieldParser("Content-Length", ContentLengthFieldImpl.PARSER);
      this.setFieldParser("Content-Transfer-Encoding", ContentTransferEncodingFieldImpl.PARSER);
      this.setFieldParser("Content-Disposition", ContentDispositionFieldLenientImpl.PARSER);
      this.setFieldParser("Content-ID", ContentIdFieldImpl.PARSER);
      this.setFieldParser("Content-MD5", ContentMD5FieldImpl.PARSER);
      this.setFieldParser("Content-Description", ContentDescriptionFieldImpl.PARSER);
      this.setFieldParser("Content-Language", ContentLanguageFieldLenientImpl.PARSER);
      this.setFieldParser("Content-Location", ContentLocationFieldLenientImpl.PARSER);
      this.setFieldParser("MIME-Version", MimeVersionFieldImpl.PARSER);
      FieldParser dateTimeParser = DateTimeFieldLenientImpl.PARSER;
      this.setFieldParser("Date", dateTimeParser);
      this.setFieldParser("Resent-Date", dateTimeParser);
      FieldParser mailboxListParser = MailboxListFieldLenientImpl.PARSER;
      this.setFieldParser("From", mailboxListParser);
      this.setFieldParser("Resent-From", mailboxListParser);
      FieldParser mailboxParser = MailboxFieldLenientImpl.PARSER;
      this.setFieldParser("Sender", mailboxParser);
      this.setFieldParser("Resent-Sender", mailboxParser);
      FieldParser addressListParser = AddressListFieldLenientImpl.PARSER;
      this.setFieldParser("To", addressListParser);
      this.setFieldParser("Resent-To", addressListParser);
      this.setFieldParser("Cc", addressListParser);
      this.setFieldParser("Resent-Cc", addressListParser);
      this.setFieldParser("Bcc", addressListParser);
      this.setFieldParser("Resent-Bcc", addressListParser);
      this.setFieldParser("Reply-To", addressListParser);
   }
}
