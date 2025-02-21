import java.util.*;

public class MatchingEngine {
    private PriceWrapper bidWrapper;
    private PriceWrapper offerWrapper;
    private List<Limit> buyOrders;
    private List<Limit> sellOrders;
    private int orderCounter = 1;
    
    public MatchingEngine() {
        bidWrapper = new PriceWrapper(0.0);
        offerWrapper = new PriceWrapper(Double.MAX_VALUE);
        buyOrders = new ArrayList<>();
        sellOrders = new ArrayList<>();
    }
    
    public void createLimitOrder(String side, double price, int qty) {
        Limit l = new Limit();
        l.setSide(side);
        l.setPrice(price);
        l.setQty(qty);
        l.setId("order_" + orderCounter++);
        System.out.println("Order created: " + l.getSide() + " " + l.getQty() +
                " @ " + l.getPrice() + " " + l.getId());
        
        if (side.equalsIgnoreCase("buy")) {
            if (price > bidWrapper.getValue()) {
                bidWrapper.setValue(price);
            }
            buyOrders.add(l);
        } else if (side.equalsIgnoreCase("sell")) {
            if (price < offerWrapper.getValue()) {
                offerWrapper.setValue(price);
            }
            sellOrders.add(l);
        }
    }
    
    public void processMarketOrder(String side, int qty) {
        if (side.equalsIgnoreCase("buy")) {
            int remaining = qty;
            Double currentPrice = null;
            int accumulatedQty = 0;
            Iterator<Limit> iter = sellOrders.iterator();
            while (remaining > 0 && iter.hasNext()) {
                Limit sellOrder = iter.next();
                if (sellOrder.getQty() > 0) {
                    int tradeQty = Math.min(remaining, sellOrder.getQty());
                    remaining -= tradeQty;
                    sellOrder.setQty(sellOrder.getQty() - tradeQty);
                    
                    if (currentPrice == null) {
                        currentPrice = sellOrder.getPrice();
                        accumulatedQty = tradeQty;
                    } else if (sellOrder.getPrice() == currentPrice) {
                        accumulatedQty += tradeQty;
                    } else {
                        System.out.println("Trade, price: " + currentPrice + ", qty: " + accumulatedQty);
                        currentPrice = sellOrder.getPrice();
                        accumulatedQty = tradeQty;
                    }
                    
                    if (sellOrder.getQty() == 0) {
                        iter.remove();
                    }
                }
            }
            if (accumulatedQty > 0) {
                System.out.println("Trade, price: " + currentPrice + ", qty: " + accumulatedQty);
            }
        } else if (side.equalsIgnoreCase("sell")) {
            int remaining = qty;
            Double currentPrice = null;
            int accumulatedQty = 0;
            Iterator<Limit> iter = buyOrders.iterator();
            while (remaining > 0 && iter.hasNext()) {
                Limit buyOrder = iter.next();
                if (buyOrder.getQty() > 0) {
                    int tradeQty = Math.min(remaining, buyOrder.getQty());
                    remaining -= tradeQty;
                    buyOrder.setQty(buyOrder.getQty() - tradeQty);
                    
                    if (currentPrice == null) {
                        currentPrice = buyOrder.getPrice();
                        accumulatedQty = tradeQty;
                    } else if (buyOrder.getPrice() == currentPrice) {
                        accumulatedQty += tradeQty;
                    } else {
                        System.out.println("Trade, price: " + currentPrice + ", qty: " + accumulatedQty);
                        currentPrice = buyOrder.getPrice();
                        accumulatedQty = tradeQty;
                    }
                    
                    if (buyOrder.getQty() == 0) {
                        iter.remove();
                    }
                }
            }
            if (accumulatedQty > 0) {
                System.out.println("Trade, price: " + currentPrice + ", qty: " + accumulatedQty);
            }
        }
    }
    
    public void cancelOrder(String orderId) {
        boolean removed = removeOrderById(orderId);
        if (removed) {
            System.out.println("Order cancelled");
            updateBidWrapper();
            updateOfferWrapper();
        } else {
            System.out.println("Order not found");
        }
    }
    
