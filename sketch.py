
move = 0

def checkWinner(opMove):
    if move == opMove:
      result = 0
    elif move == 0:
      result = -1 if opMove == 1 or opMove == 2 else 1
    elif move == 1:
      result = -1 if opMove == 1 or opMove == 3 else 1
    elif move == 2:
      result = -1 if opMove == 3 or opMove == 4 else 1
    elif move == 3:
      result = -1 if opMove == 0 or opMove == 4 else 1
    elif move == 4:
      result = -1 if opMove == 0 or opMove == 1 else 1
    return result


print(checkWinner(0))