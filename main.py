# -*-coding:Latin-1 -*

__author__ = 'Soufiane'

import os
import sys
import pp
from estimation_network import *
from create_adj_matrix import *
from create_cascade import *
from simulation_cascade_SI import *
from estimation_network_NetRate import *
from librairie_modele import Graphe
import os

"""Here is the program that compare NetRate and MultiNetRate. It uses PP to use all the cores in the processor"""


ppservers = ()
print(sys.argv)
if len(sys.argv) > 1:
    ncpus = int(sys.argv[1])
    # Creates jobserver with ncpus workers
    job_server = pp.Server(ncpus, ppservers=ppservers)
else:
    # Creates jobserver with automatically detected number of workers
    job_server = pp.Server(ppservers=ppservers)

inputs = [100]
k = 100
while k < 2000:
    inputs.append(k)
    k += 100

taches = [(input,job_server.submit(MultiNetRate,(input,),(generation_SIS,create_adj_matrix,create_cascade,create_cascade_NetRate,estimate_network,estimate_network_NetRate,transform_into_sis, norm_matrix,norm_matrix,Graphe,creer_tableau_vide,SetCascade,creer_cascade_exp_SI,ajouter_liste,inserer_dans_liste,enregistrer_cascades_SI,enregistrer_cascades_SIS,matrix_inverse_elements,log_elements,soft_thres,err_func,somme_diag_elements,est_positif,est_diagonal_positif,est_nul,argmax_diff,),('numpy.linalg','copy','scipy.stats','random','math','os','numpy',))) for input in inputs]

for input,job in taches:
    job()
