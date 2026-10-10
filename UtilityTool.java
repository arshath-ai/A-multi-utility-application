import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;

public class UtilityTool extends JFrame {
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private Color[] liquidColors = {
        new Color(138, 43, 226, 200),
        new Color(75, 0, 130, 200),
        new Color(255, 20, 147, 200),
        new Color(0, 191, 255, 200)
    };

    public UtilityTool() {
        setTitle("Utility Tool");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createHomePage(), "HOME");
        mainPanel.add(createEntertainmentPage(), "ENTERTAINMENT");
        mainPanel.add(createAboutPage(), "ABOUT");
        mainPanel.add(createCalculatorDetailsPage(), "CALCULATOR_DETAILS");
        mainPanel.add(createConverterDetailsPage(), "CONVERTER_DETAILS");
        mainPanel.add(createReminderDetailsPage(), "REMINDER_DETAILS");

        add(createNavigationBar(), BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createNavigationBar() {
        JPanel navBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        navBar.setBackground(new Color(30, 20, 50));
        navBar.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(138, 43, 226)));

        String[] navItems = {"Home", "Calculator", "Converter", "Reminder", "Entertainment", "About"};
        String[] pages = {"HOME", "CALCULATOR_DETAILS", "CONVERTER_DETAILS", "REMINDER_DETAILS", "ENTERTAINMENT", "ABOUT"};

        for (int i = 0; i < navItems.length; i++) {
            final String page = pages[i];
            RoundedNavButton navBtn = new RoundedNavButton(navItems[i]);
            navBtn.addActionListener(e -> cardLayout.show(mainPanel, page));
            navBar.add(navBtn);
        }

