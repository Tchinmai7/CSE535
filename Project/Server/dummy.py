import pyedflib
import numpy as np
import os
local_path= "data/S001/S001R01.edf"
f = pyedflib.EdfReader(local_path)
n = f.signals_in_file
signal_labels = f.getSignalLabels()
sigbufs = []
for idx in np.arange(n):
    sigbufs.append(f.readSignal(idx))
print(len(sigbufs))
print(sigbufs)
