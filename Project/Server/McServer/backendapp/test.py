import glob
import argparse
import os
import sys
import pickle

import numpy as np
from sklearn import svm
from sklearn.naive_bayes import GaussianNB
from .train import FeatureExtFromEdf

PROBABILITY_THRESHOLD = 0.5


def testEdf(fileFullPath, model_file):
    # Load edf file
    brainActList = []
    featureSet = FeatureExtFromEdf(fileFullPath)
    brainActList.append(featureSet.tolist())

    # Load trained SVM model
    with open(model_file, 'rb') as pickleFile:
        clf = pickle.load(pickleFile)

    predList = clf.predict(brainActList)
    return predList[0]

if __name__ == "__main__":
    x = testEdf("/home/ubuntu/CSE535/Project/Server/data/S002/S002R13.edf","/home/ubuntu/CSE535/Project/Server/McServer/trained_models/SGD.dat")
    print(x)
    x = testEdf("/home/ubuntu/CSE535/Project/Server/data/S001/S001R13.edf","/home/ubuntu/CSE535/Project/Server/McServer/trained_models/knnModel.dat")
    print(x)
    x = testEdf("/home/ubuntu/CSE535/Project/Server/data/S001/S001R13.edf","/home/ubuntu/CSE535/Project/Server/McServer/trained_models/naiveBayesModel.dat")
    print(x)
    x = testEdf("/home/ubuntu/CSE535/Project/Server/data/S001/S001R13.edf","/home/ubuntu/CSE535/Project/Server/McServer/trained_models/svmModel.dat")
    print(x)
