package shoppingMall;

public abstract class Product {

    private String name;
    private double price;
    private int priority;
    private int quantity;

    public Product() {
    }

    public Product(String name, double price, int priority, int quantity) {
        this.name = name;
        this.price = price;
        this.priority = priority;
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    abstract public Product generate();

    public boolean equalsName(String name) {
        return this.name.equals(name);
    }

    public boolean equalsPriority(int priority) {
        return this.priority == priority;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean soldOut() {
        return this.quantity <= 0;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", priority=" + priority +
                ", quantity=" + quantity +
                '}';
    }
}

