import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TranslatorGUI extends JFrame {
    private JTextArea chineseTextArea;
    private JTextArea englishTextArea;
    private JButton chToEngButton;
    private JButton engToChButton;
    private TranslationService translationService;

    public TranslatorGUI() {
        setTitle("中英文翻译系统");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        // 初始化翻译服务
        translationService = new TranslationService();

        // 创建主面板
        JPanel mainPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 中文区域
        JPanel chinesePanel = new JPanel(new BorderLayout(5, 5));
        chineseTextArea = new JTextArea(5, 40);
        chineseTextArea.setLineWrap(true);
        chineseTextArea.setWrapStyleWord(true);
        chToEngButton = new JButton("中译英");
        chinesePanel.add(new JScrollPane(chineseTextArea), BorderLayout.CENTER);
        chinesePanel.add(chToEngButton, BorderLayout.SOUTH);

        // 英文区域
        JPanel englishPanel = new JPanel(new BorderLayout(5, 5));
        englishTextArea = new JTextArea(5, 40);
        englishTextArea.setLineWrap(true);
        englishTextArea.setWrapStyleWord(true);
        engToChButton = new JButton("英译中");
        englishPanel.add(new JScrollPane(englishTextArea), BorderLayout.CENTER);
        englishPanel.add(engToChButton, BorderLayout.SOUTH);

        // 添加到主面板
        mainPanel.add(chinesePanel);
        mainPanel.add(englishPanel);
        add(mainPanel, BorderLayout.CENTER);

        // 添加按钮事件监听器
        chToEngButton.addActionListener(e -> {
            String chineseText = chineseTextArea.getText();
            String translatedText = translationService.translateToEnglish(chineseText);
            englishTextArea.setText(translatedText);
        });

        engToChButton.addActionListener(e -> {
            String englishText = englishTextArea.getText();
            String translatedText = translationService.translateToChinese(englishText);
            chineseTextArea.setText(translatedText);
        });

        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TranslatorGUI().setVisible(true);
        });
    }
} 