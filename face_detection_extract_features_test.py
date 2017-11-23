import glob
import numpy as np
from skimage.io import imread
from common import extract_patches_features

def extract_save_features():
    val_path = "./data/face_detection/te_raw_images/*/*"
    val_paths = glob.glob(val_path)

    total_records = []
    for idx, path in enumerate(val_paths):
        image = imread(path, as_grey=True)
        values = extract_patches_features(idx, image)
        total_records.append(values)

    total_records = np.array(total_records)
    n, p, f = total_records.shape
    total_records = total_records.reshape(n * p, f)
    np.savetxt("face_detection_test.txt", total_records, delimiter=",")

extract_save_features()     
