#!/usr/bin/env python
# coding: utf-8

# In[1]:


# Uncomment these if you are running this for the first time

#curl https://bootstrap.pypa.io/get-pip.py -o get-pip.py

#python get-pip.py


# In[2]:


#/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install.sh)"


# In[3]:


#IF ON MAC, UNCOMMENT

#brew cask install chromedriver


# In[4]:


import bs4
from bs4 import BeautifulSoup as soup
import pandas as pd
import numpy as np
from datetime import datetime

# In[5]:


from selenium import webdriver
from selenium.webdriver.support.ui import Select
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.keys import Keys


# In[6]:


print(' ')
print('Enter NAICS Code(s):','(enter codes with comma in between and no spapes)')
naics_input = input('Code(s): ')
naics_str = str(naics_input)

print(' ')
print('Enter Keyword(s):','(enter keywords with comma in between and no spapes)')
keyword_input = input('Keyword(s): ')
keyword = str(keyword_input)

if ',' in keyword:
    keyword = keyword.split(',')
else:
    keyword = [keyword]
print(keyword)

dates = {'Inactive Date': 0, 'Published Date': 0, 'Updated Date': 0}

def set_dates():
    for date in dates:
        print(' ')
        print(date + ': (enter number for option you want)')
        print('[0] Any Time')
        print('[1] Past Day')
        print('[2] Past 2 Days')
        print('[3] Past 3 Days')
        print('[4] Past Week')
        print('[5] Past Month')
        print('[6] Past 3 Months')
        print('[7] Past Year')
        try:
            dates[date] += int(input('Time Period: '))
        except:
            print('ERROR: You must enter a number 0-7')
            dates[date] += int(input('Time Period: '))

set_dates()
inactive_date, published_date, updated_date = [dates[i] for i in dates]

# In[8]:

today = datetime.today().strftime('%m/%d/%y')
m_today, d_today, y_today = today.split('/')
today_list = today.split('/')

print(' ')
print('Response/Date Offers Due: (enter number for option you want)')
print('[0] Any Time')
print('[1] Custom Day')
custom_pref = input('Due Date Preference: ')

if int(custom_pref):
    print(' ')
    print('What would you like the last possible due date be (pick a date in the future, give answer as "MM/DD/YY")')
    custom_day = input('Custom Date: ')
    if len(custom_day) != 8 or len(custom_day.split('/')) !=3:
        print('ERROR: Incorrect Format, Try Again')
        custom_day = input('Custom Date: ')

    m_custom, d_custom, y_custom = custom_day.split('/')

    today_time_dict = {'day': [d_today], 'month': [m_today], 'year': ['20' + y_today]}
    custom_time_dict = {'day': [d_custom], 'month': [m_custom], 'year': ['20' + y_custom]}

else:
    custom_time_dict = 0

option = webdriver.ChromeOptions()

option.add_argument('--ignore-certificate-errors')
option.add_argument('--incognito')
option.add_argument('--headless')

driver = webdriver.Chrome(options=option)

# In[7]:

wait = WebDriverWait(driver, 10)

# In[9]:


option_pairs = [[0, 'Any Time'], [1, 'Past day'], [2, 'Past 2 days'], [3, 'Past 3 days'], [4, 'Past week'],
               [5, 'Past month'], [6, 'Past 3 months'], [7, 'Past year']]


# In[10]:


time_filters = {0: inactive_date, 1: published_date, 2: updated_date} #, 3: due_data}


option_pairs = {0: 'Any Time', 1: 'Past day', 2: 'Past 2 days', 3: 'Past 3 days', 4: 'Past week',
               5: 'Past month', 6: 'Past 3 months', 7: 'Past year'}


time_filters = {your_key: int(time_filters[your_key]) for your_key in time_filters if time_filters[
    your_key] != 0}


