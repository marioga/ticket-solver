# -*- coding: utf-8 -*-
"""
Created on Wed Dec 31 15:09:09 2014

@author: marioga
"""

g = open("tickets6.txt",'r')
insolvable = []
cont = 0
for line in g:
    lines = line.strip().split(' ')
    if lines[2] == 'NO':
        insolvable.append(lines[1] + '\n')
        cont+=1;
with open("tickets6_insolvable.txt",'w') as f:
    f.write(str(cont)+'\n')
    for line in insolvable:
        f.write(line)
f.close()
g.close()