import java.util.*;

public class App {
    // Contador para gerar identificadores únicos para as ordens.
    private static int orderCounter = 1;

    public static void main(String[] args) throws Exception {
        System.out.println("Matching Engine");
        Scanner sc = new Scanner(System.in);
        String input;
        String[] split;

        // Listas para armazenar ordens limit de compra e venda.
        List<Limit> buyOrders = new ArrayList<>();
        List<Limit> sellOrders = new ArrayList<>();

        while (true) {
            input = sc.nextLine().trim();
            if (input.equalsIgnoreCase("exit")) {
                break;
            }
            
            split = input.split(" ");
            if(split.length == 0) continue;

            // Criação de ordem limit
            if(split[0].equalsIgnoreCase("limit")){
                if(split.length < 4){
                    System.out.println("Entrada inválida para ordem limit.");
                    continue;
                }
                Limit l = new Limit();
                l.setSide(split[1]);
                l.setPrice(Double.parseDouble(split[2]));
                l.setQty(Integer.parseInt(split[3]));
                l.setId("order_" + orderCounter++);
                System.out.println("Order created: " + l.getSide() + " " + l.getQty() + " @ " + l.getPrice() + " " + l.getId());

                if(l.getSide().equalsIgnoreCase("buy")){
                    buyOrders.add(l); 
                } else if(l.getSide().equalsIgnoreCase("sell")){
                    sellOrders.add(l);
                } else {
                    System.out.println("Lado da ordem inválido. Use 'buy' ou 'sell'.");
                }
            } 
            // Processamento de ordem market
            else if(split[0].equalsIgnoreCase("market")){
                if(split.length < 3){
                    System.out.println("Entrada inválida para ordem market.");
                    continue;
                }
                String side = split[1];
                int qty = Integer.parseInt(split[2]);
                Market m = new Market();
                m.setSide(side);
                m.setQty(qty);
                
                // Para acumular e consolidar trades do mesmo preço.
                if(m.getSide().equalsIgnoreCase("buy")){
                    int remaining = m.getQty();
                    Double currentPrice = null;
                    int accumulatedQty = 0;
                    // Itera na ordem de chegada (primeiro inserido, primeiro consumido).
                    Iterator<Limit> iter = sellOrders.iterator();
                    while(remaining > 0 && iter.hasNext()){
                        Limit sellOrder = iter.next();
                        if(sellOrder.getQty() > 0){
                            int tradeQty = Math.min(remaining, sellOrder.getQty());
                            remaining -= tradeQty;
                            sellOrder.setQty(sellOrder.getQty() - tradeQty);
                            
                            if(currentPrice == null) {
                                currentPrice = sellOrder.getPrice();
                                accumulatedQty = tradeQty;
                            } else if(sellOrder.getPrice() == currentPrice) {
                                accumulatedQty += tradeQty;
                            } else {
                                System.out.println("Trade, price: " + currentPrice + ", qty: " + accumulatedQty);
                                currentPrice = sellOrder.getPrice();
                                accumulatedQty = tradeQty;
                            }
                            
                            if(sellOrder.getQty() == 0){
                                iter.remove();
                            }
                        }
                    }
                    if(accumulatedQty > 0){
                        System.out.println("Trade, price: " + currentPrice + ", qty: " + accumulatedQty);
                    }
                    if(remaining > 0){
                        System.out.println("Ordem market buy não foi totalmente preenchida. Quantidade restante: " + remaining);
                    }
                }
                else if(m.getSide().equalsIgnoreCase("sell")){
                    int remaining = m.getQty();
                    Double currentPrice = null;
                    int accumulatedQty = 0;
                    Iterator<Limit> iter = buyOrders.iterator();
                    while(remaining > 0 && iter.hasNext()){
                        Limit buyOrder = iter.next();
                        if(buyOrder.getQty() > 0){
                            int tradeQty = Math.min(remaining, buyOrder.getQty());
                            remaining -= tradeQty;
                            buyOrder.setQty(buyOrder.getQty() - tradeQty);
                            
                            if(currentPrice == null) {
                                currentPrice = buyOrder.getPrice();
                                accumulatedQty = tradeQty;
                            } else if(buyOrder.getPrice() == currentPrice) {
                                accumulatedQty += tradeQty;
                            } else {
                                System.out.println("Trade, price: " + currentPrice + ", qty: " + accumulatedQty);
                                currentPrice = buyOrder.getPrice();
                                accumulatedQty = tradeQty;
                            }
                            
                            if(buyOrder.getQty() == 0){
                                iter.remove();
                            }
                        }
                    }
                    if(accumulatedQty > 0){
                        System.out.println("Trade, price: " + currentPrice + ", qty: " + accumulatedQty);
                    }
                    if(remaining > 0){
                        System.out.println("Ordem market sell não foi totalmente preenchida. Quantidade restante: " + remaining);
                    }
                } else {
                    System.out.println("Lado da ordem inválido. Use 'buy' ou 'sell'.");
                }
            } 
            // Cancelamento de ordem
            else if(split[0].equalsIgnoreCase("cancel")){
                if(split.length < 3){
                    System.out.println("Entrada inválida para cancelamento.");
                    continue;
                }
                if(!split[1].equalsIgnoreCase("order")){
                    System.out.println("Comando de cancelamento inválido.");
                    continue;
                }
                String orderId = split[2];
                boolean removed = false;
                Iterator<Limit> iterBuy = buyOrders.iterator();
                while(iterBuy.hasNext()){
                    Limit order = iterBuy.next();
                    if(order.getId().equals(orderId)){
                        iterBuy.remove();
                        removed = true;
                        break;
                    }
                }
                if(!removed){
                    Iterator<Limit> iterSell = sellOrders.iterator();
                    while(iterSell.hasNext()){
                        Limit order = iterSell.next();
                        if(order.getId().equals(orderId)){
                            iterSell.remove();
                            removed = true;
                            break;
                        }
                    }
                }
                if(removed){
                    System.out.println("Order cancelled");
                } else {
                    System.out.println("Order not found");
                }
            }
            // Visualização do livro de ordens
            else if(split[0].equalsIgnoreCase("book")){
                System.out.println("----- Order Book -----");
                System.out.println("Buy Orders:");
                for(Limit order : buyOrders){
                    System.out.println(order);
                }
                System.out.println("Sell Orders:");
                for(Limit order : sellOrders){
                    System.out.println(order);
                }
                System.out.println("----------------------");
            } else {
                System.out.println("Comando não reconhecido.");
            }
        }
        
        sc.close();
    }
}