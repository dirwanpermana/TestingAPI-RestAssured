package testng.data;

public class Product {
    private String name;
    private double price;

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Product))
            return false;

        Product product = (Product) o;

        if (Double.compare(product.price, price) != 0)
            return false;
        if (name != null) {
            if (product.name != null) {
                if (name.equals(product.name)) {
                    return true;
                }
            }
        }

        return false;
    }
}