def run_filters(link, filters, keywords, custom_dict):
    driver.get(link)
    if not filters and not keywords and not custom_dict:
        return soup(driver.page_source, 'html5lib')
    for i in filters:
        select = Select(driver.find_elements_by_xpath('//select[@id="time span"]')[i])
        select.select_by_visible_text(option_pairs[filters[i]])
        if i == 0:
            submit_button = driver.find_elements_by_xpath(
                '//button[@class="sam button tertiary small ng-star-inserted"]')[2]
        else:
            submit_button = driver.find_elements_by_xpath(
                '//button[@class="sam button tertiary small ng-star-inserted"]')[1]

        submit_button.click()

        try:
            wait.until(EC.presence_of_element_located((By.CLASS_NAME, 'row')))
        except:
            print(' ')
            print('No Results For Given Search')
            return

        #keyword search

    if type(custom_dict) == dict:

        select = Select(driver.find_elements_by_xpath('//select[@id="time span"]')[3])
        select.select_by_visible_text('Custom Date')

        wait.until(EC.element_to_be_clickable((By.XPATH, '//div[@class = "usa-date-of-birth date-group"]')))

        driver.find_elements_by_xpath('//div[@class = "usa-date-of-birth date-group"]')[0].click()

        for time in today_time_dict:
            driver.find_elements_by_xpath("//input[@name = 'date " + time +  "']")[0].send_keys(today_time_dict[time])

        driver.find_elements_by_xpath('//div[@class = "usa-date-of-birth date-group"]')[1].click()

        for time in custom_dict:
            driver.find_elements_by_xpath("//input[@name = 'date " + time +  "']")[1].send_keys(custom_dict[time])

        which_box = len(filters) + 5

        filter_button = driver.find_elements_by_xpath(
           '//button[@class="sam button tertiary small ng-star-inserted"]')[which_box]

        filter_button.click()

        try:
            print(driver.page_source)
            wait.until(EC.presence_of_element_located((By.CLASS_NAME, 'row')))
        except:
            print(' ')
            print('No Results For Given Search')
            return

    for kw in keywords:
        wait.until(EC.element_to_be_clickable((By.CSS_SELECTOR, 'textarea#keywordSearchName-ac-textarea'))).click()
        wait.until(EC.element_to_be_clickable((By.CSS_SELECTOR, 'textarea#keywordSearchName-ac-textarea'))).send_keys(kw)
        driver.find_element_by_id("keywordSearchName-ac-textarea").send_keys(Keys.RETURN)

        print(' ')

        try:
            wait.until(EC.presence_of_element_located((By.CLASS_NAME, 'row')))
        except:
            print(' ')
            print('No Results For Given Search')
            return

    #due date
    #selected_source = driver.page_source
    #selected_soup = soup(selected_source, 'html5lib')
    return #selected_soup


# In[15]:


#dd = 'https://beta.sam.gov/search?index=opp&naics='+ naics_str +'&page=1'
#selected_soup = run_filters(dd, time_filters)


# In[16]:


