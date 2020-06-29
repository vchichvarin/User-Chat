package client;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class History {
    private static PrintWriter out;

    private static String getHistoryFilenameByLogin(String login) {
        return "history/history_" + login + ".txt";
    }

    public static void start(String login) {
        try {
            out = new PrintWriter(new FileOutputStream(getHistoryFilenameByLogin(login), true), true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void stop() {
        if (out != null) {
            out.close();
        }
    }

    public static void writeLine(String msg) {
        out.println(msg);
    }

    public static String getLast100LinesOfHistory(String login) {
        if (!Files.exists(Paths.get(getHistoryFilenameByLogin(login)))) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        try {
            List<String> historyLines = Files.readAllLines(Paths.get(getHistoryFilenameByLogin(login)));
            int startPosition = 0;
            if (historyLines.size() > 100) {
                startPosition = historyLines.size() - 100;
            }
            for (int i = startPosition; i < historyLines.size(); i++) {
                sb.append(historyLines.get(i)).append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}