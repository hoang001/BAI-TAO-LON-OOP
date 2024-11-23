package org.example.models;

public class PublisherEntity extends BaseEntity{
    private String publisherName;
    private String address;
    private String contactEmail;
    private String contactPhone;

    public PublisherEntity() {}
    public PublisherEntity(int publisherId, String publisherName, String address, String contactEmail, String contactPhone) {
        super(publisherId);
        this.publisherName = publisherName;
        this.address = address;
        this.contactEmail = contactEmail;
        this.contactPhone = contactPhone;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }
}
