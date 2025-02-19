// Classe para representar uma ordem market.
class Market {
    private String side; // "buy" ou "sell"
    private int qty;
    
    public String getSide() {
        return side;
    }
    public void setSide(String side) {
        this.side = side;
    }
    public int getQty() {
        return qty;
    }
    public void setQty(int qty) {
        this.qty = qty;
    }
    
    @Override
    public String toString(){
        return "Market " + side + " " + qty;
    }
}