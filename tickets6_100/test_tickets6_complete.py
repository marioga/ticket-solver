# -*- coding: utf-8 -*-
"""
Created on Wed Dec 31 10:28:30 2014

@author: marioga
"""
import sys

cont = 1
with open("tickets6.txt",'r') as f:
    for line in f:
        lines = line.strip().split(' ')
        if cont <= 531441 and int(lines[0]) != cont:
            print("Fail at " + str(cont))
            sys.exit()
        cont+=1
print("Success")