        return navBar;
    }

    private JPanel createHomePage() {
        JPanel panel = new LiquidPanel(liquidColors);
        panel.setLayout(new BorderLayout());
        
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));

        JLabel title = new JLabel("UTILITY TOOLS");
        title.setFont(new Font("Arial", Font.BOLD, 48));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(title);
        topPanel.add(Box.createVerticalStrut(15));
        
        JLabel dateLabel = new JLabel();
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        dateLabel.setForeground(new Color(220, 220, 220));
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(dateLabel);
        topPanel.add(Box.createVerticalStrut(5));
        
        JLabel timeLabel = new JLabel();
        timeLabel.setFont(new Font("Arial", Font.BOLD, 56));
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(timeLabel);
        
        javax.swing.Timer clockTimer = new javax.swing.Timer(1000, e -> {
            java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("EEEE, MMMM d");
            java.text.SimpleDateFormat timeFormat = new java.text.SimpleDateFormat("h:mm");
            dateLabel.setText(dateFormat.format(new java.util.Date()));
            timeLabel.setText(timeFormat.format(new java.util.Date()));
        });
        clockTimer.start();
        
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("EEEE, MMMM d");
        java.text.SimpleDateFormat timeFormat = new java.text.SimpleDateFormat("h:mm");
        dateLabel.setText(dateFormat.format(new java.util.Date()));
        timeLabel.setText(timeFormat.format(new java.util.Date()));
        
        panel.add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0;
        gbc.gridy = 0;

        String[][] options = {
            {"Calculator", "GPA Calculator", "Scientific Calculator"},
            {"Converter", "Unit Converter", "Number System Converter"},
            {"Reminder & Alarm", "Alarm & Reminder", "Calendar"}
        };

        for (String[] option : options) {
            LiquidButton btn = new LiquidButton(option[0]);
            btn.addActionListener(e -> showSubMenu(option));
            centerPanel.add(btn, gbc);
            gbc.gridy++;
        }
        
        panel.add(centerPanel, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 20));
        LiquidButton nextBtn = new LiquidButton("Entertainment →");
        nextBtn.addActionListener(e -> cardLayout.show(mainPanel, "ENTERTAINMENT"));
        bottomPanel.add(nextBtn);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void showSubMenu(String[] options) {
        JDialog dialog = new JDialog(this, options[0], true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new LiquidPanel(liquidColors);
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        for (int i = 1; i < options.length; i++) {
            LiquidButton btn = new LiquidButton(options[i]);
            final String optionName = options[i];

            btn.addActionListener(e -> {
                if (optionName.equals("Scientific Calculator")) {
                    SwingUtilities.invokeLater(() -> new ScientificCalculator().setVisible(true));
                    dialog.dispose();
                }
                else if (optionName.equals("GPA Calculator")) {
                    SwingUtilities.invokeLater(() -> new GPACalculatorUI().setVisible(true));
                    dialog.dispose();
                } 
                else if (optionName.equals("Unit Converter")) {
                    SwingUtilities.invokeLater(() -> new UnitConverter().setVisible(true));
                    dialog.dispose();
                } else if (optionName.equals("Number System Converter")) {
                    SwingUtilities.invokeLater(() -> new NumberSystemConverter().setVisible(true));
                    dialog.dispose();
                }
                else if (optionName.equals("Alarm & Reminder")) {
                    SwingUtilities.invokeLater(() -> new ReminderAlarmApp().setVisible(true));
                    dialog.dispose();
                }
                else if (optionName.equals("Calendar")) {
                    SwingUtilities.invokeLater(() -> new ModernCalendar().setVisible(true));
                    dialog.dispose();
                }
                else {
                    JOptionPane.showMessageDialog(dialog,
                            "Opening " + optionName + "...",
                            "Tool", JOptionPane.INFORMATION_MESSAGE);
                }
            });

            panel.add(btn, gbc);
            gbc.gridy++;
        }

        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private JPanel createEntertainmentPage() {
        JPanel panel = new LiquidPanel(new Color[]{
            new Color(255, 69, 0, 200),
            new Color(255, 140, 0, 200),
            new Color(255, 215, 0, 200),
            new Color(255, 105, 180, 200)
        });
        panel.setLayout(new BorderLayout());

        JLabel title = new JLabel("ENTERTAINMENT", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        panel.add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0;
        gbc.gridy = 0;

        String[] games = {"Snake Game", "Ping Pong", "Tic Tac Toe"};

        for (String game : games) {
            LiquidButton btn = new LiquidButton(game);

            btn.addActionListener(e -> {
                switch (game) {
                    case "Snake Game":
                        new ModernSnakeGame().setVisible(true);
                        break;
                    case "Ping Pong":
                        JFrame pingPongFrame = new JFrame("Ping Pong Game");
                        pingPongFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        pingPongFrame.setSize(550, 550);
                        pingPongFrame.setResizable(false);
                        pingPongFrame.setLocationRelativeTo(this);
                        pingPongFrame.add(new PingPongGame());
                        pingPongFrame.setVisible(true);
                        break;
                    case "Tic Tac Toe":
                        showTicTacToeMenu();
                        break;
                }
            });
            
            centerPanel.add(btn, gbc);
            gbc.gridy++;
        }

        panel.add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 0));

        LiquidButton backBtn = new LiquidButton("← Back to Home");
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "HOME"));
        bottomPanel.add(backBtn);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void showTicTacToeMenu() {
        JDialog menuDialog = new JDialog(this, "Select Mode", true);
        menuDialog.setSize(350, 250);
        menuDialog.setLocationRelativeTo(this);
        menuDialog.setResizable(false);
        menuDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBackground(new Color(30, 20, 50));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel title = new JLabel("Choose Game Mode", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        panel.add(title);

        JButton twoPlayer = new JButton("Two Player");
        JButton vsAI = new JButton("Vs Computer");

        for (JButton b : new JButton[]{twoPlayer, vsAI}) {
            b.setFont(new Font("Segoe UI", Font.BOLD, 16));
            b.setBackground(new Color(40, 30, 70));
            b.setForeground(Color.WHITE);
            b.setFocusPainted(false);
            panel.add(b);
        }

        twoPlayer.addActionListener(e -> {
            menuDialog.dispose();
            new ModernTicTacToe(false);
        });
        vsAI.addActionListener(e -> {
            menuDialog.dispose();
            new ModernTicTacToe(true);
        });

        menuDialog.add(panel);
        menuDialog.setVisible(true);
    }

    private JPanel createCalculatorDetailsPage() {
        JPanel panel = new LiquidPanel(new Color[]{ new Color(255, 69, 180, 200), new Color(255, 105, 180, 200), new Color(255, 20, 147, 200), new Color(199, 21, 133, 200) });
        panel.setLayout(new BorderLayout());
        JLabel title = new JLabel("CALCULATOR", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        panel.add(title, BorderLayout.NORTH);
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        JTextArea detailsText = new JTextArea();
        detailsText.setText("CALCULATOR TOOLS\n\n═══════════════════════════════════════════\n\n1. GPA CALCULATOR\n   • Calculate your Grade Point Average easily\n   • Support for multiple grading systems\n   • Semester-wise and cumulative GPA calculation\n   • Credit hours consideration\n   • Grade to GPA conversion\n\n   HOW TO USE:\n   - Enter your course grades\n   - Add credit hours for each course\n   - Get instant GPA calculation\n   - Track your academic progress\n\n2. SCIENTIFIC CALCULATOR\n   • Advanced mathematical operations\n   • Trigonometric functions (sin, cos, tan)\n   • Logarithmic calculations (log, ln)\n   • Power and root operations\n   • Memory functions (M+, M-, MR, MC)\n   • Constants (π, e)\n\n   FEATURES:\n   - Standard arithmetic operations (+, -, ×, ÷)\n   - Parentheses for complex expressions\n   - Degree and Radian modes\n   - Scientific notation support\n   - History of calculations\n\nTIPS:\n✓ Use parentheses for complex calculations\n✓ Check your calculator mode (Degree/Radian)\n✓ Save frequently used values in memory\n✓ Review calculation history for accuracy");
        detailsText.setFont(new Font("Arial", Font.BOLD, 14)); 
        detailsText.setForeground(Color.WHITE);
        detailsText.setOpaque(false);
        detailsText.setEditable(false);
        detailsText.setLineWrap(true);
        detailsText.setWrapStyleWord(true);
        detailsText.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(detailsText);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 100), 2));
        scrollPane.setPreferredSize(new Dimension(700, 400));
        contentPanel.add(scrollPane);
        panel.add(contentPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createConverterDetailsPage() {
        JPanel panel = new LiquidPanel(new Color[]{ new Color(50, 205, 50, 200), new Color(34, 139, 34, 200), new Color(0, 255, 127, 200), new Color(46, 139, 87, 200) });
        panel.setLayout(new BorderLayout());
        JLabel title = new JLabel("CONVERTER", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        panel.add(title, BorderLayout.NORTH);
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        JTextArea detailsText = new JTextArea();
        detailsText.setText("CONVERTER TOOLS\n\n═══════════════════════════════════════════\n\n1. UNIT CONVERTER\n   Convert between various units of measurement\n\n   LENGTH:\n   • Meters, Kilometers, Miles, Feet, Inches, Yards\n   • Centimeters, Millimeters, Nautical Miles\n\n   WEIGHT/MASS:\n   • Kilograms, Grams, Pounds, Ounces\n   • Tons, Milligrams, Stone\n\n   TEMPERATURE:\n   • Celsius, Fahrenheit, Kelvin\n\n   VOLUME:\n   • Liters, Milliliters, Gallons, Cups\n   • Quarts, Pints, Fluid Ounces\n\n   AREA:\n   • Square meters, Square feet, Acres\n   • Hectares, Square kilometers\n\n   SPEED:\n   • Meters/second, Kilometers/hour\n   • Miles/hour, Knots\n\n2. NUMBER SYSTEM CONVERTER\n   Convert between different number systems\n\n   SUPPORTED SYSTEMS:\n   • Binary (Base 2) - 0, 1\n   • Octal (Base 8) - 0-7\n   • Decimal (Base 10) - 0-9\n   • Hexadecimal (Base 16) - 0-9, A-F\n\n   FEATURES:\n   - Instant conversion between all systems\n   - Large number support\n   - Step-by-step conversion process\n   - Copy results to clipboard\n\nUSAGE TIPS:\n✓ Select source and target units/systems\n✓ Enter value and get instant results\n✓ Use for engineering and scientific work\n✓ Verify conversions with multiple methods");
        detailsText.setFont(new Font("Arial", Font.BOLD, 14));
        detailsText.setForeground(Color.WHITE);
        detailsText.setOpaque(false);
        detailsText.setEditable(false);
        detailsText.setLineWrap(true);
        detailsText.setWrapStyleWord(true);
        detailsText.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(detailsText);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 100), 2));
        scrollPane.setPreferredSize(new Dimension(700, 400));
        contentPanel.add(scrollPane);
        panel.add(contentPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createReminderDetailsPage() {
        JPanel panel = new LiquidPanel(new Color[]{ new Color(255, 140, 0, 200), new Color(255, 165, 0, 200), new Color(255, 215, 0, 200), new Color(255, 99, 71, 200) });
        panel.setLayout(new BorderLayout());
        JLabel title = new JLabel("REMINDER & ALARM", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        panel.add(title, BorderLayout.NORTH);
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        JTextArea detailsText = new JTextArea();
        detailsText.setText("REMINDER & ALARM TOOLS\n\n═══════════════════════════════════════════\n\n1. ALARM & REMINDER\n   Stay organized and never miss important events\n\n   ALARM FEATURES:\n   • Set multiple alarms\n   • Repeat daily, weekly, or custom intervals\n   • Snooze functionality\n   • Custom alarm sounds\n   • Vibration alerts\n   • Label your alarms\n\n   REMINDER FEATURES:\n   • Create task reminders\n   • Set priority levels (High, Medium, Low)\n   • Add detailed descriptions\n   • Recurring reminders\n   • Location-based reminders\n   • Notification options\n\n   HOW TO USE:\n   - Tap '+' to create new alarm/reminder\n   - Set time and date\n   - Choose notification sound\n   - Add labels and notes\n   - Enable/disable as needed\n\n2. CALENDAR\n   Organize your schedule and events\n\n   CALENDAR FEATURES:\n   • Month, Week, and Day views\n   • Create events with details\n   • Color-coded categories\n   • Event reminders\n   • Recurring events support\n   • Import/Export events\n   • Search past and future events\n\n   EVENT TYPES:\n   - Meetings and appointments\n   - Birthdays and anniversaries\n   - Tasks and deadlines\n   - Holidays and special occasions\n\nPRODUCTIVITY TIPS:\n✓ Set reminders 15 minutes before events\n✓ Use color coding for different categories\n✓ Review your calendar every morning\n✓ Set recurring reminders for habits\n✓ Keep notes brief and actionable");
        detailsText.setFont(new Font("Arial", Font.BOLD, 14));
        detailsText.setForeground(Color.WHITE);
        detailsText.setOpaque(false);
        detailsText.setEditable(false);
        detailsText.setLineWrap(true);
        detailsText.setWrapStyleWord(true);
        detailsText.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(detailsText);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 100), 2));
        scrollPane.setPreferredSize(new Dimension(700, 400));
        contentPanel.add(scrollPane);
        panel.add(contentPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createAboutPage() {
        JPanel panel = new LiquidPanel(new Color[]{ new Color(30, 144, 255, 200), new Color(0, 191, 255, 200), new Color(135, 206, 250, 200), new Color(100, 149, 237, 200) });
        panel.setLayout(new BorderLayout());
        JLabel title = new JLabel("ABOUT", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        panel.add(title, BorderLayout.NORTH);
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        JTextArea aboutText = new JTextArea();
        aboutText.setText("UTILITY TOOL v1.0\n\nWelcome to your all-in-one utility application!\n\nHOME PAGE FEATURES:\n• Calculator: Access GPA Calculator and Scientific Calculator\n• Converter: Use Unit Converter and Number System Converter\n• Reminder & Alarm: Set alarms, reminders, and view calendar\n\nENTERTAINMENT PAGE:\n• Play exciting games including Snake, Ping Pong, and Tic Tac Toe\n• Navigate easily between utility tools and entertainment\n\nFEATURES:\n✓ Beautiful liquid color animated UI\n✓ Real-time clock and date display\n✓ Easy navigation with modern interface\n✓ Multiple utility tools in one place\n✓ Fun games for entertainment\n\nCONTACT US:\nEmail: support@utilitytool.com\nWebsite: www.utilitytool.com\nPhone: +1 (555) 123-4567\n\nDeveloped with ❤️ using Java Swing\n© 2025 Utility Tool. All rights reserved.");
        aboutText.setFont(new Font("Arial", Font.BOLD, 14));
        aboutText.setForeground(Color.WHITE);
        aboutText.setOpaque(false);
        aboutText.setEditable(false);
        aboutText.setLineWrap(true);
        aboutText.setWrapStyleWord(true);
        aboutText.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(aboutText);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 100), 2));
        scrollPane.setPreferredSize(new Dimension(700, 400));
        contentPanel.add(scrollPane);
        panel.add(contentPanel, BorderLayout.CENTER);
        return panel;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UtilityTool());
    }
}

