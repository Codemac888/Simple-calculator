package com.cal.app;

import java.awt.*;
import java.awt.event.*;
import java.util.Stack;

public class App {
    Label display = new Label("");  // Display label for input/output
    String input = "";  // Store user input

    public App() {
        Frame frame = new Frame("AWT Calculator");

        // Set GridLayout with 4 rows and 4 columns for buttons
        Panel buttonPanel = new Panel();
        buttonPanel.setLayout(new GridLayout(4, 4, 10, 10));  // Grid layout with gaps between buttons

        // Number buttons and operator buttons
        Button[] buttons = new Button[16];
        String[] labels = {"7", "8", "9", "/",
                           "4", "5", "6", "*",
                           "1", "2", "3", "-",
                           "C", "0", "=", "+"};

        // Create buttons and add to the panel
        for (int i = 0; i < labels.length; i++) {
            buttons[i] = new Button(labels[i]);
            buttons[i].setFont(new Font("Arial", Font.PLAIN, 20));  // Set font size for buttons
            buttonPanel.add(buttons[i]);
        }

        // Set the layout for the frame
        frame.setLayout(new BorderLayout(10, 10));  // BorderLayout with gaps between edges

        // Display label
        display.setBackground(Color.LIGHT_GRAY);
        display.setAlignment(Label.RIGHT);  // Right align the text in the display
        display.setFont(new Font("Arial", Font.BOLD, 24));
        display.setPreferredSize(new Dimension(200, 50));

        // Add components to the frame
        frame.add(display, BorderLayout.NORTH);  // Display at the top
        frame.add(buttonPanel, BorderLayout.CENTER);  // Button panel in the center

        // Set frame size and make it visible
        frame.setSize(400, 500);
        frame.setVisible(true);

        // Add action listeners to all buttons
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = e.getActionCommand();

                if (command.equals("C")) {
                    input = "";  // Clear input
                    display.setText(input);
                } else if (command.equals("=")) {
                    try {
                        int result = evaluateExpression(input);  // Evaluate the input using the Calculator method
                        display.setText(String.valueOf(result));  // Display the result
                        input = String.valueOf(result);  // Set input to result for further operations
                    } catch (Exception ex) {
                        display.setText("Error");  // Handle any evaluation errors
                        input = "";  // Reset input on error
                    }
                } else {
                    input += command;  // Append button text to input
                    display.setText(input);  // Update display
                }
            }
        };

        // Attach the listener to all buttons
        for (Button button : buttons) {
            button.addActionListener(listener);
        }

        // Handle window closing event
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                frame.dispose();
                System.exit(0);  // Ensure application exits when window is closed
            }
        });
    }

    // Method to evaluate mathematical expressions
    public static int evaluateExpression(String expression) {
        Stack<Integer> numbers = new Stack<>();
        Stack<Character> operations = new Stack<>();
        
        int number = 0;
        char operation = '+';  // Start with an assumption that the first number is positive

        for (int i = 0; i < expression.length(); i++) {
            char currentChar = expression.charAt(i);

            // Form the number if it's a digit
            if (Character.isDigit(currentChar)) {
                number = number * 10 + (currentChar - '0');
            }

            // If current character is an operator or a parenthesis or at the end of the string
            if (!Character.isDigit(currentChar) || i == expression.length() - 1) {
                if (currentChar == '(') {
                    // Find the closing bracket and recursively calculate the result inside the parentheses
                    int j = findClosingBracket(expression, i);
                    number = evaluateExpression(expression.substring(i + 1, j));
                    i = j;  // Skip to the character after the closing bracket
                }

                // Push numbers and operations when we reach an operator or parentheses end
                if (!Character.isDigit(currentChar) || i == expression.length() - 1) {
                    if (operation == '+') {
                        numbers.push(number);
                    } else if (operation == '-') {
                        numbers.push(-number);
                    } else if (operation == '*') {
                        numbers.push(numbers.pop() * number);
                    } else if (operation == '/') {
                        if (number == 0) {
                            throw new ArithmeticException("Division by zero is not allowed.");
                        }
                        numbers.push(numbers.pop() / number);
                    }
                    // Reset the number and store the new operation
                    number = 0;
                    operation = currentChar;
                }
            }
        }

        // Sum up all the results from the stack
        int result = 0;
        while (!numbers.isEmpty()) {
            result += numbers.pop();
        }

        return result;
    }

    // Helper method to find the matching closing bracket for a given open bracket
    public static int findClosingBracket(String expression, int startIndex) {
        int count = 0;
        for (int i = startIndex; i < expression.length(); i++) {
            if (expression.charAt(i) == '(') {
                count++;
            } else if (expression.charAt(i) == ')') {
                count--;
                if (count == 0) {
                    return i;
                }
            }
        }
        throw new IllegalArgumentException("Mismatched parentheses in the expression");
    }

    public static void main(String[] args) {
        new App();  // Create the calculator app
    }
}
