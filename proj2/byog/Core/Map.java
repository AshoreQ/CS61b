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
    private int Rwidth = 10;
    private int Rheight = 8;
    private int Rnumber = 25;
    private List<Room> existingRooms = new ArrayList(Rnumber);
    private TETile[][] world;

    Map(long seed, int width, int height) {
        this.seed = seed;
        this.width = width;
        this.height = height;
        random = new Random(seed);
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
        int roomWidth = uniform(random, 3, Rwidth);
        int roomHeight = uniform(random, 3, Rheight);
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

    private void addRoomFloor(TETile[][] world, Position pos, int width, int height) {
        for (int i = 0; i < height; i += 1) {
            for (int j = 0; j < width; j += 1) {
                world[pos.x + j][pos.y + i] =
                        TETile.colorVariant(Tileset.FLOOR, 64, 64, 64, random);
            }
        }
    }

    private void addLeftWall(TETile[][] world, Position pos, int height) {
        for (int i = 0; i < height; i += 1) {
            world[pos.x - 1][pos.y + i] =
                    TETile.colorVariant(Tileset.WALL, 64, 64, 64, random);
        }
    }

    private void addRightWall(TETile[][] world, Position pos, int height, int width) {
        for (int i = 0; i < height; i += 1) {
            world[pos.x + width][pos.y + i] =
                    TETile.colorVariant(Tileset.WALL, 64, 64, 64, random);
        }
    }

    private void addAboveWall(TETile[][] world, Position pos, int height, int width) {
        for (int i = 0; i < width + 2; i += 1) {
            world[pos.x - 1 + i][pos.y + height] =
                    TETile.colorVariant(Tileset.WALL, 64, 64, 64, random);
        }
    }

    private void addBelowWall(TETile[][] world, Position pos, int width) {
        for (int i = 0; i < width + 2; i += 1) {
            world[pos.x - 1 + i][pos.y - 1]
                    = TETile.colorVariant(Tileset.WALL, 64, 64, 64, random);
        }
    }

    private void addRoomWall(TETile[][] world, Position pos, int width, int height) {
        addBelowWall(world, pos, width);
        addAboveWall(world, pos, height, width);
        addLeftWall(world, pos, height);
        addRightWall(world, pos, height, width);
    }

    private Position getRandomPosition(Room room) {
        int xOffset = uniform(random, 1, room.width - 1);
        int yOffset = uniform(random, 1, room.height - 1);
        return new Position(room.position.x + xOffset, room.position.y + yOffset);
    }

    private boolean isFloor(TETile[][] world, int x, int y) {
        return !(world[x][y].character() == '#' || world[x][y].character() == ' ');
    }

    private void addHorizontalHallway(TETile[][] world, Position a, Position b) {
        int distance = Math.abs(b.x - a.x) + 1;
        if (a.x <= b.x) {
            for (int i = 0; i < distance; i += 1) {
                if (!isFloor(world, a.x + i, a.y)) {
                    world[a.x + i][a.y] =
                            TETile.colorVariant(Tileset.FLOOR, 64, 64, 64, random);
                }
                if (!isFloor(world, a.x + i, a.y - 1)) {
                    world[a.x + i][a.y - 1] =
                            TETile.colorVariant(Tileset.WALL, 64, 64, 64, random);
                }
                if (!isFloor(world, a.x + i, a.y + 1)) {
                    world[a.x + i][a.y + 1] =
                            TETile.colorVariant(Tileset.WALL, 64, 64, 64, random);
                }
            }
        } else {
            for (int i = 0; i < distance; i += 1) {
                if (!isFloor(world, b.x + i, a.y)) {
                    world[b.x + i][a.y] =
                            TETile.colorVariant(Tileset.FLOOR, 64, 64, 64, random);
                }
                if (!isFloor(world, b.x + i, a.y - 1)) {
                    world[b.x + i][a.y - 1] =
                            TETile.colorVariant(Tileset.WALL, 64, 64, 64, random);
                }
                if (!isFloor(world, b.x + i, b.y + 1)) {
                    world[b.x + i][a.y + 1] =
                            TETile.colorVariant(Tileset.WALL, 64, 64, 64, random);
                }
            }
        }
    }

    private void addVerticalHallway(TETile[][] world, Position a, Position b) {
        int distance = Math.abs(a.y - b.y) + 1;
        if (a.y <= b.y) {
            for (int i = 0; i < distance; i += 1) {
                if (!isFloor(world, b.x, a.y + i)) {
                    world[b.x][a.y + i] =
                            TETile.colorVariant(Tileset.FLOOR, 64, 64, 64, random);
                }
                if (!isFloor(world, b.x - 1, a.y + i)) {
                    world[b.x - 1][a.y + i] =
                            TETile.colorVariant(Tileset.WALL, 64, 64, 64, random);
                }
                if (!isFloor(world, b.x + 1, a.y + i)) {
                    world[b.x + 1][a.y + i] =
                            TETile.colorVariant(Tileset.WALL, 64, 64, 64, random);
                }
            }
        } else {
            for (int i = 0; i < distance; i += 1) {
                if (!isFloor(world, b.x, b.y + i)) {
                    world[b.x][b.y + i] =
                            TETile.colorVariant(Tileset.FLOOR, 64, 64, 64, random);
                }
                if (!isFloor(world, b.x - 1, b.y + i)) {
                    world[b.x - 1][b.y + i] =
                            TETile.colorVariant(Tileset.WALL, 64, 64, 64, random);
                }
                if (!isFloor(world, b.x + 1, b.y + i)) {
                    world[b.x + 1][b.y + i] =
                            TETile.colorVariant(Tileset.WALL, 64, 64, 64, random);
                }
            }
        }
    }

    private void addHallway(TETile[][] world, Position a, Position b) {
        addHorizontalHallway(world, a, b);
        addVerticalHallway(world, a, b);
    }

    private  void addDoor(TETile[][] world) {
        TETile t = TETile.colorVariant(Tileset.LOCKED_DOOR, 64, 64, 64, random);
        int xi = uniform(random, 0, width);
        int yi = uniform(random, 0, height);
        while (xi < width && yi < height) {
            if (world[xi][yi].character() == Tileset.WALL.character()) {
                world[xi][yi] = t;
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

        int totalRoom = uniform(random, 15, Rnumber);
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
