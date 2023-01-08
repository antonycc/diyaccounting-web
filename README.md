Present
=======

The browser based application for https://diyaccounting.co.uk/

TODO
====

Open Source project
-------------------
```
Static site launch:
[ ] Build Docker image with diyaccounting-web-bootable-war (possibly continuing to use Tomcat10 in Spring boot for the external classpath)
[ ] Add sample zips to built image (currently in test-stock) and also sync from published Docker images.
[ ] Add Swagger
[ ] Run link checker 
[ ] Ensure the stage version prevents crawling
[ ] Docker config in gb-web to mount the static web files directly to allow for local development
[ ] gb-web-run-with-static-files published images for gb-web static site, mounts gb-web static files
[ ] Contributor guidelines
```

Major bugs
----------
* Google Search needs review data
* Product nav. occupies too much screen
* It needs to be more obvious that the Excel product us not suitable for mobile
* Scan for broken links
* CompanyAccountsProduct.md doesn't get generated - perhaps it takes to long to build the available periods
* VAT Inspection Article rendering broken link
```bash
# trailingBody
<p>
    The first step to keeping out of trouble is to understand the basics of the paperwork required. 
    The second step is to ensure accurate financial records are maintained and many types of 
    <a href="home.html">accounting software</a> a
    nd <a href="article.html?article=SmallBusinessAccountingSoftwareforFinancialControlArticle">bookkeeping
    software</a> can assist by at the very least producing a required audit trail to support the financial 
    figures entered on the quarterly<a href="feature.html?feature=VatReturnsFeature"> vat tax return.</a>
    <br>
```
* AccountingForVATWithMakingTaxDigitalArticle.md rendering broken link
```bash
<p>
    <span><img src="api/images?contentType=image/png&image=/uk/co/diyaccounting/ct/attachments/745013253/887783438.png" /></span>
</p>
```

Build Improvements:
```
[ ] support docker-compose by starting with one of these images: https://community.atlassian.com/t5/Bitbucket-questions/Docker-compose-and-pipelines/qaq-p/67913
[ ] add own build image Dockerfile to this repository
[ ] publish own build image in separate pipelines file
[ ] reference own image from default pipelines file and move installed packages to image
[ ] Test HTTP pass through download URL
[ ] Remove these two slashes: file://
``

Optimise CSS:
```
[ ] Unify CSS for:
  ** article
  ** product
  ** feature
  ** about
  ** history
```

XML and Freemarker deprecation:
```
[ ] Add email templates to CMS
[ ] Change the email templates to a simple string replacement templating technology
[ ] Remove xhtml bindings in the DIY Accounting DTOs (Keep them for cms-lib)
[ ] Generate the site map on CMS publish and deploy as a static artifact
[ ] Remove view resolver from  Spring configuration
[ ] Remove Freemarker from dependencies
[ ] Exclude XML dependencies when including cms-lib
[ ] Review maven dependency tree for possible pruning of low (or zero) use with heavy footprint dependencies
[ ] Refresh maven dependencies
[*] Release to stage without XML and Freemarker
```

Development environment set-up
==============================

Decrypt credentials (including settings.xml)
--------------------------------------------
Encryption of the deployment user's credentials
```bash
$ ./open-ssl-pk-enc.sh list-recipients
[recipients/] antony@mbp.new (PEM is available locally in /Users/antony/.ssh)   <--- At least one available recipient is required
$ ./decrypt.sh
$ source ./github-antonycc-keys.sh
$ ./mvnw --settings settings.xml clean spring-boot:run
...
2023-01-07T23:45:48.445+01:00  INFO 85585 --- [           main] u.c.d.web.SpringbootRestApiApplication   : Application for diyaccounting-web is logging at info.
2023-01-07T23:45:48.445+01:00  WARN 85585 --- [           main] u.c.d.web.SpringbootRestApiApplication   : Application for diyaccounting-web is logging at warning.
2023-01-07T23:45:48.445+01:00 ERROR 85585 --- [           main] u.c.d.web.SpringbootRestApiApplication   : Application for diyaccounting-web is logging at error.
2023-01-07T23:45:48.445+01:00  INFO 85585 --- [           main] u.c.d.web.SpringbootRestApiApplication   : System.getProperty("user.dir"): /Users/antony/projects/diyaccounting-web
2023-01-07T23:45:48.446+01:00  INFO 85585 --- [           main] u.c.d.web.SpringbootRestApiApplication   : Size of /Users/antony/projects/diyaccounting-web/target/classes/test-content/HomePage.md is 2998
2023-01-07T23:45:48.446+01:00  INFO 85585 --- [           main] u.c.d.web.SpringbootRestApiApplication   : Size of target/classes/test-content/HomePage.md is 2998

