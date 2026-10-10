import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.Border;

public class ScientificCalculator extends JFrame implements ActionListener {

    // Modern color scheme from other tools
    private static final Color BG_DARK = new Color(30, 20, 50);
    private static final Color CARD_BG = new Color(40, 30, 70);
    private static final Color PRIMARY = new Color(138, 43, 226);
    private static final Color SUCCESS = new Color(255, 20, 147);
    private static final Color TEXT_PRIMARY = new Color(255, 255, 255);
    private static final Color BTN_NUM = new Color(50, 40, 80);
    private static final Color BTN_OP = new Color(70, 50, 110);
    private static final Color BTN_FUNC = new Color(40, 30, 70);

    private JTextField display;
    private double num1 = 0, num2 = 0, result = 0, memory = 0;
    private char operator = ' ';
    private boolean startOfNewNumber = true;

    public ScientificCalculator() {
        setTitle("Scientific Calculator");
        setSize(500, 650);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(BG_DARK);

        // --- Display Field ---
        display = new JTextField("0");
        display.setEditable(false);
        display.setFont(new Font("Consolas", Font.BOLD, 48));
        display.setHorizontalAlignment(SwingConstants.RIGHT);
        display.setBackground(BG_DARK);
        display.setForeground(TEXT_PRIMARY);
        display.setCaretColor(SUCCESS);
        Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        display.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY, 1),
            padding
        ));
        add(display, BorderLayout.NORTH);

        // --- Buttons Panel ---
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 5, 5, 5));
        panel.setBackground(BG_DARK);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // ========== "asin", "acos", "atan" වෙනස් කරන ලදී ==========
        String[] buttonLabels = {
            "sin", "cos", "tan", "log", "ln",
            "sin⁻¹", "cos⁻¹", "tan⁻¹", "√", "x²",
            "x³", "1/x", "e^x", "10^x", "%",
            "MC", "MR", "M+", "M-", "Del",
            "7", "8", "9", "/", "C",
            "4", "5", "6", "*", "±",
            "1", "2", "3", "-", "+",
            "0", ".", "(", ")", "="
        };
        // =======================================================
        
        for (String text : buttonLabels) {
            JButton btn = createStyledButton(text);
            panel.add(btn);
        }
        
        add(panel, BorderLayout.CENTER);
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setForeground(TEXT_PRIMARY);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(BG_DARK));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(this);

        String numbers = "0123456789.";
        String operators = "+-*/";
        String controls = "C±Del";
        
        final Color originalColor;

        if (numbers.contains(text)) {
            originalColor = BTN_NUM;
        } else if (operators.contains(text)) {
            originalColor = BTN_OP;
        } else if (text.equals("=")) {
            originalColor = SUCCESS;
        } else if(controls.contains(text)){
            originalColor = BTN_OP;
        } else {
            originalColor = BTN_FUNC;
        }

        btn.setBackground(originalColor);
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btn.setBackground(originalColor.brighter());
            }
            public void mouseExited(MouseEvent evt) {
                btn.setBackground(originalColor);
            }
        });
        
        return btn;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        try {
            if ("0123456789.".contains(cmd)) {
                if (startOfNewNumber || display.getText().equals("0")) {
                    display.setText(cmd);
                } else {
                    display.setText(display.getText() + cmd);
                }
                startOfNewNumber = false;
                return;
            }

            switch (cmd) {
                case "C":
                    display.setText("0");
                    num1 = num2 = result = 0;
                    operator = ' ';
                    startOfNewNumber = true;
                    break;
                case "Del": 
                    String txt = display.getText();
                    if (!txt.isEmpty() && !txt.equals("0")) {
                        display.setText(txt.substring(0, txt.length() - 1));
                        if(display.getText().isEmpty()) display.setText("0");
                    }
                    break;
                case "MC": memory = 0; break;
                case "MR": display.setText(formatNumber(memory)); startOfNewNumber = true; break;
                case "M+": memory += Double.parseDouble(display.getText()); break;
                case "M-": memory -= Double.parseDouble(display.getText()); break;
                case "±":
                    double val = Double.parseDouble(display.getText());
                    display.setText(formatNumber(-val));
                    break;
                case "=":
                    if (operator != ' ') {
                        num2 = Double.parseDouble(display.getText());
                        computeResult();
                    }
                    break;
                case "+": case "-": case "*": case "/":
                    num1 = Double.parseDouble(display.getText());
                    operator = cmd.charAt(0);
                    startOfNewNumber = true;
                    break;
                case "√": result = Math.sqrt(Double.parseDouble(display.getText())); displayResult(); break;
                case "x²": result = Math.pow(Double.parseDouble(display.getText()), 2); displayResult(); break;
                case "x³": result = Math.pow(Double.parseDouble(display.getText()), 3); displayResult(); break;
                case "1/x":
                    double v = Double.parseDouble(display.getText());
                    if (v == 0) throw new ArithmeticException("Division by zero");
                    result = 1 / v;
                    displayResult();
                    break;
                case "%": result = Double.parseDouble(display.getText()) / 100; displayResult(); break;
                case "sin": result = Math.sin(Math.toRadians(Double.parseDouble(display.getText()))); displayResult(); break;
                case "cos": result = Math.cos(Math.toRadians(Double.parseDouble(display.getText()))); displayResult(); break;
                case "tan": result = Math.tan(Math.toRadians(Double.parseDouble(display.getText()))); displayResult(); break;
                
                // ========== අදාළ case statements වෙනස් කරන ලදී ==========
                case "sin⁻¹": result = Math.toDegrees(Math.asin(Double.parseDouble(display.getText()))); displayResult(); break;
                case "cos⁻¹": result = Math.toDegrees(Math.acos(Double.parseDouble(display.getText()))); displayResult(); break;
                case "tan⁻¹": result = Math.toDegrees(Math.atan(Double.parseDouble(display.getText()))); displayResult(); break;
                // ========================================================
                
                case "log": result = Math.log10(Double.parseDouble(display.getText())); displayResult(); break;
                case "ln": result = Math.log(Double.parseDouble(display.getText())); displayResult(); break;
                case "e^x": result = Math.exp(Double.parseDouble(display.getText())); displayResult(); break;
                case "10^x": result = Math.pow(10, Double.parseDouble(display.getText())); displayResult(); break;
            }
        } catch (Exception ex) {
            display.setText("Error");
            startOfNewNumber = true;
        }
    }

    private void computeResult() {
        switch (operator) {
            case '+': result = num1 + num2; break;
            case '-': result = num1 - num2; break;
            case '*': result = num1 * num2; break;
            case '/':
                if (num2 == 0) throw new ArithmeticException("Division by zero");
                result = num1 / num2;
                break;
        }
        displayResult();
        operator = ' ';
    }
    
    private void displayResult() {
        display.setText(formatNumber(result));
        startOfNewNumber = true;
    }
    
    private String formatNumber(double num) {
        if (num == (long) num) {
            return String.format("%d", (long) num);
        } else {
            return String.format("%s", num);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ScientificCalculator().setVisible(true);
        });
    }
}