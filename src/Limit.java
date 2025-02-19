// Classe para representar uma ordem limit.
class Limit {
    private String side;  // "buy" ou "sell"
    private double price;
    private int qty;
    private String id;
    
    public String getSide() {
        return side;
    }
    public void setSide(String side) {
        this.side = side;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public int getQty() {
        return qty;
    }
    public void setQty(int qty) {
        this.qty = qty;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    
    @Override
    public String toString(){
        return "Limit " + side + " " + price + " " + qty + " " + id;
    }
}