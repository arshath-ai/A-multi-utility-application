import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class UnitConverter extends JFrame implements ActionListener {

    private JComboBox<String> categoryBox, fromBox, toBox;
    private JTextField inputField, resultField;

    // Modern color scheme
    private static final Color BG_DARK = new Color(30, 20, 50);
    private static final Color CARD_BG = new Color(40, 30, 70);
    private static final Color PRIMARY = new Color(138, 43, 226);
    private static final Color PRIMARY_HOVER = new Color(158, 63, 246);
    private static final Color SUCCESS = new Color(255, 20, 147);
    private static final Color TEXT_PRIMARY = new Color(255, 255, 255);
    private static final Color TEXT_SECONDARY = new Color(200, 200, 220);

    public UnitConverter() {
        setTitle("Unit Converter");
        setSize(600, 650);
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        // =======================================

        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(BG_DARK);
        mainPanel.setLayout(null);

        // Title
        JLabel title = new JLabel("Unit Converter", SwingConstants.CENTER);
        title.setBounds(0, 30, 600, 50);
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));
        title.setForeground(new Color(255, 215, 0)); 
        mainPanel.add(title);

        // Category Card
        JPanel categoryCard = createCard();
        categoryCard.setBounds(50, 110, 500, 70);
        categoryCard.setLayout(null);
        
        JLabel categoryLabel = new JLabel("Category");
        categoryLabel.setBounds(20, 22, 80, 25);
        categoryLabel.setForeground(new Color(255, 215, 0));
        categoryLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        categoryCard.add(categoryLabel);
        
        String[] categories = {"Length", "Weight", "Temperature", "Volume", "Time"};
        categoryBox = new JComboBox<>(categories);
        categoryBox.setBounds(110, 15, 370, 40);
        categoryBox.addActionListener(this);
        styleComboBox(categoryBox);
        categoryCard.add(categoryBox);
        mainPanel.add(categoryCard);

        // Conversion Units Card
        JPanel unitsCard = createCard();
        unitsCard.setBounds(50, 200, 500, 90);
        unitsCard.setLayout(null);
        
        JLabel fromLabel = new JLabel("From");
        fromLabel.setBounds(20, 10, 50, 25);
        fromLabel.setForeground(new Color(100, 200, 255)); 
        fromLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        fromBox = new JComboBox<>();
        fromBox.setBounds(20, 35, 210, 40);
        styleComboBox(fromBox);
        
        JLabel toLabel = new JLabel("To");
        toLabel.setBounds(270, 10, 50, 25);
        toLabel.setForeground(new Color(255, 150, 200)); 
        toLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        toBox = new JComboBox<>();
        toBox.setBounds(270, 35, 210, 40);
        styleComboBox(toBox);
        
        unitsCard.add(fromLabel);
        unitsCard.add(fromBox);
        unitsCard.add(toLabel);
        unitsCard.add(toBox);
        mainPanel.add(unitsCard);

        // Input Card
        JPanel inputCard = createCard();
        inputCard.setBounds(50, 310, 500, 70);
        inputCard.setLayout(null);
        
        JLabel inputLabel = new JLabel("Value");
        inputLabel.setBounds(20, 22, 80, 25);
        inputLabel.setForeground(new Color(255, 215, 0));
        inputLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        inputField = new JTextField();
        inputField.setBounds(110, 15, 370, 40);
        inputField.setBackground(BG_DARK); 
        inputField.setForeground(TEXT_PRIMARY);
        inputField.setCaretColor(TEXT_PRIMARY);
        inputField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY, 2, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        
        inputCard.add(inputLabel);
        inputCard.add(inputField);
        mainPanel.add(inputCard);

        // Convert button
        JButton convertBtn = new JButton("CONVERT");
        convertBtn.setBounds(50, 400, 500, 55);
        convertBtn.setBackground(SUCCESS);
        convertBtn.setForeground(TEXT_PRIMARY);
        convertBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        convertBtn.setFocusPainted(false);
        convertBtn.setBorder(BorderFactory.createEmptyBorder());
        convertBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        convertBtn.addActionListener(this);
        
        convertBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                convertBtn.setBackground(new Color(255, 60, 180));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                convertBtn.setBackground(SUCCESS);
            }
        });
        
        mainPanel.add(convertBtn);

        // Result Card
        JPanel resultCard = createCard();
        resultCard.setBounds(50, 480, 500, 120);
        resultCard.setLayout(new BorderLayout());
        resultCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 255, 150), 3, true),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel resultLabelTop = new JLabel("RESULT", SwingConstants.CENTER);
        resultLabelTop.setFont(new Font("Segoe UI", Font.BOLD, 14));
        resultLabelTop.setForeground(new Color(100, 255, 150)); 
        
        resultField = new JTextField();
        resultField.setEditable(false);
        resultField.setBackground(new Color(30, 20, 60));
        resultField.setForeground(new Color(0, 255, 150)); 
        resultField.setFont(new Font("Segoe UI", Font.BOLD, 28));
        resultField.setHorizontalAlignment(JTextField.CENTER);
        resultField.setBorder(BorderFactory.createEmptyBorder());
        resultField.setText("---");
        
        resultCard.add(resultLabelTop, BorderLayout.NORTH);
        resultCard.add(resultField, BorderLayout.CENTER);
        mainPanel.add(resultCard);

        add(mainPanel);
        updateUnits();
        setVisible(true);
    }

    private JPanel createCard() {
        JPanel panel = new JPanel();
        panel.setBackground(CARD_BG);
        panel.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 80), 1, true));
        return panel;
    }

    private void styleComboBox(JComboBox<String> box) {
        box.setBackground(BG_DARK);
        box.setForeground(Color.BLACK);
        box.setFont(new Font("Segoe UI", Font.BOLD, 14));
        box.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY, 2, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        box.setPreferredSize(new Dimension(150, 40));
        
        box.setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = new JButton() {
                    @Override
                    public void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(Color.WHITE);
                        int w = getWidth();
                        int h = getHeight();
                        int[] xPoints = {w/2 - 4, w/2 + 4, w/2};
                        int[] yPoints = {h/2 - 2, h/2 - 2, h/2 + 3};
                        g2.fillPolygon(xPoints, yPoints, 3);
                        g2.dispose();
                    }
                };
                button.setBorder(BorderFactory.createEmptyBorder());
                button.setBackground(BG_DARK);
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
                label.setBackground(isSelected ? PRIMARY : BG_DARK);
                label.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
                label.setFont(new Font("Segoe UI", Font.BOLD, 14));
                return label;
            }
        });
    }

    private void updateUnits() {
        String category = (String) categoryBox.getSelectedItem();
        fromBox.removeAllItems();
        toBox.removeAllItems();

        switch (category) {
            case "Length":
                String[] lengths = {"Meter", "Kilometer", "Centimeter", "Millimeter", "Mile", "Yard", "Foot", "Inch"};
                for (String s : lengths) { fromBox.addItem(s); toBox.addItem(s); }
                break;
            case "Weight":
                String[] weights = {"Kilogram", "Gram", "Milligram", "Pound", "Ounce"};
                for (String s : weights) { fromBox.addItem(s); toBox.addItem(s); }
                break;
            case "Temperature":
                String[] temps = {"Celsius", "Fahrenheit", "Kelvin"};
                for (String s : temps) { fromBox.addItem(s); toBox.addItem(s); }
                break;
            case "Volume":
                String[] volumes = {"Liter", "Milliliter", "Cubic Meter", "Cubic Centimeter", "Gallon"};
                for (String s : volumes) { fromBox.addItem(s); toBox.addItem(s); }
                break;
            case "Time":
                String[] times = {"Second", "Minute", "Hour", "Day", "Week"};
                for (String s : times) { fromBox.addItem(s); toBox.addItem(s); }
                break;
        }
    }

    private double convert(String category, String from, String to, double value) {
        switch (category) {
            case "Length": return convertLength(from, to, value);
            case "Weight": return convertWeight(from, to, value);
            case "Temperature": return convertTemperature(from, to, value);
            case "Volume": return convertVolume(from, to, value);
            case "Time": return convertTime(from, to, value);
            default: return value;
        }
    }

    private double convertLength(String from, String to, double val) {
        double meter = val;
        switch (from) {
            case "Kilometer": meter = val * 1000; break;
            case "Centimeter": meter = val / 100; break;
            case "Millimeter": meter = val / 1000; break;
            case "Mile": meter = val * 1609.34; break;
            case "Yard": meter = val * 0.9144; break;
            case "Foot": meter = val * 0.3048; break;
            case "Inch": meter = val * 0.0254; break;
        }
        switch (to) {
            case "Meter": return meter;
            case "Kilometer": return meter / 1000;
            case "Centimeter": return meter * 100;
            case "Millimeter": return meter * 1000;
            case "Mile": return meter / 1609.34;
            case "Yard": return meter / 0.9144;
            case "Foot": return meter / 0.3048;
            case "Inch": return meter / 0.0254;
        }
        return meter;
    }

    private double convertWeight(String from, String to, double val) {
        double kg = val;
        switch (from) {
            case "Gram": kg = val / 1000; break;
            case "Milligram": kg = val / 1e6; break;
            case "Pound": kg = val * 0.453592; break;
            case "Ounce": kg = val * 0.0283495; break;
        }
        switch (to) {
            case "Kilogram": return kg;
            case "Gram": return kg * 1000;
            case "Milligram": return kg * 1e6;
            case "Pound": return kg / 0.453592;
            case "Ounce": return kg / 0.0283495;
        }
        return kg;
    }

    private double convertTemperature(String from, String to, double val) {
        double celsius = val;
        switch (from) {
            case "Fahrenheit": celsius = (val - 32) * 5/9.0; break;
            case "Kelvin": celsius = val - 273.15; break;
        }
        switch (to) {
            case "Celsius": return celsius;
            case "Fahrenheit": return celsius * 9/5.0 + 32;
            case "Kelvin": return celsius + 273.15;
        }
        return celsius;
    }

    private double convertVolume(String from, String to, double val) {
        double liter = val;
        switch (from) {
            case "Milliliter": liter = val / 1000; break;
            case "Cubic Meter": liter = val * 1000; break;
            case "Cubic Centimeter": liter = val / 1000; break;
            case "Gallon": liter = val * 3.78541; break;
        }
        switch (to) {
            case "Liter": return liter;
            case "Milliliter": return liter * 1000;
            case "Cubic Meter": return liter / 1000;
            case "Cubic Centimeter": return liter * 1000;
            case "Gallon": return liter / 3.78541;
        }
        return liter;
    }

    private double convertTime(String from, String to, double val) {
        double seconds = val;
        switch (from) {
            case "Minute": seconds = val * 60; break;
            case "Hour": seconds = val * 3600; break;
            case "Day": seconds = val * 86400; break;
            case "Week": seconds = val * 604800; break;
        }
        switch (to) {
            case "Second": return seconds;
            case "Minute": return seconds / 60;
            case "Hour": return seconds / 3600;
            case "Day": return seconds / 86400;
            case "Week": return seconds / 604800;
        }
        return seconds;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == categoryBox) {
            updateUnits();
            resultField.setText("---");
        } else {
            try {
                if (fromBox.getSelectedItem() == null || toBox.getSelectedItem() == null) {
                       resultField.setText("Select units");
                       return;
                }
                
                double value = Double.parseDouble(inputField.getText());
                String category = (String) categoryBox.getSelectedItem();
                String from = (String) fromBox.getSelectedItem();
                String to = (String) toBox.getSelectedItem();

                double result = convert(category, from, to, value);
                resultField.setText(String.format("%.3f %s", result, to));
            } catch (NumberFormatException ex) {
                resultField.setText("Invalid input");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(UnitConverter::new);
    }
}