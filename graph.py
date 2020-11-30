import matplotlib.pyplot as plt

COLOR_NAMES = ["WHITE", "GREEN", "RED", "ORANGE", "YELLOW", "BLUE"]

# Allows you to visualize how confident the program is in its color groupings
def visualize(confidence):
    fig = plt.figure()
    ax = fig.add_axes([0, 0, 1, 1])
    ax.bar(COLOR_NAMES, confidence, color = ('grey', 'green', 'red', 'orange', 'yellow', 'blue'))
    plt.show()
