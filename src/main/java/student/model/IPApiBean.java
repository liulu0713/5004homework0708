package student.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A mutable Java Bean used to deserialize the JSON response from ipapi.co.
 *
 * <p>Jackson requires either a no-arg constructor + setters (bean pattern) or
 * a record with matching constructor parameter names. We use a bean here
 * because Jackson populates it field-by-field via setters before we convert
 * it into the immutable {@link DomainNameModel.DNRecord}.
 *
 * <p>Example API response fields this maps from:
 * <pre>
 * { "ip": "...", "city": "...", "region": "...", "country": "...",
 *   "postal": "...", "latitude": 0.0, "longitude": 0.0, ... }
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class IPApiBean {

    /** The resolved IP address. */
    private String ip;

    /** City name. */
    private String city;

    /** Region / state name. */
    private String region;

    /** Two-letter country code (e.g. "US"). */
    private String country;

    /** Postal / ZIP code. */
    private String postal;

    /** Latitude coordinate. */
    private double latitude;

    /** Longitude coordinate. */
    private double longitude;

    /** Required no-arg constructor for Jackson deserialization. */
    public IPApiBean() {
        // empty
    }

    /**
     * Gets the IP address.
     *
     * @return ip string
     */
    public String getIp() {
        return ip;
    }

    /**
     * Sets the IP address.
     *
     * @param ip the IP address
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * Gets the city.
     *
     * @return city name
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the city.
     *
     * @param city the city name
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Gets the region.
     *
     * @return region name
     */
    public String getRegion() {
        return region;
    }

    /**
     * Sets the region.
     *
     * @param region the region name
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * Gets the country code.
     *
     * @return two-letter country code
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the country code.
     *
     * @param country the country code
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Gets the postal code.
     *
     * @return postal code string
     */
    public String getPostal() {
        return postal;
    }

    /**
     * Sets the postal code.
     *
     * @param postal the postal code
     */
    public void setPostal(String postal) {
        this.postal = postal;
    }

    /**
     * Gets the latitude.
     *
     * @return latitude as a double
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Sets the latitude.
     *
     * @param latitude the latitude value
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Gets the longitude.
     *
     * @return longitude as a double
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude.
     *
     * @param longitude the longitude value
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Returns a string representation of this bean.
     *
     * @return string representation
     */
    @Override
    public String toString() {
        return "IPApiBean [ip=" + ip + ", city=" + city + ", region=" + region
                + ", country=" + country + ", postal=" + postal
                + ", latitude=" + latitude + ", longitude=" + longitude + "]";
    }
}
