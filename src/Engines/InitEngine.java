package Engines;

import Chess.ChessBoard;
import Chess.InfoBoard;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class InitEngine extends JFrame implements ActionListener {
    JButton startButton;

    public InitEngine() {
        setTitle("Start a Chess Game");
        ImageIcon logo = new ImageIcon("Resources/Icons/logo-b.png");
        setIconImage(logo.getImage());
        setSize(600, 400);
        setResizable(false);
        getContentPane().setBackground(new Color(27, 27, 27));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        JLabel mainHeading = new JLabel("Chess", JLabel.CENTER);
        mainHeading.setForeground(Color.WHITE);
        mainHeading.setFont(new Font("OLD ENGLISH TEXT MT", Font.BOLD, 55));
        mainHeading.setBounds(210, 60, 160, 39);
        add(mainHeading);

        String[] menuItems = { "Human", "Magnus (AI)" };

        JComboBox<String> dropdown_w = new JComboBox<>(menuItems);
        dropdown_w.setBounds(80, 185, 100, 30);

        ImageIcon versus = new ImageIcon("Resources/Icons/vs.png");
        JLabel versusLabel = new JLabel(versus);
        versusLabel.setBounds(180, 120, versus.getIconWidth(), versus.getIconHeight());
        add(versusLabel);

        JComboBox<String> dropdown_b = new JComboBox<>(menuItems);
        dropdown_b.setBounds(410, 185, 100, 30);

        add(dropdown_w);
        add(dropdown_b);

        JLabel whiteLabel = new JLabel("White");
        whiteLabel.setForeground(Color.WHITE);
        whiteLabel.setBounds(110, 225, 100, 30);
        add(whiteLabel);

        JLabel blackLabel = new JLabel("Black");
        blackLabel.setForeground(Color.WHITE);
        blackLabel.setBounds(440, 225, 100, 30);
        add(blackLabel);

        startButton = new JButton("Start");
        startButton.setBounds(245, 295, 100, 30);
        startButton.setFocusPainted(false);
        startButton.setBackground(new Color(255, 255, 255));
        startButton.setForeground(new Color(27, 27, 27));
        startButton.setFont(new Font("Arial", Font.BOLD, 14));
        startButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(27, 27, 27), 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        startButton.setOpaque(true);
        startButton.setBorderPainted(false);
        startButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                startButton.setBackground(new Color(27, 27, 27));
                startButton.setForeground(new Color(255, 255, 255));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                startButton.setBackground(new Color(255, 255, 255));
                startButton.setForeground(new Color(27, 27, 27));
            }
        });
        startButton.addActionListener(this);
        add(startButton);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            boolean p1 = false, p2 = false;

            String whitePlayer = (String) ((JComboBox<?>) getContentPane().getComponent(2)).getSelectedItem();
            String blackPlayer = (String) ((JComboBox<?>) getContentPane().getComponent(3)).getSelectedItem();
            if (whitePlayer.length() == 5 && whitePlayer.substring(0, 5).equals("Human")) {
                p1 = true;
            }
            if (blackPlayer.length() == 5 && blackPlayer.substring(0, 5).equals("Human")) {
                p2 = true;
            }
            InfoBoard info = null;
            if (p1 && p2) {
                String p1Name = JOptionPane.showInputDialog(this, "Enter name of Player 1:");
                while (p1Name == null || p1Name.trim().isEmpty()) {
                    p1Name = JOptionPane.showInputDialog(this, "Please enter a valid name for Player 1:");
                }
                String p2Name = JOptionPane.showInputDialog(this, "Enter name of Player 2:");
                while (p2Name == null || p2Name.trim().isEmpty()) {
                    p2Name = JOptionPane.showInputDialog(this, "Please enter a valid name for Player 2:");
                }
                info = new InfoBoard(p1Name, p2Name);
            } else if (p1) {
                String p1Name = JOptionPane.showInputDialog(this, "Enter name of Player 1:");
                while (p1Name == null || p1Name.trim().isEmpty()) {
                    p1Name = JOptionPane.showInputDialog(this, "Please enter a valid name for Player 1:");
                }
                info = new InfoBoard(p1Name, blackPlayer);
            } else if (p2) {
                String p2Name = JOptionPane.showInputDialog(this, "Enter name of Player 2:");
                while (p2Name == null || p2Name.trim().isEmpty()) {
                    p2Name = JOptionPane.showInputDialog(this, "Please enter a valid name for Player 2:");
                }
                info = new InfoBoard(whitePlayer, p2Name);
            } else {
                info = new InfoBoard(whitePlayer, blackPlayer);
            }

            new ChessBoard(p1, p2, info, whitePlayer, blackPlayer);
            dispose();
        }
    }
}
