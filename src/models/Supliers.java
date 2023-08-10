
package models;


public class Supliers {
    private int id;
    private String name;
    private String description;
    private String adress;
    private String telephone;
    private String email;
    private String city;
    private String created;
    private String updated;

    public Supliers() {
    }

    public Supliers(int id, String name, String description, String adress, String telephone, String email, String city, String created, String updated) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.adress = adress;
        this.telephone = telephone;
        this.email = email;
        this.city = city;
        this.created = created;
        this.updated = updated;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }
    
    
}