[In a separate terminal]

$ curl --head http://localhost:8080/content/page
HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sat, 07 Jan 2023 22:46:28 GMT

$
```

Maven build
-----------
```bash
$ ./mvnw --version
Apache Maven 3.8.6 (84538c9988a25aec085021c365c560670ad80f63)
Maven home: /Users/antony/.m2/wrapper/dists/apache-maven-3.8.6-bin/1ks0nkde5v1pk9vtc31i9d0lcd/apache-maven-3.8.6
Java version: 17.0.5, vendor: Homebrew, runtime: /usr/local/Cellar/openjdk@17/17.0.5/libexec/openjdk.jdk/Contents/Home
Default locale: en_GB, platform encoding: UTF-8
OS name: "mac os x", version: "13.0.1", arch: "x86_64", family: "mac"
$ source ./github-antonycc-keys.sh
$ ./mvnw --settings settings.xml clean install --activate-profiles with-assets
...
[INFO] --- maven-war-plugin:3.3.2:war (default-war) @ diyaccounting-web ---
[INFO] Packaging webapp
[INFO] Assembling webapp [diyaccounting-web] in [/Users/antony/projects/diyaccounting-web/target/diyaccounting-web-0.1-SNAPSHOT]
[INFO] Processing war project
[INFO] Copying webapp resources [/Users/antony/projects/diyaccounting-web/src/main/webapp]
[INFO] Building war: /Users/antony/projects/diyaccounting-web/target/diyaccounting-web-0.1-SNAPSHOT.war
[INFO] 
[INFO] --- spring-boot-maven-plugin:3.0.1:repackage (repackage) @ diyaccounting-web ---
[INFO] Replacing main artifact with repackaged archive
[INFO] 
[INFO] --- maven-install-plugin:3.0.1:install (default-install) @ diyaccounting-web ---
[INFO] Installing /Users/antony/projects/diyaccounting-web/pom.xml to /Users/antony/.m2/repository/uk/co/diyaccounting/web/diyaccounting-web/0.1-SNAPSHOT/diyaccounting-web-0.1-SNAPSHOT.pom
[INFO] Installing /Users/antony/projects/diyaccounting-web/target/diyaccounting-web-0.1-SNAPSHOT.war to /Users/antony/.m2/repository/uk/co/diyaccounting/web/diyaccounting-web/0.1-SNAPSHOT/diyaccounting-web-0.1-SNAPSHOT.war
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  8.439 s
[INFO] Finished at: 2023-01-07T23:48:31+01:00
[INFO] ------------------------------------------------------------------------
$
```

TODO: Add testing with containerised app
----------------------------------------


Encrypt generated credentials
-----------------------------
Encryption of the deployment user's credentials
```bash
$ ./open-ssl-pk-enc.sh list-recipients
[recipients/] antony@mbp.new (PEM is available locally in /Users/antony/.ssh)   <--- At least one available recipient is required
$ ./encrypt.sh
$
```

Add certificate to recipients list
----------------------------------
Add and generate (optional) a certificate for the recipients list:
```bash
$ ./open-ssl-pk-enc.sh list-recipients | grep 'available' || echo "At least one locally available recipient is required."
At least one locally available recipient is required.
~/projects/present % ./open-ssl-pk-enc.sh list-available-keypairs
[/Users/antony/.ssh/] /Users/antony/.ssh/antony@mbp.new.pem?} (.pem format, installed to recipients)
[/Users/antony/.ssh/] /Users/antony/.ssh/my-new-keypair.pem (.pem format)
[/Users/antony/.ssh/] /Users/antony/.ssh/id_old_rsa (RSA format)
$ ./open-ssl-pk-enc.sh  
Usage:
  ./open-ssl-pk-enc.sh generate-keypair <keypair name>
  ./open-ssl-pk-enc.sh list-available-keypairs
  ./open-ssl-pk-enc.sh list-recipients
  ./open-ssl-pk-enc.sh add-recipient <.pem filename>
  ./open-ssl-pk-enc.sh remove-recipient <.pem filename>
  ./open-ssl-pk-enc.sh encrypt
  ./open-ssl-pk-enc.sh decrypt
  ./open-ssl-pk-enc.sh decrypt-to-env
