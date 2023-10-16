public class Expense{
  private Double price;
  private String name;
  private Set<String> errorDescription;
  private Integer date;
  
  public Expense(){
    errorDescription = new HashSet<>();
  }
  public Expense(String tName, Integer tDate, Double tPrice){
    errorDescription = new HashSet<>();
    name = tName;
    date = tDate
    price = tPrice;
  }
  public void addError(String error){
    errorDescription.add(error);
  }
  public String removeError(String error){
    if (errorDescription.remove(error){
      return "Error: " + error + " was removed";
    }else{
      return "Error: " + error + " was not found";
    }
  }
  public void Set<String> getErrors {
    return errorDescription;
  }
  public void setPrice(Double newPrice){
    price = newPrice;
  }
  public Double getPrice(){return price;}
  public void setName(String newName){
    name = newName;
  }
  public String getName(){return name;}
  public void setDate(Integer newDate){
    date = newDate;
  }
  public Integer getDate(){return date;}
  
};