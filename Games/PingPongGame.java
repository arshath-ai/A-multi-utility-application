import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PingPongGame extends JPanel implements ActionListener, KeyListener {

    private static final Color BG_DARK = new Color(30, 20, 50);
    private static final Color CARD_BG = new Color(40, 30, 70);
    private static final Color SUCCESS = new Color(255, 20, 147);
    private static final Color GOLD_ACCENT = new Color(255, 215, 0);
    private static final Color TEXT_PRIMARY = Color.WHITE;

    private int ballX = 160, ballY = 100;
    private int ballXDir = 2, ballYDir = 3;

    private int paddleX = 200;
    private final int paddleY = 450, paddleW = 120, paddleH = 18;

    private int score = 0;
    private boolean gameRunning = true;
    private boolean moveLeft = false, moveRight = false;

    private final Timer timer;

    public PingPongGame() {
        setFocusable(true);
        addKeyListener(this);
        timer = new Timer(10, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(BG_DARK);
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.setColor(SUCCESS);
        g2.fillRoundRect(paddleX, paddleY, paddleW, paddleH, 20, 20);

        g2.setColor(new Color(GOLD_ACCENT.getRed(), GOLD_ACCENT.getGreen(), GOLD_ACCENT.getBlue(), 100));
        g2.fillOval(ballX - 10, ballY - 10, 40, 40);
        g2.setColor(GOLD_ACCENT);
        g2.fillOval(ballX, ballY, 20, 20);

        g2.setFont(new Font("Segoe UI", Font.BOLD, 22));
        g2.setColor(TEXT_PRIMARY);
        g2.drawString("🏆 Score: " + score, 20, 40);
        
        if (!gameRunning) {
            drawGameOver(g2);
        }
    }

    private void drawGameOver(Graphics2D g2) {
        g2.setColor(new Color(CARD_BG.getRed(), CARD_BG.getGreen(), CARD_BG.getBlue(), 220));
        g2.fillRoundRect(50, 150, getWidth() - 100, 200, 30, 30);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 36));
        g2.drawString("Game Over!", getWidth() / 2 - 110, 230);

        g2.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        g2.setColor(new Color(255, 255, 255, 200));
        g2.drawString("Final Score: " + score, getWidth() / 2 - 65, 270);
        g2.drawString("Press ENTER to Restart", getWidth() / 2 - 115, 310);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameRunning) {
            if (moveLeft && paddleX > 10) paddleX -= 5;
            if (moveRight && paddleX < getWidth() - paddleW - 10) paddleX += 5;

            ballX += ballXDir;
            ballY += ballYDir;

            if (ballX < 5 || ballX > getWidth() - 25) ballXDir = -ballXDir;
            if (ballY < 5) ballYDir = -ballYDir;

            if (new Rectangle(ballX, ballY, 20, 20).intersects(new Rectangle(paddleX, paddleY, paddleW, paddleH))) {
                ballYDir = -ballYDir;
                score += 10;
            }

            if (ballY > getHeight()) gameRunning = false;
            repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_LEFT) moveLeft = true;
        if (code == KeyEvent.VK_RIGHT) moveRight = true;
        if (code == KeyEvent.VK_ENTER && !gameRunning) restartGame();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_LEFT) moveLeft = false;
        if (code == KeyEvent.VK_RIGHT) moveRight = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    private void restartGame() {
        ballX = 160; ballY = 100;
        ballXDir = 2; ballYDir = 3;
        paddleX = 200; score = 0;
        gameRunning = true;
        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("🏓 Ping Pong Game");
        PingPongGame game = new PingPongGame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(550, 550);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.add(game);
        frame.setVisible(true);
    }
}