import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.util.regex.*;
import java.util.HashMap;
import java.util.Map;

public class CodeHighlighter {
    // 代码块样式
    private static final Color BACKGROUND_COLOR = new Color(246, 248, 250);
    private static final Color HEADER_COLOR = new Color(240, 242, 244);
    
    // 语法高亮颜色
    private static final Color KEYWORD_COLOR = new Color(207, 34, 46);    // 关键字 - 红色
    private static final Color STRING_COLOR = new Color(17, 99, 41);      // 字符串 - 绿色
    private static final Color COMMENT_COLOR = new Color(110, 119, 129);  // 注释 - 灰色
    private static final Color NORMAL_CODE_COLOR = new Color(36, 41, 47); // 普通代码 - 深灰
    
    // 语法高亮模式
    private static final Pattern KEYWORDS = Pattern.compile(
        "\\b(def|class|import|from|return|if|else|while|for|try|except|in|is|not|and|or|lambda|None|True|False)\\b"
    );
    private static final Pattern STRINGS = Pattern.compile("\"[^\"\\\\]*(\\\\.[^\"\\\\]*)*\"|'[^'\\\\]*(\\\\.[^'\\\\]*)*'");
    private static final Pattern COMMENTS = Pattern.compile("#.*$", Pattern.MULTILINE);
    
    // 代码块匹配
    private static final Pattern CODE_BLOCK_PATTERN = Pattern.compile("```(.*?)\\n([\\s\\S]*?)```");
    
    public static void insertText(JTextPane textPane, String text, boolean isUserMessage) {
        try {
            StyledDocument doc = textPane.getStyledDocument();
            
            // 创建普通文本样式
            Style normalStyle = textPane.addStyle("normalStyle", null);
            StyleConstants.setFontFamily(normalStyle, "微软雅黑");
            StyleConstants.setFontSize(normalStyle, 14);
            StyleConstants.setForeground(normalStyle, Color.BLACK);
            
            Matcher matcher = CODE_BLOCK_PATTERN.matcher(text);
            int lastEnd = 0;
            
            while (matcher.find()) {
                // 插入代码块前的普通文本
                String beforeText = text.substring(lastEnd, matcher.start());
                if (!beforeText.isEmpty()) {
                    doc.insertString(doc.getLength(), beforeText, normalStyle);
                }
                
                // 获取语言和代码内容
                String language = matcher.group(1).trim();
                String code = matcher.group(2);
                
                // 创建代码块面板
                insertCodeBlock(textPane, doc, language, code);
                
                lastEnd = matcher.end();
            }
            
            // 插入剩余的普通文本
            String remainingText = text.substring(lastEnd);
            if (!remainingText.isEmpty()) {
                doc.insertString(doc.getLength(), remainingText, normalStyle);
            }
            
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
    
    private static void insertCodeBlock(JTextPane textPane, StyledDocument doc, String language, String code) 
            throws BadLocationException {
        // 创建代码块样式
        Style codeStyle = textPane.addStyle("codeStyle", null);
        StyleConstants.setFontFamily(codeStyle, "Consolas");
        StyleConstants.setFontSize(codeStyle, 14);
        StyleConstants.setBackground(codeStyle, BACKGROUND_COLOR);
        
        // 插入语言标识和复制按钮
        doc.insertString(doc.getLength(), "\n", null);
        
        // 代码块头部（语言标识）
        Style headerStyle = textPane.addStyle("headerStyle", null);
        StyleConstants.setBackground(headerStyle, HEADER_COLOR);
        StyleConstants.setFontFamily(headerStyle, "微软雅黑");
        StyleConstants.setFontSize(headerStyle, 12);
        doc.insertString(doc.getLength(), language + "\n", headerStyle);
        
        // 添加复制按钮
        JButton copyButton = new JButton("Copy code");
        copyButton.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        copyButton.setFocusPainted(false);
        copyButton.addActionListener(e -> copyToClipboard(code));
        
        // 将按钮添加到文本面板
        StyleConstants.setComponent(codeStyle, copyButton);
        doc.insertString(doc.getLength(), " ", codeStyle);
        
        // 插入代码内容
        highlightCode(doc, code, language);
        doc.insertString(doc.getLength(), "\n", null);
    }
    
    private static void highlightCode(StyledDocument doc, String code, String language) 
            throws BadLocationException {
        String[] lines = code.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            
            // 应用语法高亮
            Matcher keywordMatcher = KEYWORDS.matcher(line);
            Matcher stringMatcher = STRINGS.matcher(line);
            Matcher commentMatcher = COMMENTS.matcher(line);
            
            // 创建基本代码样式
            Style baseStyle = doc.addStyle(null, null);
            StyleConstants.setFontFamily(baseStyle, "Consolas");
            StyleConstants.setFontSize(baseStyle, 14);
            StyleConstants.setForeground(baseStyle, NORMAL_CODE_COLOR);
            StyleConstants.setBackground(baseStyle, BACKGROUND_COLOR);
            
            // 处理注释
            if (commentMatcher.find()) {
                int commentStart = commentMatcher.start();
                if (commentStart > 0) {
                    doc.insertString(doc.getLength(), line.substring(0, commentStart), baseStyle);
                }
                Style commentStyle = doc.addStyle(null, baseStyle);
                StyleConstants.setForeground(commentStyle, COMMENT_COLOR);
                doc.insertString(doc.getLength(), line.substring(commentStart), commentStyle);
            } else {
                // 处理关键字和字符串
                StringBuilder processed = new StringBuilder();
                int lastEnd = 0;
                
                // 处理字符串
                while (stringMatcher.find()) {
                    // 添加字符串前的普通代码
                    doc.insertString(doc.getLength(), 
                                   line.substring(lastEnd, stringMatcher.start()), 
                                   baseStyle);
                    
                    // 添加字符串
                    Style stringStyle = doc.addStyle(null, baseStyle);
                    StyleConstants.setForeground(stringStyle, STRING_COLOR);
                    doc.insertString(doc.getLength(), 
                                   stringMatcher.group(), 
                                   stringStyle);
                    
                    lastEnd = stringMatcher.end();
                }
                
                // 添加剩余的代码
                if (lastEnd < line.length()) {
                    String remaining = line.substring(lastEnd);
                    // 处理关键字
                    keywordMatcher = KEYWORDS.matcher(remaining);
                    lastEnd = 0;
                    while (keywordMatcher.find()) {
                        doc.insertString(doc.getLength(), 
                                       remaining.substring(lastEnd, keywordMatcher.start()), 
                                       baseStyle);
                        
                        Style keywordStyle = doc.addStyle(null, baseStyle);
                        StyleConstants.setForeground(keywordStyle, KEYWORD_COLOR);
                        doc.insertString(doc.getLength(), 
                                       keywordMatcher.group(), 
                                       keywordStyle);
                        
                        lastEnd = keywordMatcher.end();
                    }
                    if (lastEnd < remaining.length()) {
                        doc.insertString(doc.getLength(), 
                                       remaining.substring(lastEnd), 
                                       baseStyle);
                    }
                }
            }
            
            // 添加换行符（除了最后一行）
            if (i < lines.length - 1) {
                doc.insertString(doc.getLength(), "\n", null);
            }
        }
    }
    
    private static void copyToClipboard(String text) {
        StringSelection selection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, null);
    }
} 