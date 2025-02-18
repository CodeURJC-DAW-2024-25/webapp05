@Controller
public class DataBaseInit implements CommandLineRunner {

    @Autowired
    private UserRepository repository;

    @Override
    public void run(String... args) throws Exception {
        repository.save(new User("Alicia Rodriguez", "aliciaRo@gmail,com" , "Calle Eugenia de Montijo 76"));
        //falta añadir un admin 
    }
}