import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class ShopService {
    private ProductRepo productRepo = new ProductRepo();
    private OrderRepo orderRepo = new OrderMapRepo();

    public Order addOrder(List<String> productIds) {
        List<Product> products = new ArrayList<>();
        for (String productId : productIds) {
            Optional<Product> productToOrder = productRepo.getProductById(productId);
            if (productToOrder.isPresent()) {
                products.add(productToOrder.get());
            }
            else {
                throw new IllegalArgumentException("Product mit der Id: " + productId + " konnte nicht bestellt werden!");
            }
        }

        Order newOrder = new Order(UUID.randomUUID().toString(), products, OrderStatus.PROCESSING, Instant.now());

        return orderRepo.addOrder(newOrder);
    }

    public List<Order> getOrdersByStatus (OrderStatus status) {
        List<Order> orders = orderRepo.getOrders();

        return orders.stream()
                .filter(order -> order.orderStatus() == status)
                .collect(Collectors.toList());
    }

    public Order updateOrder(String orderId, OrderStatus newOrderStatus) {
        Order newOrder = orderRepo.getOrderById(orderId);
        if (newOrder == null) {
            throw new IllegalArgumentException("Order mit der ID " + orderId + " konnte nicht gefunden werden!");
        }

        Order oldOrder = newOrder.withOrderStatus(newOrderStatus);
        orderRepo.addOrder(oldOrder);
        return newOrder;
    }
}
