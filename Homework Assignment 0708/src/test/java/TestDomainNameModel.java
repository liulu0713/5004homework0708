import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import student.model.DomainNameModel;
import student.model.DomainNameModel.DNRecord;

/**
 * JUnit tests for DomainNameModel and DomainNameModelImpl.
 *
 * Uses the preloaded hostrecords.xml so no network calls are made
 * for the existing-hostname tests. The autograder relies on fixed
 * IP locations, so we never fetch records that are already in the file.
 */
public class TestDomainNameModel {

    /** Path to the default test database. */
    private static final String DB = "data/hostrecords.xml";

    /** Model instance under test. */
    private DomainNameModel model;

    /**
     * Rebuild a fresh model before each test so state doesn't leak between tests.
     */
    @BeforeEach
    public void setUp() {
        model = DomainNameModel.getInstance(DB);
    }

    // -------------------------------------------------------------------------
    // getRecords()
    // -------------------------------------------------------------------------

    /**
     * Verify the database loads and returns the expected number of records.
     */
    @Test
    public void testGetRecordsSize() {
        List<DNRecord> records = model.getRecords();
        assertEquals(3, records.size());
    }

    /**
     * Verify the hostnames loaded from the XML match the expected values.
     */
    @Test
    public void testGetRecordsHostnames() {
        List<DNRecord> records = model.getRecords();
        List<String> hostnames = records.stream().map(DNRecord::hostname).toList();
        assertTrue(hostnames.contains("www.github.com"));
        assertTrue(hostnames.contains("www.northeastern.edu"));
        assertTrue(hostnames.contains("www.google.com"));
    }

    /**
     * Verify the list returned by getRecords() is not null.
     */
    @Test
    public void testGetRecordsNotNull() {
        assertNotNull(model.getRecords());
    }

    // -------------------------------------------------------------------------
    // getRecord() — existing hostnames (no network call)
    // -------------------------------------------------------------------------

    /**
     * Verify getRecord() returns the correct IP for github.com.
     */
    @Test
    public void testGetRecordGithubIp() {
        DNRecord r = model.getRecord("www.github.com");
        assertEquals("140.82.112.3", r.ip());
    }

    /**
     * Verify getRecord() returns the correct city for github.com.
     */
    @Test
    public void testGetRecordGithubCity() {
        DNRecord r = model.getRecord("www.github.com");
        assertEquals("San Francisco", r.city());
    }

    /**
     * Verify getRecord() returns the correct coordinates for google.com.
     */
    @Test
    public void testGetRecordGoogleCoordinates() {
        DNRecord r = model.getRecord("www.google.com");
        assertEquals(40.7746, r.latitude(), 0.001);
        assertEquals(-73.4761, r.longitude(), 0.001);
    }

    /**
     * Verify getRecord() returns the correct region for northeastern.edu.
     */
    @Test
    public void testGetRecordNortheasternRegion() {
        DNRecord r = model.getRecord("www.northeastern.edu");
        assertEquals("Colorado", r.region());
    }

    /**
     * Verify getRecord() returns the correct country for northeastern.edu.
     */
    @Test
    public void testGetRecordNortheasternCountry() {
        DNRecord r = model.getRecord("www.northeastern.edu");
        assertEquals("US", r.country());
    }

    /**
     * Verify getRecord() returns the correct postal code for github.com.
     */
    @Test
    public void testGetRecordGithubPostal() {
        DNRecord r = model.getRecord("www.github.com");
        assertEquals("94110", r.postal());
    }

    /**
     * Verify getRecord() returns a non-null result for a known hostname.
     */
    @Test
    public void testGetRecordNotNull() {
        assertNotNull(model.getRecord("www.github.com"));
    }

    /**
     * Verify getRecord() returns the correct hostname field on the record.
     */
    @Test
    public void testGetRecordHostnameField() {
        DNRecord r = model.getRecord("www.github.com");
        assertEquals("www.github.com", r.hostname());
    }

    // -------------------------------------------------------------------------
    // getInstance()
    // -------------------------------------------------------------------------

    /**
     * Verify getInstance() with default database returns a non-null model.
     */
    @Test
    public void testGetInstanceDefault() {
        assertNotNull(DomainNameModel.getInstance());
    }

    /**
     * Verify getInstance() with explicit path returns a non-null model.
     */
    @Test
    public void testGetInstanceWithPath() {
        assertNotNull(DomainNameModel.getInstance(DB));
    }

    // -------------------------------------------------------------------------
    // Edge cases
    // -------------------------------------------------------------------------

    /**
     * Verify that loading from a non-existent file results in an empty record list
     * rather than throwing an exception.
     */
    @Test
    public void testEmptyDatabaseReturnsEmptyList() {
        DomainNameModel emptyModel = DomainNameModel.getInstance("data/nonexistent.xml");
        assertNotNull(emptyModel.getRecords());
        assertTrue(emptyModel.getRecords().isEmpty());
    }

    /**
     * Verify that DNRecord stores all fields correctly via its canonical constructor.
     */
    @Test
    public void testDNRecordFields() {
        DNRecord r = new DNRecord("host.com", "1.2.3.4", "City",
                "Region", "US", "12345", 10.0, 20.0);
        assertEquals("host.com", r.hostname());
        assertEquals("1.2.3.4", r.ip());
        assertEquals("City", r.city());
        assertEquals("Region", r.region());
        assertEquals("US", r.country());
        assertEquals("12345", r.postal());
        assertEquals(10.0, r.latitude());
        assertEquals(20.0, r.longitude());
    }

    /**
     * Verify that two DNRecords with identical fields are considered equal.
     */
    @Test
    public void testDNRecordEquality() {
        DNRecord a = new DNRecord("host.com", "1.2.3.4", "City",
                "Region", "US", "12345", 10.0, 20.0);
        DNRecord b = new DNRecord("host.com", "1.2.3.4", "City",
                "Region", "US", "12345", 10.0, 20.0);
        assertEquals(a, b);
    }
}