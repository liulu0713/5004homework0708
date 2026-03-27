import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import student.DNInfoApp;

/**
 * JUnit tests for ArgsController via DNInfoApp.main().
 *
 * Uses System.setOut() to capture stdout so we can assert on the output
 * without writing to actual files in most tests.
 */
public class TestArgsController {

    /** Captures output written to stdout during each test. */
    private ByteArrayOutputStream outContent;

    /** Saved reference to the real stdout so we can restore it after each test. */
    private PrintStream originalOut;

    /**
     * Redirect stdout to a buffer before each test.
     */
    @BeforeEach
    public void setUp() {
        outContent = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outContent));
    }

    /**
     * Restore stdout after each test so other output isn't swallowed.
     */
    @AfterEach
    public void tearDown() throws Exception {
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
    }

    // -------------------------------------------------------------------------
    // Helper
    // -------------------------------------------------------------------------

    /** Returns captured output with all whitespace collapsed for easy comparison. */
    private String out() {
        return outContent.toString().replaceAll("\\s+", " ").trim();
    }

    // -------------------------------------------------------------------------
    // -h / --help
    // -------------------------------------------------------------------------

    /**
     * Verify -h prints the help text.
     */
    @Test
    public void testHelpShortFlag() {
        DNInfoApp.main(new String[]{"-h"});
        assertTrue(out().contains("DNInfoApp"));
    }

    /**
     * Verify --help prints the help text.
     */
    @Test
    public void testHelpLongFlag() {
        DNInfoApp.main(new String[]{"--help"});
        assertTrue(out().contains("DNInfoApp"));
    }

    /**
     * Verify help output contains usage info about -f flag.
     */
    @Test
    public void testHelpContainsFormatInfo() {
        DNInfoApp.main(new String[]{"-h"});
        assertTrue(out().contains("-f"));
    }

    // -------------------------------------------------------------------------
    // Default / all
    // -------------------------------------------------------------------------

    /**
     * Verify running with no args displays all three records in pretty format.
     */
    @Test
    public void testNoArgsShowsAll() {
        DNInfoApp.main(new String[]{});
        String o = out();
        assertTrue(o.contains("www.github.com"));
        assertTrue(o.contains("www.northeastern.edu"));
        assertTrue(o.contains("www.google.com"));
    }

    /**
     * Verify "all" keyword displays all three records.
     */
    @Test
    public void testAllKeyword() {
        DNInfoApp.main(new String[]{"all"});
        String o = out();
        assertTrue(o.contains("www.github.com"));
        assertTrue(o.contains("www.northeastern.edu"));
        assertTrue(o.contains("www.google.com"));
    }

    // -------------------------------------------------------------------------
    // Single hostname lookup
    // -------------------------------------------------------------------------

    /**
     * Verify looking up a single hostname shows only that record.
     */
    @Test
    public void testSingleHostname() {
        DNInfoApp.main(new String[]{"www.github.com"});
        String o = out();
        assertTrue(o.contains("www.github.com"));
        assertTrue(o.contains("140.82.112.3"));
    }

    /**
     * Verify a single hostname lookup does NOT show other records.
     */
    @Test
    public void testSingleHostnameExcludesOthers() {
        DNInfoApp.main(new String[]{"www.github.com"});
        assertTrue(!out().contains("www.google.com"));
    }

    /**
     * Verify pretty print format contains IP label for a known host.
     */
    @Test
    public void testPrettyFormatIpLabel() {
        DNInfoApp.main(new String[]{"www.github.com"});
        assertTrue(out().contains("IP:"));
    }

    /**
     * Verify pretty print format contains Location label.
     */
    @Test
    public void testPrettyFormatLocationLabel() {
        DNInfoApp.main(new String[]{"www.github.com"});
        assertTrue(out().contains("Location:"));
    }

    /**
     * Verify pretty print format contains Coordinates label.
     */
    @Test
    public void testPrettyFormatCoordinatesLabel() {
        DNInfoApp.main(new String[]{"www.github.com"});
        assertTrue(out().contains("Coordinates:"));
    }

    // -------------------------------------------------------------------------
    // -f format flag
    // -------------------------------------------------------------------------

    /**
     * Verify -f json produces JSON output (starts with [ or {).
     */
    @Test
    public void testFormatJson() {
        DNInfoApp.main(new String[]{"all", "-f", "json"});
        String o = outContent.toString().trim();
        assertTrue(o.startsWith("[") || o.startsWith("{"));
    }

    /**
     * Verify -f json output contains expected hostname field.
     */
    @Test
    public void testFormatJsonContainsHostname() {
        DNInfoApp.main(new String[]{"www.github.com", "-f", "json"});
        assertTrue(out().contains("www.github.com"));
    }

    /**
     * Verify -f xml produces XML output containing domainList tag.
     */
    @Test
    public void testFormatXml() {
        DNInfoApp.main(new String[]{"all", "-f", "xml"});
        assertTrue(out().contains("domainList"));
    }

    /**
     * Verify -f xml output contains domain tag.
     */
    @Test
    public void testFormatXmlDomainTag() {
        DNInfoApp.main(new String[]{"all", "-f", "xml"});
        assertTrue(out().contains("domain"));
    }

    /**
     * Verify -f csv output contains a header row.
     */
    @Test
    public void testFormatCsvHeader() {
        DNInfoApp.main(new String[]{"all", "-f", "csv"});
        assertTrue(out().contains("hostname"));
    }

    /**
     * Verify -f csv output contains expected IP value.
     */
    @Test
    public void testFormatCsvContainsIp() {
        DNInfoApp.main(new String[]{"all", "-f", "csv"});
        assertTrue(out().contains("140.82.112.3"));
    }

    /**
     * Verify -f pretty (explicit) still produces pretty output.
     */
    @Test
    public void testFormatPrettyExplicit() {
        DNInfoApp.main(new String[]{"all", "-f", "pretty"});
        assertTrue(out().contains("IP:"));
    }

    // -------------------------------------------------------------------------
    // --data flag
    // -------------------------------------------------------------------------

    /**
     * Verify --data with the default file path still loads correctly.
     */
    @Test
    public void testDataFlag() {
        DNInfoApp.main(new String[]{"all", "--data", "data/hostrecords.xml"});
        assertTrue(out().contains("www.github.com"));
    }

    // -------------------------------------------------------------------------
    // -o output file flag
    // -------------------------------------------------------------------------

    /**
     * Verify -o writes output to a file instead of stdout.
     */
    @Test
    public void testOutputToFile() throws Exception {
        File tmp = File.createTempFile("dninfo_test", ".txt");
        tmp.deleteOnExit();

        DNInfoApp.main(new String[]{"all", "-o", tmp.getAbsolutePath()});

        String fileContent = Files.readString(tmp.toPath());
        assertTrue(fileContent.contains("www.github.com"));
        // stdout should be empty since output went to file
        assertEquals("", outContent.toString().trim());
    }

    /**
     * Verify -o with json format writes JSON to file.
     */
    @Test
    public void testOutputToFileJson() throws Exception {
        File tmp = File.createTempFile("dninfo_test", ".json");
        tmp.deleteOnExit();

        DNInfoApp.main(new String[]{"all", "-f", "json", "-o", tmp.getAbsolutePath()});

        String fileContent = Files.readString(tmp.toPath()).trim();
        assertTrue(fileContent.startsWith("[") || fileContent.startsWith("{"));
    }
}