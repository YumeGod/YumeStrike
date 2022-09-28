package org.apache.xmlgraphics.xmp.schemas;

import java.util.Date;
import org.apache.xmlgraphics.xmp.Metadata;
import org.apache.xmlgraphics.xmp.XMPSchemaAdapter;
import org.apache.xmlgraphics.xmp.XMPSchemaRegistry;

public class DublinCoreAdapter extends XMPSchemaAdapter {
   private static final String CONTRIBUTOR = "contributor";
   private static final String COVERAGE = "coverage";
   private static final String CREATOR = "creator";
   private static final String DATE = "date";
   private static final String DESCRIPTION = "description";
   private static final String FORMAT = "format";
   private static final String IDENTIFIER = "identifier";
   private static final String LANGUAGE = "language";
   private static final String PUBLISHER = "publisher";
   private static final String RELATION = "relation";
   private static final String RIGHTS = "rights";
   private static final String SOURCE = "source";
   private static final String SUBJECT = "subject";
   private static final String TITLE = "title";
   private static final String TYPE = "type";

   public DublinCoreAdapter(Metadata meta) {
      super(meta, XMPSchemaRegistry.getInstance().getSchema("http://purl.org/dc/elements/1.1/"));
   }

   public void addContributor(String value) {
      this.addStringToBag("contributor", value);
   }

   public boolean removeContributor(String value) {
      return this.removeStringFromArray("contributor", value);
   }

   public String[] getContributors() {
      return this.getStringArray("contributor");
   }

   public void setCoverage(String value) {
      this.setValue("coverage", value);
   }

   public String getCoverage() {
      return this.getValue("coverage");
   }

   public void addCreator(String value) {
      this.addStringToSeq("creator", value);
   }

   public boolean removeCreator(String value) {
      return this.removeStringFromArray("creator", value);
   }

   public String[] getCreators() {
      return this.getStringArray("creator");
   }

   public void addDate(Date value) {
      this.addDateToSeq("date", value);
   }

   public Date[] getDates() {
      return this.getDateArray("date");
   }

   public Date getDate() {
      Date[] dates = this.getDates();
      if (dates == null) {
         return null;
      } else {
         Date latest = null;
         int i = 0;

         for(int c = dates.length; i < c; ++i) {
            if (latest == null || dates[i].getTime() > latest.getTime()) {
               latest = dates[i];
            }
         }

         return latest;
      }
   }

   public void setDescription(String lang, String value) {
      this.setLangAlt("description", lang, value);
   }

   public String getDescription() {
      return this.getDescription((String)null);
   }

   public String getDescription(String lang) {
      return this.getLangAlt(lang, "description");
   }

   public void setFormat(String value) {
      this.setValue("format", value);
   }

   public String getFormat() {
      return this.getValue("format");
   }

   public void setIdentifier(String value) {
      this.setValue("identifier", value);
   }

   public String getIdentifier() {
      return this.getValue("identifier");
   }

   public void addLanguage(String value) {
      this.addStringToBag("language", value);
   }

   public String[] getLanguages() {
      return this.getStringArray("language");
   }

   public void addPublisher(String value) {
      this.addStringToBag("publisher", value);
   }

   public String[] getPublisher() {
      return this.getStringArray("publisher");
   }

   public void addRelation(String value) {
      this.addStringToBag("relation", value);
   }

   public String[] getRelations() {
      return this.getStringArray("relation");
   }

   public void setRights(String lang, String value) {
      this.setLangAlt("rights", lang, value);
   }

   public String getRights() {
      return this.getRights((String)null);
   }

   public String getRights(String lang) {
      return this.getLangAlt(lang, "rights");
   }

   public void setSource(String value) {
      this.setValue("source", value);
   }

   public String getSource() {
      return this.getValue("source");
   }

   public void addSubject(String value) {
      this.addStringToBag("subject", value);
   }

   public String[] getSubjects() {
      return this.getStringArray("subject");
   }

   public void setTitle(String value) {
      this.setTitle((String)null, value);
   }

   public void setTitle(String lang, String value) {
      this.setLangAlt("title", lang, value);
   }

   public String getTitle() {
      return this.getTitle((String)null);
   }

   public String getTitle(String lang) {
      return this.getLangAlt(lang, "title");
   }

   public String removeTitle(String lang) {
      return this.removeLangAlt(lang, "title");
   }

   public void addType(String value) {
      this.addStringToBag("type", value);
   }

   public String[] getTypes() {
      return this.getStringArray("type");
   }
}