    public void alterOrder(String orderId, double newPrice, int newQty) {
        boolean altered = false;
        Iterator<Limit> iterBuy = buyOrders.iterator();
        while (iterBuy.hasNext()) {
            Limit order = iterBuy.next();
            if (order.getId().equals(orderId)) {
                if (order.getPrice() != newPrice) {
                    iterBuy.remove();
                    order.setPrice(newPrice);
                    order.setQty(newQty);
                    buyOrders.add(order);
                } else {
                    order.setQty(newQty);
                }
                altered = true;
                System.out.println("Order altered: " + order.getSide() + " " + order.getQty() +
                        " @ " + order.getPrice() + " " + order.getId());
                break;
            }
        }
        if (!altered) {
            Iterator<Limit> iterSell = sellOrders.iterator();
            while (iterSell.hasNext()) {
                Limit order = iterSell.next();
                if (order.getId().equals(orderId)) {
                    if (order.getPrice() != newPrice) {
                        iterSell.remove();
                        order.setPrice(newPrice);
                        order.setQty(newQty);
                        sellOrders.add(order);
                    } else {
                        order.setQty(newQty);
                    }
                    altered = true;
                    System.out.println("Order altered: " + order.getSide() + " " + order.getQty() +
                            " @ " + order.getPrice() + " " + order.getId());
                    break;
                }
            }
        }
        if (!altered) {
            System.out.println("Order not found");
        }
    }
    
    public void createPegOrder(String pegRef, String side, int qty) {
        if (pegRef.equals("bid") && side.equalsIgnoreCase("buy")) {
            Peg pegOrder = new Peg(side, qty, bidWrapper);
            pegOrder.setId("order_" + orderCounter++);
            System.out.println("Pegged order created: " + pegOrder.getSide() + " " +
                    pegOrder.getQty() + " @ " + pegOrder.getPrice() + " " + pegOrder.getId());
            buyOrders.add(pegOrder);
        } else if (pegRef.equals("offer") && side.equalsIgnoreCase("sell")) {
            Peg pegOrder = new Peg(side, qty, offerWrapper);
            pegOrder.setId("order_" + orderCounter++);
            System.out.println("Pegged order created: " + pegOrder.getSide() + " " +
                    pegOrder.getQty() + " @ " + pegOrder.getPrice() + " " + pegOrder.getId());
            sellOrders.add(pegOrder);
        } else {
            System.out.println("Comando peg inválido para o lado especificado.");
        }
    }
    
    public void printOrderBook() {
        System.out.println("----- Order Book -----");
        System.out.println("Buy Orders:");
        for (Limit order : buyOrders) {
            System.out.println(order);
        }
        System.out.println("Sell Orders:");
        for (Limit order : sellOrders) {
            System.out.println(order);
        }
        System.out.println("----------------------");
    }
    
    private boolean removeOrderById(String orderId) {
        Iterator<Limit> iterBuy = buyOrders.iterator();
        while (iterBuy.hasNext()) {
            Limit order = iterBuy.next();
            if (order.getId().equals(orderId)) {
                iterBuy.remove();
                return true;
            }
        }
        Iterator<Limit> iterSell = sellOrders.iterator();
        while (iterSell.hasNext()) {
            Limit order = iterSell.next();
            if (order.getId().equals(orderId)) {
                iterSell.remove();
                return true;
            }
        }
        return false;
    }
    
    private void updateBidWrapper() {
        double best = 0.0;
        for (Limit order : buyOrders) {
            if (!order.isPegged() && order.getSide().equalsIgnoreCase("buy")) {
                if (order.getPrice() > best) {
                    best = order.getPrice();
                }
            }
        }
        bidWrapper.setValue(best);
    }
    
    private void updateOfferWrapper() {
        double best = Double.MAX_VALUE;
        for (Limit order : sellOrders) {
            if (!order.isPegged() && order.getSide().equalsIgnoreCase("sell")) {
                if (order.getPrice() < best) {
                    best = order.getPrice();
                }
            }
        }
        offerWrapper.setValue(best);
    }
}
