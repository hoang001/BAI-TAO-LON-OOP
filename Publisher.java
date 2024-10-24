public class Publisher {
    private String name;
    private String Address;
    private String ContactEmail;
    private String ContactPhone;

    public Publisher() {
        this.name = "";
        this.Address = "";
        this.ContactEmail = "";
        this.ContactPhone = "";
    }

    public Publisher(String name) {
        this.name = name;
    }

    public Publisher(String name, String address) {
        this.name = name;
        this.Address = address;
    }

    public Publisher(String name, String address, String contactEmail) {
        this.name = name;
        this.Address = address;
        this.ContactEmail = contactEmail;
    }

    public Publisher(String name, String address, String contactEmail, String contactPhone) {
        this.name = name;
        this.Address = address;
        this.ContactEmail = contactEmail;
        this.ContactPhone = contactPhone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        this.Address = address;
    }

    public String getContactEmail() {
        return ContactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.ContactEmail = contactEmail;
    }

    public String getContactPhone() {
        return ContactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.ContactPhone = contactPhone;
    }

    @Override
    public String toString() {
        return null;
    }
}
