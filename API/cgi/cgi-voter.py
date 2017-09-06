import requests
from bs4 import BeautifulSoup
import httplib
import json
import re
import string

import cgi


print "Content-type:text/html\n"

form = cgi.FieldStorage()
voterid = form['voterid'].value
state = form['form'].value

h1 = {
'User-Agent':'SomeUserAgent',
'Accept':'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
'Accept-Language':'en-US,en;q=0.5',
'Accept-Encoding':'gzip, deflate',
'Connection':'keep-alive'}
print 'Request 1 Starts'
url1="http://electoralsearch.in/index.jsp"	

r=requests.get(url1,headers=h1)

cook=r.cookies.get_dict()   

jid= cook ['JSESSIONID']
print 'Request 1 Regex Process'
rtxt=r.text
soup=BeautifulSoup(rtxt)
all_script =soup.find_all('script')
all_script  = filter(lambda x: x.attrs == {}, all_script)
text= all_script[1]
text=str(text)
pattern=r"'[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}'"
match=re.search(pattern,text)
rere= match.group(0).replace('\'','')

print 'Request 1 complete'

h2 = {
'User-Agent':'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_3; rv:28.0) Gecko/20100101 Firefox/28.0',
'Accept': 'application/json, text/plain, */*',
'Accept-Encoding':'gzip, deflate',
'Accept-Language': 'en-US,en;q=0.5',
'Range': 'bytes=0-',
'Referer':' http://electoralsearch.in/index.jsp',
'Connection':'keep-alive'}
	
cookie={'JSESSIONID':str(jid),'runOnce':'true'}
print 'Request 2 starts'
url2='http://electoralsearch.in/Search?epic_no='+str(voterid)+'&page_no=1&results_per_page=10&reureureired='+str(rere)+'&search_type=epic&state='+str(state)

response=requests.get(url2,headers=h2,cookies=cookie)

data= response.text

print json.dumps(data)