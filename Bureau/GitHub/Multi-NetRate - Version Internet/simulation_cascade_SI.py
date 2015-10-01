# -*-coding:Latin-1 -*

from librairie_modele import *
import random
import math
import pp
import os
import copy
from create_adj_matrix import *
from transform_cascade import *

os.chdir("../donnees/")

"""We assume that there is a given network. We are simulating cascades on this network.
We are going to work this way :
    * First we generate an adjacent matrix A. Coefficients would be chosen either following an uniform law, or given the Kronecker method.
    * Then we will use the proposed laws (exponential, power-law or Rayleigh) to launch a cascade and determine it.
    * Finally, we will have a cascade and we will write it down following the J. Leskovec topography"""

def soft_thres(y,mu):
    y_iter = copy.copy(y)
    y_iter[:] = numpy.maximum(y[:] - mu,0) - numpy.maximum(-y[:] - mu,0)
    return y_iter

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

def enregistrer_cascades_SIS(L,Delta,fichier='casc.txt'):
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

def enregistrer_cascades_SI(cascades,fichier='casc.txt'):
    os.chdir("../donnees")
    with open(fichier, 'w') as mon_fichier :
        i = 0
        while i < len(cascades.liste_cascades[0]):
            mon_fichier.write('{},{}\n'.format(i,i))
            i += 1
            
        mon_fichier.write('\n')
        
        nbre_cascades = len(cascades.liste_cascades)
        i = 0
        j = 0
        dejaEcrit = False
        while i < nbre_cascades :
            j = 0
            dejaEcrit = False
            while j < cascades.graphe_sous_jacent.nbre_sommets:
                if cascades.liste_cascades[i][j] != 0:
                    mon_fichier.write('{},{},'.format(j,cascades.liste_cascades[i][j]))
                j += 1
            mon_fichier.write('\n')
            i += 1

