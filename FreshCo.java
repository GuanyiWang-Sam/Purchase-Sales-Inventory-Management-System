package shoppingMall;

import java.io.*;
import java.util.*;

public class FreshCo implements Shopping {

    private List<Product> productList = new ArrayList<>(10);
    private List<Product> purchasedList = new ArrayList<>(10);

    private boolean validName(Product product) throws ShoppingException {
        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).equalsName(product.getName())) {
                throw new ShoppingException("Name [" + product.getName() + "] is already existed!");
            }
        }
        return true;
    }

    private boolean validPriority(Product product) {
        return validNutrition(product) && validSweetness(product);
    }

    private boolean validNutrition(Product product) {
        for (Product product1 : productList) {
            if (product instanceof Fruit && product1 instanceof Fruit
                    && (((Fruit) product).getNutrition() == ((Fruit) product1).getNutrition())) {
                throw new ShoppingException(String.format("Nutrition %d is already existed!", ((Fruit) product).getNutrition()));
            }
        }
        return true;
    }

    private boolean validSweetness(Product product) {
        for (Product product1 : productList) {
            if (product instanceof Fruit && product1 instanceof Fruit) {
                if (((Fruit) product).getSweetness() == ((Fruit) product1).getSweetness()) {
                    throw new ShoppingException(String.format("Sweetness %d is already existed!", ((Fruit) product).getSweetness()));
                }
            }
        }
        return true;
    }

    public void inputFromConsole() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("please input kinds of product:");
        int size = scanner.nextInt();
        for (int i = 0; i < size; i++) {
            try {
                Fruit fruit = new Fruit();
                fruit.generate();
                if (validName(fruit) && validPriority(fruit)) {
                    productList.add(fruit);
                }
            } catch (ShoppingException e) {
                e.printStackTrace();
                System.out.println(e.getMessage() + " !Please input again");
                i--;
            }
        }
        writeListToFile(new File("list.txt"));
    }

    @Override
    public void input() {

        productList = new ArrayList<>();
        File file = new File("list.txt");
        if (file.exists()) {
            readFromFile(file);
            if (productList.isEmpty()) {
                inputFromConsole();
            }
        } else {
            inputFromConsole();
        }
    }

    private void readFromFile(File file) {
        productList = new ArrayList<>();
        if (!file.exists()) {
            return;
        }
        try {
            InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
            try (BufferedReader bufferedReader = new BufferedReader(reader)) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    String fruitStr = line.split("\\|")[1];
                    String[] properties = fruitStr.split(",");
                    String name = properties[0].trim().split("=")[1];
                    double price = Double.parseDouble(properties[1].trim().split("=")[1]);
                    int priority = Integer.parseInt(properties[2].trim().split("=")[1]);
                    int quantity = Integer.parseInt(properties[3].trim().split("=")[1]);
                    int sweetness = Integer.parseInt(properties[4].trim().split("=")[1]);
                    int nutrition = Integer.parseInt(properties[5].trim().split("=")[1]);

                    Fruit fruit = new Fruit();
                    fruit.setName(name);
                    fruit.setPriority(priority);
                    fruit.setPrice(price);
                    fruit.setNutrition(nutrition);
                    fruit.setSweetness(sweetness);
                    fruit.setQuantity(quantity);

                    productList.add(fruit);
                }
            } catch (IOException e) {
                // ... handle IO exception
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void writeListToFile(File file) {
        sort();
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8")) {
                for (Product product : productList) {
                    writer.write(product.toString() + "\n");
                }
                writer.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void sort() {

        productList.sort(new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                if (o1.getPriority() == o2.getPriority()) {
                    return o1.getQuantity() - o2.getQuantity();
                } else {
                    return o1.getPriority() - o2.getPriority();
                }
            }
        });
    }

    public Product findName(String name) {
        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).getName().equals(name)) {
                return productList.get(i);
            }
        }
        throw new ShoppingException("Wrong name.");
    }

    public boolean validQuantity(Product product, int quantity) {
        if (!product.soldOut() && product.getQuantity() - quantity >= 0) {
            return true;
        }
        throw new ShoppingException("Quantity is too large");
    }

    public boolean validBalance(double balance, double cost) {
        if ((balance - cost) >= 0) {
            return true;
        }
        throw new ShoppingException("Your balance is: " + balance + ", you can't afford!");
    }

    @Override
    public void shopping() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please input your userName: ");
        String userName = scanner.nextLine();

        System.out.println("Please input your bankAccount: ");
        double bankAccount = scanner.nextDouble();

        System.out.println("--Priority--------------------: ");
        showProductList();
        System.out.println("-------------------------------------");

        double totalMoney = 0;
        while (scanner.hasNextLine()) {
            System.out.println("Please input the product name, or 'e' exit");
            String enter = scanner.next();
            if (enter.equals("e")) {
                System.out.println("Your bankAccount is [" + bankAccount + "] before shopping;");
                bankAccount = bankAccount - totalMoney;
                System.out.println("Your bankAccount is [" + bankAccount + "] after shopping");
                Iterator<Product> iterator = productList.iterator();
                while (iterator.hasNext()) {
                    Product product = iterator.next();
                    if (product.getQuantity() == 0) {
                        iterator.remove();
                    }
                }
                writeListToFile(new File("list.txt"));
                break;
            } else {
                try {
                    Product product = findName(enter);
                    System.out.println("Please input your purchase quantity: ");
                    int quantity = scanner.nextInt();
                    if (validQuantity(product, quantity)) {
                        double cost = product.getPrice() * quantity;
                        if (validBalance((bankAccount - totalMoney), cost)) {
                            totalMoney += cost;
                            product.setQuantity(product.getQuantity() - quantity);
                            Product purchase = new Product() {
                                @Override
                                public Product generate() {
                                    setName(product.getName());
                                    setPriority(product.getPriority());
                                    setPrice(product.getPrice());
                                    setQuantity(quantity);
                                    return this;
                                }
                            };
                            purchasedList.add(purchase.generate());
                            System.out.println("Balance: " + (bankAccount - totalMoney));
                        }
                    }
                } catch (ShoppingException e) {
                    System.out.println(e.getMessage());
                    System.out.println("Please input again");
                }

            }

        }

    }

    @Override
    public void print() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("enter 1 to view purchased items and 0 to view non-purchased items,or 'e' exit: ");

        while (scanner.hasNextLine()) {
            String enter = scanner.nextLine();
            if ("e".equals(enter)) {
                break;
            } else if ("1".equals(enter)) {
                System.out.println("You purchased:-------------");
                showPurchasedList();
                System.out.println("---------------------");
            } else if ("0".equals(enter)) {
                System.out.println("Non-purchased items Remaining in the list(the list after shopping):");
                System.out.println("-------------------------------------");
                showProductList();
                System.out.println("-------------------------------------");
            }
            System.out.println("enter 1 to view purchased items and 0 to view non-purchased items, or 'e' exit: ");
        }

        scanner.close();
    }

    private void showProductList() {
        readFromFile(new File("list.txt"));
        for (int i = 0; i < productList.size(); i++) {
            Product product = productList.get(i);
            if (product instanceof Fruit) {
                System.out.println((i + 1) + ". " + ((Fruit) product).toString());
            }
        }
    }

    private void showPurchasedList() {
        for (int i = 0; i < purchasedList.size(); i++) {
            Product product = purchasedList.get(i);
            System.out.println((i + 1) + ". " + product.getName() + " * " + product.getQuantity());
        }
    }

}
