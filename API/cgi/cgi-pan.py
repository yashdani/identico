#!/usr/bin/python

import requests
from bs4 import BeautifulSoup
import urllib3
import urllib
import logging
import httplib
import json
import re
import string
import random
import sys

import cgi


form = cgi.FieldStorage()
pan = form['pan'].value

print "Content-type:text/html\n"
#print pan

logging.basicConfig(filename='temp.log',level=1)
logging.captureWarnings(True)

hb = {
'User-Agent':'SomeUserAgent',
'Referer':'https://incometaxindiaefiling.gov.in/e-Filing/Services/KnowYourJurisdiction.html',
'Connection':'keep-alive'}

url="https://incometaxindiaefiling.gov.in/e-Filing/Services/KnowYourJurisdictionLink.html"	

r=requests.get(url,headers=hb,verify=False)

cook=r.cookies.get_dict()   

jid= cook ['JSESSIONID']
itd= cook ['ITDEFILING']

def id_generator(size=6, chars=string.ascii_uppercase + string.digits):
	return ''.join(random.choice(chars) for _ in range(size))
captchaname=id_generator()

h = {
'User-Agent':'SomeUserAgent',
'Accept': 'image/png,image/*;q=0.8,*/*;q=0.5',
'Accept-Encoding':'gzip, deflate',
'Accept-Language': 'en-US,en;q=0.5',
'Range': 'bytes=0-',
'Referer':'https://incometaxindiaefiling.gov.in/e-Filing/Services/KnowYourJurisdictionLink.html',
'Connection':'keep-alive'}

picturecaptchalink='https://incometaxindiaefiling.gov.in/e-Filing/CreateCaptcha.do?0.457'
cookie={'JSESSIONID':str(jid),'ITDEFILING':str(itd)}

rp=requests.get(picturecaptchalink,verify=False,headers=h,cookies=cookie)

h2 = {
'User-Agent':'SomeUserAgent',
'Content-Length': '0',
'Accept-Encoding':'audio/webm,audio/ogg,audio/wav,audio/*;q=0.9,application/ogg;q=0.7,video/*;q=0.6,*/*;q=0.5',
'Accept-Language': 'en-US,en;q=0.5',
'Range': 'bytes=0-',
'Referer':'https://incometaxindiaefiling.gov.in/e-Filing/Services/KnowYourJurisdiction.html',
'Connection':'keep-alive'}

cookie={'JSESSIONID':str(jid),'ITDEFILING':str(itd)}
audiocaptchalink='https://incometaxindiaefiling.gov.in/e-Filing/CreateAudioCaptcha.do?0.348'
wavfile=requests.get(audiocaptchalink,verify=False,headers=h2,cookies=cookie)

with open(captchaname+'.wav', 'wb') as test:
    test.write(wavfile.content)



sfile= open(captchaname+'.wav', "rb")

response = requests.post("https://stream.watsonplatform.net/speech-to-text/api/v1/recognize",
         auth=("7d8c6f93-8841-43cf-b438-f864c593f622", "olZibUYfd9YK"),
         headers = {"content-type": "audio/wav"},
         data=sfile
         )

jsonProfile = json.loads(response.text)

inwords=jsonProfile['results'][0]['alternatives'][0]['transcript']
inwords=str(inwords)

def words2int(inwords):
	inint =inwords.replace('one', '1').replace('two', '2').replace('three', '3').replace('four', '4').replace('five', '5').replace('six', '6').replace('seven', '7').replace('eight', '8').replace('nine', '9').replace('zero', '0').replace(' ','')
	return inint

captcha=words2int(inwords)

#print captcha

hf={
	"User-Agent": "SomeUserAgent",
	"Accept":"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
	"Accept-Language":" en-US,en;q=0.5",
	"Accept-Encoding":"gzip,deflate",
	"Referer":"https://incometaxindiaefiling.gov.in/e-Filing/Services/KnowYourJurisdictionLink.html",
	"Connection":"keep-alive",
	"Content-Type":"application/x-www-form-urlencoded",
	"Content-Length":"54"}

p={'requestId':'','panOfDeductee':pan,'captchaCode':captcha}

response = requests.post("https://incometaxindiaefiling.gov.in/e-Filing/Services/KnowYourJurisdiction.html",
         headers = hf,
         cookies=cookie,
         data=p,
         verify=False
         )

stage2= response.text
#print stage2
soup = BeautifulSoup(stage2)
pan_data=[]

all_td =soup.find_all('td')
all_td = filter(lambda x: x.attrs == {}, all_td)

for data in all_td:
	pan_data.append(data.text)

#print pan_data

T = 0
data = {}
length=len(pan_data)/2
while(T<length):
	data[pan_data[0]] = pan_data[1].replace("\t", "").replace("\n", "").encode('UTF-8')	
	pan_data = pan_data[2:]
	T = T + 1

print json.dumps(data)




