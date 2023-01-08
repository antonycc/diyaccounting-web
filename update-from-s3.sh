#!/usr/bin/env bash
# Purpose: Replace the current content, catalogue and crm export with the latest from S3
# TODO: Extract the content and packages from the published docker images.
# TODO: Obtain a sample of zips for packages.

aws s3 cp 's3://diyaccounting-polycode-archive/ct-md-latest.zip' 'src/main/resources/ct-md-latest.zip' \
  && rm -rf 'src/main/resources/content' \
  && unzip 'src/main/resources/ct-md-latest.zip' -d 'src/main/resources' \
  ;

tempFile=$(mktemp)
aws s3 cp 's3://diyaccounting-polycode-gb-stock/catalogue.txt' "${tempFile?}" \
  && mv -f "${tempFile?}" 'src/main/resources/test-catalogue/catalogue.txt' \
  ;

tempFile=$(mktemp)
rm -f 'gb-web-local/src/main/resources/catalogueBundlePrices.properties'
aws s3 cp 's3://diyaccounting-polycode-gb-stock/catalogueBundlePrices.properties' "${tempFile?}" \
  && mv -f "${tempFile?}" 'src/main/resources/test-catalogue/catalogueBundlePrices.properties' \
  ;

tempFile=$(mktemp)
aws s3 cp 's3://diyaccounting-polycode-gb-stock/catalogueNameContentItemName.properties' "${tempFile?}" \
  && mv -f "${tempFile?}" 'src/main/resources/test-catalogue/catalogueNameContentItemName.properties' \
  ;
