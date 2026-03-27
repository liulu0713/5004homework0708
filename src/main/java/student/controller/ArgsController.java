package student.controller;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import student.model.DomainNameModel;
import student.model.DomainNameModel.DNRecord;
import student.model.formatters.Formats;

/**
 * A controller to handle the command line arguments and run the program.
 *
 * <p>Supported arguments (in any order):
 * <pre>
 *   [hostname|all]          hostname to look up, or "all" for every record
 *   [-f json|xml|csv|pretty] output format (default: pretty)
 *   [-o filepath]           write output to file instead of stdout
 *   [--data filepath]       use a custom database file
 *   [-h | --help]           print help and exit
 * </pre>
 */
public class ArgsController {

    /** The model instance used to retrieve records. */
    private DomainNameModel model;

    /** Output format — defaults to PRETTY. */
    private Formats format = Formats.PRETTY;

    /** Output stream — defaults to stdout. */
    private OutputStream output = System.out;

    /** Hostname to look up; "all" (or null) means display everything. */
    private String hostname = "all";

    /** Database file path — defaults to the interface constant. */
    private String dataPath = DomainNameModel.DATABASE;

    /**
     * Parses the given command line arguments and runs the program
     *
     * <p>If {@code -h} or {@code --help} is present, the help message is printed
     * and the method returns immediately without querying the model.
     *
     * @param args the command line arguments from {@code main}
     */
    public void run(String[] args) {
        // --- parse args ---
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-h":
                case "--help":
                    System.out.println(getHelp());
                    return;

                case "-f":
                    if (i + 1 < args.length) {
                        Formats f = Formats.containsValues(args[++i]);
                        if (f != null) {
                            format = f;
                        }
                    }
                    break;

                case "-o":
                    if (i + 1 < args.length) {
                        try {
                            output = new FileOutputStream(args[++i]);
                        } catch (Exception e) {
                            System.err.println("Could not open output file: " + args[i]);
                        }
                    }
                    break;

                case "--data":
                    if (i + 1 < args.length) {
                        dataPath = args[++i];
                    }
                    break;

                default:
                    // anything else is treated as the hostname
                    if (!args[i].startsWith("-")) {
                        hostname = args[i];
                    }
                    break;
            }
        }

        // --- build model (uses dataPath, which may have been overridden) ---
        model = DomainNameModel.getInstance(dataPath);

        // --- retrieve records ---
        List<DNRecord> records;
        if (hostname == null || hostname.equalsIgnoreCase("all")) {
            records = model.getRecords();
        } else {
            records = List.of(model.getRecord(hostname));
        }

        // --- write output ---
        DomainNameModel.writeRecords(records, format, output);
    }

    /**
     * Returns the help message displayed when {@code -h} or {@code --help} is passed.
     *
     * @return the help string
     */
    public String getHelp() {
        return """
                DNInfoApp [hostname|all] [-f json|xml|csv|pretty] [-o file path] [-h | --help] [--data filepath]

                Looks up the information for a given hostname (url) or displays information for
                all domains in the database. Can be output in json, xml, csv, or pretty format.
                If -o file is provided, the output will be written to the file instead of stdout.

                --data is mainly used in testing to provide a different data file, defaults to the hostrecords.xml file.
                """;
    }
}
