package com.fox.cli;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import com.fox.cli.menuitem.MenuItem;
import com.fox.cli.menuitem.QuitMenuItem;

public class Menu {
	
    private static final String START_MESSAGE_MENU = "Input number item from this menu";
    private static final String MESSAGE_INPUT_NUMBER = "Input number: ";
    private static final String CLOSE_BRACKET = "]";
    private static final String OPEN_BRACKET = "[";
    private static final String MESSAGE_EXCEPTION_WRONG_NUMBER = "You inputted the wrong number";
    private static final String MESSAGE_EXCEPTION_NOT_NUMBER = "You inputted not a number. Please input number";
    private static final String NAME_QUIT_ITEM = "Quit";
    private static final String MESSAGE_QUIT_APPLICATION = "Quitting application...";

    private Map<Integer, MenuItem> menuItems;
    private Scanner scanner;

    public Menu(Scanner scanner) {
        this.menuItems = new LinkedHashMap<>();
        this.scanner = scanner;
    }

    public void addMenuItem(Integer key, MenuItem menuItem) {
        menuItems.put(key, menuItem);
    }

    private void printMenuItems() {
        menuItems.forEach((k, v) -> System.out
                .println(OPEN_BRACKET + k + CLOSE_BRACKET + v.getName()));
    }

    private int scanInt() {
        int result = -1;
        do {
            System.out.print(MESSAGE_INPUT_NUMBER);
            try {
                result = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println(MESSAGE_EXCEPTION_NOT_NUMBER);
            } 
        } while (result == -1);
        return result;
    }

    private void addDefaultItems() {
        MenuItem menuItemQuit = new QuitMenuItem(NAME_QUIT_ITEM);
        addMenuItem(0, menuItemQuit);
    }

    public void initMenu() {
        addDefaultItems();

        boolean quit = false;

        while (!quit) {
            System.out.println(START_MESSAGE_MENU);
            printMenuItems();
            Set<Integer> keys = menuItems.keySet();
            int choice = scanInt();
            while(!keys.contains(choice)) {
                System.out.println(MESSAGE_EXCEPTION_WRONG_NUMBER);
                choice = scanInt();
            }
            if (choice == 0) {
                System.out.println(MESSAGE_QUIT_APPLICATION);
                quit = true;
            } else {
                menuItems.get(choice).execute();
            }

            System.out.println();
        }
    }
}
