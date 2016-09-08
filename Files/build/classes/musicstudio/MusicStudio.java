package musicstudio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Scanner;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;

public class MusicStudio extends JFrame implements ActionListener, Runnable {

    private static final long serialVersionUID = 1L;
    //Menu items
    private JMenuBar menuBar;
    private JMenu subMenu = new JMenu("Window Layout");
    private JMenu subMenu2 = new JMenu("Backgrounds");
    private JMenu tutorial = new JMenu("Tutorials");
    private JMenu fileMenu = new JMenu("File");
    private JMenu viewMenu = new JMenu("View");
    private JMenu helpMenu = new JMenu("Help");
    private JPopupMenu pop = new JPopupMenu();
    //Drop down items
    private JMenuItem contentItem = new JMenuItem("Content");
    private JMenuItem whiteItem = new JMenuItem("White Background");
    private JMenuItem blackItem = new JMenuItem("Black Background");
    private JMenuItem defaultItem = new JMenuItem("Default Background");
    private JMenuItem pianoItem = new JMenuItem("Show Piano");
    private JMenuItem layoutItem = new JMenuItem("Show Sequencer");
    private JMenuItem controlsItem = new JMenuItem("Show Instruments");
    private JMenuItem mixerItem = new JMenuItem("Show Mixer");
    private JMenuItem addIns, howto, remove, delete, four, exitItem;
    //Create frames
    JFrame pianoFrame = new JFrame("Piano");
    JFrame InsFrame = new JFrame("Instruments");
    //Arrays
    private ArrayList<String> notes = new ArrayList<String>(), notes2 = new ArrayList<String>(),
            ob = new ArrayList<String>(), sb = new ArrayList<String>();
    private ArrayList<JMenuItem> mi = new ArrayList<JMenuItem>(), mn = new ArrayList<JMenuItem>(),
            tu = new ArrayList<JMenuItem>();
    private Queue<String> sounds = new LinkedList<String>();
    //Buttons
    private JButton play = new JButton("Play");
    private JButton sizeButton = new JButton();
    private JButton sizeButton2 = new JButton("Return");
    private JButton playPiano = new JButton("Play Piano");
    private JButton pause = new JButton("Pause");
    private JButton stop = new JButton("Stop");
    private JButton erase = new JButton("Erase");
    private JLayeredPane panel;
    private JLayeredPane pianoPan = new JLayeredPane();
    private JButton[] maxWhiteKeys = new JButton[23];
    private JButton[] maxBlackKeys = new JButton[16];
    private JSlider vol = new JSlider();
    private JTable table;
    private int total = 0, count = 0, totalcount = 0, rc = 0, pc = 0, newX = 10, oldX = 10;
    private String fullext;
    private int xWhite = 0, wTotal = 0, xBlack = 30, bTotal = 0;
    private int k, j;
    //Midi
    Synthesizer synth;
    MidiChannel[] channels;
    Sequencer sequencer;
    //Call other classes
    PlayClip c = new PlayClip();
    MyFrames m = new MyFrames();
    static Tutorial t = new Tutorial();

