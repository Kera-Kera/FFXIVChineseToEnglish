package name.yumao.ffxiv.chn.swing;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import name.yumao.ffxiv.chn.util.res.Config;

public class CsvFilePanel extends JFrame implements ActionListener {
    private JPanel csvPanel = new JPanel();
    private JPanel headerPanel = new JPanel();
    private List<JCheckBox> checkBoxes = new ArrayList<>();
    private JCheckBox selectAllCheckBox = new JCheckBox("全選");
    private JScrollPane scrollPane = new JScrollPane(csvPanel);
    private JButton confirmButton = new JButton("確認");
    
    Logger log = Logger.getLogger("GPLogger");

    public CsvFilePanel(String targetPath) {
        setTitle("選擇漢化範圍");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        csvPanel.setLayout(new BoxLayout(csvPanel, BoxLayout.Y_AXIS));

        selectAllCheckBox.addActionListener(this);
        selectAllCheckBox.setSelected(false);
        headerPanel.add(selectAllCheckBox);
        
        // Add the header panel and scroll pane to the content pane
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(headerPanel, BorderLayout.NORTH);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        loadCsvFiles(targetPath);
        
        confirmButton.addActionListener(this);
        contentPane.add(confirmButton, BorderLayout.SOUTH);

        // Center the window on the screen
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
    	if (e.getSource() == selectAllCheckBox) {
            boolean selected = selectAllCheckBox.isSelected();
            for (JCheckBox checkBox : checkBoxes) {
                checkBox.setSelected(selected);
            }
        } else if (e.getSource() == confirmButton) {
    		StringBuilder selectedItems = new StringBuilder();
            for (JCheckBox checkBox : checkBoxes) {
                if (!checkBox.isSelected()) {
                	String itemText = checkBox.getText();
                    if (itemText.startsWith("📁 ")) {
                        itemText = itemText.substring(3); // Remove "📁 " prefix
                    }
                    if (selectedItems.length() > 0) {
                        selectedItems.append("|");
                    }
                    selectedItems.append(("exd/" + itemText).replaceAll("\\.csv$", ""));
                }
            }
            
            log.config("Skip files: " + selectedItems.toString());
            Config.setProperty("SkipFiles", selectedItems.toString());
            Config.saveProperty();
            dispose();
    	}
    }

    private void loadCsvFiles(String targetPath) {
        File folder = new File(targetPath);
        File[] files = folder.listFiles();
        List<String> skipFiles = Arrays.asList(Config.getProperty("SkipFiles").split("[|]"));

        if (files != null) {
            for (File file : files) {
            	if (file.isDirectory()) {
	                JCheckBox checkBox = new JCheckBox("📁 " + file.getName());
	                if (skipFiles.contains("exd/" + file.getName())) {
	                	checkBox.setSelected(false);
	                } else {
	                	checkBox.setSelected(true);
	                }
	                // checkBox.addActionListener(this);
	                checkBoxes.add(checkBox);
	                csvPanel.add(checkBox);
            	}
            }
            for (File file : files) {
            	if (file.isFile() && file.getName().endsWith(".csv")) {
	                JCheckBox checkBox = new JCheckBox(file.getName());
	                if (skipFiles.contains("exd/" + file.getName().replace(".csv", ""))) {
	                	checkBox.setSelected(false);
	                } else {
	                	checkBox.setSelected(true);
	                }
	                
	                // checkBox.addActionListener(this);
	                checkBoxes.add(checkBox);
	                csvPanel.add(checkBox);
            	}
            }
        }
    }

    public static void main(String[] args) {
        new CsvFilePanel("resource" + File.separator + "rawexd");
    }
}