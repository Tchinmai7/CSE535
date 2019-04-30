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
import json

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
    arr = []
    for path, subdirs, files in os.walk(directory):
        for name in files:
            arr.append(os.path.join(path, name))
    print('Number of edf files: {}'.format(len(arr)))

    for fileFullPath in arr:
        fileName = os.path.basename(fileFullPath)
        print(fileName)
        # use the file name for the label
        ID = int(fileName[1:4])
        featureSet = FeatureExtFromEdf(fileFullPath)
        brainActList.append(featureSet.tolist())
        labelList.append(ID)
        sys.stdout.write('.')
        sys.stdout.flush()
    print ('')
    return [brainActList, labelList]

def writeSvm(directory, train_data, test_data, train_labels, test_labels):
    clf = svm.SVC(kernel='linear', decision_function_shape='ovo', probability=True)
    clf.fit(train_data, train_labels)
    with open(directory + os.sep + 'svmModel.dat', 'wb') as pickleFile:
        pickle.dump(clf, pickleFile)
    return (clf.score(test_data, test_labels) * 100)

def writeknn(directory, train_data, test_data, train_labels, test_labels):
    classifier = KNeighborsClassifier(n_neighbors=5)
    classifier.fit(train_data, train_labels)
    with open(directory + os.sep + 'knnModel.dat', 'wb') as pickleFile:
        pickle.dump(classifier, pickleFile)
    return (classifer.score(test_data, test_labels) * 100)

def writeRandomForest(directory, train_data, test_data, train_labels, test_labels):
    regressor = RandomForestRegressor(n_estimators=20, random_state=0)
    regressor.fit(train_data, train_labels)
    with open(directory + os.sep + 'randomForestModel.dat', 'wb') as pickleFile:
        pickle.dump(regressor, pickleFile)
    return (regressor.score(test_data, test_labels) * 100)


def writeNaiveBayes(directory, train_data, test_data, train_labels, test_labels):
    nb = MultinomialNB()
    nb.fit(train_data, train_labels)
    with open(directory + os.sep + 'naiveBayesModel.dat', 'wb') as pickleFile:
        pickle.dump(nb, pickleFile)
    return (nb.score(test_data, test_labels) * 100)
    
def writeFiles(directory):
    data, labels  = FeaturesFromDir(directory)
    train_data, test_data, train_labels, test_labels = train_test_split(data, labels, test_size = 0.1, random_state = 42)
    results = {}
    results["SVM"] = writeSvm(directory, train_data, test_data, train_labels, test_labels)
    results["KNN"] = writeknn(directory, train_data, test_data, train_labels, test_labels)
    results["RandomForest"] = writeRandomForest(directory, train_data, test_data, train_labels, test_labels)
    results["Naive-Bayes"] = writeNaiveBayes(directory, train_data, test_data, train_labels, test_labels)
    print(results)
    with open(directory + os.sep + 'results.json', 'w') as fp:
        json.dump(results, fp)

directory = '/home/ubuntu/CSE535/Project/Server/data/'
writeFiles(directory)
