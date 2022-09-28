package org.apache.fop.area;

import java.util.ArrayList;
import java.util.List;

public class PageSequence extends AreaTreeObject {
   private List pages = new ArrayList();
   private LineArea title;
   private String language;
   private String country;

   public PageSequence(LineArea title) {
      this.setTitle(title);
   }

   public LineArea getTitle() {
      return this.title;
   }

   public void setTitle(LineArea title) {
      this.title = title;
   }

   public void addPage(PageViewport page) {
      this.pages.add(page);
   }

   public int getPageCount() {
      return this.pages.size();
   }

   public PageViewport getPage(int idx) {
      return (PageViewport)this.pages.get(idx);
   }

   public boolean isFirstPage(PageViewport page) {
      return page.equals(this.getPage(0));
   }

   public String getLanguage() {
      return this.language;
   }

   public void setLanguage(String language) {
      if ("none".equals(language)) {
         this.language = null;
      } else {
         this.language = language;
      }

   }

   public String getCountry() {
      return this.country;
   }

   public void setCountry(String country) {
      if ("none".equals(country)) {
         this.country = null;
      } else {
         this.country = country;
      }

   }
}
