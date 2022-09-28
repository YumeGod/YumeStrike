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

public class DefaultFieldParser extends DelegatingFieldParser {
   private static final DefaultFieldParser PARSER = new DefaultFieldParser();

   public static DefaultFieldParser getParser() {
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

   public DefaultFieldParser() {
      super(UnstructuredFieldImpl.PARSER);
      this.setFieldParser("Content-Type", ContentTypeFieldImpl.PARSER);
      this.setFieldParser("Content-Length", ContentLengthFieldImpl.PARSER);
      this.setFieldParser("Content-Transfer-Encoding", ContentTransferEncodingFieldImpl.PARSER);
      this.setFieldParser("Content-Disposition", ContentDispositionFieldImpl.PARSER);
      this.setFieldParser("Content-ID", ContentIdFieldImpl.PARSER);
      this.setFieldParser("Content-MD5", ContentMD5FieldImpl.PARSER);
      this.setFieldParser("Content-Description", ContentDescriptionFieldImpl.PARSER);
      this.setFieldParser("Content-Language", ContentLanguageFieldImpl.PARSER);
      this.setFieldParser("Content-Location", ContentLocationFieldImpl.PARSER);
      this.setFieldParser("MIME-Version", MimeVersionFieldImpl.PARSER);
      FieldParser dateTimeParser = DateTimeFieldImpl.PARSER;
      this.setFieldParser("Date", dateTimeParser);
      this.setFieldParser("Resent-Date", dateTimeParser);
      FieldParser mailboxListParser = MailboxListFieldImpl.PARSER;
      this.setFieldParser("From", mailboxListParser);
      this.setFieldParser("Resent-From", mailboxListParser);
      FieldParser mailboxParser = MailboxFieldImpl.PARSER;
      this.setFieldParser("Sender", mailboxParser);
      this.setFieldParser("Resent-Sender", mailboxParser);
      FieldParser addressListParser = AddressListFieldImpl.PARSER;
      this.setFieldParser("To", addressListParser);
      this.setFieldParser("Resent-To", addressListParser);
      this.setFieldParser("Cc", addressListParser);
      this.setFieldParser("Resent-Cc", addressListParser);
      this.setFieldParser("Bcc", addressListParser);
      this.setFieldParser("Resent-Bcc", addressListParser);
      this.setFieldParser("Reply-To", addressListParser);
   }
}
