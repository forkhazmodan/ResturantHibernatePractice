package max.requests.MenuItem;

public class CreateMenuItemRequest {

    private String name;

    private int weight;

    private long price;

    private boolean isDiscount;

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }

    public long getPrice() {
        return price;
    }

    public boolean isDiscount() {
        return isDiscount;
    }

    @Override
    public String toString() {
        return "CreateMenuItemRequest{" +
                "name='" + name + '\'' +
                ", weight=" + weight +
                ", price=" + price +
                ", isDiscount=" + isDiscount +
                '}';
    }
}
