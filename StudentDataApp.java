import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StudentDataApp extends JFrame {
    private DefaultTableModel tableModel;
    private JTable studentTable;
    private JTextField idField, nameField, courseField, instructorField;

    // Store data in memory (simulate a database)
    private List<String[]> data = new ArrayList<>();

    public StudentDataApp() {
        // Set up the JFrame
        setTitle("Student Data Application");
        setSize(1000, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create the table model and table
        String[] columns = {"Student ID", "Student Name", "Course", "Instructor"};
        tableModel = new DefaultTableModel(columns, 0);
        studentTable = new JTable(tableModel);
        studentTable.setGridColor(Color.BLACK);
        studentTable.setShowGrid(true);
        studentTable.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Add the table to a scroll pane
        JScrollPane tableScrollPane = new JScrollPane(studentTable);

        // Create input fields and buttons
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Student ID:"));
        idField = new JTextField();
        inputPanel.add(idField);

        inputPanel.add(new JLabel("Student Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Course:"));
        courseField = new JTextField();
        inputPanel.add(courseField);

        inputPanel.add(new JLabel("Instructor:"));
        instructorField = new JTextField();
        inputPanel.add(instructorField);

        JButton addButton = new JButton("Add Data");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addData();
            }
        });
        inputPanel.add(addButton);

        JButton clearButton = new JButton("Clear Data by ID");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearData();
            }
        });
        inputPanel.add(clearButton);

        // Add normalization buttons
        JButton unfButton = new JButton("UNF");
        unfButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showUnnormalizedForm();
            }
        });
        inputPanel.add(unfButton);

        JButton firstNFButton = new JButton("1NF");
        firstNFButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFirstNormalForm();
            }
        });
        inputPanel.add(firstNFButton);

        JButton secondNFButton = new JButton("2NF");
        secondNFButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSecondNormalForm();
            }
        });
        inputPanel.add(secondNFButton);

        JButton thirdNFButton = new JButton("3NF");
        thirdNFButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showThirdNormalForm();
            }
        });
        inputPanel.add(thirdNFButton);

        // Add components to the JFrame
        add(tableScrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.EAST);
    }

    // Add data to the table and memory
    private void addData() {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String course = courseField.getText().trim();
        String instructor = instructorField.getText().trim();

        if (id.isEmpty() || name.isEmpty() || course.isEmpty() || instructor.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Add data to the table and memory
        tableModel.addRow(new Object[]{id, name, course, instructor});
        data.add(new String[]{id, name, course, instructor});

        // Clear input fields
        idField.setText("");
        nameField.setText("");
        courseField.setText("");
        instructorField.setText("");
    }

    // Clear data by Student ID
    private void clearData() {
        String id = idField.getText().trim();

        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Student ID to clear.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Search for the row with the given Student ID
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (tableModel.getValueAt(i, 0).equals(id)) {
                tableModel.removeRow(i); // Remove the row
                data.remove(i); // Remove from memory
                JOptionPane.showMessageDialog(this, "Data cleared for Student ID: " + id, "Success", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Student ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Show Unnormalized Form (UNF)
    private void showUnnormalizedForm() {
        tableModel.setRowCount(0); // Clear the table
        for (String[] row : data) {
            tableModel.addRow(row); // Add original data
        }
    }

    // Show First Normal Form (1NF)
    private void showFirstNormalForm() {
        tableModel.setRowCount(0); // Clear the table
        for (String[] row : data) {
            String[] courses = row[2].split(","); // Split courses
            String[] instructors = row[3].split(","); // Split instructors
            for (int i = 0; i < courses.length; i++) {
                tableModel.addRow(new String[]{row[0], row[1], courses[i].trim(), instructors[i].trim()});
            }
        }
    }

    // Show Second Normal Form (2NF)
    private void showSecondNormalForm() {
        tableModel.setRowCount(0); // Clear the table
        Set<String> uniqueCourses = new HashSet<>();
        for (String[] row : data) {
            String[] courses = row[2].split(","); // Split courses
            String[] instructors = row[3].split(","); // Split instructors
            for (int i = 0; i < courses.length; i++) {
                String course = courses[i].trim();
                if (!uniqueCourses.contains(course)) {
                    tableModel.addRow(new String[]{row[0], course, instructors[i].trim()});
                    uniqueCourses.add(course);
                }
            }
        }
    }

    // Show Third Normal Form (3NF)
    private void showThirdNormalForm() {
        tableModel.setRowCount(0); // Clear the table
        Set<String> uniqueCourses = new HashSet<>();
        for (String[] row : data) {
            String[] courses = row[2].split(","); // Split courses
            for (String course : courses) {
                if (!uniqueCourses.contains(course.trim())) {
                    tableModel.addRow(new String[]{row[0], course.trim()});
                    uniqueCourses.add(course.trim());
                }
            }
        }
    }

    public static void main(String[] args) {
        // Run the GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                StudentDataApp app = new StudentDataApp();
                app.setVisible(true);
            }
        });
    }
}