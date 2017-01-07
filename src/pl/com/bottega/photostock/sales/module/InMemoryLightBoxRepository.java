package pl.com.bottega.photostock.sales.module;

import pl.com.bottega.photostock.sales.module.Client;
import pl.com.bottega.photostock.sales.module.LightBox;
import pl.com.bottega.photostock.sales.module.LightBoxRepository;

import java.util.*;

/**
 * Created by macie on 13.12.2016.
 */
public class InMemoryLightBoxRepository implements LightBoxRepository {

    Map<Client, Collection<LightBox>> memoryLightBoxRepository = new HashMap<>();

    public void put(LightBox lightBox) {
        Client client = lightBox.getOwner();
        Collection<LightBox> lightBoxPerClient = memoryLightBoxRepository.get(client);
        if (lightBoxPerClient == null) {
            lightBoxPerClient = new HashSet<>();//tworzę i mam do niego referencję
            memoryLightBoxRepository.put(client, lightBoxPerClient);             //lightBoxPerClient = memoryLightBoxRepository.get(client);//nie musze pobierac z mapy bo mam do niego referencję
        }
        lightBoxPerClient.add(lightBox);//uzywam referencji nie wnikajac skąd sie wziela - swieżo stworzona czy pobrana z dawien dawna
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
    @Override
    public Collection<LightBox> getFor(Client client) {
        return memoryLightBoxRepository.get(client);
    }
}
