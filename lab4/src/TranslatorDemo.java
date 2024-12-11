import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;

public class TranslatorDemo extends JFrame {
    private JTextArea chineseTextArea;
    private JTextArea englishTextArea;
    private JButton chToEngButton;
    private JButton engToChButton;
    private JComboBox<String> translationMethodCombo;

    public TranslatorDemo() {
        setTitle("中英互译程序");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        ((JPanel)getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));

        // 创建顶部的翻译方式选择
        JPanel topPanel = new JPanel();
        translationMethodCombo = new JComboBox<>(new String[]{"预设翻译", "在线API翻译", "大模型翻译"});
        topPanel.add(new JLabel("选择翻译方式："));
        topPanel.add(translationMethodCombo);
        add(topPanel, BorderLayout.NORTH);

        // 创建中间的主面板
        JPanel mainPanel = new JPanel(new GridLayout(2, 1, 10, 10));

        // 中文面板
        JPanel chinesePanel = new JPanel(new BorderLayout(5, 5));
        chineseTextArea = new JTextArea(5, 40);
        chineseTextArea.setLineWrap(true);
        chineseTextArea.setWrapStyleWord(true);
        chToEngButton = new JButton("中译英");
        JScrollPane chScrollPane = new JScrollPane(chineseTextArea);
        chinesePanel.add(new JLabel("中文:"), BorderLayout.NORTH);
        chinesePanel.add(chScrollPane, BorderLayout.CENTER);
        chinesePanel.add(chToEngButton, BorderLayout.SOUTH);

        // 英文面板
        JPanel englishPanel = new JPanel(new BorderLayout(5, 5));
        englishTextArea = new JTextArea(5, 40);
        englishTextArea.setLineWrap(true);
        englishTextArea.setWrapStyleWord(true);
        engToChButton = new JButton("英译中");
        JScrollPane engScrollPane = new JScrollPane(englishTextArea);
        englishPanel.add(new JLabel("英文:"), BorderLayout.NORTH);
        englishPanel.add(engScrollPane, BorderLayout.CENTER);
        englishPanel.add(engToChButton, BorderLayout.SOUTH);

        mainPanel.add(chinesePanel);
        mainPanel.add(englishPanel);
        add(mainPanel, BorderLayout.CENTER);

        // 添加按钮事件监听器
        chToEngButton.addActionListener(e -> translate(true));
        engToChButton.addActionListener(e -> translate(false));

        pack();
        setLocationRelativeTo(null);
    }

    private void translate(boolean isChToEng) {
        String text = isChToEng ? chineseTextArea.getText() : englishTextArea.getText();
        String result = "";
        
        switch(translationMethodCombo.getSelectedIndex()) {
            case 0: // 预设翻译
                result = presetTranslation(text, isChToEng);
                break;
            case 1: // 在线API翻译
                result = apiTranslation(text, isChToEng);
                break;
            case 2: // 大模型翻译
                result = aiModelTranslation(text, isChToEng);
                break;
        }

        if (isChToEng) {
            englishTextArea.setText(result);
        } else {
            chineseTextArea.setText(result);
        }
    }

    private String presetTranslation(String text, boolean isChToEng) {
        // 预设的翻译对（直接使用题目给出的原文）
        String preset1_ch = "建校41年，深圳大学秉承'自立、自律、自强'的校训，紧随特区，锐意改革、快速发展，为特区发展和国家现代化建设做出了重要贡献。";
        String preset1_en = "Sticking to the motto of \"self-reliance, self-discipline, self-improvement\", the University is dedicated to serving the Shenzhen Special Economic Zone (SEZ), demonstrating China's reform and opening up and pioneering change in higher education.";

        // 简单的文本比较
        text = text.trim();
        
        if (isChToEng) {
            System.out.println("输入文本：" + text);
            System.out.println("预设文本：" + preset1_ch);
            if (text.equals(preset1_ch)) {
                return preset1_en;
            }
        } else {
            System.out.println("输入文本：" + text);
            System.out.println("预设文本：" + preset1_en);
            if (text.equals(preset1_en)) {
                return preset1_ch;
            }
        }
        return "未找到匹配的预设翻译\n请确保输入的文本与以下示例完全一致：\n\n中文示例：\n" + preset1_ch + "\n\n英文示例：\n" + preset1_en;
    }

    private String apiTranslation(String text, boolean isChToEng) {
        return BaiduTranslator.translate(
            text,
            isChToEng ? "zh" : "en",
            isChToEng ? "en" : "zh"
        );
    }

    private String aiModelTranslation(String text, boolean isChToEng) {
        // 使用两个大模型API进行翻译，并返回两个结果
        String ernieResult = AIModelTranslator.translateWithErnie(text, isChToEng);
        String qianwenResult = AIModelTranslator.translateWithQianwen(text, isChToEng);
        
        return String.format("文心一言译文：\n%s\n\n通义千问译文：\n%s", 
            ernieResult, qianwenResult);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TranslatorDemo().setVisible(true);
        });
    }
} 