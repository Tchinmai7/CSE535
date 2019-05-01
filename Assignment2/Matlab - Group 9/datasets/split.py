import os
if not os.path.exists("data"):
    os.mkdir("data")
if not os.path.exists("data/about"):
    os.mkdir("data/about")
if not os.path.exists("data/father"):
    os.mkdir("data/father")
for filename in os.listdir("raw_data"):
    full_filename = f"raw_data/{filename}"
    if "About" in filename:
        dest_file = f"data/about/{filename}"
    elif "Father" in filename:
        dest_file = f"data/father/{filename}"
    os.rename(full_filename, dest_file)
