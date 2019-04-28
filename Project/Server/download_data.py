import urllib.request
import os
download_dir = "data"
base_url = "https://www.physionet.org/pn4/eegmmidb"
for i in range(1,110):
    subject = "S{:03d}".format(i)
    dir_path = "{0}/{1}".format(download_dir, subject)
    if not os.path.exists(dir_path):
        os.mkdir(dir_path)
    for r in range(1, 15):
        run_num = "R{:02d}".format(r)
        file_name = "{0}/{0}{1}.edf".format(subject, run_num)
        url = "{0}/{1}".format(base_url, file_name)
        local_path = "{0}/{1}".format(download_dir, file_name)
        if not os.path.exists(local_path):
            urllib.request.urlretrieve(url, local_path)
            print(url)
