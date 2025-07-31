package br.com.dio;

import br.com.dio.model.Board;
import br.com.dio.model.Space;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static br.com.dio.service.BoardService.BOARD_LIMIT;
import static br.com.dio.util.BoardTemplate.BOARD_TEMPLATE;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Main {
  private final static Scanner s = new Scanner(System.in);
  private static Board board;
  public static void main(String[] args) {
    final var positions = Stream.of(args).collect(Collectors.toMap(
        k -> k.split(";")[0],
        v -> v.split(";")[1]
    ));
    var option = -1;

    while (true) {
      System.out.println("Selecione uma das opções a seguir");
      System.out.println("1 - Iniciar um novo Jogo");
      System.out.println("2 - Colocar um novo número");
      System.out.println("3 - Remover um número");
      System.out.println("4 - Visualizar jogo atual");
      System.out.println("5 - Verificar status do jogo");
      System.out.println("6 - limpar jogo");
      System.out.println("7 - Finalizar jogo");
      System.out.println("8 - Sair");

      option = s.nextInt();

      switch (option) {
        case 1 -> startGame(positions);
        case 2 -> inputNumber();
        case 3 -> removeNumber();
        case 4 -> showCurrentGame();
        case 5 -> showGameStatus();
        case 6 -> clearGame();
        case 7 -> finishGame();
        case 8 -> System.exit(0);
        default -> System.out.println("Opção inválida, selecione uma das opções do menu");
      }
    }
  }

  private static void startGame(Map<String, String> positions) {
    if (nonNull(board)) {
      System.out.println("Jogo ja iniciado");
      return;
    }
    List<List<Space>> spaces = new ArrayList<>();
    for (int i = 0; i < BOARD_LIMIT; i++) {
      spaces.add(new ArrayList<>());
      for (int j = 0; j < BOARD_LIMIT; j++) {
        var positionConfig = (positions.get("%s,%s".formatted(i, j)));
        var expected = Integer.parseInt(positionConfig.split(",")[0]);
        var fixed = Boolean.parseBoolean(positionConfig.split(",")[1]);
        var currentSpace = new Space(expected, fixed);
        spaces.get(i).add(currentSpace);
      }
    }
    board = new Board(spaces);
    System.out.println("O está pronta para iniciar");
  }

  private static void showCurrentGame() {
    if (isNull(board)) {
      System.out.println("O jogo não foi iniciado ainda");
      return;
    }
    var args = new Object[81];
    var argsPos = 0;
    for (int i = 0; i < BOARD_LIMIT; i++) {
      for (var col1 : board.getSpaces()) {
        args[argsPos++] = " " + (isNull(col1.get(i).getActual()) ? " " : col1.get(i).getActual());
      }
    }
    System.out.println("Seu jogo se encontra da seguinte forma");
    System.out.printf((BOARD_TEMPLATE) + "%n", args);
  }

  private static void showGameStatus() {
    if (isNull(board)) {
      System.out.println("O jogo não foi iniciado ainda");
      return;
    }
    System.out.printf("O jogo se encontra no status %s \n", board.getStatus().getLabel());
    if (board.hasErrors()) {
      System.out.println("O jogo contém erros");
    } else {
      System.out.println("O jogo não possui erros");
    }
  }

  private static void clearGame() {
    if (isNull(board)) {
      System.out.println("O jogo não foi iniciado ainda");
      return;
    }
    System.out.println("Tem certeza que deseja limpar o jogo e perder tood o seu progresso?(y/n)");
    var confirm = s.next();
    while (!confirm.equalsIgnoreCase("y") && !confirm.equalsIgnoreCase("n")) {
      System.out.println("Digite \"y\" ou \"n\"");
      confirm = s.next();
    }
    if (confirm.equalsIgnoreCase("y")) {
      board.reset();
    }
  }

  private static void finishGame() {
    if (isNull(board)) {
      System.out.println("O jogo não foi iniciado ainda");
      return;
    }
    if (board.gameIsFinished()) {
      System.out.println("Parabéns jogo finalizado");
      showCurrentGame();
      board = null;
    } else if (board.hasErrors()) {
      System.out.println("Seu jogo consta erros verifique seu board e ajuste-o");
    }
  }

  private static void removeNumber() {
    if (isNull(board)) {
      System.out.println("O jogo ainda não foi iniciado");
      return;
    }
    System.out.println("Informe a coluna em que o numero sera inserido");
    int col = runUntilGetValidNumber(0, 8);
    System.out.println("Informe a linha em que o numero sera inserido");
    int row = runUntilGetValidNumber(0, 8);
    if (!board.clearValue(col, row)) {
      System.out.printf("A posição [%s,%s] possui um valor fixo\n", col, row);
    }
  }

  private static void inputNumber() {
    if (isNull(board)) {
      System.out.println("Ojogo ainda não foi iniciado");
      return;
    }
    System.out.println("Informe a coluna em que o numero sera inserido");
    int col = runUntilGetValidNumber(0, 8);
    System.out.println("Informe a linha em que o numero sera inserido");
    int row = runUntilGetValidNumber(0, 8);
    System.out.println("Informe o numero que vai entra na posição escolhida");
    int value = runUntilGetValidNumber(1, 9);
    if (!board.changeValue(col, row, value)) {
      System.out.printf("A posição [%s,%s] possui um valor fixo\n", col, row);
    }
  }

  private static int runUntilGetValidNumber(final int min, final int max) {
    int current = s.nextInt();
    while (current < min || current > max) {
      System.out.printf("Informe um numero entre %s e %s", min, max);
      current = s.nextInt();
    }
    return current;
  }

}
