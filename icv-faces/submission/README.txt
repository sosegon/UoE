#################################################################
# ICV coursework: Face detection, recognition and verification	#
# Mengzhu Cai s1734032											#
# Sebastian Velasquez s1700260									#
#################################################################

- For every task an SVM classifier was used.
- The models were developed in python using sklearn, skimage and numpy libraries.
- For evaluation, the parameters of every model (weights and biases) were stored in a text file.
- Similarly, to keep consistency, the features for the test datasets were extracted using python and saved in text files.
- The matlab scripts for evaluation read the parameters and features files to perform the evaluation.
- For bonus part (face detection), another svm model and a custom hog were developed under matlab. Also a neural network was created
  in python using Keras.
