from enum import Enum
from socket import *
import random



# Deletar classe
class Moves(Enum):
  PEDRA = 0                # 1,2
  SPOCK = 1                # 2,3
  PAPEL = 2                # 3,4
  LAGARTO = 3              # 0,4 
  TESOURA = 4              # 0,1   

movesNames = ("Pedra", "Spock", "Papel", "Lagarto", "Tesoura")

class Game:
  
  def __init__(self, ip, port, nRounds):
    self.record = []
    self.nVictories = 0
    self.oFrequency = [0, 0, 0, 0, 0]
    self.nLoss = 0
    self.round = 1
    self.move = None
    self.HOST = ip
    self.PORT =  port
    self.nRounds = nRounds
    self.soc = socket(AF_INET, SOCK_STREAM)
    self.soc.connect((self.HOST,self.PORT))

  def play(self):
    while self.round <= self.nRounds:
      self.printSummary()

      bot = Bot(self.oFrequency)
      self.move = bot.computeNextMove()

      print(f"Digite a sua próxima jogada:\n\n1. Pedra\n2. Spock\n3. Papel\n4. Lagarto\n5. Tesoura\n\nJogada: {self.move}")

      opMove = self.sendMove(self.move)
      opMove = int.from_bytes(opMove, "big")
      self.oFrequency[opMove] = self.oFrequency[opMove] + 1

      status = self.checkWinner(opMove)
      self.record.append((self.round,status,self.move,opMove))

      print(f"{movesNames[self.move]} x {movesNames[opMove]}\n")

      if status == 1:
        self.nVictories = self.nVictories + 1
        print("Você ganhou a rodada!")
      elif status == -1:
        self.nLoss = self.nLoss + 1
        print("Você perdeu a rodada...")
      elif status == 0:
        print("Você empatou a rodada")
      self.round = self.round + 1
      self.move = None
      
    self.printSummary()
    print("")


    if self.nVictories > self.nLoss:
      print("Você ganhou a partida!")
    elif self.nVictories < self.nLoss:
      print("O adversário ganhou a partida...")
    elif self.nVictories == self.nLoss:
      print("O partida terminou empatada")
    print("\n")


  def sendMove(self, roundMove):
    encodeMove = bytes(str(roundMove), 'utf-8')
    try:
      self.soc.sendall(encodeMove)
      data = self.soc.recv(1024)
      return data
    except:
      exit()

  def checkWinner(self, opMove):
    if self.move == opMove:
      result = 0
    elif self.move == 0:
      result = -1 if opMove == 1 or opMove == 2 else 1
    elif self.move == 1:
      result = -1 if opMove == 2 or opMove == 3 else 1
    elif self.move == 2:
      result = -1 if opMove == 3 or opMove == 4 else 1
    elif self.move == 3:
      result = -1 if opMove == 0 or opMove == 4 else 1
    elif self.move == 4:
      result = -1 if opMove == 0 or opMove == 1 else 1
    return result
    
  def printSummary(self):
    if self.record != []:
      print("\n\n------------ Resumo da partida ------------\n")
      for roundResult in self.record:
        if roundResult[1] == 1:  
          resultString = "V"
        elif roundResult[1] == -1:
          resultString = "D"
        elif roundResult[1] == 0:
          resultString = "E"
        print(str(roundResult[0]) + ". " + resultString + f" - {movesNames[roundResult[2]]} x {movesNames[roundResult[3]]}")
      print("")

    if self.round <= self.nRounds:
      print(f"============ RODADA {self.round} ============\n")
    


class Bot:
  def __init__(self, f):
    self.frequency = f
  
  def analyzeFrequency(self):
    amount = 0
    frequencyIndex = [0,0,0,0,0]
    for i in range(5):
      amount = amount + 15 - self.frequency[i]
      frequencyIndex[i] = amount
    return frequencyIndex
  
  def computeNextMove(self):
    oMove = 0
    frequencyIndex = self.analyzeFrequency()
    index = random.randint(0, frequencyIndex[4])
    for i in range(5):
      if index < frequencyIndex[i]:
        oMove = i
        break
    move = (oMove + 2)%5
    return move


g1 = Game("127.0.0.1", 40000, 4)

g1.play()