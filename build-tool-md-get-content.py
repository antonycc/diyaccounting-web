#!/usr/bin/env python3
# Purpose: get the md content to stdout
# Usage: ./build-tool-md-get-content.py
import logging
import os
import sys
from selenium import webdriver
import chromedriver_binary  # Adds chromedriver binary to path
from selenium.webdriver.chrome.options import Options
import argparse


# Get page content after JS execution
def main(p_url):
    driver = get_webdriver()
    print_content(driver, p_url)


# Get headless Chrome
# Use default headed version: driver = webdriver.Chrome()
# Update path using: find ./venv -type f -name 'chromedriver'
def get_webdriver():
    chrome_options = Options()
    chrome_options.add_argument("--headless")
    chrome_options.binary_location = '/Applications/Google Chrome.app/Contents/MacOS/Google Chrome'
    chromedriver_path = './venv/lib/python3.9/site-packages/chromedriver_binary/chromedriver'
    driver = webdriver.Chrome(executable_path=chromedriver_path, options=chrome_options)
    driver.set_page_load_timeout(60)
    return driver


# Extract href and a.text from:
#     <li><a href="page.html?page=about">about</a></li>def print_all_links(driver, url):
# Iterate through ul.li[].a and print [a.text] [a@href]
def print_content(driver, url):
    driver.get(url)
    pre = driver.find_element_by_id(id_='content')
    print(f'{pre.text}')


if __name__ == '__main__':
    env_logging_level = logging.getLevelName(os.getenv('LOGGING_LEVEL', 'ERROR'))
    logging.basicConfig(level=env_logging_level)
    parser = argparse.ArgumentParser()
    parser.add_argument('--url')
    args = parser.parse_args()
    sys.exit(main(p_url=args.url))