class LiquidPanel extends JPanel {
    private ArrayList<Blob> blobs = new ArrayList<>();
    private javax.swing.Timer timer;
    public LiquidPanel(Color[] colors) {
        setOpaque(false);
        Random rand = new Random();
        for (int i = 0; i < 8; i++) {
            blobs.add(new Blob(rand.nextInt(800), rand.nextInt(600), 80 + rand.nextInt(100), colors[rand.nextInt(colors.length)]));
        }
        timer = new javax.swing.Timer(50, e -> {
            for (Blob blob : blobs) { blob.move(getWidth(), getHeight()); }
            repaint();
        });
        timer.start();
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gradient = new GradientPaint(0, 0, new Color(20, 20, 40), getWidth(), getHeight(), new Color(40, 20, 60));
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        for (Blob blob : blobs) { blob.draw(g2d); }
    }
}

class Blob {
    private double x, y, size, vx, vy;
    private Color color;
    private Random rand = new Random();
    public Blob(double x, double y, double size, Color color) {
        this.x = x; this.y = y; this.size = size; this.color = color;
        this.vx = (rand.nextDouble() - 0.5) * 2;
        this.vy = (rand.nextDouble() - 0.5) * 2;
    }
    public void move(int width, int height) {
        x += vx; y += vy;
        if (x < 0 || x > width) vx *= -1;
        if (y < 0 || y > height) vy *= -1;
    }
    public void draw(Graphics2D g2d) {
        RadialGradientPaint paint = new RadialGradientPaint(new Point2D.Double(x, y), (float) size, new float[]{0.0f, 1.0f}, new Color[]{color, new Color(color.getRed(), color.getGreen(), color.getBlue(), 0)});
        g2d.setPaint(paint);
        g2d.fill(new Ellipse2D.Double(x - size, y - size, size * 2, size * 2));
    }
}

