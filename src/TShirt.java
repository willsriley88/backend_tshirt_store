import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

public class TShirt {
    private final String name;
    private final long productCode;
    private final double price;

    private final String description;
    private final Set<Size> availableSizes;
    public final TShirtSpecs TShirtSpecifications;


    public TShirt(String name,long productCode, double price,  String description, Set<Size> availableSizes, TShirtSpecs TShirtSpecifications) {
        this.name=name;
        this.productCode = productCode;
        this.price = price;
        this.description = description;
        this.availableSizes = new HashSet<>(availableSizes);
        this.TShirtSpecifications = new TShirtSpecs(TShirtSpecifications.getMinPrice(), TShirtSpecifications.getMaxPrice(), TShirtSpecifications.getBrand(), TShirtSpecifications.getSize(), TShirtSpecifications.getNeckLine());
    }

    public String getName(){
        return name;
    }
    public long getProductCode() {
        return productCode;
    }
    public double getPrice() {
        return price;
    }
    public String getDescription(){
        return description;
    }
    public Set<Size> getSizes() {
        return new HashSet<>(availableSizes);
    }


    public String getTShirtInformation(){
        DecimalFormat df = new DecimalFormat("0.00");
        return "\nItem name: "+this.getName()+"\nCaption: "+this.getDescription() +"\n\tProduct code: "+this.getProductCode()+"\n\tBrand: "
                +this.TShirtSpecifications.getBrand()+"\n\tNeckLine: "+this.TShirtSpecifications.getNeckLine()+"\n\tPrice: $"+df.format(this.getPrice())+"\n";
    }
}
