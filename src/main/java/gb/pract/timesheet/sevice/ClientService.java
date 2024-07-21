package gb.pract.timesheet.sevice;

import gb.pract.timesheet.model.Client;
import gb.pract.timesheet.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public Client saveClient(Client client){
        clientRepository.save(client);
        return client;
    }
}
