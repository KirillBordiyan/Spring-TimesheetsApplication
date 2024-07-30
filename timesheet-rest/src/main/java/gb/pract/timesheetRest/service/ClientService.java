package gb.pract.timesheetRest.service;

import gb.pract.timesheetRest.model.Client;
import gb.pract.timesheetRest.repository.ClientRepository;
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
