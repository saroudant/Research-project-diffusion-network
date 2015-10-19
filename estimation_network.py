# -*-coding:Latin-1 -*

from create_adj_matrix import *
from create_cascade import *
import numpy
import math
import copy
from numpy.linalg import norm
from scipy.linalg import eigvals
import os
import random
from fonctions_annexes import soft_thres, err_func, matrix_inverse_elements, log_elements, somme_diag_elements, est_positif, est_diagonal_positif, est_nul, argmax_diff, norm_matrix


"""estimate_network is the main estimation program of MultiNetRate.
It takes the matrices which describe the cascade, the type of diffusion (exp, ray, pow)
and the adjacent matrix to compare with the solution.
To understant this algorithm one can refer to the presentation."""

def estimate_network(A, nbre_noeuds, L, Delta, T, type_diffusion):

    num_cascades =  numpy.zeros((1,nbre_noeuds))
    Psi2 = numpy.zeros((nbre_noeuds, nbre_noeuds))
    Psi1 = numpy.zeros((nbre_noeuds, nbre_noeuds))
    T3 = list()

    i = 0
    nbre_infections = 0
    max_infections = 0
    while i < nbre_noeuds:
        nbre_infections += len(L[i])
        if len(L[i]) > max_infections:
            max_infections = len(L[i])
        i += 1

    T3 = []
    i = 0
    while i < max_infections:
        T3.append(numpy.zeros((nbre_noeuds,nbre_noeuds)))
        i += 1

    L_sorted = []
    L_int = copy.copy(L)
    L_non_touche = []
    i = 0
    j = 0
    while i < nbre_noeuds:
        while j < len(L[i]):
            L_int[i][j] = (i,j,L[i][j])
            j += 1
        L_sorted += L_int[i]
        if len(L[i]) == 0:
            L_non_touche.append(i)
        i += 1
        j = 0

    L_sorted.sort(key = lambda tup : tup[2])

    c = 0
    X = []
    i_int = 0
    j_int = 0
    tho_int = 0.
    while c < nbre_infections:
        for (i_int,j_int,tho_int) in X:
            if tho_int + Delta[i_int][j_int] < L_sorted[c][2] :
                X.remove((i_int,j_int,tho_int))
            else:
                if type_diffusion == 'exp':
                    Psi2[L_sorted[c][0]][i_int] += L_sorted[c][2] - tho_int
                    T3[L_sorted[c][1]][L_sorted[c][0]][i_int] = 1
                elif type_diffusion == 'pl' and L_sorted[c][2] - tho_int > 1 :
                    Psi2[L_sorted[c][0]][i_int] += math.log(L_sorted[c][2] - tho_int)
                    T3[L_sorted[c][1]][L_sorted[c][0]][i_int] = 1. / (L_sorted[c][2] - tho_int)
                elif type_diffusion == 'rayleigh':
                    Psi2[L_sorted[c][0]][i_int] += 0.5*((L_sorted[c][2] - tho_int)**2)
                    T3[L_sorted[c][1]][L_sorted[c][0]][i_int] = 1. * (L_sorted[c][2] - tho_int)
        if L_sorted[c][2] > 0:
            X.append(L_sorted[c])

        for i in L_non_touche:
            if type_diffusion == 'exp':
                Psi1[L_sorted[c][0]][i] += T - L_sorted[c][2]
            elif type_diffusion == 'pl':
                Psi1[L_sorted[c][0]][i] += math.log(T - L_sorted[c][2])
            elif type_diffusion == 'rayleigh':
                Psi1[L_sorted[c][0]][i] += (T - L_sorted[c][2])**2

        c += 1

    """As explained in the presentation, we do an optimization algorithm by itering the calculus.
    Because we only want to compute Psi1 and Psi2 one time we have developed formation_densite which
    uses the global variables Psi1 and Psi2 and a matrix B.
    It returns the value and the gradient of the log-likelihood"""
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
        while c < max_infections:
            produit_densite.append(copy.copy(numpy.dot(T3[c],Beta)))
            produit_gradient = produit_gradient + matrix_inverse_elements(produit_densite[c],T3[c])
            produit_densite_log.append(copy.copy(log_elements(produit_densite[c])))
            c += 1

        grad = copy.copy((Psi1_grad + Psi2_grad) - (produit_gradient))

        for i in range(len(grad)):
            for j in range(len(grad[i])):
                if Beta[i,j] == 0:
                    grad[i,j] = 0

        return ((- somme_diag_elements(Psi1_intermediaire) - somme_diag_elements(Psi2_intermediaire) - somme_diag_elements(sum(produit_densite_log)))/max_infections, grad/max_infections)

    """We then take a initial matrix and we operate a gradient-descent on it to find the estimated matrix"""
    Beta = numpy.ones((nbre_noeuds,nbre_noeuds))
    (value,gradient) = formation_densite(Beta)
    gradient_initial = norm_matrix(gradient,1)
    gradient_intermediaire = 0.00000001
    k = 0
    while norm_matrix(gradient,1) > 0.1 and norm_matrix(gradient,1) < 4 * gradient_initial and abs((norm_matrix(gradient,1) - gradient_intermediaire)/gradient_intermediaire) > 0.000005:
        Beta = soft_thres(Beta-0.0005*gradient,0.000001)
        for i in range(0,len(Beta)):
            for j in range(0,len(Beta[i])):
                if Beta[i,j] < 0:
                    Beta[i,j] = 0
        if k >= 1:
            gradient_intermediaire = norm_matrix(gradient,1)
        (value,gradient) = formation_densite(Beta)
        k += 1

    print('optimization completed')

    """In order to compare the result with the estimated matrix, we use the following indicators"""
    accuracy = 1.0
    acc_num = 0.
    acc_den = 0.
    MAE = 0.
    MAS = 0.
    for i in range (0,len(A)):
        for j in range(0,len(A)):
            if A[i,j] > 0 and Beta[i,j] > 0:
                acc_den
            elif A[i,j] > 0 or Beta[i,j] > 0:
                acc_num += 1.
                acc_den += 1.

            if A[i,j] > 0:
                MAE += abs(A[i,j] - Beta[i,j]) / A[i,j]
            if Beta[i,j] > 0:
                MAS += abs(A[i,j] - Beta[i,j]) / Beta[i,j]

    if acc_den > 0:
        accuracy -= acc_num / acc_den

    return (Beta,(accuracy,MAE,MAS))

