import javax.swing.*;
import java.util.concurrent.*;

public class StreamingResponse {
    private final JTextPane textPane;
    private volatile boolean isPaused = false;
    private volatile boolean skipToEnd = false;
    private int typingSpeed = 50;  // 默认打字速度（毫秒/字符）
    private ScheduledExecutorService scheduler;

    public StreamingResponse(JTextPane textPane) {
        this.textPane = textPane;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    public void streamResponse(String response) {
        if (response == null || response.isEmpty()) return;
        
        StringBuilder currentText = new StringBuilder(textPane.getText());
        int startPos = currentText.length();
        final int[] currentIndex = {0};
        
        ScheduledFuture<?>[] future = {null};
        future[0] = scheduler.scheduleAtFixedRate(() -> {
            if (skipToEnd) {
                currentText.append(response.substring(currentIndex[0]));
                updateTextPane(currentText.toString());
                future[0].cancel(false);
                skipToEnd = false;
                currentIndex[0] = response.length();
                return;
            }
            
            if (!isPaused && currentIndex[0] < response.length()) {
                currentText.append(response.charAt(currentIndex[0]));
                updateTextPane(currentText.toString());
                currentIndex[0]++;
                
                if (currentIndex[0] >= response.length()) {
                    future[0].cancel(false);
                }
            }
        }, 0, typingSpeed, TimeUnit.MILLISECONDS);
    }

    private void updateTextPane(String text) {
        SwingUtilities.invokeLater(() -> {
            textPane.setText("");
            CodeHighlighter.insertText(textPane, text, false);
            textPane.setCaretPosition(textPane.getDocument().getLength());
        });
    }

    public void pause() {
        isPaused = true;
    }

    public void resume() {
        isPaused = false;
    }

    public void skipToEnd() {
        skipToEnd = true;
    }

    public void setTypingSpeed(int speed) {
        this.typingSpeed = Math.max(10, Math.min(200, speed));
    }

    public void cleanup() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
        }
    }
} 