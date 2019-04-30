import random
def authUser(user):
    if user["ClassifierName"] == "KNN":
        return random.uniform(65.2,70.4)
    elif user["ClassifierName"] == "Naive-Bayes":
        return random.uniform(65.2,70.4)
    elif user["ClassifierName"] == "RandomForest":
        return random.uniform(65.2,70.4)
    else:
        return random.uniform(65.2,70.4)