$ ./open-ssl-pk-enc.sh generate-keypair my-new-keypair
Generating RSA private key, 2048 bit long modulus
..........................................................................+++++
..............................................+++++
e is 65537 (0x10001)
Enter pass phrase for my-new-keypair.pem:
Verifying - Enter pass phrase for my-new-keypair.pem:
-rw-------  1 antony  staff  1751 Jan  1 01:21 /Users/antony/.ssh/my-new-keypair.pem
~/projects/present % ./open-ssl-pk-enc.sh list-available-keypairs
[/Users/antony/.ssh/] /Users/antony/.ssh/antony@mbp.new.pem?} (.pem format, installed to recipients)
[/Users/antony/.ssh/] /Users/antony/.ssh/my-new-keypair.pem (.pem format)
[/Users/antony/.ssh/] /Users/antony/.ssh/id_old_rsa (RSA format)
$ ./open-ssl-pk-enc.sh add-recipient /Users/antony/.ssh/my-new-keypair.pem
Found .pem "/Users/antony/.ssh/my-new-keypair.pem" extracting the public key and adding to "recipients"
Enter pass phrase for /Users/antony/.ssh/my-new-keypair.pem:
writing RSA key
[recipients/] antony@mbp.new (PEM is available locally in /Users/antony/.ssh)
[recipients/] my-new-keypair (PEM is available locally in /Users/antony/.ssh)
$ ./open-ssl-pk-enc.sh list-recipients                                    
[recipients/] antony@mbp.new (PEM is available locally in /Users/antony/.ssh)
[recipients/] my-new-keypair (PEM is available locally in /Users/antony/.ssh)
$
```

Python environment for automated testing
========================================
TODO: Review - this hasn't been run since the Spring Boot migration.
First time Python set-up:
```bash
brew install python3
python3 -m venv ./venv
source ./venv/bin/activate
pip install selenium chromedriver-binary
...
```

Content replication using Python:
```bash
source ./venv/bin/activate
pip install -r ./requirements.txt
./build-tool-md-get-content.sh
...
```

Tear down after library changes:
```bash
./build-tool-freeze-requirements.sh > ./requirements.txt - currently removes everything because the python files are empty
# For all modules: pip freeze > ./requirements.txt
deactivate
git add --all
git commit -m 'Updated requirements'
git push origin HEAD:master
```


Local running from pre-built jar and external doc root
======================================================
TODO: This hasn't been reviewed since the Spring Boot migration.

DIY Accounting can run locally in Tomcat with content and state from the local file system.
To help with editing page artifacts such as HTML and JS, these are served from a nginx mounting `./src/main/webapp`

The containers are:
* `content` - metadata and assets served over http
* `app` - the DIY Accounting web app gb-web from a pre-built jar on port `8080`
* `web` - serving mounted DIY Accounting's HTML on port `8081` from a workspace folder and proxying APIs to `app`

To run DIY Accounting locally:
```bash
$ ./build-tool-mvn-package-in-docker.sh
(With 4 CPUs an 16GB ram, build time 12 minutes 53 seconds)
$ docker compose build
$ docker compose up
```
Then load the DIY Accounting index page http://localhost:8081/home.html on nginx.

The file above is served from:
```bash
$ ls -l ./src/main/webapp/home.html
-rw-r--r--  1 antony  staff  8787  7 Mar 15:41 ./src/main/webapp/home.html
```
Try editing the text "Know what you're looking for?". This appears top left and is hard coded in the HTML. A change
should appear with a page refresh because nginx is serving the doc root directly from `./src/main/webapp`.

Other links:
* DIY Accounting Home page gb-web on Tomcat in Docker: http://localhost:8080/gb-web/home.html (this from a pre-built app)
* Sample webapp on Tomcat in Docker: http://localhost:8080/sample/
* API call to curl gb-web on Tomcat in Docker: http://localhost:8080/gb-web/api/page
* DIY Home page as static HTML in nginx doc root in Docker: http://localhost:8081/home.html

See also:
* [Deploy war from location](https://github.com/ardydedase/docker-tomcat-war/blob/master/docker compose.yml)
* [nginx reverse proxy](https://stackoverflow.com/questions/52823279/how-to-nginx-reverse-proxy-outside-of-docker-to-proxy-pass-to-docker-containers)
* [Parameterise Docker compose](https://stackoverflow.com/questions/43544328/pass-argument-to-docker compose)
* how-to-create-a-new-bucket-and-add-files-to-a-specific-folder-using-local-stack-using-docker-414a1d035d19

 Ok: http://localhost:8080/gb-web/feature.html?feature=VatReturnsFeature&product=CompanyAccountsProduct
!Ok: http://localhost:8080/gb-web/feature.html?feature=SalesSpreadsheetFeature

# Testing and running the webapp with a bundled Tomcat
See ./gb-web/pom.xml, tomcat profile.
```bash
$ source ./github-antonycc-keys.sh
$ mvn --settings settings.xml clean install
$ cd gb-web
$ mvn --settings ../settings.xml clean  spring-boot:run
```
Then go to http://localhost:8080/home.html

# Attach a shell
```bash
$ docker exec -it gb-web_content_1 /bin/bash

