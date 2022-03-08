/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgbeans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.hibernate.Session;
import pojos.City;
import pojos.Country;

/**
 *
 * @author Gabika
 */
@ManagedBean
@SessionScoped
public class Varosok {

    private List<City> varosokLista;
    private List<Country> orszagokLista;
    private String kivalasztottOrszagCode;
    private Country kivalasztottOrszag;
    private Map<String, Country> orszagMap;
    private City kivalasztottVaros;

    public Varosok() {
        Session session = hibernate.HibernateUtil.getSessionFactory().openSession();
        orszagokLista = session.createQuery("FROM Country").list();
        //varosokLista = session.createQuery("FROM City").list();
        session.close();

        orszagMap = new HashMap<>();
        for (Country c : orszagokLista) {
            orszagMap.put(c.getCode(), c);
        }
    }

    public void kivalaszt() {
        kivalasztottOrszag = orszagMap.get(kivalasztottOrszagCode);
        varosokLista = new ArrayList<>(kivalasztottOrszag.getCities());
    }

    public String modosit(City kivalasztott) {
        kivalasztottVaros = kivalasztott;

        return "szerkeszt";
    }

    public String varosMent() {
        boolean uj = false;
        if (kivalasztottVaros.getId() == null) {
            uj = true;
        }

        Session session = hibernate.HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.saveOrUpdate(kivalasztottVaros);
        session.getTransaction().commit();
        session.close();

        if (uj) {
            varosokLista.add(kivalasztottVaros);
            kivalasztottOrszag.getCities().add(kivalasztottVaros);
        }

        return "index";
    }

    public String ujVaros() {
        kivalasztottVaros = new City();
        kivalasztottVaros.setCountry(kivalasztottOrszag);

        return "szerkeszt";
    }

    public void torol(City kivalasztott) {
        Session session = hibernate.HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.delete(kivalasztott);
        session.getTransaction().commit();
        session.close();

        varosokLista.remove(kivalasztott);
        kivalasztottOrszag.getCities().remove(kivalasztott);

    }

    public List<City> getVarosokLista() {
        return varosokLista;
    }

    public void setVarosokLista(List<City> varosokLista) {
        this.varosokLista = varosokLista;
    }

    public List<Country> getOrszagokLista() {
        return orszagokLista;
    }

    public void setOrszagokLista(List<Country> orszagokLista) {
        this.orszagokLista = orszagokLista;
    }

    public String getKivalasztottOrszagCode() {
        return kivalasztottOrszagCode;
    }

    public void setKivalasztottOrszagCode(String kivalasztottOrszagCode) {
        this.kivalasztottOrszagCode = kivalasztottOrszagCode;
    }

    public Country getKivalasztottOrszag() {
        return kivalasztottOrszag;
    }

    public void setKivalasztottOrszag(Country kivalasztottOrszag) {
        this.kivalasztottOrszag = kivalasztottOrszag;
    }

    public Map<String, Country> getOrszagMap() {
        return orszagMap;
    }

    public void setOrszagMap(Map<String, Country> orszagMap) {
        this.orszagMap = orszagMap;
    }

    public City getKivalasztottVaros() {
        return kivalasztottVaros;
    }

    public void setKivalasztottVaros(City kivalasztottVaros) {
        this.kivalasztottVaros = kivalasztottVaros;
    }

}
