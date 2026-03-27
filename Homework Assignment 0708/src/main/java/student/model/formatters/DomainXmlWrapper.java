package student.model.formatters;

import java.util.Collection;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import student.model.DomainNameModel.DNRecord;

/**
 * This wrapper helps when using Jackson to serialize a list of domain records to xml. Without this,
 * it tries to use <ArrayList> and <item> tags instead of <domainList> and <domain> tags.
 *
 * Suggested use (note you need try/catch with this)
 *
 * <pre>
 * XmlMapper mapper = new XmlMapper();
 * mapper.enable(SerializationFeature.INDENT_OUTPUT);
 * DomainXmlWrapper wrapper = new DomainXmlWrapper(records);
 * mapper.writeValue(out, wrapper);
 * </pre>
 */
@JacksonXmlRootElement(localName = "domainList")
public final class DomainXmlWrapper {

    /** List of the records. */
    @JacksonXmlElementWrapper(useWrapping = false)
    private Collection<DNRecord> domain;

    /**
     * Constructor.
     *
     * @param records the records to wrap
     */
    public DomainXmlWrapper(Collection<DNRecord> records) {
        this.domain = records;
    }

    /**
     * Required no-arg constructor for Jackson deserialization.
     */
    public DomainXmlWrapper() {
        // empty
    }

    /**
     * Gets the collection of domain records.
     *
     * @return the domain records
     */
    public Collection<DNRecord> getDomain() {
        return domain;
    }

    /**
     * Sets the collection of domain records.
     * Required for Jackson deserialization.
     *
     * @param domain the domain records to set
     */
    public void setDomain(Collection<DNRecord> domain) {
        this.domain = domain;
    }
}
