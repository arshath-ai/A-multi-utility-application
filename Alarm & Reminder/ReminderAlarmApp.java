import java.awt.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.Border;

class Reminder {
    String message;
    String time;
    boolean triggered;
    public Reminder(String message, String time) {
        this.message = message;
        this.time = time;
        this.triggered = false;
    }
}

class Alarm {
    String time;
    boolean triggered;
    public Alarm(String time) {
        this.time = time;
        this.triggered = false;
    }
}

public class ReminderAlarmApp extends JFrame {
    
    // Modern color scheme
    private static final Color BG_DARK = new Color(30, 20, 50);
    private static final Color CARD_BG = new Color(40, 30, 70);
    private static final Color PRIMARY = new Color(138, 43, 226);
    private static final Color SUCCESS = new Color(255, 20, 147);
    private static final Color TEXT_PRIMARY = new Color(255, 255, 255);
    private static final Color TEXT_SECONDARY = new Color(200, 200, 220);
    private static final Color GOLD_ACCENT = new Color(255, 215, 0);
    private static final Color FIELD_BG = new Color(25, 15, 45);
    private static final Color DELETE_COLOR = new Color(220, 20, 60);

    private ArrayList<Reminder> reminders = new ArrayList<>();
    private ArrayList<Alarm> alarms = new ArrayList<>();
    private Timer checkTimer;
    
    private volatile boolean alarmSoundRunning = false;
    private volatile boolean reminderSoundRunning = false;
    private volatile boolean isAnySoundPlaying = false;

    public ReminderAlarmApp() {
        setTitle("Alarm & Reminder");
        setSize(600, 550);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_DARK);

