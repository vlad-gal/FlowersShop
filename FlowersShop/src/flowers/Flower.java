package flowers;

public abstract class Flower {
    private String type;
    private String color;
    private String size;
    private double cost;
    private String description;

    public Flower() {
    }

    public Flower(String type, String color, String size, double cost, String description) {
        this.type = type;
        this.color = color;
        this.size = size;
        this.cost = cost;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return type +" "+ color +" "+ size +" "+ cost +" "+ description;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Flower flower = (Flower) o;

        if (Double.compare(flower.cost, cost) != 0) return false;
        if (type != null ? !type.equals(flower.type) : flower.type != null) return false;
        if (color != null ? !color.equals(flower.color) : flower.color != null) return false;
        if (size != null ? !size.equals(flower.size) : flower.size != null) return false;
        return description != null ? description.equals(flower.description) : flower.description == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = type != null ? type.hashCode() : 0;
        result = 31 * result + (color != null ? color.hashCode() : 0);
        result = 31 * result + (size != null ? size.hashCode() : 0);
        temp = Double.doubleToLongBits(cost);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
