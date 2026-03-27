package student;


import student.controller.ArgsController;

/**
 * Main driver for the program.
 * 
 * DO NOT modify the name of this class (we call main directly in our test code).
 * 
 */
public final class DNInfoApp {

    /** Private constructor to prevent instantiation. */
    private DNInfoApp() {
        // empty
    }

    /**
     * Main entry point for the program.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
           new ArgsController().run(args);
    }
}
