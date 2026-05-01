package is.pig.minecraft.api;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import java.util.function.LongSupplier;
import java.util.function.Supplier;

public class ActionForensics {
    private static final ActionForensics INSTANCE = new ActionForensics();
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    private BufferedWriter writer;
    private File currentLogFile;
    private boolean lastStateWasEnabled = false;

    // Providers set by implementation (piggy-lib)
    private static BooleanSupplier fullActionDebugProvider = () -> false;
    private static BooleanSupplier inMetaActionSessionProvider = () -> false;
    private static DoubleSupplier tpsProvider = () -> 20.0;
    private static DoubleSupplier msptProvider = () -> 50.0;
    private static DoubleSupplier cpsProvider = () -> 0.0;
    private static Supplier<String> positionProvider = () -> "n/a";
    private static LongSupplier tickProvider = () -> -1L;
    private static Supplier<File> logDirProvider = () -> new File("logs/piggy");

    public static void setFullActionDebugProvider(BooleanSupplier p) { fullActionDebugProvider = p; }
    public static void setInMetaActionSessionProvider(BooleanSupplier p) { inMetaActionSessionProvider = p; }
    public static void setTpsProvider(DoubleSupplier p) { tpsProvider = p; }
    public static void setMsptProvider(DoubleSupplier p) { msptProvider = p; }
    public static void setCpsProvider(DoubleSupplier p) { cpsProvider = p; }
    public static void setPositionProvider(Supplier<String> p) { positionProvider = p; }
    public static void setTickProvider(LongSupplier p) { tickProvider = p; }
    public static void setLogDirProvider(Supplier<File> p) { logDirProvider = p; }

    private ActionForensics() {}

    public static ActionForensics getInstance() {
        return INSTANCE;
    }

    public synchronized void log(String event, String source, String name, String details) {
        boolean isEnabled = fullActionDebugProvider.getAsBoolean();

        if (!isEnabled && !inMetaActionSessionProvider.getAsBoolean()) {
            if (lastStateWasEnabled) {
                closeWriter();
                lastStateWasEnabled = false;
            }
            return;
        }

        lastStateWasEnabled = true;
        ensureWriterOpen();

        if (writer != null) {
            try {
                String timestamp = LocalDateTime.now().format(TIME_FORMAT);
                
                double tps = tpsProvider.getAsDouble();
                double mspt = msptProvider.getAsDouble();
                double cps = cpsProvider.getAsDouble();
                String posStr = positionProvider.get();
                long tick = tickProvider.getAsLong();

                String telemetryStr = String.format("Tick:%d TPS:%.1f MSPT:%.1f CPS:%.1f Pos:%s", tick, tps, mspt, cps, posStr);
                
                String line = String.format("[%s] [%-10s] [%-15s] %-25s | %-75s | %s", 
                    timestamp, 
                    event, 
                    source, 
                    name, 
                    telemetryStr,
                    details != null ? details : "");
                writer.write(line);
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                System.err.println("[ActionForensics] Failed to write to log: " + e.getMessage());
            }
        }
    }

    private void ensureWriterOpen() {
        if (writer != null) return;

        File logsDir = logDirProvider.get();
        if (logsDir == null) return;
        
        if (!logsDir.exists() && !logsDir.mkdirs()) {
            System.err.println("[ActionForensics] Failed to create directory: " + logsDir.getAbsolutePath());
            return;
        }

        currentLogFile = new File(logsDir, "piggy_actions.log");
        try {
            writer = new BufferedWriter(new FileWriter(currentLogFile, true));
            String startMsg = "--- ACTION DEBUG SESSION STARTED: " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + " ---";
            writer.write("\n" + startMsg + "\n");
            writer.flush();
        } catch (IOException e) {
            System.err.println("[ActionForensics] Failed to open log file: " + e.getMessage());
        }
    }

    private void closeWriter() {
        if (writer != null) {
            try {
                writer.write("--- ACTION DEBUG SESSION PAUSED ---\n");
                writer.close();
            } catch (IOException ignored) {}
            writer = null;
        }
    }
}
