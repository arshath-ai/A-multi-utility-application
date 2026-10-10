import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;

public class ModernCalendar extends JFrame {

    // Modern color scheme from other tools
    private static final Color BG_DARK = new Color(30, 20, 50);
    private static final Color CARD_BG = new Color(40, 30, 70);
    private static final Color PRIMARY = new Color(138, 43, 226);
    private static final Color SUCCESS = new Color(255, 20, 147);
    private static final Color TEXT_PRIMARY = new Color(255, 255, 255);
    private static final Color TEXT_SECONDARY = new Color(200, 200, 220);
    private static final Color GOLD_ACCENT = new Color(255, 215, 0);

    private JLabel monthYearLabel;
    private JPanel calendarPanel;
    private YearMonth currentYearMonth;
    private final LocalDate today;

    public ModernCalendar() {
        setTitle("Calendar");
        setSize(650, 550);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_DARK);

        today = LocalDate.now();
        currentYearMonth = YearMonth.now();

        // Top panel with navigation
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(50, 35, 80));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JButton prevBtn = createNavButton("<");
        JButton nextBtn = createNavButton(">");
        monthYearLabel = new JLabel("", SwingConstants.CENTER);
        monthYearLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        monthYearLabel.setForeground(GOLD_ACCENT);

        prevBtn.addActionListener(e -> prevMonth());
        nextBtn.addActionListener(e -> nextMonth());

        topPanel.add(prevBtn, BorderLayout.WEST);
        topPanel.add(monthYearLabel, BorderLayout.CENTER);
        topPanel.add(nextBtn, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Calendar panel
        calendarPanel = new JPanel();
        calendarPanel.setLayout(new GridLayout(0, 7, 5, 5));
        calendarPanel.setBackground(BG_DARK);
        calendarPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(calendarPanel, BorderLayout.CENTER);

        drawCalendar();
        setVisible(true);
    }
    
    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 20));
        button.setForeground(TEXT_PRIMARY);
        button.setBackground(PRIMARY);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void drawCalendar() {
        calendarPanel.removeAll();

        String monthName = currentYearMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        monthYearLabel.setText(monthName + " " + currentYearMonth.getYear());

        // Weekday headers
        String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String day : days) {
            JLabel lbl = new JLabel(day, SwingConstants.CENTER);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lbl.setForeground(TEXT_SECONDARY);
            calendarPanel.add(lbl);
        }

        LocalDate firstOfMonth = currentYearMonth.atDay(1);
        int dayOfWeekOfFirst = firstOfMonth.getDayOfWeek().getValue(); // Monday=1, Sunday=7
        if (dayOfWeekOfFirst == 7) dayOfWeekOfFirst = 0; // Make Sunday=0

        int daysInMonth = currentYearMonth.lengthOfMonth();

        // Empty labels for days before the 1st
        for (int i = 0; i < dayOfWeekOfFirst; i++) {
            calendarPanel.add(new JLabel(""));
        }

        // Fill days
        for (int day = 1; day <= daysInMonth; day++) {
            JLabel lbl = new JLabel(String.valueOf(day), SwingConstants.CENTER);
            lbl.setOpaque(true);
            lbl.setBorder(new LineBorder(BG_DARK));
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
            lbl.setBackground(CARD_BG);
            lbl.setForeground(TEXT_PRIMARY);

            LocalDate thisDate = currentYearMonth.atDay(day);

            // Highlight weekends
            if (thisDate.getDayOfWeek() == DayOfWeek.SATURDAY || thisDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                lbl.setForeground(new Color(180, 160, 220));
            }

            // Highlight today
            if (thisDate.equals(today)) {
                lbl.setBackground(SUCCESS);
                lbl.setForeground(TEXT_PRIMARY);
                lbl.setBorder(new LineBorder(GOLD_ACCENT, 2));
            }
            
            calendarPanel.add(lbl);
        }

        calendarPanel.revalidate();
        calendarPanel.repaint();
    }

    private void prevMonth() {
        currentYearMonth = currentYearMonth.minusMonths(1);
        drawCalendar();
    }

    private void nextMonth() {
        currentYearMonth = currentYearMonth.plusMonths(1);
        drawCalendar();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ModernCalendar::new);
    }
}