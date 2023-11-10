import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Inventory {

    private final Set<TShirt> allTShirts = new HashSet<>();

    public void addTShirt(TShirt tshirt){
        this.allTShirts.add(tshirt);
    }

    public Set<String> getAllBrands(){
        Set<String> allBrands = new HashSet<>();
        for(TShirt tee: allTShirts){
            allBrands.add(tee.TShirtSpecifications.getBrand());
        }
        return allBrands;
    }

    public Set<String> getAllNeckLines(){
        Set<String> allNeckLines = new HashSet<>();
        for(TShirt tee: allTShirts){
            allNeckLines.add(tee.TShirtSpecifications.getNeckLine());
        }
        return allNeckLines;
    }

    public List<TShirt> findMatch(TShirtSpecs dreamTShirt){
        List<TShirt> matchingTShirts = new ArrayList<>();
        for(TShirt tShirt: allTShirts){
            if(!tShirt.TShirtSpecifications.getBrand().equals(dreamTShirt.getBrand())) continue;
            if(!tShirt.getSizes().contains(dreamTShirt.getSize())) continue;
            if(tShirt.getPrice()<dreamTShirt.getMinPrice()||tShirt.getPrice()>dreamTShirt.getMaxPrice()) continue;
            if(!TShirtSpecs.compareSpecs(tShirt.TShirtSpecifications.getNeckLine(), dreamTShirt.getNeckLine())) continue;
            matchingTShirts.add(tShirt);
        }
        return matchingTShirts;
    }

}
