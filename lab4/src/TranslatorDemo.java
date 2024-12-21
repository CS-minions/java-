import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class TranslatorDemo extends JFrame {
    private JTextArea sourceTextArea;
    private JTextArea targetTextArea;
    private JButton translateButton;
    private JComboBox<String> translationMethodCombo;
    private JComboBox<String> sourceLanguageCombo;
    private JComboBox<String> targetLanguageCombo;
    private JLabel statusLabel;

    public TranslatorDemo() {
        setTitle("多模型翻译程序");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        ((JPanel)getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));

        // 创建顶部控制面板
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        
        // 翻译方式选择
        translationMethodCombo = new JComboBox<>(new String[]{
            "预设翻译", 
            "百度API翻译", 
            "文心一言翻译",
            "通义千问翻译",
            "SiliconFlow翻译"
        });
        
        // 语言选择
        String[] languages = {"中文", "英文"};
        sourceLanguageCombo = new JComboBox<>(languages);
        targetLanguageCombo = new JComboBox<>(languages);
        
        // 设置默认选择
        sourceLanguageCombo.setSelectedItem("中文");
        targetLanguageCombo.setSelectedItem("英文");
        
        // 添加到顶部面板
        topPanel.add(new JLabel("翻译方式："));
        topPanel.add(translationMethodCombo);
        topPanel.add(new JLabel("源语言："));
        topPanel.add(sourceLanguageCombo);
        topPanel.add(new JLabel("目标语言："));
        topPanel.add(targetLanguageCombo);
        
        // 添加语言切换按钮
        JButton switchButton = new JButton("⇄");
        switchButton.setToolTipText("切换语言");
        switchButton.addActionListener(e -> switchLanguages());
        topPanel.add(switchButton);
        
        add(topPanel, BorderLayout.NORTH);

        // 创建中间的主面板
        JPanel mainPanel = new JPanel(new GridLayout(2, 1, 10, 10));

        // 源语言面板
        JPanel sourcePanel = new JPanel(new BorderLayout(5, 5));
        sourceTextArea = new JTextArea(8, 40);
        sourceTextArea.setLineWrap(true);
        sourceTextArea.setWrapStyleWord(true);
        JScrollPane sourceScrollPane = new JScrollPane(sourceTextArea);
        sourcePanel.setBorder(new TitledBorder("源文本"));
        sourcePanel.add(sourceScrollPane, BorderLayout.CENTER);

        // 目标语言面板
        JPanel targetPanel = new JPanel(new BorderLayout(5, 5));
        targetTextArea = new JTextArea(8, 40);
        targetTextArea.setLineWrap(true);
        targetTextArea.setWrapStyleWord(true);
        targetTextArea.setEditable(false);
        JScrollPane targetScrollPane = new JScrollPane(targetTextArea);
        targetPanel.setBorder(new TitledBorder("译文"));
        targetPanel.add(targetScrollPane, BorderLayout.CENTER);

        mainPanel.add(sourcePanel);
        mainPanel.add(targetPanel);
        add(mainPanel, BorderLayout.CENTER);

        // 底部控制面板
        JPanel bottomPanel = new JPanel(new BorderLayout());
        
        // 翻译按钮
        translateButton = new JButton("开始翻译");
        translateButton.addActionListener(e -> translate());
        
        // 状态标签
        statusLabel = new JLabel("就绪");
        statusLabel.setBorder(new EmptyBorder(0, 10, 0, 10));
        
        bottomPanel.add(translateButton, BorderLayout.EAST);
        bottomPanel.add(statusLabel, BorderLayout.WEST);
        add(bottomPanel, BorderLayout.SOUTH);

        // 添加组件联动
        translationMethodCombo.addActionListener(e -> updateUI());
        sourceLanguageCombo.addActionListener(e -> updateUI());
        targetLanguageCombo.addActionListener(e -> updateUI());

        pack();
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 600));
    }

    private void switchLanguages() {
        Object source = sourceLanguageCombo.getSelectedItem();
        Object target = targetLanguageCombo.getSelectedItem();
        sourceLanguageCombo.setSelectedItem(target);
        targetLanguageCombo.setSelectedItem(source);
        
        // 如果有文本，也交换文本
        String sourceText = sourceTextArea.getText();
        String targetText = targetTextArea.getText();
        if (!sourceText.isEmpty() && !targetText.isEmpty()) {
            sourceTextArea.setText(targetText);
            targetTextArea.setText(sourceText);
        }
    }

    private void updateUI() {
        // 根据选择的翻译方式更新UI状态
        boolean isPresetTranslation = translationMethodCombo.getSelectedIndex() == 0;
        
        // 预设翻译时禁用语言选择
        sourceLanguageCombo.setEnabled(!isPresetTranslation);
        targetLanguageCombo.setEnabled(!isPresetTranslation);
        
        if (isPresetTranslation) {
            sourceLanguageCombo.setSelectedItem("中文");
            targetLanguageCombo.setSelectedItem("英文");
            sourceTextArea.setToolTipText("请输入预设的示例文本进行翻译");
        } else {
            sourceTextArea.setToolTipText(null);
        }
    }

    private void translate() {
        String sourceText = sourceTextArea.getText().trim();
        if (sourceText.isEmpty()) {
            showStatus("请输入要翻译的文本", true);
            return;
        }

        // 获取源语言和目标语言的代码
        String sourceCode = getLanguageCode(sourceLanguageCombo.getSelectedItem().toString());
        String targetCode = getLanguageCode(targetLanguageCombo.getSelectedItem().toString());
        
        try {
            String result = "";
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            showStatus("正在翻译...", false);
            translateButton.setEnabled(false);
            
            switch(translationMethodCombo.getSelectedIndex()) {
                case 0: // 预设翻译
                    result = presetTranslation(sourceText);
                    break;
                case 1: // 百度API翻译
                    result = BaiduTranslator.translate(sourceText, sourceCode, targetCode);
                    break;
                case 2: // 文心一言翻译
                    result = AIModelTranslator.translateWithErnie(
                        sourceText, 
                        sourceCode.equals("zh")
                    );
                    break;
                case 3: // 通义千问翻译
                    result = AIModelTranslator.translateWithQianwen(
                        sourceText, 
                        sourceCode.equals("zh")
                    );
                    break;
                case 4: // SiliconFlow翻译
                    result = AIModelTranslator.translateWithSilicon(
                        sourceText,
                        sourceCode,
                        targetCode
                    );
                    break;
            }
            
            targetTextArea.setText(result);
            showStatus("翻译完成", false);
            
        } catch (Exception e) {
            showStatus("翻译出错: " + e.getMessage(), true);
            e.printStackTrace();
        } finally {
            setCursor(Cursor.getDefaultCursor());
            translateButton.setEnabled(true);
        }
    }

    private String getLanguageCode(String language) {
        return switch (language) {
            case "中文" -> "zh";
            case "英文" -> "en";
            default -> throw new IllegalArgumentException("不支持的语言: " + language);
        };
    }

    private void showStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.setForeground(isError ? Color.RED : Color.BLACK);
    }

    private String presetTranslation(String text) {
        // 预设的翻译对
        String preset1_ch = "建校41年，深圳大学秉承\"自立、自律、自强\"的校训，紧随特区，锐意改革、快速发展，为特区发展和国家现代化建设做出了重要贡献。";
        String preset1_en = "Sticking to the motto of \"self-reliance, self-discipline, self-improvement\", the University is dedicated to serving the Shenzhen Special Economic Zone (SEZ), demonstrating China's reform and opening up and pioneering change in higher education.";

        // 检查输入文本是否匹配预设文本
        if (text.equals(preset1_ch)) {
            return preset1_en;
        } else if (text.equals(preset1_en)) {
            return preset1_ch;
        }

        return "未找到匹配的预设翻译。\n\n请使用以下示例文本：\n\n中文：\n" + preset1_ch + "\n\n英文：\n" + preset1_en;
    }


    public static void main(String[] args) {
        // 设置Swing的外观
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new TranslatorDemo().setVisible(true);
        });
    }
} 