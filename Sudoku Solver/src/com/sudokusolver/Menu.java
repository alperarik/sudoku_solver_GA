package com.sudokusolver;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Menu implements ActionListener {

    public static final int TEXTBOXSIZE = 3;
    public static final int BOARD_SIZE = 81;

    JTextField[] jTFInputArray = new JTextField[BOARD_SIZE];

	   
	// EASY   
	   int[] sudoku =
      {0, 0, 0,  2, 6, 0,  7, 0, 1,
       6, 8, 0,  0, 7, 0,  0, 9, 0,
       1, 9, 0,  0, 0, 4,  5, 0, 0,
	   
       8, 2, 0,  1, 0, 0,  0, 4, 0,
       0, 0, 4,  6, 0, 2,  9, 0, 0,
       0, 5, 0,  0, 0, 3,  0, 2, 8,
	   
       0, 0, 9,  3, 0, 0,  0, 7, 4,
       0, 4, 0,  0, 5, 0,  0, 3, 6,
       7, 0, 3,  0, 1, 8,  0, 0, 0};	

    SudokuSolver solver;

    /**
     * Constructor
     *
     * @param solver Sudoku Solver object to start process
     */
    public Menu(SudokuSolver solver) {
        JFrame sudokuFrame = new JFrame();
        sudokuFrame.setSize(500, 500); // 500 x 500 size
        sudokuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        sudokuFrame.setTitle("Sudoku Solver");
        sudokuFrame.setLocationRelativeTo(null); // set to center
        sudokuFrame.add(initializeButtonPanel(), BorderLayout.LINE_END); // add buttons
        sudokuFrame.add(initializeSudokuPanel(), BorderLayout.CENTER); // add sudoku panel
        sudokuFrame.pack();
        sudokuFrame.setVisible(true);

        this.solver = solver;
    }

    /**
     * initializes buttons for Panel
     *
     * @return button panel
     */
    private JPanel initializeButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JButton solveButton = new JButton("Solve! ");
        solveButton.setBackground(Color.GREEN);

        JButton clearButton = new JButton("Clear  ");
        clearButton.setBackground(Color.WHITE);

        solveButton.setName("solveButton");
        clearButton.setName("clearButton");

        solveButton.addActionListener(this);
        clearButton.addActionListener(this);

        buttonPanel.add(solveButton);
        buttonPanel.add(clearButton);

        return buttonPanel;
    }

    /**
     * initializes sudoku numbers slot panel
     *
     * @return sudoku panel
     */
    private JPanel initializeSudokuPanel() {
        JPanel sudokuBoardPanel = new JPanel();
        sudokuBoardPanel.setLayout(new GridLayout(9, 9)); // creates grid layout

        for (int i = 0; i < jTFInputArray.length; i++) {
            jTFInputArray[i] = new JTextField(TEXTBOXSIZE);
            if (sudoku[i] != 0) {
                jTFInputArray[i].setText(String.valueOf(sudoku[i]));
            } else {
                jTFInputArray[i].setText(String.valueOf(""));
            }
            sudokuBoardPanel.add(jTFInputArray[i]);
        }

        return sudokuBoardPanel;
    }

    /**
     * Works when an action listener event fired.
     *
     * @param button that clicked
     */
    public void updateClicked(JButton button) {
        try {
            // if solve button clicked
            if (button.getName().equals("solveButton")) {
                solver.read(); // solver get numbers from sudoku panel
                solver.initializeSudoku(); // initializes start point
                solver.geneticAlgorithm();
            } // solve button clicked
            else if (button.getName().equals("clearButton")) {
                solver.clear();
            }
        } catch (Exception e) {

        }
    }

    /**
     * Set numbers. If number equals to 0 that means cell is empty
     *
     */
    public void setNumbers(int[] numbers) {
        for (int i = 0; i < jTFInputArray.length; i++) {
            if (numbers[i] == 0) {
                jTFInputArray[i].setText("");
            } else {
                jTFInputArray[i].setText(numbers[i] + "");
            }

        }
    }

    /**
     * Get numbers. If a cell is empty then value of it is 0
     *
     */
    public int[] getNumbers() {
        int[] numbers = new int[BOARD_SIZE];

        for (int i = 0; i < jTFInputArray.length; i++) {
            numbers[i] = 0;
            if (jTFInputArray[i].getText().length() == 0) {
                numbers[i] = 0;
            } else {
                numbers[i] = Integer.parseInt(jTFInputArray[i].getText());
            }
        }

        return numbers;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton Button = (JButton) e.getSource();

        updateClicked(Button);
    }

}
