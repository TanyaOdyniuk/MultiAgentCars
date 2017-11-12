package multiagentsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AddRoadGui extends JFrame {
    private static final long serialVersionUID = 1L;

    private EnvironmentAgent myAgent;

    private JTextField startField, endField, distanceField;

    public AddRoadGui(EnvironmentAgent a) {
        super(a.getLocalName());

        myAgent = a;

        JPanel p = new JPanel();
        p.setLayout(new GridLayout(2, 2));
        p.add(new JLabel("multiagentsystem.Road start point:"));
        startField = new JTextField(15);
        p.add(startField);
        p.add(new JLabel("multiagentsystem.Road start point:"));
        endField = new JTextField(15);
        p.add(endField);
        p.add(new JLabel("multiagentsystem.Road distance:"));
        distanceField = new JTextField(15);
        p.add(distanceField);

        getContentPane().add(p, BorderLayout.CENTER);

        JButton addButton = new JButton("Add");
        addButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                try {
                    Integer startPoint = Integer.parseInt(startField.getText().trim());
                    Integer endPoint = Integer.parseInt(endField.getText().trim());
                    Integer distance = Integer.parseInt(distanceField.getText().trim());
                    myAgent.addRoad(startPoint, endPoint, distance);
                    startField.setText("");
                    endField.setText("");
                    distanceField.setText("");
                }
                catch (Exception e) {
                    JOptionPane.showMessageDialog(AddRoadGui.this, "Invalid values. "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } );
        p = new JPanel();
        p.add(addButton);
        getContentPane().add(p, BorderLayout.SOUTH);

        // Make the agent terminate when the user closes
        // the GUI using the button on the upper right corner
        addWindowListener(new	WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                myAgent.doDelete();
            }
        } );

        setResizable(false);
    }

    public void showGui() {
        pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (int)screenSize.getWidth() / 2;
        int centerY = (int)screenSize.getHeight() / 2;
        setLocation(centerX - getWidth() / 2, centerY - getHeight() / 2);
        super.setVisible(true);
    }
}

