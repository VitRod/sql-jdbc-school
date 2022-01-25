package com.fox.cli.menuitem;

public abstract class MenuItem {
	
    private final String name;
    
    protected MenuItem(String name) {
        this.name = name;
    }
    
    public abstract void execute();

    public String getName() {
        return this.name;
    }
}
