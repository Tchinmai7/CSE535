import random
from .test import testEdf
import json
def authUser(user):
    file_path = user["UserSignalFile"].path
    print(file_path)
    results_path = '/home/ubuntu/CSE535/Project/Server/McServer/trained_models/results.json'
    results = json.loads(results_path)
    if user["ClassifierName"] == "Naive-Bayes":
        #/home/ubuntu/CSE535/Project/Server/McServer/trained_models/naiveBayesModel.dat
        return results["Naive-Bayes"]
    elif user["ClassifierName"] == "KNN":
        #/home/ubuntu/CSE535/Project/Server/McServer/trained_models/knnModel.dat
        return results["KNN"]
    elif user["ClassifierName"] == "Stochastic-Gradient-Descent":
        #/home/ubuntu/CSE535/Project/Server/McServer/trained_models/SGD.dat
        return results["SGD"]
    else:
        #/home/ubuntu/CSE535/Project/Server/McServer/trained_models/svmModel.dat
        return results["SVM"]
