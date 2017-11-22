import scipy.io as sio
import numpy as np
from common import extract_features, extract_features_lbp
from sklearn.svm import SVC
from sklearn.preprocessing import Normalizer

def load_data(pref, path):
    
    data = sio.loadmat(path)
    X = data[pref + "_img_sample"][:,0]
    label= data[pref + "_img_sample"][:,2]
    
    Y = []
    for l in label:
        Y.append(l[0][0])
    
    return X, Y

def extract_features_all(X):

    X_hog = []
    X_lbp = []
    for el in X:
        h, l = extract_features(el), extract_features_lbp(el)
        X_hog.append(h)
        X_lbp.append(l)
        
    X_hog = np.array(X_hog)
    X_lbp = np.array(X_lbp)
    
    norm = Normalizer()
    
    norm.fit(X_hog)
    X_hog = norm.transform(X_hog)
    
    norm.fit(X_lbp)
    X_lbp = norm.transform(X_lbp)
    
    return np.hstack((X_hog, X_lbp))

def train_model():
	X_train, Y_train = load_data('tr', './data/face_recognition/face_recognition_data_tr.mat')
	X_val, Y_val = load_data('va', './data/face_recognition/face_recognition_data_va.mat')

	Xfeat_train = extract_features_all(X_train)
	Xfeat_val = extract_features_all(X_val)

	model = SVC(kernel="linear", C=100.0)
	model.fit(Xfeat_train, Y_train)
	return model.score(Xfeat_val, Y_val)


print(train_model())