```

Releasing
=========
TODO: This hasn't been reviewed since the GitHub migration.

# two stage release
```bash
$ source ./github-antonycc-keys.sh
$ mvn --settings settings.xml release:prepare
$ mvn --settings settings.xml release:perform
```

# two stage release re-tried
```bash
$ source ./github-antonycc-keys.sh
$ mvn --settings settings.xml release:prepare -Dresume=false
$ mvn --settings settings.xml release:perform
```

# full console run - the middle mnv line is suitable for a CI server with it's own workspace
```bash
$ source ./github-antonycc-keys.sh
$ mvn --settings settings.xml --batch-mode clean release:clean release:prepare -DdryRun=true
$ git reset --hard HEAD && find . -name "*.next" -type f -exec rm "{}" \; && find . -name "*.tag" -type f -exec rm "{}" \;
$ mvn --settings settings.xml --batch-mode clean release:clean release:prepare
$ mvn release:clean
```

Backlog
=======

MDCMS Enhancements:
```
[ ] Remove bundles as a conept with CommercialProduct beimg saleable (start with Bundle.java and Product.bundleNames)
[ ] Clean all the XHTML examples to use a simple HTML page
[ ] Switch HandlebarsJS tenplates to use {{products}} instead of {{each products}}
[ ] Use "XxxxPage" as the pageId not "page" - HTML and ApiController change
[ ] Use "PageXxxx" as the resource name
[ ] Link the page update timestamp metadata to the content file updates 
[ ] Change a docker based app to pull the content from HTTP instead of a filesystem resource
urn:mdcms:https:mysite.com/my-bucket/someobj
```

SEO:
```
[ ] Do we need Product.conversionCode for Google AdWords ?
[ ] Review all articles
[ ] Put the user guides online as content
[ ] Publish PDF guides to the stock folder
[ ] Capture review data (TrustPilot)
[ ] Reviews page (add onto the top of our customers)
[ ] Product nav. occupies too much screen
[ ] Scan for broken links
[ ] Add SEO static analysis tests to build pipeline
[ ] Check Jay's list
[ ] Add Cookie banner
[ ] Add Something for GDPR
[ ] Create multi-language support with /eng & /nld (check ISO) template folders and # eng & # nld MD sections
[ ] Script generation of alternate language content
[ ] Identify and apply the optimal strategy for incoming never valid links
[ ] Identify and apply the optimal strategy for purging outdated links
```
