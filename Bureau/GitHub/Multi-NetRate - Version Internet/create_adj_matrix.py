# -*-coding:Latin-1 -*

__author__ = 'Soufiane'

""" The aim of this program is to generate the adjacent matrix thanks to file that have been
 laid out following the requirements given by J. Leskovec in NetRate"""

import os
import numpy

"""First thing is to transform the file so that we can exploit it. We will read the file's contenu and divide this
lecture in two parts :
- first we count the number of vertices and we build an appropriate-sized matrix
- then we put all the informations of the file into the matrix"""
def create_adj_matrix(lien_fichier):
    with open(lien_fichier, "r") as mon_fichier :
        contenu = mon_fichier.read()

        nbre_lettres_parcourues = 0
        nbre_sommets = 0
        while(contenu[nbre_lettres_parcourues] != "\n" or contenu[nbre_lettres_parcourues + 1] !="\n"):
            if contenu[nbre_lettres_parcourues+1] == "\n" :
                nbre_sommets += 1
            nbre_lettres_parcourues += 1

        A = numpy.zeros((nbre_sommets, nbre_sommets))

        nbre_lettres_parcourues += 2
        couples = [0,0,0]
        phase = 0
        variable_stockage = ""
        longueur_contenu = len(contenu)
        while nbre_lettres_parcourues < longueur_contenu:
            if contenu[nbre_lettres_parcourues] == ",":
                couples[phase] = int(variable_stockage)
                variable_stockage = ""
                phase += 1
            elif contenu[nbre_lettres_parcourues] == "\n":
                couples[phase] = float(variable_stockage)
                phase = 0
                variable_stockage = ""
                A[couples[0]][couples[1]] = couples[2]
            else:
                variable_stockage += contenu[nbre_lettres_parcourues]
            nbre_lettres_parcourues += 1

        return (A, nbre_sommets)

"""This function could be useful if we should have to limit the matrix (for example if there is a
all matrix bloc of zeros)."""
def limiter_matrice(A, N, fichier):
    with open(fichier, 'w') as mon_fichier :
        i = 0
        while i < N:
            mon_fichier.write('{},{}\n'.format(i,i))
            i += 1
        mon_fichier.write('\n')
        i = 0
        j = 0
        while i < len(A):
            while j < len(A):
                if A[i][j] != 0 and i < N and j < N:
                    mon_fichier.write('{},{},{}\n'.format(i,j,A[i][j]))
                j += 1
            i += 1
            j = 0