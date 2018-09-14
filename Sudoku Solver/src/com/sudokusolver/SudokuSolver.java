package com.sudokusolver;

public class SudokuSolver {

    private static final int SUDOKU_SIZE = 9; // classical sudoku size = 9
    private static final int BLOCK_SIZE = 3;  // classical  block size = 3
    private static final int LENGTH = SUDOKU_SIZE * SUDOKU_SIZE; // 9*9 = 81
    private static final int POPULATION_SIZE = 20; // population size
    private static final int KILL_POPULATION = 5000; // kill after generation
    private static final double MUTATION_RATE = 0.05; // Mutation rate

    private boolean[][] initialUsedRow;
    private boolean[][] initialUsedColumn;
    private boolean[][] initialUsedBlock;
    private boolean[][] workingUsedRow;
    private boolean[][] workingUsedColumn;
    private boolean[][] workingUsedBlock;
    private int[] initialValues; // Values from GUI
    private Population population;

    private Menu mainMenu = new Menu(this); // JFRAME

    public SudokuSolver() {
        initialUsedRow = new boolean[SUDOKU_SIZE][SUDOKU_SIZE + 1];
        initialUsedColumn = new boolean[SUDOKU_SIZE][SUDOKU_SIZE + 1];
        initialUsedBlock = new boolean[SUDOKU_SIZE][SUDOKU_SIZE + 1];
        workingUsedRow = new boolean[SUDOKU_SIZE][SUDOKU_SIZE + 1];
        workingUsedColumn = new boolean[SUDOKU_SIZE][SUDOKU_SIZE + 1];
        workingUsedBlock = new boolean[SUDOKU_SIZE][SUDOKU_SIZE + 1];
        initialValues = new int[LENGTH]; // for GUI values
        population = new Population(LENGTH, POPULATION_SIZE);
    }

    public int getRow(int squareNum) {
        return squareNum / SUDOKU_SIZE; // ex : 27 in 3rd row
    }

    public int getColumn(int squareNum) {
        return squareNum % SUDOKU_SIZE; // ex : 27 in 0th column
    }

    public int getBlock(int rowNum, int columnNum) {
        return ((rowNum / BLOCK_SIZE) * BLOCK_SIZE) + columnNum / BLOCK_SIZE;
        // ex : row = 3 column = 0 means 3th block(there is 3 in first 3 row then this)
    }

