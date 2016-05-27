import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.text.*;

class myTextEditor extends JFrame {
    private JTextArea textArea = new JTextArea(50, 150);
    private JFileChooser fileDialog = new JFileChooser(System.getProperty("user.dir"));
    private String curFile = "Untitled";
    private boolean hasChanged = false;
    private KeyListener listener = new KeyListener() {
        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            hasChanged = true;
            Save.setEnabled(true);
            SaveAs.setEnabled(true);
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    };
    // Actions
    Action Open = new AbstractAction("Open") {
        @Override
        public void actionPerformed(ActionEvent e) {
            saveOld();
            if (fileDialog.showOpenDialog(null)==JFileChooser.APPROVE_OPTION) {
                readInFile(fileDialog.getSelectedFile().getAbsolutePath());
            }
            SaveAs.setEnabled(true);
        }
    };
    Action Save = new AbstractAction("Save") {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(!curFile.equals("Untitled")) {
                saveFile(curFile);
            }
            else {
                saveFileAs();
            }
        }
    };
    Action SaveAs = new AbstractAction("Save as...") {
        @Override
        public void actionPerformed(ActionEvent e) {
            saveFileAs();
        }
    };
    Action Quit = new AbstractAction("Quit") {
        @Override
        public void actionPerformed(ActionEvent e) {
            saveOld();
            System.exit(0);
        }
    };
    Action New = new AbstractAction("New") {
        @Override
        public void actionPerformed(ActionEvent e) {
            saveOld();
            textArea.setText("");
            curFile = "Untitled";
            setTitle(curFile);
            hasChanged = false;
            Save.setEnabled(false);
            SaveAs.setEnabled(false);
        }
    };

    ActionMap actionMap = textArea.getActionMap();
    Action Cut = actionMap.get(DefaultEditorKit.cutAction);
    Action Copy = actionMap.get(DefaultEditorKit.copyAction);
    Action Paste = actionMap.get(DefaultEditorKit.pasteAction);

    private void saveFileAs() {
        if(fileDialog.showSaveDialog(null)==JFileChooser.APPROVE_OPTION) {
            saveFile(fileDialog.getSelectedFile().getAbsolutePath());
        }
    }

    private void saveOld() {
        if (hasChanged) {
            if (JOptionPane.showConfirmDialog(
                    this, "Would you like to save?", "Save", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                saveFile(curFile);
            }
        }
    }

    private void readInFile(String fileName) {
        FileReader reader = null;
        try {
            reader =  new FileReader(fileName);
            textArea.read(reader, null);
            curFile = fileName;
            setTitle(curFile);
            hasChanged = true;
        }
        catch (IOException e) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(this, "File not found!");
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void saveFile(String fileName) {
        FileWriter writer = null;

        try {
            writer = new FileWriter(fileName);
            textArea.write(writer);
            curFile = fileName;
            hasChanged = false;
            Save.setEnabled(false);
        }
        catch (IOException e) {
        }
        finally {
            if (writer != null) {
                try {
                    writer.close();
                }
                catch (IOException e) {

                }
            }
        }
    }

    private void closeListener() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (hasChanged) {
                    int confirmed = JOptionPane.showConfirmDialog(null,
                            "Would you like to save before exit?", "Unsaved changes!",
                            JOptionPane.YES_NO_OPTION);

                    if (confirmed == JOptionPane.YES_OPTION) {
                        if (curFile == "Untitled") {
                            saveFileAs();
                        }
                        else {
                            saveFile(curFile);
                        }
                    }

                    else {
                        System.exit(0);
                    }
                }
                else {
                    System.exit(0);
                }
            }
        });
    }

    public myTextEditor() {
        // font
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        // wrap those words dude
        textArea.setLineWrap(true);
        // scroll bars
        JScrollPane scroller = new JScrollPane(
                textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        add(scroller, BorderLayout.CENTER);
        // menu bar
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        // menu tabs
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        menuBar.add(fileMenu);
        menuBar.add(editMenu);

        // Menu tabs

        // file
        fileMenu.add(New);
        fileMenu.add(Open);
        fileMenu.add(Save);
        fileMenu.add(Quit);
        fileMenu.add(SaveAs);
        fileMenu.addSeparator();

        for (int i=0; i<4; ++i) {
            fileMenu.getItem(i).setIcon(null);
        }

        // edit
        editMenu.add(Cut);
        editMenu.add(Copy);
        editMenu.add(Paste);

        editMenu.getItem(0).setText("Cut");
        editMenu.getItem(1).setText("Copy");
        editMenu.getItem(2).setText("Paste");

        //

        JToolBar toolBar = new JToolBar();
        add(toolBar, BorderLayout.NORTH);
        toolBar.add(New);
        toolBar.add(Open);
        toolBar.add(Save);
        toolBar.addSeparator();

        JButton cut = toolBar.add(Cut);
        JButton copy = toolBar.add(Copy);
        JButton paste = toolBar.add(Paste);

        cut.setText("CUT");
        copy.setText("COPY");
        paste.setText("PASTE");

        Save.setEnabled(false);
        SaveAs.setEnabled(false);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        textArea.addKeyListener(listener);
        setTitle(curFile);
        setVisible(true);
        closeListener();
    }

}
public class Main {
    public static void main(String[] args) {
        new myTextEditor();
    }
}
