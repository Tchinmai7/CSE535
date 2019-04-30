import pandas as pd
import os
download_dir = os.getcwd() + os.sep + "data"
df = pd.read_csv(download_dir + os.sep + 'data_64.csv')

for i in range(1,len(df.columns)):
    df.ix[:,i]  = df.ix[:,i].apply(lambda x: (x - x.min()) / (x.max() - x.min()))
df.to_csv(download_dir + os.sep +'data_64_pre.csv')
