import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

public class InputGUI {
    private JPanel rootPanel;
    private JEditorPane previewArea;
    private JTextArea inputArea;
    private JTextArea msgArea;

    private Style styleNormal, styleError;

    void setGoodMessage(String msg) {
        msgArea.setForeground(Color.GRAY);
        msgArea.setFont(new Font("Arial", Font.ITALIC, 12));
        msgArea.setText(msg);

    }
    void setErrorMessage(String msg) {
        msgArea.setForeground(Color.BLACK);
        msgArea.setFont(new Font("Arial", Font.BOLD, 12));
        msgArea.setText(msg);
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
        styleError = doc.addStyle("styleError", styleNormal);
        StyleConstants.setForeground(styleNormal, Color.black);
        StyleConstants.setForeground(styleError, Color.red);

        setCodeDoc("", new TextValidator.ValidationResult(true));

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

            void validate() {
                String s = inputArea.getText();
                TextValidator.ValidationResult r = new TextValidator(s).run();
                setCodeDoc(s, r);
            }
        });
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("InputGUI");
        frame.setContentPane(new InputGUI().rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}