def webscrape(naics, counter_start = 0): #source, counter_start = 0):

    naics = int(naics)

    wait

    try:
        wait.until(EC.presence_of_element_located((By.XPATH, '//list-results-message')))
        selected_source = driver.page_source
        selected_soup = soup(selected_source, 'html5lib')
        source = selected_soup
        results_shown = source.find('list-results-message').text

    except:
        print(' ')
        print('No Results For Given Search')
        return

        print(driver.current_url)
        print('FAILED, TRY AGAIN')
        #run_filters(dd, time_filters)
        return 'FAILED, TRY AGAIN'#webscrape(naics, counter_start)

    if ' ' in results_shown[12:14]:
        first_results =  int(results_shown[12])
        total_results = int(results_shown[17])
        num_pages = 1
    else:
        first_results =  int(results_shown[12:14])
        total_results = int(results_shown[18:21])
        if (int(results_shown[18:21]) % 10) != 0:
            num_pages = total_results // first_results + 1
        else:
            num_pages = total_results // first_results

    scraped = pd.DataFrame({'Contract Name': [], 'NAICS': [], 'Contract Link':[],
                            'Description': [], 'Department/Ind. Agency': [], 'Sub-tier': [], 'Office': [],
                            'Notice ID': [], 'Current Date Offers Due': [], 'Last Updated Date': [],
                            'Last Published Date': [],'Type': [], 'Current Response Date': [], 'Awardee': [],
                            'Product Service Code': []
                           })

    counter = counter_start

    print('Total of ' + str(total_results) + ' results.')
    print(' ')

    for page in range(1, num_pages + 1):

        page_num_len = len(str(page))
        beta_sam = driver.current_url.split('page=')[0] + 'page=' + str(page) + driver.current_url.split(
            'page=')[1][page_num_len:]

        driver.get(beta_sam)

        wait.until(EC.presence_of_element_located(
            (By.XPATH, '//list-results-message[@class="ng-tns-c3-1 ng-star-inserted"]')))
        sam_source = driver.page_source
        sam_soup = soup(sam_source, 'html5lib')

        rows = sam_soup.findAll(class_ = 'sam-ui grid')

        print(str(round(100 * (page - .5)/num_pages, 2)) + '% Done with ' + str(
            naics) + ', total of ' + str(num_pages) + ' pages')

        for row in rows:

            #outer page
            scraped.loc[counter, 'Contract Name'] = row.div.div.h3.a.text
            scraped.loc[counter, 'Contract Link'] = ('https://beta.sam.gov' + row.div.div.h3.a['href']).split('?index=')[0]

            description = [i.text for i in row.div.div.span.findAll('p')]

            if len(description) == 1:
                description = description.pop()
            elif len(description) == 0:
                description = None
            else:
                description = ', '.join(description)

            scraped.loc[counter, 'Description'] = description

            for i in row.findAll(class_ = 'sam-ui small list')[0].findAll('li'):
                if i.a:
                    scraped.loc[counter, i.strong.text.strip()] = i.a.text.strip()
                else:
                    scraped.loc[counter, i.strong.text.strip()] = i.span.text.strip()

            scraped.loc[counter, 'Office'] = row.div.div.ul.findAll('span')[0].text

            for j in row.findAll(class_ = 'four wide column')[0].findAll('li'):
                if j.span.text.strip() != 'Contract Opportunities':
                    scraped.loc[counter, j.strong.text.strip()] = j.span.text.strip()

            #inner page
            driver.get(scraped.loc[counter, 'Contract Link'])

            WebDriverWait(driver, 10).until(EC.presence_of_element_located(
                (By.XPATH, '//div[@class="sam-ui padded raised segment"]')))
            inner_source = driver.page_source
            inner_soup = soup(inner_source, 'html5lib')

            scraped.loc[counter, 'Product Service Code'] = inner_soup.find("li", {
                "id": "classification-classification-code"}).text[22:]
            scraped.loc[counter, 'NAICS'] = inner_soup.find("li", {
                "id": "classification-naics-code"}).text[12:]
            scraped.loc[counter, 'Primary Point of Contact (PPOC)'] = inner_soup.find("li", {
                "id": "contact-primary-poc-full-name"}).text
            scraped.loc[counter, 'PPOC Email'] = inner_soup.find("li", {
                "id": "contact-primary-poc-email"}).a['href']
            try:
                scraped.loc[counter, 'PPOC Phone'] = inner_soup.find("li", {
                    "id": "contact-primary-poc-phone"}).text[16:]
            except:
                scraped.loc[counter, 'PPOC Phone'] = np.nan

            driver.back()

            counter += 1

        #progress report
        print(str(round(100 * page/num_pages, 2)) + '% Done with ' + str(naics) + ', total of ' + str(
            num_pages) + ' pages')

    #if it is too short
    if scraped.shape[0] != int(total_results):
        print('ERROR: Trying again now')
        print('Length is ' + str(scraped.shape[0]) + ', should be ' + total_results)
        return webscrape(naics, counter_start)
    scraped.fillna('None Given', inplace = True)
    return scraped

# In[17]:


def run_webscrape(naics):
    master_scraped = pd.DataFrame({'Contract Name': [], 'NAICS': [], 'Contract Link':[],
                                    'Description': [], 'Department/Ind. Agency': [], 'Sub-tier': [], 'Office': [],
                                    'Notice ID': [], 'Current Date Offers Due': [], 'Last Updated Date': [],
                                    'Last Published Date': [],'Type': [], 'Current Response Date': [], 'Awardee': []
                                   })
    if ',' in naics:
        naics = [int(i) for i in naics.split(',')]
        for code in naics:
            print(' ')
            print('Scraping ' + str(code))
            dd = 'https://beta.sam.gov/search?index=opp&naics='+ str(code) +'&page=1'
            selected_soup = run_filters(dd, time_filters, keyword, custom_time_dict)
            wait
            master_scraped = master_scraped.append(
            webscrape(code, selected_soup, master_scraped.shape[0]))
        return master_scraped
    else:
        dd = 'https://beta.sam.gov/search?index=opp&naics='+ naics +'&page=1'
        selected_soup = run_filters(dd, time_filters, keyword, custom_time_dict)
        return webscrape(naics)#, selected_soup)


# In[18]:


scraped = run_webscrape(naics_str)
scraped


# In[ ]:

try:
    scraped.loc[1,:]
    with pd.ExcelWriter('webscraped.xlsx') as writer:
        scraped.to_excel(writer, sheet_name='Sales Data')

except:
    None
# In[ ]:
