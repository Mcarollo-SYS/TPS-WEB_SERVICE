package web_service;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import web_service.utils.Client;

public class App {
    private static Client client;
    private static JTextArea resultArea;

    public static void main(String[] args) {
        // Creazione del client
        client = new Client("http://localhost/web-service/API.php");

        // Creazione della finestra
        JFrame frame = new JFrame("Concessionario Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        // Area di testo per i risultati
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Pannello per i pulsanti e gli input
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(8, 1, 5, 5));

        // Pulsanti per le operazioni GET
        JButton getCarsButton = new JButton("Get Cars");
        JButton getBrandsButton = new JButton("Get Brands");
        JButton getCustomersButton = new JButton("Get Customers");
        JButton getCarsWithDetailsButton = new JButton("Get Cars and Details");

        // Componenti per Create Car
        JPanel createPanel = new JPanel(new FlowLayout());
        JTextField createModelField = new JTextField(10);
        JTextField createBrandIdField = new JTextField(5);
        JTextField createYearField = new JTextField(5);
        JTextField createPriceField = new JTextField(5);
        JTextField createColorField = new JTextField(5);
        JButton createCarButton = new JButton("Create Car");
        createPanel.add(new JLabel("Model:"));
        createPanel.add(createModelField);
        createPanel.add(new JLabel("Brand ID:"));
        createPanel.add(createBrandIdField);
        createPanel.add(new JLabel("Year:"));
        createPanel.add(createYearField);
        createPanel.add(new JLabel("Price:"));
        createPanel.add(createPriceField);
        createPanel.add(new JLabel("Color:"));
        createPanel.add(createColorField);
        createPanel.add(createCarButton);

        // Componenti per Create Customer
        JPanel createCustomerPanel = new JPanel(new FlowLayout());
        JTextField createFirstNameField = new JTextField(10);
        JTextField createLastNameField = new JTextField(10);
        JTextField createCarIdField = new JTextField(5);
        JTextField createEmailField = new JTextField(10);
        JButton createCustomerButton = new JButton("Create Customer");
        createCustomerPanel.add(new JLabel("First Name:"));
        createCustomerPanel.add(createFirstNameField);
        createCustomerPanel.add(new JLabel("Last Name:"));
        createCustomerPanel.add(createLastNameField);
        createCustomerPanel.add(new JLabel("Car ID:"));
        createCustomerPanel.add(createCarIdField);
        createCustomerPanel.add(new JLabel("Email:"));
        createCustomerPanel.add(createEmailField);
        createCustomerPanel.add(createCustomerButton);

        // Componenti per Update Car
        JPanel updatePanel = new JPanel(new FlowLayout());
        JTextField updateIdField = new JTextField(5);
        JTextField updateModelField = new JTextField(10);
        JTextField updatePriceField = new JTextField(5);
        JTextField updateColorField = new JTextField(5);
        JButton updateCarButton = new JButton("Update Car");
        updatePanel.add(new JLabel("ID:"));
        updatePanel.add(updateIdField);
        updatePanel.add(new JLabel("Model:"));
        updatePanel.add(updateModelField);
        updatePanel.add(new JLabel("Price:"));
        updatePanel.add(updatePriceField);
        updatePanel.add(new JLabel("Color:"));
        updatePanel.add(updateColorField);
        updatePanel.add(updateCarButton);

        // Componenti per Delete Car
        JPanel deletePanel = new JPanel(new FlowLayout());
        JTextField deleteIdField = new JTextField(5);
        JButton deleteCarButton = new JButton("Delete Car");
        deletePanel.add(new JLabel("ID:"));
        deletePanel.add(deleteIdField);
        deletePanel.add(deleteCarButton);

        // Aggiunta dei pulsanti e pannelli al pannello principale
        buttonPanel.add(getCarsButton);
        buttonPanel.add(getBrandsButton);
        buttonPanel.add(getCustomersButton);
        buttonPanel.add(getCarsWithDetailsButton);
        buttonPanel.add(createPanel);
        buttonPanel.add(updatePanel);
        buttonPanel.add(deletePanel);
        buttonPanel.add(createCustomerPanel);

        frame.add(buttonPanel, BorderLayout.WEST);

        // Azioni dei pulsanti
        getCarsButton.addActionListener(e -> execute(() -> {
            try {
                client.getCars(); // Chiama e lascia che Client gestisca tutto
            } catch (Exception e1) {
                resultArea.setText("Errore: " + e1.getMessage());
                e1.printStackTrace();
            }
        }));

        getBrandsButton.addActionListener(e -> execute(() -> {
            try {
                client.getBrands();
            } catch (Exception e1) {
                resultArea.setText("Errore: " + e1.getMessage());
                e1.printStackTrace();
            }
        }));

        getCustomersButton.addActionListener(e -> execute(() -> {
            try {
                client.getCustomers();
            } catch (Exception e1) {
                resultArea.setText("Errore: " + e1.getMessage());
                e1.printStackTrace();
            }
        }));

        getCarsWithDetailsButton.addActionListener(e -> execute(() -> {
            try {
                client.getCarsWithDetails();
            } catch (Exception e1) {
                resultArea.setText("Errore: " + e1.getMessage());
                e1.printStackTrace();
            }
        }));

        createCarButton.addActionListener(e -> {
            try {
                String model = createModelField.getText();
                int brandId = Integer.parseInt(createBrandIdField.getText());
                int year = Integer.parseInt(createYearField.getText());
                double price = Double.parseDouble(createPriceField.getText());
                String color = createColorField.getText();
                execute(() -> {
                    try {
                        client.createCar(model, brandId, year, price, color);
                    } catch (Exception e1) {
                        resultArea.setText("Errore: " + e1.getMessage());
                        e1.printStackTrace();
                    }
                });
                clearFields(createModelField, createBrandIdField, createYearField, createPriceField, createColorField);
            } catch (NumberFormatException ex) {
                resultArea.setText("Inserisci valori numerici validi");
            }
        });
        createCustomerButton.addActionListener(e ->{
            try {
                String first_name = createFirstNameField.getText();
                String last_name = createLastNameField.getText();
                String email = createEmailField.getText();
                int car_id = Integer.parseInt(createCarIdField.getText());
                execute(() -> {
                    try {
                        client.createCustomer(first_name, last_name,email,car_id);
                    } catch (Exception e1) {
                        resultArea.setText("Errore: " + e1.getMessage());
                        e1.printStackTrace();
                    }
                });
                clearFields(createFirstNameField, createLastNameField);
            } catch (NumberFormatException ex) {
                resultArea.setText("Errore: Inserisci valori validi");
            }
        });

        updateCarButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(updateIdField.getText());
                String model = updateModelField.getText();
                double price = Double.parseDouble(updatePriceField.getText());
                String color = updateColorField.getText();
                execute(() -> {
                    try {
                        client.updateCar(id, model, price, color);
                    } catch (Exception e1) {
                        resultArea.setText("Errore: " + e1.getMessage());
                        e1.printStackTrace();
                    }
                });
                clearFields(updateIdField, updateModelField, updatePriceField, updateColorField);
            } catch (NumberFormatException ex) {
                resultArea.setText("Errore: Inserisci ID,Price validi");
            }
        });

        deleteCarButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(deleteIdField.getText());
                execute(() -> {
                    try {
                        client.deleteCar(id);
                    } catch (Exception e1) {
                        resultArea.setText("Errore: " + e1.getMessage());
                        e1.printStackTrace();
                    }
                });
                deleteIdField.setText("");
            } catch (NumberFormatException ex) {
                resultArea.setText("Errore: Inserisci ID numerico valido");
            }
        });

        frame.setVisible(true);
    }

    // Metodo per eseguire le operazioni e catturare l'output
    private static void execute(Runnable operation) {
        try {
            JTextAreaOutputStream outputStream = new JTextAreaOutputStream(resultArea);
            System.setOut(new java.io.PrintStream(outputStream));
            operation.run();
        } catch (Exception e) {
            resultArea.setText("Errore: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Metodo per pulire i campi di testo
    private static void clearFields(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }
}

class JTextAreaOutputStream extends java.io.OutputStream {
    private JTextArea textArea;

    public JTextAreaOutputStream(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void write(int b) {
        textArea.append(String.valueOf((char)b));
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
}