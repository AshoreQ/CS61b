package byog.lab6;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        int seed = Integer.parseInt(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, int seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        // Initialize random number generator
        this.rand = new Random(seed);
    }

    public String generateRandomString(int n) {
        // Generate random string of letters of length
        StringBuilder str = new StringBuilder();
        while (str.length() < n) {
            str.append(CHARACTERS[rand.nextInt(CHARACTERS.length)]);
        }
        return str.toString();
    }

    public void drawFrame(String s) {
        // Take the string and display it in the center of the screen
        // If game is not over, display relevant game information at the top of the screen
        int midWidth = width / 2;
        int midHeight = height / 2;

        StdDraw.clear();
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);

        if (!gameOver) {
            Font smallFont = new Font("Monaco", Font.BOLD, 20);
            StdDraw.setFont(smallFont);
            StdDraw.textLeft(1, height - 1, "Round:" + round);
            StdDraw.text(midWidth, height - 1, playerTurn ? "Type!" : "Watch!");
            StdDraw.textRight(width - 1, height - 1, ENCOURAGEMENT[round % ENCOURAGEMENT.length]);
            StdDraw.line(0, height - 2, width, height - 2);
        }

        Font bigFont = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(bigFont);
        StdDraw.text(midWidth, midHeight,s);
        StdDraw.show();
    }

    public void flashSequence(String letters) {
        // Display each character in letters, making sure to blank the screen between letters
        int size = letters.length();
        for (int i = 0; i < size; i += 1) {
            drawFrame(letters.substring(i, i + 1));
            StdDraw.pause(1000);
            drawFrame(" ");
            StdDraw.pause(500);
        }
    }

    public String solicitNCharsInput(int n) {
        // Read n letters of player input
        String str = "";
        drawFrame(str);
        int totalRead = 0;
        while (totalRead != n) {
            if (StdDraw.hasNextKeyTyped()) {
                str += StdDraw.nextKeyTyped();
                totalRead += 1;
                drawFrame(str);
            }
        }
        StdDraw.pause(500);
        return str;
    }

    public void startGame() {
        // Set any relevant variables before the game starts
        round = 1;
        gameOver = false;
        playerTurn = false;
        String str = "", randStr;
        // Establish Game loop
        while (!gameOver) {
            playerTurn = false;
            drawFrame(str + "Round:" + round + "! Good luck!");
            StdDraw.pause(1500);

            randStr = generateRandomString(round);
            flashSequence(randStr);

            playerTurn = true;

            if (solicitNCharsInput(round).equals(randStr)) {
                drawFrame("Correct, Well done!");
                StdDraw.pause(1000);
                round += 1;
            } else {
                drawFrame("Game Over! You made it to round:" + round);
                gameOver = true;
            }
        }
    }
}
