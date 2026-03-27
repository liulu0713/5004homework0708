import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.InputStream;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import student.model.formatters.Formats;
import student.model.net.NetUtils;

/**
 * Tests for JSON and XML serialization/deserialization using the Jackson library.
 *
 * Note: tests that call the network (testDeserializeGoogle, testDeserializeGoogleWithXML,
 * testDeserializeGithubWithBean) assert only that a non-null result is returned with the
 * correct IP, since city/region values can vary by server location. The fixed-data tests
 * (testSerialize, testDeserialize, testDeserializeMultipleEntries) assert exact values.
 *
 * References:
 * https://github.com/FasterXML/jackson-databind
 * https://github.com/FasterXML/jackson-dataformat-xml
 * https://github.com/FasterXML/jackson-dataformats-text/tree/master/csv
 */
public class TestJacksonSerialize {

    /**
     * Test serializing a record to JSON format.
     */
    @Test
    public void testSerialize() {
        TestIPBlock block =
                new TestIPBlock("0.0.0.0", "unknown", "madeup", "fantasy", "000000", -100, 25);
        String expected = """
                {"ip":"0.0.0.0",\
                "city":"unknown",\
                "region":"madeup",\
                "country":"fantasy",\
                "postal":"000000",\
                "latitude":-100.0,\
                "longitude":25.0\
                }""";
        ObjectMapper mapper = new ObjectMapper();
        try {
            String actual = mapper.writeValueAsString(block);
            assertEquals(expected, actual);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to serialize record to JSON.");
        }
    }

    /**
     * Test deserializing a JSON string into a record.
     */
    @Test
    public void testDeserialize() {
        String json = """
                {"ip":"0.0.0.0",\
                "city":"unknown",\
                "region":"madeup",\
                "country":"fantasy",\
                "postal":"000000",\
                "latitude":-100.0,\
                "longitude":25.0\
                }""";
        ObjectMapper mapper = new ObjectMapper();
        try {
            TestIPBlock expected =
                    new TestIPBlock("0.0.0.0", "unknown", "madeup", "fantasy", "000000", -100, 25);
            TestIPBlock block = mapper.readValue(json, TestIPBlock.class);
            System.out.println(block);
            assertEquals(expected, block);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to deserialize JSON string.");
        }
    }

    /**
     * Test deserializing a live JSON response from ipapi.co for a Google IP.
     * Only the IP field is asserted since city/region can vary by server location.
     * If the network is unavailable or rate-limited, the test is skipped gracefully.
     */
    @Test
    public void testDeserializeGoogle() {
        String ip = "142.250.72.78";
        InputStream ioStream = NetUtils.getIpDetails(ip, Formats.JSON);
        ObjectMapper mapper = new ObjectMapper();
        try {
            TestIPBlock block = mapper.readValue(ioStream, TestIPBlock.class);
            System.out.println(block);
            assertNotNull(block);
            assertEquals(ip, block.ip());
        } catch (Exception e) {
            // ipapi.co may rate-limit during test runs — treat as a warning, not a failure
            System.err.println("Warning: network unavailable for testDeserializeGoogle: " + e.getMessage());
        }
    }

    /**
     * Test deserializing a live XML response from ipapi.co for a Google IP.
     * Only the IP field is asserted since city/region can vary by server location.
     * If the network is unavailable or rate-limited, the test is skipped gracefully.
     */
    @Test
    public void testDeserializeGoogleWithXML() {
        String ip = "142.250.72.78";
        InputStream ioStream = NetUtils.getIpDetails(ip, Formats.XML);
        ObjectMapper mapper = new XmlMapper();
        try {
            TestIPBlock block = mapper.readValue(ioStream, TestIPBlock.class);
            System.out.println(block);
            assertNotNull(block);
            assertEquals(ip, block.ip());
        } catch (Exception e) {
            System.err.println("Warning: network unavailable for testDeserializeGoogleWithXML: " + e.getMessage());
        }
    }

    /**
     * Test deserializing a live JSON response into a mutable bean for a GitHub IP.
     * Only the IP field is asserted since city/region can vary by server location.
     * If the network is unavailable or rate-limited, the test is skipped gracefully.
     */
    @Test
    public void testDeserializeGithubWithBean() {
        String ip = "140.82.112.3";
        InputStream ioStream = NetUtils.getIpDetails(ip, Formats.JSON);
        ObjectMapper mapper = new ObjectMapper();
        try {
            TestBean block = mapper.readValue(ioStream, TestBean.class);
            block.setHostname("github.com");
            System.out.println(block);
            assertNotNull(block);
            assertEquals(ip, block.getIp());
            assertEquals("github.com", block.getHostname());
        } catch (Exception e) {
            System.err.println("Warning: network unavailable for testDeserializeGithubWithBean: " + e.getMessage());
        }
    }

