package pl.com.bottega.photostock.sales.module;

import pl.com.bottega.photostock.sales.module.Client;
import pl.com.bottega.photostock.sales.module.LightBox;

import java.util.Collection;

/**
 * Created by macie on 13.12.2016.
 */
public interface LightBoxRepository {

    void put(LightBox lightBox);

    Collection<LightBox> getFor(Client client);

}
