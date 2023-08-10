
package models;


public class Purchases {
    private int id;
    private int code;
    private String product_name;
    private int purchase_amount;
    private double purchase_price;
    private double purchase_subtotal;
    private double total;
    private String created;
    private String suplier_name_product;
    private String purcharser;

    public Purchases() {
    }

    public Purchases(int id, int code, String product_name, int product_amount, double product_price, double product_subtotal, double total, String created, String suplier_name_product, String purcharser) {
        this.id = id;
        this.code = code;
        this.product_name = product_name;
        this.purchase_amount = product_amount;
        this.purchase_price = product_price;
        this.purchase_subtotal = product_subtotal;
        this.total = total;
        this.created = created;
        this.suplier_name_product = suplier_name_product;
        this.purcharser = purcharser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getPurchase_amount() {
        return purchase_amount;
    }

    public void setPurchase_amount(int purchase_amount) {
        this.purchase_amount = purchase_amount;
    }

    public double getPurchase_price() {
        return purchase_price;
    }

    public void setPurchase_price(double purchase_price) {
        this.purchase_price = purchase_price;
    }

    public double getPurchase_subtotal() {
        return purchase_subtotal;
    }

    public void setPurchase_subtotal(double purchase_subtotal) {
        this.purchase_subtotal = purchase_subtotal;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getSuplier_name_product() {
        return suplier_name_product;
    }

    public void setSuplier_name_product(String suplier_name_product) {
        this.suplier_name_product = suplier_name_product;
    }

    public String getPurcharser() {
        return purcharser;
    }

    public void setPurcharser(String purcharser) {
        this.purcharser = purcharser;
    }

    
}
