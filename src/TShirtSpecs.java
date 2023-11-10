class TShirtSpecs{
    private double minPrice;
    private double maxPrice;
    private String brand;
    private Size size;
    private String neckLineInfo;


    public TShirtSpecs(double minPrice, double maxPrice, String brand, Size size, String neckLineInfo) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.brand = brand;
        this.size = size;
        this.neckLineInfo = neckLineInfo;
    }

    public String getNeckLine(){return neckLineInfo;}
    public void setNeckLine(String neckLine){ this.neckLineInfo = neckLine;}
    public void setBrand(String brand){this.brand = brand;}
    public String getBrand() {return brand;}
    public void setSize(Size size) {this.size = size;}
    public Size getSize() {return size;}
    public double getMinPrice() {
        return minPrice;
    }
    public double getMaxPrice() {
        return maxPrice;
    }
    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }
    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public static boolean compareSpecs(String spec1, String otherSpec){
        return spec1.equals(otherSpec);
    }


}