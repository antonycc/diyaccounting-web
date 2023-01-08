#!/usr/bin/env bash
# Purpose: Reduce the output of pip freeze to only only imported modules
# Usage: ./build-tool-freeze-requirements.sh > ./requirements.txt
# Usage: ./build-tool-freeze-requirements.sh ./some-other > ./some-other/requirements.txt
# Usage: ./build-tool-freeze-requirements.sh < ./requirements.txt > ./some-other/requirements.txt
if [[ -n "${1}" ]] ;
then
  source_dir="${1}/"
else
  source_dir="./"
fi
if [ -t 0 ] ;
then
  installed_modules=$(pip freeze)
else
  installed_modules=$(cat)
fi
imported_modules=$(cat "${source_dir?}"*.py | grep '^import \|^from ' | sed 's/^[^[:space:]]*[[:space:]]\([^[:space:]|\.]*\).*/\1/' | sort | uniq)
# Supported lambda runtime: Python 3.8 / boto3-1.15.16 botocore-1.18.16 / Amazon Linux 2
#echo "boto3==1.15.16"
#echo "botocore==1.18.16"
echo "${installed_modules?}" \
  | sed 's/==/>=/' \
  | grep -v '^boto3>=\|^botocore>=' \
  | while read -r module ; do [[ $imported_modules =~ ${module/[>|=]*.*/} ]] && echo "${module?}" ; done \
  | sort \
  | uniq