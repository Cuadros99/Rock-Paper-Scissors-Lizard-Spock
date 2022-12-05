import java.util.*;

public class Game {
    ArrayList<Integer[]> record;
    Integer nVictories, nLoss, round, nRounds, move;
    String[] movesNames = {"Pedra", "Spock", "Papel", "Lagarto", "Tesoura"};
    Communication server;
  
    public Game(int port, int limitRounds) {
        record = new ArrayList<Integer[]>();
        nVictories = 0;
        nLoss = 0;
        round = 1;
        move = null;
        nRounds = limitRounds;
        server = new Communication(port);
    }

    public void play() {
        while (round <= nRounds) {
            Integer status;
            int opMove;
            printSummary();
            System.out.printf("Digite a sua próxima jogada:\n\n1. Pedra\n2. Spock\n3. Papel\n4. Lagarto\n5. Tesoura\n\nJogada: ");

            move = Bot.computeNextMove(record);
            System.out.printf("%d\n\n",move);
            
            opMove = server.listen() - (int)'0';

            server.respond(move);
            status = checkWinner(opMove);
            
            Integer[] roundRecord = {round,status,move,opMove};
            record.add(roundRecord);

            System.out.printf("%s x %s\n\n",movesNames[move], movesNames[opMove]);

            if (status == 1) {
                nVictories = nVictories + 1;
                System.out.println("Você ganhou a rodada!");
            }
            else if (status == -1) {
                nLoss = nLoss + 1;
                System.out.println("Você perdeu a rodada...");
            }
            else if (status == 0) {
                System.out.println("Você empatou a rodada");
            }
            round = round + 1;
            move = null;
        }

        server.close();
        printSummary();
        System.out.println("");

        if (nVictories > nLoss) 
            System.out.println("Você ganhou a partida!");
        else if (nVictories < nLoss)
            System.out.println("O adversário ganhou a partida...");
        else if (nVictories == nLoss)
            System.out.println("O partida terminou empatada");
        System.out.println("\n");

    }

    private Integer checkWinner(Integer opMove) {
        Integer result = null;
        if (move == opMove)
            result = 0;
        else if (move == 0)
            result = opMove == 1 || opMove == 2 ? -1 : 1;
        else if (move == 1)
            result = opMove == 2 || opMove == 3 ? -1 : 1;
        else if (move == 2)
            result = opMove == 3 || opMove == 4 ? -1 : 1;
        else if (move == 3)
            result = opMove == 0 || opMove == 4 ? -1 : 1;
        else if (move == 4)
            result = opMove == 0 || opMove == 1 ? -1 : 1;
        return result;
    }
        
    private void printSummary() {
        String resultString = "";
        if(!record.isEmpty()) {
            System.out.println("\n\n------------ Resumo da partida ------------\n");
            for (Integer[] roundResult: record) {
                if (roundResult[1] == 1)  
                    resultString = "V";
                else if (roundResult[1] == -1)
                    resultString = "D";
                else if (roundResult[1] == 0)
                    resultString = "E";
                System.out.printf("%d. %s - %s x %s\n",roundResult[0], resultString, 
                    movesNames[roundResult[2]], movesNames[roundResult[3]]);
            }
            System.out.println("");
        }
        if (round <= nRounds)
            System.out.printf("============ RODADA %d ============\n", round);
    }
}
