<?xml version="1.0" encoding="UTF-8"?>
<?xml-stylesheet type="text/xsl" href="sitemap.xsl"?>
<urlset
      xmlns="http://www.sitemaps.org/schemas/sitemap/0.9"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
      xsi:schemaLocation="http://www.sitemaps.org/schemas/sitemap/0.9 http://www.sitemaps.org/schemas/sitemap/0.9/sitemap.xsd">
   
   <!-- Default document -->
   <url>
      <loc>https://www.diyaccounting.co.uk/</loc>
      <priority>1.0</priority>
   </url>

   <!-- Navigational pages -->
   <url>
      <loc>https://www.diyaccounting.co.uk/home.html</loc>
   </url>
   <url>
      <loc>https://www.diyaccounting.co.uk/about.html</loc>
   </url>
   <url>
      <loc>https://www.diyaccounting.co.uk/ourcustomers.html</loc>
   </url>
   <url>
      <loc>https://www.diyaccounting.co.uk/support.html</loc>
   </url>
   <url>
      <loc>https://www.diyaccounting.co.uk/whatsnew.html</loc>
   </url>
   <url>
      <loc>https://www.diyaccounting.co.uk/contact.html</loc>
   </url>
   <url>
      <loc>https://www.diyaccounting.co.uk/history.html</loc>
   </url>
   <url>
      <loc>https://www.diyaccounting.co.uk/articles.html</loc>
   </url>
   <url>
      <loc>https://www.diyaccounting.co.uk/products.html</loc>
   </url>
   <url>
      <loc>https://www.diyaccounting.co.uk/get.html</loc>
   </url>

   <!-- Product pages -->
   <#list products as product>
      <url>
         <loc>https://www.diyaccounting.co.uk/product.html?product=${product.name}</loc>
         <priority>0.9</priority>
      </url>
   </#list>
   
   <!-- Feature pages -->
   <#list features as feature>
      <url>
         <loc>https://www.diyaccounting.co.uk/feature.html?feature=${feature.name}</loc>
         <priority>0.8</priority>
      </url>
   </#list>

   <!-- Article pages -->
   <#list articles as article>
      <url>
         <loc>https://www.diyaccounting.co.uk/article.html?article=${article.name}</loc>
      </url>
   </#list>
</urlset>
