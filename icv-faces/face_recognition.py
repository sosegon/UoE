import argparse
import scipy.io as sio
import numpy as np
from common import extract_features, extract_features_lbp
from sklearn.svm import SVC, LinearSVC
from sklearn.preprocessing import Normalizer
from sklearn.externals import joblib

def load_data(pref, path):
    
    data = sio.loadmat(path)
    X = data[pref + "_img_sample"][:,0]
    label= data[pref + "_img_sample"][:,2]
    
    Y = []
    for l in label:
        Y.append(l[0][0])
    
    return X, np.array(Y)

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

# Files with features for data sets to be used for evaluation in matlab
def extract_save_features():
    X_train, Y_train = load_data('tr', './data/face_recognition/face_recognition_data_tr.mat')
    X_val, Y_val = load_data('va', './data/face_recognition/face_recognition_data_va.mat')
    X_test, Y_test = load_data('va', './data/face_recognition/face_recognition_data_te.mat')

    Xfeat_train = extract_features_all(X_train)
    Xfeat_val = extract_features_all(X_val)
    Xfeat_test = extract_features_all(X_test)

    YXt_ = np.hstack((Y_train.reshape((-1, 1)), Xfeat_train))
    YXv_ = np.hstack((Y_val.reshape((-1, 1)), Xfeat_val))
    YXts_ = np.hstack((Y_test.reshape((-1, 1)), Xfeat_test))

    np.savetxt("face_recognition_train.txt", YXt_, delimiter=",")
    np.savetxt("face_recognition_val.txt", YXv_, delimiter=",")
    np.savetxt("face_recognition_test.txt", YXts_, delimiter=",")

def train_model():
    X_train, Y_train = load_data('tr', './data/face_recognition/face_recognition_data_tr.mat')
    X_val, Y_val = load_data('va', './data/face_recognition/face_recognition_data_va.mat')

    Xfeat_train = extract_features_all(X_train)
    Xfeat_val = extract_features_all(X_val)

    model = LinearSVC(C=100.0)
    model.fit(Xfeat_train, Y_train)

    # Save the params of the model to use them for evaluation in matlab
    weights = model.coef_
    bias = model.intercept_.reshape((-1, 1))
    params = np.hstack((bias, weights))
    np.savetxt("face_recognition_w_b.txt", params, delimiter=",")

    joblib.dump(model, 'recognition.pkl')

    return model.score(Xfeat_val, Y_val)

def val_model():
    clf = joblib.load("recognition.pkl")
    X_test, Y_test = load_data('va', './data/face_recognition/face_recognition_data_te.mat')

    Xfeat_test = extract_features_all(X_test)

    return clf.score(Xfeat_test, Y_test)

def perform(stage):
    if stage == 0:
        score = train_model()
        print("Score during training: {:.2f}".format(score))
    elif stage ==1:
        score = val_model()
        print("Score during testing: {:.2f}".format(score))
    elif stage == 2:
        extract_save_features()

desc = """ SVM classifier for face recognition
           """
parser = argparse.ArgumentParser(description=desc)
parser.add_argument('stage', type=int, help="Train or testing")

args = parser.parse_args()
stage = args.stage

perform(stage)
