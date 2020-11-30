import cv2
import numpy
from sklearn.cluster import KMeans
from collections import Counter
import matplotlib.pyplot as plt
import graph

ORANGE = [47, 87, 188]
BLUE = [75, 51, 27]
RED = [43, 47, 139]
GREEN = [51, 125, 55]
WHITE = [116, 138, 154]
YELLOW = [55, 138, 154]

COLORS = [WHITE, GREEN, RED, ORANGE, YELLOW, BLUE]
COLOR_NAMES = ["WHITE", "GREEN", "RED", "ORANGE", "YELLOW", "BLUE"]

def get_dominant_color(image, k=4, image_processing_size = None):
    """
    takes an image as input
    returns the dominant color of the image as a list
    
    dominant color is found by running k means on the 
    pixels & returning the centroid of the largest cluster

    processing time is sped up by working with a smaller image; 
    this resizing can be done with the image_processing_size param 
    which takes a tuple of image dims as input

    >>> get_dominant_color(my_image, k=4, image_processing_size = (25, 25))
    [56.2423442, 34.0834233, 70.1234123]
    """
    #resize image if new dims provided
    if image_processing_size is not None:
        image = cv2.resize(image, image_processing_size, 
                            interpolation = cv2.INTER_AREA)
    
    #reshape the image to be a list of pixels
    image = image.reshape((image.shape[0] * image.shape[1], 3))

    #cluster and assign labels to the pixels 
    clt = KMeans(n_clusters = k)
    labels = clt.fit_predict(image)

    #count labels to find most popular
    label_counts = Counter(labels)

    #subset out most popular centroid
    dominant_color = clt.cluster_centers_[label_counts.most_common(1)[0][0]]

    return list(dominant_color)

def matchColor(sticker):
    cv2.imshow("Sticker", sticker)
    bestValue = 100000
    bestColor = -1
    
    imageColor = get_dominant_color(sticker)
    #print(f"Image color {imageColor}")
    cv2.rectangle(sticker, (0, 0), (100, 100), imageColor, 100)
    cv2.imshow("Color", sticker)
    for i, color in enumerate(COLORS):
        #print(f"Processing {COLOR_NAMES[i]}...")
        closeness = 0
        difference = tuple(numpy.subtract(color, imageColor))
        
        #Finding sum of values in the tuple
        for value in difference:
            closeness += abs(value)
        
        if(closeness < bestValue):
            bestValue = closeness
            bestColor = i
            #print(f"{bestColor} is now the closest")
    print(f"{COLOR_NAMES[bestColor]}")
    cv2.waitKey()
    return bestColor

#Matches color, but returns an array of confidence instead of a single color
def matchColorConfidence(sticker):
    cv2.imshow("Sticker", sticker)

    imageColor = get_dominant_color(sticker)
    cv2.rectangle(sticker, (0, 0), (100, 100), imageColor, 100)
    cv2.imshow("Color", sticker)

    confidence = []

    for i, color in enumerate(COLORS): 
        closeness = 0
        difference = tuple(numpy.subtract(color, imageColor))
        #Finding sum of values in the tuple
        for value in difference:
            closeness += abs(value)
        confidence.append(closeness)
    graph.visualize(confidence)
    #cv2.waitKey()
    return confidence