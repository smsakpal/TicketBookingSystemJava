package ticket.booking.services;


public class TrainService {

    private List<Train> trainList;
    private final ObjectMapper objectMapper;
    private static final String TRAIN_DB_PATH = "app/src/main/java/ticket/booking/localDb/trains.json";

    public TrainService() throws IOException{
        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        loadTrains();
    }

    

}
