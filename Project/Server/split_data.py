import pandas as pd
import os
from sklearn.model_selection import train_test_split

data_dir = os.getcwd() + os.sep + 'data'
filename = data_dir + os.sep + 'data_new.csv'
test_file = data_dir + os.sep + 'test.csv'
train_file = data_dir + os.sep + 'train.csv'
df = pd.read_csv(filename, header=None)
for i in range(1,110):
    Labels = "S{:03d}".format(i)
    label_df = df.loc[( df[0] == Labels)]
    train_label, test_label = train_test_split(label_df,  test_size=0.33, random_state=42)
    print(Labels, label_df.shape, train_label.shape, test_label.shape)
    with open(train_file, 'a') as f:
        train_label.to_csv(f, header=False, index=False)
    with open(test_file, 'a') as f:
        test_label.to_csv(f, header=False, index=False)
    del(test_label)
    del(train_label)
    del(label_df)