    public MusicStudio() {
        super.setTitle("Music Studio");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        buildMenuBar();
        setBackground(Color.DARK_GRAY.darker().darker().darker());
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setVisible(true);
        play.setBounds(580, 0, 60, 30);
        add(play);
        stop.setBounds(630, 0, 60, 30);
        add(stop);
        pause.setBounds(680, 0, 60, 30);
        pause.setForeground(Color.RED);
        add(pause);
        erase.setBounds(730, 0, 60, 30);
        add(erase);
        playPiano.setBounds(780, 0, 100, 30);
        add(playPiano);
        sizeButton.setBounds(10, 0, 60, 30);
        sizeButton.setIcon(new ImageIcon("piano.png"));
        sizeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent sb) {
                sizePiano(0, 115);
            }
        });
        add(sizeButton);
        setLayout(null);
        vol = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        vol.setMajorTickSpacing(10);
        vol.setPaintTicks(true);
        add(vol);
        vol.setBounds(500, 5, 90, 20);
        //getContent(); 
        m.getSequencePanel();
        m.addLabel("");
        getInstruments();
        getPiano(630, 620, 1063, 230);
    }
    /*public MusicStudio(){
     super.setTitle("Music Studio");
     SpringLayout layout = new SpringLayout();
     setLayout(layout);
     setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
     buildMenuBar();
     setBackground(Color.DARK_GRAY.darker().darker().darker());
     this.setExtendedState(JFrame.MAXIMIZED_BOTH);
     setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
     setVisible(true);
     sizeButton.setIcon(new ImageIcon("piano.png"));
     sizeButton.addActionListener(new ActionListener(){
     public void actionPerformed(ActionEvent sb){
     sizePiano(0,115);
     }});
     add(sizeButton);  
     play.setSize(60,30);
     add(play);  
     stop.setSize(60,30);
     add(stop);    
     pause.setSize(60,30);
     pause.setForeground(Color.RED);
     add(pause);
     erase.setSize(60,30);
     add(erase);
     playPiano.setSize(60,30);
     add(playPiano);
     vol = new JSlider(JSlider.HORIZONTAL,0,100,50);
     vol.setMajorTickSpacing(10);
     vol.setPaintTicks(true);
     //add(vol);
     layout.putConstraint(SpringLayout.WEST, sizeButton, 10, SpringLayout.WEST,super.getContentPane());
     layout.putConstraint(SpringLayout.WEST, play, 100, SpringLayout.WEST,sizeButton);
     layout.putConstraint(SpringLayout.WEST, stop, 70, SpringLayout.WEST,play);
     layout.putConstraint(SpringLayout.WEST, pause, 70, SpringLayout.WEST,stop);
     layout.putConstraint(SpringLayout.WEST, erase, 70, SpringLayout.WEST,pause);
     layout.putConstraint(SpringLayout.WEST, playPiano, 70, SpringLayout.WEST,erase);
     //getContent(); 
     //m.getSequencePanel();
     //m.addLabel(""); 
     //getInstruments(); 
     getPiano(630, 620,1063,230);
     }*/

    //Build menu bar

    private JMenuBar buildMenuBar() {
        menuBar = new JMenuBar();
        buildFileMenu();
        buildViewMenu();
        buildHelpMenu();
        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
        remove = new JMenuItem("Remove");
        four = new JMenuItem("Four Measures");
        pop.add(remove);
        pop.add(four);
        return menuBar;
    }

    //file menu 

    private JMenu buildFileMenu() {
        exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ex) {
                System.exit(0);
            }
        });
        fileMenu.add(exitItem);
        return fileMenu;
    }

    //view menu

    private JMenu buildViewMenu() {
        mi.add(whiteItem);
        mi.add(blackItem);
        mi.add(defaultItem);
        mn.add(pianoItem);
        mn.add(layoutItem);
        mn.add(mixerItem);
        mn.add(controlsItem);
        for (int i = 0; i < mi.size(); i++) {
            subMenu2.add(mi.get(i));
            mi.get(i).addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent men) {
                    if (men.getActionCommand().equalsIgnoreCase("white background")) {
                        setBackground(Color.WHITE);
                    }
                    if (men.getActionCommand().equalsIgnoreCase("black background")) {
                        setBackground(Color.BLACK);
                    }
                    if (men.getActionCommand().equalsIgnoreCase("default background")) {
                        setBackground(Color.DARK_GRAY.darker().darker());
                    }
                }
            });
        }
        for (int i = 0; i < mn.size(); i++) {
            subMenu.add(mn.get(i));
            mn.get(i).addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent menn) {
                    if (menn.getActionCommand().equalsIgnoreCase("show piano")) {
                        getPiano(630, 620, 1063, 230);
                    }
                    if (menn.getActionCommand().equalsIgnoreCase("show mixer")) {
                        m.getMixer();
                    }
                    if (menn.getActionCommand().equalsIgnoreCase("show sequencer")) {
                        m.getSequencePanel();
                    }
                    if (menn.getActionCommand().equalsIgnoreCase("show instruments")) {
                        getInstruments();
                    }
                }
            });
        }
        viewMenu.add(subMenu2);
        viewMenu.add(subMenu);
        return viewMenu;
    }

    //builds a help menu

    private JMenu buildHelpMenu() {
        addIns = new JMenuItem("Add Instrument");
        delete = new JMenuItem("Delete Instrument");
        howto = new JMenuItem("How To:");
        tutorial.add(howto);
        tutorial.addSeparator();
        tu.add(addIns);
        tu.add(delete);
        contentItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent cont) {
                m.getContent();
            }
        });
        for (int i = 0; i < tu.size(); i++) {
            tutorial.add(tu.get(i));
            tu.get(i).addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent tu) {
                    if (tu.getActionCommand().equalsIgnoreCase("add instrument")) {
                        t.addInstrument();
                    }
                    if (tu.getActionCommand().equalsIgnoreCase("delete instrument")) {
                        t.removeInstrument();
                    }
                }
            });
        }
        //Add items to help menu
        helpMenu.add(tutorial);
        helpMenu.add(contentItem);
        return helpMenu;
    }

    //display content

    private JFrame getContent() {
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
                m.getSequencePanel();
                m.addLabel("");
                getInstruments();
                getPiano(630, 620, 1063, 230);
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
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        inputFile.close();
        frame.setBackground(Color.gray.darker().darker().darker());
        frame.setVisible(true);
        return frame;
    }

    //display piano method

    private JFrame getPiano(int x, int y, int xx, int yy) {
        panel = new JLayeredPane();
        pianoFrame.setBackground(Color.gray.darker());
        pianoFrame.setLocation(x, y);
        pianoFrame.setSize(new Dimension(xx, yy));
        pianoFrame.setResizable(false);
        //White keys created
        for (int i = 0; i < maxWhiteKeys.length; i++) {
            maxWhiteKeys[i] = new JButton();
            maxWhiteKeys[i].addActionListener(this);
            maxWhiteKeys[i].setBackground(Color.WHITE);
            maxWhiteKeys[i].setLocation(xWhite, 0);
            maxWhiteKeys[i].setSize(50, 210);
            panel.add(maxWhiteKeys[i], 0, -1);
            wTotal = xWhite + 46;
            xWhite = wTotal;
        }
        //Black keys created
        for (int i = 0; i < maxBlackKeys.length; i++) {
            maxBlackKeys[i] = new JButton();
            maxBlackKeys[i].addActionListener(this);
            maxBlackKeys[i].setBackground(Color.BLACK);
            maxBlackKeys[i].setOpaque(true);
            maxBlackKeys[i].setBorderPainted(false);
            maxBlackKeys[i].setLocation(xBlack, 0);
            maxBlackKeys[i].setSize(40, 150);
            panel.add(maxBlackKeys[i], 1, -1);
            bTotal = xBlack + 50;
            xBlack = bTotal;
            if (i == 1 || i == 4 || i == 6 || i == 9 || i == 11 || i == 14) {
                if (i == 1) {
                    xBlack = 170;
                    panel.add(maxBlackKeys[i], 1, -1);
                    continue;
                }
                if (i == 4) {
                    xBlack = 350;
                    panel.add(maxBlackKeys[i], 1, -1);
                    continue;
                }
                if (i == 6) {
                    xBlack = 490;
                    panel.add(maxBlackKeys[i], 1, -1);
                    continue;
                }
                if (i == 9) {
                    xBlack = 675;
                    panel.add(maxBlackKeys[i], 1, -1);
                    continue;
                }
                if (i == 11) {
                    xBlack = 815;
                    panel.add(maxBlackKeys[i], 1, -1);
                    continue;
                }
                if (i == 14) {
                    xBlack = 995;
                    panel.add(maxBlackKeys[i], 1, -1);
                    continue;
                }
            }
        }
        pianoFrame.add(panel);
        pianoFrame.setAlwaysOnTop(true);
        pianoFrame.setVisible(true);
        Keys();
        Note();
        return pianoFrame;
    }

    //note press display button

    public void Note() {
        pianoPan.setLocation(0, 210);
        pianoPan.setSize(new Dimension(1440, 530));
        pianoPan.setLayout(null);
        pianoFrame.add(pianoPan);
        for (int i = 0; i < maxWhiteKeys.length; i++) {
            maxWhiteKeys[i].addMouseListener(new MouseListener() {
                public void mousePressed(MouseEvent e) {
                    JButton pb = new JButton();
                    pb.setSize(35, 20);
                    pc++;
                    if (pc == 1) {
                        pb.setLocation(oldX, 400);
                    } else {
                        oldX = newX + 40;
                        newX = oldX;
                        pb.setLocation(newX, 400);
                    }
                    pianoPan.add(pb);
                    pianoPan.repaint();
                }

                public void mouseClicked(MouseEvent arg0) {
                }

                public void mouseEntered(MouseEvent arg0) {
                }

                public void mouseExited(MouseEvent arg0) {
                }

                public void mouseReleased(MouseEvent arg0) {
                }
            });
        }
    }

    //resize piano method

    public void sizePiano(int j, int k) {
        pianoFrame.setSize(1440, 735);
        pianoFrame.setLocation(j, k);
        for (int i = 0; i < maxWhiteKeys.length; i++) {
            maxWhiteKeys[i].setLocation(0, xWhite);
            maxWhiteKeys[i].setSize(210, 50);
            wTotal = xWhite - 46;
            xWhite = wTotal;
        }
        pianoFrame.setLayout(null);
        sizeButton2.setBounds(1360, 0, 80, 30);
        sizeButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent sb2) {
                pianoFrame.setSize(new Dimension(1063, 230));
                pianoFrame.setLocation(380, 620);
            }
        });
        pianoFrame.add(sizeButton2);
    }

    //key press method

    public void Keys() {
        String name = "", name2 = "";
        //store piano sounds in array
        for (int i = 1; i < 24; i++) {
            name = i + ".au";
            notes.add(name);
        }
        for (char c = 'a'; c <= 'p'; c++) {
            name2 = c + ".au";
            notes2.add(name2);
        }
        for (int i = 0; i < maxWhiteKeys.length; i++) {
            maxWhiteKeys[i].addKeyListener(new KeyListener() {
                public void keyPressed(KeyEvent e) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_A:
                            c.playSound(notes.get(0));
                            break;
                        case KeyEvent.VK_S:
                            c.playSound(notes.get(1));
                            break;
                        case KeyEvent.VK_D:
                            c.playSound(notes.get(2));
                            break;
                        case KeyEvent.VK_F:
                            c.playSound(notes.get(3));
                            break;
                        case KeyEvent.VK_G:
                            c.playSound(notes.get(4));
                            break;
                        case KeyEvent.VK_H:
                            c.playSound(notes.get(5));
                            break;
                        case KeyEvent.VK_J:
                            c.playSound(notes.get(6));
                            break;
                        case KeyEvent.VK_K:
                            c.playSound(notes.get(7));
                            break;
                        case KeyEvent.VK_L:
                            c.playSound(notes.get(8));
                            break;
                    }
                }

                public void keyReleased(KeyEvent arg0) {
                }

                public void keyTyped(KeyEvent arg0) {
                }
            });
        }
    }

    //play audio for piano

    public void actionPerformed(final ActionEvent e) {
        for (int i = 0; i < maxWhiteKeys.length; i++) {
            if (e.getSource() == maxWhiteKeys[i]) {
                totalcount++;
                c.playSound(notes.get(i));
                ob.add(notes.get(i).toString());
                playPiano.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent es) {
                        for (int w = 0; w / totalcount < ob.size(); w++) {
                            c.playPiano(ob.get(w).toString());
                        }
                    }
                });
            }
        }
        for (int x = 0; x < maxBlackKeys.length; x++) {
            if (e.getSource() == maxBlackKeys[x]) {
                c.playSound(notes2.get(x));
                ob.add(notes2.get(x).toString());
            }
        }
        erase.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent er) {
                ob.clear();
            }
        });
    }

    //get instruments

    private JFrame getInstruments() throws NoSuchElementException {
        setLayout(new BorderLayout());
        String names[] = {"Kicks", "Hats", "Claps", "Snares", "Toms", "Basses", "Strings"};
        Object[][] data = {{"Kick 1", "Hi-Hat 1", "Clap 1", "Snare 1", "Tom 1", "Bass 1", "String 1"},
        {"Kick 2", "Hi-Hat 2", "Clap 2", "Snare 2", "Tom 2", "Bass 2", "String 2"}};
        table = new JTable(data, names) {
            private static final long serialVersionUID = 1L;

            public boolean isCellEditable(int d, int c) {
                return false;
            }
        }; //makes cell non edible
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //makes single selection available
        table.setCellSelectionEnabled(true); //sets cell selection enabled
        table.setFillsViewportHeight(true); //sets viewport height
        table.getTableHeader().setFont(new Font("SansSerif", Font.ITALIC, 13)); //sets font for header
        table.setFont(new Font("Arial", Font.PLAIN, 12)); //sets table font
        table.setGridColor(Color.gray); //color of grid
        JScrollPane scroll = new JScrollPane(table); //adds scroll bar
        InsFrame.add(scroll);
        scroll.setBackground(Color.gray);
        table.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                int row, col;
                if (e.getClickCount() == 2) {
                    m.addLabel("n"); //remove label
                    row = table.getSelectedRow(); //get row 
                    col = table.getSelectedColumn(); //get column
                    final JButton button = new JButton(table.getValueAt(row, col).toString()); //set name from cell text
                    int kb = button.getX(); //adds x location to variable
                    int ky = button.getY();//adds y location to variable
                    ky = total; //adds ky to the running total
                    total = ky;
                    if (row + col != 0) { //if cell does not equal cell 1
                        button.setLocation(kb, ky + 30);	 //set location relative to cell1 
                        button.setSize(90, 30);
                        button.addActionListener(new SoundActionListener());
                        button.setForeground(Color.blue);
                        m.seqFrame.add(button);
                        total = ky + 30;
                        ky = total;
                    } else {
                        button.setSize(90, 30);
                        button.addActionListener(new SoundActionListener());
                        button.setForeground(Color.blue);
                        m.seqFrame.add(button);
                    }
                    //create small butttons
                    final JButton beatsPS[] = new JButton[64];
                    int xbeats = 90;
                    for (int i = 0; i < beatsPS.length; i++) {
                        beatsPS[i] = new JButton();
                        beatsPS[i].setBackground(Color.CYAN.darker().darker().darker());
                        beatsPS[i].setOpaque(true);
                        beatsPS[i].setBorderPainted(false);
                        beatsPS[i].addMouseListener(new MouseAdapter() {
                            public void mouseClicked(MouseEvent bsp) {
                                for (int i = 0; i < beatsPS.length; i++) {
                                    if (bsp.getClickCount() == 1 && bsp.getSource() == beatsPS[i]) { //single clicked
                                        beatsPS[i].setBackground(Color.ORANGE);
                                        beatsPS[i].setOpaque(true);
                                        beatsPS[i].setBorderPainted(false);
                                        count++;
                                        String ext = ".au";
                                        final String fullext;
                                        final String buttonText = button.getText().toLowerCase().toString();
                                        fullext = buttonText.replaceAll("\\s", "").concat(ext).replaceAll("\\s", "");
                                        sounds.add(fullext);
                                    }
                                    if (bsp.getClickCount() == 2 && bsp.getSource() == beatsPS[i] //double clicked
                                            && i / 4 != 0 && i / 4 != 2
                                            && i / 4 != 4 && i / 4 != 6
                                            && i / 4 != 8 && i / 4 != 10 && i / 4 != 12 && i / 4 != 14) {
                                        beatsPS[i].setBackground(Color.CYAN.darker().darker().darker());
                                        count--;
                                    } else if (bsp.getClickCount() == 2 && bsp.getSource() == beatsPS[i]) {
                                        beatsPS[i].setBackground(Color.gray);
                                    }
                                }
                                play.addActionListener(new ActionListener() {
                                    public void actionPerformed(ActionEvent pla) {
                                        Iterator<String> it = sounds.iterator();
                                        while (it.hasNext()) {
                                            c.playAudio(it.next());
                                        }
                                    }
                                });
                            }
                        });
                        if (table.getValueAt(row, col).toString().equals("Kick 1")) {
                            ky = 0;
                        }
                        kb = xbeats;
                        beatsPS[i].setBounds(kb, ky + 8, 15, 15); //size and location of small buttons
                        kb = xbeats + 20;
                        xbeats = kb;
                        m.seqFrame.add(beatsPS[i]); //add small buttons to frame
                        if (i / 4 == 0 || i / 4 == 2 || i / 4 == 4 || i / 4 == 6 || i / 4 == 8 //create gray small buttons
                                || i / 4 == 10 || i / 4 == 12 || i / 4 == 14) {
                            beatsPS[i].setBackground(Color.GRAY);
                            beatsPS[i].setOpaque(true);
                            beatsPS[i].setBorderPainted(false);
                        }
                        button.addMouseListener(new MouseAdapter() {
                            public void mouseClicked(MouseEvent e) {
                                if (SwingUtilities.isRightMouseButton(e)) {
                                    button.setComponentPopupMenu(pop); //gets pop up menu 
                                    remove.addActionListener(new ActionListener() {
                                        public void actionPerformed(ActionEvent rem) {
                                            String ext = ".au";
                                            String buttonText = button.getText().toLowerCase().toString();
                                            String fullext = buttonText.replaceAll("\\s", "").concat(ext).replaceAll("\\s", "");
                                            for (int i = 0; i < sb.size(); i++) {
                                                sb.remove(fullext);
                                            }
                                            //count = 0;
                                            m.seqFrame.remove(button);
                                            m.seqFrame.repaint();
                                            for (int v = 0; v < beatsPS.length; v++) { //loop to remove buttons
                                                m.seqFrame.remove(beatsPS[v]);
                                                m.seqFrame.repaint();
                                            }
                                        }
                                    });
                                    four.addActionListener(new ActionListener() {
                                        public void actionPerformed(ActionEvent four) {
                                            count = 16; //count equals 16
                                            for (int v = 0; v < beatsPS.length; v++) { //loop to change color buttons
                                                if (v % 4 == 0) {
                                                    beatsPS[v].setBackground(Color.ORANGE);
                                                    beatsPS[v].setOpaque(true);
                                                    beatsPS[v].setBorderPainted(false);
                                                }
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            }

            public void mouseEntered(MouseEvent arg0) {
            }

            public void mouseExited(MouseEvent arg0) {
            }

            public void mousePressed(MouseEvent arg0) {
            }

            public void mouseReleased(MouseEvent arg0) {
            }
        });
        InsFrame.setResizable(false);
        InsFrame.setBounds(0, 620, 380, 230);
        InsFrame.setAlwaysOnTop(true);
        InsFrame.setVisible(true);
        return InsFrame;
    }

    //play instrument

    private class SoundActionListener implements ActionListener {

        public void actionPerformed(ActionEvent sound) {
            String ext = ".au", fullext;
            String buttonText = ((JButton) sound.getSource()).getText().toLowerCase();
            fullext = buttonText.replaceAll("\\s", "").concat(ext).replaceAll("\\s", "");
            c.playSound(fullext);
        }
    }

    public void run() {
    }

    //main method

    public static void main(String[] args) {
        new MusicStudio();
    }
}
