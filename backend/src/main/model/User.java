@Entity 
public class User { 
    @Id 
    @GeneratedValue(strategy = GenerationType.AUTO) 
    private long id; 
    
    private String name; 
    private String email;
    private String address;
    
    // Constructor necesario para la carga desde BBDD 
    protected User() {} 
    
    public User(String name, String email, String address) {
        this.name = name; 
        this.email = email; 
        this.address = address;
    }

}