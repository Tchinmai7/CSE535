import pandas as pd
import os
from sklearn.svm import SVC
from joblib import dump, load

download_dir = os.getcwd() + os.sep + "data"
print("Trying to read", flush=True)
df = pd.read_csv(download_dir + os.sep + 'train.csv',header=None)
print("Done", flush=True)
new_df = df.ix[:,1:1000]
labels = df.ix[:,0]
print(labels)
del(df)
print("Training Model", flush=True)
svmModel = SVC(kernel = 'linear', C = 1).fit(new_df, labels)
dump(svmModel, 'svmmodel.joblib') 

del(new_df)
del(labels)
print("Trying to read", flush=True)
df = pd.read_csv(download_dir + os.sep + 'test.csv',header=None)
print("Done", flush=True)
new_df = df.ix[:,1:1000]
labels = df.ix[:,0]
print(labels)
del(df)

print("Testing Model", flush=True)
prediction = svmModel.predict(new_df)

count = 0

for i in range(len(labels)):
    print(prediction[i],labels[i])
    if prediction[i] == labels[i]:
        count+=1

print(float(count)/len(labels))