def creer_cascade_exp_SI(graphe,L,T,pile_depart):

    j = 0
    delta = 0
    g = 0
    pile = pile_depart
    to_add = []

    while pile != []:
        (i_int,t_i,gi) = pile[0]
        j = 0
        if gi >= 0 and t_i < T: #Il faut que le noeud que l'on considère soit infecteur
            while j < graphe.nbre_sommets:
                if i_int != j and graphe.A[int(i_int)][j] > 0.0001:
                    l = 0
                    to_add = []
                    delta = random()
                    follow = True
                    nbre_rencontres = 0 #On compte le nbre de rencontres pour pouvoir ajouter tout de même le temps si le noeud n'a pas été touché.
                    not_added = False #variable intermédiaire pour vérifier que l'on ajoute pas n'importe quoi dans le premier if
                    while l < len(pile) and follow:
                        elt = pile[l]
                        if elt[0] == j:
                            nbre_rencontres += 1
                            #Cas infecté plus tard d'après la pile, mais on découvre qu'en fait il est infecté plus tôt par i
                            if elt[2] >= 0 and t_i - math.log(1-delta) / graphe.A[int(i_int)][j] < elt[1] and not(not_added):
                                #On ne détruit pas ce qu'il y a après, mais on vérifie que c'est compatible : si pas compatible : on dégage!
                                for h,elt2 in enumerate(pile):
                                    if elt2[0] == j:
                                        if t_i - math.log(1-delta) / graphe.A[int(i_int)][j] + g > elt2[1] and elt2[2] > 0 and t_i - math.log(1-delta) / graphe.A[int(i_int)][j] < elt2[1]:
                                            pile.remove(elt2)
                                        if t_i - math.log(1-delta) / graphe.A[int(i_int)][j] + g > elt2[1]+elt2[2] and elt2[2] < 0 and t_i - math.log(1-delta) / graphe.A[int(i_int)][j] < elt2[1]+elt2[2]:
                                            pile.remove(elt2)
                                if t_i - math.log(1-delta) / graphe.A[int(i_int)][j] < T:
                                    
                                    to_add.append((j,t_i - math.log(1-delta) / graphe.A[int(i_int)][j],g))
                                    to_add.append((j,t_i - math.log(1-delta) / graphe.A[int(i_int)][j] + g, -g))
                                    follow = False
                            #Cas où j sera infecté par i, mais après qu'il soit désinfecté...
                            elif elt[2] <= 0 and t_i + gi > elt[1]:
                                 if math.exp(-(elt[1]-t_i)/graphe.A[int(i_int)][j]) > delta:
                                    if t_i - math.log(math.exp(-(elt[1]-t_i)*graphe.A[int(i_int)][j]) - delta) / graphe.A[int(i_int)][j] > elt[1]:
                                        #On vérifie tout ce qu'il y a après
                                        ajouter = True
                                        #On le parcourt deux fois : une fois pour être sûr qu'on l'ajoute, et une autre fois pour enlever tout ce qui pose problème
                                        for h,elt2 in enumerate(pile):
                                            if (h >= l and ((t_i - math.log(math.exp(-(elt[1]-t_i)*graphe.A[int(i_int)][j]) - delta) / graphe.A[int(i_int)][j] + g > elt2[1] and elt2[2] >= 0 and (t_i - math.log(math.exp(-(elt[1]-t_i)*graphe.A[int(i_int)][j]) - delta) / graphe.A[int(i_int)][j] + g < elt2[1]+ elt2[2])) or ((t_i - math.log(math.exp(-(elt[1]-t_i)/graphe.A[int(i_int)][j]) - delta) / graphe.A[int(i_int)][j] - elt2[2] > elt2[1]) and elt2[2] < 0 and (t_i - math.log(math.exp(-(elt[1]-t_i)/graphe.A[int(i_int)][j]) - delta) / graphe.A[int(i_int)][j] < elt2[1] )))):
                                                ajouter = False

                                        if ajouter and t_i - math.log(math.exp(-(elt[1]-t_i)/graphe.A[int(i_int)][j]) - delta) / graphe.A[int(i_int)][j] < T:
                                            for h,elt2 in enumerate(pile):
                                                if (h >= l and ((t_i - math.log(math.exp(-(elt[1]-t_i)*graphe.A[int(i_int)][j]) - delta) / graphe.A[int(i_int)][j] < elt2[1] and elt2[2 >= 0] and t_i - math.log(math.exp(-(elt[1]-t_i)*graphe.A[int(i_int)][j]) - delta) / graphe.A[int(i_int)][j] + g > elt2[1]) or (t_i - math.log(math.exp(-(elt[1]-t_i)*graphe.A[int(i_int)][j]) - delta) / graphe.A[int(i_int)][j] < elt2[1] and elt2[2] < 0 and t_i - math.log(math.exp(-(elt[1]-t_i)*graphe.A[int(i_int)][j]) - delta) / graphe.A[int(i_int)][j] > elt2[1] - elt2[2]))):
                                                    depiler_arret(pile,elt2)

                                            to_add.append((j,t_i - math.log(math.exp(-(elt[1]-t_i)/graphe.A[int(i_int)][j]) - delta) / graphe.A[int(i_int)][j],g))
                                            to_add.append((j, t_i - math.log(math.exp(-(elt[1]-t_i)/graphe.A[int(i_int)][j]) - delta) / graphe.A[int(i_int)][j] + g, - g))

                            if elt[2] <= 0 and t_i - math.log(1-delta) / graphe.A[int(i_int)][j] < elt[1] and t_i - math.log(1-delta) / graphe.A[int(i_int)][j] > elt[1] + elt[2]:
                                not_added = True
                        l += 1
                    ajouter_liste(to_add,pile)
                    if nbre_rencontres == 0:
                        inserer_dans_liste((j,t_i - math.log(1-delta) / graphe.A[int(i_int)][j],T),pile)
                        inserer_dans_liste((j,t_i - math.log(1-delta) / graphe.A[int(i_int)][j] + T, -T),pile)
                j+=1
            #Et maintenant on rempli la matrice L
            L[int(i_int)] = t_i

        pile = pile[1:]

def creer_tableau_vide(longueur,largeur):
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

def generation_SIS(nbre_cascades):

    A_matrix,nbre = create_adj_matrix('anr.txt')
    A_list = list()
    for i in range(0,len(A_matrix)):
        A_list.append(list(A_matrix[i]))

    pile_depart = [(0,0.5,10)]
    graphe = Graphe(A_list)
    tableau_cascade = creer_tableau_vide(nbre_cascades,len(A_matrix))
    i = 0
    cascade = SetCascade(graphe,10)

    while i < nbre_cascades:
        b = random()
        pile_depart = [(math.floor(graphe.nbre_sommets * b),0.005,10)]
        creer_cascade_exp_SI(graphe,tableau_cascade[i],cascade.T,pile_depart)
        cascade.ajouter_cascade(tableau_cascade[i])
        i += 1

    file = 'cnr'+str(nbre_cascades//100)+'.txt'
    enregistrer_cascades_SI(cascade,file)

    (L,Delta) = transform_into_sis(cascade.liste_cascades,len(A_matrix),graphe.A,10,10)

    file = 'c'+str(nbre_cascades//100)+'.txt'
    enregistrer_cascades_SIS(L,Delta,file)