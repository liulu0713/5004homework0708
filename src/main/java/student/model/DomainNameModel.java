package student.model;

import java.io.OutputStream;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import student.model.formatters.DataFormatter;
import student.model.formatters.Formats;


/**
 * Interface to the model.
 * 
 * We have left a few of these methods as examples, but you are free to change this class as you
 * need. The exception is the data/hostrecords.xml file, which should be used as the default data
 * file.
 * 
 */
public interface DomainNameModel {
    /** Do not change the file address! */
    String DATABASE = "data/hostrecords.xml";

    /**
     * Get the records as a list.
     * 
     * @return the list of records
     */
    List<DNRecord> getRecords();

    /**
     * Gets a single record by hostname.
     * 
     * If the record does not exist, gets the information based off the IP address, builds the
     * record, adds (and saves) it to hostrecords.xml, then returns the new record.
     * 
     * @param hostname the hostname to look up
     * @return the record
     * @see NetUtils#lookUpIp(String)
     * @see NetUtils#getIpDetails(String, Formats)
     */
    DNRecord getRecord(String hostname);

    /**
     * Writes out the records to the outputstream.
     * 
     * OutputStream could be System.out or a FileOutputStream.
     * 
     * @param records the records to write, could be a single entry.
     * @param format the format to write the records in
     * @param out the output stream to write to
     */
    static void writeRecords(List<DNRecord> records, Formats format, OutputStream out) {
        DataFormatter.write(records, format, out);
    }


    /**
     * Gets an instance of the model using the 'default' location.
     * 
     * @return the instance of the model
     */
    static DomainNameModel getInstance() {
        return new DomainNameModelImpl(DATABASE);
    }

    /**
     * Gets an instance of the model using the 'default' class.
     * 
     * Good spot to get the InputStream from the DATABASE file, and use that stream to build the
     * model.
     * 
     * From another class this would be called like
     * 
     * <pre>
     * DomainNameModel model = DomainNameModel.getInstance();
     * </pre>
     * 
     * @param database the name of the file to use
     * @return the instance of the model
     */
    static DomainNameModel getInstance(String database) {
        return new DomainNameModelImpl(database);
        // you will want to implement this specific to your type of model - if you use it!
    }


    /**
     * Primary record to pass around between objects. Is immutable and uses Jackson annotations for
     * serialization.
     * 
     * @param hostname the hostname
     * @param ip the IP address
     * @param city the city
     * @param region the region
     * @param country the country
     * @param postal the postal code
     * @param latitude the latitude
     * @param longitude the longitude
     * @return the record
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JacksonXmlRootElement(localName = "domain")
    @JsonPropertyOrder({"hostname", "ip", "city", "region", "country", "postal", "latitude",
            "longitude"})
    record DNRecord(String hostname, String ip, String city, String region, String country,
            String postal, double latitude, double longitude) {
    }

}
