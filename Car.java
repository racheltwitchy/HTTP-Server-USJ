public class Car{
    String brand;
    String model;
    String horsePower;
    int price;

    public  Car(String brand, String model, String horsePower, int price) {
        this.brand = brand;
        this.model = model;
        this.horsePower = horsePower;
        this.price = price;
    }

    public boolean equals(Car c){
        return this.brand.equals(c.brand) && this.model.equals(c.model) && this.horsePower.equals(c.horsePower) ;       
    }

    public String toString(){
        return "Marca: " + brand + " Modelo: " + model + " Cilindrada: " + horsePower + " Precio: " + price + "\r\n";      
    }
}