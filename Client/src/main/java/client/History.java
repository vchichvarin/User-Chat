package client;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.omg.CORBA.Environment;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.List;
import org.apache.commons.csv.CSVPrinter;

public class History {
    private static PrintWriter out;
    private static Connection connection;
    private static PreparedStatement psGetHistory;

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

    public static void getHistoryFromDB (String login) {
        Connection c = null;
        Statement stmt = null;
        String filename = new String("csv/csv_" + login + ".csv");

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:main.db");
            stmt = c.createStatement();
            String select_sql = "SELECT sender, receiver, text, date FROM messags IF sender = ? OR receiver = ?";
            ResultSet resultSet = stmt.executeQuery(select_sql);

            BufferedWriter writer = Files.newBufferedWriter(Paths.get(filename));
            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                    .withHeader(resultSet.getMetaData()).withQuoteMode(QuoteMode.ALL));
            while (resultSet.next()) {
                csvPrinter.printRecord(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getDate(4));
            }

            csvPrinter.flush();
            csvPrinter.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}