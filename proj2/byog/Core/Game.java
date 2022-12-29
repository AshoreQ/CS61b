package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 60;
    public static final int HEIGHT = 30;

    private static final int HUD_OFFSET = 3;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    private void init() {
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
    }

    private boolean isControlCommand(Character c) {
        return c == 'w' || c == 'a' || c == 's' || c == 'd';
    }

    private static void drawMenu() {
        int midWidth = WIDTH / 2;
        int midHeight = HEIGHT / 2;
        StdDraw.clear();
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);

        Font bigFont = new Font("Monaco", Font.BOLD, 40);
        StdDraw.setFont(bigFont);
        StdDraw.text(midWidth, HEIGHT - 6, "CS61B: The Game");

        Font smallFont = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(smallFont);
        StdDraw.text(midWidth, midHeight, "New Game (N)");
        StdDraw.text(midWidth, midHeight - 2, "Load Game (L)");
        StdDraw.text(midWidth, midHeight - 4, "Quit (Q)");
        StdDraw.show();
    }

    private long beginNewGame() {
        int midWidth = WIDTH / 2, midHeight = HEIGHT / 2;
        long seed;
        String input = "";

        StdDraw.clear(Color.BLACK);
        Font bigFont = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(bigFont);
        StdDraw.text(midWidth, midHeight, "Random seed:");

        Font smallFont = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(smallFont);
        StdDraw.text(midWidth, midHeight - 6, "( Please enter S after the number )");
        StdDraw.show();

        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                if (c == 'S') {
                    seed = Long.parseLong(input);
                    return seed;
                } else {
                    input += c;
                    StdDraw.clear(Color.BLACK);
                    StdDraw.setFont(bigFont);
                    StdDraw.text(midWidth, midHeight, "Random seed:");
                    StdDraw.text(midWidth, midHeight - 2, input);
                    StdDraw.setFont(smallFont);
                    StdDraw.text(midWidth, midHeight - 6, "( Please enter S after the number )");
                    StdDraw.show();
                }
            }
        }
    }

    private void displayInformation(World world) {
        String info;
        int xPosition = (int) StdDraw.mouseX();
        int yPosition = (int) StdDraw.mouseY();
        if (yPosition < HEIGHT - HUD_OFFSET) {
            info = world.map[xPosition][yPosition].description();
        } else {
            info = "nothing";
        }
        StdDraw.clear(Color.black);
        StdDraw.setPenColor(Color.white);
        StdDraw.textLeft(1, HEIGHT - 1, info);
        ter.renderFrameWithoutShow(world.map);
        StdDraw.show();
    }

    private void startGame(World world) {
        ter.initialize(WIDTH, HEIGHT, 0, 0);
        boolean quitFlag = false;
        while (!world.gameOver) {
            displayInformation(world);
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                c = Character.toLowerCase(c);
                if (isControlCommand(c)) {
                    world.controlPlayer(c);
                }
                if (c == ':') {
                    quitFlag = true;
                } else if (quitFlag) {
                    if (c == 'q') {
                        saveWorld(world);
                        System.exit(0);
                    } else {
                        quitFlag = false;
                    }
                }
            }
        }
    }

    private void drawGameOver() {
        int midWidth = WIDTH / 2;
        int midHeight = HEIGHT / 2;
        StdDraw.clear(Color.black);
        StdDraw.setPenColor(Color.white);
        Font largeFont = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(largeFont);
        StdDraw.text(midWidth, midHeight, "Good job, you left the place.");
        StdDraw.show();
        StdDraw.pause(1500);
        drawMenu();
    }

    private static World loadWorld() {
        File f = new File("./world.txt");
        if (f.exists()) {
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                World loadWorld = (World) os.readObject();
                os.close();
                return loadWorld;
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                System.exit(0);
            }
        }
        /* In the case no World has been saved yet, we return null. */
        return null;
    }

    private static void saveWorld(World w) {
        File f = new File("./world.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(w);
            os.close();
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    public void playWithKeyboard() {
        init();
        drawMenu();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                c = Character.toLowerCase(c);
                switch (c) {
                    case 'n':
                        long seed = beginNewGame();
                        Map mp = new Map(seed, WIDTH, HEIGHT - HUD_OFFSET);
                        TETile[][] map = mp.generateWorld();
                        World world = new World(map, seed);
                        System.out.println(TETile.toString(world.map));
                        startGame(world);
                        drawGameOver();
                        break;
                    case 'l':
                        try {
                            World w = loadWorld();
                            System.out.println(TETile.toString(w.map));
                            startGame(w);
                            drawGameOver();
                        } catch (NullPointerException e) {
                            System.out.println("No archives," + e);
                        }
                        break;
                    case 'q':
                        System.exit(0);
                        break;
                    default:
                }
            }
        }
    }

    private World parseInputString(World world, String command) {
        for (int i = 0; i < command.length(); i++) {
            char c = command.charAt(i);
            if (isControlCommand(c)) {
                world.controlPlayer(c);
            } else if (c == ':' && command.charAt(i + 1) == 'q') {
                saveWorld(world);
                break;
            }
        }
        return world;
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        input = input.toLowerCase();
        if (input.startsWith("n")) {
            int sIndex = input.indexOf("s");
            long seed = Long.parseLong(input.substring(1, sIndex));
            Map mp = new Map(seed, WIDTH, HEIGHT - HUD_OFFSET);
            TETile[][] map = mp.generateWorld();
            World initWorld = new World(map, seed);
            World world = parseInputString(initWorld, input.substring(sIndex + 1));
            return world.map;
        } else if (input.startsWith("l")) {
            World initWorld = loadWorld();
            if (!initWorld.equals(null)) {
                World world = parseInputString(initWorld, input.substring(1));
                return world.map;
            }
        }
        return null;
    }
}
