package view;

import flowers.Flower;

public class Order {
    private Flower flower;
    private int count;

    public Order(Flower flower, int count) {
        this.flower = flower;
        this.count = count;
    }

    @Override
    public String toString() {
        return flower.toString() + " "+ count;
    }

    public Flower getFlower() {
        return flower;
    }

    public int getCount() {
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (count != order.count) return false;
        return flower != null ? flower.equals(order.flower) : order.flower == null;
    }

    @Override
    public int hashCode() {
        int result = flower != null ? flower.hashCode() : 0;
        result = 31 * result + count;
        return result;
    }
}
