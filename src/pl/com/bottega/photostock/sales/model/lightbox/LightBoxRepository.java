package pl.com.bottega.photostock.sales.model.lightbox;

import pl.com.bottega.photostock.sales.model.client.Client;

import java.util.Collection;

/**
 * Created by macie on 13.12.2016.
 */
public interface LightBoxRepository {


    void put(LightBox lightBox);

    Collection<LightBox> getFor(Client client);

    LightBox findLightBox(Client client, String lightBoxName);

    Collection<String> getLightBoxNames(Client client);

}
