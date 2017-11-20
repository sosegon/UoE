import json
import numpy as np
from skimage.feature import hog
from keras.models import model_from_json

# from https://classroom.udacity.com/nanodegrees/nd013/parts/fbf77062-5703-404e-b60c-95b78b2f3f9e/modules/2b62a1c3-e151-4a0e-b6b6-e424fa46ceab/lessons/fd66c083-4ccb-4fe3-bda1-c29db76f50a0/concepts/d479f43a-7bbb-4de7-9452-f6b991ece599
def get_hog_features(img, orient, pix_per_cell, cell_per_block, vis=False, feature_vec=True):
    if vis == True:
        features, hog_image = hog(img, orientations=orient, pixels_per_cell=(pix_per_cell, pix_per_cell),
                                  cells_per_block=(cell_per_block, cell_per_block), transform_sqrt=False, 
                                  visualise=True, feature_vector=False)
        return features, hog_image
    else:      
        features = hog(img, orientations=orient, pixels_per_cell=(pix_per_cell, pix_per_cell),
                       cells_per_block=(cell_per_block, cell_per_block), transform_sqrt=False, 
                       visualise=False, feature_vector=feature_vec)
        return features

def extract_features(image, orient=9, pix_per_cell=8, cell_per_block=2):
    hog_feats = get_hog_features(
            image, 
            orient, 
            pix_per_cell, 
            cell_per_block, 
            vis=False, 
            feature_vec=True
        )

    return hog_feats

def slide_image(image, window_size, stride):
        
    patches = []
    locations = []
    h, w = image.shape
    
    for j in range(0, h - window_size, stride):
        for i in range(0, w - window_size, stride):
            single_patch = image[i:i+window_size, j:j+window_size]
            patches.append(single_patch)
            locations.append([i, j, window_size, window_size])
            
    return patches, locations

def predict(clf, patch_image):
    feats = extract_features(patch_image)
    pred = clf.predict(feats.reshape((1, -1)), batch_size=1)
    pred = np.abs(pred[0][0])
    
    return pred

def try_prediction(clf, image, stride):
    patches, locations = slide_image(image, 64, stride)
    
    predictions = []
    
    for patch in patches:
        pred = predict(clf, patch)
        predictions.append(pred)
        
    return locations, predictions

def predictsvm(clf, patch_image):
    feats = extract_features(patch_image)
    pred = clf.predict(feats.reshape((1, -1)))
    
    return pred

def try_predictionsvm(clf, image, stride):
    patches, locations = slide_image(image, 64, stride)
    
    predictions = []
    
    for patch in patches:
        pred = predictsvm(clf, patch)
        predictions.append(pred)
        
    return locations, predictions

def load_clf(clf_name):
    with open("{:s}.json".format(clf_name), 'r') as jfile:
        model = model_from_json(json.loads(jfile.read()))\

    model.compile("adam", "binary_crossentropy")
    model.load_weights("{:s}.h5".format(clf_name))
    
    return model