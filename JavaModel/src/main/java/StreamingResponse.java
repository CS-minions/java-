import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class StreamingResponse {
    private final JTextArea textArea;
    private volatile boolean isPaused = false;
    private volatile boolean skipToEnd = false;
    private int typingSpeed = 50;
    private Thread streamingThread;

    public StreamingResponse(JTextArea textArea) {
        this.textArea = textArea;
    }

    public synchronized void streamResponse(String text) {
        if (streamingThread != null) {
            streamingThread.interrupt();
            try {
                streamingThread.join(100); // 等待之前的线程结束
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        skipToEnd = false;
        
        streamingThread = new Thread(() -> {
            if (skipToEnd) {
                SwingUtilities.invokeLater(() -> textArea.append(text));
                return;
            }

            char[] chars = text.toCharArray();
            StringBuilder remaining = new StringBuilder(text);
            
            for (int i = 0; i < chars.length && !Thread.currentThread().isInterrupted(); i++) {
                while (isPaused && !skipToEnd && !Thread.currentThread().isInterrupted()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                
                if (skipToEnd) {
                    SwingUtilities.invokeLater(() -> textArea.append(remaining.toString()));
                    break;
                }

                final char currentChar = chars[i];
                SwingUtilities.invokeLater(() -> textArea.append(String.valueOf(currentChar)));
                
                if (remaining.length() > 0) {
                    remaining.deleteCharAt(0);
                }
                
                try {
                    Thread.sleep(typingSpeed);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        });
        
        streamingThread.start();
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
        if (streamingThread != null) {
            streamingThread.interrupt();
            try {
                streamingThread.join(100); // 等待线程结束
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            streamingThread = null;
        }
    }
} 