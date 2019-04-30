import glob
import argparse
import os
import sys
import pickle

import numpy as np
from sklearn import svm
from sklearn.naive_bayes import GaussianNB
from train import FeatureExtFromEdf

PROBABILITY_THRESHOLD = 0.5


def testEdf(fileFullPath,model_file):

    # Load edf file
    brainActList = []
    featureSet = FeatureExtFromEdf(fileFullPath)
    brainActList.append(featureSet.tolist())

    # Load trained SVM model
    with open(model_file, 'rb') as pickleFile:
        clf = pickle.load(pickleFile)

    predList = clf.predict(brainActList)

    return predList[0]


#x = testEdf("S010R13.edf","randomForestModel.dat")
#print(x)