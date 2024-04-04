public class Car{
    String brand;
    String model;
    int horsePower;
    int price;

    public  Car(String brand, String model, int horsePower, int price) {
        this.brand = brand;
        this.model = model;
        this.horsePower = horsePower;
        this.price = price;
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Car other = (Car) obj;
        return this.brand.equals(other.brand) && 
               this.model.equals(other.model) && 
               this.horsePower == other.horsePower &&
               this.price == other.price;
    }

    @Override
    public int hashCode() {
        int result = brand != null ? brand.hashCode() : 0;
        result = 31 * result + (model != null ? model.hashCode() : 0);
        result = 31 * result + horsePower;
        result = 31 * result + price;
        return result;
    }

    public String toString(){
        return " Brand: " + brand + " Model: " + model + " Horse Power: " + horsePower + " Price: " + price +"\r\n";      
    }
}