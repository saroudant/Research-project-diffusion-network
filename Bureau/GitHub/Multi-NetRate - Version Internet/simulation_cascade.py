# -*-coding:Latin-1 -*

from librairie_modele import *
from random import random
import numpy
from math import log,exp,floor
from scipy.stats import gamma
import os

os.chdir("C:/Users/Soufiane/Documents/Mines 1A/S3 Recherche/Multi-NetRate") #Pour enregistrer nos cascades

"""We assume that there is a given network. We are simulating cascades on this network.
We are going to work this way :
    * First we generate an adjacent matrix A. Coefficients would be chosen either following an uniform law, or given the Kronecker method.
    * Then we will use the proposed laws (exponential, power-law or Rayleigh) to launch a cascade and determine it.
    * Finally, we will have a cascade and we will write it down following the J. Leskovec topography"""

def inserer_dans_liste((i,t,g),liste):
    if liste == []:
        liste.append((i,t,g))
    else:
        added = False
        for (k,(j,to,go)) in enumerate(liste):
            if to > t:
                liste.insert(k, (i,t,g))
                added = True
                break
        if not(added) :
            liste.append((i,t,g))


def ajouter_liste(liste,p):
    while liste != []:
        inserer_dans_liste(liste[0],p)
        liste = liste[1:]

def depiler_arret(pile,t):
    for elt in pile:
        if elt[1] < t:
            pile.remove(elt)
        else:
            break

"""* graphe : Graphe instance
* L : matrix L cf. estimate_network
* last_index :  (len(L[j])) vector to remember the last index
* i : first vertice
* T : temporal horizon
* Delta : matrix Delta. cf. estimate_network """
def creer_cascade_exp(graphe,L,T,Delta,pile_depart):

    j = 0
    delta = 0
    g = 0
    pile = pile_depart
    to_add = []

    while pile != []:
        (i_int,t_i,gi) = pile[0]
        print(t_i)
        j = 0
        if gi >= 0 and t_i < T:
            while j < graphe.nbre_sommets:
                if i_int != j:
                    l = 0
                    to_add = []
                    delta = random()
                    g = gamma.rvs(8)
                    follow = True
                    nbre_rencontres = 0 #On compte le nbre de rencontres pour pouvoir ajouter tout de même le temps si le noeud n'a pas été touché.
                    not_added = False #variable intermédiaire pour vérifier que l'on ajoute pas n'importe quoi dans le premier if
                    while l < len(pile) and follow:
                        elt = pile[l]
                        if elt[0] == j:
                            nbre_rencontres += 1
                            #Cas infecté plus tard d'après la pile, mais on découvre qu'en fait il est infecté plus tôt par i
                            if elt[2] >= 0 and t_i - log(1-delta) / graphe.A[int(i_int)][j] < elt[1] and not(not_added):
                                #On ne détruit pas ce qu'il y a après, mais on vérifie que c'est compatible : si pas compatible : on dégage!
                                for h,elt2 in enumerate(pile):
                                    if elt2[0] == j:
                                        if t_i - log(1-delta) / graphe.A[int(i_int)][j] + g > elt2[1] and elt2[2] > 0 and t_i - log(1-delta) / graphe.A[int(i_int)][j] < elt2[1]:
                                            pile.remove(elt2)
                                        if t_i - log(1-delta) / graphe.A[int(i_int)][j] + g > elt2[1]+elt2[2] and elt2[2] < 0 and t_i - log(1-delta) / graphe.A[int(i_int)][j] < elt2[1]+elt2[2]:
                                            pile.remove(elt2)
                                if t_i - log(1-delta) / graphe.A[int(i_int)][j] < T:
                                    #print(t_i - log(1-delta) / graphe.A[int(i_int)][j],g)
                                    to_add.append((j,t_i - log(1-delta) / graphe.A[int(i_int)][j],g))
                                    to_add.append((j,t_i - log(1-delta) / graphe.A[int(i_int)][j] + g, -g))
                                    follow = False
                            #Cas où j sera infecté par i, mais après qu'il soit désinfecté...
                            elif elt[2] <= 0 and t_i + gi > elt[1]:
                                 if exp(-(elt[1]-t_i)/graphe.A[int(i_int)][j]) > delta:
                                    if t_i - math.log(exp(-(elt[1]-t_i)*graphe.A[int(i_int)][j]) - delta) / graphe.A[int(i_int)][j] > elt[1]:
                                        #On vérifie tout ce qu'il y a après
                                        ajouter = True
                                        #On le parcourt deux fois : une fois pour être sûr qu'on l'ajoute, et une autre fois pour enlever tout ce qui pose problème
                                        for h,elt2 in enumerate(pile):
                                            if (h >= l and ((t_i - math.log(exp(-(elt[1]-t_i)*graphe.A[int(i_int)][j]) - delta) / graphe.A[int(i_int)][j] + g > elt2[1] and elt2[2] >= 0 and (t_i - math.log(exp(-(elt[1]-t_i)*graphe.A[int(i_int)][j]) - delta) / graphe.A[int(i_int)][j] + g < elt2[1]+ elt2[2])) or ((t_i - math.log(exp(-(elt[1]-t_i)/graphe.A[int(i_int)][j]) - delta) / graphe.A[int(i_int)][j] - elt2[2] > elt2[1]) and elt2[2] < 0 and (t_i - math.log(exp(-(elt[1]-t_i)/graphe.A[int(i_int)][j]) - delta) / graphe.A[int(i_int)][j] < elt2[1] )))):
                                                ajouter = False

                                        if ajouter and t_i - math.log(exp(-(elt[1]-t_i)/graphe.A[int(i_int)][j]) - delta) / graphe.A[int(i_int)][j] < T:
                                            for h,elt2 in enumerate(pile):
                                                if (h >= l and ((t_i - math.log(exp(-(elt[1]-t_i)*graphe.A[int(i_int)][j]) - delta) / graphe.A[int(i_int)][j] < elt2[1] and elt2[2 >= 0] and t_i - math.log(exp(-(elt[1]-t_i)*graphe.A[int(i_int)][j]) - delta) / graphe.A[int(i_int)][j] + g > elt2[1]) or (t_i - math.log(exp(-(elt[1]-t_i)*graphe.A[int(i_int)][j]) - delta) / graphe.A[int(i_int)][j] < elt2[1] and elt2[2] < 0 and t_i - math.log(exp(-(elt[1]-t_i)*graphe.A[int(i_int)][j]) - delta) / graphe.A[int(i_int)][j] > elt2[1] - elt2[2]))):
                                                    depiler_arret(pile,elt2)
                                                    print("yeah man, it's decreasing!")

                                            to_add.append((j,t_i - math.log(exp(-(elt[1]-t_i)/graphe.A[int(i_int)][j]) - delta) / graphe.A[int(i_int)][j],g))
                                            to_add.append((j, t_i - math.log(exp(-(elt[1]-t_i)/graphe.A[int(i_int)][j]) - delta) / graphe.A[int(i_int)][j] + g, - g))

                            if elt[2] <= 0 and t_i - log(1-delta) / graphe.A[int(i_int)][j] < elt[1] and t_i - log(1-delta) / graphe.A[int(i_int)][j] > elt[1] + elt[2]:
                                not_added = True
                        l += 1
                    ajouter_liste(to_add,pile)
                    if nbre_rencontres == 0:
                        inserer_dans_liste((j,t_i - log(1-delta) / graphe.A[int(i_int)][j],g),pile)
                        inserer_dans_liste((j,t_i - log(1-delta) / graphe.A[int(i_int)][j] + g, -g),pile)
                j+=1
            #Et maintenant on rempli la matrice L
            (L[i_int]).append(t_i)
            (Delta[i_int]).append(gi)

        pile = pile[1:]


