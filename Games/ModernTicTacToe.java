import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class ModernTicTacToe extends JFrame implements ActionListener {

    private static final Color BG_DARK = new Color(30, 20, 50);
    private static final Color CARD_BG = new Color(40, 30, 70);
    private static final Color SUCCESS = new Color(255, 20, 147); // X Color
    private static final Color GOLD_ACCENT = new Color(255, 215, 0); // O Color
    private static final Color TEXT_PRIMARY = Color.WHITE;
    private static final Color HIGHLIGHT_COLOR = new Color(80, 70, 120);

    private final JButton[][] buttons = new JButton[3][3];
    private boolean xTurn = true;
    private final JLabel statusLabel;
    private boolean vsComputer = false;
    private boolean gameOver = false;
    private final Random random = new Random();

    public ModernTicTacToe(boolean vsAI) {
        this.vsComputer = vsAI;

        setTitle("Tic Tac Toe");
        setSize(400, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); 
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_DARK);

        JLabel title = new JLabel("TIC TAC TOE", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(TEXT_PRIMARY);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        mainPanel.add(title, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(3, 3, 10, 10));
        gridPanel.setOpaque(false);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        Font btnFont = new Font("Segoe UI", Font.BOLD, 48);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                JButton btn = new JButton("");
                btn.setFont(btnFont);
                btn.setBackground(CARD_BG);
                btn.setFocusPainted(false);
                btn.setBorder(null);
                btn.addActionListener(this);
                buttons[i][j] = btn;
                gridPanel.add(btn);
            }
        }
        mainPanel.add(gridPanel, BorderLayout.CENTER);

        statusLabel = new JLabel("Turn: X", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        statusLabel.setForeground(TEXT_PRIMARY);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
        mainPanel.add(statusLabel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver) return;
        JButton btn = (JButton) e.getSource();
        if (!btn.getText().equals("")) return;
        btn.setText(xTurn ? "X" : "O");
        btn.setForeground(xTurn ? SUCCESS : GOLD_ACCENT);
        if (checkWinner()) return;
        xTurn = !xTurn;
        statusLabel.setText("Turn: " + (xTurn ? "X" : "O"));
        if (vsComputer && !xTurn && !gameOver) aiMove();
    }

    private void aiMove() {
        SwingUtilities.invokeLater(() -> {
            try {
                Thread.sleep(400);
            } catch (InterruptedException ignored) {}
            int row, col;
            do {
                row = random.nextInt(3);
                col = random.nextInt(3);
            } while (!buttons[row][col].getText().equals(""));
            buttons[row][col].setText("O");
            buttons[row][col].setForeground(GOLD_ACCENT);
            if (checkWinner()) return;
            xTurn = true;
            statusLabel.setText("Turn: X");
        });
    }

    private boolean checkWinner() {
        String winner = null;
        for (int i = 0; i < 3; i++) {
            if (checkEqual(buttons[i][0], buttons[i][1], buttons[i][2])) {
                winner = buttons[i][0].getText();
                highlight(buttons[i][0], buttons[i][1], buttons[i][2]);
            }
            if (checkEqual(buttons[0][i], buttons[1][i], buttons[2][i])) {
                winner = buttons[0][i].getText();
                highlight(buttons[0][i], buttons[1][i], buttons[2][i]);
            }
        }
        if (checkEqual(buttons[0][0], buttons[1][1], buttons[2][2])) {
            winner = buttons[0][0].getText();
            highlight(buttons[0][0], buttons[1][1], buttons[2][2]);
        }
        if (checkEqual(buttons[0][2], buttons[1][1], buttons[2][0])) {
            winner = buttons[0][2].getText();
            highlight(buttons[0][2], buttons[1][1], buttons[2][0]);
        }
        if (winner != null && !winner.isEmpty()) {
            statusLabel.setText("Winner: " + winner + "!");
            gameOver = true;
            showEndOptions("Winner: " + winner + "!");
            return true;
        } else if (isBoardFull()) {
            statusLabel.setText("It's a Draw!");
            gameOver = true;
            showEndOptions("It's a Draw!");
            return true;
        }
        return false;
    }

    private boolean checkEqual(JButton a, JButton b, JButton c) {
        return !a.getText().isEmpty() && a.getText().equals(b.getText()) && b.getText().equals(c.getText());
    }

    private void highlight(JButton a, JButton b, JButton c) {
        a.setBackground(HIGHLIGHT_COLOR);
        b.setBackground(HIGHLIGHT_COLOR);
        c.setBackground(HIGHLIGHT_COLOR);
    }
    
    private boolean isBoardFull() {
        for (JButton[] row : buttons) 
            for (JButton btn : row) 
                if (btn.getText().isEmpty()) return false;
        return true;
    }
    
    private void showEndOptions(String message) {
        Object[] options = {"Play Again", "Back to Menu"};
        int choice = JOptionPane.showOptionDialog(this, message + "\nWhat would you like to do?",
              "Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (choice == JOptionPane.YES_OPTION) { // Play Again
            dispose();
            new ModernTicTacToe(vsComputer);
        } else if (choice == JOptionPane.NO_OPTION) { // Back to Menu
            dispose();
            showModeMenu(); // ✅ Reopen the menu automatically
        }
    }

    // ✅ Mode Selection Menu stays available after finishing a game
    private static void showModeMenu() {
        JDialog menuDialog = new JDialog((Frame) null, "Select Mode", true);
        menuDialog.setSize(350, 250);
        menuDialog.setLocationRelativeTo(null);
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
    
    public static void main(String[] args) {
        showModeMenu();
    }
}
