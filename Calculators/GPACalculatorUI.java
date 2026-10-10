import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;

public class GPACalculatorUI extends JFrame {

    // Modern color scheme (from other tools)
    private static final Color BG_DARK = new Color(30, 20, 50);
    private static final Color CARD_BG = new Color(40, 30, 70);
    private static final Color PRIMARY = new Color(138, 43, 226);
    private static final Color SUCCESS = new Color(255, 20, 147);
    private static final Color TEXT_PRIMARY = new Color(255, 255, 255);
    private static final Color TEXT_SECONDARY = new Color(200, 200, 220);
    private static final Color GOLD_ACCENT = new Color(255, 215, 0); 
    private static final Color ORANGE_ACCENT = new Color(255, 165, 0);

    private JTextField[] subjectFields;
    private JTextField[] gradeFields;
    private JTextField[] creditFields;
    private JButton calculateButton;
    private JLabel resultLabel;
    private JTextArea detailsArea;

    public GPACalculatorUI() {
        setTitle("GPA Calculator");
        setSize(750, 800);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); 
        setLayout(new BorderLayout()); 
        getContentPane().setBackground(BG_DARK);
        setLocationRelativeTo(null);
        
        SwingUtilities.invokeLater(() -> {
            String input = JOptionPane.showInputDialog(this, "Enter number of subjects:", "Setup", JOptionPane.QUESTION_MESSAGE);
            if (input == null || input.trim().isEmpty()) {
                dispose();
                return;
            }
            try {
                int numSubjects = Integer.parseInt(input);
                if (numSubjects <= 0) throw new NumberFormatException();
                buildUI(numSubjects); 
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid positive number.", "Error", JOptionPane.ERROR_MESSAGE);
                dispose();
            }
        });
    }
    
    private void buildUI(int numSubjects) {

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(50, 35, 80));
        titlePanel.setPreferredSize(new Dimension(0, 80)); 

        JLabel title = new JLabel("GPA Calculator", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 34)); 
        title.setForeground(GOLD_ACCENT);
        titlePanel.add(title, BorderLayout.CENTER);
        // =======================================================
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 15));
        mainPanel.setBackground(BG_DARK);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        // --- Input Panel ---
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(CARD_BG);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(50, 50, 80)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel header1 = new JLabel("Subject Name");
        JLabel header2 = new JLabel("Grade (A+, A, B-, etc.)");
        JLabel header3 = new JLabel("Credits");

        header1.setForeground(TEXT_SECONDARY);
        header2.setForeground(TEXT_SECONDARY);
        header3.setForeground(TEXT_SECONDARY);
        header1.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header3.setFont(new Font("Segoe UI", Font.BOLD, 14));

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.5;
        inputPanel.add(header1, gbc);
        gbc.gridx = 1; gbc.weightx = 0.25;
        inputPanel.add(header2, gbc);
        gbc.gridx = 2; gbc.weightx = 0.25;
        inputPanel.add(header3, gbc);

        subjectFields = new JTextField[numSubjects];
        gradeFields = new JTextField[numSubjects];
        creditFields = new JTextField[numSubjects];

        for (int i = 0; i < numSubjects; i++) {
            subjectFields[i] = createTextField();
            gradeFields[i] = createTextField();
            creditFields[i] = createTextField();

            gbc.gridx = 0; gbc.gridy = i + 1;
            inputPanel.add(subjectFields[i], gbc);
            gbc.gridx = 1;
            inputPanel.add(gradeFields[i], gbc);
            gbc.gridx = 2;
            inputPanel.add(creditFields[i], gbc);
        }

        // --- Bottom Panel ---
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 15));
        bottomPanel.setOpaque(false);

        calculateButton = new JButton("✨ Calculate GPA ✨");
        calculateButton.setBackground(SUCCESS);
        calculateButton.setForeground(TEXT_PRIMARY);
        calculateButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        calculateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        calculateButton.setFocusPainted(false);
        calculateButton.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));
        
        calculateButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                calculateButton.setBackground(new Color(255, 60, 180));
            }
            public void mouseExited(MouseEvent evt) {
                calculateButton.setBackground(SUCCESS);
            }
        });

        JPanel resultDisplayPanel = new JPanel(new BorderLayout());
        resultDisplayPanel.setBackground(CARD_BG);
        resultDisplayPanel.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 80)));

        detailsArea = new JTextArea();
        detailsArea.setFont(new Font("Consolas", Font.BOLD, 18));
        detailsArea.setForeground(GOLD_ACCENT);
        detailsArea.setBackground(CARD_BG);
        detailsArea.setEditable(false);
        detailsArea.setMargin(new Insets(10, 15, 10, 15));
        
        resultLabel = new JLabel("Final GPA: -", SwingConstants.CENTER);
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        resultLabel.setForeground(ORANGE_ACCENT);
        resultLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        resultDisplayPanel.add(detailsArea, BorderLayout.CENTER);
        resultDisplayPanel.add(resultLabel, BorderLayout.SOUTH);
        
        calculateButton.addActionListener(e -> calculateGPA(numSubjects));
        
        bottomPanel.add(calculateButton, BorderLayout.NORTH);
        bottomPanel.add(resultDisplayPanel, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(inputPanel);
        scrollPane.getViewport().setBackground(CARD_BG);
        scrollPane.setBorder(null);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(titlePanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        
        revalidate();
        repaint();
    }
    
    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setBackground(new Color(25, 15, 45));
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(SUCCESS);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        
        Border line = BorderFactory.createLineBorder(PRIMARY, 1, true);
        Border padding = BorderFactory.createEmptyBorder(5, 10, 5, 10);
        field.setBorder(BorderFactory.createCompoundBorder(line, padding));
        
        field.setPreferredSize(new Dimension(200, 35));
        return field;
    }

    private double convertGradeToPoint(String grade) {
        switch (grade.toUpperCase()) {
            case "A+": return 4.0;
            case "A":  return 4.0;
            case "A-": return 3.7;
            case "B+": return 3.3;
            case "B":  return 3.0;
            case "B-": return 2.7;
            case "C+": return 2.3;
            case "C":  return 2.0;
            case "C-": return 1.7;
            case "D+": return 1.3;
            case "D":  return 1.0;
            case "E":  return 0.0;
            default:   return -1;
        }
    }

    private void calculateGPA(int numSubjects) {
        double totalPoints = 0;
        double totalCredits = 0;

        try {
            StringBuilder breakdown = new StringBuilder();
            
            for (int i = 0; i < numSubjects; i++) {
                String subject = subjectFields[i].getText().trim();
                if (subject.isEmpty()) subject = "Subject " + (i + 1);

                String gradeInput = gradeFields[i].getText().trim();
                double gradePoint = convertGradeToPoint(gradeInput);

                if (gradePoint == -1) {
                    JOptionPane.showMessageDialog(this, "Invalid grade: '" + gradeInput + "' for " + subject, "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double credits = Double.parseDouble(creditFields[i].getText());
                totalPoints += gradePoint * credits;
                totalCredits += credits;
                
                String line = String.format("%s: %s (%.1f) × %.1f credits\n", subject, gradeInput.toUpperCase(), gradePoint, credits);
                breakdown.append(line);
            }
            
            if (totalCredits == 0) {
                 resultLabel.setText("Total credits cannot be zero.");
                 detailsArea.setText("");
                 return;
            }

            double gpa = totalPoints / totalCredits;

            detailsArea.setText(breakdown.toString()); 
            resultLabel.setText("Final GPA: " + String.format("%.2f", gpa));

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for credits.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GPACalculatorUI().setVisible(true);
        });
    }
}