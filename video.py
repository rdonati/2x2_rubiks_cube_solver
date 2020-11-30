import cv2
import color_matcher as cm
import numpy as np
from sklearn.cluster import KMeans
import math
import os
from itertools import permutations

sides = []
stickerColors = []
colorNames = ["WHITE", "GREEN", "RED", "ORANGE", "YELLOW", "BLUE"]

WHITE = (245, 245, 245)
GREEN = (0, 200, 0)
RED = (0, 0, 225)
ORANGE = (0,128,255)
YELLOW = (0, 230, 230)
BLUE = (225, 98, 0)

COLORS = [WHITE, GREEN, RED, ORANGE, YELLOW, BLUE]

def convertState(state):
    A2 = list(np.zeros(24, dtype = int))
    A2[0] = state[19]
    A2[1] = state[18]
    A2[2] = state[17]
    A2[3] = state[16]
    A2[4] = state[8]
    A2[5] = state[9]
    A2[6] = state[10]
    A2[7] = state[11]
    A2[8] = state[12]
    A2[9] = state[13]
    A2[10] = state[14]
    A2[11] = state[15]
    A2[12] = state[4]
    A2[13] = state[5]
    A2[14] = state[6]
    A2[15] = state[7]
    A2[16] = state[23]
    A2[17] = state[22]
    A2[18] = state[21]
    A2[19] = state[20]
    A2[20] = state[3]
    A2[21] = state[2]
    A2[22] = state[1]
    A2[23] = state[0]
    return A2

#Takes a CROPPED image of the cube, returns the color of the top left color
def splitFace(cube):
    #Stores each side [topleft, topright, bottomleft, bottomright]
    stickers = []
    dims = cube.shape
    length = (int)(dims[0] / 2)
    stickers.append(cube[0 : length, 0 : length])
    stickers.append(cube[0 : length, length : 2 * length])
    stickers.append(cube[length : 2 * length, 0 : length])
    stickers.append(cube[length : 2 * length, length : 2 * length])
    # for i, sticker in enumerate(stickers):
    #     cv2.imwrite(f"{n}.jpg", sticker)
    #     cv2.imshow("test", sticker)
    #     cv2.waitKey(1000)
    return stickers

def make(frame):
    frame = cv2.flip(frame, 1)
    frame = addOutline(frame)
    cv2.putText(frame, (str)(len(sides)), (5,frame.shape[0] - 5), cv2.FONT_HERSHEY_SIMPLEX, 2, (0, 255, 0), 2, cv2.LINE_AA)
    return frame

def crop(img):
    dim = img.shape
    centerY = dim[0] / 2
    centerX = dim[1] / 2
    length = dim[0] * 0.2

    return img[(int)(centerY - length): (int)(centerY + length), (int)(centerX - length): (int)(centerX + length)]

def addOutline(frame):
    dim = frame.shape
    centerY = dim[0] / 2
    centerX = dim[1] / 2
    length = dim[0] * 0.2

    topLeft = ((int)(centerX - length), (int)(centerY - length))
    topRight = ((int)(centerX - length), (int)(centerY + length))
    bottomLeft = ((int)(centerX + length), (int)(centerY - length))
    bottomRight = ((int)(centerX + length), (int)(centerY + length))

    frame = cv2.line(frame, topLeft, topRight, (0, 256, 0), 5)
    frame = cv2.line(frame, topLeft, bottomLeft, (0, 256, 0), 5)
    frame = cv2.line(frame, topRight, bottomRight, (0, 256, 0), 5)
    frame = cv2.line(frame, bottomLeft, bottomRight, (0, 256, 0), 5)
    return frame

def openVideo():
    vid = cv2.VideoCapture(0)
    
    while(len(sides) < 6):
        ret, frame = vid.read()
        cv2.imshow("Image", make(frame))

        k = cv2.waitKey(1)
        if(k == ord('q')):
            break
        elif(k == ord(' ')):
            sides.append(crop(frame))

    vid.release()
    cv2.destroyAllWindows()

def matchColors(colors, groupLabels):
    """Converts a list of group labels into a proper list of colors (e.g. white may originally be assigned to 3... this corrects that)""" 

    bestCloseness = 100000000
    bestMatch = []
    
    #Gets all permutations of a list of 0-5, uses these as indices in the folling for loop
    perms = list(permutations(range(6)))

    for idx, permutation in enumerate(perms):
        closeness = 0
        for i in range(6):
            closeness += compareColors(colors[permutation[i]], COLORS[i])
        if(closeness < bestCloseness):
            bestCloseness = closeness
            bestMatch = permutation

    correct = map(lambda x: bestMatch.index(x), groupLabels)

    return list(correct)

def compareColors(c1, c2):
    """Returns the euclidean distance between two colors"""
    sumOfSquares = 0
    for i in range(3):
        sumOfSquares += pow(c1[i] - c2[i], 2)
    return math.sqrt(sumOfSquares)

def colorsAreValid(colors):
    """Takes the labels assigned to each sticker and ensures that there are 6 in each group"""
    for i in range(6):
        if(colors.count(i) != 4):
            print(f"The colors were not able to be accurately grouped into 6 groups of 4 (issue with {i})")
            return False
    print("Colors grouped successfully!")
    return True

def getSolution(state):
    WORKING_DIR = os.getcwd()
    print(WORKING_DIR)
    print(state)
    cmd = f"java -cp {WORKING_DIR}/solving/ CubeGraph {state}"
    os.system(cmd)

def numToChar(state):
    """Converts a list of numbers into a string of chars"""
    chars = ['w', 'g', 'r', 'o', 'y', 'b']
    s = ""
    for sticker in state:
        s += chars[sticker]
    return s

def printColorPallate():
    img = cv2.imread("cube.jpg")
    for i in range(6):
        cv2.rectangle(img, (0, 0), (img.shape[1], img.shape[0]), COLORS[i], -1)
        cv2.imshow("Color", img)
        cv2.waitKey()

def main():
    openVideo()
    for num, side in enumerate(sides):
        #cv2.imshow(f"Side {num}", side)
        stickers = splitFace(side)
        for sticker in stickers:
            stickerColors.append(cm.get_dominant_color(sticker))
    km = KMeans(6)
    km.fit(stickerColors)

    labels = list(km.labels_)
    clusterCenters = list(km.cluster_centers_)

    #Checking sticker color validity
    if(not colorsAreValid(labels)):
        return

    # print(f"Original labels: {labels}")
    matchedColors = matchColors(clusterCenters, labels)
    # print(f"Matched Colors: {matchedColors}")
    finalState = convertState(matchedColors)
    # print(f"Final answer: {finalState}")
    getSolution(numToChar(finalState))

if __name__ == "__main__":
    main()