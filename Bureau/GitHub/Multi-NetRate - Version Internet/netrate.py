# -*-coding:Latin-1 -*
__author__ = 'Soufiane'

from create_adj_matrix import *
from create_cascade import *
from estimation_network import estimate_network

"""Estimation of a network given a set of cascades"""
def netrate(network, cascades, T, type_diffusion):

    #True data
    A, nbre_noeuds = create_adj_matrix(network)
    C, nbre_infections = create_cascade(cascades)

    #OEstimated data given cascades
    A_est = estimate_network(A, C, nbre_noeuds, T, type_diffusion, nbre_infections, 0.001)
    return A_est, A

