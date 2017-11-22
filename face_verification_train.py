import scipy.io as sio
import numpy as np
from common import extract_features, extract_features_lbp
from sklearn.svm import SVC

def load_data(pref, path):
    
    data = sio.loadmat(path)
    X1 = data[pref + "_img_pair"][:, 0]
    X2 = data[pref + "_img_pair"][:, 2]
    X = []
    
    for x1, x2 in zip(X1, X2):
        X.append([x1, x2])
        
    Y = data['Y' + pref]
    return np.array(X), Y

def recursive_extraction(el, num_iter=2):
    ori, pix_cell, block_size = 9, 8, 1


    if num_iter == 1:
        return extract_features

def extract_features_all(X):
    X0_hog, X1_hog = [], []
    
    for i, el in enumerate(X):
        # Feature extraction for left image
        h0 = extract_features(el[0], 9, 8, 1)
        X0_hog.append(h0)
        
        # Feature extraction for right image
        h1 = extract_features(el[1], 9, 8, 1)
        X1_hog.append(h1)

    X_hog = np.array(X0_hog) - np.array(X1_hog)
    
    return X_hog

def convert_features(features):
    n, l = features.shape
    
    f_ = features.reshape((n, 9, 64))
    std = np.std(f_, axis=2)
       
    return std
    
def train_model():
    X_train, Y_train = load_data('tr', './data/face_verification/face_verification_tr.mat')
    X_val, Y_val = load_data('va', './data/face_verification/face_verification_va.mat')

    Xfeat_train = extract_features_all(X_train)
    Xfeat_val = extract_features_all(X_val)

    print(Xfeat_train)

    Xt_ = convert_features(Xfeat_train)
    Xv_ = convert_features(Xfeat_val)

    model = SVC(kernel="linear", C=100.0)
    model.fit(Xt_, Y_train)
    return model.score(Xv_, Y_val)

print(train_model())
