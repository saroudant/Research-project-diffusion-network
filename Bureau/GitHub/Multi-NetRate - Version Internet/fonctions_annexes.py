__author__ = 'Soufiane'
# -*-coding:Latin-1 -*
"""In this file one can find small but yet very important programs that are used everywhere in
the others algorithms of both NetRate and MultiNetRate"""

import numpy
import numpy.linalg
import math
import copy

def soft_thres(y,mu):
    y_iter = copy.copy(y)
    y_iter[:] = numpy.maximum(y[:] - mu,0) - numpy.maximum(-y[:] - mu,0)
    return y_iter

def err_func(x,beta_true):
    return numpy.linalg.norm(x - beta_true) / numpy.linalg.norm(beta_true)

def matrix_inverse_elements(x,t):
    y = numpy.zeros((len(x),len(x)))
    for i in range(len(x)):
        for j in range(len(x[i])):
            if x[j,j] != 0:
                y[i,j] = t[j,i] / x[j,j]
    return y

def log_elements(x):
    y = numpy.zeros((len(x),len(x)))
    for i in range(len(x)):
        for j in range(len(x[i])):
            if x[i,j] > 0:
                y[i,j] = math.log(x[i,j])
    return y

def somme_diag_elements(x):
    sum = 0
    for i in range(len(x)):
        sum += x[i,i]
    return sum

def est_positif(x):
    value = True
    i = 0
    j = 0
    while value and i < len(x):
        j = 0
        while value and j < len(x):
            value = value and (x[i,j] >= 0)
            j += 1
        i+=1
        j=0
    return value

def est_diagonal_positif(x):
    value = True
    i = 0
    while value and i < len(x):
        value = (value and (x[i,i] >= 0))
        i+=1
    return value

def est_nul(x):
    value = True
    i = 0
    j = 0
    while value and i < len(x):
        j = 0
        while value and j < len(x):
            value = value and (x[i,j] == 0)
            j += 1
        i+=1
        j=0
    return value

def argmax_diff(x1,x2):
    i0 = 0
    j0 = 0
    i1 = 0
    j1 = 0
    y = x2 -x1
    for i in range(len(y)):
        for j in range(len(y[i])):
            if y[i,j] > y[i0][j0]:
                i1,j1 = i0,j0
                i0,j0 = i,j
            elif y[i,j] > y[i1][j1]:
                i1,j1 = i,j
    return ((i0,j0),(i1,j1))

def norm_matrix(x,k):
    sum = 0
    for i in range(len(x)):
        sum += numpy.linalg.norm(x[i],k)
    return sum