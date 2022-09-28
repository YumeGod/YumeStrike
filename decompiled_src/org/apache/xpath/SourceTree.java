package org.apache.xpath;

public class SourceTree {
   public String m_url;
   public int m_root;

   public SourceTree(int root, String url) {
      this.m_root = root;
      this.m_url = url;
   }
}
