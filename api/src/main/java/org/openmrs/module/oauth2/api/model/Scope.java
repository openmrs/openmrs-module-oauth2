package org.openmrs.module.oauth2.api.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

import org.openmrs.module.oauth2.Client;

/**
 * Created by OPSKMC on 8/19/15.
 */
@Entity
@Table(name = "oauth2_client_scopes")
public class Scope implements Parametrized {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @ManyToOne
    @JoinColumn(name = "client_id",  nullable = false, updatable = false, insertable = true)
    private Client client;

    @Basic
    @Column(name = "scope")
    private String scope;

    public Scope() {
    }

    public Scope(String scope) {

        this.scope = scope;
    }

    public Scope(Client client, String scope) {
        this.client = client;
        this.scope = scope;
    }

    public static List<Scope> getAllSupportedScopes(){
        Scope readScope = new Scope("read");
        Scope writeScope = new Scope("write");
        Scope launchScope = new Scope("launch");
        Scope patientReadScope = new Scope("patient/*.read");
        Scope patientWriteScope = new Scope("patient/*.write");
        Scope patientReadWriteScope = new Scope("patient/*.*");
        Scope profileScope = new Scope("profile");
        Scope openidScope = new Scope("openid");
        List<Scope> scopeList = new ArrayList<Scope>();
        scopeList.add(readScope);
        scopeList.add(writeScope);
        scopeList.add(launchScope);
        scopeList.add(patientReadScope);
        scopeList.add(patientWriteScope);
        scopeList.add(patientReadWriteScope);
        scopeList.add(profileScope);
        scopeList.add(openidScope);
        return scopeList;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public String getParameter() {
        return scope;
    }

    @Override
    public void setParameter(String parameter) {
        this.scope = parameter;
    }

    @Override
    public String toString() {
        return scope;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj.toString().equals(scope))
            return true;
        else
            return false;
    }
}
