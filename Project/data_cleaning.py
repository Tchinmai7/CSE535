import urllib.request
import pyedflib
import numpy as np
import os
import pandas as pd
download_dir = os.getcwd()

for i in range(1,110):
    subject = "S{:03d}".format(i)
    subject_upload = "S{:03d}CSV".format(i)
    dir_path = "{0}/{1}".format(download_dir, subject)
    dir_path_csv = "{0}/{1}".format(download_dir, subject_upload)
    if not os.path.exists(dir_path_csv):
        os.mkdir(dir_path_csv)
    for r in range(1, 15):
        run_num = "R{:02d}".format(r)
        file_name = "{0}/{0}{1}.edf".format(subject, run_num)
        file_name_csv = "{0}{1}.csv".format(subject_upload, run_num)
        local_path = "{0}/{1}".format(download_dir, file_name)
        f = pyedflib.EdfReader(local_path)
        n = f.signals_in_file
        signal_labels = f.getSignalLabels()
        sigbufs = []
        for idx in np.arange(n):
            sigbufs.append(f.readSignal(idx))
        #print(len(sigbufs))
        #print(len(sigbufs[0]))
        #print(dir_path_csv,file_name_csv)
        np.savetxt(dir_path_csv + "/"+file_name_csv, sigbufs, delimiter=",")



