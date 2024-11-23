import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;

public class LoginPanel {

    private static String accessToken = null;

    public static void main(String[] args) {
        // Create the main JFrame
        JFrame frame = new JFrame("Login Panel");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        // Create the login button
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.PLAIN, 16));
        frame.add(loginButton, BorderLayout.CENTER);

        // Add action listener for the login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (accessToken == null) {
                    try {
                        // Simulate generating a login link
                        String loginLink = LoginClass.getLoginLink();

                        // Open the link in the default browser
                        Desktop.getDesktop().browse(URI.create(loginLink));
                        JOptionPane.showMessageDialog(frame, "Please login using the opened link.");

                        // Simulate token retrieval
                        accessToken = LoginClass.getToken(); // Replace with real token retrieval logic

                        // Create and add the "Continue" button dynamically
                        JButton continueButton = new JButton("Continue");
                        continueButton.setFont(new Font("Arial", Font.PLAIN, 16));
                        frame.getContentPane().removeAll(); // Clear the frame
                        frame.getContentPane().add(continueButton, BorderLayout.CENTER); // Add continue button
                        frame.revalidate(); // Refresh the frame
                        frame.repaint(); // Redraw the frame

                        // Add action listener for the continue button
                        continueButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                frame.dispose(); // Close the login panel
                                launchSecondPanel(); // Show the second panel
                            }
                        });
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(frame, "Failed to open browser.", "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        // Make the frame visible
        frame.setVisible(true);
    }

    // Method to launch the second panel
    private static void launchSecondPanel() {
        // Create a new JFrame for the second panel
        JFrame secondFrame = new JFrame("Options Panel");
        secondFrame.setSize(300, 200);
        secondFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        secondFrame.setLayout(new FlowLayout());
        secondFrame.setLocationRelativeTo(null);

        // Create buttons for options
        JButton opt1Button = new JButton("Opt1");
        JButton opt2Button = new JButton("Opt2");

        // Add action listeners to the buttons
        opt1Button.addActionListener(e -> JOptionPane.showMessageDialog(secondFrame, "You selected Option 1"));
        opt2Button.addActionListener(e -> JOptionPane.showMessageDialog(secondFrame, "You selected Option 2"));

        // Add buttons to the frame
        secondFrame.add(opt1Button);
        secondFrame.add(opt2Button);

        // Make the second frame visible
        secondFrame.setVisible(true);
    }
}