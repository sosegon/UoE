import argparse
import glob
import numpy as np
import matplotlib.pyplot as plt

from random import randrange

from skimage.io import imread
from skimage.feature import hog
from sklearn.model_selection import train_test_split
from sklearn.svm import SVC
from sklearn.externals import joblib

from common import extract_features

#########################################################################################
def load_classes_paths(faces_path, non_faces_path):
    face_paths = glob.glob(faces_path)
    non_face_paths = glob.glob(non_faces_path)
    
    return face_paths, non_face_paths

def load_data_from_paths(face_paths, non_face_paths):
    feats_data, labels_data = [], []
    for path in face_paths:
        feats_data.append(imread(path, as_grey=True))
        labels_data.append(1) # for faces
            
    for path in non_face_paths:
        feats_data.append(imread(path, as_grey=True))
        labels_data.append(0) # for non faces

    X = np.array(feats_data)
    y = np.array(labels_data)
    
    return X, y

def load_data():
    faces_path = "./data/face_detection/cropped_faces/*.pgm"
    non_faces_path = "./data/face_detection/non_faces_images/neg_cropped_img/*.png"
    
    face_paths, non_face_paths = load_classes_paths(faces_path, non_faces_path)

    X_all, y_all = load_data_from_paths(face_paths, non_face_paths)

    X_feats_all = []
    for el in X_all:
        X_feats_all.append(extract_features(el))

    X_feats_all = np.array(X_feats_all)

    return train_test_split(X_feats_all, y_all, test_size=0.3, random_state=50)


def train_classifier(model_name):
    X_train, X_val, y_train, y_val = load_data()

    model = SVC(kernel="linear", C=100.0, probability=True)
    model.fit(X_train, y_train)
    model.score(X_val, y_val)

    joblib.dump(model, '{:s}.pkl'.format(model_name))

#########################################################################################
desc = """ Train a SVM classifier for face detection.
           The images used to train the model are under ./data/face_detection
           Each image is converted to a HoG representation.
           The model is saved under model_name.pkl
           """
parser = argparse.ArgumentParser(description=desc)
parser.add_argument('model_name', type=str, help="Name of the classifier")

args = parser.parse_args()
clf_name = args.model_name

train_classifier(clf_name)