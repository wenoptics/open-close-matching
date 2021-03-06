import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Files;

public class InputGUI {
    private JPanel rootPanel;
    private JEditorPane previewArea;
    private JTextArea inputArea;
    private JTextArea msgArea;
    private JButton btnOpenFile;

    private Style styleNormal, styleError;

    void setGoodMessage(String msg) {
        msgArea.setForeground(Color.GRAY);
        msgArea.setFont(new Font("Arial", Font.ITALIC, 12));
        msgArea.setText(msg);
        msgArea.setBackground(Color.white);

    }
    void setErrorMessage(String msg) {
        msgArea.setForeground(Color.BLACK);
        msgArea.setFont(new Font("Arial", Font.BOLD, 12));
        msgArea.setText(msg);
        msgArea.setBackground(Color.pink);
    }

    public void setCodeDoc(String text, TextValidator.ValidationResult r) {
        StyledDocument doc = (StyledDocument) previewArea.getDocument();
        previewArea.setText("");
        setGoodMessage("No error, all good!");
        if (r == null) {
            try {
                doc.insertString(doc.getLength(), text, styleNormal);
            } catch (BadLocationException e) { e.printStackTrace(); }
            return;
        }

        if ( ! r.isGood) {
            setErrorMessage("Error: " + r.message);
        }

        // Append to document
        try {
            int lastP = 0;
            if (r.badStart != -1) {
                doc.insertString(doc.getLength(), text.substring(0, r.badStart), styleNormal);
                lastP = r.badStart + 1;
                doc.insertString(doc.getLength(), text.substring(r.badStart, lastP), styleError);
            }
            if (r.badEnd != -1) {
                doc.insertString(doc.getLength(), text.substring(lastP, r.badEnd), styleNormal);
                lastP = r.badEnd + 1;
                doc.insertString(doc.getLength(), text.substring(r.badEnd, lastP), styleError);
            }
            doc.insertString(doc.getLength(), text.substring(lastP), styleNormal);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public InputGUI() {

        StyledDocument doc = (StyledDocument) previewArea.getDocument();

        // Create a style object and then set the style attributes
        styleNormal = doc.addStyle("styleNormal", null);
        StyleConstants.setForeground(styleNormal, Color.black);
        StyleConstants.setFontFamily(styleNormal, "Monospace");
        styleError = doc.addStyle("styleError", styleNormal);
        StyleConstants.setBackground(styleError, Color.pink);
        StyleConstants.setBold(styleError, true);

        inputArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                validate();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                validate();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                validate();
            }
        });

        btnOpenFile.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //Create a file chooser
                final JFileChooser fc = new JFileChooser();
                //In response to a button click:
                int returnVal = fc.showOpenDialog(InputGUI.this.rootPanel);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    //This is where a real application would open the file.
                    System.out.println("Opening: " + file.getAbsolutePath());
                    try {
                        inputArea.setText(Helper.readFileAsString(file.getAbsolutePath()));
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    validate();
                }
            }
        });

        validate();
    }

    private void validate() {
        String s = inputArea.getText();
        TextValidator.ValidationResult r = new TextValidator(s).run();
        setCodeDoc(s, r);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Open-Close Matching (GUI)");
        frame.setContentPane(new InputGUI().rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
