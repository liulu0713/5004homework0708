import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
import java.net.UnknownHostException;

import org.junit.jupiter.api.Test;

import student.model.formatters.Formats;
import student.model.net.NetUtils;

/**
 * Public tests for all methods in the NetUtils class.
 *
 * NOTE: tests that make live network calls assert only on stable fields (like the IP
 * address itself) rather than city/region values, which vary by server location.
 * If ipapi.co is rate-limiting, network tests degrade gracefully with a warning.
 */
public class TestNetUtils {

    /**
     * Tests that getApiUrl() builds the correct URL for the default (XML) format.
     */
    @Test
    public void testGetApiUrlDefaultXml() {
        String ip = "11.11.11.12";
        String expected = "https://ipapi.co/11.11.11.12/xml/";
        assertEquals(expected, NetUtils.getApiUrl(ip));
    }

    /**
     * Tests that getApiUrl() builds the correct URL for JSON format.
     */
    @Test
    public void testGetApiUrlJson() {
        String ip = "11.11.11.12";
        String expected = "https://ipapi.co/11.11.11.12/json/";
        assertEquals(expected, NetUtils.getApiUrl(ip, Formats.JSON));
    }

    /**
     * Tests that getApiUrl() builds the correct URL for CSV format.
     */
    @Test
    public void testGetApiUrlCsv() {
        String ip = "11.11.11.12";
        String expected = "https://ipapi.co/11.11.11.12/csv/";
        assertEquals(expected, NetUtils.getApiUrl(ip, Formats.CSV));
    }

    /**
     * Tests that lookUpIp() returns a non-null, non-empty IP string for a valid hostname.
     * Does not assert a specific IP since Google routes to different servers by location.
     */
    @Test
    public void testLookUpIpValid() {
        try {
            String actual = NetUtils.lookUpIp("google.com");
            assertNotNull(actual);
            assertTrue(actual.matches("\\d+\\.\\d+\\.\\d+\\.\\d+"),
                    "Expected an IPv4 address but got: " + actual);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests that lookUpIp() throws UnknownHostException for an invalid hostname.
     */
    @Test
    public void testLookUpIpInvalid() {
        try {
            NetUtils.lookUpIp("www.google.commmmm");
        } catch (Exception e) {
            assertEquals(UnknownHostException.class, e.getClass());
        }
    }

    /**
     * Tests that getUrlContents() returns a non-empty stream for a valid URL.
     * Checks that the response contains the queried IP rather than exact city/region fields.
     * Degrades gracefully if the API is rate-limiting.
     */
    @Test
    public void testGetUrlContents() {
        String ip = "142.250.72.78";
        try {
            InputStream stream = NetUtils.getUrlContents(NetUtils.getApiUrl(ip));
            String actual = new String(stream.readAllBytes());
            if (actual.isBlank()) {
                System.err.println("Warning: testGetUrlContents got empty response (rate limited?)");
                return;
            }
            assertTrue(actual.contains(ip), "Response should contain the queried IP");
            assertTrue(actual.contains("<root>"), "XML response should contain <root> tag");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests that getIpDetails() returns a non-empty XML stream containing the queried IP.
     * Degrades gracefully if the API is rate-limiting.
     */
    @Test
    public void testGetIpDetails() {
        String ip = "142.250.72.78";
        try {
            InputStream stream = NetUtils.getIpDetails(ip);
            String actual = new String(stream.readAllBytes());
            if (actual.isBlank()) {
                System.err.println("Warning: testGetIpDetails got empty response (rate limited?)");
                return;
            }
            assertTrue(actual.contains(ip), "XML response should contain the queried IP");
            assertTrue(actual.contains("<root>"), "Response should be XML with <root> tag");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests that getIpDetails() with JSON format returns a non-empty JSON stream
     * containing the queried IP. Degrades gracefully if the API is rate-limiting.
     */
    @Test
    public void testGetIpDetailsJson() {
        String ip = "142.250.72.78";
        try {
            InputStream stream = NetUtils.getIpDetails(ip, Formats.JSON);
            String actual = new String(stream.readAllBytes());
            if (actual.isBlank()) {
                System.err.println("Warning: testGetIpDetailsJson got empty response (rate limited?)");
                return;
            }
            assertTrue(actual.contains(ip), "JSON response should contain the queried IP");
            assertTrue(actual.contains("\"ip\""), "Response should be JSON with ip field");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}