    public int getSquareNum(int row, int col) {
        // ex : row = 3 col = 0 means 27th square/cell
        return SUDOKU_SIZE * row + col;
    }

    
    public void randomizeArray(int[] array, int size) {
        int index;
        int temp;
        for (int i = size - 1; i > 0; i--) {
            index = (int) (Math.random() * i);
            temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }


    public void initArray(int[] array, int size, boolean startsZero) {
        int increment;
        if (startsZero == true) {
            increment = 0;
        } else {
            increment = 1;
        }
        for (int i = 0; i < size; i++) {
            array[i] = i + increment;
        }
    }

    /**
     * sets value to given row column and block
     *
     */
    public void setValue(int row, int column, int block, int value) {
        workingUsedRow[row][value] = true;
        workingUsedColumn[column][value] = true;
        workingUsedBlock[block][value] = true;
    }

    /**
     * According to given value, if row, column and block are not filled then
     * return true
     *
     */
    public boolean checkValue(int row, int column, int block, int value) {
        if (workingUsedRow[row][value]) {
            return false;
        }
        if (workingUsedColumn[column][value]) {
            return false;
        }
        if (workingUsedBlock[block][value]) {
            return false;
        }
        return true;
    }

    /**
     * fill working row ,column,block
     *
     */
    public void fillWorkingValues(PopulationElement element) {
        int[] array = element.getEntries();
        for (int i = 0; i < array.length; i++) {
            array[i] = initialValues[i];
        }
        for (int i = 0; i < workingUsedRow.length; i++) {
            for (int j = 0; j < workingUsedRow[i].length; j++) {
                workingUsedRow[i][j] = initialUsedRow[i][j];
                workingUsedColumn[i][j] = initialUsedColumn[i][j];
                workingUsedBlock[i][j] = initialUsedBlock[i][j];
            }
        }
    }

    /**
     * reads values from GUI
     */
    public void read() {
        initialValues = mainMenu.getNumbers();
    }

    /**
     * Initialize row,column,block with initialValues which comes from GUI
     */
    public void initializeSudoku() {
        int row;
        int column;
        int block;
        for (int i = 0; i < initialValues.length; i++) {
            if (initialValues[i] != 0) {
                row = getRow(i); // gets row corresponds to that index
                column = getColumn(i); // gets column corresponds to that index
                block = getBlock(row, column); // gets block corresponds to that index
                initialUsedRow[row][initialValues[i]] = true; // this value exist in this row
                initialUsedColumn[column][initialValues[i]] = true; // this value exist in this column
                initialUsedBlock[block][initialValues[i]] = true; // this value exist in this block
            }
        }
    }

    /**
     * initialize a population which different members
     */
    public void initializePopulation() {
        PopulationElement element;
        for (int i = 0; i < population.getSize(); i++) {
            element = population.getMember(i);
            fillWorkingValues(element);
            attemptFill(element);
            population.insertElement(i);
        }
        element = population.getMember(0);
        element.printEntries();
        System.out.println("This is the HIGHEST score in our population...\n");
        element = population.getMember(population.getSize() - 1);
        element.printEntries();
        System.out.println("This is the LOWEST score in our population...\n");
    }

    /**
     * Tries to fill empty sqaures
     *
     */
    public void attemptFill(PopulationElement child) {
        int score = 0;
        int[] tmp1 = new int[81]; // to start with random sqaure
        int[] tmp2 = new int[9]; // to start with random number 
        initArray(tmp1, 81, true); //0,1,2,3,...,80
        initArray(tmp2, 9, false); //1,2,3,4..,9
        randomizeArray(tmp1, 81);

        int square; // square we re in
        int value;
        int row; // row index
        int column; // column index
        int block; // block index
        //init child
        fillWorkingValues(child);

        int[] childEntries = child.getEntries();

        for (int i = 0; i < LENGTH; i++) {

            square = tmp1[i];

            if (childEntries[square] != 0) {
                score++;
            } else {
                randomizeArray(tmp2, SUDOKU_SIZE);
                row = getRow(square);
                column = getColumn(square);
                block = getBlock(row, column);
                for (int j = 0; j < SUDOKU_SIZE; j++) {
                    value = tmp2[j];
                    if (checkValue(row, column, block, value)) {
                        setValue(row, column, block, value);
                        childEntries[square] = value;
                        score++;
                        break;
                    }

                }
            }
            child.setScore(score);
        }
    }

    /**
     * Genetic algorithm phase
     */
    public void geneticAlgorithm() {
        long start = System.currentTimeMillis();
        initializePopulation();
        PopulationElement parent1;
        PopulationElement parent2;
        PopulationElement child = population.getMember(0);
        int position;
        int childNum = 0;
        int kill = 0;
        while (population.getMember(0).getScore() < LENGTH) {
            if (childNum < KILL_POPULATION) {
                // MUTATION START
                for (int i = 0; i < population.getSize(); i++) {
                    if (Math.random() < MUTATION_RATE) {
                        fillWorkingValues(population.getMember(i));
                        attemptFill(population.getMember(i));
//                        position = population.insertElement(i);
                    }
                }
                population.sort();
                if (population.getMember(0).getScore() == LENGTH) {
                    break;
                }
                //MUTATION END
                
                // crossovers half of the population and kills other half
                for (int i = 0; i < population.getSize() / 2; i += 2) {
                    parent1 = population.getMember(i);
                    parent2 = population.getMember(i + 1);
                    child = population.getMember(population.getSize());
                    crossingOver(parent1, parent2, child);

                    position = population.insertElement(population.getSize());
                    // if our best 
                    if (position == 0) {
                        child.printEntries();
                        if (child.getScore() == LENGTH) {
                            break;
                        }
                    }

                }

                System.out.println("Generation " + childNum + " was born!");
                childNum++;
            } else {
                childNum = 0;
                initializePopulation();
                kill++;
                System.out.println("Population killed");
            }

        }
        long end = System.currentTimeMillis();
        population.getMember(0).printEntries();
        System.out.println("Time : " + (end - start) + "ms");
        System.out.println("Population killed " + kill + " times");
        mainMenu.setNumbers(child.getEntries());
    }

    /**
     * It randomly inputs parts of parent1 and parent2 into child till filled.
     *
     * @param parent1 this will be crossed with parent2 to overwrite child.
     * @param parent2 this will be crossed with parent1 to overwrite child.
     * @param child this population member will be overwritten and reborn as a
     * new product of parent1 and parent2
     */
    public void crossingOver(PopulationElement parent1, PopulationElement parent2, PopulationElement child) {
        int score = 0;
        //mixes up numbers 0-81
        int[] tmp1 = new int[81];
        //mixes up digits 1-9
        int[] tmp2 = new int[11];
        initArray(tmp1, 81, true);
        initArray(tmp2, 9, false);

        randomizeArray(tmp1, 81);

        int square;
        int value;
        int row;
        int column;
        int block;

        fillWorkingValues(child);

        int[] childEntry = child.getEntries();
        for (int i = 0; i < LENGTH; i++) {
            square = tmp1[i];
            if (childEntry[square] != 0) {
                score++;
            } else {
                double random = Math.random();
                if (parent1.getEntry(square) == parent2.getEntry(square)
                        && parent1.getEntry(square) != 0) {
                    tmp2[9] = parent1.getEntry(square);
                } else if (random <= 0.5) {
                    tmp2[9] = parent1.getEntry(square);
                } else if (random > 0.5) {
                    tmp2[9] = parent2.getEntry(square);
                }
                randomizeArray(tmp2, 9);
                row = getRow(square);
                column = getColumn(square);
                block = getBlock(row, column);
                for (int j = 9; j >= 0; j--) {
                    if (tmp2[j] != 0) {
                        value = tmp2[j];
                        if (checkValue(row, column, block, value)) {
                            setValue(row, column, block, value);
                            childEntry[square] = value;
                            score++;
                            break;
                        }
                    }
                }
            }
        }
        child.setScore(score);
    }

    public void clear() {

        for (int i = 0; i < SUDOKU_SIZE; i++) {
            for (int j = 0; j < SUDOKU_SIZE + 1; j++) {
                initialUsedRow[i][j] = false;
                initialUsedColumn[i][j] = false;
                initialUsedBlock[i][j] = false;
            }
        }
        for (int z = 0; z < LENGTH; z++) {
            initialValues[z] = 0;
        }
        mainMenu.setNumbers(initialValues);
    }
}
