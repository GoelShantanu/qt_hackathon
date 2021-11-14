package com.qt.hackathon;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class SolveMazeUi {


    private static final Maze m = new Maze();
    private static int rows;
    private final WebDriver driver;

    SolveMazeUi(WebDriver driver) {
        this.driver = driver;
    }

    public void extractMaze() {

        List<WebElement> maze_rows = driver.findElements(By.cssSelector("table#maze tr"));
        rows = maze_rows.size();
        System.out.println(rows);
        m.maze = new int[rows][];
        for (int i = 0; i < rows; i++) {
            String locator_cells_row = "//table//tr[" + (i + 1) + "]//td";
            List<WebElement> maze_cols = driver.findElements(By.xpath(locator_cells_row));
            List<String> array_col = new ArrayList<>();
            for (WebElement e : maze_cols) {
                array_col.add(isPath(e.getAttribute("class").toLowerCase()));
            }
            m.maze[i] = array_col.stream().mapToInt(Integer::parseInt).toArray();
        }

        WebElement start_element = driver.findElement(By.cssSelector("table#maze tr td.deep-purple"));
        String start_x = start_element.getAttribute("class").toLowerCase()
                .split(" ")[0].replace("x", "");
        String start_y = start_element.getAttribute("class").toLowerCase().
                split(" ")[1].replace("y", "");

        m.start = new Position(Integer.parseInt(start_y), Integer.parseInt(start_x));
        printMaze();
        System.out.println(m.start.x);
        System.out.println(m.start.y);
    }

    private static void printMaze() {
        for (int i = 0; i < rows; i++) {
            for(int j : m.maze[i]) {
                System.out.print(j+" ");
            }
            System.out.println();
        }
    }

    public void solveMazeUi() {
        WebElement left_click = driver.findElement(By.cssSelector("a.btn[onclick ='left();']"));
        WebElement right_click = driver.findElement(By.cssSelector("a.btn[onclick ='right();']"));
        WebElement down_click = driver.findElement(By.cssSelector("a.btn[onclick ='down();']"));
        WebElement up_click = driver.findElement(By.cssSelector("a.btn[onclick ='up();']"));
        WebElement maze_proceed = driver.findElement(By.id("crystalMazeFormSubmit"));

        List<String>  path = MazeSolver.maze(m);
        for (String move : path) {
            if (move.equalsIgnoreCase("L"))
                left_click.click();
            if (move.equalsIgnoreCase("R"))
                right_click.click();
            if (move.equalsIgnoreCase("U"))
                up_click.click();
            if (move.equalsIgnoreCase("D"))
                down_click.click();
        }
        maze_proceed.click();
    }

    private static String isPath(String className) {
        if (className.contains("blue-grey") || className.contains("deep-purple"))
            return "1";
        if (className.contains("green"))
            return "2";
        return "0";
    }


}
