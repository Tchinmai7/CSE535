import pyedflib
import os
import numpy as np
import csv
download_dir = os.getcwd() + os.sep + 'data'
for i in range(1,15):
    subject = "S{:03d}".format(i)
    dir_path = "{0}/{1}".format(download_dir, subject)
    for r in range(1, 15):
        run_num = "R{:02d}".format(r)
        file_name = "{0}/{0}{1}.edf".format(subject, run_num)
        local_path = "{0}/{1}".format(download_dir, file_name)
        f = pyedflib.EdfReader(local_path)
        n = f.signals_in_file
        signal_labels = f.getSignalLabels()
        sigbufs = []
        for idx in np.arange(n):
            sigbufs.extend([subject])
            sigbufs.extend(f.readSignal(idx))
        with open(download_dir + os.sep + "data_64.csv", 'a') as f:
            writer = csv.writer(f, delimiter=',', quotechar='|', quoting=csv.QUOTE_MINIMAL, lineterminator='\n')

            writer.writerow(sigbufs)
        print("Done",i)

