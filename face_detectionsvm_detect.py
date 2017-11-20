import argparse
import matplotlib.pyplot as plt
import matplotlib.patches as pypatches
from skimage.io import imread
from common import try_predictionsvm 
from sklearn.externals import joblib

#########################################################################################
def detect_faces(clf_name, img_name, stride):
    clf = joblib.load("{:s}.pkl".format(clf_name))

    image_rgb = imread(img_name)
    fig = plt.figure()
    ax = fig.add_subplot(111)
    ax.imshow(image_rgb)

    image = imread(img_name, as_grey=True)
    locations, predictions = try_predictionsvm(clf, image, stride)
    
    for loc, pred in zip(locations, predictions):
    	if pred > 0.9:
    		ax.add_patch(pypatches.Rectangle(
                (loc[0], loc[1]),
                loc[2],
                loc[3],
                fill = False,
                edgecolor="#0000ff"
            )
        )

    fig.savefig("detection.png", dpi=90)
    plt.show()

#########################################################################################
desc = """ Use a SVM classifier for face detection.
           An image with the detected face is saved
           under detection.png
           """

parser = argparse.ArgumentParser(description=desc)
parser.add_argument('model_name', type=str, help="Name of the classifier")
parser.add_argument('image_name', type=str, help="Image file")
parser.add_argument('-s', dest='stride', type=int, default=48)

args = parser.parse_args()
clf_name = args.model_name
img_name = args.image_name
stride = args.stride

detect_faces(clf_name, img_name, stride)