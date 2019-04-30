import random
from .test import testEdf
import json
import os
def authUser(user):
    file_path = user["UserSignalFile"]
    print(file_path)
    results_path = os.getcwd() + os.sep + 'trained_models/results.json'
    result = {"status": "Success", "accuracy": 0}
    with open(results_path, 'r') as f:
        results = json.load(f)
    if user["ClassifierName"] == "Naive-Bayes":
        prediction = testEdf(file_path, 'trained_models/naiveBayesModel.dat')
        if str(prediction) not in user["UserName"]:
            result["status"] = "failure"
        result["accuracy"] = results["Naive-Bayes"]
        return results["Naive-Bayes"]
    elif user["ClassifierName"] == "KNN":
        prediction = testEdf(file_path, 'trained_models/knnModel.dat')
        if str(prediction) not in user["UserName"]:
            result["status"] = "failure"
        result["accuracy"] = results["KNN"]
        return results["KNN"]
    elif user["ClassifierName"] == "Stochastic-Gradient-Descent":
        prediction = testEdf(file_path, 'trained_models/SGD.dat')
        if str(prediction) not in user["UserName"]:
            result["status"] = "failure"
        result["accuracy"] = results["SGD"]
        return results["SGD"]
    else:
        prediction = testEdf(file_path, 'trained_models/svmModel.dat')
        if str(prediction) not in user["UserName"]:
            result["status"] = "failure"
        result["accuracy"] = results["SVM"]
        return results["SVM"]
