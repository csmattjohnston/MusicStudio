package musicstudio;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MyFrames {

    JFrame seqFrame = new JFrame("Sequencer");
    JFrame mixFrame = new JFrame("Mixer");
    JLabel label = new JLabel("Add Instruments Here");

    //sequence window

    public JFrame getSequencePanel() {
        seqFrame.setLayout(null);
        seqFrame.setBackground(Color.BLUE.darker().darker().darker().darker());
        seqFrame.setBounds(0, 320, 1439, 300);
        seqFrame.setResizable(false);
        seqFrame.setAlwaysOnTop(true);
        seqFrame.setVisible(true);
        return seqFrame;
    }

    //add label to sequencer window

    public JLabel addLabel(String s) {
        if (s.equalsIgnoreCase("")) {
            label.setForeground(Color.WHITE);
            label.setFont(new Font("SansSerif", Font.ITALIC, 14));
            label.setBounds(650, 100, 200, 50);
            seqFrame.add(label);
        } else {
            seqFrame.remove(label);
            seqFrame.repaint();
        }
        return label;
    }

    //content window

    public JFrame getContent() {
        Scanner inputFile = null;
        String inputLine = "";
        File file = new File("Content.txt");
        try {
            inputFile = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.getMessage();
        }
        final JFrame frame = new JFrame();
        frame.setTitle("Content");
        frame.setBounds(450, 0, 510, 400);
        frame.setLayout(new BorderLayout());
        frame.setAlwaysOnTop(true);
        JPanel panel = new JPanel();
        JButton button = new JButton("Close");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        while (inputFile.hasNext()) {
            inputLine = inputFile.nextLine();
            JLabel label = new JLabel(inputLine);
            label.setForeground(Color.WHITE.darker());
            panel.add(label, BorderLayout.CENTER);
        }
        panel.add(button);
        frame.add(panel);
        frame.setResizable(false);
        inputFile.close();
        frame.setBackground(Color.gray.darker().darker().darker());
        frame.setVisible(true);
        return frame;
    }

    //mixer window

    public JFrame getMixer() {
        mixFrame.setBackground(Color.GRAY.darker().darker().darker());
        mixFrame.setBounds(1128, 67, 310, 252);
        mixFrame.setResizable(false);
        mixFrame.setAlwaysOnTop(true);
        mixFrame.setVisible(true);
        return mixFrame;
    }
}
