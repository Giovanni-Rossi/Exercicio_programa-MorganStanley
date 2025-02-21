public class Peg extends Limit {
    private PriceWrapper priceRef;

    public Peg(String side, int qty, PriceWrapper priceRef) {
        this.setSide(side);
        this.setQty(qty);
        this.priceRef = priceRef;
        this.setPrice(priceRef.getValue());
    }

    public double getPrice() {
        return priceRef.getValue();
    }

    public void setPriceRef(PriceWrapper priceRef) {
        this.priceRef = priceRef;
    }
    
    public void updatePrice() {
        this.setPrice(priceRef.getValue());
    }
    
    public String toString() {
        return "Peg " + getSide() + " " + getPrice() + " " + getQty() + " " + getId() + " [ref=" + priceRef.getValue() + "]";
    }
}