def enregistrer_matrix_adj(graphe, fichier='adj.txt'):
    os.chdir("../donnees")
    with open(fichier, 'w') as mon_fichier :
        i = 0
        while i < graphe.nbre_sommets:
            mon_fichier.write('{},{}\n'.format(i,i))
            i += 1
        mon_fichier.write('\n')
        i = 0
        j = 0
        while i < graphe.nbre_sommets:
            while j < graphe.nbre_sommets:
                if graphe.A[i][j] != 0:
                    mon_fichier.write('{},{},{}\n'.format(i,j,graphe.A[i][j]))
                j += 1
            i += 1
            j = 0

def enregistrer_matrix_adj(A, nbre, fichier='adj.txt'):
    os.chdir("../donnees")
    with open(fichier, 'w') as mon_fichier :
        i = 0
        while i < nbre:
            mon_fichier.write('{},{}\n'.format(i,i))
            i += 1
        mon_fichier.write('\n')
        i = 0
        j = 0
        while i < nbre:
            while j < nbre:
                if A[i][j] != 0:
                    mon_fichier.write('{},{},{}\n'.format(i,j,A[i][j]))
                j += 1
            i += 1
            j = 0

def enregistrer_cascades(L,Delta,fichier='casc.txt'):
    os.chdir("../donnees")
    with open(fichier, 'w') as mon_fichier :
        i = 0
        while i < len(L):
            mon_fichier.write('{},{}\n'.format(i,i))
            i += 1
        mon_fichier.write('\n')

        for (i,elt) in enumerate(L):
            for (j,t) in enumerate(elt):
                mon_fichier.write('{},{},{},'.format(i,t,Delta[i][j]))
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
    return liste

def generer ():
    graphe = Graphe()
    i = 0
    L = list()
    Delta = list()
    while i < 10:
        graphe.ajouter_sommet()
        L.append([])
        Delta.append([])
        i += 1

    graphe.tirage_A()
    i = 0
    cascade = SetCascade(graphe,200)

    j = 1
    pile_depart = []
    pile_depart.append((int(floor(10*random())), 5., gamma.rvs(8)))
    while j < 10:
        gam = gamma.rvs(8)
        inserer_dans_liste((j, float(j*5), gam),pile_depart)
        inserer_dans_liste((j,float(j*5)+gam, - gam),pile_depart)
        j += 1

    creer_cascade_exp(graphe,L,cascade.T,Delta,pile_depart)

    enregistrer_matrix_adj(graphe,'adj.txt')
    enregistrer_cascades(L,Delta,'casc.txt')