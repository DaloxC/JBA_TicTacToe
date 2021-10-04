/* Game TicTacToe

DaloxC
*/
package tictactoe;

import java.util.*;

class OutBoundsMoveException extends Exception {
}

class OccupiedCoordinateException extends Exception {
}

class Coordinates {
    public final int x;
    public final int y;

    Coordinates(int x, int y) throws OutBoundsMoveException {
        if (x < 0 || x > 2 || y < 0 || y > 2) {
            throw new OutBoundsMoveException();
        }
        this.x = x;
        this.y = y;
    }
}

public class TicTacToe {
    public static String[][] getRows(String[] gameState) {
        String[][] rows = new String[3][3];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(gameState, i * 3, rows[i], 0, 3);
        }
        return rows;
    }

    public static String[][] getCols(String[] gameState) {
        String[][] cols = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                cols[i][j] = gameState[i + 3 * j];
            }
        }
        return cols;
    }

    public static boolean isLineOf(String player, String[] line) {
        String[] expectedLine = new String[]{player, player, player};
        return Arrays.equals(line, expectedLine);
    }

    public static boolean isLinesOf(String player, String[][] lines) {
        for (String[] line : lines) {
            if (isLineOf(player, line)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isRowOf(String player, String[] gameState) {
        return isLinesOf(player, getRows(gameState));
    }

    public static boolean isColOf(String player, String[] gameState) {
        return isLinesOf(player, getCols(gameState));
    }

    public static boolean isRowOfXs(String[] gameState) {
        return isRowOf("X", gameState);
    }

    public static boolean isRowOfOs(String[] gameState) {
        return isRowOf("O", gameState);
    }

    public static boolean isColOfXs(String[] gameState) {
        return isColOf("X", gameState);
    }

    public static boolean isColOfOs(String[] gameState) {
        return isColOf("O", gameState);
    }

    public static String[] getRightDiagonal(String[] gameState) {
        return new String[]{gameState[0], gameState[4], gameState[8]};
    }

    public static String[] getLeftDiagonal(String[] gameState) {
        return new String[]{gameState[2], gameState[4], gameState[6]};
    }

    public static boolean isRightDiagonalOf(String player, String[] gameState) {
        return isLineOf(player, getRightDiagonal(gameState));
    }

    public static boolean isLeftDiagonalOf(String player, String[] gameState) {
        return isLineOf(player, getLeftDiagonal(gameState));
    }

    public static boolean isRightDiagonalOfXs(String[] gameState) {
        return isRightDiagonalOf("X", gameState);
    }

    public static boolean isRightDiagonalOfOs(String[] gameState) {
        return isRightDiagonalOf("O", gameState);
    }

    public static boolean isLeftDiagonalOfXs(String[] gameState) {
        return isLeftDiagonalOf("X", gameState);
    }

    public static boolean isLeftDiagonalOfOs(String[] gameState) {
        return isLeftDiagonalOf("O", gameState);
    }

    public static boolean isDiagonalOfXs(String[] gameState) {
        return isRightDiagonalOfXs(gameState) || isLeftDiagonalOfXs(gameState);
    }

    public static boolean isDiagonalOfOs(String[] gameState) {
        return isRightDiagonalOfOs(gameState) || isLeftDiagonalOfOs(gameState);
    }

    public static boolean isXWins(String[] gameState) {
        return isRowOfXs(gameState) || isColOfXs(gameState) || isDiagonalOfXs(gameState);
    }

    public static boolean isOWins(String[] gameState) {
        return isRowOfOs(gameState) || isColOfOs(gameState) || isDiagonalOfOs(gameState);
    }

    public static boolean movesAvailable(String[] gameState) {
        return Arrays.asList(gameState).contains(" ");
    }

    public static boolean hasNoWinner(String[] gameState) {
        return !isXWins(gameState) && !isOWins(gameState);
    }

    public static int count(String player, String[] gameState) {
        int count = 0;
        for (String move : gameState) {
            if (move.equals(player)) {
                count++;
            }
        }
        return count;
    }

    public static boolean wrongNumberOfMoves(String[] gameState) {
        int movesDiff = count("X", gameState) - count("O", gameState);
        return movesDiff < -1 || movesDiff > 1;
    }

    public static boolean isDraw(String[] gameState) {
        return hasNoWinner(gameState) && !movesAvailable(gameState);
    }

    public static boolean isNotFinished(String[] gameState) {
        return hasNoWinner(gameState) && movesAvailable(gameState);
    }

    public static boolean isImpossible(String[] gameState) {
        return isXWins(gameState) && isOWins(gameState) || wrongNumberOfMoves(gameState);
    }

    public static void printBoard(String[] gameState) {
        System.out.println("---------");
        for (int i = 0; i < 3; i++) {
            System.out.print("| ");
            for (int j = 0; j < 3; j++) {
                System.out.printf("%s ", gameState[3 * i + j]);
            }
            System.out.println("|");
        }
        System.out.println("---------");
    }

    public static String getStatus(String[] gameState) {
        String status = "Unknown game state";

        if (isImpossible(gameState)) {
            status = "Impossible";
        } else if (isNotFinished(gameState)) {
            status = "Game not finished";
        } else if (isXWins(gameState)) {
            status = "X wins";
        } else if (isOWins(gameState)) {
            status = "O wins";
        } else if (isDraw(gameState)) {
            status = "Draw";
        }

        return status;
    }

    public static Coordinates readUserMove() throws OutBoundsMoveException {
        Scanner scanner = new Scanner(System.in);
        int y = scanner.nextInt() - 1;
        int x = scanner.nextInt() - 1;
        return new Coordinates(x, y);
    }

    public static void makeUserMove(Coordinates coordinates, String[] gameState, String player)
            throws OccupiedCoordinateException {
        String target = gameState[coordinates.y * 3 + coordinates.x];

        if (target.equals("X") || target.equals("O")) {
            throw new OccupiedCoordinateException();
        }

        gameState[coordinates.y * 3 + coordinates.x] = player;
    }

    public static void processUserMove(String[] gameState, String player) {
        try {
            System.out.print("Enter the coordinates: ");
            Coordinates coordinates = readUserMove();
            makeUserMove(coordinates, gameState, player);
        } catch (OutBoundsMoveException e) {
            System.out.println("Coordinates should be from 1 to 3!");
            processUserMove(gameState, player);
        } catch (OccupiedCoordinateException e) {
            System.out.println("This cell is occupied! Choose another one!");
            processUserMove(gameState, player);
        } catch (InputMismatchException e) {
            System.out.println("You should enter numbers!");
            processUserMove(gameState, player);
        }
    }

    public static String[] emptyBoard() {
        String[] board = new String[9];
        Arrays.fill(board, " ");
        return board;
    }

    public static void play() {
        String[] gameState = emptyBoard();
        String gameStatus = getStatus(gameState);
        String nextPlayer = "X";

        while (gameStatus.equals("Game not finished")) {
            printBoard(gameState);
            processUserMove(gameState, nextPlayer);
            gameStatus = getStatus(gameState);
            nextPlayer = nextPlayer.equals("X") ? "O" : "X";
        }

        printBoard(gameState);
        System.out.println(gameStatus);
    }

    public static void main(String[] args) {
        play();
    }
}
