import random
from .test import testEdf
import json
import os


def authUser(user):
    file_path = user["UserSignalFile"]
    print(file_path)
    results_path = os.getcwd() + os.sep + 'trained_models/results.json'
    result = {"status": "Success", "accuracy": 0, "userName": user["UserName"]}
    with open(results_path, 'r') as f:
        results = json.load(f)
    if user["ClassifierName"] == "Naive-Bayes":
        prediction = testEdf(file_path, 'trained_models' + os.sep + 'naiveBayesModel.dat')
        result["prediction"] = str(prediction)
        if str(prediction) not in user["UserName"]:
            result["status"] = "failure"
        result["accuracy"] = results["Naive-Bayes"]
        return result
    elif user["ClassifierName"] == "KNN":
        prediction = testEdf(file_path, 'trained_models' + os.sep + 'knnModel.dat')
        result["prediction"] = str(prediction)
        if str(prediction) not in user["UserName"]:
            result["status"] = "failure"
        result["accuracy"] = results["KNN"]
        return result
    elif user["ClassifierName"] == "Stochastic-Gradient-Descent":
        prediction = testEdf(file_path, 'trained_models' + os.sep + 'SGD.dat')
        result["prediction"] = str(prediction)
        if str(prediction) not in user["UserName"]:
            result["status"] = "failure"
        result["accuracy"] = results["SGD"]
        return result
    else:
        prediction = testEdf(file_path, 'trained_models' + os.sep + 'svmModel.dat')
        result["prediction"] = str(prediction)
        if str(prediction) not in user["UserName"]:
            result["status"] = "failure"
        result["accuracy"] = results["SVM"]
        return result
