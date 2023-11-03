package homework2;

import java.io.*;
import java.nio.file.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.*;

public class FileProcessingApplication {
    private static final Logger logger = Logger.getLogger(FileProcessingApplication.class.getName());
    private static final int THREAD_POOL_SIZE = 4;
    private static final String INPUT_DIRECTORY = "input_files";
    private static final String OUTPUT_DIRECTORY = "output_logs";

    public static void main(String[] args) {
        setupLogging();

        createOutputDirectory();

        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        try {
            Files.walk(Paths.get(INPUT_DIRECTORY))
                    .filter(Files::isRegularFile)
                    .forEach(file -> {
                        executorService.execute(() -> processFile(file));
                    });

            executorService.shutdown();

            while (!executorService.isTerminated()) {
            }

            generateSummaryLog();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "An error occurred while processing the files.", e);
        }
    }

    private static void setupLogging() {
        try {
            File logsDirectory = new File(OUTPUT_DIRECTORY);
            logsDirectory.mkdirs();

            LogManager.getLogManager().readConfiguration(
                    FileProcessingApplication.class.getResourceAsStream("logging.properties"));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "An error occurred while setting up logging.", e);
        }
    }

    private static void createOutputDirectory() {
        File outputDirectory = new File(OUTPUT_DIRECTORY);
        if (!outputDirectory.exists()) {
            if (outputDirectory.mkdirs()) {
                logger.log(Level.INFO, "Output directory created: " + outputDirectory.getAbsolutePath());
            } else {
                logger.log(Level.SEVERE, "Failed to create output directory.");
            }
        }
    }

    private static void processFile(Path file) {
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }

            String fileName = file.getFileName().toString();
            int wordCount = countWords(content.toString());
            double averageWordLength = calculateAverageWordLength(content.toString());

            String logEntry = String.format("File: %s, Word Count: %d, Average Word Length: %.2f",
                    fileName, wordCount, averageWordLength);
            logger.log(Level.INFO, logEntry);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "An error occurred while processing file: " + file.getFileName(), e);
        }
    }

    private static int countWords(String text) {
        String[] words = text.split("\\s+");
        return words.length;
    }

    private static double calculateAverageWordLength(String text) {
        String[] words = text.split("\\s+");
        int totalLength = 0;
        for (String word : words) {
            totalLength += word.length();
        }
        return (double) totalLength / words.length;
    }

    private static void generateSummaryLog() {
        try {
            int fileCount = 0;
            double totalAverageWordLength = 0.0;

            File logsDirectory = new File(OUTPUT_DIRECTORY);
            File[] logFiles = logsDirectory.listFiles();
            if (logFiles != null) {
                for (File logFile : logFiles) {
                    BufferedReader reader = new BufferedReader(new FileReader(logFile));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("File:")) {
                            fileCount++;
                            int startIndex = line.indexOf("Average Word Length:") + "Average Word Length:".length();
                            double averageWordLength = Double.parseDouble(line.substring(startIndex).trim());
                            totalAverageWordLength += averageWordLength;
                        }
                    }
                    reader.close();
                }
            }

            double averageWordLengthAcrossFiles = totalAverageWordLength / fileCount;

            String summaryLogEntry = String.format("Total Files Processed: %d, Average Word Length Across Files: %.2f",
                    fileCount, averageWordLengthAcrossFiles);
            logger.log(Level.INFO, summaryLogEntry);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "An error occurred while generating the summary log.", e);
        }
    }
}