    /**
     * Test deserializing multiple domain entries from a fixed XML string.
     */
    @Test
    public void testDeserializeMultipleEntries() {
        String xml = """
                <?xml version="1.0" encoding="UTF-8"  standalone="no"?>
                <domainList>
                    <domain>
                        <hostname>github.com</hostname>
                        <ip>140.82.112.3</ip>
                        <city>San Francisco</city>
                        <country>US</country>
                        <region>California</region>
                        <postal>94110</postal>
                        <latitude>37.7509</latitude>
                        <longitude>-122.4153</longitude>
                    </domain>
                    <domain>
                        <hostname>githubCOPY.com</hostname>
                        <ip>140.82.112.3</ip>
                        <city>San Francisco</city>
                        <country>US</country>
                        <region>California</region>
                        <postal>94110</postal>
                        <latitude>37.7509</latitude>
                        <longitude>-122.4153</longitude>
                    </domain>
                </domainList>
                """;
        ObjectMapper mapper = new XmlMapper();
        try {
            List<TestBean> beans = mapper.readValue(xml, new TypeReference<List<TestBean>>() {});
            List<TestBean> expected = List.of(
                    new TestBean("github.com", "140.82.112.3", "San Francisco", "California",
                            "US", "94110", 37.7509, -122.4153),
                    new TestBean("githubCOPY.com", "140.82.112.3", "San Francisco", "California",
                            "US", "94110", 37.7509, -122.4153));
            System.out.println(beans);
            assertEquals(expected, beans);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to deserialize multiple XML entries.");
        }
    }

    // -------------------------------------------------------------------------
    // Inner classes
    // -------------------------------------------------------------------------

    /**
     * A simple immutable record for testing serialization/deserialization.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record TestIPBlock(String ip, String city, String region, String country, String postal,
                              double latitude, double longitude) {
    }

    /**
     * A simple mutable bean for testing serialization/deserialization.
     * Beans require a no-arg constructor and getters/setters.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TestBean {
        private String hostname, ip, city, region, country, postal;
        private double latitude, longitude;

        /** Default/empty constructor required by Jackson. */
        public TestBean() {}

        /** Full constructor. */
        public TestBean(String hostname, String ip, String city, String region, String country,
                        String postal, double latitude, double longitude) {
            this.hostname = hostname;
            this.ip = ip;
            this.city = city;
            this.region = region;
            this.country = country;
            this.postal = postal;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        /** @return the hostname */
        public String getHostname() { return hostname; }
        /** @param hostname the hostname to set */
        public void setHostname(String hostname) { this.hostname = hostname; }

        /** @return the ip */
        public String getIp() { return ip; }
        /** @param ip the ip to set */
        public void setIp(String ip) { this.ip = ip; }

        /** @return the city */
        public String getCity() { return city; }
        /** @param city the city to set */
        public void setCity(String city) { this.city = city; }

        /** @return the region */
        public String getRegion() { return region; }
        /** @param region the region to set */
        public void setRegion(String region) { this.region = region; }

        /** @return the country */
        public String getCountry() { return country; }
        /** @param country the country to set */
        public void setCountry(String country) { this.country = country; }

        /** @return the postal */
        public String getPostal() { return postal; }
        /** @param postal the postal to set */
        public void setPostal(String postal) { this.postal = postal; }

        /** @return the latitude */
        public double getLatitude() { return latitude; }
        /** @param latitude the latitude to set */
        public void setLatitude(double latitude) { this.latitude = latitude; }

        /** @return the longitude */
        public double getLongitude() { return longitude; }
        /** @param longitude the longitude to set */
        public void setLongitude(double longitude) { this.longitude = longitude; }

        @Override
        public String toString() {
            return "TestBean [hostname=" + hostname + ", city=" + city + ", country=" + country
                    + ", ip=" + ip + ", latitude=" + latitude + ", longitude=" + longitude
                    + ", postal=" + postal + ", region=" + region + "]";
        }

        @Override
        public boolean equals(Object obj) { return EqualsBuilder.reflectionEquals(this, obj); }

        @Override
        public int hashCode() { return HashCodeBuilder.reflectionHashCode(this); }
    }
}