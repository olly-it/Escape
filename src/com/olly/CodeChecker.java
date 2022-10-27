package com.olly;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

public class CodeChecker implements KeyListener {

    private JFrame frame;

    private static List<String> validPasswords = Arrays.asList("belinone45", "besugo007", "abbagascia2", "gabibbo123");
    private static String message = "Prendete i vostri PEG. Condivideteli brevemente e, seguendo le indicazioni che avete ricevuto, scegliete quello fatto meglio. Poi tornate dal gazebo (avete 30')";

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        System.out.println("CTRL + SHIFT + ESC for exit\n");
        
        try {
        	Properties p = new Properties();
        	InputStream config = null;
        	try {
        		config = new FileInputStream("config.properties");
        		p.load(config);
        		System.out.println("config.properties loaded from curr dir");
        	} catch (Exception e) {
            	System.out.println("problems loading config.properties from curr dir. try with classpath");
        		config = CodeChecker.class.getResourceAsStream("/config.properties");
        		p.load(config);
        		System.out.println("config.properties loaded from classpath");
        	}
        	config.close();
        	String passwords = p.getProperty("codes");
        	validPasswords = Arrays.asList(passwords.split(",")).stream().map(x->x.trim()).collect(Collectors.toList());
        	message = p.getProperty("message");
        } catch (Exception e) {
        	System.out.println("problems loading config.properties, using default values");
        	System.out.println(e);
        }
        	
        try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
		}
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    CodeChecker window = new CodeChecker();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public CodeChecker() {
        initialize();
    }

    static GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
    private JTextField textField;
    private JTextPane outputLabel;
    private JLabel imageLabel;
    private JPanel panel2;
    private JLabel leftL;
    private JLabel rightL;

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        // image
        /*try {
            backgroundImage = ImageIO.read(this.getClass().getResourceAsStream("./bg.jpg"));
        } catch (IOException e) {
            System.err.println(e);
        }
        
        frame = new JFrame() {
            @Override
            public void paintAll(Graphics g) {
                super.paintAll(g);
        
                int fw = this.getWidth();
                int fh = this.getHeight();
                int iw = backgroundImage.getWidth();
        
                int ih = backgroundImage.getHeight();
        
                Image scaledInstance = backgroundImage.getScaledInstance(fw, fh, 1);
        
                // g.drawImage(scaledInstance, fw / 2 - iw / 2, fh / 2 - ih / 2, this);
                g.drawImage(scaledInstance, 0, 0, this);
            }
        };*/

        frame = new JFrame();
        frame.setAlwaysOnTop(true);
        frame.getContentPane().setBackground(Color.BLACK);

        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        panel.setBackground(Color.BLACK);
        frame.getContentPane().add(panel, BorderLayout.NORTH);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        textField = new JTextField();
        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ok();
            }
        });
        URL imgURL = getClass().getResource("lock.jpg");
        ImageIcon ii = new ImageIcon(imgURL);
        textField.setFont(new Font("Lucida Grande", Font.PLAIN, 30));
        panel.add(textField);
        textField.setColumns(10);

        JLabel label = new JLabel(" ");
        label.setFont(new Font("Lucida Grande", Font.PLAIN, 80));
        panel.add(label);

        JButton btnNewButton = new JButton("OK");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ok();
            }
        });
        btnNewButton.setFont(new Font("Lucida Grande", Font.PLAIN, 30));
        panel.add(btnNewButton);

        outputLabel = new JTextPane();
        outputLabel.setMargin(new Insets(50, 50, 50, 50));
        outputLabel.setFocusable(false);
        outputLabel.setEditable(false);
        outputLabel.setForeground(Color.GREEN);
        outputLabel.setBackground(Color.DARK_GRAY);
        outputLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 50));
        frame.getContentPane().add(outputLabel, BorderLayout.CENTER);

        panel2 = new JPanel();
        panel2.setBackground(Color.BLACK);
        frame.getContentPane().add(panel2, BorderLayout.SOUTH);
        panel2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        imageLabel = new JLabel("");
        imageLabel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel2.add(imageLabel);
        imageLabel.setIcon(ii);

        leftL = new JLabel("   ");
        leftL.setFont(new Font("Lucida Grande", Font.PLAIN, 50));
        frame.getContentPane().add(leftL, BorderLayout.WEST);

        rightL = new JLabel("   ");
        rightL.setFont(new Font("Lucida Grande", Font.PLAIN, 50));
        frame.getContentPane().add(rightL, BorderLayout.EAST);

        // my stuff
        textField.addKeyListener(this);
        btnNewButton.addKeyListener(this);
        frame.setUndecorated(true);

        reset();

        device.setFullScreenWindow(frame);
    }

    private void reset() {
        outputLabel.setText("enter the code");
        textField.setText("");
    }

    private void ok() {
        String pwd = textField.getText();
        if (validPasswords.contains(pwd.toLowerCase())) {
            outputLabel.setText("SOLVED\n" + message+"\n-> premere ESC per eliminare il msg <-");
            textField.setText("");
        } else {
            outputLabel.setText("WRONG PASSWORD...\nenter the code");
            textField.setText("");
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        int k = e.getKeyChar();
        int m = e.getModifiers();
        if (k == 27) {
            reset();
        }
        if (k == 27 && m == 3) {
            // ctrl+shift+esc
            System.exit(0);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
