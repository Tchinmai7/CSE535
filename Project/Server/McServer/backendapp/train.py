#!/usr/bin/env python3

import glob
import argparse
import os
import sys
import pickle

import pyedflib
import numpy as np
from scipy.fftpack import fft
from sklearn.model_selection import train_test_split
from sklearn import svm
from sklearn.naive_bayes import GaussianNB
from sklearn.neighbors import KNeighborsClassifier
from sklearn.ensemble import RandomForestRegressor
from sklearn.naive_bayes import MultinomialNB

FFT_SAMPLE_START = 0
FFT_SAMPLE_STOP = 20

# This follows the standard fft procedure found on Matlab fft example
def FeatureExtFromEdf(filename):
    edfFile = pyedflib.EdfReader(filename)
    n_sensor = edfFile.signals_in_file
    n_sample = edfFile.getNSamples()[0]
    featureSet = np.empty(shape=0)
    # Read out the sensor's data one by one
    for sensorIdx in np.arange(n_sensor):
        channel = edfFile.readSignal(sensorIdx)
        Phase1 = fft(channel)
        Phase2 = abs(Phase1/n_sample)*2
        ChannelFFT = Phase2[0:int(n_sample/2)]
        featureSet = np.concatenate((featureSet, ChannelFFT[FFT_SAMPLE_START:FFT_SAMPLE_STOP]))

    return featureSet

def FeaturesFromDir(directory):
    brainActList = []
    # This is for data label
    labelList = []
    # This is only to check how many label are used this time of training
    labelDict = {}
    print('Number of edf files: {}'.format(len(glob.glob(directory + '/*.edf'))))
    for fileFullPath in glob.glob(directory + '/*.edf'):
        fileName = os.path.basename(fileFullPath)
        # use the file name for the label
        ID = int(fileName[1:4])
        featureSet = FeatureExtFromEdf(fileFullPath)
        brainActList.append(featureSet.tolist())
        labelList.append(ID)
        sys.stdout.write('.')
        sys.stdout.flush()
    print ('')
    return [brainActList, labelList]

def writeSvm(directory,modelData):
    clf = svm.SVC(kernel='linear', decision_function_shape='ovo', probability=True)
    clf.fit(modelData[0], modelData[1])
    with open('./svmModel.dat', 'wb') as pickleFile:
        pickle.dump(clf, pickleFile)

def writeknn(directory,modelData):

    classifier = KNeighborsClassifier(n_neighbors=5)
    classifier.fit(modelData[0], modelData[1])
    with open('./knnModel.dat', 'wb') as pickleFile:
        pickle.dump(classifier, pickleFile)

def writeRandomForest(directory,modelData):

    regressor = RandomForestRegressor(n_estimators=20, random_state=0)
    regressor.fit(modelData[0], modelData[1])
    with open('./randomForestModel.dat', 'wb') as pickleFile:
        pickle.dump(regressor, pickleFile)


def writeNaiveBayes(directory,modelData):

    nb = MultinomialNB().fit(modelData[0], modelData[1])
    with open('./naiveBayesModel.dat', 'wb') as pickleFile:
        pickle.dump(nb, pickleFile)


def writeFiles(directory):
    modelData = FeaturesFromDir(directory)
    writeSvm(directory,modelData)
    writeknn(directory,modelData)
    writeRandomForest(directory,modelData)
    writeNaiveBayes(directory,modelData)

#directory = '/Users/sangeethaswaminathan/PycharmProjects/tarun-mc/train2/'
#writeFiles(directory)
