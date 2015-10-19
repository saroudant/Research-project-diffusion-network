__author__ = 'Soufiane'
# -*-coding:Latin-1 -*

from estimation_network import *
from create_adj_matrix import *
from create_cascade import *
from simulation_cascade_SI import *
from estimation_network_NetRate import *
from librairie_modele import *
import os


""" Tool which aims at comparing two cascades estimated by NetRate (SIS) and MultiNetRate (SI).
It can work only if there is a link between the two studied cascades. The idea of this function is to study the independance between cascades in NetRate."""
def MultiNetRate(nbre,file):
    os.chdir("../donnees")

    generation_SIS(nbre)

    A,nbre_noeuds = create_adj_matrix(file)
    file_SI = 'c'+str(nbre//100)+'.txt'
    L,Delta = create_cascade(file_SI)
    file_SIS = 'cnr'+str(nbre//100)+'.txt'
    C,nbre_infections = create_cascade_NetRate(file_SIS)

    #MultiNetRate estimation
    (Beta,(accuracy,MAE,MAS)) = estimate_network(A,nbre_noeuds,L,Delta,nbre*20,'exp')

    #NetRate estimation
    (Alpha) = estimate_network_NetRate(A,C,nbre_noeuds,20,'exp',nbre_infections,0.00001)

    #We compare the two solutions.
    diff0 = norm_matrix(Alpha-Beta,0)
    diff2 = norm_matrix(Alpha-Beta,2)

    #We write down the result so that we can use them in the futur.
    sdiff0 = str(diff0)
    position_point = 0
    for i,elt in enumerate(sdiff0):
        if elt == '.':
            position_point = i

    with open('diff0.txt','a') as fichier1:
        fichier1.write(str(k)+',')
        fichier1.write(sdiff0[0:position_point]+','+sdiff0[(position_point+1):])
        fichier1.write('\n')

    sdiff2 = str(diff2)
    position_point = 0
    for i,elt in enumerate(sdiff2):
        if elt == '.':
            position_point = i

    with open('diff1.txt','a') as fichier2:
        fichier2.write(str(k)+',')
        fichier2.write(sdiff2[0:position_point]+','+sdiff2[(position_point+1):])
        fichier2.write('\n')

    with open('indic_NetRate.txt','a') as fichier3:
        fichier3.write(str(k))
        fichier3.write(str(accuracy))
        fichier3.write(str(MAE))
        fichier3.write(str(MAS))
