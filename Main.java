import java.io.*;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

        try {

            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("-h")) {
                    System.out.println();
                    System.out.println("=====================================================================================================================");
                    System.out.println("Tools ini berfungsi untuk mengubah file log menjadi .txt / .json");
                    System.out.println("-t json / -t txt untuk menentukan jenis file yang diinginkan");
                    System.out.println("-o untuk menentukan lokasi path tujuan file yang akan dihasilkan");
                    System.out.println("Contoh : sudo bash converting-log-tools.sh -t /var/log/log-catalina json -o /home/maruli/log-tomcat");
                    System.out.println("=====================================================================================================================");
                } else {
                    if (args.length < 6) {
                        String inputPath = args[0];
                        if (args.length > 1) {
                            if (args[1].equals("-t")) {
                                String[] inputPathArr = inputPath.split("/");
                                int indexTerakhir = inputPathArr.length - 1;
                                String namaFile = inputPathArr[indexTerakhir];
                                String[] namaFileArr = namaFile.split("\\.");
                                if (args.length != 2) {
                                    if (args.length > 3) {
                                        Tools.convert(args[0], args[4]);
                                    }
                                    if (args[2].equalsIgnoreCase("json")) {
                                        String namaFileFinal = namaFileArr[0] + ".json";
                                        inputPathArr[indexTerakhir] = namaFileFinal;
                                        Tools.convert(inputPath, Arrays.toString(inputPathArr).replace("]", "").replace("[", ""));
                                    } else {
                                        String namaFileFinal = namaFileArr[0] + ".txt";
                                        inputPathArr[indexTerakhir] = namaFileFinal;
                                        Tools.copy(inputPath, Arrays.toString(inputPathArr).replace("]", "").replace("[", ""));
                                    }
                                } else {
                                    String namaFileFinal = namaFileArr[0] + ".txt";
                                    inputPathArr[indexTerakhir] = namaFileFinal;
                                    Tools.copy(inputPath, Arrays.toString(inputPathArr).replace("]", "").replace("[", ""));
                                }
                            } else if (args[1].equals("-o")) {
                                if (args[2].equalsIgnoreCase("json")) {
                                    Tools.convert(inputPath, args[2]);
                                } else {
                                    Tools.copy(inputPath, args[2]);
                                }
                            }
                        } else {
                            String[] inputPathArr = inputPath.split("/");
                            int indexTerakhir = inputPathArr.length - 1;
                            String namaFile = inputPathArr[indexTerakhir];
                            String[] namaFileArr = namaFile.split("\\.");
                            String namaFileFinal = namaFileArr[0] + ".txt";
                            inputPathArr[indexTerakhir] = namaFileFinal;
                            Tools.copy(inputPath, Arrays.toString(inputPathArr).replace("]", "").replace("[", ""));
                        }
                        System.out.println("Done");
                    } else {
                        System.out.println("Parameter is too much");
                    }
                }

            } else {
                System.out.println("Parameter can't be empty");

            }

        } catch (Exception e) {
            e.printStackTrace();
//            System.out.println("Error converting, input/ output path is not correct");
        }


    }
}


class Tools {

    public static void convert(String inputFilePath, String outputFilePath) throws Exception {
        // Buka file
        FileInputStream fileInputStream = new FileInputStream(inputFilePath);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

        // inititialize sb
        StringBuilder json = new StringBuilder();
        json.append("[{");

        // baca setiap baris
        String line;
        int lineNumber = 1;
        while ((line = bufferedReader.readLine()) != null) {
            // Add each line as a key-value pair in the JSON object
            json.append("\"line" + lineNumber + "\":\"" + line + "\",");
            lineNumber++;
        }

        // hapus kom terakhir
        int index = json.lastIndexOf(",");
        if (index != -1) {
            json.deleteCharAt(index);
        }

        json.append("}]");

        // Write the JSON data to the output file
        fileCreator(outputFilePath);
        FileWriter fileWriter = new FileWriter(outputFilePath);
        fileWriter.write(json.toString());
        fileWriter.flush();
        fileWriter.close();

        // Close the input file
        bufferedReader.close();
        fileInputStream.close();
    }

    public static void copy(String inputFilePath, String outputFilePath) throws Exception {
        // Membuka file sumber
        FileInputStream fileInputStream = new FileInputStream(inputFilePath);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

        // Membuat file tujuan
        fileCreator(outputFilePath);
        FileWriter fileWriter = new FileWriter(outputFilePath);

        // Menyalin setiap baris dari file sumber ke file tujuan
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            fileWriter.write(line + System.lineSeparator());
        }

        // Menuliskan data ke file tujuan dan menutup file tujuan
        fileWriter.flush();
        fileWriter.close();

        // Menutup file sumber
        bufferedReader.close();
        fileInputStream.close();
    }

    public static File fileCreator(String path) throws Exception {
        File file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }
}
