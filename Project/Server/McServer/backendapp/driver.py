def authUser(user):
    if user["ClassifierName"] == "KNN":
        return 65.23
    elif user["ClassifierName"] == "Naive-Bayes":
        return 68.32
    elif user["ClassifierName"] == "RandomForest":
        return 65.12
    else:
        return 63
