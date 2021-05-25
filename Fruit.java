package shoppingMall;

import java.io.Serializable;
import java.util.Scanner;

public class Fruit extends Product implements Serializable {
    private int sweetness;

    private int nutrition;

    public Fruit() {

    }

    public Fruit(String name, double price, int priority, int quantity, int sweetness) {
        super(name, price, priority, quantity);
        this.sweetness = sweetness;
    }

    public int getSweetness() {
        return sweetness;
    }

    public void setSweetness(int sweetness) {
        this.sweetness = sweetness;
    }

    public int getNutrition() {
        return nutrition;
    }

    public void setNutrition(int nutrition) {
        this.nutrition = nutrition;
    }

    @Override
    public Product generate() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Add Product into list: ");
        System.out.println("Name:");
        String name = scanner.next();
        setName(name);

        System.out.println("Price: ");
        double price = scanner.nextDouble();
        setPrice(price);

        System.out.println("Priority: ");
        int priority = scanner.nextInt();
        setPriority(priority);

        System.out.println("Quantity: ");
        int quantity = scanner.nextInt();
        setQuantity(quantity);

        System.out.println("Sweetness: ");
        int sweetness = scanner.nextInt();
        setSweetness(sweetness);

        System.out.println("Nutrition:");
        int nutrition = scanner.nextInt();
        setNutrition(nutrition);

        return this;
    }

    @Override
    public String toString() {
        return "Fruit|" +
                "name=" + this.getName() +
                ", price=" + this.getPrice() +
                ", priority=" + this.getPriority() +
                ", quantity=" + this.getQuantity() +
                ", sweetness=" + sweetness +
                ", nutrition=" + nutrition;
    }
}
