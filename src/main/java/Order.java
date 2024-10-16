import java.time.Instant;
import java.util.List;
import lombok.With;

public record Order(
        String id,
        List<Product> products,
        @With OrderStatus orderStatus,
        Instant orderTimestamp
) {
}
