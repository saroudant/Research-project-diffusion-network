# -*-coding:Latin-1 -*

__author__ = 'Soufiane'

"""Netrates's creators have built cascades and other algorithms developed during my internship
 produced cascades. Those cascades are written in files following a very strict written requirement.
 The aim of create-cascade is to read those files and generate the cascade-matrix

 create_cascade reads one MultiNetRate cascade"""

import os
import numpy

def create_cascade(lien):
    with open(lien, "r") as mon_fichier :
        contenu = mon_fichier.read()

        nbre_lettres_parcourues = 0
        nbre_sommets = 0
        L = []
        Delta = []
        while(contenu[nbre_lettres_parcourues] != "\n" or contenu[nbre_lettres_parcourues + 1] !="\n"):
            if contenu[nbre_lettres_parcourues+1] == "\n" :
                nbre_sommets += 1
                L.append([])
                Delta.append([])
            nbre_lettres_parcourues += 1

        nbre_infections = 0
        nbre_lettres_parcourues += 2
        nbre_lettres_parcourues_apres_noeuds = nbre_lettres_parcourues
        nbre_caracteres = len(contenu)
        while nbre_lettres_parcourues < nbre_caracteres :
            if contenu[nbre_lettres_parcourues] == "\n":
                nbre_infections += 1
            nbre_lettres_parcourues += 1

        k = 0
        nbre_lettres_parcourues = nbre_lettres_parcourues_apres_noeuds - 1
        caractere = ""
        ID = 0

        while k < nbre_infections:

            nbre_lettres_parcourues += 1
            phase = 0 # 0 : we are looking at the ID, 1 : we are looking at the time of contamination, 2: we are looking at how many time the vertice remains contaminated
            caractere = ''
            while contenu[nbre_lettres_parcourues] != '\n':
                if contenu[nbre_lettres_parcourues] == ',' and phase == 0:
                    ID = int(caractere)
                    phase = 1
                    caractere = ''
                elif contenu[nbre_lettres_parcourues] == ',' and phase == 1:
                    L[ID].append(float(caractere))
                    caractere = ''
                    phase = 2
                elif contenu[nbre_lettres_parcourues] == ',' and phase == 2:
                    Delta[ID].append(float(caractere))
                    caractere = ''
                    phase = 0
                else:
                    caractere += contenu[nbre_lettres_parcourues]
                nbre_lettres_parcourues += 1
            k += 1

        return (L,Delta)

def limiter_cascade(C,N,fichier):
    with open(fichier, 'w') as mon_fichier :
        i = 0
        while i < N:
            mon_fichier.write('{},{}\n'.format(i,i))
            i += 1
        mon_fichier.write('\n')
        nbre_cascades = len(C)
        i = 0
        j = 0
        dejaEcrit = False
        while i < nbre_cascades :
            j = 0
            dejaEcrit = False
            while j < N:
                if C[i][j] > 0:
                    if i < N and j < N:
                        if dejaEcrit:
                            mon_fichier.write(',')
                        mon_fichier.write('{},{}'.format(j,C[i][j]))
                        dejaEcrit = True
                j += 1
            i += 1
            if dejaEcrit:
                mon_fichier.write('\n')

def creer_tableau_vide(longueur, largeur):
    liste = list(list())
    i = 0
    j = 0
    while i < longueur :
        liste.append(list())
        while j < largeur:
            liste[i].append(0)
            j += 1
        j = 0
        i += 1