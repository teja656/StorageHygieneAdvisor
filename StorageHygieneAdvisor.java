import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class StorageHygieneAdvisor extends JFrame {


JTextArea output;
JProgressBar progressBar;
File selectedFolder;

java.util.List<File> allFiles = new ArrayList<>();
Map<String, java.util.List<File>> duplicates = new HashMap<>();
Map<String, Long> categorySize = new HashMap<>();

GraphPanel graphPanel;

public StorageHygieneAdvisor() {
    setTitle("Storage Hygiene Advisor");
    setSize(900, 600);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    JPanel topPanel = new JPanel();

    JButton selectBtn = new JButton("Select Folder");
    JButton scanBtn = new JButton("Scan");
    JButton dupBtn = new JButton("Show Duplicates");
    JButton exportBtn = new JButton("Export Report");

    topPanel.add(selectBtn);
    topPanel.add(scanBtn);
    topPanel.add(dupBtn);
    topPanel.add(exportBtn);

    add(topPanel, BorderLayout.NORTH);

    JPanel centerPanel = new JPanel(new GridLayout(1, 2));

    output = new JTextArea();
    output.setEditable(false);
    centerPanel.add(new JScrollPane(output));

    graphPanel = new GraphPanel();
    centerPanel.add(graphPanel);

    add(centerPanel, BorderLayout.CENTER);

    progressBar = new JProgressBar();
    progressBar.setStringPainted(true);
    add(progressBar, BorderLayout.SOUTH);

    selectBtn.addActionListener(e -> selectFolder());
    scanBtn.addActionListener(e -> startScan());
    dupBtn.addActionListener(e -> showDuplicates());
    exportBtn.addActionListener(e -> exportReport());
}

void selectFolder() {
    JFileChooser chooser = new JFileChooser();
    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
        selectedFolder = chooser.getSelectedFile();
        output.setText("Selected: " + selectedFolder.getAbsolutePath());
    }
}

void startScan() {
    if (selectedFolder == null) return;

    new Thread(() -> {
        allFiles.clear();
        duplicates.clear();
        categorySize.clear();

        progressBar.setValue(0);

        scanRecursive(selectedFolder);
        analyze();

        SwingUtilities.invokeLater(() -> {
            output.setText(generateInsights());
            graphPanel.repaint();
            progressBar.setValue(100);
        });

    }).start();
}

void scanRecursive(File dir) {
    File[] files = dir.listFiles();
    if (files == null) return;

    for (File f : files) {
        if (f.isDirectory()) {
            scanRecursive(f);
        } else {
            allFiles.add(f);
        }
    }
}

void analyze() {
    for (File f : allFiles) {
        String key = f.getName() + f.length();
        duplicates.computeIfAbsent(key, k -> new ArrayList<>()).add(f);

        String type = getType(f.getName());
        categorySize.put(type, categorySize.getOrDefault(type, 0L) + f.length());
    }
}

String getType(String name) {
    name = name.toLowerCase();
    if (name.endsWith(".jpg") || name.endsWith(".png")) return "Images";
    if (name.endsWith(".mp4") || name.endsWith(".mkv")) return "Videos";
    if (name.endsWith(".pdf") || name.endsWith(".doc")) return "Docs";
    return "Others";
}

String generateInsights() {
    StringBuilder sb = new StringBuilder();

    int dupCount = 0;
    for (java.util.List<File> list : duplicates.values()) {
        if (list.size() > 1) dupCount += list.size();
    }

    sb.append("Total Files: ").append(allFiles.size()).append("\n");
    sb.append("Duplicate Files: ").append(dupCount).append("\n\n");

    for (Map.Entry<String, Long> e : categorySize.entrySet()) {
        sb.append(e.getKey()).append(": ").append(e.getValue() / 1024).append(" KB\n");
    }

    return sb.toString();
}

void showDuplicates() {
    StringBuilder sb = new StringBuilder();

    for (java.util.List<File> list : duplicates.values()) {
        if (list.size() > 1) {
            sb.append("Duplicate:\n");
            for (File f : list) {
                sb.append(f.getAbsolutePath()).append("\n");
            }
            sb.append("\n");
        }
    }

    JTextArea area = new JTextArea(sb.toString());
    JOptionPane.showMessageDialog(this, new JScrollPane(area), "Duplicates", JOptionPane.INFORMATION_MESSAGE);
}

void exportReport() {
    try {
        FileWriter fw = new FileWriter("report.txt");
        fw.write(output.getText());
        fw.close();
        JOptionPane.showMessageDialog(this, "Report Saved!");
    } catch (Exception e) {
        e.printStackTrace();
    }
}

class GraphPanel extends JPanel {
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int x = 20;
        int y = 50;
        int totalWidth = 400;

        long total = 0;
        for (long v : categorySize.values()) total += v;

        if (total == 0) return;

        for (Map.Entry<String, Long> entry : categorySize.entrySet()) {
            int width = (int) ((entry.getValue() * totalWidth) / total);

            g.drawRect(x, y, width, 30);
            g.drawString(entry.getKey(), x + 5, y + 20);

            x += width;
        }
    }
}

public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        new StorageHygieneAdvisor().setVisible(true);
    });
}

}
