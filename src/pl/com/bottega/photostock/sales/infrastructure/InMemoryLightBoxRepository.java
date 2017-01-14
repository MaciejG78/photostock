package pl.com.bottega.photostock.sales.infrastructure;

import pl.com.bottega.photostock.sales.model.Client;
import pl.com.bottega.photostock.sales.model.LightBox;
import pl.com.bottega.photostock.sales.model.LightBoxRepository;

import java.util.*;

/**
 * Created by macie on 13.12.2016.
 */
public class InMemoryLightBoxRepository implements LightBoxRepository {

    private static final Map<Client, Collection<LightBox>> REPOSITORY = new HashMap<>();

    @Override
    public void put(LightBox lightBox) {
        Client owner = lightBox.getOwner();
        REPOSITORY.putIfAbsent(owner, new HashSet<>());
        REPOSITORY.get(owner).add(lightBox);
    }

    @Override
    public Collection<LightBox> getFor(Client client) {
        Collection<LightBox> temporaryStorage = new HashSet<>();

        if (REPOSITORY.containsKey(client))
            temporaryStorage.addAll(REPOSITORY.get(client));
        else
            throw new IllegalArgumentException("There are no LightBoxes stored for this client.");

        return temporaryStorage;
    }

    @Override
    public Collection<String> getLightBoxNames(Client client) {
        Collection<String> lightBoxNames = new LinkedList<>();
        Collection<LightBox> lightBoxes = REPOSITORY.get(client);
        if(lightBoxes != null)
            for(LightBox lb : lightBoxes)
                lightBoxNames.add(lb.getName());
        return lightBoxNames;
    }

    @Override
    public LightBox findLightBox(Client client, String lightBoxName) {
        Collection<LightBox> lightBoxes = REPOSITORY.get(client);
        if(lightBoxes != null)
            for(LightBox lb : lightBoxes)
                if(lb.getName().equals(lightBoxName))
                    return lb;
        return null;
    }
}


    /* Wersja 1
       public void put(LightBox lightBox) {
           Client client = lightBox.getOwner();
           Collection<LightBox> lightBoxPerClient = memoryLightBoxRepository.get(client);
           if (lightBoxPerClient == null){
                 memoryLightBoxRepository.put(client, new HashSet<>());
               lightBoxPerClient = memoryLightBoxRepository.get(client);
           }
           lightBoxPerClient.add(lightBox);
       }


/* Wersja 2
       @Override
       public void put(LightBox lightBox) {
           Client client = lightBox.getOwner();
           Collection<LightBox> lightBoxPerClient = memoryLightBoxRepository.get(client);
           if (!(lightBoxPerClient == null)) 		//można krócej, zamiast 2 linijek:
               lightBoxPerClient.add(lightBox);
           else {
           memoryLightBoxRepository.put(client, new HashSet<>());//memoryLightBoxRepository.putIfAbsent(client, new HashSet<>());
           memoryLightBoxRepository.get(client).add(lightBox);
           }
       }

      /* Wersja 3
       public void put(LightBox lightBox) {
           Client client = lightBox.getOwner();
           Collection<LightBox> lightBoxPerClient = memoryLightBoxRepository.get(client);
           if (lightBoxPerClient == null) 		//można krócej, zamiast 2 linijek:
                 memoryLightBoxRepository.put(client, new HashSet<>());//memoryLightBoxRepository.putIfAbsent(client, new HashSet<>());
           lightBoxPerClient.add(lightBox);
       }
    */
