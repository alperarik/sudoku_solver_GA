package com.sudokusolver;

public class PopulationElement {

    private int score; // score of population element
    private int[] entries; // numbers of sudoku cell

    /**
     * Constructor for new population member with the given size.
     *
     * @param size size of population
     */
    public PopulationElement(int size) {
        score = 0;
        entries = new int[size];
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int[] getEntries() {
        return entries;
    }

    public void setEntries(int[] entries) {
        this.entries = entries;
    }

    public int getEntry(int index) {
        return entries[index];
    }

    public void setEntry(int index, int value) {
        entries[index] = value;
    }

    public int getSize() {
        return entries.length;
    }

    /**
     * Copy one element to another
     *
     */
    public void copy(PopulationElement element) {
        score = element.score;
        for (int i = 0; i < element.entries.length; i++) {
            entries[i] = element.entries[i];
        }
    }

    public void printEntries() {
        for (int i = 0; i < entries.length; i++) {
            System.out.print(entries[i] + " ");
            if (i % 9 == 8) {
                System.out.println();
            } else if (i % 3 == 2) {
                System.out.print(" ");
            }
            if (i % 27 == 26) {
                System.out.println();
            }
        }
        System.out.println("Score: " + score);
    }
}
