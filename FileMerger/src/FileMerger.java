import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;


/**
 * Класс склеивателя всех текстовых файлов в директории и её поддиеркториях в алфавитном порядке.
 */
public class FileMerger {
    private final ArrayList<File> textFiles;
    private final File root;
    private final File resultFile;

    /**
     * Метод для получения с консоли адреса корректной начальной директории от пользователя.
     @return возвращает введённую пользователем начальную директорию
     */
    public static File getStartingDirectory() {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter the path to the starting directory:");
        String directoryPath = in.next();
        File startingDirectory = new File(directoryPath);
        while (!(startingDirectory.exists() && startingDirectory.isDirectory())) {
            System.out.println("Incorrect path to the directory! Please, try again:");
            directoryPath = in.next();
            startingDirectory = new File(directoryPath);
        }
        return startingDirectory;
    }

    /**
     * Беспараметрический конструктор класса FileMerger.
     */
    public FileMerger() {
        root = getStartingDirectory();
        textFiles = new ArrayList<File>();
        resultFile = new File(root, "result.txt");
        if (resultFile.exists()) {
            resultFile.delete();
        }
    }

    /**
     * Метод для добавления всех файлов из директории и всех её поддиректорий в {@link FileMerger#textFiles}.
     * @param currentDirectory текущая директория(файлы которой мы хотим добавить в {@link FileMerger#textFiles})
     */
    public void getAllFiles(File currentDirectory) {
        if (currentDirectory.listFiles() == null) {
            return;
        }
        for (File file :
                currentDirectory.listFiles()) {
            if (file.isFile()) {
                textFiles.add(file);
            } else {
                getAllFiles(file);
            }
        }
    }

    /**
     * Метод для соединения содержимого файлов, отсортированных по алфавиту, и записи в результирующий файл.
     */
    public void mergeByAlphabet() {
        getAllFiles(root);
        Comparator<File> comparatorByName = Comparator.comparing(file -> file.getName());
        textFiles.sort(comparatorByName);
        if (resultFile.exists()) {
            resultFile.delete();
        }
        try {
            resultFile.createNewFile();
            FileWriter resultWriter = new FileWriter(resultFile, false);
            FileReader currentFileReader;
            BufferedReader bufferedReader;
            for (File file :
                    textFiles) {
                currentFileReader = new FileReader(file);
                bufferedReader = new BufferedReader(currentFileReader);
                String line = bufferedReader.readLine();
                while (line != null) {
                    System.out.println(line);
                    resultWriter.write(line + "\n");
                    line = bufferedReader.readLine();
                }
                bufferedReader.close();
                currentFileReader.close();
            }
            resultWriter.close();
        } catch (Exception ex) {
            System.out.println("Something went wrong with the creation of the result file, reading or" +
                    " writing to files. Please, check the security rights of the directory and try again.");
        }
    }
}
