import java.util.*;

public class App {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        MatchingEngine engine = new MatchingEngine();
        String input;
        
        while (true) {
            input = sc.nextLine().trim();
            if (input.equalsIgnoreCase("exit")) {
                break;
            }
            
            String[] split = input.split(" ");
            if (split.length == 0) continue;
            
            String command = split[0].toLowerCase();
            switch (command) {
                case "limit":
                    if (split.length < 4) continue;
                    String side = split[1];
                    double price = Double.parseDouble(split[2]);
                    int qty = Integer.parseInt(split[3]);
                    engine.createLimitOrder(side, price, qty);
                    break;
                case "market":
                    if (split.length < 3) continue;
                    side = split[1];
                    qty = Integer.parseInt(split[2]);
                    engine.processMarketOrder(side, qty);
                    break;
                case "cancel":
                    if (split.length < 3 || !split[1].equalsIgnoreCase("order")) continue;
                    String orderId = split[2];
                    engine.cancelOrder(orderId);
                    break;
                case "alter":
                    if (split.length < 5) {
                        System.out.println("Entrada inválida para alteração de ordem. Use: alter order <orderId> <newPrice> <newQty>");
                        continue;
                    }
                    if (!split[1].equalsIgnoreCase("order")) {
                        System.out.println("Comando inválido para alteração.");
                        continue;
                    }
                    orderId = split[2];
                    double newPrice = Double.parseDouble(split[3]);
                    int newQty = Integer.parseInt(split[4]);
                    engine.alterOrder(orderId, newPrice, newQty);
                    break;
                case "peg":
                    if (split.length < 4) {
                        System.out.println("Entrada inválida para comando peg. Use: peg <bid|offer> <side> <qty>");
                        continue;
                    }
                    String pegRef = split[1].toLowerCase();
                    side = split[2];
                    qty = Integer.parseInt(split[3]);
                    engine.createPegOrder(pegRef, side, qty);
                    break;
                case "book":
                    engine.printOrderBook();
                    break;
                default:
                    System.out.println("Comando não reconhecido.");
            }
        }
        
        sc.close();
    }
}
