import argparse
import glob
import numpy as np
import matplotlib.pyplot as plt
import json

from random import randrange

from skimage.io import imread
from skimage.feature import hog
from sklearn.model_selection import train_test_split

from keras.models import Sequential
from keras.layers import Dense, Activation
from keras.optimizers import Adam
from keras import regularizers
from keras.callbacks import ModelCheckpoint

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

def get_model(learning_rate, num_feats):
    model = Sequential()
    model.add(Activation('relu', batch_input_shape=(None, num_feats)))
    
    model.add(Dense(500, init='uniform',
                    kernel_regularizer=regularizers.l2(0.01)))
    model.add(Activation('relu'))
    
    model.add(Dense(100, init='uniform'))
    model.add(Activation('relu'))

    model.add(Dense(10, init='uniform'))
    model.add(Activation('relu'))

    model.add(Dense(1, init='uniform'))
    model.add(Activation('sigmoid'))

    optimizer = Adam(lr=learning_rate)
    model.compile(optimizer=optimizer, loss="binary_crossentropy", metrics=['binary_accuracy'])

    return model

def generator(X, y, batch_size):
    total_input = len(X)
    
    while True:
        features, targets = [], []
        i = 0
        while len(features) < batch_size:
            index = randrange(0, total_input)
            feats = X[index]
            labels = y[index]
           
            features.append(feats)
            targets.append(labels)
            
        yield (np.array(features), np.array(targets))

def getFeaturesTargets(X, y):
    feats = []
    targets = []

    for feat, label in zip(X, y):
        feats.append(feat)
        targets.append(label)

    return np.array(feats), np.array(targets)

def save_plot_metrics(model_name, history):
    keys = history.history.keys()

    f, ax = plt.subplots(len(keys), 1, figsize=(5, 22))

    for idx, k in enumerate(keys):
        ax[idx].plot(history.history[k])
        ax[idx].set_title("model " + k)
        ax[idx].set_ylabel(k)
        ax[idx].set_xlabel('epoch')
    
    f.savefig("{:s}.png".format(model_name), dpi=90)

def train_classifier(model_name, hyper_params, model_save):
    learning_rate = hyper_params[0]
    training_size = hyper_params[1]
    batch_size = hyper_params[2]
    num_epochs = hyper_params[3]

    X_train, X_val, y_train, y_val = load_data()

    model = get_model(learning_rate, len(X_train[0]))

    if model_save:
        filepath = model_name + "-{epoch:02d}-{val_loss:.2f}.h5"
        checkpoint = ModelCheckpoint(filepath, monitor='val_loss', verbose=1, save_best_only=True, mode='min')
        callbacks_list = [checkpoint]
    else:
        callbacks_list = []

    history = model.fit_generator(
        generator(X_train, y_train, batch_size),
        samples_per_epoch = training_size,
        validation_data = getFeaturesTargets(X_val, y_val),
        nb_epoch = num_epochs,
        callbacks = callbacks_list
        )

    save_plot_metrics(model_name, history)

    model.save_weights("{:s}.h5".format(model_name))
    with open("{:s}.json".format(model_name), "w+") as outfile:
        json.dump(model.to_json(), outfile)

    with open("{:s}.hyper".format(model_name), "w+") as hyper_file:
        line = "learning_rate, training_size, batch_size, num_epochs\n"
        hyper_file.write(line)
        line = "{:.4f}, {:d}, {:d}, {:d}".format(
            learning_rate, training_size, batch_size, num_epochs)
        hyper_file.write(line)

    hyper_file.close()

#########################################################################################
desc = """ Train a Neural Network for face detection.
           The images used to train the model are under ./data/face_detection
           Each image is converted to a HoG representation.
           The network has 3 hidden layers with ReLU activations.
           L2 regularization is used.
           Adam optimizer is used.
           The architecture of the model is saved under model_name.json
           The hyperparameters of the model are saved under model_name.hyper
           The weights of the model are saved under model_name.h5
           A plot of the metrics is saved under model_name.png
           """
parser = argparse.ArgumentParser(description=desc)
parser.add_argument('model_name', type=str, help="Name of the classifier")
parser.add_argument('-s', dest='save_iterations', type=bool, default=False)
parser.add_argument('-l', dest='learning_rate', type=float, default=0.001)
parser.add_argument('-t', dest='training_size', type=int, default=200)
parser.add_argument('-b', dest='batch_size', type=int, default=50)
parser.add_argument('-n', dest='num_epochs', type=int, default=30)

args = parser.parse_args()
clf_name = args.model_name
clf_save = args.save_iterations
learning_rate = args.learning_rate
training_size = args.training_size
batch_size = args.batch_size
num_epochs = args.num_epochs

train_classifier(clf_name, [learning_rate, training_size, batch_size, num_epochs], clf_save)