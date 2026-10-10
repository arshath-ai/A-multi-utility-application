import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class NumberSystemConverter extends JFrame implements ActionListener {

    private JComboBox<String> comboFrom, comboTo;
    private JTextField inputField;
    private JTextArea resultArea;

    // Modern color scheme
    private static final Color BG_DARK = new Color(30, 20, 50);
    private static final Color CARD_BG = new Color(40, 30, 70);
    private static final Color PRIMARY = new Color(138, 43, 226);
    private static final Color PRIMARY_HOVER = new Color(158, 63, 246);
    private static final Color SUCCESS = new Color(255, 20, 147);
    private static final Color TEXT_PRIMARY = new Color(255, 255, 255);
    private static final Color TEXT_SECONDARY = new Color(200, 200, 220);

    public NumberSystemConverter() {
        setTitle("Number System Converter");
        setSize(700, 650);
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        // =======================================

        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(BG_DARK);
        mainPanel.setLayout(null);

        // Title with icon-style design
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(50, 35, 80));
        titlePanel.setBounds(0, 0, 700, 100);
        titlePanel.setLayout(null);
        
        JLabel title = new JLabel("Number System Converter", SwingConstants.CENTER);
        title.setBounds(0, 25, 700, 50);
        title.setFont(new Font("Segoe UI", Font.BOLD, 34)); 
        title.setForeground(new Color(255, 215, 0));
        titlePanel.add(title);
        mainPanel.add(titlePanel);

        // Input Section
        JPanel inputSection = createCard();
        inputSection.setBounds(50, 130, 600, 80);
        inputSection.setLayout(null);
        
        JLabel inputLabel = new JLabel("INPUT");
        inputLabel.setBounds(20, 12, 100, 20);
        inputLabel.setForeground(new Color(255, 215, 0));
        inputLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        inputField = new JTextField();
        inputField.setBounds(20, 35, 560, 35);
        inputField.setBackground(new Color(25, 15, 45)); 
        inputField.setForeground(TEXT_PRIMARY);
        inputField.setCaretColor(SUCCESS);
        inputField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SUCCESS, 2, true),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        inputField.setFont(new Font("Segoe UI", Font.BOLD, 20));
        
        inputSection.add(inputLabel);
        inputSection.add(inputField);
        mainPanel.add(inputSection);

        // Conversion Selection - Side by Side
        JPanel conversionPanel = createCard();
        conversionPanel.setBounds(50, 230, 600, 100);
        conversionPanel.setLayout(null);
        
        // From Section
        JLabel fromLabel = new JLabel("FROM");
        fromLabel.setBounds(20, 20, 100, 20);
        fromLabel.setForeground(new Color(100, 200, 255));
        fromLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        String[] systems = {"Decimal", "Binary", "Octal", "Hexadecimal"};
        comboFrom = new JComboBox<>(systems);
        comboFrom.setBounds(20, 45, 260, 40);
        styleModernComboBox(comboFrom);
        
        // To Section
        JLabel toLabel = new JLabel("TO");
        toLabel.setBounds(320, 20, 100, 20);
        toLabel.setForeground(new Color(255, 150, 200));
        toLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        comboTo = new JComboBox<>(systems);
        comboTo.setBounds(320, 45, 260, 40);
        styleModernComboBox(comboTo);
        
        conversionPanel.add(fromLabel);
        conversionPanel.add(comboFrom);
        conversionPanel.add(toLabel);
        conversionPanel.add(comboTo);
        mainPanel.add(conversionPanel);

        // Convert button
        JButton convertBtn = new JButton("CONVERT NOW");
        convertBtn.setBounds(150, 360, 400, 60);
        convertBtn.setBackground(new Color(255, 20, 147)); 
        convertBtn.setForeground(TEXT_PRIMARY);
        convertBtn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        convertBtn.setFocusPainted(false);
        convertBtn.setBorder(BorderFactory.createEmptyBorder());
        convertBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        convertBtn.addActionListener(this);
        
        // Hover effect
        convertBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                convertBtn.setBackground(new Color(255, 60, 180));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                convertBtn.setBackground(new Color(255, 20, 147));
            }
        });
        
        mainPanel.add(convertBtn);

        // Result Card
        JPanel resultCard = createCard();
        resultCard.setBounds(50, 450, 600, 140);
        resultCard.setLayout(null);
        
        JLabel resultLabelTop = new JLabel("RESULT");
        resultLabelTop.setBounds(20, 17, 100, 25);
        resultLabelTop.setFont(new Font("Segoe UI", Font.BOLD, 14));
        resultLabelTop.setForeground(new Color(100, 255, 150));
        
        resultArea = new JTextArea();
        resultArea.setBounds(20, 50, 560, 75);
        resultArea.setEditable(false);
        resultArea.setBackground(new Color(20, 15, 40));
        resultArea.setForeground(new Color(0, 255, 150));
        resultArea.setFont(new Font("Consolas", Font.BOLD, 24));
        resultArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 255, 150), 2, true),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        resultArea.setText("---");
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        
        resultCard.add(resultLabelTop);
        resultCard.add(resultArea);
        mainPanel.add(resultCard);

        add(mainPanel);
        setVisible(true);
    }

    private JPanel createCard() {
        JPanel panel = new JPanel();
        panel.setBackground(CARD_BG);
        panel.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 80), 1, true));
        return panel;
    }

    private void styleModernComboBox(JComboBox<String> box) {
        box.setBackground(new Color(25, 15, 45));
        box.setForeground(Color.BLACK);
        box.setFont(new Font("Segoe UI", Font.BOLD, 14));
        box.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY, 2, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        box.setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = new JButton() {
                    @Override
                    public void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(new Color(0, 255, 200));
                        int w = getWidth();
                        int h = getHeight();
                        int[] xPoints = {w/2 - 4, w/2 + 4, w/2};
                        int[] yPoints = {h/2 - 2, h/2 - 2, h/2 + 3};
                        g2.fillPolygon(xPoints, yPoints, 3);
                        g2.dispose();
                    }
                };
                button.setBorder(BorderFactory.createEmptyBorder());
                button.setBackground(new Color(25, 15, 45));
                button.setContentAreaFilled(false);
                button.setFocusPainted(false);
                return button;
            }
        });
        
        box.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setForeground(index == -1 ? Color.BLACK : TEXT_PRIMARY);
                label.setBackground(isSelected ? PRIMARY : new Color(25, 15, 45));
                label.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
                label.setFont(new Font("Segoe UI", Font.BOLD, 14));
                return label;
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String input = inputField.getText().trim();
        String from = (String) comboFrom.getSelectedItem();
        String to = (String) comboTo.getSelectedItem();

        try {
            int number = 0;

            switch (from) {
                case "Decimal":
                    number = Integer.parseInt(input);
                    break;
                case "Binary":
                    number = Integer.parseInt(input, 2);
                    break;
                case "Octal":
                    number = Integer.parseInt(input, 8);
                    break;
                case "Hexadecimal":
                    number = Integer.parseInt(input, 16);
                    break;
            }

            String result = "";
            switch (to) {
                case "Decimal":
                    result = String.valueOf(number);
                    break;
                case "Binary":
                    result = Integer.toBinaryString(number);
                    break;
                case "Octal":
                    result = Integer.toOctalString(number);
                    break;
                case "Hexadecimal":
                    result = Integer.toHexString(number).toUpperCase();
                    break;
            }

            resultArea.setText(result);
        } catch (NumberFormatException ex) {
            resultArea.setText("Invalid Input!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(NumberSystemConverter::new);
    }
}