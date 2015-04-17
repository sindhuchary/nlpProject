#!/usr/bin/python
#usage: "parser.py <file>" 
#output: parsed text to stdout
import sys
import re

j = len(sys.argv) 
for i in range(1, j):   
    file = open(sys.argv[i], 'r')
    counter = 0 
    num_lines = sum(1 for line in file) 
    file = open(sys.argv[i], 'r')
    a = 0
    b = 1
    c = 9
    fileList = file.readlines()
    while (a < num_lines): 
        line1 = fileList[a].rstrip('\r\n')
        line1 = re.sub('\\bproduct/productId: \\b', '', line1)
        line2 = fileList[b].rstrip('\r\n')
        line2 = re.sub('\\bproduct/title: \\b', '', line2)
        line3 = fileList[c].rstrip('\r\n')
        line3 = re.sub('\\breview/text: \\b', '', line3)
        print line1
        print line2
        print line3
        print 
        a=a+11
        b=b+11
        c=c+11

