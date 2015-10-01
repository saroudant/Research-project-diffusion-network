__author__ = 'Soufiane'
# -*-coding:Latin-1 -*

import numpy
import math
import copy
from numpy.linalg import norm
from scipy.linalg import eigvals
import os
import random
from fonctions_annexes import soft_thres, err_func, matrix_inverse_elements, log_elements, somme_diag_elements, est_positif, est_diagonal_positif, est_nul, argmax_diff, norm_matrix

"""estimate_network_NetRate is the estimation algorithm developed following the Netrate methods.
To know more one can refer to the presentation. It is not quite different from the
MultiNetRate algorithm is estimate_network"""

def estimate_network_NetRate(A, C, nbre_noeuds, T, type_diffusion, nbre_infections,mu,return_value=False):

    Psi2 = numpy.zeros((nbre_noeuds, nbre_noeuds))
    Psi1 = numpy.zeros((nbre_noeuds, nbre_noeuds))
    T3 = list()
    nbre_touche_potentiel = numpy.zeros((nbre_noeuds,nbre_noeuds))
    c = 0

    indice_dernier_noeud_touche = 0
    while c < nbre_infections:
        i = nbre_noeuds - 1
        mat_int = C[c].argsort()
        T3.append(numpy.zeros((nbre_noeuds,nbre_noeuds)))
        while (i >= 0 and (C[c][mat_int[i]] > -1)) :
            j = 0
            while j < i:
                nbre_touche_potentiel[mat_int[j],mat_int[i]] += 1
                if (C[c][mat_int[j]] != -1):
                    if(type_diffusion == 'exp'):
                        Psi2[mat_int[i]][mat_int[j]] +=  C[c][mat_int[i]] - C[c][mat_int[j]]
                        T3[c][mat_int[i]][mat_int[j]] = 1
                    elif (type_diffusion == 'pl') and (C[c][mat_int[i]]-C[c][mat_int[j]] > 1):
                        Psi2[mat_int[i]][mat_int[j]] += math.log(C[c][mat_int[i]]-C[c][mat_int[j]])
                        T3[c][mat_int[i]][mat_int[j]] = 1 / (C[c][mat_int[i]]-C[c][mat_int[j]])
                    elif (type_diffusion == 'rayleigh'):
                        Psi2[mat_int[i]][mat_int[j]] += 0.5*((C[c][mat_int[i]] - C[c][mat_int[j]])*(C[c][mat_int[i]] - C[c][mat_int[j]]))
                        T3[c][mat_int[i]][mat_int[j]] = C[c][mat_int[i]]-C[c][mat_int[j]]
                j += 1
            i -= 1

        indice_dernier_noeud_touche = i + 1
        #Dans le calcul de Psi1, exceptionnellement on compte dans le sens inverse
        while i >= 0: #On obtient des j qui ont une valeur nulle
            j = nbre_noeuds - 1
            while j >= indice_dernier_noeud_touche :
                if type_diffusion == 'exp':
                    Psi1[mat_int[j]][mat_int[i]] += T - C[c][mat_int[j]]
                elif type_diffusion == 'pl' and T > 1:
                    Psi1[mat_int[j]][mat_int[i]] += math.log(T-C[c][mat_int[j]])
                elif type_diffusion == 'rayleigh':
                    Psi1[mat_int[j]][mat_int[i]] += 0.5*(T-C[c][mat_int[j]])*(T-C[c][mat_int[j]]);
                j -= 1
            i -= 1

        c += 1

    def formation_densite(Beta=numpy.ones((nbre_noeuds,nbre_noeuds))):
        Psi2_grad = numpy.zeros((nbre_noeuds,nbre_noeuds))
        Psi1_grad = numpy.zeros((nbre_noeuds,nbre_noeuds))
        Psi3_grad = numpy.zeros((nbre_noeuds,nbre_noeuds))
        Psi1_intermediaire = numpy.zeros((nbre_noeuds,nbre_noeuds))
        Psi2_intermediaire = numpy.zeros((nbre_noeuds,nbre_noeuds))
        grad = numpy.zeros((nbre_noeuds,nbre_noeuds))

        Psi1_grad = copy.copy(Psi1)
        Psi1_intermediaire = copy.copy(numpy.dot(-Psi1,Beta.transpose()))

        Psi2_grad = copy.copy(Psi2.transpose())
        Psi2_intermediaire = copy.copy(numpy.dot(-Psi2,Beta))

        produit_densite = list()
        produit_densite_log = list()
        produit_gradient = numpy.zeros((nbre_noeuds,nbre_noeuds))

        c = 0
        while c < nbre_infections:
            produit_densite.append(copy.copy(numpy.dot(T3[c],Beta)))
            produit_gradient = produit_gradient + matrix_inverse_elements(produit_densite[c],T3[c])
            produit_densite_log.append(copy.copy(log_elements(produit_densite[c])))
            c += 1

        grad = copy.copy((Psi1_grad + Psi2_grad) - (produit_gradient))

        for i in range(len(grad)):
            for j in range(len(grad[i])):
                if Beta[i,j] == 0:
                    grad[i,j] = 0


        def hessienne():
            while c < nbre_infections:
                i = 0
                j = 0
                k = 0
                while i < nbre_noeuds:
                    j = 0
                    while j < nbre_noeuds:
                        k = 0
                        while k < nbre_noeuds:
                            if produit_densite[c][j,j] != 0:
                                hessienne[i*nbre_noeuds+j][k*nbre_noeuds+j] += T3[c][i,j]*T3[c][k,j]/(produit_densite[c][j,j]**2)
                            k += 1
                        j += 1
                    i += 1
                c+=1

        return ((- somme_diag_elements(Psi1_intermediaire) - somme_diag_elements(Psi2_intermediaire) - somme_diag_elements(sum(produit_densite)))/nbre_infections, grad/nbre_infections)

    """We then take a initial matrix and we operate a gradient-descent on it to find the estimated matrix"""
    Alpha2 = 0.5 *numpy.ones((nbre_noeuds,nbre_noeuds))
    i = 0
    j = 0
    while i < nbre_noeuds:
        while j < nbre_noeuds:
            if nbre_touche_potentiel[i,j] == 0:
                Alpha2[i,j] = 0
            j+=1
        i += 1
        j = 0

    k = 0
    (value,gradient) = formation_densite(Alpha2)
    value_int = 0
    gradient_intermediaire = 0.00001

    while k < 10000 and norm_matrix(gradient,1) > 0.1:
        Alpha2 = copy.copy(soft_thres(Alpha2 - 0.0005 * gradient,mu))
        i = 0
        while i < len(Alpha2):
            while j < len(Alpha2[i]):
                if Alpha2[i,j] < 0:
                    Alpha2[i,j] = 0
                j += 1
            i += 1
            j = 0

        (value,gradient) = formation_densite(Alpha2)
        if k % 1000 == 0:
            print('GRADIENT -> {}'.format(norm_matrix(gradient,1)))
            print('DIFF --> {}'.format(abs((norm_matrix(gradient,1) - gradient_intermediaire)/gradient_intermediaire)))
            print(value)
            if k % 500000 == 0:
                print(k/500000)
        if k % 1000 == 0 and k > 0:
            print(k)
        k += 1

    if return_value:
        return Alpha2,value
    else:
        return Alpha2