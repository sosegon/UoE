import argparse
import glob
from skimage.io import imread
from common import try_prediction, load_clf

#########################################################################################
def evaluate_classifier(clf_name, stride, prob_tresh):
    clf = load_clf(clf_name)

    val_path = "./data/face_detection/te_raw_images/*/*"
    val_paths = glob.glob(val_path)

    with open("{:s}_val.log".format(clf_name), "w+") as log_file:
        for idx, path in enumerate(val_paths):
            image = imread(path, as_grey=True)
            locations, predictions = try_prediction(clf, image, stride, prob_tresh)
            
            for loc, pred in zip(locations, predictions):
                line = "{:d}, {:.6f}, {:d}, {:d}, {:d}, {:d}\n".format(
                idx, pred, loc[0], loc[1], loc[2], loc[3])
                log_file.write(line)
    log_file.close()

#########################################################################################
desc = """ Validate a Neural Network for face detection.
           A log file with the predictions and locations of
           the windows is saved under model_name_val.log
           """

parser = argparse.ArgumentParser(description=desc)
parser.add_argument('model_name', type=str, help="Name of the classifier")
parser.add_argument('-s', dest='stride', type=int, default=48)
parser.add_argument('-p', dest='prob_tresh', type=float, default=0.9)

args = parser.parse_args()
clf_name = args.model_name
stride = args.stride
prob_tresh = args.prob_tresh

evaluate_classifier(clf_name, stride, prob_tresh)