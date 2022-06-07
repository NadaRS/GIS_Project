package org.example;//View Package

public class App {
    View view;

    public static void main(String[] args) {
        new App().mainFunction();
    }

    public void mainFunction() {
        view = new View();
        view.setVisible(true);
    }
}