package com.qt.hackathon;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MazeSolver {
    //0 = wall
    //1 = path
    //2 = destination

    static List<String> path = new ArrayList<String>();

    public static List<String> maze(Maze maze) {

        if (solveMaze(maze)) {
            System.out.println("Maze Solved!");
            return path;
        }
        System.out.println("No path");
        return null;
    }

    private static boolean solveMaze(Maze m) {

        Position p = m.start;
        m.path.push(p);


        while (true) {
            int y = m.path.peek().y;
            int x = m.path.peek().x;

            m.maze[y][x] = 0;

            //down
            if (isValid(y + 1, x, m)) {
                if (m.maze[y + 1][x] == 2) {
                    path.add("D");
                    System.out.println("Moved down");
                    return true;
                } else if (m.maze[y + 1][x] == 1) {
                    path.add("D");
                    System.out.println("Moved down");
                    m.path.push(new Position(y + 1, x));
                    continue;
                }
            }

            //left
            if (isValid(y, x - 1, m)) {
                if (m.maze[y][x - 1] == 2) {
                    path.add("L");
                    System.out.println("Moved left");
                    return true;
                } else if (m.maze[y][x - 1] == 1) {
                    path.add("L");
                    System.out.println("Moved left");
                    m.path.push(new Position(y, x - 1));
                    continue;
                }
            }

            //up
            if (isValid(y - 1, x, m)) {
                if (m.maze[y - 1][x] == 2) {
                    path.add("U");
                    System.out.println("Moved up");
                    return true;
                } else if (m.maze[y - 1][x] == 1) {
                    path.add("U");
                    System.out.println("Moved up");
                    m.path.push(new Position(y - 1, x));
                    continue;
                }
            }

            //right
            if (isValid(y, x + 1, m)) {
                if (m.maze[y][x + 1] == 2) {
                    path.add("R");
                    System.out.println("Moved right");
                    return true;
                } else if (m.maze[y][x + 1] == 1) {
                    path.add("R");
                    System.out.println("Moved right");
                    m.path.push(new Position(y, x + 1));
                    continue;
                }
            }

            m.path.pop();
            moveBack();
            System.out.println("Moved back");
            if (m.path.size() <= 0) {
                return false;
            }
        }
    }

    public static boolean isValid(int y, int x, Maze m) {
        if (y < 0 ||
                y >= m.maze.length ||
                x < 0 ||
                x >= m.maze[y].length
        ) {
            return false;
        }
        return true;
    }

    private static void moveBack() {
        int last_element_index = path.size()-1;
        String lastMove = path.get(last_element_index);
        if(lastMove.equalsIgnoreCase("L"))
            path.add("R");
        if(lastMove.equalsIgnoreCase("R"))
            path.add("L");
        if(lastMove.equalsIgnoreCase("U"))
            path.add("D");
        if(lastMove.equalsIgnoreCase("D"))
            path.add("U");
    }

    public static void main(String[] args) throws FileNotFoundException {

        ArrayList<Maze> mazes = new ArrayList<Maze>();

        Maze m = new Maze();

        Scanner in = new Scanner(new File("target/mazes.txt"));
        int rows = Integer.parseInt(in.nextLine());
        m.maze = new int[rows][];

        for(int i = 0; i < rows; i++) {
            String line = in.nextLine();
            m.maze[i] = Arrays.stream(line.split(" ")).mapToInt(Integer::parseInt).toArray();
        }

        m.start = new Position(Integer.parseInt(in.nextLine()), Integer.parseInt(in.nextLine()));

        mazes.add(m);

        int i = 0;
        while(i < mazes.size()) {
            if(solveMaze(mazes.get(i))) {
                System.out.println("You won!");
            } else {
                System.out.println("No path");
            }
            i++;
        }
    }
}
