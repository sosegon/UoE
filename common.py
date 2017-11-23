import json
import numpy as np
from skimage.feature import hog
from keras.models import model_from_json
from skimage.feature import local_binary_pattern

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

def extract_features_lbp(image, num_points=12, radius=4, eps=1e-7):
    lbp = local_binary_pattern(image, num_points, radius, method="uniform")
    hist, _ = np.histogram(lbp.ravel(),
                          bins=np.arange(0, num_points + 3),
                          range=(0, num_points + 2))
    # normalization
    hist = hist.astype(float)
    hist /= (hist.sum() + eps)
    
    return hist



def slide_image(image, window_size, stride):
        
    patches = []
    locations = []
    h, w = image.shape
    
    for j in range(0, h - window_size, stride):
        for i in range(0, w - window_size, stride):
            single_patch = image[i:i+window_size, j:j+window_size]
            patches.append(single_patch)
            locations.append([i, j, window_size, window_size])
            
    return np.array(patches), np.array(locations)

def predict(clf, patch_image):
    feats = extract_features(patch_image)
    pred = clf.predict(feats.reshape((1, -1)), batch_size=1)
    pred = np.abs(pred[0][0])
    
    return pred

def try_prediction(clf, image, stride, prob_tresh=0.5):
    patches, locations = slide_image(image, 64, stride)
    
    predictions = []
    
    for patch in patches:
        pred = predict(clf, patch)
        predictions.append(pred)
        
    predictions = np.array(predictions)
    return locations, predictions
    # locations = locations[predictions > prob_tresh]
    # predictions = predictions[predictions > prob_tresh]
    
    # return non_max_suppression_fast(locations, predictions)

def predictsvm(clf, patch_image):
    feats = extract_features(patch_image)
    #pred = clf.predict(feats.reshape((1, -1)))
    pred = clf.predict_proba(feats.reshape((1, -1)))[0][1]
    
    return pred

def extract_patches_features(id, image, stride=48):
    patches, locations = slide_image(image, 64, stride)
    features = []

    for patch in patches:
        feat = extract_features(patch)
        features.append(feat)

    features = np.array(features)

    ids = np.ones(len(patches)).reshape((-1, 1))*id

    return np.hstack((ids, locations, features))


def try_predictionsvm(clf, image, stride, prob_tresh=0.5):
    patches, locations = slide_image(image, 64, stride)
    
    predictions = []
    
    for patch in patches:
        pred = predictsvm(clf, patch)
        predictions.append(pred)
        
    predictions = np.array(predictions)
    return locations, predictions
    
    # locations = locations[predictions > prob_tresh]
    # predictions = predictions[predictions > prob_tresh]
    
    # return non_max_suppression_fast(locations, predictions)

def load_clf(clf_name):
    with open("{:s}.json".format(clf_name), 'r') as jfile:
        model = model_from_json(json.loads(jfile.read()))\

    model.compile("adam", "binary_crossentropy")
    model.load_weights("{:s}.h5".format(clf_name))
    
    return model

# from https://www.pyimagesearch.com/2015/02/16/faster-non-maximum-suppression-python/
# Malisiewicz et al.
def non_max_suppression_fast(boxes, probs, overlapThresh=0.3):
    # if there are no boxes, return an empty list
    if len(boxes) == 0:
        return np.array([]), np.array([])
 
    # if the bounding boxes integers, convert them to floats --
    # this is important since we'll be doing a bunch of divisions
    if boxes.dtype.kind == "i":
        boxes = boxes.astype("float")
 
    # initialize the list of picked indexes 
    pick = []
 
    # grab the coordinates of the bounding boxes
    x1 = boxes[:,0]
    y1 = boxes[:,1]
    w = boxes[:,2]
    h = boxes[:,3]
    x2 = x1 + w
    y2 = y1 + h
 
    # compute the area of the bounding boxes and sort the bounding
    # boxes by the bottom-right y-coordinate of the bounding box
    area = w * h
    idxs = np.argsort(y2)
 
    # keep looping while some indexes still remain in the indexes
    # list
    while len(idxs) > 0:
        # grab the last index in the indexes list and add the
        # index value to the list of picked indexes
        last = len(idxs) - 1
        i = idxs[last]
        pick.append(i)
 
        # find the largest (x, y) coordinates for the start of
        # the bounding box and the smallest (x, y) coordinates
        # for the end of the bounding box
        xx1 = np.maximum(x1[i], x1[idxs[:last]])
        yy1 = np.maximum(y1[i], y1[idxs[:last]])
        xx2 = np.minimum(x2[i], x2[idxs[:last]])
        yy2 = np.minimum(y2[i], y2[idxs[:last]])
 
        # compute the width and height of the bounding box
        w = np.maximum(0, xx2 - xx1 + 1)
        h = np.maximum(0, yy2 - yy1 + 1)
 
        # compute the ratio of overlap
        overlap = (w * h) / area[idxs[:last]]
 
        # delete all indexes from the index list that have
        idxs = np.delete(idxs, np.concatenate(([last],
            np.where(overlap > overlapThresh)[0])))
 
    if len(pick) > 0:
        return boxes[pick].astype("int"), probs[pick]
    else:
        return np.array([]), np.array([])