class LiquidButton extends JButton {
    private boolean hover = false;
    public LiquidButton(String text) {
        super(text);
        setPreferredSize(new Dimension(280, 60));
        setFont(new Font("Arial", Font.BOLD, 16));
        setForeground(Color.WHITE);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
            public void mouseExited(MouseEvent e) { hover = false; repaint(); }
        });
    }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (hover) {
            g2d.setPaint(new GradientPaint(0, 0, new Color(255, 100, 200, 200), getWidth(), getHeight(), new Color(100, 100, 255, 200)));
        } else {
            g2d.setPaint(new GradientPaint(0, 0, new Color(100, 50, 200, 180), getWidth(), getHeight(), new Color(50, 100, 200, 180)));
        }
        g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
        g2d.setColor(new Color(255, 255, 255, 100));
        g2d.setStroke(new BasicStroke(2));
        g2d.draw(new RoundRectangle2D.Double(1, 1, getWidth() - 2, getHeight() - 2, 30, 30));
        super.paintComponent(g);
    }
}

class RoundedNavButton extends JButton {
    private boolean hover = false;
    public RoundedNavButton(String text) {
        super(text);
        setPreferredSize(new Dimension(110, 35));
        setFont(new Font("Arial", Font.BOLD, 14));
        setForeground(Color.WHITE);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
            public void mouseExited(MouseEvent e) { hover = false; repaint(); }
        });
    }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (hover) { g2d.setColor(new Color(138, 43, 226)); }
        else { g2d.setColor(new Color(80, 50, 130)); }
        g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
        super.paintComponent(g);
    }
}