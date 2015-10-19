# -*-coding:Latin-1 -*


import os
import random
import numpy
import math
from scipy import *

class Graphe:

    def __init__(self):
        self.sommets = list()
        self.nbre_sommets = 0
        self.A = list(list())
        self.beta = 0
        self.noeud = list()

    def __init__(self,A):
        self.A = A
        self.sommets = list(numpy.arange(0,len(A)))
        self.nbre_sommets = len(A)
        self.beta = 0
        self.noeud = list()

    def ajouter_sommet(self):
        self.sommets.append(self.nbre_sommets)

        i = 0
        liste_vide = list()
        while i < self.nbre_sommets :
            liste_vide.append(0)
            self.A[i].append(0)
            i += 1
        liste_vide.append(0)
        self.A.append(liste_vide)
        del liste_vide

        self.nbre_sommets += 1

    def ajouter_noeud(self, i,j):
        self.noeud.append([i,j])

    def tirage_A(self):
        i = 0
        j = 0
        while i < self.nbre_sommets :
            while j < self.nbre_sommets :
                self.A[i][j] = 0.5 * random.random()
                j += 1
            
            i += 1
            j = 0

    def tirage_kroneck_A(self):
        log_value = math.log(self.nbre_sommets,4)

        while self.nbre_sommets < 4**(log_value+1):
            self.ajouter_sommet()

        matrice_creation = numpy.array([[1/2*random.random(),1/2*random.random()],[1/2*random.random(),1/2*random.random()]])
        k = 0

        while k <= log_value:
            B = kronecker_product(self.A,B)
            k += 1

        return B
    
class SetCascade:

    def __init__(self, graphe, T):
        self.graphe_sous_jacent = graphe
        self.liste_cascades = list(list())
        self.T = T
        self.matrice_difference_temps = list(list(list()))
        self.nbre_cascades = 0

    def ajouter_cascade(self, cascade):
        if len(cascade) <= self.graphe_sous_jacent.nbre_sommets :
            for i,elt in enumerate(cascade):
                if elt > self.T:
                    cascade[i] = self.T + 1
                elif elt < 0:
                    cascade[i] = 0

            self.liste_cascades.append(cascade)

            i = 0
            liste_vide = list()
            self.matrice_difference_temps.append([])
            while i < self.graphe_sous_jacent.nbre_sommets:
                liste_vide.append(0)
                i += 1
            while i > 0:
                self.matrice_difference_temps[self.nbre_cascades].append(liste_vide)
                i -= 1

            self.nbre_cascades += 1

        else:
            print("""La cascade n'est pas conforme""")

    def former_matrice_difference(self, numero_cascade):
        for i, ti in enumerate (self.liste_cascades[numero_cascade]):
            for j, tj in enumerate (self.liste_cascades[numero_cascade]):
                if  (ti < tj and tj != self.T + 1):
                    self.matrice_difference_temps[numero_cascade][i][j] = tj - ti
                    print('{}{}'.format(i,j))
                    print(self.matrice_difference_temps[numero_cascade][i][j])

def kronecker_product(A,B):
    AxB = numpy.zeros((len(A)*len(B),len(A[0])*len(B[0])))
    for i in range(0,len(A)*len(B)):
        for j in range(0,len(A)*len(B)):
            AxB[i][j] = A[i//len(B)][j//len(B[0])]*B[i%len(B)][j%len(B[0])]
    return AxB