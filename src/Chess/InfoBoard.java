package Chess;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import Utils.Move;

public class InfoBoard extends JFrame {
    static JScrollPane scrollPane1;
    static JScrollPane scrollPane2;

    public InfoBoard(String p1, String p2) {
        setTitle("Chess");
        setSize(400, 700);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
        setLocation(1000, 0); // Set the location to the top right corner

        setLayout(new GridLayout(2, 1)); // Use a 1x2 grid layout
        setVisible(true);

        JPanel mp1 = new JPanel();
        mp1.setLayout(new GridLayout(1, 2)); // 2 rows, 1 column for mp1

        JLabel p1_label = new JLabel(p1, JLabel.CENTER);
        p1_label.setHorizontalAlignment(SwingConstants.CENTER);
        p1_label.setFont(new Font("New Times Roman", Font.BOLD, 20));
        p1_label.setSize(200, 100);
        mp1.add(p1_label);

        JLabel p2_label = new JLabel(p2, JLabel.CENTER);
        p2_label.setHorizontalAlignment(SwingConstants.CENTER);
        p2_label.setFont(new Font("New Times Roman", Font.BOLD, 20));
        p2_label.setForeground(Color.WHITE);
        p2_label.setBackground(Color.BLACK);
        p2_label.setSize(200, 100);
        p2_label.setOpaque(true);

        mp1.add(p2_label);

        JPanel mp2 = new JPanel();
        mp2.setLayout(new GridLayout(1, 2));

        String[] data = {};

        JList<String> list1 = new JList<String>(data);
        scrollPane1 = new JScrollPane(list1);
        scrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        mp2.add(scrollPane1);

        JList<String> list2 = new JList<String>(data);
        scrollPane2 = new JScrollPane(list2);
        mp2.add(scrollPane2);
        scrollPane1.getVerticalScrollBar().setModel(scrollPane2.getVerticalScrollBar().getModel());

        add(mp1);
        add(mp2);

    }

    public void UpdateMoves(List<Move> moves) {
        DefaultListModel<String> listModel1 = new DefaultListModel<String>();
        DefaultListModel<String> listModel2 = new DefaultListModel<String>();

        for (int i = 0; i < moves.size(); i++) {
            Move move = moves.get(i);
            String moveString = move.getChessNotation();

            if (i % 2 == 0) {
                listModel1.addElement(moveString);
            } else {
                listModel2.addElement(moveString);
            }
        }

        JList<String> list1 = new JList<>(listModel1);
        JList<String> list2 = new JList<>(listModel2);

        scrollPane1.setViewportView(list1);
        scrollPane2.setViewportView(list2);

        // Sync both scrollable lists together with one scrollable
        scrollPane1.getVerticalScrollBar().setModel(scrollPane2.getVerticalScrollBar().getModel());
    }
}
