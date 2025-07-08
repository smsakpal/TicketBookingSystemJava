package ticket.booking.services;


public class UserBookingService {

    private User user;

    private List<User> userList;

    private final ObjectMapper objectMapper;

    private final String USERS_PATH = "app/src/main/java/ticket/booking/localDb/users.json";


    public UserBookingService() throws IOException{
        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        loadUsers();
    }
    
}
