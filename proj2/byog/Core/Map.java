package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import static byog.Core.RandomUtils.uniform;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Map {
    private int width;
    private int height;
    private long seed;
    private Random random;
    private int rWidth = 10;
    private int rHeight = 8;
    private int rNumber = 15;
    private List<Room> existingRooms = new ArrayList(rNumber);
    private TETile[][] world;

    Map(long s, int w, int h) {
        this.seed = s;
        this.width = w;
        this.height = h;
        random = new Random(this.seed);
        world = new TETile[width][height];
    }

    private class Position {
        private int x;
        private int y;


        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private class Room implements Comparable<Room> {
        private int width;
        private int height;
        private Position position;

        Room(Position position, int width, int height) {
            this.position = position;
            this.width = width;
            this.height = height;
        }

        @Override
        public int compareTo(Map.Room o) {
            return this.position.x - o.position.x;
        }
    }




    private Room getNewRandomRoom() {
        int xPos = uniform(random, 0, width);
        int yPos = uniform(random, 0, height);
        int roomWidth = uniform(random, 3, rWidth);
        int roomHeight = uniform(random, 3, rHeight);
        Position pos = new Position(xPos, yPos);
        Room room = new Room(pos, roomWidth, roomHeight);
        return room;
    }


    private Position getRoomLBPosition(Room r) {
        int x = r.position.x - 1;
        int y = r.position.y - 1;
        return new Position(x, y);
    }

    private Position getRoomRAPosition(Room r) {
        int x = r.position.x + r.width;
        int y = r.position.y + r.height;
        return new Position(x, y);
    }


    private boolean isRoomOverlap(Room r1, Room r2) {
        return Math.max(getRoomLBPosition(r1).x, getRoomLBPosition(r2).x)
                  <= Math.min(getRoomRAPosition(r1).x, getRoomRAPosition(r2).x)
                && Math.max(getRoomLBPosition(r1).y, getRoomLBPosition(r2).y)
                  <= Math.min(getRoomRAPosition(r1).y, getRoomRAPosition(r2).y);
    }

    private boolean isSomeRoomOverlap() {
        int size = existingRooms.size();
        if (size <= 1) {
            return false;
        }
        Room last = existingRooms.get(size - 1);
        boolean sign = false;
        for (int i = 0; i < size - 1; i += 1) {
            sign = sign | (isRoomOverlap(last, existingRooms.get(i)));
            if (sign) {
                return true;
            }
        }
        return false;
    }

    private boolean isRoomValid(Room room) {
        return (room.position.x - 1 >= 0) && (room.position.y - 1 >= 0)
                && (room.position.x + room.width < width)
                && (room.position.y + room.height < height);
    }

    private void addRoomFloor(TETile[][] myWorld, Position pos, int myWidth, int myHeight) {
        for (int i = 0; i < myHeight; i += 1) {
            for (int j = 0; j < myWidth; j += 1) {
                myWorld[pos.x + j][pos.y + i] =
                        TETile.colorVariant(Tileset.FLOOR, 64, 64, 64, random);
            }
        }
    }

    private void addLeftWall(TETile[][] myWorld, Position pos, int myHeight) {
        for (int i = 0; i < myHeight; i += 1) {
            myWorld[pos.x - 1][pos.y + i] =
                    TETile.colorVariant(Tileset.WALL, 64, 64, 64, random);
        }
    }

    private void addRightWall(TETile[][] myWorld, Position pos, int myHeight, int myWidth) {
        for (int i = 0; i < myHeight; i += 1) {
            myWorld[pos.x + myWidth][pos.y + i] =
                    TETile.colorVariant(Tileset.WALL, 64, 64, 64, random);
        }
    }

    private void addAboveWall(TETile[][] myWorld, Position pos, int myHeight, int myWidth) {
        for (int i = 0; i < myWidth + 2; i += 1) {
            myWorld[pos.x - 1 + i][pos.y + myHeight] =
                    TETile.colorVariant(Tileset.WALL, 64, 64, 64, random);
        }
    }

    private void addBelowWall(TETile[][] myWorld, Position pos, int myWidth) {
        for (int i = 0; i < myWidth + 2; i += 1) {
            myWorld[pos.x - 1 + i][pos.y - 1]
                    = TETile.colorVariant(Tileset.WALL, 64, 64, 64, random);
        }
    }

    private void addRoomWall(TETile[][] myWorld, Position pos, int myWidth, int myHeight) {
        addBelowWall(myWorld, pos, myWidth);
        addAboveWall(myWorld, pos, myHeight, myWidth);
        addLeftWall(myWorld, pos, myHeight);
        addRightWall(myWorld, pos, myHeight, myWidth);
    }

    private Position getRandomPosition(Room room) {
        int xOffset = uniform(random, 1, room.width - 1);
        int yOffset = uniform(random, 1, room.height - 1);
        return new Position(room.position.x + xOffset, room.position.y + yOffset);
    }

    private boolean isFloor(TETile[][] myWorld, int x, int y) {
        return !(myWorld[x][y].character() == '#' || myWorld[x][y].character() == ' ');
    }

    private void addHorizontalHallway(TETile[][] myWorld, Position a, Position b) {
        int distance = Math.abs(b.x - a.x) + 1;
        if (a.x <= b.x) {
            for (int i = 0; i < distance; i += 1) {
                if (!isFloor(myWorld, a.x + i, a.y)) {
                    myWorld[a.x + i][a.y] =
                            TETile.colorVariant(Tileset.FLOOR, 64, 64, 64, random);
                }
                if (!isFloor(myWorld, a.x + i, a.y - 1)) {
                    myWorld[a.x + i][a.y - 1] =
                            TETile.colorVariant(Tileset.WALL, 64, 64, 64, random);
                }
                if (!isFloor(myWorld, a.x + i, a.y + 1)) {
                    myWorld[a.x + i][a.y + 1] =
                            TETile.colorVariant(Tileset.WALL, 64, 64, 64, random);
                }
            }
        } else {
            for (int i = 0; i < distance; i += 1) {
                if (!isFloor(myWorld, b.x + i, a.y)) {
                    myWorld[b.x + i][a.y] =
                            TETile.colorVariant(Tileset.FLOOR, 64, 64, 64, random);
                }
                if (!isFloor(myWorld, b.x + i, a.y - 1)) {
                    myWorld[b.x + i][a.y - 1] =
                            TETile.colorVariant(Tileset.WALL, 64, 64, 64, random);
                }
                if (!isFloor(myWorld, b.x + i, b.y + 1)) {
                    myWorld[b.x + i][a.y + 1] =
                            TETile.colorVariant(Tileset.WALL, 64, 64, 64, random);
                }
            }
        }
    }

    private void addVerticalHallway(TETile[][] myWorld, Position a, Position b) {
        int distance = Math.abs(a.y - b.y) + 1;
        if (a.y <= b.y) {
            for (int i = 0; i < distance; i += 1) {
                if (!isFloor(myWorld, b.x, a.y + i)) {
                    myWorld[b.x][a.y + i] =
                            TETile.colorVariant(Tileset.FLOOR, 64, 64, 64, random);
                }
                if (!isFloor(myWorld, b.x - 1, a.y + i)) {
                    myWorld[b.x - 1][a.y + i] =
                            TETile.colorVariant(Tileset.WALL, 64, 64, 64, random);
                }
                if (!isFloor(myWorld, b.x + 1, a.y + i)) {
                    myWorld[b.x + 1][a.y + i] =
                            TETile.colorVariant(Tileset.WALL, 64, 64, 64, random);
                }
            }
        } else {
            for (int i = 0; i < distance; i += 1) {
                if (!isFloor(myWorld, b.x, b.y + i)) {
                    myWorld[b.x][b.y + i] =
                            TETile.colorVariant(Tileset.FLOOR, 64, 64, 64, random);
                }
                if (!isFloor(myWorld, b.x - 1, b.y + i)) {
                    myWorld[b.x - 1][b.y + i] =
                            TETile.colorVariant(Tileset.WALL, 64, 64, 64, random);
                }
                if (!isFloor(myWorld, b.x + 1, b.y + i)) {
                    myWorld[b.x + 1][b.y + i] =
                            TETile.colorVariant(Tileset.WALL, 64, 64, 64, random);
                }
            }
        }
    }

    private void addHallway(TETile[][] myWorld, Position a, Position b) {
        addHorizontalHallway(myWorld, a, b);
        addVerticalHallway(myWorld, a, b);
    }

    private  void addDoor(TETile[][] myWorld) {
        TETile t = TETile.colorVariant(Tileset.LOCKED_DOOR, 64, 64, 64, random);
        int xi = uniform(random, 0, width);
        int yi = uniform(random, 0, height);
        while (xi < width && yi < height) {
            if (myWorld[xi][yi].character() == Tileset.WALL.character()) {
                myWorld[xi][yi] = t;
                break;
            }
            yi++;
            if (yi == height) {
                xi++;
                yi = 0;
            }
            if (xi == width) {
                xi = 0;
            }
        }
    }

    public TETile[][] generateWorld() {
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        int totalRoom = uniform(random, 10, rNumber);
        while (totalRoom > 0) {
            Room room =  getNewRandomRoom();
            existingRooms.add(room);
            if (isRoomValid(room) && !isSomeRoomOverlap()) {
                addRoomFloor(world, room.position, room.width, room.height);
                addRoomWall(world, room.position, room.width, room.height);
                totalRoom -= 1;
            } else {
                existingRooms.remove(room);
            }
        }

        Collections.sort(existingRooms, Room::compareTo);

        int size = existingRooms.size();
        for (int i = 0; i < size - 1; i += 1) {
            Position first = getRandomPosition(existingRooms.get(i));
            Position next = getRandomPosition(existingRooms.get(i + 1));
            addHallway(world, first, next);
        }

        addDoor(world);

        return world;
    }
}