        UIManager.put("TabbedPane.selectedForeground", GOLD_ACCENT);
        UIManager.put("TabbedPane.foreground", TEXT_SECONDARY);
        UIManager.put("TabbedPane.selected", CARD_BG);
        UIManager.put("TabbedPane.background", BG_DARK);
        UIManager.put("TabbedPane.borderHightlightColor", PRIMARY);
        UIManager.put("TabbedPane.focus", new Color(0,0,0,0));

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 16));

        tabbedPane.addTab("Alarm", createAlarmPanel());
        tabbedPane.addTab("Reminder", createReminderPanel());
        tabbedPane.addTab("Stopwatch", createStopwatchPanel());

        add(tabbedPane, BorderLayout.CENTER);

        checkTimer = new Timer(1000, e -> checkAlarmsAndReminders());
        checkTimer.start();
    }
    
    private JPanel createAlarmPanel(){
        JPanel alarmPanel = new JPanel(new BorderLayout(10,10));
        alarmPanel.setBackground(CARD_BG);
        // **Changed outline border to WHITE**
        alarmPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 2),
            BorderFactory.createEmptyBorder(13, 13, 13, 13)
        ));
        
        DefaultListModel<String> alarmListModel = new DefaultListModel<>();
        JList<String> alarmList = new JList<>(alarmListModel);
        alarmList.setBackground(FIELD_BG);
        alarmList.setForeground(TEXT_PRIMARY);
        alarmList.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        alarmList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane alarmScroll = new JScrollPane(alarmList);
        alarmScroll.setBorder(BorderFactory.createLineBorder(PRIMARY));

        JPanel alarmInputPanel = new JPanel(new GridBagLayout());
        alarmInputPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField alarmField = styleTextField(new JTextField(5)); 
        JButton addAlarmButton = styleButton(new JButton("Add Alarm"), SUCCESS);
        JButton deleteAlarmButton = styleButton(new JButton("Delete Selected"), DELETE_COLOR);
        JButton stopAlarmButton = styleButton(new JButton("Stop Alarm"), PRIMARY);

        gbc.gridx = 0; gbc.gridy = 0; alarmInputPanel.add(new JLabel("Time (HH:mm):"){{setForeground(TEXT_SECONDARY);}}, gbc);
        gbc.gridx = 1; gbc.gridy = 0; alarmInputPanel.add(alarmField, gbc);
        gbc.gridx = 2; gbc.gridy = 0; alarmInputPanel.add(addAlarmButton, gbc);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.HORIZONTAL; alarmInputPanel.add(deleteAlarmButton, gbc);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.HORIZONTAL; alarmInputPanel.add(stopAlarmButton, gbc);

        addAlarmButton.addActionListener(e -> {
            String time = alarmField.getText().trim();
            if (time.matches("\\d{2}:\\d{2}")) {
                alarms.add(new Alarm(time));
                alarmListModel.addElement(time);
                alarmField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Please use HH:mm format.", "Invalid Time", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteAlarmButton.addActionListener(e -> {
            int selectedIndex = alarmList.getSelectedIndex();
            if (selectedIndex != -1) {
                alarmListModel.remove(selectedIndex);
                alarms.remove(selectedIndex);
            } else {
                JOptionPane.showMessageDialog(this, "Please select an alarm to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });

        stopAlarmButton.addActionListener(e -> {
            alarmSoundRunning = false;
            isAnySoundPlaying = false;
        });

        alarmPanel.add(alarmScroll, BorderLayout.CENTER);
        alarmPanel.add(alarmInputPanel, BorderLayout.SOUTH);
        return alarmPanel;
    }

    private JPanel createReminderPanel(){
        JPanel reminderPanel = new JPanel(new BorderLayout(10,10));
        reminderPanel.setBackground(CARD_BG);
        // **Changed outline border to WHITE**
        reminderPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 2),
            BorderFactory.createEmptyBorder(13, 13, 13, 13)
        ));

        DefaultListModel<String> reminderListModel = new DefaultListModel<>();
        JList<String> reminderList = new JList<>(reminderListModel);
        reminderList.setBackground(FIELD_BG);
        reminderList.setForeground(TEXT_PRIMARY);
        reminderList.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        reminderList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane reminderScroll = new JScrollPane(reminderList);
        reminderScroll.setBorder(BorderFactory.createLineBorder(PRIMARY));

        JPanel reminderInputPanel = new JPanel(new GridBagLayout());
        reminderInputPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField reminderTimeField = styleTextField(new JTextField(5));
        JTextField reminderMessageField = styleTextField(new JTextField(15));
        JButton addReminderButton = styleButton(new JButton("Add Reminder"), SUCCESS);
        JButton deleteReminderButton = styleButton(new JButton("Delete Selected"), DELETE_COLOR);
        JButton stopReminderButton = styleButton(new JButton("Stop Reminder Sound"), PRIMARY);

        gbc.gridx=0; gbc.gridy=0; reminderInputPanel.add(new JLabel("Time (HH:mm):"){{setForeground(TEXT_SECONDARY);}}, gbc);
        gbc.gridx=1; gbc.gridy=0; reminderInputPanel.add(reminderTimeField, gbc);
        gbc.gridx=0; gbc.gridy=1; reminderInputPanel.add(new JLabel("Message:"){{setForeground(TEXT_SECONDARY);}}, gbc);
        gbc.gridx=1; gbc.gridy=1; reminderInputPanel.add(reminderMessageField, gbc);
        gbc.gridx=0; gbc.gridy=2; gbc.gridwidth=2; reminderInputPanel.add(addReminderButton, gbc);
        gbc.gridx=0; gbc.gridy=3; gbc.gridwidth=2; reminderInputPanel.add(deleteReminderButton, gbc);
        gbc.gridx=0; gbc.gridy=4; gbc.gridwidth=2; reminderInputPanel.add(stopReminderButton, gbc);

        addReminderButton.addActionListener(e -> {
            String time = reminderTimeField.getText().trim();
            String msg = reminderMessageField.getText().trim();
            if (time.matches("\\d{2}:\\d{2}") && !msg.isEmpty()) {
                reminders.add(new Reminder(msg, time));
                reminderListModel.addElement(time + " - " + msg);
                reminderTimeField.setText("");
                reminderMessageField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Please provide a valid time (HH:mm) and a message.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        deleteReminderButton.addActionListener(e -> {
            int selectedIndex = reminderList.getSelectedIndex();
            if (selectedIndex != -1) {
                reminderListModel.remove(selectedIndex);
                reminders.remove(selectedIndex);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a reminder to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });

        stopReminderButton.addActionListener(e -> {
            reminderSoundRunning = false;
            isAnySoundPlaying = false;
        });

        reminderPanel.add(reminderScroll, BorderLayout.CENTER);
        reminderPanel.add(reminderInputPanel, BorderLayout.SOUTH);
        return reminderPanel;
    }

    private JPanel createStopwatchPanel() {
        JPanel stopwatchPanel = new JPanel(new BorderLayout(10, 20));
        stopwatchPanel.setBackground(CARD_BG);
        // **Changed outline border to WHITE**
        stopwatchPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 2),
            BorderFactory.createEmptyBorder(28, 28, 28, 28)
        ));

        JLabel stopwatchLabel = new JLabel("00:00:00", SwingConstants.CENTER);
        stopwatchLabel.setFont(new Font("Segoe UI", Font.BOLD, 72));
        stopwatchLabel.setForeground(SUCCESS);
        stopwatchPanel.add(stopwatchLabel, BorderLayout.CENTER);

        JPanel stopwatchButtons = new JPanel(new GridLayout(1, 3, 10, 10));
        stopwatchButtons.setOpaque(false);
        JButton startBtn = styleButton(new JButton("Start"), SUCCESS);
        JButton stopBtn = styleButton(new JButton("Stop"), PRIMARY);
        JButton resetBtn = styleButton(new JButton("Reset"), new Color(80, 50, 130));
        
        stopwatchButtons.add(startBtn);
        stopwatchButtons.add(stopBtn);
        stopwatchButtons.add(resetBtn);
        stopwatchPanel.add(stopwatchButtons, BorderLayout.SOUTH);

        final int[] elapsedSeconds = {0};
        Timer stopwatchTimer = new Timer(1000, e -> {
            elapsedSeconds[0]++;
            int hrs = elapsedSeconds[0] / 3600;
            int mins = (elapsedSeconds[0] % 3600) / 60;
            int secs = elapsedSeconds[0] % 60;
            stopwatchLabel.setText(String.format("%02d:%02d:%02d", hrs, mins, secs));
        });

        startBtn.addActionListener(e -> stopwatchTimer.start());
        stopBtn.addActionListener(e -> stopwatchTimer.stop());
        resetBtn.addActionListener(e -> {
            stopwatchTimer.stop();
            elapsedSeconds[0] = 0;
            stopwatchLabel.setText("00:00:00");
        });

        return stopwatchPanel;
    }

    private JTextField styleTextField(JTextField field) {
        field.setBackground(FIELD_BG);
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(SUCCESS);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        Border line = BorderFactory.createLineBorder(PRIMARY, 1);
        Border padding = BorderFactory.createEmptyBorder(5, 10, 5, 10);
        field.setBorder(BorderFactory.createCompoundBorder(line, padding));
        return field;
    }

    private JButton styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(TEXT_PRIMARY);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void checkAlarmsAndReminders() {
        if (isAnySoundPlaying) return;

        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Colombo"));
        String currentHM = now.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"));

        for (Alarm a : alarms) {
            if (!a.triggered && a.time.equals(currentHM)) {
                alarmSoundRunning = true;
                playSoundLoop("ALARM", "Alarm Ringing!");
                a.triggered = true;
                return; 
            }
        }

        for (Reminder r : reminders) {
            if (!r.triggered && r.time.equals(currentHM)) {
                reminderSoundRunning = true;
                playSoundLoop("REMINDER", "Reminder: " + r.message);
                r.triggered = true;
                return;
            }
        }
    }

    private void playSoundLoop(String soundType, String message) {
        isAnySoundPlaying = true;
        new Thread(() -> {
            try {
                int duration = 300, pause = 100, freq = 1000;
                
                boolean condition = soundType.equals("ALARM") ? alarmSoundRunning : reminderSoundRunning;

                while (condition) {
                    byte[] buf = new byte[1];
                    AudioFormat af = new AudioFormat(8000f, 8, 1, true, false);
                    try (SourceDataLine sdl = AudioSystem.getSourceDataLine(af)) {
                        sdl.open(af);
                        sdl.start();
                        for (int j = 0; j < duration * 8; j++) {
                            double angle = j / (8000.0 / freq) * 2.0 * Math.PI;
                            buf[0] = (byte)(Math.sin(angle) * 127.0);
                            sdl.write(buf, 0, 1);
                        }
                        sdl.drain();
                    }
                    Thread.sleep(pause);
                    
                    if(soundType.equals("ALARM")){
                        condition = alarmSoundRunning;
                    } else {
                        condition = reminderSoundRunning;
                    }
                }
            } catch (Exception e) {
                Toolkit.getDefaultToolkit().beep();
            } finally {
                isAnySoundPlaying = false; 
            }
        }).start();
        
        String fullMessage = message + "\nClick the corresponding 'Stop' button to stop the sound.";
        JOptionPane.showMessageDialog(this, fullMessage, soundType, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ReminderAlarmApp().setVisible(true);
        });
    }
}